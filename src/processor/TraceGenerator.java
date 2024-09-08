package processor;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class TraceGenerator {
    private final Map<String, Map<Character, String>> transitionTable;
    private final Set<String> finalStates;

    public TraceGenerator(Map<String, Map<Character, String>> transitionTable, Set<String> finalStates) {
        this.transitionTable = transitionTable;
        this.finalStates = finalStates;
    }

    public String generateTrace(int length) {
        Random random = new Random();
        StringBuilder trace = new StringBuilder(length);
        String currentState = "I1"; 

        for (int i = 0; i < length; i++) {
            Map<Character, String> transitions = transitionTable.get(currentState);
            if (transitions == null || transitions.isEmpty()) {
                break;  
            }

            Character[] symbols = transitions.keySet().toArray(new Character[0]);
            char randomSymbol = symbols[random.nextInt(symbols.length)];
            trace.append(randomSymbol);
            currentState = transitions.get(randomSymbol);
        }

        if (finalStates.contains(currentState)) {
            System.out.println("String accepted, reached final state.");
        } else {
            System.out.println("String rejected, did not reach final state.");
        }

        return trace.toString();
    }
}
