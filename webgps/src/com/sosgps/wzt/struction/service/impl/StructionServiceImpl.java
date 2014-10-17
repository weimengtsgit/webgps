package com.sosgps.wzt.struction.service.impl;

import java.util.Calendar;

import org.apache.log4j.Logger;

import com.sosgps.wzt.directl.TerminalFactory;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TStructions;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.struction.dao.StructionDAO;
import com.sosgps.wzt.struction.service.StructionService;

/**
 * @Title:
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-18 下午07:54:20
 */
public class StructionServiceImpl implements StructionService {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(StructionServiceImpl.class);

	private StructionDAO structionDAO;

	public StructionDAO getStructionDAO() {
		return structionDAO;
	}

	public void setStructionDAO(StructionDAO structionDAO) {
		this.structionDAO = structionDAO;
	}

	public void turnOffOilPower(String deviceId) {
		if (deviceId == null || deviceId.equals("")) {
			return;
		}
		TStructions stru = new TStructions();
		TTerminal terminal = new TTerminal();
		terminal.setDeviceId(deviceId);
		stru.setTTerminal(terminal);

		TerminalFactory tf = new TerminalFactory();
		com.sosgps.wzt.directl.idirectl.Terminal t = tf
				.createTerminalBySn(deviceId);
		com.sosgps.wzt.directl.idirectl.TerminalControl terminalControl = t
				.createTerminalControl();
		//modify liuyuan 2010-10-12
		long seq = structionDAO.getCurrentSequence();
		//String seq = "";
		String type = "1";// 1表示锁定车辆油路；0表示恢复车辆油路
		String instruction = terminalControl.setFlameout(String.valueOf(seq), type, null, null);
		//add zhangwei 2010-10-12
		stru.setId(seq);
		stru.setInstruction(instruction);
		stru.setCreatedate(Calendar.getInstance().getTime());
		stru.setState("1");// 待发送
		stru.setType("2");// 断油电指令
		stru.setParam("断油断电");
		structionDAO.save(stru);
	}

	public void turnOnOilPower(String deviceId) {
		if (deviceId == null || deviceId.equals("")) {
			return;
		}
		TStructions stru = new TStructions();
		TTerminal terminal = new TTerminal();
		terminal.setDeviceId(deviceId);
		stru.setTTerminal(terminal);

		TerminalFactory tf = new TerminalFactory();
		com.sosgps.wzt.directl.idirectl.Terminal t = tf
				.createTerminalBySn(deviceId);
		com.sosgps.wzt.directl.idirectl.TerminalControl terminalControl = t
				.createTerminalControl();
	
		//modify liuyuan 2010-10-12
		long seq = structionDAO.getCurrentSequence();
		String type = "0";// 1表示锁定车辆油路；0表示恢复车辆油路
		String instruction = terminalControl.setFlameout(String.valueOf(seq), type, null, null);
		stru.setInstruction(instruction);
		//add zhangwei 2010-10-12
		stru.setId(seq);
		stru.setCreatedate(Calendar.getInstance().getTime());
		stru.setState("1");// 待发送
		stru.setType("2");// 断油电指令
		stru.setParam("断油断电");
		structionDAO.save(stru);
	}

	public void stopHoldAlarm(String deviceId) {
		if (deviceId == null || deviceId.equals("")) {
			return;
		}
		TStructions stru = new TStructions();
		TTerminal terminal = new TTerminal();
		terminal.setDeviceId(deviceId);
		stru.setTTerminal(terminal);

		TerminalFactory tf = new TerminalFactory();
		com.sosgps.wzt.directl.idirectl.Terminal t = tf
				.createTerminalBySn(deviceId);
		com.sosgps.wzt.directl.idirectl.Alarm alarm = t.createAlarm();
		//modify liuyuan 2010-10-12
		long seq = structionDAO.getCurrentSequence();
		//String seq = "";
		String state = "2";// 解除劫警
		String instruction = alarm.stopAlarm(String.valueOf(seq), state);
		//add zhangwei 2010-10-12
		stru.setId(seq);
		stru.setInstruction(instruction);
		stru.setCreatedate(Calendar.getInstance().getTime());
		stru.setState("1");// 待发送
		stru.setType("7");// 报警指令
		stru.setParam("解除劫警");
		structionDAO.save(stru);
	}

	public void stopSpeedAlarm(String deviceId) {
		if (deviceId == null || deviceId.equals("")) {
			return;
		}
		TStructions stru = new TStructions();
		TTerminal terminal = new TTerminal();
		terminal.setDeviceId(deviceId);
		stru.setTTerminal(terminal);

		TerminalFactory tf = new TerminalFactory();
		com.sosgps.wzt.directl.idirectl.Terminal t = tf
				.createTerminalBySn(deviceId);
		com.sosgps.wzt.directl.idirectl.Alarm alarm = t.createAlarm();
		//modify liuyuan 2010-10-12
		long seq = structionDAO.getCurrentSequence();
		//String seq = "";
		String state = "4";// 解除超速报警
		String instruction = alarm.stopAlarm(String.valueOf(seq), state);
		//add zhangwei 2010-10-12
		stru.setId(seq);
		stru.setInstruction(instruction);
		stru.setCreatedate(Calendar.getInstance().getTime());
		stru.setState("1");// 待发送
		stru.setType("3");// 报警指令
		stru.setParam("解除超速报警");
		structionDAO.save(stru);
	}
	//add by 2012-12-17 zss 解除区域报警
	public void stopAreaAlarm(String deviceId) {
		// TODO Auto-generated method stub
		if (deviceId == null || deviceId.equals("")) {
			return;
		}
		TStructions stru = new TStructions();
		TTerminal terminal = new TTerminal();
		terminal.setDeviceId(deviceId);
		stru.setTTerminal(terminal);

		TerminalFactory tf = new TerminalFactory();
		com.sosgps.wzt.directl.idirectl.Terminal t = tf
				.createTerminalBySn(deviceId);
		com.sosgps.wzt.directl.idirectl.Alarm alarm = t.createAlarm();
		long seq = structionDAO.getCurrentSequence();
		String state = "5";// 解除区域报警
		String instruction = alarm.stopAlarm(String.valueOf(seq), state);
		stru.setId(seq);
		stru.setInstruction(instruction);
		stru.setCreatedate(Calendar.getInstance().getTime());
		stru.setState("1");// 待发送
		stru.setType("8");// 报警指令
		stru.setParam("解除区域报警");
		structionDAO.save(stru);
	}

	public Page<Object[]> listStructionsRecord(int pageNo, int pageSize,
			String startDate, String endDate, String deviceIds,String type) {
		try {
			return structionDAO.listStructionsRecord(pageNo, pageSize, startDate, endDate, deviceIds,type);
		} catch (Exception e) {
			logger.error("按时间段查询指令信息统计错误", e);
			return null;
		}
	}

	
}
