package com.sm.testservice;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startService(View view) {
        /*
        Intent i = new Intent(this, MyService.class);
        i.putExtra("url", "https://www.daum.net");
        startService(i);
         */
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.daum.net"));
        ComponentName cpName = new ComponentName("com.sm.testservice", "com.sm.testservice.MyService");
        i.setComponent(cpName);
        startService(i);
    }

    public void stopService(View view) {
        Intent i = new Intent();
        ComponentName cpName = new ComponentName("com.sm.testservice", "com.sm.testservice.MyService");
        i.setComponent(cpName);
        stopService(i);
        /*
        stopService(new Intent(this, MyService.class);
         */
    }
}
