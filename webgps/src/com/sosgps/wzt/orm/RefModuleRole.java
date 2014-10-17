package com.sosgps.wzt.orm;

/**
 * RefModuleRole entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class RefModuleRole extends AbstractRefModuleRole implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public RefModuleRole() {
	}

	/** full constructor */
	public RefModuleRole(TModule TModule, TRole TRole, Long moduleOrder) {
		super(TModule, TRole, moduleOrder);
	}

}
