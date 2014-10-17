package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * TempOnlineUser entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TempOnlineUser extends AbstractTempOnlineUser implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public TempOnlineUser() {
	}

	/** full constructor */
	public TempOnlineUser(Date createTime, Long userId, String sessionid,
			String empCode) {
		super(createTime, userId, sessionid, empCode);
	}

}
