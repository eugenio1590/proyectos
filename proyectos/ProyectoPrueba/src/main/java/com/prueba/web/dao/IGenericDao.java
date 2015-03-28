package com.prueba.web.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IGenericDao<T, ID> {
	public T findByPrimaryKey(ID id);
	public List<T> findAll(int first, int numberElements);
	public List<T> findAll();
	
	public List<T> findByExample(T exampleInstance, String[] excludeProperty, 
			boolean caseSensitive, boolean enableLike);
	public List<T> findByExample(T exampleInstance, String[] excludeProperty);
	
	public T save(T entity);
	public void delete(T entity);
	public T update(T entity);
	public long countAll();
	
	//
	public void beginTransaction();
	public void commitTransaction();
	public T refresh(T entity);
	public void rollbackTransaction();
	//
	
	public Collection<T> excuteJPQL(String query, Map<String, Object> params, 
									int first, int numberElements);
	public Collection<T> excuteJPQL(String query);
	public void close();
}
