package edu.jaen.android.storage.notepad3;

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
import android.widget.TextView;

public class Game extends Activity {
    static final int [] images = {R.drawable.love, R.drawable.happy, R.drawable.soso, R.drawable.sad, R.drawable.angry};
    static final String [] toast = {"사랑스러워\n 10점 획득", "행복해\n5점 획득", "그냥 그래\n0점 획득", "슬퍼\n5점 감소", "화나\n10점 감소"};
    static final int [] scores = { 10, 5, 0, -5, -10};
    static final int INIT_SCORE = 30;
    static final int EMO_COUNT = 5;
    static final int SCORE_LOVE = 10;
    static final int SCORE_HAPPY = 5;

    FrameLayout f;
    FrameLayout.LayoutParams params;
    int score=0;
    int delay=1200;
    static boolean threadEndFlag=true;
    MouseTask mt;

    TextView scoreTv;
    int scoreleft = 0;

    Random rand;
    int myWidth;  // 내 폰의 너비
    int myHeight; // 내 폰의 높이
    int imgWidth=150;  //그림 크기
    int imgHeight=150;//그림 크기
    Random r=new Random();  // 이미지 위치를 랜덤하게 발생시킬 객체

    SoundPool pool;   // 소리
    int liveMouse;    // 소리
    MediaPlayer mp;   // 소리
    float speed = 1.0f;

    int x=200;        //시작위치
    int y=200;        //시작위치
    ImageView[] imgs; // 이미지들을 담아 놓을 배열

    int level=1;      // 게임 레벨
    int nums=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        f=(FrameLayout) findViewById(R.id.frame);
        params=new FrameLayout.LayoutParams(1, 1);

        // 점수판
        scoreTv = findViewById(R.id.scoreTv);

        //랜덤 숫자 생성기
        rand = new Random();

        //디스플레이 크기 체크
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        myWidth=metrics.widthPixels;
        myHeight=metrics.heightPixels;
        Log.d("game","My Window "+myWidth+" : "+myHeight);

        //사운드 셋팅
        pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        liveMouse = pool.load(this, R.raw.itemreceiving, 1);

        mp=MediaPlayer.create(this, R.raw.bgm);

        mp.setLooping(true);
        mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));

        init(40);

    }

    public void init(int nums){
        int imgsrc;

        scoreleft = 0;
        score=INIT_SCORE;
        threadEndFlag=true;
        this.nums=nums;
        delay=(int)(delay*(10-level)/10.);

        f.removeAllViews();

        imgs=new ImageView[nums];
        for(int i=0; i<nums; i++){
            ImageView iv=new ImageView(this);
            imgsrc = rand.nextInt(EMO_COUNT);
            iv.setImageResource(images[imgsrc]);  // 이미지 소스 설정
            f.addView(iv, params);  // 화면에 표시
            imgs[i]=iv;    // 배열에 담기
            iv.setTag(imgsrc);
            if (imgsrc == 0) scoreleft += SCORE_LOVE;
            else if (imgsrc ==1) scoreleft += SCORE_HAPPY;
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

    protected void newGame() {
        delay=1200;
        level = 1;
        speed = 1.0f;

        mp.stop();
        mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
        mp.start();

        init(50);
    }

    protected void onDestroy() {
        super.onDestroy();
        mp.release();
        mt.cancel(true);
        threadEndFlag = false;
    };


    View.OnClickListener  h=new View.OnClickListener() {
        public void onClick(View v) {
            AlertDialog.Builder dia;
            int tag = (Integer)v.getTag();
            score += scores[tag];
            scoreTv.setText("SCORE: " + Integer.toString(score));
            if (tag == 0) scoreleft -= SCORE_LOVE;
            else if (tag == 1) scoreleft -= SCORE_HAPPY;
            ImageView iv=(ImageView)v;
            pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
            iv.setVisibility(View.INVISIBLE);          // 이미지(쥐) 제거

            Toast.makeText(Game.this, toast[tag], Toast.LENGTH_LONG).show();

            if (score + scoreleft < 100) {
                threadEndFlag=false;
                mt.cancel(true);

                dia=new AlertDialog.Builder(Game.this);
                dia.setTitle("아무리 해도 행복해질 수 없음");
                dia.setIcon(R.drawable.sad);
                dia.setNegativeButton("메인으로 돌아가기", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dia.setPositiveButton("다시 시작하기", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        newGame();
                    }
                });
                dia.show();
            } else if(score>=100){
                threadEndFlag=false;
                mt.cancel(true);

                if (level == 3) {
                    dia=new AlertDialog.Builder(Game.this);
                    dia.setTitle("행복해서 하늘로 날아갔음");
                    dia.setIcon(R.drawable.love);
                    dia.setPositiveButton("메인으로 돌아가기", new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    dia.setNegativeButton("다시 시작하기", new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            newGame();
                        }
                    });
                    dia.show();
                } else {

                    dia = new AlertDialog.Builder(Game.this);
                    dia.setTitle("계속하시겠습니까?");

                    dia.setPositiveButton("네", new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            level++;
                            init(50);
                            mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed += 0.2f));
                        }
                    });
                    dia.setNegativeButton("아니오", new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    dia.show();
                }
            } else if (score <= 0) {
                threadEndFlag=false;
                mt.cancel(true);
                dia=new AlertDialog.Builder(Game.this);
                dia.setTitle("너무 화나서 죽어버림");
                dia.setIcon(R.drawable.angry);
                dia.setNegativeButton("메인으로 돌아가기", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dia.setPositiveButton("다시 시작하기", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        newGame();
                    }
                });
                dia.show();
            }

        }
    };

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

    class MouseTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            while(threadEndFlag){
                publishProgress();
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
    };
}