package com.sm.test4result;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    EditText editTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent recInt = getIntent();
        String str = recInt.getStringExtra("title");

        editTv = findViewById(R.id.editTv);
        editTv.setText(str);
    }

    public void save(View view) {
        Intent i = new Intent();

        String str = editTv.getText().toString();
        i.putExtra("title", str);

        setResult(RESULT_OK, i);
        finish();
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
