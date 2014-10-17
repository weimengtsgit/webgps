package com.sosgps.wzt.orm;

/**
 * TBossRole entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TBossRole extends AbstractTBossRole implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public TBossRole() {
	}

	/** full constructor */
	public TBossRole(Long bossUserType, String bossUserTypeDesc, Long roleId) {
		super(bossUserType, bossUserTypeDesc, roleId);
	}

}
