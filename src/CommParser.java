import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//Class to find the committees that each member belongs to
public class CommParser {

    private String currentURL;
    private Document currentDoc;

    private List<String> allSenators;
    private Map<String, String> commURLs;
    private Map<String, Set<String>> neighborsInComms;

    /*
     * Constructor that initializes the base URL and loads the document produced from that URL.
     */
    public CommParser() {
        resetURL("https://www.senate.gov/general/committee_assignments/assignments.htm");
    }

    /*
     * Resets the current URL to a provided new page and loads the document produced from this URL.
     *
     * @param newURL    a new URL
     */
    private void resetURL(String newURL) {
        this.currentURL = newURL;
        try {
            this.currentDoc = Jsoup.connect(this.currentURL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSenatorList() {
        this.allSenators = new ArrayList<String>();

        Pattern pattern = Pattern.compile("([-A-zÀ-ú ]+)(\\, )(\\w)");
        Elements senatorStyle = this.currentDoc.select("div[style *= float:left; width:25%]");

        for (Element senator : senatorStyle) {
            String allSenInfo = senator.text();
            Matcher senMatcher = pattern.matcher(allSenInfo);
            if (senMatcher.find()) {
                String senName = senMatcher.group(1) + senMatcher.group(2) + senMatcher.group(3);
                if (senName.equals("Luján, B")) {
                    senName = "Lujan, B";
                }
                this.allSenators.add(senName);
            }
        }
        return allSenators;
    }

    public Map<String, String> getCommURLs() {
        this.commURLs = new HashMap<String, String>();

        Elements committees = this.currentDoc.select("a[href *= /general/committee_membership/committee_memberships]");
        for (Element comm : committees) {
            String commURL = comm.attr("href");
            String commName = comm.text();
            if (!commURLs.containsKey(commName)) {
                this.commURLs.put(commName, "https://www.senate.gov" + commURL);
            }
        }

        // remove joint committees:
        commURLs.remove("Commission on Security and Cooperation in Europe");
        commURLs.remove("Joint Committee on Printing");
        commURLs.remove("Joint Committee on the Library");
        commURLs.remove("Joint Committee on Taxation");
        commURLs.remove("Joint Economic Committee");

        return commURLs;
    }

    public List<String> getCommMembers(String commName) {
        List<String> allMemNames = new ArrayList<String>();
        resetURL(commURLs.get(commName));

        Pattern pattern = Pattern.compile("([-A-zÀ-ú ]+)(\\, )(\\w)");

        Element firstComm = this.currentDoc.selectFirst("td[valign *= top]");
        String[] firstCommMembs = firstComm.html().split("<br>");
        for (int i = 0; i < firstCommMembs.length; i++) {
            Matcher firstMemMatcher = pattern.matcher(firstCommMembs[i]);
            if (firstMemMatcher.find()) {
                String firstMemName = firstMemMatcher.group(1) + firstMemMatcher.group(2) + firstMemMatcher.group(3);
                allMemNames.add(firstMemName);
            }
        }

        Element secondComm = firstComm.nextElementSibling();
        String[] secCommMembs = secondComm.html().split("<br>");
        for (int i = 0; i < secCommMembs.length; i++) {
            Matcher secMemMatcher = pattern.matcher(secCommMembs[i]);
            if (secMemMatcher.find()) {
                String secMemName = secMemMatcher.group(1) + secMemMatcher.group(2) + secMemMatcher.group(3);
                allMemNames.add(secMemName);
            }
        }

        return allMemNames;
    }

    public Map<String, Set<String>> getSenToCommMembs() {
        this.neighborsInComms = new HashMap<String, Set<String>>();
        Map<String, List<String>> sensInComms = new HashMap<String, List<String>>();

        for (String commName : commURLs.keySet()) {
            sensInComms.put(commName, getCommMembers(commName));
        }

        for (String sen : allSenators) {
            Set<String> currNeighbors = new HashSet<String>();

            for (String commName : commURLs.keySet()) {
                List<String> sensInComm = sensInComms.get(commName);
                if (sensInComm.contains(sen)) {
                    currNeighbors.addAll(sensInComm);
                }
            }
            currNeighbors.remove(sen);
            this.neighborsInComms.put(sen, currNeighbors);
        }

        return this.neighborsInComms;
    }
}
