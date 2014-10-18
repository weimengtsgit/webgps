package com.autonavi.directl.parse;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.lbsgateway.GBLTerminalList;

public class ParseShouHangG40 extends ParseBase {
	String deviceid = null;

	@Override
	public void parseGPRS(String hexString) {
		Log.getInstance().outLog("���յ�SHԭʼ���ݣ�" + hexString);
		//System.out.println("���յ�SHԭʼ���ݣ�" + hexString);
		hexString = this.recParseData(hexString);
		byte[] cont = Tools.fromHexString(hexString);

//		String scont = new String(cont);
		//System.out.println("scont��" + scont);
		//System.out.println("scont��" + scont);
//		byte bIdentifier = cont[0]; //��ʶλ
//		String hIdentifier = Tools.bytesToHexString(new byte[] { bIdentifier });
		//System.out.println("��ʶλ��" + hIdentifier);
		
		byte[] bMsgId = new byte [2]; //��ϢID
		System.arraycopy(cont, 1, bMsgId, 0, bMsgId.length);
//		String hMsgId = Tools.bytesToHexString(bMsgId);
		//System.out.println("��ϢID��" + hMsgId);
		
		byte[] bMsgAttId = new byte [2]; //��Ϣ������
		System.arraycopy(cont, 3, bMsgAttId, 0, bMsgAttId.length);
//		String hMsgAttId = Tools.bytesToHexString(bMsgAttId);
		//System.out.println("��Ϣ�����ԣ�" + hMsgAttId);
		//System.out.println("��Ϣ�����ԣ�" + Tools.bytes2BinaryString(bMsgAttId));
		
		int iMsgLength = Tools.byte2Int(bMsgAttId) & 0x03FF;
		//System.out.println("��Ϣ�峤�ȣ�" + iMsgLength);

		int iFenBao = Tools.byte2Int(bMsgAttId) & 0x2000;
		//System.out.println("�ְ���ʶλ��" + iFenBao);
		
		byte[] bSimCardId = new byte [6]; //�ն��ֻ���
		System.arraycopy(cont, 5, bSimCardId, 0, bSimCardId.length);
		String hSimCardId = Tools.bytesToHexString(bSimCardId);
		//System.out.println("�ն��ֻ��ţ�" + hSimCardId);
		
		deviceid = hSimCardId;
		this.setDeviceSN(deviceid);
		String phnum = null;
//		phnum = "018201248264";//ɽ��
//        phnum = "014701569028";//ɽ��
		phnum = GBLTerminalList.getInstance().getSimcardNum(this.getDeviceSN());
		//phnum = "144012171029";
		if (phnum == null || hexString == null || phnum.trim().length() == 0
				|| hexString.trim().length() == 0) {
			Log.getInstance().outLog("ϵͳ��û�����䵽ָ�����նˣ�device_id=" + this.getDeviceSN());
			return;
		}
		this.setPhnum(phnum);
		
		byte[] bMsgSd = new byte [2]; //��Ϣ��ˮ��
		System.arraycopy(cont, 11, bMsgSd, 0, bMsgSd.length);
		String hMsgSd = Tools.bytesToHexString(bMsgSd);
		//System.out.println("��Ϣ��ˮ�ţ�" + hMsgSd);
		
		int fenbaoint = 0;
		if(iFenBao == 1){
			//�ְ�����
			byte[] bBaoLength = new byte [2]; //��Ϣ�ܰ���
			System.arraycopy(cont, 13, bBaoLength, 0, bBaoLength.length);
//			String hBaoLength = Tools.bytesToHexString(bBaoLength);
			//System.out.println("��Ϣ�ܰ�����" + hBaoLength);
			
			byte[] bBaoNum = new byte [2]; //���к�
			System.arraycopy(cont, 15, bBaoNum, 0, bBaoNum.length);
//			String hBaoNum = Tools.bytesToHexString(bBaoNum);
			//System.out.println("���кţ�" + hBaoNum);
			fenbaoint = 4;
		}

		byte[] bMsgBody = new byte [iMsgLength]; //��Ϣ��
		System.arraycopy(cont, 13 + fenbaoint, bMsgBody, 0, bMsgBody.length);
//		String hMsgBody = Tools.bytesToHexString(bMsgBody);
		//System.out.println("��Ϣ�壺" + hMsgBody);
		
		byte[] bCheckCode = new byte [1]; //������
		System.arraycopy(cont, 13 + fenbaoint + iMsgLength, bCheckCode, 0, bCheckCode.length);
//		String hCheckCode = Tools.bytesToHexString(bCheckCode);
		//System.out.println("�����룺" + hCheckCode);
		
		//�ж�Э��ָ��
		switch (Tools.byte2Int(bMsgId)) {
			case  1:
				//�ն�ͨ��Ӧ��
				Log.getInstance().outLog("�ն�ͨ��Ӧ��");
				this.setReplyByte1(platformReply(hMsgSd, hSimCardId, "0001"));
				break;
			case 2:
				//�ն�����
				Log.getInstance().outLog("�ն�����");
				this.setReplyByte1(platformReply(hMsgSd, hSimCardId, "0002"));
				break;
			case 256:
				//�ն�ע��
				Log.getInstance().outLog("�ն�ע��");
				this.setReplyByte1(registReply(hMsgSd, hSimCardId));
				break;
			case 258:
				//�ն˼�Ȩ
				Log.getInstance().outLog("�ն˼�Ȩ");
				this.setReplyByte1(platformReply(hMsgSd, hSimCardId, "0102"));
				break;
			case 512:
				//λ����Ϣ�㱨
				Log.getInstance().outLog("λ����Ϣ�㱨");
				this.parsePosition(bMsgBody);
				break;
				
		}
			
	}
	
	//����λ����Ϣ
	private void parsePosition(byte[] gpsdata) {
		byte[] bAlarmFlag = new byte [4]; //������־
		System.arraycopy(gpsdata, 0, bAlarmFlag, 0, bAlarmFlag.length);
//		String hAlarmFlag = Tools.bytesToHexString(bAlarmFlag);
		//System.out.println("������־��" + hAlarmFlag);

		byte[] bState = new byte [4]; //״̬
		System.arraycopy(gpsdata, 4, bState, 0, bState.length);
//		String hState = Tools.bytesToHexString(bState);
		//System.out.println("״̬��" + hState);
		String sState = Tools.bytes2BinaryString(bState);
		String accState = sState.substring(sState.length() - 1, sState.length());
		//System.out.println("ACC״̬��" + accState);
		this.setAccStatus(accState);
		String locState = sState.substring(sState.length() - 2, sState.length() - 1);
		//System.out.println("��λ״̬��" + locState);
		this.setLocateStatus(locState);
		
		byte[] bLatitude = new byte [4]; //γ��
		System.arraycopy(gpsdata, 8, bLatitude, 0, bLatitude.length);
		BigInteger bi = new BigInteger(bLatitude);
		String y = bi.toString();
		if(y.length()>3){
			y = y.substring(0,2)+"."+y.substring(2);
		}
		this.setCoordY(y);
		//System.out.println("γ�ȣ�" + y);
		
		byte[] bLongitude = new byte [4]; //����
		System.arraycopy(gpsdata, 12, bLongitude, 0, bLongitude.length);
		bi = new BigInteger(bLongitude);
		String x = bi.toString();
		if(x.length()>4){
			x = x.substring(0,3)+"."+x.substring(3);
		}
		this.setCoordX(x);
		//System.out.println("���ȣ�" + x);
		
		byte[] bHeight = new byte [2]; //�߳�
		System.arraycopy(gpsdata, 16, bHeight, 0, bHeight.length);
		bi = new BigInteger(bHeight);
//		String hHeight = bi.toString();
		//System.out.println("�̣߳�" + hHeight);
		
		byte[] bSpeed = new byte [2]; //�ٶ�
		System.arraycopy(gpsdata, 18, bSpeed, 0, bSpeed.length);
		bi = new BigInteger(bSpeed);
		//System.out.println("�ٶ�1��" + bi.toString());
		//bi = bi.divide(new BigInteger("10"));
		float speedl = bi.floatValue();
		try{
			speedl = speedl/10f;
		}catch(Exception e){
			Log.getInstance().errorLog("speed division error", e);
		}
		//String speed = bi.toString();
		//int ispd = Tools.byte2Int(bSpeed);
		//String speed = Tools.formatKnotToKm(ispd + "");
		this.setSpeed(speedl+"");
		//System.out.println("�ٶȣ�" + speedl);
		
		byte[] direction = new byte [2];//����
		System.arraycopy(gpsdata, 20, bSpeed, 0, bSpeed.length);
		int idirection = Tools.byte2Int(direction ) * 3;
		this.setDirection(idirection + "");
		//System.out.println("����" + idirection);
		
		byte[] date = new byte[6];//����
		System.arraycopy(gpsdata, 22, date, 0, 6);
		String dates = Tools.bytesToHexString(date);
		String yy = dates.substring(0, 2);
		String mm = dates.substring(2, 4);
		String dd = dates.substring(4, 6);
		String hh = dates.substring(6, 8);
		String MM = dates.substring(8, 10);
		String ss = dates.substring(10, 12);
		String format_date = this.conformtime(hh + MM + ss, dd + mm + yy);
		//System.out.println("ʱ�䣺" + format_date);
		this.setTime(format_date);
		//System.out.println("gpsdata.length��" + gpsdata.length);
		if(gpsdata.length > 28){
			//������Ϣ
			byte fjxx = gpsdata[28];
			String sfjxx = Tools.bytesToHexString(new byte[]{fjxx});
			//System.out.println("������Ϣ��" + sfjxx);
			if(sfjxx.equals("01")){
				byte fjxxcd = gpsdata[29];
				bi = new BigInteger(new byte[]{fjxxcd});
				int l = bi.intValue();
				//System.out.println("������Ϣ���ȣ�" + l);
				if(l>0){
					byte[] lc = new byte [l];//���
					System.arraycopy(gpsdata, 30, lc, 0, l);
					bi = new BigInteger(lc);
					//System.out.println("������Ϣ���1��" + bi.toString());
					float lcl = bi.floatValue();
					try{
						lcl = lcl/10f;
					}catch(Exception e){
						Log.getInstance().errorLog("lc division error", e);
					}
					//bi = bi.divide(new BigInteger("10"));
					//System.out.println("������Ϣ���2��" + lcl);
					this.setLC(lcl+"");
				}else{
					this.setLC("0");
				}
			}
			
	        byte[] bExtras = new byte [6]; //������Ϣ
	        System.arraycopy(gpsdata, gpsdata.length - 6, bExtras, 0, bExtras.length);
            byte bExtrasCmd = bExtras[0]; //������Ϣ��ָ���� 2b
            byte bExtrasLength = bExtras[1]; //���� 04
            if(bExtrasCmd == 0x2b && bExtrasLength == 0x04){
              try {
                byte[] bExtrasWD = new byte [2]; //�¶�
                System.arraycopy(bExtras, 2, bExtrasWD, 0, bExtrasWD.length);
//                String hExtrasWD = Tools.bytesToHexString(bExtrasWD);
                this.setTemperature(parseTemperature(bExtrasWD));
                byte[] bExtrasSD = new byte [2]; //ʪ��
                System.arraycopy(bExtras, 4, bExtrasSD, 0, bExtrasSD.length);
//                String hExtrasSD = Tools.bytesToHexString(bExtrasSD);
                this.setHumidity(parseHumidity(bExtrasSD));
                
              } catch (Exception e) {
                Log.getInstance().errorLog("Parse Temperature or Humidity Error!", e);
              }
            }
		}
		
		String desc = "�Ѷ�λGPS����";
		if (this.getLocateStatus() != null
				&& this.getLocateStatus().equals("1")) {
			this.sentPost(true);
		} else {
		  desc = "δ��λGPS����";
          this.sentPost(true);
		}
        Log.getInstance().outLog(
            this.getDeviceSN() + desc+"��lng=" + this.getCoordX()
                    + ",lat=" + this.getCoordY() + ",Speed="
                    + this.getSpeed() + ",Dirction="
                    + this.getDirection() + ",Mileage=" + this.getLC()
                    + ",Temperature=" + this.getTemperature()
                    + ",Humidity=" + this.getHumidity()
                    + ",gpstime=" + this.getTime());
	}
	
	/**
	 * �¶�
	 * @param bTemperature
	 * @return
	 */
    private String parseTemperature(byte[] bTemperature){
      byte pmJudge = (byte) 0x80; //������Ϣ��ָ���� 2b
      byte pm = (byte) (bTemperature[0] & pmJudge);
      String sTemperature = "";
      float fTemperature0 = Tools.byte2Int(new byte[]{bTemperature[0]});
      float fTemperature1 = Tools.byte2Int(new byte[]{bTemperature[1]});
      if(pm == 0x00){
        //�¶���ֵ
        sTemperature = ((fTemperature0 * 256 + fTemperature1) /10) +"";
      }else if(pm == (byte) 0x80){
        //�¶ȸ�ֵ
        sTemperature = (-((fTemperature0 - 128) * 256 + fTemperature1) /10) +"";
      }
      return sTemperature;
    }
    
    /**
     * ʪ��
     * @param bHumidity
     * @return
     */
    private String parseHumidity(byte[] bHumidity){
      String sHumidity = "";
      float fHumidity0 = Tools.byte2Int(new byte[]{bHumidity[0]});
      float fHumidity1 = Tools.byte2Int(new byte[]{bHumidity[1]});
      sHumidity = ((fHumidity0 * 256 + fHumidity1) /10) +"";
      return sHumidity;
    }
    
	private String recParseData(String data){
		//System.out.println("ת��ǰ����:"+data);
		Log.getInstance().outLog("ת��ǰ����:"+data);
		data = data.substring(2, data.length());
		data = data.substring(0, data.length() - 2);
		String[] datas = data.split("7d01");
		StringBuffer dataSb = new StringBuffer();
		for(int i = 0;i < datas.length;i++){
			String tmpData = datas[i].replaceAll("7d02", "7e");
			dataSb.append(tmpData);
			dataSb.append(",");
		}
		if(dataSb.length()>0){
			dataSb.deleteCharAt(dataSb.length()-1);
		}
		data = dataSb.toString().replaceAll(",", "7d");
		data = "7e"+data+"7e";
		//System.out.println("ת�������:"+data);
		Log.getInstance().outLog("ת�������:"+data);
		return data;
	}
	

	private String sendParseData(String data){
		Log.getInstance().outLog("ת��ǰ����:"+data);
		data = data.substring(2, data.length());
		data = data.substring(0, data.length() - 2);
		data = data.replaceAll("7d", "7d01");
		data = data.replaceAll("7e", "7d02");
		data = "7e"+data+"7e";
		Log.getInstance().outLog("ת�������:"+data);
		return data;
	}
	
	public static void main(String[] args) {
		//String hex = "7e0200001c144012171031007d010200002614401217103100a900080000000000020262a94506ee34ba0e7e";
		//String hex = "7e0200001c14401217103100f202000026144012171031002200080000000000020262d71b06f445a45f7e";
		//String hex = "7e0200001c144012171031007d0102000026144012171031002200080000000000020262d71b06f445a4d07e";
	    //String hex = "7e0200001c14401217103100de0000000000000003026249e006ee6d8c003701d80153120516184631a17e";
	    //ɽ��
	    //String hex = "7e0200002f01820124826403480200000000040003025dfd2a06be4ecc03d80000000014092813571101040000e99e03020000690100ef0400000000da7e";
	    //ɽ��
	    String hex = "7e0200003001470156902800040000000000000003021f5d5906f9de84005c0000b30014101416384701040000408202020000030200002b04001e0277347e";
		ParseShouHangG40 sh = new ParseShouHangG40();
		sh.parseGPRS(hex);
		
		//7e   ��ʶλ
		//0100   ��ϢID
		//0025   ��Ϣ������
		//018665958353 �ն��ֻ���
		//0026  ��Ϣ��ˮ��
		//001100005346434f4d53463131304734303231303232343100000000000000000000000000
		//b0        ������
		//7e   ��ʶλ
		//ParseShouHangG40 sh = new ParseShouHangG40();
		//sh.parseGPRS(hex);
		/*String hex = "7e01020009144012171031008b736f73677073673430997e";
		int port = 7001; // �˿ں�
		//String host = "localhost"; // ��������ַ
		String host = "220.181.186.67"; // ��������ַ
		Socket socket; // �ͻ����׽���
		byte[] buffer = Tools.fromHexString(hex);
		try {
			socket = new Socket(InetAddress.getByName(host), port); // ʵ�����װ���

			DataInputStream in = new DataInputStream(socket.getInputStream()); // �õ�������
			DataOutputStream out = new DataOutputStream(socket.getOutputStream()); // �õ������
			byte[] buffer1 = new byte[256];
			out.write(buffer);
			out.flush();
			in.read(buffer1); // �������ݵ�������
			in.close();
			System.out.println(Tools.bytesToHexString(buffer1)); // �����Ϣ
			System.out.println("Connect Success!");
			socket.close(); // �ر��׽���
		} catch (IOException ex) {
			ex.printStackTrace(); // ���������Ϣ
			System.out.println("Connect fail! error" + ex.getMessage());
			
		}*/
		/*String hex = "0100002501866595835300bc001100005346434f4d53463131304734303231303232343100000000000000000000000000";
		byte[] cont = Tools.fromHexString(hex);
		byte[] verifyCode = new byte[1];
		verifyCode[0] = verifyCode(cont);
		String verifyCodeS = Tools.bytesToHexString(verifyCode);
		System.out.println("verifyCodeS = " + verifyCodeS);*/
		
		/*String hex = "7e0200001c144012171031007d010200002614401217103100a900080000000000020262a94506ee34ba0e7e";
		byte[] cont = Tools.fromHexString(hex);
		ParseShouHangG40 sh = new ParseShouHangG40();
		sh.parsePosition(cont);*/
		
	}

	@Override
	public void parseSMS(String phnum, String content) {
		// TODO Auto-generated method stub
		
	}

	public static int getByteBit(byte data, double pos) {
		int bitData = 0;

		byte compare = (byte) Math.pow(2.0, pos);

		//byte b = (byte) (data & compare);
		if ((data & compare) == compare) {
			bitData = 1;
		}
		return bitData;
	}
	//У����
	public static byte verifyCode(byte[] data){
		byte verifyCode = (byte) (data[0]^data[1]);
		for(int i=2;i<data.length;i++){
			verifyCode = (byte) (data[i]^verifyCode);
		}
		return verifyCode;
	}
	/**
	 * �ն�ע��Ӧ��
	 * @param hMsgSd     ��Ϣ��ˮ��
	 * @param hSimCardId �ն��ֻ���
	 * @return
	 */
	public static byte[] registReply(String hMsgSd, String hSimCardId){
		//��Ϣͷ����:��ϢID+��Ϣ������+�ն��ֻ���+��Ϣ��ˮ��
		String head = "8100"+"000C"+hSimCardId+hMsgSd;
		//��Ϣ������:��Ϣ��ˮ��+���(0:�ɹ�)+��Ȩ��(sosgpsg40)
		String body = hMsgSd + "00"+ "736F73677073673430";
		byte[] verifyCodeArr = Tools.fromHexString(head+body);
		byte[] verifyCode = new byte[1];
		verifyCode[0] = verifyCode(verifyCodeArr);
		String verifyCodeS = Tools.bytesToHexString(verifyCode);
		String reps = "7e"+head+body+verifyCodeS+"7e";
		Log.getInstance().outLog("�ն�ע��Ӧ�� reply = "+reps);
		ParseShouHangG40 pshg40 = new ParseShouHangG40();
		reps = pshg40.sendParseData(reps);
		byte[] repb = Tools.fromHexString(reps);
		return repb;
	}

	public static byte[] platformReply(String hMsgSd, String hSimCardId, String terminalReqId){
		//��Ϣͷ����:��ϢID+��Ϣ������+�ն��ֻ���+��Ϣ��ˮ��
		String head = "8001"+"0005"+hSimCardId+hMsgSd;
		//��Ϣ������:��Ϣ��ˮ��+�ն���ϢID+��Ȩ��(sosgpsg40)
		String body = hMsgSd + terminalReqId + "00";
		byte[] verifyCodeArr = Tools.fromHexString(head+body);
		byte[] verifyCode = new byte[1];
		verifyCode[0] = verifyCode(verifyCodeArr);
		String verifyCodeS = Tools.bytesToHexString(verifyCode);
		String reps = "7e"+head+body+verifyCodeS+"7e";
		Log.getInstance().outLog("ƽ̨ͨ��Ӧ�� reply = "+reps);
		ParseShouHangG40 pshg40 = new ParseShouHangG40();
		reps = pshg40.sendParseData(reps);
		byte[] repb = Tools.fromHexString(reps);
		return repb;
	}
	
	public String conformtime(String time, String date) {
		try {
			String hour = time.substring(0, 2);
			String min = time.substring(2, 4);
			String sec = time.substring(4, 6);
			String day = date.substring(0, 2);
			String month = date.substring(2, 4);
			String year = date.substring(4, 6);
			String result = "";
			result = "20" + year + "-" + month + "-" + day + " ";
			result += hour + ":" + min + ":" + sec;
			SimpleDateFormat simpleDate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date d = simpleDate.parse(result);
			Calendar car = Calendar.getInstance();
			car.setTime(d);
			Date newDate = new Date(car.getTimeInMillis());
			return simpleDate.format(newDate);
		} catch (Exception ex) {
			Log.getInstance().errorLog("GPSʱ��ת������", ex);
		}
		return null;
	}
}

/**
 * 
ɽ��------
7e0200003001470156902800040000000000000003021f5d5906f9de84005c0000b30014101416384701040000408202020000030200002b04001e0277347e
7e   ��ʶλ
0200 ��Ϣͷ-��ϢID
0030 ��Ϣͷ-��Ϣ������
014701569028 ��Ϣͷ-�ն��ֻ���
0004 ��Ϣͷ-��Ϣ��ˮ��
00000000 ������־
00000003 ״̬
021f5d59 γ��
06f9de84 ����
005c �߳�
0000 �ٶ�
b300 ����
141014163847 ����
01 ������ϢID 01Ϊ���
04 ����
00004082 ���ֵ
0202000003020000
2b ������Ϣ��ָ����
04 ����
001e �¶�
0277 ʪ��
34 У����
7e ��ʶλ
 */
    