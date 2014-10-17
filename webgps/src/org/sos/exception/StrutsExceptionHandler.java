package org.sos.exception;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.sos.constant.GlobalConstant;

/**
 * @Title: StrutsExceptionHandler.java
 * @Description:
 * @Copyright:
 * @Date: 2008-7-2 ÏÂÎç06:43:20
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author IBM
 * @version 1.0
 */
public class StrutsExceptionHandler extends ExceptionHandler {
	public ActionForward execute(Exception ex, ExceptionConfig ec, ActionMapping mapping,
			ActionForm formInstance, HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		ActionForward forward = null;

		if (ec.getPath() != null) {
			forward = new ActionForward(ec.getPath());
		} else {
			forward = mapping.getInputForward();
		}

		if ("request".equals(ec.getScope())) {
			request.setAttribute(GlobalConstant.ROOT_EXCEPTION, ex);
		} else {
			request.getSession().setAttribute(GlobalConstant.ROOT_EXCEPTION, ex);
		}

		return forward;
	}

}
