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
			//���ݻ�ǿ�ֳ�GPS
			HQ20Util.setProtocolHead("*HQ23");
			
		}
	}

	/**
	 * ���ñ�����Χ v='I':��ʾ����Χ���� v='O':��ʾ����Χ���� v='A':��ʾ������Χ������ v='D':��ʾȡ��������Χ
	 * aaaaaaaaooooooooo:��ʾxy�㣨��γ�󾭣��ľ�γ��ֵ aaaaaaaaooooooooo:��ʾXY�㣨СγС�����ľ�γ��ֵ
	 * 
	 * @param alarmType:0��ʾ�����򱨾���1��ʾ�����򱨾���2��ʾ������Χ��������3��ʾȡ�����򱨾�
	 * @param xy����ʽΪaaaaaaaaooooooooo��ʾxy�㣨��γ�󾭣��ľ�γ��ֵ
	 * @param XY����ʽΪaaaaaaaaooooooooo��ʾXY�㣨СγС�����ľ�γ��ֵ
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
	 * @param speedType:���ϣ�����
	 * @param speed��Ϊʮ�����Ƶ��ٶ�ֵ���ٶȵ�λΪ��
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
	 * ƫ���
	 * 
	 * @param xxxxxxxxyyyyyyyy:γ��ƫ��ֵ
	 */
	public String setWarpAlarm(EarthCoord xxxxxxxxyyyyyyyy) {
		// this.isNeedReply = "1";
		// String tmp = "BL" + xxxxxxxxyyyyyyyy.x + xxxxxxxxyyyyyyyy.y;
		// String cmdStr = makeCommandStr(tmp);
		// return this.sent(cmdStr);
		return "";
	}

	/**
	 * �������
	 * 
	 * @param v:�������������
	 *            0 :�ر����б��� 1�������б���
	 */
	public String stopAlarm(String v) {
		String tmp = "BC";
		String cmdStr = HQ20Util.makeCommandStr(tmp, false, true);
		return this.sent(cmdStr);
	}

	// ---------------------------------------new-----------------------------//

	// �޳���ʱ�����Ƶĳ�������
	public String setSpeedAlarm(String seq, String speedType, String speed) {
		int dSpeed = Integer.parseInt(speed);
		if (dSpeed>255 || dSpeed==0){//ȡ����������
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
	 * ��������򱨾�����(��������Ҳ�����ô˽ӿ�)
	 * 
	 * @param seq��ָ�����к�
	 * @param alarmType������
	 *            0�� 1�� 2ȡ��
	 * @param rangeId��������
	 * @param startDate:������Ч�Ŀ�ʼʱ��
	 * @param endDate:������Ч�Ľ���ʱ��
	 * @param points����������,��������
	 * @return
	 */
	public String setRangeAlarm(String seq, String alarmType, String rangeId,
			String startDate, String endDate, com.mapabc.geom.DPoint[] points) {
		
		String head = Tools.bytesToHexString("[HQ2000AO".getBytes());
	 	String type = "";
		if (alarmType.equals("0")) {// ��
			type= "O";
		} else if (alarmType.equals("1")) {// ��
			type="I";
		} else if (alarmType.equals("2")) {// ȡ��
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
	 * ȡ��������û�ж���ı�����ȡ����ǰ����ʵ�ּ� v=0
	 *  @param seq:���к�
	 *  @param v:0ȡ����ǰ���� 1�������� 2�پ� 3���򱨾� 4���ٱ���
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

		// �������򱨾�
		EarthCoord xy = new EarthCoord("128.23404578", "E", "67.234923", "N");
		EarthCoord XY = new EarthCoord("56.957203", "E", "9.299395", "N");
		String cmd = HQ20Alarm.setRangeAlarm("0", xy, XY);
		System.out.println("���򱨾�ָ�" + cmd);

		// ����·�߱���
		cmd = HQ20Alarm.setSpeedAlarm("", "128");
		System.out.println("·�߱���ָ�" + cmd);

		// �������
		cmd = HQ20Alarm.stopAlarm("");
		System.out.println("�������ָ�" + cmd);

	}

}
