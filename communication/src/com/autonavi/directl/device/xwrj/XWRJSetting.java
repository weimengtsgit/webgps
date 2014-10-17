/**
 * 
 */
package com.autonavi.directl.device.xwrj;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.autonavi.directl.CRC16;
import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.idirectl.TerminalParam;
import com.autonavi.directl.idirectl.TerminalSettingAdaptor;

/**
 * @author asia-auto
 * 
 */
public class XWRJSetting extends TerminalSettingAdaptor {

	private String head = "7e00";

	private String simcard = "";

	private String head1 = "";

	public XWRJSetting() {
	}

	/**
	 * 
	 */
	public XWRJSetting(TerminalParam param) {
		this.terminalParam = param;
		simcard = param.getSimCard() + "0";
		head1 = "2626" + simcard + "20";
	}

	/**
	 * TerminalSetting.java: ���ͼ��ʱ������
	 * 
	 * @param intervalType
	 *            String �������
	 * @param intervalTime
	 *            String ���ʱ��
	 */
	public String setSendIntervalTime(String seq, String intervalType, String intervalTime) {
		String ret = "";
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);

		String baowen = cmdsn + "0201" + "0000"
				+ Tools.convertToHex(intervalTime, 4) + "000001";
		String compress = Tools.compressHexData(baowen);
		String cont = "2626" + simcard + "20" + compress;
		ret = head
				+ Tools
						.convertToHex(String.valueOf((cont.length() + 4) / 2),
								8) + cont
				+ CRC16.CRC16(Tools.fromHexString(cont));
		//this.setByteArrayCmd(Tools.fromHexString(ret));
 
//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
		return ret;

	}

	/**
	 * ���ö�ʱ��λ�ļ��ʱ��ʹ�����
	 * 
	 * @param intervalTime
	 *            String --- ���ʱ��
	 * @param degree
	 *            String --- ����
	 * @return String
	 */
	public String setLocatorIntervalTimeAndDegree(String seq,String intervalTime,
			String degree) {
		String ret = "";
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		
		String paramCmd = cmdsn+"0203" + Tools.convertToHex(degree, 4)
				+ Tools.convertToHex(intervalTime, 4) + "000001";
		String cnt = head1 + Tools.compressHexData(paramCmd);
		String cmd = cnt + CRC16.CRC16(Tools.fromHexString(cnt));
		ret = head + Tools.convertToHex(String.valueOf(cmd.length() / 2), 8)
				+ cmd;
//		this.setByteArrayCmd(Tools.fromHexString(ret));
//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
		return ret;
	}

	/**
	 * ͨ�����ƹ���
	 */
	public String setCallLimitMode(String type1, String type2, String num,
			String tel) {
		String ret = "";
		String c1 = "03";// ����������
		String c = "";
		String[] inPhone = num.split(";");
		int inphoneCounts = inPhone.length;

		String[] outPhone = tel.split(";");
		int outPhoneCount = outPhone.length;

		String inphones = "";
		for (int i = 0; i < inphoneCounts; i++) {
			inphones += this.getFormatString(inPhone[i], 15);
		}
		String outphones = "";
		for (int i = 0; i < outPhoneCount; i++) {
			inphones += this.getFormatString(outPhone[i], 15);
		}
		if (type1.equals("0") && type2.equals("0")) {
			c = "00";
		}
		if (type1.equals("0") && type2.equals("1")) {
			c = "01";
		}
		if (type1.equals("1") && type2.equals("0")) {
			c = "02";
		}
		if (type1.equals("1") && type2.equals("1")) {
			c = "03";
		}

		String baowen = "000000000102" + c + c1
				+ Tools.int2Hexstring(inphoneCounts, 2)
				+ Tools.bytesToHexString(inphones.getBytes())
				+ Tools.int2Hexstring(outPhoneCount, 2)
				+ Tools.bytesToHexString(outphones.getBytes());

		String cmd = head1 + Tools.compressHexData(baowen);
		////Log.getInstance().outXWRJ////Loger(" ͨ�����Ʊ���ָ��:" + cmd);
		String cmd1 = cmd + CRC16.CRC16(Tools.fromHexString(cmd));
		ret = head + Tools.convertToHex(String.valueOf(cmd1.length() / 2), 8)
				+ cmd1;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		////Log.getInstance().outXWRJ////Loger(" ͨ������ָ��:" + ret);
		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return ret;

	}

	// ����IP,PORT
	public String setIPPort(String serverIP, String serverPort, String localPort) {
		String ret = "";
		String cmdtype = "";
		if (localPort.equals("1")) {
			cmdtype = "0b";
		} else if (localPort.equals("2")) {
			cmdtype = "0c";
		} else {
			cmdtype = "1e";
		}
		String[] ips = serverIP.trim().split("\\.");
		String ip = "";
		for (int i = 0; i < ips.length; i++) {
			if (i > 3) {
				break;
			}
			ip += getIPstring(ips[i].trim(), 3);
		}
		String port = "";
		port = getIPstring(serverPort, 5);
		String baowen = "0000000001" + cmdtype
				+ Tools.bytesToHexString((ip + port.trim()).getBytes());
		String pre = Tools.compressHexData(baowen);
		String cmd = head1 + pre;
		String cmd1 = cmd + CRC16.CRC16(Tools.fromHexString(cmd));
		ret = head + Tools.convertToHex(String.valueOf(cmd1.length() / 2), 8)
				+ cmd1;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		////Log.getInstance().outXWRJ////Loger(" ����IP,PORTָ��:" + ret);
		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return ret;
	}

	private String getFormatString(String phone, int len) {
		String ret = phone;
		while (ret.length() < len) {
			ret = ret + "\0";
		}
		return ret;
	}

	private String getIPstring(String S, int n) {
		String ret = S;
		while (ret.length() < n) {
			ret = "0" + ret;
		}
		return ret;
	}

	/**
	 * ���ó�ʱʱ����
	 * 
	 * @param type
	 * @param t
	 * @return
	 */
	public String setExtendTime(String type, String t) {
		String ret = "";
		String h = "0000000001";
		String tx = Tools.convertToHex(t, 4);
		String cid = "";
		if (type.equals("1")) {
			cid = "21";
		} else if (type.equals("2")) {
			cid = "22";
		} else if (type.equals("3")) {
			cid = "23";
		}
		String baow = h + cid + tx;
		String preb = Tools.compressHexData(baow);
		String cmd = head1 + preb;
		String cmd1 = cmd + CRC16.CRC16(Tools.fromHexString(cmd));
		ret = head + Tools.convertToHex(String.valueOf(cmd1.length() / 2), 8)
				+ cmd1;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		////Log.getInstance().outXWRJ////Loger(" ���ó�ʱָ��:" + ret);
		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return ret;
	}

	// �����¶Ȼش����
	public String setTemptertureInterval(String time) {

		String ret = "";
		String t = Tools.convertToHex(time.trim(), 4);

		String paramCmd = "00000000013a" + t;
		String cnt = head1 + Tools.compressHexData(paramCmd);
		String cmd = cnt + CRC16.CRC16(Tools.fromHexString(cnt));
		ret = head + Tools.convertToHex(String.valueOf(cmd.length() / 2), 8)
				+ cmd;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		////Log.getInstance().outXWRJ////Loger(" �����¶Ȼش����ָ��:" + ret);
		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return ret;
	}

	// ����APN
	public String setAPN(String v) {
		String ret = "";
		byte[] apn = getFormatString(v.trim(), 20).getBytes();
		String apnhex = Tools.bytesToHexString(apn);

		String paramCmd = "000000000109" + apnhex;
		String cnt = head1 + Tools.compressHexData(paramCmd);
		String cmd = cnt + CRC16.CRC16(Tools.fromHexString(cnt));
		ret = head + Tools.convertToHex(String.valueOf(cmd.length() / 2), 8)
				+ cmd;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		////Log.getInstance().outXWRJ////Loger(" ����APNָ��:" + ret);
		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * �����û�����
	 * 
	 * @param UserKey���û�����
	 * @param pwd���û�����
	 */
	public String setPassWord(String UserKey, String pwd) {
		String ret = "";
		byte[] apn = (getFormatString(UserKey, 16) + getFormatString(pwd, 16))
				.getBytes();
		String apnhex = Tools.bytesToHexString(apn);

		String paramCmd = "00000000010a" + apnhex;
		String cnt = head1 + Tools.compressHexData(paramCmd);
		String cmd = cnt + CRC16.CRC16(Tools.fromHexString(cnt));
		ret = head + Tools.convertToHex(String.valueOf(cmd.length() / 2), 8)
				+ cmd;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return ret;
	}

	public String setOther(String v1, String v2) {
		String ret = "";
		String h = "0000000001";
		String para = "";
		if (v2.equals("0")) {
			para = "00";
		} else {
			para = "01";
		}

		String cid = "";
		if (v1.equals("1")) {
			cid = "1f";// ���ô���ʹ��
		} else if (v1.equals("2")) {
			cid = "24";// ����ä������
		} else if (v1.equals("3")) {
			cid = "29";// ǩ���㱨
		}
		String baow = h + cid + para;
		String preb = Tools.compressHexData(baow);
		String cmd = head1 + preb;
		String cmd1 = cmd + CRC16.CRC16(Tools.fromHexString(cmd));
		ret = head + Tools.convertToHex(String.valueOf(cmd1.length() / 2), 8)
				+ cmd1;
		this.setByteArrayCmd(Tools.fromHexString(ret));

		////Log.getInstance().outXWRJ////Loger(cid + "����ָ��:" + ret);
		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * ��������
	 * 
	 * @param type:1,���ó�̨��������;2,���ó�̨��������;3,���ó�̨ͨ������;4���ó�̨��������
	 */
	public String changeTerminalPwd(String type, String newPwd) {
		String ret = "";
		String h = "0000000004";
		String para = "";
		if (type.equals("1")) {
			para = "0a";
		} else if (type.equals("2")) {
			para = "0b";
		} else if (type.equals("3")) {
			para = "0c";
		}  else if (type.equals("4")) {
			para = "0d";
		}  
		String pwd = Tools.bytesToHexString(getFormatString(newPwd,8).getBytes());
		String baow = h  + para + pwd;
		String preb = Tools.compressHexData(baow);
		String cmd = head1 + preb;
		String cmd1 = cmd + CRC16.CRC16(Tools.fromHexString(cmd));
		ret = head + Tools.convertToHex(String.valueOf(cmd1.length() / 2), 8)
				+ cmd1;
		this.setByteArrayCmd(Tools.fromHexString(ret));

		////Log.getInstance().outXWRJ////Loger("���ó�������ָ��:" + ret);
		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return ret;
	}

	public String setOilDoorValue(String type, String value, String time) {
		String ret = "";
		String h = "0000000001";
		String tx = Tools.convertToHex(value, 4);
		String tt = Tools.convertToHex(time, 4);
		String cid = "3e";
		 
		String baow = h + cid + tx+tt;
		String preb = Tools.compressHexData(baow);
		String cmd = head1 + preb;
		String cmd1 = cmd + CRC16.CRC16(Tools.fromHexString(cmd));
		ret = head + Tools.convertToHex(String.valueOf(cmd1.length() / 2), 8)
				+ cmd1;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		////Log.getInstance().outXWRJ////Loger(" �����ͷ�ֵָ��:" + ret);
		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return ret;
	}
	public static void main(String[] ages) {
		XWRJSetting X = new XWRJSetting();
		X.setAPN("cmwap");
	}

}
