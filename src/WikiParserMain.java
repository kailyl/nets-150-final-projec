import java.util.Map;

public class WikiParserMain {

    public static void main (String[] args) {
        WikiParser parse = new WikiParser();
        Map<String, String> map = parse.getTableFillMap();
        System.out.print(map);
    }
}
