package com.autonavi.lbsgateway;

import java.util.*;

public class LocationInfo {

  private String simcard; //Sim卡号
  private String dateTime; //更新时间
  private String x; //经度
  private String y; //纬度
  private String speed; //速度
  private String v; //方向
  private String lc;//里程
  private String picurl;//上传图片路径
  private String alarmDesc;//报警描述
  private String h; //高程
  private String c; //卫星个数
  private String flag; //定位状态


  private String encodex;//加密后的X坐标
  private String encodey;//加密后的Y坐标
  private String adress;//位置描述
  private int errcode=1000;//错误代码
  private String errdesc;//错误描述
  private String trcode; //终端类型
  private String linkway;//连接方式
  private boolean locValid=false;//是否可以定位
  private boolean isValid=true;//用户是否注册了此终端
  private int locState=0;//0标识正在等待定位
                 //1标识正在定位中
                 //2标识定位成功，得到结果
                 //3标识定位失败
  private int urid;
  private String username;
  private String imgpath;
  private int ugpid;
  private String extend1;
  private String extend2;
  private String extend3;
  private String isCharged;//是否已经付费：“0”：费用不足 ，“1”：还有费用
  public  LocationInfo() {
    String sTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new
        Date());
    this.setDateTime(sTime);
  }

  public synchronized void  setX(String x) {
    this.x = x;
  }

  public synchronized void setY(String y) {
    this.y = y;
  }

  public synchronized void setSpeed(String speed) {
    this.speed = speed;
  }

  public synchronized void setFlag(String flag) {
    this.flag = flag;
  }

  public synchronized void setV(String v) {
    this.v = v;
  }

  public synchronized void setH(String h) {
    this.h = h;
  }

  public synchronized void setC(String c) {
    this.c = c;
  }
  public synchronized void setLinkWay(String lnkway) {
    this.linkway = lnkway;
  }

  public String getLinkWay(){
    return this.linkway;
  }

  public String getX() {
    return x;
  }

  public String getY() {
    return y;
  }

  public String getSpeed() {
    return speed;
  }

  public String getFlag() {
    return flag;
  }

  public String getV() {
    return v;
  }

  public String getH() {
    return h;
  }

  public String getC() {
    return c;
  }

  public String getSimcard() {
    return simcard;
  }

  public synchronized void setSimcard(String simcard) {
    this.simcard = simcard;
  }

  public String getDateTime() {
    return dateTime;
  }

  public String getEncodex() {
    return encodex;
  }

  public String getEncodey() {
    return encodey;
  }

  public String getAdress() {
    return adress;
  }

  public int getErrcode() {
    return errcode;
  }

  public String getErrdesc() {
    return errdesc;
  }

  public String getTrcode() {
    return trcode;
  }

  public synchronized void setDateTime(String dt) {
    this.dateTime = dt;
  }

  public synchronized void setEncodex(String encodex) {
    this.encodex = encodex;
  }

  public synchronized void setEncodey(String encodey) {
    this.encodey = encodey;
  }

  public synchronized void setAdress(String adress) {
    this.adress = adress;
  }

  public synchronized void setErrcode(int errcode) {
    this.errcode = errcode;
  }

  public synchronized void setErrdesc(String errdesc) {
    this.errdesc = errdesc;
  }

  public synchronized void setTrcode(String trcode) {
    this.trcode = trcode;
  }

  public synchronized void setlocValid(boolean b){
    this.locValid=b;
  }
  public boolean getlocValid(){
    return this.locValid;
  }
  public synchronized void setlocState(int s){
    this.locState=s;
  }
  public int getlocState(){
    return this.locState;
  }
  public synchronized void setisValid(boolean b){
    this.isValid=b;
  }
  public boolean getisValid(){
    return this.isValid;
  }

  public synchronized void setUrid(int urid){
    this.urid=urid;
  }
  public int getUrid(){
    return this.urid;
  }
  public synchronized void setUserName(String pName){
   this.username=pName;
 }
 public String getUserName(){
   return this.username;
 }
 public synchronized void setImgpath(String pImgpath){
  this.imgpath=pImgpath;
}

  public void setExtend1(String extend1) {
    this.extend1 = extend1;
  }

  public void setExtend2(String extend2) {
    this.extend2 = extend2;
  }

  public void setExtend3(String extend3) {
    this.extend3 = extend3;
  }

  public synchronized void setIsCharged(String isCharged) {
    this.isCharged = isCharged;
  }

  public String getImgpath(){
  return this.imgpath;
}
public synchronized void setUgpID(int pUGPID){
 this.ugpid=pUGPID;
}
public int getUgpID(){
 return this.ugpid;
}

  public synchronized void setLC(String lc){
   this.lc=lc;
  }
  public String getLC(){
   return this.lc;
}

 public synchronized void setPicURL(String url){
  this.picurl=url;
 }
 public String getPicURL(){
  return this.picurl;
}
public synchronized void setAlamrDesc(String desc){
 this.alarmDesc=desc;
}
public String getAlarmDesc(){
 return this.alarmDesc;
}

  public String getExtend1() {
    return extend1;
  }

  public String getExtend2() {
    return extend2;
  }

  public String getExtend3() {
    return extend3;
  }

  public String getIsCharged() {
    return isCharged;
  }

  public String getXML() {
    StringBuffer sbf = new StringBuffer();
    sbf.append("<LOCTIONINFO>");
    sbf.append("<SIMCARD>" + this.getSimcard() + "</SIMCARD>");
    sbf.append("<TIME>" + this.getDateTime() + "</TIME>");
    sbf.append("<X>" + this.getX() + "</X>");
    sbf.append("<Y>" + this.getY() + "</Y>");
    sbf.append("<SPEED>" + this.getSpeed() + "</SPEED>");
    sbf.append("<V>" + this.getV() + "</V>");
    sbf.append("<LC>" + this.getLC() + "</LC>");
    sbf.append("<ALARMDESC>" + this.getAlarmDesc() + "</ALARMDESC>");
    sbf.append("<FLAG>" + this.getFlag() + "</FLAG>");
    sbf.append("<ENCODEX>" + this.getEncodex() + "</ENCODEX>");
    sbf.append("<ENCODEY>" + this.getEncodey() + "</ENCODEY>");
    sbf.append("<ADRESS>" + this.getAdress() + "</ADRESS>");
    sbf.append("<ERRCODE>" + this.getErrcode() + "</ERRCODE>");
    sbf.append("<ERRDESC>" + this.getErrdesc() + "</ERRDESC>");
    sbf.append("<TRCODE>" + this.getTrcode() + "</TRCODE>");
    sbf.append("<LOCSTATE>" + this.getlocState() + "</LOCSTATE>");
    sbf.append("<URID>" + this.getUrid() + "</URID>");
    sbf.append("<USERNAME>" + this.getUserName() + "</USERNAME>");
    sbf.append("<IMGPATH>" + this.getImgpath() + "</IMGPATH>");
    sbf.append("<UGPID>" + this.getUgpID() + "</UGPID>");
    sbf.append("<EXTEND1>"+this.getExtend1()+"</EXTEND1>");
    sbf.append("<EXTEND2>"+this.getExtend2()+"</EXTEND2>");
    sbf.append("<EXTEND3>"+this.getExtend3()+"</EXTEND3>");
    sbf.append("</LOCTIONINFO>");
    return sbf.toString();
  }

}
