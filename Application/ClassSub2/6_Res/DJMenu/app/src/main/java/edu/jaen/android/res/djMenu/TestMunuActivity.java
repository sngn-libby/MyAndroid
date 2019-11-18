package edu.jaen.android.res.djMenu;

import edu.jaen.android.res.djMenu.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/*
 * 수행되는 코드
 * */
public class TestMunuActivity extends Activity {

	// public static final int MENU1 = Menu.FIRST +1;
	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button b = (Button) findViewById(R.id.button1);
		registerForContextMenu(b);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setSubtitle("동진만세");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// menu.add(Menu.NONE, MENU1, Menu.NONE, "메뉴1");

		menu.add(0, INSERT_ID, Menu.NONE, R.string.menu_name) // Menu.None (순서 안주겠다)
				.setIcon(android.R.drawable.ic_menu_share)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); // Action Bar에 공간이 있으면 표출하겠다.
		menu.add(0, DELETE_ID, Menu.NONE, R.string.menu_name1);

		// addSubMenu(menu);
		SubMenu sm = menu.addSubMenu("Sub Menu");
		sm.add("Sub1");
		sm.add("Sub2");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_name1);
		
		
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		ListView view = null;
		ContextMenuInfo menuInfo = item.getMenuInfo();
		AdapterContextMenuInfo aptInfo = (AdapterContextMenuInfo) menuInfo;
		String selItem = view.getAdapter().getItem(aptInfo.position).toString();
		
		
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case INSERT_ID:
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();

			break;
		case DELETE_ID:
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	public void addSubMenu(Menu menu) {
		SubMenu sm = menu.addSubMenu("Sub Menu");
		sm.add("Sub1");
		sm.add("Sub2");
	}

}
