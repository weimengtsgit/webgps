//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.9.210/xslt/JavaClass.xsl

package com.sosgps.wzt.system.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.sosgps.wzt.orm.TModuleGroup;



/** 
 * MyEclipse Struts
 * Creation date: 01-07-2006
 * 
 * XDoclet definition:
 * @struts:form name="ModuleGroupForm"
 */
public class ModuleGroupForm extends BaseForm {

	// --------------------------------------------------------- Instance Variables
private TModuleGroup moduleGroup = new TModuleGroup();
private TModuleGroup parentModuleGroup;
private List parentList = new ArrayList();
private List moduleGroupList = new ArrayList();
private String id;

	// --------------------------------------------------------- Methods

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {

		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {

		// TODO Auto-generated method stub
	}

	public TModuleGroup getModuleGroup() {
		return moduleGroup;
	}

	public void setModuleGroup(TModuleGroup moduleGroup) {
		this.moduleGroup = moduleGroup;
	}

	public List getParentList() {
		return parentList;
	}

	public void setParentList(List parentList) {
		this.parentList = parentList;
	}

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Returns the parentModuleGroup.
     */
    public TModuleGroup getParentModuleGroup() {
        return parentModuleGroup;
    }

    /**
     * @param parentModuleGroup The parentModuleGroup to set.
     */
    public void setParentModuleGroup(TModuleGroup parentModuleGroup) {
        this.parentModuleGroup = parentModuleGroup;
    }

	public List getModuleGroupList() {
		return moduleGroupList;
	}

	public void setModuleGroupList(List moduleGroupList) {
		this.moduleGroupList = moduleGroupList;
	}

}

