package com.sm.moviemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView mListV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListV = findViewById(R.id.movieListView);

        MovieAdapter<Movie> movieAdapter = new MovieAdapter<>(R.layout.row_layout, getAllInfo());
        mListV.setAdapter(movieAdapter);
    }

    class MovieAdapter<M> extends BaseAdapter {

        private int layout;
        private ArrayList<Movie> movieList;
        // LayoutInflater layoutInflater;



        public MovieAdapter(int rowLayout, ArrayList<Movie> movieList) {
            this.layout = rowLayout;
            this.movieList = movieList;
        }

        @Override
        public int getCount() {  return movieList.size(); }

        @Override
        public Object getItem(int position) { return movieList.get(position); }

        @Override
        public long getItemId(int position) {  return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            boolean NORM = false;
            Movie selMovie = (Movie) getItem(position);
            ViewHolder viewHolder;

            if(NORM) {
                // normal way
                View nView = View.inflate(MainActivity.this, layout, null);

                TextView titleV = nView.findViewById(R.id.mTitleV);
                TextView contentV = nView.findViewById(R.id.mConV);
                ImageView imgV = nView.findViewById(R.id.mImgV);

                titleV.setText(selMovie.getmTitle());
                contentV.setText(selMovie.getmContent());
                imgV.setImageResource(selMovie.getmImg());

                return nView;

            } else {
                if(convertView == null) {
                    convertView = View.inflate(MainActivity.this, layout, null);

                    viewHolder = new ViewHolder();
                    viewHolder.mTv = convertView.findViewById(R.id.mTitleV);
                    viewHolder.mConTv = convertView.findViewById(R.id.mConV);
                    viewHolder.mImgV = convertView.findViewById(R.id.mImgV);
                    convertView.setTag(viewHolder);

                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

//                TextView titleV = convertView.findViewById(R.id.mTitleV);
//                TextView contentV = convertView.findViewById(R.id.mConV);
//                ImageView imgV = convertView.findViewById(R.id.mImgV);

                viewHolder.mTv.setText(selMovie.getmTitle());
                viewHolder.mConTv.setText(selMovie.getmContent());
                viewHolder.mImgV.setImageResource(selMovie.getmImg());

                return convertView;
            }
        }
    }

    public ArrayList<Movie> getAllInfo() {
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Miss Sloane", "Lobbyist change Constitution", R.drawable.misssloane));
        movies.add(new Movie("Dunkirk", "Last Battle", R.drawable.dunkirk));
        movies.add(new Movie("Inception", "Dream Seeker", R.drawable.inception));
        movies.add(new Movie("Interstellar", "Lego Dimension and Past", R.drawable.interstellar));
        movies.add(new Movie("82 Kimyjiyoung", "Kimjiyoung's life", R.drawable.kimjiyoung));

        movies.add(new Movie("Miss Sloane", "Lobbyist change Constitution", R.drawable.misssloane));
        movies.add(new Movie("Dunkirk", "Last Battle", R.drawable.dunkirk));
        movies.add(new Movie("Inception", "Dream Seeker", R.drawable.inception));
        movies.add(new Movie("Interstellar", "Lego Dimension and Past", R.drawable.interstellar));
        movies.add(new Movie("82 Kimyjiyoung", "Kimjiyoung's life", R.drawable.kimjiyoung));

        return movies;
    }

    class ViewHolder {
        TextView mTv, mConTv;
        ImageView mImgV;
    }

}
