package edu.jaen.android.ui.toast;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyToastActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.toastBut).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast toa = Toast.makeText(MyToastActivity.this,
								"나 토스트야 ~", Toast.LENGTH_LONG);
						toa.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						toa.show();

					}
				});

		findViewById(R.id.changeToastB).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						LinearLayout linar = (LinearLayout) View.inflate(
								MyToastActivity.this, R.layout.toastlayout,
								null);

						Toast toa = Toast.makeText(MyToastActivity.this,
								"나 안드로이드야 ~~", 1);
						toa.setView(linar);
						toa.show();

					}
				});

	}
}
