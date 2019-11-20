package com.scsa.andr.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context c, Intent i) {
		Toast.makeText(c, "Alarm!!", Toast.LENGTH_LONG).show();
		Intent it = new Intent(c, AlarmActivity.class);
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.startActivity(it);
	}
}
