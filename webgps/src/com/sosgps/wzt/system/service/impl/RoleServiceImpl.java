package com.sosgps.wzt.system.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefModuleRole;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.system.dao.TRoleDao;
import com.sosgps.wzt.system.service.RoleService;
/**
 * ��ɫ�������
 * @author zhangwei
 *
 */
public class RoleServiceImpl implements RoleService{
	private TRoleDao tRoleDao;
	
	public void saveRole(TRole role){
		try{
			this.tRoleDao.save(role);

		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: ��ɫ����-�����ɫʧ��,����ԭ��"+re.getMessage(),re);
			throw re;
		}
	}
	public void deleteRole(TRole role){
		
	}
	
	public TRole retrieveRole(java.lang.Long id){
		TRole tRole = null;
		try{
			tRole = this.tRoleDao.findById(id);
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: ��ɫ����-��ȡ��ɫʧ��,����ԭ��"+re.getMessage(),re);
		}catch(Exception ex){
			SysLogger.sysLogger.error("E062: ��ɫ����-��ȡ��ɫʧ��,����ԭ��"+ex.getMessage(),ex);
		}
		return tRole;
	}
	
	public List getAllRole(){
		return this.tRoleDao.findAll();
	}
	public List getRoleByEmpCode(String empCode){
		return this.tRoleDao.findByEmpCode(empCode);
	}
	public void updateRole(TRole role){
		try{
			 this.tRoleDao.update(role);
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: ��ɫ����-���½�ɫʧ��,����ԭ��"+re.getMessage(),re);
			re.fillInStackTrace();
			throw re;
		}catch(Exception ex){
			SysLogger.sysLogger.error("E062: ��ɫ����-���½�ɫʧ��,����ԭ��"+ex.getMessage(),ex);
			ex.fillInStackTrace();
		}
	}
	
	public void updateRole(TRole role,List moduleList,Map order){
		this.tRoleDao.update(role);
		/*����Ȩ�޽�ɫ��ϵ��
		  ����ɾ����Ӧ��ɫ��Ȩ��
		  ������ӽ�ɫȨ�޹�ϵ
		*/
	}
	/**
	 * ɾ����ɫ
	 * ɾ����ɫ��Ҫɾ����ɫ���������û�
	 */
	public boolean deleteAll(Long[] longIds){
		return this.tRoleDao.deleteAll(longIds);
		
	}
	public TRoleDao getTRoleDao() {
		return tRoleDao;
	}
	public void setTRoleDao(TRoleDao roleDao) {
		tRoleDao = roleDao;
	}
	
	
	public TRole findByRoleCode(String entCode, String roleCode) {
		TRole instance = new TRole();
		instance.setEmpCode(entCode);
		instance.setRoleCode(roleCode);
		List re = tRoleDao.findByExample(instance);
		if(re!=null && re.size()==1)
			return (TRole)re.get(0);
		return null;
	}
	
	public TRole findRoleAndModulesByRoleCode(String entCode, String roleCode) {
		try {
			return tRoleDao.findRoleAndModulesByRoleCode(entCode, roleCode);
		} catch (Exception e) {
			SysLogger.sysLogger.error("���ݽ�ɫ�����ý�ɫ�Լ�����Ȩ�޴���", e);
			return null;
		}
	}
	
	public Page<TRole> listRole(String entCode, int page, int limitint,
			String searchValue) {
		try{
			 return this.tRoleDao.listRole(entCode, page, limitint,
						searchValue);
		}catch(Exception ex){
			SysLogger.sysLogger.error("E062: ��ɫ����-��ѯ��ɫ�б�ʧ��,����ԭ��"+ex.getMessage(),ex);
			ex.fillInStackTrace();
		}
		return null;
	}
	public TRole queryRoleAndModulesById(Long roleId) {
		try {
			return tRoleDao.queryRoleAndModulesById(roleId);
		} catch (Exception e) {
			SysLogger.sysLogger.error("���ݽ�ɫid���ɫ�Լ�����ӵ�е�Ȩ���б����", e);
			return null;
		}
	}
}
