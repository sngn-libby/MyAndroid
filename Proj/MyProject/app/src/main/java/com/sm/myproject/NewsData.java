package com.sm.myproject;

import java.io.Serializable;

public class NewsData implements Serializable {
    private String title;
    private String urlToImage;
    private String contents;
    private String url;

    public String getTitle() {
        return title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getContents() {
        return contents;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "NewsData{" +
                "title='" + title + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", contents='" + contents + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
