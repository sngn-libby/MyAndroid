package com.sm.testmemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditMemoActivity extends AppCompatActivity implements View.OnClickListener {

    MemoManager manager = MemoManager.getInstance();
    EditText titleTx, contentTx, dateTx;
    Button okBtn, cancelBtn;
    public static int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        titleTx = findViewById(R.id.editTitle);
        contentTx = findViewById(R.id.editContent);
        dateTx = findViewById(R.id.editDate);
        okBtn = findViewById(R.id.okBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.okBtn) {
            if(titleTx.getText()==null && contentTx.getText()==null) return;
            Intent i = new Intent();
            ++id;
            Memo m = new Memo(id, titleTx.getText().toString()
                    , contentTx.getText().toString()
                    , dateTx.getText().toString());
            manager.addMemo(m);

            i.putExtra("ok", id);

            setResult(RESULT_OK, i);
            finish();
        }
        else if(v.getId() == R.id.cancelBtn) {
            setResult(RESULT_CANCELED);
            finish();
        }

    }
}
