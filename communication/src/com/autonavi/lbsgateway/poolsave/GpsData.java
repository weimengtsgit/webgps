package com.autonavi.lbsgateway.poolsave;
public class GpsData {
  public GpsData() {
  }
  String DEVICE_ID="";
  String SIMCARD = "";//�ֻ���
  String TIME = "";//ʱ��
  float X = 0.0f;//��������ƫת�ľ���
  float Y = 0.0f;//��������ƫת��γ��
  float S = 0.0f;//�ٶ�
  float V = 0.0f;//����
  float LC = 0.0f;//���
  String PICURL = "";//ͼƬ
  String ALARMDESC = "";//��������
  float H = 0.0f;//�߳�
  int C = 0;//���Ǹ���
  String F = "";//��λ״̬
  int EPID=0;//��ҵ��
  int URID=0;//�û�ID
  String Adress="";//λ������
  String batchID="";//�������
  double TX = 0.0;//δ��������ƫת�ľ���
  double TY = 0.0;//δ��������ƫת��γ��
  String Extend1="";//��չ�ֶ�1
  String Extend2="";//��չ�ֶ�2
  String Extend3="";//��չ�ֶ�3
  String gpstime = "";
  Float temperature = null;//�¶�
  Float humidity = null;//ʪ��
  String accStatus;
  String locateType = "";
  java.sql.Timestamp gpsTime=null;
  
  int targetID;

 public String toString(){
	 return this.DEVICE_ID+","+this.X +","+this.Y+","+this.TIME;
 }
  
  
}
