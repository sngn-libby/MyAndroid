package com.sm.testforegroundnfcreader;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    PendingIntent pIntent;
    IntentFilter[] filters;
    TextView infoTv, typeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        infoTv = findViewById(R.id.infoTv);
        typeTv = findViewById(R.id.typeTv);
        if(nfcAdapter == null) {
            Toast.makeText(this, "NFC not available", Toast.LENGTH_SHORT).show();
            finish();
        }

        /* 1. Create Intent
        (프레임워크에서 전달된 인텐트를 처리할 액티비티를 호출할 인텐트 생성) */
        Intent i = new Intent(this, this.getClass());
        // getClass() : Class라는 class를 가져온다. (Reflection API)
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        /* 2. Create Pending Intent (인텐트를 처리할 펜딩인텐트생성) */
        pIntent = PendingIntent.getActivity(this, 0, i, 0);

        /* 3. Create Intent Filter */
//        IntentFilter ndefFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        IntentFilter ndefFilter2 = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        IntentFilter ndefFilter3 = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//
//        try {
//            ndefFilter.addDataType("text/plain");
//            ndefFilter2.addDataType("https");
//            ndefFilter3.addDataType("http");
//            filters = new IntentFilter[] {ndefFilter, ndefFilter2, ndefFilter3};
//            // processIntent(i);
//        } catch (IntentFilter.MalformedMimeTypeException e) {
//            e.printStackTrace();
//        }

        IntentFilter tagFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        filters = new IntentFilter[] {tagFilter, };
    }

    private NdefMessage makeNdefMsg(String data, String type) {
        type = "URI";
        data = "https://www.github.com/sngn-libby";
        NdefMessage ndefMsg = null;
        if(type.equals("URI")) {
            NdefRecord urlR = NdefRecord.createUri(data); // String type을 metadata로 쓸 경우

            /* AAR Record를 넣고싶을 때
            NdefRecord aarR = NdefRecord.createApplicationRecord(getPackageName());
            NdefRecord [] recArr = new NdefRecord[] {urlR, aarR};
             */

            NdefRecord [] recArr = new NdefRecord[] {urlR}; // ndefMsg.getRecords(); // 읽을떄와 반대순서로 하면된다.
            ndefMsg = new NdefMessage(recArr);
            /*
            <READ>
            (Parcelable 사용) NdefMessage -> NdefRecord[] -> NdefRecord
            <WRITE>
            NdefRecord -> NdefRecord[] -> NdefMsg
             */
        }

        return ndefMsg;
    }

    @Override
    protected void onResume() { // Activate
        super.onResume();
        // 5. Activate
        nfcAdapter.enableForegroundDispatch(this, pIntent, filters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.i("INFO", "onNewIntent call...");
        String action = intent.getAction();
        // infoTv.append(action+"\n");
        processIntent(intent);
    }

    @Override
    protected void onPause() { // UnActivate
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void processIntent(Intent i) {
        Parcelable[] rawData = i.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage ndefMsg = (NdefMessage) rawData[0];
        NdefRecord[] rawRecords = ndefMsg.getRecords();
        NdefRecord textRcd = rawRecords[0];
        byte[] recB = textRcd.getType();
        String recType = new String(recB);
        Log.i("INFO","type : "+recType);
        typeTv.setText(recType);
        // typeTv.setText("type: "+recType);
        infoTv.setText("");
        if(recType.equals("T")) {
            byte[] bData = textRcd.getPayload();

            String sData = new String(bData, 3, bData.length-3); // " en" 빼고 끄집어내기 (encoding 값)
            infoTv.setText(sData + "\n");
        } else if(recType.equals("U")) {
            Uri recUi = textRcd.toUri();
            infoTv.setText(recUi.toString() + "\n");
        } else {

        }
    }
}
