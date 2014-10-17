package com.sosgps.wzt.orm;

import java.util.Set;

/**
 * TRole entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TRole extends AbstractTRole implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public TRole() {
	}

	/** minimal constructor */
	public TRole(String roleName, String roleCode, String usageFlag,
			String createBy, String createDate, String empCode) {
		super(roleName, roleCode, usageFlag, createBy, createDate, empCode);
	}

	/** full constructor */
	public TRole(String roleName, String roleCode, String roleDesc,
			String usageFlag, String createBy, String createDate,
			String empCode, Set refModuleRoles, Set refUserRoles) {
		super(roleName, roleCode, roleDesc, usageFlag, createBy, createDate,
				empCode, refModuleRoles, refUserRoles);
	}

}
