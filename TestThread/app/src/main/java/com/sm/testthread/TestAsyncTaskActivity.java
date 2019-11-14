package com.sm.testthread;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestAsyncTaskActivity extends AppCompatActivity {

    TextView mainTv;
    TextView backTv;
    Button button;
    private static int mainVal = 0;
    private static int timerVal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_async_task);

        mainTv = findViewById(R.id.mainTv);
        backTv = findViewById(R.id.backTv);
        button = findViewById(R.id.button);

        mainTv.setText("Main : 0");
        backTv.setText("Back : 0");

        new MyCountAsyncTask().execute(20);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainTv.setText("Main : 0");
        backTv.setText("Back : 0");
        mainVal = timerVal = 0;
    }

    public void addNum(View view) {
        // 버튼 클릭시 +1씩 증가시켜 값을 TextView(mainTv)에 반영
        mainTv.setText("Main : "+(++mainVal));
    }

    class MyCountAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute() { // 생성자 역할 (1)
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) { // 진행상황표출 (퍼블리싱패턴필요)
        // (2)
            Log.i("INFO", "doInBack "+Thread.currentThread().getName());
            for(timerVal = 1; timerVal <= integers[0]; timerVal++) {
                publishProgress(timerVal);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return integers[0];
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // 작업상황 업데이트
            super.onProgressUpdate(values);
            backTv.setText("Back : "+values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) { // 결과값 사용자에게 알려주기 위해
            super.onPostExecute(integer);
            Toast.makeText(TestAsyncTaskActivity.this, "Back result : "+integer, Toast.LENGTH_SHORT).show();
        }
    }
}
