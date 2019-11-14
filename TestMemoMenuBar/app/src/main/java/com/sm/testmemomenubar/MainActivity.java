package com.sm.testmemomenubar;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_MODE = 1;
    public static final int DELETE_MODE = 2;
    public static final int VIEW_MODE = 3;

    private static final int ADD_MENU_ID = Menu.FIRST;
    private static final int DELETE_MENU_ID = Menu.FIRST + 1;
    private static final int OPTION_MENU_ID = Menu.FIRST + 2;

    ListView memoListView;
    com.sm.testmemomenubar.MemoManager manager;
    ArrayList<com.sm.testmemomenubar.Memo> memoArr;
    String sel;
    MemoAdapter<com.sm.testmemomenubar.Memo> memoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = com.sm.testmemomenubar.MemoManager.getInstance();
        memoListView = findViewById(R.id.memoListView);
        memoArr = manager.getAllMemo();
        memoAdapter = new MemoAdapter<>(R.layout.row_layout,  memoArr);

        memoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                com.sm.testmemomenubar.Memo m = manager.getAllMemo().get(position);
                sel = m.getTitle();
            }
        });

        memoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditMemoActivity.class);
                com.sm.testmemomenubar.Memo m = manager.getAllMemo().get(position);
                i.putExtra("status", VIEW_MODE);
                i.putExtra("title", m.getTitle());
                i.putExtra("contents", m.getContent());
                i.putExtra("date", m.getDate());
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
        menu.add(0, ADD_MENU_ID, Menu.NONE, R.string.add_menu)
                .setIcon(android.R.drawable.ic_menu_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(1, DELETE_MENU_ID, Menu.NONE, R.string.delete_menu)
                .setIcon(android.R.drawable.ic_menu_delete)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(2, OPTION_MENU_ID, Menu.NONE, R.string.version_menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == ADD_MENU_ID) {
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent i = new Intent(MainActivity.this, EditMemoActivity.class);
                    i.putExtra("status", ADD_MODE);
                    startActivityForResult(i, ADD_MODE);
                    return true;
                }
            });
        }
        else if(item.getItemId() == DELETE_MENU_ID) {
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(manager.deleteMemo(sel))
                        memoAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "memo deleted", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
        else if(item.getItemId() == OPTION_MENU_ID) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Version Info");

            int versionCode = BuildConfig.VERSION_CODE;
            String versionName = BuildConfig.VERSION_NAME;
            String ver = "Version Code  =" + versionCode +
                    "\nVersion Name =" + versionName;

            dialog.setMessage(ver);
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    class MemoAdapter<M> extends BaseAdapter {

        private int rowLayout;
        private ArrayList<com.sm.testmemomenubar.Memo> memoList;

        public MemoAdapter(int rowLayout, ArrayList<com.sm.testmemomenubar.Memo> memoList) {
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

            com.sm.testmemomenubar.Memo selMemo = memoList.get(position);
            final ViewHolder viewHolder;

            if(convertView == null) {
                convertView = View.inflate(MainActivity.this, rowLayout, null);

                viewHolder = new ViewHolder();
                viewHolder.title = convertView.findViewById(R.id.row_title);
                viewHolder.date = convertView.findViewById(R.id.row_date);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(selMemo.getTitle());
            viewHolder.date.setText(selMemo.getDate());


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
                    data.getStringExtra("date"));


        } else if(requestCode == VIEW_MODE) {
            Toast.makeText(this, "memo edited", Toast.LENGTH_SHORT).show();
            manager.editMemo(data.getStringExtra("title"),
                    data.getStringExtra("contents"),
                    data.getStringExtra("date"));
        }
    }

    class ViewHolder {
        TextView title, date;
    }
}
