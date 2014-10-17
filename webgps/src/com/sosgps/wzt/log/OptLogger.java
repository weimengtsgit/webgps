package com.sosgps.wzt.log;
import org.sos.helper.SpringHelper;

import com.sosgps.wzt.log.service.OptLoggerService;
import com.sosgps.wzt.orm.TOptLog;
/**
 * <p>Title: OptLogger</p>
 * <p>Description: ϵͳ������־����¼ϵͳ�����и������ܲ�����־</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: ͼ�˿Ƽ�</p>
 * @author λ��ͨ��Ŀ�� ����
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
			SysLogger.sysLogger.error("E099: "+"��־д�����"+ex.getMessage(),ex);
			throw ex;
		}	
	}
	 public static void main(String args[]){

	 }
}
