import java.util.*;

public class GraphAnalysis {

    // Step one calculate the average clustering coefficient over all nodes in the graph
    public static Double averageClusteringCoefficient(Map<String, Set<String>> map) {
        Double aggregate = 0.0;
        for (String member : map.keySet()) {
            Set<Pair<String, String>> edges = new HashSet<>();
            Set<String> neighbors = map.get(member);
            int maxEdges = neighbors.size() * (neighbors.size() - 1) / 2;

            for (String neighbor : neighbors) {
                for (String secondNeighbor : map.get(neighbor)) {
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

            double clusteringCoefficient = ((double) edges.size()) / (double) maxEdges;
            aggregate += clusteringCoefficient;

        }

        Double average =  aggregate / (map.keySet().size());
        return average;
    }



    // Step two calculate the clustering coefficient for members in a seniority range
    public static Map<Integer, Double> similarClusteringCoefficient(Map<Integer, List<String>> seniorityMap,
                                                                    Map<String, Set<String>> map) {
        Map<Integer, Double> values = new HashMap();

        for (Integer seniority : seniorityMap.keySet()) {
            Double aggregate = 0.0;
            List<String> similar = seniorityMap.get(seniority);

            if (similar.size() == 1 || similar.size() == 2) {
                values.put(seniority, 0.0);
            } else {
                for (String member : similar) {

                    Set<Pair<String, String>> edges = new HashSet<>();
                    Set<String> neighbors = map.get(member);
                    Set<String> similarNeighbors = new HashSet<>();
                    for (String neighbor : neighbors) {
                        if (similar.contains(neighbor)) {
                            similarNeighbors.add(neighbor);
                        }
                    }
                    Integer maxEdges = similarNeighbors.size() * (similarNeighbors.size() - 1) / 2;

                    for (String neighbor : similarNeighbors) {
                        for (String secondNeighbor : map.get(neighbor)) {
                            if (similarNeighbors.contains(secondNeighbor) && !secondNeighbor.equals(neighbor)
                                    && !secondNeighbor.equals(member)) {
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
                        aggregate += clusteringCoefficient;
                    }
                }
                Double averageSimilar = aggregate / similar.size();
                values.put(seniority, averageSimilar);
            }
        }
        return values;
    }

    //by generation
    public static Map<String, Double> generationClusteringCoefficient(Map<Integer, List<String>> seniorityMap,
                                                                      Map<String, Set<String>> map) {
        Map<String, Double> values = new HashMap();
        values.put("Milennials (26 – 41)", generationCalculations(26, 41, seniorityMap, map));
        values.put("GenX (42 – 57)", generationCalculations(42, 57, seniorityMap, map));
        values.put("Boomers II (58 – 67)", generationCalculations(58, 67, seniorityMap, map));
        values.put("Boomers I (68 – 76)", generationCalculations(68, 76, seniorityMap, map));
        values.put("Post War (77 – 94)", generationCalculations(77, 94, seniorityMap, map));
        return values;
    }



    private static Double generationCalculations(int lowerBound, int upperBound, Map<Integer, List<String>> seniorityMap,
                                                 Map<String, Set<String>> map) {
        List<String> similar = new LinkedList<>();
        Double aggregate = 0.0;
        for (Integer seniority : seniorityMap.keySet()) {
            if (seniority >= lowerBound && seniority <= upperBound) {
                for (String mem : seniorityMap.get(seniority)) {
                    similar.add(mem);
                }
            }
        }
        if (similar.size() <= 2) {
            return 0.0;
        }
        for (String member : similar) {

            Set<Pair<String, String>> edges = new HashSet<>();
            Set<String> neighbors = map.get(member);
            Set<String> similarNeighbors = new HashSet<>();
            for (String neighbor : neighbors) {
                if (similar.contains(neighbor)) {
                    similarNeighbors.add(neighbor);
                }
            }
            Integer maxEdges = similarNeighbors.size() * (similarNeighbors.size() - 1) / 2;

            for (String neighbor : similarNeighbors) {
                for (String secondNeighbor : map.get(neighbor)) {
                    if (similarNeighbors.contains(secondNeighbor) && !secondNeighbor.equals(neighbor)
                            && !secondNeighbor.equals(member)) {
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
                aggregate += clusteringCoefficient;
            }
        }
        Double averageSimilar = aggregate / similar.size();
        return averageSimilar;
    }


    public static Double averageSimilarSeniorityClusteringCoefficient(Map<Integer, Double> map) {
        Double sum = 0.0;
        for (Integer key : map.keySet()) {
            sum += map.get(key);
        }
        return sum / map.keySet().size();
    }

    // Step three find average clustering coefficient over multiple iterations of a subset of members randomly chosen
    // all from different committees
    public static Double averageDistinctSeniorityClusteringCoefficient(Map<Integer, List<String>> seniorityMap,
                                                                       Map<String, Set<String>> map, int iterations) {
        Set<String> distinct = new HashSet<>();
        Double iterationAggregate = 0.0;
        Random random = new Random();

        for (int i = 0; i < iterations; i++) {
            for (Integer seniority : seniorityMap.keySet()) {
                List<String> similar = seniorityMap.get(seniority);
                int max = similar.size();
                String potentialMember = similar.get(random.nextInt(max));
                potentialMember = similar.get(random.nextInt(max));
                distinct.add(potentialMember);
            }

            Double aggregate = 0.0;
            for (String member : distinct) {
                Set<Pair<String, String>> edges = new HashSet<>();
                Set<String> neighbors = map.get(member);
                LinkedList<String> similarNeighbors = new LinkedList<>();
                for (String neighbor : neighbors) {
                    if (distinct.contains(neighbor) && !neighbor.equals(member)) {
                        similarNeighbors.add(neighbor);
                    }
                }
                Integer maxEdges = similarNeighbors.size() * (similarNeighbors.size() - 1) / 2;

                for (String neighbor : similarNeighbors) {
                    for (String secondNeighbor : map.get(neighbor)) {
                        if (similarNeighbors.contains(secondNeighbor) && !secondNeighbor.equals(neighbor)
                                && !secondNeighbor.equals(member)) {
                            if (neighbor.compareTo(secondNeighbor) < 0) {
                                edges.add(new Pair<>(neighbor, secondNeighbor));
                            } else {
                                edges.add(new Pair<>(secondNeighbor, neighbor));
                            }
                        }
                    }
                }
                Double clusteringCoefficient = (double) edges.size() / (double) maxEdges;
                aggregate += clusteringCoefficient;
            }
            Double averageDistinct = aggregate / distinct.size();
            iterationAggregate += averageDistinct;
        }
        return iterationAggregate / (double) iterations;
    }
}
 
