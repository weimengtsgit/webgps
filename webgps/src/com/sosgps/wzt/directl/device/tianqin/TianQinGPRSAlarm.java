package com.sosgps.wzt.directl.device.tianqin;

import com.mapabc.geom.DPoint;
import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.AlarmAdapter;
import com.sosgps.wzt.directl.idirectl.TerminalParam;
import com.sosgps.wzt.log.Log;

 

/**
 * <p>
 * Title: GPS网关
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: www.mapabc.com
 * </p>
 * 
 * HQor yang lei
 * 
 * @version 1.0
 */
public class TianQinGPRSAlarm extends AlarmAdapter {
	public TianQinGPRSAlarm() {
	}

	private String head = "*HQ,";
	private String end = "#";
	private String gpssn;

	public TianQinGPRSAlarm(TerminalParam param) {
		this.terminalParam = param;

		gpssn = terminalParam.getSeriesNo();
	}

	public static void main(String[] args) {
		TianQinGPRSAlarm t = new TianQinGPRSAlarm();
		 System.out.println(t.setTemperatureAlarm("1","15","1"));
	}

	/*
	 * 取消报警，没有定义的报警用取消当前报警实现即 v=0 @param seq:序列号 @param v:0取消当前报警 1紧急报警 2劫警
	 * 3区域报警 4超速报警
	 */
	public String stopAlarm(String seq, String type) {
		String ret = "";

//		if (type.equals("3")) {
//			ret = head + gpssn + ",S21," + Tools.getCurHMS() + ",0,0" + end;
//		} else if (type.equals("4")) {
//			ret = head + gpssn + ",S14," + Tools.getCurHMS() + ",0,"
//			+ ",0,1,10" + end;
// 		} else {
			ret = head + gpssn + ",R7," + Tools.getCurHMS() + end;
//		}

		return ret;
	}

	// 无持续时间限制的超速设置
	public String setSpeedAlarm(String seq, String speedType, String speed) {
		String time = "";
		String ret = "";
		String spd = "";
		try {
			double dspd = Double.parseDouble(speed) / 1.852;
			spd = (int) dspd + "";
		} catch (Exception e) {
			spd = "0";// 为取消
			e.printStackTrace();
		}
		// *XX,YYYYYYYYYY,S14,HHMMSS,Max_speed,Min_speed,M,countinue #
		ret = head + gpssn + ",S14," + Tools.getCurHMS() + "," + spd
				+ ",0,2,10" + end;

		return ret;
	}

	public String setRangeAlarm(String seq, String alarmType, String rangeId,
			String startDate, String endDate, com.mapabc.geom.DPoint[] points) {
		String ret = "";
		// *XX,YYYYYYYYYY,S21,HHMMSS,Lx,M,D,Minlatitude,Maxlatitude,G,Minlongitude,Maxlongitude#
		String type = "";
		if (alarmType.equals("0"))
			type = "1"; // 出区域
		if (alarmType.equals("1")) {
			type = "3";// 进区域
		}
		if (alarmType.equals("2")) {
			type = "0";
		}
		DPoint leftUp = points[0];
		DPoint rightDown = points[1];

		double leftUpX = leftUp.x;
		double leftUpY = leftUp.y;
		double rightDownX = rightDown.x;
		double rightDownY = rightDown.y;
		double leftDownX = points[0].x;
		double leftDownY = points[1].y;
		double rightUpX = points[1].x;
		double rightUpY = points[0].y;

		String maxx = getLatLongString(rightUpX, 0);
		String maxy = getLatLongString(rightUpY, 1);
		String minx = getLatLongString(leftDownX, 0);
		String miny = getLatLongString(leftDownY, 1);

		String lux = getLatLongString(leftUpX, 0);
		String luy = getLatLongString(leftUpY, 1);
		String rux = getLatLongString(rightUpX, 0);
		String ruy = getLatLongString(rightUpY, 1);

		String rdx = getLatLongString(rightDownX, 0);
		String rdy = getLatLongString(rightDownY, 1);
		String ldx = getLatLongString(leftDownX, 0);
		String ldy = getLatLongString(leftDownY, 1);

		ret = head + gpssn + ",S21," + Tools.getCurHMS() + ",1," + type + ",N,"
				+ ldy + "," + luy + "," + "E," + ldx + "," + rdx + end;

		return ret;

	}
	
	// 报警参数设置
	public String setAlarmParam(String seq, String type, String timelen,
			String interval, String times) {
		// C M 1 4C54:1001|219|202;5;2;5 571
		// C M 223 4C54:13455141504|219|202;2;2;5 6DD

		String ret = "";
		if (type.equals("200")) {
			ret = head + gpssn + ",S18," + Tools.getCurHMS() + "," + timelen
					+ ",1"

					+ end;
		}

		return ret;
	}

	public static String getLatLongString(double value, int type) {
		StringBuffer buf = new StringBuffer();
		
		int du = (int) value;
		int length = (type == 0) ? 3 : 2;
		
		String sdu = extentString(Integer.toString(du), length);
		
		double fen =  ((value - du) * 60.0);
		String sfen = String.valueOf(fen).substring(0,6);
		
//		String sfen = extentString(Integer.toString(fen), 2);
//		
//		double ffen = (value - du) * 60.0 - fen;
//		
//		String subfen = String.valueOf(ffen);
		
//		String ret = sdu + sfen + subfen.substring(subfen.indexOf(".") + 1);
		
		String ret = sdu+sfen;
		// buf.append(extentString(Integer.toString(subfen), 4));
		return ret;
	}

	/**
	 * 将字符串扩充成指定长度，扩充的方式是在前面增加'0'
	 * 
	 * @param org
	 *            String
	 * @param length
	 *            int
	 * @return String
	 */
	public static String extentString(String org, int length) {
		if (org == null) {
			return null;
		}

		if (org.length() == length) {
			return org;
		}

		StringBuffer buf = new StringBuffer();
		int num = length - org.length();
		for (int i = 0; i < num; i++) {
			buf.append('0');
		}
		buf.append(org);
		return buf.toString();
	}
	
	public String setTemperatureAlarm(String seq,String up, String down){
		String ret = "";
		try{
			double duptmp = Double.parseDouble(up);
			int iup = (int)duptmp;
			double xup = Math.abs(duptmp - iup);
			if (xup <0.5){
				duptmp = Double.parseDouble(iup+".0");
			}else{
				duptmp = Double.parseDouble(iup+".5");
			}
			
			double ddowntmp = Double.parseDouble(down);
			int idown = (int)ddowntmp;
			double xdown = Math.abs(ddowntmp - idown) ;
			if (xdown <0.5){
				ddowntmp = Double.parseDouble(idown+".0");
			}else{
				ddowntmp = Double.parseDouble(idown+".5");
			}
			 
		 String upTmp = Tools.getNumberFormatString(duptmp, 1, 1).replaceAll("\\.", ",");
		 String downTmp = Tools.getNumberFormatString(ddowntmp, 1, 1).replaceAll("\\.", ",");;
			ret = head + gpssn + ",S31," + Tools.getCurHMS() + "," + upTmp+","+downTmp	 
 					+ end;
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	 
 
}
