package com.sm.testmemomenubar;

public class Memo {
    private String title, Content, Date;

    public Memo(String title, String content, String date) {
        this.title = title;
        Content = content;
        Date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return Content;
    }

    public String getDate() {
        return Date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setDate(String date) {
        Date = date;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "title='" + title + '\'' +
                ", Content='" + Content + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}
