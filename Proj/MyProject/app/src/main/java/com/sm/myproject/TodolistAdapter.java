package com.sm.myproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class TodolistAdapter extends RecyclerView.Adapter<TodolistAdapter.TodoViewHolder> {

    private final LayoutInflater mInflater;
    private List<Memo> memoList; // Chached copy

    public TodolistAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {

        private final TextView dateTv, titleTv;
        private final CheckBox statBox;

        private TodoViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTv = itemView.findViewById(R.id.row_date);
            titleTv = itemView.findViewById(R.id.row_title);
            statBox = itemView.findViewById(R.id.row_stat);
        }
    }

    public void setDataList(List<Memo> memos) {
        memoList = memos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.todoli_row_layout, parent, false);
        return new TodoViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        if(memoList != null) {
            try {
                Memo m = memoList.get(position);

                holder.titleTv.setText(m.getTitle());
                holder.dateTv.setText(m.getFinDate());
                holder.statBox.setActivated(m.isDone());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return memoList == null ? 0 : memoList.size();
    }
}
