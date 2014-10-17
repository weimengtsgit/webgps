package com.autonavi.directl;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.autonavi.directl.dbutil.DbOperation;
import com.autonavi.lbsgateway.poolsave.GpsData;
import com.mapabc.geom.CoordCvtAPI;

public class Tools {
	public Tools() {
	}

	// �Ѹ�ʽΪ�ȷֵľ���ת���ɶ�
	public static String formatXtoDu(String DDDMMmmmmm) {
		String result = null;
		double DDD = Double.parseDouble(DDDMMmmmmm.substring(0, 3));
		double MMmmmm = Double.parseDouble(DDDMMmmmmm.substring(3, 5) + "."
				+ DDDMMmmmmm.substring(5, 9));
		MMmmmm = MMmmmm / 60;
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(6);
		format.setMinimumFractionDigits(6);
		result = format.format(DDD + MMmmmm);
		return result;
	}

	// �Ѹ�ʽΪ�ȷֵ�γ��ת���ɶ�
	public static String formatYtoDu(String DDMMmmmmm) {
		String result = null;
		double DDD = Double.parseDouble(DDMMmmmmm.substring(0, 2));
		double MMmmmm = Double.parseDouble(DDMMmmmmm.substring(2, 4) + "."
				+ DDMMmmmmm.substring(4, 8));
		MMmmmm = MMmmmm / 60;
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(6);
		format.setMinimumFractionDigits(6);
		result = format.format(DDD + MMmmmm);
		return result;
	}

	// �Ѷ�ת����16���ƺ���
	public static String Du2Second(String DDMMmmmmm) {
		String result = null;
		double DDD = Double.parseDouble(DDMMmmmmm);
		double ms = DDD * 60 * 60 * 1000;
		DecimalFormat format = new DecimalFormat("0");
		String ss = format.format(ms);
		String hex = Integer.toHexString(Integer.parseInt(ss));
		return hex;
	}

	// ��16���Ƶ��ַ�ת���� byte����
	public static byte[] fromHexString(String s) {
		int stringLength = s.length();
		if ((stringLength & 0x1) != 0) {
			Log.getInstance().errorLog(s, null);
			throw new IllegalArgumentException(
					"fromHexString   requires   an   even   number   of   hex   characters");
		}
		byte[] b = new byte[stringLength / 2];

		for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
			int high = charToNibble(s.charAt(i));
			int low = charToNibble(s.charAt(i + 1));
			b[j] = (byte) ((high << 4) | low);
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
			throw new IllegalArgumentException("Invalid   hex   character:   "
					+ c);
		}
	}

	// ��byte����ת����16�����ַ�
	public static String bytesToHexString(byte[] bs) {
		String s = "";
		for (int i = 0; i < bs.length; i++) {
			String tmp = Integer.toHexString(bs[i] & 0xff);
			if (tmp.length() < 2) {
				tmp = "0" + tmp;
			}
			s = s + tmp;
		}
		return s;
	}

	// �� byte����ת���ɶ����Ʊ�ʾ���ַ�
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

	/**
	 * ����ʱ������� time=055727.747 (ǰ��λСʱ05��57���ӣ�27�룬С�������Բ�Ҫ) date=110507
	 * (ǰ��λ��11��05�£�07��)
	 * 
	 * @param time
	 *            String
	 * @param date
	 *            String
	 * @return String
	 */
	public static String conformtime(String time, String date) {
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
			car.add(Calendar.HOUR, 8);
			Date newDate = new Date(car.getTimeInMillis());
			return simpleDate.format(newDate);
		} catch (Exception ex) {
			Log.getInstance().errorLog("GPSʱ��ת������", ex);
		}
		return null;
	}

	// ��int ת���� 4λ byte����
	public static byte[] intToBytes(int n) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (n >> (24 - i * 8));
		}
		return b;
	}

	// byte to int
	public static int byte2Int(byte[] decBytes) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < decBytes.length; i++) {
			String s = Integer.toHexString(decBytes[i] & 0xFF);
			if (s.length() < 2) {
				s = "0" + s;
			}
			str.append(s);
		}
		int ret = Integer.parseInt(str.toString(), 16);
		return ret;
	}

	// byte to long
	public static long byte2Long(byte[] decBytes) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < decBytes.length; i++) {
			String s = Integer.toHexString(decBytes[i] & 0xFF);
			if (s.length() < 2) {
				s = "0" + s;
			}
			str.append(s);
		}
		long ret = Long.parseLong(str.toString(), 16);
		return ret;
	}

	// ������numת���ɳ���Ϊws��ʮ�����Ƶ��ַ�
	public static String int2Hexstring(int num, int ws) {
		String intHex = Integer.toHexString(num);
		while (intHex.length() < ws) {
			intHex = "0" + intHex;
		}
		return intHex;
	}

	public static String convertToHex(String num, int n) {
		String temp = "";
		int i = Integer.parseInt(num);
		String hex = Integer.toHexString(i).toUpperCase();
		if (hex.length() > n) {
			int off = 0;
			while (off < n) {
				temp = temp + "F";
				off++;
			}
			return temp;
		} else if (hex.length() < n) {
			while (hex.length() < n) {
				hex = "0" + hex;
			}
			temp = hex;
		} else {
			temp = hex;
		}
		return temp;
	}

	// �Ѹ�ʽΪ�ȷֵ�γ��ת���ɶ�
	public static String formatYtoDu1(String DDMMmmmmm) {
		String result = null;
		double DDD = Double.parseDouble(DDMMmmmmm.substring(0, 2));
		double MMmmmm = Double.parseDouble(DDMMmmmmm.substring(2, 9));
		MMmmmm = MMmmmm / 60;
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(6);
		format.setMinimumFractionDigits(6);
		result = format.format(DDD + MMmmmm);
		return result;
	}

	// ������ ��ʽΪ�ȷֵľ���ת���ɶ�
	public static String formatXtoDu1(String DDDMMmmmmm) {
		String result = null;
		double DDD = Double.parseDouble(DDDMMmmmmm.substring(0, 3));
		double MMmmmm = Double.parseDouble(DDDMMmmmmm.substring(3, 10));
		MMmmmm = MMmmmm / 60;
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(6);
		format.setMinimumFractionDigits(6);
		result = format.format(DDD + MMmmmm);
		return result;
	}

	/**
	 * ��GPS�ϱ����ݵ�ʱ���Ǹ�������ʱ�䣬+8������ʵʱ�䡣
	 * 
	 * @param time
	 *            --- ����yyyy-MM-dd HH:mm:ss����ʽ��GPSʱ�䡣
	 * @return
	 */
	public static String getGPSCurrentTime(String time) {
		SimpleDateFormat simpleDate = null;
		Date newDate = null;
		try {
			simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = simpleDate.parse(time);
			Calendar car = Calendar.getInstance();
			car.setTime(date);
			car.add(Calendar.HOUR, 8);
			newDate = new Date(car.getTimeInMillis());
		} catch (java.text.ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return simpleDate.format(newDate);
	}

	// �õ���ǰHHMMSS
	public static String getCurHMS() {
		String ret = "";
		Date date = new Date();
		Calendar calend = Calendar.getInstance();
		calend.setTime(date);
		SimpleDateFormat simpleDate = null;
		simpleDate = new SimpleDateFormat("HHmmss");
		ret = simpleDate.format(date);
		return ret;
	}

	// ZLIBѹ��16���ƴ�
	public static String compressHexData(String input) {
		try {

			Deflater compresser = new Deflater(Deflater.BEST_COMPRESSION);
			byte[] inputB = Tools.fromHexString(input);

			byte[] output = new byte[512];
			compresser.setInput(inputB);
			compresser.finish();
			int compressedDataLength = compresser.deflate(output);
			// Log.getInstance().outXWRJLoger("ѹ��16���ƴ������"+(Tools.bytesToHexString(output)));
			byte[] out = new byte[compressedDataLength];
			System.arraycopy(output, 0, out, 0, out.length);
			return Tools.bytesToHexString(out);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "UNZIP_ERR";
		}

	}

	// ZLIB��ѹ��16���ƴ�
	public static String decompressHexData(String input) {
		try {

			byte[] output = Tools.fromHexString(input);
			// Decompress the bytes
			Inflater decompresser = new Inflater();
			decompresser.setInput(output);
			byte[] result = new byte[input.length()];
			int resultLength = decompresser.inflate(result);
			decompresser.end();
			byte[] out = new byte[resultLength];
			System.arraycopy(result, 0, out, 0, resultLength);
			// Decode the bytes into a String
			String outputString = Tools.bytesToHexString(out);
			// Log.getInstance().outXWRJLoger("��ѹ��16���ƴ������"+(Tools.bytesToHexString(output)));

			return outputString;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "UNZIP_ERR";
		}
	}

	/**
	 * @��������: BCD��תΪ10���ƴ�(����������)
	 * @�������: BCD��
	 * @������: 10���ƴ�
	 */
	public static String bcd2Str(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
				.toString().substring(1) : temp.toString();
	}

	/**
	 * @��������: 10���ƴ�תΪBCD��
	 * @�������: 10���ƴ�
	 * @������: BCD��
	 */
	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;

		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}

		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}

		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;

		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}

			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}

			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	// �������
	// public static String getEncrptCoord(double n) {
	// try {
	// return com.mapabc.encrptor.EncrptorFactory.getInstance().
	// getStrFormCoordinate(n, 0);
	// }
	// catch (Exception ex) {
	// ex.printStackTrace();
	// return "";
	// }
	// }
	// �������
	// public static double getCoordinate(String encrptValue) {
	// try {
	// return
	// com.mapabc.encrptor.EncrptorFactory.getInstance().getCoordinate(encrptValue);
	// }
	// catch (Exception ex) {
	// ex.printStackTrace();
	// return 0;
	// }
	// }

	// ��sege���յ�����Ϣ���з�ת��
	public static String reverseFormat(String str) {

		String ret = "";
		String content = str;
		StringBuffer buf = new StringBuffer();
		if (!str.startsWith("5b") || str.lastIndexOf("5d") != str.length() - 2) {
			Log.getInstance().outLog("���������ָ�����ݲ�����:" + str);
			return "";
		}
		for (int i = 0; i < str.length(); i = i + 2) {
			if (i + 2 > str.length()) {
				break;
			}
			if (str.substring(i, i + 2).equalsIgnoreCase("5c")) {
				String s = str.substring(i + 2, i + 4);
				byte resu = (byte) getReverseValue(Tools.fromHexString(s)[0]);
				byte[] rs = new byte[1];
				rs[0] = resu;
				String s1 = Tools.bytesToHexString(rs);
				buf.append(s1);
				i = i + 2;
			} else {
				buf.append(str.substring(i, i + 2));
			}
		}

		return buf.toString();
	}
	
	public static String getRandomString(int size) {
		// String
		// seed="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String seed = "0123456789";
		byte chs[] = seed.getBytes();
		byte bs[] = new byte[size];
		Random random = new Random();
		int length = chs.length;
		for (int i = 0; i < size; i++) {
			bs[i] = chs[random.nextInt(length)];
		}
		return new String(bs);
	}

	// sege
	public static int getReverseValue(byte b) {
		int ret = 0;
		ret = b ^ (byte) 0x50;
		return ret;
	}

	// �롪��ȫ��ת��
	public static final String BQchange(String QJstr) {
		String outStr = "";
		String Tstr = "";
		byte[] b = null;

		for (int i = 0; i < QJstr.length(); i++) {
			try {
				Tstr = QJstr.substring(i, i + 1);
				b = Tstr.getBytes("unicode");
				System.out.println("----" + Tools.bytesToHexString(b));
			} catch (java.io.UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (b[3] != -1) {
				b[2] = (byte) (b[2] - 32);
				b[3] = -1;
				try {
					outStr = outStr + new String(b, "unicode");
				} catch (java.io.UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				outStr = outStr + Tstr;
			}
		}
		return outStr;
	}

	// ��ȡ���ݿ⵱ǰ����
	public static String getDBCurDate() {
		String date = "";
		// Connection conn = com.mapabc.db.DBConnectionManager.getInstance().
		// getConnection();
		Connection conn = DbOperation.getConnection();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("select sysdate from sys.dual");
			if (rs.next()) {
				date = rs.getString("sysdate").substring(0, 19);
			}
		} catch (SQLException ex) {
			Log.getInstance().errorLog("��ѯ���ݿ�ϵͳʱ���쳣��" + ex.getMessage(), ex);
		} finally {
			DbOperation.release(st, rs, null, conn);
			// com.mapabc.db.DBConnectionManager.close(conn, st, rs);
		}
		return date;

	}

	/**
	 * ���ַ���ת��ΪDate
	 * 
	 * @param date
	 *            String:����
	 * @param format
	 *            String�����ڸ�ʽ
	 * @return Date
	 */
	public static Date formatStrToDate(String date, String format) {
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			d = sdf.parse(date);
		} catch (ParseException ex) {
		}
		return d;
	}

	/**
	 * ��Dateת��Ϊ�ַ���
	 */
	public static String formatDate2Str(Date date, String format) {
		String d = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		d = sdf.format(date);

		return d;
	}

	private static final String Algorithm = "DESede"; // ���� �����㷨,����

	// DES,DESede,Blowfish

	// keybyteΪ������Կ������Ϊ24�ֽ�
	// srcΪ�����ܵ����ݻ�������Դ��
	public static byte[] encryptMode(byte[] keybyte, byte[] src) {
		try {
			// ������Կ
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			// ����
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// keybyteΪ������Կ������Ϊ24�ֽ�
	// srcΪ���ܺ�Ļ�����
	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
		try {
			// ������Կ
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			// ����
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// ��У��ͣ���ת��Ϊ16����(��ͼ)
	public static String getVerfyCode(String cont) {
		String ret = "";
		byte[] br = cont.getBytes();
		int sum = 0;
		for (int i = 0; i < br.length; i++) {
			sum += br[i] & 0xFF;
		}
		ret = Integer.toHexString(sum).toUpperCase() + "\r\n";

		return ret;
	}

	// �Ѷ�ת����16���ƺ���
	public static String Du2Mills(String DDMMmmmmm) {
		String result = null;
		double DDD = Double.parseDouble(DDMMmmmmm);
		int ms = (int) (DDD * 60 * 60 * 1000);
		String hex = Integer.toHexString(ms);
		return hex.toUpperCase();
	}
	
	// ȥ���ַ���ǰ��0
	public static String removeZeroStr(String str) {
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
	public static String getNumberFormatString(double df, int maxfracDigit,
			int minfracDigit) {
		String ret = "";
		NumberFormat nf = NumberFormat.getInstance();

		nf.setMaximumFractionDigits(maxfracDigit);
		nf.setMinimumFractionDigits(minfracDigit);
		ret = nf.format(df).replaceAll("\\,", "");

		return ret;
	}
	
	//�����Ӳ����ȡ��ԭ��
	public static int getValueFromCompCode(String hexString){
		int ret = 0;
		int i = Integer.parseInt(hexString.substring(1), 16);
		int j = Integer.parseInt("fffffff", 16);
		int m = i^j;
		
		if (hexString.charAt(0)=='F'){//����
 			ret = -(m+1);
		} 
 		return ret;
		
 	}
	
	//�ѽ�ת���ɹ���
    public static String formatKnotToKm(String knot) {
    	if (knot == null || knot.trim().length()<=0){
    		return "0";
    	}
        String ret = "";
        double speed = 0;
        if (knot != null) {
            try {
                speed = Double.parseDouble(knot);
            } catch (java.lang.NumberFormatException ex) {
            	ex.printStackTrace();
            }
            speed = speed * 1.852;
        }
        ret = "" + speed;
 
        return ret;
    }
    
    //�ѹ���ת���ɽ�
    public static String formatKmToKnot(String km){
    	String knot = "";
    	double dSpeed = Double.parseDouble(km);
        if (dSpeed > 999) {
            dSpeed = 999;
        }
        double hSpeed = dSpeed / 1.852;
        int iSpeed = (int) hSpeed;
          knot = "" + iSpeed;
        
        return knot ;
    }
	
 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int len = 8;
		for (int i = 0; i < len; i++) {
			 
				
			if ((i+1) % 5 == 0) {
 			 System.out.println("�ύ��"+i);
 			 
			}else if (i == len-1){
				 
				System.out.println("�ύʣ��������"+(i));
				
			}
			
		}
		 
		 
	}
		

}
