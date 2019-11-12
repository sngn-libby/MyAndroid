package com.sm.testreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle myB = intent.getExtras();
        Object[] objArr = (Object[]) myB.get("pdus"); // pdus 는 SMS메세지를 끄집어낼때 약속된 key값
        SmsMessage[] smsArr = new SmsMessage[objArr.length];

        smsArr[0] = SmsMessage.createFromPdu((byte[]) objArr[0]);

        String telNum = smsArr[0].getOriginatingAddress();
        String msg = smsArr[0].getMessageBody();

//        SmsManager smsMgr = SmsManager.getDefault();
//        smsMgr.sendTextMessage("010-1234-5678", "010-2345-6789", msg, null, null);

        Toast.makeText(context, "phoneNum : "+telNum+"\n"+"content : "+msg, Toast.LENGTH_SHORT).show();

    } // Broadcast Receiver는 Context와 상속관계가아니다. --> this로 넘겨주면 안된다.


}
