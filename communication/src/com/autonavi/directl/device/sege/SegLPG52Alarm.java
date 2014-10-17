package com.autonavi.directl.device.sege;

import java.nio.*;
import java.text.NumberFormat;

import com.autonavi.directl.Tools;
import com.autonavi.directl.idirectl.AlarmAdapter;
import com.autonavi.directl.idirectl.TerminalParam;

public class SegLPG52Alarm extends AlarmAdapter {
	public SegLPG52Alarm() {
	}
	String centerSn = "01";//中心号
	String zuoSn = "001"; //坐席号
	public SegLPG52Alarm(TerminalParam tparam) {
		this.terminalParam = tparam;
	}

	public String setSpeedAlarm(String seq, String speedType, String speed,
			String time) {
		String cmdsn = centerSn+zuoSn;//报文序列号
        String inSeq = seq;
		 while(inSeq.length()<5){
			inSeq = "0"+inSeq;
		 }
		 cmdsn = cmdsn + inSeq;

		if (speedType.equals("1")) {
			String tmpSpeed = "000";
			if (speed != null) {
				try {
					double dSpeed = Double.parseDouble(speed);
					if (dSpeed > 999) {
						dSpeed = 999;
					}
					double hSpeed = dSpeed / 1.852;
					int iSpeed = (int) hSpeed;
					String tmpSpeed1 = "" + iSpeed;
					tmpSpeed = tmpSpeed1;
					for (int i = 0; i < 3 - tmpSpeed1.length(); i++) {
						tmpSpeed = "0" + tmpSpeed;
					}
				} catch (java.lang.NumberFormatException ex) {
				}
			}
			ByteBuffer buf = ByteBuffer.allocate(27);
			buf.put((byte) 0x5B);
			buf.put((byte) 0x31);
			buf.put(cmdsn.getBytes());
			buf.put((byte) 13);
			buf.put("(CMD,SPD,".getBytes());
			buf.put(tmpSpeed.getBytes());
			buf.put((byte) 0x29);
			buf.put((byte) 0x5D);
			String ret =  new String(buf.array());//Tools.bytesToHexString(buf.array()).toUpperCase();

			return ret ;
		} else {
			ByteBuffer buf = ByteBuffer.allocate(27);
			buf.put((byte) 0x5B);
			buf.put((byte) 0x31);
			buf.put(cmdsn.getBytes());
			buf.put((byte) 13);
			buf.put("(CMD,SPD,000)".getBytes());
			buf.put((byte) 0x5D);
			String ret =  new String(buf.array());//Tools.bytesToHexString(buf.array()).toUpperCase();

			return ret ;
		}
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
	public String setAreaAlarm(String seq, String leftDownX, String leftDownY,
			String rightUpX, String rightUpY) {
		String cmdsn = centerSn+zuoSn;//报文序列号
        String inSeq = seq;
		 while(inSeq.length()<5){
			inSeq = "0"+inSeq;
		 }
		 cmdsn = cmdsn + inSeq;

		String ret = "";
		String cmd = "(CMD,RNG," + leftDownX + "N," + leftDownY + "E,"
				+ rightUpX + "N," + rightUpY + "E)";
		ByteBuffer buf = ByteBuffer.allocate(14 + cmd.length());
		buf.put((byte) 0x5B);
		buf.put((byte) 0x32);
		buf.put(cmdsn.getBytes());
		buf.put((byte) cmd.length());
		buf.put(cmd.getBytes());
		buf.put((byte) 0x5D);
		ret = Tools.bytesToHexString(buf.array()).toUpperCase();
		this.setByteArrayCmd(buf.array());

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
		String cmdsn = centerSn+zuoSn;//报文序列号
        String inSeq = seq;
		 while(inSeq.length()<5){
			inSeq = "0"+inSeq;
		 }
		 cmdsn = cmdsn + inSeq;
		 
		String xy = "";
		if (alarmType.equals("2")){
			String cmd = "(CMD,POL," + points.length+")";
			ByteBuffer buf = ByteBuffer.allocate(14 + cmd.length());
			buf.put((byte) 0x5B);
			buf.put((byte) 0xED);
			buf.put(cmdsn.getBytes());
			buf.put((byte) cmd.length());
			buf.put(cmd.getBytes());
			buf.put((byte) 0x5D);
			ret = Tools.bytesToHexString(buf.array()).toUpperCase();
			return ret;
		}
		for (int i = 0; i < points.length; i++) {
			if (i == points.length-1){
				xy = xy + (i+1) + ","
				+ this.covertDuToDUFEN(points[i].y) + "N,"
				+ this.covertDuToDUFEN(points[i].x) + "E";

			}else {
			xy = xy + (i+1) + ","
					+ this.covertDuToDUFEN(points[i].y) + "N,"
					+ this.covertDuToDUFEN(points[i].x) + "E,";
			}
		}
		String cmd = "(CMD,POL," + points.length + "," + xy + ")";
		ByteBuffer buf = ByteBuffer.allocate(14 + cmd.length());
		buf.put((byte) 0x5B);
		buf.put((byte) 0xED);
		buf.put(cmdsn.getBytes());
		buf.put((byte) cmd.length());
		buf.put(cmd.getBytes());
		buf.put((byte) 0x5D);
		ret =   Tools.bytesToHexString(buf.array()).toUpperCase();
	 

		return ret;
	}

	// 把度格式转换为度分格式
	private String covertDuToDUFEN(double xys) {

		String ret = "";
		String xy = String.valueOf(xys);
		String du = xy.substring(0, xy.lastIndexOf("."));// 度
		String fen = "0" + xy.substring(xy.lastIndexOf("."));// 小数度
		double df = Double.parseDouble(fen) * 60;// 分
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumIntegerDigits(2);
		nf.setMinimumIntegerDigits(2);
		nf.setMaximumFractionDigits(4);
		nf.setMinimumFractionDigits(4);
		String sfen = nf.format(df).replaceAll("\\,", "");
		ret = du + sfen;
		return ret;
	}

	public static void main(String[] args) {
		SegLPG52Alarm AL = new SegLPG52Alarm();
		String x = AL.covertDuToDUFEN(113.201052);
		//5b 31 30313030313030333930 0d 28434d442c5350442c30 3032 29 5d
										//28434D442C5350442C30 3433 29 5D
		byte[] bs = Tools.fromHexString("303032");
		String s = new String(bs);
		AL.setSpeedAlarm("1","1","5","5");
		//[í0100100320o(CMD,POL,4,1,401.21966N,1168.08376E,2,3945.3530N,1168.91142E,3,3947.6398N,11644.2699E,4,402.93615N,11640.9792E)]
	}

}
