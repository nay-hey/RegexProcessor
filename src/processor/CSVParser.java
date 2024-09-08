package processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVParser {

    public static Map<String, Map<Character, String>> parseTransitionTable(String csvFilePath) {
        Map<String, Map<Character, String>> transitionTable = new HashMap<>();
        String[] headers;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line = reader.readLine(); // Read header line
            if (line != null) {
                headers = line.split(",");
            } else {
                return transitionTable;
            }

            // Convert headers to a, b, c, ...
            Map<String, Character> headerToChar = new HashMap<>();
            for (int i = 1; i < headers.length; i++) {
                headerToChar.put(headers[i].trim(), (char) ('a' + i - 1));
            }

            // Process each subsequent line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == headers.length) {
                    String state = parts[0].trim();
                    Map<Character, String> transitions = new HashMap<>();

                    for (int i = 1; i < parts.length; i++) {
                        char symbol = headerToChar.get(headers[i].trim());
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
