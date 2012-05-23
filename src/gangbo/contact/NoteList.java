package gangbo.contact;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

;

public class NoteList extends Activity {

	private dbHelper db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ListView lv = (ListView) findViewById(R.id.list);

		db = new dbHelper(getApplicationContext());

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
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.v("====", "position=" + position);
				Log.v("====", "id=" + id);
				Intent intent = new Intent(Constants.ACTION_EDIT);
				Bundle bundle = new Bundle();
				bundle.putLong("id", id);
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// menu
				Log.w("----long click=", "" + id);
				return true;
			}
		});

		Button bt = (Button) findViewById(R.id.new_bn);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Constants.ACTION_NEW);
				// intent.addCategory(Constants.CATEGORY_DEFAULT);
				startActivity(intent);
			}
		});
	}
	@Override
	protected  void onResume(){
		
	}

}
