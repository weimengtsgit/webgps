package com.sosgps.wzt.system.service;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.tree.MapabcTree;
import com.sosgps.wzt.tree.TreeNode;

public interface ModuleManagerService {
	public void saveModule(TModule module);
	public void deleteModule(TModule module);
	public boolean deleteAll(Long[] longIds);
	public List queryModuleList(Object ob);
	public List queryModuleByModuleLevel(Long ob);
	public List queryAllModuleList();
	public List queryModuleList(Long[] ids);
	//public List queryModuleByRoleId(TRole role);//根据role查找权限
	
	public List queryModuleByGradeList(Long ob);//根据权限等级获得权限列表
	public List queryModuleByEmpCode(String empCode);//根据empCode 获得指定企业的权限
	public TModule retrieveModule(Long id);
	public void updateModule(TModule module);
	
	public List getSortedNodeList();
	public TreeNode getTreeNodeByID(Long id);
	public TreeNode getTreeNodeByRoleID(Long userId);
	public TreeNode getTreeNodeByRole(Long roleId);
	public TreeNode getRootTreeNode();
	public int findNextSortedIndexByParentID(Long parentID);
	public MapabcTree getModuleMapabcTree();
	public boolean moveUpSameParent(Long moduleID) ;
	public boolean moveDownSameParent(Long moduleID) ;
	
	// sos根据角色id查角色以及其所拥有的权限列表
	public Page<TModule> queryRoleModulesById(Long roleId);
	// sos根据角色id和权限级别查其所拥有的权限列表
	public Page<TModule> queryRoleModulesByIdAndModuleGrade(Long roleId, long moduleGrade);
}
