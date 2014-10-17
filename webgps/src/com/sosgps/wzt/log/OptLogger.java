package com.sosgps.wzt.log;
import org.sos.helper.SpringHelper;

import com.sosgps.wzt.log.service.OptLoggerService;
import com.sosgps.wzt.orm.TOptLog;
/**
 * <p>Title: OptLogger</p>
 * <p>Description: 系统操作日志，记录系统运行中各个功能操作日志</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: 图盟科技</p>
 * @author 位置通项目组 张卫
 * @version 1.0
 */
public class OptLogger implements Log{
	private static OptLogger instance = null;
	private OptLoggerService optLoggerService;
	public static synchronized OptLogger getInstance() {
		if (instance == null) {
			instance = new OptLogger();
			
		}
		return instance;
	}
	public void info(Object obj) throws Exception{
		optLoggerService = (OptLoggerService)SpringHelper.getBean("optLoggerService");
		try{
			this.optLoggerService.save((TOptLog)obj);
		}catch(Exception ex){
			SysLogger.sysLogger.error("E099: "+"日志写入错误"+ex.getMessage(),ex);
			throw ex;
		}	
	}
	 public static void main(String args[]){

	 }
}
