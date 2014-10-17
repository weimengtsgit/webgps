package com.sosgps.wzt.orm;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractTModule entity provides the base persistence definition of the
 * TModule entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTModule implements java.io.Serializable {

	// Fields

	private Long id;
	private String moduleName;
	private String moduleNameEn;
	private String moduleCode;
	private String moduleType;
	private String modulePath;
	private String moudleDesc;
	private String usageFlag;
	public String getModuleNameEn() {
		return moduleNameEn;
	}

	public void setModuleNameEn(String moduleNameEn) {
		this.moduleNameEn = moduleNameEn;
	}

	private String createBy;
	private String createDate;
	private Long moduleId;
	private Long moduleGrade;
	private Long parentid;
	private Long leafFlag;
	private Long visibleFlag;
	private Long enableFlag;
	private Long treeLevel;
	private Long sortedIndex;
	private Set refModuleRoles = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractTModule() {
	}

	/** minimal constructor */
	public AbstractTModule(String moduleName, String moduleCode,
			String moduleType, String modulePath, String usageFlag,
			String createBy, String createDate, Long moduleId) {
		this.moduleName = moduleName;
		this.moduleCode = moduleCode;
		this.moduleType = moduleType;
		this.modulePath = modulePath;
		this.usageFlag = usageFlag;
		this.createBy = createBy;
		this.createDate = createDate;
		this.moduleId = moduleId;
	}

	/** full constructor */
	public AbstractTModule(String moduleName, String moduleCode,
			String moduleType, String modulePath, String moudleDesc,
			String usageFlag, String createBy, String createDate,
			Long moduleId, Long moduleGrade, Long parentid, Long leafFlag,
			Long visibleFlag, Long enableFlag, Long treeLevel,
			Long sortedIndex, Set refModuleRoles) {
		this.moduleName = moduleName;
		this.moduleCode = moduleCode;
		this.moduleType = moduleType;
		this.modulePath = modulePath;
		this.moudleDesc = moudleDesc;
		this.usageFlag = usageFlag;
		this.createBy = createBy;
		this.createDate = createDate;
		this.moduleId = moduleId;
		this.moduleGrade = moduleGrade;
		this.parentid = parentid;
		this.leafFlag = leafFlag;
		this.visibleFlag = visibleFlag;
		this.enableFlag = enableFlag;
		this.treeLevel = treeLevel;
		this.sortedIndex = sortedIndex;
		this.refModuleRoles = refModuleRoles;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleCode() {
		return this.moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleType() {
		return this.moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getModulePath() {
		return this.modulePath;
	}

	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
	}

	public String getMoudleDesc() {
		return this.moudleDesc;
	}

	public void setMoudleDesc(String moudleDesc) {
		this.moudleDesc = moudleDesc;
	}

	public String getUsageFlag() {
		return this.usageFlag;
	}

	public void setUsageFlag(String usageFlag) {
		this.usageFlag = usageFlag;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Long getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getModuleGrade() {
		return this.moduleGrade;
	}

	public void setModuleGrade(Long moduleGrade) {
		this.moduleGrade = moduleGrade;
	}

	public Long getParentid() {
		return this.parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public Long getLeafFlag() {
		return this.leafFlag;
	}

	public void setLeafFlag(Long leafFlag) {
		this.leafFlag = leafFlag;
	}

	public Long getVisibleFlag() {
		return this.visibleFlag;
	}

	public void setVisibleFlag(Long visibleFlag) {
		this.visibleFlag = visibleFlag;
	}

	public Long getEnableFlag() {
		return this.enableFlag;
	}

	public void setEnableFlag(Long enableFlag) {
		this.enableFlag = enableFlag;
	}

	public Long getTreeLevel() {
		return this.treeLevel;
	}

	public void setTreeLevel(Long treeLevel) {
		this.treeLevel = treeLevel;
	}

	public Long getSortedIndex() {
		return this.sortedIndex;
	}

	public void setSortedIndex(Long sortedIndex) {
		this.sortedIndex = sortedIndex;
	}

	public Set getRefModuleRoles() {
		return this.refModuleRoles;
	}

	public void setRefModuleRoles(Set refModuleRoles) {
		this.refModuleRoles = refModuleRoles;
	}

}