package com.sosgps.wzt.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sosgps.wzt.locate.dao.hibernate.LocateDAOImpl;
import com.sosgps.wzt.orm.TLocrecord;
import com.sosgps.wzt.util.GeoUtils.GaussSphere;

/**
 * @title Description:漂移点过滤工具类
 * @copyright Copyright (c) 2001
 * @company www.sosgps.net.cn
 * @author liuhongxiao
 * @version 1.0
 */

public final class DriftFilter {

    private static final Logger logger = Logger.getLogger(DriftFilter.class);
    
    private final int RATE_VALVE = 100;

    private int interval = 30 * 60;// 两点之间时间大于interval值得点作为正常点保留,单位:秒(可以解决4.在火车、飞机上行驶过程中速度过快上报的定位数据被当做漂移点过滤的问题)

    private double speed = 200d; // 两点之间(里程数/时间)大于speed值得点作为漂移点去除,单位:公里/小时

    private double rate = 1d;// 计算漂移点与有效定位数据的百分比，如果该比例大于rate值，说明本次过滤失败，删除定位数据中的第一条数据，重新计算

    public DriftFilter() {
    }

    public DriftFilter(int interval, double speed, double rate) {
        this.interval = interval;
        this.speed = speed;
        this.rate = rate;
    }

    /**
     * 判断unknown点是否为漂移点
     * 
     * @param effect 有效点
     * @param unknown 待检测点
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
     * 漂移点过滤
     * 
     * @param list 定位数据
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
     * 去除无效数据,将经纬度为0.0的数据过滤掉
     * 
     * @param list 合并后的定位数据
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
     * 漂移点过滤,递归调用,选择列表中的第一个点作为基准点,如果记录数小于等于RATE_VALVE值,遍历所有点计算漂移点概率;
     * 如果大于RATE_VALVE值,遍历RATE_VALVE条记录计算漂移点概率,
     * 如果该比例大于rate值,说明本次过滤失败,删除所有记录中的第一条数据,重新计算
     * 
     * modify by liuhongxiao 2012-06-15
     * 增加一个漂移点过滤的基准点,如果某一个点距前后相邻的两个点的速度都大于speed,该点定义为漂移点
     * 
     * @param list 有效的定位数据
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
                // 首点作为基准点
                if (driftCounter > 0 && normal.size() == 1) {
                    /** 增加一个漂移点过滤的基准点,如果漂移点/正常点小于rate,首点漂移 * */
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
                    // 如果漂移点/正常点小于rate,首点漂移,本次过滤失败
                    if (this.getRate(drift, normal) <= rate) {
                        list.remove(0);
                        return removeDrift(list);
                    }
                }
                // 其他
                else if (driftCounter > 0) {
                    /** 增加一个漂移点过滤的基准点,如果某一个点距前后相邻的两个点的速度都大于speed,该点定义为漂移点 * */
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
                    // 本次过滤失败
                    if (size >= RATE_VALVE && this.getRate(drift, normal) > rate) {
                        list.remove(0);
                        return removeDrift(list);
                    }
                }

                prev = current;
                normal.add(current);
            }
        }

        // 失败
        if (this.getRate(drift, normal) > rate) {
            list.remove(0);
            // 递归调用
            return removeDrift(list);
        }
        // 成功
        else return normal;
    }

    /**
     * 将经纬度相同或者距离为0.0的连续点合并为同一个点(去重，同时可以解决2.多个经纬度相同且连续的漂移点不能过滤 的问题)
     * 
     * @param list
     * @return 去重后的数据,且按时间间隔大于interval值分组
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
            // 经纬度相同||距离为0.0的连续点合并 add 203-7-16 gpsTime相同的合并
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
     * 计算两点间的行驶速度,单位:公里/小时
     * 
     * @param front
     * @param back
     * @return
     */
    private double getSpeed(PointBean front, PointBean back) {
        int interval = this.getInterval(front, back);// 时间差,单位:秒
        //gpsTime is identical or coverGpstime is null
        if (interval == 0) {
            return 0;
        }
        double distance = this.getDistance(front, back);// 距离,单位:米
        return distance / interval * 3.6;
    }

    /**
     * 计算两点间的时间差,单位:秒
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
     * 计算两点间的时间差,单位:秒
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
     * 计算两点间的距离,单位:米
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
     * 计算漂移点与有效定位数据的百分比
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
