import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WikiParser {
    private String baseURL;
    private Document currentDoc;

    public WikiParser() {
        this.baseURL = "https://en.wikipedia.org/wiki/List_of_United_States_senators_in_the_117th_Congress";
        try {
            this.currentDoc = Jsoup.connect(this.baseURL).get();
        } catch (IOException e) {
            System.out.println("Could not get the site :(");
        }
    }

    public Map<String, String> getTableFillMap() {
        Map<String, String> senators = new HashMap<>();
        Elements articleElements = this.currentDoc.select("table[class*=wikitable sortable]");
        Elements rows = articleElements.select("tbody");
        Elements senateAttr = rows.select("td");

        String currSenator = null;
        String currYear = null;
        for (Element elem : senateAttr) {
            if (elem.childNodeSize() >= 2 && elem.childNode(0).hasAttr("data-sort-value")) {
                currSenator = elem.childNode(0).attr("data-sort-value");
                if (currYear != null) {
                    String[] str = currYear.split(" ");
                    senators.put(currSenator, str[2]);
                }
            }
            if (elem.childNodeSize() == 1) {
                TextNode n = (TextNode) elem.childNode(0);
                if (n.text().matches(".*[0-9].*") && n.text().matches(".*,.*")) {
                    currYear = n.text();
                    String[] str = currYear.split(" ");
                    senators.put(currSenator, str[2]);
                }
            }
        }
        return senators;
    }
}

