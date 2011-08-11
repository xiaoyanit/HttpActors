package com.novoda.lib.httpservice.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.novoda.lib.httpservice.exception.RequestException;

public class IntentReader {
	
	private static final String ENCODING = "UTF-8";
	
	private static final char AND = '&';
	
	private static final String EMPTY = "";
	
	public static interface Extra {
		String method = "novoda.lib.httpservice.extra.METHOD";
		String params = "novoda.lib.httpservice.extra.PARAMS";
		String body = "novoda.lib.httpservice.extra.BODY";
	}
	
	public static interface Method {
		int GET = 0;
		int POST = 1;	
		int DELETE = 2;
	} 
	
	private Intent intent;
	
	public IntentReader(Intent intent) {
		if(intent == null) {
			throw new RequestException("Intent is null! A Intent wrapper need an intent to work properly");
		}
		this.intent = intent;
	}
	
	public boolean isGet() {
		return isMethod(Method.GET);
	}

	public boolean isPost() {
		return isMethod(Method.POST);
	}
	
	public boolean isDelete() {
		return isMethod(Method.DELETE);
	}
	
	private boolean isMethod(int method) {
		if(method == getMethod()) {
			return true;
		}
		return false;
	}
	
	public boolean hasBodyEntity() {
		if(intent.hasExtra(Extra.body)) {
			return true;
		}
		return false;
	}
	
	public String getBodyEntity() {
		return intent.getStringExtra(Extra.body);
	}

	public int getMethod() {
		return intent.getIntExtra(Extra.method, Method.GET);
	}
	
	public Uri getUri() {
		Uri uri = intent.getData();
		if(uri == null) {
			throw new RequestException("Request url and uri are not specified. Need at least one!");
		}
		return uri;
	}
	
	public URI asURI(Uri uri, List<ParcelableBasicNameValuePair> params) {
		StringBuilder query = new StringBuilder(EMPTY);
		if(params != null) {
			query.append(URLEncodedUtils.format(params, ENCODING));
	        if (uri.getQuery() != null && uri.getQuery().length() > 3) {
	            if (params.size() > 0) {
	                query.append(AND);
	            }
	            query.append(uri.getQuery());
	        }
        }
        return asURI(uri, query.toString());
    }
	
	public static final URI asURI(Uri uri, String query) {
        try {
        	if(TextUtils.isEmpty(query)) {
        		query = null;
        	}
            return URIUtils.createURI(uri.getScheme(), uri.getHost(), uri.getPort(),
                    uri.getEncodedPath(), query, uri.getFragment());
        } catch (URISyntaxException e) {
            throw new RequestException("Problem generating the URI with " + uri);
        }
    }
	
	public static final URI asURI(Uri uri) {
        return asURI(uri, EMPTY);
    }
	
	public URI asURI() {	
		return asURI(getUri(), getParams());
	}
	
	public List<ParcelableBasicNameValuePair> getParams() {
		return intent.getParcelableArrayListExtra(Extra.params);
	}

}
