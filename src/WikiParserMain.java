import java.util.List;
import java.util.Map;

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
    }
}
