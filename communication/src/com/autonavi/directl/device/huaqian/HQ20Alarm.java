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
      ���ñ�����Χ
      v='I':��ʾ����Χ����
      v='O':��ʾ����Χ����
      v='A':��ʾ������Χ������
      v='D':��ʾȡ��������Χ
      aaaaaaaaooooooooo:��ʾxy�㣨��γ�󾭣��ľ�γ��ֵ
      aaaaaaaaooooooooo:��ʾXY�㣨СγС�����ľ�γ��ֵ
   * @param alarmType:0��ʾ�����򱨾���1��ʾ�����򱨾���2��ʾ������Χ��������3��ʾȡ�����򱨾�
   * @param xy����ʽΪaaaaaaaaooooooooo��ʾxy�㣨��γ�󾭣��ľ�γ��ֵ
   * @param XY����ʽΪaaaaaaaaooooooooo��ʾXY�㣨СγС�����ľ�γ��ֵ
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
   * @param speedType:���ϣ�����
   * @param speed��Ϊʮ�����Ƶ��ٶ�ֵ���ٶȵ�λΪ��
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
   *ƫ���
   *@param xxxxxxxxyyyyyyyy:γ��ƫ��ֵ
   */
  public String setWarpAlarm(EarthCoord xxxxxxxxyyyyyyyy) {
//        this.isNeedReply = "1";
//        String tmp = "BL" + xxxxxxxxyyyyyyyy.x + xxxxxxxxyyyyyyyy.y;
//        String cmdStr = makeCommandStr(tmp);
//        return this.sent(cmdStr);
    return "";
  }

  /**
   *�������
   * @param v:������������� 0 :�ر����б��� 1�������б���
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

    //�������򱨾�
    EarthCoord xy = new EarthCoord("128.23404578", "E", "67.234923", "N");
    EarthCoord XY = new EarthCoord("56.957203", "E", "9.299395", "N");
    String cmd = HQ20Alarm.setRangeAlarm("0", xy, XY);
    System.out.println("���򱨾�ָ�" + cmd);

    //����·�߱���
    cmd = HQ20Alarm.setSpeedAlarm("", "128");
    System.out.println("·�߱���ָ�" + cmd);

    //�������
    cmd = HQ20Alarm.stopAlarm("");
    System.out.println("�������ָ�" + cmd);

  }

}
