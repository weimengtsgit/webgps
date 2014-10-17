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
		String cmdsn = "";//报文序列号
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
			//Log.getInstance().outXWRJ//Loger("下发普通消息指令：" + ret);
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}

//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
		return ret;
	}

	/**
	 * 下发短信息。
	 * 
	 * @param type
	 *            String --- 信息类型
	 * @param msg
	 *            String --- 信息
	 * @return String
	 */
	public String sendMessageByType(String seq, String type, String msg) {
		String ret = "";
		String cmdType = "";
		String phone = "";
		String cmdsn = "";//报文序列号
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
			//Log.getInstance().outXWRJ//Loger("类型：" + cmdType + "下发调度消息指令：" + ret);
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}

//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
		return ret;
	}

	// 设置监听号码
	public String setListening(String seq,String state, String tel) {
		String ret = "";
		String id = "";
		String cmdsn = "";//报文序列号
		cmdsn = Tools.convertToHex(seq, 8);
		
		if (state.equals("1")) {
			id = "0402";// 监听号码
		} else if (state.equals("2")) {
			id = "0101";// 设置报警号码
		} else if (state.equals("3")) {
			id = "0104";// 设置求助号码
		} else if (state.equals("4")) {
			id = "010d";// 设置VCC号码
		} else if (state.equals("5")) {
			id = "0115";// 设置调度汇报中心号
		} else if (state.equals("6")) {
			id = "0117";// 设置中心监听录音电话号码
		} else if (state.equals("7")) {
			id = "0119";// 设置医疗救护电话号码
		} else if (state.equals("8")) {
			id = "011a";// 设置维修电话号码
		} else if (state.equals("9")) {
			id = "0128";// 设置移动监控中心号码,会把通讯方式自动改为短信方式
		}
		String phone = this.getFormatString(tel, 15);
		String baowen = cmdsn + id
				+ Tools.bytesToHexString(phone.getBytes());
		String precmd = Tools.compressHexData(baowen);
		String bwcmd = head1 + precmd
				+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
		ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
//		this.setByteArrayCmd(Tools.fromHexString(ret));
//		//Log.getInstance().outXWRJ//Loger(id + "设置号码指令：" + ret);
//
//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
		return ret;
	}

	// 报警确认
	public byte[] getAlarmAply() {
		String ret = "";

		String baowen = "00000000040401";
		String precmd = Tools.compressHexData(baowen);
		String bwcmd = head1 + precmd
				+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
		ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
		//Log.getInstance().outXWRJ//Loger("报警确认：" + ret);

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
	 * 开关油门 state:0代表断开油门,1代表打开油门
	 */
	public String setOilState(String seq,String state) {
		String ret = "";
		String type = "";
		String cmdsn = "";//报文序列号
		cmdsn = Tools.convertToHex(seq, 8);
		
		if (state.equals("0")) {
			type = "40";        //断油路
		} else {
			type = "00";        //开油路
		}
		String baowen = cmdsn+"040180" + type; //80:1000 0000
		String precmd = Tools.compressHexData(baowen);
		String bwcmd = head1 + precmd
				+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
		ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
//		this.setByteArrayCmd(Tools.fromHexString(ret));
//		//Log.getInstance().outXWRJ//Loger("设置断油电路指令：" + ret);
//
//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
		return ret;
	}

	/**
	 * 锁定车门 state:0代表锁定车门,1代表打开锁定车门
	 */
	public String setDoorState(String seq,String state) {
		String ret = "";
		String type = "";
		String cmdsn = "";//报文序列号
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
//		//Log.getInstance().outXWRJ//Loger("遥控设防指令：" + ret);
//
//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
		return ret;
	}

	/**
	 * 油锁控制
	 * 
	 * @param state:油路控制，state1:锁控制
	 */
	public String setOilAndLock(String seq,String state, String state1) {
		int type = 0;
		int type1 = 0;
		String ret = "";
		String cmdsn = "";//报文序列号
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
//		//Log.getInstance().outXWRJ//Loger("油锁控制指令：" + ret);
//
//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
		return ret;
	}

	// 重启终端
	public String reset(String seq) {
		String ret = "";
		String cmdsn = "";//报文序列号
		cmdsn = Tools.convertToHex(seq, 8);
		
		String baowen = cmdsn+"0110";
		String precmd = Tools.compressHexData(baowen);
		String bwcmd = head1 + precmd
				+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
		ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
//		this.setByteArrayCmd(Tools.fromHexString(ret));
//		//Log.getInstance().outXWRJ//Loger("重启终端指令：" + ret);
//
//		try {
//			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
//		} catch (UnsupportedEncodingException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
		return ret;
	}

	/**
	 * 摄像监控,单帧单路 ,立即监控
	 * @param type: 0监控 1停止监控 其他值休眠
	 * @param superviseType: 0立即监控 1条件监控
	 * @param action: 上传或保存在终端
	 * @param times:监控次数
	 * @param interval:间隔时间 单位0.1秒
	 * @param quality:图片质量
	 * @param brightness:图片亮度
	 * @param contrast:对比度
	 * @param saturation:饱和度
	 * @param color:色度
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
			//上传
			act = "00000000";
		} else {
			//保存在终端
			act = "00000001";
		}
		if (type.equals("0")){
			tp = "00000001";//立即监控
			 params = "0101" +tp + act +  Tools.convertToHex(times, 4)
				+ Tools.convertToHex(timeval, 4)
				+ Tools.convertToHex(quality, 2)+ Tools.convertToHex(brightness, 2)
				+ Tools.convertToHex(contrast, 2) + Tools.convertToHex(saturation, 2)
				+ Tools.convertToHex(color, 2);
			 baowen = "000000000901"+params;
		}else if (type.equals("1")){
			//停止立即监控
			baowen = "000000000902010000000100000000";
		}else {
			//摄像头休眠
			baowen = "000000000903";
		}
 		 
		String precmd = Tools.compressHexData(baowen);
		String bwcmd = head1 + precmd
				+ CRC16.CRC16(Tools.fromHexString(head1 + precmd));
		ret = head + Tools.convertToHex(bwcmd.length() / 2 + "", 8) + bwcmd;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		logger.info("摄像指令：" + ret);

 
		return ret;
	}
	
	/*
	 * 以特定的参数拼成一个拍照指令
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
