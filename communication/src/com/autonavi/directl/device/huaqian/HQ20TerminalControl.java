package com.autonavi.directl.device.huaqian;

import com.autonavi.directl.Log;
import com.autonavi.directl.idirectl.*;
 
 
import java.util.StringTokenizer;

public class HQ20TerminalControl extends TerminalControlAdaptor {
  public HQ20TerminalControl() {
  }

  public HQ20TerminalControl(com.autonavi.directl.idirectl.TerminalParam tparam) {
    this.terminalParam = tparam;
  }

  /**
   * ������
   * @return
   */
  public String reset() {
    String tmp = "BA0";
    String cmd = HQ20Util.makeCommandStr(tmp, false, true);
    return cmd;//this.sent(cmd);

  }

  /**
   * �Ͽ���·
   * @param v��'1'��ʾ����������·��'0'��ʾ�ָ�������·��
   * @return
   */
  public String setOilState(String v) {
    String tmp = "BB" + v;
    String cmd = HQ20Util.makeCommandStr(tmp, false, true);
    return cmd;//this.sent(cmd);

  }

  /**
   * �����ն˹���״̬
   * @param v:'0'��ʾ��ACC��������£�ʹ�ն�����������'1'��ʾ������Ϊ����ǰ���ò�����������'2'��ʾ��ACC�ص�����£�ʹ�ն˽���ʡ��ģʽ;'3'��ʾ����GPS��λģ��.
   * @return
   */
  public String setWorkState(String v) {
    String tmp = "BA" + v;
//    String cmd =makeCommandStr(tmp);
//   return this.sent(cmd);
    return null;

  }

  /**
   * ����������Ϣ
   * @param msg
   * @return
   */
  public String sentPubMsg(String msg) {
    String codeMsg = HQ20Util.to64Code(msg);
    String tmp = "CA" + codeMsg;
    String cmd = HQ20Util.makeCommandStr(tmp, true, true);
    return cmd;//this.sent(cmd);

  }

  /**
   * ���ֻش�
   * @param ptype���Ƿ��Ƿ�Χ ���ֻش�,'1'�Ƿ�Χ ���ֻش�;'2'��Χ ���ֻش�
   * @param xy
   * @param XY
   * @param msg
   * @return
   */
  public String replyNum(String ptype, EarthCoord xy, EarthCoord XY,
                         String msg) {
//    String tmp="CD";
//    if(ptype.trim().equalsIgnoreCase("1")){
//      tmp=tmp+ptype+"("+msg+")";
//    }else if(ptype.trim().equalsIgnoreCase("2")){
//      tmp=tmp+ptype+"("+xy.x+xy.y+XY.x+XY.y+")"+"("+msg+")";
//    }else{
//      return null;
//    }
//    String cmd =makeCommandStr(tmp);
//
//    return this.sent(cmd);
    return null;

  }

  /**
   * Ҫ�����GPRSģʽ
   * @param v��'0'��ʾ�˳�GPRS����״̬��'1'��ʾ����GPRS����״̬��
   * @param pTime��'FFFF'��ʾ�ն˳���GPRS״̬�������ʾ����GPRS�ĳ���ʱ�䣻
   * @return
   */
  public String gointoGPRS(String v, String pTime) {
//    String tmp="BD"+v+pTime;
//    String cmd =makeCommandStr(tmp);
//  return  this.sent(cmd);
    return null;

  }

  /**
   * ĳ��Χ����
   * @param xy
   * @param XY
   * @return
   */
  public String rangeCalling(EarthCoord xy, EarthCoord XY) {
    StringBuffer buf = new StringBuffer();
    buf.append("BM(");
    double maxLongitude = Double.parseDouble(xy.x);
    double maxLatitude = Double.parseDouble(xy.y);
    double minLongitude = Double.parseDouble(XY.x);
    double minLatitude = Double.parseDouble(XY.y);

    buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
    buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
    buf.append(HQ20Util.getLatLongString(minLatitude, 1));
    buf.append(HQ20Util.getLatLongString(minLongitude, 0));
    buf.append(")");
    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * �ն˻ز��绰
   * @param tel
   * @return
   */
  public String callBack(String tel) {
//    String tmp="BF"+tel;
//    String cmd =makeCommandStr(tmp);
// return   this.sent(cmd);
    return null;

  }

  /**
   ���ն˸���������Ϣ
   * @param affix:�ն˸�������,�磺��ʾƽ����Ϊ04
   * @param protocol��Э��
   * @param msg�����͵���Ϣ
   * @return
   */
  public String sentAffixMsg(String affix, String protocol, String msg) {
//    String tmp="BZ"+affix+protocol+msg;
//    String cmd =makeCommandStr(tmp);
//  return  this.sent(cmd);
    return null;

  }

  /**
   * ��������
   * @param ptype String --- �Ƿ��Ƿ�Χ����,"0"�����"1"������
   * @param x String --- X1�㾭��
   * @param y String --- X1��γ��
   * @param X String --- X2�㾭��
   * @param Y String --- X2��γ��
   * @param msg String --- ������Ϣ�����������Ϣ֮����","�ָ�
   * @return String
   */
  public String revertShortMessage(String ptype, String x, String y, String X, String Y, String msg) {
    EarthCoord xy = new EarthCoord(x, "", y, "");
    EarthCoord XY = new EarthCoord(X, "", Y, "");
    StringBuffer buf = new StringBuffer();
    buf.append("CB");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("1");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("2(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }

    //����������Ϣ
    buf.append("(");
    StringTokenizer tokenizer = new StringTokenizer(msg, ",");
    int num = tokenizer.countTokens() > 4 ? 4 : tokenizer.countTokens(); //�����������Ϣ
    StringBuffer msgBuf = new StringBuffer(); //���ڴ�Ÿ�ʽ�����������Ϣ
    for (int i = 1; i <= num; i++) {
      if (i > 1) {
        msgBuf.append("&");
      }
      String numStr = HQ20Util.extentString(Integer.toString(i), 2);
      msgBuf.append(numStr);
      msgBuf.append(tokenizer.nextToken());
    }
    buf.append(HQ20Util.to64Code(msgBuf.toString()));
    buf.append(")");

    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, false);
    return cmd;//this.sent(cmd);

  }

  /**
   * ��������
   *
   * @param v���Ƿ��Ƿ�Χ����,"0"�����"1"������
   * @param xy X1�㾭γ��
   * @param XY X2�㾭γ��
   * @param msg ������Ϣ�����������Ϣ֮����","�ָ�
   * @return ����ָ��
   */
  public String quickRevertShortMessage(String ptype, EarthCoord xy,
                                        EarthCoord XY, String msg) {
    StringBuffer buf = new StringBuffer();
    buf.append("CB");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("1");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("2(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }

    //����������Ϣ
    buf.append("(");
    StringTokenizer tokenizer = new StringTokenizer(msg, ",");
    int num = tokenizer.countTokens() > 4 ? 4 : tokenizer.countTokens(); //�����������Ϣ
    StringBuffer msgBuf = new StringBuffer(); //���ڴ�Ÿ�ʽ�����������Ϣ
    for (int i = 1; i <= num; i++) {
      if (i > 1) {
        msgBuf.append("&");
      }
      String numStr = HQ20Util.extentString(Integer.toString(i), 2);
      msgBuf.append(numStr);
      msgBuf.append(tokenizer.nextToken());
    }
    buf.append(HQ20Util.to64Code(msgBuf.toString()));
    buf.append(")");

    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * �绰����
   * @param ptype String --- �Ƿ��Ƿ�Χ����,'3'�Ƿ�Χ�绰����;'4'��Χ�绰����
   * @param x String --- X1�㾭��
   * @param y String --- X1��γ��
   * @param X String --- X2�㾭��
   * @param Y String --- X2��γ��
   * @param tel String --- �绰
   * @param msg String --- ������Ϣ
   * @return String
   */
  public String revertCalling(String ptype, String x, String y, String X, String Y,
                                   String tel, String msg) {
    EarthCoord xy = new EarthCoord(x, "", y, "");
    EarthCoord XY = new EarthCoord(X, "", Y, "");
    StringBuffer buf = new StringBuffer();
    buf.append("CB");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("3");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("4(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }

    //����������Ϣ
    buf.append("(");
    buf.append(tel);
    buf.append(",");
    buf.append(HQ20Util.to64Code("p1" + msg));
    buf.append(")");

    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);

  }

  /**
   *�绰����
   * @param ptype���Ƿ��Ƿ�Χ����,'0'�Ƿ�Χ����;'1'��Χ����
   * @param xy X1�㾭γ��
   * @param XY X2�㾭γ��
   * @param tel �绰����
   * @param msg ������Ϣ
   * @return ����ָ��
   */
  public String quickRevertCalling(String ptype, EarthCoord xy, EarthCoord XY,
                                   String tel, String msg) {
    StringBuffer buf = new StringBuffer();
    buf.append("CB");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("3");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("4(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }

    //����������Ϣ
    buf.append("(");
    buf.append(tel);
    buf.append(",");
    buf.append(HQ20Util.to64Code("p1" + msg));
    buf.append(")");

    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * ����Ϣ����
   * @param ptype String
   * @param x String
   * @param y String
   * @param X String
   * @param Y String
   * @param tel String
   * @param msg String
   * @return String
   */
  public String shortMessageInforAttemper(String ptype, String x, String y, String X, String Y,
                                          String msg) {
    EarthCoord xy = new EarthCoord(x, "", y, "");
    EarthCoord XY = new EarthCoord(X, "", Y, "");
    StringBuffer buf = new StringBuffer();
    buf.append("CC");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("1");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("2(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }
    //����������Ϣ
    buf.append("(");
    buf.append(HQ20Util.to64Code("01" + msg));
    buf.append(")");
    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);

  }

  /**
   * ���ŵ���
   * @param ptype���Ƿ��Ƿ�Χ����,'0'�Ƿ�Χ����;'1'��Χ����
   * @param xy
   * @param XY
   * @param msg
   * @return
   */
  public String shortMessageAttemper(String ptype, EarthCoord xy,
                                     EarthCoord XY, String msg) {
    StringBuffer buf = new StringBuffer();
    buf.append("CC");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("1");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("2(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }
    //����������Ϣ
    buf.append("(");
    buf.append(HQ20Util.to64Code("01" + msg));
    buf.append(")");
    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * �绰����
   * @param ptype String���Ƿ��Ƿ�Χ����,'3'�Ƿ�Χ����;'4'��Χ����
   * @param x String: ��������ľ���
   * @param y String: ���������ά��
   * @param X String: ��������ľ���
   * @param Y String: ���������ά��
   * @param tel String: �绰
   * @param msg String����Ϣ
   * @return String
   */
  public String telphoneInforAttemper(String ptype, String x, String y, String X, String Y,
                                      String tel, String msg) {
    EarthCoord xy = new EarthCoord(x, "", y, "");
    EarthCoord XY = new EarthCoord(X, "", Y, "");
    StringBuffer buf = new StringBuffer();
    buf.append("CC");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("3");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("4(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }

    //����������Ϣ
    buf.append("(");
    buf.append(tel);
    buf.append(",");
    buf.append(HQ20Util.to64Code("01" + msg));
    buf.append(")");

    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);

  }

  /**
   *�绰����
   * @param ptype���Ƿ��Ƿ�Χ����,'3'�Ƿ�Χ����;'4'��Χ����
   * @param xy
   * @param XY
   * @param tel
   * @param msg
   * @return
   */
  public String telphoneAttemper(String ptype, EarthCoord xy, EarthCoord XY,
                                 String tel, String msg) {
    StringBuffer buf = new StringBuffer();
    buf.append("CC");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("3");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("4(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }

    //����������Ϣ
    buf.append("(");
    buf.append(tel);
    buf.append(",");
    buf.append(HQ20Util.to64Code("01" + msg));
    buf.append(")");

    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * ���ü���
   * @param state String ��������
   * @param tel String �绰����
   * @return String ����ָ��
   */
  public String setListening(String state, String tel) {
    StringBuffer buf = new StringBuffer();
    buf.append("BF");
    buf.append(tel);
    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * �ϴ�һ��ͼƬ
   * added 2007-9-20 for HQ20
   * @return String
   */
  public String takePicture() {
    StringBuffer buf = new StringBuffer();
    buf.append("HA");
    buf.append( (char) 0x00);
    buf.append( (char) 0x00);
    String cmd = HQ20Util.makespCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * ����ָ����ʱ���ϴ�ָ��������ͼƬ
   * added 2007-9-20 for HQ20
   * @param frequence String ͼ��ش���Ƶ�ʣ���λΪ��
   * @param times String ͼ��ش��Ĵ����� 0����ֹͣ��FFFF��������ץ��
   * @return String
   */
  public String takePictures(String frequence, String times) {
    StringBuffer buf = new StringBuffer();
    buf.append("HB");
    buf.append( (char) 0x00);
    buf.append( (char) 0x08);
    String hexFreq = Integer.toHexString(Integer.parseInt(frequence));
    buf.append(HQ20Util.extentString(hexFreq, 4));
    if (!times.equals("FFFF")) {
      String hexTimes = Integer.toHexString(Integer.parseInt(times));
      buf.append(HQ20Util.extentString(hexTimes, 4));
    } else {
      buf.append(times);
    }
    String cmd = HQ20Util.makespCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * ���Ļظ���
   * @param simcard String
   * @return String
   */
  public String restoreInfor(String funNumAndkey) {
    StringBuffer buf = new StringBuffer();
    buf.append("*HQ20");
    buf.append("Y");
    buf.append(funNumAndkey);
    buf.append("#");
    String cmd = buf.toString();//HQ20Util.makeCommandStr(buf.toString(), false, false);
    Log.getInstance().outLog("��ͨ���Ļ�Ӧ��ǿ�ն�ָ�"+cmd);
     return cmd;//this.sent(cmd);
  }
  
  /**
   * ��������Э��ظ���
   * @param simcard String
   * @return String
   */
  public String restoreSPInfor(String funNumAndkey) {
    StringBuffer buf = new StringBuffer();
    buf.append("[HQ20");
    buf.append("Y");
    buf.append(funNumAndkey);
    buf.append("]");
    String cmd = buf.toString();
    Log.getInstance().outLog("�������Ļ�Ӧ��ǿ�ն�ָ�"+cmd);
    
     return cmd;//this.sent(cmd);
  }

  public static void main(String[] args) {
    HQ20TerminalControl tc = new HQ20TerminalControl();
    TerminalParam tp = new TerminalParam();
    tp.setGPRSModal(true);
    tp.setSimCard("13911238237");
    //tc.setTerminalParam(tp);
    EarthCoord xy = new EarthCoord("128.23404578", "E", "67.234923", "N");
    EarthCoord XY = new EarthCoord("56.957203", "E", "9.299395", "N");

    //������ͨ����
    String msg = tc.sentPubMsg(
        "���ڷǵ�������Ƶĺܺã�������һ��ҽ����Ա�ܵ���Ⱦ");
    System.out.println("��ͨ����ָ�" + msg);

    //��������ָ��
    msg = tc.reset();
    System.out.println("����ָ�" + msg);

    //�Ͽ���·ָ��
    msg = tc.setOilState("0");
    System.out.println("�ָ�������·ָ�" + msg);
    msg = tc.setOilState("1");
    System.out.println("����������·ָ�" + msg);

    //����
    msg = tc.setListening("", "13911238237");
    System.out.println("����ָ�" + msg);

    //��������
    String msg111 = "����ǿ������,���ʸڿڰ�ж��";
    msg = tc.quickRevertShortMessage("0", xy, XY, msg111);
    System.out.println("�Ƿ�Χ��������ָ�" + msg);
    msg = tc.quickRevertShortMessage("1", xy, XY, msg111);
    System.out.println("��Χ��������ָ�" + msg);

    //��Χ����
    msg = tc.rangeCalling(xy, XY);
    System.out.println("��Χ����ָ�" + msg);

    //�绰����
    String tel = "13911238237";
    msg111 = "����ǿ������";
    msg = tc.quickRevertCalling("0", xy, XY, tel, msg111);
    System.out.println("�Ƿ�Χ�绰����ָ�" + msg);
    msg = tc.quickRevertCalling("1", xy, XY, tel, msg111);
    System.out.println("��Χ�绰����ָ�" + msg);

    //shortMessageAttemper
    //���ŵ���
    msg111 = "����ǿ��Ů�������ſ�����";
    msg = tc.shortMessageAttemper("0", xy, XY, msg111);
    System.out.println("�Ƿ�Χ���ŵ���ָ�" + msg);
    msg = tc.shortMessageAttemper("1", xy, XY, msg111);
    System.out.println("��Χ���ŵ���ָ�" + msg);

    //telphoneAttemper
    //�绰����
    msg = tc.telphoneAttemper("0", xy, XY, tel, msg111);
    System.out.println("�Ƿ�Χ�绰����ָ�" + msg);
    msg = tc.telphoneAttemper("1", xy, XY, tel, msg111);
    System.out.println("��Χ�绰����ָ�" + msg);
  }

}
