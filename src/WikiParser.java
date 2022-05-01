import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WikiParser {
    private String baseURL;
    private Document currentDoc;
    private Map<String, String> senatorsToYear;
    private Map<String, List<String>> yearToSenator;

    public WikiParser() {
        this.baseURL = "https://en.wikipedia.org/wiki/List_of_United_States_senators_in_the_117th_Congress";
        try {
            this.currentDoc = Jsoup.connect(this.baseURL).get();
            this.senatorsToYear = new HashMap<>();
            this.yearToSenator = new HashMap<>();
        } catch (IOException e) {
            System.out.println("Could not get the site :(");
        }
    }

    public Map<String, String> getSenatorsToYear() {
        Elements articleElements = this.currentDoc.select("table[class*=wikitable sortable]");
        Elements rows = articleElements.select("tbody");
        Elements senateAttr = rows.select("td");

        String currSenator = null;
        String currYear = null;
        for (Element elem : senateAttr) {
            if (elem.childNodeSize() >= 2 && elem.childNode(0).hasAttr("data-sort-value")) {
                currSenator = elem.childNode(0).attr("data-sort-value");
                if (currYear != null) {
                    putInHashMaps(currYear, currSenator);
                }
            }
            if (elem.childNodeSize() == 1) {
                TextNode n = (TextNode) elem.childNode(0);
                if (n.text().matches(".*[0-9].*") && n.text().matches(".*,.*")) {
                    currYear = n.text();
                    putInHashMaps(currYear, currSenator);
                }
            }
        }
        System.out.println(yearToSenator);
        return senatorsToYear;
    }

    private void putInHashMaps(String currYear, String currSenator) {
        String[] str = currYear.split(" ");
        senatorsToYear.put(currSenator, str[2]);
        if (yearToSenator.containsKey(str[2])) {
            containsSenator(yearToSenator, currSenator);
            yearToSenator.get(str[2]).add(currSenator);
        } else {
            containsSenator(yearToSenator, currSenator);
            List<String> list = new ArrayList<>();
            list.add(currSenator);
            yearToSenator.put(str[2], list);
        }
    }

    private void containsSenator(Map<String, List<String>> map, String senator) {
        for (String key : map.keySet()) {
            if (map.get(key).contains(senator)) {
                map.get(key).remove(senator);
            }
        }
    }
}

