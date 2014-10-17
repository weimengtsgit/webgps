/**
 * 
 */
package com.autonavi.directl.device.xwrj;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.mapabc.geom.DPoint;
import com.autonavi.directl.CRC16;
import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.idirectl.AlarmAdapter;
import com.autonavi.directl.idirectl.TerminalParam;
 

/**
 * @author asia-auto 星网锐捷报警类
 */
public class XWRJAlarm extends AlarmAdapter {

	private String head = "7e00";

	private String simcard = "";

	private String head1 = "";

	public XWRJAlarm() {
	}

	/**
	 * 
	 */
	public XWRJAlarm(TerminalParam param) {
		this.terminalParam = param;
		simcard = param.getSimCard();
		head1 = "2626" + simcard + "020";
	}

	/**
	 * 设置定距离回传
	 */
	public String setWarpAlarm(String seq, String distance) {

		String ret = "";
		String cmdsn = "";// 报文序列号
		cmdsn = Tools.convertToHex(seq, 8);

		String cmd = cmdsn + "020200" + Tools.convertToHex(distance, 4)
				+ "000001";
		String cmd1 = Tools.compressHexData(cmd);
		String cmd2 = head1 + cmd1;
		String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
		ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8) + cmd3;
		// this.setByteArrayCmd(Tools.fromHexString(ret));
		//
		// try {
		// ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		// } catch (UnsupportedEncodingException e) {
		// // TODO 自动生成 catch 块
		// e.printStackTrace();
		// }
		return ret;
	}

	/**
	 * 超速报警
	 * 
	 */
	public String setSpeedAlarm(String seq, String speedType, String speed,
			String time) {
		String ret = "";
		String cmdsn = "";// 报文序列号
		cmdsn = Tools.convertToHex(seq, 8);

		if (speedType.equals("1")) {
			String t = time;// speed.substring(0, speed.indexOf(","));
			String spd = speed;// speed.substring(speed.indexOf(",") + 1);
			// Log.getInstance().outXWRJ//Loger("报警持续时间：" + t + " 速度值：" + spd);
			String tmpSpeed = "000";

			try {
				double dSpeed = Double.parseDouble(spd);
				if (dSpeed > 999) {
					dSpeed = 999;
				}
				double hSpeed = dSpeed / 1.852;
				byte iSpeed = (byte) hSpeed;
				byte bt = Byte.valueOf(t);
				byte[] param = new byte[2];
				param[0] = iSpeed;
				param[1] = bt;

				String tmpSpeed1 = "" + iSpeed;
				tmpSpeed = Tools.convertToHex(tmpSpeed1, 2);
				String tt = Tools.convertToHex(t, 2);

				String cmd = cmdsn + "010e" + tmpSpeed + tt;
				String cmd1 = Tools.compressHexData(cmd);
				String cmd2 = head1 + cmd1;
				String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
				ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8)
						+ cmd3;

				this.setByteArrayCmd(Tools.fromHexString(ret));
				// Log.getInstance().outXWRJ//Loger(" 设置最大速度指令:" + ret);

			} catch (java.lang.NumberFormatException ex) {
				ex.printStackTrace();
			}

		} else {
			String cmd = cmdsn + "0407";
			String cmd1 = Tools.compressHexData(cmd);
			String cmd2 = head1 + cmd1;
			String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
			ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8) + cmd3;
			// this.setByteArrayCmd(Tools.fromHexString(ret));
			// Log.getInstance().outXWRJ//Loger(" 取消超速报警指令:" + ret);
		}

		// try {
		// ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		// } catch (UnsupportedEncodingException e) {
		// // TODO 自动生成 catch 块
		// e.printStackTrace();
		// }
		return ret;
	}

	/**
	 * 多边形区域报警设置
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
		String cmdsn = "";// 报文序列号
		cmdsn = Tools.convertToHex(seq, 8);

		String cmd = cmdsn;
		String precmd = "";
		String contidition = "ffffffff";// 默认报警状态字，任何状态下触发区域报警，都会上报。
		String type = Tools.convertToHex(alarmType, 2);// 区域性质
		String num = Tools.convertToHex(rangeId, 2); // 区域编号
		String pointNum = Tools.convertToHex(points.length + "", 4); // 轨迹点数
		String stime = startDate.replaceAll("[-:\\s]", "").substring(2);// 开始时间
																		// yyMMddhhmmss
		String etime = endDate.replaceAll("[-:\\s]", "").substring(2);// 结束日期
																		// yyMMddhhmmss
		String xy = "";// 经纬度串
		for (int i = 0; i < points.length; i++) {
			DPoint dp = points[i];
			xy += this.getFormatXY(String.valueOf(dp.x))
					+ this.getFormatXY(String.valueOf(dp.y));
		}
		if (alarmType.equals("0") || alarmType.equals("1")) {// 出或进

			precmd = "013d" + num + type + pointNum + xy + stime + etime;

		} else if (alarmType.equals("2")) {
			precmd = "0405";// 取消区域报警
		}
		String cmd1 = Tools.compressHexData(cmd + precmd);
		String cmd2 = head1 + cmd1;// "78DA6360606064616104000020000B";

		String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
		ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8) + cmd3;
		 
		return ret;
	}

	/**
	 * 
	 * @param v
	 *            0 :关闭报警
	 * @return String
	 * @todo Implement this com.autonavi.directl.idirectl.Alarm method
	 */
	public String stopAlarm(String v) {
		String ret = "";
		String cmd = "00000001040401";
		String cmd1 = Tools.compressHexData(cmd);
		String cmd2 = head1 + cmd1;// "78DA6360606064616104000020000B";

		String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
		ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8) + cmd3;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		// Log.getInstance().outXWRJ//Loger("停止报警指令:" + ret);

		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 设置温度报警
	 * 
	 * @param up，上限温度
	 * @param down，下限温度
	 * @return
	 */
	public String setTemperatureAlarm(String up, String down) {
		String ret = "";
		int upt = Integer.parseInt(up);
		int downt = Integer.parseInt(down);
		if (upt > 327)
			upt = 327;
		if (downt < -327)
			downt = -327;

		String upts = Tools.int2Hexstring(upt, 4);
		String downts = Tools.int2Hexstring(downt, 4);

		String cmd = "00000000012f" + downts + upts;
		String cmd1 = Tools.compressHexData(cmd);
		String cmd2 = head1 + cmd1;// "78DA6360606064616104000020000B";

		String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
		ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8) + cmd3;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		// Log.getInstance().outXWRJ//Loger("温度范围设置指令:" + ret);

		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return ret;

	}

	// 矩形区域报警
	public String setRangeAlarmById(String alarmType, String rangeId, String x,
			String y, String X, String Y) {

		String ret = "";
		String cmd = "00000000";
		String precmd = "";
		if (alarmType.equals("0")) {
			precmd = "010f0000" + Tools.convertToHex("4", 4)
					+ Tools.convertToHex("4", 4) + getFormatXY(x)
					+ getFormatXY(y) + getFormatXY(X) + getFormatXY(Y);
		} else if (alarmType.equals("1")) {
			precmd = "010f00" + Tools.convertToHex("1", 2)
					+ Tools.convertToHex("4", 4) + getFormatXY(x)
					+ getFormatXY(y) + getFormatXY(X) + getFormatXY(Y);
			;
		} else if (alarmType.equals("2")) {
			precmd = "010f00" + Tools.convertToHex("2", 2) + getFormatXY(x)
					+ getFormatXY(y) + getFormatXY(X) + getFormatXY(Y);
			;
		} else if (alarmType.equals("3")) {
			precmd = "0405";// 取消区域报警
		}
		String cmd1 = Tools.compressHexData(cmd + precmd);
		String cmd2 = head1 + cmd1;// "78DA6360606064616104000020000B";

		String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
		ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8) + cmd3;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		// Log.getInstance().outXWRJ//Loger("区域报警设置指令:" + ret);

		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return ret;
	}

	private String getFormatXY(String xy) {
		String ret = "";
		String du = xy.substring(0, xy.lastIndexOf("."));// 度
		String fen = "0" + xy.substring(xy.lastIndexOf("."));// 小数度
		double df = Double.parseDouble(fen) * 60;// 分
		int zf = (int) df; // 整数分
		String dot = String.valueOf(df).substring(
				String.valueOf(df).lastIndexOf(".") + 1);// 分小数部分
		String sb = dot.substring(0, 2);// 小数分十、百分位
		String qw = dot.substring(2, 4);// 小数分千、万分位
		ret = Tools.convertToHex(du, 2) + Tools.int2Hexstring(zf, 2)
				+ Tools.convertToHex(sb, 2) + Tools.convertToHex(qw, 2);

		return ret;
	}

	public static void main(String[] args) {
		XWRJAlarm x = new XWRJAlarm();
		String cmd = "000000000407";
		String cmd1 = Tools.compressHexData(cmd);
		String cmd2 = x.head1 + cmd1;
		String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
		String ret = x.head + Tools.convertToHex(cmd3.length() / 2 + "", 8)
				+ cmd3;

		x.getFormatXY("116.394021");
		System.out.println(ret);
	}

}
