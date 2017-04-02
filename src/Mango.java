import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hakyoshyt on 3/14/17.
 */
public class Mango {

    public static void main(String[] args) throws IOException {

        String activeUrl;
        String nextPage;
        String startUrl;
        String baseUrl;


        baseUrl= "http://search.sunbiz.org";   //main url for the sunbiz website
        startUrl= "http://search.sunbiz.org/Inquiry/CorporationSearch/SearchResults?inquiryType=EntityName&searchNameOrder=A&searchTerm=a";

        Document callPage = Jsoup.connect(startUrl).get(); //grabs the page

        Elements sitelinks = callPage.select("td.large-width a");
        Elements status = callPage.select("td.small-width");

        List<String> siteLinksList = new ArrayList<>();
        List<String> statuslist = new ArrayList<>();


        for(Element links : sitelinks)
        {
            siteLinksList.add(links.attr("href"));

        }

        for(Element aStatus : status)
        {
            statuslist.add(aStatus.text());
        }


        Iterator<String> linkIter =  siteLinksList.iterator();
        Iterator<String> statusIter = statuslist.iterator();


        while(linkIter.hasNext() && statusIter.hasNext())
        {

            String companyLink = linkIter.next();
            String companyStatus = statusIter.next();

            Elements pagination = callPage.select("#maincontent > div:nth-child(4) > div.navigationBarPaging > span:nth-child(2) > a");
            for(Element paginationLink : pagination)
            {
                startUrl = baseUrl + paginationLink.attr("href");
                //callPage = Jsoup.connect(nextPage).get();
                //System.out.println(nextPage);
            }

            if(companyStatus.equals("Active"))
            {
                //open url with jsoup and extract page data
                activeUrl = baseUrl + companyLink;
                callPage =  Jsoup.connect(activeUrl).get();

                Elements companyType = callPage.select("#maincontent > div.searchResultDetail > div.detailSection.corporationName > p:nth-child(1)");
                Elements companyName = callPage.select("#maincontent > div.searchResultDetail > div.detailSection.corporationName > p:nth-child(2)");
                Elements dateFiled = callPage.select("#maincontent > div.searchResultDetail > div.detailSection.filingInformation > span:nth-child(2) > div > span:nth-child(14)");
                Elements mainAddress = callPage.select("#maincontent > div.searchResultDetail > div:nth-child(4) > span:nth-child(2) > div");
                Elements agentName = callPage.select("#maincontent > div.searchResultDetail > div:nth-child(6) > span:nth-child(2)");
                Elements agentAddress = callPage.select("#maincontent > div.searchResultDetail > div:nth-child(6) > span:nth-child(3) > div");
                System.out.println(companyType.text()
                        + "\n" + companyName.text()
                        + "\n" + dateFiled.text()
                        + "\nMain address " + mainAddress.text()
                        + "\nAgent name " + agentName.text()
                        + "\nAgent address " + agentAddress.text());

            }
            else
            {
                continue;
            }

            break;
        }

    }
}
