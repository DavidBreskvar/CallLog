package calllog.project;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class DBAdapter {
	
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String NUMBER = "number";
	public static final String TYPE = "type";
	public static final String DATE = "date";
	public static final String DURATION = "duration";
	public static final String OPOMBE = "opombe";
	
	private static final String DATABASE_NAME = "LogCat";
	private static final String DATABASE_TABLE = "logcatString";
	private static final int DATABASE_VERSION = 1;
	private static final String[] ALL_CALLS = new String[] {
		ID, NAME, NUMBER,TYPE, DATE, DURATION, OPOMBE
	};
	
	
	private final Context context;
	
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
						ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
						NAME + " VARCHAR, " + 
						NUMBER + " VARCHAR, " +
						TYPE + " VARCHAR, " +
						DATE + " VARCHAR, " +
						DURATION + " VARCHAR, " +
						OPOMBE + " VARCHAR);");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
			// on database upgrade, create new table, copy old to new, drop old table
			Log.w("Warning", "Upgrading, old to new: " +oldV + "=> "+newV);
			db.execSQL("DROP TABLE IF EXISTS contacts");
			onCreate(db);
		}
	}

	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		DBHelper.close();
	}
	
	public long insertCall (String name, String number, String type, String date, String duration, String opombe) {
		
		ContentValues callValues = new ContentValues();
		callValues.put(NAME, name);
		callValues.put(NUMBER, number);
		callValues.put(TYPE, type);
		callValues.put(DATE, date);
		callValues.put(DURATION, duration);
		callValues.put(OPOMBE, opombe);
		
		return db.insert(DATABASE_TABLE, null, callValues);
		
	}

	
	public boolean deleteCallLog(long rowId) {
		return db.delete(DATABASE_TABLE, ID + "=" + rowId, null) > 0;
	}
	
	public Cursor getCallLog(long rowId) throws SQLException {
		
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				ID, NAME, NUMBER, TYPE, DATE, DURATION}, 
				ID + "=" + rowId, null, null, null, null, null);
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public boolean updateLog(long rowId, String name, String number, String date, String type, String duration, String opombe) {
		ContentValues newCall = new ContentValues();
		newCall.put(NAME, name);
		newCall.put(NUMBER, number);
		newCall.put(TYPE, type);
		newCall.put(DATE, date);
		newCall.put(DURATION, duration);
		newCall.put(OPOMBE, opombe);
		
		return db.update(DATABASE_TABLE, newCall, ID + "=" + rowId, null) > 0;
	}
	
	public Cursor getAllRows() {
		String where = null;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_CALLS, 
							where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}


}
