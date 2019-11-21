package com.sm.myproject;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TodolistActivity extends AppCompatActivity {

    public static final int ADD_MODE = 1;
    public static final int VIEW_MODE = 3;

    public static final boolean DONE_STAT = true;
    public static final boolean DOING_STAT = false;

    private static final int DELETE_MENU_ID = Menu.FIRST;
    private static final int FILTER_ALL_ID = Menu.FIRST + 1;
    private static final int FILTER_DONE_ID = Menu.FIRST + 2;
    private static final int FILTER_DOING_ID = Menu.FIRST + 3;


    ListView memoListView;
    TodoManager manager;
    List<Memo> memoArr, mMemoArr, mKeepArr, delMemoArr;
    String sel;
    TodoAdapter memoAdapter;
    FloatingActionButton addFab;
    SearchView searchView;
    TodoDatabase db;
    TodoDao tDao;
    private TodoViewModel mTodoViewModel;

    boolean delete_mode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        // 0. Database
        db = Room.databaseBuilder(this, TodoDatabase.class, "todo-db").build();
        tDao = db.todoDao();
        memoAdapter = new TodoAdapter(R.layout.todoli_row_layout, memoArr);
        // manager = TodoManager.getInstance();
        memoListView = findViewById(R.id.memoListView);
        addFab = (FloatingActionButton) findViewById(R.id.addFab);
        searchView = findViewById(R.id.searchView);
        mTodoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);

        tDao.getAll().observe(this, new Observer<List<Memo>>() {
            @Override
            public void onChanged(@Nullable List<Memo> memos) {

                memoArr = memos;
            }
        });

        mTodoViewModel.getAll().observe(this, new Observer<List<Memo>>() {
            @Override
            public void onChanged(@Nullable List<Memo> memos) {

                memoAdapter.setDataList(memos);
                memoListView.setAdapter(memoAdapter);

            }
        });

        // 1. add
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TodolistActivity.this, EditTodoActivity.class);
                i.putExtra("status", ADD_MODE);
                startActivityForResult(i, ADD_MODE);
            }
        });
        // 2. multi delete --- OptionsMenu
        // 3. search --- Text, Voice
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mKeepArr == null) mKeepArr = memoArr;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchIncludeStr(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchIncludeStr(newText);
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                memoArr = mKeepArr;
                mKeepArr = null;
                return true;
            }
        });

        memoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(delMemoArr!=null) delMemoArr = null;

                if(delete_mode) {
                    delMemoArr.add((Memo) memoAdapter.getItem(position));
                } else {
                    Memo m = (Memo) memoAdapter.getItem(position);
                    sel = m.getTitle();
                }
            }
        });

        memoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TodolistActivity.this, EditTodoActivity.class);
                Memo m = (Memo) memoAdapter.getItem(position);
                i.putExtra("mode", VIEW_MODE);
                i.putExtra("title", m.getTitle());
                i.putExtra("contents", m.getContent());
                i.putExtra("date", m.getStDate());
                i.putExtra("stat", m.isDone());
                startActivityForResult(i, VIEW_MODE);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
         memoListView.setAdapter(memoAdapter);
        // memoAdapter.notifyDataSetChanged(); // 변경된 작업 Refresh!!
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, DELETE_MENU_ID, Menu.NONE, R.string.delete_todo)
                .setIcon(android.R.drawable.ic_menu_delete)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        SubMenu subMenu = menu.addSubMenu("Filter");
        subMenu.add(1, FILTER_ALL_ID, Menu.NONE, "All");
        subMenu.add(1, FILTER_DOING_ID, Menu.NONE, "Doing");
        subMenu.add(1, FILTER_DONE_ID, Menu.NONE, "Done");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if(mKeepArr == null) mKeepArr = memoArr;

        switch (item.getItemId()) {

            case FILTER_ALL_ID:
                searchIncludeStr(null);
                memoArr = mKeepArr;
                mKeepArr = null;
                break;
            case FILTER_DOING_ID:
                filterStatus(DOING_STAT);
                break;
            case FILTER_DONE_ID:
                filterStatus(DONE_STAT);
                break;
            case DELETE_MENU_ID:
                memoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(delete_mode == false) {
                            delete_mode = true;
                            item.setIcon(android.R.drawable.ic_menu_set_as);
                        }
                        else if(delete_mode) {
                            delete_mode = false;
                            item.setIcon(android.R.drawable.ic_menu_delete);

                            if(delMemoArr!=null) {
                                for (int i = 0; i < delMemoArr.size(); i++) {
                                    // tDao.delete(delMemoArr.get(i));
                                    mTodoViewModel.delete(delMemoArr.get(i));
                                }
                                delMemoArr = null;
                            }
                        }
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CANCELED) return;

        if(requestCode == ADD_MODE) {
            Toast.makeText(this, "todo added", Toast.LENGTH_SHORT).show();

            Memo m = new Memo(data.getStringExtra("title"),
                    data.getStringExtra("contents"),
                    data.getStringExtra("stDate"),
                    data.getStringExtra("finDate"));
            m.setDone(data.getBooleanExtra("stat", false));

            mTodoViewModel.insert(m);
            // tDao.insert(m);


        } else if(requestCode == VIEW_MODE) {
            Toast.makeText(this, "todo edited", Toast.LENGTH_SHORT).show();

            Memo m = new Memo(data.getStringExtra("title"),
                    data.getStringExtra("contents"),
                    data.getStringExtra("stDate"),
                    data.getStringExtra("finDate"));
            m.setDone(data.getBooleanExtra("stat", false));
            // tDao.update(m);
            mTodoViewModel.update(m);
        }
    }

    public void searchIncludeStr(String str) {
        memoArr = null;

        if(str == null) { // 문자 입력이 없는 상태
            memoArr = mKeepArr;
        } else {
            for(int i=0; i<mKeepArr.size(); i++) {
                if(!mKeepArr.get(i).getTitle().toLowerCase().contains(str))
                    memoArr.add(mKeepArr.get(i));
            }
        }

        memoAdapter.notifyDataSetChanged();
    }

    public void filterStatus(boolean stat) {
        memoArr = null;

        if(stat == DOING_STAT) {
            for(int i=0; i<mKeepArr.size(); i++) {
                if(!mKeepArr.get(i).isDone()) {
                    memoArr.add(mKeepArr.get(i));
                }
            }
        } else {
            for(int i=0; i<mKeepArr.size(); i++) {
                if(mKeepArr.get(i).isDone()) {
                    memoArr.add(mKeepArr.get(i));
                }
            }
        }
    }
}
