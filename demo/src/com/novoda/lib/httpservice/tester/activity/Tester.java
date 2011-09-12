package com.novoda.lib.httpservice.tester.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.novoda.lib.httpservice.R;
import com.novoda.lib.httpservice.tester.service.AppService;
import com.novoda.lib.httpservice.tester.util.Log;
import com.novoda.lib.httpservice.utils.IntentBuilder;

public class Tester extends BaseActivity {

	private static final String HOST = "http://httpmock.appspot.com/test/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tester);
		final EditText edit = ((EditText) findViewById(R.id.requestNumber));
		final Button start = ((Button)findViewById(R.id.start));
		
		final Spinner httpMethods = (Spinner) findViewById(R.id.http_method);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.http_methods, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    httpMethods.setAdapter(adapter);
	    
	    final Spinner httpResponseTypes = (Spinner) findViewById(R.id.http_respose_types);
	    ArrayAdapter<CharSequence> adapterTypes = ArrayAdapter.createFromResource(
	            this, R.array.http_response_types, android.R.layout.simple_spinner_item);
	    adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    httpResponseTypes.setAdapter(adapterTypes);
	    
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String method = httpMethods.getSelectedItem().toString();
				String responseType = httpResponseTypes.getSelectedItem().toString();
				String numberOfCalls = edit.getText().toString();
				if(TextUtils.isEmpty(numberOfCalls)) {
					numberOfCalls = "1";
				}
				Log.v("Value : " + method + ", " + responseType + ", " + numberOfCalls);
				for(int i= 0; i<Integer.valueOf(numberOfCalls); i++) {
					startService(buildIntent(method, responseType));
				}
			}
		});
	}
	
	private Intent buildIntent(String method, String responseType) {
		String relativePath;
		if("404".equalsIgnoreCase(responseType)) {
			relativePath = "notfound";
		} else if("500".equalsIgnoreCase(responseType)) {
			relativePath = "servererror";
		} else if("external res".equalsIgnoreCase(responseType)) {
			relativePath = "staticresource";
		} else if("string res".equalsIgnoreCase(responseType)) {
			relativePath = "staticresource";
		} else {
			relativePath = "success";
		}
		//NOTE the type can drive the type of actor to run
		IntentBuilder builder = new IntentBuilder(HOST + relativePath, AppService.TYPE1);
		if("post".equalsIgnoreCase(method)) {
			builder.withBody("{json:\"xx\"}").asPost();
		}
		return builder.build();
	}
	
}
