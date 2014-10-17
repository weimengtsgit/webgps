/**
 * 
 */
package com.sosgps.wzt.stat.funstat.bean;

import java.util.Hashtable;
import java.util.List;

import com.sosgps.wzt.orm.TTermGroup;

/**
 * @author xiaojun.luan
 * 
 */
public class GroupTreeNode {
	private TTermGroup tTermGroup;
	private String longGroupName;
	private GroupTreeNode parentTreeNode;

	public GroupTreeNode getParentTreeNode() {
		return parentTreeNode;
	}

	public String getLongGroupName() {
		return longGroupName;
	}

	public void setLongGroupName(String longGroupName) {
		this.longGroupName = longGroupName;
	}

	public void setParentTreeNode(GroupTreeNode parentTreeNode) {
		this.parentTreeNode = parentTreeNode;
	}

	private List<GroupTreeNode> childrenList = new java.util.ArrayList<GroupTreeNode>();

	public GroupTreeNode(TTermGroup tTermGroup) {
		this.tTermGroup = tTermGroup;
	}

	public List<GroupTreeNode> getChildrenList() {
		return childrenList;
	}

	public void addChildrenNode(GroupTreeNode childrenNode) {
		childrenList.add(childrenNode);
	}

	public TTermGroup getTTermGroup() {
		return tTermGroup;
	}

	public void setTTermGroup(TTermGroup termGroup) {
		tTermGroup = termGroup;
	}

	public Hashtable getLongGroupNameTreeNode() {
		Hashtable hash = new Hashtable();
		doLoopNode(this, hash);
		return hash;
	}

	private void doLoopNode(GroupTreeNode treeNode, Hashtable hash) {
		List<GroupTreeNode> childrenList = treeNode.getChildrenList();
		treeNode.setLongGroupName(treeNode.getTTermGroup().getGroupName());
		if (childrenList.size() == 0) {
			if (treeNode.getParentTreeNode().getLongGroupName() == null) {
				treeNode.setLongGroupName(treeNode.getLongGroupName());

			} else {
				treeNode.setLongGroupName(treeNode.getParentTreeNode()
						.getLongGroupName()
						+ "/" + treeNode.getLongGroupName());
			}
			hash.put(treeNode.getTTermGroup().getId(), treeNode);
			return;
		} else {
			for (int i = 0; i < childrenList.size(); i++) {
				doLoopNode(childrenList.get(i), hash);
			}
		}
	}
}
