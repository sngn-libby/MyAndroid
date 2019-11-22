package com.sm.myproject;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository mRepository;

    private LiveData<List<Todo>> mAllTodo;
    private List<Todo> mAllToday;

    String today;

    public TodoViewModel(@NonNull Application application) {
        super(application);

        mRepository = new TodoRepository(application);
        today = new SimpleDateFormat("yyyy-MM-dd").format(
                Calendar.getInstance().getTime());
        mAllTodo = mRepository.getAll();
        mAllToday = mRepository.getToday(today).getValue();
    }

    public LiveData<List<Todo>> getAll() {
        return mAllTodo;
    }

    public List<Todo> getToday() {
        List<Todo> mArr = getAll().getValue();
        mAllToday = null;

        for(int i=0; i<mArr.size(); i++) {
            if(mArr.get(i).getFinDate().equals(today))
                mAllToday.add(mArr.get(i));
        }

        return mAllToday;
    }

    public Todo getItem(int id) {
        final List<Todo> mArr = getAll().getValue();
        for(int i=0; i<mArr.size(); i++) {
            if(mArr.get(i).getId() == id) {
                return mArr.get(i);
            }
        }
        return null;
    }

    public void insert(Todo todo) {
        mRepository.insert(todo);
    }

    public void update(Todo todo) { mRepository.update(todo); }

    public void delete(Todo todo) {
        mRepository.delete(todo);
    }

    public void deleteAll() {
        List<Todo> mArr = getAll().getValue();
        for(int i=0; i<mArr.size(); i++) {
            delete(mArr.get(i));
        }
    }
}
