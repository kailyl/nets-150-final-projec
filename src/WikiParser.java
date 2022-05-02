import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiParser {
    private String baseURL;
    private Document currentDoc;
    private Map<String, String> senatorsToLink;
    private Map<String, String> senatorsToYear;
    private Map<String, List<String>> yearToSenator;

    public WikiParser() {
        this.baseURL = "https://en.wikipedia.org/wiki/List_of_United_States_senators_in_the_117th_Congress";
        try {
            this.currentDoc = Jsoup.connect(this.baseURL).get();
            this.senatorsToYear = new HashMap<>();
            this.yearToSenator = new HashMap<>();
            this.senatorsToLink = new HashMap<>();
        } catch (IOException e) {
            System.out.println("Could not get the site :(");
        }
    }

    public void initHashMaps() {
        Elements articleElements = this.currentDoc.select("table[class*=wikitable sortable]");
        Elements rows = articleElements.select("tbody");
        Elements senateAttr = rows.select("td");

        String currSenator;
        String currHTML;
        for (Element elem : senateAttr) {
            if (elem.childNode(0).hasAttr("data-sort-value")) {
                currSenator = elem.childNode(0).attr("data-sort-value");
                Node node = elem.childNode(0).childNode(0).childNode(0).childNode(0);
                currHTML = node.attr("href");
                senatorsToLink.put(currSenator, "https://en.wikipedia.org/" + currHTML);
            }
        }
        getAges();
    }

    public Map<String, String> getSenatorsToYears() {
        return senatorsToYear;
    }

    public Map<String, List<String>> getYearToSenator() {
        return yearToSenator;
    }

    public void getAges() {
        String link;
        String age;

        Pattern pattern = Pattern.compile("(.*)(;)(\\d+)");

        for (String sen : senatorsToLink.keySet()) {
            link = senatorsToLink.get(sen);
            resetURL(link);
            Element senAgeElement = this.currentDoc.selectFirst("span[class*=ForceAgeToShow]");
            String senAgeText = senAgeElement.childNode(0).toString();
            Matcher senAgeMatcher = pattern.matcher(senAgeText);
            if (senAgeMatcher.find()) {
                age = senAgeMatcher.group(3);
                putInHashMaps(age, sen);
            }

        }
    }

    private void putInHashMaps(String age, String currSenator) {
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
        senatorsToYear.put(newName, age);
        if (yearToSenator.containsKey(age)) {
            containsSenator(yearToSenator, newName);
            yearToSenator.get(age).add(newName);
        } else {
            containsSenator(yearToSenator, newName);
            List<String> list = new ArrayList<>();
            list.add(newName);
            yearToSenator.put(age, list);
        }
    }

    private void containsSenator(Map<String, List<String>> map, String senator) {
        for (String key : map.keySet()) {
            if (map.get(key).contains(senator)) {
                map.get(key).remove(senator);
            }
        }
    }

    private void resetURL(String newURL) {
        this.baseURL = newURL;
        try {
            this.currentDoc = Jsoup.connect(this.baseURL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

