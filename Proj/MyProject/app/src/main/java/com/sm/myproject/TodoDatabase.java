package com.sm.myproject;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Memo.class}, version=1)
public abstract class TodoDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();
}
