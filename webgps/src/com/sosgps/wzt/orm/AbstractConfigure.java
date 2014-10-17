package com.sosgps.wzt.orm;

/**
 * AbstractConfigure entity provides the base persistence definition of the
 * Configure entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractConfigure implements java.io.Serializable {

	// Fields

	private Long id;
	private String CKey;
	private String CValue;
	private String CComments;

	// Constructors

	/** default constructor */
	public AbstractConfigure() {
	}

	/** full constructor */
	public AbstractConfigure(String CKey, String CValue, String CComments) {
		this.CKey = CKey;
		this.CValue = CValue;
		this.CComments = CComments;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCKey() {
		return this.CKey;
	}

	public void setCKey(String CKey) {
		this.CKey = CKey;
	}

	public String getCValue() {
		return this.CValue;
	}

	public void setCValue(String CValue) {
		this.CValue = CValue;
	}

	public String getCComments() {
		return this.CComments;
	}

	public void setCComments(String CComments) {
		this.CComments = CComments;
	}

}