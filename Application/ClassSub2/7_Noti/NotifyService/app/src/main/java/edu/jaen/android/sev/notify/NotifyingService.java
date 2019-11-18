package edu.jaen.android.sev.notify;

import edu.jaen.android.sev.notify.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.widget.Toast;


public class NotifyingService extends Service {
 
    private NotificationManager nm;
    private ConditionVariable mCondition;
 
    @Override
    public void onCreate() {
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Thread notifyingThread = new Thread(null, mTask, "NotifyingService");
        mCondition = new ConditionVariable(false);
        notifyingThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Notification Occurs", Toast.LENGTH_SHORT).show();
        showNoti();
        return super.onStartCommand(intent, flags, startId);
    }

    private void showNoti() {
        Notification.Builder  b=new Notification.Builder(this);
        b.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);
        b.setTicker(null);
        b.setContentTitle("Important Weather Newsbreak");
        b.setContentText("now Storm is comming in 20m/s towards Seoul, if you need any detail please call...");

        // 확장 상태바를 눌렀을 때 --> PendingIntent
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.weather.go.kr/weather/main.jsp"));
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);

        b.setContentIntent(pIntent);

        long[] pat = new long[] {500, 100, 1000, 100, 2000};
        b.setVibrate(pat);

        nm.notify(MUSIC, b.build());
    }

    @Override
    public void onDestroy() {
        nm.cancel(MUSIC);
         mCondition.open();
    }

    private Runnable mTask = new Runnable() {
        public void run() {
            for (int i = 0; i < 4; ++i) {
                showNotification(R.drawable.play,
                        R.string.status_bar_notifications_happy_message);
                if (mCondition.block(5 * 1000)) 
                    break;
                showNotification(R.drawable.stop,
                        R.string.status_bar_notifications_ok_message);
                if (mCondition.block(5 * 1000)) 
                    break;
                showNotification(R.drawable.top_arrow_right,
                        R.string.status_bar_notifications_sad_message);
                if (mCondition.block(5 * 1000)) 
                    break;
            }
            NotifyingService.this.stopSelf();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public static final int MUSIC=100;
    private void showNotification(int moodId, int textId) {
       /* CharSequence text = getText(textId);
        Notification notification = new Notification(moodId, null, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, NotifyingController.class), 0);
        notification.setLatestEventInfo(this, getText(R.string.status_bar_info),
                       text, contentIntent);*/
    	Notification.Builder  b=new Notification.Builder(this);
    	b.setSmallIcon(moodId);
    	b.setTicker(null);
    	b.setContentTitle(getText(R.string.status_bar_info));
    	b.setContentText(getText(textId));
    	b.setContentIntent(PendingIntent.getActivity(this, 0,
                new Intent(this, NotifyingController.class), 0));
        nm.notify(MUSIC, b.build());
    }


}
