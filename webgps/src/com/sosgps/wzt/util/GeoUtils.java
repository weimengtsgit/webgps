package com.sosgps.wzt.util;


/**
 * 
 * CopyRight (C) All rights reserved.
 * <p>
 * 
 * 
 * Author 
 * <p>
 * Project Name: 获取两点经纬度距离
 * 
 * @version 1.0 2009-09-07
 * 
 *          <p>
 *          Base on : JDK1.5
 *          <p>
 * 
 */
public class GeoUtils {
	public enum GaussSphere {
		Beijing54, Xian80, WGS84,
	}

	private static double Rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 
	 * @param lng1
	 *            经度
	 * @param lat1
	 *            维度
	 * @param lng2
	 *            经度
	 * @param lat2
	 *            维度
	 * @param gs
	 *            坐标系
	 * @return 两点距离 单位 米
	 */
	public static double DistanceOfTwoPoints(double lng1, double lat1,
			double lng2, double lat2, GaussSphere gs) {
		double radLat1 = Rad(lat1);
		double radLat2 = Rad(lat2);
		double a = radLat1 - radLat2;
		double b = Rad(lng1) - Rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s
				* (gs == GaussSphere.WGS84 ? 6378137.0
						: (gs == GaussSphere.Xian80 ? 6378140.0 : 6378245.0));
		s = Math.round(s * 10000) / (10000);

		return s;
	}

	public static void main(String[] args) {
		double distance = GeoUtils.DistanceOfTwoPoints(113.223, 39.2451127,
				113.223, 39.64011110, GaussSphere.WGS84);
		System.out.println(distance/1000);
	}
}

