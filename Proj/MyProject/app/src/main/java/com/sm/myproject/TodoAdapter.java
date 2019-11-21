package com.sm.myproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class TodoAdapter extends BaseAdapter {

    private int rowLayout;
    private List<Memo> memoList; // Chached copy

    public TodoAdapter(int rowLayout, List<Memo> memoList) {
        this.rowLayout = rowLayout;
        this.memoList = memoList;
    }

    public void setDataList(List<Memo> memoList) {
        this.memoList = memoList;
    }

    @Override
    public int getCount() {
        return memoList == null ? 0 : memoList.size();
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

        Memo sel = memoList.get(position);
        final ViewHolder viewHolder;

        if(convertView == null) {
            convertView = View.inflate(parent.getContext(), rowLayout, null);

            viewHolder = new ViewHolder();

            viewHolder.title = convertView.findViewById(R.id.row_title);
            viewHolder.finDate = convertView.findViewById(R.id.row_date);
            viewHolder.stat = convertView.findViewById(R.id.row_stat);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(sel.getTitle());
        viewHolder.finDate.setText(sel.getStDate());
        viewHolder.stat.setChecked(sel.isDone());

        return convertView;
    }

    class ViewHolder {
        TextView title, finDate;
        CheckBox stat;
    }
}
