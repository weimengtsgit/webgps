package com.sosgps.wzt.listener;
import java.util.Calendar;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.sosgps.wzt.listener.task.GetDescTask;



/**
 * <p>Title: </p>
 * <p>Description:����������</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
 * @author luoyj
 * @version 1.0
 */
public class ApplicationListener implements ServletContextListener{
	private static final Logger logger = Logger.getLogger(ApplicationListener.class);
	/**
	 * �����ر�ʱִ��
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		System.gc();
	}
	/**
	 * ��������ʱִ��
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		try{
			Timer timer = new Timer();
			com.sosgps.wzt.directl.Config config = com.sosgps.wzt.directl.Config.getInstance();
			//��ʱ��ȡλ������ʱ����
			String strInterval = config.getProperty("getDecsTaskInterval");
			//��СʱΪ��λ
			int intInterval = Integer.parseInt(strInterval);
//			Calendar cal = Calendar.getInstance();
//			cal.set(Calendar.HOUR_OF_DAY,24);
//			cal.set(Calendar.MINUTE, 0);
//			cal.set(Calendar.SECOND,0);
			//10���ʼִ�� ÿ��һ��Сʱִ��һ�� ��ѯλ�ñ���û�л�ȡλ�����������ݲ���ȡ������
			//timer.schedule(GetDescTask.getInstance(),0,1000*60*60);
			timer.schedule(GetDescTask.getInstance(),1000*10,intInterval*1000*60*60);
		}catch(Exception e){
			logger.error("############ϵͳ���ش���############",e);
		}
	}
}