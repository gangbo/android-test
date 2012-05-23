package gangbo.contact;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class Item extends Activity {
	private EditText et;
	private dbHelper db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		if (db != null) {
			db.close();
		}
		db = new dbHelper(getApplicationContext());
		et = (EditText) findViewById(R.id.detail);
		Intent intent = getIntent();
		if (intent.getAction().equals(Constants.ACTION_EDIT)) {
			Long id = intent.getExtras().getLong("id");
			Cursor c = db.select(id);
			try {
				c.moveToFirst();
				et.setText(c.getString(c.getColumnIndex(dbHelper.FIELD_TITLE)));
			} finally {
				c.close();
			}
		} else if (intent.getAction().equals(Constants.ACTION_NEW)) {

		} else {
			Log.w("error", "unknown action " + intent.getAction());
			finish();
			return;
		}

	}

	public void onPause() {
		super.onPause();
		// save
		saveNote();
		db.close();
	}

	public void saveNote() {
		String content = et.getText().toString();
		Intent intent = getIntent();
		if (intent.getAction().equals(Constants.ACTION_EDIT)) {
			Long id = intent.getExtras().getLong("id");
			db.update(id.intValue(), content);
		} else if (intent.getAction().equals(Constants.ACTION_NEW)) {
			db.insert(content);
		} else {
			Log.w("error", "unknown action " + intent.getAction());
			finish();
			return;
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.item_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.share_bt:
			shareIt();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void shareIt() {
		// TODO Auto-generated method stub
		Log.v("----", "分享");
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain"); // 纯文本
		/*
		 * // * 图片分享 　　　 intent.setType("image/png"); // 添加图片 // File f = new
		 * File(Environment.getExternalStorageDirectory()+ //
		 * "/download/ads/143/0.png"); File f = new File("/sdcard/qb.jpg"); Uri
		 * u = Uri.fromFile(f); intent.putExtra(Intent.EXTRA_STREAM, u);
		 */
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		intent.putExtra(Intent.EXTRA_TEXT, et.getText() + "[来自pjbaby]");
		startActivity(Intent.createChooser(intent, getTitle()));
	}
}
