package edu.scsa.android.cardviewtest;

import java.io.Serializable;

public class News implements Serializable {
    private String title;
    private String img;
    private String content;
    private String url;

    public News(){}

    public News(String title, String img, String content, String url) {
        this.title = title;
        this.img = img;
        this.content = content;
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getImg() {
        return img;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
