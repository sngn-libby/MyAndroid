package com.jaen.xml.pull;

import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyPullActivity extends Activity {
	
	ListView v;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		v = (ListView) findViewById(R.id.result);
		new XMLParserTask().execute("http://192.168.123.167:8080/result.xml");

	}

	class XMLParserTask extends AsyncTask<String, String, ArrayList<Check>> {
		
		ArrayList<Check> list = new ArrayList<Check>();
		XmlPullParser parser = Xml.newPullParser();

		protected ArrayList<Check> doInBackground(String... arg) {
			try {

				parser.setInput(new URL(arg[0]).openConnection()
						.getInputStream(), null);
				int eventType = parser.getEventType();
				Check ch = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					String name = null;
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase("Check")) {
							ch = new Check();
							ch.setCode(parser.getAttributeValue(0));
						} else if (ch != null) {
							if (name.equalsIgnoreCase("Clean")) {
								ch.setClean(parser.nextText());
							} else if (name.equalsIgnoreCase("Ready")) {
								ch.setReady(parser.nextText());
							} else if (name.equalsIgnoreCase("Response")) {
								ch.setResponse(parser.nextText());
							} else if (name.equalsIgnoreCase("Request")) {
								// if(parser.next()!= -1)
								ch.setRequest(parser.nextText());
							}
						}
						break;
					case XmlPullParser.END_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase("Check") && ch != null) {
							list.add(ch);
						}
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e("MyPullActivity", e.getMessage(), e);
				throw new RuntimeException(e);
			}
			return list;
		}

		protected void onPostExecute(ArrayList<Check> result) {
			v.setAdapter(new ArrayAdapter<Check>(MyPullActivity.this,
					android.R.layout.simple_list_item_1, result));
		}

	}
}