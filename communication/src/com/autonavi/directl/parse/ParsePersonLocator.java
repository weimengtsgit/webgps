package com.autonavi.directl.parse;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.dbutil.DbOperation;
import com.autonavi.directl.dbutil.service.DBService;
import com.autonavi.directl.dbutil.service.DBServiceImpl;
import com.autonavi.lbsgateway.GBLTerminalList;

public class ParsePersonLocator extends ParseBase {

	@Override
	public void parseGPRS(String hexString) {

		// #TH100,deviceId,TIME,V,N24.123456,E121.123456,80,16:30:30,31/12/04,1000*

		byte[] cont = Tools.fromHexString(hexString);
		String scont = new String(cont);
		Log.getInstance().outLog("��ͯ�ֻ�ԭʼ���ݣ�"+scont);
		String[] split = scont.split(",");
		String deviceId = split[1];
		
		String phnum = GBLTerminalList.getInstance().getSimcardNum(deviceId);
		if (phnum == null || hexString == null
				|| phnum.trim().length() == 0
				|| hexString.trim().length() == 0) {
			
			 Log.getInstance().outLog("ϵͳ��û�����䵽ָ�����նˣ�device_id="+deviceId);
			return;
		}
		this.setDeviceSN(deviceId);
		this.setPhnum(phnum);
		String type = split[2];
		String params = "";
		// ����������Ϣ����
		if (type.equalsIgnoreCase("REQUEST")) {
			this.parsePosition(split);

			String track = split[10];
			if (track.equalsIgnoreCase("S")) {
				params += "ͨ��������,";
			} else {
				params += "ͨ����TCP,";
			}

			String repMode = split[11];
			if (repMode.equalsIgnoreCase("TIME")) {
				params += " �ش�ģʽ����ʱ�ش�,";
			} else if (repMode.equalsIgnoreCase("MOVE")) {
				params += " �ش�ģʽ��������ش�,";
			} else {
				params += "�ش�ģʽ��δ����, ";
			}

			String tInterval = split[12];
			String dInterval = split[13];
			params += "ʱ��Ƶ�ʣ�" + tInterval + ",����Ƶ�ʣ�" + dInterval + ",";

			String power = split[14];
			params += "ȱ���ϱ���" + power + ",";
			String remainingPower = split[15];
			params += "ʣ�������" + remainingPower + ",";
			String smsCenter = split[16];
			params += "�������ĺ��룺" + smsCenter + ",";
			String ip = split[17];
			String port = split[18];
			params += "IP:" + ip + ",�˿ڣ�" + port + ",";
			String variablePhone = split[19] + "," + split[20] + ","
					+ split[21] + "," + split[22];
			params += "�޲����룺" + variablePhone;
		} else if (type.equalsIgnoreCase("REPLY")) {
			DBService dbservice = new DBServiceImpl();
			String status = split[3];
			String cmdId = split[4];

			if (status.equalsIgnoreCase("OK")) {

				dbservice.updateInstructionsState(this.getDeviceSN(), "0",
						cmdId);

			} else {
				dbservice.updateInstructionsState(this.getDeviceSN(), "2",
						cmdId);

			}
		} else {
			this.parsePosition(split);
		}
		this.sentPost(true);

	}

	public void parsePosition(String[] split) {

		String deviceId = split[1];
		this.setDeviceSN(deviceId);
		String type = split[2];
		String typeDesc = null;
		if (type.equalsIgnoreCase("EMERGENCY")) {
			typeDesc = "�����ش�";
		} else if (type.equalsIgnoreCase("TIME")) {
			typeDesc = "��ʱ��ش�";
		} else if (type.equalsIgnoreCase("MOVE")) {
			typeDesc = "������ش�";
		} else if (type.equalsIgnoreCase("POWER SHORTAGE")) {
			typeDesc = "��������ش�";
		} else if (type.equalsIgnoreCase("CALL")) {
			typeDesc = "�绰�ش�";
		} else if (type.equalsIgnoreCase("GPS")) {
			typeDesc = "GPS�ش�";
		}

		String status = split[3];
		String statusDes = null;
		if (status.equalsIgnoreCase("A")) {
			statusDes = "��λ�ɹ�";
		} else {
			statusDes = "��λʧ��";
		}
		String y = split[4].substring(1);
		this.setCoordY(y);
		String x = split[5].substring(1);
		this.setCoordX(x);
		String velocity = split[6];
		this.setSpeed(velocity);
		String time = split[7].replaceAll(":", "");
		String date = split[8].replaceAll("/", "");
		String datetime = this.conformtime(time, date);
		this.setTime(datetime);
		Date gpsdate = formatStrToDate(datetime, "yyyy-MM-dd HH:mm:ss");
		Timestamp ts = new Timestamp(gpsdate.getTime());
		this.setTimeStamp(ts);

		String areaFlag = split[9];
		String afstr = "";
		for (int i = 0; i < areaFlag.length(); i++) {
			char c = areaFlag.charAt(i);
			if (c == '1') {
				afstr += "����" + (i + 1) + "����";
			} else if (c == '0'){
				afstr += "��" + (i + 1) + "����";
			}
		}
		Log.getInstance().outLog("��ͯ�ֻ���λ��Ϣ��DEVICE_SN="+this.getDeviceSN()+",X="+x+",Y="+y+",�ٶ�="+velocity+"ʱ�䣺"+datetime);
	}

	private Date formatStrToDate(String date, String format) {
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			d = sdf.parse(date);
		} catch (ParseException ex) {
		}
		return d;
	}

	@Override
	public void parseSMS(String phnum, String content) {
		// TODO �Զ����ɷ������

	}
	
	private static String conformtime(String time, String date) {
		try {
			String hour = time.substring(0, 2);
			String min = time.substring(2, 4);
			String sec = time.substring(4, 6);
			String day = date.substring(0, 2);
			String month = date.substring(2, 4);
			String year = date.substring(4, 6);
			String result = "";
			result = "20" + year + "-" + month + "-" + day + " ";
			result += hour + ":" + min + ":" + sec;
			SimpleDateFormat simpleDate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date d = simpleDate.parse(result);
			Calendar car = Calendar.getInstance();
			car.setTime(d);
			 
			Date newDate = new Date(car.getTimeInMillis());
			return simpleDate.format(newDate);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return null;
	}
	
	public static void main(String[] args){
		ParsePersonLocator plp = new ParsePersonLocator();
							
		plp.parseGPRS("2354483130302c31333639383630373437342c4750532c562c3030302e3030303030302c303030302e3030303030302c302c30303a30303a30302c30302f30302f30302c585858582a");
	}

}
