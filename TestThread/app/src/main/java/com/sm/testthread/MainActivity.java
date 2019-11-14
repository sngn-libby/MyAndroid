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
    private static final boolean MSGTEST = false;
    private static final boolean RUNTEST = false;


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
                final String sendData = "Back : " + (++timerVal);
                if(MSGTEST) {
                    /* 1. Send Message type */
                    Message msg = myHandler.obtainMessage(1, sendData);
                    myHandler.sendMessage(msg); // Looper가 Q에 데이터를 넣어준다.
                } else if(RUNTEST) { /* 2. Send Runnable type --> MainThread가 실행시킨다 */
                    myHandler.post(new Runnable() {
                        final String data = sendData;
                        @Override
                        public void run() {
                            backTv.setText(data);
                        }
                    });
                } else { /* 3. runOnUiThread - 1과 2중 선택해서 돌려버림 */
                    runOnUiThread(new Runnable() {
                        final String data = sendData;
                        @Override
                        public void run() {
                            backTv.setText(data);
                        }
                    });
                }
                super.run();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Handler myHandler = new Handler() {
//            MSGTEST == true 일 때 주석 해제하기
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            String sendData = msg.obj.toString();
//            backTv.setText(sendData);
//        }
    };

}
