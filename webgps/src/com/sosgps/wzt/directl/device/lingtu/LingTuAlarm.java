package com.sosgps.wzt.directl.device.lingtu;

import org.apache.log4j.Logger;

import java.nio.*;
import java.text.NumberFormat;

import com.mapabc.geom.DPoint;
import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.AlarmAdapter;
import com.sosgps.wzt.directl.idirectl.TerminalParam;

public class LingTuAlarm extends AlarmAdapter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(LingTuAlarm.class);

	public LingTuAlarm() {
	}

	String head = "C M ";
	String oemcode = "";
	String deviceid = "";

	public LingTuAlarm(TerminalParam tparam) {
		this.terminalParam = tparam;
		this.oemcode = tparam.getOemCode();
		this.deviceid = tparam.getSeriesNo();
	}

	/*
	 * 取消报警，没有定义的报警用取消当前报警实现即 v=0 @param seq:序列号 @param v:0取消当前报警 1紧急报警 2劫警
	 * 3区域报警 4超速报警
	 */
	public String stopAlarm(String seq, String type) {
		String ret = "";
		// C M c 4C54:1001|60|1 38B
		String hexSeq = Integer.toHexString(Integer.parseInt(seq));

		head = head + seq + " ";

		String cmd = "";
		
		if (type.equals("3")) {
			cmd = this.oemcode + ":" + this.deviceid + "|201| ";
		} else if (type.equals("4")) {
			cmd = this.oemcode + ":" + this.deviceid + "|202|000 ";
		} else {
			cmd = this.oemcode + ":" + this.deviceid + "|60|" + type + " ";
		}
		String vcode = Tools.getVerfyCode(cmd);
		ret = head + cmd + vcode;
		return ret;
	}

	// 无持续时间限制的超速设置
	public String setSpeedAlarm(String seq, String speedType, String speed) {
		String time = "";
		String ret = "";
		// C M 1 4C54:1001|202|64 3F2
		// C M 21b 4C54:13455139738|202|5 53c

		String hexSeq = Integer.toHexString(Integer.parseInt(seq));

		head = head + seq + " ";
		int s = Integer.parseInt(speed);

		String hexS = Integer.toHexString(s);
		if (s == 0) {
			hexS = "000";
		}
		if (s > 255) {
			s = 255;
		}
		String cmd = this.oemcode + ":" + this.deviceid + "|202|" + hexS + " ";
		String vcode = Tools.getVerfyCode(cmd);
		ret = head + cmd + vcode;
		return ret;
	}

	// 带持续时间限制的超速设置C M 2 4C54:1001|203|1;2d;11001600 64F
	public String setSpeedAlarm(String seq, String speedType, String speed,
			String t) {
		String ret = "";
		t = "06002000";
		String hexSeq = Integer.toHexString(Integer.parseInt(seq));
		head = head + seq + " ";
		int s = Integer.parseInt(speed);

		String hexS = Integer.toHexString(s);
		if (s == 0) {
			hexS = "000";
		}
		if (s > 255) {
			s = 255;
		}
		String cmd = this.oemcode + ":" + this.deviceid + "|203|2;" + hexS
				+ ";" + t + " ";
		String vcode = Tools.getVerfyCode(cmd);
		ret = head + cmd + vcode;
		return ret;
	}

	/**
	 * 矩形区域报警设置
	 * 
	 * @param leftDownX
	 *            String：
	 * @param leftDownY
	 *            String：
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
	 * 多边形区域报警设置(矩形区域也可以用此接口)
	 * 
	 * @param seq：指令序列号
	 * @param alarmType：类型
	 *            0出 1进 2取消
	 * @param rangeId：区域编号
	 * @param startDate:区域生效的开始时间
	 * @param endDate:区域生效的结束时间
	 * @param points：顶点数组
	 * @return
	 */
	public String setRangeAlarm(String seq, String alarmType, String rangeId,
			String startDate, String endDate, com.mapabc.geom.DPoint[] points) {
		String ret = "";
		// C M 13 4C54:1001|200|2;15752A00!8954400,1BE51D00!44AA200;1 B1E

		String hexSeq = Integer.toHexString(Integer.parseInt(seq));
		head = head + seq + " ";
		if (alarmType.equals("2")) {
			// 取消终端区域设置C M 14 4C54:1001|201| 387
			String cancelCmd = this.oemcode + ":" + this.deviceid + "|201| ";
			ret = head + cancelCmd + Tools.getVerfyCode(cancelCmd);
			return ret;
		}
		String xys = "";
		for (int i = 0; i < points.length; i++) {
			double x = points[i].x;
			double y = points[i].y;

			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(6);
			String fx = nf.format(x);
			String fy = nf.format(y);
			logger.info("区域报警经纬度信息：x=" + fx + ",y=" + fy);
			String sx = Tools.Du2Mills(fx);
			String sy = Tools.Du2Mills(fy);
			if (i == points.length - 1) {
				xys += sx + "!" + sy;
			} else {
				xys += sx + "!" + sy + ",";
			}
		}

		String cmd = this.oemcode + ":" + this.deviceid + "|200|2;" + xys + ";"
				+ alarmType + " ";
		String vcode = Tools.getVerfyCode(cmd);
		ret = head + cmd + vcode;

		// ret = this.oemcode + ":" + this.deviceid +
		// "|200|2;"+"15752A00!8954400,1BE51D00!44AA200;1 ";
		// ret = head + ret + Tools.getVerfyCode(ret);
		return ret;

	}

	// 偏航报警
	public String setLineAlarm(String seq, String type, String distance,
			DPoint[] points) {
		String ret = "";
		// (C M 2 7367:13698607474|206|1;2;3C;15752A00!8954400,1BE51D00!44AA200
		// BD5)
		// C M 3 4C54:1001|207|1 3BE
		// C M 1de
		// 7367:13698607474|206|1;5;64;1918ced5!7ee2319,191a9c77!7ead142,191afcd0!7e75187,191be3a3!7e4c7dd,191be395!7e4c7e9
		// 1c47
		String hexSeq = Integer.toHexString(Integer.parseInt(seq));
		head = head + seq + " ";
		if (type.equals("0")) {
			String cmd = this.oemcode + ":" + this.deviceid + "|207|1 ";
			String vcode = Tools.getVerfyCode(cmd);
			ret = head + cmd + vcode;
			return ret;
		}
		int maxDis = Integer.parseInt("ffff", 16);
		int curDis = Integer.parseInt(distance);
		if (curDis > maxDis) {
			curDis = maxDis;
		}
		String hexDis = Integer.toHexString(curDis);

		String xys = "";
		for (int i = 0; i < points.length; i++) {
			double x = points[i].x;
			double y = points[i].y;
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(6);
			String fx = nf.format(x);
			String fy = nf.format(y);
			String sx = Tools.Du2Mills(fx);
			String sy = Tools.Du2Mills(fy);
			
			if (i == points.length - 1) {
				xys += sx + "!" + sy;
			} else {
				xys += sx + "!" + sy + ",";
			}
		}
		String cmd = this.oemcode + ":" + this.deviceid + "|206|1;"
				+ points.length + ";" + hexDis + ";" + xys + " ";
		String vcode = Tools.getVerfyCode(cmd);
		ret = head + cmd + vcode;
		// ret = "7367:13698607474|206|1;2;3C;15752A00!8954400,1BE51D00!44AA200
		// ";
		// ret = "C M 2 "+ret + Tools.getVerfyCode(ret).toUpperCase();
		return ret;
	}

	// 报警参数设置
	public String setAlarmParam(String seq, String type, String timelen,
			String interval, String times) {
		// C M 1 4C54:1001|219|202;5;2;5 571
		// C M 223 4C54:13455141504|219|202;2;2;5 6DD

		String ret = "";
		String hexSeq = Integer.toHexString(Integer.parseInt(seq));

		head = head + seq + " ";

		String cmd = this.oemcode + ":" + this.deviceid + "|219|" + type + ";"
				+ timelen + ";" + interval + ";" + times + " ";
		String vcode = Tools.getVerfyCode(cmd);
		ret = head + cmd + vcode;
		return ret;
	}

	public static void main(String[] args) {
		int x = Integer.parseInt("172DA7F3", 16);
		double dx = x / 60 / 60 / 3600;
	}

}
