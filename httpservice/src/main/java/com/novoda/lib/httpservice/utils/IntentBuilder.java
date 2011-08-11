package com.novoda.lib.httpservice.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.net.Uri;

public class IntentBuilder {
	
	private Intent intent;

    private ArrayList<ParcelableBasicNameValuePair> requestParameters = new ArrayList<ParcelableBasicNameValuePair>();

    public IntentBuilder(String url) {
        this(Uri.parse(url));
    }

    public IntentBuilder(String url, String type) {
    	intent = new Intent(Intent.ACTION_SYNC);
    	intent.setDataAndType(Uri.parse(url), type);
    }

    public IntentBuilder(Uri uri) {
        intent = new Intent(Intent.ACTION_SYNC, uri);
    }

    public IntentBuilder asPost() {
        return method(IntentReader.Method.POST);
    }
    
    public IntentBuilder asDelete() {	
        return method(IntentReader.Method.DELETE);
    }

    private IntentBuilder method(int method) {
        intent.putExtra(IntentReader.Extra.method, method);
        return this;
    }

    public IntentBuilder withParams(Map<String, String> parameters) {
        for (Entry<String, String> entry : parameters.entrySet()) {
            requestParameters.add(new ParcelableBasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    public IntentBuilder withParam(String key, String value) {
        requestParameters.add(new ParcelableBasicNameValuePair(key, value));
        return this;
    }

    public IntentBuilder withParams(ArrayList<ParcelableBasicNameValuePair> params) {
        requestParameters.addAll(params);
        return this;
    }

    public Intent build() {
        ArrayList<ParcelableBasicNameValuePair> list = new ArrayList<ParcelableBasicNameValuePair>(
                Collections.unmodifiableList(requestParameters)
        );
        intent.putParcelableArrayListExtra(IntentReader.Extra.params, list);
//        intent.putExtra(IntentReader.Extra.uid, System.nanoTime());
        return intent;
    }
    
    public IntentBuilder withBody(String body) {
        intent.putExtra(IntentReader.Extra.body, body);
        return this;
    }

//	public IntentBuilder withDisableCache() {
//		intent.putExtra(IntentWrapper.Extra.cache_disabled, true);
//        return this;
//	}

//	public IntentBuilder withMiltipartFile(String paramName, String file) {
//		intent.putExtra(IntentWrapper.Extra.multipart_file_name, paramName);
//		intent.putExtra(IntentWrapper.Extra.multipart_file, file);		
//		return this;
//	}
//
//	public IntentBuilder withMiltipartUri(String paramName, String uri) {
//		intent.putExtra(IntentWrapper.Extra.multipart_uri_name, paramName);
//		intent.putExtra(IntentWrapper.Extra.multipart_uri, uri);
//		return this;
//	}
//
//	public IntentBuilder withMultipartExtra(String extraParam, String extraValue) {
//		if(extraParam != null && extraValue != null) {
//			intent.putExtra(IntentWrapper.Extra.multipart_extra_value, extraValue);
//		}
//		return this;
//	}

}
