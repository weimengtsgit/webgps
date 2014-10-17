package com.sosgps.wzt.system.service;

import java.util.List;

import com.sosgps.wzt.orm.TModuleGroup;

public interface ModuleGroupService {
	public void saveModuleGroup(TModuleGroup moduleGroup);
	public void deleteModuleGroup(TModuleGroup moduleGroup);
	public void updateModuleGroup(TModuleGroup moduleGroup);
	public List queryModuleGroupByLevel(Long level);
    public List queryChildGroup(Long parentId);
    public boolean deleteAll(Long[] longIds) throws Exception;
	public List getAll();
	
    public TModuleGroup retrieveModuleGroup(Long id);
}
