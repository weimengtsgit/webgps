/**
 * 
 */
package com.sosgps.wzt.stat.funstat.bean;

import java.util.Hashtable;

import com.sosgps.wzt.orm.TTermGroup;

/**
 * @author xiaojun.luan
 *
 */
public class GroupTree {
	private Hashtable nodeHash=new Hashtable();
	private GroupTreeNode rootNode;
	public GroupTree(){
		TTermGroup tTermGroup=new TTermGroup();
		tTermGroup.setId(-1l);
		rootNode=new GroupTreeNode(tTermGroup);
		nodeHash.put(tTermGroup.getId(), rootNode);
	}
	/**
	 * 加入的节点保证已经按 parent_id ,group_sort 排序
	 * @param tTermGroup
	 */
	public void addTreeNode(TTermGroup tTermGroup){
		GroupTreeNode treeNode=new GroupTreeNode(tTermGroup);
		GroupTreeNode parentNode=(GroupTreeNode)nodeHash.get(tTermGroup.getParentId());
		if(parentNode!=null){
			parentNode.addChildrenNode(treeNode);
			treeNode.setParentTreeNode(parentNode);
		}else{
			
		}
		nodeHash.put(tTermGroup.getId(), treeNode);
		
	}
	public GroupTreeNode getRootNode() {
		return rootNode;
	}
	public void setRootNode(GroupTreeNode rootNode) {
		this.rootNode = rootNode;
	}	
	public GroupTreeNode getTreeNodeById(Long id){
		return (GroupTreeNode)nodeHash.get(id);
	}
}
