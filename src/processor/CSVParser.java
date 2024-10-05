package processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CSVParser {

    public static List<Transition> parseTransitionTable(String csvFilePath) {
        List<Transition> transitionTable = new ArrayList<>();
        String[] headers;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line = reader.readLine(); // Read headers
            if (line != null) {
                headers = line.split(","); // delimiter is a comma
            } else {
                return transitionTable;
            }

            HashMap<String, Integer> headerToInt = new HashMap<>();
            for (int i = 1; i < headers.length; i++) {
                headerToInt.put(headers[i].trim(), i);
            }

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == headers.length) {
                    String state = parts[0].trim();
                    Transition transition = new Transition(state);

                    for (int i = 1; i < parts.length; i++) {
                        int symbol = headerToInt.get(headers[i].trim());
                        String nextState = parts[i].trim();
                        transition.symbolTransitions.add(new SymbolTransition(symbol, nextState));
                    }
                    transitionTable.add(transition);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transitionTable;
    }
}
