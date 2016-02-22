package com.example.serge.rianews;


import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class RiaHtmlParser {

    private static final String SRC = "src";
    private static final String ARTICLE_ILLUSTRATION = "article_illustration";
    private static final String IMG_SRC_SELECTOR = "img[src]";
    private static final String ARTICLE_FULL_TEXT = "article_full_text";
    private static final String P_TAG = "p";

    public String getImageLink(String newsLink) {
        String imageLink = null;
        try {
            Document document = Jsoup.connect(newsLink).get();
            Element articleIllustrationBlock = document.select("." + ARTICLE_ILLUSTRATION).first();
            if(articleIllustrationBlock != null) {
                Element imgSrc = articleIllustrationBlock.select(IMG_SRC_SELECTOR).first();
                if(imgSrc != null) {
                    imageLink = imgSrc.attr(SRC);
                }
            }
        } catch (IOException ex) {
            Log.d("load", "IO_Ex in getImageLink");
        }
        return imageLink;
    }

    public String getTextNews(String newsLink) {
        String text = null;
        try {
            Document document = Jsoup.connect(newsLink).get();
            Element articleText = document.select("#" + ARTICLE_FULL_TEXT).first();
            Elements p_tags = articleText.select(P_TAG);
            for(Element el : p_tags) {
                text += el.text();
            }

        } catch (IOException ex) {
            Log.d("load", "IO_Ex in getTextNews");
        }
        return text;
    }

}
