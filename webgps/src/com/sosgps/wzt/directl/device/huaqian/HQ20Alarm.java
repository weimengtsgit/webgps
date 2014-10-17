package com.sosgps.wzt.directl.device.huaqian;

import java.nio.ByteBuffer;
import java.text.NumberFormat;

import com.mapabc.geom.DPoint;
import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.AlarmAdapter;
import com.sosgps.wzt.directl.idirectl.EarthCoord;
import com.sosgps.wzt.directl.idirectl.TerminalParam;
import com.sosgps.wzt.log.Log;

public class HQ20Alarm extends AlarmAdapter {
	public HQ20Alarm() {
	}

	public HQ20Alarm(TerminalParam tparam) {
		this.terminalParam = tparam;
		String typeCode = tparam.getTypeCode();
		if (typeCode != null && typeCode.equalsIgnoreCase("GP-HQHAND-GPRS")){
			//兼容华强手持GPS
			HQ20Util.setProtocolHead("*HQ23");
			
		}
	}

	/**
	 * 设置报警范围 v='I':表示进范围报警 v='O':表示出范围报警 v='A':表示进出范围均报警 v='D':表示取消报警范围
	 * aaaaaaaaooooooooo:表示xy点（大纬大经）的经纬度值 aaaaaaaaooooooooo:表示XY点（小纬小经）的经纬度值
	 * 
	 * @param alarmType:0表示出区域报警，1表示进区域报警，2表示进出范围均报警，3表示取消区域报警
	 * @param xy：格式为aaaaaaaaooooooooo表示xy点（大纬大经）的经纬度值
	 * @param XY：格式为aaaaaaaaooooooooo表示XY点（小纬小经）的经纬度值
	 * @return
	 */
	public String setRangeAlarm(String alarmType, EarthCoord xy, EarthCoord XY) {
		StringBuffer buf = new StringBuffer();
		buf.append("AD");
		if (alarmType.equals("0")) {
			buf.append("O(");
		} else if (alarmType.equals("1")) {
			buf.append("I(");
		} else if (alarmType.equals("2")) {
			buf.append("A(");
		} else if (alarmType.equals("3")) {
			buf.append("D(");
		}

		double maxLongitude = Double.parseDouble(xy.x);
		double maxLatitude = Double.parseDouble(xy.y);
		double minLongitude = Double.parseDouble(XY.x);
		double minLatitude = Double.parseDouble(XY.y);

		buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
		buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
		buf.append(HQ20Util.getLatLongString(minLatitude, 1));
		buf.append(HQ20Util.getLatLongString(minLongitude, 0));
		buf.append(")");

		String cmd = HQ20Util.makeCommandStr(buf.toString(), true, true);

		return this.sent(cmd);
	}

	/**
	 * 
	 * @param speedType:作废，不用
	 * @param speed：为十六进制的速度值，速度单位为节
	 * @return
	 */
	public String setSpeedAlarm(String speedType, String speed) {
		StringBuffer buf = new StringBuffer();
		buf.append("AH(1");
		double dSpeed = Double.parseDouble(speed);
		double knotSpeed = HQ20Util.KMHOUR2Knot(dSpeed);
		Integer.toHexString((int) knotSpeed);
		String knotSpeedStr = HQ20Util.extentString(Integer
				.toHexString((int) knotSpeed), 2);
		buf.append(knotSpeedStr);
		buf.append(")");
		String cmdStr = HQ20Util.makeCommandStr(buf.toString(), true, true);
		return this.sent(cmdStr);
	}

	/**
	 * 偏差报警
	 * 
	 * @param xxxxxxxxyyyyyyyy:纬度偏差值
	 */
	public String setWarpAlarm(EarthCoord xxxxxxxxyyyyyyyy) {
		// this.isNeedReply = "1";
		// String tmp = "BL" + xxxxxxxxyyyyyyyy.x + xxxxxxxxyyyyyyyy.y;
		// String cmdStr = makeCommandStr(tmp);
		// return this.sent(cmdStr);
		return "";
	}

	/**
	 * 解除报警
	 * 
	 * @param v:解除、开启报警
	 *            0 :关闭所有报警 1：打开所有报警
	 */
	public String stopAlarm(String v) {
		String tmp = "BC";
		String cmdStr = HQ20Util.makeCommandStr(tmp, false, true);
		return this.sent(cmdStr);
	}

	// ---------------------------------------new-----------------------------//

	// 无持续时间限制的超速设置
	public String setSpeedAlarm(String seq, String speedType, String speed) {
		int dSpeed = Integer.parseInt(speed);
		if (dSpeed>255 || dSpeed==0){//取消超速设置
			String cancelCmd = "AH(1FF)";
			cancelCmd = HQ20Util.makeCommandStr(cancelCmd, true, true);
			return cancelCmd;
		}
		StringBuffer buf = new StringBuffer();
		buf.append("AH(1");
		double knotSpeed = HQ20Util.KMHOUR2Knot(dSpeed);
		String knotSpeedStr = HQ20Util.extentString(Integer
				.toHexString((int) knotSpeed), 2);
		buf.append(knotSpeedStr);
		buf.append(")");
		String cmdStr = HQ20Util.makeCommandStr(buf.toString(), true, true);
		return cmdStr;
		
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
	 * @param points：顶点数组,左上右下
	 * @return
	 */
	public String setRangeAlarm(String seq, String alarmType, String rangeId,
			String startDate, String endDate, com.mapabc.geom.DPoint[] points) {
		
		String head = Tools.bytesToHexString("[HQ2000AO".getBytes());
	 	String type = "";
		if (alarmType.equals("0")) {// 出
			type= "O";
		} else if (alarmType.equals("1")) {// 进
			type="I";
		} else if (alarmType.equals("2")) {// 取消
			String cancelcmd = "";
			cancelcmd += head + "0004";
			cancelcmd += "01"+Tools.bytesToHexString("D".getBytes());
			cancelcmd += "0004"+Tools.bytesToHexString("]".getBytes());
		    return cancelcmd;
		}
		String cmd = head+"0034";
		cmd +="01"+Tools.bytesToHexString(type.getBytes());
		cmd += "0004";
		
 		DPoint leftUp = points[0];
		DPoint rightDown = points[1];
		
		double leftUpX = leftUp.x;
		double leftUpY = leftUp.y;
		double rightDownX= rightDown.x;
		double rightDownY = rightDown.y;
		double leftDownX = points[0].x;
		double leftDownY= points[1].y;
		double rightUpX = points[1].x;
		double rightUpY = points[0].y;
  
 		String maxx = HQ20Util.getLatLongString(rightUpX, 0);
		String maxy = HQ20Util.getLatLongString(rightUpY, 1);
		String minx = HQ20Util.getLatLongString(leftDownX, 0);
		String miny = HQ20Util.getLatLongString(leftDownY, 1);
		 		
		String lux = HQ20Util.getLatLongString(leftUpX, 0);
		String luy = HQ20Util.getLatLongString(leftUpY, 1);
		String rux = HQ20Util.getLatLongString(rightUpX, 0);
		String ruy = HQ20Util.getLatLongString(rightUpY, 1);
 		
		String rdx = HQ20Util.getLatLongString(rightDownX, 0);
		String rdy = HQ20Util.getLatLongString(rightDownY, 1);
		String ldx = HQ20Util.getLatLongString(leftDownX,  0);
		String ldy = HQ20Util.getLatLongString(leftDownY,  1);
		
 		cmd += maxx+maxy+minx+miny+lux+luy+rux+ruy+rdx+rdy+ldx+ldy;
 		
 		cmd += Tools.bytesToHexString("]".getBytes()); 
 		
		return cmd;

	}

	/**
	 * 
	 * 取消报警，没有定义的报警用取消当前报警实现即 v=0
	 *  @param seq:序列号
	 *  @param v:0取消当前报警 1紧急报警 2劫警 3区域报警 4超速报警
	 *  
	 */
	 
	public String stopAlarm(String seq, String type) {
		 
		if (type.equals("3")) {
			String head = Tools.bytesToHexString("[HQ2000AO".getBytes());
			String cancelcmd = "";
			cancelcmd += head + "0004";
			cancelcmd += "01"+Tools.bytesToHexString("D".getBytes());
			cancelcmd += "0004"+Tools.bytesToHexString("]".getBytes());
		    return cancelcmd;
		} else if (type.equals("4")) {			 
			StringBuffer buf = new StringBuffer();
			buf.append("AH(1");
			buf.append("FF");
			buf.append(")");
			String cmdStr = HQ20Util.makeCommandStr(buf.toString(), true, true);
			return cmdStr;
		} else {
			String ret = "";
			String cmd = "BC";
			ret = HQ20Util.makeCommandStr(cmd, false, true);
			return ret;
		}
		
		
	}

	// -------------------------------------------------------------------------------------//

	public static void main(String[] args) {
		
		
		System.out.println((char)0x4F);
		HQ20Alarm HQ20Alarm = new HQ20Alarm();
		HQ20Alarm.setSpeedAlarm(null, null, "30");
		
		int s = Integer.parseInt("5");
		System.out.println(Integer.toHexString(s));
		
		TerminalParam tp = new TerminalParam();
		tp.setGPRSModal(true);
		tp.setSimCard("13911238237");
		HQ20Alarm.setTerminalParam(tp);

		// 测试区域报警
		EarthCoord xy = new EarthCoord("128.23404578", "E", "67.234923", "N");
		EarthCoord XY = new EarthCoord("56.957203", "E", "9.299395", "N");
		String cmd = HQ20Alarm.setRangeAlarm("0", xy, XY);
		System.out.println("区域报警指令：" + cmd);

		// 测试路线报警
		cmd = HQ20Alarm.setSpeedAlarm("", "128");
		System.out.println("路线报警指令：" + cmd);

		// 解除报警
		cmd = HQ20Alarm.stopAlarm("");
		System.out.println("解除报警指令：" + cmd);

	}

}
