package com.scsa.andr.mousecatch;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	FrameLayout f;
	FrameLayout.LayoutParams params;
    int count=0;   //잡은 쥐 개수를 저장할 변수
    int delay=1200;  // 게임 속도 조절
    static boolean threadEndFlag=true; // 쓰레드 끄기
    MouseTask mt;				// 쓰레드 구현

	int myWidth;  // 내 폰의 너비
	int myHeight; // 내 폰의 높이
	int imgWidth=150;  //그림 크기
	int imgHeight=150;//그림 크기
	Random r=new Random();  // 이미지 위치를 랜덤하게 발생시킬 객체
	
	SoundPool pool;   // 소리
	int liveMouse;    // 소리
	MediaPlayer mp;   // 소리
	
	int x=200;        //시작위치
	int y=200;        //시작위치
	ImageView[] imgs; // 이미지들을 담아 놓을 배열
	
	int level=1;      // 게임 레벨
	int nums=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		f=(FrameLayout) findViewById(R.id.frame);
		params=new FrameLayout.LayoutParams(1, 1);
		
		//디스플레이 크기 체크
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		myWidth=metrics.widthPixels;
		myHeight=metrics.heightPixels;
		Log.d("game","My Window "+myWidth+" : "+myHeight);
		
		//사운드 셋팅
		pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		liveMouse = pool.load(this, R.raw.mouse_scream, 1);
		mp=MediaPlayer.create(this, R.raw.bgm);
		mp.setLooping(true);
		
		init(5);
		
	}
	public void init(int nums){
		//초기화
		count=0;
		threadEndFlag=true;
		this.nums=nums;
		delay=(int)(delay*(10-level)/10.);
		
		f.removeAllViews();

		//이미지 담을 배열 생성과 이미지 담기
		imgs=new ImageView[nums];		
		for(int i=0; i<nums; i++){
			ImageView iv=new ImageView(this);
			iv.setImageResource(R.drawable.running_mouse_trans);  // 이미지 소스 설정
			f.addView(iv, params);  // 화면에 표시
			imgs[i]=iv;     // 배열에 담기
			iv.setOnClickListener(h);  // 이벤트 등록
		}
		
		mt=new MouseTask();  //일정 간격으로 이미지 위치를 바꿀 쓰레드 실행
		mt.execute();
	}
	protected void onResume() {
		super.onResume();
		mp.start();
	};
	protected void onPause() {
		super.onPause();
		mp.stop();
	};
	
	protected void onDestroy() {
		super.onDestroy();
		mp.release();
		mt.cancel(true);
		threadEndFlag = false;
		
	};
	View.OnClickListener  h=new View.OnClickListener() {
		public void onClick(View v) {   // 쥐를 잡았을 때
			count++;
			ImageView iv=(ImageView)v;
			pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
			iv.setVisibility(View.INVISIBLE);          // 이미지(쥐) 제거

			Toast.makeText(MainActivity.this, "Die...."+count, Toast.LENGTH_LONG).show();
			if(count==nums){   // 쥐를 다 잡았을때
				threadEndFlag=false;
				mt.cancel(true);
			
				AlertDialog.Builder dia=new AlertDialog.Builder(MainActivity.this);
				dia.setMessage("계속하시겠습니까?");
				dia.setPositiveButton("네", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						level++;
						init(6);					
					}
				});
				dia.setNegativeButton("아니오", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();				
					}
				});
				dia.show();
			}
	
		}
	};
	
	// 쥐 위치 이동하여 다시 그리기
	public void update(){  
		if(!threadEndFlag) return;
		Log.d("game", "update:");
		for(ImageView img:imgs){
			x=r.nextInt(myWidth-imgWidth);
			y=r.nextInt(myHeight-imgHeight);
			
			img.layout(x, y, x+imgWidth, y+imgHeight);
			img.invalidate();
		}
		
	}
	// 일정 시간 간격으로 쥐를 다시 그리도록 update()를 호출하는 쓰레드
	class MouseTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {// 다른 쓰레드 
			while(threadEndFlag){
				//다른 쓰레드에서는 UI를 접근할 수 없으므로
				publishProgress();	//자동으로 onProgressUpdate() 가 호출된다.
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {e.printStackTrace();}						
			}
			return null;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			update();
		}
	};//end MouseTask
}// end MainActivity