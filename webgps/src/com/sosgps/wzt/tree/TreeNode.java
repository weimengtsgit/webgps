package com.sosgps.wzt.tree;

import java.util.ArrayList;
import java.util.List;

import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.util.CharTools;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class TreeNode {

	public TModule tModule;
	private ArrayList<TreeNode> childrenNodes = new ArrayList<TreeNode>();

	private String isSelected;
	


	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	public TreeNode(TModule tModule) {
		this.tModule = tModule;
	}

	public TreeNode(long id) {
		this.tModule = new TModule();
		setId(id);
	}

	public void addChild(TreeNode node) {
		childrenNodes.add(node);
	}

	public void clearChildren() {
		childrenNodes.clear();
	}

	public ArrayList<TreeNode> getChildrenNodes() {
		return childrenNodes;
	}

	public long getId() {
		return tModule.getId().longValue();
	}

	public long getParentItemID() {
		if (tModule.getParentid() == null)
			return -1;
		return tModule.getParentid().longValue();
	}

	public void setId(long id) {
		tModule.setId(new Long(id));
	}

	public void setParentItemID(long parentItemID) {
		tModule.setParentid(new Long(parentItemID));
	}

	public long getSortIndex() {
		return tModule.getSortedIndex().longValue();
	}

	public void setSortIndex(long sortIndex) {
		tModule.setSortedIndex(new Long(sortIndex));
	}

	/**
	 * id,moduleName,moduleCode,modulePath,moduleDesc,moduleGrade,parentId
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getId());
		sb.append(",");
		sb.append(CharTools.escape(getModuleName()));
		sb.append(",");
		sb.append(CharTools.escape(getModuleCode()));
		sb.append(",");
		sb.append(CharTools.escape(getModulePath()));
		sb.append(",");
		sb.append(CharTools.escape(getModuleDesc()));
		sb.append(",");
		sb.append(CharTools.escape(getModuleGrade()));
		sb.append(",");
		sb.append(getParentItemID());
		return sb.toString();
	}

	public String getModuleName() {
		return tModule.getModuleName();
	}

	public void setModuleName(String moduleName) {
		tModule.setModuleName(moduleName);
	}

	public String getModuleCode() {
		return tModule.getModuleCode();
	}

	public void setModuleCode(String moduleCode) {
		tModule.setModuleCode(moduleCode);
	}

	public String getModuleType() {
		return tModule.getModuleType();
	}

	public void setModuleType(String moduleType) {
		tModule.setModuleType(moduleType);
	}

	public String getModulePath() {
		return tModule.getModulePath();
	}

	public void setModulePath(String modulePath) {
		tModule.setModulePath(modulePath);
	}

	public String getModuleDesc() {
		return tModule.getMoudleDesc();
	}

	public void setModuleDesc(String moduleDesc) {
		tModule.setMoudleDesc(moduleDesc);
	}

	public Long getLeafFlag() {
		return tModule.getLeafFlag();
	}

	public void setLeafFlag(Long leafFlag) {
		tModule.setLeafFlag(leafFlag);
	}

	public String getModuleGrade() {
		Long moduleGrade = tModule.getModuleGrade();
		if (moduleGrade == null) {
			moduleGrade = new Long(1);
		}
		return moduleGrade.toString();
	}

	public void setModuleGrade(String moduleGrade) {
		Long gradeLong = null;
		try {
			gradeLong = new Long(moduleGrade);
		} catch (Exception e) {
			gradeLong = new Long(1);
		}
		tModule.setModuleGrade(gradeLong);
	}

	public TModule getTModule() {
		return tModule;
	}

	public void setTModule(TModule module) {
		tModule = module;
	}

	public int getChildrenSize() {
		return childrenNodes.size();
	}

	public List<TModule> getModuleLeafList() {
		List leafList = new ArrayList();
		doLeafList(this, leafList);
		return leafList;
	}

	private void doLeafList(TreeNode node, List leafList) {
		if (node.childrenNodes.size() == 0) {
			leafList.add(node.getTModule());
		} else {
			for (int i = 0; i < node.childrenNodes.size(); i++) {
				TreeNode childNode = node.childrenNodes.get(i);
				doLeafList(childNode, leafList);
			}
		}
	}

	public static void showTreeNode(TreeNode treeNode) {
		ArrayList<TreeNode> children = treeNode.getChildrenNodes();
		String nodeStr = treeNode.toString();
		nodeStr = com.sosgps.wzt.util.CharTools.unescape(nodeStr);
		System.out.println(nodeStr);
		if (children.size() == 0) {

		} else {

			for (int i = 0; i < children.size(); i++) {
				showTreeNode(children.get(i));
			}
		}
	}

}
