package com.sosgps.wzt.orm;

// Generated by MyEclipse Persistence Tools

import java.util.Date;

/**
 * TBajuTask generated by MyEclipse Persistence Tools
 */
public class TBajuTask extends AbstractTBajuTask implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public TBajuTask() {
	}

	/** full constructor */
	public TBajuTask(String deviceId, String taskContent, String state,
			String type, String reply, Date crtdate, String crtman) {
		super(deviceId, taskContent, state, type, reply, crtdate, crtman);
	}

}
