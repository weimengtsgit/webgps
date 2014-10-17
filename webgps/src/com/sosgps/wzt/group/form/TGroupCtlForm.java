package com.sosgps.wzt.group.form;

import org.apache.struts.action.ActionForm;

/**
 * 组/部门增，删，改表单
 * 
 * @author ww
 * 
 */
public class TGroupCtlForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ctl;

	private Long groupId;

	private String groupName;

	private String childGroupName;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getChildGroupName() {
		return childGroupName;
	}

	public void setChildGroupName(String childGroupName) {
		this.childGroupName = childGroupName;
	}

	public String getCtl() {
		return ctl;
	}

	public void setCtl(String ctl) {
		this.ctl = ctl;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
