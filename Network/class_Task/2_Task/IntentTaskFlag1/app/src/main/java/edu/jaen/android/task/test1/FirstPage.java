package edu.jaen.android.task.test1;

import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FirstPage extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent.FLAG_ACTIVITY_CLEAR_TOP

        Button b=(Button) findViewById(R.id.next1);
        b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			    Intent i=new Intent("Second2");
			    startActivity(i);
			}
		});
        Button b3=(Button) findViewById(R.id.next3);
        b3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			    Intent i=new Intent("Third3");
			    //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//호출된 객체와 같은 객체가 stack에 있다면 마지막 객체를 Top으로 옮긴다.
			    //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//stack에 존재하는 객체를 다시 호출시 존재하는 객체까지 stack지우고 새로운 객체 호출
			    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);//객체 재사용
			    //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// Service 또는 BroadcastReceiver에서 Activity 호출시 사용해야 함
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
    		Log.i("ACTIVITY11","ROOT:"+cName+":"+cnt+":"+id+":"+c);
    	}
		super.onResume();
	}
}