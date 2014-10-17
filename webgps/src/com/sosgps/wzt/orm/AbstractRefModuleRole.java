package com.sosgps.wzt.orm;

/**
 * AbstractRefModuleRole entity provides the base persistence definition of the
 * RefModuleRole entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractRefModuleRole implements java.io.Serializable {

	// Fields

	private Long id;
	private TModule TModule;
	private TRole TRole;
	private Long moduleOrder;

	// Constructors

	/** default constructor */
	public AbstractRefModuleRole() {
	}

	/** full constructor */
	public AbstractRefModuleRole(TModule TModule, TRole TRole, Long moduleOrder) {
		this.TModule = TModule;
		this.TRole = TRole;
		this.moduleOrder = moduleOrder;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TModule getTModule() {
		return this.TModule;
	}

	public void setTModule(TModule TModule) {
		this.TModule = TModule;
	}

	public TRole getTRole() {
		return this.TRole;
	}

	public void setTRole(TRole TRole) {
		this.TRole = TRole;
	}

	public Long getModuleOrder() {
		return this.moduleOrder;
	}

	public void setModuleOrder(Long moduleOrder) {
		this.moduleOrder = moduleOrder;
	}

}