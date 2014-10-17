package com.sosgps.wzt.system.dao;

import java.util.List;

import com.sosgps.wzt.exception.WZTException;
import com.sosgps.wzt.orm.RefUserRole;

public interface RefUserRoleDao {
	public void save(RefUserRole transientInstance) throws WZTException;
	public void update(RefUserRole transientInstance) ;
	public void delete(RefUserRole persistentInstance);
	public RefUserRole findById( java.lang.Long id);
	public List findByProperty( String propertyName,Object  value);
}
