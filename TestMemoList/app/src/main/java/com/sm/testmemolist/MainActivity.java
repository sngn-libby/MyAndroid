package com.sm.testmemolist;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_MODE = 1;
    public static final int DELETE_MODE = 2;
    public static final int VIEW_MODE = 3;

    ListView memoListView;
    Button addBtn, deleteBtn;
    MemoManager manager;
    MemoAdapter<Memo> memoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = MemoManager.getInstance();
        memoListView = findViewById(R.id.memoListView);
        addBtn = findViewById(R.id.addBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        memoAdapter = new MemoAdapter<>(R.layout.row_layout, manager.getAllMemo());
        memoListView.setAdapter(memoAdapter);

        memoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditMemoActivity.class);
                i.putExtra("status", VIEW_MODE);
                startActivityForResult(i, VIEW_MODE);
                return false;
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EditMemoActivity.class);
                i.putExtra("status", ADD_MODE);
                startActivityForResult(i, ADD_MODE);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Memo m = (Memo) memoListView.getSelectedItem(); // check
                if(manager.deleteMemo(m.getTitle()))
                    Toast.makeText(MainActivity.this, "memo deleted", Toast.LENGTH_SHORT).show();
            }
        });
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
            Toast.makeText(MainActivity.this, selMemo.toString(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MainActivity.this, selMemo.getTitle(), Toast.LENGTH_SHORT).show();
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

        } else if(requestCode == VIEW_MODE) {
            Toast.makeText(this, "memo edited", Toast.LENGTH_SHORT).show();
        }

        memoListView.setAdapter(memoAdapter);
    }

    class ViewHolder {
        TextView title, date;
    }
}
