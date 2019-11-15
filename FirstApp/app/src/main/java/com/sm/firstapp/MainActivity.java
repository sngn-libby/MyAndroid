package com.sm.firstapp;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int curPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this,"Main onCreate", Toast.LENGTH_SHORT).show();
        Log.i("INFO", "Main OnCreate");
    }
    /*
    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this,"Main onRestart", Toast.LENGTH_SHORT).show();
        Log.i("INFO", "Main onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this,"Main onStart", Toast.LENGTH_SHORT).show();
        Log.i("INFO", "Main onStart");
    }
    */

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"Main onResume", Toast.LENGTH_SHORT).show();
        Log.i("INFO", "Main onResume");
        curPage = getSharedPreferences("myData", MODE_PRIVATE).getInt("page", 0);
        Toast.makeText(this, "current Page : "+curPage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        getSharedPreferences("myData", MODE_PRIVATE)
                .edit()
                .putInt("page", curPage+=7)
                .commit();
        Toast.makeText(this,"Page Info Saved - "+curPage, Toast.LENGTH_SHORT).show();
        Log.i("INFO", "Main onPause");
    }

    /*
    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this,"Main onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Main onDestroy", Toast.LENGTH_SHORT).show();
    }
    */

    public void a1Call(View view) {
        // 정적구성
        // 같은 프로세스(같은앱)에서 다른 컴포넌트 **명시적** 호출 (다른 프로세스 호출 불가)
        Toast.makeText(this, "a1 Call", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, A1_Activity.class);
        i.putExtra("path", "mySDcard/myData.dat");

        startActivity(i); // 페이지전환 component 호출 <==> 보내기
    }

    public void a2Call(View view) {
        // 다른 앱(프로세스)에서 컴포넌트 사용자 정의 방법으로 **묵시적**으로 호출하기 (SecondApp 호출)
        //Intent i2 = new Intent("com.sm.action.EDIT"); // 사용자정의 action (여기서는 수정작업을 하는 Activity호출하기)
        // 위를 **명시적**으로 호출하기
        ComponentName cpName = new ComponentName("com.sm.secondapp", "com.sm.secondapp.TestActivity");
        Intent i2 = new Intent();
        i2.setComponent(cpName);
        startActivity(i2);
    }

    public void a3Call(View view) {
        // 묵시적 방법 : 이미 정해져 있는 액션 호출
        Toast.makeText(this, "Call Map", Toast.LENGTH_SHORT).show();
        // Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.naver.com"));
        Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse("geo"));
        Intent i4 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:01043231111"));
        Intent i5 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent i6 = new Intent(Intent.ACTION_PICK);
        i6.setType("image/*");

        startActivity(i6);
    }
}
