package edu.jaen.android.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MyActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 requestWindowFeature(Window.FEATURE_LEFT_ICON);
         
         setContentView(R.layout.dialog_activity);
         
         getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, 
                 android.R.drawable.ic_dialog_alert);
     }
 }

