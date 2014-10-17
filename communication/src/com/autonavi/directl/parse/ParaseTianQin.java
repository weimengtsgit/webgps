package com.autonavi.directl.parse;

import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Date;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.dbutil.service.DBService;
import com.autonavi.directl.dbutil.service.DBServiceImpl;
 
import com.autonavi.lbsgateway.GBLTerminalList;
import com.autonavi.lbsgateway.GPRSThreadList;
import com.autonavi.lbsgateway.alarmpool.AlarmQueue;

/**
 * <p>
 * Title: GPS����
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
 * @author yang lei
 * @version 1.0
 */
public class ParaseTianQin extends ParseBase {
	Timestamp ts = null;

	/**
	 * parseGPRS
	 * 
	 * @param hexString
	 *            String
	 * @todo Implement this com.mapabc.directl.parse.ParseBase method
	 */
	// 2490906186041248011308093958792300116179891e000000fffffbffff0019
	public void parseGPRS(String hexString) {

		Log.getInstance().outLog("SWYJԭʼ���ݣ�" + hexString);

		if (hexString.startsWith("24")) {

			this.parse$Position(hexString);
			Log.getInstance().outLog(
					"˼άԶ����׼ģʽ���ݣ�deviceid=" + this.getDeviceSN() + "lng="
							+ this.getCoordX() + ",lat=" + this.getCoordY()
							+ ",speed=" + this.getSpeed() + ",direction="
							+ this.getDirection() + ",date=" + this.getTime());
			// this.requestDistance();
		} else if (hexString.startsWith("2a48512c")
				|| hexString.startsWith("2a5448")) {
			byte[] cont = Tools.fromHexString(hexString);
			// *XX,YYYYYYYYYY,V1,HHMMSS,S,latitude,D,longitude,G,speed,direction,DDMMYY,vehicle_status#
			this.parseV1Position(cont);
		} else if (hexString.startsWith("58")) {
			Socket socketAddress = this.getSocket();
			String deviceId = GPRSThreadList.getInstance()
					.getDeviceIdByTcpAddress(socketAddress);
			Log.getInstance().outLog(
					"�ӻ����ȡ ��TCP�ͻ��˵�ַ�����кţ���=����" + socketAddress + "," + deviceId
							+ "��");
			this.setDeviceSN(deviceId);

			String phnum = null;
			phnum = GBLTerminalList.getInstance().getSimcardNum(deviceId);// ���ڴ��ȡ�ն�SIMCARD
			if (phnum == null || phnum.trim().length() == 0) {
				Log.getInstance().outLog("ϵͳ��û�����䵽ָ�����նˣ�device_id=" + deviceId);
				return;
			}
			this.setPhnum(phnum);

			this.parseXPosition(hexString);

			Log.getInstance().outLog(
					"˼άԶ�� X��¼ģʽ����,deviceid=" + this.getDeviceSN() + "lng="
							+ this.getCoordX() + ",lat=" + this.getCoordY()
							+ ",speed=" + this.getSpeed() + ",direction="
							+ this.getDirection() + ",date=" + this.getTime()+",distance="+this.getLC()+",�¶�="+this.getTemperature());

		}

		Log.getInstance().outLog("��άԶ����������״̬��Ϣ��" + this.getAlarmDesc());

	}

	public void requestDistance() {
		// *XX,YYYYYYYYYY,S32,HHMMSS,M#
		String lc = "*HQ," + this.getDeviceSN() + ",S32," + Tools.getCurHMS()
				+ ",1#";
		this.setReplyByte(lc.getBytes());
		Log.getInstance().outLog(this.getDeviceSN() + " ������̻ش���" + lc);
	}

	// ����̡��¶�λ����Ϣ
	private void parseXPosition(String hex) {
		int secLen = hex.length() / 64;
		for (int i = 0; i < secLen; i++) {
			String hexString = hex.substring(i * 64, i * 64 + 64);
			String dis = hexString.substring(2, 12);
			String sdis = Tools.removeZeroStr(dis);
			long ldis = Long.parseLong(sdis);
			float ddis = (ldis * 0.51444f) / 1000; // ��̣���λ����
			this.setLC(ddis + "");

			String tempHex = hexString.substring(32, 34);
			String stemp = "";
			

			String hms = hexString.substring(12, 12 + 6);
			String dmy = hexString.substring(18, 18 + 6);
			String ystr = hexString.substring(24, 28) + "."
					+ hexString.substring(28, 32);
			String latitude = this.getLatitudeValue(ystr);
			String xstr = hexString.substring(34, 39) + "."
					+ hexString.substring(39, 43);
			String longitude = this.getLongitudeValue(xstr);

			byte[] gs = Tools.fromHexString("0" + hexString.substring(43, 44));

			StringBuffer sbuf = new StringBuffer();

			String speed = hexString.substring(44, 47);
			speed = this.formatSpeed(speed);
			String direct = hexString.substring(47, 50);
			direct = Integer.parseInt(direct) + "";

			String vehicleStatus = hexString.substring(50, 58);
			byte[] statusBytes = Tools.fromHexString(vehicleStatus);// vehicleStatus.getBytes();

			String Usr_alarm_flag = hexString.substring(58, 60);
			String recordNo = hexString.substring(62, 64);

			String gpstime = Tools.conformtime(hms, dmy);

			ts = new Timestamp(new Date().getTime());

			this.setCoordX(longitude);
			this.setCoordY(latitude);
			// this.setPhnum(phnum);
			this.setTime(gpstime);
			this.setTimeStamp(ts);
			this.setSpeed(speed);
			this.setDirection(direct);
			String alarmStatus = null;
			alarmStatus = this.getAlarmStatus(statusBytes);
 			
			if (tempHex.equals("ff")) {
				//stemp = "127.5";
				Log.getInstance().outLog(this.getDeviceSN() + " û�������¶ȴ�����.");
			} else if (tempHex.equals("fe")) {
				//stemp = "127";
				Log.getInstance().outLog(this.getDeviceSN() + " �����в���¶ȴ�����.");
			} else {
				stemp = Integer.parseInt(tempHex, 16) / 2.0 + ""; // �¶�				 
				if (this.getByteBit(gs[0], 0) == 1) {
					stemp = "-" + stemp;
				}
				this.setTemperature(stemp);

			}
			

			if (this.getByteBit(gs[0], 1) == 1) {
				sbuf.append("��λ������Ч;");
				this.sentPost(true);
			} else {
				sbuf.append("��λ������Ч;");
			}
			if (this.getByteBit(gs[0], 2) == 1) {
				sbuf.append("��γ");
			} else {
				sbuf.append("��γ");
			}
			if (this.getByteBit(gs[0], 3) == 1) {
				sbuf.append("����");
			} else {
				sbuf.append("����");
			}

		}

	}

	private void parseV1Position(byte[] cont) {
		String dataStr = new String(cont);
		if (dataStr != null) {
			String[] contArr = dataStr.split("\\*HQ");
			for (int i = 0; i < contArr.length; i++) {
				if (!contArr[i].endsWith("#")) {
					continue;
				} else {
					dataStr = "*HQ" + contArr[i];
				}

				String[] contents = dataStr.split(",");
				String gpssn = contents[1];
				this.setDeviceSN(gpssn);
				String phnum = null;
				phnum = GBLTerminalList.getInstance().getSimcardNum(gpssn);// ���ڴ��ȡ�ն�SIMCARD
				if (phnum == null || phnum.trim().length() == 0) {
					Log.getInstance()
							.outLog("ϵͳ��û�����䵽ָ�����նˣ�device_id=" + gpssn);
					return;
				}
				this.setPhnum(phnum);

				String modeFlag = contents[2];

				String HHMMSS = null; // ���ػ�ʱ��,��׼ʱ�䣬�뱱��ʱ����8Сʱʱ��
				String s = null; // ������Чλ��A/V��
				String latitude = null; // γ��
				String d = null; // γ�ȱ�־��N����γ��S����γ��
				String longitude = null; // ����
				String g = null; // ���ȱ�־��E��������W��������
				String speed = null; // �ٶ�,��Χ000.00 ~ 999.99 �ڣ�������λС��
				String direction = null; // ��λ�ǣ�����Ϊ0�ȣ��ֱ���1�ȣ�˳ʱ�뷽��
				String ddmmyy = null; // ��/��/��
				String dateConfirm = null;
				String keyS = null;
				String status = null;
				 
				
				String vehicleStatus = null; // ����״̬�������ֽڣ���ʾ���ػ�����״̬����������״̬�Լ�����״̬��
				if (modeFlag.equals("V1")) {
					HHMMSS = contents[3];
					s = contents[4];
					latitude = contents[5];
					d = contents[6];
					longitude = contents[7];
					g = contents[8];
					speed = this.formatSpeed(contents[9]);
					direction = contents[10];
					ddmmyy = contents[11];
					vehicleStatus = contents[12].substring(0, contents[12]
							.indexOf("#") + 1);
					 keyS = "D1";
					DBService dbservice = new DBServiceImpl();
					dbservice.updateNoCmdIdInstructionsState(gpssn, "0", keyS); // ����Ƶ������ָ��״̬

				} else if (modeFlag.equals("V2")) {

				} else if (modeFlag.equals("V4")) {
					// *XX,YYYYYYYYYY,V4,CMD,hhmmss,HHMMSS,S,latitude,D,longitude,G,speed,direction,DDMMYY,vehicle_status#
					Log.getInstance().outLog("V4Ӧ��ָ�" + dataStr);
					String cmd = contents[3];
					if (cmd.equals("S14")) {
						dateConfirm = contents[8];
						String param = contents[4] + "," + contents[5] + ","
								+ contents[6] + "," + contents[7];
						keyS = cmd + "," + dateConfirm + "," + param;
						status = "0";
						Log.getInstance().outLog(
								this.getDeviceSN() + " ��������Ӧ��");
					} else if (cmd.equals("S18")) {
						dateConfirm = contents[6];
						String param = contents[4] + "," + contents[5];
						keyS = cmd + "," + dateConfirm + "," + param;
						status = "0";
						Log.getInstance().outLog(
								this.getDeviceSN() + " �������򱨾�����ʱ��Ӧ��");
					} else if (cmd.equals("S21")) {
						dateConfirm = contents[6];
						String param = contents[4] + "," + contents[5];
						keyS = cmd + "," + dateConfirm + "," + param;
						status = "0";
						Log.getInstance().outLog(
								this.getDeviceSN() + " ����Χ��Ӧ��");
					} else if (cmd.equals("S20")) {
						String desc = contents[4];
						if (desc != null && desc.equals("ERROR")) {
							status = "2";
							Log.getInstance().outLog(
									this.getDeviceSN() + " ��֧�ֶ��͵繦��");
						} else {
							status = "0";
						}
						dateConfirm = contents[5];
						keyS = cmd + "," + dateConfirm;

					} else if (cmd.equals("S6")) {
						String desc = contents[4];
						if (desc != null && desc.equals("ERROR")) {
							status = "2";
							 
						} else {
							status = "0";
						}
						dateConfirm = contents[5];
						keyS = cmd + "," + dateConfirm;

					} else if (cmd.equals("R8")) {
						String desc = contents[4];
						if (desc != null && desc.equals("ERROR")) {
							status = "2";
							Log.getInstance().outLog(
									this.getDeviceSN() + " ���ڽ�ֹ����״̬");
						} else {
							status = "0";
						}
						dateConfirm = contents[5];
						keyS = cmd + "," + dateConfirm;
					} else if (cmd.equals("S23")) {
						String addr = contents[4].replaceAll("\\.", ",")
								.replaceAll("\\:", ",");
						dateConfirm = contents[5];
						keyS = cmd + "," + dateConfirm + "," + addr;
						status = "0";
						Log.getInstance().outLog(
								this.getDeviceSN() + " ͨѶ��ַ����Ӧ��");
					} else if (cmd.equals("S24")) {
						String addr = contents[4];
						dateConfirm = contents[5];
						keyS = cmd + "," + dateConfirm + "," + addr;
						status = "0";
						Log.getInstance().outLog(
								this.getDeviceSN() + " APN����Ӧ��");
					} else if (cmd.equals("S26")) { 
						
					} else if (cmd.equals("S31")) {
						String desc = contents[4];
						if (desc != null && desc.equals("ERROR")) {
							status = "2";
							Log.getInstance().outLog(
									this.getDeviceSN() + " û�д�����");
						} else {
							status = "0";
						}
						dateConfirm = contents[5];
						keyS = cmd + "," + dateConfirm;
					} else if (cmd.endsWith("S32")) {
						String dis = contents[4];
						double fdis = Double.parseDouble(dis) * 0.51444 / 1000; // ��λ����
						Log.getInstance().outLog(
								this.getDeviceSN() + "������ݣ�" + fdis + "����");
						dateConfirm = contents[5];
						keyS = cmd + "," + dateConfirm;
					} else if (cmd.equals("S34")) {
						String desc = contents[4];

						dateConfirm = contents[5];
						keyS = cmd + "," + dateConfirm;
					} else if (cmd.equals("S40")) {
						// *HQ,6091127203,V4,S40,2,1800,1200,180,180,FF,093234,001232,V,3958.8510,N,11617.9313,E,000.00,089,160710,FFFFFBFF#
						// *HQ,6091127203, S40,093234,2,1800,1200,180,180,FF#
						keyS = contents[3] + "," + contents[10];
						String  param =   contents[4] + "," + contents[5] + "," + contents[6] + "," + contents[7] + "," + contents[8] + "," + contents[9];
		 				Log.getInstance().outLog(
								this.getDeviceSN() + "����ƣ�ͼ�ʻ������"
										+ param);

					} else {
						dateConfirm = contents[4];
						keyS = cmd + "," + dateConfirm;
						HHMMSS = contents[5];
						status = "0";
					}
				 
					Log.getInstance().outLog("keyS=" + keyS);
					try {
						DBService dbservice = new DBServiceImpl();
						dbservice.updateInstructionsState(gpssn, status, keyS);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// ����ָ��״̬
				}
				if (s.equals("A")) {
					if (longitude == null || "".equals(longitude)) {
						longitude = String.valueOf(0);
					} else {
						longitude = this.getLongitudeValue(longitude);
					}

					if (latitude == null || "".equals(latitude)) {
						latitude = String.valueOf(0);
					} else {
						latitude = this.getLatitudeValue(latitude);
					}

					if (direction == null || "".equals(direction)) {
						direction = String.valueOf(0);
					} else {
						direction = Integer.parseInt(direction) + "";
					}

					String gpstime = Tools.conformtime(HHMMSS, ddmmyy);
					// Date gdate = Tools.formatStrToDate(gpstime,
					// "yyyy-MM-dd HH:mm:ss");
					ts = new Timestamp(new Date().getTime());

					this.setCoordX(longitude);
					this.setCoordY(latitude);
					this.setPhnum(phnum);
					this.setDeviceSN(gpssn);

					this.setTime(gpstime);
					this.setTimeStamp(ts);
					this.setSpeed(speed);
					this.setDirection(direction);

					byte[] statusBytes = Tools.fromHexString(vehicleStatus
							.substring(0, vehicleStatus.length() - 1));// vehicleStatus.getBytes();
					String alarmStatus = null;
					alarmStatus = this.getAlarmStatus(statusBytes);
					this.setAlarmDesc(alarmStatus);
					this.sentPost(true);
				}
			}
		}
	}

	// ȥ���ַ���ǰ��0
	private String removeZeroStr(String str) {
		String ret = null;
		if (str != null && str.trim() != "") {
			int i = 0;
			while (i < str.length()) {
				if (str.charAt(i) != '0') {
					break;
				}
				i++;
			}
			if (i != str.length())
				ret = str.substring(i);
			else
				ret = "0";
		}

		return ret;
	}

	private void parse$Position(String hexString) {
		// 249090618604 124801 130809 39587923 00 116179891e 000000 fffffbff ff
		// 00 19
		int secLen = hexString.length() / 64;
		for (int i = 0; i < secLen; i++) {
			String hex = hexString.substring(i * 64, i * 64 + 64);

			if (hex.length() < 64)
				return;

			String device_id = hex.substring(2, 2 + 10);
			this.setDeviceSN(device_id);

			String phnum = null;
			phnum = GBLTerminalList.getInstance().getSimcardNum(device_id);// ���ڴ��ȡ�ն�SIMCARD
			if (phnum == null || phnum.trim().length() == 0) {
				Log.getInstance()
						.outLog("ϵͳ��û�����䵽ָ�����նˣ�device_id=" + device_id);
				return;
			}

			phnum = device_id;
			String hms = hex.substring(12, 12 + 6);
			String dmy = hex.substring(18, 18 + 6);
			String ystr = hex.substring(24, 28) + "." + hex.substring(28, 32);
			String latitude = this.getLatitudeValue(ystr);
			String xstr = hex.substring(34, 39) + "." + hex.substring(39, 43);
			String longitude = this.getLongitudeValue(xstr);

			byte[] gs = Tools.fromHexString("0" + hex.substring(43, 44));

			StringBuffer sbuf = new StringBuffer();

			String speed = hex.substring(44, 47);
			speed = this.formatSpeed(speed);
			String direct = hex.substring(47, 50);
			direct = Integer.parseInt(direct) + "";

			String vehicleStatus = hex.substring(50, 58);
			byte[] statusBytes = Tools.fromHexString(vehicleStatus);// vehicleStatus.getBytes();

			String Usr_alarm_flag = hex.substring(58, 60);
			String recordNo = hex.substring(62, 64);

			String gpstime = Tools.conformtime(hms, dmy);
			// Date gdate = Tools.formatStrToDate(gpstime,
			// "yyyy-MM-dd HH:mm:ss");
			ts = new Timestamp(new Date().getTime());

			this.setCoordX(longitude);
			this.setCoordY(latitude);
			this.setPhnum(phnum);
			this.setTime(gpstime);
			this.setTimeStamp(ts);
			this.setSpeed(speed);
			this.setDirection(direct);
			String alarmStatus = null;
			alarmStatus = this.getAlarmStatus(statusBytes);

			if (this.getByteBit(gs[0], 1) == 1) {
				sbuf.append("��λ������Ч;");
				this.sentPost(true);
			} else {
				sbuf.append("��λ������Ч;");
			}
			if (this.getByteBit(gs[0], 2) == 1) {
				sbuf.append("��γ");
			} else {
				sbuf.append("��γ");
			}
			if (this.getByteBit(gs[0], 3) == 1) {
				sbuf.append("����");
			} else {
				sbuf.append("����");
			}
		}
	}

	private String getGpsStatus(byte gs) {
		String ret = "";
		StringBuffer sbuf = new StringBuffer();
		if (this.getByteBit(gs, 1) == 1) {
			sbuf.append("��λ������Ч;");
		} else {
			sbuf.append("��λ������Ч;");
		}
		if (this.getByteBit(gs, 2) == 1) {
			sbuf.append("��γ");
		} else {
			sbuf.append("��γ");
		}
		if (this.getByteBit(gs, 3) == 1) {
			sbuf.append("����");
		} else {
			sbuf.append("����");
		}
		return ret;
	}

	private String getAlarmStatus(byte[] statusBytes) {

		StringBuffer statusBuffer = new StringBuffer();
		DBService dbs = new DBServiceImpl();

		byte  Status1 = statusBytes[0];
		byte  Status2 = statusBytes[1];
		if (this.getByteBit(Status1, 0) == 0){
			statusBuffer.append("�¶ȱ�����");
			this.setAlarmType("11");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		
		byte vehCmptStatus = statusBytes[2];
		byte alarmStatus = statusBytes[3];
		if (this.getByteBit(vehCmptStatus, 0) == 0) {
			statusBuffer.append("���ſ���");
		}
		if (this.getByteBit(vehCmptStatus, 1) == 0) {
			statusBuffer.append("���������");
		}
		if (this.getByteBit(vehCmptStatus, 2) == 0) {
			statusBuffer.append("ACC�أ�");
			this.setAccStatus("0");
		} else {
			this.setAccStatus("1");
		}

		if (this.getByteBit(alarmStatus, 0) == 0) {
			statusBuffer.append("������");
		}
		if (this.getByteBit(alarmStatus, 1) == 0) {
			String confirm = "*HQ,"+this.getDeviceSN()+",A1,203100#";
			this.setReplyByte(confirm.getBytes());
			//Log.getInstance().outLog(this.getDeviceSN()+" �پ�ȷ�Ͻ����"+confirm);
			statusBuffer.append("�پ���");
			this.setAlarmType("3");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		if (this.getByteBit(alarmStatus, 2) == 0) {
			statusBuffer.append("���ٱ�����");
			this.setAlarmType("1");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		if (this.getByteBit(alarmStatus, 3) == 0) {
			statusBuffer.append("�Ƿ���𱨾���");
		}
		if (this.getByteBit(alarmStatus, 4) == 0) {
			statusBuffer.append("��ֹʻ��Խ�籨����");
			this.setAlarmType("2");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		if (this.getByteBit(alarmStatus, 7) == 0) {
			statusBuffer.append("��ֹʻ��Խ�籨����");
			this.setAlarmType("2");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		Log.getInstance().outLog(
				this.getDeviceSN() + "����״̬��Ϣ��" + statusBuffer.toString());
		return statusBuffer.toString();
	}

	private String formatSpeed(String tmpSpeed) {
		String ret = "";
		double speed = 0;
		if (tmpSpeed != null && !tmpSpeed.trim().equals("")) {
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

	/**
	 * parseGPRS
	 * 
	 * @param phnum
	 *            String
	 * @param content
	 *            String
	 * @todo Implement this com.mapabc.directl.parse.ParseBase method
	 */
	public void parseGPRS(String phnum, String content) {
	}

	/**
	 * parseGPRS
	 * 
	 * @param phnum
	 *            String
	 * @param content
	 *            byte[]
	 * @todo Implement this com.mapabc.directl.parse.ParseBase method
	 */
	public void parseGPRS(String phnum, byte[] content) {
	}

	/**
	 * parseGPRS
	 * 
	 * @param hexString
	 *            byte[]
	 * @todo Implement this com.mapabc.directl.parse.ParseBase method
	 */
	public void parseGPRS(byte[] hexString) {

	}

	/**
	 * 
	 * @param longitude
	 *            String
	 * @return String
	 */
	public String getLongitudeValue(String longitude) {
		String result = "";
		double degree = Double.parseDouble(longitude.substring(0, 3));
		double xDegree = Double.parseDouble(longitude.substring(3, longitude
				.length())) / 60;
		java.text.DecimalFormat df1 = new java.text.DecimalFormat("##.000000");

		String blxs = "0" + df1.format(xDegree); // ����6λС��
		result = String.valueOf(degree + Double.parseDouble(blxs));
		return result;
	}

	/**
	 * 
	 * @param latitude
	 *            String
	 * @return String
	 */
	public String getLatitudeValue(String latitude) {
		String result = "";
		double degree = Double.parseDouble(latitude.substring(0, 2));
		double xDegree = Double.parseDouble(latitude.substring(2, latitude
				.length())) / 60;
		java.text.DecimalFormat df1 = new java.text.DecimalFormat("##.000000");
		String blxs = "0" + df1.format(xDegree); // ����6λС��
		result = String.valueOf(degree + Double.parseDouble(blxs));
		return result;
	}

	public String getTime(String value) {
		String result = "";
		String temp = value.substring(0, 2);
		int hour = Integer.parseInt(temp) + 8;
		if (hour < 10) {
			result = "0" + String.valueOf(hour) + value.substring(2);
		} else if (10 <= hour && hour <= 23) {
			result = String.valueOf(hour) + value.substring(2);
		} else if (hour > 23) {
			result = "0" + String.valueOf(Math.abs(hour - 24))
					+ value.substring(2);
		}
		return result;
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

	/**
	 * parseSMS
	 * 
	 * @param phnum
	 *            String
	 * @param content
	 *            String
	 * @todo Implement this com.mapabc.directl.parse.ParseBase method
	 */
	public void parseSMS(String phnum, String content) {
	}

	public static void main(String[] args) { 
		ParaseTianQin tj = new ParaseTianQin();
		tj.parseXPosition("5803489649760939170210103417225133117126363e000010fefffbffff0023");
		
	}

}
