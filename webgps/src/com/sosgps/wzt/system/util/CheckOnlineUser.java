/**
 * 
 */
package com.sosgps.wzt.system.util;

import javax.servlet.http.HttpSession;

import org.sos.constant.SessionConstant;
import org.sos.helper.SpringHelper;

import com.sosgps.wzt.cs.server.ex.UserInfoException;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.service.OnlineUserService;

/**
 * @author xiaojun.luan 检测在线用户状态
 */
public class CheckOnlineUser {
	private static void checkUserIP(UserInfo userInfo, String accessIP)
			throws UserInfoException {
		String limitedIp=userInfo.getUser().getLimitedIp();
		if(accessIP==null){
			throw new UserInfoException("无法获得登陆IP，该用户已经绑定IP。");
		}
		if(limitedIp!=null){
			if(!limitedIp.equalsIgnoreCase(accessIP)){
				throw new UserInfoException("用户'"
						+ userInfo.getUser().getUserAccount() + "'与绑定IP不符。");
			}
		}
	}

	private static void checkOnlineUser(HttpSession session, UserInfo userInfo)
			throws UserInfoException {
		if (isOnline(session, userInfo)) {
			throw new UserInfoException("用户'"
					+ userInfo.getUser().getUserAccount() + "'已经登陆");
		}
		registerUser(session, userInfo);
	}

	public synchronized static void checkUser(HttpSession session,
			UserInfo userInfo, String accessIp) throws UserInfoException {
		checkUserIP(userInfo, accessIp);
		checkOnlineUser(session, userInfo);
	}
	public static synchronized boolean isOnline(HttpSession session,
			UserInfo userInfo) {
		OnlineUserService onlineUserService=(OnlineUserService)SpringHelper.getBean("onlineUserService");
		return onlineUserService.isOnline(userInfo, session.getId());
		/*
		boolean flag = false;
		HttpSession vsession = (HttpSession) hUserName
				.get(userInfo.getUserId());

		if (vsession != null) {
			if (session.getId().equals(vsession.getId())) {
				flag = false;//同一session
			} else {
				flag = true;//非同一session，用户在线
			}
		}
		return flag;
		*/
	}

	public static void registerUser(HttpSession session, UserInfo userInfo) {
		OnlineUserService onlineUserService=(OnlineUserService)SpringHelper.getBean("onlineUserService");
		//hUserName.put(userInfo.getUserId(), session);
		onlineUserService.registerUser(userInfo, session.getId());
	}
	public static boolean destroySession(HttpSession session){
		OnlineUserService onlineUserService = (OnlineUserService) SpringHelper
		.getBean("onlineUserService");
		UserInfo userInfo = (UserInfo) session.getAttribute(
		SessionConstant.USER_INFO);
		if (userInfo != null) {
			// hUserName.remove(userInfo.getUserId());
			return onlineUserService.unRegisterUser(userInfo);
			//System.out.println(userInfo.getUser().getUserAccount() + "用户退出！");
		}
		return false;
	}
}
