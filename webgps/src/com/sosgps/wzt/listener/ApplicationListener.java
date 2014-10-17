package com.sosgps.wzt.listener;
import java.util.Calendar;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.sosgps.wzt.listener.task.GetDescTask;



/**
 * <p>Title: </p>
 * <p>Description:容器监听类</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
 * @author luoyj
 * @version 1.0
 */
public class ApplicationListener implements ServletContextListener{
	private static final Logger logger = Logger.getLogger(ApplicationListener.class);
	/**
	 * 容器关闭时执行
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		System.gc();
	}
	/**
	 * 容器启动时执行
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		try{
			Timer timer = new Timer();
			com.sosgps.wzt.directl.Config config = com.sosgps.wzt.directl.Config.getInstance();
			//定时获取位置描述时间间隔
			String strInterval = config.getProperty("getDecsTaskInterval");
			//以小时为单位
			int intInterval = Integer.parseInt(strInterval);
//			Calendar cal = Calendar.getInstance();
//			cal.set(Calendar.HOUR_OF_DAY,24);
//			cal.set(Calendar.MINUTE, 0);
//			cal.set(Calendar.SECOND,0);
			//10秒后开始执行 每隔一个小时执行一次 查询位置表中没有获取位置描述的数据并获取其数据
			//timer.schedule(GetDescTask.getInstance(),0,1000*60*60);
			timer.schedule(GetDescTask.getInstance(),1000*10,intInterval*1000*60*60);
		}catch(Exception e){
			logger.error("############系统加载错误############",e);
		}
	}
}