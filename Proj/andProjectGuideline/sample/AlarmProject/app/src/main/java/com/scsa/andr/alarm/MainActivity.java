package com.scsa.andr.alarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {
	AlarmManager am;
	
	EditText edtSec;
	EditText edtDate;
	EditText edtTime;
	
	Calendar cal = Calendar.getInstance();
	int year = 0;
	int month = 0;
	int day = 0;
	int hour = 0;
	int min = 0;

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		am=(AlarmManager)getSystemService(ALARM_SERVICE);
		
		edtSec=(EditText)findViewById(R.id.elap);
		edtDate=(EditText)findViewById(R.id.date);
		edtTime=(EditText)findViewById(R.id.time);
		
		Button btnReg1 = (Button)findViewById(R.id.reg1);
		btnReg1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int after = Integer.parseInt(edtSec.getText().toString());
				
				long now = SystemClock.elapsedRealtime();
				long atTime = now + (after * 1000);
				
				Intent i = new Intent();
				i.setClass(MainActivity.this, AlarmReceiver.class);
				PendingIntent pi=PendingIntent.getBroadcast(MainActivity.this, 101, i, 0);
				am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, atTime, pi);
				Toast.makeText(MainActivity.this, after+"초 후 알람등륵", Toast.LENGTH_SHORT).show();
				//finish();
			}
		});
		Button btnCancel1 = (Button)findViewById(R.id.cancel1);
		btnCancel1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(MainActivity.this, AlarmReceiver.class);
				PendingIntent pi=PendingIntent.getBroadcast(MainActivity.this, 101, i, 0);
				Toast.makeText(MainActivity.this, "알람 해제", Toast.LENGTH_SHORT).show();
				am.cancel(pi);
			}
		});
		edtDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(
					MainActivity.this, 
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							edtDate.setText(year +"-" + pad(monthOfYear+1) + "-"+ pad(dayOfMonth));
							MainActivity.this.year  = year;
							MainActivity.this.month = monthOfYear;
							MainActivity.this.day   = dayOfMonth;
						}
					}, 
					Calendar.getInstance().get(Calendar.YEAR), 
					Calendar.getInstance().get(Calendar.MONTH), 
					Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
				).show();
			}
		});
		edtTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new TimePickerDialog(
					MainActivity.this, 
					new OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							edtTime.setText(pad(hourOfDay) +":" + pad(minute));
							MainActivity.this.hour = hourOfDay;
							MainActivity.this.min = minute;						
						}
					}, 
					Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 
					Calendar.getInstance().get(Calendar.MINUTE), 
					true
				).show();
			}
		});
		findViewById(R.id.reg2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    cal.set(Calendar.YEAR, MainActivity.this.year);
			    cal.set(Calendar.MONTH, MainActivity.this.month);
			    cal.set(Calendar.DAY_OF_MONTH, MainActivity.this.day);
			    cal.set(Calendar.HOUR_OF_DAY, MainActivity.this.hour);
			    cal.set(Calendar.MINUTE, MainActivity.this.min);
			    cal.set(Calendar.SECOND, 0);
			    cal.set(Calendar.MILLISECOND, 0);
				long atTime = cal.getTimeInMillis();
				
				Intent i = new Intent();
				i.setClass(MainActivity.this, AlarmReceiver.class);
				PendingIntent pi=PendingIntent.getBroadcast(MainActivity.this, 102, i, 0);
				am.set(AlarmManager.RTC_WAKEUP, atTime, pi);
				Toast.makeText(MainActivity.this, "알람등륵", Toast.LENGTH_SHORT).show();
				//finish();
			}
		});
		findViewById(R.id.cancel2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, AlarmReceiver.class);
				PendingIntent pi=PendingIntent.getBroadcast(MainActivity.this, 102, i, 0);
				Toast.makeText(MainActivity.this, "알람 해제", Toast.LENGTH_SHORT).show();
				am.cancel(pi);
			}
		});
	}
}
