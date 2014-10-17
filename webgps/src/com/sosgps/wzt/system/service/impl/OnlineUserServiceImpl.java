/**
 * 
 */
package com.sosgps.wzt.system.service.impl;

import java.util.List;

import org.sos.helper.SpringHelper;

import com.sosgps.wzt.orm.Configure;
import com.sosgps.wzt.orm.TempOnlineUser;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.dao.TempOnlineUserDao;
import com.sosgps.wzt.system.service.ConfigService;
import com.sosgps.wzt.system.service.OnlineUserService;

/**
 * @author xiaojun.luan
 * 
 */
public class OnlineUserServiceImpl implements OnlineUserService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.wzt.system.service.OnlineUserService#clearAllRegisterUser()
	 */
	public boolean clearAllRegisterUser() {
		com.sosgps.wzt.log.SysLogger.sysLogger.info("清除所有临时登陆信息！");
		tempOnlineUserDao.deleteAll();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.wzt.system.service.OnlineUserService#getRegisterUser(com.sosgps.wzt.system.common.UserInfo)
	 */
	public TempOnlineUser getRegisterUser(UserInfo userInfo) {
		TempOnlineUser tempOnlineUser = new TempOnlineUser();
		tempOnlineUser.setUserId(userInfo.getUserId());
		tempOnlineUser.setEmpCode(userInfo.getEmpCode());
		List list = tempOnlineUserDao.findByExample(tempOnlineUser);
		if (list == null || list.size() == 0)
			return null;
		else {
			return (TempOnlineUser) list.get(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.wzt.system.service.OnlineUserService#isOnline(com.sosgps.wzt.system.common.UserInfo,
	 *      java.lang.String)
	 */
	public boolean isOnline(UserInfo userInfo, String sessionID) {
		ConfigService configureService = (ConfigService) SpringHelper
				.getBean("configService");
		Configure configure=configureService.getByKey(ConfigService.PARA_LOGIN);
		if(configure.getCValue().equals("0")){
			return false;
		}
		TempOnlineUser tempOnlineUser = new TempOnlineUser();
		tempOnlineUser.setUserId(userInfo.getUserId());
		tempOnlineUser.setEmpCode(userInfo.getEmpCode());
		// tempOnlineUser.setSessionid(sessionID);
		List list = tempOnlineUserDao.findByExample(tempOnlineUser);
		if (list == null || list.size() == 0)
			return false;
		else {
			tempOnlineUser = (TempOnlineUser) list.get(0);
			if (tempOnlineUser.getSessionid().equals(sessionID))
				return false;
			else
				return true;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.wzt.system.service.OnlineUserService#registerUser(com.sosgps.wzt.system.common.UserInfo,
	 *      java.lang.String)
	 */
	public boolean registerUser(UserInfo userInfo, String sessionID) {
		TempOnlineUser tempOnlineUser = new TempOnlineUser();
		tempOnlineUser.setSessionid(sessionID);
		tempOnlineUser.setUserId(userInfo.getUserId());
		tempOnlineUser.setEmpCode(userInfo.getEmpCode());
		List list = tempOnlineUserDao.findByExample(tempOnlineUser);
		if (list != null && list.size() > 0) {
			tempOnlineUser = (TempOnlineUser) list.get(0);
			return true;
		}
		tempOnlineUserDao.save(tempOnlineUser);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.wzt.system.service.OnlineUserService#unRegisterUser(com.sosgps.wzt.system.common.UserInfo)
	 */
	public boolean unRegisterUser(UserInfo userInfo) {
		TempOnlineUser tempOnlineUser = new TempOnlineUser();
		tempOnlineUser.setUserId(userInfo.getUserId());
		List list = tempOnlineUserDao.findByExample(tempOnlineUser);
		if (list == null || list.size() == 0)
			return true;
		else {
			tempOnlineUser = (TempOnlineUser) list.get(0);
			tempOnlineUserDao.delete(tempOnlineUser);
			return true;
		}
	}

	private TempOnlineUserDao tempOnlineUserDao;

	public TempOnlineUserDao getTempOnlineUserDao() {
		return tempOnlineUserDao;
	}

	public void setTempOnlineUserDao(TempOnlineUserDao tempOnlineUserDao) {
		this.tempOnlineUserDao = tempOnlineUserDao;
	}
}
