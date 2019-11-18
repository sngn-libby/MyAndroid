package com.sm.testnetasync;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText urlTx;
    TextView dispTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlTx = findViewById(R.id.urlTx);
        dispTv = findViewById(R.id.displayTv);

    }

    public void search(View view) {
        String url = urlTx.getText().toString();
        new DataDownTask().execute(url);
        Toast.makeText(this, url + "search just started", Toast.LENGTH_SHORT).show();
    }

    class DataDownTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            try {

                URL myUrl = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) myUrl.openConnection();
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(
                                        httpURLConnection.getInputStream()));
                String line;
                while((line = br.readLine()) != null){
                    sb.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // dispTv.setText("");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            dispTv.append(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dispTv.setText(s);
            Toast.makeText(MainActivity.this, urlTx.getText().toString()+" searched", Toast.LENGTH_SHORT).show();
        }
    }


}
