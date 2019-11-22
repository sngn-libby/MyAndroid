package com.sm.myproject;

import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class InitializeActivity extends AppCompatActivity {


    private static final String NFC_TAG = "database";
    NfcAdapter nAdapter;
    PendingIntent pIntent;
    IntentFilter[] filters;
    Intent i;
    private TodoViewModel mTodoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickfood);

        nAdapter = NfcAdapter.getDefaultAdapter(this);
        i = getIntent();
//        Intent i = new Intent(this, TodolistActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        pIntent = PendingIntent.getActivity(this, 0, i, 0);
//
//        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
//        nAdapter.enableForegroundDispatch(this, pIntent, filters, null);

        try {
            if(i.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED))
                getNFCData(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();



//        Intent i = new Intent(this, MainActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        pIntent = PendingIntent.getActivity(this, 0, i, 0);
//
//        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
//
//        filters = new IntentFilter[] { filter };
//        nAdapter.enableForegroundDispatch(this, pIntent, filters, null);
    }


    private void getNFCData(Intent intent) throws InterruptedException {


        // NFC_TAG
        Log.e("INFO", intent.getAction());

        Intent i = new Intent(InitializeActivity.this, TodolistActivity.class);
        i.putExtra("initialize", NFC_TAG);
        startActivityForResult(i, 20);
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
////            mTodoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
////            mTodoViewModel.deleteAll();
////            Toast.makeText(this, "DataBase Initialized\nSuccessfully", Toast.LENGTH_SHORT).show();
////
////            Thread.sleep(1000);
////            finish();
//
//
//        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 20) {

            finish();
        }
    }
}
