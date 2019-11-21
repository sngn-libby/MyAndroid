package com.sm.myproject;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.os.AsyncTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TodoRepository {

    private TodoDao mTodoDao;
    private LiveData<List<Memo>> mAllTodo;
    private LiveData<List<Memo>> mAllToday;
    private static TodoDao mAsyncTaskDao;

    public TodoRepository(Application application) {

        TodoDatabase db = TodoDatabase.getDatabase(application);
        mTodoDao = db.todoDao();
        mAllTodo = mTodoDao.getAll();
        mAllToday = mTodoDao.getToday(
                new SimpleDateFormat("yyyy-MM-dd").format(
                Calendar.getInstance().getTime()));

    }

    public LiveData<List<Memo>> getAll() {
        return mAllTodo;
    }

    public LiveData<List<Memo>> getToday(String format) {
        return mAllToday;
    }

    public void insert(Memo memo) {
        new insertAsyncTask(mTodoDao).execute(memo);
    }

    public void update(Memo memo) { new updateAsyncTask(mTodoDao).execute(memo); }

    public void delete(Memo memo) { new deleteAsyncTask(mTodoDao).execute(memo); }

    private static class insertAsyncTask extends AsyncTask<Memo, Void, Void> {

        insertAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Memo... memos) {
            mAsyncTaskDao.insert(memos[0]);

            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Memo, Void, Void> {

        updateAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Memo... memos) {
            mAsyncTaskDao.update(memos[0]);

            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Memo, Void, Void> {

        deleteAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Memo... memos) {
            mAsyncTaskDao.delete(memos[0]);

            return null;
        }
    }

}
