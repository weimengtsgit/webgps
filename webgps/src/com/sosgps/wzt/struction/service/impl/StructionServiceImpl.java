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
 * @date: 2010-4-18 ����07:54:20
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
		String type = "1";// 1��ʾ����������·��0��ʾ�ָ�������·
		String instruction = terminalControl.setFlameout(String.valueOf(seq), type, null, null);
		//add zhangwei 2010-10-12
		stru.setId(seq);
		stru.setInstruction(instruction);
		stru.setCreatedate(Calendar.getInstance().getTime());
		stru.setState("1");// ������
		stru.setType("2");// ���͵�ָ��
		stru.setParam("���Ͷϵ�");
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
		String type = "0";// 1��ʾ����������·��0��ʾ�ָ�������·
		String instruction = terminalControl.setFlameout(String.valueOf(seq), type, null, null);
		stru.setInstruction(instruction);
		//add zhangwei 2010-10-12
		stru.setId(seq);
		stru.setCreatedate(Calendar.getInstance().getTime());
		stru.setState("1");// ������
		stru.setType("2");// ���͵�ָ��
		stru.setParam("���Ͷϵ�");
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
		String state = "2";// ����پ�
		String instruction = alarm.stopAlarm(String.valueOf(seq), state);
		//add zhangwei 2010-10-12
		stru.setId(seq);
		stru.setInstruction(instruction);
		stru.setCreatedate(Calendar.getInstance().getTime());
		stru.setState("1");// ������
		stru.setType("7");// ����ָ��
		stru.setParam("����پ�");
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
		String state = "4";// ������ٱ���
		String instruction = alarm.stopAlarm(String.valueOf(seq), state);
		//add zhangwei 2010-10-12
		stru.setId(seq);
		stru.setInstruction(instruction);
		stru.setCreatedate(Calendar.getInstance().getTime());
		stru.setState("1");// ������
		stru.setType("3");// ����ָ��
		stru.setParam("������ٱ���");
		structionDAO.save(stru);
	}
	//add by 2012-12-17 zss ������򱨾�
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
		String state = "5";// ������򱨾�
		String instruction = alarm.stopAlarm(String.valueOf(seq), state);
		stru.setId(seq);
		stru.setInstruction(instruction);
		stru.setCreatedate(Calendar.getInstance().getTime());
		stru.setState("1");// ������
		stru.setType("8");// ����ָ��
		stru.setParam("������򱨾�");
		structionDAO.save(stru);
	}

	public Page<Object[]> listStructionsRecord(int pageNo, int pageSize,
			String startDate, String endDate, String deviceIds,String type) {
		try {
			return structionDAO.listStructionsRecord(pageNo, pageSize, startDate, endDate, deviceIds,type);
		} catch (Exception e) {
			logger.error("��ʱ��β�ѯָ����Ϣͳ�ƴ���", e);
			return null;
		}
	}

	
}
