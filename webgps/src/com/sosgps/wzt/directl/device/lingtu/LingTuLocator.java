package com.sosgps.wzt.directl.device.lingtu;

import java.io.*;
import java.nio.*;

import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.LocatorAdaptor;
import com.sosgps.wzt.directl.idirectl.TerminalParam;

 

public class LingTuLocator extends LocatorAdaptor {
	String head = "C M ";
	String oemcode = "";
	String deviceid = "";
	
    public LingTuLocator( TerminalParam tparam) {
        this.terminalParam = tparam;
        this.oemcode = tparam.getOemCode();
        this.deviceid = tparam.getSeriesNo();
    }
    //点名
    public String singleLocator(String seq) {
        String ret = "";
        //C M 6 4C54:1001|2| 326
        String hexSeq = Integer.toHexString(Integer.parseInt(seq));
		 
        head = head + seq + " ";
		 
		String cmd = this.oemcode + ":" + this.deviceid + "|2| ";
		String vcode = Tools.getVerfyCode(cmd);
		ret = head + cmd + vcode;
        return ret.toUpperCase();
    }

    //设置频率
    //	C M SEQ OEMCODE:COMMADDR|CMDID|CMDARGUS VERIFYCODE\r\n
    public String multiLocator(String seq, String t, String count) {
        String ret = "";
        
        return ret.toUpperCase();
    }

    //停止跟踪
    public String setMultiLocatorState(String seq,String state) {
        String ret = "";
        String hexSeq = Integer.toHexString(Integer.parseInt(seq));
        head = head + seq + " ";
        String cmd =  this.oemcode+":"+this.deviceid+"|1| ";
        String vcode = Tools.getVerfyCode(cmd);
        ret = head + cmd + vcode;
        return ret.toUpperCase();
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
        
        return cmd;
    }

    public static void main(String[] args) {
        String s = "你好";
        String ss = "2008-10-11 22:33:22";
        String aa = ss.replaceAll("[-:\\s]", "");
    }
}
