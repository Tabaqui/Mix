package ru.motleycrew;


import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ru.motleycrew.model.UWMessage;
import ru.motleycrew.model.Work;

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
        webClient.setJavaScriptTimeout(5000);
        webClient.getOptions().setTimeout(10000);

        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }

    public static void close() {
        webClient.close();
    }

    public static UserData getLogin(String urlString) {
        if (refresh) {
            return null;
        }
        try {
            HtmlPage p = webClient.getPage(urlString);
//            JavaScriptJobManager m = p.getEnclosingWindow().getJobManager();
            String token = p.getHtmlElementById("login__token").getAttribute("value");
            String iovation = p.getHtmlElementById("login_iovation").getAttribute("value");
            UserData userData = new UserData(iovation, token);
            return userData;
        } catch (Exception ex) {
            System.out.println("Error get login page");
            return null;
        }
    }

    private static WebRequest loginRequest(String urlString, UserData data) throws MalformedURLException {
        URL url = new URL(urlString);
        WebRequest request = new WebRequest(url, HttpMethod.POST);
        request.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/x-javascript,application/xml;q=0.9,*/*;q=0.8");
        request.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        request.setAdditionalHeader("Referer", "https://www.upwork.com/login");
        request.setAdditionalHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3");
        request.setAdditionalHeader("Accept-Encoding", "gzip,deflate,br");
        request.setAdditionalHeader("Cache-Control", "no-cache");

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new NameValuePair("login[_token]", data.getToken()));
        parameters.add(new NameValuePair("login[iovation]", data.getIovation()));
        parameters.add(new NameValuePair("login[password]", data.getPassword()));
        parameters.add(new NameValuePair("login[redir]", data.getRedirect()));
        parameters.add(new NameValuePair("login[username]", data.getLogin()));
        request.setRequestParameters(parameters);

        refresh = true;

        return request;
    }

    private static WebRequest findWorkRequest(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        WebRequest request = new WebRequest(url, HttpMethod.GET);
        request.setAdditionalHeader("Accept", "application/json, text/plain, */*");
        request.setAdditionalHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3");
        request.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
        request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
        return request;
    }

    public static UWMessage lastWorkHeader(String urlString, UserData data) {
        try {
            Page p;
            if (!refresh) {
//                try {
//                    WebRequest refreshRequest = findWorkRequest("https://www.upwork.com/find-work-home/");
//                    WebRequest refreshRequest = findWorkRequest("https://www.upwork.com/ab/find-work/");
//                    p = webClient.getPage(refreshRequest);
//                } catch (Exception ex) {
//                    refresh = false;
//                    webClient.getCache().clear();
//                    webClient.getCookieManager().clearCookies();
//                    return null;
//                }
//            } else {
                WebRequest loginRequest = loginRequest(urlString, data);
                p = webClient.getPage(loginRequest);
//                p = webClient.getPage(p.getUrl());
            }
            p = webClient.getPage(findWorkRequest("https://www.upwork.com/ab/find-work/api/feeds/search?topic=2142440"));
            String json = p.getWebResponse().getContentAsString();
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("results");
//            try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            Type collectionType = new TypeToken<List<Work>>() {
            }.getType();
            List<Work> works = gson.fromJson(jsonArray, collectionType);
            Work last = works.get(0);
            System.out.println(last.getCreatedOn());


//            List<DomElement> elementList = (List<DomElement>) p.getByXPath("//h1[@class='oRowTitle oH3']");
//            DomElement hrefElement = elementList.get(0).getFirstElementChild();
//            String hrefValue = URLDecoder.decode(hrefElement.getAttributeNode("href").getValue(), "utf8");
//            String header = hrefElement.getFirstChild().asText();
//            elementList = (List<DomElement>) p.getByXPath("//div[@class='jsTruncated']");
//            DomElement descriptionElement = elementList.get(0);
//            String message = descriptionElement.getFirstChild().asText();
            return new UWMessage(last.getTitle(), last.getCreatedOn(), last.getCiphertext());
//            return null;
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
