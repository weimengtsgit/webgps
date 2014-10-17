package com.sosgps.wzt.commons.util;

import java.io.UnsupportedEncodingException;

/**
 * @Title: StringUitl.java
 * @Description: �ַ����������߼�
 * @Copyright:
 * @Date: 2008-8-10 ����03:50:36
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yanglei
 * @version 1.0
 */
public class StringUitl {
	public StringUitl() {

	}

	/**
	 * @see �ж��ַ����Ƿ�Ϊ�ջ�Null
	 * @param str
	 * @return boolean
	 */
	public static boolean isNullOrBlank(String str) {
		return (str == null || str.trim().length() == 0 ? true : false);
	}

	/**
	 * ���ַ�������ת��
	 * @param oldString
	 * @return ����ת�����ַ�����Ĭ���ַ���"ISO8859-1"��"GB2312"
	 */
	public static String encoding(String oldString) {
		try {
			return new String(oldString.getBytes("ISO8859-1"), "GB2312");
		} catch (UnsupportedEncodingException ex) {
			return "";
		}
	}

	/**
	 * ���ַ�������ת��.
	 * @param oldString
	 * @return ����ת�����ַ�����Ĭ����GB2312.
	 */
	public static String encodeURL(String oldString) {
		try {
			return java.net.URLEncoder.encode(oldString, "GB2312");
		} catch (UnsupportedEncodingException ex) {
			return "";
		}
	}

	/**
	 * ���ַ�������ת��
	 * @param oldString
	 * @param oldCharsetName ԭ�ַ���
	 * @param newCharsetName ���ַ���
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String encoding(String oldString, String oldCharsetName, String newCharsetName)
			throws UnsupportedEncodingException {
		return new String(oldString.getBytes(oldCharsetName), newCharsetName);
	}

	/**
	 * Function: �õ�һ���ַ�����ǰn���ַ� 
	 * @param: SourceString Stirng,SubStringLength int.
	 * @return: String
	 */
	public static String getSubStirng(String s, int endIndex) {
		int length = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) > 127) {
				length += 2;
			} else {
				length++;
			}

			if (endIndex < length) {
				s = s.substring(0, i);
				break;
			}
		}
		return s;
	}

	/* �����Сдת��д-------------begin */
	public static String numtochinese(String input) {
		String s1 = "��Ҽ��������½��ƾ�";
		String s4 = "�ֽ���Ԫʰ��Ǫ��ʰ��Ǫ��ʰ��Ǫ";
		String temp = "";
		String result = "";
		if (input == null)
			return "�����ִ��������ִ�ֻ�ܰ��������ַ���'0'��'9'��'.')�������ִ����ֻ�ܾ�ȷ��Ǫ�ڣ�С����ֻ����λ��";
		temp = input.trim();

		try {
			Float.parseFloat(temp);
		} catch (Exception e) {
			return "�����ִ��������ִ�ֻ�ܰ��������ַ���'0'��'9'��'.')�������ִ����ֻ�ܾ�ȷ��Ǫ�ڣ�С����ֻ����λ��";
		}
		int len = 0;
		if (temp.indexOf(".") == -1)
			len = temp.length();
		else
			len = temp.indexOf(".");
		if (len > s4.length() - 3)
			return ("�����ִ����ֻ�ܾ�ȷ��Ǫ�ڣ�С����ֻ����λ��");
		int n1 = 0;
		String num = "";
		String unit = "";

		for (int i = 0; i < temp.length(); i++) {
			if (i > len + 2) {
				break;
			}
			if (i == len) {
				continue;
			}
			n1 = Integer.parseInt(String.valueOf(temp.charAt(i)));
			num = s1.substring(n1, n1 + 1);
			n1 = len - i + 2;
			unit = s4.substring(n1, n1 + 1);
			result = result.concat(num).concat(unit);
		}
		if ((len == temp.length()) || (len == temp.length() - 1))
			result = result.concat("��");
		if (len == temp.length() - 2)
			result = result.concat("���");
		return result;
	}

	/**
	 * @param args void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
