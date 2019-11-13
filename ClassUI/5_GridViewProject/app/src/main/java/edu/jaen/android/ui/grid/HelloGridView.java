package edu.jaen.android.ui.grid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class HelloGridView extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		private Integer[] mThumbIds = { 
			R.drawable.apple, R.drawable.banana,
			R.drawable.bananatwins, R.drawable.bench, R.drawable.cdnew,
			R.drawable.coffeetime, R.drawable.graphicsapplication,
			R.drawable.hotdogcar, R.drawable.icecream,
			R.drawable.ic_launcher, R.drawable.mail,
			R.drawable.marmaladecubes, R.drawable.mightyducks,
			R.drawable.mycomputer, R.drawable.mypictures,
			R.drawable.orange, R.drawable.pear, R.drawable.peartwo,
			R.drawable.peasheart, R.drawable.shoe, R.drawable.umbrella,
			R.drawable.trashempty 
		};

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return mThumbIds.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				//imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}
			imageView.setImageResource(mThumbIds[position]);
			return imageView;
		}
		// references to our images
	}
}