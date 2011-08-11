package com.novoda.lib.httpservice.actor;

import com.novoda.lib.httpservice.storage.Storage;
import com.novoda.lib.httpservice.utils.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Intent;
import android.os.Bundle;

public class LoggingActor extends Actor {

    public LoggingActor() {
    }
    
	public LoggingActor(Intent intent, Storage storage) {
		super(intent, storage);
	}

	@Override
	public void onCreate(Bundle bundle) {
		Log.v("onCreate");
	}

	@Override
	public void onResume() {
		Log.v("onResume");
	}

	@Override
	public void onPause() {
		Log.v("onPause");
	}

	@Override
	public void onStop() {
		Log.v("onStop");
	}

	@Override
	public void onDestroy() {
		Log.v("onDestroy");
	}

	@Override
	public void onLowMemory() {
		Log.v("onLowMemory");
	}
	
	@Override
	public void onPreprocess(HttpUriRequest method, HttpContext context) {
		Log.v("onPreprocess");
	}

	@Override
	public void onPostprocess(HttpResponse httpResponse, HttpContext context) {
		Log.v("onPostprocess");
	}

	@Override
	public void onThrowable(Throwable t) {
		Log.v("onThrowable");
	}

	@Override
	public void onResponseReceived(HttpResponse httpResponse) {		
		try {
			Log.v("onResponseReceived ");
			String content = EntityUtils.toString(httpResponse.getEntity());
			Log.v("content is : " + content);
		} catch (Exception e) {
			onThrowable(e);
		}
	}
	
	@Override
	public boolean onResponseError(int statusCode) {
		Log.v("onResponseError : " + statusCode);		
		return false;
	}

}
