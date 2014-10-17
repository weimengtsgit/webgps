package com.sosgps.wzt.directl.device.sege;

import com.sosgps.wzt.directl.CRC16;
import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.TerminalParam;
import com.sosgps.wzt.directl.idirectl.TerminalQueryAdaptor;
import com.sosgps.wzt.directl.idirectl.TerminalSettingAdaptor;
 
import java.nio.ByteBuffer;
import java.io.*;
 

public class SegLPG52TerminalSetting extends TerminalSettingAdaptor {
	
	String centerSn = "01";//���ĺ�
	String zuoSn = "001"; //��ϯ��
	
    public SegLPG52TerminalSetting() {
    }

    public SegLPG52TerminalSetting( TerminalParam
                                   tparam) {
        this.terminalParam = tparam;
    }
    
    
    /**
	 * TerminalSetting.java: ���ͼ��ʱ������
	 * 
	 * @param intervalType
	 *            
	 * @param t
	 *            String ���ʱ��
	 */
	public String setSendIntervalTime(String seq, String intervalType, String t) {
		 String ret = "";
	        String time = "";
	        
	        String cnt = "";
	        String cmdsn = centerSn+zuoSn;//�������к�
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
        //��RPM,13512345678,202105138021,11000,2048,T,3,01,,��
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
     * ��ȡ����ͼ��
     * @param type String��ͼ�����ͼ�����,��1����ʾ�������գ���2����ʾ��̨һ���Զ����գ���3����ʾ��̨����״̬����
     * @param number String��ͼ���ţ�0-255
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
     * �������������
     * @param photoNumber String����������
     * @param interval String���Զ���ʱ����
     * @param count String���Զ�������
     * @param condition String������������N=0 Ϊ�����գ�N=1 Ϊ���Ź���������գ�N=2 �����س�״�������գ�N=3 һ�ྯ��
     * @param isUpload String���Ƿ������ϴ� 0���պ��ϴ� 1���պ������ϴ�
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
     * Ҫ���ش����ݰ�
     * @param type String:����ʱ�Ĵ�����ʽ
     * @param number String��ͼ����
     * @param packageNums String���ش������ݰ���
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
      //  Log.getInstance().outLog("�ش����ݰ�ָ�" + cmd);
        return cmd;

    }

    /**
     * ������ֹ���������ͼ��
     * @param number String����������
     * @param property String:��ֹ����:C-��ֹ��ǰ A-��ֹ����
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

//��ѯ�����ϴ�ͼ��Ŀ¼
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
     * ���Ľ����ϴ�ָ��ͼ��ȷ��
     * @param index String:ָ�����
     * @param type String:ͼ������ʱ�Ĵ�����ʽ
     * @param number String��ͼ����
     * @param state String������״̬��FF�������� 00�쳣���� 11ָ���쳣
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
       // Log.getInstance().outLog("�����ϴ�ͼƬָ�" + cmd);
        return cmd;

    }

    /**
     * ���Ĳ�ѯ����ͷ
     * @param param String��Q--��ѯ��ǰ���� A--���²���
     * @return String
     */
    public String findPhotoParam(String seq,String param) {
    	
        String cmd = "";
        String cmdsn = "";//�������к�
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
