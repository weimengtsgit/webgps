package com.sosgps.wzt.stat.dao.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDistanceDayDAO;
import com.sosgps.wzt.stat.dao.DistanceStatDao;
import com.sosgps.wzt.util.PageQuery;

/**
 * @Title:里程统计dao接口hibernate实现类
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 下午12:59:18
 */
public class DistanceStatHibernateDao extends TDistanceDayDAO implements
		DistanceStatDao {

	public Page<Object[]> listDistanceStatByCustom(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds) {
		String hql = "select d.deviceId,d.distance,t.vehicleNumber,d.tjdate "
				+ "from TDistanceDay d,TTerminal t "
				+ "where d.tjdate>=? and d.tjdate<=? "
				+ "and d.deviceId=t.deviceId " + "and d.deviceId in "
				+ "(select distinct ref.TTerminal.deviceId "
				+ "from RefTermGroup ref " + "left join ref.TTermGroup g "
				+ "left join g.refUserTgroups ref2 "
				+ "where g.TEnt.entCode=? and ref2.id.userId=? "
				+ "and ref.TTerminal.vehicleNumber like ?) "
				+ "and d.deviceId in (" + deviceIds + ") "
//				+ "group by d.deviceId,t.vehicleNumber "
				+ "order by t.vehicleNumber,d.tjdate";
		PageQuery<Object[]> pq = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pq.findByPage(page, hql, startDate, endDate, entCode, userId,
				"%" + searchValue + "%");
	}

	public Page<Object[]> listDistanceStatByMonth(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds) {
		String hql = "select d.deviceId,d.distance,t.vehicleNumber,d.tjdate "
				+ "from TDistanceMonth d,TTerminal t "
				+ "where d.tjdate>=? and d.tjdate<=? "
				+ "and d.deviceId=t.deviceId " + "and d.deviceId in "
				+ "(select distinct ref.TTerminal.deviceId "
				+ "from RefTermGroup ref " + "left join ref.TTermGroup g "
				+ "left join g.refUserTgroups ref2 "
				+ "where g.TEnt.entCode=? and ref2.id.userId=? "
				+ "and ref.TTerminal.vehicleNumber like ?) "
				+ "and d.deviceId in (" + deviceIds + ") "
//				+ "group by d.deviceId,t.vehicleNumber "
				+ "order by t.vehicleNumber,d.tjdate";
		PageQuery<Object[]> pq = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pq.findByPage(page, hql, startDate, endDate, entCode, userId,
				"%" + searchValue + "%");
	}

	public Page<Object[]> listDistanceStatByYear(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds) {
		String hql = "select d.deviceId,d.distance,t.vehicleNumber,d.tjdate "
				+ "from TDistanceYear d,TTerminal t "
				+ "where d.tjdate>=? and d.tjdate<=? "
				+ "and d.deviceId=t.deviceId " + "and d.deviceId in "
				+ "(select distinct ref.TTerminal.deviceId "
				+ "from RefTermGroup ref " + "left join ref.TTermGroup g "
				+ "left join g.refUserTgroups ref2 "
				+ "where g.TEnt.entCode=? and ref2.id.userId=? "
				+ "and ref.TTerminal.vehicleNumber like ?) "
				+ "and d.deviceId in (" + deviceIds + ") "
//				+ "group by d.deviceId,t.vehicleNumber "
				+ "order by t.vehicleNumber,d.tjdate";
		PageQuery<Object[]> pq = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pq.findByPage(page, hql, startDate, endDate, entCode, userId,
				"%" + searchValue + "%");
	}
	
	public Page<Object[]> listTotalDistanceStat(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String deviceIds) {
		String hql = "select d.deviceId,d.distance,t.vehicleNumber "
				+ "from TTotalDistance d,TTerminal t "
				+ "where  d.deviceId=t.deviceId " 
				+" and d.deviceId in "
				+ "(select distinct ref.TTerminal.deviceId "
				+ "from RefTermGroup ref " 
				+ "left join ref.TTermGroup g "
				+ "left join g.refUserTgroups ref2 "
				+ "where g.TEnt.entCode=? and ref2.id.userId=? "
				+ "and ref.TTerminal.vehicleNumber like ?) "
				+ "and d.deviceId in (" + deviceIds + ") "
				+ "order by d.distance desc ";
		PageQuery<Object[]> pq = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pq.findByPage(page, hql, entCode, userId,
				"%" + searchValue + "%");
	}
	
	//按时间段查询里程
	public Page<Object[]> listTimeDistanceStatByCustom(int startint, int limitint, String startDate, 
			String endDate, String deviceIds, String searchValue) {

		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
        try {
        	String sql = " select count(*) from (select a.*, rownum row_num from ( "
        		+ " select tt.vehicle_number, tt.device_id, a.max_gpstime, a.min_gpstime, a.sub_distance "
        		+ " from t_terminal tt "
        		+ " left join "
        		+ " ( "
        		+ " select max(t.gpstime) as max_gpstime, min(t.gpstime) as min_gpstime, "
        		+ " max(t.distance) as max_distance, min(t.distance) as min_distance, "
        		+ " t.device_id, (max(t.distance) - min(t.distance)) as sub_distance from t_locrecord t "
        		+ " where t.gpstime >= to_date('"+startDate+"', 'yyyy-mm-dd hh24:mi:ss') "
        		+ " and t.gpstime <= to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss') "
        		+ " and t.device_id in ("+deviceIds+") "
        		+ " group by t.device_id "
        		+ " ) a on tt.device_id = a.device_id "
        		+ " where tt.device_id in ("+deviceIds+") "
        		+ " and tt.vehicle_number like '%"+searchValue+"%' "
        		+ " ) a) b ";
        	session = getHibernateTemplate().getSessionFactory().openSession();
            Page<Object[]> page = new Page<Object[]>();
            page.setPageNo(pageNo);
    		page.setPageSize(limitint);
            Query query = session.createSQLQuery(sql);
            list = query.list();
            Iterator iterator = list.iterator();
            if(iterator.hasNext()){
            	BigDecimal object = (BigDecimal)iterator.next();
                int count = object.intValue();
                page.setTotalCount(count);
            }else{
            	return null;
            }
            
        	sql = " select * from (select a.*, rownum row_num from ( "
        		+ " select tt.vehicle_number, tt.device_id, a.max_gpstime, a.min_gpstime, a.sub_distance "
        		+ " from t_terminal tt "
        		+ " left join "
        		+ " ( "
        		+ " select to_char(max(t.gpstime),'yyyy-mm-dd hh24:mi:ss') as max_gpstime, to_char(min(t.gpstime),'yyyy-mm-dd hh24:mi:ss') as min_gpstime, "
        		+ " max(t.distance) as max_distance, min(t.distance) as min_distance, "
        		+ " t.device_id, (max(t.distance) - min(t.distance)) as sub_distance from t_locrecord t "
        		+ " where t.gpstime >= to_date('"+startDate+"', 'yyyy-mm-dd hh24:mi:ss') "
        		+ " and t.gpstime <= to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss') "
        		+ " and t.device_id in ("+deviceIds+") "
        		+ " group by t.device_id "
        		+ " ) a on tt.device_id = a.device_id "
        		+ " where tt.device_id in ("+deviceIds+") "
        		+ " and tt.vehicle_number like '%"+searchValue+"%' "
        		+ ") a) b "
            	+ " where b.row_num between "+(startint+1)+" and "+(startint+limitint)+"";

            query = session.createSQLQuery(sql);
            list = query.list();
            page.setResult(list);
            return page;
        } catch (RuntimeException e) {
            throw e;
        } finally {
            if(session != null) {
                session.clear();
                session.close();
                getHibernateTemplate().getSessionFactory().close();
            }
        }
        
        
	}

	public Page<Object[]> listAttendanceRecord(int startint, int limitint,
			String startDate, String endDate, String deviceIds) {
		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
        try {
        	String sql = "select count(*) " +
        			"from T_Attendance ta,T_Terminal tt,t_term_group ttg,ref_term_group rtg " +
        			" where tt.device_id = ta.device_id" +
        			" and rtg.device_id = tt.device_id" +
        			" and rtg.group_id = ttg.id" +
        			" and ta.attendance_date >= "+startDate+
        			" and ta.attendance_date <= "+endDate+
        			" and ta.device_id in ("+deviceIds+") and ta.delete_flag =0" +
        			" order by ta.id";
        	session = getHibernateTemplate().getSessionFactory().openSession();
            Page<Object[]> page = new Page<Object[]>();
            page.setPageNo(pageNo);
    		page.setPageSize(limitint);
            Query query = session.createSQLQuery(sql);
            list = query.list();
            Iterator iterator = list.iterator();
            if(iterator.hasNext()){
            	BigDecimal object = (BigDecimal)iterator.next();
                int count = object.intValue();
                page.setTotalCount(count);
            }else{
            	return null;
            }
            
        	sql = "select tt.term_name, ta.attendance_date," +
        			"to_char(ta.signin_time, 'YYYY-MM-DD hh24:mi:ss') as signin_time," +
        			" ta.signin_desc," +
        			"to_char(ta.signoff_time, 'YYYY-MM-DD hh24:mi:ss') as signoff_time," +
        			" ta.signoff_desc," +
        			" ta.device_id," +
        			" to_char(ta.create_time ,'YYYY-MM-DD hh24:mi:ss') as create_time," +
        			" ttg.group_name" +
        			" from T_Attendance ta,T_Terminal tt,t_term_group ttg,ref_term_group rtg  " +
			" where tt.device_id = ta.device_id" +
			" and rtg.device_id = tt.device_id" +
			" and rtg.group_id = ttg.id" +
			" and ta.attendance_date >= "+startDate+
			" and ta.attendance_date <= "+endDate+
			" and ta.device_id in ("+deviceIds+") and ta.delete_flag =0" +
			" order by ta.id";
            query = session.createSQLQuery(sql);
            list = query.list();
            page.setResult(list);
            return page;
        } catch (RuntimeException e) {
            throw e;
        } finally {
            if(session != null) {
                session.clear();
                session.close();
                getHibernateTemplate().getSessionFactory().close();
            }
        }
	}
}
