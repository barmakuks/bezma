package com.fairbg.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	int id = 0;
	public static final String KEY_ROWID = "_id";
	public static final String KEY_VALUE = "value";
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "BezmAndroid";
	private static final String DATABASE_TABLE = "tblParameters";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table tblParameters (_id char(20) primary key, value varchar(255));";

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
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS tblRandomQuotes");
			onCreate(db);
		}
	}

	/** ��������� ���� ������ */
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	/** ��������� ���� ������ */
	public void close() {
		DBHelper.close();
	}

	/**���������� � �� �������� ���� String*/
	public void putValue(String key, String value) {
		try {
			if (getString(key, "").equals("")) {
				ContentValues content = new ContentValues();
				content.put(KEY_ROWID, key);
				content.put(KEY_VALUE, value);
				Log.i("INSERT "+ key, value);
				db.insert(DATABASE_TABLE, null, content);
			} else {
				ContentValues content = new ContentValues();
				content.put(KEY_VALUE, value);
				Log.i("UPDATE " + key, value);
				db.update(DATABASE_TABLE, content, "_id='" + key + "'", null);
			}
		} catch (SQLException ex) {
			Log.e("SQL error", ex.getMessage());
		}
	}
	/**���������� � �� �������� ���� Boolean */
	public void putValue(String key, boolean value){
		putValue(key, Boolean.toString(value));
	}
	/**���������� � �� �������� ���� Integer*/
	public void putValue(String key, int value){
		putValue(key, Integer.toString(value));
	}
	
	/**�������� �� �� ��������� ���� String*/
	public String getString(String key, String defaultValue) {
		try {
			Cursor cursor = db.rawQuery(
					"SELECT value FROM tblParameters WHERE _id = '" + key + "'", null);
			String value = defaultValue;
			if (cursor.moveToFirst()) {
				value = cursor.getString(0);
			}
			cursor.close();
			return value;
		} catch (SQLException ex) {
			Log.e("SQL error", ex.getMessage());
			return null;
		}
	}
	/**�������� �� �� ��������� ���� Integer*/
	public int getInteger(String key, int defaultValue){
		return Integer.parseInt(getString(key, Integer.toString(defaultValue)));
	}
	/**�������� �� �� ��������� ���� Boolean */
	public boolean getBoolean(String key, boolean defaultValue){
		String value = getString(key, Boolean.toString(defaultValue));
		return Boolean.parseBoolean(value);
	}
}
