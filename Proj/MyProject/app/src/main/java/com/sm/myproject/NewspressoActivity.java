package com.sm.myproject;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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

public class NewspressoActivity extends AppCompatActivity {


    private RecyclerView newsBoard;
    private NewsAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspresso);

        newsBoard = findViewById(R.id.newsBoard);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        newsBoard.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        newsBoard.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        queue = Volley.newRequestQueue(this);
        getNews();
    }

    public void getNews() {
        // Instantiate the RequestQueue.
        String url ="https://newsapi.org/v2/top-headlines?country=kr&apiKey=56194bb553a14ed2ba63c9ebc33401fc";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // Log.d("NEWS", response);

                        try {
                            ArrayList<NewsData> newsList = new ArrayList<>();

                            JSONObject jObj = new JSONObject(response);
                            JSONArray arrArticle = jObj.getJSONArray("articles");
                            for(int i=0; i<arrArticle.length(); i++) {
                                JSONObject obj = arrArticle.getJSONObject(i);
                                NewsData newsData = new NewsData();

                                newsData.setTitle(obj.getString("title"));
                                newsData.setContents(obj.getString("content"));
                                newsData.setUrlToImage(obj.getString("urlToImage"));
                                newsList.add(newsData);

                            }

                            mAdapter = new NewsAdapter(newsList, NewspressoActivity.this);
                            newsBoard.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
