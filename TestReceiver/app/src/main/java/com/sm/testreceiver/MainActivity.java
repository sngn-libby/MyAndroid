package com.sm.testreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    IntentFilter filter;
    MyReceiver myReceiver; // 수신객체 직접 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED"); // 받고싶은 BroadCast name
        // 1:N을 통신을 위해 filter추가하기
        filter.addAction("edu.scsa.action.Connection_Fail");

        myReceiver = new MyReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // BR 등록
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // BR 해지
        unregisterReceiver(myReceiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            startActivity(i);

            String action = intent.getAction();
            if(action.equals("edu.scsa.action.Connection_Fail")) {

            }
            else if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {

            }
        }
    }
}
