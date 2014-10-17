package com.sosgps.wzt.directl;

import java.sql.*;
import java.util.*;


public class PicCache extends Vector {
    static PicCache instance = null;
    private int cacheSize; //ͼ�󻺳��С
    private int number=-1; //ͼ����
    private String type = null; //���մ������ͣ���1����ʾ�������գ���2����ʾ��̨һ���Զ����գ���3����ʾ��̨����״̬����
    private Timestamp timeStamp; //��Ƭ�ϴ�ʱ��
    private String path; //�洢·��
    private String phone; //���յ��ն˺���
    private double x;
    private double y;

    private PicCache() {
    }

    public static synchronized PicCache getInstance() {
        if (instance == null) {
            instance = new PicCache();
        }
        return instance;
    }


    public int getCacheSize() {
        return cacheSize;
    }

    public int getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public String getPath() {
        return path;
    }

    public String getPhone() {
        return phone;
    }


    public  void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public  void setNumber(int number) {
        this.number = number;
    }

    public  void setType(String type) {
        this.type = type;
    }

    public  void setY(double y) {
        this.y = y;
    }

    public  void setX(double x) {
        this.x = x;
    }

    public  void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public  void setPath(String path) {
        this.path = path;
    }

    public  void setPhone(String phone) {
        this.phone = phone;
    }

    //���ݰ��Ƿ������
    public boolean isTraverOver() {
        boolean ret = false;
        if (instance.size() != 0 && (instance.size() == instance.getCacheSize())) {
            ret = true;
        }
        return ret;
    }

    //����ͼƬ������
    public  void reset() {
        this.clear();
        this.cacheSize = 0;
        this.number = -1;
        this.type = null;
        this.x = 0.0;
        this.y = 0.0;
        this.timeStamp = null;
        this.path = null;
    }


}
