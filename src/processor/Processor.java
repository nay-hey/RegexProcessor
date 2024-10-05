package processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Processor {
    private final List<Transition> transitionTable;
    private String currentState;
    private final HashMap<String, Integer> stateAccessCount;
    private final HashMap<String, Integer> edgeAccessCount;
    private final List<String> stateVisitSequence; 
    
    public Processor(List<Transition> transitionTable) {
        this.transitionTable = transitionTable;

        this.currentState = transitionTable.stream()
            .filter(transition -> transition.state.startsWith("I"))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No initial state found"))
            .state;

        this.stateAccessCount = new HashMap<>();
        for (Transition transition : transitionTable) {
            stateAccessCount.put(transition.state, 0);
        }

        this.edgeAccessCount = new HashMap<>();
        this.stateVisitSequence = new ArrayList<>(); 
    }

    public boolean processInput(String input) {
        String[] symbols = input.split(",");
        
        for (int i = 0; i < symbols.length; i++) {
            int ch = Integer.parseInt(symbols[i]);
            Transition currentTransition = transitionTable.stream()
                .filter(t -> t.state.equals(currentState))
                .findFirst()
                .orElse(null);

            // Track state access and visit sequence
            stateAccessCount.put(currentState, stateAccessCount.get(currentState) + 1);
            stateVisitSequence.add(currentState); // Add current state to the visit sequence

            if (currentTransition != null) {
                SymbolTransition nextTransition = currentTransition.symbolTransitions.stream()
                    .filter(st -> st.symbol == ch)
                    .findFirst()
                    .orElse(null);

                if (nextTransition != null) {
                    String edgeKey = currentState + "," + ch + "->" + nextTransition.nextState;
                    edgeAccessCount.put(edgeKey, edgeAccessCount.getOrDefault(edgeKey, 0) + 1);

                    currentState = nextTransition.nextState;
                    System.out.println("Current state after processing '" + ch + "': " + currentState);
                } else {
                    System.out.println("Transition not found for state '" + currentState + "' and character '" + ch + "'");
                    return false;
                }
            }

            if (i == symbols.length - 1) {
                stateAccessCount.put(currentState, stateAccessCount.get(currentState) + 1);
            }
        }

        boolean isAccepted = currentState.startsWith("F");
        return isAccepted;
    }

    public HashMap<String, Integer> getStateAccessCounts() {
        return stateAccessCount;
    }

    public HashMap<String, Integer> getEdgeAccessCounts() {
        return edgeAccessCount;
    }

    public List<String> getStateVisitSequence() {
        return stateVisitSequence;
    }

    public String analyzeTemporalLocality() {
        HashMap<String, List<Integer>> revisitDistances = new HashMap<>();
        HashMap<String, Integer> lastVisitIndex = new HashMap<>();

        for (int i = 0; i < stateVisitSequence.size(); i++) {
            String state = stateVisitSequence.get(i);

            if (lastVisitIndex.containsKey(state)) {
                int lastIndex = lastVisitIndex.get(state);
                int distance = i - lastIndex;
                
                if (!revisitDistances.containsKey(state)) {
                    revisitDistances.put(state, new ArrayList<>());
                }
                revisitDistances.get(state).add(distance);
            }

            lastVisitIndex.put(state, i);
        }

        StringBuilder analysisResult = new StringBuilder();
        for (String state : revisitDistances.keySet()) {
            List<Integer> distances = revisitDistances.get(state);

            int totalDistance = distances.stream().mapToInt(Integer::intValue).sum();
            double averageDistance = (double) totalDistance / distances.size();

            int maxDistance = distances.stream().mapToInt(Integer::intValue).max().orElse(0);
            int minDistance = distances.stream().mapToInt(Integer::intValue).min().orElse(0);

            analysisResult.append("State: ").append(state)
                          .append(", Revisits: ").append(distances.size())
                          .append(", Avg Distance: ").append(String.format("%.2f", averageDistance))
                          .append(", Min Distance: ").append(minDistance)
                          .append(", Max Distance: ").append(maxDistance)
                          .append("\n");
        }

        return analysisResult.toString();
    }
}
