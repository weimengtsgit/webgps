/**
 * 
 */
package com.autonavi.directl.parse;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.bean.UdpPhoto;
import com.autonavi.directl.dbutil.service.DBService;
import com.autonavi.directl.dbutil.service.DBServiceImpl;
import com.autonavi.lbsgateway.GBLTerminalList;
import com.autonavi.lbsgateway.alarmpool.AlarmQueue;
import com.autonavi.lbsgateway.service.udpphoto.UdpPhotoSave;
import com.sos.sosgps.api.CoordAPI;
import com.sos.sosgps.api.DPoint;


/**
 * @author shiguang.zhou
 * 
 */
public class ParseShouHang extends ParseBase {
	private boolean isHaveTmpr;
	private boolean isHaveOil;
	String deviceid = null;
    CoordAPI api = new CoordAPI();

	public void parsePhoto(String hexdata){
		byte[] dataByte = Tools.fromHexString(hexdata);

		byte[] fh = new byte[2];//帧头2字节
		System.arraycopy(dataByte, 0, fh, 0, fh.length);
		byte[] version = new byte[1];//版本1字节
		System.arraycopy(dataByte, 2, version, 0, version.length);
		byte[] route = new byte[2];//路由
		System.arraycopy(dataByte, 3, route, 0, route.length);
		byte[] deviceId = new byte[8];//终端ID
		System.arraycopy(dataByte, 5, deviceId, 0, deviceId.length);
		byte businessType = dataByte[13];//业务类型
		byte dataType = dataByte[14];//数据类型
		byte[] dataLength = new byte[2];//数据长度
		System.arraycopy(dataByte, 15, dataLength, 0, dataLength.length);
		
		byte[] photoNumByte = new byte[2];//图片编号
		System.arraycopy(dataByte, 17, photoNumByte, 0, photoNumByte.length);
		byte[] photoPacketsByte = new byte[2];//图片总包数
		System.arraycopy(dataByte, 19, photoPacketsByte, 0, photoPacketsByte.length);
		byte[] currentPhotoPacketByte = new byte[2];//当前包号
		System.arraycopy(dataByte, 21, currentPhotoPacketByte, 0, currentPhotoPacketByte.length);
		byte[] dataSizeByte = new byte[2];//数据长度
		System.arraycopy(dataByte, 23, dataSizeByte, 0, dataSizeByte.length);
		byte[] data = new byte[Tools.byte2Int(dataSizeByte)];//数据内容
		System.arraycopy(dataByte, 25, data, 0, data.length);
		Log.getInstance().outLog("parse photo data: deviceId = " + Tools.bytesToHexString(deviceId) + ", photoNum = " + Tools.bytesToHexString(photoNumByte) + ", photoPackets = " + Tools.bytesToHexString(photoPacketsByte));
		
		String photoNum = Tools.byte2Int(photoNumByte)+"";
		int photoPackets = Tools.byte2Int(photoPacketsByte);
		int currentPhotoPacket = Tools.byte2Int(currentPhotoPacketByte);
		int dataSize = Tools.byte2Int(dataSizeByte);
		UdpPhoto shouHangPhoto = new UdpPhoto();
		shouHangPhoto.setPhotoNum(photoNum);
		shouHangPhoto.setPhotoPackets(photoPackets);
		shouHangPhoto.setCurrentPhotoPacket(currentPhotoPacket);
		shouHangPhoto.setDataSize(dataSize);
		shouHangPhoto.setData(data);
		shouHangPhoto.setDeviceId(deviceid);
		String key = deviceid+UdpPhotoSave.HTKEY_SEPARATOR+photoNum;
		ArrayList<UdpPhoto> al = UdpPhotoSave.updPhotoHt.get(key) == null ? null : (ArrayList<UdpPhoto>)UdpPhotoSave.updPhotoHt.get(key);
		if(al == null){
			al = new ArrayList<UdpPhoto>();
		}
		al.add(shouHangPhoto);
		UdpPhotoSave.updPhotoHt.put(key, al);
		Log.getInstance().outLog(key + " photo num: " + al.size());
    }
    
	@Override
	public void parseGPRS(String hexString) {
		String cmdSec = "";
		Log.getInstance().outLog("接收到SH原始数据：" + hexString);

		byte[] cont = Tools.fromHexString(hexString);
		String scont = new String(cont);
		byte vercode = cont[cont.length - 2];// 上传的校验码
		String hexvd = Tools.bytesToHexString(new byte[] { vercode });

		byte[] verdata = new byte[cont.length - 4];// 待计算校验的内容
		System.arraycopy(cont, 2, verdata, 0, verdata.length);

		byte ver = verdata[0];// 版本
		String hexVer = Tools.bytesToHexString(new byte[] { ver });

		// 路由信息
		byte[] route = new byte[] { verdata[1], verdata[2] };
		String hexRoute = Tools.bytesToHexString(route);
		// 设备ID
		byte[] bdevid = new byte[8];
		System.arraycopy(verdata, 3, bdevid, 0, 8);
		deviceid = Tools.bytesToHexString(bdevid);
		this.setDeviceSN(this.getDeviceId(deviceid));
		String phnum = null;
		phnum = GBLTerminalList.getInstance().getSimcardNum(this.getDeviceSN());
		//phnum  = "1106163082101655";
		
		if (phnum == null || hexString == null || phnum.trim().length() == 0
				|| hexString.trim().length() == 0) {
			Log.getInstance().outLog(
					"系统中没有适配到指定的终端：device_id=" + this.getDeviceSN());
			return;
		}

		this.setPhnum(phnum);

		// 业务类型
		byte businessType = verdata[11];
		String hextype = Tools.bytesToHexString(new byte[] { businessType });
		if (hextype.length() < 2)
			hextype = "0" + hextype;

		String bstr = Integer.toBinaryString(Integer.parseInt(hextype.charAt(0)
				+ ""));
		while (bstr.length() < 4) {
			bstr = "0" + bstr;
		}
		char cmdtype = bstr.charAt(0);
		char ctltype = bstr.charAt(1);

		String busyType = Integer.toBinaryString(Integer.parseInt(hextype
				.charAt(1)
				+ ""));
		while (busyType.length() < 4) {
			busyType = "0" + busyType;
		}
		int btype = Integer.parseInt("0000" + busyType, 2);

		String temp = "";
		boolean isNeedReply = false;
		if (cmdtype == '0') {
			temp += " 主帧";
		} else {
			temp += " 应答帧";
		}
		if (ctltype == '0' && cmdtype != '1') {
			temp += " 接收方需要应答";
			isNeedReply = true;
		} else {
			isNeedReply = false;
			temp += " 接收方不需要应答";
		}

		// 数据类型
		byte dataType = verdata[12];
		String hexDataType = Tools.bytesToHexString(new byte[] { dataType });
		// 数据长度
		byte[] lenbyte = new byte[] { verdata[13], verdata[14] };
		int len = Tools.byte2Int(lenbyte);
		// 数据
		byte[] data = new byte[verdata.length - 15];
		System.arraycopy(verdata, 15, data, 0, data.length);
		String hexdata = "";

		switch (btype) {
		case (byte) 0x00:
			Log.getInstance().outLog(this.getDeviceSN() + " 链路控制。");
			if (dataType == (byte) 0x81) {
				hexdata = "01";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 终端登录：" + hexString);

			} else if (dataType == (byte) 0x82) {
				int heartInterval = Tools.byte2Int(data);
				Log.getInstance().outLog(
						this.getDeviceSN() + " 心跳间隔：" + heartInterval);

				hexdata = "01";

			}
			break;
		case (byte) 0x01:

			if (dataType == (byte) 0x81) {
				int count = Tools.byte2Int(new byte[] { data[0] });
				byte[] gpsdata = new byte[data.length - 1];
				System.arraycopy(data, 1, gpsdata, 0, gpsdata.length);
				Log.getInstance().outLog(
						this.getDeviceSN() + " 定时监控,点个数：" + count);
				this.parsePosition(gpsdata);
			} else if (dataType == (byte) 0x82) {
				int count = Tools.byte2Int(new byte[] { data[0] });
				byte[] gpsdata = new byte[data.length - 1];
				System.arraycopy(data, 1, gpsdata, 0, gpsdata.length);
				Log.getInstance().outLog(
						this.getDeviceSN() + " 报警数据,点个数：" + count);
				hexdata = "01";
				byte[] repb = this.crtCmdByte(ver, route, bdevid, businessType,
						(byte) 0x82, hexdata);
				this.setReplyByte(repb);
				this.parsePosition(gpsdata);
			} else if (dataType == (byte) 0x83) {
				int count = Tools.byte2Int(new byte[] { data[0] });
				byte[] gpsdata = new byte[data.length - 1];
				System.arraycopy(data, 1, gpsdata, 0, gpsdata.length);
				Log.getInstance().outLog(
						this.getDeviceSN() + " 主动上传数据,点个数：" + count);
				this.parsePosition(gpsdata);
			} else if (dataType == (byte) 0x84) {
				byte[] repb = this.crtCmdByte(ver, route, bdevid, businessType,
						(byte) 0x84, hexdata);
				this.setReplyByte(repb);
				
				int count = Tools.byte2Int(new byte[] { data[0] });
				byte[] gpsdata = new byte[data.length - 1];
				System.arraycopy(data, 1, gpsdata, 0, gpsdata.length);
				Log.getInstance().outLog(
						this.getDeviceSN() + " 盲区补偿数据,点个数：" + count);

				int datalen = gpsdata.length / count;

				for (int m = 0; m < count; m++) {
					byte[] tmpgpsdata = new byte[datalen];
					System.arraycopy(gpsdata, m * datalen, tmpgpsdata, 0,
							datalen);

					this.parsePosition(tmpgpsdata);
					
					if (this.isIsPost())
						this.getParseList().add(this);
				}
			} else if (dataType == (byte) 0x90) {
				byte[] gpsdata = new byte[data.length];
				System.arraycopy(data, 0, gpsdata, 0, gpsdata.length);

				this.parsePosition(gpsdata);

				Log.getInstance().outLog(
						this.getDeviceSN() + " 查车位置坐标，x=" + this.getCoordX()
								+ ",y=" + this.getCoordY());

				try {
					String addr = this.locateDesc();
					if (addr != null) {
						byte[] bMsg = addr.getBytes("UTF-16");
						byte[] addrb = new byte[bMsg.length - 2];
						System.arraycopy(bMsg, 2, addrb, 0, addrb.length);
						hexdata = Tools.bytesToHexString(addrb);

						byte[] repb = this.crtCmdByte(ver, route, bdevid,
								businessType, (byte) 0x10, hexdata);
						this.setReplyByte(repb);
						Log.getInstance().outLog(
								this.getDeviceSN() + " 查车位置描述为：" + addr
										+ ",回复指令："
										+ Tools.bytesToHexString(repb));

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (dataType == (byte) 0x01) {
				byte[] gpsdata = new byte[data.length - 1];
				System.arraycopy(data, 1, gpsdata, 0, gpsdata.length);
				this.parsePosition(gpsdata);
				cmdSec = deviceid + "0101";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 点名监控应答" + hexString);
			} else if (dataType == (byte) 0x02) {
				cmdSec = deviceid + "0102";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 定时监控应答" + hexString);
			} else if (dataType == (byte) 0x03) {
				cmdSec = deviceid + "0103";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 定距离应答" + hexString);
			} else if (dataType == (byte) 0x04) {
				cmdSec = deviceid + "0104";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 停止监控应答" + hexString);
			} else if (dataType == (byte) 0x05) {
				cmdSec = deviceid + "0105";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 自动上传应答" + hexString);
			} else if (dataType == (byte) 0x06) {
				cmdSec = deviceid + "0106";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 撤销报警应答" + hexString);
			}

			break;

		case (byte) 0x02:
			Log.getInstance().outLog(this.getDeviceSN() + " 管理业务。");
			if (dataType == (byte) 0x01) {
				cmdSec = deviceid + "0201";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 设置本机号码应答" + hexString);
			} else if (dataType == (byte) 0x02) {
				cmdSec = deviceid + "0202";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 设置中心号码应答" + hexString);
			} else if (dataType == (byte) 0x03) {
				cmdSec = deviceid + "0203";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 设置报警号码应答" + hexString);
			} else if (dataType == (byte) 0x09) {
				cmdSec = deviceid + "0209";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 设置超速应答" + hexString);
			} else if (dataType == (byte) 0x05) {
				cmdSec = deviceid + "0205";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 设置连接参数应答" + hexString);
			} else if (dataType == (byte) 0x0a) {
				cmdSec = deviceid + "020a";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 区域围栏设置应答" + hexString);
			}

			break;
		case (byte) 0x03:
			Log.getInstance().outLog(this.getDeviceSN() + " 远程控制业务。");
			if (dataType == (byte) 0x01) {
				cmdSec = deviceid + "0301";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 远程复位应答" + hexString);
			} else if (dataType == (byte) 0x02) {
				cmdSec = deviceid + "0302";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 恢复默认参数应答" + hexString);
			} else if (dataType == (byte) 0x03) {
				cmdSec = deviceid + "0303";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 设置监听应答" + hexString);
			} else if (dataType == (byte) 0x04) {
				cmdSec = deviceid + "0304";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 设置油电控制应答" + hexString);
			} else if (dataType == (byte) 0x05) {
				cmdSec = deviceid + "0305";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 设置车门控制应答" + hexString);
			} else if (dataType == (byte) 0x0a) {
				cmdSec = deviceid + "020a";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 区域围栏设置应答" + hexString);
			}
			break;

		case (byte) 0x06://业务数据
			Log.getInstance().outLog(this.getDeviceSN() + " 数据业务。");
			/*if (dataType == (byte) 0x03) {//抓拍图像
				cmdSec = deviceid + "0603";
				Log.getInstance().outLog(
						this.getDeviceSN() + " 抓拍图像 " + hexString);
			}else*/ if (dataType == (byte) 0x84) {//传输图片
				cmdSec = deviceid + "0603";
				parsePhoto(hexString);
				Log.getInstance().outLog(
						this.getDeviceSN() + " 上传图片 " + hexString);
			}
			break;
		}

		byte bvercode = this.getVerfyCode(verdata);
		String hexcode = Tools.bytesToHexString(new byte[] { bvercode });

		if (isNeedReply) {
			byte[] repb = this.crtCmdByte(ver, route, bdevid, businessType,
					dataType, hexdata);
			this.setReplyByte(repb);
			Log.getInstance()
					.outLog(
							this.getDeviceSN() + " 应答帧："
									+ Tools.bytesToHexString(repb));
		}

		if (cmdSec != "") {
			DBService service = new DBServiceImpl();
			service.updateInstructionsState(this.getDeviceSN(), "0", cmdSec);
		}
		Log.getInstance().outLog(
				this.getDeviceSN() + " 版本：" + ver + ",路由：" + hexRoute
						+ ",业务类型：" + temp + ",数据类型：" + hexDataType + ",上传校验码:"
						+ hexvd + ",计算检验码：" + hexcode + ",数据区长度：" + len
						+ ",数据内容：" + Tools.bytesToHexString(data));

	}

	private String locateDesc() throws Exception {
		byte[] addrb = null;
		String addr = "";
		DPoint[] point = null;
		try {
			point = api.encryptConvert(new double[] { Double.parseDouble(this
					.getCoordX()) }, new double[] { Double.parseDouble(this
					.getCoordY()) });
			addr = CoordAPI.getAddress(point[0].encryptX, point[0].encryptY);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addr;
	}

	private void parsePosition(byte[] gpsdata) {

		byte[] date = new byte[3];
		System.arraycopy(gpsdata, 0, date, 0, 3);

		byte[] time = new byte[3];
		System.arraycopy(gpsdata, 3, time, 0, 3);

		String dates = Tools.bytesToHexString(date);
		String dd = Integer.parseInt(dates.substring(4), 16) + "";
		String mm = Integer.parseInt(dates.substring(2, 4), 16) + "";
		String yy = Integer.parseInt(dates.substring(0, 2), 16) + "";
		if (dd.length() < 2)
			dd = "0" + dd;
		if (mm.length() < 2)
			mm = "0" + mm;
		if (yy.length() < 2)
			yy = "0" + yy;

		String hh = Tools.byte2Int(new byte[] { time[0] }) + "";
		if (hh.length() < 2)
			hh = "0" + hh;
		String MM = Tools.byte2Int(new byte[] { time[1] }) + "";
		if (MM.length() < 2)
			MM = "0" + MM;
		String ss = Tools.byte2Int(new byte[] { time[2] }) + "";
		if (ss.length() < 2)
			ss = "0" + ss;

		String format_date = Tools.conformtime(hh + MM + ss, dd + mm + yy);
		this.setTime(format_date);

		byte[] lat = new byte[4];
		System.arraycopy(gpsdata, 6, lat, 0, 4);
		String latHex = Tools.bytesToHexString(lat);
		int latdu = Tools.byte2Int(new byte[] { lat[0] });
		int zhenfen = Tools.byte2Int(new byte[] { lat[1] });
		int fac1 = Tools.byte2Int(new byte[] { lat[2] });
		int fac2 = Tools.byte2Int(new byte[] { lat[3] });
		String sfen = zhenfen + ".";
		String sfac1 = fac1 + "";
		if (fac1 < 10) {
			sfac1 = "0" + fac1;
		}
		String sfac2 = fac2 + "";
		if (fac2 < 10) {
			sfac2 = "0" + fac2;
		}

		float latfen = Float.parseFloat(sfen + sfac1 + sfac2);

		String y = (latdu + latfen / 60f) + "";
		this.setCoordY(y);
		byte[] lng = new byte[4];
		System.arraycopy(gpsdata, 10, lng, 0, 4);
		int lngdu = Tools.byte2Int(new byte[] { lng[0] });

		int xzhengfen = Tools.byte2Int(new byte[] { lng[1] });
		int xfac1 = Tools.byte2Int(new byte[] { lng[2] });
		int xfac2 = Tools.byte2Int(new byte[] { lng[3] });
		sfen = xzhengfen + ".";
		sfac1 = xfac1 + "";
		if (xfac1 < 10) {
			sfac1 = "0" + sfac1;
		}
		sfac2 = xfac2 + "";
		if (xfac2 < 10) {
			sfac2 = "0" + xfac2;
		}

		float lngfen = Float.parseFloat(sfen + sfac1 + sfac2);
		String x = (lngdu + lngfen / 60f) + "";
		this.setCoordX(x);
		byte spd = gpsdata[14];
		int ispd = Tools.byte2Int(new byte[] { spd });
		String speed = Tools.formatKnotToKm(ispd + "");
		this.setSpeed(speed);
		byte direction = gpsdata[15];
		int idirection = Tools.byte2Int(new byte[] { direction }) * 3;
		this.setDirection(idirection + "");

		byte[] state = new byte[6];
		System.arraycopy(gpsdata, 16, state, 0, 6);
		this.parseState(state);

		byte[] lc = new byte[4];
		System.arraycopy(gpsdata, 22, lc, 0, 4);
		float ilc = Tools.byte2Int(lc) / 1000f;
		this.setLC(ilc + "");
		if (this.isHaveOil) {
			byte[] oil = new byte[2];
			System.arraycopy(gpsdata, 26, oil, 0, 2);
			int ioil = Tools.byte2Int(oil);
		}
		if (this.isHaveTmpr) {
			byte[] tmpr = new byte[4];
			if(this.isHaveOil){
				System.arraycopy(gpsdata, 28, tmpr, 0, 4);
			}else{
				System.arraycopy(gpsdata, 26, tmpr, 0, 4);
			}
			  
			for (int k=0; k<tmpr.length; k++){
				if (tmpr[k] == (byte)0xff){
					Log.getInstance().outLog(this.getDeviceSN()+" 第"+k+"路温度传感无效");
				}else{
					int itmpr = Tools.byte2Int(new byte[]{tmpr[k]});
					if (itmpr>=128){
						itmpr = -(itmpr-128);
					}
					this.setTemperature(itmpr+"" );
					Log.getInstance().outLog(this.getDeviceSN()+" 第"+k+"路温度传感器温度为："+this.getTemperature());
				}
			}
			
		}
		if (this.getLocateStatus() != null
				&& this.getLocateStatus().equals("1")) {
			this.sentPost(true);
			Log.getInstance().outLog(
					this.getDeviceSN() + " 已定位GPS数据：x=" + this.getCoordX()
							+ ",y=" + this.getCoordY() + ",s="
							+ this.getSpeed() + ",dirction="
							+ this.getDirection() + ",lc=" + this.getLC()
							+ ",temperature=" + this.getTemperature()
							+ ",gpstime=" + this.getTime());
		} else {
			Log.getInstance().outLog(
					this.getDeviceSN() + " 未定位GPS数据：x=" + this.getCoordX()
							+ ",y=" + this.getCoordY() + ",s="
							+ this.getSpeed() + ",dirction="
							+ this.getDirection() + ",lc=" + this.getLC()
							+ ",temperature=" + this.getTemperature()
							+ ",gpstime=" + this.getTime());
		}

	}

	private void parseState(byte[] state) {
		byte s1 = state[0];
		String tmp = this.getDeviceSN();
		if (this.getByteBit(s1, 0) == 0) {
			tmp += " 已定位，";
			this.setLocateStatus("1");
		} else {
			tmp += " 未定位，";
			this.setLocateStatus("0");
		}
		if (this.getByteBit(s1, 1) == 0) {
			tmp += " 南纬，";

		} else {
			tmp += " 北纬，";
		}
		if (this.getByteBit(s1, 2) == 0) {
			tmp += " 西经，";

		} else {
			tmp += " 东经，";

		}
		if (this.getByteBit(s1, 6) == 0) {
			tmp += "   数据中没带油量字段，";

		} else {
			tmp += " 数据中带有油量字段，";
			this.isHaveOil = true;
		}
		if (this.getByteBit(s1, 7) == 0) {
			tmp += " 数据中没带温度字段，";

		} else {
			tmp += " 数据中带有温度字段，";
			this.isHaveTmpr = true;

		}
		byte s2 = state[1];
		if (this.getByteBit(s2, 0) == 0) {
			tmp += " ACC关闭，";
			this.setAccStatus("0");

		} else {
			tmp += " ACC打开，";
			this.setAccStatus("1");

		}
		if (this.getByteBit(s2, 1) == 0) {
			tmp += " 车门关闭，";
			this.setExtend1("0");
		} else {
			tmp += " 车门打开，";
			this.setExtend1("1");
		}
		if (this.getByteBit(s2, 2) == 0) {
			tmp += " 油电恢复，";

		} else {
			tmp += " 油电断开，";

		}
		if (this.getByteBit(s2, 3) == 0) {
			tmp += " 空车，";
		} else {
			tmp += " 重车，";
		}
		if (this.getByteBit(s2, 4) == 0) {
			tmp += " 撤防，";
		} else {
			tmp += " 设防，";
		}
		byte s3 = state[2];
		if (this.getByteBit(s3, 0) == 0) {

		} else {
			tmp += " GPS天线开路 ，";
		}
		if (this.getByteBit(s3, 1) == 0) {

		} else {
			tmp += " GPS天线短路 ，";
		}
		if (this.getByteBit(s3, 2) == 0) {

		} else {
			tmp += "  GPS模块故障，";
		}
		if (this.getByteBit(s3, 3) == 0) {

		} else {
			tmp += " GPS定位时间过长 ，";
		}
		if (this.getByteBit(s3, 4) == 0) {

		} else {
			tmp += "  低电，";
		}
		if (this.getByteBit(s3, 5) == 0) {

		} else {
			tmp += " 断电 ，";
		}
		if (this.getByteBit(s3, 6) == 0) {

		} else {

			String data = null;
			try {
				data = "您的车辆于" + this.getTime() + "在" + this.locateDesc()
						+ " 发生了超速报警。";
				byte[] bMsg = data.getBytes("UTF-16");
				byte[] addrb = new byte[bMsg.length - 2];
				System.arraycopy(bMsg, 2, addrb, 0, addrb.length);
				String hexdata = "00" + Tools.bytesToHexString(addrb);

				byte[] repb = this.crtCmdByte((byte) 0x10, new byte[] {
						(byte) 0xff, (byte) 0xff }, Tools
						.fromHexString(deviceid), (byte) 0x01, (byte) 0x11,
						hexdata);
				this.setReplyByte1(repb);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			tmp += " 超速 ，";
			this.setAlarmType("1");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		byte s4 = state[3];
		if (this.getByteBit(s4, 0) == 0) {

		} else {
			String data = null;
			try {
				data = "您的车辆于" + this.getTime() + "在" + this.locateDesc()
						+ " 发生了劫警。";
				byte[] bMsg = data.getBytes("UTF-16");
				byte[] addrb = new byte[bMsg.length - 2];
				System.arraycopy(bMsg, 2, addrb, 0, addrb.length);
				String hexdata = "00" + Tools.bytesToHexString(addrb);

				byte[] repb = this.crtCmdByte((byte) 0x10, new byte[] {
						(byte) 0xff, (byte) 0xff }, Tools
						.fromHexString(deviceid), (byte) 0x01, (byte) 0x11,
						hexdata);
				this.setReplyByte1(repb);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tmp += " 劫警 ，";
			this.setAlarmType("3");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		if (this.getByteBit(s4, 1) == 0) {

		} else {
			tmp += " 断电 ，";

		}
		if (this.getByteBit(s4, 2) == 0) {

		} else {
			tmp += " 低压 ，";

		}
		if (this.getByteBit(s4, 3) == 0) {

		} else {
			String data = null;
			try {
				data = "您的车辆于" + this.getTime() + "在" + this.locateDesc()
						+ " 发生了超速报警。";
				byte[] bMsg = data.getBytes("UTF-16");
				byte[] addrb = new byte[bMsg.length - 2];
				System.arraycopy(bMsg, 2, addrb, 0, addrb.length);
				String hexdata = "00" + Tools.bytesToHexString(addrb);

				byte[] repb = this.crtCmdByte((byte) 0x10, new byte[] {
						(byte) 0xff, (byte) 0xff }, Tools
						.fromHexString(deviceid), (byte) 0x01, (byte) 0x11,
						hexdata);
				this.setReplyByte1(repb);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tmp += " 超速，";
			this.setAlarmType("1");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		if (this.getByteBit(s4, 4) == 0) {

		} else {
			String data = null;
			try {
				data = "您的车辆于" + this.getTime() + "在" + this.locateDesc()
						+ " 发生了进区域报警。";
				byte[] bMsg = data.getBytes("UTF-16");
				byte[] addrb = new byte[bMsg.length - 2];
				System.arraycopy(bMsg, 2, addrb, 0, addrb.length);
				String hexdata = "00" + Tools.bytesToHexString(addrb);

				byte[] repb = this.crtCmdByte((byte) 0x10, new byte[] {
						(byte) 0xff, (byte) 0xff }, Tools
						.fromHexString(deviceid), (byte) 0x01, (byte) 0x11,
						hexdata);
				this.setReplyByte1(repb);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tmp += " 进区 ，";
			this.setAlarmType("2");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		if (this.getByteBit(s4, 5) == 0) {

		} else {
			String data = null;
			try {
				data = "您的车辆于" + this.getTime() + "在" + this.locateDesc()
						+ " 发生了出区域报警。";
				byte[] bMsg = data.getBytes("UTF-16");
				byte[] addrb = new byte[bMsg.length - 2];
				System.arraycopy(bMsg, 2, addrb, 0, addrb.length);
				String hexdata = "00" + Tools.bytesToHexString(addrb);

				byte[] repb = this.crtCmdByte((byte) 0x10, new byte[] {
						(byte) 0xff, (byte) 0xff }, Tools
						.fromHexString(deviceid), (byte) 0x01, (byte) 0x11,
						hexdata);
				this.setReplyByte1(repb);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tmp += " 出区 ，";
			this.setAlarmType("2");
			AlarmQueue.getInstance().addAlarm(this.clone());
		}
		byte s5 = state[4];
		if (this.getByteBit(s5, 0) == 0) {

		} else {
			tmp += " 震动 ，";

		}
		if (this.getByteBit(s5, 1) == 0) {

		} else {
			tmp += " 非法开车门 ，";

		}
		if (this.getByteBit(s5, 2) == 0) {

		} else {
			tmp += " 非法点火 ，";

		}
		if (this.getByteBit(s5, 3) == 0) {

		} else {
			tmp += " 身份识别 ，";

		}
		if (this.getByteBit(s5, 4) == 0) {

		} else {
			String data = null;
			try {
				data = "您的车辆于" + this.getTime() + "在" + this.locateDesc()
						+ " 发生了超长停车报警。";
				byte[] bMsg = data.getBytes("UTF-16");
				byte[] addrb = new byte[bMsg.length - 2];
				System.arraycopy(bMsg, 2, addrb, 0, addrb.length);
				String hexdata = "00" + Tools.bytesToHexString(addrb);

				byte[] repb = this.crtCmdByte((byte) 0x10, new byte[] {
						(byte) 0xff, (byte) 0xff }, Tools
						.fromHexString(deviceid), (byte) 0x01, (byte) 0x11,
						hexdata);
				this.setReplyByte1(repb);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tmp += " 停车超长报警 ，";

		}
		if (this.getByteBit(s5, 5) == 0) {

		} else {
			tmp += " 温度超标 ，";

		}
		byte s6 = state[5];
		if (this.getByteBit(s6, 0) == 0) {

		} else {
			tmp += "  碰撞 ，";

		}
		if (this.getByteBit(s6, 1) == 0) {

		} else {
			tmp += "  拖车 ，";

		}
		if (this.getByteBit(s6, 2) == 0) {

		} else {
			tmp += "   密码错误，";

		}
		if (this.getByteBit(s6, 3) == 0) {

		} else {
			tmp += "   超声波报警，";

		}
		Log.getInstance().outLog(this.getDeviceSN() + " 状态：" + tmp);

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

	private byte[] crtCmdByte(byte version, byte[] route, byte[] deviceids,
			byte businessType, byte dataType, String hexdata) {

		byte[] data = Tools.fromHexString(hexdata);

		String head = "SF";
		byte end = (byte) 0xaf;
		byte[] ret = null;
		ByteBuffer buf = ByteBuffer.allocate(19 + data.length);
		buf.put(head.getBytes());
		buf.put(version);
		buf.put(route);
		buf.put(deviceids);
		buf.put((byte) (businessType + 0x80));
		buf.put(dataType);
		int len = data.length;
		String hexlen = Tools.int2Hexstring(len, 4);
		buf.put(Tools.fromHexString(hexlen));
		buf.put(data);

		byte[] tmp = buf.array();
		byte[] vertype = ByteBuffer.wrap(tmp, 2, tmp.length - 2).array();
		byte vtycode = this.getVerfyCode(vertype);
		buf.put(vtycode);
		buf.put(end);
		ret = buf.array();

		Log.getInstance().outLog("中心=》终端数据：" + Tools.bytesToHexString(ret));

		return ret;

	}

	public static byte getVerfyCode(byte[] br) {

		int sum = 0;
		for (int i = 0; i < br.length; i++) {
			sum += br[i] & 0xFF;
		}

		return (byte) sum;
	}

	private String formatDeviceId(String tid) {
		String ret = tid;
		while (ret.length() <= 16) {
			ret = ret + "F";
		}
		return ret;
	}

	private String getDeviceId(String tid) {
		return tid.replaceAll("f", "");
	}

	@Override
	public void parseSMS(String phnum, String content) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		//String hex = "534610ffff110616308210165501840083050c03141038012124400c77014e3c004d0600000000000000e9380c03141039012124400c77014e3c004d0600000000000000e9380c0314103a012124400c77014e3c004d0600000000000000e9380c0314103b012124400c77014e3c004d0600000000000000e9380c03141100012124400c77014e3c004d0600000000000000e93883af";
		//ParseShouHang sh = new ParseShouHang();
		//sh.parseGPRS(hex);
		
		//*HQ20014705445614,BA&A1435043807121411428086060006190314&B0000000000&C03?197<8&F0000#  ->
		//2a485132303031343730353434353631342c4241264131343335303433383037313231343131343238303836303630303036313930333134264230303030303030303030264330333f3139373c3826463030303023
		/*String hex = "2a485132303031343730353434353631342c4241264131343335303433383037313231343131343238303836303630303036313930333134264230303030303030303030264330333f3139373c3826463030303023";
		byte[] b =  Tools.fromHexString(hex);
		
		String binary = "";
		try {
			binary = new String(b, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(binary);*/
		
		ParseShouHang parseShouHang = new ParseShouHang();
		String hexdata = "534610ffff1309241251302030468402080015000900000200ffd8ffdb008400191113161310191614161c1b191e263f29262222264d373a2d3f5b505f5e595058566471907a646a886c56587daa7f889499a1a3a16178b1bdaf9cbc909ea19b011b1c1c262126492929499b6758679b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9b9bffc000110800f0014003012100021101031101ffdd00040014ffc401a20000010501010101010100000000000000000102030405060708090a0b100002010303020403050504040000017d01020300041105122131410613516107227114328191a1082342b1c11552d1f02433627282090a161718191a25262728292a3435363738393a434445464748494a535455565758595a636465666768696a737475767778797a838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae1e2e3e4e5e6e7e8e9eaf1f2f3f4f5f6f7f8f9fa0100030101010101010101010000000000000102030405060708090a0b1100020102040403040705040400010277000102031104052131061241510761711322328108144291a1b1c109233352f0156272d10a162434e125f11718191a262728292a35363738393a434445464748494a535455565758595a636465666768696a737475767778797a8283841daf";
		parseShouHang.deviceid = "1309241251302030";
		parseShouHang.parsePhoto(hexdata);
		
	}

}
