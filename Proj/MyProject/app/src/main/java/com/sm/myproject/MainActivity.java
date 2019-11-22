package com.sm.myproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView mainTv, infoTv;
    Button todoliBtn, newspressoBtn, candypangBtn, pickfoodBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTv = findViewById(R.id.mainTv);
        infoTv = findViewById(R.id.infoTv);
        todoliBtn = findViewById(R.id.todoliBtn);
        newspressoBtn = findViewById(R.id.newspressoBtn);
        candypangBtn = findViewById(R.id.candypangBtn);
        pickfoodBtn = findViewById(R.id.pickfoodBtn);
    }

    public void todoliStart(View view) {
        Intent i = new Intent(MainActivity.this, TodolistActivity.class);
        startActivity(i);
    }

    public void newspressoStart(View view) {
        Intent i = new Intent(MainActivity.this, NewspressoActivity.class);
        startActivity(i);
    }

    public void candypangStart(View view) {
        Intent i = new Intent(MainActivity.this, WhackamoleActivity.class);
        startActivity(i);
    }

    public void pickfoodStart(View view) {
        Intent i = new Intent(MainActivity.this, InitializeActivity.class);
        startActivity(i);
    }
}
