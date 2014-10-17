package com.sosgps.wzt.commons.util;

import java.security.MessageDigest;

/**
 * @Title: MD5Util.java
 * @Description:
 * @Copyright:
 * @Date: 2008-8-16 ����11:35:59
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yanglei
 * @version 1.0
 */

/*******************************************************************************
 * md5 ��ʵ����RSA Data Security, Inc.���ύ��IETF ��RFC1321�е�MD5 message-digest �㷨��
 ******************************************************************************/

public class MD5Util {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"a", "b", "c", "d", "e", "f" };

	/**
	 * ת���ֽ�����Ϊ16�����ִ� 
	 * @param b �ֽ�����
	 * @return 16�����ִ�
	 */

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
             ex.printStackTrace();
		}
		return resultString;
	}

	/**
	 * @param args void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
