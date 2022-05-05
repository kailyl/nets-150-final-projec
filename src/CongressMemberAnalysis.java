import java.util.*;

//Class to calculate the clustering coefficients of Congress members based on shared committees/subcommittees
public class CongressMemberAnalysis {

    private Map<Integer, List<String>> ageMap;
    private Map<String, Set<String>> committeeGraph;
    private Map<String, Set<String>> generationalGraphs;

    private Map<String, Double> originalCoefficients;
    private Map<String, Double> generationalCoefficients;
    private Map<String, Double> difference;


    public CongressMemberAnalysis(Map<Integer, List<String>> ageMap,
                                  Map<String, Set<String>> committeeGraph) {
        this.ageMap = ageMap;
        this.committeeGraph = committeeGraph;
        generationalGraphs = findGenerationalGraphs();

        originalCoefficients = new HashMap<>();
        generationalCoefficients = new HashMap<>();
        difference = new HashMap<>();
    }

    public Map<String, Double> getOriginalCoefficients() {
        return originalCoefficients;
    }

    public Map<String, Double> getGenerationalCoefficients() {
        return generationalCoefficients;
    }

    public Map<String, Double> getDifference() {
        return difference;
    }

    // Step one calculate the clustering coefficient over all nodes in the graph
    public void findOriginalClusteringCoefficients() {
        for (String member : committeeGraph.keySet()) {
            Set<Pair<String, String>> edges = new HashSet<>();
            Set<String> neighbors = committeeGraph.get(member);

            if (neighbors.size() == 0) {
                originalCoefficients.put(member, 0.0);
            } else {
                int maxEdges = neighbors.size() * (neighbors.size() - 1) / 2;

                for (String neighbor : neighbors) {
                    for (String secondNeighbor : committeeGraph.get(neighbor)) {
                        if (neighbors.contains(secondNeighbor) && !secondNeighbor.equals(member)
                                && !secondNeighbor.equals(neighbor)) {
                            if (neighbor.compareTo(secondNeighbor) < 0) {
                                edges.add(new Pair<>(neighbor, secondNeighbor));
                            } else {
                                edges.add(new Pair<>(secondNeighbor, neighbor));
                            }
                        }
                    }
                }
                double clusteringCoefficient = ((double) edges.size()) / maxEdges;
                originalCoefficients.put(member, clusteringCoefficient);
            }

        }
    }



    // Step two calculate the clustering coefficient for members in a generation range
    public void findGenerationalClusteringCoefficients() {
        for (String gen : generationalGraphs.keySet()) {
            Set<String> generation = generationalGraphs.get(gen);
            if (!generation.isEmpty()) {
                for (String mem : generation) {
                    Set<Pair<String, String>> edges = new HashSet<>();
                    Set<String> neighbors = committeeGraph.get(mem);
                    Set<String> similarNeighbors = new HashSet<>();
                    for (String neighbor : neighbors) {
                        if (generation.contains(neighbor)) {
                            similarNeighbors.add(neighbor);
                        }
                    }
                    if (similarNeighbors.size() == 0) {
                        generationalCoefficients.put(mem, 0.0);
                    } else {
                        Integer maxEdges = similarNeighbors.size() * (similarNeighbors.size() - 1) / 2;

                        for (String neighbor : similarNeighbors) {
                            for (String secondNeighbor : committeeGraph.get(neighbor)) {
                                if (similarNeighbors.contains(secondNeighbor) && !secondNeighbor.equals(neighbor)
                                        && !secondNeighbor.equals(mem)) {
                                    if (neighbor.compareTo(secondNeighbor) < 0) {
                                        edges.add(new Pair<>(neighbor, secondNeighbor));
                                    } else {
                                        edges.add(new Pair<>(secondNeighbor, neighbor));
                                    }
                                }
                            }
                        }
                        if (edges.size() != 0) {
                            Double clusteringCoefficient = ((double) edges.size()) /  maxEdges;
                            generationalCoefficients.put(mem, clusteringCoefficient);
                        }
                        else {
                            generationalCoefficients.put(mem, 0.0);
                        }
                    }
                }
            }
        }
    }

    //by generation
    public Map<String, Set<String>> findGenerationalGraphs() {
        Map<String, Set<String>> values = new HashMap();
        values.put("Milennials (26 – 41)", generationCalculations(26, 41));
        values.put("GenX (42 – 57)", generationCalculations(42, 57));
        values.put("Boomers II (58 – 67)", generationCalculations(58, 67));
        values.put("Boomers I (68 – 76)", generationCalculations(68, 76));
        values.put("Post War (77 – 94)", generationCalculations(77, 94));
        return values;
    }

    private Set<String> generationCalculations(int lowerBound, int upperBound) {
        Set<String> similar = new HashSet<>();
        for (Integer age : ageMap.keySet()) {
            if (age >= lowerBound && age <= upperBound) {
                similar.addAll(ageMap.get(age));
            }
        }
        return similar;
    }

    public void findDifference() {
        for (String member : originalCoefficients.keySet()) {
            difference.put(member, (generationalCoefficients.get(member)) - originalCoefficients.get(member));
        }
    }

    public Double findDifferenceAverage() {
        double count = 0.0;
        for (String member : difference.keySet()) {
            count += difference.get(member);
        }
        return count / difference.size();
    }

}