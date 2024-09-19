package processor;

import java.util.Map;

public class Processor {
    private final Map<String, Map<Integer, String>> transitionTable;
    private String currentState;

    public Processor(Map<String, Map<Integer, String>> transitionTable) {
        this.transitionTable = transitionTable;
        this.currentState = transitionTable.keySet().stream()
            .filter(state -> state.startsWith("I"))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No initial state found"));
   
    }

    public boolean processInput(String input) {
        String[] symbols = input.split(","); 
        
        for (String symbol : symbols) {
            int ch = Integer.parseInt(symbol); 
            Map<Integer, String> transitions = transitionTable.get(currentState);

            if (transitions != null && transitions.containsKey(ch)) {
                currentState = transitions.get(ch);
                System.out.println("Current state after processing '" + ch + "': " + currentState);
            } else {
                System.out.println("Transition not found for state '" + currentState + "' and character '" + ch + "'");
                return false;
            }
        }
        boolean isAccepted = currentState.startsWith("F");
        return isAccepted;
    }
}
