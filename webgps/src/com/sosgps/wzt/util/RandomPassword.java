/**
 * 
 */
package com.sosgps.wzt.util;

import java.util.Random;

/**
 * @author xiaojun.luan
 * 
 */
public class RandomPassword {

	private Random rd = new Random();
	private final static int randomLength = 122 - 48;

	// 'z'=122 '0'=48
	public char randomChar() {

		int randInt;
		do {
			randInt = 48 + rd.nextInt(randomLength);
		} while ((57 < randInt && randInt < 65)
				|| (randInt > 90 && randInt < 97));

		return (char) randInt;

	}

	public String generatePassword(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(randomChar());
		}
		return sb.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RandomPassword rp = new RandomPassword();
		System.out.println(rp.generatePassword(100));
	}

}
