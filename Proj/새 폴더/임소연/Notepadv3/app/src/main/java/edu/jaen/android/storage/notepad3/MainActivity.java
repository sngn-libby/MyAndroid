package edu.jaen.android.storage.notepad3;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    Intent i;
    private NotesDbAdapter mDbHelper;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.mainList);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        fillData();
    }

    private void fillData() {
        Cursor notesCursor = mDbHelper.fetchTodayNotes();
        startManagingCursor(notesCursor);

        String[] from = new String[]{NotesDbAdapter.KEY_TITLE, NotesDbAdapter.KEY_TIME};

        int[] to = new int[]{R.id.text1, R.id.text2};

        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.notes_row2, notesCursor, from, to) {

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                super.bindView(view, context, cursor);
                LinearLayout myLinear = view.findViewById(R.id.myLinear2);
                myLinear.setClickable(false);
                TextView titleTv = (TextView) myLinear.findViewById(R.id.text1);
                TextView dateTv = (TextView)  myLinear.findViewById(R.id.text2);
                String title = titleTv.getText().toString();
                String date = dateTv.getText().toString();

                long id = mDbHelper.searchNoteId(title, date);

                Cursor c = mDbHelper.fetchNote(id);
                if (id == -2) return;
                if (c.getInt(4) == 1) {
                    titleTv.setTextColor(Color.GRAY);
                    titleTv.setPaintFlags(titleTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else if (c.getInt(4) == 0) {
                    titleTv.setTextColor(Color.rgb(91, 108,255));
                    titleTv.setPaintFlags(titleTv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                return;
            }

        };

        listView.setAdapter(notes);
    }

    public void openNews(View view) {
        i = new Intent(MainActivity.this, NewsReader.class);
        startActivity(i);

    }

    public void openGame(View view) {
        i = new Intent(MainActivity.this, Game.class);
        startActivity(i);

    }

    public void openTodo(View view) {
        i = new Intent(MainActivity.this, Notepadv3.class);
        startActivity(i);
    }
}
