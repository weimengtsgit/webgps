package com.sosgps.wzt.directl;

import java.io.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: www.sosgps.com</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Picture implements Serializable{
  private int num;  //ͼ����
  private int pakcNo;  //����
  private int packcounts ; //��ͼ�������
  private byte[] imgcontent; //ͼ������
  private String imgStrCont;  //ͼ������
  private String localFileName; //�洢ͼ��·��
  private String type; //��������

  public Picture() {
  }

  public static void main(String[] args) {
    Picture picture = new Picture();
  }

  public byte[] getImgcontent() {
    return imgcontent;
  }

  public int getNum() {
    return num;
  }

  public int getPackcounts() {
    return packcounts;
  }

  public String getImgStrCont() {
    return imgStrCont;
  }

  public int getPakcNo() {
    return pakcNo;
  }

    public String getType() {
        return type;
    }

    public void setImgcontent(byte[] imgcontent) {
    this.imgcontent = imgcontent;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public void setPackcounts(int packcounts) {
    this.packcounts = packcounts;
  }

  public void setImgStrCont(String imgStrCont) {
    this.imgStrCont = imgStrCont;
  }

  public void setPakcNo(int pakcNo) {
    this.pakcNo = pakcNo;
  }

    public void setType(String type) {
        this.type = type;
    }
}
