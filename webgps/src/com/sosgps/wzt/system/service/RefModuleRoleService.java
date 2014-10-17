package com.sosgps.wzt.system.service;

import java.util.List;
import java.util.Map;

import com.sosgps.wzt.orm.RefModuleRole;
import com.sosgps.wzt.orm.TRole;

public interface RefModuleRoleService {
	public void saveModuleRole(RefModuleRole transientInstance);
	public void saveModuleRole(TRole role,List moduleList,Map orderMap)throws Exception;
	public void updateModuleRole(TRole role,List moduleList,Map orderMap)throws Exception;
}
