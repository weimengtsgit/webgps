package com.sosgps.wzt.log;

import com.sosgps.wzt.system.common.UserInfo;

/**
 * <p>Title: LogFactory</p>
 * <p>Description: 日志工厂类，根据请求服务的类型，
 *                 动态创建日志服务</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: 图盟科技</p>
 * @author 位置通项目组 张卫
 * @version 1.0
 */
public class LogFactory {
	 public static Log newLogInstance(String log){
		 //获取登录日志实例
		 if(log.equals("loginLogger")){
		      return new LoginLogger();
		 }
		 //获取操作日志实例
		 else if(log.equals("optLogger")){
			 return new OptLogger();
		 }
		 //获取系统日志实例
		 else if(log.equals("sysLogger")){
			 return new SysLogger();
		 }
		 //获取定位日志实例
		 else if(log.equals("")){
			 return null;
		 }
		 //....其他
		 else {
			 return null;
		 }
	 }
	 public static void main(String args[]){
		 SysLogger logger = (SysLogger)LogFactory.newLogInstance("sysLogger");
		 logger.sysLogger.error("文件日志记录");
		 LoginLogger login = (LoginLogger)LogFactory.newLogInstance("loginLogger");
		 UserInfo userInfo =new UserInfo(null);
		 userInfo.setIp("1.1.1.1");
		 try{
		 login.info(userInfo);
		 }catch(Exception ex){
			 ex.printStackTrace();
		 }
	 }
}
