package com.sm.myproject;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewspressoActivity extends AppCompatActivity {


    private ListView newsBoard;
    private NewsAdapter mAdapter;
    RequestQueue queue;
    List<NewsData> newsList;
    StringRequest stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspresso);

        newsBoard = findViewById(R.id.newsBoard);

        queue = Volley.newRequestQueue(this);
        getNews();

        newsBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = parent.getAdapter().getItem(position).toString();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            }
        });
    }

    public void getNews() {
        // Instantiate the RequestQueue.
        String url ="https://newsapi.org/v2/top-headlines?country=kr&apiKey=56194bb553a14ed2ba63c9ebc33401fc";

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            newsList = new ArrayList<>();

                            JSONObject jObj = new JSONObject(response);
                            JSONArray arrArticle = jObj.getJSONArray("articles");
                            for(int i=0; i<arrArticle.length(); i++) {
                                JSONObject obj = arrArticle.getJSONObject(i);
                                NewsData newsData = new NewsData();

                                newsData.setTitle(obj.getString("title"));
                                newsData.setContents(obj.getString("description"));
                                newsData.setUrl(obj.getString("url"));
                                newsData.setUrlToImage(obj.getString("urlToImage"));
                                newsList.add(newsData);

                                mAdapter = new NewsAdapter(newsList);
                                newsBoard.setAdapter(mAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    class NewsAdapter<N> extends BaseAdapter {

        private List<NewsData> newsList;

        public NewsAdapter(List<NewsData> newsList) {
            this.newsList = newsList;
            Fresco.initialize(NewspressoActivity.this);
        }

        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public Object getItem(int position) {
            return newsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(NewspressoActivity.this, R.layout.news_row_layout, null);
            }

            NewsData news = (NewsData) getItem(position);
            TextView title = convertView.findViewById(R.id.titleTv);
            TextView content = convertView.findViewById(R.id.contentsTv);
            title.setText(news.getTitle());
            content.setText(news.getContents());

            Uri uri = Uri.parse(news.getUrlToImage());
            SimpleDraweeView draweeView = convertView.findViewById(R.id.draweeView);
            draweeView.setImageURI(uri);

            return convertView;
        }
    }
}
