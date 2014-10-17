package com.sosgps.wzt.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sosgps.wzt.system.common.Constants;
import com.sosgps.wzt.system.common.UserInfo;

/**
 * �û�session����
 * 
 * @author  zhangwei
 * 
 */
public class UserOnlineListener implements HttpSessionListener {

    private static final Log logger = LogFactory
	    .getLog(UserOnlineListener.class);

    // key sid ;value user
    private static Map sessionMap = new HashMap();

    public void sessionCreated(HttpSessionEvent event) {
    }

    public void sessionDestroyed(HttpSessionEvent event) {
	HttpSession session = event.getSession();
	logger.info("Destroy a session:" + session.getId());
	sessionMap.remove(session.getId());// ���ûỰID��ʾ�ض��Ự
    }

    public static Map getSessionMap() {
	return sessionMap;
    }

    public static List getUserList() {
	List result = new ArrayList();
	for (Iterator iterator = sessionMap.keySet().iterator(); iterator
		.hasNext();) {
	    String key = (String) iterator.next();
	    HttpSession session = (HttpSession) sessionMap.get(key);

	    UserInfo user = (UserInfo) session.getAttribute("userInfo");
	    // ��sid����password,����view�����
	    //user.setPassword(key);
	    result.add(user);
	}
	return result;
    }

}
