package com.autonavi.lbsgateway.poolsave;
public class GpsData {
  public GpsData() {
  }
  String DEVICE_ID="";
  String SIMCARD = "";//手机号
  String TIME = "";//时间
  float X = 0.0f;//经过坐标偏转的经度
  float Y = 0.0f;//经过坐标偏转的纬度
  float S = 0.0f;//速度
  float V = 0.0f;//方向
  float LC = 0.0f;//里程
  String PICURL = "";//图片
  String ALARMDESC = "";//报警描述
  float H = 0.0f;//高程
  int C = 0;//卫星个数
  String F = "";//定位状态
  int EPID=0;//企业号
  int URID=0;//用户ID
  String Adress="";//位置描述
  String batchID="";//批处理号
  double TX = 0.0;//未经过坐标偏转的经度
  double TY = 0.0;//未经过坐标偏转的纬度
  String Extend1="";//扩展字段1
  String Extend2="";//扩展字段2
  String Extend3="";//扩展字段3
  String gpstime = "";
  Float temperature = null;//温度
  Float humidity = null;//湿度
  String accStatus;
  String locateType = "";
  java.sql.Timestamp gpsTime=null;
  
  int targetID;

 public String toString(){
	 return this.DEVICE_ID+","+this.X +","+this.Y+","+this.TIME;
 }
  
  
}
