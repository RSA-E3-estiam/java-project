package estiam.projets.immataeronef;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class ImmatCSVReader {

	private final Map<String, Map<String, String>> entries;

	public ImmatCSVReader() {
		entries = new HashMap<>();
	}

	public void importFile(File export) throws IOException, CsvException {
		FileReader filereader = null;
		try {
			filereader = new FileReader(export);

			final var parser = new CSVParserBuilder()
					.withSeparator(';')
					.withQuoteChar('`');

			final var csvReader = new CSVReaderBuilder(filereader)
					.withCSVParser(parser.build())
					.build();

			String[] nextRecord;
			var header = new ArrayList<String>(); 
			
			while ((nextRecord = csvReader.readNext()) != null) {
				if (header.isEmpty()) {
					header.addAll(asList(nextRecord));
					continue;
				}
				
				var mapRow = new HashMap<String, String>();
				for (int pos = 0; pos < nextRecord.length; pos++) {
					mapRow.put(header.get(pos), nextRecord[pos]);
				}
				
				entries.put(nextRecord[0], mapRow);
			}
		} finally {
			if (filereader != null) {
				filereader.close();
			}
		}
	}

	public Map<String, String> getEntryByImmat(String immat) {
		return unmodifiableMap(entries.getOrDefault(requireNonNull(immat), Map.of()));
	}

}
