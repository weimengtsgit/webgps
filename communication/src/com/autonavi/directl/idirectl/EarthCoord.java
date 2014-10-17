package com.autonavi.directl.idirectl;

public class EarthCoord {
  public String x;//����
  public String xType;//����������,E,W
  public String y;//γ��
  public String yType;//��γ����γ,S,N
  public EarthCoord(){
  }
  public EarthCoord(String px,String pxType,String py,String pyType) {
    this.x=px;
    this.xType=pxType;
    this.y=py;
    this.yType=pyType;
  }
  public String getFormatX(int numberCount){
    return this.x.replaceAll("\\.","").substring(0,numberCount);
  }
  public String getFormatY(int numberCount){
    return this.y.replaceAll("\\.","").substring(0,numberCount);
  }
  public static void main(String[] args) {
    EarthCoord earthCoord1 = new EarthCoord();
  }
}