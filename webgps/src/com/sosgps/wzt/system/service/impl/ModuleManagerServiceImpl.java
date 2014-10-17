package com.sosgps.wzt.system.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.sos.helper.SpringHelper;

import com.sosgps.wzt.commons.util.DateUtil;
import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefModuleRole;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TRole;

import com.sosgps.wzt.system.dao.TModuleDao;
import com.sosgps.wzt.system.service.ModuleManagerService;
import com.sosgps.wzt.system.service.RefModuleRoleService;
import com.sosgps.wzt.system.service.RoleService;
import com.sosgps.wzt.tree.MapabcTree;
import com.sosgps.wzt.tree.TreeNode;

/**
 * 权限功能设置
 * 
 * @author Administrator
 * 
 */
public class ModuleManagerServiceImpl implements ModuleManagerService {
	private TModuleDao tModuleDao;

	/**
	 * 保存权限
	 */
	public void saveModule(TModule module) {
		this.tModuleDao.save(module);
	}

	/**
	 * 删除权限
	 */
	public void deleteModule(TModule module) {
		List list=tModuleDao.findByProperty("parentid", module.getId());
		for(int i=0;i<list.size();i++){
			TModule childModule=(TModule)list.get(i);
			deleteModule(childModule);
		}
		this.tModuleDao.delete(module);
	}
	
	/**
	 * 依据id查询模块
	 */
	public TModule retrieveModule(Long id) {
		return this.tModuleDao.findById(id);
	}

	/*
	 * 查找所有权限,即所有叶节点
	 */
	public List queryModuleList(Object ob) {
		return this.getRootTreeNode().getModuleLeafList();
	}

	public boolean deleteAll(Long[] longIds) {
		return this.tModuleDao.deleteAll(longIds);
	}
	
	public List queryModuleList(Long[] ids){
		return this.tModuleDao.fingModuleList(ids);
	}
	/**
	 * 更新权限
	 * 
	 * @return
	 */
	public void updateModule(TModule module) {
		try {
			this.tModuleDao.update(module);
		} catch (RuntimeException re) {
			SysLogger.sysLogger.error("E062: 权限管理-更新权限失败,错误原因："
					+ re.getMessage(), re);
		} catch (Exception ex) {
			SysLogger.sysLogger.error("E062: 权限管理-更新权限失败,错误原因："
					+ ex.getMessage(), ex);
		}
	}

	public List queryAllModuleList() {
		List list = null;
		try {
			list = this.tModuleDao.fingAllModuleList();
		} catch (RuntimeException re) {
			SysLogger.sysLogger.error("E062: 权限管理-查询权限失败,错误原因："
					+ re.getMessage(), re);
		}
		return list;
	}

	public List queryModuleByGradeList(Long grade) {
		List list = null;
		try {
			list = this.tModuleDao.findByModuleGradeList(grade);
		} catch (RuntimeException re) {
			SysLogger.sysLogger.error("E062: 权限管理-查询权限失败,错误原因："
					+ re.getMessage(), re);
		}
		return list;
	}

	public List queryModuleByEmpCode(String empCode) {
		List list = null;
		try {
			list = this.tModuleDao.findByModuleEmpCodeList(empCode);
		} catch (RuntimeException re) {
			SysLogger.sysLogger.error("E062: 权限管理-查询权限失败,错误原因："
					+ re.getMessage(), re);
		}
		return list;
	}

	public List queryModuleByModuleLevel(Long moduleLevel) {
		return tModuleDao.findByModuleLevel(moduleLevel);
	}

	public List getSortedNodeList() {
		return getModuleMapabcTree().getSortedNodeList();
	}

	public MapabcTree getModuleMapabcTree() {
		List moduleList = tModuleDao.fingAllModuleList();
		// System.out.println("MapabcTree:"+moduleList.size());
		MapabcTree sosgpsTree = MapabcTree.getMapabcTree(moduleList);
		return sosgpsTree;
	}

	public TreeNode getTreeNodeByID(Long id) {
		return getModuleMapabcTree().getTreeNodeByRootID(id);
	}

	public TreeNode getRootTreeNode() {
		return getTreeNodeByID(new Long(-1));
	}
	
	public  TreeNode getTreeNodeByRole(Long roleId){
		RefModuleRole refModuleRole = null;

		// set allModuleParentKeyHash

		List allModuleList = tModuleDao.fingAllModuleList();
		HashMap<Long, TModule> allModuleHash = new HashMap<Long, TModule>();
		for (int i = 0; i < allModuleList.size(); i++) {
			TModule module = (TModule) allModuleList.get(i);
			allModuleHash.put(module.getId(), module);
		}

		HashMap<Long, TModule> parentNodeHash = new HashMap<Long, TModule>();
		
		TRole role = tModuleDao.findRoleAndModules(roleId);
		Set refModuleRoles = role.getRefModuleRoles();

		for (Iterator it = refModuleRoles.iterator(); it.hasNext();) {
			refModuleRole = (RefModuleRole) it.next();
			TModule childModule = refModuleRole.getTModule();
			if(childModule.getSortedIndex()==null)continue;
			parentNodeHash.put(childModule.getId(), childModule);

			findParentRoot(allModuleHash, parentNodeHash, childModule);
		}

		MapabcTree sosgpsTree = MapabcTree.getMapabcTree(parentNodeHash);
		TreeNode rootNode = sosgpsTree.getRootTreeNode();
		// rootNode.showTreeNode(rootNode);
		return rootNode;
	}
	
	public TreeNode getTreeNodeByRoleID(Long roleId) {
		RoleService roleService = (RoleService) SpringHelper
				.getBean("roleService");
//		TRole role = roleService.retrieveRole(new Long(roleId));
		return getTreeNodeByRole(roleId);
	}

	private void findParentRoot(HashMap<Long, TModule> allModuleHash,
			HashMap<Long, TModule> parentNodeHash, TModule childModule) {

		Long parentID = childModule.getParentid();
		if (parentID.intValue() == -1)
			return;
		TModule parentNode = parentNodeHash.get(parentID);
		if (parentNode == null) {
			parentNode = allModuleHash.get(parentID);
			if(parentNode==null){
				return;
			}
			parentNodeHash.put(parentID, parentNode);
		}

		findParentRoot(allModuleHash, parentNodeHash, parentNode);
	}

	public int findNextSortedIndexByParentID(Long parentID) {
		return tModuleDao.findNextSortedIndexByParentID(parentID);
	}

	public boolean moveUpSameParent(Long moduleID) {
		TModule module = tModuleDao.findById(moduleID);
		Long sortIndex = module.getSortedIndex();
		Long parentID = module.getParentid();

		TModule lastIndexModule = tModuleDao.findLastIndexModule(
				parentID, sortIndex);

		if (lastIndexModule == null) {
			return false;
		}

		Long changeCataID = lastIndexModule.getId();
		Long changeSortIndex = lastIndexModule.getSortedIndex();

		module.setSortedIndex(changeSortIndex);
		tModuleDao.update(module);

		lastIndexModule.setSortedIndex(sortIndex);
		tModuleDao.update(lastIndexModule);
		return true;
	}

	public boolean moveDownSameParent(Long moduleID) {
		TModule selectedModule = tModuleDao.findById(moduleID);
		Long sortIndex = selectedModule.getSortedIndex();
		Long parentID = selectedModule.getParentid();

		TModule nextIndexModule = tModuleDao.findNextIndexModule(
				parentID, sortIndex);

		if (nextIndexModule == null)
			return false;

		Long changeCataID = nextIndexModule.getId();
		Long changeSortIndex = nextIndexModule.getSortedIndex();

		selectedModule.setSortedIndex(changeSortIndex);
		tModuleDao.update(selectedModule);

		nextIndexModule.setSortedIndex(sortIndex);
		tModuleDao.update(nextIndexModule);
		return true;
	}

	public TModuleDao getTModuleDao() {
		return tModuleDao;
	}

	public void setTModuleDao(TModuleDao moduleDao) {
		tModuleDao = moduleDao;
	}

	public Page<TModule> queryRoleModulesById(Long roleId) {
		try {
			return tModuleDao.queryRoleModulesById(roleId);
		} catch (Exception e) {
			SysLogger.sysLogger.error("", e);
			return null;
		}
	}

	public Page<TModule> queryRoleModulesByIdAndModuleGrade(Long roleId, long moduleGrade) {
		try {
			return tModuleDao.queryRoleModulesByIdAndModuleGrade(roleId, moduleGrade);
		} catch (Exception e) {
			SysLogger.sysLogger.error("", e);
			return null;
		}
	}
}
