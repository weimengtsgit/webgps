/**
 * 
 */
package com.sosgps.wzt.directl.device.xwrj;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.sosgps.wzt.directl.CRC16;
import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.TerminalControlAdaptor;
import com.sosgps.wzt.directl.idirectl.TerminalParam;

/**
 * @author asia-auto
 * 
 */
public class XWRJControl extends TerminalControlAdaptor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(XWRJControl.class);

	private String head = "7e00";

	private String simcard = "";

	private String head1 = "";

	public XWRJControl() {

	}

	/**
	 * 
	 */
	public XWRJControl(TerminalParam param) {
		this.terminalParam = param;
		simcard = param.getSimCard();
		head1 = "2626" + simcard + "020";
	}

	public String SendMessage(String seq, String msg) {
		String ret = "";
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		
		byte[] bMsg;
		try {
			bMsg = msg.getBytes("UNICODE");
			// byte[] bmsg = new byte[bMsg.length - 2];
			// System.arraycopy(bMsg, 2, bmsg, 0, bmsg.length);
			String cmd1 = cmdsn + "0301" + Tools.bytesToHexString(bMsg);
			String precmd = Tools.compressHexData(cmd1);
			String bwcmd = head1 + precmd
					+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
			ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
			//this.setByteArrayCmd(Tools.fromHexString(ret));
			//Log.getInstance().outXWRJ//Loger("�·���ͨ��Ϣָ�" + ret);
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}

//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
		return ret;
	}

	/**
	 * �·�����Ϣ��
	 * 
	 * @param type
	 *            String --- ��Ϣ����
	 * @param msg
	 *            String --- ��Ϣ
	 * @return String
	 */
	public String sendMessageByType(String seq, String type, String msg) {
		String ret = "";
		String cmdType = "";
		String phone = "";
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		
		if (type.equals("1")) {
			cmdType = "01";
		} else if (type.equals("2")) {
			cmdType = "02";
		} else if (type.equals("3")) {
			cmdType = "03";
		} else {
			cmdType = "05";
			phone = this.getFormatString(type, 15);

		}
		byte[] bMsg;
		try {
			bMsg = msg.getBytes("UNICODE");

			String cmd1 = "";
			if (cmdType.equals("05")) {
				cmd1 = cmdsn+"03" + cmdType
						+ Tools.bytesToHexString(phone.getBytes())
						+ Tools.bytesToHexString(bMsg);
			} else {
				cmd1 = cmdsn+"03" + cmdType + Tools.bytesToHexString(bMsg);
			}
			String precmd = Tools.compressHexData(cmd1);
			String bwcmd = head1 + precmd
					+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
			ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
			//this.setByteArrayCmd(Tools.fromHexString(ret));
			//Log.getInstance().outXWRJ//Loger("���ͣ�" + cmdType + "�·�������Ϣָ�" + ret);
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}

//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
		return ret;
	}

	// ���ü�������
	public String setListening(String seq,String state, String tel) {
		String ret = "";
		String id = "";
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		
		if (state.equals("1")) {
			id = "0402";// ��������
		} else if (state.equals("2")) {
			id = "0101";// ���ñ�������
		} else if (state.equals("3")) {
			id = "0104";// ������������
		} else if (state.equals("4")) {
			id = "010d";// ����VCC����
		} else if (state.equals("5")) {
			id = "0115";// ���õ��Ȼ㱨���ĺ�
		} else if (state.equals("6")) {
			id = "0117";// �������ļ���¼���绰����
		} else if (state.equals("7")) {
			id = "0119";// ����ҽ�ƾȻ��绰����
		} else if (state.equals("8")) {
			id = "011a";// ����ά�޵绰����
		} else if (state.equals("9")) {
			id = "0128";// �����ƶ�������ĺ���,���ͨѶ��ʽ�Զ���Ϊ���ŷ�ʽ
		}
		String phone = this.getFormatString(tel, 15);
		String baowen = cmdsn + id
				+ Tools.bytesToHexString(phone.getBytes());
		String precmd = Tools.compressHexData(baowen);
		String bwcmd = head1 + precmd
				+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
		ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
//		this.setByteArrayCmd(Tools.fromHexString(ret));
//		//Log.getInstance().outXWRJ//Loger(id + "���ú���ָ�" + ret);
//
//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
		return ret;
	}

	// ����ȷ��
	public byte[] getAlarmAply() {
		String ret = "";

		String baowen = "00000000040401";
		String precmd = Tools.compressHexData(baowen);
		String bwcmd = head1 + precmd
				+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
		ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
		//Log.getInstance().outXWRJ//Loger("����ȷ�ϣ�" + ret);

		return Tools.fromHexString(ret);
	}

	private String getFormatString(String phone, int len) {
		String ret = phone;
		while (ret.length() < len) {
			ret = ret + "\0";
		}
		return ret;
	}

	/**
	 * �������� state:0����Ͽ�����,1���������
	 */
	public String setOilState(String seq,String state) {
		String ret = "";
		String type = "";
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		
		if (state.equals("0")) {
			type = "40";        //����·
		} else {
			type = "00";        //����·
		}
		String baowen = cmdsn+"040180" + type; //80:1000 0000
		String precmd = Tools.compressHexData(baowen);
		String bwcmd = head1 + precmd
				+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
		ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
//		this.setByteArrayCmd(Tools.fromHexString(ret));
//		//Log.getInstance().outXWRJ//Loger("���ö��͵�·ָ�" + ret);
//
//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
		return ret;
	}

	/**
	 * �������� state:0������������,1�������������
	 */
	public String setDoorState(String seq,String state) {
		String ret = "";
		String type = "";
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		
		if (state.equals("0")) {
			type = "80";
		} else {
			type = "00";
		}
		String baowen = cmdsn+"040100" + type;
		String precmd = Tools.compressHexData(baowen);
		String bwcmd = head1 + precmd
				+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
		ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
//		this.setByteArrayCmd(Tools.fromHexString(ret));
//		//Log.getInstance().outXWRJ//Loger("ң�����ָ�" + ret);
//
//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
		return ret;
	}

	/**
	 * ��������
	 * 
	 * @param state:��·���ƣ�state1:������
	 */
	public String setOilAndLock(String seq,String state, String state1) {
		int type = 0;
		int type1 = 0;
		String ret = "";
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		if (state.equals("0")) {
			type = (int) Math.pow(2, 6);
		}
		if (state1.equals("0")) {
			type1 = (int) Math.pow(2, 7);
		}
		int result = type + type1;
		String hex = Tools.int2Hexstring(result, 2);

		String baowen = cmdsn + "040100" + hex;
		String precmd = Tools.compressHexData(baowen);
		String bwcmd = head1 + precmd
				+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
		ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
//		this.setByteArrayCmd(Tools.fromHexString(ret));
//		//Log.getInstance().outXWRJ//Loger("��������ָ�" + ret);
//
//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
		return ret;
	}

	// �����ն�
	public String reset(String seq) {
		String ret = "";
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		
		String baowen = cmdsn+"0110";
		String precmd = Tools.compressHexData(baowen);
		String bwcmd = head1 + precmd
				+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
		ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
//		this.setByteArrayCmd(Tools.fromHexString(ret));
//		//Log.getInstance().outXWRJ//Loger("�����ն�ָ�" + ret);
//
//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
		return ret;
	}

	/**
	 * ������,��֡��· ,�������
	 * @param type: 0��� 1ֹͣ��� ����ֵ����
	 * @param superviseType: 0������� 1�������
	 * @param action: �ϴ��򱣴����ն�
	 * @param times:��ش���
	 * @param interval:���ʱ�� ��λ0.1��
	 * @param quality:ͼƬ����
	 * @param brightness:ͼƬ����
	 * @param contrast:�Աȶ�
	 * @param saturation:���Ͷ�
	 * @param color:ɫ��
	 */
	public String setCameraControl(String seq, String type, String action, String times, String interval,
			String quality, String brightness, String contrast, String saturation,
			String color) {
		String ret = "";
		String tp = null;
		String act = "";
		String params = "";
		String baowen = "";
		int interv = Integer.parseInt(interval);
		String timeval = interv * 10 + "";
		if (action.equals("S")){
			//�ϴ�
			act = "00000000";
		} else {
			//�������ն�
			act = "00000001";
		}
		if (type.equals("0")){
			tp = "00000001";//�������
			 params = "0101" +tp + act +  Tools.convertToHex(times, 4)
				+ Tools.convertToHex(timeval, 4)
				+ Tools.convertToHex(quality, 2)+ Tools.convertToHex(brightness, 2)
				+ Tools.convertToHex(contrast, 2) + Tools.convertToHex(saturation, 2)
				+ Tools.convertToHex(color, 2);
			 baowen = "000000000901"+params;
		}else if (type.equals("1")){
			//ֹͣ�������
			baowen = "000000000902010000000100000000";
		}else {
			//����ͷ����
			baowen = "000000000903";
		}
 		 
		String precmd = Tools.compressHexData(baowen);
		String bwcmd = head1 + precmd
				+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
		ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		logger.info("����ָ�" + ret);

 
		return ret;
	}
	
	/*
	 * ���ض��Ĳ���ƴ��һ������ָ��
	 * @see com.sosgps.wzt.directl.idirectl.TerminalControlAdaptor#camera()
	 */
	public String camera(String seq){
		String ret = "";
		
		ret = this.setCameraControl(seq,"0","S","1","60","3","128","64","64","128");
		
		return ret;
	}

	public static void main(String[] args) {
		XWRJControl x = new XWRJControl();
		String p = x.getFormatString("13522576910", 15);
		double t = Math.pow(2, 7);
		 
	}
}
