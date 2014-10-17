package com.sosgps.wzt.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sosgps.wzt.locate.dao.hibernate.LocateDAOImpl;
import com.sosgps.wzt.orm.TLocrecord;
import com.sosgps.wzt.util.GeoUtils.GaussSphere;

/**
 * @title Description:Ư�Ƶ���˹�����
 * @copyright Copyright (c) 2001
 * @company www.sosgps.net.cn
 * @author liuhongxiao
 * @version 1.0
 */

public final class DriftFilter {

    private static final Logger logger = Logger.getLogger(DriftFilter.class);
    
    private final int RATE_VALVE = 100;

    private int interval = 30 * 60;// ����֮��ʱ�����intervalֵ�õ���Ϊ�����㱣��,��λ:��(���Խ��4.�ڻ𳵡��ɻ�����ʻ�������ٶȹ����ϱ��Ķ�λ���ݱ�����Ư�Ƶ���˵�����)

    private double speed = 200d; // ����֮��(�����/ʱ��)����speedֵ�õ���ΪƯ�Ƶ�ȥ��,��λ:����/Сʱ

    private double rate = 1d;// ����Ư�Ƶ�����Ч��λ���ݵİٷֱȣ�����ñ�������rateֵ��˵�����ι���ʧ�ܣ�ɾ����λ�����еĵ�һ�����ݣ����¼���

    public DriftFilter() {
    }

    public DriftFilter(int interval, double speed, double rate) {
        this.interval = interval;
        this.speed = speed;
        this.rate = rate;
    }

    /**
     * �ж�unknown���Ƿ�ΪƯ�Ƶ�
     * 
     * @param effect ��Ч��
     * @param unknown ������
     * @return
     * @throws Exception
     */
    public boolean isDrift(PointBean effect, PointBean unknown) throws Exception {
        effect.setCover(false);
        if (getInterval(effect, unknown) > interval) {
            return true;
        } else if (this.getSpeed(effect, unknown) > speed) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Ư�Ƶ����
     * 
     * @param list ��λ����
     * @return
     * @throws Exception
     */
    public List<PointBean> deal(List<TLocrecord> list) throws Exception {
        if (list == null || list.size() < 1) {
            return null;
        }
        List<List<PointBean>> parentList = this.mergeSames(list);
        List<PointBean> rtnList = new ArrayList<PointBean>();
        for (List<PointBean> childList : parentList) {
            rtnList.addAll(this.removeDrift(this.removeUseless(childList)));
        }
        return rtnList;
    }

    /**
     * ȥ����Ч����,����γ��Ϊ0.0�����ݹ��˵�
     * 
     * @param list �ϲ���Ķ�λ����
     * @return
     */
    private List<PointBean> removeUseless(List<PointBean> list) {
        int size = list.size();
        if (size < 1) {
            return list;
        }
        List<PointBean> rtnList = new ArrayList<PointBean>();
        for (int i = 0; i < size; i++) {
            PointBean current = list.get(i);
            if (!current.getLatitude().equals(0f) && !current.getLongitude().equals(0f)) {
                rtnList.add(current);
            }
        }
        return rtnList;
    }

    /**
     * Ư�Ƶ����,�ݹ����,ѡ���б��еĵ�һ������Ϊ��׼��,�����¼��С�ڵ���RATE_VALVEֵ,�������е����Ư�Ƶ����;
     * �������RATE_VALVEֵ,����RATE_VALVE����¼����Ư�Ƶ����,
     * ����ñ�������rateֵ,˵�����ι���ʧ��,ɾ�����м�¼�еĵ�һ������,���¼���
     * 
     * modify by liuhongxiao 2012-06-15
     * ����һ��Ư�Ƶ���˵Ļ�׼��,���ĳһ�����ǰ�����ڵ���������ٶȶ�����speed,�õ㶨��ΪƯ�Ƶ�
     * 
     * @param list ��Ч�Ķ�λ����
     * @return
     */
    private List<PointBean> removeDrift(List<PointBean> list) throws Exception {
        int size = list.size();
        if (size <= 1) {
            return list;
        }

        PointBean prev = null;
        List<PointBean> normal = new ArrayList<PointBean>();
        List<PointBean> drift = new ArrayList<PointBean>();
        int driftCounter = 0;
        for (int i = 0; i < size; i++) {
            PointBean current = list.get(i);
            if (prev != null && this.getSpeed(prev, current) > speed) {
                drift.add(current);
                driftCounter++;
            } else {
                // �׵���Ϊ��׼��
                if (driftCounter > 0 && normal.size() == 1) {
                    /** ����һ��Ư�Ƶ���˵Ļ�׼��,���Ư�Ƶ�/������С��rate,�׵�Ư�� * */
                    PointBean descPrev = current;
                    int driftSize = drift.size();
                    for (int j = 0; j < driftCounter; j++) {
                        PointBean descCurrent = drift.get(driftSize - 1 - j);
                        if (this.getSpeed(descPrev, descCurrent) > speed) {
                            continue;
                        } else {
                            normal.add(normal.size() - j, descCurrent);
                            drift.remove(driftSize - 1 - j);
                            descPrev = descCurrent;
                        }
                    }
                    driftCounter = 0;
                    // ���Ư�Ƶ�/������С��rate,�׵�Ư��,���ι���ʧ��
                    if (this.getRate(drift, normal) <= rate) {
                        list.remove(0);
                        return removeDrift(list);
                    }
                }
                // ����
                else if (driftCounter > 0) {
                    /** ����һ��Ư�Ƶ���˵Ļ�׼��,���ĳһ�����ǰ�����ڵ���������ٶȶ�����speed,�õ㶨��ΪƯ�Ƶ� * */
                    PointBean descPrev = current;
                    int driftSize = drift.size();
                    for (int j = 0; j < driftCounter; j++) {
                        PointBean descCurrent = drift.get(driftSize - 1 - j);
                        if (this.getSpeed(descPrev, descCurrent) > speed) {
                            continue;
                        } else {
                            try{
                                normal.add(normal.size() - j, descCurrent);
                                drift.remove(driftSize - 1 - j);
                                descPrev = descCurrent;
                            }catch(Exception e){
                                logger.error(e.getMessage(), e);
                            }
                        }
                    }
                    driftCounter = 0;
                    // ���ι���ʧ��
                    if (size >= RATE_VALVE && this.getRate(drift, normal) > rate) {
                        list.remove(0);
                        return removeDrift(list);
                    }
                }

                prev = current;
                normal.add(current);
            }
        }

        // ʧ��
        if (this.getRate(drift, normal) > rate) {
            list.remove(0);
            // �ݹ����
            return removeDrift(list);
        }
        // �ɹ�
        else return normal;
    }

    /**
     * ����γ����ͬ���߾���Ϊ0.0��������ϲ�Ϊͬһ����(ȥ�أ�ͬʱ���Խ��2.�����γ����ͬ��������Ư�Ƶ㲻�ܹ��� ������)
     * 
     * @param list
     * @return ȥ�غ������,�Ұ�ʱ��������intervalֵ����
     */
    private List<List<PointBean>> mergeSames(List<TLocrecord> list) {
        List<List<PointBean>> parentList = new ArrayList<List<PointBean>>();
        List<PointBean> childList = new ArrayList<PointBean>();
        PointBean prev = null;
        for (int i = 0; i < list.size(); i++) {
            TLocrecord current = list.get(i);
            Float latitude = current.getLatitude();
            Float longitude = current.getLongitude();
            if (latitude == null || longitude == null) {
                continue;
            } else if (prev == null) {
                prev = new PointBean();
                prev.setLongitude(longitude);
                prev.setLatitude(latitude);
                prev.setGpstime(current.getGpstime());
                prev.setDistance(current.getDistance());
                prev.setSpeed(current.getSpeed() == null ? 0 : current.getSpeed());
                prev.setStatlliteNum(current.getStatlliteNum());
                prev.setLocateType(current.getLocateType());
                prev.setCover(false);
                List<TLocrecord> prevLocList = new ArrayList<TLocrecord>();
                prevLocList.add(current);
                prev.setLocrecordList(prevLocList);
                childList.add(prev);
                parentList.add(childList);
            }
            // ��γ����ͬ||����Ϊ0.0��������ϲ� add 203-7-16 gpsTime��ͬ�ĺϲ�
            else if (((current.getLatitude().equals(prev.getLatitude()) && current.getLongitude()
                    .equals(prev.getLongitude())) || GeoUtils.DistanceOfTwoPoints(longitude,
                    latitude, prev.getLongitude(), prev.getLatitude(), GaussSphere.WGS84) == 0d /*|| current.getGpstime()
                                                                                                .equals(prev.getGpstime())*/)
                    && (prev.getSpeed() == 0)) {
                prev.setCover(true);
                prev.setCoverGpstime(current.getGpstime());
                prev.getLocrecordList().add(current);
                if (current.getLocateType().equals("0")
                        && current.getStatlliteNum() > prev.getStatlliteNum()) {
                    prev.setStatlliteNum(current.getStatlliteNum());
                }
                continue;
            } else if (getInterval(prev, current) > interval) {
                prev = new PointBean();
                prev.setLongitude(longitude);
                prev.setLatitude(latitude);
                prev.setGpstime(current.getGpstime());
                prev.setDistance(current.getDistance());
                prev.setSpeed(current.getSpeed() == null ? 0 : current.getSpeed());
                prev.setStatlliteNum(current.getStatlliteNum());
                prev.setLocateType(current.getLocateType());
                prev.setCover(false);
                List<TLocrecord> prevLocList = new ArrayList<TLocrecord>();
                prevLocList.add(current);
                prev.setLocrecordList(prevLocList);
                childList = new ArrayList<PointBean>();
                childList.add(prev);
                parentList.add(childList);
            } else {
                prev = new PointBean();
                prev.setLongitude(longitude);
                prev.setLatitude(latitude);
                prev.setGpstime(current.getGpstime());
                prev.setDistance(current.getDistance());
                prev.setSpeed(current.getSpeed() == null ? 0 : current.getSpeed());
                prev.setStatlliteNum(current.getStatlliteNum());
                prev.setLocateType(current.getLocateType());
                prev.setCover(false);
                List<TLocrecord> prevLocList = new ArrayList<TLocrecord>();
                prevLocList.add(current);
                prev.setLocrecordList(prevLocList);
                childList.add(prev);
            }
        }
        return parentList;
    }

    /**
     * ������������ʻ�ٶ�,��λ:����/Сʱ
     * 
     * @param front
     * @param back
     * @return
     */
    private double getSpeed(PointBean front, PointBean back) {
        int interval = this.getInterval(front, back);// ʱ���,��λ:��
        //gpsTime is identical or coverGpstime is null
        if (interval == 0) {
            return 0;
        }
        double distance = this.getDistance(front, back);// ����,��λ:��
        return distance / interval * 3.6;
    }

    /**
     * ����������ʱ���,��λ:��
     * 
     * @param front
     * @param back
     * @return
     */
    private int getInterval(PointBean front, PointBean back) {
        Date frontTime = front.isCover() ? front.getCoverGpstime() : front.getGpstime();
        Date backTime = back.getGpstime();
        return Math.abs(DateUtility.betweenSecond(backTime, frontTime));
    }

    /**
     * ����������ʱ���,��λ:��
     * 
     * @param front
     * @param back
     * @return
     */
    private int getInterval(PointBean front, TLocrecord back) {
        Date frontTime = front.isCover() ? front.getCoverGpstime() : front.getGpstime();
        Date backTime = back.getGpstime();

        return Math.abs(DateUtility.betweenSecond(backTime, frontTime));
    }

    /**
     * ���������ľ���,��λ:��
     * 
     * @param front
     * @param back
     * @return
     */
    private double getDistance(PointBean front, PointBean back) {
        return GeoUtils.DistanceOfTwoPoints(front.getLongitude(), front.getLatitude(),
                back.getLongitude(), back.getLatitude(), GaussSphere.WGS84);
    }

    /**
     * ����Ư�Ƶ�����Ч��λ���ݵİٷֱ�
     * 
     * @param drift
     * @param normal
     * @return
     */
    private double getRate(List<PointBean> drift, List<PointBean> normal) {
        int driftSize = 0, normalSize = 0;
        for (PointBean point : drift) {
            driftSize += point.getLocrecordList().size();
        }
        for (PointBean point : normal) {
            normalSize += point.getLocrecordList().size();
        }
        return (double) driftSize / (double) normalSize;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

}
