package edu.jaen.android.nfc.beamUri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyBeamUriActivity extends Activity implements
		CreateBeamUrisCallback, OnNdefPushCompleteCallback {
	
	private static final String TAG = "MyBeamUriActivity";
	private static final int PICK_IMAGE = 100;

	private NfcAdapter mNfcAdapter;
	private Uri mSelImgUri;

	private TextView mUriName;
	private ImageView mPreviewImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mUriName = (TextView) findViewById(R.id.imagePathText);
		mPreviewImage = (ImageView) findViewById(R.id.selImageView);

		// Check for available NFC Adapter
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		if (mNfcAdapter == null) {
			mUriName.setText("NFC is not available on this device.");
		} else {
			// Register callback to set NDEF message
			mNfcAdapter.setBeamPushUrisCallback(this, this);
			mNfcAdapter.setBeamPushUrisCallback(beamCallBack, this);
			// Register callback to listen for message-sent success
			mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
		}
	}

	CreateBeamUrisCallback beamCallBack = new CreateBeamUrisCallback() {
		@Override
		public Uri[] createBeamUris(NfcEvent event) {
			return new Uri[] {mSelImgUri, };
		}
	};

	
	//onClick 속성을 이용한 callback 함수
	public void selectImage(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*"); // intent.setType("audio/*");
		startActivityForResult(intent, PICK_IMAGE);
	}

	@Override // 모든 data는 무조건 Content Provider가 내용을 제공한다. ==> mUriName은 content://로 시작
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
				&& data != null) {
			mUriName.setText(data.getData().toString());
			mSelImgUri = data.getData();
			mPreviewImage.setImageURI(data.getData());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// Check to see that the Activity started due to an Android Beam
		if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
			processIntent(getIntent());
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		Toast.makeText(this, "111 : "+intent.getAction(), 0).show();
		setIntent(intent);
	}

	void processIntent(Intent intent) {
		Uri data = intent.getData();
		if (data != null) {
			mPreviewImage.setImageURI(data);
		} else {
			mUriName.setText("Received Invalid Image Uri");
		}
	}


	@Override
	public Uri[] createBeamUris(NfcEvent event) {
		if (mSelImgUri == null) {
			return null;
		}
		return new Uri[] { mSelImgUri }; // Uri 배열에 전송하고싶은 내용만 넣어주면 -> 블루투스켜고, 내용전송하고, 블루투스 끄는 모든 기능을 알아서 수행한다.
	}

	@Override
	public void onNdefPushComplete(NfcEvent event) {
		Log.i(TAG, "Push Complete!");
		
	}
}
