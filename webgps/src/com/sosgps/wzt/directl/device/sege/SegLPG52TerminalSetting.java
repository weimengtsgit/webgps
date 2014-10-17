package com.sosgps.wzt.directl.device.sege;

import com.sosgps.wzt.directl.CRC16;
import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.TerminalParam;
import com.sosgps.wzt.directl.idirectl.TerminalQueryAdaptor;
import com.sosgps.wzt.directl.idirectl.TerminalSettingAdaptor;
 
import java.nio.ByteBuffer;
import java.io.*;
 

public class SegLPG52TerminalSetting extends TerminalSettingAdaptor {
	
	String centerSn = "01";//中心号
	String zuoSn = "001"; //坐席号
	
    public SegLPG52TerminalSetting() {
    }

    public SegLPG52TerminalSetting( TerminalParam
                                   tparam) {
        this.terminalParam = tparam;
    }
    
    
    /**
	 * TerminalSetting.java: 发送间隔时间设置
	 * 
	 * @param intervalType
	 *            
	 * @param t
	 *            String 间隔时间
	 */
	public String setSendIntervalTime(String seq, String intervalType, String t) {
		 String ret = "";
	        String time = "";
	        
	        String cnt = "";
	        String cmdsn = centerSn+zuoSn;//报文序列号
	        String inSeq = seq;
			 while(inSeq.length()<5){
				inSeq = "0"+inSeq;
			 }
			 cmdsn = cmdsn + inSeq;
			 
			String count = "1";
	        int tt = Integer.parseInt(t);
	        int cou = Integer.parseInt(count);

	        time = Tools.convertToHex(t.trim(), 2);
	        if (cou < 0 || cou == 1){
	             cnt ="FF";
	        }else {
	          cnt = Tools.convertToHex(count.trim(), 2);
	        }
	        String cmd = "(CMD,002," + cnt + "," + time + ")";
	        ByteBuffer buf = ByteBuffer.allocate(cmd.length()+cmdsn.getBytes().length+4);
	        buf.put((byte) 0x5B);
	        buf.put((byte) 0x11);
	        buf.put(cmdsn.getBytes());
	        buf.put((byte) 0x0F);
	        buf.put(cmd.getBytes());
	        buf.put((byte) 0x5D);
	        ret = Tools.bytesToHexString(buf.array());
	        return ret;

	}

    private String getIp(String ip) {
        String temp = ip;
        if (temp != null && temp.trim().length() != 0) {
            while (temp.length() < 3) {
                temp = "0" + temp;
            }
        }
        return temp;
    }

    public String setIPPort(String serverIP, String serverPort, String reDial) {
        //（RPM,13512345678,202105138021,11000,2048,T,3,01,,）
        String content = "";
        String simCard = this.terminalParam.getSimCard();
        String[] ips = serverIP.split("\\.");
        String ipStr = getIp(ips[0]) + getIp(ips[1]) + getIp(ips[2]) +
                       getIp(ips[3]);
        int interval = Integer.parseInt(reDial);
        String hexInter = Integer.toHexString(interval).toUpperCase();
        if (hexInter.length() > 3) {
            hexInter = "FF";
        }
        if (hexInter.length() == 1) {
            hexInter = "0" + hexInter;
        }
        content = "(RPM," + simCard + "," + ipStr + "," + serverPort + "," +
                  "2048,T,3," + hexInter + ",,)";
        int size = 14 + content.length();
        ByteBuffer buf = ByteBuffer.allocate(size);
        buf.put((byte) 0x5B);
        buf.put((byte) 0x37);
        buf.put("0100100001".getBytes());
        buf.put((byte) content.length());
        buf.put(content.getBytes());
        buf.put((byte) 0x5D);
        this.setByteArrayCmd(buf.array());
        String ret = new String(buf.array());
        this.setCmd(ret);
        return ret;
    }

    public static void main(String[] args) {
        SegLPG52TerminalSetting s = new SegLPG52TerminalSetting();

    }

    public String setAPN(String v) {
        String ret = "";
        int length = 20 + v.length();
        ByteBuffer buf = ByteBuffer.allocate(length);
        buf.put((byte) 0x5B);
        buf.put((byte) 0x40);
        buf.put("0100100001".getBytes());
        buf.put((byte) (6 + v.length()));
        buf.put("(RPD,".getBytes());
        buf.put(v.getBytes());
        buf.put(")".getBytes());
        buf.put((byte) 0x5D);
        ret = new String(buf.array());

        return ret;
    }


    /**
     * 提取单幅图像
     * @param type String：图像类别，图象类别,’1’表示中心拍照，’2’表示车台一般自动拍照，’3’表示车台警情状态拍照
     * @param number String：图像编号，0-255
     * @return String
     */
    public String getSinglePic(String type, String number) {
        String cmd = "";
        if (type == null || number == null) {
            return "";
        }
        String num = this.convertToHex(number, 2);
        String content = "(PIC,02," + type + num + ")";
        int size = 14 + content.length();
        ByteBuffer buf = ByteBuffer.allocate(size);
        buf.put((byte) 0x5B);
        buf.put((byte) 0xDC);
        buf.put("0100100001".getBytes());
        buf.put((byte) content.length());
        buf.put(content.getBytes());
        buf.put((byte) 0x5D);
        this.setByteArrayCmd(buf.array());
        try {
            cmd = new String(buf.array(), "ISO8859-1");
        } catch (UnsupportedEncodingException ex) {
        }

        return cmd;

    }

    /**
     * 设置摄象机参数
     * @param photoNumber String：摄象机编号
     * @param interval String：自动拍时间间隔
     * @param count String：自动拍张数
     * @param condition String：拍照条件：N=0 为不拍照，N=1 为车门关情况下拍照，N=2 进入重车状况下拍照，N=3 一类警情
     * @param isUpload String：是否立即上传 0拍照后不上传 1拍照后立即上传
     * @return String
     */

    public String setPhotoParam(String photoNumber, String interval,
                                String count,
                                String condition, String isUpload) {
        String cmd = "";
        String content = "(PIC,07," + this.convertToHex(photoNumber, 1) + "," +
                         this.convertToHex(interval, 4) + "," +
                         this.convertToHex(count, 2) + "," + condition +
                         ",1000," + isUpload +
                         ")";
        int size = 14 + content.length();
        ByteBuffer buf = ByteBuffer.allocate(size);
        buf.put((byte) 0x5B);
        buf.put((byte) 0xDC);
        buf.put("0100100001".getBytes());
        buf.put((byte) content.length());
        buf.put(content.getBytes());
        buf.put((byte) 0x5D);
        this.setByteArrayCmd(buf.array());
        try {
            cmd = new String(buf.array(), "ISO8859-1");
        } catch (UnsupportedEncodingException ex) {
        }

        return cmd;
    }

    /**
     * 要求重传数据包
     * @param type String:拍照时的触发方式
     * @param number String：图象编号
     * @param packageNums String：重传的数据包号
     * @return String
     */

    public String setRetryData(String type, String number, String packageNums) {
        String cmd = "";
        String[] packNos = packageNums.split(",");
        String hexStr = "";
        for (int i = 0; i < packNos.length; i++) {
            if (packNos[i] != null && !packNos[i].equals("")) {
                hexStr = hexStr + this.convertToHex(packNos[i], 3);
            }
        }
        String content = "(PIC,04," + type + convertToHex(number, 2) + hexStr +
                         ")";
        int size = 14 + content.length();
        ByteBuffer buf = ByteBuffer.allocate(size);
        buf.put((byte) 0x5B);
        buf.put((byte) 0xDC);
        buf.put("0100100001".getBytes());
        buf.put((byte) content.length());
        buf.put(content.getBytes());
        buf.put((byte) 0x5D);
        this.setByteArrayCmd(buf.array());
        try {
            cmd = new String(buf.array(), "ISO8859-1");
        } catch (UnsupportedEncodingException ex) {
        }
      //  Log.getInstance().outLog("重传数据包指令：" + cmd);
        return cmd;

    }

    /**
     * 中心终止摄象机传输图象
     * @param number String：摄象机编号
     * @param property String:终止属性:C-终止当前 A-终止所有
     * @return String
     */
    public String stopPisTrans(String number, String property) {
        String cmd = "";
        String content = "(PIC,08," + this.convertToHex(number, 1) + "," +
                         property +
                         ")";
        int size = 14 + content.length();
        ByteBuffer buf = ByteBuffer.allocate(size);
        buf.put((byte) 0x5B);
        buf.put((byte) 0xDC);
        buf.put("0100100001".getBytes());
        buf.put((byte) content.length());
        buf.put(content.getBytes());
        buf.put((byte) 0x5D);
        this.setByteArrayCmd(buf.array());
        try {
            cmd = new String(buf.array(), "ISO8859-1");
        } catch (UnsupportedEncodingException ex) {
        }

        return cmd;

    }

//查询所有上传图像目录
    public String findAllUploadImage() {
        String cmd = "";
        String content = "(PIC,06,A)";
        int size = 14 + content.length();
        ByteBuffer buf = ByteBuffer.allocate(size);
        buf.put((byte) 0x5B);
        buf.put((byte) 0xDC);
        buf.put("0100100001".getBytes());
        buf.put((byte) content.length());
        buf.put(content.getBytes());
        buf.put((byte) 0x5D);
        this.setByteArrayCmd(buf.array());
        try {
            cmd = new String(buf.array(), "ISO8859-1");
        } catch (UnsupportedEncodingException ex) {
        }

        return cmd;

    }

    /**
     * 中心结束上传指定图像确认
     * @param index String:指令序号
     * @param type String:图像拍照时的触发方式
     * @param number String：图像编号
     * @param state String：结束状态：FF正常结束 00异常结束 11指令异常
     * @return String
     */
    public String stopImgUpload(String index, String type, String number,
                                String state) {
        String cmd = "";
        String content = "(PIC,05," + type + convertToHex(number, 2) + "," +
                         state + ")";
        int size = 14 + content.length();
        ByteBuffer buf = ByteBuffer.allocate(size);
        buf.put((byte) 0x5B);
        buf.put((byte) 0xDC);
        buf.put(index.getBytes());
        buf.put((byte) content.length());
        buf.put(content.getBytes());
        buf.put((byte) 0x5D);
        this.setByteArrayCmd(buf.array());
        try {
            cmd = new String(buf.array(), "ISO8859-1");
        } catch (UnsupportedEncodingException ex) {
        }
       // Log.getInstance().outLog("结束上传图片指令：" + cmd);
        return cmd;

    }

    /**
     * 中心查询摄象头
     * @param param String：Q--查询当前属性 A--重新查找
     * @return String
     */
    public String findPhotoParam(String seq,String param) {
    	
        String cmd = "";
        String cmdsn = "";//报文序列号
		cmdsn = Tools.convertToHex(seq, 8);
		
        String content = "(PIC,09," + convertToHex(param, 2) + ")";
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
}
