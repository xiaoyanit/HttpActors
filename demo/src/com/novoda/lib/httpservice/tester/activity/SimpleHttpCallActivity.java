
package com.novoda.lib.httpservice.tester.activity;

import com.novoda.lib.httpservice.HttpService;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class SimpleHttpCallActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent("GET", Uri.parse("http://httpmock.appspot.com/test/simplejson"));
        intent.setClass(getApplicationContext(), HttpService.class);
        startService(intent);
    }
}
