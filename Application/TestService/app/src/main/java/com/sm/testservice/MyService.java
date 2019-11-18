package com.sm.testservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {

    MediaPlayer player;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
        player = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*
        String url = intent.getStringExtra("url");
         */
//        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
//        String str = intent.getData().toString();
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
//        Log.i("INFO", "I am "+Thread.currentThread().getName());

        if(player!=null) {
            player.stop();
            player.release();
        }
        player = MediaPlayer.create(this, R.raw.back);
        player.setLooping(false);
        player.start();


//        return super.onStartCommand(intent, flags, startId);
        // return : stop후 다시 살아나는 이유 (비정상적 종료시 다시 실행시키는 것)
        return START_REDELIVER_INTENT;
//        return START_NOT_STICKY;
        // stop 후 다시 살아나지 않는다
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        if(player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
