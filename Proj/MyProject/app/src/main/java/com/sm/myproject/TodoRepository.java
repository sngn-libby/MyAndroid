package com.sm.myproject;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TodoRepository {

    private TodoDao mTodoDao;
    private LiveData<List<Todo>> mAllTodo;
    private LiveData<List<Todo>> mAllToday;
    private static TodoDao mAsyncTaskDao;

    public TodoRepository(Application application) {

        TodoDatabase db = TodoDatabase.getDatabase(application);
        mTodoDao = db.todoDao();
        mAllTodo = mTodoDao.getAll();
        mAllToday = mTodoDao.getToday(
                new SimpleDateFormat("yyyy-MM-dd").format(
                Calendar.getInstance().getTime()));
    }

    public LiveData<List<Todo>> getAll() {
        return mAllTodo;
    }

    public LiveData<List<Todo>> getToday(String format) {
        return mAllToday;
    }

    public void insert(Todo todo) {
        new insertAsyncTask(mTodoDao).execute(todo);
    }

    public void update(Todo todo) { new updateAsyncTask(mTodoDao).execute(todo); }

    public void delete(Todo todo) { new deleteAsyncTask(mTodoDao).execute(todo); }

    public void deleteAll() { new deleteAllAsyncTask(mTodoDao).execute(); }

    private static class insertAsyncTask extends AsyncTask<Todo, Void, Void> {

        insertAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            mAsyncTaskDao.insert(todos[0]);

            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Todo, Void, Void> {

        updateAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            mAsyncTaskDao.update(todos[0]);

            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Todo, Void, Void> {

        deleteAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            mAsyncTaskDao.delete(todos[0]);

            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Todo, Void, Void> {

        deleteAllAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            //for(int i=0; i<)

            return null;
        }
    }

}
