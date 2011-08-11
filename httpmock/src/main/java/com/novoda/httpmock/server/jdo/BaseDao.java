package com.novoda.httpmock.server.jdo;

import java.util.List;

/**
 * @author Luigi Agosti <luigi.agosti@gmail.com>
 */
public interface BaseDao<M> {

	void save(M model);

	void save(List<M> models);
	
	M get(Long id);

	boolean delete(Long id);

	boolean deleteByProperty(String propertyName, String propertyType,
			Object propertyValue);

	M getByProperty(String propertyName, String propertyType,
			Object propertyValue);

	List<M> searchByProperty(String propertyName, String propertyValue);
	
}
