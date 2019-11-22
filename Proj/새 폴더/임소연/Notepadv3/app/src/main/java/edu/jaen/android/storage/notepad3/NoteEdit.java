package edu.jaen.android.storage.notepad3;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class NoteEdit<getView> extends Activity {

    private EditText mTitleText;
    private EditText mBodyText;
    private EditText mDateText;

    DatePickerDialog dPicker;

    private Long mRowId;
    private NotesDbAdapter mDbHelper;
    int rqCode;
    String Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        setContentView(R.layout.note_edit);


        mTitleText = (EditText) findViewById(R.id.titleEt);
        mBodyText = (EditText) findViewById(R.id.contentEt);
        mDateText = (EditText) findViewById(R.id.dateEt);

        Button confirmButton = (Button) findViewById(R.id.saveB);
        Button cancelButton = (Button) findViewById(R.id.ccB);

        mRowId = savedInstanceState != null ? savedInstanceState.getLong(NotesDbAdapter.KEY_ROWID)
                : null;
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
                    : null;
        }

        Intent here = getIntent();
        rqCode = here.getIntExtra("requestCode", 0);
        if (rqCode > 0) {
            mTitleText.setFocusable(false);
            mTitleText.setClickable(false);

            long id = here.getLongExtra("id", 0);
            Cursor c = mDbHelper.fetchNote(id);
            mTitleText.setText(c.getString(1));
            mBodyText.setText(c.getString(2));
            mDateText.setText(c.getString(3));
        }
        if (rqCode == 2) {
            mBodyText.setFocusable(false);
            mBodyText.setClickable(false);
            mDateText.setFocusable(false);
            mDateText.setClickable(false);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }

        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                rqCode = -1;
                finish();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(NotesDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState(); // DB Access
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent here = getIntent();
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        String time = mDateText.getText().toString();

        if (rqCode == -1) return;
        mRowId = mDbHelper.searchNoteId(title, time);
        if (mRowId == -2) {
            long id = mDbHelper.createNote(title, body, time);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateNote(mRowId, title, body, time, 0);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void selectDate(View view) {
        dPicker = new DatePickerDialog(this);
        dPicker.show();
        dPicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DatePicker dateP = dPicker.getDatePicker();
                int Year = dateP.getYear();
                int Month = dateP.getMonth() + 1;
                int Day = dateP.getDayOfMonth();
                String dayM;
                String mM;
                if (Month < 10) mM = "0" + Month;
                else mM = Integer.toString(Month);
                if (Day < 10) dayM = "0" + Day;
                else dayM = Integer.toString(Day);
                Date = Year + "-" + mM + "-" + dayM;

                mDateText.setText(Date);
            }
        });


    }
}
