package processor;

import java.util.Map;

public class Processor {
    private final Map<String, Map<Character, String>> transitionTable;
    private String currentState;

    public Processor(Map<String, Map<Character, String>> transitionTable) {
        this.transitionTable = transitionTable;
        this.currentState = "I1"; 
    }

    public boolean processInput(String input) {
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            Map<Character, String> transitions = transitionTable.get(currentState);

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
