package com.sosgps.wzt.system.action;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sosgps.wzt.system.common.Constants;
import com.sosgps.wzt.system.util.CheckOnlineUser;
import com.sosgps.wzt.util.CharTools;
/**
 * <p>Title: LoginAction</p>
 * <p>Description: 退出</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: 图盟科技</p>
 * @author 位置通项目组 张卫
 * @version 1.0
 */
public class LogoutAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) 
    throws Exception {
		HttpSession session = request.getSession();
		CheckOnlineUser.destroySession(session);
		this.clearSession(session);
		String edition = (String)request.getSession().getAttribute("edition");
		String cooperation = (String)request.getSession().getAttribute("cooperation");
		cooperation = CharTools.killNullString(cooperation, "");
		return mapping.findForward(Constants.GLOBAL_FORWARD_LOGOUT+"_"+edition+cooperation);
	}
	protected void clearSession(HttpSession session) {

		Set sessionAttributes = Constants.SESSION_KEYS_SET;
		Iterator iter = sessionAttributes.iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			session.removeAttribute(key);
		}
	}
}
