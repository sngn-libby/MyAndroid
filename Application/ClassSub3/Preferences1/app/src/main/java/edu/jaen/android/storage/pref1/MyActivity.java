package edu.jaen.android.storage.pref1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView tv = (TextView) findViewById(R.id.text1);

		/* 저장된 정보 읽어오기 */
		//SharedPreferences prefs = getPreferences(MODE_PRIVATE); // 파일 생성시 권한주는과정
		SharedPreferences prefs = getSharedPreferences("myData", MODE_PRIVATE);
		int v = prefs.getInt("key", 0); // Int로 저장된파일이며, 파일내 키에 값이 없으면 defValue 리턴

		/* 파일에 해당 정보를 저장하기 */
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("key", ++v);
		// editor.putInt("key", ++v);
		editor.commit(); // 반드시 commit() 해야 값이 반영된다.

		tv.setText("Preference Test: " + v);

		Button b = (Button) findViewById(R.id.next);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(MyActivity.this, NextActivity.class);
				startActivity(i);
			}
		});
	}
}