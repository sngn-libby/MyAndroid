package edu.jaen.android.sev.notify;

import edu.jaen.android.sev.notify.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NotifyingController extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notifying_controller);

        Button button = (Button) findViewById(R.id.notifyStart);
        button.setOnClickListener(mStartListener);
        button = (Button) findViewById(R.id.notifyStop);
        button.setOnClickListener(mStopListener);
    }

    private OnClickListener mStartListener = new OnClickListener() {
        public void onClick(View v) {
            startService(new Intent(NotifyingController.this, 
                    NotifyingService.class));
        }
    };

    private OnClickListener mStopListener = new OnClickListener() {
        public void onClick(View v) {
            stopService(new Intent(NotifyingController.this, 
                    NotifyingService.class));
        }
    };
}

