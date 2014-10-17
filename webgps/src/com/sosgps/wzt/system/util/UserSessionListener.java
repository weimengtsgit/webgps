package com.sosgps.wzt.system.util;

import java.util.Hashtable;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.sos.constant.SessionConstant;
import org.sos.helper.SpringHelper;

import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.service.OnlineUserService;

public class UserSessionListener implements HttpSessionListener {

	// private static Hashtable hUserName = new Hashtable();

	public void sessionCreated(HttpSessionEvent arg0) {
		// System.out.println("sessionCreated!");
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		System.out.println("sessionDestroyed!");
		CheckOnlineUser.destroySession(se.getSession());
	}

}
