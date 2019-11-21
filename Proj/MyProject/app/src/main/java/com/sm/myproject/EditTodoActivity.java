package com.sm.myproject;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.sm.myproject.TodolistActivity.VIEW_MODE;

public class EditTodoActivity extends AppCompatActivity {

    TodoManager manager;
    AlarmManager am;

    CheckBox statusBox;
    EditText titleTx, contentsTx;
    Button editBtn, cancelBtn, dateBtn, timeBtn;
    ImageView voiceBtn1, voiceBtn2;
    String date;
    private ArrayList<String> res_title;
    private ArrayList<String> res_detail;
    private String selected;
    Calendar cal;
    int year = 0;
    int month = 0;
    int day = 0;
    int hour = 0;
    int min = 0;

    int flag;
    // boolean alarm = false;
    public static final String EDIT_MODE = "Edit";
    public static final String SAVE_MODE = "Save";

    public static final int DATE_CODE = 3;
    private final int GOOGLE_STT = 1000;
    private final int GOOGLE_STT_DETAIL = 1001;


    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        manager = TodoManager.getInstance();
        am = (AlarmManager)getSystemService(ALARM_SERVICE);

        Intent i = getIntent();

        cal = Calendar.getInstance();
        titleTx = findViewById(R.id.titleTx);
        contentsTx = findViewById(R.id.contentsTx);
        dateBtn = findViewById(R.id.dateBtn);
        timeBtn = findViewById(R.id.timeBtn);
        // alarmTx = findViewById(R.id.alarmTx);
        editBtn = findViewById(R.id.editBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        statusBox = findViewById(R.id.statusBox);
        // alarmBox = findViewById(R.id.alarmBox);
        voiceBtn1 = findViewById(R.id.voiceBtn1);
        voiceBtn2 = findViewById(R.id.voiceBtn2);


        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(cal.getTime());

        flag = i.getIntExtra("mode", 0);
        if(flag == VIEW_MODE) {

            editBtn.setText(EDIT_MODE);
            titleTx.setText(i.getStringExtra("title"));
            contentsTx.setText(i.getStringExtra("contents"));
            dateBtn.setText(i.getStringExtra("date"));
            statusBox.setChecked(i.getBooleanExtra("stat", false));

        } else {
            editBtn.setText(SAVE_MODE);
            dateBtn.setText(date);
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditTodoActivity.this, EditTodoActivity.class);
                i.putExtra("stat", statusBox.isChecked());
                i.putExtra("title", titleTx.getText().toString());
                i.putExtra("contents", contentsTx.getText().toString());
                i.putExtra("stDate", date);
                i.putExtra("finDate", dateBtn.getText().toString());

                setResult(RESULT_OK, i);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        voiceBtn1.setOnClickListener(new View.OnClickListener() { // Title
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "말을 하세요.");
                startActivityForResult(i, GOOGLE_STT);
            }
        });

        voiceBtn2.setOnClickListener(new View.OnClickListener() { // Detail
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "말을 하세요.");
                startActivityForResult(i, GOOGLE_STT_DETAIL);
            }
        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditTodoActivity.this, "pick date", Toast.LENGTH_SHORT).show();
                new DatePickerDialog(
                        EditTodoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateBtn.setText(year +"-" + pad(monthOfYear+1) + "-"+ pad(dayOfMonth));
                                EditTodoActivity.this.year  = year;
                                EditTodoActivity.this.month = monthOfYear;
                                EditTodoActivity.this.day   = dayOfMonth;
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(
                        EditTodoActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                timeBtn.setText(pad(hourOfDay) +":" + pad(minute));
                                EditTodoActivity.this.hour = hourOfDay;
                                EditTodoActivity.this.min = minute;
                            }
                        },
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE),
                        true
                ).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data==null || resultCode!=RESULT_OK || (requestCode!=GOOGLE_STT && requestCode!=GOOGLE_STT_DETAIL)) {
            Toast.makeText(EditTodoActivity.this, "Error", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case GOOGLE_STT:
                res_title = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);        //인식된 데이터 list 받아옴.
                String[] sa = res_title.toArray(new String[res_title.size()]);

                AlertDialog ad = new AlertDialog.Builder(this)
                        .setTitle("Select")
                        .setSingleChoiceItems(sa, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected = res_title.get(which);
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                titleTx.setText(selected);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                titleTx.setText("");
                                selected = null;
                            }
                        }).create();
                ad.show();
                break;
            case GOOGLE_STT_DETAIL:
                res_detail = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);        //인식된 데이터 list 받아옴.
                String[] sb = res_detail.toArray(new String[res_detail.size()]);

                AlertDialog ad2 = new AlertDialog.Builder(this)
                        .setTitle("Select")
                        .setSingleChoiceItems(sb, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected = res_detail.get(which);
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                contentsTx.setText(selected);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                contentsTx.setText("");
                                selected = null;
                            }
                        }).create();
                ad2.show();
                break;
        }
    }
}
