package com.autonavi.directl.device.huaqian;

import com.autonavi.directl.idirectl.*;

public class HQ20Locator extends LocatorAdaptor {
  /**
   * ���캯��
   */
  public HQ20Locator() {
  }

  /**
   * ���캯��
   * @param tparam TerminalParam
   */
  public HQ20Locator(com.autonavi.directl.idirectl.TerminalParam tparam) {
    this.terminalParam = tparam;
  }

  /**
   * B.5 ������Χ�ĵ�����Ϣ
   * ���ζ�λ.�ȷ��Ͷ�λָ�Ȼ�����ݿ���ץ��
   * @return
   */
  public String singleLocator() {
    String tmp = "BE";
    String cmd = HQ20Util.makeCommandStr(tmp, false, true);
    return this.sent(cmd);
  }

  /**
   * B.9 ���ö�ʱ�ش�ʱ�����ʹ���
   * ����������λ
   * @param DDDD:����ʱ����
   * @param NNNN�����ʹ���
   * @return
   */
  public String multiLocator(String DDDD, String NNNN) {
    //ʮ���Ƶ�ʮ������ת��
    int frequence = Integer.parseInt(DDDD);
    int times = Integer.parseInt(NNNN);
    String hexFrequence = Integer.toHexString(frequence);
    hexFrequence = HQ20Util.extentString(hexFrequence, 4);
    String hexTimes = null;
    if (times != -1) {
      hexTimes = Integer.toHexString(times);
      hexTimes = HQ20Util.extentString(hexTimes, 4);
    } else { //�������ͣ���ʱ������
      hexTimes = "FFFF";
    }
    String tmp = "BI" + hexFrequence + hexTimes;
    String cmd = HQ20Util.makeCommandStr(tmp, false, true);
    return  cmd ;
    
  }

  /**
   * ����������λ����
   * @param state String:0��ʾ�رգ�1��ʾ����
   * @return String
   */
  public String setMultiLocatorState(String state) {
    if (state.equals("1")) {
      //�ն˲�֧�ֿ������ܣ�����������Ҫͨ��multiLocator�ӿ�ָ�������ʱ������
      return "";
    } else if (state.equals("0")) {
      //�ó�ʱ����ģ��ر�
      return this.multiLocator("65535", "0");
    } else {
      return "";
    }
  }

  public static void main(String[] args) {
    HQ20Locator HQ20Locator = new HQ20Locator();
    TerminalParam tp = new TerminalParam();
    tp.setGPRSModal(true);
    tp.setSimCard("13911238237");
    HQ20Locator.setTerminalParam(tp);

    //���Բ�����Χ�ĵ�����Ϣ
    String cmd = HQ20Locator.singleLocator();
    System.out.println(cmd);

    //�����س�ʱ�趨��ʱ�ش�ʱ�����ʹ���
    cmd = HQ20Locator.multiLocator("23", "34");
    System.out.println("multiLocator:" + cmd);
  }
}
