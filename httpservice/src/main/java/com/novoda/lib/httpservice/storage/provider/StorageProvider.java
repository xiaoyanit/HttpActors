package com.novoda.lib.httpservice.storage.provider;

import static com.novoda.lib.httpservice.utils.Log.Storage.e;
import static com.novoda.lib.httpservice.utils.Log.Storage.v;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.novoda.lib.httpservice.storage.provider.DatabaseManager.IntentModel;
import com.novoda.lib.httpservice.utils.Log;

public class StorageProvider extends ContentProvider {

	private StorageUriMatcher matcher = new StorageUriMatcher();

	private DatabaseManager databaseManager;

	private SQLiteDatabase database;

	protected SQLiteDatabase getDataBase() {
		if (database == null) {
			databaseManager = new DatabaseManager(getContext());
			database = databaseManager.getWritableDatabase();
		}
		return database;
	}

	@Override
	public String getType(Uri uri) {
		String type = null;
		switch (matcher.match(uri)) {
			case StorageUriMatcher.INTENT_INCOMING_COLLECTION: {
				type = StorageUriMatcher.INTENT_COLLECTION_TYPE;
				break;
			}
			case StorageUriMatcher.INTENT_INCOMING_ITEM: {
				type = StorageUriMatcher.INTENT_ITEM_TYPE;
				break;
			}
			default: {
				if(Log.errorLoggingEnabled()) {
					e("Problem with uri : " + uri);
				}
				throw new RuntimeException("Problem with uri : " + uri);
			}
		}
		return type;
	}

	@Override
	public boolean onCreate() {
		return false;
	}

	@Override
	public int delete(Uri uri, String selection, String[] args) {
		int rowAffected = 0;
        switch (matcher.match(uri)) {
            case StorageUriMatcher.INTENT_INCOMING_COLLECTION: {
                rowAffected = database.delete(IntentModel.NAME, selection, args);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
        return rowAffected;
	}

	@Override
	public Uri insert(Uri uri, ContentValues cv) {
		Uri result = null;
		switch (matcher.match(uri)) {
			case StorageUriMatcher.INTENT_INCOMING_COLLECTION: {
				long id = getDataBase().insert(IntentModel.NAME, "", cv);
				result = Uri.withAppendedPath(uri, "" + id);
				getContext().getContentResolver().notifyChange(result, null);
				break;
			}
			default: {
				if(Log.errorLoggingEnabled()) {
					e("Problem with insert, not Implemented for : " + uri);
				}
				throw new RuntimeException(
						"Problem with query, not Implemented for : " + uri);
			}
		}
		return result;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor;
		switch (matcher.match(uri)) {
			case StorageUriMatcher.INTENT_INCOMING_COLLECTION: {
				cursor = getDataBase().query(IntentModel.NAME, projection,
						selection, selectionArgs, null, null, sortOrder, null);
				break;
			}
			case StorageUriMatcher.INTENT_INCOMING_ITEM: {
				cursor = getDataBase().query(IntentModel.NAME, projection,
						selection, selectionArgs, null, null, sortOrder);
				break;
			}
			default: {
				if(Log.errorLoggingEnabled()) {
					e("Problem with query, not Implemented for : " + uri);
				}
				throw new RuntimeException(
						"Problem with query, not Implemented for : " + uri);
			}
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int rowUpdated = 0;
		switch (matcher.match(uri)) {
			case StorageUriMatcher.INTENT_INCOMING_COLLECTION: {
				rowUpdated = getDataBase().update(IntentModel.NAME,
						values, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(Uri.withAppendedPath(uri, 
						selectionArgs[0] + "/" + values.get(IntentModel.Column.status)), null);
				break;
			}
			default: {
				if(Log.errorLoggingEnabled()) {
					e("Problem with query, not Implemented for : " + uri);
				}
				throw new RuntimeException(
						"Problem with query, not Implemented for : " + uri);
			}
		}
		return rowUpdated;
	}

}
