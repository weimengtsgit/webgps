package com.sosgps.wzt.directl;

import java.sql.*;
import java.util.*;


public class PicCache extends Vector {
    static PicCache instance = null;
    private int cacheSize; //图象缓冲大小
    private int number=-1; //图象编号
    private String type = null; //拍照触发类型，’1’表示中心拍照，’2’表示车台一般自动拍照，’3’表示车台警情状态拍照
    private Timestamp timeStamp; //照片上传时间
    private String path; //存储路径
    private String phone; //拍照的终端号码
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

    //数据包是否传输完毕
    public boolean isTraverOver() {
        boolean ret = false;
        if (instance.size() != 0 && (instance.size() == instance.getCacheSize())) {
            ret = true;
        }
        return ret;
    }

    //重置图片各参数
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
