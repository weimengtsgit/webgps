package com.autonavi.directl.parse;

import java.io.IOException;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import org.logicalcobwebs.cglib.core.TinyBitSet;

import com.autonavi.directl.Base641111;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;

import com.autonavi.directl.Tools;
import com.autonavi.directl.bean.TArea;
import com.autonavi.directl.bean.TSpeedCase;
import com.autonavi.directl.dbutil.DbOperation;
import com.autonavi.directl.dbutil.DbUtil;
import com.autonavi.directl.dbutil.service.DBService;
import com.autonavi.directl.dbutil.service.DBServiceImpl;
import com.autonavi.directl.pic.ByteCont;
import com.autonavi.directl.pic.PicCache;
import com.autonavi.directl.pic.Picture;

import com.autonavi.lbsgateway.GBLTerminalList;
import com.autonavi.lbsgateway.GPRSThreadList;
import com.autonavi.lbsgateway.alarmpool.AlarmQueue;
import com.autonavi.lbsgateway.bean.InstructionBean;
import com.autonavi.lbsgateway.service.CommonGatewayServiceImpl;
import com.autonavi.lbsgateway.service.ICommonGatewayService;
import com.autonavi.util.OverLoadUtil;
import com.autonavi.util.PropertyReader;
import com.mapabc.geom.CoordCvtAPI;
import com.mapabc.geom.DPoint;

//山东 雅讯终端 
public class ParseLingTu extends ParseBase {

	private static String isOverLoad = null;// 是否有负载
	private PropertyReader proRead;
	private String content = null;

	public ParseLingTu() {
		
	}

	@Override
	public void parseGPRS(String hex) {
		// A{space}N{space}OEMCODE:COMADDR{space}UTC|Latitude|Longitude|Altitude|Heading|Speed|
		// TransactionFlag|OEMStatus|StatusString{space}VERFYCODE\r\n
		// A P 4C54:13455141509
		// 090217044957|07DD7458|191BB06C|80000000|0108|73C0|00000000,00000000|00040004,000021A2|
		// 1713
 		String hexString = hex;
//		int endIndex = hex.lastIndexOf("0d0a"); // "\r\n结束符合"
//		if (endIndex != -1) {
//			hexString = hex.substring(0, endIndex);// 截取\r\n之前的字节
//		} else {
//			Log.getInstance().outLog("灵图协议车机数据格式有误：" + hex);
//			return;
//		}
		byte[] bc = Tools.fromHexString(hexString);

		content = new String(bc);
		String[] splitcont = content.split(" "); // 以空格分隔指令
		Log.getInstance().outLog("灵图车机数据：" + content);

		String cmdtype = splitcont[1]; // 上行类型

		String oemCode = null;
		String deviceId = null;
		try {
			oemCode = splitcont[2].substring(0, splitcont[2].indexOf(":")); // OEM码
			deviceId = splitcont[2].substring(splitcont[2].indexOf(":") + 1);// 终端ID
			this.setOemCode(oemCode);
			// this.setPhnum(deviceId);
			String phnum = null;
			phnum = GBLTerminalList.getInstance().getSimcardNum(deviceId);

			if (phnum == null || hexString == null
					|| phnum.trim().length() == 0
					|| hexString.trim().length() == 0) {
				Log.getInstance().outLog("系统中没有适配到指定的终端：device_id=" + deviceId);
				return;
			}
			this.setDeviceSN(deviceId);
			this.setPhnum(phnum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String vertyCont = "";

		vertyCont = content.substring(4, content.lastIndexOf(" ") + 1);

		byte[] baow = vertyCont.getBytes(); // 校验和前内容

		int checksum = 0;
		String hexVerty = splitcont[splitcont.length - 1];

		String rep = ""; // 回应终端变量
		if (cmdtype.equals("N")) {
			// 090211024834|07DDF7F2|191B5C60|80000000|0|0|N|80000000|00040004|

			String loginmsg = splitcont[3]; // 登陆时的GPS信息
			String[] loginArra = loginmsg.split("\\|");
			StringBuffer sbuf = new StringBuffer();

			if (loginArra.length >= 9) {
				for (int k = 0; k < loginArra.length; k++) {
					if (k != 6) {
						sbuf.append(loginArra[k]);
						sbuf.append("|");
					}
				}
			} else {
				sbuf.append(loginmsg);
			}
			String temp = sbuf.toString();
			try {
				boolean oemFlag = setTerminalOemCode(this.getOemCode(), this
						.getDeviceSN());
				if (!oemFlag) {
					Log.getInstance().outLog(
							"设置" + this.getDeviceSN() + "的OEM码："
									+ this.getOemCode() + " 失败！");
					return;
				}
			} catch (Exception e) {
				
			}
			this.parsePosition(sbuf.toString()); // 解析位置信息
			String cmd = this.getOemCode() + ":" + this.getDeviceSN();
			rep = "C R " + cmd + "|N " + this.getVerfyCode(cmd + "|N ");
			this.setReplyByte(rep.getBytes());

			Log.getInstance().outLog(
					this.getDeviceSN() + "车机登陆时间：" + this.getTime()
							+ " 回复终端信息：" + rep);

		} else if (cmdtype.equals("T")) {

			String loginmsg = splitcont[3]; // 离线时的GPS信息
			String[] loginArra = loginmsg.split("\\|");
			StringBuffer sbuf = new StringBuffer();

			if (loginArra.length >= 9) {
				for (int k = 0; k < loginArra.length; k++) {
					if (k != 6) {
						sbuf.append(loginArra[k]);
						sbuf.append("|");
					}
				}
			} else {
				sbuf.append(loginmsg);
			}
			this.parsePosition(sbuf.toString()); // 解析位置信息
			GPRSThreadList.getInstance().removeUDP(this.getDeviceSN());
			Log.getInstance().outLog(
					this.getDeviceSN() + "车机离线,离线时间：" + this.getTime());

		} else if (cmdtype.equals("K")) {

			String cmd = this.getOemCode() + ":" + this.getDeviceSN() + "|K ";
			rep = "C R " + cmd + this.getVerfyCode(cmd);
			this.setReplyByte(rep.getBytes());

			//			
			Log.getInstance()
					.outLog(
							this.getDeviceSN() + "维持连接:" + hexString
									+ " 回应终端信息：" + rep);

		} else if (cmdtype.equals("P")) {

			this.parsePosition(splitcont[3]);

			Log.getInstance().outLog("定位报文：" + hexString);

		} else if (cmdtype.equals("F")) {

			try {

				this.parsePosition(splitcont[3]);
				// 回应0X302 :C M 1 4C54:1001|302|1;ff 4C1
				String cmd = this.getOemCode() + ":" + this.getDeviceSN()
						+ "|302|1;ff ";
				rep = "C M " + Tools.getRandomString(4) + " " + cmd
						+ this.getVerfyCode(cmd);
				this.setReplyByte1(rep.getBytes());

				Picture pic = new Picture();// .getInstance();
				pic.setReq(true);
				// pic.setDate(new Date());
				pic.setDeviceId(this.getDeviceSN());
				pic.setX(Float.parseFloat(this.getCoordX()));
				pic.setY(Float.parseFloat(this.getCoordY()));
				pic.setTimeStamp(this.getTimeStamp());
				// pic.setOemCode(this.getOemCode());
				pic.setImgStrCont("");
				PicCache.getInstance().addPicture(this.getDeviceSN(), pic);

				// PictureCache.getInstance().addPacks("0", pic);

				Log.getInstance().outJHSLoger("图片请求报文：" + hexString);
				Log.getInstance().outJHSLoger(
						"上传照片时的GPS信息：经度=" + this.getCoordX() + ",纬度="
								+ this.getCoordY() + ",时间=" + this.getTime());
			} catch (Exception e) {
				Log.getInstance().errorLog("解析图片请求异常", e);
			}

		} else if (cmdtype.equals("U")) {
			try {
				proRead = new PropertyReader("load.properties");
				isOverLoad = proRead.getProperty("isOverLoad");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.getInstance().errorLog("获取负载配置异常：",e);
			}
			String date = splitcont[3];
			String dates = date.substring(4, 6) + date.substring(2, 4)
					+ date.substring(0, 2);// ddMMyy
			String times = date.substring(6);// hhmmss
			String format_date = Tools.conformtime(times, dates);// 传输时间
			String trackNum = splitcont[4]; // 传输的通道
			String totalNum = splitcont[5]; // 图像总块数
			int tnum = Integer.parseInt(totalNum, 16);
			String orderNum = splitcont[6]; // 图像编号
			int onum = Integer.parseInt(orderNum, 16);
			// 回应终端，C M 1 4C54:1001|303|1;1 427
			String cmd = this.getOemCode() + ":" + this.getDeviceSN() + "|303|"
					+ trackNum + ";" + orderNum + " ";
			rep = "C M " + Tools.getRandomString(4) + " " + cmd
					+ this.getVerfyCode(cmd);
			this.setReplyByte1(rep.getBytes());

			String hexImg = null;

			try {
				String head = content.substring(0, content.indexOf("<") + 1);
				Log.getInstance().outJHSLoger("image head:" + head);

				String end = content.substring(content.length()
						- hexVerty.length() - 2);
				Log.getInstance().outJHSLoger("end:" + end);

				byte[] imageByte = new byte[bc.length - head.length()
						- end.length()];
				System.arraycopy(bc, head.getBytes().length, imageByte, 0,
						imageByte.length);
				hexImg = Tools.bytesToHexString(imageByte);

				Log.getInstance().outJHSLoger(
						"DDD服务收到原始数据字节数=" + (hex.length() / 2) + "原始数据报内容："
								+ hex);
				Log.getInstance().outJHSLoger(
						(onum) + " image bytes size:" + imageByte.length
								+ "cont:" + Tools.bytesToHexString(imageByte));
			} catch (Exception e) {

				Log.getInstance().errorLog(null, e);
				e.printStackTrace();
				hexImg = null;
			}
			boolean isHaveReq = false;

			Picture pic = PicCache.getInstance().getPicture(this.getDeviceSN());
			if (pic != null)
				isHaveReq = pic.isReq();

			Log.getInstance().outJHSLoger("本机是否存在A F请求包：" + isHaveReq);

			if (isHaveReq) {// 有图片位置时A F指令时

				// Picture pic = new Picture();
				if (pic.get(onum + "") == null) {
					pic.setDeviceId(this.getDeviceSN());
					pic.setPackcounts(tnum);
					pic.setPakcNo(onum);
					pic.setReq(isHaveReq);
					pic.setX(pic.getX());
					pic.setY(pic.getY());
					pic.setTimeStamp(pic.getTimeStamp());
					pic.setType("0");
//					pic.setDate(new Date());
//					pic.setOemCode(this.getOemCode());
					Log.getInstance().outJHSLoger(
							this.getDeviceSN() + "图片包基本信息:x=" + pic.getX()
									+ ",y=" + pic.getY() + ",time="
									+ pic.getTimeStamp());

					if (hexImg != null && onum > 0) {
						pic.addImgContHex(onum + "", hexImg);
						PicCache.getInstance().addPicture(this.getDeviceSN(),
								pic);
					}
					if (pic.isTraverOver()) {
						// 传输完毕
						Log.getInstance().outJHSLoger(
								this.getDeviceSN() + "图片传输完毕！");
						DBService service = new DBServiceImpl();
						try {
							boolean flag = service.insertPicInfo(pic);
						} catch (Exception e) {
							e.printStackTrace();
						}
						pic.reset();

						PicCache.getInstance()
								.removePicture(this.getDeviceSN());
					}
					Log.getInstance().outJHSLoger(
							this.getDeviceSN() + "图片传输通道：" + trackNum + ",总包数="
									+ tnum + ",当前包数=" + onum);
				} else {

					Log.getInstance().outJHSLoger(
							"设备" + this.getDeviceSN() + "缓存中已经存在第" + onum
									+ "包数据");
				}

			} else if (isOverLoad != null && isOverLoad.equals("1")) {// 负载转发

				String udpAddr = proRead.getProperty("overLoadUdpAddr");
				String host = udpAddr.split(":")[0];
				String sport = udpAddr.split(":")[1];
				int port = Integer.parseInt(sport);
				byte[] b = Tools.fromHexString(hex);

				OverLoadUtil.sendToUdp(host, port, b);

			}

			// Log.getInstance().outLog("图片数据报文：" + hexString);

		} else if (cmdtype.equals("M")) {
			// A M 7367:1001 060118121010|AGwAaQBuAGcAdAB1 XXX\r\n

			String MSG = splitcont[3].split("\\|")[1];
			try {
				byte[] msgB = Base641111.base64Decode(MSG);
				String msgCont = new String(msgB, "UTF-16");
				DbOperation dbopr = new DbOperation();
				dbopr.insertMessage(this, "1", msgCont);
				Log.getInstance().outLog("上传短信报文：" + msgCont);
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}

		} else if (cmdtype.equals("R")) {
			// A R 7367:13698607474 000000000000|444|00 89F
			String detail = splitcont[3];
			String[] tails = detail.split("\\|");

			String date = tails[0];
			String dates = date.substring(4, 6) + date.substring(2, 4)
					+ date.substring(0, 2);// ddMMyy
			String times = date.substring(6);// hhmmss
			String format_date = Tools.conformtime(times, dates);// 登陆日期yyyy-MM-dd
			// HH:mm:ss

			String hexSeq = tails[1];
			int seq = 0;
			try {
				seq = Integer.parseInt(hexSeq);
			} catch (Exception e) {
				// seq = Integer.parseInt(hexSeq, 16);
			}
			String status = null;
			if (tails[2].indexOf(";") != -1) {
				status = tails[2].substring(0, tails[2].indexOf(";"));
			} else {
				status = tails[2];
			}
			DbOperation dopt = new DbOperation();
			// 异常处理

			int state = 1;
			try {
				state = Integer.parseInt(status, 16);
			} catch (Exception e) {
				state = -1;
			}
			if (state == 0) {
				dopt.updateInstructionsState(this.getDeviceSN(), "0", seq);
				Log.getInstance().outLog(
						this.getDeviceSN() + "更新状态成功,日期：" + format_date + "id:"
								+ seq + " " + hexSeq);
			} else {
				dopt.updateInstructionsState(this.getDeviceSN(), "2", seq);
				Log.getInstance().outLog(
						this.getDeviceSN() + "更新状态失败,日期：" + format_date
								+ " 指令内容：" + hexString);
			}

		} else if (cmdtype.equals("D")) { 
			
		} else if (cmdtype.equals("X")) {

			String subtype = "";
			String[] splitCont = content.split(" ");
			subtype = splitCont[4];
			if (subtype.equalsIgnoreCase("E01")) {
				String ymd = Tools.formatDate2Str(new Date(), "yyyyMMdd");
				String hms = Tools.formatDate2Str(new Date(), "HHmmss");
				String date = this.setLedDate("1", oemCode, ymd, hms);
				this.setReplyByte(date.getBytes());
				Log.getInstance().outLog(this.getDeviceSN() + " 时间校对：" + date);
			} else if (subtype.equals("510")) {
				Log.getInstance().outLog("油耗数据报文：" + hexString);
				String oilmass = splitCont[5]; // 当前油耗量，单位0.1升
				String oilwear = splitCont[6]; // 加油量，单位0.1升
				float foilmass = Integer.parseInt(oilmass) * 0.1f;
				float foilwear = Integer.parseInt(oilwear) * 0.1f;
				Log.getInstance().outLog(
						this.getDeviceSN() + "当前油耗=" + foilmass + "升，本次加油量="
								+ foilwear);
				DBService service = new DBServiceImpl();
				//service.saveFuelRecord(this.getDeviceSN(), foilmass, foilwear);
			} else if (subtype.equalsIgnoreCase("E02")) {// 下发应答
				// A X 7868:13600030075 041231184539 E02 3E8,1, 0809
				String indexHex = splitCont[5].split(",")[0].toLowerCase();

				long index = Long.parseLong(indexHex, 16);
				String cmd = oemCode + ":" + this.getDeviceSN() + "|" + subtype
						+ " ";
				String repLed = "C R " + cmd + this.getVerfyCode(cmd);
				this.setReplyByte(repLed.getBytes());
				String cmdId = this.getDeviceSN() + "|" + subtype + "|"
						+ indexHex;
				String cmdP = this.getDeviceSN() + "|E04|" + indexHex;

				Log.getInstance().outLog(
						"LED中心应答：" + repLed + ",cmdId=" + cmdId);

				String state = splitCont[5].split(",")[1];
				if (splitCont[5].equalsIgnoreCase("3,4,")) {
					DBService service = new DBServiceImpl();
					service.updateInstructionsState(deviceId, "0",
							"13600030075|E06|");

					// service.updateInstructionsState(deviceId, "0", cmdP);

					Log.getInstance().outLog(
							this.getDeviceSN() + " LED亮度设置成功，指令为：" + content);

				} else if (state.equals("1")) {
					DBService service = new DBServiceImpl();
					service.updateInstructionsState(deviceId, "0", cmdId);

					service.updateInstructionsState(deviceId, "0", cmdP);

					Log.getInstance().outLog(
							this.getDeviceSN() + " LED更新状态成功,短信INDEX=" + index
									+ ",指令为：" + content);
				} else {
//					DBService service = new DBServiceImpl();
//					ArrayList list = service.getLedInstructions(this.getDeviceSN());
//					CommonGatewayServiceImpl gater = new CommonGatewayServiceImpl();
//					
//					for (int i=0; i<list.size(); i++){
//						InstructionBean bean = (InstructionBean)list.get(i);
//						String ledcmd = bean.getInstuction();
//						if (ledcmd != null){
//							try{
//							gater.sendDataToTerminal(this.getDeviceSN(),ledcmd.getBytes());
//							}catch(Exception e){
//								continue;
//							}
//						}
//					}
//					service.updateInstructionsState(deviceId, "2", cmdId);
//					service.updateInstructionsState(deviceId, "2", cmdP);
//					Log.getInstance().outLog(
//							this.getDeviceSN() + " LED更新状态失败,短信INDEX=" + index
//									+ ",指令为：" + content);
				}

			} else if (subtype.equalsIgnoreCase("E03")) {// 删除应答
				// A X 7868:13600030075 041231184539 E02 3E8,1, 0809
				String indexHex = splitCont[5].split(",")[0].toLowerCase();
				long index = Long.parseLong(indexHex, 16);
				String cmd = oemCode + ":" + this.getDeviceSN() + "|" + subtype
						+ " ";
				String repLed = "C R " + cmd + this.getVerfyCode(cmd);
				this.setReplyByte(repLed.getBytes());
				String cmdId = this.getDeviceSN() + "|" + subtype + "|"
						+ indexHex;
				Log.getInstance().outLog(
						"LED中心应答：" + repLed + ",cmdId=" + cmdId);

				String cmdP = this.getDeviceSN() + "|E04|" + indexHex;
				String state = splitCont[5].split(",")[1];
				if (state.equals("1")) {
					DBService service = new DBServiceImpl();
					service.updateInstructionsState(deviceId, "0", cmdId);
					service.updateInstructionsState(deviceId, "1", cmdP);
					Log.getInstance().outLog(
							this.getDeviceSN() + " LED信息删除更新状态成功,短信INDEX="
									+ index + ",指令为：" + content);
				} else {
					DBService service = new DBServiceImpl();
					service.updateInstructionsState(deviceId, "2", cmdId);
					service.updateInstructionsState(deviceId, "2", cmdP);
					Log.getInstance().outLog(
							this.getDeviceSN() + " LED信息删除更新状态失败,短信INDEX="
									+ index + ",指令为：" + content);
				}

			}
		}
		/**
		 * else if (cmdtype.equals("S")) {// LBS补偿 this.setLocateType("0");
		 * String flag = splitcont[3];
		 * 
		 * if (flag != null && flag.trim().equals("1")) { LBSRequestService lbs =
		 * LBSRequestService.getInstance(); Position position =
		 * lbs.getPositionByCellid(this.getDeviceSN()); if (position != null) {
		 * this.setCoordX(position.getCoordX() + "");
		 * this.setCoordY(position.getCoordY() + ""); this.sentPost(true);
		 * Log.getInstance().outLog( this.getDeviceSN() + " LBS信息：" +
		 * position.toString()); } } }
		 */
		bc = null;
	}

	public String setLedDate(String seq, String omeCode, String yyyyMMdd,
			String HHmmss) {
		String ret = "";
		String cmd = "";

		String head = "C M " + seq + " ";

		String date = yyyyMMdd + ";" + HHmmss;

		cmd = omeCode + ":" + this.getDeviceSN() + "|E05|" + date + " ";

		String vcode = Tools.getVerfyCode(cmd);
		ret = head + cmd + vcode;
		return ret;
	}

	// 解析位置信息
	private void parsePosition(String ps) {
		// 090205102340|08942A90|18F48828|80000000|0|0|A|00000000|00040004|
		// 090205120454|0894311A|18F48252|80000000|0|0|
		// 00000001,00000000|00040004,0000415F| 15A9

		String[] split = ps.split("\\|");
		String date = split[0].substring(0, 6);
		String dates = date.substring(4, 6) + date.substring(2, 4)
				+ date.substring(0, 2);// ddMMyy
		String times = split[0].substring(6);// hhmmss

		String format_date = Tools.conformtime(times, dates);// 登陆日期yyyy-MM-dd
		// HH:mm:ss
		 Date gpsdate = formatStrToDate(format_date, "yyyy-MM-dd HH:mm:ss");
		 Timestamp ts = null;
		 ts = new Timestamp(gpsdate.getTime());
		 this.setTimeStamp(ts);
//		Timestamp ts = new Timestamp(new Date().getTime());
//		this.setTimeStamp(ts);

		this.setTime(format_date);

		String y = fromMs2XY(this.removeZeroStr(split[1])); // 纬度
		this.setCoordY(y);
		String x = fromMs2XY(this.removeZeroStr(split[2])); // 经度
		this.setCoordX(x);
		if (!split[3].equals("80000000") && !split[3].equals("ffffffff")) {
			String h = "";
			try {
				h = Integer.parseInt(this.removeZeroStr(split[3]), 16) + "";// 高度，单位米
			} catch (Exception e) {
				e.printStackTrace();
				h = "0";
			}
			this.setAltitude(h);
		} else {
			this.setAltitude("0.0");
		}

		String v = Integer.parseInt(this.removeZeroStr(split[4]), 16) + "";// 方向,单位度
		this.setDirection(v);
		String s = Integer.parseInt(this.removeZeroStr(split[5]), 16) / 1000.0
				+ ""; // 速度，单位KM/H
		this.setSpeed(s);
		String alarmStaus = split[6];// 报警状态字
		String[] firstAlarm = alarmStaus.split(",");
		String alarmHex = firstAlarm[0];
		String alarm_type = null;

		try {
			alarm_type = this.parseAlarm(alarmHex);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}// this.parseAlarmStatus(alarmHex);
		if (firstAlarm.length > 1) {
			String extendAlarm = firstAlarm[1];

		}

		String oemStatus = split[7]; // 扩展状态字
		String[] oemSplit = oemStatus.split(",");
		String oem_type = oemSplit[0];
		String lineAlarm = null;
		try {
			lineAlarm = parseOemStatusNew(oem_type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (oemSplit.length > 1) {
			String distance = Integer.parseInt(this.removeZeroStr(oemSplit[1]),
					16)
					+ ""; // 里程，单位KM
			this.setLC(distance);
		}
		if (oemSplit.length > 2) {
			String tpra = oemSplit[2];
			String tempera = null;

			if (tpra.charAt(0) == 'F' || tpra.charAt(0) == 'f') {// 温度，单位0.01摄氏度

				int intTempera = Tools.getValueFromCompCode(tpra);
				tempera = intTempera * 0.01f + "";

			} else {// 温度，单位0.01摄氏度

				tempera = Integer.parseInt(this.removeZeroStr(tpra), 16)
						* 0.01f + "";
			}
			this.setTemperature(tempera);
			Log.getInstance().outLog(this.getDeviceSN() + "当前温度值：" + tempera);
		}
		if (this.getLocateStatus() != null
				&& this.getLocateStatus().equals("0")) {
			this.sentPost(false);

		} else {
			this.sentPost(true);

		}

	}

	// 去除字符串前的0
	private String removeZeroStr(String str) {
		String ret = null;
		if (str != null && str.trim() != "") {
			int i = 0;
			while (i < str.length()) {
				if (str.charAt(i) != '0') {
					break;
				}
				i++;
			}
			if (i != str.length())
				ret = str.substring(i);
			else
				ret = "0";
		}

		return ret;
	}

	// 把毫秒转换成经纬度
	private String fromMs2XY(String xy) {
		String ret = "";
		try {
			double ms = Integer.parseInt(xy, 16);
			double ds = ms / 1000 / 60 / 60;

			DecimalFormat format = new DecimalFormat("0.000000");
			format.setMaximumFractionDigits(6);
			ret = format.format(ds);
		} catch (Exception e) {
			ret = "0";
		}
		return ret;
	}

	private Date formatStrToDate(String date, String format) {
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			d = sdf.parse(date);
		} catch (ParseException ex) {
			Log.getInstance().errorLog(ex.getMessage(), ex);
			return null;
		}
		return d;
	}

	// 解析OEM 状态
	private String parseOemStatusNew(String hex) throws Exception {
		String ret = "";
		while (hex.length() < 8) {
			hex = "0" + hex;
		}
		String bit1 = "0" + String.valueOf(hex.charAt(hex.length() - 1));
		byte b = Tools.fromHexString(bit1)[0];
		DBService dbservice = new DBServiceImpl();
		if (this.getByteBit(b, 0) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "重车！");
		} else {
			Log.getInstance().outLog(this.getDeviceSN() + "空车！");
		}

		if (this.getByteBit(b, 1) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "罐车正转！");
		} else {
			Log.getInstance().outLog(this.getDeviceSN() + "罐车反转报警！");
			// this.setAlarmType("27");
			// AlarmQueue.getInstance().addAlarm(this);
		}
		if (this.getByteBit(b, 2) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "车辆点火！");
			this.setAccStatus("1");
		} else {
			this.setAccStatus("0");
		}
		if (this.getByteBit(b, 3) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "前门开！");
		}

		String bit2 = String.valueOf(hex.charAt(hex.length() - 2));
		byte b2 = Byte.parseByte(bit2, 16);
		if (this.getByteBit(b2, 0) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "后门开！");
		}
		if (this.getByteBit(b2, 1) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "发生了主电源破坏报警！");
			this.setAlarmType("14");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		if (this.getByteBit(b2, 2) == 1) {
			this.setAlarmType("15");
			AlarmQueue.getInstance().addAlarm(this.clone());
			Log.getInstance().outLog(this.getDeviceSN() + "发生了GPS天线开路报警！");
		}
		if (this.getByteBit(b2, 3) == 1) {
			this.setAlarmType("16");
			AlarmQueue.getInstance().addAlarm(this);
			Log.getInstance().outLog(this.getDeviceSN() + "发生了GPS天线短路报警！");
		}

		String bit3 = String.valueOf(hex.charAt(hex.length() - 3));
		byte b3 = Byte.parseByte(bit3, 16);
		if (this.getByteBit(b3, 0) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "发生了断电报警！");
			this.setAlarmType("5");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		if (this.getByteBit(b3, 1) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "发生了欠压报警！");
			this.setAlarmType("17");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		if (this.getByteBit(b3, 2) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "GPS接收机故障！");
			this.setAlarmType("28");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		if (this.getByteBit(b3, 3) == 1) {
			this.setAlarmType("18");
			AlarmQueue.getInstance().addAlarm(this.clone());
			Log.getInstance().outLog(this.getDeviceSN() + "发生了超声波报警！");
		}

		String bit4 = String.valueOf(hex.charAt(hex.length() - 4));
		byte b4 = Byte.parseByte(bit4, 16);
		if (this.getByteBit(b4, 0) == 1) {
			this.setAlarmType("19");
			AlarmQueue.getInstance().addAlarm(this.clone());
			Log.getInstance().outLog(this.getDeviceSN() + "发生了拖吊报警！");
		}
		if (this.getByteBit(b4, 1) == 1) {
			this.setAlarmType("20");
			AlarmQueue.getInstance().addAlarm(this.clone());
			Log.getInstance().outLog(this.getDeviceSN() + "发生了熄火报警！");
		}
		if (this.getByteBit(b4, 2) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "发生了偏航报警！");
			try {
				// dbservice.saveLineAlarm(this);
				this.setAlarmType("6");
				AlarmQueue.getInstance().addAlarm(this.clone());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.getInstance().errorLog("保存偏航报警异常", e);
			}
		}
		if (this.getByteBit(b4, 3) == 1) {

		}

		return ret;

	}

	// 解析报警状态字
	private String parseAlarm(String hex) throws Exception {
		String ret = "";
		while (hex.length() < 8) {
			hex = "0" + hex;
		}
		String bit1 = "0" + String.valueOf(hex.charAt(hex.length() - 1));
		byte b = Tools.fromHexString(bit1)[0];
		DBService dbService = new DBServiceImpl();

		if (this.getByteBit(b, 0) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "发生了主动报警！");
			try {
				// dbService.saveActiveAlarm(this);
				this.setAlarmType("3");
				AlarmQueue.getInstance().addAlarm(this.clone());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.getInstance().errorLog("保存主动报警异常", e);
			}
		}
		if (this.getByteBit(b, 1) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "发生了超速报警！");
			try {
				// dbService.saveSpeedAlarm(this);
				this.setAlarmType("1");
				AlarmQueue.getInstance().addAlarm(this.clone());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.getInstance().errorLog("保存超速报警异常", e);
			}

		}
		if (this.getByteBit(b, 2) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "发生了区域报警！");
			try {
				// dbService.saveAreaAlarm(this);
				this.setAlarmType("2");
				AlarmQueue.getInstance().addAlarm(this.clone());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.getInstance().errorLog("保存区域报警异常", e);
			}
		}
		if (this.getByteBit(b, 3) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "发生了设备故障报警！");
		}

		String bit2 = String.valueOf(hex.charAt(hex.length() - 2));
		byte b2 = Byte.parseByte(bit2, 16);
		if (this.getByteBit(b2, 0) == 1) {
			this.setAlarmType("8");
			AlarmQueue.getInstance().addAlarm(this.clone());
			Log.getInstance().outLog(this.getDeviceSN() + "发生了求助报警！");
		} else if (this.getByteBit(b2, 1) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "区域内超速报警！");
		} else if (this.getByteBit(b2, 2) == 1) {
			this.setAlarmType("13");
			AlarmQueue.getInstance().addAlarm(this.clone());
			Log.getInstance().outLog(this.getDeviceSN() + "超时停车报警！");
		} else if (this.getByteBit(b2, 3) == 1) {

		}

		String bit3 = String.valueOf(hex.charAt(hex.length() - 3));
		byte b3 = Byte.parseByte(bit3, 16);
		if (this.getByteBit(b3, 0) == 1) {

		} else if (this.getByteBit(b3, 1) == 1) {

		} else if (this.getByteBit(b3, 2) == 1) {

		} else if (this.getByteBit(b3, 3) == 1) {

		}

		String bit4 = String.valueOf(hex.charAt(hex.length() - 4));
		byte b4 = Byte.parseByte(bit4, 16);
		if (this.getByteBit(b4, 0) == 1) {

		} else if (this.getByteBit(b4, 1) == 1) {

		} else if (this.getByteBit(b4, 2) == 1) {

		} else if (this.getByteBit(b4, 3) == 1) {

		}

		String bit5 = String.valueOf(hex.charAt(hex.length() - 5));
		byte b5 = Byte.parseByte(bit5, 16);
		if (this.getByteBit(b5, 0) == 1) {

		} else if (this.getByteBit(b5, 1) == 1) {
			this.setAlarmType("9");
			AlarmQueue.getInstance().addAlarm(this.clone());
			Log.getInstance().outLog(this.getDeviceSN() + "发生防盗报警！");
		} else if (this.getByteBit(b5, 2) == 1) {

		} else if (this.getByteBit(b5, 3) == 1) {

		}

		String bit6 = String.valueOf(hex.charAt(hex.length() - 6));
		byte b6 = Byte.parseByte(bit6, 16);
		if (this.getByteBit(b6, 0) == 1) {

		} else if (this.getByteBit(b6, 1) == 1) {

		} else if (this.getByteBit(b6, 2) == 1) {

		} else if (this.getByteBit(b6, 3) == 1) {

		}

		String bit7 = String.valueOf(hex.charAt(hex.length() - 7));
		byte b7 = Byte.parseByte(bit7, 16);
		if (this.getByteBit(b7, 0) == 1) {
			this.setAlarmType("10");
			AlarmQueue.getInstance().addAlarm(this.clone());
			Log.getInstance().outLog(this.getDeviceSN() + "发生超时驾驶报警！");
		} else if (this.getByteBit(b7, 1) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "发生了入界报警！");
			try {
				// dbService.saveAreaAlarm(this);
				this.setAlarmType("2");
				AlarmQueue.getInstance().addAlarm(this.clone());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.getInstance().errorLog("保存入区域报警异常", e);
			}
		} else if (this.getByteBit(b7, 2) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "发生了出界报警！");
			try {
				// dbService.saveAreaAlarm(this);
				this.setAlarmType("2");
				AlarmQueue.getInstance().addAlarm(this.clone());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.getInstance().errorLog("保存出区域报警异常", e);
			}
		} else if (this.getByteBit(b7, 3) == 1) {
			this.setAlarmType("11");
			AlarmQueue.getInstance().addAlarm(this.clone());
			Log.getInstance().outLog(this.getDeviceSN() + "发生了温度报警！");
		}

		String bit8 = String.valueOf(hex.charAt(hex.length() - 8));
		byte b8 = Byte.parseByte(bit8, 16);
		if (this.getByteBit(b8, 0) == 1) {
			this.setAlarmType("12");
			AlarmQueue.getInstance().addAlarm(this.clone());
			Log.getInstance().outLog(this.getDeviceSN() + "发生了碰撞报警！");
		} else if (this.getByteBit(b8, 1) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "油箱开盖！");
		} else if (this.getByteBit(b8, 2) == 1) {
			Log.getInstance().outLog(this.getDeviceSN() + "货箱开盖！");
		} else if (this.getByteBit(b8, 3) == 1) {// 与未定位状态标志相同了
			Log.getInstance().outLog(this.getDeviceSN() + " 未定位状态！");
			this.setLocateStatus("0");
			// this.setAlarmType("29");
			// AlarmQueue.getInstance().addAlarm(this);
			// Log.getInstance().outLog(this.getDeviceSN() + "发生了胎压报警！");
		}

		return ret;
	}

	public static int getByteBit(byte data, double pos) {
		int bitData = 0;

		byte compare = (byte) Math.pow(2.0, pos);

		byte b = (byte) (data & compare);
		if ((data & compare) == compare) {
			bitData = 1;
		}
		return bitData;
	}

	// 求校验和，并转换为16进制
	private String getVerfyCode(String cont) {
		String ret = "";
		byte[] br = cont.getBytes();
		int sum = 0;
		for (int i = 0; i < br.length; i++) {
			sum += br[i] & 0xFF;
		}
		ret = Integer.toHexString(sum) + "\r\n";

		return ret;
	}

	/**
	 * 设置终端OEM码
	 * 
	 * @param code
	 */
	private synchronized boolean setTerminalOemCode(String code, String deviceId) {
		Connection conn = null;
		CallableStatement pst = null;
		boolean flag = false;
		// 数据库存储过程要去掉OEMCODE IS NOT NULL
		String sql = "{call PROC_UPDATE_TERM_OEMCODE(?,?)}";

		try {
			conn = DbOperation.getConnection();

			pst = conn.prepareCall(sql);
			pst.setString(1, code);
			pst.setString(2, deviceId);
			pst.execute();
			conn.commit();
			flag = true;
		} catch (SQLException e) {
			Log.getInstance().outLog("设置终端OEM码异常：" + e.getMessage());
			e.printStackTrace();

			flag = false;
		} finally {

			try {
				if (pst != null)
					pst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DbOperation.release(null, null, null, conn);
		}
		return flag;

	}

	@Override
	public void parseSMS(String phnum, String content) {
		// TODO 自动生成方法存根

	}

	public static void main(String[] args) {

		// A F 7868:13698607474
		// 100421074316|054112FE|195BF94C|57|0|0|00000000,00000000|000C000C,00000000,000009EC|0
		// 16C1

		// +
		// "091215112011|07DE2906|1918636E|80000000|0103|0|0000000f,00000000|00040004,00003ADD|
		// 167A\r\n";
		String s = "A P 7367:13705310079 100804054232|7DF46A2|1920BAAE|10|FA|0|1,0|12,C98E1| 1164\r\n";
		String hex = Tools.bytesToHexString(s.getBytes());
		
		ParseLingTu lt = new ParseLingTu();

		lt
				.parseGPRS("41204E20595354463A3133393233343536303436203130303831303035353034307C344437333731417C31383736373430307C307C36457C307C30303030303030307C30303034303030342C302C307C20313146410D0A");

	}

}
