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
        StringBuilder trace = new StringBuilder();
        String currentState = transitionTable.keySet()
                                             .stream()
                                             .filter(s -> s.startsWith("I"))
                                             .findFirst()
                                             .orElse(null);

        if (currentState == null) {
            System.err.println("No initial state found starting with 'I'.");
            return "";
        }

        for (int i = 0; i < length; i++) {
            Map<Integer, String> transitions = transitionTable.get(currentState);
            if (transitions == null || transitions.isEmpty()) {
                break;  
            }

            Integer[] symbols = transitions.keySet().toArray(new Integer[0]);
            int randomSymbol = symbols[random.nextInt(symbols.length)];
            trace.append(randomSymbol);
            currentState = transitions.get(randomSymbol);
        }

        if (finalStates.contains(currentState)) {
            System.out.println("String accepted, reached final state.");
        } else {
            System.out.println("String rejected, did not reach final state.");
        }
        if (trace.length() > 0) {
            trace.setLength(trace.length() - 1); // Remove last comma
        }
        return trace.toString();
    }
}
