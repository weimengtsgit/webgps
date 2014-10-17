package com.sosgps.wzt.tree;

import java.sql.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.*;

import com.sosgps.wzt.orm.TModule;

public class MapabcTree {
	Vector treeNodes = new Vector();

	public MapabcTree() {

	}

	public void addTreeNode(TreeNode treeNode) {
		treeNodes.addElement(treeNode);
	}

	public Vector getSortedNodeList() {
		Vector sortedNodeList = new Vector();
		TreeNode rootNode = new TreeNode(-1);
		addToSortedNodeList(sortedNodeList, rootNode);
		return sortedNodeList;
	}

	public TreeNode getRootTreeNode() {

		return getTreeNodeByRootID(-1);
	}

	public TreeNode getTreeNodeByRootID(long rootID) {
		TreeNode rootNode = new TreeNode(rootID);
		constructTree(rootNode);
		return rootNode;
	}

	private void constructTree(TreeNode treeNode) {
		SortedTree rootSortedTree = getSortedTree(treeNode);
		if (rootSortedTree.size() == 0) {
			return;
		}
		Iterator iterator = rootSortedTree.values().iterator();
		while (iterator.hasNext()) {
			TreeNode tempNode = (TreeNode) iterator.next();
			treeNode.addChild(tempNode);
			constructTree(tempNode);
		}
	}

	private void addToSortedNodeList(Vector v, TreeNode treeNode) {
		SortedTree rootSortedTree = getSortedTree(treeNode);
		if (rootSortedTree.size() == 0) {
			return;
		}
		Iterator iterator = rootSortedTree.values().iterator();
		while (iterator.hasNext()) {
			TreeNode tempNode = (TreeNode) iterator.next();
			v.addElement(tempNode);
			addToSortedNodeList(v, tempNode);
		}
	}

	public static MapabcTree getMapabcTree(ResultSet rs, Statement stmt,
			int classid) throws Exception {
		String sql = "select * from T_Modulenew ";
		MapabcTree sosgpsTree = new MapabcTree();
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			int id = rs.getInt("id");
			int parentID = rs.getInt("PARENTID");
			String moduleName = rs.getString("MODULE_NAME");
			int sortIndex = rs.getInt("SORTED_INDEX");
			String moduleDesc = rs.getString("MODULE_DESC");
			String moduleCode = rs.getString("MODULE_CODE");
			String modulePath = rs.getString("MODULE_PATH");
			String moduleGrade = rs.getString("MODULE_GRADE");
			TreeNode treeNode = new TreeNode(id);
			treeNode.setParentItemID(parentID);
			treeNode.setSortIndex(sortIndex);

			treeNode.setModuleGrade(moduleGrade);
			treeNode.setModuleDesc(moduleDesc);
			treeNode.setModuleName(moduleName);
			treeNode.setModulePath(modulePath);
			treeNode.setModuleCode(moduleCode);
			sosgpsTree.addTreeNode(treeNode);
		}
		return sosgpsTree;
	}

	public static MapabcTree getMapabcTree(List moduleList) {
		MapabcTree sosgpsTree = new MapabcTree();
		for (int i = 0; i < moduleList.size(); i++) {
			TModule module = (TModule) moduleList.get(i);
			TreeNode treeNode = new TreeNode(module);
			//treeNode.setTModulenew(module);
			sosgpsTree.addTreeNode(treeNode);
		}
		return sosgpsTree;
	}
	public static MapabcTree getMapabcTree(HashMap moduleHash){
		MapabcTree sosgpsTree = new MapabcTree();
		Iterator iterator=moduleHash.keySet().iterator();
		while(iterator.hasNext()){
			Object key=iterator.next();
			TModule module = (TModule)moduleHash.get(key);
			TreeNode treeNode = new TreeNode(module);
			sosgpsTree.addTreeNode(treeNode);
		}
		return sosgpsTree;		
	}
	public SortedTree getSortedTree(TreeNode treeNode) {
		SortedTree sortedTree = new SortedTree();
		long parentID = treeNode.getId();
		for (int i = 0; i < this.treeNodes.size(); i++) {
			TreeNode tempNode = (TreeNode) treeNodes.get(i);
			if (tempNode.getParentItemID() == parentID) {
				sortedTree.addTreeNode(tempNode);
			}
		}
		return sortedTree;
	}

	public void printTreeInfo() {
		Vector tree = getSortedNodeList();
		for (int i = 0; i < tree.size(); i++) {
			TreeNode treeNode = (TreeNode) tree.get(i);
			// System.out.println("treeID:"+treeNode.getId()+"
			// treeValue"+(String)treeNode.getObj());
		}
	}

	public static void getEntryInTreeVector(java.util.Vector treeVector,
			int startIndex, java.util.Vector outVector) {
		int nextIndex = startIndex + 1;
		outVector.addElement(treeVector.get(startIndex));
		// Vector childNodes=new Vector();
		findChildNodeVector(treeVector, (TreeNode) treeVector.get(startIndex),
				outVector);
		/*
		 * if(nextIndex<treeVector.size()&&((TreeNode)treeVector.get(nextIndex)).getParentItemID()==((TreeNode)treeVector.get(startIndex)).getId()){
		 * getEntryInTreeVector(treeVector,nextIndex,outVector); }
		 */
	}

	public static void findChildNodeVector(java.util.Vector treeVector,
			TreeNode treeNode, java.util.Vector childNodesV) {
		for (int i = 0; i < treeVector.size(); i++) {
			TreeNode tNode = (TreeNode) treeVector.get(i);
			if (tNode.getParentItemID() == treeNode.getId()) {
				childNodesV.addElement(tNode);
				findChildNodeVector(treeVector, tNode, childNodesV);
			}
		}
	}
	// 改变某个treeNode的索引
}
