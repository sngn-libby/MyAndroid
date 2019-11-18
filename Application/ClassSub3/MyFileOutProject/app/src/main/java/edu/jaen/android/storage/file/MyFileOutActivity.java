package edu.jaen.android.storage.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MyFileOutActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView t = (TextView) findViewById(R.id.text1);
		BufferedWriter bo = null;
		BufferedReader br = null;
		try {
			bo = new BufferedWriter(new OutputStreamWriter(openFileOutput(
					"data.txt", MODE_PRIVATE)));
			bo.append("안녕하세요.");
			bo.append("반갑습니다.");
			t.setText("파일이 정상적으로 생성되었습니다.");
			bo.flush();
			bo.close();

			// 생성된 파일을 읽어서 TextView에 출력해보기
			br = new BufferedReader(new InputStreamReader(openFileInput("data.txt")));
			String line;
			t.setText("");
			while((line = br.readLine())!=null) {
				t.append(line);
			}
			br.close();
		} catch (IOException e) {
			Log.e("IO", "File Output Error");
			t.setText("파일이 생성시 오류가 발생했습니다.");
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}
}