/**
 * <p>Title:Toten</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: WinChannel </p>
 * <p>Date:Jul 27, 2006</p>
 * @author bxz
 * @version 1.0
 */
package com.sosgps.wzt.util;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
public class NumberUtility {
public static String formatCurrency(double money){
	NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
	return format.format(money);
	
}
public static String formatNumber(int number) {
    DecimalFormat df = new DecimalFormat("00000000");
    return df.format(number);
}

public static String formatNumber(Long number) {
    DecimalFormat df = new DecimalFormat("00000000");
    return df.format(number);
}

public static String formatNumber(long number) {
    DecimalFormat df = new DecimalFormat("00000000");
    return df.format(number);
}

public static String formatDouble(double number) {
    DecimalFormat df = new DecimalFormat("##0.00");
    return df.format(number);
}

public static String formatDouble(Double number) {
    DecimalFormat df = new DecimalFormat("##0.00");
    return df.format(number);
}

public static String formatDouble(String number) {
    DecimalFormat df = new DecimalFormat("##0.00");
    return df.format(number);
}
}
