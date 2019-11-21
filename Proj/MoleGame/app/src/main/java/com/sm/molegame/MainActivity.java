package com.sm.molegame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView timeLeftTv, playerNameTv;
    TextView myScore, targetScore;
    TextView readySignTv;
    ImageButton startBtn;
    RelativeLayout timeSetLayout;

    ImageView[] img_array = new ImageView[9];
    int[] imageID = {
            R.id.imageView1,
            R.id.imageView2,
            R.id.imageView3,
            R.id.imageView4,
            R.id.imageView5,
            R.id.imageView6,
            R.id.imageView7,
            R.id.imageView8,
            R.id.imageView9,
    };

    final String TAG_ON = "on"; //태그용
    final String TAG_OFF = "off";
    final int PAUSE_TIME = 1000*60*5; // pause버튼 눌렀을 때 5분까지 정지 가능
    static int score = 0;
    private static boolean start_flag = false;

    final int MAXTIME = 60;
    final int TARGET_SCORE = 20;

    final int GAME_START_MSG = 0;
    final int GAME_LOSE_MSG = 1;
    final int GAME_WIN_MSG = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeLeftTv = (TextView)findViewById(R.id.timeLeftTv);
        playerNameTv = (TextView)findViewById(R.id.playerNameTv);
        myScore = (TextView)findViewById(R.id.myScore);
        targetScore = (TextView)findViewById(R.id.targetScore);
        startBtn = (ImageButton)findViewById(R.id.gameStartBtn);
        readySignTv = (TextView)findViewById(R.id.readySignTv);
        timeSetLayout = (RelativeLayout)findViewById(R.id.timeSetLayout);

        timeSetLayout.setVisibility(View.GONE);
        readySignTv.setText("");
        timeLeftTv.setText(""+MAXTIME);
        myScore.setText("0");
        targetScore.setText(""+TARGET_SCORE);

        dlg_handler.sendEmptyMessage(GAME_START_MSG);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setVisibility(View.GONE);
                timeSetLayout.setVisibility(View.VISIBLE);
                myScore.setVisibility(View.VISIBLE);

                try {
                    readySignTv.bringToFront();
                    readySignTv.setText("READY");
                    Thread.sleep(1000);
                    readySignTv.setText("GO!");
                    Thread.sleep(500);
                    readySignTv.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                new Thread(new timeCheck()).start();

                for(int i = 0; i<img_array.length; i++){
                    new Thread(new DThread(i)).start();
                }
            }
        });

        for(int i = 0; i<img_array.length; i++){

            img_array[i] = (ImageView)findViewById(imageID[i]);
            img_array[i].setImageResource(R.drawable.moledown);
            img_array[i].setTag(TAG_OFF);

            img_array[i].setOnClickListener(new View.OnClickListener() { //두더지이미지에 온클릭리스너
                @Override
                public void onClick(View v) {
                    if(((ImageView)v).getTag().toString().equals(TAG_ON)){
                        Toast.makeText(getApplicationContext(), "good", Toast.LENGTH_LONG).show();
                        myScore.setText(String.valueOf(++score));
                        ((ImageView) v).setImageResource(R.drawable.moledown);
                        v.setTag(TAG_OFF);
                    }else{
                        Toast.makeText(getApplicationContext(), "bad", Toast.LENGTH_LONG).show();
                        myScore.setText(String.valueOf(--score));

                        if(score<0){
                            myScore.setText("0");
                            dlg_handler.sendEmptyMessage(GAME_LOSE_MSG);
                        }

                        ((ImageView) v).setImageResource(R.drawable.moleup);
                        v.setTag(TAG_ON);
                    }
                }
            });
        }
    }

    Handler onHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            img_array[msg.arg1].setImageResource(R.drawable.moleup);
            img_array[msg.arg1].setTag(TAG_ON); //올라오면 ON태그 달아줌
        }
    };

    Handler offHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            img_array[msg.arg1].setImageResource(R.drawable.moledown);
            img_array[msg.arg1].setTag(TAG_OFF); //내려오면 OFF태그 달아줌

        }
    };

    public class DThread implements Runnable{ //두더지를 올라갔다 내려갔다 해줌
        int index = 0; //두더지 번호

        DThread(int index){
            this.index=index;
        }

        @Override
        public void run() {
            while(true){
                try {
                    Message msg1 = new Message();
                    int offtime = new Random().nextInt(5000) + 500 ;
                    Thread.sleep(offtime); //두더지가 내려가있는 시간

                    msg1.arg1 = index;
                    onHandler.sendMessage(msg1);

                    int ontime = new Random().nextInt(1000)+500;
                    Thread.sleep(ontime); //두더지가 올라가있는 시간
                    Message msg2 = new Message();
                    msg2.arg1= index;
                    offHandler.sendMessage(msg2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            timeLeftTv.setText(msg.arg1 + "");
        }
    };

    public class timeCheck implements Runnable {
        int i;

        @Override
        public void run() {
            for (i = MAXTIME; i >= 0; i--) {
                Message msg = new Message();
                msg.arg1 = i;
                handler.sendMessage(msg);

                if(Integer.parseInt((String) myScore.getText())
                        >= Integer.parseInt((String) targetScore.getText())) {
                    dlg_handler.sendEmptyMessage(GAME_WIN_MSG);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(i < 0) {
                dlg_handler.sendEmptyMessage(GAME_LOSE_MSG);
            }
        }
    }

    public Handler dlg_handler = new Handler() {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void handleMessage(Message msg) {

            // setGameStartDialog
            if(msg.what == GAME_START_MSG) {
                final EditText input_txt = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.whackamolelogo)
                        .setTitle("WHACK A MOLE!")
                        .setMessage("Uesrname")
                        .setView(input_txt)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(input_txt.getText().equals("")) input_txt.setText("Unknown");
                                playerNameTv.setText(input_txt.getText());
                                Toast.makeText(MainActivity.this, input_txt.getText() + " entered", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
            }

            else if (msg.what == GAME_LOSE_MSG) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("You Lose")
                        .setMessage("Your Record\n\t" + myScore.getText())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .show();
            }

            else if (msg.what == GAME_WIN_MSG) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("You Win")
                        .setMessage("Your Record\n\t" + myScore.getText())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .create();
            }
        }
    };
}