package com.sosgps.wzt.system.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.sosgps.wzt.orm.TModule;

/**
 * MyEclipse Struts Creation date: 01-07-2006
 * 
 * XDoclet definition:
 * 
 * @struts:form name="ModuleForm"
 */
public class ModuleNewForm extends BaseForm {

	// --------------------------------------------------------- Instance
	// Variables
	private TModule module = new TModule();
	private List groupList = new ArrayList();
	private List moduleGradeList = new ArrayList();
	private String groupId;
	private String moduleName;
	private String moduleDesc;
	private String moduleCode;
	private String modulePath;
	private String moduleType;
	private Long moduleGrade;
	private String gradeId;
	private String grade;
	private String id;
	private Long moduleLevel;

	// --------------------------------------------------------- Methods

	public Long getModuleLevel() {
		return moduleLevel;
	}

	public void setModuleLevel(Long moduleLevel) {
		this.moduleLevel = moduleLevel;
	}

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

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModulePath() {
		return modulePath;
	}

	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getModuleDesc() {
		return moduleDesc;
	}

	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}

}
