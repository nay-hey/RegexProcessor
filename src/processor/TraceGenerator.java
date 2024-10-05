package processor;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class TraceGenerator {
    private final List<Transition> transitionTable;
    private final Set<String> finalStates;
    private String initialState;

    public TraceGenerator(List<Transition> transitionTable, Set<String> finalStates) {
        this.transitionTable = transitionTable;
        this.finalStates = finalStates;
        this.initialState = transitionTable.stream()
            .filter(transition -> transition.state.startsWith("I"))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No initial state found"))
            .state;
    }

    public String generateTrace(int length) {
        Random random = new Random();
        StringBuilder trace = new StringBuilder(length);
        String[] currentState = {initialState}; 
        int[] chosenSymbol = {-1}; 

        for (int i = 0; i < length; i++) {
            Transition currentTransition = transitionTable.stream()
                .filter(t -> t.state.equals(currentState[0])) 
                .findFirst()
                .orElse(null);

            if (currentTransition == null || currentTransition.symbolTransitions.isEmpty()) {
                break; 
            }

            List<SymbolTransition> transitions = currentTransition.symbolTransitions;
            String nextState = null;

            if (i == length - 1) {
                for (SymbolTransition symbolTransition : transitions) {
                    if (finalStates.contains(symbolTransition.nextState)) {
                        chosenSymbol[0] = symbolTransition.symbol; 
                        nextState = symbolTransition.nextState;
                        break;
                    }
                }

                if (nextState == null) {
                    chosenSymbol[0] = transitions.get(random.nextInt(transitions.size())).symbol;
                    nextState = transitions.stream()
                        .filter(st -> st.symbol == chosenSymbol[0]) 
                        .findFirst()
                        .orElse(null).nextState;
                }
            } else {
                do {
                    chosenSymbol[0] = transitions.get(random.nextInt(transitions.size())).symbol; // Update chosen symbol
                    nextState = transitions.stream()
                        .filter(st -> st.symbol == chosenSymbol[0]) 
                        .findFirst()
                        .orElse(null).nextState;
                } while (finalStates.contains(nextState));
            }

            if (chosenSymbol[0] != -1) {
                trace.append(chosenSymbol[0]).append(",");
            }
            currentState[0] = nextState; 
        }

        if (trace.length() > 0) {
            trace.setLength(trace.length() - 1); 
        }

        return trace.toString();
    }
}
