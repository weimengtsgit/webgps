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
     * ��������
     * @param seq:ָ������
     * @param state:Ŀǰ����Ϊ��
     * @param tel:GPS�ն˻ز��ĵ绰����
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
     *��������  state:0������������,1�������������
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
     * ң��Ϩ��
     * type:1Ϩ�� 0�ָ�Ϩ��  
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

    //�·�����Ϣ
    public String SendMessage(String seq,String msg)  { 
    	return null;
    }
    /**
     * @param seq:���к�
     * @param no: ����ͨ����
     */
    public  String takePictures(String seq,String size, String action, String no){return null;}
    //��ȡָ����Ƭ
    public String getPicture(String seq, String trackNo, String picId) {return null;}

    //��byte����ת����16�����ַ�
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
