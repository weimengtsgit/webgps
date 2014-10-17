package com.sosgps.wzt.log;

import org.apache.log4j.Logger ;
/**
 * <p>Title: SysLogger</p>
 * <p>Description: 系统异常日志，记录系统运行中异常日志</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: 图盟科技</p>
 * @author 位置通项目组 张卫
 * @version 1.0
 */
public class SysLogger implements Log{
  public static final Logger sysLogger = (Logger) Logger.getLogger(SysLogger.class);
  public SysLogger() {
  }
  public void info(Object obj) throws Exception{
		
  }
  public static void main(String args[]){
	  try{
	      com.sosgps.wzt.log.SysLogger.sysLogger.info("E001:协同平台返回错误，错误代码:");
	  }catch(Exception ex){
		  com.sosgps.wzt.log.SysLogger.sysLogger.error("E001:返回结果标识为失败", ex);
	  }
	  
  }
}

