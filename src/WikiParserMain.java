import java.util.List;
import java.util.Map;
import java.util.Set;

public class WikiParserMain {

    public static void main (String[] args) {
        WikiParser parse = new WikiParser();
        parse.initHashMaps();
        Map<String, String> senateToDate = parse.getSenatorsToYears();
        for (String key : senateToDate.keySet()) {
            System.out.println(key);
        }
        for (String key : senateToDate.keySet()) {
            System.out.println(key + ": " + senateToDate.get(key));
        }
        System.out.println(" ");
        Map<String, List<String>> dateToSenate = parse.getYearToSenator();
        for (String key : dateToSenate.keySet()) {
            System.out.println(key + ": " + dateToSenate.get(key));
        }

        CommParser parser = new CommParser();
        //Map<String, List<String>> map = parser.getSenToColleagues();
        //System.out.print(map);

        parser.getSenatorList();
        parser.getCommURLs();
//        parser.getCommMembers("Committee on the Judiciary");

        Map<String, Set<String>> sen = parser.getSenToCommMembs();
        for (String key : sen.keySet()) {
            System.out.println(key + ": " + sen.get(key));
        }
    }
}
