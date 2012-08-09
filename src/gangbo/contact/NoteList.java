package gangbo.contact;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class NoteList extends Activity {

	private dbHelper db;
	private ListView lv;
	private Button bt_list_edit;
	private Button bt_add;
	private Button bt_web;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		lv = (ListView) findViewById(R.id.list);
		db = new dbHelper(getApplicationContext());

		bt_add = (Button) findViewById(R.id.new_bn);
		bt_add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Constants.ACTION_NEW);
				// intent.addCategory(Constants.CATEGORY_DEFAULT);
				startActivity(intent);
			}
		});
		bt_list_edit = (Button) findViewById(R.id.list_edit);
		bt_list_edit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("-----bt_list_edit---------", "--");
				v.clearFocus();
				lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			}
		});
		bt_web = (Button) findViewById(R.id.bt_web);
		bt_web.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), WebViewActivity.class);
				// intent.addCategory(Constants.CATEGORY_DEFAULT);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		SQLiteCursor c = (SQLiteCursor) db.select();
		Log.v("cccccccccc", "===" + c.getCount());
		SimpleCursorAdapter adapter;
		try {
			adapter = new SimpleCursorAdapter(this, R.layout.list_item, c,
					new String[] { dbHelper.FIELD_TITLE,
							dbHelper.FIELD_UPDATE_TIME }, new int[] {
							R.id.item, R.id.update_time });
			lv.setAdapter(adapter);
		} catch (Exception e) {
			c.close();
			db.close();
		}

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Constants.ACTION_EDIT);
				Bundle bundle = new Bundle();
				bundle.putLong("id", id);
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
		registerForContextMenu(lv);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delete:
			deleteNote(info.id);
			onResume();
			return true;
		case R.id.list_item_share:		
			shareIt(info.id);
			return true;
		case R.id.item_edit:
			
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void deleteNote(Long id) {
		db.delete(id.intValue());
	}
	public void shareIt(Long id) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain"); // 纯文本
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		Cursor c = db.select(id);
		c.moveToFirst();
		String str = c.getString(c.getColumnIndex(dbHelper.FIELD_TITLE));
		intent.putExtra(Intent.EXTRA_TEXT, str + "[来自pjbaby]");
		startActivity(Intent.createChooser(intent, getTitle()));
	}

}
