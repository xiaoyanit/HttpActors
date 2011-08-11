package com.novoda.lib.httpservice.storage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

public interface Storage {
	
	void queued(Context context, Intent intent);
	
	void contendReceived(Context context, Intent intet);
	
	void contendConsumed(Context context, Intent intet);
	
	ContentValues getIntent(Context context, Intent intent);
	
	void updateDownload(Context context, Intent intent, String filename, long currentSize);
	
}
