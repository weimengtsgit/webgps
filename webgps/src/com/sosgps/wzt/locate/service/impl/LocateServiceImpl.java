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

    // ���Ŷ�λkey
    private String oauth_consumer_key;

    // oauth_consumer_key��Ӧ����Կ
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
     * t_locrecord�в���û��ƫת����ȡ���������ݲ���ȡλ��������ƫת����
     * 
     * @return
     */
    public void updateNoEncryptionAndNoLocDesc() {
        List<TLocrecord> loclist = this.tLocateDAO.queryNoEncryptionAndNoLocDesc();

        if (loclist != null) {
            log.info("��ʱ����-����û��ƫת����ȡ���������ݲ���ȡλ��������ƫת����-��ʼ");
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
                    log.error("����û��ƫת����ȡ���������ݲ���ȡλ��������ƫת����-��ȡλ������error! x=" + lngX + " y=" + latY
                            + "error=" + e.getMessage());
                }
                // ���¼�¼
                try {
                    tLocrecordDao.update(loclist.get(i));

                } catch (Exception e) {
                    log.error("����û��ƫת����ȡ���������ݲ���ȡλ��������ƫת����-��������error! x=" + lngX + " y=" + latY
                            + "error=" + e.getMessage());
                }
                log.info("��ʱ����-����û��ƫת����ȡ���������ݲ���ȡλ��������ƫת����-���");
            }
        }

    }

    /**
     * ��ѯѡ���ն˵�����λ��
     */
    /*
     * public String findLastLoc(String deviceIds) { StringBuffer returnXML =
     * new StringBuffer(); returnXML.append("<?xml version='1.0'
     * encoding='UTF-8' ?>"); returnXML.append("<ls>"); if (deviceIds == null)
     * return returnXML.toString(); List<TLastLocrecord> loclist =
     * this.tLocateDAO.findLocByDeviceId(deviceIds); int size = loclist.size();
     * double[] xx = new double[size];//ԭʼ���� double[] yy = new
     * double[size];//ԭʼγ�� com.mapabc.geom.CoordCvtAPI coordAPI = new
     * CoordCvtAPI(); com.mapabc.geom.DPoint[] pts =new
     * com.mapabc.geom.DPoint[size] ; for (int i = 0; i < size; i++) { double x
     * = loclist.get(i).getLongitude(); double y = loclist.get(i).getLatitude();
     * double fx = loclist.get(i).getDirection(); double speed=
     * loclist.get(i).getSpeed(); if( speed > 0){ com.mapabc.geom.DPoint dp =
     * coordAPI.roadConvert(x, y,fx); loclist.get(i).setJmx(dp.getEncryptX() );
     * loclist.get(i).setJmy( dp.getEncryptY() );
     * //System.out.println("�ٶȴ���0���е�·��ƫ"); }else{ double xs[] ={x}; double
     * ys[]={y}; try { com.mapabc.geom.DPoint[] dps =
     * coordAPI.encryptConvert(xs, ys); loclist.get(i).setJmx(
     * coordAPI.encrypt(dps[0].x ) );
     * loclist.get(i).setJmy(coordAPI.encrypt(dps[0].y ) );
     * //System.out.println("�ٶ�==0�����е�·��ƫ"); } catch (Exception e) { // TODO
     * Auto-generated catch block e.printStackTrace(); } } } for (int i=0;
     * i<loclist.size(); i++) { TLastLocrecord tloc = loclist.get(i); String
     * posDesc = ""; // posDesc = coodApi.getAddress(tloc.getJmx(),
     * tloc.getJmy()); returnXML.append("<l>"); returnXML.append("<id>" +
     * tloc.getId() + "</id>");// Ŀ��������� returnXML.append("<x>" + tloc.getJmx()
     * + "</x>");// ���� returnXML.append("<y>" + tloc.getJmy() + "</y>");// γ��
     * returnXML.append("<lng>" + tloc.getLongitude() + "</lng>");// ����
     * returnXML.append("<lat>" + tloc.getLatitude() + "</lat>");// γ��
     * returnXML.append("<h>" + String.valueOf(tloc.getHeight()) + "</h>");// �߶�
     * returnXML.append("<s>" + String.valueOf(tloc.getSpeed()) + "</s>");// �ٶ�
     * returnXML.append("<d>" + String.valueOf(tloc.getDirection()) + "</d>");//
     * ���� returnXML.append("<k>" + tloc.getDeviceId() + "</k>");// simcard
     * returnXML.append("<t>" + tloc.getGpstime() + "</t>");// date
     * returnXML.append("<p>" + posDesc + "</p>");//λ������
     * returnXML.append("</l>"); } returnXML.append("</ls>");
     * System.out.println("λ�ø���:\r\n" +returnXML.toString() );
     * System.out.println("������ ��·��ƫ����"); return returnXML.toString(); }
     */
    public String findLastLoc(String deviceIds) {
        return findLastLoc(deviceIds, true);
    }

    /**
     * ��ѯѡ���ն˵�����λ��
     */
    public String findLastLoc(String deviceIds, boolean isEncrypt) {
        StringBuffer returnXML = new StringBuffer();
        returnXML.append("<?xml version='1.0' encoding='GBK' ?>");
        returnXML.append("<ls>");
        if (deviceIds == null) {
            returnXML.append("</ls>");
            return returnXML.toString();
        }
        /* Ϊÿ��deviceid��ӵ����ţ�����SQL����Ч���ִ���add by shiguang.zhou */
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
        double[] xx = new double[size];// ԭʼ����
        double[] yy = new double[size];// ԭʼγ��

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
            if (!isEncrypt) {// C/S����
                loclist.get(i).setJmx(x + "");
                loclist.get(i).setJmy(y + "");
                continue;
            }
            if (speed > 0) {
                try {
                    // modify  2009-06-03 ���Ӿ�ƫ�ж� GPS��ƫLBS����ƫ
                    //String tLocateType = loclist.get(i).getLocateType(); // �ն�����0��lbs
                    // 1:gps
                    //					if (tLocateType != null && "1".equals(tLocateType)) {
                    //						com.mapabc.geom.DPoint dp = coordAPI.roadConvert(x, y,
                    //								fx);
                    //						if (dp == null) {
                    //							log.error("����λ��findLastLoc-��·��ƫerror! x=" + x
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
                    log.error("����λ��findLastLoc-ƫת����error! x=" + x + " y=" + y);
                }
                // System.out.println("�ٶȴ���0���е�·��ƫ");
            } else {
                double xs[] = { x };
                double ys[] = { y };
                try {
                    //					com.mapabc.geom.DPoint[] dps = coordAPI.encryptConvert(xs,
                    //							ys);
                    com.sos.sosgps.api.DPoint[] dps = coordApizw.encryptConvert(xs, ys);

                    loclist.get(i).setJmx(coordAPI.encrypt(dps[0].x));
                    loclist.get(i).setJmy(coordAPI.encrypt(dps[0].y));
                    // System.out.println("�ٶ�==0�����е�·��ƫ");
                } catch (Exception e) {
                    log.error("����λ��findLastLoc-ƫת����error! x=" + x + " y=" + y);
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
            returnXML.append("<id>" + tloc.getId() + "</id>");// Ŀ���������
            returnXML.append("<x>" + tloc.getJmx() + "</x>");// ����
            returnXML.append("<y>" + tloc.getJmy() + "</y>");// γ��
            returnXML.append("<type>" + tloc.getLocateType() + "</type>");// �ն����ͣ�0ΪLBS��1ΪGPS��
            returnXML.append("<lng>" + tloc.getLongitude() + "</lng>");// ����
            returnXML.append("<lat>" + tloc.getLatitude() + "</lat>");// γ��
            returnXML.append("<h>" + String.valueOf(tloc.getHeight()) + "</h>");// �߶�
            if (tloc.getSpeed() <= 1) {
                returnXML.append("<s>0</s>");// �ٶ�
            } else {
                returnXML.append("<s>" + String.valueOf(tloc.getSpeed()) + "</s>");// �ٶ�
            }
            returnXML.append("<d>" + String.valueOf(tloc.getDirection()) + "</d>");// ����
            returnXML.append("<k>" + tloc.getDeviceId() + "</k>");// simcard
            returnXML.append("<t>" + format.format(tloc.getGpstime()) + "</t>");// date
            returnXML.append("<p>" + CharTools.replayStr(posDesc) + "</p>");// λ������
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
                // ����ƫתԭʼ����
                pts = coordApizw.encryptConvert(xx, yy);
            } catch (Exception e) {
                log.error("ƫת����error! x=" + xx + " y=" + yy);
            }
            long etime = System.currentTimeMillis();
            System.out.println("ʵʱ���٣�����ƫת����ʱ�䣺" + (etime - btime) / 1000 + "S");
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
     * ��ѯѡ���ն˵�����λ��
     */
    public String findLastLocBack(String deviceIds) {
        StringBuffer returnXML = new StringBuffer();
        returnXML.append("<?xml version='1.0' encoding='GBK' ?>");
        returnXML.append("<ls>");
        if (deviceIds == null) return returnXML.toString();
        /* Ϊÿ��deviceid��ӵ����ţ�����SQL����Ч���ִ���add by shiguang.zhou */
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
        double[] xx = new double[size];// ԭʼ����
        double[] yy = new double[size];// ԭʼγ��

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
            // ����ƫתԭʼ����
            pts = coordApizw.encryptConvert(xx, yy);
        } catch (Exception e) {
            log.error("����λ��findLastLocBack-ƫת����error! x=" + xx + " y=" + yy);
        }
        long etime = System.currentTimeMillis();
        System.out.println("ʵʱ���٣�����ƫת����ʱ�䣺" + (etime - btime) / 1000 + "S");

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
            returnXML.append("<id>" + tloc.getId() + "</id>");// Ŀ���������
            returnXML.append("<x>" + pts[i].getEncryptX() + "</x>");// ����
            returnXML.append("<y>" + pts[i].getEncryptY() + "</y>");// γ��
            returnXML.append("<type>" + tloc.getLocateType() + "</type>");// �ն����ͣ�0ΪLBS��1ΪGPS��
            returnXML.append("<lng>" + tloc.getLongitude() + "</lng>");// ����
            returnXML.append("<lat>" + tloc.getLatitude() + "</lat>");// γ��
            returnXML.append("<h>" + String.valueOf(tloc.getHeight()) + "</h>");// �߶�
            returnXML.append("<s>" + String.valueOf(tloc.getSpeed()) + "</s>");// �ٶ�
            returnXML.append("<d>" + String.valueOf(tloc.getDirection()) + "</d>");// ����
            returnXML.append("<k>" + tloc.getDeviceId() + "</k>");// simcard
            returnXML.append("<t>" + tloc.getGpstime() + "</t>");// date
            returnXML.append("<p>" + posDesc + "</p>");// λ������
            returnXML.append("</l>");
        }
        returnXML.append("</ls>");

        return returnXML.toString();
    }

    /**
     * ����Ϊƫת����
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
        /* Ϊÿ��deviceid��ӵ����ţ�����SQL����Ч���ִ���add by shiguang.zhou */
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
        double[] xx = new double[size];// ԭʼ����
        double[] yy = new double[size];// ԭʼγ��

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
            // C/S����
            // modify sunye 2009-06-02 ���ӵ�·��ƫ�ж� isRoadConvert=true��ƫ
            isRoadConvert = false;
            try {
                if (isRoadConvert == true) {
                    com.mapabc.geom.DPoint dp = coordAPI.roadConvert(x, y, fx);
                    if (dp == null) {
                        log.error("����λ��findLastLoc-��·��ƫerror! x=" + x + " y=" + y + " fx=" + fx);
                    } else {
                        loclist.get(i).setJmx(String.valueOf(dp.getX()));
                        loclist.get(i).setJmy(String.valueOf(dp.getY()));
                    }
                } else {
                    double xs[] = { x };
                    double ys[] = { y };
                    // ����ƫת
                    com.sos.sosgps.api.DPoint[] dps = coordApizw.encryptConvert(xs, ys);
                    loclist.get(i).setJmx(String.valueOf(dps[0].x));
                    loclist.get(i).setJmy(String.valueOf(dps[0].y));
                }

            } catch (Exception e) {
                log.error("����λ��findLastLocWithDeflect-ƫת����error! x=" + x + " y=" + y);
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
            returnXML.append("<id>" + tloc.getId() + "</id>");// Ŀ���������
            returnXML.append("<x>" + tloc.getJmx() + "</x>");// ����
            returnXML.append("<y>" + tloc.getJmy() + "</y>");// γ��
            returnXML.append("<type>" + tloc.getLocateType() + "</type>");// �ն����ͣ�0ΪLBS��1ΪGPS��
            returnXML.append("<lng>" + tloc.getLongitude() + "</lng>");// ����
            returnXML.append("<lat>" + tloc.getLatitude() + "</lat>");// γ��
            returnXML.append("<h>" + String.valueOf(tloc.getHeight()) + "</h>");// �߶�
            returnXML.append("<s>" + String.valueOf(tloc.getSpeed()) + "</s>");// �ٶ�
            returnXML.append("<d>" + String.valueOf(tloc.getDirection()) + "</d>");// ����
            returnXML.append("<k>" + tloc.getDeviceId() + "</k>");// simcard
            returnXML.append("<t>" + tloc.getGpstime() + "</t>");// date
            returnXML.append("<p>" + posDesc + "</p>");// λ������
            returnXML.append("</l>");
        }
        returnXML.append("</ls>");
        return returnXML.toString();
    }

    /**
     * add by yanglei ���λ�ö�λ
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
        // LBS��λ��
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
         * System.out.println("LBSԭʼ���꣺" + x + ";" + y); double[] xs = { x };
         * double[] ys = { y }; String simcard = position.getSimcard(); if
         * (simcard == null || simcard.equals("null")) { simcard =
         * deviceIds[i]; } // String time = position.getDateTime(); // time =
         * this.formatDateStrToTimestamp(time); // ʹ�õ�ǰʱ�� Calendar now =
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
         * String locDesc = ""; // ��������Ϊ0����λʧ�ܵ���� if (x == 0 || y == 0) {
         * locDesc = errorCode + "|" + errorDesc; } else { DPoint[] dPoint =
         * coordCvtApi.encryptConvert(xs, ys); lngX =
         * dPoint[0].getEncryptX(); latY = dPoint[0].getEncryptY(); x =
         * dPoint[0].x; y = dPoint[0].y; // System.out.println("LBSƫת���꣺" +
         * lngX + ";" + latY);
         * 
         * locDesc = coordCvtApi.getAddress(String.valueOf(x),
         * String.valueOf(y)); } returnXML.append("<l>");
         * returnXML.append("<id>" + position.getSimcard() + "</id>");//
         * Ŀ��������� returnXML.append("<x>" + (lngX == "" ? "null" : lngX) + "</x>");//
         * ���� returnXML.append("<y>" + (latY == "" ? "null" : latY) + "</y>");//
         * γ�� returnXML.append("<type>0</type>");// ����:LBS=0��GPS=1
         * returnXML.append("<lng>" + x + "</lng>");// ����
         * returnXML.append("<lat>" + y + "</lat>");// γ��
         * returnXML.append("<h>0</h>");// �߶� returnXML.append("<s>0</s>");//
         * �ٶ� returnXML.append("<d>0</d>");// ���� returnXML.append("<k>" +
         * simcard + "</k>");// simcard returnXML.append("<t>" + time + "</t>");//
         * date returnXML.append("
         * <p> " + locDesc + "
         * </p>
         * ");// λ������ returnXML.append("</l>");
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
         * (Exception ex) { log.error("���λ��LBS��λ�쳣��" + ex.getMessage());
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
                        returnXML.append("<id>" + tloc.getDeviceId() + "</id>");// Ŀ���������
                        returnXML.append("<x>" + lngX + "</x>");// ����
                        returnXML.append("<y>" + latY + "</y>");// γ��
                        returnXML.append("<type>1</type>");// ����:LBS=0��GPS=1
                        returnXML.append("<lng>" + x + "</lng>");// ����
                        returnXML.append("<lat>" + y + "</lat>");// γ��
                        returnXML.append("<h>" + String.valueOf(tloc.getHeight()) + "</h>");// �߶�
                        returnXML.append("<s>" + String.valueOf(tloc.getSpeed()) + "</s>");// �ٶ�
                        returnXML.append("<d>" + String.valueOf(tloc.getDirection()) + "</d>");// ����
                        returnXML.append("<k>" + tloc.getDeviceId() + "</k>");// simcard
                        returnXML.append("<t>" + tloc.getGpstime() + "</t>");// date
                        returnXML.append("<p>null</p>");// λ������
                        returnXML.append("</l>");
                    } catch (Exception ex) {
                        System.out.println("���λ��GPS��λ�쳣��" + ex.getMessage());
                        log.error("���λ��GPS��λ�쳣��" + ex.getMessage());
                        ex.printStackTrace();
                    }
                }

            }
        }
        returnXML.append("</ls>");
        return returnXML.toString();
    }

    /**
     * ���ַ���ʱ�����ݰ�yyyy-MM-dd HH:mm:ss��ʽ��
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
     * ������ȡ��λ������
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

        // ������ɵ��ַ��������ڷ�ֹ������طţ���ֹ���ķǷ�����
        String oauth_nonce = "f7024c89eefcc3e1029ae51743019f59";
        // ���������ʱ�������ֵ�Ǿ�1970 00:00:00 GMT�������������Ǵ���0�����������������ʱ���������ڻ��ߵ����ϴε�ʱ���
        String oauth_timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        // δ��Ȩ��Request Token
        String oauth_token = null;
        // δ��Ȩ��Request Token��Ӧ��Request Token Secret
        String oauth_token_secret = null;
        // ��Ȩ��oauth_verifier ��֤��
        String oauth_verifier = null;
        // ��Ȩ��Access Token
        String oauth_token_access = null;
        // ��Ȩ��Access Token��Ӧ��Access Token Secret
        String oauth_token_secret_access = null;

        /** ******* 1. ��ȡδ��Ȩ��Request Token ******** */
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
        if (respCode == null || !respCode.equals("200")) {// ����ʧ��
            log.error("����ʧ�ܣ���ȡδ��Ȩ��Request Token��ԭ��" + (re[3] != null ? re[3] : ""));
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
                log.error("ʧ�ܣ���ȡδ��Ȩ��Request Token�������쳣���ݣ�" + re[3]);
                return reStrArr;
            }
        }

        /** ******* 2. �����û���ȨRequest Token ******** */
        url = "http://219.143.8.118:8080/op/oauth/authorizeToken";
        url += "?oauth_token=" + oauth_token;
        url += "&oauth_nonce=" + oauth_nonce;
        url += "&oauth_timestamp=" + oauth_timestamp;
        url += "&oauth_consumer_key=" + oauth_consumer_key;
        url += "&oauth_signature_method=PLAINTEXT";
        url += "&oauth_signature=" + oauth_signature;

        re = HttpUtil.get(url, timeout);
        respCode = re == null ? null : re[0];
        if (respCode == null || !respCode.equals("200")) {// ����ʧ��
            log.error("����ʧ�ܣ������û���ȨRequest Token��ԭ��" + (re[3] != null ? re[3] : ""));
            return reStrArr;
        } else {
            // oauth_verifier=544e7c8e-21fe-495c-b2af-ac292e0d1558
            String respMessage = re[2];
            if (respMessage != null && respMessage.startsWith("oauth_verifier=")) {
                oauth_verifier = respMessage.substring("oauth_verifier=".length());
                reStrArr[2] = oauth_verifier;
            } else {
                log.error("ʧ�ܣ������û���ȨRequest Token�������쳣���ݣ�" + re[3]);
                return reStrArr;
            }
        }

        /** ******* 3. ʹ����Ȩ���Request Token��ȡAccess Token ******** */
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
        if (respCode == null || !respCode.equals("200")) {// ����ʧ��
            log.error("����ʧ�ܣ�ʹ����Ȩ���Request Token��ȡAccess Token��ԭ��" + (re[3] != null ? re[3] : ""));
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
                log.error("ʧ�ܣ������û���ȨRequest Token�������쳣���ݣ�" + re[3]);
                return reStrArr;
            }
        }
        return reStrArr;
    }

    public List locTelecom(String oauth_token_access, String oauth_verifier,
            String oauth_token_secret_access, String deviceIds) {
        if (deviceIds == null || oauth_token_access == null || oauth_verifier == null
                || oauth_token_secret_access == null) return null;
        // ������ɵ��ַ��������ڷ�ֹ������طţ���ֹ���ķǷ�����
        String oauth_nonce = "f7024c89eefcc3e1029ae51743019f59";

        // ���������ʱ�������ֵ�Ǿ�1970 00:00:00 GMT�������������Ǵ���0�����������������ʱ���������ڻ��ߵ����ϴε�ʱ���
        String oauth_timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        int index = deviceIds.indexOf(",");
        // ��λ�����
        String orId = index == -1 ? deviceIds : deviceIds.substring(0, index);
        // ��λ���б�
        String msIds = deviceIds;
        // ����ʽ��1/0(�ֶ�λ������λ)
        String requestFlag = "0";
        // ���ط�ʽ��1/0(���ꡢ��ַ)
        String responseFlag = "1";

        /** ******* 4. ʹ�� Access Token ���ʻ��޸��ܱ�����Դ ******** */
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
        if (respCode == null || !respCode.equals("200")) {// ����ʧ��
            log.error("����ʧ�ܣ�ʹ�� Access Token ���ʻ��޸��ܱ�����Դ��ԭ��" + (re[3] != null ? re[3] : ""));
        } else {
            // <?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
            // <L1PosList>
            // <code>0</code>
            // <itemList>
            // <lat>39.52655</lat>
            // <lon>116.71835</lon>
            // <number>18911292888</number>
            // <pos>����С����</pos> <result/>
            // </itemList>
            // <itemList>
            // <number>18911292600</number>
            // <result>��λ����ʧ��</result>
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
                log.error("������λ���ʧ��", e);
            }
        }
        return null;
    }

    //sos�켣�طų�����Ϣ��ҳ
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
                excelWorkBook.addHeader("����", 15);
                excelWorkBook.addHeader("�ն�����", 15);
                excelWorkBook.addHeader("����", 15);
                excelWorkBook.addHeader("�ֻ�����", 15);
                excelWorkBook.addHeader("���һ�γɹ���λʱ��", 15);
                excelWorkBook.addHeader("�൱ǰʱ��(Сʱ)", 15);
                excelWorkBook.addHeader("���һ�����ݽ���ʱ��", 15);
                excelWorkBook.addHeader("�൱ǰʱ��(Сʱ)", 15);
                excelWorkBook.write();
                return;
            }
            Page<Object[]> list = tLocateDAO.lastLocrecordList(entCode, deviceIds, name, locateType, gpstime, inputtime, 1, 65536);

            ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
            // header
            excelWorkBook.addHeader("����", 15);
            excelWorkBook.addHeader("�ն�����", 15);
            excelWorkBook.addHeader("����", 15);
            excelWorkBook.addHeader("�ֻ�����", 15);
            excelWorkBook.addHeader("���һ�γɹ���λʱ��", 15);
            excelWorkBook.addHeader("�൱ǰʱ��(Сʱ)", 15);
            excelWorkBook.addHeader("���һ�����ݽ���ʱ��", 15);
            excelWorkBook.addHeader("�൱ǰʱ��(Сʱ)", 15);
            if (list != null && list.getResult() != null && list.getResult().size() > 0) {
                int row = 0;
                for (Object[] objects : list.getResult()) {
                    String termNameTmp = (String) objects[0];
                    String locateTypeTmp = (String) objects[1];
                    if (locateTypeTmp.equals("0")) {
                        locateTypeTmp = "��Ա";
                    } else {
                        locateTypeTmp = "����";
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
