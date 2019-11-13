package com.sm.testlistview;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends ListActivity {

    ListView mylv;
    String[] foodArr = {"마라탕", "탕수육", "초밥", "양꼬치", "떡볶이"};
    boolean SIMPLE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        if(SIMPLE) {
            // 1. 사용할 ListView 객체를 가져오기
            mylv = findViewById(R.id.myListView);
            // 2. 어댑터뷰에 설정한 어댑터를 만들고
//            ArrayAdapter<String> foodAdapter
//                    = new ArrayAdapter<String>(
//                            this, android.R.layout.simple_list_item_1, foodArr);
            ArrayAdapter<String> foodAdapter
                    = new ArrayAdapter<String>(
                    this, R.layout.row_layout, R.id.titleTv, foodArr);
            // 3. 어댑터뷰에 어댑터를 설정한다.
            mylv.setAdapter(foodAdapter);
            // 4. Event
            mylv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // String sel = foodArr[position]; // not recommended
                    String selfood = parent.getAdapter().getItem(position).toString();
                    Toast.makeText(MainActivity.this, selfood+" is selected", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            final ArrayAdapter<String> foodAdapter
                = new ArrayAdapter<String>(
                        this, android.R.layout.simple_list_item_1, foodArr);

            setListAdapter(foodAdapter);


        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }
}
