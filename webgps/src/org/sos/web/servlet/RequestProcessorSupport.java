package org.sos.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.RequestProcessor;
import org.sos.constant.SessionConstant;



/**
 * @Title: RequestProcessorSupport.java
 * @Description:
 * @Copyright:
 * @Date: 2008-8-15 下午02:37:45
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yanglei
 * @version 1.0
 */
public class RequestProcessorSupport extends RequestProcessor {

	private static final Logger logger = Logger.getLogger(RequestProcessorSupport.class);

	public RequestProcessorSupport() {
		super();
	}

	/**
	 * 自定义的processPreprocess()方法，检查用户是否已经登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		// 如果用户请求的是登录页面则不需要检查
		if (logger.isDebugEnabled()) {
			logger.debug("Request getServletPath value is =: " + request.getServletPath());
		}
		if (request.getServletPath().equals("/login.do")) {
			return true;
		}
		if(request.getServletPath().equals("/system/validateCode.do")
				|| request.getServletPath().equals("/system/shortMessage.do")){
			return true;
		}
		// 检查session中是否存在userName属性，如果存在则表示用户已经登录
		if (logger.isDebugEnabled()) {
			logger.debug("Session value of LOGIN_USER_NAME is =: "
					+ (String) session.getAttribute(SessionConstant.LOGIN_USER_NAME));
		}
		if (session != null && session.getAttribute(SessionConstant.USER_INFO) != null) {
			return true;
		} else {
			String servletPath=request.getServletPath();
			String returnURL="/error_session.jsp";
			try {
				response.sendRedirect(returnURL);
				
				//request.getRequestDispatcher(returnURL).forward(request, response);
				return false;
			//} catch (ServletException ex) {
			//	logger.debug("CustomRequestProcessor ServletException is: " + ex.getMessage());
			} catch (IOException ex) {
				logger.debug("CustomRequestProcessor IOException is: " + ex.getMessage());
			}
			
		}
		return true;
	}

	/**
	 * 自定义的processContent()方法，用户设置响应类型
	 * 
	 * @param request
	 * @param response
	 */

	protected void processContent(HttpServletRequest request, HttpServletResponse response) {
		// 检查用户是否请求ContactImagAction,如果是则配置contentType为image/gif
		if (request.getServletPath().equals("/contactimage.do")) {
			response.setContentType("image/gif");
			return;
		}
		super.processContent(request, response);
	}

}
