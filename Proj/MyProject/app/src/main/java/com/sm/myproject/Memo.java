package com.sm.myproject;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Memo {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String content, finDate;

    private String stDate;

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

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", stDate='" + stDate + '\'' +
                ", finDate='" + finDate + '\'' +
                ", done=" + done +
                ", alarm=" + alarm +
                '}';
    }
}
