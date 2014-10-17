/**
 * 
 */
package com.autonavi.directl.device.xwrj;

import java.io.UnsupportedEncodingException;

import com.autonavi.directl.CRC16;
import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.idirectl.LocatorAdaptor;
import com.autonavi.directl.idirectl.TerminalParam;

/**
 * @author asia-auto ������ݶ�λ������
 */
public class XWRJLocator extends LocatorAdaptor {

	private String head = "7e00";

	private String simcard = "";

	private String head1 = "";

	/**
	 * 
	 */
	public XWRJLocator(TerminalParam param) {
		this.terminalParam = param;
		simcard = param.getSimCard();
		head1 = "2626" + simcard + "020";
	}

	public String singleLocator(String seq) {
		String ret = "";
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		
		String paramCmd = cmdsn + "020300010000000001";
		String cnt = head1 + Tools.compressHexData(paramCmd);
		String cmd = cnt + CRC16.CRC16(Tools.fromHexString(cnt));
		ret = head + Tools.convertToHex(String.valueOf(cmd.length() / 2), 8)
				+ cmd;
//		this.setByteArrayCmd(Tools.fromHexString(ret));
//		//Log.getInstance().outXWRJ//Loger("���ζ�λ��" + ret);
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
	 * ���ð����붨λ
	 * 
	 * @param distance
	 * @return
	 */
	public String setLocatorByDistance(String seq,String distance) {
		String ret = "";
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		
		String cmd = cmdsn+"020200" + Tools.convertToHex(distance, 4)
				+ "000001";
		String cmd1 = Tools.compressHexData(cmd);
		String cmd2 = head1 + cmd1;
		String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
		ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8) + cmd3;
//		this.setByteArrayCmd(Tools.fromHexString(ret));
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
	 * ���μ��
	 */
	public String multiLocator(String seq,String t, String count) {
		String ret = "";
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		
		String paramCmd = cmdsn+"0203" + Tools.convertToHex(count, 4)
				+ Tools.convertToHex(t, 4) + "000001";
		String cnt = Tools.compressHexData(head1 + paramCmd);
		String cmd = cnt + CRC16.CRC16(Tools.fromHexString(cnt));
		ret = head + Tools.convertToHex(String.valueOf(cmd.length() / 2), 8)
				+ cmd;
//		this.setByteArrayCmd(Tools.fromHexString(ret));
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
	 * 
	 * @param state
	 *            String:�ر�
	 * @return String
	 * @todo Implement this com.autonavi.directl.idirectl.Locator method
	 */
	public String setMultiLocatorState(String seq, String state) {
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		String ret = "";
		String paramcmd = head1 + Tools.compressHexData(cmdsn+"020400");
		String bcmd = paramcmd + CRC16.CRC16(Tools.fromHexString(paramcmd));
		String cmd = head
				+ Tools.convertToHex(String.valueOf(bcmd.length() / 2), 8)
				+ bcmd;
		ret =cmd;
//		this.setByteArrayCmd(Tools.fromHexString(cmd));
//		//Log.getInstance().outXWRJ//Loger("�ر�������λ��" + cmd);
//		String ret = "";
//		try {
//			ret = new String(Tools.fromHexString(cmd), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
		return ret;
	}

	/**
	 * ���ÿ��س��ϴ����
	 */
	public String setCarInterval(String seq,String type, String emptime, String ztime,
			String tasktime) {
		String cmdtype = null;
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		
		if (type.equals("0")) {
			cmdtype = "00";
		} else {
			cmdtype = "01";
		}
		String interval = Tools.convertToHex(emptime, 4)
				+ Tools.convertToHex(ztime, 4)
				+ Tools.convertToHex(tasktime, 4);
		String baow = cmdsn+"0114" + cmdtype + interval;
		String cmd = Tools.compressHexData(baow);
		String cmd1 = head1 + cmd;
		String cmd2 = cmd1 + CRC16.CRC16(Tools.fromHexString(cmd1));
		String cmd3 = head
				+ Tools.convertToHex(String.valueOf(cmd2.length() / 2), 8)
				+ cmd2;
		//this.setByteArrayCmd(Tools.fromHexString(cmd3));
		//Log.getInstance().outXWRJ//Loger("���ÿ��س��ϴ������" + cmd3);
		String ret = cmd3;
//		try {
//			ret = new String(Tools.fromHexString(cmd3), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
		return ret;
	}

	/**
	 * ���ÿ��س��ϴ����
	 */
	public String setBlackInterval(String seq,String type, String emptime) {
		String cmdtype = null;
		String cmdsn = "";//�������к�
		cmdsn = Tools.convertToHex(seq, 8);
		
		if (type.equals("0")) {
			cmdtype = "01";
		} else if (type.equals("1")) {
			cmdtype = "10";
		} else if (type.equals("2")) {
			cmdtype = "00";
		}
		String interval = "";
		if (emptime != null && emptime.trim().length() > 0) {
			interval = Tools.convertToHex(emptime, 4);
		}
		String baow = cmdsn+"0105" + cmdtype + interval + "0000";
		String cmd = Tools.compressHexData(baow);
		String cmd1 = head1 + cmd;
		String cmd2 = cmd1 + CRC16.CRC16(Tools.fromHexString(cmd1));
		String cmd3 = head
				+ Tools.convertToHex(String.valueOf(cmd2.length() / 2), 8)
				+ cmd2;
		//this.setByteArrayCmd(Tools.fromHexString(cmd3));
		//Log.getInstance().outXWRJ//Loger("���ú�ϻ�Ӳɼ������" + cmd3);
		String ret = "";
		try {
			ret = new String(Tools.fromHexString(cmd3), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return ret;
	}
	  //��ϻ����������
	  public String setBlackQuest(){
		  String paramcmd = head1 + Tools.compressHexData("00000000020b");
			String bcmd = paramcmd + CRC16.CRC16(Tools.fromHexString(paramcmd));
			String cmd = head
					+ Tools.convertToHex(String.valueOf(bcmd.length() / 2), 8)
					+ bcmd;
			this.setByteArrayCmd(Tools.fromHexString(cmd));
			//Log.getInstance().outXWRJ//Loger("��ϻ�������ϴ�����" + cmd);
			String ret = "";
			try {
				ret = new String(Tools.fromHexString(cmd), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
			return ret;
		  
	  }
	  
	  

}