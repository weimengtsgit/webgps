package com.sosgps.wzt.system.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.orm.RefModuleRole;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.system.dao.RefModuleRoleDao;
import com.sosgps.wzt.system.service.RefModuleRoleService;

public class RefModuleRoleServiceImpl implements RefModuleRoleService{
	private RefModuleRoleDao refModuleRoleDao;
	
	public void saveModuleRole(RefModuleRole transientInstance){
		this.refModuleRoleDao.save(transientInstance);
	}
	/**
	 * ���Ȩ�����ɫ��Ӧ��ϵsos
	 */
	public void saveModuleRole(TRole role,List moduleList,Map orderMap)throws Exception {
		RefModuleRole refModuleRole = new RefModuleRole();
		try{
		for(Iterator it = moduleList.iterator();it.hasNext();){
			String moduleid = (String)it.next();
			TModule tmodule = new TModule();
			tmodule.setId(Long.parseLong(moduleid));
			
			
			//Long order = Long.valueOf((String)orderMap.get(String.valueOf(module.getId())));
			refModuleRole = new RefModuleRole();
			refModuleRole.setTRole(role);
			refModuleRole.setTModule(tmodule);
			//refModuleRole.setModuleOrder(order);
			refModuleRoleDao.save(refModuleRole);
			
		}
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E065: ��ɫȨ�޹���-���ӽ�ɫȨ�޹�ϵʧ�ܣ� "+re);
			throw re;
		}catch(Exception ex){
			SysLogger.sysLogger.error("E065: ��ɫȨ�޹���-���ӽ�ɫȨ�޹�ϵʧ�ܣ� "+ex);
			throw ex;
		}
	}
	/**
	 * ɾ����Ӧ��ɫ����Ȩ��(ref_module_role)
	 * ���¼���Ȩ��
	 */
	public void updateModuleRole(TRole role,List moduleList,Map orderMap)throws Exception {
		RefModuleRole refModuleRole = new RefModuleRole();
		try{
			//����ɾ����ɫ����Ȩ��
			this.refModuleRoleDao.delete(role.getId());
			for(Iterator it = moduleList.iterator();it.hasNext();){
				String moduleid = (String)it.next();
				TModule tmodule = new TModule();
				tmodule.setId(Long.parseLong(moduleid));
				
				
				//Long order = Long.valueOf((String)orderMap.get(String.valueOf(module.getId())));
				refModuleRole = new RefModuleRole();
				refModuleRole.setTRole(role);
				refModuleRole.setTModule(tmodule);
				//refModuleRole.setModuleOrder(order);
				refModuleRoleDao.save(refModuleRole);
				
			}

		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E065: ��ɫȨ�޹���-���½�ɫȨ�޹�ϵʧ�ܣ� "+re);
			throw re;
		}catch(Exception ex){
			SysLogger.sysLogger.error("E065: ��ɫȨ�޹���-���½�ɫȨ�޹�ϵʧ�ܣ� "+ex);
			throw ex;
		}
	}
	public RefModuleRoleDao getRefModuleRoleDao() {
		return refModuleRoleDao;
	}
	public void setRefModuleRoleDao(RefModuleRoleDao refModuleRoleDao) {
		this.refModuleRoleDao = refModuleRoleDao;
	}
}
