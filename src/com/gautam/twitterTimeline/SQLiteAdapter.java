package com.gautam.twitterTimeline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteAdapter {

	private static final String MYDATABASE_NAME = "TWITTERDB";
	private static final String MYDATABASE_TABLE = "TIMELINE";
	private static final int MYDATABASE_VERSION = 1;
	public static final String KEY_ID = "_id";
	public static final String KEY_COLUMN1 = "ScreenName";
	public static final String KEY_COLUMN2 = "TwittterName";
	public static final String KEY_COLUMN3 = "ProfileImage";
	public static final String KEY_COLUMN4 = "Tweet";
	public static final String KEY_COLUMN5 = "TweetTime";

	private static final String SCRIPT_CREATE_DATABASE = "create table "
			+ MYDATABASE_TABLE + " (" + KEY_ID
			+ " integer primary key autoincrement, " + KEY_COLUMN1 + " text,"
			+ KEY_COLUMN2 + " text," + KEY_COLUMN3 + " text," + KEY_COLUMN4
			+ " text unique , " + KEY_COLUMN5 + " text );";

	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;

	private Context context;

	public SQLiteAdapter(Context c) {
		context = c;
	}

	public SQLiteAdapter openToRead() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;
	}

	public SQLiteAdapter openToWrite() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		sqLiteHelper.close();
	}

	public long insert(String value1, String value2, String value3,
			String value4, String value5) {
		ContentValues values = new ContentValues();
		values.put(KEY_COLUMN1, value1.toString());
		values.put(KEY_COLUMN2, value2.toString());
		values.put(KEY_COLUMN3, value3.toString());
		values.put(KEY_COLUMN4, value4.toString());
		values.put(KEY_COLUMN5, value5.toString());
		Log.v("", "inserted");
		return sqLiteDatabase.insert(MYDATABASE_TABLE, null, values);
	}

	public int deleteAll() {
		return sqLiteDatabase.delete(MYDATABASE_TABLE, null, null);
	}

	public Cursor queueAll() {
		String[] columns = new String[] { KEY_ID, KEY_COLUMN1, KEY_COLUMN2,
				KEY_COLUMN3, KEY_COLUMN4, KEY_COLUMN5 };
		Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, null, null,
				null, null, null, "_id desc");

		return cursor;
	}

	public class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(SCRIPT_CREATE_DATABASE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

	}

}