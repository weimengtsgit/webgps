package com.autonavi.directl.parse;

 
import java.text.NumberFormat;
import java.io.*;

import com.autonavi.directl.Log;
import com.autonavi.directl.dbutil.DbOperation;
 

public class ParseUtility {
  public ParseUtility() {
  }

  public static void main(String[] args) {
//    ParseUtility parseutility = new ParseUtility();
//    System.out.println(parseutility.formatXtoDu("116590010"));
//    System.out.println(parseutility.formatYtoDu("39590010"));
//    String abc = parseutility.getSimBySn("355632000688565");
//    System.out.println(abc);
    String t = "%%";
    byte[] bs = t.getBytes();
 
  }

  public static String[] split(String srcStr, String delim) {
    java.util.StringTokenizer stk = new java.util.StringTokenizer(srcStr, delim);
    String rStr[] = new String[stk.countTokens()];
    int index = 0;
    while (stk.hasMoreTokens()) {
      rStr[index] = stk.nextToken();
      index++;
    }
    return rStr;
  }

  /**
   * 分析GPS回传的内容，得到GPS类型
   * @param gpsContent String
   * @return String
   */
  public static String getGPSType(String gpsContent) {
    String gpsType = null;
    if (gpsContent != null) {

      if (gpsContent.startsWith("DG") || gpsContent.startsWith("DM")) {
        gpsType = "GP-POLICE-001";
      } else if (gpsContent.substring(0, 5).equalsIgnoreCase("*HQ20")) {
//        gpsType = "GP-HQ-20";
        gpsType = "GP-SZHQ-GPRS";
      } else if (gpsContent.startsWith("GDAT") || gpsContent.startsWith("MDAT")) {
        gpsType = "GP-TZTF-GPRS";
      } else if (gpsContent.startsWith("M")) {
        gpsType = "GP-TZ-001";
      } else if (gpsContent.startsWith("<")) {
        gpsType = "GP-YM-2000";
      } else if (gpsContent.startsWith("$") && gpsContent.endsWith("!")) {
        gpsType = "GP-TR-102";
      } else if (gpsContent.startsWith("(ONE")) {
        gpsType = "GP-SEGEM-SM";
      } else if (gpsContent.startsWith("[")) {
        gpsType = "GP-SEGEM-GPRS";
      } else if (gpsContent.startsWith(">RTKP")) {
        gpsType = "GP-TRIMTRAC-SM";
      } else if (gpsContent.startsWith("**")) {
        gpsType = "GP-VIKON-SM";
      } else if (gpsContent.startsWith("$GPRMC")) {
        gpsType = "GP-BC-SM";
      } else if (gpsContent.startsWith("#GPUSR") ||
                 gpsContent.startsWith("#GPGPS") ||
                 gpsContent.startsWith("#GPSHK") ||
                 gpsContent.startsWith("#CKMSG")) {
        gpsType = "GP-XINKE-GPRS";
      } else if (gpsContent.startsWith("@@Pe")) {
        gpsType = "GP-AVLS50-GPRS";
      } else if (gpsContent.substring(8, 20).equalsIgnoreCase("7E7E00007E7E")) {
        gpsType = "GP-STRAY-SM";
      } else if (gpsContent.startsWith("*D") || gpsContent.startsWith("*Y") ||
                 gpsContent.startsWith("*P") || gpsContent.startsWith("*p") ||
                 gpsContent.startsWith("*S") || gpsContent.startsWith("*G") ||
                 gpsContent.startsWith("*R")) {
        gpsType = "GP-SDTX-SM";
      } else if (gpsContent.startsWith("%%")) {
        gpsType = "GP-SZHJ-GPRS";
      } else if (gpsContent.startsWith("Th")) {
        gpsType = "GP-SHQS-GPRS";
      }
    }
    return gpsType;
  }

  public static boolean isYMGPRS(byte[] bs) {
    boolean b = false;
    try {
      if (bs.length < 13) {
        return b;
      }
      byte[] bts = {
          bs[0]};
      String s = null;
      s = new String(bts, "GBK");
      if (!s.equalsIgnoreCase("$")) {
        return b;
      }
      int conlen = bs[13] & 0xFF;
      byte[] bcontent = new byte[conlen];
      int t = 13;
      for (int i = 0; i < conlen; i++) {
        t++;
        bcontent[i] = bs[t];
      }
      byte[] ed = new byte[2];
      ed[0] = bs[t + 3];
      ed[1] = bs[t + 4];

      if ( (ed[0] & 0xFF) != 13 || (ed[1] & 0xFF) != 10) {
        return b;
      }
    } catch (Exception ex) {
     // Log.getInstance().outYMLog("isYMGPRS ERR：" + ex.getMessage());
      b = false;
      return b;
    }
    b = true;
    return b;
  }

  public static boolean isYM(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (line.startsWith("YMGPS")) {
        b = true;
      }
    } catch (UnsupportedEncodingException ex) {
    }
    return b;
  }

  public static boolean isSegemGPRSLogin(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (!line.startsWith("[")) {
        return false;
      }
      //Log.getInstance().outLog("Segem GPRS Login:proID="+(int)bs[1] );
      if (bs[1] == (byte) 0x80 || bs[1] == (byte) 0x01 || bs[1] == (byte) 0xF0) {
        // Log.getInstance().outLog("Segem GPRS Login:true" );
        return true;
      }
      //Log.getInstance().outLog("Segem GPRS Login:false" );
    } catch (UnsupportedEncodingException ex) {
    }
    return b;
  }

  public static String getGPSSN(String gpsContent) {
    String gpssn = null;
    if (gpsContent == null) {
      return null;
    }
    if (gpsContent.startsWith("$") && !gpsContent.startsWith("$OK!")) {
      //String abc1="$355632000688565,0,1,000000,000000,E0,N0,0,0,0,0*37!";
      int t = gpsContent.indexOf(",");
      gpssn = gpsContent.substring(1, t);
    } else if (gpsContent.startsWith("[")) { //赛格GPRS终端
      try {
        byte[] bs = gpsContent.getBytes("ISO8859-1"); // bs={'[',(byte)0x90,手机号.....};
        //Log.getInstance().outLog("Segem GPRS getGPSSN():proID2="+(int)bs[1] );
        if (bs[1] == (byte) 0x90) {
          byte[] lsh = {
              bs[2], bs[3], bs[4], bs[5], bs[6], bs[7], bs[8], bs[9], bs[10],
              bs[11]};
          gpssn = "1" + new String(lsh, "ISO8859-1");
        }
      } catch (UnsupportedEncodingException ex) {
      }
    }
    return gpssn;
  }

  public static String getSimBySn(String gpsSn) {
    String sim = null;
    java.sql.Statement stmt = null;
    java.sql.Connection conn = null;
    java.sql.ResultSet rs = null;
    String sql = null;
    try {
      conn = DbOperation.getConnection();//com.mapabc.db.DBConnectionManager.getInstance().getConnection();
      if (conn == null) {
        throw new Exception("");
      }
      stmt = conn.createStatement();
      sql =
          "select SIMCARD AS SIMCARD from t_terminal t WHERE DEVICE_ID ='" +
          gpsSn + "'";
      //System.out.println(sql);
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        sim = rs.getString("SIMCARD");
      }
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return null;
    } finally {
      //com.mapabc.db.DBConnectionManager.close(conn, stmt, rs);
    	DbOperation.release(stmt, rs, null, conn);
    }
    return sim;
  }

  public static String formatXtoDu(String DDDMMmmmmm) {
    String result = null;
    double DDD = Double.parseDouble(DDDMMmmmmm.substring(0, 3));
    double MMmmmm = Double.parseDouble(DDDMMmmmmm.substring(3, 5) + "." +
                                       DDDMMmmmmm.substring(5, 9));
    MMmmmm = MMmmmm / 60;
    NumberFormat format = NumberFormat.getNumberInstance();
    format.setMaximumFractionDigits(6);
    format.setMinimumFractionDigits(6);
    result = format.format(DDD + MMmmmm);
    return result;
  }

  public static String formatYtoDu(String DDMMmmmmm) {
    String result = null;
    double DDD = Double.parseDouble(DDMMmmmmm.substring(0, 2));
    double MMmmmm = Double.parseDouble(DDMMmmmmm.substring(2, 4) + "." +
                                       DDMMmmmmm.substring(4, 8));
    MMmmmm = MMmmmm / 60;
    NumberFormat format = NumberFormat.getNumberInstance();
    format.setMaximumFractionDigits(6);
    format.setMinimumFractionDigits(6);
    result = format.format(DDD + MMmmmm);
    return result;
  }

  public static byte[] fromHexString(String s) {
    int stringLength = s.length();
    if ( (stringLength & 0x1) != 0) {
      throw new IllegalArgumentException(
          "fromHexString   requires   an   even   number   of   hex   characters");
    }
    byte[] b = new byte[stringLength / 2];

    for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
      int high = charToNibble(s.charAt(i));
      int low = charToNibble(s.charAt(i + 1));
      b[j] = (byte) ( (high << 4) | low);
    }
    return b;
  }

  private static int charToNibble(char c) {
    if ('0' <= c && c <= '9') {
      return c - '0';
    } else if ('a' <= c && c <= 'f') {
      return c - 'a' + 0xa;
    } else if ('A' <= c && c <= 'F') {
      return c - 'A' + 0xa;
    } else {
      throw new IllegalArgumentException("Invalid   hex   character:   " + c);
    }
  }

  public static String bytes2BinaryString(byte[] bs) {
    String ret = "";
    for (int i = 0; i < bs.length; i++) {
      byte b = bs[i];
      String tmp = Integer.toBinaryString(b);
      while (tmp.length() < 8) {
        tmp = "0" + tmp;
      }
      ret = ret + tmp;
    }
    return ret;
  }

  public static boolean isMapabcSM(String psmcontent) {
    if (psmcontent.startsWith("MAPABCSM")) {
      return true;
    }
    return false;
  }

  public static String getSimcard(String psmcontent) {
    psmcontent = psmcontent.substring(8);
    String simCard = psmcontent.substring(0, psmcontent.indexOf("MAPABCSM"));
    return simCard;
  }

  public static String getSMContent(String psmcontent) {
    psmcontent = psmcontent.substring(8);
    String content = psmcontent.substring(psmcontent.indexOf("MAPABCSM") + 8);
    return content;
  }

  //是否是北京天行九洲GPRS终端
  public static boolean isTXJZ(byte[] bs) {
    boolean b = false;
    String s = "";
    for (int i = 0; i < bs.length; i++) {
      String tmp = Integer.toHexString(bs[i] & 0xff).toUpperCase();
      if (tmp.length() < 2) {
        tmp = "0" + tmp;
      }
      s = s + tmp;
    }
//FFFF5633301019211103133103959083111620571600000538000020202
    if (s.startsWith("FFFF5633301019")) { //相当于0xffff+V30+0x1019
      b = true;
    }
    return b;
  }

  //是否是新科GPRS
  public static boolean isXINKE(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (line.startsWith("#GPUSR") || line.startsWith("#GPGPS") ||
          line.startsWith("#GPSHK") || line.startsWith("#CKMSG")) {
        b = true;
      }
    } catch (UnsupportedEncodingException ex) {
      Log.getInstance().outLog("isXINKE ERR：" + ex.getMessage());
    }
    return b;
  }

  //是否是AVSL50
  public static boolean isAVSL50(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (line.startsWith("@@Pe")) {
        b = true;
      }

    } catch (UnsupportedEncodingException ex) {
      Log.getInstance().outLog("isAVSL50 ERR：" + ex.getMessage());
    }
    return b;
  }

  //是否是TZTF
  public static boolean isTZTF(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (line.startsWith("GDAT") || line.startsWith("MDAT")) {
        b = true;
      }
    } catch (UnsupportedEncodingException ex) {
      Log.getInstance().outLog("isTZTF ERR：" + ex.getMessage());
    }
    return b;
  }

  //是否是Sege
  public static boolean isSegemGPRS(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (line.startsWith("[")) {
        b = true;
      }
    } catch (UnsupportedEncodingException ex) {
      Log.getInstance().outLog("isSegemGPRS ERR：" + ex.getMessage());
    }
    return b;
  }

  //是否是BTC
  public static boolean isBTC(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (line.startsWith("^")) {
        b = true;
      }
    } catch (UnsupportedEncodingException ex) {
      Log.getInstance().outLog("isBTC ERR：" + ex.getMessage());
    }
    return b;
  }

  //是否是深圳慧晶
  public static boolean isSZHJ(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (line.startsWith("%%")) {
        b = true;
      }
    } catch (UnsupportedEncodingException ex) {
      Log.getInstance().outLog("isBTC ERR：" + ex.getMessage());
    }
    return b;
  }

  //是否是上海乾神
  public static boolean isSHQS(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (line.startsWith("Th")) {
        b = true;
      }
    } catch (UnsupportedEncodingException ex) {
      Log.getInstance().outLog("isSHQS ERR：" + ex.getMessage());
    }
    return b;
  }

  //是否是中交信科终端
  public static boolean isCCTI(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (line.startsWith("@") && !line.startsWith("@@")) {
        b = true;
      }
    } catch (UnsupportedEncodingException ex) {
      Log.getInstance().outLog("isCCTI ERR：" + ex.getMessage());
    }
    return b;
  }

  //是否是山东天旋终端
  public static boolean isSDTX(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (line.startsWith("Th")) {
        b = true;
      }
    } catch (UnsupportedEncodingException ex) {
      Log.getInstance().outLog("isSDTX ERR：" + ex.getMessage());
    }
    return b;
  }

//是否是金士达GPRS
  public static boolean isStray(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (line.startsWith("$SJHX,") || line.startsWith("$SJHXR")) {
        b = true;
      }
    } catch (UnsupportedEncodingException ex) {
      Log.getInstance().outLog("isStray ERR：" + ex.getMessage());
    }
    return b;
  }

  //是否是维康GPRS
  public static boolean isViKON(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (line.startsWith("**")) {
        b = true;
      }
    } catch (UnsupportedEncodingException ex) {
      Log.getInstance().outLog("is ViKon ERR：" + ex.getMessage());
    }
    return b;
  }

//是否是神达GPS手机
  public static boolean isShenDa(byte[] bs) {
    boolean b = false;
    try {
      String line = new String(bs, "ISO8859-1");
      if (line.startsWith("SDGPS")) {
        b = true;
      }
    } catch (UnsupportedEncodingException ex) {
      Log.getInstance().outLog("is isShenDa ERR：" + ex.getMessage());
    }
    return b;
  }

}
