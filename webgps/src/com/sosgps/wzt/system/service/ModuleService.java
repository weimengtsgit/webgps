package com.sosgps.wzt.system.service;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TModule;

public interface ModuleService {
	public boolean saveModule(TModule module);
	public void deleteModule(TModule module);
	public boolean deleteAll(Long[] longIds);
	public List queryModuleList(Object ob);
	public List queryAllModuleList();
	public List queryModuleByGradeList(Long ob);//����Ȩ�޵ȼ����Ȩ���б�
	public List queryModuleByEmpCode(String empCode);//����empCode ���ָ����ҵ��Ȩ��
	public TModule retrieveModule(Long id);
	public void updateModule(TModule module);
	public TModule findByModuleCode(String moduleCode);
	public Page<TModule> listModule(String entCode, int page, int limitint,
			String searchValue);
}
