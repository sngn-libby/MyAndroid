package edu.jaen.android.storage.notepad3;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Notepadv3 extends ListActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    private static final int ACTIVITY_READ=2;

    private static final int PERSONAL_ID = Menu.FIRST;
    private static final int GROUP_ID = Menu.FIRST + 1;

    private NotesDbAdapter mDbHelper;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        fillData();
    }

    public void delete(View view) {
        View row = (View)view.getParent();
        TextView titleTv = row.findViewById(R.id.text1);
        TextView dateTv = row.findViewById(R.id.text2);
        mDbHelper.deleteNote(titleTv.getText().toString(),  dateTv.getText().toString());
        fillData();
    }

    public void edit(View view) {
        View row = (View)view.getParent();
        TextView titleTv = row.findViewById(R.id.text1);
        String title = titleTv.getText().toString();

        Intent i = new Intent(this, NoteEdit.class);
        i.putExtra("requestCode", ACTIVITY_EDIT);
        i.putExtra("title", title);
        long id = mDbHelper.searchNoteId(title);
        i.putExtra("id",id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    private void fillData() {
        Cursor notesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(notesCursor);

        String[] from = new String[]{NotesDbAdapter.KEY_TITLE, NotesDbAdapter.KEY_TIME};

        int[] to = new int[]{R.id.text1, R.id.text2};

        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to) {

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                super.bindView(view, context, cursor);
                LinearLayout myLinear = view.findViewById(R.id.myLinear);

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
                    titleTv.setTextColor(Color.rgb(40, 53,147));
                    titleTv.setPaintFlags(titleTv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                return;
            }

        };

        setListAdapter(notes);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, PERSONAL_ID, 0, R.string.personal);
        menu.add(0, GROUP_ID, 0, R.string.group);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case PERSONAL_ID:
                createNote();
                return true;
            case GROUP_ID:
                writeGroupNote();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        i.putExtra("requestCode" , ACTIVITY_CREATE);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    private void writeGroupNote() {
        Intent i = new Intent(this, GroupNoteShare.class);
        i.putExtra("requestCode", ACTIVITY_CREATE);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, 
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && resultCode != ACTIVITY_READ) fillData();
    }

    LayoutInflater layoutInf;


    // 다했으면 체크하고 안했으면 체크 풀기 -> inflator 로..
    public void checkDone(View view) {
        LinearLayout myLinear = view.findViewById(R.id.myLinear);
        TextView titleTv = (TextView) view.findViewById(R.id.text1);
        TextView dateTv = (TextView) view.findViewById(R.id.text2);
        String title = (titleTv).getText().toString();
        String date = (dateTv).getText().toString();

        long id = mDbHelper.searchNoteId(title, date);
        Cursor c = mDbHelper.fetchNote(id);

        if (c.getInt(4) == 1) {
            mDbHelper.updateNote(id, title, c.getString(2), date, 0);
        }
        else if (c.getInt(4) == 0) {
            mDbHelper.updateNote(id, title, c.getString(2), date, 1);
        }

        fillData();
    }
}
