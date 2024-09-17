package processor;

import java.util.Map;
import java.util.Set;

public class Processor {
    private final Map<String, Map<Integer, String>> transitionTable;
    private final Set<String> finalStates;
    private String currentState;

    public Processor(Map<String, Map<Integer, String>> transitionTable, Set<String> finalStates) {
        this.transitionTable = transitionTable;
        this.finalStates = finalStates;
        // Initialize currentState with an initial state starting with "I"
        this.currentState = transitionTable.keySet()
                                           .stream()
                                           .filter(s -> s.startsWith("I"))
                                           .findFirst()
                                           .orElse(null);

        if (this.currentState == null) {
            throw new IllegalStateException("No initial state found starting with 'I'.");
        }
    }

    public boolean processInput(String input) {
        String[] symbols = input.split(","); // Split by the delimiter (comma)
        
        for (int i = 0; i < input.length(); i++) {
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
        // Check if the final state starts with 'F'
        if (currentState != null && finalStates.stream().anyMatch(f -> f.startsWith("F") && f.equals(currentState))) {
            System.out.println("Accepted: Final state reached.");
            return true;
        } else {
            System.out.println("Rejected: Final state not reached.");
            return false;
        }
    }
}
