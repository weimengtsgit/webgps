package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * RefEntCallednumber entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class RefEntCallednumber extends AbstractRefEntCallednumber implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public RefEntCallednumber() {
	}

	/** minimal constructor */
	public RefEntCallednumber(Date inputdate) {
		super(inputdate);
	}

	/** full constructor */
	public RefEntCallednumber(TEnt TEnt, String calledNumber, Date inputdate) {
		super(TEnt, calledNumber, inputdate);
	}

}
