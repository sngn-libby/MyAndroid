package com.sm.testnfcreader;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView infoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoTv = findViewById(R.id.infoTv);
        // typeTv = findViewById(R.id.typeTv);

        Intent recI = getIntent();
        String action = recI.getAction();
//        infoTv.setText("action: "+action);
        if(action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) processIntent(recI); // 중요
    }

    private void processIntent(Intent i) {
        Parcelable[] rawData = i.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage ndefMsg = (NdefMessage) rawData[0];
        NdefRecord[] rawRecords = ndefMsg.getRecords();
        NdefRecord textRcd = rawRecords[0];
        byte[] recB = textRcd.getType();
        String recType = new String(recB);
        Log.i("INFO","type : "+recType);
        // typeTv.setText("type: "+recType);
        if(recType.equals("T")) {
            byte[] bData = textRcd.getPayload();

            String sData = new String(bData, 3, bData.length-3); // " en" 빼고 끄집어내기 (encoding 값)
            infoTv.setText(sData + "\n");
            infoTv.append(bData.toString());

        } else if(recType.equals("U")) {
            Uri recUi = textRcd.toUri();
            infoTv.setText(recUi.toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, recUi);
            startActivity(intent);
        } else {

        }
    }
}
