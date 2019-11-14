package com.sm.testmemolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.sm.testmemolist.MainActivity.VIEW_MODE;

public class EditMemoActivity extends AppCompatActivity {

    TextView statusTv;
    EditText titleTx, contentsTx, dateTx;
    Button editBtn, cancelBtn;
    MemoManager manager;
    String title;
    String date;
    Calendar cal;
    int flag;
    public static final String EDIT_MODE = "Edit";
    public static final String SAVE_MODE = "Save";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        Intent i = getIntent();

        cal = Calendar.getInstance();
        titleTx = findViewById(R.id.titleTx);
        contentsTx = findViewById(R.id.contentsTx);
        dateTx = findViewById(R.id.dateTx);
        editBtn = findViewById(R.id.editBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        statusTv = findViewById(R.id.statusTv);
        manager = MemoManager.getInstance();


        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(cal.getTime());

        flag = i.getIntExtra("status", 0);
        if(flag == VIEW_MODE) {

            statusTv.setText("View");
            editBtn.setText(EDIT_MODE);
            titleTx.setText(i.getStringExtra("title"));
            contentsTx.setText(i.getStringExtra("contents"));
            dateTx.setText(i.getStringExtra("date"));
            titleTx.setFocusable(false);
            contentsTx.setFocusable(false);
            dateTx.setFocusable(false);

        } else {
            statusTv.setText("New");
            editBtn.setText(SAVE_MODE);
            dateTx.setText(date);
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editBtn.getText() == EDIT_MODE) {
                    editBtn.setText(SAVE_MODE);
                    statusTv.setText("Edit");
                    contentsTx.setFocusable(true);
                }
                else if(editBtn.getText() == SAVE_MODE) {
                    Intent i = new Intent(EditMemoActivity.this, EditMemoActivity.class);
                    i.putExtra("status", "Edit");
                    i.putExtra("title", titleTx.getText().toString());
                    i.putExtra("contents", contentsTx.getText().toString());
                    i.putExtra("date", date);

                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
