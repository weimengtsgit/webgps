package com.sosgps.wzt.directl.device.shouhang;

import org.apache.log4j.Logger;

import java.nio.*;
import java.text.NumberFormat;

import com.mapabc.geom.DPoint;

import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.AlarmAdapter;
import com.sosgps.wzt.directl.idirectl.TerminalParam;
 

public class ShouHangAlarm extends AlarmAdapter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ShouHangAlarm.class);

	public ShouHangAlarm() {
	}

	String deviceid = "";

	public ShouHangAlarm(TerminalParam tparam) {
		this.terminalParam = tparam;

		this.deviceid = tparam.getSeriesNo();
	}

	/*
	 * ȡ��������û�ж���ı�����ȡ����ǰ����ʵ�ּ� v=0 @param seq:���к� @param v:0ȡ����ǰ���� 1�������� 2�پ�
	 * 3���򱨾� 4���ٱ���
	 */
	public String stopAlarm(String seq, String type) {

		String ret = ShouHangUtil.crtCmdByte(this.terminalParam.getSeriesNo(),
				"01", "06", "");

		return ret;
	}

	// �޳���ʱ�����Ƶĳ�������
	public String setSpeedAlarm(String seq, String speedType, String speed) {
		// Ĭ�ϳ���5��
		String spdHex = Tools.convertToHex(speed, 2) + "05";

		String ret = ShouHangUtil.crtCmdByte(this.terminalParam.getSeriesNo(),
				"02", "09", spdHex);

		return ret;
	}

	// ������ʱ�����Ƶĳ�������C M 2 4C54:1001|203|1;2d;11001600 64F
	public String setSpeedAlarm(String seq, String speedType, String speed,
			String t) {
		return null;
	}

	/**
	 * �������򱨾�����
	 * 
	 * @param leftDownX
	 *            String��
	 * @param leftDownY
	 *            String��
	 * @param rightUpX
	 *            String
	 * @param rightUpY
	 *            String
	 * @return String
	 */
	public String setAreaAlarm(String leftDownX, String leftDownY,
			String rightUpX, String rightUpY) {
		String ret = "";

		return ret;
	}

	/**
	 * ��������򱨾�����(��������Ҳ�����ô˽ӿ�)
	 * 
	 * @param seq��ָ�����к�
	 * @param alarmType������
	 *            0�� 1�� 2ȡ��
	 * @param rangeId��������
	 * @param startDate:������Ч�Ŀ�ʼʱ��
	 * @param endDate:������Ч�Ľ���ʱ��
	 * @param points���������飺�����Ϊ�������µ�
	 * @return
	 */
	public String setRangeAlarm(String seq, String alarmType, String rangeId,
			String startDate, String endDate, com.mapabc.geom.DPoint[] points) {
		String ret = "";
		String dataHex = "01";
		if (alarmType.equals("2")) {
			dataHex = "ff";
			ret = ShouHangUtil.crtCmdByte(deviceid, "02", "0a", dataHex);
			return ret;
		}
		if (alarmType.equals("0")) {
			dataHex += "0201";
		} else if (alarmType.equals("1")) {
			dataHex += "0101";
		}
		DPoint leftUp = points[0];
		DPoint rightDown = points[1];
		double leftDownX = leftUp.x;
		double leftDownY = rightDown.y;
		double rightUpX = rightDown.x;
		double rightUpY = leftUp.y;
		dataHex += this.formatXY(leftDownX) + this.formatXY(leftDownY)
				+ this.formatXY(rightUpX) + this.formatXY(rightUpY);

		ret = ShouHangUtil.crtCmdByte(deviceid, "02", "0a", dataHex);

		return ret;

	}

	private String formatXY(double xy) {
		int ldx = (int) xy;
		double ldxfac = (xy - ldx) * 60;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(4);
		nf.setMinimumFractionDigits(4);
		nf.setMaximumIntegerDigits(2);
		nf.setMinimumIntegerDigits(2);
		String sldx = nf.format(ldxfac).replaceAll("\\.", "");
		String hex = Tools.int2Hexstring(ldx, 2)
				+ Tools.convertToHex(sldx.substring(0, 2), 2)
				+ Tools.convertToHex(sldx.substring(2, 4), 2)
				+ Tools.convertToHex(sldx.substring(4), 2);

		return hex;
	}

	// ƫ������
	public String setLineAlarm(String seq, String type, String distance,
			DPoint[] points) {
		return null;
	}

	// ������������
	public String setAlarmParam(String seq, String type, String timelen,
			String interval, String times) {
		return null;
	}

	public static void main(String[] args) {
		int x = Integer.parseInt("172DA7F3", 16);
		double dx = 22.03;

		ShouHangAlarm a = new ShouHangAlarm();
		a.formatXY(dx);
	}

}
