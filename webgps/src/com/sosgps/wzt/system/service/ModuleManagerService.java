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
	//public List queryModuleByRoleId(TRole role);//����role����Ȩ��
	
	public List queryModuleByGradeList(Long ob);//����Ȩ�޵ȼ����Ȩ���б�
	public List queryModuleByEmpCode(String empCode);//����empCode ���ָ����ҵ��Ȩ��
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
	
	// sos���ݽ�ɫid���ɫ�Լ�����ӵ�е�Ȩ���б�
	public Page<TModule> queryRoleModulesById(Long roleId);
	// sos���ݽ�ɫid��Ȩ�޼��������ӵ�е�Ȩ���б�
	public Page<TModule> queryRoleModulesByIdAndModuleGrade(Long roleId, long moduleGrade);
}
