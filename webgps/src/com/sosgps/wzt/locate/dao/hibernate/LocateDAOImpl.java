package com.sosgps.wzt.locate.dao.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sosgps.wzt.locate.dao.LocateDAO;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TLastLocrecord;
import com.sosgps.wzt.orm.TLastLocrecordDAO;
import com.sosgps.wzt.orm.TLocrecord;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.poi.action.LayerAndPoiAction;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.util.PageQuery;

public class LocateDAOImpl extends TLastLocrecordDAO implements LocateDAO {
    private static final Logger logger = Logger.getLogger(LocateDAOImpl.class);

	/**
	 * 最后一条位置
	 */
	public List findLocByDeviceId(String deviceId) {
		List<TLastLocrecord> loclist = null;
		if (deviceId == null || deviceId.equals("")) {
			return null;
		}
		StringBuffer sb = new StringBuffer(
				"from TLastLocrecord as model where ");
		sb.append(" model.deviceId in(");
		sb.append(deviceId);
		sb.append(")");
		sb.append(" and model.latitude>0 and model.longitude>0    ");
		loclist = this.getHibernateTemplate().find(sb.toString());

		if (loclist.size() > 0)
			return loclist;

		return null;
	}

	public TLocrecord queryLocByTime(String deviceId, Date time) {
		if (deviceId == null || deviceId.equals("")) {
			return null;
		}
		PageQuery<TLocrecord> p = new PageQuery<TLocrecord>(this);
		Page<TLocrecord> page = new Page<TLocrecord>();
		page.setAutoCount(false);
		page.setPageNo(1);
		page.setPageSize(1);
		String hql = "from TLocrecord as model where model.longitude>0 and model.latitude>0 and model.deviceId=? and model.gpstime between ? and ? order by model.gpstime desc";
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		c.add(Calendar.DAY_OF_MONTH, -1);
		Date btime = c.getTime();
		logger.info("deviceId = "+deviceId+"; time = "+time+"; btime = "+btime+"; hql = "+hql);
		Page<TLocrecord> re = p.findByPage(page, hql, deviceId, btime, time);
		if (re != null && re.getResult() != null && re.getResult().size() >= 1) {
			return re.getResult().get(0);
		}
		return null;
	}

	public List queryLocsByTime(String deviceId, Date startTime, Date endTime) {
		String hql = "from TLocrecord t "
				+ "where t.longitude>0 and t.latitude>0 "
				+ "and t.deviceId = ? " + "and t.gpstime between ? and ? "
				+ "and (t.locateType=1 or t.statlliteNum>3) "
				+ "order by t.gpstime";
		PageQuery<TLocrecord> p = new PageQuery<TLocrecord>(this);
		Page<TLocrecord> page = new Page<TLocrecord>();
		page.setAutoCount(false);
		page.setPageNo(1);
//		page.setPageSize(Integer.MAX_VALUE);
		page.setPageSize(1000);
        logger.info("deviceId = "+deviceId+"; hql = "+hql);
		Page<TLocrecord> re = p.findByPage(page, hql, deviceId, startTime,
				endTime);
		if (re != null && re.getResult() != null && re.getResult().size() >= 1) {
			return re.getResult();
		}
		return null;
	}

	public List queryLocsByTime(TTerminal terminal, String startDate,
			String endDate, String startTime, String endTime) {
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(terminal.getDeviceId());

		// 封装工作时间段内的查询条件 add by liuhongxiao 2012-04-18
		StringBuffer sb = new StringBuffer();
		Date tmpStartDate, tmpEndDate, tmpDate;
		tmpStartDate = tmpDate = DateUtility.strToDate(startDate);
		tmpEndDate = DateUtility.strToDate(endDate);
		while (tmpDate.getTime() >= tmpStartDate.getTime()
				&& tmpDate.getTime() <= tmpEndDate.getTime()) {
			if (tmpDate.getTime() == tmpStartDate.getTime()) {
				if (tmpDate.getTime() == tmpEndDate.getTime()) {
					sb.append("and (t.gpstime between ? and ?) ");
					paramList.add(DateUtility.strToDateTime(startDate + " "
							+ startTime));
					paramList.add(DateUtility.strToDateTime(endDate + " "
							+ endTime));
				} else {
					sb.append("and (t.gpstime between ? and ? ");
					paramList.add(DateUtility.strToDateTime(startDate + " "
							+ startTime));
					paramList.add(DateUtility.strToDateTime(startDate + " "
							+ terminal.getEndTime() + ":00"));
				}
			} else if (tmpDate.getTime() < tmpEndDate.getTime()) {
				long tmp = (1 << (tmpDate.getDay() == 0 ? 6
						: (tmpDate.getDay() - 1)));
				if ((terminal.getWeek() & tmp) == tmp) {
					sb.append("or t.gpstime between ? and ? ");
					paramList.add(DateUtility.strToDateTime(DateUtility
							.dateToStr(tmpDate)
							+ " " + terminal.getStartTime() + ":00"));
					paramList.add(DateUtility.strToDateTime(DateUtility
							.dateToStr(tmpDate)
							+ " " + terminal.getEndTime() + ":00"));
				}
			} else {
				sb.append("or t.gpstime between ? and ?) ");
				paramList.add(DateUtility.strToDateTime(DateUtility
						.dateToStr(tmpDate)
						+ " " + terminal.getStartTime() + ":00"));
				paramList.add(DateUtility
						.strToDateTime(endDate + " " + endTime));
			}
			tmpDate = DateUtility.add(tmpDate, 1);
		}

		String hql = "from TLocrecord t " + "where t.deviceId = ? "
				+ sb.toString() + "order by t.gpstime";

        logger.info("deviceId = "+terminal.getDeviceId()+"; startDate = "+startDate+"; endDate = "+endDate+"; hql = "+hql);
		PageQuery<TLocrecord> p = new PageQuery<TLocrecord>(this);
		Page<TLocrecord> page = new Page<TLocrecord>();
		page.setAutoCount(false);
		page.setPageNo(1);
		page.setPageSize(Integer.MAX_VALUE);
//        page.setPageSize(1000);
		Page<TLocrecord> re = p.findByPage(page, hql, paramList.toArray());
		if (re != null && re.getResult() != null && re.getResult().size() >= 1) {
			return re.getResult();
		}
		return null;
	}

	public List queryNoEncryptionAndNoLocDesc() {
		List<TLocrecord> loclist = null;

		StringBuffer sb = new StringBuffer("from TLocrecord as model where ");
		sb.append(" model.locDesc is null ");
		sb.append(" and ");
		sb.append(" model.latitude >0 ");
		sb.append(" and ");
		sb.append(" model.longitude >0 ");
		sb.append(" and ");
		sb.append(" (model.jmx is null or model.jmy is null)");
        logger.info("hql = "+sb.toString());
		loclist = this.getHibernateTemplate().find(sb.toString());

		if (loclist.size() > 0)
			return loclist;

		return null;
	}

	// 轨迹回放车辆信息分页
	public Page<TLocrecord> listQueryLocsByTime(String deviceId,
			Date startTime, Date endTime, int pageNo, int pageSize) {
		String hql = "from TLocrecord t "
				+ "where t.longitude>0 and t.latitude>0 "
				+ "and t.deviceId = ? " + "and t.gpstime between ? and ? "
				+ "and (t.locateType=1 or t.statlliteNum>3) "
				+ "order by t.gpstime";

        logger.info("deviceId = "+deviceId+"; startTime = "+startTime+"; endTime = "+endTime+"; hql = "+hql);
		PageQuery<TLocrecord> p = new PageQuery<TLocrecord>(this);
		Page<TLocrecord> page = new Page<TLocrecord>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return p.findByPage(page, hql, deviceId, startTime, endTime);
	}


    public List queryLocsByTime2(TTerminal terminal, String startDate,
            String endDate, String startTime, String endTime) {
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(terminal.getDeviceId());

        // 封装工作时间段内的查询条件 add by liuhongxiao 2012-04-18
        StringBuffer sb = new StringBuffer();
        StringBuffer sqlsb = new StringBuffer();
        Date tmpStartDate, tmpEndDate, tmpDate;
        tmpStartDate = tmpDate = DateUtility.strToDate(startDate);
        tmpEndDate = DateUtility.strToDate(endDate);
        while (tmpDate.getTime() >= tmpStartDate.getTime()
                && tmpDate.getTime() <= tmpEndDate.getTime()) {
            if (tmpDate.getTime() == tmpStartDate.getTime()) {
                if (tmpDate.getTime() == tmpEndDate.getTime()) {
                    sb.append("and (t.gpstime between ? and ?) ");
                    sqlsb.append("and (t.gpstime between "+DateUtility.strToDateTime(startDate + " "
                            + startTime)+" and "+DateUtility.strToDateTime(endDate + " "
                                    + endTime)+") ");
                    paramList.add(DateUtility.strToDateTime(startDate + " "
                            + startTime));
                    paramList.add(DateUtility.strToDateTime(endDate + " "
                            + endTime));
                } else {
                    sb.append("and (t.gpstime between ? and ? ");
                    sqlsb.append("and (t.gpstime between "+DateUtility.strToDateTime(startDate + " "
                            + startTime)+" and "+DateUtility.strToDateTime(startDate + " "
                                    + terminal.getEndTime() + ":00")+") ");
                    paramList.add(DateUtility.strToDateTime(startDate + " "
                            + startTime));
                    paramList.add(DateUtility.strToDateTime(startDate + " "
                            + terminal.getEndTime() + ":00"));
                }
            } else if (tmpDate.getTime() < tmpEndDate.getTime()) {
                long tmp = (1 << (tmpDate.getDay() == 0 ? 6
                        : (tmpDate.getDay() - 1)));
                if ((terminal.getWeek() & tmp) == tmp) {
                    sb.append("or t.gpstime between ? and ? ");
                    sqlsb.append("and (t.gpstime between "+DateUtility.strToDateTime(DateUtility
                            .dateToStr(tmpDate)
                            + " " + terminal.getStartTime() + ":00")+" and "+DateUtility.strToDateTime(DateUtility
                                    .dateToStr(tmpDate)
                                    + " " + terminal.getEndTime() + ":00")+") ");
                    paramList.add(DateUtility.strToDateTime(DateUtility
                            .dateToStr(tmpDate)
                            + " " + terminal.getStartTime() + ":00"));
                    paramList.add(DateUtility.strToDateTime(DateUtility
                            .dateToStr(tmpDate)
                            + " " + terminal.getEndTime() + ":00"));
                }
            } else {
                sb.append("or t.gpstime between ? and ?) ");
                sqlsb.append("or t.gpstime between "+DateUtility.strToDateTime(DateUtility
                        .dateToStr(tmpDate)
                        + " " + terminal.getStartTime() + ":00")+" and "+DateUtility
                        .strToDateTime(endDate + " " + endTime)+") ");
                paramList.add(DateUtility.strToDateTime(DateUtility
                        .dateToStr(tmpDate)
                        + " " + terminal.getStartTime() + ":00"));
                paramList.add(DateUtility
                        .strToDateTime(endDate + " " + endTime));
            }
            tmpDate = DateUtility.add(tmpDate, 1);
        }

        String sql = "select t.latitude, t.longitude, to_char(t.gpstime,'yyyy-mm-dd hh24:mi:ss'), t.distance, t.statllite_num, t.locate_type " +
        		" from T_Locrecord t " + " where t.device_Id = ? "
                + sb.toString() + " order by t.gpstime ";

        logger.info("sql = "+"select t.latitude, t.longitude, t.gpstime, t.distance, t.statllite_num, t.locate_type " +
                " from T_Locrecord t " + " where t.device_Id = "+terminal.getDeviceId()+" "
                + sqlsb.toString() + " order by t.gpstime ");
        
        PageQuery<Object[]> p = new PageQuery<Object[]>(this);
        Page<Object[]> page = new Page<Object[]>();
        page.setAutoCount(false);
        page.setPageNo(1);
      page.setPageSize(Integer.MAX_VALUE);
//        page.setPageSize(1000);
        Page<Object[]> re = p.findByPageWithSql(page, sql, paramList.toArray());
        if (re != null && re.getResult() != null && re.getResult().size() >= 1) {
            return re.getResult();
        }
        return null;
    }
    
    public Page<Object[]> lastLocrecordList(String entCode, String deviceIds, String name, String locateType, int gpstime, int inputtime, int startint, int limitint) {

        int pageNo= startint / limitint + 1;
        int pageSize=limitint;
        StringBuilder sql = new StringBuilder();
        List<Object> paramList = new ArrayList<Object>();
        sql.append(" select term_name, locate_type, group_name, simcard, to_char(gpstime, 'yyyy-mm-dd hh24:mi:ss') as gpstime, dis_gpstime, to_char(last_time, 'yyyy-mm-dd hh24:mi:ss') as last_time, dis_last_time from ");
        sql.append(" ( ");
        sql.append(" select tt.term_name, t.locate_type, tg.group_name, tt.simcard,  ");
        sql.append(" t.gpstime, (mapsearch_date_to_utc(sysdate) - mapsearch_date_to_utc(t.gpstime))/1000/60/60 as dis_gpstime,  ");
        sql.append(" t.last_time, (mapsearch_date_to_utc(sysdate) - mapsearch_date_to_utc(t.last_time))/1000/60/60 as dis_last_time ");
        sql.append(" from t_last_locrecord t, t_terminal tt, ref_term_group rtg, t_term_group tg ");
        sql.append(" where t.device_id = tt.device_id ");
        sql.append(" and tt.device_id = rtg.device_id ");
        sql.append(" and t.device_id in (" +deviceIds + ") ");
        sql.append(" and rtg.group_id = tg.id ");
        sql.append(" and tg.ent_code = ? ");
        sql.append(" and tt.ent_code = ? ");
        paramList.add(entCode);
        paramList.add(entCode);
        if(name != null && name.length() > 0){
            sql.append(" and tt.term_name like ? ");
            paramList.add("%" + name + "%");
        }
        if(locateType != null && locateType.length() > 0){
            sql.append(" and t.locate_type = ? ");
            paramList.add(locateType);
        }
        sql.append(" ) base ");
        sql.append(" where  ");
        sql.append(" dis_gpstime >= ? ");
        paramList.add(gpstime);
        sql.append(" and dis_last_time >= ? ");
        sql.append(" order by dis_gpstime desc, dis_last_time desc, term_name ");
        paramList.add(inputtime);
                
        PageQuery<Object[]> p = new PageQuery<Object[]>(this);
        Page<Object[]> page = new Page<Object[]>(pageSize, true);
        page.setPageNo(pageNo);
        Page<Object[]> re = p.findByPageWithSql(page, sql.toString(), paramList.toArray());
        if (re != null && re.getResult() != null && re.getResult().size() >= 1) {
            return re;
        }
        return null;
    }

}
