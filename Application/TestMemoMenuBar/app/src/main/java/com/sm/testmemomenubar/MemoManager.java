package com.sm.testmemomenubar;

import java.util.ArrayList;

public class MemoManager {
    private ArrayList<com.sm.testmemomenubar.Memo> manager;
    private static MemoManager instance;

    private  MemoManager() {
        manager = new ArrayList<>();
    }

    public static MemoManager getInstance()  {
        if(instance == null) instance = new MemoManager();
        return instance;
    }

    public void addMemo(String title, String contents, String date) {
        if(getMemo(title) == null)
            manager.add(new com.sm.testmemomenubar.Memo(title, contents, date));
    }

    public boolean deleteMemo(String title) {
        for(int i=0; i<manager.size(); i++) {
            if(manager.get(i).getTitle().equals(title)) {
                manager.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean editMemo(String title, String content, String date) {
        com.sm.testmemomenubar.Memo m = getMemo(title);
        if(m == null) return false;
        m.setContent(content);
        m.setDate(date);

        return true;
    }

    public com.sm.testmemomenubar.Memo getMemo(String title) {
        for(int i=0; i<manager.size(); i++) {
            if(manager.get(i).getTitle().equals(title)) return manager.get(i);
        }

        return null;
    }

    public ArrayList<com.sm.testmemomenubar.Memo> getAllMemo() {
        return manager;
    }
}
