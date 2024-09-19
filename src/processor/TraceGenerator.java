package processor;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class TraceGenerator {
    private final Map<String, Map<Integer, String>> transitionTable;
    private final Set<String> finalStates;

    public TraceGenerator(Map<String, Map<Integer, String>> transitionTable, Set<String> finalStates) {
        this.transitionTable = transitionTable;
        this.finalStates = finalStates;
    }

    public String generateTrace(int length) {
        Random random = new Random();
        StringBuilder trace = new StringBuilder(length);
        String currentState = "I1";

        for (int i = 0; i < length; i++) {
            Map<Integer, String> transitions = transitionTable.get(currentState);
            if (transitions == null || transitions.isEmpty()) {
                break; 
            }

            Integer[] symbols = transitions.keySet().toArray(new Integer[0]);
            String nextState = null;
            int chosenSymbol = -1;

            if (i == length - 1) {
                // We are at the last step (n-th transition), must transition to a final state if possible
                for (int symbol : symbols) {
                    if (finalStates.contains(transitions.get(symbol))) {
                        chosenSymbol = symbol;
                        nextState = transitions.get(symbol);
                        break;
                    }
                }

                // If no final state transition found, fall back to a random transition
                if (nextState == null) {
                    chosenSymbol = symbols[random.nextInt(symbols.length)];
                    nextState = transitions.get(chosenSymbol);
                }
            } else {
                // For the first (n-1) transitions, avoid final states
                do {
                    chosenSymbol = symbols[random.nextInt(symbols.length)];
                    nextState = transitions.get(chosenSymbol);
                } while (finalStates.contains(nextState));  // Avoid final state before the last step
            }

            trace.append(chosenSymbol).append(",");
            currentState = nextState;

        }

        // Remove the trailing delimiter
        if (trace.length() > 0) {
            trace.setLength(trace.length() - 1); 
        }

        // Check if the final state is reached after n transitions
        if (finalStates.contains(currentState)) {
            System.out.println("String accepted, reached final state.");
        } else {
            System.out.println("String rejected, did not reach final state.");
        }

        return trace.toString();
    }
}
