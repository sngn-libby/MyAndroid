package com.sm.testmemolist;

import java.util.ArrayList;

public class MemoManager {
    private ArrayList<Memo> manager;
    private static MemoManager instance;

    private  MemoManager() {
        manager = new ArrayList<>();
    }

    public static MemoManager getInstance()  {
        if(instance == null) instance = new MemoManager();
        return instance;
    }

    public void addMemo(String title, String content, String date) {
        manager.add(new Memo(title, content, date));
    }

    public int cntMemo() {
        return manager.size();
    }

    public boolean deleteMemo(String title) {
        for(int i=0; i<manager.size(); i++) {
            if(manager.get(i).getTitle() == title) {
                manager.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean editMemo(String title, String content, String date) {
        for(int i=0; i<manager.size(); i++) {
            if(manager.get(i).getTitle() == title) {

                manager.get(i).setContent(content);
                manager.get(i).setDate(date);

                return true;
            }
        }

        return false;
    }

    public Memo getMemo(String title) {
        for(int i=0; i<manager.size(); i++) {
            if(manager.get(i).getTitle() == title) return manager.get(i);
        }

        return null;
    }

    public ArrayList<Memo> getAllMemo() {
        return manager;
    }
}
