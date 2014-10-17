package com.autonavi.directl.device.sege;

import java.io.*;
import java.nio.*;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.idirectl.LocatorAdaptor;
import com.autonavi.directl.idirectl.TerminalParam;

 

public class SegLPG52Locator extends LocatorAdaptor {
	String centerSn = "01";//���ĺ�
	String zuoSn = "001"; //��ϯ��
	
    public SegLPG52Locator( TerminalParam tparam) {
        this.terminalParam = tparam;
    }

    public String singleLocator(String seq) {
    	String cmdsn = "";//�������к�
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

    //��ζ�λ
    public String multiLocator(String seq,String t, String count) {
    	
        String ret = "";
        String time = "";
        
        String cnt = "";
        String cmdsn = centerSn+zuoSn;//�������к�
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
        Log.getInstance().outLog("�·�Ƶ������ָ�"+ret);
        return ret;
    }

    //ֹͣ����
    public String setMultiLocatorState(String seq, String state) {
    	String cmdsn = centerSn+zuoSn;//�������к�
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
     * ������ش�
     * @param type String: 0,��������ر� 1��ֹͣ����ر�
     * @param distance String:����������λ100��
     * @param count String:�ش�����
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
