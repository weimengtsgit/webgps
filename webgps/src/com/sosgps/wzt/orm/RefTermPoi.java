package com.sosgps.wzt.orm;

// Generated 2010-4-10 16:44:48 by Hibernate Tools 3.2.5.Beta

/**
 * RefTermPoi generated by hbm2java
 */
public class RefTermPoi implements java.io.Serializable {

	private Long id;
	private TPoi TPoi;
	private TTerminal TTerminal;

	public RefTermPoi() {
	}

	public RefTermPoi(TPoi TPoi, TTerminal TTerminal) {
		this.TPoi = TPoi;
		this.TTerminal = TTerminal;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TPoi getTPoi() {
		return this.TPoi;
	}

	public void setTPoi(TPoi TPoi) {
		this.TPoi = TPoi;
	}

	public TTerminal getTTerminal() {
		return TTerminal;
	}

	public void setTTerminal(TTerminal tTerminal) {
		TTerminal = tTerminal;
	}

}
