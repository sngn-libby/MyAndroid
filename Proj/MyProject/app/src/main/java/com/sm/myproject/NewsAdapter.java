package com.sm.myproject;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private ArrayList<NewsData> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView titleTv;
        public TextView contentsTv;
        public ImageView imageView;


        public MyViewHolder(LinearLayout v) {
            super(v);
            titleTv = v.findViewById(R.id.titleTv);
            contentsTv = v.findViewById(R.id.contentsTv);
            // imageView = v.findViewById(R.id.imageView);
            SimpleDraweeView imageView = v.findViewById(R.id.imageView);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
//    public NewsAdapter(ArrayList<NewsData> myDataset, Context context) {
//        Fresco.initialize(context);
//        mDataset = myDataset;
//    }

    public NewsAdapter(ArrayList<NewsData> myDataset, Context context) {
        this.mDataset = myDataset;
        Fresco.initialize(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_row_layout, parent, false);

        NewsAdapter.MyViewHolder vh = new NewsAdapter.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NewsAdapter.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // holder.textView.setText(mDataset[position]);
        NewsData news = mDataset.get(position);

        holder.titleTv.setText(news.getTitle());
        String content = news.getContents();
        if(content!=null && content.length()>0)
            holder.contentsTv.setText(content);
        else holder.contentsTv.setText("");

        Uri uri = Uri.parse(news.getUrlToImage());
        holder.imageView.setImageURI(uri);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mDataset == null) return 0;
        return mDataset.size();
    }

}
