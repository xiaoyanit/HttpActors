package com.novoda.httpmock.server.jdo;

import com.novoda.httpmock.shared.Request;

public class JdoRequestDao extends BaseDaoImpl<Request> implements BaseDao<Request> {
	
	public JdoRequestDao() {
		super(Request.class);
	}

	public Request getByUrl(String sampleUrl) {
		return getByProperty(Request.URL_PROPERTY, ParameterType.string_type, sampleUrl);
	}
	
}
