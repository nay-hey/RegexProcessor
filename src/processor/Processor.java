package processor;

import java.util.*;

public class Processor {
    private final List<Transition> transitionTable;
    private String currentState;
    private final HashMap<String, Integer> stateAccessCount;
    private final HashMap<String, Integer> edgeAccessCount;
    private final List<String> stateVisitSequence;
    private final HashMap<String, Integer> addressMap;
    private final List<Integer> addressAccessSequence; 
    private final CacheSimulator cacheSimulator;

    public Processor(List<Transition> transitionTable) {
        this.transitionTable = transitionTable;

        this.currentState = transitionTable.stream()
            .filter(transition -> transition.state.startsWith("I"))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No initial state found"))
            .state;

        this.stateAccessCount = new HashMap<>();
        this.edgeAccessCount = new HashMap<>();
        this.stateVisitSequence = new ArrayList<>(); 
        this.addressMap = new HashMap<>();
        this.addressAccessSequence = new ArrayList<>();
        this.cacheSimulator = new CacheSimulator(4);

        int addressCounter = 1;
        for (Transition transition : transitionTable) {
            stateAccessCount.put(transition.state, 0);
            addressMap.put(transition.state, addressCounter++); 
            for (SymbolTransition st : transition.symbolTransitions) {
                String edgeKey = transition.state + "," + st.symbol + "->" + st.nextState;
                addressMap.put(edgeKey, addressCounter++); 
            }
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

            stateAccessCount.put(currentState, stateAccessCount.get(currentState) + 1);
            stateVisitSequence.add(currentState); 
            int stateAddress = addressMap.get(currentState);
            addressAccessSequence.add(stateAddress);

            cacheSimulator.access(stateAddress);

            if (currentTransition != null) {
                SymbolTransition nextTransition = currentTransition.symbolTransitions.stream()
                    .filter(st -> st.symbol == ch)
                    .findFirst()
                    .orElse(null);

                if (nextTransition != null) {
                    String edgeKey = currentState + "," + ch + "->" + nextTransition.nextState;
                    edgeAccessCount.put(edgeKey, edgeAccessCount.getOrDefault(edgeKey, 0) + 1);
                    int edgeAddress = addressMap.get(edgeKey);
                    addressAccessSequence.add(edgeAddress);

                    cacheSimulator.access(edgeAddress);

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

    public String cacheLocalityAnalysis() {
        return cacheSimulator.getCacheStats();
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

    private class CacheSimulator {
        private final int cacheSize;
        private final LinkedHashSet<Integer> cache;
        private int hits, misses;

        public CacheSimulator(int cacheSize) {
            this.cacheSize = cacheSize;
            this.cache = new LinkedHashSet<>();
            this.hits = 0;
            this.misses = 0;
        }

        public void access(int address) {
            if (cache.contains(address)) {
                hits++;
                cache.remove(address);
                cache.add(address);
                System.out.println("Cache Hit: Address " + address);
            } else {
                misses++;
                System.out.println("Cache Miss: Address " + address);
                if (cache.size() >= cacheSize) {
                    Iterator<Integer> it = cache.iterator();
                    if (it.hasNext()) {
                        System.out.println("Evicting Address: " + it.next());
                        it.remove();
                    }
                }
                cache.add(address);
            }
        }

        public String getCacheStats() {
            return "Cache Hits: " + hits + ", Cache Misses: " + misses + ", Hit Ratio: " +
                   (hits + misses == 0 ? 0 : (double) hits / (hits + misses));
        }
    }

    public void printAddressMap() {
        System.out.println("Address Map:");
        for (Map.Entry<String, Integer> entry : addressMap.entrySet()) {
            System.out.println("State/Edge: " + entry.getKey() + " - Address: " + entry.getValue());
        }
    }
    public void printAddressAccessSequence() {
        System.out.println("Address Access Sequence: " + addressAccessSequence);
    }
    
    
}
