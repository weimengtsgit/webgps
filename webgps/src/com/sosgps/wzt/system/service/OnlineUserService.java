/**
 * 
 */
package com.sosgps.wzt.system.service;

import com.sosgps.wzt.orm.TempOnlineUser;
import com.sosgps.wzt.system.common.UserInfo;

/**
 * @author xiaojun.luan
 *
 */
public interface OnlineUserService {
	public boolean registerUser(UserInfo userInfo,String sessionID);
	public boolean unRegisterUser(UserInfo userInfo);
	public boolean clearAllRegisterUser();
	public boolean isOnline(UserInfo userInfo,String sessionID);
	public TempOnlineUser getRegisterUser(UserInfo userInfo);
}
