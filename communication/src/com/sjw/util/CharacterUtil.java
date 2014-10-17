package com.sjw.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * @Title:�ַ�������
 * @Description:
 * @Company:
 * @author: 
 * @version 1.0
 * @date: 2010-3-17 ����03:12:07
 */
public class CharacterUtil {
	// ������ʽ
	static final String Digits = "(\\p{Digit}+)";// ʮ�������֣�[0-9]
	static final String HexDigits = "(\\p{XDigit}+)";// ʮ���������֣�[0-9a-fA-F]
	// an exponent is 'e' or 'E' followed by an optionally
	// .
	static final String Exp = "[eE][+-]?" + Digits;//
	static final String fpRegex = ("[\\x00-\\x20]*" + // ��ͷ�����пո�
			"[+-]?(" + // ��������
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

			")[pP][+-]?" + Digits + "))" + "[fFdD]?))" + "[\\x00-\\x20]*");// ��β�����пո�
	static final String IntDigits = "[-]?" + Digits;// ����
	static final String NegativeIntDigits = "[-]" + Digits;// ������

	/**
	 * �ַ���ת��Ϊdouble�������ʽ��ƥ�䣬����Ĭ��ֵ
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
	 * �ַ���ת��Ϊint�������ʽ��ƥ�䣬����Ĭ��ֵ
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
	 * ��byte����ת���� 16���Ƶ��ַ�
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
	 * md5�������
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
