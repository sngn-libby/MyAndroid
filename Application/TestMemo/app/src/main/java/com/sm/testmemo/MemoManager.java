package com.sm.testmemo;

import java.util.ArrayList;

public class MemoManager {

    private static MemoManager instance;
    public ArrayList<Memo> arr;

    private MemoManager() {
        arr = new ArrayList<>();
    }

    public static MemoManager getInstance() {
        if(instance == null) {
            instance = new MemoManager();
        }
        return instance;
    }

    public void addMemo(Memo m) {
        arr.add(m);
    }

    public ArrayList<Memo> getAllMemo() {
        return arr;
    }
}
