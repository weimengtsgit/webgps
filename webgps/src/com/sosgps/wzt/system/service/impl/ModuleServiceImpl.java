package com.sosgps.wzt.system.service.impl;

import java.util.List;

import com.sosgps.wzt.commons.util.DateUtil;
import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TModule;

import com.sosgps.wzt.system.dao.TModuleDao;
import com.sosgps.wzt.system.service.ModuleService;
/**
 * Ȩ�޹�������
 * @author Administrator
 *
 */
public class ModuleServiceImpl implements ModuleService{
	private TModuleDao tModuleDao;
	
	/**
	 * ����Ȩ��
	 */
	public boolean saveModule(TModule module){
		return this.tModuleDao.save(module);
	}
	/**
	 * ɾ��Ȩ��
	 */
	public void deleteModule(TModule module){
		this.tModuleDao.delete(module);
	}
	/**
	 * ����id��ѯģ��
	 */
	public TModule retrieveModule(Long id){
		return this.tModuleDao.findById(id);
	}
	/*
	 * ��������Ȩ�� 
	 */
	public List queryModuleList(Object ob){
		return this.tModuleDao.findByUsageFlag(ob);
	}
	public boolean deleteAll(Long[] longIds){
		return this.tModuleDao.deleteAll(longIds);
	}

	/**
	 * ����Ȩ��
	 * @return
	 */
	public void updateModule(TModule module){
		try{
			this.tModuleDao.update(module);
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: Ȩ�޹���-ɾ��Ȩ��ʧ��,����ԭ��"+re.getMessage(),re);
		}catch(Exception ex){
			SysLogger.sysLogger.error("E062: Ȩ�޹���-ɾ��Ȩ��ʧ��,����ԭ��"+ex.getMessage(),ex);
		}
	}
	
	public List queryAllModuleList(){
		List list = null;
		try{
			list = this.tModuleDao.fingAllModuleList();
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: Ȩ�޹���-��ѯȨ��ʧ��,����ԭ��"+re.getMessage(),re);
		}
		return list;
	}
	public List queryModuleByGradeList(Long grade){
		List list  = null;
		try{
			list = this.tModuleDao.findByModuleGradeList(grade);
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: Ȩ�޹���-��ѯȨ��ʧ��,����ԭ��"+re.getMessage(),re);
		}
		return list;
	}
	
	public List queryModuleByEmpCode(String empCode){
		List list  = null;
		try{
			list = this.tModuleDao.findByModuleEmpCodeList(empCode);
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: Ȩ�޹���-��ѯȨ��ʧ��,����ԭ��"+re.getMessage(),re);
		}
		return list;
	}
	
	
	public TModuleDao getTModuleDao() {
		return tModuleDao;
	}
	public void setTModuleDao(TModuleDao moduleDao) {
		tModuleDao = moduleDao;
	}
	public TModule findByModuleCode(String moduleCode) {
		List re = tModuleDao.findByProperty("moduleCode", moduleCode);
		if(re!=null && re.size()==1){
			return (TModule)re.get(0);
		}
		return null;
	}
	public Page<TModule> listModule(String entCode, int page, int limitint,
			String searchValue) {
		try {
			return tModuleDao.listModule(entCode, page, limitint,
					searchValue);
		} catch (Exception e) {
			SysLogger.sysLogger.error("E062: Ȩ�޹���-��ѯȨ���б�ʧ��",e);
		}
		return null;
	}
}
