package com.sosgps.wzt.log;

import org.apache.log4j.Logger ;
/**
 * <p>Title: SysLogger</p>
 * <p>Description: ϵͳ�쳣��־����¼ϵͳ�������쳣��־</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: ͼ�˿Ƽ�</p>
 * @author λ��ͨ��Ŀ�� ����
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
	      com.sosgps.wzt.log.SysLogger.sysLogger.info("E001:Эͬƽ̨���ش��󣬴������:");
	  }catch(Exception ex){
		  com.sosgps.wzt.log.SysLogger.sysLogger.error("E001:���ؽ����ʶΪʧ��", ex);
	  }
	  
  }
}

