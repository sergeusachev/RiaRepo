package com.example.serge.rianews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class NewsData implements Serializable {

    public static final String LOAD_NONE = "DO_NOT_LOAD";
    public static final String IMAGE_WAS_LOADED = "loaded";
    public static final String HAS_TEXT = "hasText";

    private static final String JSON_DATE = "date";
    private static final String JSON_TITLE = "title";
    private static final String JSON_NEWS_URL= "newsUrl";
    private static final String JSON_IMAGE_URL = "imageUrl";
    private static final String JSON_HAS_IMAGE_FLAG = "hasImage";
    private static final String JSON_TEXT_NEWS = "textNews";

    private String date;
    private String title;
    private String newsUrl;
    private String imageUrl;
    private String textNews;

    private boolean hasImage;
    private boolean hasBody;
    private boolean imageWasLoaded;

    public NewsData() {

    }

    public NewsData(JSONObject jsonObject) throws JSONException {
        date = jsonObject.getString(JSON_DATE);
        title = jsonObject.getString(JSON_TITLE);
        newsUrl = jsonObject.getString(JSON_NEWS_URL);
        hasImage = jsonObject.getBoolean(JSON_HAS_IMAGE_FLAG);
        imageWasLoaded = jsonObject.getBoolean(IMAGE_WAS_LOADED);
        hasBody = jsonObject.getBoolean(HAS_TEXT);
        if(imageWasLoaded) {
            if(hasImage) {
                imageUrl = jsonObject.getString(JSON_IMAGE_URL);
            } else {
                imageUrl = LOAD_NONE;
            }
        } else {
            imageUrl = null;
        }

        if(hasBody) {
            textNews = jsonObject.getString(JSON_TEXT_NEWS);
        } else {
            textNews = null;
        }
    }

    public JSONObject toJSON() throws JSONException {

        JSONObject json = new JSONObject();

        json.put(JSON_DATE, date);
        json.put(JSON_TITLE, title);
        json.put(JSON_NEWS_URL, newsUrl);
        json.put(JSON_HAS_IMAGE_FLAG, hasImage);
        json.put(IMAGE_WAS_LOADED, imageWasLoaded);
        json.put(HAS_TEXT, hasBody);
        if(imageWasLoaded && hasImage) {
            json.put(JSON_IMAGE_URL, imageUrl);
        }
        if(hasBody) {
            json.put(JSON_TEXT_NEWS, textNews);
        }

        return json;
    }

    public void setHasBody(boolean hasBody) {
        this.hasBody = hasBody;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public void setImageWasLoaded(boolean imageWasLoaded) {
        this.imageWasLoaded = imageWasLoaded;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTextNews(String textNews) {
        this.textNews = textNews;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTextNews() {
        return textNews;
    }
}
