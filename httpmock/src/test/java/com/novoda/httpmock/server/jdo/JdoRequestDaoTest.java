package com.novoda.httpmock.server.jdo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.novoda.httpmock.shared.Request;

public class JdoRequestDaoTest {
	
	private LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());
    
    protected DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    
    @Before
    public void setUp() {
        helper.setUp();
    }
    
    @After
    public void tearDown() {
        helper.tearDown();
    }
    
    private JdoRequestDao dao = new JdoRequestDao();
    
    private static final String SAMPLE_URL = "http://httpmock.appspot.com/success";
    
    @Test
    public void shouldStoreRequest() {
    	Request r = new Request(SAMPLE_URL);
    	dao.save(r);
    	
    	Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(Request.URL_PROPERTY, SAMPLE_URL);

    	assertRequestHasProperties(r.getId(), properties);
    }
    
    @Test
    public void shouldGetRequestByUrl() {
    	Entity e = new Entity(Request.class.getSimpleName());
		e.setProperty(Request.URL_PROPERTY, SAMPLE_URL);
		e.setProperty(Request.COUNT_PROPERTY, Long.valueOf(0L));
		e.setProperty(Request.RESPONSE_PROPERTY, "404");
		ds.put(e);
		
		Request r = dao.getByUrl(SAMPLE_URL);
		
		assertNotNull(r);
		assertEquals(0L, r.getCount().longValue(), 0.1);
		assertEquals(SAMPLE_URL, r.getUrl());
    }
    
    @Test
    public void shouldGetRequestByUrlReturnNotNullIfThereAreNotRequestStored() {
		Request r = dao.getByUrl("hello");
		assertNull(r);
    }

    private void assertRequestHasProperties(long id, Map<String, Object> properties) {
    	DatastoreAssert.assertEntintySaved(ds, Request.class.getSimpleName(), id, properties);
    }
    
}
