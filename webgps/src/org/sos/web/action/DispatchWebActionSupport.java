package org.sos.web.action;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//apache
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
//sos
import org.sos.constant.GlobalConstant;
import org.sos.exception.AppException;
import org.sos.exception.OperateFailureException;
import org.sos.exception.WebException;

import com.sosgps.wzt.system.common.UserInfo;

/**
 * @Title: DispatchWebActionSupport.java
 * @Description:
 * @Copyright:
 * @Date: 2008-8-15 下午03:08:06
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yanglei
 * @version 1.0
 */
public class DispatchWebActionSupport extends Action {
	private static final Logger logger = Logger.getLogger(DispatchWebActionSupport.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String className = this.getClass().getName();
		ActionForward actionForword = null;
		String parameter = mapping.getParameter();
		if (parameter != null || parameter.trim().length() > 0) {
			String methodName = request.getParameter(parameter);
			if (methodName != null || methodName.trim().length() > 0) {
				try {
					Class[] parameters = { ActionMapping.class,ActionForm.class, HttpServletRequest.class,
							HttpServletResponse.class };
					Object[] objects = { mapping, form, request, response };
					Method method = this.getClass().getMethod(methodName, parameters);
					logger.debug(GlobalConstant.LOG_PREFIX + "Process: " + className
							+ ".doExecute() begin ...");
					actionForword = (ActionForward)method.invoke(this, objects);
					logger.debug(GlobalConstant.LOG_PREFIX + "Process: " + className
							+ ".doExecute() end.");

					// forward
					//actionForword = doForward(mapping, request, response);
				} catch (Exception ex) {
					if (ex instanceof AppException) {
						ActionMessages messages = new ActionMessages();
						Object[] obj = new Object[] { ex.getMessage() };
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"error.appservice", obj));
						saveMessages(request, messages);
					}
					if (ex instanceof WebException) {
						ActionMessages messages = new ActionMessages();
						Object[] obj = new Object[] { ex.getMessage() };
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"error.webaction", obj));
						saveMessages(request, messages);
					}
					if (ex instanceof OperateFailureException) {
						ActionMessages messages = new ActionMessages();
						Object[] obj = new Object[] { ex.getMessage() };
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"error.unknow"));
						saveMessages(request, messages);
					}
					logger.error("", ex);
					System.out.println(ex.getMessage());
					throw ex;
				}
			}
		}
		return actionForword;
	}

	/**
	 * @param mapping
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward doForward(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// Success forward
		return processForward(GlobalConstant.SUCCESS_KEY, mapping, request, response);
	}

	/**
	 * For support the .do mapping in forward, default unsupport in struts, such as:
	 * <forward name="success" path="test.do"/> 
	 * @param forwardTarget String
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws Exception
	 * @return ActionForward
	 */
	protected ActionForward processForward(String forwardTarget, ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = mapping.findForward(forwardTarget);
		if (forward == null) {
			forward = mapping.getInputForward();
		}
		String target = forward.getPath();
		if (target != null) {
			if (target.endsWith(".do")) {
				String destination = request.getContextPath()
						+ mapping.getModuleConfig().getPrefix() + "/" + target;
				logger.debug("Destination path: " + destination);
				response.sendRedirect(destination);
				// getServlet().getServletContext().getRequestDispatcher(destination).forward(request,
				// response);
				return null;
			}
		} else {
			forward = null;
		}
		return forward;
	}
	/**
	 * 获取登录用户信息
	 * @param request
	 * @return
	 */
	public UserInfo getCurrentUser(HttpServletRequest request) {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		return user;
	}
}
