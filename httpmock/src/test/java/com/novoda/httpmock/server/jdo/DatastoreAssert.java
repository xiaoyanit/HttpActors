package com.novoda.httpmock.server.jdo;

import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author Luigi Agosti <luigi.agosti@gmail.com>
 */
public class DatastoreAssert {
	
	public static void assertEntintySaved(DatastoreService ds, Key key){
		try {
			Entity entity = ds.get(key);
			assertNotNull(entity);
		} catch(EntityNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static void assertEntintySaved(DatastoreService ds, Key key, Map<String, Object> properties){
		try {
			Entity entity = ds.get(key);
			assertNotNull(entity);
			for(Entry<String, Object> entry : properties.entrySet()) {
				Assert.assertEquals(entry.getValue(), entity.getProperty(entry.getKey()));
			}
		} catch(EntityNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static void assertEntintySaved(DatastoreService ds, String kind, Long id, Map<String, Object> properties){
		assertEntintySaved(ds, KeyFactory.createKey(kind, id), properties);
	}

	public static void assertEquals(DatastoreService ds, String simpleName, Long id, String propertyName, Object value) {
		Assert.assertEquals(value, getProperty(ds, simpleName, id, propertyName));
	}

	public static Object getProperty(DatastoreService ds, String kind, Long id, String propertyName) {
		try {
			Entity entity = ds.get(KeyFactory.createKey(kind, id));
			assertNotNull(entity);
			return entity.getProperty(propertyName);
		} catch(EntityNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
}
