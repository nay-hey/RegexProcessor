package processor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Processor {
    private final List<Transition> transitionTable;
    private String currentState;
    private final List<String> stateVisitSequence;
    private final HashMap<String, Integer> addressMap;
    private final List<Integer> addressAccessSequence; 

    private static final int EDGE_SIZE = 4;  // Size in bytes for each edge


    public Processor(List<Transition> transitionTable) {
        this.transitionTable = transitionTable;

        this.currentState = transitionTable.stream()
            .filter(transition -> transition.state.startsWith("I"))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No initial state found"))
            .state;

        this.stateVisitSequence = new ArrayList<>(); 
        this.addressMap = new HashMap<>();
        this.addressAccessSequence = new ArrayList<>();
        int currentAddress = 0;
        for (Transition transition : transitionTable) {
            addressMap.put(transition.state, currentAddress); 
           
            for (SymbolTransition st : transition.symbolTransitions) {
                String edgeKey = transition.state + "," + st.symbol + "->" + st.nextState;
                addressMap.put(edgeKey, currentAddress);
                currentAddress += EDGE_SIZE; 
            }
        }
    }
    public void saveAddressAccessSequenceToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Integer address : addressAccessSequence) {
                writer.write(address + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean processInput(String input) {
        String[] symbols = input.split(",");

        for (int i = 0; i < symbols.length; i++) {
            int ch = Integer.parseInt(symbols[i]);
            Transition currentTransition = transitionTable.stream()
                .filter(t -> t.state.equals(currentState))
                .findFirst()
                .orElse(null);

            
            if (currentTransition != null) {
                SymbolTransition nextTransition = currentTransition.symbolTransitions.stream()
                    .filter(st -> st.symbol == ch)
                    .findFirst()
                    .orElse(null);

                if (nextTransition != null) {
                    String edgeKey = currentState + "," + ch + "->" + nextTransition.nextState;
                    int edgeAddress = addressMap.get(edgeKey);
                    addressAccessSequence.add(edgeAddress);

                    currentState = nextTransition.nextState;
                    stateVisitSequence.add(currentState); 
                    int stateAddress = addressMap.get(currentState);
                    System.out.println(stateAddress);
                   // addressAccessSequence.add(stateAddress);
        
                    System.out.println("Current state after processing '" + ch + "': " + currentState);
                } else {
                    System.out.println("Transition not found for state '" + currentState + "' and character '" + ch + "'");
                    return false;
                }
            }

        }

        boolean isAccepted = currentState.startsWith("F");
        return isAccepted;
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

    public String analyzeSpatialLocality() {
        HashMap<Integer, Integer> addressAccessCount = new HashMap<>();

        for (Integer address : addressAccessSequence) {
            addressAccessCount.put(address, addressAccessCount.getOrDefault(address, 0) + 1);
        }

        StringBuilder analysisResult = new StringBuilder();
        analysisResult.append("Address Spatial Locality Analysis:\n");
        for (Map.Entry<Integer, Integer> entry : addressAccessCount.entrySet()) {
            analysisResult.append("Address: ").append(entry.getKey())
                          .append(", Access Count: ").append(entry.getValue()).append("\n");
        }

        return analysisResult.toString();
    }

    public void printAddressAccessSequence() {
        System.out.println("Address Access Sequence: " + addressAccessSequence);
    }
    
    
}
