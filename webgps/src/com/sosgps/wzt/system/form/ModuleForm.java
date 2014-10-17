
package com.sosgps.wzt.system.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.sosgps.wzt.orm.TModule;



/** 
 * MyEclipse Struts
 * Creation date: 01-07-2006
 * 
 * XDoclet definition:
 * @struts:form name="ModuleForm"
 */
public class ModuleForm extends BaseForm {

	// --------------------------------------------------------- Instance Variables
private TModule module = new TModule();
private List groupList = new ArrayList();
private List moduleGradeList = new ArrayList();
private String groupId;
private Long moduleGrade;
private String gradeId;
private String grade;
private String id;
	// --------------------------------------------------------- Methods


	public List getGroupList() {
	return groupList;
}

public void setGroupList(List groupList) {
	this.groupList = groupList;
}

	
	public TModule getModule() {
		return module;
	}

	public void setModule(TModule module) {
		this.module = module;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List getModuleGradeList() {
		return moduleGradeList;
	}

	public void setModuleGradeList(List moduleGradeList) {
		this.moduleGradeList = moduleGradeList;
	}


	public Long getModuleGrade() {
		return moduleGrade;
	}

	public void setModuleGrade(Long moduleGrade) {
		this.moduleGrade = moduleGrade;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}



}

