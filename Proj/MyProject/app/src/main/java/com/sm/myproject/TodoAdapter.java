package com.sm.myproject;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.List;

public class TodoAdapter extends BaseAdapter {

    private int rowLayout;
    private List<Todo> todoList; // Chached copy

    public TodoAdapter(int rowLayout, List<Todo> todoList) {
        this.rowLayout = rowLayout;
        this.todoList = todoList;
    }

    public void setDataList(List<Todo> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return todoList == null ? 0 : todoList.size();
    }

    @Override
    public Object getItem(int position) {
        return todoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Todo sel = todoList.get(position);
        final ViewHolder viewHolder;

        if(convertView == null) {
            convertView = View.inflate(parent.getContext(), rowLayout, null);

            viewHolder = new ViewHolder();

            viewHolder.title = convertView.findViewById(R.id.row_title);
            viewHolder.date = convertView.findViewById(R.id.row_date);
            viewHolder.stat = convertView.findViewById(R.id.row_stat);
            viewHolder.delete = convertView.findViewById(R.id.row_delete);


            viewHolder.title.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.e("INFO", "title selected "+ position + " mId: "+TodolistActivity.mId);
                    if(TodolistActivity.mId == -1 && TodolistActivity.DELETE_MODE == false)
                        TodolistActivity.mId = (int)((Todo) getItem(position)).getId();
                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("INFO", "delete selected "+ position);
                    if(TodolistActivity.DELETE_MODE == false) {

                    } else {
                        if(TodolistActivity.mId == -1)
                            TodolistActivity.mId = (int) ((Todo) getItem(position)).getId();
                    }
                }
            });

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(sel.getTitle());
        viewHolder.date.setText(sel.getFinDate());
        viewHolder.stat.setChecked(sel.isDone());

        return convertView;
    }

    class ViewHolder {
        TextView title, date;
        CheckBox stat;
        ImageView delete;
    }
}
