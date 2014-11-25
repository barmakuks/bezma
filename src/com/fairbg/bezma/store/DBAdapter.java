package com.fairbg.bezma.store;

import com.fairbg.bezma.log.BezmaLog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
	int id = 0;
	public static final String KEY_ROWID = "_id";
	public static final String KEY_VALUE = "value";
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "BezmAndroid";
	private static final String DATABASE_TABLE = "tblParameters";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table tblParameters (_id char(20) primary key, value varchar(255));";

	/**
	 */
	private final Context context;

	/**
	 */
	private DatabaseHelper DBHelper;
	/**
	 */
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
			BezmaLog.i(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS tblRandomQuotes");
			onCreate(db);
		}
	}

	/** открывает базу данных */
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	/** закрывает базу данных */
	public void close() {
		DBHelper.close();
	}

	/**записывает в БД параметр типа String*/
	public void putValue(String key, String value) {
		try {
			final String keyStr = getString(key, "");
			if (keyStr == null || keyStr.equals("")) {
				ContentValues content = new ContentValues();
				content.put(KEY_ROWID, key);
				content.put(KEY_VALUE, value);
				BezmaLog.i("INSERT "+ key, value);
				db.insert(DATABASE_TABLE, null, content);
			} else {
				ContentValues content = new ContentValues();
				content.put(KEY_VALUE, value);
				BezmaLog.i("UPDATE " + key, value);
				db.update(DATABASE_TABLE, content, "_id='" + key + "'", null);
			}
		} catch (SQLException ex) {
			BezmaLog.e("SQL error", ex.getMessage());
		}
	}
	/**записывает в БД параметр типа Boolean */
	public void putValue(String key, boolean value){
		putValue(key, Boolean.toString(value));
	}
	/**записывает в БД параметр типа Integer*/
	public void putValue(String key, int value){
		putValue(key, Integer.toString(value));
	}
	
	/**получаем из БД параметро типа String*/
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
			BezmaLog.e("SQL error", ex.getMessage());
			return null;
		}
	}
	/**получаем из БД параметро типа Integer*/
	public int getInteger(String key, int defaultValue){
		return Integer.parseInt(getString(key, Integer.toString(defaultValue)));
	}
	/**получаем из БД параметро типа Boolean */
	public boolean getBoolean(String key, boolean defaultValue){
		String value = getString(key, Boolean.toString(defaultValue));
		return Boolean.parseBoolean(value);
	}
}
