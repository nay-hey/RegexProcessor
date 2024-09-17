package processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVParser {

    public static Map<String, Map<Integer, String>> parseTransitionTable(String csvFilePath) {
        Map<String, Map<Integer, String>> transitionTable = new HashMap<>();
        String[] headers;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line = reader.readLine(); 
            if (line != null) {
                headers = line.split(","); //delimiter is a comma
            } else {
                return transitionTable;
            }

            // Convert headers to positive integers starting from 1
            Map<String, Integer> headerToInt = new HashMap<>();
            for (int i = 1; i < headers.length; i++) {
                headerToInt.put(headers[i].trim(), i);
            }

            // Process each subsequent line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == headers.length) {
                    String state = parts[0].trim();
                    Map<Integer, String> transitions = new HashMap<>();

                    for (int i = 1; i < parts.length; i++) {
                        int symbol = headerToInt.get(headers[i].trim());
                        transitions.put(symbol, parts[i].trim());
                    }

                    transitionTable.put(state, transitions);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transitionTable;
    }
}
