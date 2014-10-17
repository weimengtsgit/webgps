package com.sosgps.wzt.log;

import org.sos.helper.SpringHelper;
import com.sosgps.wzt.log.service.LoginLoggerService;
import com.sosgps.wzt.orm.TLoginLog;

/**
 * <p>Title: LoginLogger</p>
 * <p>Description: ��¼������־����¼ϵͳ�����е�¼������־</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: ͼ�˿Ƽ�</p>
 * @author λ��ͨ��Ŀ�� ����
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
			SysLogger.sysLogger.error("E099: "+"��־д�����"+ex.getMessage(),ex);
			throw ex;
		}	
	}
}
