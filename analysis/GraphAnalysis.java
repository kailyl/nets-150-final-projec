import java.util.*;

public class GraphAnalysis {


    // Step one calculate the average clustering coefficient over all nodes in the graph
    public static Integer averageClusteringCoefficient(HashMap<String, LinkedList<String>> map) {
        Integer aggregate = 0;
        for (String member : map.keySet()) {
            Set<String[]> edges = new HashSet<>();
            LinkedList<String> neighbors = map.get(member);
            Integer maxEdges = neighbors.size() * (neighbors.size() - 1) / 2;

            for (String neighbor : neighbors) {
                for (String secondNeighbor : map.get(neighbor)) {
                    if (neighbors.contains(secondNeighbor)) {
                        String[] edge = new String[2];
                        if (neighbor.compareTo(secondNeighbor) < 0) {
                            edge[0] = neighbor;
                            edge[1] = secondNeighbor;
                        } else {
                            edge[1] = neighbor;
                            edge[0] = secondNeighbor;
                        }
                        edges.add(edge);
                    }
            }
        }
            Integer clusteringCoefficient = edges.size() / maxEdges;
            aggregate += clusteringCoefficient;

        }

        Integer average = aggregate / map.keySet().size();
        return average;
    }



    // Step two calculate the clustering coefficient for members in a seniority range
    public static Map<Integer, Integer> similarClusteringCoefficient(HashMap<Integer, LinkedList<String>> seniorityMap,
                                                   HashMap<String, LinkedList<String>> map) {
        Map<Integer, Integer> values = new HashMap();


        for (Integer seniority : seniorityMap.keySet()) {
            Integer aggregate = 0;

            LinkedList<String> similar = seniorityMap.get(seniority);

            for (String member : similar) {
                Set<String[]> edges = new HashSet<>();
                LinkedList<String> neighbors = map.get(member);
                LinkedList<String> similarNeighbors = new LinkedList<>();
                for (String neighbor : neighbors) {
                    if (similar.contains(neighbor)) {
                        similarNeighbors.add(neighbor);
                    }
                }
                Integer maxEdges = similarNeighbors.size() * (similarNeighbors.size() - 1) / 2;

                for (String neighbor : similarNeighbors) {
                    for (String secondNeighbor : map.get(neighbor)) {
                        if (similarNeighbors.contains(secondNeighbor)) {
                            String[] edge = new String[2];
                            if (neighbor.compareTo(secondNeighbor) < 0) {
                                edge[0] = neighbor;
                                edge[1] = secondNeighbor;
                            } else {
                                edge[1] = neighbor;
                                edge[0] = secondNeighbor;
                            }
                            edges.add(edge);
                        }
                    }
                }
                Integer clusteringCoefficient = edges.size() / maxEdges;
                aggregate += clusteringCoefficient;
            }
            Integer averageSimilar = aggregate / similar.size();

            values.put(seniority, averageSimilar);
        }
        return values;
    }

    public static Integer averageSimilarSeniorityClusteringCoefficient(Map<Integer, Integer> map) {
        Integer sum = 0;
        for (Integer key : map.keySet()) {
            sum += map.get(key);
        }
        return sum / map.keySet().size();
    }

    // Step three find average clustering coefficient over multiple iterations of a subset of members randomly chosen
    // all from different committees
    public static Integer averageDistinctSeniorityClusteringCoefficient(HashMap<Integer, LinkedList<String>>
                                                                                seniorityMap,
                                                                        HashMap<String, LinkedList<String>> map) {
        Set<String> distinct = new HashSet<>();

        for (Integer seniority : seniorityMap.keySet()) {
            LinkedList<String> similar = seniorityMap.get(seniority);
            int max = similar.size();
            Random random = new Random();
            String potentialMember = similar.get(random.nextInt(max));
            while (!distinct.add(potentialMember)) {
                potentialMember = similar.get(random.nextInt(max));
            }
            distinct.add(potentialMember);
        }

        Integer aggregate = 0;
        for (String member : distinct) {
            Set<String[]> edges = new HashSet<>();
            LinkedList<String> neighbors = map.get(member);
            LinkedList<String> similarNeighbors = new LinkedList<>();
            for (String neighbor : neighbors) {
                if (distinct.contains(neighbor)) {
                    similarNeighbors.add(neighbor);
                }
            }
            Integer maxEdges = similarNeighbors.size() * (similarNeighbors.size() - 1) / 2;

            for (String neighbor : similarNeighbors) {
                for (String secondNeighbor : map.get(neighbor)) {
                    if (similarNeighbors.contains(secondNeighbor)) {
                        String[] edge = new String[2];
                        if (neighbor.compareTo(secondNeighbor) < 0) {
                            edge[0] = neighbor;
                            edge[1] = secondNeighbor;
                        } else {
                            edge[1] = neighbor;
                            edge[0] = secondNeighbor;
                        }
                        edges.add(edge);
                    }
                }
            }
            Integer clusteringCoefficient = edges.size() / maxEdges;
            aggregate += clusteringCoefficient;
        }
        Integer averageDistinct = aggregate / distinct.size();

        return averageDistinct;
    }
}