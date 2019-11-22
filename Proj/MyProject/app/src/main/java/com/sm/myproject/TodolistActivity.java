package com.sm.myproject;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
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

    private static final String NFC_TAG = "database";


    public static int mId = -1;
    ListView memoListView;
    CheckBox statBox;
    TextView titleTv;
    List<Todo> todoArr, mTodoArr, mKeepArr, delTodoArr;
    TodoAdapter memoAdapter;
    FloatingActionButton addFab;
    SearchView searchView;

    private TodoViewModel mTodoViewModel;

    public static boolean DELETE_MODE = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        memoAdapter = new TodoAdapter(R.layout.todoli_row_layout, todoArr);
        // manager = TodoManager.getInstance();
        memoListView = findViewById(R.id.memoListView);
        addFab = (FloatingActionButton) findViewById(R.id.addFab);
        searchView = findViewById(R.id.searchView);
        mTodoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        titleTv = findViewById(R.id.row_title);
        statBox = findViewById(R.id.row_stat);


        mTodoViewModel.getAll().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(@Nullable List<Todo> todos) {

                memoAdapter.setDataList(todos);
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
                if(mKeepArr == null) mKeepArr = todoArr;
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
                todoArr = mKeepArr;
                mKeepArr = null;
                return true;
            }
        });

        /*
        memoListView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("INFO", "selected\nmode: "+DELETE_MODE);

                if(DELETE_MODE) { // delete

                    Todo m = mTodoViewModel.getItem(mId);
                    if(mId == -1 ) {}
                    else if(m != null) {
                        mTodoViewModel.delete(mTodoViewModel.getItem(mId));
                    }

                } else { // edit
                    if(mId == -1) {}
                    else {
                        Intent i = new Intent(TodolistActivity.this, EditTodoActivity.class);
                        Todo m = (Todo) memoAdapter.getItem(mId);
                        i.putExtra("mode", VIEW_MODE);
                        i.putExtra("title", m.getTitle());
                        i.putExtra("contents", m.getContent());
                        i.putExtra("date", m.getStDate());
                        i.putExtra("stat", m.isDone());
                        i.putExtra("id", mId);
                        startActivityForResult(i, VIEW_MODE);
                        mId = -1;
                    }
                }

                return true;
            }
        });

        */
        memoListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("INFO", "selected\nmode: "+DELETE_MODE);

                if(DELETE_MODE) { // delete

                    if(mId == 0) mId = 1;
                    Todo m = mTodoViewModel.getItem(mId-1);
                    if(mId == -1 ) {}
                    else if(m != null) {
                        mTodoViewModel.delete(mTodoViewModel.getItem(mId));
                    }

                } else { // edit
                    if(mId == -1) {}
                    else {
                        Intent i = new Intent(TodolistActivity.this, EditTodoActivity.class);
                        Todo m = (Todo) memoAdapter.getItem(mId-1);
                        i.putExtra("mode", VIEW_MODE);
                        i.putExtra("title", m.getTitle());
                        i.putExtra("contents", m.getContent());
                        i.putExtra("finDate", m.getFinDate());
                        i.putExtra("stat", m.isDone());
                        i.putExtra("id", mId);
                        startActivityForResult(i, VIEW_MODE);

                    }
                }

                mId = -1;
                return true;
            }
        });


        /*
        Intent i = getIntent();
        if(i!=null && i.getStringExtra("initialize").equals(NFC_TAG))
        {
            Toast.makeText(this, "Start Clear", Toast.LENGTH_SHORT).show();
            mTodoViewModel.deleteAll();
            setResult(20);
            finish();
        }
        */
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
                .setIcon(R.drawable.ic_delete_black_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        SubMenu subMenu = menu.addSubMenu("Filter");
        subMenu.add(1, FILTER_ALL_ID, Menu.NONE, "All");
        subMenu.add(1, FILTER_DOING_ID, Menu.NONE, "Doing");
        subMenu.add(1, FILTER_DONE_ID, Menu.NONE, "Done");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if(mKeepArr == null) mKeepArr = todoArr;
        Log.e("INFO", "option selected "+item.getItemId());
        switch (item.getItemId()) {

            case FILTER_ALL_ID:
                searchIncludeStr(null);
                todoArr = mKeepArr;
                mKeepArr = null;
                break;
            case FILTER_DOING_ID:
                filterStatus(DOING_STAT);
                break;
            case FILTER_DONE_ID:
                filterStatus(DONE_STAT);
                break;
            case DELETE_MENU_ID:
                if(DELETE_MODE == false) {
                    DELETE_MODE = true;
                    Log.e("INFO", "iconRefredshed");
                    item.setIcon(R.drawable.ic_check_black_24dp);
                }
                else if(DELETE_MODE) {
                    DELETE_MODE = false;
                    mId = -1;

                    item.setIcon(R.drawable.ic_delete_black_24dp);

                    if(delTodoArr !=null) {
                        for (int i = 0; i < delTodoArr.size(); i++) {
                            mTodoViewModel.delete(delTodoArr.get(i));
                        }
                        delTodoArr = null;
                    }
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CANCELED) return;

        Todo m = new Todo(data.getStringExtra("title"),
                data.getStringExtra("contents"),
                data.getStringExtra("stDate"),
                data.getStringExtra("finDate"));
        m.setDone(data.getBooleanExtra("stat", false));

        if(requestCode == ADD_MODE) {
            Toast.makeText(this, "todo added", Toast.LENGTH_SHORT).show();
            mTodoViewModel.insert(m);

        } else if(requestCode == VIEW_MODE) {
            m.setId(data.getIntExtra("id", 0));
            Toast.makeText(this, "todo edited", Toast.LENGTH_SHORT).show();
            mTodoViewModel.update(m);
        }
    }

    public void searchIncludeStr(String str) {
        todoArr = null;

        if(str == null) { // 문자 입력이 없는 상태
            todoArr = mKeepArr;
        } else {
            for(int i=0; i<mKeepArr.size(); i++) {
                if(!mKeepArr.get(i).getTitle().toLowerCase().contains(str))
                    todoArr.add(mKeepArr.get(i));
            }
        }

        memoAdapter.notifyDataSetChanged();
    }

    public void filterStatus(boolean stat) {
        todoArr = null;

        if(stat == DOING_STAT) {
            for(int i=0; i<mKeepArr.size(); i++) {
                if(!mKeepArr.get(i).isDone()) {
                    todoArr.add(mKeepArr.get(i));
                }
            }
        } else {
            for(int i=0; i<mKeepArr.size(); i++) {
                if(mKeepArr.get(i).isDone()) {
                    todoArr.add(mKeepArr.get(i));
                }
            }
        }
    }
}
