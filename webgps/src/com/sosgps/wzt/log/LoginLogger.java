package com.sosgps.wzt.log;

import org.sos.helper.SpringHelper;
import com.sosgps.wzt.log.service.LoginLoggerService;
import com.sosgps.wzt.orm.TLoginLog;

/**
 * <p>Title: LoginLogger</p>
 * <p>Description: 登录操作日志，记录系统运行中登录操作日志</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: 图盟科技</p>
 * @author 位置通项目组 张卫
 * @version 1.0
 */
public class LoginLogger implements Log{
	private static LoginLogger instance = null;
	private LoginLoggerService loginLoggerService;
	public static synchronized LoginLogger getInstance(){
		if (instance == null) {
			instance = new LoginLogger();			
		}
		return instance;
	}
	public void info(Object obj) throws Exception{
		loginLoggerService = (LoginLoggerService)SpringHelper.getBean("loginLoggerService");
		try{
			this.loginLoggerService.save((TLoginLog)obj);
		}catch(Exception ex){
			SysLogger.sysLogger.error("E099: "+"日志写入错误"+ex.getMessage(),ex);
			throw ex;
		}	
	}
}
