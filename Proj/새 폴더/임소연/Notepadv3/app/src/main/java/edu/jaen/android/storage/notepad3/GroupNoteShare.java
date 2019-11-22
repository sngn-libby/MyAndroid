package edu.jaen.android.storage.notepad3;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Parcelable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class GroupNoteShare extends Activity {

    private EditText mTitleText;
    private EditText mDateText;
    private String shNTitle;
    private String shNDate;
    private String shData;

    DatePickerDialog dPicker;

    Button shareB;
    private Long mRowId;
    private NotesDbAdapter mDbHelper;
    int rqCode;
    String Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_note_share);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();

        mTitleText = findViewById(R.id.shareTitleEt);
        mDateText = findViewById(R.id.shareDateEt);
        shareB = findViewById(R.id.shareB);

        shareB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mWriteMode = true;
                new AlertDialog.Builder(GroupNoteShare.this)
                        .setMessage("Touch tag to write")
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(
                                            DialogInterface dialog) {
                                        mWriteMode = false;

                                    }
                                }).create().show();
            }
        });

        mRowId = savedInstanceState != null ? savedInstanceState.getLong(NotesDbAdapter.KEY_ROWID)
                : null;
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
                    : null;
        }
        Intent here = getIntent();
        rqCode = here.getIntExtra("requestCode", 0);


        pIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndefFilter = new IntentFilter(
                NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefFilter.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        mNdefFilters = new IntentFilter[] { ndefFilter };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(NotesDbAdapter.KEY_ROWID, mRowId);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void selectDateS(View view) {
        dPicker = new DatePickerDialog(this);
        dPicker.show();
        mDateText = findViewById(R.id.shareDateEt);
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

    public void cancelShare(View view) {
        finish();
    }

    private static final String TAG = "WriteTagActivity";
    private boolean mWriteMode = false;
    NfcAdapter mNfcAdapter;

    PendingIntent pIntent;
    IntentFilter[] mNdefFilters;

    @Override
    protected void onResume() {
        super.onResume();

        if (mNfcAdapter == null) {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        } else {
            mNfcAdapter.enableForegroundDispatch(this, pIntent, mNdefFilters, null);
        }
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
            setIntent(new Intent()); // Consume this intent.
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mNfcAdapter == null) {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        } else {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(mWriteMode && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())){
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            writeTag(makeNdefMessage(), detectedTag);

        } else if (!mWriteMode && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] msgs = getNdefMessages(intent);
            promptForContent(msgs[0]);
        }
    }

    private void promptForContent(final NdefMessage msg) {
        new AlertDialog.Builder(this)
                .setTitle("새로운 Tag가 인식되었습니다.\n 읽으시겠습니까?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String body = new String(msg.getRecords()[0]
                                        .getPayload());

                                String[] titleNdate = body.split("==");
                                mDbHelper.createNote(titleNdate[0], "", titleNdate[1]);

                                Toast.makeText(GroupNoteShare.this, "그룹 일정이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                }).show();
    }

    private NdefMessage makeNdefMessage() {
        shNTitle = mTitleText.getText().toString();
        shNDate = mDateText.getText().toString();
        if (shNTitle.length() == 0 || shNDate.length() == 0) {
            Toast.makeText(this, "제목과 날짜를 모두 선택해주세요", Toast.LENGTH_SHORT).show();
            return null;
        }
        shData = "[shared] " + shNTitle + "==" +  shNDate;
        mWriteMode = true;
        byte[] textBytes = shData.getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                "text/plain".getBytes(), new byte[] {}, textBytes);
        mDbHelper.createNote("[shared] " + shNTitle, "", shNDate);
        return new NdefMessage(new NdefRecord[] { textRecord });
    }

    private NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent
                    .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,
                        empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
            }
        } else {
            finish();
        }
        return msgs;
    }

    boolean writeTag(NdefMessage message, Tag tag) {

        int size = message.toByteArray().length;

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    return false;
                }
                ndef.writeNdefMessage(message);
                Toast.makeText(GroupNoteShare.this, "성공적으로 공유되었습니다", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            } else {
            }
        } catch (Exception e) {

        }
        return false;
    }

}
