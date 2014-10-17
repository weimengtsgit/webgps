//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.9.210/xslt/JavaClass.xsl
package com.sosgps.wzt.system.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.tree.TreeNode;



/** 
 * MyEclipse Struts
 * Creation date: 08-08-2008
 * 
 * XDoclet definition:
 * @struts:form name="RoleForm"
 */
public class RoleForm extends BaseForm {

	// --------------------------------------------------------- Instance Variables
private String id;
private List moduleList = new ArrayList();
private List list = new ArrayList();
private TRole role = new TRole();
private String[] moduleIds;
private Map order = new HashMap();
private String empCode; //À˘ Ù∆Û“µId 
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List getModuleList() {
		return moduleList;
	}

	public void setModuleList(List moduleList) {
		this.moduleList = moduleList;
	}

	public TRole getRole() {
		return role;
	}

	public void setRole(TRole role) {
		this.role = role;
	}

	public String[] getModuleIds() {
		return moduleIds;
	}

	public void setModuleIds(String[] moduleIds) {
		this.moduleIds = moduleIds;
	}

	public Map getOrder() {
		return order;
	}

	public void setOrder(Map order) {
		this.order = order;
	}
	private void setOrder(String key,Object value){
		this.order.put(key,value);
		
	}
	private Object getOrder(String key){
		return this.order.get(key);
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}


	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}



}

