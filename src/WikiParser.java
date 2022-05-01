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

    public void initHashMaps() {
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
    }

    public Map<String, String> getSenatorsToYears() {
        return senatorsToYear;
    }

    public Map<String, List<String>> getYearToSenator() {
        return yearToSenator;
    }

    private void putInHashMaps(String currYear, String currSenator) {
        String[] str = currYear.split(" ");
        String newName;
        if (currSenator.contains("Jr., ")) {
            currSenator = currSenator.replace("Jr., ", "");
        }
        if (currSenator.contains("Dick")) {
            currSenator = currSenator.replace("Dick", "Richard");
        }
        if (currSenator.contains("Bob")) {
            currSenator = currSenator.replace("Bob", "Robert");
        }
        if (currSenator.contains("Luján")) {
            currSenator = currSenator.replace("Luján", "Lujan");
        }
        newName = currSenator.split(" ")[0] + " " +
                currSenator.split(" ")[1].charAt(0);
        if (newName.contains("Masto")) {
            newName = "Cortez Masto, C";
        }
        if (newName.contains("Van H")) {
            newName = "Van Hollen, C";
        }
        if (newName.contains("Harris") || newName.contains("Loeffler")) {
            return;
        }
        senatorsToYear.put(newName, str[2]);
        if (yearToSenator.containsKey(str[2])) {
            containsSenator(yearToSenator, newName);
            yearToSenator.get(str[2]).add(newName);
        } else {
            containsSenator(yearToSenator, newName);
            List<String> list = new ArrayList<>();
            list.add(newName);
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

