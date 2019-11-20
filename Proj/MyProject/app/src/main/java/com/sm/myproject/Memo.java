package com.sm.myproject;

public class Memo {
    private String title, content, stDate, finDate;

    private boolean done, alarm;

    public Memo(String title, String content, String stDate, String finDate) {
        this.title = title;
        this.content = content;
        this.stDate = stDate;
        this.finDate = null;
        this.done = false;
        this.alarm = false;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getStDate() {
        return stDate;
    }

    public String getFinDate() {
        return finDate;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public boolean isDone() {
        return done;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStDate(String stDate) {
        this.stDate = stDate;
    }

    public void setFinDate(String finDate) {
        this.finDate = finDate;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", stDate='" + stDate + '\'' +
                ", finDate='" + finDate + '\'' +
                ", done=" + done +
                ", alarm=" + alarm +
                '}';
    }
}
