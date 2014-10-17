/**
 * 
 */
package com.sosgps.wzt.system.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.sos.helper.SpringHelper;

import com.sosgps.wzt.system.service.OnlineUserService;

/**
 * @author xiaojun.luan
 *
 */
public class WebInitServlet extends HttpServlet {

	/**
	 * 
	 */
	public WebInitServlet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		//super.init();
		com.sosgps.wzt.log.SysLogger.sysLogger.info("初始化容器！");
		//容器启动时清除临时登陆信息
		OnlineUserService onlineUserService = (OnlineUserService) SpringHelper
		.getBean("onlineUserService");
		onlineUserService.clearAllRegisterUser();

	}

}
