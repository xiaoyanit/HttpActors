
package com.novoda.lib.httpservice.actor;

import com.novoda.lib.httpservice.controller.ContextHttpWrapper;
import com.novoda.lib.httpservice.storage.Storage;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class Actor extends Handler {

    public static final int ON_CREATE = 1;

    public static final int ON_PAUSE = 2;

    public static final int ON_DESTROY = 3;

    public static final int ON_RESUME = 4;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case ON_CREATE:
                onCreate(msg.getData());
                break;
            case ON_RESUME:
                onResume();
                break;
            case ON_PAUSE:
                onPause();
                break;
            case ON_DESTROY:
                onDestroy();
                break;
        }
        super.handleMessage(msg);
    }

    private Intent intent;

    private ContextHttpWrapper context;

    private Storage storage;

    public Actor() {
    }

    public Actor(Intent intent, Storage storage) {
        this.intent = intent;
        this.setStorage(storage);
    }

    public void applyContext(ContextHttpWrapper context) {
        this.context = context;
    }

    public Context getHttpContext() {
        return context;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent newIntent) {
        intent = newIntent;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Storage getStorage() {
        return storage;
    }

    protected void onCreate(Bundle bundle) {
        if (storage != null) {
            storage.queued(context, intent);
        }
    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onStop() {

    }

    public void onDestroy() {
        storage.contendConsumed(context, intent);
    }

    public void onLowMemory() {

    }

    public void onPreprocess(HttpUriRequest method, HttpContext context) {
        // TODO Auto-generated method stub

    }

    public void onPostprocess(HttpResponse httpResponse, HttpContext context2) {
        // TODO Auto-generated method stub

    }

	public void onThrowable(Throwable t) {
        // TODO Auto-generated method stub

    }

    public void onResponseReceived(HttpResponse httpResponse) {
        if (storage != null) {
            storage.contendReceived(context, intent);
        }
    }

    public boolean onResponseError(int statusCode) {
        // TODO Auto-generated method stub
        return false;
    }

    public void onHeaderReceived(HttpResponse httpResponse) {
        // TODO Auto-generated method stub

    }

}
