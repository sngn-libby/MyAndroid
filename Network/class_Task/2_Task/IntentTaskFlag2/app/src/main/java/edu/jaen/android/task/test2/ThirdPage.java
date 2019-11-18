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

public class ThirdPage extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button b=(Button) findViewById(R.id.next1);
        b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			    Intent i=new Intent("Fourth4");
			    startActivity(i);
			}
		});
        Button b2=(Button) findViewById(R.id.my1);
        b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			    Intent i=new Intent("Third3");			    
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
    		Log.i("ACTIVITY33","ROOT:"+cName+":"+cnt+":"+id+":"+c);
    	}
		super.onResume();
	}
}