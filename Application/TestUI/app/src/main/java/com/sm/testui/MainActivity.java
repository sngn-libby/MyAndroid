package com.sm.testui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button Btn, nestBtn, anonBtn; // Handler 등록

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Btn = findViewById(R.id.Btn);
        nestBtn = findViewById(R.id.nestBtn);
        anonBtn = findViewById(R.id.anonBtn);

        Btn.setOnClickListener(this); // 자기자신이 Handler가 된다.
        nestBtn.setOnClickListener(new MyClickHandler());
        anonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "anonymous Button", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "nested Button", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "just Button", Toast.LENGTH_SHORT).show();
    }
}
