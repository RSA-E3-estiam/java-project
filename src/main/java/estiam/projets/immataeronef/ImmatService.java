package estiam.projets.immataeronef;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.exceptions.CsvException;

@Service
public class ImmatService {

	private final ImmatCSVReader immatCSVReader;
	private final PlanespottersAPI planespottersAPI;  // Déclaration du param dans le constructeur
	public static final String UNKNOWN = "unknown";
	
	public ImmatService(@Autowired ImmatCSVReader immatCSVReader, @Autowired AppConf appConf, @Autowired PlanespottersAPI planespottersAPI) throws CsvException, IOException {
		this.immatCSVReader = immatCSVReader;
		this.planespottersAPI = new PlanespottersAPI(); // Initialisation du param dans le constructeur
		immatCSVReader.importFile(new File(appConf.getFilename()));
	}

	public Optional<AeronefDTO> getAeronefFromImmat(String immat) {
		var entry = immatCSVReader.getEntryByImmat(immat);
		if (entry.isEmpty()) {
			return Optional.empty();
		}
		
		var constructeur = entry.getOrDefault("CONSTRUCTEUR", UNKNOWN);
		var modele = entry.getOrDefault("MODELE", UNKNOWN);
		var aerodromeAttache = entry.getOrDefault("AERODROME_ATTACHE", UNKNOWN);
		
		//------------------------------------------------------------------------------
		
        var planespottersResult = planespottersAPI.getPlane(immat); //Récupération des infos via la fonction getPlane de PlanespottersAPI
        
        String photo_Url = null; 
        String photo_Link = null; 
        String photographe = null;
        
        if (planespottersResult != null && !planespottersResult.photos().isEmpty()) {
            var photo = planespottersResult.photos().get(0);
            photo_Url = photo.thumbnail_large().src();
            photo_Link = photo.link();
            photographe = photo.photographer();
        }

        var aeronefDTO = new AeronefDTO(immat, constructeur, modele, aerodromeAttache, photo_Url, photo_Link, photographe);

        return Optional.of(aeronefDTO);
	}
	
}
