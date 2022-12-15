package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static Pattern pattern = Pattern.compile("\\d+\\.\\d+");
    private static Document getPage() throws IOException {
        String url = "https://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }
    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new Exception("Can not extract date from String!");
        }
    }

    private static int printPartValues(Elements values, int index) {
        int iterationCount = 4 + index;
        if (index == 0) {
            if (values.get(0).text().contains("День")) {
                iterationCount = 3;
            } else if (values.get(0).text().contains("Вечер")) {
                iterationCount = 2;
            } else if (values.get(0).text().contains("Ночь")) {
                iterationCount = 5;
            }
        }
        for (int i = index; i < iterationCount; i++) {
            Element valueLine = values.get(i);
            for (Element value : valueLine.select("td")) {
                System.out.printf("%s\t", value.text());
            }
            System.out.println();
        }
        return iterationCount;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;
        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + "\tЯвления\tТемпература\tДавление\tВлажность\tВетер");
            int iterationCount = printPartValues(values, index);
            index = iterationCount;
        }
    }
}