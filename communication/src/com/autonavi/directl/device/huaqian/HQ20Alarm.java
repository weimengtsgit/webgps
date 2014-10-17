package com.autonavi.directl.device.huaqian;

import com.autonavi.directl.Tools;
import com.autonavi.directl.idirectl.*;

public class HQ20Alarm extends AlarmAdapter {
  public HQ20Alarm() {
  }

  public HQ20Alarm(com.autonavi.directl.idirectl.TerminalParam tparam) {
    this.terminalParam = tparam;
  }

  /**
      设置报警范围
      v='I':表示进范围报警
      v='O':表示出范围报警
      v='A':表示进出范围均报警
      v='D':表示取消报警范围
      aaaaaaaaooooooooo:表示xy点（大纬大经）的经纬度值
      aaaaaaaaooooooooo:表示XY点（小纬小经）的经纬度值
   * @param alarmType:0表示出区域报警，1表示进区域报警，2表示进出范围均报警，3表示取消区域报警
   * @param xy：格式为aaaaaaaaooooooooo表示xy点（大纬大经）的经纬度值
   * @param XY：格式为aaaaaaaaooooooooo表示XY点（小纬小经）的经纬度值
   * @return
   */
  public String setRangeAlarm(String alarmType, EarthCoord xy, EarthCoord XY) {
    StringBuffer buf = new StringBuffer();
    buf.append("AD");
    if (alarmType.equals("0")) {
      buf.append("O(");
    } else if (alarmType.equals("1")) {
      buf.append("I(");
    } else if (alarmType.equals("2")) {
      buf.append("A(");
    } else if (alarmType.equals("3")) {
      buf.append("D(");
    }

    double maxLongitude = Double.parseDouble(xy.x);
    double maxLatitude = Double.parseDouble(xy.y);
    double minLongitude = Double.parseDouble(XY.x);
    double minLatitude = Double.parseDouble(XY.y);

    buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
    buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
    buf.append(HQ20Util.getLatLongString(minLatitude, 1));
    buf.append(HQ20Util.getLatLongString(minLongitude, 0));
    buf.append(")");

    String cmd = HQ20Util.makeCommandStr(buf.toString(), true, true);

    return this.sent(cmd);
  }

  /**
   *
   * @param speedType:作废，不用
   * @param speed：为十六进制的速度值，速度单位为节
   * @return
   */
  public String setSpeedAlarm(String speedType, String speed) {
    StringBuffer buf = new StringBuffer();
    buf.append("AH(1");
    double dSpeed = Double.parseDouble(speed);
    double knotSpeed = HQ20Util.KMHOUR2Knot(dSpeed);
    Integer.toHexString( (int) knotSpeed);
    String knotSpeedStr = HQ20Util.extentString(Integer.toHexString( (int)
        knotSpeed), 2);
    buf.append(knotSpeedStr);
    buf.append(")");
    String cmdStr = HQ20Util.makeCommandStr(buf.toString(), true, true);
    return this.sent(cmdStr);
  }

  /**
   *偏差报警
   *@param xxxxxxxxyyyyyyyy:纬度偏差值
   */
  public String setWarpAlarm(EarthCoord xxxxxxxxyyyyyyyy) {
//        this.isNeedReply = "1";
//        String tmp = "BL" + xxxxxxxxyyyyyyyy.x + xxxxxxxxyyyyyyyy.y;
//        String cmdStr = makeCommandStr(tmp);
//        return this.sent(cmdStr);
    return "";
  }

  /**
   *解除报警
   * @param v:解除、开启报警 0 :关闭所有报警 1：打开所有报警
   */
  public String stopAlarm(String v) {
    String tmp = "BC";
    String cmdStr = HQ20Util.makeCommandStr(tmp, false, true);
    return this.sent(cmdStr);
  }

  public static void main(String[] args) {
	  
	  
	byte[] ts = Tools.fromHexString("5b485132303131414f0334014fff040b464e55034021470b464c48034020280b464c48034021470b464e55034021470b464e55034020280b464c48034020285d"); 
	  
    HQ20Alarm HQ20Alarm = new HQ20Alarm();
    TerminalParam tp = new TerminalParam();
    tp.setGPRSModal(true);
    tp.setSimCard("13911238237");
    HQ20Alarm.setTerminalParam(tp);

    //测试区域报警
    EarthCoord xy = new EarthCoord("128.23404578", "E", "67.234923", "N");
    EarthCoord XY = new EarthCoord("56.957203", "E", "9.299395", "N");
    String cmd = HQ20Alarm.setRangeAlarm("0", xy, XY);
    System.out.println("区域报警指令：" + cmd);

    //测试路线报警
    cmd = HQ20Alarm.setSpeedAlarm("", "128");
    System.out.println("路线报警指令：" + cmd);

    //解除报警
    cmd = HQ20Alarm.stopAlarm("");
    System.out.println("解除报警指令：" + cmd);

  }

}
