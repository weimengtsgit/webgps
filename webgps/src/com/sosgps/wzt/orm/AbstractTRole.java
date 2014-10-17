package com.sosgps.wzt.orm;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractTRole entity provides the base persistence definition of the TRole
 * entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTRole implements java.io.Serializable {

	// Fields

	private Long id;
	private String roleName;
	private String roleCode;
	private String roleDesc;
	private String usageFlag;
	private String createBy;
	private String createDate;
	private String empCode;
	private Set refModuleRoles = new HashSet(0);
	private Set refUserRoles = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractTRole() {
	}

	/** minimal constructor */
	public AbstractTRole(String roleName, String roleCode, String usageFlag,
			String createBy, String createDate, String empCode) {
		this.roleName = roleName;
		this.roleCode = roleCode;
		this.usageFlag = usageFlag;
		this.createBy = createBy;
		this.createDate = createDate;
		this.empCode = empCode;
	}

	/** full constructor */
	public AbstractTRole(String roleName, String roleCode, String roleDesc,
			String usageFlag, String createBy, String createDate,
			String empCode, Set refModuleRoles, Set refUserRoles) {
		this.roleName = roleName;
		this.roleCode = roleCode;
		this.roleDesc = roleDesc;
		this.usageFlag = usageFlag;
		this.createBy = createBy;
		this.createDate = createDate;
		this.empCode = empCode;
		this.refModuleRoles = refModuleRoles;
		this.refUserRoles = refUserRoles;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleDesc() {
		return this.roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
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

	public String getEmpCode() {
		return this.empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public Set getRefModuleRoles() {
		return this.refModuleRoles;
	}

	public void setRefModuleRoles(Set refModuleRoles) {
		this.refModuleRoles = refModuleRoles;
	}

	public Set getRefUserRoles() {
		return this.refUserRoles;
	}

	public void setRefUserRoles(Set refUserRoles) {
		this.refUserRoles = refUserRoles;
	}

}