package com.autonavi.directl.parse;

import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;
import java.util.regex.*;

import com.autonavi.directl.parse.PictureCacher;
import com.mapabc.geom.CoordCvtAPI;

public abstract class ParseBase implements Cloneable {
	private String coordX;
	private String coordY;
	private String time;
	private String phnum;
	private String direction;
	private String speed;
	private String gpstag;
	private String altitude; // �߳�
	private String satellites; // ���Ǹ���
	private String status; // ״̬
	private String lc; // ���
	private String picUrl; // �ն��ϴ���ͼƬURL
	private String alarmDesc; // ��������
	private String temperature;// �¶�ֵ
	private String humidity;//ʪ��ֵ
	private String accStatus = "";// Ĭ��δ���
	private String signalFlag = "0";
	private String programVersion = "";
	private String lastDistance = "0";

	private String bat = "";

	private String varExt1 = "";
	private String varExt2 = "";
	private String varExt3 = "";
	private String varExt4 = "";
	private String varExt5 = "";
	private String varExt6 = "";
	private String varExt7 = "";
	private String varExt8 = "";
	private String varExt9 = "";
	private String varExt10 = "";
	private String varExt11 = "";
	private String varExt12 = "";
	private String varExt13 = "";
	private String varExt14 = "";
	private String varExt15 = "";
	private String varExt16 = "";
	private String varExt17 = "";
	private String varExt18 = "";
	private String varExt19 = "";
	private String varExt20 = "";
	private String varExt21 = "";
	private String varExt22 = "";

	private String state = "";
	private String stateShell = "";
	private String stateAnt = "";
	private String stateWire = "";
	private String relay1 = "";
	private String reasonRelay1 = "";

	private boolean isvalid; // �Ƿ��ֵ����
	private boolean isPost; // �Ƿ����

	private String extend1; // ��չ1
	private String extend2; // ��չ2
	private String extend3; // ��չ3

	private boolean isReply; // �Ƿ���Ҫ�ظ�
	private String reply; // �ظ���Ϣ
	private String deviceSN; // �豸ID
	private String address; // ��ַ
	private String encodex;
	private String encodey;
	private byte[] replyByte;// ��Ӧ��Ϣ
	private byte[] replyByte1;
	private byte[] replyByte2;
	private String locateStatus = "1"; // ��λ״̬ 0δ��λ 1�Ѷ�λ
	private int packsize;

	private String sequence; // ��ˮ��
	private String positionNum;// λ����Ϣ����

	private Timestamp timeStamp;

	private String oemCode; // �ն�OEM��
	private String locateType = "1"; // 0:LBS 1:GPS,Ĭ��GPS
	private String alarmType; // �������� 1���� 2���� 3���� 4���� 6ƫ��
	private boolean isAlarm; // �Ƿ��Ǳ�����¼

	private Socket socket;// �ͻ��˵�ַ
	private ArrayList<ParseBase> parseList = new ArrayList<ParseBase>();

	public String getHumidity() {
    return humidity;
  }

  public void setHumidity(String humidity) {
    this.humidity = humidity;
  }

  public String getLc() {
		return lc;
	}

	public void setLc(String lc) {
		this.lc = lc;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getSignalFlag() {
		return signalFlag;
	}

	public void setSignalFlag(String signalFlag) {
		this.signalFlag = signalFlag;
	}

	public String getProgramVersion() {
		return programVersion;
	}

	public void setProgramVersion(String programVersion) {
		this.programVersion = programVersion;
	}

	public String getLastDistance() {
		return lastDistance;
	}

	public void setLastDistance(String lastDistance) {
		this.lastDistance = lastDistance;
	}

	public String getBat() {
		return bat;
	}

	public void setBat(String bat) {
		this.bat = bat;
	}

	public String getVarExt1() {
		return varExt1;
	}

	public void setVarExt1(String varExt1) {
		this.varExt1 = varExt1;
	}

	public String getVarExt2() {
		return varExt2;
	}

	public void setVarExt2(String varExt2) {
		this.varExt2 = varExt2;
	}

	public String getVarExt3() {
		return varExt3;
	}

	public void setVarExt3(String varExt3) {
		this.varExt3 = varExt3;
	}

	public String getVarExt4() {
		return varExt4;
	}

	public void setVarExt4(String varExt4) {
		this.varExt4 = varExt4;
	}

	public String getVarExt5() {
		return varExt5;
	}

	public void setVarExt5(String varExt5) {
		this.varExt5 = varExt5;
	}

	public String getVarExt6() {
		return varExt6;
	}

	public void setVarExt6(String varExt6) {
		this.varExt6 = varExt6;
	}

	public String getVarExt7() {
		return varExt7;
	}

	public void setVarExt7(String varExt7) {
		this.varExt7 = varExt7;
	}

	public String getVarExt8() {
		return varExt8;
	}

	public void setVarExt8(String varExt8) {
		this.varExt8 = varExt8;
	}

	public String getVarExt9() {
		return varExt9;
	}

	public void setVarExt9(String varExt9) {
		this.varExt9 = varExt9;
	}

	public String getVarExt10() {
		return varExt10;
	}

	public void setVarExt10(String varExt10) {
		this.varExt10 = varExt10;
	}

	public String getVarExt11() {
		return varExt11;
	}

	public void setVarExt11(String varExt11) {
		this.varExt11 = varExt11;
	}

	public String getVarExt12() {
		return varExt12;
	}

	public void setVarExt12(String varExt12) {
		this.varExt12 = varExt12;
	}

	public String getVarExt13() {
		return varExt13;
	}

	public void setVarExt13(String varExt13) {
		this.varExt13 = varExt13;
	}

	public String getVarExt14() {
		return varExt14;
	}

	public void setVarExt14(String varExt14) {
		this.varExt14 = varExt14;
	}

	public String getVarExt15() {
		return varExt15;
	}

	public void setVarExt15(String varExt15) {
		this.varExt15 = varExt15;
	}

	public String getVarExt16() {
		return varExt16;
	}

	public void setVarExt16(String varExt16) {
		this.varExt16 = varExt16;
	}

	public String getVarExt17() {
		return varExt17;
	}

	public void setVarExt17(String varExt17) {
		this.varExt17 = varExt17;
	}

	public String getVarExt18() {
		return varExt18;
	}

	public void setVarExt18(String varExt18) {
		this.varExt18 = varExt18;
	}

	public String getVarExt19() {
		return varExt19;
	}

	public void setVarExt19(String varExt19) {
		this.varExt19 = varExt19;
	}

	public String getVarExt20() {
		return varExt20;
	}

	public void setVarExt20(String varExt20) {
		this.varExt20 = varExt20;
	}

	public String getVarExt21() {
		return varExt21;
	}

	public void setVarExt21(String varExt21) {
		this.varExt21 = varExt21;
	}

	public String getVarExt22() {
		return varExt22;
	}

	public void setVarExt22(String varExt22) {
		this.varExt22 = varExt22;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateShell() {
		return stateShell;
	}

	public void setStateShell(String stateShell) {
		this.stateShell = stateShell;
	}

	public String getStateAnt() {
		return stateAnt;
	}

	public void setStateAnt(String stateAnt) {
		this.stateAnt = stateAnt;
	}

	public String getStateWire() {
		return stateWire;
	}

	public void setStateWire(String stateWire) {
		this.stateWire = stateWire;
	}

	public String getRelay1() {
		return relay1;
	}

	public void setRelay1(String relay1) {
		this.relay1 = relay1;
	}

	public String getReasonRelay1() {
		return reasonRelay1;
	}

	public void setReasonRelay1(String reasonRelay1) {
		this.reasonRelay1 = reasonRelay1;
	}

	public boolean isPost() {
		return isPost;
	}

	public void setPost(boolean isPost) {
		this.isPost = isPost;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEncodex() {
		return encodex;
	}

	public void setEncodex(String encodex) {
		this.encodex = encodex;
	}

	public String getEncodey() {
		return encodey;
	}

	public void setEncodey(String encodey) {
		this.encodey = encodey;
	}

	public void setReply(boolean isReply) {
		this.isReply = isReply;
	}

	/**
	 * @return the alarmType
	 */
	public String getAlarmType() {
		return this.alarmType;
	}

	/**
	 * @param alarmType
	 *            the alarmType to set
	 */
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	/**
	 * @return the locateType
	 */
	public String getLocateType() {
		return this.locateType;
	}

	/**
	 * @param locateType
	 *            the locateType to set
	 */
	public void setLocateType(String locateType) {
		this.locateType = locateType;
	}

	public String getOemCode() {
		return oemCode;
	}

	public void setOemCode(String oemCode) {
		this.oemCode = oemCode;
	}

	/**
	 * @return timeStamp
	 */
	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp
	 *            Ҫ���õ� timeStamp
	 */
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public ParseBase() {
	}

	/**
	 * @return positionNum
	 */
	public String getPositionNum() {
		return positionNum;
	}

	/**
	 * @param positionNum
	 *            Ҫ���õ� positionNum
	 */
	public void setPositionNum(String positionNum) {
		this.positionNum = positionNum;
	}

	/**
	 * @return sequence
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * @param sequence
	 *            Ҫ���õ� sequence
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getEncodeX() {
		return encodex;
	}

	public void setEncodeX(String encodex) {
		this.encodex = encodex;
	}

	public String getEncodeY() {
		return encodey;
	}

	public void setEncodeY(String encodey) {
		this.encodey = encodey;
	}

	public String getAdress() {
		return address;
	}

	public void setAdress(String address) {
		this.address = address;
	}

	public String getCoordX() {
		return coordX;
	}

	public void setCoordX(String coordX) {
		this.coordX = coordX;
	}

	public String getCoordY() {
		return coordY;
	}

	public void setCoordY(String coordY) {
		this.coordY = coordY;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPhnum() {
		return phnum;
	}

	public void setPhnum(String phnum) {
		this.phnum = phnum;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getGpstag() {
		return gpstag;
	}

	public void setGpstag(String gpstag) {
		this.gpstag = gpstag;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

	public String getSatellites() {
		return satellites;
	}

	public void setSatellites(String satellites) {
		this.satellites = satellites;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLC() {
		return lc;
	}

	public void setLC(String lc) {
		this.lc = lc;
	}

	public String getPicURL() {
		return this.picUrl;
	}

	public void setPicURL(String picurl) {
		this.picUrl = picurl;
	}

	public String getAlarmDesc() {
		return this.alarmDesc;
	}

	public boolean isIsvalid() {
		return isvalid;
	}

	public String getExtend1() {
		return extend1;
	}

	public String getExtend2() {
		return extend2;
	}

	public String getExtend3() {
		return extend3;
	}

	public boolean isIsPost() {
		return isPost;
	}

	public boolean getIsReply() {
		return isReply;
	}

	public String getReply() {
		return reply;
	}

	public String getDeviceSN() {
		return deviceSN;
	}

	public int getPacksize() {
		return packsize;
	}

	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
	}

	public void setIsvalid(boolean isvalid) {
		this.isvalid = isvalid;
	}

	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}

	public void setExtend2(String extend2) {
		this.extend2 = extend2;
	}

	public void setExtend3(String extend3) {
		this.extend3 = extend3;
	}

	public void setIsPost(boolean isPost) {
		this.isPost = isPost;
	}

	public void setIsReply(boolean isReply) {
		this.isReply = isReply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}

	public void setPacksize(int packsize) {
		this.packsize = packsize;
	}

	// ����GPRS��ʽ�ϱ�������
	// public abstract void parseGPRS(String phnum, String content);
	// public abstract void parseGPRS(String phnum, byte[] content);

	// ����GPRS��ʽ�ϱ�������,hexStringΪʮ���Ƶ��ַ�
	public abstract void parseGPRS(String hexString);

	// public abstract void parseGPRS(byte[] hexString);

	public abstract void parseSMS(String phnum, String content);

	public void showGPSInfo() {
		String tmp = "����:" + this.coordX + "\tγ��:" + this.coordY + "\t��ʶ:"
				+ this.gpstag;
		tmp += "\n" + "�ٶ�:" + this.speed + "(��λ:km/h)" + "\t����:"
				+ this.direction;
		tmp += "\n" + "�߳�:" + this.altitude + "(��λ:��)" + "\t���Ǹ���:"
				+ this.satellites;
		tmp += "\n" + "ʱ��:" + this.time + "\t�ն�ID:" + this.getDeviceSN();
		tmp += "\n" + "״̬:" + this.status;
		tmp += "\n" + "��������:" + this.getAlarmDesc();
		System.out.println(tmp);
	}

	public String makePostXml() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("<LOCTIONINFO>");
		sbf.append("<SIMCARD>" + this.getPhnum() + "</SIMCARD>");
		if (checkDate(this.getTime())) {
			sbf.append("<TIME>" + formatDate(this.getTime()) + "</TIME>");
		} else {
			sbf.append("<TIME>" + this.getTime() + "</TIME>");
		}
		sbf.append("<X>" + this.getCoordX() + "</X>");
		sbf.append("<Y>" + this.getCoordY() + "</Y>");
		sbf.append("<S>" + this.getSpeed() + "</S>");
		sbf.append("<V>" + this.getDirection() + "</V>");
		sbf.append("<LC>" + this.getLC() + "</LC>");
		sbf.append("<PICURL>" + this.getPicURL() + "</PICURL>");
		sbf.append("<ALARMDESC>" + this.getAlarmDesc() + "</ALARMDESC>");
		sbf.append("<H>" + this.getAltitude() + "</H>");
		sbf.append("<C>" + this.getSatellites() + "</C>");
		sbf.append("<F>" + this.getStatus() + "</F>");
		sbf.append("</LOCTIONINFO >");
		return sbf.toString();
	}

	// ת����JMS�ĸ�ʽ
	public String makeJMSInfo() {
		// SIM������,����,γ��,�ٶ�,����,ʱ��,����,λ����������,λ������
		// String locateType = null;
		// // com.mapabc.geom.CoordCvtAPI coordAPI = new CoordCvtAPI();
		// // String lngX = String.valueOf(this.getCoordX());
		// // String latY = String.valueOf(this.getCoordY());
		String posDes = "";// coordAPI.getAddress(lngX, latY);
		if (posDes == null)
			posDes = "";
		// double xs[] = { Double.parseDouble(this.getCoordX()) };
		// double ys[] = { Double.parseDouble(this.getCoordY()) };
		// com.mapabc.geom.DPoint[] dps;

		String ret = "";
		ret = "LOC," + this.getDeviceSN() + "," + this.getCoordX() + ","
				+ this.getCoordY() + "," + this.getSpeed();
		ret += "," + this.getDirection() + "," + this.getTime() + ",0,"
				+ posDes.getBytes().length + "," + posDes + "\r\n";

		return ret;
	}

	public void sentPost(boolean flag) {
		this.setIsPost(flag);
	}

	/**
	 * ����GPS�ϱ�ʱ��Ϊ����ʱ�䣬������8��Сʱ��ʱ�� �˷�����+8��Сʱ������23�Ĳ��ϸ�ʱ���ʽ�ַ���ת��Ϊ�ϸ�ʱ���ʽ�ַ���
	 * 
	 * @param dateStr
	 *            String
	 * @return String
	 */
	public static String formatDate(String dateStr) {
		int hour = Integer.parseInt(dateStr.substring(11, 13));
		int i = 0;
		if (hour > 23) {
			i = hour - 24;
			dateStr = dateStr.substring(0, 11) + i + dateStr.substring(13);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		Date d = null;
		try {
			d = sdf.parse(dateStr);
			c.setTime(d);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		if (d == null) {
			return null;
		}
		c.add(Calendar.DAY_OF_MONTH, 1);
		return sdf.format(c.getTime());
	}

	/**
	 * ����GPS�ϱ�ʱ��Ϊ����ʱ�䣬������8��Сʱ��ʱ�� �˷�������+8��Сʱ���Ƿ�Ϊ����23�Ĳ��ϸ�ʱ���ʽ�ַ���
	 * 
	 * @param dateStr
	 *            String
	 * @return boolean
	 */
	public static boolean checkDate(String dateStr) {
		if (dateStr == null || dateStr.trim().length() == 0) {
			return false;
		}
		Pattern pattern = Pattern
				.compile("^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01]) (2[4-9]|3[01]):[0-5]\\d{1}:([0-5]\\d{1})$");
		Matcher matcher = pattern.matcher(dateStr);
		return matcher.matches();
	}

	/**
	 * ���ƶ���
	 * 
	 * @return Object
	 */
	public Object clone() {
		ParseCELLID parse = new ParseCELLID();
		parse.setAdress(this.getAdress());
		parse.setAlarmDesc(this.getAlarmDesc());
		parse.setAltitude(this.getAltitude());
		parse.setCoordX(this.getCoordX());
		parse.setCoordY(this.getCoordY());
		parse.setDeviceSN(this.getDeviceSN());
		parse.setDirection(this.getDirection());
		parse.setEncodeX(this.getEncodeX());
		parse.setEncodeY(this.getEncodeY());
		parse.setExtend1(this.getExtend1());
		parse.setExtend2(this.getExtend2());
		parse.setExtend3(this.getExtend3());
		parse.setGpstag(this.getGpstag());
		parse.setIsReply(this.getIsReply());
		parse.setLC(this.getLC());
		parse.setPhnum(this.getPhnum());
		parse.setPicURL(this.getPicURL());
		parse.setReply(this.getReply());
		parse.setSatellites(this.getSatellites());
		parse.setSpeed(this.getSpeed());
		parse.setStatus(this.getStatus());
		parse.setTime(this.getTime());
		parse.setTimeStamp(this.getTimeStamp());
		parse.setIsvalid(this.isIsvalid());
		parse.setIsPost(this.isIsPost());
		parse.setLocateType(this.locateType);
		parse.setTemperature(temperature);
        parse.setHumidity(humidity);
		parse.setAccStatus(accStatus);
		parse.setAlarmType(alarmType);
		parse.setLocateStatus(locateStatus);
		parse.setExtend1(this.getExtend1());
		return parse;
	}

	public byte[] getReplyByte() {
		return replyByte;
	}

	public void setReplyByte(byte[] replyByte) {
		this.replyByte = replyByte;
	}

	public byte[] getReplyByte1() {
		return replyByte1;
	}

	public void setReplyByte1(byte[] replyByte1) {
		this.replyByte1 = replyByte1;
	}

	public byte[] getReplyByte2() {
		return replyByte2;
	}

	public void setReplyByte2(byte[] replyByte2) {
		this.replyByte2 = replyByte2;
	}

	public static void main(String[] args) {
		String s = "";
		System.out.println(s.length() + "," + s.getBytes().length);
	}

	/**
	 * @return the isAlarm
	 */
	public boolean isAlarm() {
		return this.isAlarm;
	}

	/**
	 * @param isAlarm
	 *            the isAlarm to set
	 */
	public void setAlarm(boolean isAlarm) {
		this.isAlarm = isAlarm;
	}

	/**
	 * @return the temperature
	 */
	public String getTemperature() {
		return this.temperature;
	}

	/**
	 * @param temperature
	 *            the temperature to set
	 */
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the accStatus
	 */
	public String getAccStatus() {
		return this.accStatus;
	}

	/**
	 * @param accStatus
	 *            the accStatus to set
	 */
	public void setAccStatus(String accStatus) {
		this.accStatus = accStatus;
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return this.socket;
	}

	/**
	 * @param socket
	 *            the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * @return the locateStatus
	 */
	public String getLocateStatus() {
		return this.locateStatus;
	}

	/**
	 * @param locateStatus
	 *            the locateStatus to set
	 */
	public void setLocateStatus(String locateStatus) {
		this.locateStatus = locateStatus;
	}

	/**
	 * @return the parseList
	 */
	public ArrayList<ParseBase> getParseList() {
		return this.parseList;
	}

	/**
	 * @param parseList
	 *            the parseList to set
	 */
	public void setParseList(ArrayList<ParseBase> parseList) {
		this.parseList = parseList;
	}
}
