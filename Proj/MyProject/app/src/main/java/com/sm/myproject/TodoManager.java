package com.sm.myproject;

import java.util.ArrayList;

public class TodoManager {
    private ArrayList<Memo> manager;
    private static TodoManager instance;

    private TodoManager() {
        manager = new ArrayList<>();
    }

    public static TodoManager getInstance()  {
        if(instance == null) instance = new TodoManager();
        return instance;
    }

    public void addMemo(String title, String contents, String stDate, String finDate) {
        if(getMemo(title) == null)
            manager.add(new Memo(title, contents, stDate, finDate));
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

    public boolean editMemo(String title, String content, String date, boolean stat) {
        Memo m = getMemo(title);
        if(m == null) return false;
        m.setContent(content);
        m.setStDate(date);
        m.setDone(stat);

        return true;
    }

    public Memo getMemo(String title) {
        for(int i=0; i<manager.size(); i++) {
            if(manager.get(i).getTitle().equals(title)) return manager.get(i);
        }

        return null;
    }

    public ArrayList<Memo> getAllMemo() {
        return manager;
    }
}
