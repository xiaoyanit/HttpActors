
package com.novoda.lib.httpservice.processor;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import java.io.IOException;

public class Processor implements IProcessor {

    private Context context;

    @Override
    public void onCreate(Bundle fromXml) {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException,
            IOException {
    }

    public void attach(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
