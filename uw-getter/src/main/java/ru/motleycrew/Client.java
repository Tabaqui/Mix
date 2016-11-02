package ru.motleycrew;


import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.motleycrew.model.UWMessage;
import ru.motleycrew.model.Work;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 06.04.2016.
 */
public class Client {

    private static WebClient webClient;
    private static boolean refresh = false;

    static {
        webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
        webClient.getOptions().setCssEnabled(false);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setTimeout(10000);
        webClient.getCookieManager().clearCookies();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }

    public static void close() {
        webClient.close();
    }


    private static WebRequest getRequest(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        WebRequest request = new WebRequest(url, HttpMethod.GET);
        return request;
    }



    public static UWMessage lastWorkHeader() {
        try {
            Page p;
            WebRequest getRequest = getRequest("https://www.upwork.com/ab/feed/topics/rss?securityToken=d6c40f15cdf07ecae96c48ad7a2f6cbf5f53e26051029a2fa106bde815eb482b%7E77afcb5");
            p = webClient.getPage(getRequest);

//            String cntnt = p.getWebResponse().getContentAsString();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(p.getWebResponse().getContentAsStream());
            NodeList list = doc.getElementsByTagName("item");
            String title;
            String date;
            String link;
            if (list.getLength() > 0) {
                Node first = list.item(0).getFirstChild();
                title = first.getTextContent();
                link = first.getNextSibling().getTextContent();
                while (!first.getNextSibling().getNodeName().equals("pubDate")) {
//                    System.out.println(first.getNodeName());
                    first = first.getNextSibling();
                }
                date = first.getNextSibling().getTextContent();
            } else {
                title = "Rss is empty";
                date = null;
                link = null;
            }
//            System.out.println(list.getLength());
//            System.out.println(cntnt);

            return new UWMessage(title, date, link);
        } catch (Exception ex) {
            refresh = false;
            System.out.println("Error login post");
            webClient.getCookieManager().clearCookies();
            webClient.getCache().clear();
            webClient.close();
            ex.printStackTrace();
            return null;
        }
    }

}
