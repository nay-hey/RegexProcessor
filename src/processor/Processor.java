package processor;

import java.util.HashMap;
import java.util.Map;

public class Processor {
    private final Map<String, Map<Integer, String>> transitionTable;
    private String currentState;
    private final Map<String, Integer> stateAccessCount; // Track state access frequency

    public Processor(Map<String, Map<Integer, String>> transitionTable) {
        this.transitionTable = transitionTable;
        this.currentState = transitionTable.keySet().stream()
            .filter(state -> state.startsWith("I"))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No initial state found"));
        
        // Initialize state access count map
        this.stateAccessCount = new HashMap<>();
        for (String state : transitionTable.keySet()) {
            stateAccessCount.put(state, 0);
        }
    }

    public boolean processInput(String input) {
        String[] symbols = input.split(","); 
        
        for (int i = 0; i < symbols.length; i++) {
            int ch = Integer.parseInt(symbols[i]); 
            Map<Integer, String> transitions = transitionTable.get(currentState);
    
            stateAccessCount.put(currentState, stateAccessCount.get(currentState) + 1);
            
            if (transitions != null && transitions.containsKey(ch)) {
                currentState = transitions.get(ch);
                System.out.println("Current state after processing '" + ch + "': " + currentState);
            } else {
                System.out.println("Transition not found for state '" + currentState + "' and character '" + ch + "'");
                return false;
            }
            
            if (i == symbols.length - 1) {
                stateAccessCount.put(currentState, stateAccessCount.get(currentState) + 1);
            }
        }
        
        boolean isAccepted = currentState.startsWith("F");
        return isAccepted;
    }
    
    public Map<String, Integer> getStateAccessCounts() {
        return stateAccessCount;
    }
}
