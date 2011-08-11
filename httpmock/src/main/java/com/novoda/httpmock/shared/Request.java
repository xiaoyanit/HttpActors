package com.novoda.httpmock.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Request implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String URL_PROPERTY = "url";

	public static final String COUNT_PROPERTY = "count";

	public static final String RESPONSE_PROPERTY = "response";
	
	private static final Long DEFAULT_COUNT = Long.valueOf(0L);
	private static final Integer DEFAULT_TYPE = Integer.valueOf(0);
	
	public static final Integer NOT_FOUND_404 = DEFAULT_TYPE;
	public static final Integer SERVER_ERROR_500 = Integer.valueOf(1);
	public static final Integer STATIC_RESOURCE = Integer.valueOf(2);
	public static final Integer EXTERNAL_RESOURCE = Integer.valueOf(3);
	public static final Integer STRING_RESOURCE = Integer.valueOf(4);
	
	public static final Integer GET = Integer.valueOf(1);
	public static final Integer POST = Integer.valueOf(2);

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent private String url;	
	@Persistent private String resource;	
	@Persistent private Long count;
	@Persistent private Integer type;
	@Persistent private Integer method;
	@Persistent private String body;	
	
	public Request() {
		setCount(DEFAULT_COUNT);
		setType(DEFAULT_TYPE);
	}
	
	public Request(String url) {
		this();
		setUrl(url);
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getCount() {
		return count;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return type;
	}

	public boolean isExternalResource() {
		if(getType() == EXTERNAL_RESOURCE) {
			return true;
		}
		return false;
	}

	public boolean isServerException() {
		if(getType() == SERVER_ERROR_500) {
			return true;
		}
		return false;
	}

	public boolean isNotFound() {
		if(getType() == NOT_FOUND_404) {
			return true;
		}
		return false;
	}

	public boolean isStaticResource() {
		if(getType() == STATIC_RESOURCE) {
			return true;
		}
		return false;
	}

	public void setMethod(Integer method) {
		this.method = method;
	}
	
	public int getMethod() {
		return method;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}
	
}
