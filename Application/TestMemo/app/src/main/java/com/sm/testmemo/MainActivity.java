package com.sm.testmemo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_MODE = 1;
    Button addBtn;
    TextView tv1;
    TextView tv2;
    ArrayList<Memo> memos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.addMemo);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        MemoManager manager = MemoManager.getInstance();
        memos = manager.getAllMemo();
        showAllMemo();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent("com.scsa.android.ADD"), ADD_MODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == ADD_MODE) {
            showAllMemo();
        }
    }

    public void showAllMemo() {
        if(memos.size()==0) return;
        tv1.setText(getMemo(memos.size()).getTitle());
        if(memos.size()>1) tv2.setText(getMemo(memos.size()-1).getTitle());
    }

    public Memo getMemo(int id) {
        for(int i=0; i<memos.size(); i++) {
            if(memos.get(i).getId()==id) return memos.get(i);
        }

        return null;
    }
}
