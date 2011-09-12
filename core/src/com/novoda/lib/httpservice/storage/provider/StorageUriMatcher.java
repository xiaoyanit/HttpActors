package com.novoda.lib.httpservice.storage.provider;

import com.novoda.lib.httpservice.storage.provider.DatabaseManager.IntentModel;

import android.content.UriMatcher;
import android.net.Uri;


public class StorageUriMatcher extends UriMatcher {
	
	public static final int INTENT_INCOMING_ITEM = 10;
	public static final int INTENT_INCOMING_COLLECTION = 20;
	public static final String INTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.httpservice.intent";
	public static final String INTENT_COLLECTION_TYPE = "vnd.android.cursor.dir/vnd.httpservice.intent";
	public static final String AUTHORITY = "com.novoda.lib.httpservice";
	
	
	public StorageUriMatcher(int code) {
        super(code);
        setUp();
    }

	public StorageUriMatcher() {
		super(UriMatcher.NO_MATCH);
        setUp();
    }

    public void setUp() {
        add(IntentModel.NAME, INTENT_INCOMING_COLLECTION);
        add(IntentModel.NAME + "/#", INTENT_INCOMING_ITEM);
    }

    public void add(String path, int code) {
        super.addURI(AUTHORITY, path, code);
    }

    public static final String[] getIdSelectionArgumentsFromUri(Uri uri) {
        return new String[] {
            uri.getPathSegments().get(1)
        };
    }
}
