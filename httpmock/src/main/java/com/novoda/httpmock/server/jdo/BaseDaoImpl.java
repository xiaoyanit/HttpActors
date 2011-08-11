package com.novoda.httpmock.server.jdo;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOOptimisticVerificationException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author Luigi Agosti <luigi.agosti@gmail.com>
 */
public abstract class BaseDaoImpl<M> implements BaseDao<M> {
	
    protected interface ParameterType {
        String string_type = "String";
        String long_type = "Long";        
    }
	
	protected static final int PAGE_SIZE = 25;
	
	private Class<M> clazz;
	
	public BaseDaoImpl(Class<M> clazz) {
		this.clazz = clazz;
	}

	protected PersistenceManager getPM() {
		return PMF.get().getPersistenceManager();
	}
	
	@Override
	public void save(final M model) {
		executeInTransaction(new TransactionalCommand() {
			@Override
			public void execute(PersistenceManager pm) {
				pm.makePersistent(model);
			}
		});
	}
	
	@Override
	public void save(final List<M> models) {
		for(M model : models) {
			save(model);
		}
	}
	
	@Override
	public M get(Long id) {
		Key k = KeyFactory.createKey(clazz.getSimpleName(), id);
		PersistenceManager pm = getPM();
		M e = pm.getObjectById(clazz, k);
        if(e == null) {
        	throw new RuntimeException("Entity with element cannot be found!");
        }
        e = pm.detachCopy(e);
        pm.close();
		return e;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<M> searchByProperty(String propertyName, String propertyValue) {
		PersistenceManager pm = getPM();
        Query q = pm.newQuery(clazz);
        q.setFilter(propertyName + " == '" + propertyValue + "'");
        return (List<M>) q.execute(propertyValue);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public M getByProperty(String propertyName, String propertyType, Object propertyValue) {
		PersistenceManager pm = getPM();
		Query q = pm.newQuery(clazz);
		q.setFilter(propertyName + " == propertyParam");
		q.declareParameters(propertyType + " propertyParam");
		q.setRange(0, 1);
		List<M> result = (List<M>) q.execute(propertyValue);
		if(result != null && !result.isEmpty()) {
			M m = result.get(0);
			return pm.detachCopy(m);
		} else {
			return null;
		}
	}
	
	@Override
	public boolean delete(Long id) {
		Key k = KeyFactory.createKey(clazz.getSimpleName(), id);
		PersistenceManager pm = getPM();
		M e = pm.getObjectById(clazz, k);
        if(e == null) {
        	return false;
        }
        pm.deletePersistent(e);
        pm.close();
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteByProperty(String propertyName, String propertyType, Object propertyValue) {
		PersistenceManager pm = getPM();
        Query q = pm.newQuery(clazz);
        q.setFilter(propertyName + " == propertyParam");
        q.declareParameters(propertyType + " propertyParam");
        q.setRange(0, 1);
        List<M> result = (List<M>) q.execute(propertyValue);
        if(result != null && !result.isEmpty()) {
        	M m = result.get(0);
        	pm.deletePersistent(m);
            pm.close();
            return true;
        } else {
        	pm.close();
            return false;
        }
	}
	
	protected void executeInTransaction(TransactionalCommand command) {
		PersistenceManager pm = getPM();
		pm.currentTransaction().begin();
	    try {
			command.execute(pm);
	    	pm.currentTransaction().commit();
	    } catch (JDOOptimisticVerificationException e) {
	    	throw new RuntimeException("JDOOptimisticVerificationException", e);
	    } finally {
	        if (pm.currentTransaction().isActive()) {
	            pm.currentTransaction().rollback();
	        }
	        pm.close();
	    }
	}
	
	@SuppressWarnings("unchecked")
	protected List<M> executeQuery(QueryPersonalizer qp) {
		PersistenceManager pm = getPM();
		Query q = qp.getBasicQuery(pm);
		qp.get(q);
		List<M> pages = (List<M>)q.execute();
		if(pages == null) {
			pages = new ArrayList<M>();
		}
		return pages;
	}
	
	protected abstract class QueryPersonalizer {		
		public static final String QUERY_EQUALS = " == ";
		public static final String AND = " && ";
		private static final String STRING_VALUE_SEPARATOR = "'";
		private static final String IS_NULL = " == null";
		private static final String IS_NOT_NULL = " != null";
		private Class<?> clazz;
		public QueryPersonalizer(Class<?> clazz){
			this.clazz = clazz;
		};
		
		public Query getBasicQuery(PersistenceManager pm) {
			return pm.newQuery(this.clazz);
		}
		
		public void get(Query q) { }
		
		protected String getStringFilter(String property, String value) {
			return property + QUERY_EQUALS + STRING_VALUE_SEPARATOR + value + STRING_VALUE_SEPARATOR;
		}
		
		protected String getIsNullConditionOn(String property) {
			return property + IS_NULL;
		}
		
		protected String getIsNotNullConditionOn(String property) {
			return property + IS_NOT_NULL;
		}
	}
	
	protected abstract class TransactionalCommand {
		public abstract void execute(PersistenceManager pm);
	}
	
}
