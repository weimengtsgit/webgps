package com.sosgps.wzt.orm;

/**
 * AbstractTBossRole entity provides the base persistence definition of the
 * TBossRole entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTBossRole implements java.io.Serializable {

	// Fields

	private Long id;
	private Long bossUserType;
	private String bossUserTypeDesc;
	private Long roleId;

	// Constructors

	/** default constructor */
	public AbstractTBossRole() {
	}

	/** full constructor */
	public AbstractTBossRole(Long bossUserType, String bossUserTypeDesc,
			Long roleId) {
		this.bossUserType = bossUserType;
		this.bossUserTypeDesc = bossUserTypeDesc;
		this.roleId = roleId;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBossUserType() {
		return this.bossUserType;
	}

	public void setBossUserType(Long bossUserType) {
		this.bossUserType = bossUserType;
	}

	public String getBossUserTypeDesc() {
		return this.bossUserTypeDesc;
	}

	public void setBossUserTypeDesc(String bossUserTypeDesc) {
		this.bossUserTypeDesc = bossUserTypeDesc;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}