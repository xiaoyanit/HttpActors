package com.novoda.lib.httpservice.storage.provider;

import static com.novoda.lib.httpservice.utils.Log.Storage.*;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;


public class DatabaseManager extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "com.novoda.lib.httpservice.db";
	private static final int DATABASE_VERSION = 2;
	
	public static interface IntentModel {
		String NAME = "intent";
		
		public static interface Status {
			String consumed = "consumed";
			String received = "received";
			String queued = "queued";
			String started = "started";
			String interrupted = "interrupted";
		}
		
		public static interface Column {
			String status = "status";
			String uri = "uri";
			String id = "_id";
			String modified = "modified";
			String created = "created";
			String filename = "filename";
			String filelength = "filelength";
		}
		
		Uri URI = Uri.parse("content://" + StorageUriMatcher.AUTHORITY + "/" + NAME);
		String CREATE_STM = "create table if not exists intent(" +
				"_id integer primary key on conflict replace, " + 
				"uri text, " +
				"status text, " +
				"modified integer, " +
				"filename text, " +
				"filelength integer, " +
				"created integer);";
		String DROP_STM = "drop table if exists intent;";		
	}
	
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        drop(db);
        onCreate(db);
    }

    private void create(SQLiteDatabase db) {
    	List<String> stms = new ArrayList<String>();
    	stms.add(IntentModel.CREATE_STM);
        exec(db, stms);
    }

    private void drop(SQLiteDatabase db) {
        List<String> stms = new ArrayList<String>();
        stms.add(IntentModel.DROP_STM);
        exec(db, stms);
    }

    private static final void exec(SQLiteDatabase db, List<String> staments) {
        for (String stm : staments) {
            try {
                db.execSQL(stm);
            } catch (RuntimeException re) {
            	e("RuntimeException", re);
            }
        }
    }

}