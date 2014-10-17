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
 * @author xiaojun.luan ��������û�״̬
 */
public class CheckOnlineUser {
	private static void checkUserIP(UserInfo userInfo, String accessIP)
			throws UserInfoException {
		String limitedIp=userInfo.getUser().getLimitedIp();
		if(accessIP==null){
			throw new UserInfoException("�޷���õ�½IP�����û��Ѿ���IP��");
		}
		if(limitedIp!=null){
			if(!limitedIp.equalsIgnoreCase(accessIP)){
				throw new UserInfoException("�û�'"
						+ userInfo.getUser().getUserAccount() + "'���IP������");
			}
		}
	}

	private static void checkOnlineUser(HttpSession session, UserInfo userInfo)
			throws UserInfoException {
		if (isOnline(session, userInfo)) {
			throw new UserInfoException("�û�'"
					+ userInfo.getUser().getUserAccount() + "'�Ѿ���½");
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
				flag = false;//ͬһsession
			} else {
				flag = true;//��ͬһsession���û�����
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
			//System.out.println(userInfo.getUser().getUserAccount() + "�û��˳���");
		}
		return false;
	}
}
