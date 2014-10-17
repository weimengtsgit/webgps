package com.sosgps.wzt.listener.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.sos.helper.SpringHelper;

import com.sosgps.wzt.locate.service.LocateService;
import com.sosgps.wzt.locate.service.impl.GetPosDesc;
import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.log.common.LogConstants;

/**
 * 定时获取位置描述定时服务
 * @author wei.zhang
 *
 */
public class GetDescTask extends TimerTask {
	
	private static final GetDescTask getDescTask = new GetDescTask();


	/**
	 * 单例模式
	 */
	private GetDescTask() {

	}

	public static final GetDescTask getInstance() {
		return getDescTask;
	}

	public void run() {
		getPosDesc();
	}
	/**
	 * 计算上月统计任务
	 */
	void getPosDesc() {
		SysLogger.sysLogger.info(LogConstants.TIMER_SET_GETLOCDESC+" #定时任务# ");
		//System.out.println("getPosDesc");
		GetPosDesc getPosDesc = new GetPosDesc();
		getPosDesc.getPosDesc();
		
	}

	public static void main(String[] args) {
		try {
			GetDescTask.getInstance().getPosDesc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
