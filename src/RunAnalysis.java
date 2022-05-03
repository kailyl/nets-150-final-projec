import java.util.*;

public class RunAnalysis {
    public static void main (String[] args) {
        WikiParser parse = new WikiParser();
        parse.initHashMaps();
        Map<String, Integer> senateToDate = parse.getSenatorsToYears();

        Map<Integer, List<String>> dateToSenate = parse.getYearToSenator();


        CommParser parser = new CommParser();

        parser.getSenatorList();
        parser.getCommURLs();

        Map<String, Set<String>> sen = parser.getSenToCommMembs();



        CongressMemberAnalysis analysis = new CongressMemberAnalysis(dateToSenate, sen);
        analysis.findOriginalClusteringCoefficients();
        analysis.findGenerationalClusteringCoefficients();
        analysis.findDifference();

        System.out.println("original clustering coefficients:");
        Map<String, Double> originalClusteringCoefficients = analysis.getOriginalCoefficients();
        for (String member : originalClusteringCoefficients.keySet()) {
            System.out.println(member + "=" + originalClusteringCoefficients.get(member));
        }
        System.out.println(" ");
        System.out.println("generational clustering coefficients:");
        Map<String, Double> generationalClusteringCoefficients = analysis.getGenerationalCoefficients();
        for (String member : generationalClusteringCoefficients.keySet()) {
            System.out.println(member + " = " + generationalClusteringCoefficients.get(member));
        }
        System.out.println(" ");
        System.out.println("difference in clustering coefficients:");
        Map<String, Double> difference = analysis.getDifference();
        for (String member : difference.keySet()) {
            System.out.println(member + " = " + difference.get(member));
        }
        System.out.println(" ");
        System.out.println("average difference:\n" + analysis.findDifferenceAverage());


    }
}
