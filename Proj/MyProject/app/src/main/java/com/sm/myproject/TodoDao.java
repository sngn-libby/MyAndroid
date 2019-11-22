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
    @Query("SELECT * FROM Todo WHERE (Todo.id=(:id))")
    Todo getMemo(int id);

    @Query("SELECT * FROM Todo")
    LiveData<List<Todo>> getAll();

    @Query("SELECT * FROM Todo WHERE (Todo.finDate=(:date))")
    LiveData<List<Todo>> getToday(String date);

    // query for filter
    @Query("SELECT * FROM Todo WHERE (Todo.done=0)")
    List<Todo> getDoing();

    @Query("SELECT * FROM Todo WHERE (Todo.done=1)")
    List<Todo> getDone();

    @Query("SELECT * FROM Todo ORDER BY stDate ASC")
    List<Todo> sortStDate();

    @Query("SELECT * FROM Todo ORDER BY finDate ASC")
    List<Todo> sortfinDate();

    @Query("DELETE FROM Todo")
    void deleteAll();

    @Insert
    void insert(Todo todo);

    @Update
    void update(Todo todo);

    @Delete
    void delete(Todo todo);

}
