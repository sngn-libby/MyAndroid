package com.sm.myproject;

import android.arch.persistence.room.Room;
import android.content.Intent;
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

import java.util.ArrayList;

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
    ArrayList<Memo> memoArr, memoOrgArr;
    String filterStr, filteringStr;
    String sel;
    MemoAdapter<Memo> memoAdapter;
    FloatingActionButton addFab;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        manager = TodoManager.getInstance();
        memoListView = findViewById(R.id.memoListView);
        memoOrgArr = memoArr = manager.getAllMemo();
        memoAdapter =  new MemoAdapter<>(R.layout.todoli_row_layout,  memoArr);
        addFab = (FloatingActionButton) findViewById(R.id.addFab);
        searchView = findViewById(R.id.searchView);

        // 0. Database
        TodoDatabase db = Room.databaseBuilder(this, TodoDatabase.class, "todo-db")
                .allowMainThreadQueries().build();

         db.todoDao().getAll().toString();

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
        // 4. filter --- OptionsMenu


        memoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo m = manager.getAllMemo().get(position);
                sel = m.getTitle();
            }
        });

        memoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TodolistActivity.this, EditTodoActivity.class);
                Memo m = manager.getAllMemo().get(position);
                i.putExtra("status", VIEW_MODE);
                i.putExtra("title", m.getTitle());
                i.putExtra("contents", m.getContent());
                i.putExtra("date", m.getStDate());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case FILTER_ALL_ID:
                searchIncludeStr(null);
                break;
            case FILTER_DOING_ID:
                filterStatus(DOING_STAT);
                break;
            case FILTER_DONE_ID:
                filterStatus(DONE_STAT);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    class MemoAdapter<M> extends BaseAdapter {

        private int rowLayout;
        private ArrayList<Memo> memoList;

        public MemoAdapter(int rowLayout, ArrayList<Memo> memoList) {
            this.rowLayout = rowLayout;
            this.memoList = memoList;
        }

        @Override
        public int getCount() {
            return memoList.size();
        }

        @Override
        public Object getItem(int position) {
            return memoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Memo selMemo = memoList.get(position);
            final ViewHolder viewHolder;

            if(convertView == null) {
                convertView = View.inflate(TodolistActivity.this, rowLayout, null);

                viewHolder = new TodolistActivity.ViewHolder();
                viewHolder.title = convertView.findViewById(R.id.row_todo);
                viewHolder.finDate = convertView.findViewById(R.id.row_date);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (TodolistActivity.ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(selMemo.getTitle());
            viewHolder.finDate.setText(selMemo.getStDate());


            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CANCELED) return;

        if(requestCode == ADD_MODE) {
            Toast.makeText(this, "memo added", Toast.LENGTH_SHORT).show();
            manager.addMemo(data.getStringExtra("title"),
                    data.getStringExtra("contents"),
                    data.getStringExtra("stDate"),
                    data.getStringExtra("finDate"));


        } else if(requestCode == VIEW_MODE) {
            Toast.makeText(this, "memo edited", Toast.LENGTH_SHORT).show();
            manager.editMemo(data.getStringExtra("title"),
                    data.getStringExtra("contents"),
                    data.getStringExtra("date"),
                    data.getBooleanExtra("stat", false));
        }
    }

    public void searchIncludeStr(String str) {
        memoArr.clear();

        if(str == null) { // 문자 입력이 없는 상태
            memoArr = memoOrgArr;
        } else {
            for(int i=0; i<memoOrgArr.size(); i++) {
                if(memoOrgArr.get(i).getTitle().toLowerCase().contains(str))
                    memoArr.add(memoOrgArr.get(i));
            }
        }

        memoAdapter.notifyDataSetChanged();
    }

    public void filterStatus(boolean stat) {
        memoArr.clear();

        if(stat == DOING_STAT) {
            for(int i=0; i<memoOrgArr.size(); i++) {
                if(!memoOrgArr.get(i).isDone()) {
                    memoArr.add(memoOrgArr.get(i));
                }
            }
        } else {
            for(int i=0; i<memoOrgArr.size(); i++) {
                if(memoOrgArr.get(i).isDone()) {
                    memoArr.add(memoOrgArr.get(i));
                }
            }
        }
    }

    class ViewHolder {
        TextView title, finDate;
    }
}
