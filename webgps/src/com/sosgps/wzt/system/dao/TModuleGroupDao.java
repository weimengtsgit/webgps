package com.sosgps.wzt.system.dao;

import java.util.List;

import com.sosgps.wzt.orm.TModuleGroup;

public interface TModuleGroupDao {
	public List findByGroupLevel(Object groupLevel);
	public void save(TModuleGroup transientInstance);
	public TModuleGroup findById( java.lang.Long id);
	public void delete(TModuleGroup tModuleGroup);
	public void update(TModuleGroup persistentInstance);
	public boolean deleteAll(Long[] ids) throws Exception;
	public List findAll();
}
