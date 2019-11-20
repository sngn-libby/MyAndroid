package edu.scsa.android.cardviewtest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    ListView newsListV;
    NewsAdapter<News> newsA;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsListV = findViewById(R.id.newsListV);
        queue = Volley.newRequestQueue(this);

        getNews();

        newsListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        String url = "https://newsapi.org/v2/top-headlines?country=kr&apiKey=f7431ddcf5904b608b3f7d04ea366d8c";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //json 파일이 온다
                        //Log.d("NEWS", response);

                        try {
                            ArrayList<News> newsList = new ArrayList<News>();

                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray jsonArr = jsonObj.getJSONArray("articles");
                            for (int i=0; i< jsonArr.length(); i++) {
                                JSONObject obj = jsonArr.getJSONObject(i);
                                News newsData = new News();
                                newsData.setTitle(obj.getString("title"));
                                newsData.setContent(obj.getString("description"));
                                newsData.setUrl(obj.getString("url"));
                                newsData.setImg(obj.getString("urlToImage"));

                                Log.d("NEWS", obj.toString());

                                newsList.add(newsData);
                            }

                            newsA = new NewsAdapter(R.layout.news_layout, newsList);
                            newsListV.setAdapter(newsA);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    class NewsAdapter<N> extends BaseAdapter {

        private int rowLayout;
        private ArrayList<News> newsList;

        public NewsAdapter(int rowLayout, ArrayList<News> newsList) {
            this.rowLayout = rowLayout;
            this.newsList = newsList;
            Fresco.initialize(MainActivity.this);
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
                convertView = View.inflate(MainActivity.this, rowLayout, null);
            }

            News selNews = (News) getItem(position);
            TextView title = convertView.findViewById(R.id.titleTv);
            TextView content = convertView.findViewById(R.id.contentTv);
            title.setText(selNews.getTitle());
            content.setText(selNews.getContent());

            Uri uri = Uri.parse(selNews.getImg());
            SimpleDraweeView draweeView = convertView.findViewById(R.id.newsImg);
            draweeView.setImageURI(uri);

            return convertView;
        }
    }
}
