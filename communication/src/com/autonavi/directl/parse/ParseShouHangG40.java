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
		Log.getInstance().outLog("接收到SH原始数据：" + hexString);
		//System.out.println("接收到SH原始数据：" + hexString);
		hexString = this.recParseData(hexString);
		byte[] cont = Tools.fromHexString(hexString);

//		String scont = new String(cont);
		//System.out.println("scont：" + scont);
		//System.out.println("scont：" + scont);
//		byte bIdentifier = cont[0]; //标识位
//		String hIdentifier = Tools.bytesToHexString(new byte[] { bIdentifier });
		//System.out.println("标识位：" + hIdentifier);
		
		byte[] bMsgId = new byte [2]; //消息ID
		System.arraycopy(cont, 1, bMsgId, 0, bMsgId.length);
//		String hMsgId = Tools.bytesToHexString(bMsgId);
		//System.out.println("消息ID：" + hMsgId);
		
		byte[] bMsgAttId = new byte [2]; //消息体属性
		System.arraycopy(cont, 3, bMsgAttId, 0, bMsgAttId.length);
//		String hMsgAttId = Tools.bytesToHexString(bMsgAttId);
		//System.out.println("消息体属性：" + hMsgAttId);
		//System.out.println("消息体属性：" + Tools.bytes2BinaryString(bMsgAttId));
		
		int iMsgLength = Tools.byte2Int(bMsgAttId) & 0x03FF;
		//System.out.println("消息体长度：" + iMsgLength);

		int iFenBao = Tools.byte2Int(bMsgAttId) & 0x2000;
		//System.out.println("分包标识位：" + iFenBao);
		
		byte[] bSimCardId = new byte [6]; //终端手机号
		System.arraycopy(cont, 5, bSimCardId, 0, bSimCardId.length);
		String hSimCardId = Tools.bytesToHexString(bSimCardId);
		//System.out.println("终端手机号：" + hSimCardId);
		
		deviceid = hSimCardId;
		this.setDeviceSN(deviceid);
		String phnum = null;
//		phnum = "018201248264";//山西
//        phnum = "014701569028";//山东
		phnum = GBLTerminalList.getInstance().getSimcardNum(this.getDeviceSN());
		//phnum = "144012171029";
		if (phnum == null || hexString == null || phnum.trim().length() == 0
				|| hexString.trim().length() == 0) {
			Log.getInstance().outLog("系统中没有适配到指定的终端：device_id=" + this.getDeviceSN());
			return;
		}
		this.setPhnum(phnum);
		
		byte[] bMsgSd = new byte [2]; //消息流水号
		System.arraycopy(cont, 11, bMsgSd, 0, bMsgSd.length);
		String hMsgSd = Tools.bytesToHexString(bMsgSd);
		//System.out.println("消息流水号：" + hMsgSd);
		
		int fenbaoint = 0;
		if(iFenBao == 1){
			//分包数据
			byte[] bBaoLength = new byte [2]; //消息总包数
			System.arraycopy(cont, 13, bBaoLength, 0, bBaoLength.length);
//			String hBaoLength = Tools.bytesToHexString(bBaoLength);
			//System.out.println("消息总包数：" + hBaoLength);
			
			byte[] bBaoNum = new byte [2]; //序列号
			System.arraycopy(cont, 15, bBaoNum, 0, bBaoNum.length);
//			String hBaoNum = Tools.bytesToHexString(bBaoNum);
			//System.out.println("序列号：" + hBaoNum);
			fenbaoint = 4;
		}

		byte[] bMsgBody = new byte [iMsgLength]; //消息体
		System.arraycopy(cont, 13 + fenbaoint, bMsgBody, 0, bMsgBody.length);
//		String hMsgBody = Tools.bytesToHexString(bMsgBody);
		//System.out.println("消息体：" + hMsgBody);
		
		byte[] bCheckCode = new byte [1]; //检验码
		System.arraycopy(cont, 13 + fenbaoint + iMsgLength, bCheckCode, 0, bCheckCode.length);
//		String hCheckCode = Tools.bytesToHexString(bCheckCode);
		//System.out.println("检验码：" + hCheckCode);
		
		//判断协议指令
		switch (Tools.byte2Int(bMsgId)) {
			case  1:
				//终端通用应答
				Log.getInstance().outLog("终端通用应答");
				this.setReplyByte1(platformReply(hMsgSd, hSimCardId, "0001"));
				break;
			case 2:
				//终端心跳
				Log.getInstance().outLog("终端心跳");
				this.setReplyByte1(platformReply(hMsgSd, hSimCardId, "0002"));
				break;
			case 256:
				//终端注册
				Log.getInstance().outLog("终端注册");
				this.setReplyByte1(registReply(hMsgSd, hSimCardId));
				break;
			case 258:
				//终端鉴权
				Log.getInstance().outLog("终端鉴权");
				this.setReplyByte1(platformReply(hMsgSd, hSimCardId, "0102"));
				break;
			case 512:
				//位置信息汇报
				Log.getInstance().outLog("位置信息汇报");
				this.parsePosition(bMsgBody);
				break;
				
		}
			
	}
	
	//解析位置信息
	private void parsePosition(byte[] gpsdata) {
		byte[] bAlarmFlag = new byte [4]; //报警标志
		System.arraycopy(gpsdata, 0, bAlarmFlag, 0, bAlarmFlag.length);
//		String hAlarmFlag = Tools.bytesToHexString(bAlarmFlag);
		//System.out.println("报警标志：" + hAlarmFlag);

		byte[] bState = new byte [4]; //状态
		System.arraycopy(gpsdata, 4, bState, 0, bState.length);
//		String hState = Tools.bytesToHexString(bState);
		//System.out.println("状态：" + hState);
		String sState = Tools.bytes2BinaryString(bState);
		String accState = sState.substring(sState.length() - 1, sState.length());
		//System.out.println("ACC状态：" + accState);
		this.setAccStatus(accState);
		String locState = sState.substring(sState.length() - 2, sState.length() - 1);
		//System.out.println("定位状态：" + locState);
		this.setLocateStatus(locState);
		
		byte[] bLatitude = new byte [4]; //纬度
		System.arraycopy(gpsdata, 8, bLatitude, 0, bLatitude.length);
		BigInteger bi = new BigInteger(bLatitude);
		String y = bi.toString();
		if(y.length()>3){
			y = y.substring(0,2)+"."+y.substring(2);
		}
		this.setCoordY(y);
		//System.out.println("纬度：" + y);
		
		byte[] bLongitude = new byte [4]; //经度
		System.arraycopy(gpsdata, 12, bLongitude, 0, bLongitude.length);
		bi = new BigInteger(bLongitude);
		String x = bi.toString();
		if(x.length()>4){
			x = x.substring(0,3)+"."+x.substring(3);
		}
		this.setCoordX(x);
		//System.out.println("经度：" + x);
		
		byte[] bHeight = new byte [2]; //高程
		System.arraycopy(gpsdata, 16, bHeight, 0, bHeight.length);
		bi = new BigInteger(bHeight);
//		String hHeight = bi.toString();
		//System.out.println("高程：" + hHeight);
		
		byte[] bSpeed = new byte [2]; //速度
		System.arraycopy(gpsdata, 18, bSpeed, 0, bSpeed.length);
		bi = new BigInteger(bSpeed);
		//System.out.println("速度1：" + bi.toString());
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
		//System.out.println("速度：" + speedl);
		
		byte[] direction = new byte [2];//方向
		System.arraycopy(gpsdata, 20, bSpeed, 0, bSpeed.length);
		int idirection = Tools.byte2Int(direction ) * 3;
		this.setDirection(idirection + "");
		//System.out.println("方向：" + idirection);
		
		byte[] date = new byte[6];//日期
		System.arraycopy(gpsdata, 22, date, 0, 6);
		String dates = Tools.bytesToHexString(date);
		String yy = dates.substring(0, 2);
		String mm = dates.substring(2, 4);
		String dd = dates.substring(4, 6);
		String hh = dates.substring(6, 8);
		String MM = dates.substring(8, 10);
		String ss = dates.substring(10, 12);
		String format_date = this.conformtime(hh + MM + ss, dd + mm + yy);
		//System.out.println("时间：" + format_date);
		this.setTime(format_date);
		//System.out.println("gpsdata.length：" + gpsdata.length);
		if(gpsdata.length > 28){
			//附加信息
			byte fjxx = gpsdata[28];
			String sfjxx = Tools.bytesToHexString(new byte[]{fjxx});
			//System.out.println("附加信息：" + sfjxx);
			if(sfjxx.equals("01")){
				byte fjxxcd = gpsdata[29];
				bi = new BigInteger(new byte[]{fjxxcd});
				int l = bi.intValue();
				//System.out.println("附加信息长度：" + l);
				if(l>0){
					byte[] lc = new byte [l];//里程
					System.arraycopy(gpsdata, 30, lc, 0, l);
					bi = new BigInteger(lc);
					//System.out.println("附加信息里程1：" + bi.toString());
					float lcl = bi.floatValue();
					try{
						lcl = lcl/10f;
					}catch(Exception e){
						Log.getInstance().errorLog("lc division error", e);
					}
					//bi = bi.divide(new BigInteger("10"));
					//System.out.println("附加信息里程2：" + lcl);
					this.setLC(lcl+"");
				}else{
					this.setLC("0");
				}
			}
			
	        byte[] bExtras = new byte [6]; //附加信息
	        System.arraycopy(gpsdata, gpsdata.length - 6, bExtras, 0, bExtras.length);
            byte bExtrasCmd = bExtras[0]; //附加信息的指令码 2b
            byte bExtrasLength = bExtras[1]; //长度 04
            if(bExtrasCmd == 0x2b && bExtrasLength == 0x04){
              try {
                byte[] bExtrasWD = new byte [2]; //温度
                System.arraycopy(bExtras, 2, bExtrasWD, 0, bExtrasWD.length);
//                String hExtrasWD = Tools.bytesToHexString(bExtrasWD);
                this.setTemperature(parseTemperature(bExtrasWD));
                byte[] bExtrasSD = new byte [2]; //湿度
                System.arraycopy(bExtras, 4, bExtrasSD, 0, bExtrasSD.length);
//                String hExtrasSD = Tools.bytesToHexString(bExtrasSD);
                this.setHumidity(parseHumidity(bExtrasSD));
                
              } catch (Exception e) {
                Log.getInstance().errorLog("Parse Temperature or Humidity Error!", e);
              }
            }
		}
		
		String desc = "已定位GPS数据";
		if (this.getLocateStatus() != null
				&& this.getLocateStatus().equals("1")) {
			this.sentPost(true);
		} else {
		  desc = "未定位GPS数据";
          this.sentPost(true);
		}
        Log.getInstance().outLog(
            this.getDeviceSN() + desc+"：lng=" + this.getCoordX()
                    + ",lat=" + this.getCoordY() + ",Speed="
                    + this.getSpeed() + ",Dirction="
                    + this.getDirection() + ",Mileage=" + this.getLC()
                    + ",Temperature=" + this.getTemperature()
                    + ",Humidity=" + this.getHumidity()
                    + ",gpstime=" + this.getTime());
	}
	
	/**
	 * 温度
	 * @param bTemperature
	 * @return
	 */
    private String parseTemperature(byte[] bTemperature){
      byte pmJudge = (byte) 0x80; //附加信息的指令码 2b
      byte pm = (byte) (bTemperature[0] & pmJudge);
      String sTemperature = "";
      float fTemperature0 = Tools.byte2Int(new byte[]{bTemperature[0]});
      float fTemperature1 = Tools.byte2Int(new byte[]{bTemperature[1]});
      if(pm == 0x00){
        //温度正值
        sTemperature = ((fTemperature0 * 256 + fTemperature1) /10) +"";
      }else if(pm == (byte) 0x80){
        //温度负值
        sTemperature = (-((fTemperature0 - 128) * 256 + fTemperature1) /10) +"";
      }
      return sTemperature;
    }
    
    /**
     * 湿度
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
		//System.out.println("转义前数据:"+data);
		Log.getInstance().outLog("转义前数据:"+data);
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
		//System.out.println("转义后数据:"+data);
		Log.getInstance().outLog("转义后数据:"+data);
		return data;
	}
	

	private String sendParseData(String data){
		Log.getInstance().outLog("转义前数据:"+data);
		data = data.substring(2, data.length());
		data = data.substring(0, data.length() - 2);
		data = data.replaceAll("7d", "7d01");
		data = data.replaceAll("7e", "7d02");
		data = "7e"+data+"7e";
		Log.getInstance().outLog("转义后数据:"+data);
		return data;
	}
	
	public static void main(String[] args) {
		//String hex = "7e0200001c144012171031007d010200002614401217103100a900080000000000020262a94506ee34ba0e7e";
		//String hex = "7e0200001c14401217103100f202000026144012171031002200080000000000020262d71b06f445a45f7e";
		//String hex = "7e0200001c144012171031007d0102000026144012171031002200080000000000020262d71b06f445a4d07e";
	    //String hex = "7e0200001c14401217103100de0000000000000003026249e006ee6d8c003701d80153120516184631a17e";
	    //山西
	    //String hex = "7e0200002f01820124826403480200000000040003025dfd2a06be4ecc03d80000000014092813571101040000e99e03020000690100ef0400000000da7e";
	    //山东
	    String hex = "7e0200003001470156902800040000000000000003021f5d5906f9de84005c0000b30014101416384701040000408202020000030200002b04001e0277347e";
		ParseShouHangG40 sh = new ParseShouHangG40();
		sh.parseGPRS(hex);
		
		//7e   标识位
		//0100   消息ID
		//0025   消息体属性
		//018665958353 终端手机号
		//0026  消息流水号
		//001100005346434f4d53463131304734303231303232343100000000000000000000000000
		//b0        检验码
		//7e   标识位
		//ParseShouHangG40 sh = new ParseShouHangG40();
		//sh.parseGPRS(hex);
		/*String hex = "7e01020009144012171031008b736f73677073673430997e";
		int port = 7001; // 端口号
		//String host = "localhost"; // 服务器地址
		String host = "220.181.186.67"; // 服务器地址
		Socket socket; // 客户端套接字
		byte[] buffer = Tools.fromHexString(hex);
		try {
			socket = new Socket(InetAddress.getByName(host), port); // 实例化套按字

			DataInputStream in = new DataInputStream(socket.getInputStream()); // 得到输入流
			DataOutputStream out = new DataOutputStream(socket.getOutputStream()); // 得到输出流
			byte[] buffer1 = new byte[256];
			out.write(buffer);
			out.flush();
			in.read(buffer1); // 读入数据到缓冲区
			in.close();
			System.out.println(Tools.bytesToHexString(buffer1)); // 输出信息
			System.out.println("Connect Success!");
			socket.close(); // 关闭套接字
		} catch (IOException ex) {
			ex.printStackTrace(); // 输出错误信息
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
	//校验码
	public static byte verifyCode(byte[] data){
		byte verifyCode = (byte) (data[0]^data[1]);
		for(int i=2;i<data.length;i++){
			verifyCode = (byte) (data[i]^verifyCode);
		}
		return verifyCode;
	}
	/**
	 * 终端注册应答
	 * @param hMsgSd     消息流水号
	 * @param hSimCardId 终端手机号
	 * @return
	 */
	public static byte[] registReply(String hMsgSd, String hSimCardId){
		//消息头内容:消息ID+消息体属性+终端手机号+消息流水号
		String head = "8100"+"000C"+hSimCardId+hMsgSd;
		//消息体内容:消息流水号+结果(0:成功)+鉴权码(sosgpsg40)
		String body = hMsgSd + "00"+ "736F73677073673430";
		byte[] verifyCodeArr = Tools.fromHexString(head+body);
		byte[] verifyCode = new byte[1];
		verifyCode[0] = verifyCode(verifyCodeArr);
		String verifyCodeS = Tools.bytesToHexString(verifyCode);
		String reps = "7e"+head+body+verifyCodeS+"7e";
		Log.getInstance().outLog("终端注册应答 reply = "+reps);
		ParseShouHangG40 pshg40 = new ParseShouHangG40();
		reps = pshg40.sendParseData(reps);
		byte[] repb = Tools.fromHexString(reps);
		return repb;
	}

	public static byte[] platformReply(String hMsgSd, String hSimCardId, String terminalReqId){
		//消息头内容:消息ID+消息体属性+终端手机号+消息流水号
		String head = "8001"+"0005"+hSimCardId+hMsgSd;
		//消息体内容:消息流水号+终端消息ID+鉴权码(sosgpsg40)
		String body = hMsgSd + terminalReqId + "00";
		byte[] verifyCodeArr = Tools.fromHexString(head+body);
		byte[] verifyCode = new byte[1];
		verifyCode[0] = verifyCode(verifyCodeArr);
		String verifyCodeS = Tools.bytesToHexString(verifyCode);
		String reps = "7e"+head+body+verifyCodeS+"7e";
		Log.getInstance().outLog("平台通用应答 reply = "+reps);
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
			Log.getInstance().errorLog("GPS时间转换错误", ex);
		}
		return null;
	}
}

/**
 * 
山东------
7e0200003001470156902800040000000000000003021f5d5906f9de84005c0000b30014101416384701040000408202020000030200002b04001e0277347e
7e   标识位
0200 消息头-消息ID
0030 消息头-消息体属性
014701569028 消息头-终端手机号
0004 消息头-消息流水号
00000000 报警标志
00000003 状态
021f5d59 纬度
06f9de84 经度
005c 高程
0000 速度
b300 方向
141014163847 日期
01 附加信息ID 01为里程
04 长度
00004082 里程值
0202000003020000
2b 附加信息的指令码
04 长度
001e 温度
0277 湿度
34 校验码
7e 标识位
 */
    