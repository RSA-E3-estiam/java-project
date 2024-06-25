package estiam.projets.immataeronef;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AeronefDTO {

	private String immatriculation; 
	private String constructeur;
	private String modele;
	private String aerodromeAttache;
	private String url; // URL de la photo
	private String link; // Lien de la photo
	private String photographer; // Nom du photographe

}
