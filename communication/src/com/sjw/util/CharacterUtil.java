package com.sjw.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * @Title:字符工具类
 * @Description:
 * @Company:
 * @author: 
 * @version 1.0
 * @date: 2010-3-17 下午03:12:07
 */
public class CharacterUtil {
	// 正则表达式
	static final String Digits = "(\\p{Digit}+)";// 十进制数字：[0-9]
	static final String HexDigits = "(\\p{XDigit}+)";// 十六进制数字：[0-9a-fA-F]
	// an exponent is 'e' or 'E' followed by an optionally
	// .
	static final String Exp = "[eE][+-]?" + Digits;//
	static final String fpRegex = ("[\\x00-\\x20]*" + // 开头可以有空格
			"[+-]?(" + // 正负符号
			"NaN|" + // "NaN" string
			"Infinity|" + // "Infinity" string
			// A decimal floating-point string representing a finite positive
			// number without a leading sign has at most five basic pieces:
			// Digits . Digits ExponentPart FloatTypeSuffix
			// 
			// Since this method allows integer-only strings as input
			// in addition to strings of floating-point literals, the
			// two sub-patterns below are simplifications of the grammar
			// productions from the Java Language Specification, 2nd
			// edition, section 3.10.2.

			// Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
			"(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +

	// . Digits ExponentPart_opt FloatTypeSuffix_opt
			"(\\.(" + Digits + ")(" + Exp + ")?)|" +

			// Hexadecimal strings
			"((" +
			// 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
			"(0[xX]" + HexDigits + "(\\.)?)|" +

			// 0[xX] HexDigits_opt . HexDigits BinaryExponent
			// FloatTypeSuffix_opt
			"(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

			")[pP][+-]?" + Digits + "))" + "[fFdD]?))" + "[\\x00-\\x20]*");// 结尾可以有空格
	static final String IntDigits = "[-]?" + Digits;// 整数
	static final String NegativeIntDigits = "[-]" + Digits;// 负整数

	/**
	 * 字符串转换为double，如果格式不匹配，返回默认值
	 * 
	 * @param s
	 * @param defaultValue
	 * @return
	 */
	public static double str2Double(String s, double defaultValue) {
		if (Pattern.matches(fpRegex, s)) {
			// Will not throw NumberFormatException
			return Double.valueOf(s);
		} else {
			// return default value
			return defaultValue;
		}
	}

	/**
	 * 字符串转换为int，如果格式不匹配，返回默认值
	 * 
	 * @param s
	 * @param defaultValue
	 * @return
	 */
	public static int str2Int(String s, int defaultValue) {
		if (Pattern.matches(IntDigits, s)) {
			return Integer.parseInt(s);
		} else {
			return defaultValue;
		}
	}

	/**
	 * 把byte数组转换成 16进制的字符
	 * 
	 * @param bs
	 * @return
	 */
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

	/**
	 * md5编码加密
	 * 
	 * @param originBytes
	 * @return
	 */
	public static String encryptByMd5(byte[] originBytes) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] encryptByte = md.digest(originBytes);
			return bytesToHexString(encryptByte);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
