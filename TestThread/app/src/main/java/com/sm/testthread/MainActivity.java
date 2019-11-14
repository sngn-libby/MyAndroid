package com.sm.testthread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    TextView mainTv;
    TextView backTv;
    Button button;
    private static int mainVal = 0;
    private static int timerVal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTv = findViewById(R.id.mainTv);
        backTv = findViewById(R.id.backTv);
        button = findViewById(R.id.button);

        new MyNetworkThread().start();
    }

    public void addNum(View view) {
        // 버튼 클릭시 +1씩 증가시켜 값을 TextView(mainTv)에 반영
        mainTv.setText("Main : "+(++mainVal));
    }

    class MyNetworkThread extends Thread {
        // 1 ~ 10 까지 1초마다 +1씩 증가시키면서 해당 값을 TextView 반영
        @Override
        public void run() { // Thread
            for(;;) {
                // backTv.setText("Back : " + (++timerVal));
                String sendData = "Back : " + (++timerVal);
                Message msg = myHandler.obtainMessage(1, sendData);
                myHandler.sendMessage(msg); // Looper가 Q에 데이터를 넣어준다.
                super.run();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Handler myHandler = new Handler(){ // handler
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String sendData = msg.obj.toString();
            backTv.setText(sendData);
        }
    };
}
