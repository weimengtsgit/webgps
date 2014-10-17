package com.sosgps.wzt.directl.device.lingtu;

import java.io.*;
import java.nio.*;

import com.sosgps.wzt.directl.Base64;
import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.TerminalControlAdaptor;
import com.sosgps.wzt.directl.idirectl.TerminalParam;

 

public class LingTuControl extends TerminalControlAdaptor {
	String head = "C M ";
	String oemcode = "";
	String deviceid = "";
	
    public LingTuControl( TerminalParam tparam) {
    	this.terminalParam = tparam;
		this.oemcode = tparam.getOemCode();
		this.deviceid = tparam.getSeriesNo();
    }

    public LingTuControl() {

    }

    public String setEletricityState(String seq,String state) {
    	String ret = "";
		
		return ret;
    }

    /**
     *锁定车门  state:0代表锁定车门,1代表打开锁定车门
     */
    public String setDoorState(String seq,String state) {
        String ret = "";
        
        return null;
    }
    /**
     * 遥控熄火
     * type:1熄火 0恢复熄火  
     */
    public  String setFlameout(String seq,String type, String state, String speed){
    	String ret = "";
    	//C M f 4C54:1001|100|0 3B5
		String hexSeq = Integer.toHexString(Integer.parseInt(seq));

		head = head + seq + " ";
 
		String cmd = this.oemcode + ":" + this.deviceid + "|100|" + type + " ";
		String vcode = Tools.getVerfyCode(cmd);
		ret = head + cmd + vcode;
		return ret;
    }

    public static void main(String[] args) {

        LingTuControl sp = new LingTuControl();
        sp.SendMessage("1", "lingtu");
        

    }

    //下发短消息
    public String SendMessage(String seq,String msg)  {
        String ret = null;
        //String data = "lingtu"; 
        //byte[] dl = data.getBytes("UTF-16");
        //byte[] bs =  new byte[dl.length-2];
        //System.arraycopy(dl,2, bs, 0, dl.length-2);
        //String data1 = base64encode(bs);
        try {
			byte[] msgByte = msg.trim().getBytes("UTF-16");
			byte cpyByte[] = new byte[msgByte.length-2];
			System.arraycopy(msgByte, 2, cpyByte, 0, msgByte.length-2);
			String base64str = Base64.base64encode(cpyByte).trim();
			head = head + seq + " ";
			 
			String cmd = this.oemcode + ":" + this.deviceid + "|10|5;" + base64str + " ";
			String vcode = Tools.getVerfyCode(cmd);
			ret = head + cmd + vcode;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        return ret;
    }
    /**
     * @param seq:序列号
     * @param no: 拍照通道号
     */
    public  String takePictures(String seq,String size, String action, String no){
    	String ret = "";
    	//C M 1 4C54:1001|301|1 3B9
		String hexSeq = Integer.toHexString(Integer.parseInt(seq));

		head = head + seq + " ";
 
		String cmd = this.oemcode + ":" + this.deviceid + "|301|" + no + " ";
		String vcode = Tools.getVerfyCode(cmd);
		ret = head + cmd + vcode;
		return ret;
    }
    //提取指定照片
    public String getPicture(String seq, String trackNo, String picId) {
    	String ret = "";
    	//C M 1 4C54:1001|301|1 3B9
		String hexSeq = Integer.toHexString(Integer.parseInt(seq));

		head = head + seq + " ";
		
		String cmd = this.oemcode + ":" + this.deviceid + "|302|" + trackNo + " ";
		String vcode = Tools.getVerfyCode(cmd);
		ret = head + cmd + vcode;
		return ret;
    }

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
