package com.autonavi.directl.device.sege;

import java.io.*;
import java.nio.*;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.idirectl.LocatorAdaptor;
import com.autonavi.directl.idirectl.TerminalParam;

 

public class SegLPG52Locator extends LocatorAdaptor {
	String centerSn = "01";//中心号
	String zuoSn = "001"; //坐席号
	
    public SegLPG52Locator( TerminalParam tparam) {
        this.terminalParam = tparam;
    }

    public String singleLocator(String seq) {
    	String cmdsn = "";//报文序列号
		cmdsn = Tools.convertToHex(seq, 10);
		
        String ret = "";
        ByteBuffer buf = ByteBuffer.allocate(23);
        buf.put((byte) 0x5B);
        buf.put((byte) 0x10);
        buf.put(cmdsn.getBytes());
        buf.put((byte) 0x09);
        buf.put("(CTR,000)".getBytes());
        buf.put((byte) 0x5D);
        ret =Tools.bytesToHexString(buf.array());

        return ret;
    }

    //多次定位
    public String multiLocator(String seq,String t, String count) {
    	
        String ret = "";
        String time = "";
        
        String cnt = "";
        String cmdsn = centerSn+zuoSn;//报文序列号
        String inSeq = seq;
		 while(inSeq.length()<5){
			inSeq = "0"+inSeq;
		 }
		 cmdsn = cmdsn + inSeq;
		
        int tt = Integer.parseInt(t);
        int cou = Integer.parseInt(count);

        time = Tools.convertToHex(t.trim(), 2);
        if (cou < 0 || cou == 1){
             cnt ="FF";
        }else {
          cnt = Tools.convertToHex(count.trim(), 2);
        }
        String cmd = "(CMD,002," + cnt + "," + time + ")";
        ByteBuffer buf = ByteBuffer.allocate(29);
        buf.put((byte) 0x5B);
        buf.put((byte) 0x11);
        buf.put(cmdsn.getBytes());
        buf.put((byte) 0x0F);
        buf.put(cmd.getBytes());
        buf.put((byte) 0x5D);
        ret = new String(buf.array());//Tools.bytesToHexString(buf.array());
        Log.getInstance().outLog("下发频率设置指令："+ret);
        return ret;
    }

    //停止跟踪
    public String setMultiLocatorState(String seq, String state) {
    	String cmdsn = centerSn+zuoSn;//报文序列号
        String inSeq = seq;
		 while(inSeq.length()<5){
			inSeq = "0"+inSeq;
		 }
		 cmdsn = cmdsn + inSeq;
        String ret = "";
        String cmd = "(CMD,002,00,00)";
        ByteBuffer buf = ByteBuffer.allocate(29);
        buf.put((byte) 0x5B);
        buf.put((byte) 0x11);
        buf.put(cmdsn.getBytes());
        buf.put((byte) 0x0F);
        buf.put(cmd.getBytes());
        buf.put((byte) 0x5D);
        ret = Tools.bytesToHexString(buf.array());
        
        return ret;
    }

    /**
     * 定距离回传
     * @param type String: 0,启动定距回报 1，停止定距回报
     * @param distance String:距离间隔，单位100米
     * @param count String:回传条数
     * @return String
     */
    public String locatorByDistance(String type, String distance, String count) {
        String cmd = "";
        String state = "";
        if (type.equals("0")) {
            state = "FF";
        } else {
            state = "00";
        }
        String hexDist = Tools.convertToHex(distance, 3);
        String hexCount = Tools.convertToHex(count, 3);
        String content = "(CMD,DUT," + state + "," + hexCount + "," + hexDist +
                         ")";
        ByteBuffer buf = ByteBuffer.allocate(14 + content.length());
        buf.put((byte) 0x5b);
        buf.put((byte) 0x10);
        buf.put("0100100001".getBytes());
        buf.put((byte) content.length());
        buf.put(content.getBytes());
        buf.put((byte) 0x5d);
        this.setByteArrayCmd(buf.array());
        try {
            cmd = new String(buf.array(), "ISO8859-1");
        } catch (UnsupportedEncodingException ex) {
        }
        return cmd;
    }

    public static void main(String[] args) {
        SegLPG52Locator l = new SegLPG52Locator(null);
       String s = l.multiLocator("1", "20", "1");
       Tools.fromHexString(s);
    }
}
