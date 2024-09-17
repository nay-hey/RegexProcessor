package processor;

import java.util.Map;

public class Processor {
    private final Map<String, Map<Integer, String>> transitionTable;
    private String currentState;

    public Processor(Map<String, Map<Integer, String>> transitionTable) {
        this.transitionTable = transitionTable;
        this.currentState = "I1";
    }

    public boolean processInput(String input) {
        String[] symbols = input.split(","); // Split by the delimiter (comma)
        
        for (String symbol : symbols) {
            int ch = Integer.parseInt(symbol); // Convert string to integer
            Map<Integer, String> transitions = transitionTable.get(currentState);

            if (transitions != null && transitions.containsKey(ch)) {
                currentState = transitions.get(ch);
                System.out.println("Current state after processing '" + ch + "': " + currentState);
            } else {
                System.out.println("Transition not found for state '" + currentState + "' and character '" + ch + "'");
                return false;
            }
        }
        return currentState.equals("F3");
    }
}
