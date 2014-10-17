package com.sosgps.wzt.commons.util;

import java.math.BigDecimal;

/**
 * @Title: MathUitl.java
 * @Description:
 * @Copyright:
 * @Date: 2008-8-16 上午11:44:24
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yanglei
 * @version 1.0
 */
public class MathUitl {
	public MathUitl() {

	}

	/**
	 * 对数值进行四舍五入！ 
	 * @param value
	 * @param deci
	 * @return
	 */
	public static double round(double value, int deci) {
		double module = Math.pow(10, deci);
		return Math.round(value * module) / module;
	}

	/**
	 * 将一实数四舍五入指定位数后,转换成字符串并返回 
	 * @param d 实数
	 * @param dec 小数位
	 * @return
	 */
	public static String getString(double d, int dec) {
		String temp = String.valueOf(round(d, dec));

		for (int i = temp.length() - temp.indexOf("."); i <= dec; i++) {
			temp += "0";
		}
		return temp;
	}

	/**
	 * Function: 得到指定保留小数位数的数字 
	 * @param: SourceData double,Scals int
	 * @return: String
	 */
	public static double getRound(double doubleVluae, int scale) {
		BigDecimal b = new BigDecimal(doubleVluae);
		BigDecimal c = new BigDecimal("1");
		BigDecimal d = b.divide(c, scale, BigDecimal.ROUND_HALF_UP);
		return d.doubleValue();
	}

	/**
	 * Function: 判断数据是否整数 
	 * @param: sourceData String
	 * @return: Boolean true-是,false-不是
	 */
	public static boolean isInteger(String s) {
		if (s.trim().length() < 1) {
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) < '0' || s.charAt(i) > '9') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Function: 判断数据是否数字型 
	 * @param: sourceData String
	 * @return: Boolean true-是,false-不是
	 */
	public static boolean isNumeric(String s) {
		int dot = 0;

		if (s.trim().length() < 1) {
			return false;
		}
		if (s.charAt(0) == '-') {
			s = s.substring(1, s.length() - 1);
		}
		if (s.charAt(0) == '.' || s.charAt(s.length() - 1) == '.') {
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			if (dot > 1) {
				return false;
			}
			if (s.charAt(i) == '.') {
				dot++;
			}
			if (s.charAt(i) < '0' || s.charAt(i) > '9') {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param args
	 *            void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
