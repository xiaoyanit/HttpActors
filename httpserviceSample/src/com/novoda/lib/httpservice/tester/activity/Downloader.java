package com.novoda.lib.httpservice.tester.activity;

import java.io.File;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.novoda.lib.httpservice.R;
import com.novoda.lib.httpservice.storage.provider.DatabaseManager.IntentModel;
import com.novoda.lib.httpservice.tester.service.AppService;
import com.novoda.lib.httpservice.utils.FileReader;
import com.novoda.lib.httpservice.utils.IntentBuilder;

public class Downloader extends BaseActivity {

	private static final String HOST = "http://httpmock.appspot.com/test.zip";
	
//	private static final String HOST = "http://httpmock.appspot.com/test/success";

	private Intent intent = new IntentBuilder(HOST, AppService.ZIP).build();
	private int filterhashcode = intent.filterHashCode();
	
	private TextView progress;
	
	private Button delete;
	
	private Button start;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.downloader);
	
		prepareStartButton();
		prepareDeleteButton();
		
		File file = exists();
		if(file == null) {
			enableDownload();
		} else {
			enableDelete();
		}
		progress = (TextView)findViewById(R.id.progress);
	}
	
	private void prepareStartButton() {
		start = ((Button)findViewById(R.id.download));
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				start.setEnabled(false);
				registerObserver();
				startService(intent);
			}
		});
	}
	
	private void prepareDeleteButton() {
		delete = ((Button)findViewById(R.id.delete));
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				File file = exists();
				if(file != null) {
					FileReader fileReader = new FileReader();					
					fileReader.delete(file.getAbsolutePath());
				}
				getContentResolver().delete(IntentModel.URI, "_id = ?", new String[]{"" + filterhashcode});
				enableDownload();
			}
		});
	}
	
	private void enableDownload() {
		start.setEnabled(true);
		delete.setEnabled(false);		
	}
	
	private void enableDelete() {
		start.setEnabled(false);
		delete.setEnabled(true);
	}
	
	
	private File exists() {
		Cursor c = managedQuery(IntentModel.URI, null, "_id = ?", new String[]{"" + filterhashcode}, null);
		if(c.moveToNext()) {
			String filename = c.getString(c.getColumnIndex(IntentModel.Column.filename));
			File file = new File(filename);
			if(file.exists()) {
				return file;
			}
			return null; 
		}
		return null;
	}
	
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		registerObserver();
	}
	
	@Override
	protected void onPause() {	
		getContentResolver().unregisterContentObserver(syncObserver);
		super.onPause();
	}
	
	private ContentObserver syncObserver = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			delete.setEnabled(false);
			Cursor c = managedQuery(IntentModel.URI, null, "_id = ?", new String[]{"" + filterhashcode}, null);
			if(c.moveToNext()) {
				Long filelenght = c.getLong(c.getColumnIndex(IntentModel.Column.filelength));				
				progress.setText("dowloaded size : " + filelenght);
				String status = c.getString(c.getColumnIndex(IntentModel.Column.status));
				if("consumed".equals(status)) {
					enableDelete();
				}
			}
		}
	};
	
	private void registerObserver(){
		if(filterhashcode != 0) {
			getContentResolver().registerContentObserver(Uri.withAppendedPath(IntentModel.URI, "" + filterhashcode), true, syncObserver);
		}
	}
	
}
