import java.util.*;

public class RunAnalysis {
    public static void main (String[] args) {
        WikiParser parse = new WikiParser();
        parse.initHashMaps();
        Map<String, Integer> senateToDate = parse.getSenatorsToYears();
        for (String key : senateToDate.keySet()) {
            //System.out.println(key);
        }
        for (String key : senateToDate.keySet()) {
            //System.out.println(key + ": " + senateToDate.get(key));
        }
        //System.out.println(" ");
        Map<Integer, List<String>> dateToSenate = parse.getYearToSenator();
        for (Integer key : dateToSenate.keySet()) {
            System.out.println(key + ": " + dateToSenate.get(key));
        }

        CommParser parser = new CommParser();

        parser.getSenatorList();
        parser.getCommURLs();

        Map<String, Set<String>> sen = parser.getSenToCommMembs();
        for (String key : sen.keySet()) {
            System.out.println(key + ": " + sen.get(key));
        }

        /*Map<String,Set<String> > map = new HashMap<>();
        Set<String> one = new HashSet<>();
        one.add("anna");
        one.add("kaily");
        one.add("michelle");
        map.put("cindy", one);
        Set<String> two = new HashSet<>();
        two.add("anna");
        two.add("cindy");
        two.add("michelle");
        two.add("annama");
        map.put("kaily", two);
        Set<String> four = new HashSet<>();
        four.add("anna");
        four.add("cindy");
        four.add("kaily");
        map.put("michelle", four);
        Set<String> three = new HashSet<>();
        three.add("kaily");
        three.add("cindy");
        three.add("michelle");
        map.put("anna", three);
        Set<String> five = new HashSet<>();
        five.add("anna");
        five.add("kaily");
        map.put("annama", five);*/





        //System.out.println("answer" + GraphAnalysis.averageClusteringCoefficient(map));
        System.out.println(" ");
        System.out.println("Experiment One: Finding the Average Clustering Coefficient over all vertices:");
        Double averageCC = GraphAnalysis.averageClusteringCoefficient(sen);
        System.out.println(averageCC);

        System.out.println(" ");
        System.out.println("Experiment Two: Finding the Clustering Coefficients for similar seniority groups");
        System.out.println(GraphAnalysis.similarClusteringCoefficient(dateToSenate, sen));
        System.out.println(
                GraphAnalysis.averageSimilarSeniorityClusteringCoefficient(
                        GraphAnalysis.similarClusteringCoefficient(dateToSenate, sen)));
        System.out.println(" ");
        System.out.println("Experiment 2.1: Find the Clustering Coefficient per Generation");
        System.out.println(GraphAnalysis.generationClusteringCoefficient(dateToSenate, sen));


        System.out.println(" ");
        System.out.println("Experiment Three: Finding the Average Clustering Coefficients for a randomized group of " +
                "members from completely different seniority groups over 1000 iterations");
        System.out.println(GraphAnalysis.averageDistinctSeniorityClusteringCoefficient(dateToSenate, sen, 100));


    }
}
