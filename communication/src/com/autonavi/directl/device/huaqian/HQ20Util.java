package com.autonavi.directl.device.huaqian;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class HQ20Util {
  private static char[] CHANG_256_64;
  private static String protocolHead = "*HQ20";
  private static String protocolEnd = "#";
  private static String spProtocolHead = "[HQ20";
  private static String spProtocolEnd = "]";

  static { //��ʼ��CHANG_256_64
    CHANG_256_64 = new char[64];
    for (int i = 0; i <= 9; i++) {
      CHANG_256_64[i] = (char) ('0' + i);
    }
    CHANG_256_64[10] = ':';
    CHANG_256_64[11] = ';';
    for (int i = 0; i < 26; i++) {
      CHANG_256_64[i + 12] = (char) ('A' + i);
    }
    for (int i = 0; i < 26; i++) {
      CHANG_256_64[i + 38] = (char) ('a' + i);
    }
  }

  /**
   * 256<-->64ת���㷨
   * @param text String ԭʼ����
   * @return String ת���������
   */
  public static String to64Code(String text) {
    //rebuild short message
    StringBuffer hexbuf = new StringBuffer();
    StringBuffer sixbuf = new StringBuffer();
    byte[] bytes = text.getBytes();
    for (int i = 0; i < bytes.length; i++) {
      String str = Integer.toHexString( (char) bytes[i]);
      if (str.length() == 2) {
        hexbuf.append(str);
      } else {
        hexbuf.append(str.substring(2));
      }
    }
    int loopnum = hexbuf.length() / 3;
    int leave = hexbuf.length() % 3;
    for (int i = 0; i < loopnum; i++) {
      int n = Integer.parseInt(hexbuf.substring(i * 3, i * 3 + 3), 16);
      sixbuf.append( (char) CHANG_256_64[n / 64]);
      sixbuf.append( (char) CHANG_256_64[n % 64]);
    }
    if (leave > 0) {
      int n = Integer.parseInt(hexbuf.substring(loopnum * 3), 16);
      if (leave == 1) {
        n *= 4;
        sixbuf.append( (char) CHANG_256_64[n]);
      } else {
        n *= 16;
        sixbuf.append( (char) CHANG_256_64[n / 64]);
        sixbuf.append( (char) CHANG_256_64[n % 64]);
      }
    }
    return sixbuf.toString();

  }

  /**
   * ���ɿ���ָ��
   * @param cmdKey String ԭʼ����ָ��
   * @param save boolean �Ƿ񱣴�
   * @param replay boolean �Ƿ���Ҫ��Ӧ
   * @return String ���˰�ͷ��β��ָ��
   */
  public static String makeCommandStr(String cmdKey, boolean save, boolean replay) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(protocolHead);
    buffer.append(save ? '1' : '0');
    buffer.append(replay ? '1' : '0');
    buffer.append(cmdKey);
    buffer.append(protocolEnd);
    return buffer.toString();
  }

  /**
   * ���ɿ���ָ��
   * @param cmdKey String ԭʼ����ָ��
   * @param save boolean �Ƿ񱣴�
   * @param replay boolean �Ƿ���Ҫ��Ӧ
   * @return String ���˰�ͷ��β��ָ��
   */
  public static String makespCommandStr(String cmdKey, boolean save, boolean replay) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(spProtocolHead);
    buffer.append(save ? '1' : '0');
    buffer.append(replay ? '1' : '0');
    buffer.append(cmdKey);
    buffer.append(spProtocolEnd);
    return buffer.toString();
  }

  /**
   * ���ַ��������ָ�����ȣ�����ķ�ʽ����ǰ������'0'
   * @param org String
   * @param length int
   * @return String
   */
  public static String extentString(String org, int length) {
    if (org == null) {
      return null;
    }

    if (org.length() == length) {
      return org;
    }

    StringBuffer buf = new StringBuffer();
    int num = length - org.length();
    for (int i = 0; i < num; i++) {
      buf.append('0');
    }
    buf.append(org);
    return buf.toString();
  }

  /**
   * ��ʽ����γ�����꣬ʹ���ʽΪγ�ȣ���aa\u00BAaa.aaaa'�����ȣ���ooo\u00BAoo.oooo'��
   * @param value double ��γ��ֵ
   * @param type int 0�����ȣ�1����γ��
   * @return String ��ʽ������ַ���
   */
  public static String getLatLongString(double value, int type) {
    StringBuffer buf = new StringBuffer();
    int du = (int) value;
    int length = (type == 0) ? 3 : 2;
    buf.append(extentString(Integer.toString(du), length));
    int fen = (int) ( (value - du) * 60.0);
    buf.append(extentString(Integer.toString(fen), 2));
    int miao = (int) Math.round( ( (value - du) * 60.0 - fen) * 10000);
    buf.append(extentString(Integer.toString(miao), 4));
    return buf.toString();
  }

  /**
   * ���ַ����н�������γ��ֵ
   * @param value String �����о�γ��ֵ���ַ���
   * @param type int 0�����ȣ�1����γ��
   * @return double ���Ȼ���γ��
   */
  public static double getLatLongValue(String value, int type) {
    if (type == 0) { //��������
      int du = Integer.parseInt(value.substring(0, 3));
      int fen = Integer.parseInt(value.substring(3, 5));
      int miao = Integer.parseInt(value.substring(5));
      double longitude = du + (fen + miao / 10000.0) / 60.0;
      return longitude;
    }
    if (type == 1) {
      int du = Integer.parseInt(value.substring(0, 2));
      int fen = Integer.parseInt(value.substring(2, 4));
      int miao = Integer.parseInt(value.substring(4));
      double latitude = du + (fen + miao / 10000.0) / 60.0;
      return latitude;
    }
    return 0.0;
  }

  /**
   * �ӽڻ���ɹ���/Сʱ��λ
   * һ�ڣ�һ����/Сʱ��1.852����/Сʱ
   * [��]��Ϊ�ִ������ٶȵĵ�λ��������Ҳ��춷缰�������ٶȡ�
   * ���ڡ��Ĵ�����Ӣ�ġ�Knot������ָ������������γ��1�ֵĳ��ȣ����ڵ����Գ�������״��
   * ��ͬγ�ȴ���1�ֻ������в��졣�ڳ����1����Լ����1843�ף�γ��45�㴦Լ����1852.2 �ף�
   * ����Լ����1861.6 �ס�1929�����ˮ�ĵ���ѧ���飬ͨ����1��ƽ������1852����Ϊ1���
   * 1948�����������ȫ������ϣ�1852�׻�6O76.115Ӣ��Ϊ1���
   * �ʹ����ϲ���1852��Ϊ��׼���ﳤ�ȡ�
   * �й�������һ��׼���ô��š�M����ʾ��
   * @param value double �Խ�Ϊ��λ��ʾ����ֵ
   * @return double �Թ���/СʱΪ��λ��ʾ����ֵ
   */
  public static double Knot2KMHOUR(double value) {
    return value * 1.852;
  }

  /**
   * �ӹ���/Сʱ����ɽڵ�λ
   * @param value double �Թ���/СʱΪ��λ��ʾ����ֵ
   * @return double �Խ�Ϊ��λ��ʾ����ֵ
   */
  public static double KMHOUR2Knot(double value) {
    return value / 1.852;
  }

  public static void main(String[] args) {
    HQ20Util hq20util = new HQ20Util();
//        String msg = "���ڷǵ�������Ƶĺܺã�������һ��ҽ����Ա�ܵ���Ⱦ";
    String msg = "��A";
    String msgCode2 = HQ20Util.to64Code(msg);
    int n = 0;

    //���Ծ�γ�ȱ���ͽ���
    double latitude = 12.2067209;
    double longitude = 23.5867292;
    String longStr = HQ20Util.getLatLongString(longitude, 0);
    double longCheck = HQ20Util.getLatLongValue(longStr, 0);
    if (Math.abs(longCheck - longitude) < 0.00001) {
      System.out.println("check longitude change ok");
    } else {
      System.out.println("check longitude change error");
    }

    String latStr = HQ20Util.getLatLongString(latitude, 1);
    double latCheck = HQ20Util.getLatLongValue(latStr, 1);
    if (Math.abs(latCheck - latitude) < 0.00001) {
      System.out.println("check latitude change ok");
    } else {
      System.out.println("check latitude change error");
    }

  }
}
