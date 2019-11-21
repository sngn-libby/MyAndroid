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

    private LiveData<List<Memo>> mAllTodo;
    private List<Memo> mAllToday;

    String today;

    public TodoViewModel(@NonNull Application application) {
        super(application);

        mRepository = new TodoRepository(application);
        today = new SimpleDateFormat("yyyy-MM-dd").format(
                Calendar.getInstance().getTime());
        mAllTodo = mRepository.getAll();
        mAllToday = mRepository.getToday(today).getValue();
    }

    public LiveData<List<Memo>> getAll() {
        return mAllTodo;
    }

    public List<Memo> getToday() {
        List<Memo> mArr = mAllTodo.getValue();
        mAllToday = null;

        for(int i=0; i<mArr.size(); i++) {
            if(mArr.get(i).getFinDate().equals(today))
                mAllToday.add(mArr.get(i));
        }

        return mAllToday;
    }

    public void insert(Memo memo) {
        mRepository.insert(memo);
    }

    public void update(Memo memo) {
        mRepository.update(memo);
    }

    public void delete(Memo memo) {
        mRepository.delete(memo);
    }
}
