/**
 * 
 */
package com.autonavi;

/**
 * @author shiguang.zhou
 *  计算两个经纬度之间的距离
 */
public class DistanceCount {
	
	public DistanceCount(){}

//-------------------------代码一--------------------------//	
	double hypot(double x, double y) 
	{  
		return Math.sqrt(x * x + y * y);
	}

	double distance(double wd1, double jd1, double wd2, double jd2) {
		// 根据经纬度坐标计算实际距离,单位M
		double x, y, out;  
		double PI = 3.1415926535898; 
		double R = 6.371229 * 1e6; 
		
		x = (jd2 - jd1) * PI * R * Math.cos(((wd1 + wd2)/ 2) * PI / 180) / 180; 
		y = (wd2 - wd1) * PI * R / 180; 
		out = hypot(x, y);  
		return out;
		
	}
//-------------------------------------------------------------//																		 

		
//-----------------------------代码二-----------------------------//		
		private  double EARTH_RADIUS=6378.137; //地球半径
		private  double rad(double d)
		{  return d*Math.PI/180.0; } 
		
		public  double GetDistance(double lat1,double lng1,double lat2,double lng2) 
		{  
			double radLat1=rad(lat1);  
			double radLat2=rad(lat2); 
			double a=radLat1-radLat2;  
			double b=rad(lng1)-rad(lng2); 
			double s=2*Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)+Math.pow(Math.sin(b/2),2)));
			s=s*EARTH_RADIUS; 
			s=Math.round(s*10000)/10000;  
			return s;
		}
//------------------------------------------------------------------//
		
		public static void main(String[] args){
			DistanceCount dc = new DistanceCount();
			double d = dc.distance(39.981411, 116.299510,39.980602, 116.299504);
			System.out.println(d);
		}

  }
