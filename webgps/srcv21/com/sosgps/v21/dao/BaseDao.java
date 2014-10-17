package com.sosgps.v21.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

public interface BaseDao {

	public abstract void setInjectionSessionFacotry(
			SessionFactory sessionFacotry);

	public abstract Object merge(Object detachedInstance);

	public abstract Object save(Object transientInstance);

	public abstract Object saveOrUpdate(Object entity);

	public abstract void delete(Object persistentInstance);

	public abstract Object findById(java.lang.Long id, Class<?> clazz);

	public abstract <T> T findById(int id, Class<?> clazz);

	public abstract <T> T findById(Number id, Class<?> clazz);

	public abstract void attachDirty(Object instance);

	public abstract void attachClean(Object instance);

	public abstract int queryForInt(String hql);

	public abstract int queryForInt(String hql, Object[] values);

	public abstract int queryForInt(Query query);

	public abstract int update(String hql, Object... values);

	public abstract int excute(final String hql, final Object... values);

	public abstract void update(Object entity);

	public abstract <T> List<T> findList(String hql, Object... values);

	public abstract <T> List<T> findList(String hql);

	public abstract <T> List<T> findList(String hql, Object[] values,
			int offset, int rowCount);

	public abstract <T> List<T> findList(String hql, int offset, int rowCount);

	public abstract <T> T findObject(String hql, Object... values);

	public abstract String makeExpression(String[] properties);

	public abstract List<?> findByProperty(Class<?> clazz, String propertyName,
			Object propertyValue);

	public abstract List<?> findByExample(Object instance);

	public abstract List<?> findAll(Class<?> clazz);

}
