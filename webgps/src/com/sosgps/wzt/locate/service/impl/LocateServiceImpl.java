package com.sosgps.wzt.locate.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.mapabc.geom.CoordCvtAPI;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.locate.bean.Position;
import com.sosgps.wzt.locate.dao.LbsLocateDAO;
import com.sosgps.wzt.locate.dao.LocateDAO;
import com.sosgps.wzt.locate.dao.TLocrecordDao;
import com.sosgps.wzt.locate.service.LocateService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TLastLocrecord;
import com.sosgps.wzt.orm.TLocrecord;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.util.HttpUtil;

public class LocateServiceImpl implements LocateService {

    private static final int RETRY = 2;

    private Logger log = Logger.getLogger(LocateServiceImpl.class);

    private LocateDAO tLocateDAO;

    private LbsLocateDAO lbsLocateDAO;

    private TLocrecordDao tLocrecordDao;

    // 电信定位key
    private String oauth_consumer_key;

    // oauth_consumer_key对应的密钥
    private String oauth_signature;

    public String getOauth_consumer_key() {
        return oauth_consumer_key;
    }

    public void setOauth_consumer_key(String oauth_consumer_key) {
        this.oauth_consumer_key = oauth_consumer_key;
    }

    public String getOauth_signature() {
        return oauth_signature;
    }

    public void setOauth_signature(String oauth_signature) {
        this.oauth_signature = oauth_signature;
    }

    public LocateDAO getTLocateDAO() {
        return this.tLocateDAO;
    }

    public void setTLocateDAO(LocateDAO locateDAO) {
        this.tLocateDAO = locateDAO;
    }

    public LbsLocateDAO getLbsLocateDAO() {
        return lbsLocateDAO;
    }

    public void setLbsLocateDAO(LbsLocateDAO lbsLocateDAO) {
        this.lbsLocateDAO = lbsLocateDAO;
    }

    public TLocrecordDao getTLocrecordDao() {
        return tLocrecordDao;
    }

    public void setTLocrecordDao(TLocrecordDao locrecordDao) {
        tLocrecordDao = locrecordDao;
    }

    /**
     * t_locrecord中查找没有偏转及获取描述的数据并获取位置描述及偏转数据
     * 
     * @return
     */
    public void updateNoEncryptionAndNoLocDesc() {
        List<TLocrecord> loclist = this.tLocateDAO.queryNoEncryptionAndNoLocDesc();

        if (loclist != null) {
            log.info("定时任务-查找没有偏转及获取描述的数据并获取位置描述及偏转数据-开始");
            com.mapabc.geom.CoordCvtAPI coordAPI = new CoordCvtAPI();
            com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
            String posDesc = "";
            String lngX = "";
            String latY = "";
            for (int i = 0; i < loclist.size(); i++) {

                double x = loclist.get(i).getLongitude();
                double y = loclist.get(i).getLatitude();
                double xs[] = { x };
                double ys[] = { y };
                //com.mapabc.geom.DPoint[] dps = null;
                com.sos.sosgps.api.DPoint[] dps = null;
                try {

                    dps = coordApizw.encryptConvert(xs, ys);
                    loclist.get(i).setJmx(coordAPI.encrypt(dps[0].x));
                    loclist.get(i).setJmy(coordAPI.encrypt(dps[0].y));
                    lngX = String.valueOf(loclist.get(i).getLongitude());
                    latY = String.valueOf(loclist.get(i).getLatitude());
                    posDesc = coordAPI.getAddress(lngX, latY);
                    loclist.get(i).setLocDesc(posDesc);
                    // System.out.println("posDesc="+posDesc);

                } catch (Exception e) {
                    log.error("查找没有偏转及获取描述的数据并获取位置描述及偏转数据-获取位置描述error! x=" + lngX + " y=" + latY
                            + "error=" + e.getMessage());
                }
                // 更新记录
                try {
                    tLocrecordDao.update(loclist.get(i));

                } catch (Exception e) {
                    log.error("查找没有偏转及获取描述的数据并获取位置描述及偏转数据-保存数据error! x=" + lngX + " y=" + latY
                            + "error=" + e.getMessage());
                }
                log.info("定时任务-查找没有偏转及获取描述的数据并获取位置描述及偏转数据-完成");
            }
        }

    }

    /**
     * 查询选中终端的最新位置
     */
    /*
     * public String findLastLoc(String deviceIds) { StringBuffer returnXML =
     * new StringBuffer(); returnXML.append("<?xml version='1.0'
     * encoding='UTF-8' ?>"); returnXML.append("<ls>"); if (deviceIds == null)
     * return returnXML.toString(); List<TLastLocrecord> loclist =
     * this.tLocateDAO.findLocByDeviceId(deviceIds); int size = loclist.size();
     * double[] xx = new double[size];//原始经度 double[] yy = new
     * double[size];//原始纬度 com.mapabc.geom.CoordCvtAPI coordAPI = new
     * CoordCvtAPI(); com.mapabc.geom.DPoint[] pts =new
     * com.mapabc.geom.DPoint[size] ; for (int i = 0; i < size; i++) { double x
     * = loclist.get(i).getLongitude(); double y = loclist.get(i).getLatitude();
     * double fx = loclist.get(i).getDirection(); double speed=
     * loclist.get(i).getSpeed(); if( speed > 0){ com.mapabc.geom.DPoint dp =
     * coordAPI.roadConvert(x, y,fx); loclist.get(i).setJmx(dp.getEncryptX() );
     * loclist.get(i).setJmy( dp.getEncryptY() );
     * //System.out.println("速度大于0进行道路纠偏"); }else{ double xs[] ={x}; double
     * ys[]={y}; try { com.mapabc.geom.DPoint[] dps =
     * coordAPI.encryptConvert(xs, ys); loclist.get(i).setJmx(
     * coordAPI.encrypt(dps[0].x ) );
     * loclist.get(i).setJmy(coordAPI.encrypt(dps[0].y ) );
     * //System.out.println("速度==0不进行道路纠偏"); } catch (Exception e) { // TODO
     * Auto-generated catch block e.printStackTrace(); } } } for (int i=0;
     * i<loclist.size(); i++) { TLastLocrecord tloc = loclist.get(i); String
     * posDesc = ""; // posDesc = coodApi.getAddress(tloc.getJmx(),
     * tloc.getJmy()); returnXML.append("<l>"); returnXML.append("<id>" +
     * tloc.getId() + "</id>");// 目标对象序列 returnXML.append("<x>" + tloc.getJmx()
     * + "</x>");// 经度 returnXML.append("<y>" + tloc.getJmy() + "</y>");// 纬度
     * returnXML.append("<lng>" + tloc.getLongitude() + "</lng>");// 经度
     * returnXML.append("<lat>" + tloc.getLatitude() + "</lat>");// 纬度
     * returnXML.append("<h>" + String.valueOf(tloc.getHeight()) + "</h>");// 高度
     * returnXML.append("<s>" + String.valueOf(tloc.getSpeed()) + "</s>");// 速度
     * returnXML.append("<d>" + String.valueOf(tloc.getDirection()) + "</d>");//
     * 方向 returnXML.append("<k>" + tloc.getDeviceId() + "</k>");// simcard
     * returnXML.append("<t>" + tloc.getGpstime() + "</t>");// date
     * returnXML.append("<p>" + posDesc + "</p>");//位置描述
     * returnXML.append("</l>"); } returnXML.append("</ls>");
     * System.out.println("位置跟踪:\r\n" +returnXML.toString() );
     * System.out.println("返回了 道路纠偏坐标"); return returnXML.toString(); }
     */
    public String findLastLoc(String deviceIds) {
        return findLastLoc(deviceIds, true);
    }

    /**
     * 查询选中终端的最新位置
     */
    public String findLastLoc(String deviceIds, boolean isEncrypt) {
        StringBuffer returnXML = new StringBuffer();
        returnXML.append("<?xml version='1.0' encoding='GBK' ?>");
        returnXML.append("<ls>");
        if (deviceIds == null) {
            returnXML.append("</ls>");
            return returnXML.toString();
        }
        /* 为每个deviceid添加单引号，否则SQL报无效数字错误add by shiguang.zhou */
        String[] devs = CharTools.split(deviceIds, ",");
        String temp = "";
        for (int k = 0; k < devs.length; k++) {
            if (k == devs.length - 1) {
                temp += "'" + devs[k] + "'";
            } else {
                temp += "'" + devs[k] + "'" + ",";
            }
        }
        List<TLastLocrecord> loclist = this.tLocateDAO.findLocByDeviceId(temp);

        if (loclist == null) {
            returnXML.append("</ls>");
            return returnXML.toString();
        }

        int size = loclist.size();
        double[] xx = new double[size];// 原始经度
        double[] yy = new double[size];// 原始纬度

        for (int i = 0; i < size; i++) {
            xx[i] = loclist.get(i).getLongitude();
            yy[i] = loclist.get(i).getLatitude();
        }

        int showSize = loclist.size();
        com.mapabc.geom.CoordCvtAPI coordAPI = new CoordCvtAPI();
        com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
        for (int i = 0; i < size; i++) {
            double x = loclist.get(i).getLongitude();
            double y = loclist.get(i).getLatitude();
            double fx = loclist.get(i).getDirection();
            double speed = loclist.get(i).getSpeed();
            if (!isEncrypt) {// C/S调用
                loclist.get(i).setJmx(x + "");
                loclist.get(i).setJmy(y + "");
                continue;
            }
            if (speed > 0) {
                try {
                    // modify  2009-06-03 增加纠偏判断 GPS纠偏LBS不纠偏
                    //String tLocateType = loclist.get(i).getLocateType(); // 终端类型0：lbs
                    // 1:gps
                    //					if (tLocateType != null && "1".equals(tLocateType)) {
                    //						com.mapabc.geom.DPoint dp = coordAPI.roadConvert(x, y,
                    //								fx);
                    //						if (dp == null) {
                    //							log.error("最新位置findLastLoc-道路纠偏error! x=" + x
                    //									+ " y=" + y + " fx=" + fx);
                    //						} else {
                    //							loclist.get(i).setJmx(dp.getEncryptX());
                    //							loclist.get(i).setJmy(dp.getEncryptY());
                    //						}
                    //					} else {
                    double xs[] = { x };
                    double ys[] = { y };
                    //						com.mapabc.geom.DPoint[] dps = coordAPI.encryptConvert(
                    //								xs, ys);
                    com.sos.sosgps.api.DPoint[] dps = coordApizw.encryptConvert(xs, ys);
                    loclist.get(i).setJmx(coordAPI.encrypt(dps[0].x));
                    loclist.get(i).setJmy(coordAPI.encrypt(dps[0].y));
                    //}

                } catch (Exception e) {
                    log.error("最新位置findLastLoc-偏转加密error! x=" + x + " y=" + y);
                }
                // System.out.println("速度大于0进行道路纠偏");
            } else {
                double xs[] = { x };
                double ys[] = { y };
                try {
                    //					com.mapabc.geom.DPoint[] dps = coordAPI.encryptConvert(xs,
                    //							ys);
                    com.sos.sosgps.api.DPoint[] dps = coordApizw.encryptConvert(xs, ys);

                    loclist.get(i).setJmx(coordAPI.encrypt(dps[0].x));
                    loclist.get(i).setJmy(coordAPI.encrypt(dps[0].y));
                    // System.out.println("速度==0不进行道路纠偏");
                } catch (Exception e) {
                    log.error("最新位置findLastLoc-偏转加密error! x=" + x + " y=" + y);
                }
            }

        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < showSize; i++) {
            TLastLocrecord tloc = loclist.get(i);
            String posDesc = "";
            if ("0".equals(tloc.getLocateType())) {
                String lngX = tloc.getJmx();
                String latY = tloc.getJmy();
                //String lngX = String.valueOf(tloc.getLongitude());
                //String latY = String.valueOf(tloc.getLatitude());
                posDesc = coordAPI.getAddress(lngX, latY);
            } else {
                posDesc = "null";
            }
            if (tloc.getJmx() == null || tloc.getJmx().equals("")) continue;

            returnXML.append("<l>");
            returnXML.append("<id>" + tloc.getId() + "</id>");// 目标对象序列
            returnXML.append("<x>" + tloc.getJmx() + "</x>");// 经度
            returnXML.append("<y>" + tloc.getJmy() + "</y>");// 纬度
            returnXML.append("<type>" + tloc.getLocateType() + "</type>");// 终端类型：0为LBS；1为GPS；
            returnXML.append("<lng>" + tloc.getLongitude() + "</lng>");// 经度
            returnXML.append("<lat>" + tloc.getLatitude() + "</lat>");// 纬度
            returnXML.append("<h>" + String.valueOf(tloc.getHeight()) + "</h>");// 高度
            if (tloc.getSpeed() <= 1) {
                returnXML.append("<s>0</s>");// 速度
            } else {
                returnXML.append("<s>" + String.valueOf(tloc.getSpeed()) + "</s>");// 速度
            }
            returnXML.append("<d>" + String.valueOf(tloc.getDirection()) + "</d>");// 方向
            returnXML.append("<k>" + tloc.getDeviceId() + "</k>");// simcard
            returnXML.append("<t>" + format.format(tloc.getGpstime()) + "</t>");// date
            returnXML.append("<p>" + CharTools.replayStr(posDesc) + "</p>");// 位置描述
            returnXML.append("</l>");
        }
        returnXML.append("</ls>");
        return returnXML.toString();
    }

    private com.sos.sosgps.api.DPoint[] encryptConvert(double[] xx, double[] yy, boolean isEncrypt) {
        com.sos.sosgps.api.DPoint[] pts = null;
        if (isEncrypt) {
            long btime = System.currentTimeMillis();
            com.mapabc.geom.CoordCvtAPI coordAPI = new CoordCvtAPI();
            com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
            //com.mapabc.geom.DPoint dpoint = new DPoint();
            //com.sos.sosgps.api.DPoint dpoint = new com.sos.sosgps.api.DPoint();
            String mx = "";
            String my = "";
            try {
                // 加密偏转原始坐标
                pts = coordApizw.encryptConvert(xx, yy);
            } catch (Exception e) {
                log.error("偏转加密error! x=" + xx + " y=" + yy);
            }
            long etime = System.currentTimeMillis();
            System.out.println("实时跟踪，坐标偏转花费时间：" + (etime - btime) / 1000 + "S");
        } else {
            pts = new com.sos.sosgps.api.DPoint[xx.length];
            for (int i = 0; i < xx.length; i++) {
                pts[i] = new com.sos.sosgps.api.DPoint();
                pts[i].encryptX = xx[i] + "";
                pts[i].encryptY = yy[i] + "";
            }
        }
        return pts;
    }

    /**
     * 查询选中终端的最新位置
     */
    public String findLastLocBack(String deviceIds) {
        StringBuffer returnXML = new StringBuffer();
        returnXML.append("<?xml version='1.0' encoding='GBK' ?>");
        returnXML.append("<ls>");
        if (deviceIds == null) return returnXML.toString();
        /* 为每个deviceid添加单引号，否则SQL报无效数字错误add by shiguang.zhou */
        String[] devs = CharTools.split(deviceIds, ",");
        String temp = "";
        for (int k = 0; k < devs.length; k++) {
            if (k == devs.length - 1) {
                temp += "'" + devs[k] + "'";
            } else {
                temp += "'" + devs[k] + "'" + ",";
            }
        }
        List<TLastLocrecord> loclist = this.tLocateDAO.findLocByDeviceId(temp);

        int size = loclist.size();
        double[] xx = new double[size];// 原始经度
        double[] yy = new double[size];// 原始纬度

        for (int i = 0; i < size; i++) {
            xx[i] = loclist.get(i).getLongitude();
            yy[i] = loclist.get(i).getLatitude();
        }
        long btime = System.currentTimeMillis();
        com.mapabc.geom.CoordCvtAPI coordAPI = new CoordCvtAPI();
        com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();

        //com.mapabc.geom.DPoint[] pts = null;
        com.sos.sosgps.api.DPoint[] pts = null;
        try {
            // 加密偏转原始坐标
            pts = coordApizw.encryptConvert(xx, yy);
        } catch (Exception e) {
            log.error("最新位置findLastLocBack-偏转加密error! x=" + xx + " y=" + yy);
        }
        long etime = System.currentTimeMillis();
        System.out.println("实时跟踪，坐标偏转花费时间：" + (etime - btime) / 1000 + "S");

        for (int i = 0; i < pts.length; i++) {
            TLastLocrecord tloc = loclist.get(i);
            String posDesc = "";
            if (tloc.getLocateType() == "0") {
                //				posDesc = coordAPI.getAddress(
                //						String.valueOf(tloc.getLongitude()),
                //						String.valueOf(tloc.getLatitude()));
                posDesc = coordAPI.getAddress(pts[i].getEncryptX(), pts[i].getEncryptY());

            } else {
                posDesc = "null";
            }
            if (tloc.getJmx() == null || tloc.getJmx().equals("")) continue;
            returnXML.append("<l>");
            returnXML.append("<id>" + tloc.getId() + "</id>");// 目标对象序列
            returnXML.append("<x>" + pts[i].getEncryptX() + "</x>");// 经度
            returnXML.append("<y>" + pts[i].getEncryptY() + "</y>");// 纬度
            returnXML.append("<type>" + tloc.getLocateType() + "</type>");// 终端类型：0为LBS；1为GPS；
            returnXML.append("<lng>" + tloc.getLongitude() + "</lng>");// 经度
            returnXML.append("<lat>" + tloc.getLatitude() + "</lat>");// 纬度
            returnXML.append("<h>" + String.valueOf(tloc.getHeight()) + "</h>");// 高度
            returnXML.append("<s>" + String.valueOf(tloc.getSpeed()) + "</s>");// 速度
            returnXML.append("<d>" + String.valueOf(tloc.getDirection()) + "</d>");// 方向
            returnXML.append("<k>" + tloc.getDeviceId() + "</k>");// simcard
            returnXML.append("<t>" + tloc.getGpstime() + "</t>");// date
            returnXML.append("<p>" + posDesc + "</p>");// 位置描述
            returnXML.append("</l>");
        }
        returnXML.append("</ls>");

        return returnXML.toString();
    }

    /**
     * 坐标为偏转坐标
     * 
     * @param deviceIds
     * @param isRoadConvert
     * 
     * @return
     */
    public String findLastLocWithDeflect(String deviceIds, boolean isRoadConvert) {
        StringBuffer returnXML = new StringBuffer();
        returnXML.append("<?xml version='1.0' encoding='GBK' ?>");
        returnXML.append("<ls>");
        if (deviceIds == null) {
            returnXML.append("</ls>");
            return returnXML.toString();
        }
        /* 为每个deviceid添加单引号，否则SQL报无效数字错误add by shiguang.zhou */
        String[] devs = CharTools.split(deviceIds, ",");
        String temp = "";
        for (int k = 0; k < devs.length; k++) {
            if (k == devs.length - 1) {
                temp += "'" + devs[k] + "'";
            } else {
                temp += "'" + devs[k] + "'" + ",";
            }
        }

        List<TLastLocrecord> loclist = this.tLocateDAO.findLocByDeviceId(temp);

        if (loclist == null) {
            returnXML.append("</ls>");
            return returnXML.toString();
        }

        int size = loclist.size();
        double[] xx = new double[size];// 原始经度
        double[] yy = new double[size];// 原始纬度

        for (int i = 0; i < size; i++) {
            xx[i] = loclist.get(i).getLongitude();
            yy[i] = loclist.get(i).getLatitude();
        }

        int showSize = loclist.size();
        com.mapabc.geom.CoordCvtAPI coordAPI = new CoordCvtAPI();
        com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
        for (int i = 0; i < size; i++) {
            double x = loclist.get(i).getLongitude();
            double y = loclist.get(i).getLatitude();
            double fx = loclist.get(i).getDirection();
            double speed = loclist.get(i).getSpeed();
            // C/S调用
            // modify sunye 2009-06-02 增加道路纠偏判断 isRoadConvert=true纠偏
            isRoadConvert = false;
            try {
                if (isRoadConvert == true) {
                    com.mapabc.geom.DPoint dp = coordAPI.roadConvert(x, y, fx);
                    if (dp == null) {
                        log.error("最新位置findLastLoc-道路纠偏error! x=" + x + " y=" + y + " fx=" + fx);
                    } else {
                        loclist.get(i).setJmx(String.valueOf(dp.getX()));
                        loclist.get(i).setJmy(String.valueOf(dp.getY()));
                    }
                } else {
                    double xs[] = { x };
                    double ys[] = { y };
                    // 坐标偏转
                    com.sos.sosgps.api.DPoint[] dps = coordApizw.encryptConvert(xs, ys);
                    loclist.get(i).setJmx(String.valueOf(dps[0].x));
                    loclist.get(i).setJmy(String.valueOf(dps[0].y));
                }

            } catch (Exception e) {
                log.error("最新位置findLastLocWithDeflect-偏转加密error! x=" + x + " y=" + y);
            }
            continue;
        }

        for (int i = 0; i < showSize; i++) {
            TLastLocrecord tloc = loclist.get(i);
            String posDesc = "";
            //			String lngX = String.valueOf(tloc.getLongitude());
            //			String latY = String.valueOf(tloc.getLatitude());
            String lngX = String.valueOf(tloc.getJmx());
            String latY = String.valueOf(tloc.getJmy());
            if ("0".equals(tloc.getLocateType())) {
                posDesc = coordAPI.getAddress(lngX, latY);
            } else {
                posDesc = "null";
            }
            if (tloc.getJmx() == null || tloc.getJmx().equals("")) continue;
            returnXML.append("<l>");
            returnXML.append("<id>" + tloc.getId() + "</id>");// 目标对象序列
            returnXML.append("<x>" + tloc.getJmx() + "</x>");// 经度
            returnXML.append("<y>" + tloc.getJmy() + "</y>");// 纬度
            returnXML.append("<type>" + tloc.getLocateType() + "</type>");// 终端类型：0为LBS；1为GPS；
            returnXML.append("<lng>" + tloc.getLongitude() + "</lng>");// 经度
            returnXML.append("<lat>" + tloc.getLatitude() + "</lat>");// 纬度
            returnXML.append("<h>" + String.valueOf(tloc.getHeight()) + "</h>");// 高度
            returnXML.append("<s>" + String.valueOf(tloc.getSpeed()) + "</s>");// 速度
            returnXML.append("<d>" + String.valueOf(tloc.getDirection()) + "</d>");// 方向
            returnXML.append("<k>" + tloc.getDeviceId() + "</k>");// simcard
            returnXML.append("<t>" + tloc.getGpstime() + "</t>");// date
            returnXML.append("<p>" + posDesc + "</p>");// 位置描述
            returnXML.append("</l>");
        }
        returnXML.append("</ls>");
        return returnXML.toString();
    }

    /**
     * add by yanglei 最近位置定位
     * 
     * @param gpsDevicedIds
     * @param lbsDevicedIds
     * @return
     */
    public String queryClosestLocSelectedTerm(String entCode, String gpsDeviceIds,
            String lbsDeviceIds, String username) {
        // TOptLog tOptLog = new TOptLog();
        StringBuffer returnXML = new StringBuffer();
        returnXML.append("<?xml version='1.0' encoding='GBK'?>");
        returnXML.append("<ls>");
        // LBS定位：
        /**
         * if (lbsDeviceIds != null && !"".equals(lbsDeviceIds)) { String[]
         * deviceIds = CharTools.split(lbsDeviceIds, ","); LbsLoc lbsLoc =
         * new LbsLoc(); CoordCvtAPI coordCvtApi = new CoordCvtAPI();
         * 
         * for (int i = 0; i < deviceIds.length; i++) { try {
         * System.out.println("DeviceIds[" + i + "]" + deviceIds[i]);
         * Position position = lbsLoc .getPositionByCellid(deviceIds[i]); //
         * System.out.println("Position ====: " + position); double x =
         * position.getCoordX(); double y = position.getCoordY(); //
         * System.out.println("LBS原始坐标：" + x + ";" + y); double[] xs = { x };
         * double[] ys = { y }; String simcard = position.getSimcard(); if
         * (simcard == null || simcard.equals("null")) { simcard =
         * deviceIds[i]; } // String time = position.getDateTime(); // time =
         * this.formatDateStrToTimestamp(time); // 使用当前时间 Calendar now =
         * Calendar.getInstance(); int year = now.get(Calendar.YEAR); int
         * month = now.get(Calendar.MONTH) + 1; int day =
         * now.get(Calendar.DAY_OF_MONTH); int hour =
         * now.get(Calendar.HOUR_OF_DAY); int min =
         * now.get(Calendar.MINUTE); int sec = now.get(Calendar.SECOND);
         * String time = year + "-" + month + "-" + day + " " + hour + ":" +
         * min + ":" + sec; String errorCode =
         * String.valueOf(position.getErrorCode()); String errorDesc = "";
         * if (position.getErrorDesc() != null) { errorDesc =
         * position.getErrorDesc(); } String lngX = ""; String latY = "";
         * String locDesc = ""; // 处理坐标为0，定位失败的情况 if (x == 0 || y == 0) {
         * locDesc = errorCode + "|" + errorDesc; } else { DPoint[] dPoint =
         * coordCvtApi.encryptConvert(xs, ys); lngX =
         * dPoint[0].getEncryptX(); latY = dPoint[0].getEncryptY(); x =
         * dPoint[0].x; y = dPoint[0].y; // System.out.println("LBS偏转坐标：" +
         * lngX + ";" + latY);
         * 
         * locDesc = coordCvtApi.getAddress(String.valueOf(x),
         * String.valueOf(y)); } returnXML.append("<l>");
         * returnXML.append("<id>" + position.getSimcard() + "</id>");//
         * 目标对象序列 returnXML.append("<x>" + (lngX == "" ? "null" : lngX) + "</x>");//
         * 经度 returnXML.append("<y>" + (latY == "" ? "null" : latY) + "</y>");//
         * 纬度 returnXML.append("<type>0</type>");// 类型:LBS=0或GPS=1
         * returnXML.append("<lng>" + x + "</lng>");// 经度
         * returnXML.append("<lat>" + y + "</lat>");// 纬度
         * returnXML.append("<h>0</h>");// 高度 returnXML.append("<s>0</s>");//
         * 速度 returnXML.append("<d>0</d>");// 方向 returnXML.append("<k>" +
         * simcard + "</k>");// simcard returnXML.append("<t>" + time + "</t>");//
         * date returnXML.append("
         * <p> " + locDesc + "
         * </p>
         * ");// 位置描述 returnXML.append("</l>");
         * 
         * if (lbsLocateDAO.isExistRecordLastLoc(simcard)) {
         * lbsLocateDAO.deleteLastLocByDeviceId(simcard); } if (simcard !=
         * null) { lbsLocateDAO.insertLastLocrecordByLbsData(simcard,
         * String.valueOf(x), String.valueOf(y), time);
         * lbsLocateDAO.insertLbsLocrecord(simcard, String .valueOf(x),
         * String.valueOf(y), entCode, time, errorCode, errorDesc, locDesc,
         * lngX, latY); } // tOptLog.setEmpCode(entCode); //
         * tOptLog.setFunFType("F-009"); //
         * tOptLog.setFunCType("F-009-001"); //
         * tOptLog.setOptDesc(locDesc); // if (!errorCode.equals("0")) { //
         * tOptLog.setOptDesc(errorDesc); // } // // tOptLog.setResult(1L); //
         * tOptLog.setAccessIp("255.255.255.255"); //
         * tOptLog.setUserId(1L); // tOptLog.setUserName(username); //
         * LogFactory.newLogInstance("optLogger").info(tOptLog); } catch
         * (Exception ex) { log.error("最近位置LBS定位异常：" + ex.getMessage());
         * ex.printStackTrace(); } } }
         */
        if (gpsDeviceIds != null && !"".equals(gpsDeviceIds)) {
            String[] devs = CharTools.split(gpsDeviceIds, ",");
            String temp = "";
            for (int k = 0; k < devs.length; k++) {
                if (k == devs.length - 1) {
                    temp += "'" + devs[k] + "'";
                } else {
                    temp += "'" + devs[k] + "'" + ",";
                }
            }
            List<TLastLocrecord> loclist = this.tLocateDAO.findLocByDeviceId(temp);
            if (loclist != null) {
                CoordCvtAPI coordCvtApi = new CoordCvtAPI();
                com.sos.sosgps.api.CoordAPI coodApizw = new com.sos.sosgps.api.CoordAPI();
                for (int i = 0; i < loclist.size(); i++) {
                    try {
                        TLastLocrecord tloc = (TLastLocrecord) loclist.get(i);
                        double[] xs = { tloc.getLongitude() };
                        double[] ys = { tloc.getLatitude() };
                        //DPoint[] dPoint = coordCvtApi.encryptConvert(xs, ys);
                        com.sos.sosgps.api.DPoint[] dPoint = coodApizw.encryptConvert(xs, ys);
                        String lngX = dPoint[0].getEncryptX();
                        String latY = dPoint[0].getEncryptY();
                        double x = dPoint[0].x;
                        double y = dPoint[0].y;
                        // posDesc =
                        // coodApi.getAddress(tloc.getJmx(),tloc.getJmy());
                        returnXML.append("<l>");
                        returnXML.append("<id>" + tloc.getDeviceId() + "</id>");// 目标对象序列
                        returnXML.append("<x>" + lngX + "</x>");// 经度
                        returnXML.append("<y>" + latY + "</y>");// 纬度
                        returnXML.append("<type>1</type>");// 类型:LBS=0或GPS=1
                        returnXML.append("<lng>" + x + "</lng>");// 经度
                        returnXML.append("<lat>" + y + "</lat>");// 纬度
                        returnXML.append("<h>" + String.valueOf(tloc.getHeight()) + "</h>");// 高度
                        returnXML.append("<s>" + String.valueOf(tloc.getSpeed()) + "</s>");// 速度
                        returnXML.append("<d>" + String.valueOf(tloc.getDirection()) + "</d>");// 方向
                        returnXML.append("<k>" + tloc.getDeviceId() + "</k>");// simcard
                        returnXML.append("<t>" + tloc.getGpstime() + "</t>");// date
                        returnXML.append("<p>null</p>");// 位置描述
                        returnXML.append("</l>");
                    } catch (Exception ex) {
                        System.out.println("最近位置GPS定位异常：" + ex.getMessage());
                        log.error("最近位置GPS定位异常：" + ex.getMessage());
                        ex.printStackTrace();
                    }
                }

            }
        }
        returnXML.append("</ls>");
        return returnXML.toString();
    }

    /**
     * 将字符串时间数据按yyyy-MM-dd HH:mm:ss格式化
     * 
     * @param dateStr
     * @return
     */
    private String formatDateStrToTimestamp(String dateStr) {
        if (!"".equals(dateStr) && dateStr != null) {
            SimpleDateFormat fomat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String year = dateStr.substring(0, 4);
            String month = dateStr.substring(4, 6);
            String day = dateStr.substring(6, 8);
            String hour = dateStr.substring(8, 10);
            String min = dateStr.substring(10, 12);
            String sec = dateStr.substring(12, 14);
            return year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
        }
        return "";
    }

    /**
     * 由坐标取得位置描述
     * 
     * @param x
     * @param y
     * @return
     */
    public String positionDesc(String x, String y) {// add by sunjingwei
        String positionDesc = "";
        for (int j = 0; j < RETRY; j++) {
            try {
                positionDesc = CoordCvtAPI.getAddress(x, y);
                if (positionDesc == null || positionDesc.length() < 1) {
                    continue;
                } else {
                    break;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return positionDesc;
    }

    public List queryLocByTime(String deviceIds) {
        deviceIds = CharTools.splitAndAdd(deviceIds);
        return tLocateDAO.findLocByDeviceId(deviceIds);
    }

    public List queryLocByTime(String deviceIds, String time) {
        Date timeDate = DateUtility.strToDateTime(time);
        if (timeDate == null || deviceIds == null || deviceIds.length() == 0) {
            return null;
        }
        List<TLocrecord> re = new ArrayList<TLocrecord>();
        String[] deviceIdss = CharTools.split(deviceIds, ",");
        for (int i = 0; i < deviceIdss.length; i++) {
            String deviceId = deviceIdss[i];
            TLocrecord loc = tLocateDAO.queryLocByTime(deviceId, timeDate);
            if (loc != null) {
                re.add(loc);
            }
        }
        return re;
    }

    public List queryLocsByTime(String deviceId, String startTime, String endTime) {
        Date startTimeDate = DateUtility.strToDateTime(startTime);
        Date endTimeDate = DateUtility.strToDateTime(endTime);
        if (startTimeDate == null || endTimeDate == null || deviceId == null
                || deviceId.length() == 0) {
            return null;
        }
        return tLocateDAO.queryLocsByTime(deviceId, startTimeDate, endTimeDate);
    }

    public List queryLocsByTime(TTerminal terminal, String startDate, String endDate,
            String startTime, String endTime) {
        return tLocateDAO.queryLocsByTime(terminal, startDate, endDate, startTime, endTime);
    }

    public String[] telecomGetUnauthorizedToken() {
        String[] reStrArr = new String[5];

        // 随机生成的字符串，用于防止请求的重放，防止外界的非法攻击
        String oauth_nonce = "f7024c89eefcc3e1029ae51743019f59";
        // 发起请求的时间戳，其值是距1970 00:00:00 GMT的秒数，必须是大于0的整数。本次请求的时间戳必须大于或者等于上次的时间戳
        String oauth_timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        // 未授权的Request Token
        String oauth_token = null;
        // 未授权的Request Token对应的Request Token Secret
        String oauth_token_secret = null;
        // 授权的oauth_verifier 验证码
        String oauth_verifier = null;
        // 授权的Access Token
        String oauth_token_access = null;
        // 授权的Access Token对应的Access Token Secret
        String oauth_token_secret_access = null;

        /** ******* 1. 获取未授权的Request Token ******** */
        String url = "http://219.143.8.118:8080/op/oauth/requestToken";
        url += "?oauth_callback=";
        url += "&oauth_version=1.0";
        url += "&oauth_nonce=" + oauth_nonce;
        url += "&oauth_timestamp=" + oauth_timestamp;
        url += "&oauth_consumer_key=" + oauth_consumer_key;
        url += "&oauth_signature_method=PLAINTEXT";
        url += "&oauth_signature=" + oauth_signature;
        int timeout = 60000;

        String[] re = HttpUtil.get(url, timeout);
        String respCode = re == null ? null : re[0];
        if (respCode == null || !respCode.equals("200")) {// 请求失败
            log.error("请求失败：获取未授权的Request Token，原因：" + (re[3] != null ? re[3] : ""));
            return reStrArr;
        } else {
            // oauth_token=d30f9ebf-9743-4930-81dd-7d9b423ce3d0&oauth_token_secret=77e7f737-03c9-4d05-bc5c-e8e639d3ac91&oauth_callback_confirmed=true
            String respMessage = re[2];
            // boolean oauth_callback_confirmed = false;
            if (respMessage != null && respMessage.startsWith("oauth_token=")) {
                int index = respMessage.indexOf("&oauth_token_secret=");
                int index2 = respMessage.indexOf("&oauth_callback_confirmed=");
                oauth_token = respMessage.substring("oauth_token=".length(), index);
                oauth_token_secret = respMessage.substring(index + "&oauth_token_secret=".length(),
                        index2);
                reStrArr[0] = oauth_token;
                reStrArr[1] = oauth_token_secret;
            } else {
                log.error("失败：获取未授权的Request Token，返回异常内容：" + re[3]);
                return reStrArr;
            }
        }

        /** ******* 2. 请求用户授权Request Token ******** */
        url = "http://219.143.8.118:8080/op/oauth/authorizeToken";
        url += "?oauth_token=" + oauth_token;
        url += "&oauth_nonce=" + oauth_nonce;
        url += "&oauth_timestamp=" + oauth_timestamp;
        url += "&oauth_consumer_key=" + oauth_consumer_key;
        url += "&oauth_signature_method=PLAINTEXT";
        url += "&oauth_signature=" + oauth_signature;

        re = HttpUtil.get(url, timeout);
        respCode = re == null ? null : re[0];
        if (respCode == null || !respCode.equals("200")) {// 请求失败
            log.error("请求失败：请求用户授权Request Token，原因：" + (re[3] != null ? re[3] : ""));
            return reStrArr;
        } else {
            // oauth_verifier=544e7c8e-21fe-495c-b2af-ac292e0d1558
            String respMessage = re[2];
            if (respMessage != null && respMessage.startsWith("oauth_verifier=")) {
                oauth_verifier = respMessage.substring("oauth_verifier=".length());
                reStrArr[2] = oauth_verifier;
            } else {
                log.error("失败：请求用户授权Request Token，返回异常内容：" + re[3]);
                return reStrArr;
            }
        }

        /** ******* 3. 使用授权后的Request Token换取Access Token ******** */
        url = "http://219.143.8.118:8080/op/oauth/accessToken";
        url += "?oauth_token=" + oauth_token;
        url += "&oauth_verifier=" + oauth_verifier;
        url += "&oauth_nonce=" + oauth_nonce;
        url += "&oauth_timestamp=" + oauth_timestamp;
        url += "&oauth_consumer_key=" + oauth_consumer_key;
        url += "&oauth_signature_method=PLAINTEXT";
        url += "&oauth_signature=" + oauth_token_secret;
        re = HttpUtil.get(url, timeout);
        respCode = re == null ? null : re[0];
        if (respCode == null || !respCode.equals("200")) {// 请求失败
            log.error("请求失败：使用授权后的Request Token换取Access Token，原因：" + (re[3] != null ? re[3] : ""));
            return reStrArr;
        } else {
            // oauth_token=2d701258-a5eb-4494-a5c0-e944b7f355db&oauth_token_secret=c9a1a4e9-524c-475c-978d-c4dc6ea1cf63
            String respMessage = re[2];
            if (respMessage != null && respMessage.startsWith("oauth_token=")) {
                int index = respMessage.indexOf("&oauth_token_secret=");
                oauth_token_access = respMessage.substring("oauth_token=".length(), index);
                oauth_token_secret_access = respMessage.substring(index
                        + "&oauth_token_secret=".length());
                reStrArr[3] = oauth_token_access;
                reStrArr[4] = oauth_token_secret_access;
            } else {
                log.error("失败：请求用户授权Request Token，返回异常内容：" + re[3]);
                return reStrArr;
            }
        }
        return reStrArr;
    }

    public List locTelecom(String oauth_token_access, String oauth_verifier,
            String oauth_token_secret_access, String deviceIds) {
        if (deviceIds == null || oauth_token_access == null || oauth_verifier == null
                || oauth_token_secret_access == null) return null;
        // 随机生成的字符串，用于防止请求的重放，防止外界的非法攻击
        String oauth_nonce = "f7024c89eefcc3e1029ae51743019f59";

        // 发起请求的时间戳，其值是距1970 00:00:00 GMT的秒数，必须是大于0的整数。本次请求的时间戳必须大于或者等于上次的时间戳
        String oauth_timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        int index = deviceIds.indexOf(",");
        // 定位发起号
        String orId = index == -1 ? deviceIds : deviceIds.substring(0, index);
        // 定位号列表
        String msIds = deviceIds;
        // 请求方式：1/0(粗定位、精定位)
        String requestFlag = "0";
        // 返回方式：1/0(坐标、地址)
        String responseFlag = "1";

        /** ******* 4. 使用 Access Token 访问或修改受保护资源 ******** */
        String url = "http://219.143.8.118:8080/op/rest/lir";
        url += "?orId=" + orId;
        url += "&msIds=" + msIds;
        url += "&requestFlag=" + requestFlag;
        url += "&responseFlag=" + responseFlag;

        url += "&oauth_token=" + oauth_token_access;
        url += "&oauth_nonce=" + oauth_nonce;
        url += "&oauth_timestamp=" + oauth_timestamp;
        url += "&oauth_verifier=" + oauth_verifier;
        url += "&oauth_consumer_key=" + oauth_consumer_key;
        url += "&oauth_signature_method=PLAINTEXT";
        url += "&oauth_signature=" + oauth_token_secret_access;

        int timeout = 60000 * 3;

        String[] re = HttpUtil.get(url, timeout);
        String respCode = re == null ? null : re[0];
        if (respCode == null || !respCode.equals("200")) {// 请求失败
            log.error("请求失败：使用 Access Token 访问或修改受保护资源，原因：" + (re[3] != null ? re[3] : ""));
        } else {
            // <?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
            // <L1PosList>
            // <code>0</code>
            // <itemList>
            // <lat>39.52655</lat>
            // <lon>116.71835</lon>
            // <number>18911292888</number>
            // <pos>网络小区号</pos> <result/>
            // </itemList>
            // <itemList>
            // <number>18911292600</number>
            // <result>定位激活失败</result>
            // </itemList>
            // </L1PosList>
            String respMessage = re[2];
            try {
                respMessage = new String(respMessage.getBytes(), "UTF-8");
                Document doc = DocumentHelper.parseText(respMessage);
                Element root = doc.getRootElement();
                List l = root.elements("itemList");
                List reList = new ArrayList();
                for (Object obj : l) {
                    Element e = (Element) obj;
                    String number = e.elementTextTrim("number");
                    String result = e.elementTextTrim("result");
                    String lat = e.elementTextTrim("lat");
                    String lon = e.elementTextTrim("lon");

                    Position p = new Position();
                    p.setSimcard(number);
                    p.setErrorDesc(result);
                    if (lat == null || lon == null) {} else {
                        p.setCoordX(CharTools.str2Double(lon, 0D));
                        p.setCoordY(CharTools.str2Double(lat, 0D));
                    }
                    reList.add(p);
                }
                return reList;
            } catch (Exception e) {
                log.error("解析定位结果失败", e);
            }
        }
        return null;
    }

    //sos轨迹回放车辆信息分页
    public Page<TLocrecord> listQueryLocsByTime(String deviceId, String startTime, String endTime,
            int pageNo, int pageSize) {
        Date startTimeDate = DateUtility.strToDateTime(startTime);
        Date endTimeDate = DateUtility.strToDateTime(endTime);
        if (startTimeDate == null || endTimeDate == null || deviceId == null
                || deviceId.length() == 0) {
            return null;
        }
        return tLocateDAO.listQueryLocsByTime(deviceId, startTimeDate, endTimeDate, pageNo,
                pageSize);
    }
    
    public List queryLocsByTime2(TTerminal terminal, String startDate, String endDate,
            String startTime, String endTime) {
        return tLocateDAO.queryLocsByTime2(terminal, startDate, endDate, startTime, endTime);
    }
    
    public String lastLocrecordList(String entCode, String deviceIds, String name, String locateType, int gpstime, int inputtime, int startint, int limitint){
        StringBuilder jsonSb = new StringBuilder();
        int total = 0;
        Page<Object[]> list = tLocateDAO.lastLocrecordList(entCode, deviceIds, name, locateType, gpstime, inputtime, startint, limitint);
        if (list != null && list.getResult() != null && list.getResult().size() > 0) {
            total = list.getTotalCount();
            for (Object[] objects : list.getResult()) {
                    String termNameTmp = (String)objects[0];
                    String locateTypeTmp = (String)objects[1];
                    String groupNameTmp = (String) objects[2];
                    String simcardTmp = (String) objects[3];
                    String gpstimeTmp = (String) objects[4];
                    BigDecimal disGpstimeBTmp = (BigDecimal)objects[5];
                    float disGpstimeTmp = disGpstimeBTmp.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                    String lastTimeTmp = (String) objects[6];
                    BigDecimal disLastTimeBTmp = (BigDecimal)objects[5];
                    float disLastTimeTmp = disLastTimeBTmp.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                    jsonSb.append("{");
                    jsonSb.append("termName:'" + CharTools.javaScriptEscape(termNameTmp) + "',");
                    jsonSb.append("locateType:'" + locateTypeTmp + "',");
                    jsonSb.append("groupName:'" + groupNameTmp + "',");
                    jsonSb.append("simcard:'" + simcardTmp + "',");
                    jsonSb.append("gpstime:'" + gpstimeTmp + "',");
                    jsonSb.append("disGpstime:'" + disGpstimeTmp + "',");
                    jsonSb.append("lastTime:'" + lastTimeTmp + "',");
                    jsonSb.append("disLastTime:'" + disLastTimeTmp + "'");
                    jsonSb.append("},");
            }
            if (jsonSb.length() > 0) {
                jsonSb.deleteCharAt(jsonSb.length() - 1);
            }
        }
        log.info("json = " + "{total:" + total + ",data:["
                + jsonSb.toString() + "]}");
        return "{total:" + total + ",data:[" + jsonSb.toString() + "]}";

    }
    
    public void lastLocrecordListExcel(HttpServletResponse response, String entCode, String deviceIds, String name, String locateType, int gpstime, int inputtime){
        try {
            if(deviceIds == null || deviceIds.length() <= 0){
                ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
                // header
                excelWorkBook.addHeader("名称", 15);
                excelWorkBook.addHeader("终端类型", 15);
                excelWorkBook.addHeader("部门", 15);
                excelWorkBook.addHeader("手机号码", 15);
                excelWorkBook.addHeader("最后一次成功定位时间", 15);
                excelWorkBook.addHeader("距当前时间(小时)", 15);
                excelWorkBook.addHeader("最后一次数据接收时间", 15);
                excelWorkBook.addHeader("距当前时间(小时)", 15);
                excelWorkBook.write();
                return;
            }
            Page<Object[]> list = tLocateDAO.lastLocrecordList(entCode, deviceIds, name, locateType, gpstime, inputtime, 1, 65536);

            ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
            // header
            excelWorkBook.addHeader("名称", 15);
            excelWorkBook.addHeader("终端类型", 15);
            excelWorkBook.addHeader("部门", 15);
            excelWorkBook.addHeader("手机号码", 15);
            excelWorkBook.addHeader("最后一次成功定位时间", 15);
            excelWorkBook.addHeader("距当前时间(小时)", 15);
            excelWorkBook.addHeader("最后一次数据接收时间", 15);
            excelWorkBook.addHeader("距当前时间(小时)", 15);
            if (list != null && list.getResult() != null && list.getResult().size() > 0) {
                int row = 0;
                for (Object[] objects : list.getResult()) {
                    String termNameTmp = (String) objects[0];
                    String locateTypeTmp = (String) objects[1];
                    if (locateTypeTmp.equals("0")) {
                        locateTypeTmp = "人员";
                    } else {
                        locateTypeTmp = "车辆";
                    }
                    String groupNameTmp = (String) objects[2];
                    String simcardTmp = (String) objects[3];
                    String gpstimeTmp = (String) objects[4];
                    BigDecimal disGpstimeBTmp = (BigDecimal) objects[5];
                    float disGpstimeTmp = disGpstimeBTmp.setScale(2, BigDecimal.ROUND_HALF_UP)
                            .floatValue();
                    String lastTimeTmp = (String) objects[6];
                    BigDecimal disLastTimeBTmp = (BigDecimal) objects[5];
                    float disLastTimeTmp = disLastTimeBTmp.setScale(2, BigDecimal.ROUND_HALF_UP)
                            .floatValue();
                    int col = 0;
                    row += 1;
                    excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(termNameTmp));
                    excelWorkBook.addCell(col++, row, locateTypeTmp);
                    excelWorkBook.addCell(col++, row, groupNameTmp);
                    excelWorkBook.addCell(col++, row, simcardTmp);
                    excelWorkBook.addCell(col++, row, gpstimeTmp);
                    excelWorkBook.addCell(col++, row, disGpstimeTmp + "");
                    excelWorkBook.addCell(col++, row, lastTimeTmp);
                    excelWorkBook.addCell(col++, row, disLastTimeTmp + "");
                }
            }
            excelWorkBook.write();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        
    }
}
