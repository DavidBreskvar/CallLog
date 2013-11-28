package calllog.project;

import java.util.ArrayList;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.LoaderManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Log extends FragmentActivity {

	DBAdapter db = new DBAdapter(this);
	TextView name, number, date, duration, type, opombaText;
	EditText opombaEdit;
	Button edit, delete, opombaB;
	String textOpomba;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);

		edit = (Button) findViewById(R.id.btnEdit);
		delete = (Button) findViewById(R.id.btnDel);
		opombaB = (Button) findViewById(R.id.btnOpomba);
		opombaText = (TextView) findViewById(R.id.callText);
		opombaEdit = (EditText) findViewById(R.id.callEdit);

		db.open();
		//preveri ;e je baza že polna
		if (true) {
			String[] callInfo = { android.provider.CallLog.Calls.CACHED_NAME,
					android.provider.CallLog.Calls.NUMBER,
					android.provider.CallLog.Calls.TYPE,
					android.provider.CallLog.Calls.DATE,
					android.provider.CallLog.Calls.DURATION };
			String callOrder = android.provider.CallLog.Calls.DATE + " DESC";

			Cursor c = getContentResolver().query(
					android.provider.CallLog.Calls.CONTENT_URI, callInfo, null,
					null, callOrder);

			if (c.moveToFirst()) {
				do {
					String name = c.getString(0);
					String number = c.getString(1);
					String type = c.getString(2);
					String date = c.getString(3);
					Date callDate = new Date(Long.valueOf(date));
					String duration = c.getString(4) + "s";

					int typeContent = Integer.parseInt(type);
					switch (typeContent) {
					case CallLog.Calls.OUTGOING_TYPE:
						type = "OUTGOING";
						break;

					case CallLog.Calls.INCOMING_TYPE:
						type = "INCOMING";
						break;

					case CallLog.Calls.MISSED_TYPE:
						type = "MISSED";
						break;
					}

					date = callDate.toString();

					if (name == null) {
						name = "Unknown";
					}

					db.insertCall(name, number, type, date, duration, null);
				} while (c.moveToNext());
				callsToList();
			}

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
	}

	private void callsToList() {
		Cursor c = db.getAllRows();

		String[] fromDB = new String[] { DBAdapter.NAME, DBAdapter.NUMBER,
				DBAdapter.TYPE, DBAdapter.DATE, DBAdapter.DURATION,
				DBAdapter.OPOMBE };
		int[] toList = new int[] { R.id.callName, R.id.callNum, R.id.callType,
				R.id.callDate, R.id.callDur, R.id.callText };

		SimpleCursorAdapter adapter = new myAdapter(this, R.layout.calls, c,
				fromDB, toList);

		ListView list = (ListView) findViewById(R.id.logList);

		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log, menu);
		return true;
	}

}
