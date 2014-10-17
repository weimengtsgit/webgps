package com.sosgps.wzt.log;

import com.sosgps.wzt.system.common.UserInfo;

/**
 * <p>Title: LogFactory</p>
 * <p>Description: ��־�����࣬���������������ͣ�
 *                 ��̬������־����</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: ͼ�˿Ƽ�</p>
 * @author λ��ͨ��Ŀ�� ����
 * @version 1.0
 */
public class LogFactory {
	 public static Log newLogInstance(String log){
		 //��ȡ��¼��־ʵ��
		 if(log.equals("loginLogger")){
		      return new LoginLogger();
		 }
		 //��ȡ������־ʵ��
		 else if(log.equals("optLogger")){
			 return new OptLogger();
		 }
		 //��ȡϵͳ��־ʵ��
		 else if(log.equals("sysLogger")){
			 return new SysLogger();
		 }
		 //��ȡ��λ��־ʵ��
		 else if(log.equals("")){
			 return null;
		 }
		 //....����
		 else {
			 return null;
		 }
	 }
	 public static void main(String args[]){
		 SysLogger logger = (SysLogger)LogFactory.newLogInstance("sysLogger");
		 logger.sysLogger.error("�ļ���־��¼");
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
