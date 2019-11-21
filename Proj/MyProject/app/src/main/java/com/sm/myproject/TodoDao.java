package com.sm.myproject;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TodoDao {
    @Query("SELECT * FROM Memo WHERE (Memo.id == :id)")
    Memo getMemo(int id);

    @Query("SELECT * FROM Memo")
    LiveData<List<Memo>> getAll();

    @Query("SELECT * FROM Memo WHERE (Memo.finDate=:date)")
    LiveData<List<Memo>> getToday(String date);

    // query for filter
    @Query("SELECT * FROM Memo WHERE (Memo.done=0)")
    List<Memo> getDoing();

    @Query("SELECT * FROM Memo WHERE (Memo.done=1)")
    List<Memo> getDone();

    @Query("SELECT * FROM Memo ORDER BY stDate ASC")
    List<Memo> sortStDate();

    @Query("SELECT * FROM Memo ORDER BY finDate ASC")
    List<Memo> sortfinDate();

    @Insert
    void insert(Memo memo);

    @Update
    void update(Memo memo);

    @Delete
    void delete(Memo memo);

}
