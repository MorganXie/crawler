package com.github.hcsp;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException {
        List<String> linkPool = new ArrayList<>();
        Set<String> processedLinks = new HashSet<>();
        linkPool.add("https://sina.cn/");
        while (true) {
            if (linkPool.isEmpty()) {
                break;
            }
            String link = linkPool.remove(linkPool.size()-1);
            if (processedLinks.contains(link)) {
                continue;
            }
            if (link.contains("news.sina.cn")||link.equals("https://sina.cn/")) {
                System.out.println(link);
                if (link.startsWith("//")){
                    link+="htttps:";
                    System.out.println(link);
                }
                try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                    HttpGet httpGet = new HttpGet(link);
                    httpGet.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36");
                    try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
                        HttpEntity entity1 = response1.getEntity();
                        String html = EntityUtils.toString(entity1);
                        Document doc = Jsoup.parse(html);
                        ArrayList<Element> links = doc.select("a");
                        for (Element aTag : links) {
                            if (!aTag.attr("href").equals("javascript:;")){
                                linkPool.add(aTag.attr("href"));
                            }
                        }
                        ArrayList<Element> articleTags = doc.select("article");
                        if (!articleTags.isEmpty()){
                            for (Element articleTag:articleTags) {
                                System.out.println(articleTag.child(0).text());
                            }
                        }
                    }
                }
                processedLinks.add(link);
            } else {
                continue;
            }
        }


    }
}
