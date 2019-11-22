package edu.jaen.android.storage.notepad3;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;


public class NewsReader extends AppCompatActivity {

    static final int NEWS_FILTER = 0;
    static final int NEWS_COUNT = 30;

    AlertDialog filterD;
    public static boolean [] newsFilter = new boolean[] {true, true,true,true};
    static final String [] newsName = new String [] { "시사인", "조선일보", "미디어오늘", null};

    ListView newsListV;
    NewsLoadingActivity NewsMgr;
    NewsAdapter<News> NewsA;
    ArrayList<News> viewNews;
    ArrayList<ArrayList<News>> allNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_reader);
        //Log.i("info", "제발");

        NewsMgr = NewsLoadingActivity.getInstance();
        try {
            allNews = NewsMgr.getNewsList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getViewNews();
        newsListV = findViewById(R.id.newsList);
        NewsA = new NewsAdapter<News>(R.layout.news_row, viewNews);

        newsListV.setAdapter(NewsA);
        newsListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = (News)NewsA.getItem(position);
                Uri newsUri = Uri.parse(news.getLink());
                Intent i = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(i);
            }
        });

    }

    private void getViewNews() {
        viewNews = new ArrayList<News>();

        for (int i = 0; i < NEWS_COUNT;) {
            if (newsFilter[i % 4]) {
                // 참일 떄
                int len = allNews.get(i%4).size();
                if (len == 0) continue;
                News tmp = allNews.get(i%4).remove(0);
                if (tmp == null) continue;
                viewNews.add(tmp);
                i++;
            }
            else i++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, NEWS_FILTER, Menu.NONE, "add")
                .setIcon(R.drawable.filtericon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == NEWS_FILTER) {

            showDialog(0);
            return true;
        }

        return false;
    }

    protected Dialog onCreateDialog(int id) {
        return new AlertDialog.Builder(this).setIcon(R.drawable.news).setTitle("언론사 선택")
                .setMultiChoiceItems(R.array.newspaper_filters, newsFilter, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                }).setPositiveButton("설정 적용", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("info", "대체 무슨일이 " + which);

                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
//                        for (int i = 0; i <4; i++) {
//                            Log.i("어떻게", "+" +newsFilter[i]);
//                        }
                        try {
                            allNews = NewsMgr.getNewsList();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        getViewNews();
                        NewsA = new NewsAdapter<News>(R.layout.news_row, viewNews);
                        newsListV.setAdapter(NewsA);
                        //Log.i("어떻게", "ㅏㅏ");
                    }
                }).create();
    }



    class NewsAdapter<n> extends BaseAdapter {

        private int layout;
        private ArrayList<News> newsList;
        //News selNews;

        public NewsAdapter(int Layout, ArrayList<News> newsList){

            this.layout = Layout;
            this.newsList = newsList;
            //this.selNews = new News();
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
            ViewHolder viewHolder;
            //Log.i("info", "몇번이나 될까" + position);
            if (convertView == null) {
                convertView = View.inflate(NewsReader.this, layout, null);
                viewHolder = new ViewHolder();
                viewHolder.titleTv = convertView.findViewById(R.id.nTitleTv);
                viewHolder.nameTv = convertView.findViewById(R.id.newsTv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            News tmp = (News)getItem(position);
            if (tmp != null) {
                viewHolder.titleTv.setText(tmp.getTitle());
                viewHolder.nameTv.setText(tmp.toString());
            }
            return convertView;
        }
    }

    class ViewHolder {
        TextView titleTv;
        TextView nameTv;
    }

}
