package com.mango.extract;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hakyoshyt on 3/14/17.
 */
public class CompanyProfile
{

    public static void main(String[] args) throws IOException {

        String nextPageUrl;
        String baseUrl = "http://search.sunbiz.org";
        String genisis = "http://search.sunbiz.org/Inquiry/CorporationSearch/SearchResults?inquiryType=EntityName&searchNameOrder=A&searchTerm=a";
        Document pageHtml = Jsoup.connect(genisis).get();

        while(true)
        {

            try
            {
                Elements pagination =  pageHtml.select("#maincontent > div:nth-child(4) > div.navigationBarPaging > span:nth-child(2) > a");
                for(Element paginationLink : pagination) {

                    nextPageUrl = baseUrl + paginationLink.attr("href");
                    pageHtml = Jsoup.connect(nextPageUrl).get();
                }
                    Elements companyLink = pageHtml.select("td.large-width a");
                    Elements companyStatus = pageHtml.select("td.small-width");

                    List<String> companyLinkList = new ArrayList<>();
                    List<String> companyActiveList = new ArrayList<>();

                    for(Element companyProfileLink : companyLink)
                    {
                        String clink = companyProfileLink.attr("href");
                        companyLinkList.add(baseUrl + clink);
                    }

                    for(Element companyStat : companyStatus)
                    {
                        String companyActive = companyStat.text();
                        companyActiveList.add(companyActive);
                    }

                Iterator<String> companyLinkIter = companyLinkList.iterator();
                Iterator<String> companyActiveIter = companyActiveList.iterator();

                while(companyLinkIter.hasNext() && companyActiveIter.hasNext())
                {
                    String linkIter = companyLinkIter.next();
                    String activeIter = companyActiveIter.next();

                    if(activeIter.equals("Active"))
                    {
                        pageHtml = Jsoup.connect(linkIter).get();

                        Elements companyType = pageHtml.select("#maincontent > div.searchResultDetail > div.detailSection.corporationName > p:nth-child(1)");
                        Elements companyName = pageHtml.select("#maincontent > div.searchResultDetail > div.detailSection.corporationName > p:nth-child(2)");
                        Elements dateFiled = pageHtml.select("#maincontent > div.searchResultDetail > div.detailSection.filingInformation > span:nth-child(2) > div > span:nth-child(6)");
                        Elements mainAddress = pageHtml.select("#maincontent > div.searchResultDetail > div:nth-child(4) > span:nth-child(2) > div");
                        Elements agentName = pageHtml.select("#maincontent > div.searchResultDetail > div:nth-child(6) > span:nth-child(2)");
                        Elements agentAddress = pageHtml.select("#maincontent > div.searchResultDetail > div:nth-child(6) > span:nth-child(3) > div");
                        System.out.println(companyType.text()
                                + "\n" + companyName.text()
                                + "\n" + dateFiled.text()
                                + "\nMain address " + mainAddress.text()
                                + "\nAgent name " + agentName.text()
                                + "\nAgent address " + agentAddress.text()
                                + "\n");
                    }




                }


            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                break;
            }


        }
    }
}
