package com.novoda.lib.httpservice.provider;

import java.io.InputStream;

import com.novoda.lib.httpservice.exception.RequestException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

/**
 * Wrapper for the HttpResponse.
 * 
 * @author luigi
 *
 */
public class Response {

	private HttpResponse httpResponse;
	
	private InputStream content;
	
	public void setHttpResponse(HttpResponse httpResponse) {
		this.httpResponse  = httpResponse;
	}
	
	public HttpResponse getHttpResponse() {
		return this.httpResponse;
	}

	public InputStream getContent() {
		if(content != null) {
			return content;
		}
		InputStream content = null;
		HttpEntity entity = null;
		try {
			entity = httpResponse.getEntity();
			content = entity.getContent();
		} catch (Exception e) {
			throw new RequestException("Exception getting the content", e);
		} finally {
			try {
				entity.consumeContent();
			} catch (Exception e) {
				throw new RequestException("Exception consuming content", e);
			}
		}
		return content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}
	
	public String getContentAsString() {
		HttpEntity entity = null;
		try {
			entity = httpResponse.getEntity();
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			throw new RequestException("Exception converting entity to string", e);
		} finally {
			try {
				entity.consumeContent();
			} catch (Exception e) {
				throw new RequestException("Exception consuming content", e);
			}
		}
	}
	
	public int getStatusCode() {
		return getHttpResponse().getStatusLine().getStatusCode();
	}
	
}
