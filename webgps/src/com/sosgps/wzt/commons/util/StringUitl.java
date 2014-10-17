package com.sosgps.wzt.commons.util;

import java.io.UnsupportedEncodingException;

/**
 * @Title: StringUitl.java
 * @Description: 字符串操作工具集
 * @Copyright:
 * @Date: 2008-8-10 下午03:50:36
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yanglei
 * @version 1.0
 */
public class StringUitl {
	public StringUitl() {

	}

	/**
	 * @see 判断字符串是否为空或Null
	 * @param str
	 * @return boolean
	 */
	public static boolean isNullOrBlank(String str) {
		return (str == null || str.trim().length() == 0 ? true : false);
	}

	/**
	 * 对字符串进行转码
	 * @param oldString
	 * @return 返回转码后的字符串，默认字符集"ISO8859-1"到"GB2312"
	 */
	public static String encoding(String oldString) {
		try {
			return new String(oldString.getBytes("ISO8859-1"), "GB2312");
		} catch (UnsupportedEncodingException ex) {
			return "";
		}
	}

	/**
	 * 对字符串进行转码.
	 * @param oldString
	 * @return 返回转码后的字符串，默认是GB2312.
	 */
	public static String encodeURL(String oldString) {
		try {
			return java.net.URLEncoder.encode(oldString, "GB2312");
		} catch (UnsupportedEncodingException ex) {
			return "";
		}
	}

	/**
	 * 对字符串进行转码
	 * @param oldString
	 * @param oldCharsetName 原字符集
	 * @param newCharsetName 新字符集
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String encoding(String oldString, String oldCharsetName, String newCharsetName)
			throws UnsupportedEncodingException {
		return new String(oldString.getBytes(oldCharsetName), newCharsetName);
	}

	/**
	 * Function: 得到一个字符串的前n个字符 
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

	/* 人民币小写转大写-------------begin */
	public static String numtochinese(String input) {
		String s1 = "零壹贰叁肆伍陆柒捌玖";
		String s4 = "分角整元拾佰仟万拾佰仟亿拾佰仟";
		String temp = "";
		String result = "";
		if (input == null)
			return "输入字串不是数字串只能包括以下字符（'0'～'9'，'.')，输入字串最大只能精确到仟亿，小数点只能两位！";
		temp = input.trim();

		try {
			Float.parseFloat(temp);
		} catch (Exception e) {
			return "输入字串不是数字串只能包括以下字符（'0'～'9'，'.')，输入字串最大只能精确到仟亿，小数点只能两位！";
		}
		int len = 0;
		if (temp.indexOf(".") == -1)
			len = temp.length();
		else
			len = temp.indexOf(".");
		if (len > s4.length() - 3)
			return ("输入字串最大只能精确到仟亿，小数点只能两位！");
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
			result = result.concat("整");
		if (len == temp.length() - 2)
			result = result.concat("零分");
		return result;
	}

	/**
	 * @param args void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
