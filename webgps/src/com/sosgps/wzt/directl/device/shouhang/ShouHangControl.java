package com.sosgps.wzt.directl.device.shouhang;

import java.io.*;
import java.nio.*;

import com.sosgps.wzt.directl.Base64;
import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.TerminalControlAdaptor;
import com.sosgps.wzt.directl.idirectl.TerminalParam;

 

public class ShouHangControl extends TerminalControlAdaptor {
	 
	String deviceid = "";
	
    public ShouHangControl( TerminalParam tparam) {
    	this.terminalParam = tparam;
		 
		this.deviceid = tparam.getSeriesNo();
    }

    public ShouHangControl() {

    }
    /**
     * 监听功能
     * @param seq:指令序列
     * @param state:目前可以为空
     * @param tel:GPS终端回拨的电话号码
     */
    public String setListening(String seq, String state, String tel) {
    	String dataHex = Tools.bytesToHexString(tel.getBytes());
		
		 String ret = ShouHangUtil.crtCmdByte(this.terminalParam.getSeriesNo(), "03", "03", dataHex);

		return  ret;
    }

    public String setEletricityState(String seq,String state) {
    	String ret = "";
		
		return ret;
    }

    /**
     *锁定车门  state:0代表锁定车门,1代表打开锁定车门
     */
    public String setDoorState(String seq,String state) {
    	String dataHex = "";
    	if (state.equals("1")){
    		dataHex = "01";
    	}else{
    		dataHex = "02";
    	}
		
		 String ret = ShouHangUtil.crtCmdByte(this.terminalParam.getSeriesNo(), "03", "05", dataHex);

		return  ret;
    }
    /**
     * 遥控熄火
     * type:1熄火 0恢复熄火  
     */
    public  String setFlameout(String seq,String type, String state, String speed){
    	String dataHex = "";
    	if (type.equals("1")){
    		dataHex = "01";
    	}else{
    		dataHex = "02";
    	}
		
		 String ret = ShouHangUtil.crtCmdByte(this.terminalParam.getSeriesNo(), "03", "04", dataHex);

		return  ret;
    }

    public static void main(String[] args) {

        ShouHangControl sp = new ShouHangControl();
        sp.SendMessage("1", "lingtu");
        

    }

    //下发短消息
    public String SendMessage(String seq,String msg)  { 
    	return null;
    }
    /**
     * @param seq:序列号
     * @param no: 拍照通道号
     */
    public  String takePictures(String seq,String size, String action, String no){return null;}
    //提取指定照片
    public String getPicture(String seq, String trackNo, String picId) {return null;}

    //把byte数组转换成16进制字符
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
    
    
    public String camera(String seq) {
    	 
    	return null;
    }
    
    

     

    
}
