package com.sm.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;


public class A1_Activity extends AppCompatActivity {

    TextView infoTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a1_);
        infoTv = findViewById(R.id.infoTv);

        Intent recInt = getIntent();
        String path = recInt.getStringExtra("path");
        infoTv.setText("Recieved Data : " + path);
    }



    public void mainCall(View view) {
        Toast.makeText(this, "Main Call", Toast.LENGTH_SHORT).show();
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);

//        finish(); // back 키와 같은 효과
    }
}
