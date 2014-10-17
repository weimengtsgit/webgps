package com.autonavi.directl.parse;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.bean.TArea;
import com.autonavi.directl.bean.TSpeedCase;
import com.autonavi.directl.dbutil.DbOperation;
import com.autonavi.directl.dbutil.DbUtil;
import com.autonavi.directl.dbutil.service.DBService;
import com.autonavi.directl.dbutil.service.DBServiceImpl;
import com.autonavi.directl.device.huaqian.HQ20TerminalControl;
import com.autonavi.directl.device.huaqian.HQ20Util;
import com.autonavi.lbsgateway.GBLTerminalList;
import com.mapabc.geom.CoordCvtAPI;
import com.mapabc.geom.DPoint;

import java.io.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * ɽ����ǿ�ն�Э�����
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author yang lei
 * 
 * �������ݣ�2a485132303131353831303333303432332c414826413038313931393232333933353637313133353432363234373030303030363031383026423033303030303030303026463030303023
 * @version 1.0
 * 
 */

public class ParseHQ20 extends ParseBase {
	public ParseHQ20() {
	}

	public static void main(String[] args) {
		// ������ǿ�ն˷��������ǣ�*HQ20015806660749,YYA&A1757503640717911702859460000080709&B0300000000&F0000#
		// 2009-07-08 17:57:52,793 - ��ǿ�ն�15806660749
		// GPS��Ϣ��x=117.028594,y=36.407179,speed=0,time=2009-07-08
		// 17:57:50��״̬=ACC��;�س�;
		ParseHQ20 parseHQ201 = new ParseHQ20();
		//*HQ20014763015039,BA&A1435043807121411428086060006190314&B0000000000&C03?197<8&F0000#
		String s = "*HQ20015992016982,AA&A1637074008704111651136860000201110&B0000004000&E00018360&L2006F006F#";// "*HQ20013455033294,BA&A1124463525278011931116160001200709&B0100000000&C0000;35;&F0001#";
		// String lc = parseHQ201.formatSpeed(".2");
		// String hex = Tools.bytesToHexString("00004?=?".getBytes());
		//String temp = 
		parseHQ201.commonProtocolFunc(s);

		String lcstr = "00033322";
		int i = 0;
		while (i < lcstr.length()) {
			if (lcstr.charAt(i) != '0') {
				break;
			}
			i++;
		}
		lcstr = lcstr.substring(i);
		int ilc = Integer.parseInt(lcstr);
	}

	private String getGpsSN(String content) {
		String gpssn = null;

		return gpssn;
	}

	static String getGPSTag(String p) {
		String tmp = "";
		int tag = Integer.parseInt(p);
		switch (tag) {
		case 0:
			tmp = "��������γ����λ";
			break;
		case 1:
			tmp = "��������γ���Ƕ�λ";
			break;
		case 2:
			tmp = "��������γ����λ";
			break;
		case 3:
			tmp = "��������γ���Ƕ�λ";
			break;
		case 4:
			tmp = "��������γ����λ";
			break;
		case 5:
			tmp = "��������γ���Ƕ�λ";
			break;
		case 6:
			tmp = "��������γ����λ";
			break;
		case 7:
			tmp = "��������γ���Ƕ�λ";
			break;
		}
		return tmp;
	}

	public void parseGPRS(String simCard, String content) {

	}

	public void parseSMS(String phnum, String content) {

	}

	// ��ʽ��"*HQ20&AhhmmssaaaaaaaaoooooooooFvvffddmmyyabc";
	// *HQ20015810330423,AD1&A0804202239356711354262470000060180&B0300000000&F0000#
	public void parseGPRS(String hexString) {
		try {

			hexString = new String(Tools.fromHexString(hexString), "ISO8859-1");
			Log.getInstance().outLog("������ǿ�ն˷��������ǣ�" + hexString);
		} catch (UnsupportedEncodingException ex1) {
			Log.getInstance().outLog(
					"UnsupportedEncodingException is: " + ex1.getMessage());
		}

		if (hexString.startsWith("*HQ") && hexString.endsWith("#")) {
			// ��ͨЭ��
			commonProtocolFunc(hexString);
		} else if (hexString.startsWith("[HQ") && hexString.endsWith("]")) {
			// ����Э��
			specialProtocolFunc(hexString);
		}
	}

	private void commonProtocolFunc(String hexString) {
		try {
			// *HQ20115866751224,AH&A1104403640721411702869460000140409&B0300000000&F0000#
			int flag = hexString.indexOf(",");
			String head = hexString.substring(0, flag); // eg:*HQ20015810330423
			String deviceId = head.substring(6);
			String phnum = null;
			phnum = GBLTerminalList.getInstance().getSimcardNum(deviceId);// ���ڴ��ȡ�ն�SIMCARD
			if (phnum == null || hexString == null
					|| phnum.trim().length() == 0
					|| hexString.trim().length() == 0) {
				Log.getInstance().outLog("ϵͳ��û�����䵽ָ�����նˣ�device_id=" + deviceId);
				return;
			}
			this.setPhnum(phnum);
			this.setDeviceSN(deviceId);

			String backProperties = head.substring(5, 6); // �ظ�����
			// 36 40.7306 11702861660021
			int p = hexString.indexOf("&A"); // GPS��λ���ݱ�־λ��
			int sp = hexString.indexOf("&B"); // ״̬�����������־λ��
			int lc = hexString.indexOf("&C"); // �����Ϣ
			int lci = hexString.indexOf("&E"); // X80ϵ�л�Я��&E�����Ϣ
			int spi = hexString.indexOf("&F"); // ��ȷ���ٶ�
			int temperature = hexString.indexOf("&L"); // �¶� add liuyuan
														// 2010-08-17

			String funNumAndkey = hexString.substring(flag + 1, p); // ���ܱ�š��ؼ���

			String tmp = null;
			if (p < 0) {
				return;
			}
			tmp = hexString.substring(p + 2, p + 2 + 34);
			String statusDatas = hexString.substring(sp + 2, sp + 2 + 10); // ״̬��������������;

			String strTime = "20" + tmp.substring(32, 34) + "-"
					+ tmp.substring(30, 32) + "-" + tmp.substring(28, 30) + " ";
			strTime += tmp.substring(0, 2) + ":" + tmp.substring(2, 4) + ":"
					+ tmp.substring(4, 6);
			String x = "";// tmp.substring(14, 14 + 3) + "."+
			// tmp.substring(17, 17 + 6);
			x = Tools.formatXtoDu(tmp.substring(14, 23));
			String y = "";// tmp.substring(6, 6 + 2) + "." + tmp.substring(8,
			// 8 + 6);
			y = Tools.formatYtoDu(tmp.substring(6, 14));
			String f = tmp.substring(23, 23 + 1);
			String gpsTag = getGPSTag(f);
			String strSpeed = tmp.substring(24, 24 + 2);
			if (strSpeed.charAt(0) == '0') {
				strSpeed = strSpeed.charAt(1) + "";
			}
			int spd = Integer.parseInt(strSpeed) * 2;
			String speed = this.formatSpeed(spd + "");
			String dreicet = tmp.substring(26, 28);
			if (dreicet.charAt(0) == '0') {
				dreicet = dreicet.charAt(1) + "";
			}
			int tDirect = Integer.parseInt(dreicet) * 10;

			if ("1".equals(backProperties)) {
				// ��Ҫ���Ļظ�
				HQ20TerminalControl control = new HQ20TerminalControl();
				this.setExtend1(control.restoreInfor(funNumAndkey.substring(0,
						2)));
				Log.getInstance()
				.outLog(this.getDeviceSN()+
						"��ǿ�ն�Ҫ������Ӧ��ָ� " + this.getExtend1());
			}
			
//			Log.getInstance()
//			.outLog(this.getDeviceSN()+
//					"========================��ǿ�ն��ϱ��¶�Э�����ݣ�==================" + temperature);
			if (temperature != -1) { // &L �¶Ƚ��� add liuyuan 2010-08-17
				String strCount = hexString.substring(temperature + 2,
						temperature + 3);
				try {
					// ��ȡ�¶ȴ���������
					int count = Integer.parseInt(strCount);
	
//					// ѭ����ȡÿ���������¶�
//					for (int i = 0; i < count; i++) {
//						String tmps = hexString.substring(temperature + 3
//								+ (i * 4), temperature + 7 + (i * 4));
//						String strTemperatures = hexString.substring(
//								temperature + 3 + (i * 4), temperature + 7
//										+ (i * 4));
//						// System.out.println("strTemperature="+strTemperatures);
//						String status = strTemperatures.substring(0, 1);
//						// System.out.println("status="+status);
//						String strTemperature = strTemperatures.substring(1, 4);
//						int intTemperature = Integer.parseInt(strTemperature);
//						// ��ʾΪ����
//						if (status.equals("1"))
//							intTemperature = -intTemperature;
//						
//						temperatures +=",";
//						// System.out.println("status="+strTemperature);
//
//					}
//					if(temperatures.length()>0){
//						temperatures = temperatures.substring(0,temperatures.lastIndexOf(","));
//						this.setTemperature(temperatures);
//					}
					float tmpe=0;
					boolean hasTemperature = false;
					if(count>0){
						hasTemperature =true;
					}
					// ѭ����ȡÿ���������¶�
					for (int i = 0; i < count; i++) {
						String tmps = hexString.substring(temperature + 3
								+ (i * 4), temperature + 7 + (i * 4));
						String strTemperatures = hexString.substring(
								temperature + 3 + (i * 4), temperature + 7
										+ (i * 4));
						// System.out.println("strTemperature="+strTemperatures);
						String status = strTemperatures.substring(0, 1);
						// System.out.println("status="+status);
						String strTemperature = strTemperatures.substring(1, 4);
						
						int intTemperature = Integer.parseInt(strTemperature,16);
						// ��ʾΪ����
						if (status.equals("1"))
							intTemperature = -intTemperature;
						
						float s = Float.valueOf(intTemperature)/10;
						
	
						
						tmpe +=s;
						

					}
					if(hasTemperature){
						tmpe = tmpe/count;
						 java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
						 
						this.setTemperature(df.format(tmpe));
					}
					
//					Log.getInstance()
//					.outLog(this.getDeviceSN()+"===========================��ǿ�ն��ϱ��¶ȣ�"+tmpe+"================================");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (spi != -1) { // &F
				String spstr = hexString.substring(spi + 2, spi + 6);
				int i = 0;
				while (i < spstr.length()) {
					if (spstr.charAt(i) != '0') {
						break;
					}
					i++;
				}
				String spdi = "";
				if (i != spstr.length())
					spdi = spstr.substring(i, spstr.length() - 1) + "."
							+ spstr.charAt(spstr.length() - 1);
				else
					spdi = "0";
				String speeds = this.formatSpeed(spdi + "");
				this.setSpeed(speeds);
			}
			if (lc != -1) {// &C
				String lcstr = hexString.substring(lc + 2, lc + 10);
				String hexLc = Tools.bytesToHexString(lcstr.getBytes());
				String hex = "";
				for (int j = 0; j < hexLc.length(); j = j + 2) {
					hex += hexLc.substring(j + 1, j + 2);
				}
				String distance = this.formatLC(hex);
				this.setLC(distance);
			}
			if (lci != -1) {// &E
				String lcs = hexString.substring(lci + 2, lci + 10);
				int i = 0;
				while (i < lcs.length()) {
					if (lcs.charAt(i) != '0') {
						break;
					}
					i++;
				}
				if (i != lcs.length())
					lcs = lcs.substring(i);
				else
					lcs = "0";
				// String distance = this.formatLC(lcs);
				String distance;
				if (lcs.length() > 1) {
					distance = lcs.substring(0, lcs.length() - 1) + "."
							+ lcs.substring(lcs.length() - 1);
					try {
						Double.parseDouble(distance);
					} catch (Exception e) {
						distance = "0.0";
					}
				} else {
					distance = lcs;
				}
				this.setLC(distance);
			}

			// Date gpsdate = Tools
			// .formatStrToDate(strTime, "yyyy-MM-dd HH:mm:ss");
			Timestamp ts = new Timestamp(new Date().getTime());
			this.setTimeStamp(ts);

			this.setTime(strTime);
			this.setCoordX(x);
			this.setCoordY(y);
			this.setGpstag(gpsTag);
			this.setSpeed(speed);
			this.setDirection(tDirect + "");
			String status = this.getStatusAndAlarm(statusDatas); // �õ�����״̬�ͱ�����Ϣ��
			if (funNumAndkey.startsWith("AB")) {// �ն˽���GPSRS
				this.setReplyByte(getOdograph().getBytes());// Ҫ���ն˻ش������Ϣ
				// this.sentPost(false);
				String onLocate = "*HQ2011AI(A1)#";// �򿪶�λ��Ϣ���ٶ��ϴ�
				this.setReplyByte1(onLocate.getBytes());
				Log.getInstance()
						.outLog(
								"��ǿ�ն�  ��½ʱ���� " + this.getDeviceSN()
										+ " GPS��Ϣ��x=" + x + ",y=" + y
										+ ",speed=" + this.getSpeed()
										+ ",time=" + strTime + "��״̬=" + status
										+ ",���=" + this.isIsPost()
										+ ",����ش����ָ�" + this.getOdograph()
										+ ",�����ٶ��ϴ�ָ�" + onLocate);
			} else if (funNumAndkey.startsWith("AD1")) {// �ն˽���GPSRS
				this.setReplyByte(getOdograph().getBytes());// Ҫ���ն˻ش������Ϣ
				 this.sentPost(true);
				String onLocate = "*HQ2011AI(A1)#";// �򿪶�λ��Ϣ���ٶ��ϴ�
				this.setReplyByte1(onLocate.getBytes());
				Log.getInstance().outLog(
						"��ǿ�ն�  ����GPRS���ݣ� " + this.getDeviceSN() + " GPS��Ϣ��x="
								+ x + ",y=" + y + ",speed=" + this.getSpeed()
								+ ",time=" + strTime + "��״̬=" + status + ",���="
								+ this.isIsPost()+ ",����ش����ָ�" + this.getOdograph()
								+ ",�����ٶ��ϴ�ָ�" + onLocate);
			} else if (funNumAndkey.startsWith("AH")) {// ��·ά��,
				Log.getInstance().outLog(
						"��ǿ�ն�  �ն�Ӧ��ָ�� " + this.getDeviceSN() + " GPS��Ϣ��x=" + x
								+ ",y=" + y + ",speed=" + this.getSpeed()
								+ ",time=" + strTime + "��״̬=" + status + ",���="
								+ this.isIsPost());

			} else if (funNumAndkey.startsWith("Y")) {// �ն�Ӧ��ָ��

				DBService dbservice = new DBServiceImpl();
				dbservice.updateInstructionsState(this.getDeviceSN(), "0",
						funNumAndkey.substring(1)); // ����ָ��״̬

				Log.getInstance().outLog(
						"��ǿ�ն�  �ն�Ӧ��ָ�� " + this.getDeviceSN() + " GPS��Ϣ��x=" + x
								+ ",y=" + y + ",speed=" + this.getSpeed()
								+ ",time=" + strTime + "��״̬=" + status + ",���="
								+ this.isIsPost());
			} else if ((funNumAndkey.startsWith("BA"))) {
				this.sentPost(true);
				Log.getInstance().outLog(
						"��ǿ�ն� ��ʱ�ϱ����� " + this.getDeviceSN() + " GPS��Ϣ��x=" + x
								+ ",y=" + y + ",speed=" + this.getSpeed()
								+ ",time=" + strTime + "��״̬=" + status + ",���="
								+ this.isIsPost());
				
			} else if ((funNumAndkey.startsWith("AA"))) {
				this.sentPost(true);
				Log.getInstance().outLog(
						"��ǿ�ն� �����ϱ����� " + this.getDeviceSN() + " GPS��Ϣ��x=" + x
								+ ",y=" + y + ",speed=" + this.getSpeed()
								+ ",time=" + strTime + "��״̬=" + status + ",���="
								+ this.isIsPost());
				
			}
			this.setStatus(status);

		} catch (Exception ex) {
			Log.getInstance().outLog("���ڻ�ǿ�����ն��ϱ������쳣�ǣ�" + ex.getMessage());
			ex.printStackTrace();
		}
	}

	// �����Ϣ
	public String getOdograph() {
		String tmp = "BH11";
		String cmdStr = HQ20Util.makeCommandStr(tmp, true, true);
		return cmdStr;
	}

	private void specialProtocolFunc(String hexString) {
		int flag = hexString.indexOf(",");
		String head = hexString.substring(0, flag); // eg:[HQ20015810330423
		String deviceId = head.substring(6);
		String end = hexString.substring(flag + 1);
		String backProperties = head.substring(5, 6); // �ظ�����
		int p = hexString.indexOf("&A"); // GPS��λ���ݱ�־λ��
		int sp = hexString.indexOf("&B"); // ״̬�����������־λ��
		int lc = hexString.indexOf("&C"); // �����Ϣ
		String phnum = GBLTerminalList.getInstance().getSimcardNum(deviceId);// ���ڴ��ȡ�ն�SIMCARD
		if (phnum == null || hexString == null || phnum.trim().length() == 0
				|| hexString.trim().length() == 0) {
			Log.getInstance().outLog("ϵͳ��û�����䵽ָ�����նˣ�device_id=" + deviceId);
			return;
		}
		this.setPhnum(phnum);
		this.setDeviceSN(deviceId);
		if (end.startsWith("AO")) {
			DBService dbservice = new DBServiceImpl();
			dbservice.updateInstructionsState(this.getDeviceSN(), "0", Tools
					.bytesToHexString("AO".getBytes())); // ������������ָ��״̬
			return;
		} else if (end.startsWith("Y")) {// �ն�Ӧ��ָ��
			String fkey = end.substring(end.indexOf("]") - 2, end.indexOf("]"));
			DBService dbservice = new DBServiceImpl();
			dbservice.updateInstructionsState(this.getDeviceSN(), "0", fkey); // ����ָ��״̬

		}
		String funNumAndkey = hexString.substring(flag + 1, p); // ���ܱ�š��ؼ���

		if ("1".equals(backProperties)) {
			// ��Ҫ���Ļظ�
			HQ20TerminalControl control = new HQ20TerminalControl();
			this.setExtend1(control
					.restoreSPInfor(funNumAndkey.substring(0, 2)));
		}
	}

	public void parseGPRS(String phnum, byte[] content) {
	}

	public void parseGPRS(byte[] hexString) {
	}

	/**
	 * ����״̬�������������ݵõ�������״̬��
	 * 
	 * @param datas
	 *            String
	 * @return String
	 */
	public String getStatusAndAlarm(String datas) throws Exception {
		DbOperation dop = new DbOperation();
		long caseid = 0;

		StringBuffer buffer = new StringBuffer();
		// StringBuffer tempBuffer = new StringBuffer();
		// for (int s = 0; s < datas.length(); s++) {
		// tempBuffer.append("0" + datas.substring(s, s + 1));
		// }
		// 03000 02000
		// datas = tempBuffer.toString();
		byte[] bArrays = datas.getBytes();// Tools.fromHexString(datas);
		// ״̬���룺
		int b00 = this.getByteBit(bArrays[0], 0);
		int b01 = this.getByteBit(bArrays[0], 1);
		int b02 = this.getByteBit(bArrays[0], 2);
		int b03 = this.getByteBit(bArrays[0], 3);
		if (b00 == 1) {
			buffer.append("���߹���;");
		}
		if (b01 == 1) {
			buffer.append("GSMģ�����;");
		}
		if (b02 == 1) {
			buffer.append("GPSģ�����;");
		}
		if (b03 == 1) {
			buffer.append("������·����;");
		}

		int b10 = this.getByteBit(bArrays[1], 0);
		int b11 = this.getByteBit(bArrays[1], 1);
		int b12 = this.getByteBit(bArrays[1], 2);
		if (b10 == 1) {
			buffer.append("ACC��;");
			this.setAccStatus("1");
		} else {
			this.setAccStatus("0");
		}
		if (b11 == 1) {
			buffer.append("�س�;");
		}
		if (b12 == 1) {
			buffer.append("���ſ�;");
		}

		int b20 = this.getByteBit(bArrays[2], 0);
		if (b20 == 1) {
			buffer.append("˽��״̬;");
		}

		int b30 = this.getByteBit(bArrays[3], 0);
		int b31 = this.getByteBit(bArrays[3], 1);
		int b32 = this.getByteBit(bArrays[3], 2);
		int b33 = this.getByteBit(bArrays[3], 3);
		if (b30 == 1) {
			buffer.append("Զ��ƿ�;");
		}
		if (b31 == 1) {
			buffer.append("��ת�ƿ�;");
		}
		if (b32 == 1) {
			buffer.append("��ת�ƿ�;");
		}
		if (b33 == 1) {
			buffer.append("ɲ���ƿ�;");
		}

		int b40 = this.getByteBit(bArrays[4], 0);
		int b41 = this.getByteBit(bArrays[4], 1);
		int b42 = this.getByteBit(bArrays[4], 2);
		int b43 = this.getByteBit(bArrays[4], 3);
		if (b40 == 1) {
			buffer.append("�����ƿ�or����ƿ�or������;");
		}
		if (b41 == 1) {
			buffer.append("ǰ��ƿ�;");
		}
		if (b42 == 1) {
			buffer.append("���Ź�;");
		}
		if (b43 == 1) {
			buffer.append("����ƿ�;");
		}
		// �������룺
		int b50 = this.getByteBit(bArrays[5], 0);
		int b51 = this.getByteBit(bArrays[5], 1);
		if (b50 == 1) {
			buffer.append("�پ�;");
		}
		if (b51 == 1) {
			buffer.append("����;");
		}

		int b60 = this.getByteBit(bArrays[6], 0);
		int b61 = this.getByteBit(bArrays[6], 1);
		int b62 = this.getByteBit(bArrays[6], 2);
		int b63 = this.getByteBit(bArrays[6], 3);
		if (b60 == 1) {
			buffer.append("����Χ����;");
		}
		if (b61 == 1) {
			buffer.append("����Χ����;");
		}
		if (b62 == 1) {
			buffer.append("���ٱ���;");
		}

		DBService dbservice = new DBServiceImpl();
		if (b62 == 1) {// ���ٱ���
			dbservice.saveActiveAlarm(this, "1");
		}
		if (b60 == 1 || b61 == 1) {// ���򱨾�
			dbservice.saveActiveAlarm(this, "2");
			String cancelAlarm = "*HQ2001BC#";
			this.setReplyByte2(cancelAlarm.getBytes());// �������򱨾�������ȡ��������־λ
		}
		if (b50 == 1) {// ��������
			dbservice.saveActiveAlarm(this, "3");
		}

		if (b63 == 1) {
			buffer.append("ƫ��·�߱���;");
		}

		int b70 = this.getByteBit(bArrays[7], 0);
		int b71 = this.getByteBit(bArrays[7], 1);
		int b72 = this.getByteBit(bArrays[7], 2);
		int b73 = this.getByteBit(bArrays[7], 3);
		if (b70 == 1) {
			buffer.append("�Ƿ�ʱ����ʻ����;");
		}
		if (b71 == 1) {
			buffer.append("ͣ����Ϣʱ�䲻�㱨��;");
		}
		if (b72 == 1) {
			buffer.append("Խվ����;");
		}
		if (b73 == 1) {
			buffer.append("�Ƿ�������;");
		}

		int b80 = this.getByteBit(bArrays[8], 0);
		int b81 = this.getByteBit(bArrays[8], 1);
		int b82 = this.getByteBit(bArrays[8], 2);
		int b83 = this.getByteBit(bArrays[8], 3);
		if (b80 == 1) {
			buffer.append("���;");
		}
		if (b81 == 1) {
			buffer.append("���߱���;");
		}
		if (b82 == 1) {
			buffer.append("��ƿ��ѹ�ͱ���;");
		}
		if (b83 == 1) {
			buffer.append("������󱨾�;");
		}
		// Log.getInstance().outHQLog("HQ״̬��" + buffer.toString());
		return buffer.toString();
	}

	/**
	 * �õ�byte�е�λֵ
	 * 
	 * @param data
	 *            byte
	 * @param pos
	 *            int
	 * @return int
	 */
	public static int getByteBit(byte data, int pos) {
		int bitData = 0;
		byte compare = (byte) Math.pow(2.0, pos);
		if ((data & compare) == compare) {
			bitData = 1;
		}
		return bitData;
	}

	// �������
	private String formatLC(String dis) {
		String ret = "";
		// long lc = Tools.byte2Long(dis.getBytes()) * 2;
		long lc = Tools.byte2Long(Tools.fromHexString(dis)) * 2;

		double dlc = lc * 1.852 / 3600;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		ret = nf.format(dlc).replaceAll("\\,", "");
		return ret;
	}

	private String formatSpeed(String tmpSpeed) {
		String ret = "";
		double speed = 0;
		if (tmpSpeed != null) {
			try {
				speed = Double.parseDouble(tmpSpeed);
			} catch (java.lang.NumberFormatException ex) {
				ex.printStackTrace();
			}
			speed = speed * 1.852;
		}
		// ret = "" + speed;
		// if (ret.length() > 4) {
		// ret = ret.substring(0, 4);
		// }
		NumberFormat nformat = NumberFormat.getNumberInstance();
		nformat.setMaximumFractionDigits(2);
		nformat.setMinimumFractionDigits(2);
		ret = nformat.format(speed).replaceAll("\\,", "");

		return ret;
	}

}
