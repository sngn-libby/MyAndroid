package com.sm.test4result;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView infoTv;
    public static final int EDIT_MODE = 1;
    public static final int SEARCH_MODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoTv = findViewById(R.id.infoTv);
    }

    public void edit(View view) {
        Intent i = new Intent("com.sm.android.section.EDIT");

        String str = (String) infoTv.getText();
        i.putExtra("title", str);
        startActivityForResult(i, EDIT_MODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == EDIT_MODE)
            infoTv.setText(data.getStringExtra("title"));
    }
}
