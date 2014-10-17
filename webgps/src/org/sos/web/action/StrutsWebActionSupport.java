package org.sos.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.sos.constant.GlobalConstant;
import org.sos.exception.AppException;
import org.sos.exception.OperateFailureException;
import org.sos.exception.WebException;

/**
 * @Title: StrutsWebActionSupport.java
 * @Description:
 * @Copyright:
 * @Date: 2008-7-10 ÉÏÎç12:02:50
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author IBM
 * @version 1.0
 */

public abstract class StrutsWebActionSupport extends Action {
	private static Logger logger = Logger.getLogger(StrutsWebActionSupport.class);

	/**
	 * Method execute 
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String className = getClass().getName();
		try {
			// construct ServiceRequestValue
			logger.debug(GlobalConstant.LOG_PREFIX + "Process: " + className + ".doExecute() begin ...");
			doExecute(form, request, response);
			logger.debug(GlobalConstant.LOG_PREFIX + "Process: " + className + ".doExecute() end.");

			// forward
			ActionForward forward = doForward(mapping, request, response);
			return forward;
		} catch (Exception ex) {
			if (ex instanceof AppException) {
				ActionMessages messages = new ActionMessages();
				Object[] obj = new Object[] { ex.getMessage() };
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.appservice",
						obj));
				saveMessages(request, messages);
			}
			if (ex instanceof WebException) {
				ActionMessages messages = new ActionMessages();
				Object[] obj = new Object[] { ex.getMessage() };
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.webaction",
						obj));
				saveMessages(request, messages);
			}
			if (ex instanceof OperateFailureException) {
				ActionMessages messages = new ActionMessages();
				Object[] obj = new Object[] { ex.getMessage() };
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.unknow"));
				saveMessages(request, messages);
			}
			logger.error("", ex);
			throw ex;
		}
	}

	/**
	 * Step 1: Developer implement 
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @return Event
	 */
	public abstract void doExecute(ActionForm form, HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * Step 2: Developer implement 
	 * @param mapping ActionMapping
	 * @return ActionForward
	 */
	public ActionForward doForward(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// Success forward
		return processForward(GlobalConstant.SUCCESS_KEY, mapping, request, response);
	}

	/**
	 * For support the .do mapping in forward, default unsupport in struts, such as: <br>
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
				// getServlet().getServletContext().getRequestDispatcher(destination).forward(request, response);
				return null;
			}
		} else {
			forward = null;
		}
		return forward;
	}

}
