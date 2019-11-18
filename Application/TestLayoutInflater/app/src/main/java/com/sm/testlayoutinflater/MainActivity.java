package com.sm.testlayoutinflater;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LayoutInflater layoutInflater;
    LinearLayout myLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        myLinear = findViewById(R.id.myLinear);
    }

    public void showAtype(View view) {
        View aView = layoutInflater.inflate(R.layout.a_layout, null);
        myLinear.removeAllViews();
        myLinear.addView(aView);
    }

    public void showBtype(View view) {
        View bView = layoutInflater.inflate(R.layout.b_layout, null);
        TextView tv = bView.findViewById(R.id.titleTv);
        tv.setText("오늘점심은 마라탕");
        myLinear.removeAllViews();
        myLinear.addView(bView);
    }
}
