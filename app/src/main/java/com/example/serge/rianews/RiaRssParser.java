package com.example.serge.rianews;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RiaRssParser {

    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String DATE = "pubDate";
    private static final String LINK = "link";

    private ArrayList<NewsData> oldList;

    public RiaRssParser(ArrayList<NewsData> oldList) {
        this.oldList = oldList;
    }

    public ArrayList<NewsData> fetchNews(String url) {
        ArrayList<NewsData> list = new ArrayList<>();

        try {
            String xml = getUrlString(url);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            parseNews(list, parser);
        } catch (IOException ex) {
            Log.e("load", "IO_Ex in fetchNews");
        } catch (XmlPullParserException ex) {
            Log.e("load", "XPP_Ex in fetchNews");
            ex.printStackTrace();
        }

        return list;
    }

    public byte[] getUrl(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();

        return response.body().bytes();
    }

    private String getUrlString(String url) throws IOException {
        return new String(getUrl(url));
    }

    private void parseNews(ArrayList<NewsData> items, XmlPullParser parser)
            throws XmlPullParserException, IOException {

        String title = null;
        String date = null;
        String linkNews = null;

        int eventType = parser.next();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG && ITEM.equals(parser.getName())) {
                eventType = parser.next();
                while (!ITEM.equals(parser.getName())) {
                    if(eventType == XmlPullParser.START_TAG) {
                        switch (parser.getName()) {
                            case TITLE:
                                parser.next();
                                title = parser.getText();
                                break;
                            case DATE:
                                parser.next();
                                date = parser.getText();
                                break;
                            case LINK:
                                parser.next();
                                linkNews = parser.getText();
                                break;
                        }
                    }
                    eventType = parser.next();
                }
                NewsData newsData = new NewsData();
                newsData.setTitle(title);
                newsData.setDate(date);
                newsData.setNewsUrl(linkNews);
                items.add(newsData);
            }
            eventType = parser.next();
        }
    }
}
