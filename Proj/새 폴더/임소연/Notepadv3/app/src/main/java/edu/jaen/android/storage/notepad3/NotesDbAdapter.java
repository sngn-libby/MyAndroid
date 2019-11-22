package edu.jaen.android.storage.notepad3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesDbAdapter {

    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TIME = "time";
    public static final String KEY_FLAG = "flag";

    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mDbHelper; // DB, Table 생성 및 초기화 -> SQL 파일로 가져온다
    private SQLiteDatabase mDb; // SQL CRUD 메소드 정의

    private static final String DATABASE_CREATE =
            "create table notes (_id integer primary key autoincrement, "
                    + "title text not null, body text not null, time text not null, flag int not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "notes";

    private static final int DATABASE_VERSION = 4;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        // 생성자 -> super (데이터 베이스 생성)
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }


    public NotesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }


    public NotesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }


    public long createNote(String title, String body, String time) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_FLAG, 0);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }


    public boolean deleteNote(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteNote(String title, String date) {
        long id = searchNoteId(title, date);
        if (id == -2) return false;
        deleteNote(id);
        return true;
    }

    public long searchNoteId(String title) {
        Cursor c = mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE}, "title" + "=\'" + title + "\'", null,
                null, null, null, null);

        c.moveToNext();
        return c.getLong(0);
    }

    public long searchNoteId(String title, String date) {
        Cursor c = mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE}, "title" + "=\'" + title + "\' AND "+KEY_TIME+"=\'" + date + "\'", null,
                null, null, null, null);
        if (c.getCount() == 0) return -2;

        c.moveToNext();
        return c.getLong(0);
    }


    public Cursor fetchAllNotes() {
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_BODY, KEY_TIME, KEY_FLAG}, null, null, null, null, KEY_FLAG+", "+KEY_TIME);
    }

    public Cursor fetchTodayNotes() {
        SimpleDateFormat sdf= new SimpleDateFormat("YYYY-MM-dd");
        String date = sdf.format(new Date());
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_BODY, KEY_TIME, KEY_FLAG}, KEY_TIME +"=\'" + date +"\'", null, null, null, KEY_FLAG+", "+KEY_TIME);
    }

    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                        KEY_TITLE, KEY_BODY, KEY_TIME, KEY_FLAG}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateNote(long rowId, String title, String body, String time, int flag) {
        ContentValues args = new ContentValues();

        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        args.put(KEY_TIME, time);
        args.put(KEY_FLAG, flag);
        return mDb.update(DATABASE_TABLE, args, KEY_TITLE + "=\'" + title + "\'", null) > 0;
    }
}
