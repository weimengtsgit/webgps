package com.sosgps.wzt.orm;

/**
 * TempShortMessage entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TempShortMessage extends AbstractTempShortMessage implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public TempShortMessage() {
	}

	/** full constructor */
	public TempShortMessage(String phoneNumber, Long createTime, Long userId,
			String dynamicPassword) {
		super(phoneNumber, createTime, userId, dynamicPassword);
	}

}
