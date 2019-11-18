package edu.jaen.android.task.test2;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FourthPage extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        Button b=(Button) findViewById(R.id.next2);
        b.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
			    Intent i=new Intent("First1");
			   // i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); //호출한 흔적을 back stack에 남기지 않는다.back키 호출시 불려지지 않음
			    startActivity(i);
			}
		});
        Button b2=(Button) findViewById(R.id.my2);
        b2.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
			    Intent i=new Intent("Fourth4");			    
			   // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//Top에 존재하는 객체를 다시 호출시 기존 객체 지우고 호출
			   // i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//Top에 존재하는 객체를 다시 호출시 새로운 객체를 생성하지 않고 기존 객체 사용
			    startActivity(i);
			}
		});
    }
    @Override
	protected void onResume() {
    	ActivityManager m=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
    	List<ActivityManager.RunningTaskInfo> tList =m.getRunningTasks(10);
    	for( ActivityManager.RunningTaskInfo info : tList){
    		int cnt=info.numActivities;
    		int id=info.id;
    		int c=info.numRunning;
    		String cName=info.baseActivity.getClassName();
    		Log.i("ACTIVITY44","ROOT:"+cName+":"+cnt+":"+id+":"+c);
    	}
		super.onResume();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
}