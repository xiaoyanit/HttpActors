package com.novoda.lib.httpservice.storage;

import com.novoda.lib.httpservice.storage.provider.DatabaseManager.IntentModel;
import com.novoda.lib.httpservice.utils.IntentReader;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class DatabaseStorage implements Storage {
	
	@Override
	public void contendConsumed(Context context, Intent intent) {
		ContentValues cv = asValues(intent, false);
		cv.put(IntentModel.Column.status, IntentModel.Status.consumed);
		context.getContentResolver().update(IntentModel.URI, cv, "_id=?", new String[]{"" + intent.filterHashCode()});
	}

	@Override
	public void contendReceived(Context context, Intent intent) {
		ContentValues cv = asValues(intent, false);
		cv.put(IntentModel.Column.status, IntentModel.Status.received);
		context.getContentResolver().update(IntentModel.URI, cv, "_id=?", new String[]{"" + intent.filterHashCode()});
	}

	@Override
	public void queued(Context context, Intent intent) {
		ContentValues cv = asValues(intent, true);
		cv.put(IntentModel.Column.status, IntentModel.Status.queued);
		context.getContentResolver().insert(IntentModel.URI, cv);
	}
	
	private ContentValues asValues(Intent intent, boolean forInsert) {
		ContentValues cv = new ContentValues();
		IntentReader reader = new IntentReader(intent);
		cv.put(IntentModel.Column.id, intent.filterHashCode());
		if(forInsert) {
			cv.put(IntentModel.Column.created, System.currentTimeMillis());	
			cv.put(IntentModel.Column.uri, reader.asURI().toString());
		}
		cv.put(IntentModel.Column.modified, System.currentTimeMillis());
		return cv;
	}

	@Override
	public ContentValues getIntent(Context context, Intent intent) {
		ContentValues cv = null;
		Cursor c = null;
		try {
			c = context.getContentResolver().query(IntentModel.URI, null, 
				IntentModel.Column.id + "=?", new String[]{"" + intent.filterHashCode()}, null);
			if(c.moveToFirst()) {
				cv = new ContentValues();
				cv.put(IntentModel.Column.id, c.getLong(c.getColumnIndex(IntentModel.Column.id)));
				cv.put(IntentModel.Column.status, c.getString(c.getColumnIndex(IntentModel.Column.status)));
				cv.put(IntentModel.Column.uri, c.getString(c.getColumnIndex(IntentModel.Column.uri)));
				cv.put(IntentModel.Column.filename, c.getString(c.getColumnIndex(IntentModel.Column.filename)));
				cv.put(IntentModel.Column.modified, c.getLong(c.getColumnIndex(IntentModel.Column.modified)));
				cv.put(IntentModel.Column.created, c.getLong(c.getColumnIndex(IntentModel.Column.created)));
			}
		} finally {
			if(c != null) {
				c.close();
			}
		}
		return cv;
	}

	@Override
	public void updateDownload(Context context, Intent intent, String fileName, long fileLength) {
		ContentValues cv = new ContentValues();
		cv.put(IntentModel.Column.modified, System.currentTimeMillis());
		cv.put(IntentModel.Column.filelength, fileLength);
		cv.put(IntentModel.Column.filename, fileName);
		context.getContentResolver().update(IntentModel.URI, cv, IntentModel.Column.id + "=?", new String[]{"" + intent.filterHashCode()});
	}
	

}
