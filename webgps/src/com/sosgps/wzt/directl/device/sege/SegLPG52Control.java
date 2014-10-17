package com.sosgps.wzt.directl.device.sege;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.*;

import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.TerminalControlAdaptor;
import com.sosgps.wzt.directl.idirectl.TerminalParam;

public class SegLPG52Control extends TerminalControlAdaptor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(SegLPG52Control.class);

	public SegLPG52Control(TerminalParam tparam) {
		this.terminalParam = tparam;
	}
	String centerSn = "01";//中心号
	String zuoSn = "001"; //坐席号
	public SegLPG52Control() {

	}

	/**
	 * 油电控制
	 * 
	 * @param state
	 * @return
	 */
	public String setOilState(String seq,String state) {
		
		String ret = "";
		String cmdsn = centerSn+zuoSn;//报文序列号
        String inSeq = seq;
		 while(inSeq.length()<5){
			inSeq = "0"+inSeq;
		 }
		 cmdsn = cmdsn + inSeq;
    	
		if (state != null && state.equals("0")) {
			int length = 23;
			ByteBuffer buf = ByteBuffer.allocate(length);
			buf.put((byte) 0x5B);
			buf.put((byte) 0x20);
			buf.put(cmdsn.getBytes());
			buf.put((byte) 0x09);
			buf.put("(CTR,555)".getBytes());
			buf.put((byte) 0x5D);
			ret = new String(buf.array());//Tools.bytesToHexString(buf.array()).toUpperCase();
			 
			return ret;
		} else {
			if (state != null && state.equals("1")) {
				int length = 23;
				ByteBuffer buf = ByteBuffer.allocate(length);
				buf.put((byte) 0x5B);
				buf.put((byte) 0x21);
				buf.put(cmdsn.getBytes());
				buf.put((byte) 9);
				buf.put("(CTR,666)".getBytes());
				buf.put((byte) 0x5D);
				ret = new String(buf.array());//Tools.bytesToHexString(buf.array()).toUpperCase() ;
				 
				return ret;
			}
		}
		return null;
	}

	/**
	 * 锁定车门 state:0代表锁定车门,1代表打开锁定车门
	 */
	public String setDoorState(String seq, String state) {
		String cmdsn = centerSn+zuoSn;//报文序列号
        String inSeq = seq;
		 while(inSeq.length()<5){
			inSeq = "0"+inSeq;
		 }
		 cmdsn = cmdsn + inSeq;
		
		String ret = "";
		if (state != null && state.equals("0")) {
			int length = 23;
			ByteBuffer buf = ByteBuffer.allocate(length);
			buf.put((byte) 0x5B);
			buf.put((byte) 0x3B);
			buf.put(cmdsn.getBytes());
			buf.put((byte) 9);
			buf.put("(CTR,333)".getBytes());
			buf.put((byte) 0x5D);

			ret = Tools.bytesToHexString(buf.array()) ;

			return ret;
		} else {
			if (state != null && state.equals("1")) {
				int length = 23;
				ByteBuffer buf = ByteBuffer.allocate(length);
				buf.put((byte) 0x5B);
				buf.put((byte) 0x3C);
				buf.put(cmdsn.getBytes());
				buf.put((byte) 9);
				buf.put("(CTR,444)".getBytes());
				buf.put((byte) 0x5D);
				ret = Tools.bytesToHexString(buf.array());

				return ret;
			}
		}
		return null;
	}

    /**
     * 中心拍照
     * @param number String：像头编号
     * @param count String：拍照数：FF-循环拍，00-停止
     * @param interval String：连续拍照时间间隔,单位5 秒
     * @param isUpload String：1为拍照后上传，0拍照后不上传
     * @return String
     */
    public String setTakePhoto(String seq,String number, String count, String interval,
                               String isUpload) {
    	String cmdsn = centerSn+zuoSn;//报文序列号
        String inSeq = seq;
		 while(inSeq.length()<5){
			inSeq = "0"+inSeq;
		 }
		 cmdsn = cmdsn + inSeq;
		
        String cmd = "";

        String content = "(PIC,01," + this.convertToHex(number, 1) + "," +
                         this.convertToHex(count, 2) + ",2000,J," +
                         this.convertToHex(interval, 4) + "," + isUpload + ")";
        int size = 14 + content.length();
        ByteBuffer buf = ByteBuffer.allocate(size);
        buf.put((byte) 0x5B);
        buf.put((byte) 0xDC);
        buf.put(cmdsn.getBytes());
        buf.put((byte) content.length());
        buf.put(content.getBytes());
        buf.put((byte) 0x5D);
       
            cmd = Tools.bytesToHexString(buf.array());
         
        return cmd;
    }
    /**
     * 以特定参数拼拍照指令
     * @param seq
     * @return
     */
    public String camera(String seq){
    	String ret = null;
    	ret = this.setTakePhoto(seq, "1", "1", "10", "1");
    	return ret;
    }
    
    private String convertToHex(String num, int n) {
        String temp = "";
        int i = Integer.parseInt(num);
        String hex = Integer.toHexString(i).toUpperCase();
        if (hex.length() > n) {
            int off = 0;
            while (off < n) {
                temp = temp + "F";
                off++;
            }
            return temp;
        } else if (hex.length() < n) {
            while (hex.length() < n) {
                hex = "0" + hex;
            }
            temp = hex;
        } else {
            temp = hex;
        }
        return temp;
    }

	public String SendMessage(String seq, String msg) {
		String cmdsn = centerSn+zuoSn;//报文序列号
        String inSeq = seq;
		 while(inSeq.length()<5){
			inSeq = "0"+inSeq;
		 }
		 cmdsn = cmdsn + inSeq;
		
		String ret = null;
		String mesg = "";// .replaceAll("\\s","");//Tools.BQchange(msg);
							// //msg.replaceAll("\\s",""); //convertUTF16(msg);
							// //ChinaConvert(msg);
		int size = 0;
		try {
			// 转换成全角
//			char[] a = msg.replaceAll("\\s", "").toCharArray();
//			for (int i = 0; i < a.length; i++) {
//				if ((int) a[i] > 128) {
//					mesg = mesg + String.valueOf(a[i]);
//				} else {
//					mesg += Tools.BQchange(String.valueOf(a[i]));
//				}
//			}
			//901A77E5FF0C660E59290037      70B951C665F65230516C53F85F004F1A
			 
			byte[] bmsg = msg.getBytes("UTF-16");
 
			String retmsg = Tools.bytesToHexString(bmsg);
			String  submsg = retmsg.substring(4);
			
			byte[] subb = Tools.fromHexString(submsg); 
			
			ByteBuffer buf = ByteBuffer.allocate(subb.length+14);
			buf.put((byte) 0x5B);
			buf.put((byte) 0xE0);
			buf.put(cmdsn.getBytes());
			buf.put((byte) subb.length);
			buf.put(subb);
			buf.put((byte) 0x5D);
			 
			ret = new String(buf.array());//Tools.bytesToHexString(buf.array()).toUpperCase();
			 
		} catch (UnsupportedEncodingException ex) {
			  //Log.getInstance().errorLog("下行信息异常：" + ex.getMessage(), ex);
		}
		return ret;
	}

	// 把byte数组转换成16进制字符
	public static String bytesToHexString(byte[] bs) {
		String s = "";
		for (int i = 0; i < bs.length; i++) {
			String tmp = Integer.toHexString(bs[i] & 0xff);
			if (tmp.length() < 2) {
				tmp = "0" + tmp;
			}
			s = s + tmp;
		}
		return s;
	}

	private static String ChinaConvert(String chs) {
		String qw = "";
		for (int i = 0; i < chs.length(); i++) {
			String c = chs.substring(i, i + 1);
			byte[] b = c.getBytes();
			if (b.length == 1) {
				char c1 = 127;
				qw = qw + c1 + c;
			} else {
				if (b.length == 3) {
					byte b1 = b[0];
					byte b2 = b[1];
					byte b3 = b[3];
					byte b11 = (byte) (b1 + (byte) (96 + 27));
					byte b22 = (byte) (b2 + (byte) (96 + 27));
					byte b33 = (byte) (b3 + (byte) (96 + 27));
					char c11 = (char) b11;
					char c22 = (char) b22;
					char c33 = (char) b33;
					qw = qw + c11 + c22 + c33;
				}
			}
		}
		return qw;
	}

	public String AnserLogin(String mobile, String index) {
		String ret = "";
		int length = 15 + mobile.length();
		ByteBuffer buf = ByteBuffer.allocate(length);
		buf.put((byte) 0x5B);
		buf.put((byte) 0x0);
		buf.put(index.getBytes());
		buf.put((byte) (mobile.length() + 1));
		buf.put((byte) 0x33);
		buf.put(mobile.getBytes());
		buf.put((byte) 0x5D);
		try {
			ret = new String(buf.array(), "ISO8859-1");
		} catch (UnsupportedEncodingException ex) {
		}
		return ret;
	}

	public String AnserLogout(String mobile, String index) {
		String ret = "";
		int length = 14 + mobile.length();
		ByteBuffer buf = ByteBuffer.allocate(length);
		buf.put((byte) 0x5B);
		buf.put((byte) 0x81);
		buf.put(index.getBytes());
		buf.put((byte) mobile.length());
		buf.put(mobile.getBytes());
		buf.put((byte) 0x5D);
		try {
			ret = new String(buf.array(), "ISO8859-1");
		} catch (UnsupportedEncodingException ex) {
		}
		return ret;
	}

	public String AnserSelfCheck(String mobile, String index) {
		String ret = "";
		int length = 15 + mobile.length();
		ByteBuffer buf = ByteBuffer.allocate(length);
		buf.put((byte) 0x5B);
		buf.put((byte) 0xFF);
		buf.put(index.getBytes());
		buf.put((byte) (mobile.length() + 1));
		buf.put((byte) 0x33);
		buf.put(mobile.getBytes());
		buf.put((byte) 0x5D);
		try {
			ret = new String(buf.array(), "ISO8859-1");
		} catch (UnsupportedEncodingException ex) {
		}
		return ret;
	}

	// 报警应答
	public String replyAlarm(String mobile, String index) {
		String ret = "";
		int length = 14 + mobile.length();
		ByteBuffer buf = ByteBuffer.allocate(length);
		buf.put((byte) 0x5B);
		buf.put((byte) 0x70);
		buf.put(index.getBytes());
		buf.put((byte) (mobile.length()));
		buf.put(mobile.getBytes());
		buf.put((byte) 0x5D);
	 
			ret = new String(buf.array());
			logger.info("车台报警回应信息:" + ret);
		return ret;
	}

 
	
	public static void main(String[] args){
		SegLPG52Control CON = new SegLPG52Control();
		CON.SendMessage("1", "你好");
		System.out.println("5b70303130303130303030310b31333835353936373437345d".toUpperCase());
	}
}
