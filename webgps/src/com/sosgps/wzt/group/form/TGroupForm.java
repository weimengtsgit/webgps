package com.sosgps.wzt.group.form;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class TGroupForm extends ActionForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List tTargetGroupList;

	public List getTTargetGroupList() {
		return tTargetGroupList;
	}

	public void setTTargetGroupList(List targetGroupList) {
		tTargetGroupList = targetGroupList;
	}
}
