package com.sosgps.wzt.system.bean;

public class ModuleModuleGroupBean {

	private Long moduleId;
	private String moduleGroupName;
	private String moduleGroupDesc;
	private String moduleName;
	private String moduleDesc;
	private String modulePath;
	private String moduleGrade;

	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getModuleDesc() {
		return moduleDesc;
	}
	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}
	public String getModulePath() {
		return modulePath;
	}
	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
	}

	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleGroupName() {
		return moduleGroupName;
	}
	public void setModuleGroupName(String moduleGroupName) {
		this.moduleGroupName = moduleGroupName;
	}
	public String getModuleGroupDesc() {
		return moduleGroupDesc;
	}
	public void setModuleGroupDesc(String moduleGroupDesc) {
		this.moduleGroupDesc = moduleGroupDesc;
	}
	public String getModuleGrade() {
		return moduleGrade;
	}
	public void setModuleGrade(String moduleGrade) {
		this.moduleGrade = moduleGrade;
	}
	
}
