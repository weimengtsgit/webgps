package com.sosgps.wzt.stat.dao.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.sosgps.wzt.locate.dao.hibernate.LocateDAOImpl;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TVisitTjHome;
import com.sosgps.wzt.stat.dao.VisitStatDao;
import com.sosgps.wzt.util.PageQuery;

/**
 * @Title:拜访统计dao接口hibernate实现类
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 下午08:21:31
 */
public class VisitStatHibernateDao extends TVisitTjHome implements VisitStatDao {
    private static final Logger logger = Logger.getLogger(VisitStatHibernateDao.class);

	public Page<Object[]> listCustomVisitCountTjByCustom(String entCode,
			Long userId, int pageNo, int pageSize, Date startDate,
			Date endDate, Long poiId, String deviceIds, int duration) {
		String hql = "select tj,t from TVisitTj tj,TTerminal t "
				+ "where tj.deviceId=t.deviceId and tj.arriveTime>=? and tj.arriveTime<=? "
				+ "and tj.leaveTime-tj.arriveTime>(" + duration + "/60/24) "
				+ "and tj.deviceId in "
				+ "(select distinct ref.TTerminal.deviceId "
				+ "from RefTermGroup ref " + "left join ref.TTermGroup g "
				+ "left join g.refUserTgroups ref2 "
				+ "where g.TEnt.entCode=? and ref2.id.userId=?) "
				+ "and tj.poiId=? " + "and t.deviceId in(" + deviceIds + ") "
				+ "order by tj.arriveTime,tj.poiName";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, startDate, endDate, entCode,
				userId, poiId);
	}

	public Page<Object[]> listVisitCountTjByDeviceId(String entCode,
			Long userId, int pageNo, int pageSize, Date startDate,
			Date endDate, String deviceId, int duration) {
		String hql = "select tj,t from TVisitTj tj,TTerminal t "
				+ "where tj.deviceId=t.deviceId and tj.arriveTime>=? and tj.arriveTime<=? "
				+ "and tj.leaveTime-tj.arriveTime>(" + duration + "/60/24) "
				+ "and tj.deviceId in "
				+ "(select distinct ref.TTerminal.deviceId "
				+ "from RefTermGroup ref " + "left join ref.TTermGroup g "
				+ "left join g.refUserTgroups ref2 "
				+ "where g.TEnt.entCode=? and ref2.id.userId=?) "
				+ "and tj.deviceId=? " + "order by tj.arriveTime,tj.poiName";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, startDate, endDate, entCode,
				userId, deviceId);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Object[]> listAttendanceReport(String deviceIds,
			String entCode, Long userId, int startint, int limitint,
			String startDate, String endDate, String searchValue) {

		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
		try {
			String sql = "select count(t.vehicle_number) as num from T_TERMINAL t "
					+ " left join "
					+ " (select tl.DEVICE_ID as DEVICE_ID, to_char(min(tl.GPSTIME), 'yyyy-mm-dd hh24:mi:ss') as MIN_GPSTIME, to_char(max(tl.GPSTIME), 'yyyy-mm-dd hh24:mi:ss') as MAX_GPSTIME, trunc(tl.GPSTIME) as GPSTIME "
					+ " from  T_LOCRECORD     tl "
					+ " where tl.GPSTIME between "
					+ " TO_DATE('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') and "
					+ " TO_DATE('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') and "
					+ " tl.DEVICE_ID in ("
					+ deviceIds
					+ " ) "
					+ " group by tl.DEVICE_ID, trunc(tl.GPSTIME) "
					+ " )tab_1 on tab_1.DEVICE_ID = t.DEVICE_ID "
					+ " left join REF_TERM_GROUP rtg on rtg.device_id = t.DEVICE_ID "
					+ " left join T_TERM_GROUP ttg on ttg.id = rtg.group_id "
					+ " where  t.DEVICE_ID in ("
					+ deviceIds
					+ " ) "
					+ " and t.vehicle_number like '%"
					+ searchValue
					+ "%' "
					+ " order by t.VEHICLE_NUMBER, tab_1.MIN_GPSTIME,ttg.group_name ";
			session = getHibernateTemplate().getSessionFactory().openSession();
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(limitint);
			Query query = session.createSQLQuery(sql);
			list = query.list();
			Iterator iterator = list.iterator();
			if (iterator.hasNext()) {
				BigDecimal object = (BigDecimal) iterator.next();
				int count = object.intValue();
				page.setTotalCount(count);
			} else {
				return null;
			}

			sql = " select * from (select a.*, rownum row_num from ( "
					+ " select t.week, t.start_time, t.end_time, t.vehicle_number,t.simcard,ttg.group_name,tab_1.*  from T_TERMINAL t "
					+ " left join "
					+ " (select tl.DEVICE_ID as DEVICE_ID, to_char(min(tl.GPSTIME), 'yyyy-mm-dd hh24:mi:ss') as MIN_GPSTIME, to_char(max(tl.GPSTIME), 'yyyy-mm-dd hh24:mi:ss') as MAX_GPSTIME, trunc(tl.GPSTIME) as GPSTIME "
					+ " from  T_LOCRECORD     tl "
					+ " where tl.GPSTIME between " + " TO_DATE('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') and "
					+ " TO_DATE('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') and "
					+ " tl.DEVICE_ID in ("
					+ deviceIds
					+ " ) "
					+ " group by tl.DEVICE_ID, trunc(tl.GPSTIME) "
					+ " )tab_1 on tab_1.DEVICE_ID = t.DEVICE_ID "
					+ " left join REF_TERM_GROUP rtg on rtg.device_id = t.DEVICE_ID "
					+ " left join T_TERM_GROUP ttg on ttg.id = rtg.group_id "
					+ " where  t.DEVICE_ID in ("
					+ deviceIds
					+ " ) "
					+ " and t.vehicle_number like '%"
					+ searchValue
					+ "%' "
					+ " order by t.VEHICLE_NUMBER, tab_1.MIN_GPSTIME,ttg.group_name ) a) b "
					+ " where b.row_num between "
					+ (startint + 1)
					+ " and "
					+ (startint + limitint) + "";

			query = session.createSQLQuery(sql);
			list = query.list();
			page.setResult(list);
			return page;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}

	public Page<Object[]> listAttendanceReportDetail(String deviceId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue) {
		String hql = "select l,t.vehicleNumber,t.simcard "
				+ "from TTerminal t left join t.TLocrecords l "
				+ "where l.gpstime>=? and l.gpstime<=? and t.vehicleNumber like ? "
				+ " and l.deviceId in (" + deviceId + ") order by l.gpstime ";
		// + "where l.gpstime>=? and l.gpstime<=? " + "and l.deviceId= ? "
		// + "order by l.gpstime asc";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		// return pageQuery.findByPage(page, hql, startDate, endDate,deviceId);
		return pageQuery.findByPage(page, hql, startDate, endDate, "%"
				+ searchValue + "%");
	}

	// 业务员出访统计报表
	public Page<Object[]> listVisitCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration) {
		String hql = "select t.deviceId,t.vehicleNumber,count(tj.id) from TVisitTj tj,TTerminal t "
				+ "where tj.deviceId=t.deviceId and tj.arriveTime>=? and tj.arriveTime<=? "
				+ "and tj.leaveTime-tj.arriveTime>("
				+ duration
				+ "/60/24) "
				// + "and tj.deviceId in "
				// + "(select distinct ref.TTerminal.deviceId "
				// + "from RefTermGroup ref "
				// + "left join ref.TTermGroup g "
				// + "left join g.refUserTgroups ref2 "
				// + "where g.TEnt.entCode=? and ref2.id.userId=? "
				// + "and ref.TTerminal.vehicleNumber like ?) "
				+ "and t.vehicleNumber like ? "
				+ "and tj.deviceId in ("
				+ deviceIds
				+ ") "
				+ "group by t.deviceId,t.vehicleNumber "
				+ "order by count(tj.id), t.vehicleNumber";

		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, startDate, endDate, "%"
				+ searchValue + "%");
		// return pageQuery.findByPage(page, hql, startDate, endDate, startDate,
		// endDate, startDate, endDate,
		// entCode, userId, "%" + searchValue + "%", deviceIds);

	}

	// 客户被拜访统计 add by renxianliang
	/**
	 * modify by wangzhen
	 * modify time :2012-09-05 14:02:00
	 * modify cause :sql error 导致如东营代理商反映sdwdf企业下，
	 * 有个喜民乐标注点已删除，但在之后的客户被拜访统计中依旧会显示。
	 */
	public Page<Object[]> listCustomVisitCountLing(int pageNo, int pageSize,
			String startDate, String endDate, String searchValue, String deviceIds,
			int duration) {
			String sql = "select ta.id, ta.poi_name,tb.b"
					+ " from (select tp.id, tp.poi_name"
					+ " from t_poi tp where tp.id in (select rtp.poi_id"
					+ " from ref_term_poi rtp where rtp.device_id in ("+ deviceIds+ "))" 
					+ " and (tp.states = 0 or tp.states is null)) ta" 
					+ " left join (select tttt.poi_id, tttt.poi_name, count(tttt.id) as b"
					+ " from t_visit_tj tttt"
					+ " where tttt.arrive_time >= TO_DATE ('"+ startDate + "', 'yyyy-MM-dd hh24:mi:ss') "
					+ " and tttt.arrive_time <= TO_DATE ('"+ endDate + "', 'yyyy-MM-dd hh24:mi:ss') "
					+ " and tttt.leave_time - tttt.arrive_time >= (" + duration + "/60/24) "
					+ " and tttt.device_id in (" + deviceIds + ") "
					+ " group by tttt.poi_id, tttt.poi_name" + " ) tb"
					+ " on ta.id = tb.poi_id"
					+ " where ta.poi_name like '%"+searchValue+"%'" + "order by tb.b desc nulls last";
			Page<Object[]> page = new Page<Object[]>(pageSize, true);
			page.setPageNo(pageNo);
			return new PageQuery<Object[]>(this).findByPageWithSql(page,
					sql.toString());
	}
 
	public Page<Object[]> listCustomVisitCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration) {
		String hql = "select tj.poiId,tj.poiName,count(tj.id) from TVisitTj tj,TTerminal t "
				+ "where tj.deviceId=t.deviceId and tj.arriveTime>=? and tj.arriveTime<=? "
				+ "and tj.leaveTime-tj.arriveTime>("
				+ duration
				+ "/60/24) "
				+ "and tj.deviceId in "
				+ "(select distinct ref.TTerminal.deviceId "
				+ "from RefTermGroup ref "
				+ "left join ref.TTermGroup g "
				+ "left join g.refUserTgroups ref2 "
				+ "where g.TEnt.entCode=? and ref2.id.userId=?) "
				+ "and tj.deviceId in ("
				+ deviceIds
				+ ") "
				+ "and tj.poiName like ? "
				+ "group by tj.poiId,tj.poiName "
				+ "order by count(tj.id) desc,tj.poiName";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, startDate, endDate, entCode,
				userId, "%" + searchValue + "%");
	}

	public Page<Object[]> listCustomVisitTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration) {
		String hql = "select tj,t from TVisitTj tj,TTerminal t "
				+ "where tj.deviceId=t.deviceId and tj.arriveTime>=? and tj.arriveTime<=? "
				+ "and tj.leaveTime-tj.arriveTime>(" + duration + "/60/24) "
				+ "and tj.deviceId in "
				+ "(select distinct ref.TTerminal.deviceId "
				+ "from RefTermGroup ref "
				+ "left join ref.TTermGroup g "
				+ "left join g.refUserTgroups ref2 "
				+ "where g.TEnt.entCode=? and ref2.id.userId=? ) "
				// + "and ref.TTerminal.vehicleNumber like ?) "
				+ "and tj.deviceId in (" + deviceIds + ") "
				+ "and tj.poiName like ? "
				+ "order by tj.poiName,tj.arriveTime,tj.deviceId";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, startDate, endDate, entCode,
				userId, "%" + searchValue + "%");
	}

	// 拜访客户数
	public Page<Object[]> listVisitCustomerCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration) {
		String hql = "select t.deviceId, count(tj.id) from TVisitCustomersTj tj,TTerminal t "
				+ "where tj.deviceId=t.deviceId and tj.arriveTime>=? and tj.arriveTime<=? "
				+ "and tj.leaveTime-tj.arriveTime>("
				+ duration
				+ "/60/24) "
				+ "and t.vehicleNumber like ? "
				+ "and tj.deviceId in ("
				+ deviceIds
				+ ") "
				+ "group by t.deviceId "
				+ "order by count(tj.id), t.vehicleNumber";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, startDate, endDate, "%"
				+ searchValue + "%");

	}

	// 拜访地点数
	public Page<Object[]> listVisitPlaceCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration) {
		String hql = "select t.deviceId, count(tj.id) from TVisitPlaceTj tj,TTerminal t "
				+ "where tj.deviceId=t.deviceId and tj.arriveTime>=? and tj.arriveTime<=? "
				+ "and tj.leaveTime-tj.arriveTime>("
				+ duration
				+ "/60/24) "
				+ "and t.vehicleNumber like ? "
				+ "and tj.deviceId in ("
				+ deviceIds
				+ ") "
				+ "group by t.deviceId "
				+ "order by count(tj.id), t.vehicleNumber";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, startDate, endDate, "%"
				+ searchValue + "%");

	}

	// 拜访终端名
	public Page<Object[]> listVisitTerminal(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String deviceIds) {
		String hql = "select t.deviceId,t.vehicleNumber from TTerminal t "
				+ " where " + " t.deviceId in "
				+ " (select distinct ref.TTerminal.deviceId "
				+ " from RefTermGroup ref " + "left join ref.TTermGroup g "
				+ " left join g.refUserTgroups ref2 "
				+ " where g.TEnt.entCode=? and ref2.id.userId=? ) "
				+ " and t.vehicleNumber like ? " + " and t.deviceId in ("
				+ deviceIds + ") ";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, entCode, userId, "%"
				+ searchValue + "%");

	}

	// 拜访终端名
	public Page<Object[]> listVisitCountTj(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String deviceIds) {
		String hql = "select t.deviceId,t.vehicleNumber) from TTerminal t "
				+ "where t.vehicleNumber like ? " + "and t.deviceId in ("
				+ deviceIds + ") ";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, "%" + searchValue + "%");

	}

	// 拜访次数图表
	public Page<Object[]> visitCountChart(int pageNo, int pageSize,
			Date startDate, Date endDate, String deviceId, int duration) {
		String hql = "select count(tj.arriveTime),trunc(tj.arriveTime) "
				+ "from TVisitTj tj " + "where (tj.deviceId = " + deviceId
				+ ") " + "and (tj.arriveTime >= ?) "
				+ "and (tj.arriveTime <= ?) "
				+ "and (tj.leaveTime - tj.arriveTime > (" + duration
				+ " / 60 / 24)) " + "group by trunc(tj.arriveTime) "
				+ "order by trunc(tj.arriveTime) ";

		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, startDate, endDate);
	}

	// 拜访地点数地图显示数据
	@SuppressWarnings("unchecked")
	public Page<Object[]> visitStatPlaceMap(int pageNo, int pageSize,
			String startDate, String endDate, String deviceId, int duration) {
		/*
		 * String hql = "select tj.longitude,tj.latitude " +
		 * "from TVisitPlaceTj tj " + "where (tj.deviceId = ? ) " +
		 * "and (tj.arriveTime >= ?) " + "and (tj.arriveTime <= ?) " +
		 * "and (tj.leaveTime - tj.arriveTime > ("+duration+" / 60 / 24)) ";
		 * 
		 * PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		 * Page<Object[]> page = new Page<Object[]>(); page.setAutoCount(true);
		 * page.setPageNo(pageNo); page.setPageSize(pageSize); return
		 * pageQuery.findByPage(page, hql, deviceId, startDate, endDate);
		 */
		Session session = null;
		List<Object[]> list = null;
		try {
			String sql = "select tj.longitude,tj.latitude "
					+ " from T_Visit_Place_Tj tj " + " where tj.device_Id = '"
					+ deviceId + "'  " + " and arrive_time >= TO_DATE ('"
					+ startDate + "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND arrive_time <= TO_DATE ('" + endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ "  and (tj.leave_Time - tj.arrive_Time > (" + duration
					+ " / 60 / 24))";

			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql);
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			list = query.list();
			page.setTotalCount(list.size());
			page.setResult(list);
			return page;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}

	// 业务员出访统计报表(sql)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Object[]> listVisitCountTjSql(String entCode, Long userId,
			int startint, int limitint, String startDate, String endDate,
			String searchValue, String deviceIds, int duration) {
		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
		try {
			String sql = "SELECT count(tab_1.device_id) as num  "
					+ " FROM (SELECT   t.device_id,t.vehicle_number, COUNT (v_tj.device_id) v_device "
					+ " FROM (SELECT * " + " FROM t_visit_tj "
					+ " WHERE arrive_time >= TO_DATE ('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND arrive_time <= TO_DATE ('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24)) v_tj, "
					+ " t_terminal t "
					+ " WHERE v_tj.device_id(+) = t.device_id "
					+ " GROUP BY t.device_id,t.vehicle_number) tab_1, "
					+ " (SELECT   t.device_id, COUNT (vc_tj.device_id) vc_device "
					+ " FROM (SELECT * "
					+ " FROM t_visit_customers_tj "
					+ " WHERE arrive_time >= TO_DATE ('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND arrive_time <= TO_DATE ('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24)) vc_tj, "
					+ " t_terminal t "
					+ " WHERE vc_tj.device_id(+) = t.device_id "
					+ " GROUP BY t.device_id) tab_2, "
					+ " (SELECT   t.device_id, COUNT (vp_tj.device_id) vp_device "
					+ " FROM (SELECT * "
					+ " FROM t_visit_place_tj "
					+ " WHERE arrive_time >= TO_DATE ('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND arrive_time <= TO_DATE ('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24)) vp_tj, "
					+ " t_terminal t "
					+ " WHERE vp_tj.device_id(+) = t.device_id "
					+ " GROUP BY t.device_id) tab_3 "
					+ " WHERE tab_1.device_id = tab_2.device_id "
					+ " and tab_1.device_id = tab_3.device_id "
					+ " and tab_1.device_id in ("
					+ deviceIds
					+ ") "
					+ " and tab_1.vehicle_number like '%"
					+ searchValue
					+ "%' "
					+ " order by tab_1.v_device desc, tab_2.vc_device desc, tab_3.vp_device desc, tab_1.vehicle_number desc ";

			session = getHibernateTemplate().getSessionFactory().openSession();
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(limitint);
			Query query = session.createSQLQuery(sql);
			list = query.list();
			Iterator iterator = list.iterator();
			if (iterator.hasNext()) {
				BigDecimal object = (BigDecimal) iterator.next();
				int count = object.intValue();
				page.setTotalCount(count);
			} else {
				return null;
			}

			sql = " select * from (select a.*, rownum row_num from ( "
					+ " SELECT tab_1.*, tab_2.vc_device, tab_3.vp_device "
					+ " FROM (SELECT   t.device_id,t.vehicle_number, COUNT (v_tj.device_id) v_device "
					+ " FROM (SELECT * " + " FROM t_visit_tj "
					+ " WHERE arrive_time >= TO_DATE ('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND arrive_time <= TO_DATE ('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24)) v_tj, "
					+ " t_terminal t "
					+ " WHERE v_tj.device_id(+) = t.device_id "
					+ " GROUP BY t.device_id,t.vehicle_number) tab_1, "
					+ " (SELECT   t.device_id, COUNT (vc_tj.device_id) vc_device "
					+ " FROM (SELECT * "
					+ " FROM t_visit_customers_tj "
					+ " WHERE arrive_time >= TO_DATE ('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND arrive_time <= TO_DATE ('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24)) vc_tj, "
					+ " t_terminal t "
					+ " WHERE vc_tj.device_id(+) = t.device_id "
					+ " GROUP BY t.device_id) tab_2, "
					+ " (SELECT   t.device_id, COUNT (vp_tj.device_id) vp_device "
					+ " FROM (SELECT * "
					+ " FROM t_visit_place_tj "
					+ " WHERE arrive_time >= TO_DATE ('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND arrive_time <= TO_DATE ('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24)) vp_tj, "
					+ " t_terminal t "
					+ " WHERE vp_tj.device_id(+) = t.device_id "
					+ " GROUP BY t.device_id) tab_3 "
					+ " WHERE tab_1.device_id = tab_2.device_id "
					+ " and tab_1.device_id = tab_3.device_id "
					+ " and tab_1.device_id in ("
					+ deviceIds
					+ ") "
					+ " and tab_1.vehicle_number like '%"
					+ searchValue
					+ "%' "
					+ " order by tab_1.v_device desc, tab_2.vc_device desc, tab_3.vp_device desc, tab_1.vehicle_number desc ) a) b "
					+ " where b.row_num between "
					+ (startint + 1)
					+ " and "
					+ (startint + limitint) + "";

			query = session.createSQLQuery(sql);
			list = query.list();
			page.setResult(list);
			return page;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}

	// 业务员拜访次数所有人员统计图表(sql)
	@SuppressWarnings("unchecked")
	public Page<Object[]> visitCountChartAll(int pageNo, int pageSize,
			String startDate, String endDate, String searchValue,
			String deviceIds, int duration) {
		Session session = null;
		List<Object[]> list = null;
		try {
			String sql = " SELECT   t.device_id,t.vehicle_number, COUNT (v_tj.device_id) v_device "
					+ " FROM (SELECT *  "
					+ " FROM t_visit_tj  "
					+ " WHERE arrive_time >= TO_DATE ('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND arrive_time <= TO_DATE ('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24)) v_tj,  "
					+ " t_terminal t  "
					+ " WHERE v_tj.device_id(+) = t.device_id  "
					+ " and t.device_id in ("
					+ deviceIds
					+ ") "
					+ " and t.vehicle_number like '%"
					+ searchValue
					+ "%' "
					+ " GROUP BY t.device_id,t.vehicle_number "
					+ " order by v_device desc ";
			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql);
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			list = query.list();
			page.setTotalCount(list.size());
			page.setResult(list);
			return page;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}

	// 业务员单个拜访次数图表(sql)
	@SuppressWarnings("unchecked")
	public Page<Object[]> visitCountChartSql(int pageNo, int pageSize,
			String startDate, String endDate, String utcStartDate,
			String utcEndDate, String deviceId, int duration) {
		Session session = null;
		List<Object[]> list = null;
		try {
			String sql = "select nvl(target_base.vj_count, 0) vj_count, base.arrive_time from  "
					+ " (select to_char(mapsearch_utc_to_date((trunc("
					+ utcStartDate
					+ " / (24 * 60 * 60 * 1000)) + rownum ) * (24 * 60 * 60 * 1000)), 'yyyy-mm-dd') as arrive_time  "
					+ " from dual connect by rownum <= (trunc(("
					+ utcEndDate
					+ " - "
					+ utcStartDate
					+ ") / (24 * 60 * 60 * 1000)) + 1)) base, "
					+ " (SELECT count(arrive_time) vj_count, to_char(trunc(arrive_time), 'yyyy-mm-dd') as arrive_time "
					+ " FROM t_visit_tj   "
					+ " WHERE device_id = '"
					+ deviceId
					+ "' "
					+ " AND arrive_time >= TO_DATE ('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')   "
					+ " AND arrive_time <= TO_DATE ('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')   "
					+ " AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24)   "
					+ " GROUP BY trunc(arrive_time) ) target_base "
					+ " where base.arrive_time = target_base.arrive_time(+) "
					+ " order by base.arrive_time ";
			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql);
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			list = query.list();
			page.setTotalCount(list.size());
			page.setResult(list);
			return page;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}

	// 业务员单个拜访客户数图表(sql)
	@SuppressWarnings("unchecked")
	public Page<Object[]> visitCusCountChartSql(int pageNo, int pageSize,
			String startDate, String endDate, String utcStartDate,
			String utcEndDate, String deviceId, int duration) {
		Session session = null;
		List<Object[]> list = null;
		try {
			String sql = "select nvl(target_base.vj_count, 0) vj_count, base.arrive_time from  "
					+ " (select to_char(mapsearch_utc_to_date((trunc("
					+ utcStartDate
					+ " / (24 * 60 * 60 * 1000)) + rownum ) * (24 * 60 * 60 * 1000)), 'yyyy-mm-dd') as arrive_time  "
					+ " from dual connect by rownum <= (trunc(("
					+ utcEndDate
					+ " - "
					+ utcStartDate
					+ ") / (24 * 60 * 60 * 1000)) + 1)) base, "
					+ " (SELECT count(arrive_time) vj_count, to_char(trunc(arrive_time), 'yyyy-mm-dd') as arrive_time "
					+ " FROM t_visit_customers_tj   "
					+ " WHERE device_id = '"
					+ deviceId
					+ "' "
					+ " AND arrive_time >= TO_DATE ('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')   "
					+ " AND arrive_time <= TO_DATE ('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')   "
					+ " AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24)   "
					+ " GROUP BY trunc(arrive_time) ) target_base "
					+ " where base.arrive_time = target_base.arrive_time(+) "
					+ " order by base.arrive_time ";
			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql);
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			list = query.list();
			page.setTotalCount(list.size());
			page.setResult(list);
			return page;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}

	// 业务员单个拜访地点数图表(sql)
	@SuppressWarnings("unchecked")
	public Page<Object[]> visitPlaceCountChartSql(int pageNo, int pageSize,
			String startDate, String endDate, String utcStartDate,
			String utcEndDate, String deviceId, int duration) {
		Session session = null;
		List<Object[]> list = null;
		try {
			String sql = "select nvl(target_base.vj_count, 0) vj_count, base.arrive_time from  "
					+ " (select to_char(mapsearch_utc_to_date((trunc("
					+ utcStartDate
					+ " / (24 * 60 * 60 * 1000)) + rownum ) * (24 * 60 * 60 * 1000)), 'yyyy-mm-dd') as arrive_time  "
					+ " from dual connect by rownum <= (trunc(("
					+ utcEndDate
					+ " - "
					+ utcStartDate
					+ ") / (24 * 60 * 60 * 1000)) + 1)) base, "
					+ " (SELECT count(arrive_time) vj_count, to_char(trunc(arrive_time), 'yyyy-mm-dd') as arrive_time "
					+ " FROM t_visit_place_tj   "
					+ " WHERE device_id = '"
					+ deviceId
					+ "' "
					+ " AND arrive_time >= TO_DATE ('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')   "
					+ " AND arrive_time <= TO_DATE ('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')   "
					+ " AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24)   "
					+ " GROUP BY trunc(arrive_time) ) target_base "
					+ " where base.arrive_time = target_base.arrive_time(+) "
					+ " order by base.arrive_time ";
			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql);
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			list = query.list();
			page.setTotalCount(list.size());
			page.setResult(list);
			return page;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}

	// 业务员拜访客户数所有人员统计图表(sql)
	@SuppressWarnings("unchecked")
	public Page<Object[]> visitCusCountChartAll(int pageNo, int pageSize,
			String startDate, String endDate, String searchValue,
			String deviceIds, int duration) {
		Session session = null;
		List<Object[]> list = null;
		try {
			String sql = " SELECT   t.device_id,t.vehicle_number, COUNT (v_tj.device_id) v_device "
					+ " FROM (SELECT *  "
					+ " FROM t_visit_customers_tj  "
					+ " WHERE arrive_time >= TO_DATE ('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND arrive_time <= TO_DATE ('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24)) v_tj,  "
					+ " t_terminal t  "
					+ " WHERE v_tj.device_id(+) = t.device_id  "
					+ " and t.device_id in ("
					+ deviceIds
					+ ") "
					+ " and t.vehicle_number like '%"
					+ searchValue
					+ "%' "
					+ " GROUP BY t.device_id,t.vehicle_number "
					+ " order by v_device desc ";
			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql);
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			list = query.list();
			page.setTotalCount(list.size());
			page.setResult(list);
			return page;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}

	// 业务员拜访地点数所有人员统计图表(sql)
	@SuppressWarnings("unchecked")
	public Page<Object[]> visitPlaceCountChartAll(int pageNo, int pageSize,
			String startDate, String endDate, String searchValue,
			String deviceIds, int duration) {
		Session session = null;
		List<Object[]> list = null;
		try {
			String sql = " SELECT   t.device_id,t.vehicle_number, COUNT (v_tj.device_id) v_device "
					+ " FROM (SELECT *  "
					+ " FROM t_visit_place_tj  "
					+ " WHERE arrive_time >= TO_DATE ('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND arrive_time <= TO_DATE ('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24)) v_tj,  "
					+ " t_terminal t  "
					+ " WHERE v_tj.device_id(+) = t.device_id  "
					+ " and t.device_id in ("
					+ deviceIds
					+ ") "
					+ " and t.vehicle_number like '%"
					+ searchValue
					+ "%' "
					+ " GROUP BY t.device_id,t.vehicle_number "
					+ " order by v_device desc ";
			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql);
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			list = query.list();
			page.setTotalCount(list.size());
			page.setResult(list);
			return page;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}

	public Page<Object[]> listVehicleGPS(String deviceIds, int pageNo,
			int pageSize, String startDate, String endDate) {
		String hql = " select l,t.vehicleNumber,t.simcard "
				+ " from TTerminal t left join t.TLocrecords l "
				+ " where l.gpstime>= TO_DATE('" + startDate
				+ "', 'yyyy-mm-dd hh24:mi:ss') " + " and l.gpstime<=TO_DATE('"
				+ endDate + "', 'yyyy-mm-dd hh24:mi:ss')  "
				+ " and l.deviceId in (" + deviceIds + ") order by l.gpstime ";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql);
	}

	// 业务员出访统计表(改)2011-8-1 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Object[]> listVisitCountTjSql2(String entCode, Long userId,
			int startint, int limitint, String startDate, String endDate,
			String searchValue, String deviceIds, int duration) {
		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
		try {
			String sql = " SELECT count(tab_2.vc_device) FROM ( "
					+ " SELECT t.device_id, t.vehicle_number, COUNT(v_tj.device_id) v_device FROM ( "
					+ " SELECT * FROM t_visit_tj WHERE arrive_time >= TO_DATE('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') AND arrive_time <= TO_DATE('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24) and device_id in ("
					+ deviceIds
					+ ") "
					+ " ) v_tj, t_terminal t "
					+ " WHERE v_tj.device_id(+) = t.device_id "
					+ " and t.device_id in ("
					+ deviceIds
					+ ") "
					+ " GROUP BY t.device_id, t.vehicle_number "
					+ " ) tab_1, "
					+ " ( "
					+ " select nvl(tab1.c_dev,0) as vc_device, t.device_id from "
					+ " (select tab.device_id, count(tab.device_id) as c_dev from "
					+ " (SELECT v_tj.poi_id, v_tj.device_id FROM t_visit_tj v_tj "
					+ " WHERE v_tj.arrive_time >= TO_DATE('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND v_tj.arrive_time <= TO_DATE('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND (v_tj.leave_time - v_tj.arrive_time) > ("
					+ duration
					+ " / 60 / 24) "
					+ " and v_tj.device_id in ("
					+ deviceIds
					+ ") "
					+ " group by v_tj.poi_id, v_tj.device_id "
					+ " )tab "
					+ " group by tab.device_id "
					+ " )tab1, t_terminal t "
					+ " where tab1.device_id(+) = t.device_id "
					+ " and t.device_id in ("
					+ deviceIds
					+ ") "
					+ " )tab_2 "
					+ " where tab_1.device_id = tab_2.device_id and tab_1.device_id in "
					+ " ("
					+ deviceIds
					+ ") "
					+ " and tab_1.vehicle_number like '%%' order by tab_1.v_device desc, tab_2.vc_device desc, tab_1.vehicle_number desc ";
			System.out.println(sql);
			session = getHibernateTemplate().getSessionFactory().openSession();
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(limitint);
			Query query = session.createSQLQuery(sql);
			list = query.list();
			Iterator iterator = list.iterator();
			if (iterator.hasNext()) {
				BigDecimal object = (BigDecimal) iterator.next();
				int count = object.intValue();
				page.setTotalCount(count);
			} else {
				return null;
			}

			sql = " select * from (select a.*, rownum row_num from ( "
					+ " SELECT tab_1.*, tab_2.vc_device FROM ( "
					+ " SELECT t.device_id, t.vehicle_number, COUNT(v_tj.device_id) v_device FROM ( "
					+ " SELECT * FROM t_visit_tj WHERE arrive_time >= TO_DATE('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') AND arrive_time <= TO_DATE('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24) and device_id in ("
					+ deviceIds
					+ ") "
					+ " ) v_tj, t_terminal t "
					+ " WHERE v_tj.device_id(+) = t.device_id "
					+ " and t.device_id in ("
					+ deviceIds
					+ ") "
					+ " GROUP BY t.device_id, t.vehicle_number "
					+ " ) tab_1, "
					+ " ( "
					+ " select nvl(tab1.c_dev,0) as vc_device, t.device_id from "
					+ " (select tab.device_id, count(tab.device_id) as c_dev from "
					+ " (SELECT v_tj.poi_id, v_tj.device_id FROM t_visit_tj v_tj "
					+ " WHERE v_tj.arrive_time >= TO_DATE('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND v_tj.arrive_time <= TO_DATE('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND (v_tj.leave_time - v_tj.arrive_time) > ("
					+ duration
					+ " / 60 / 24) "
					+ " and v_tj.device_id in ("
					+ deviceIds
					+ ") "
					+ " group by v_tj.poi_id, v_tj.device_id "
					+ " )tab "
					+ " group by tab.device_id "
					+ " )tab1, t_terminal t "
					+ " where tab1.device_id(+) = t.device_id "
					+ " and t.device_id in ("
					+ deviceIds
					+ ") "
					+ " )tab_2 "
					+ " where tab_1.device_id = tab_2.device_id and tab_1.device_id in "
					+ " ("
					+ deviceIds
					+ ") "
					+ " and tab_1.vehicle_number like '%%' order by tab_1.v_device desc, tab_2.vc_device desc, tab_1.vehicle_number desc ) a) b "
					+ " where b.row_num between "
					+ (startint + 1)
					+ " and "
					+ (startint + limitint) + "";
			System.out.println(sql);
			query = session.createSQLQuery(sql);
			list = query.list();
			page.setResult(list);
			return page;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Object[]> listVisitCountTjByDeviceId2(String entCode,
			Long userId, int startint, int limitint, String startDate,
			String endDate, String deviceId, int duration) {
		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
		try {
			String sql = "select count(tp.poi_name) from ( "
					+ " SELECT v_tj.poi_id FROM t_visit_tj v_tj "
					+ " WHERE v_tj.arrive_time >= TO_DATE('" + startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND v_tj.arrive_time <= TO_DATE('" + endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND (v_tj.leave_time - v_tj.arrive_time) > ("
					+ duration + " / 60 / 24) " + " and v_tj.device_id = '"
					+ deviceId + "' " + " group by v_tj.poi_id "
					+ " )tab_1, t_poi tp " 
					+ " where tp.id = tab_1.poi_id "
					//v2.1 weimeng 2012-8-31
					+ " and (tp.states = 0 or tp.states is null) ";
			session = getHibernateTemplate().getSessionFactory().openSession();
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(limitint);
			Query query = session.createSQLQuery(sql);
			list = query.list();
			Iterator iterator = list.iterator();
			if (iterator.hasNext()) {
				BigDecimal object = (BigDecimal) iterator.next();
				int count = object.intValue();
				page.setTotalCount(count);
			} else {
				return null;
			}

			sql = " select * from (select a.*, rownum row_num from ( "
					+ " select tp.poi_name, tp.loc_desc, tp.poi_datas, tab_1.poi_id_c from ( "
					+ " SELECT v_tj.poi_id, count(v_tj.poi_id) as poi_id_c FROM t_visit_tj v_tj "
					+ " WHERE v_tj.arrive_time >= TO_DATE('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND v_tj.arrive_time <= TO_DATE('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND (v_tj.leave_time - v_tj.arrive_time) > ("
					+ duration
					+ " / 60 / 24) "
					+ " and v_tj.device_id = '"
					+ deviceId
					+ "' "
					+ " group by v_tj.poi_id "
					+ " )tab_1, t_poi tp "
					+ " where tp.id = tab_1.poi_id "
					//v2.1 weimeng 2012-8-31
					+ " and (tp.states = 0 or tp.states is null) "
					+ " order by tp.poi_name ) a) b "
					+ " where b.row_num between " + (startint + 1) + " and "
					+ (startint + limitint) + "";

			query = session.createSQLQuery(sql);
			list = query.list();
			page.setResult(list);
			return page;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}

	public Page<Object[]> visitStatPlaceMap2(int pageNo, int pageSize,
			String startDate, String endDate, String deviceId, int duration) {
		Session session = null;
		List<Object[]> list = null;
		try {
			String sql = " select count(tp.poi_datas) from ( "
					+ " SELECT v_tj.poi_id FROM t_visit_tj v_tj "
					+ " WHERE v_tj.arrive_time >= TO_DATE('" + startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND v_tj.arrive_time <= TO_DATE('" + endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND (v_tj.leave_time - v_tj.arrive_time) > ("
					+ duration + " / 60 / 24) " + " and v_tj.device_id = '"
					+ deviceId + "' " + " group by v_tj.poi_id "
					+ " )tab_1, t_poi tp "
					+ " where tp.id = tab_1.poi_id " 
					//v2.1 weimeng 2012-8-31
					+ " and (tp.states = 0 or tp.states is null) "
					+ " order by tp.poi_name";
			session = getHibernateTemplate().getSessionFactory().openSession();
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			Query query = session.createSQLQuery(sql);
			list = query.list();
			Iterator iterator = list.iterator();
			if (iterator.hasNext()) {
				BigDecimal object = (BigDecimal) iterator.next();
				int count = object.intValue();
				page.setTotalCount(count);
			} else {
				return null;
			}

			sql = " select tp.poi_datas from ( "
					+ " SELECT v_tj.poi_id FROM t_visit_tj v_tj "
					+ " WHERE v_tj.arrive_time >= TO_DATE('" + startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND v_tj.arrive_time <= TO_DATE('" + endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " AND (v_tj.leave_time - v_tj.arrive_time) > ("
					+ duration + " / 60 / 24) " + " and v_tj.device_id = '"
					+ deviceId + "' " + " group by v_tj.poi_id "
					+ " )tab_1, t_poi tp "
					+ " where tp.id = tab_1.poi_id "
					//v2.1 weimeng 2012-8-31
					+ " and (tp.states = 0 or tp.states is null) " 
					+ " order by tp.poi_name";

			query = session.createSQLQuery(sql);
			list = query.list();
			page.setResult(list);
			return page;

		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}

	// 定位信息关键字查询 add by zhaofeng
	public Page<Object[]> listVehicleGPS(String searchValue, String deviceIds,
			int pageNo, int pageSize, String startDate, String endDate) {
		String hql = " select l,t.vehicleNumber,t.simcard "
				+ " from TTerminal t left join t.TLocrecords l "
				+ " where l.gpstime>= TO_DATE('" + startDate
				+ "', 'yyyy-mm-dd hh24:mi:ss') " + " and l.gpstime<=TO_DATE('"
				+ endDate + "', 'yyyy-mm-dd hh24:mi:ss')  "
				+
				// " and t.vehicleNumber like '%"+searchValue+"%' "+
				"and (t.vehicleNumber like '%" + searchValue
				+ "%'   or  t.simcard like '%" + searchValue + "%' ) " +
				// " or t.simcard like '%"+searchValue+"%' "+
				" and l.deviceId in (" + deviceIds + ") order by l.gpstime ";
		
		logger.info("deviceIds = "+deviceIds+"; startDate = "+startDate+"; endDate = "+endDate+"; hql = "+hql);
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql);
	}

	// 业务员单个拜访次数图表(sql)改
	@SuppressWarnings("unchecked")
	public Page<Object[]> visitCountChartSql2(int pageNo, int pageSize,
			String startDate, String endDate, String utcStartDate,
			String utcEndDate, String deviceId, int duration) {
		Session session = null;
		List<Object[]> list = null;
		try {
			String sql = "select nvl(target_base.vj_count, 0) vj_count, base.arrive_time from  "
					+ " (select to_char(mapsearch_utc_to_date((trunc("
					+ utcStartDate
					+ " / (24 * 60 * 60 * 1000)) + rownum ) * (24 * 60 * 60 * 1000)), 'yyyy-mm-dd') as arrive_time  "
					+ " from dual connect by rownum <= (trunc(("
					+ utcEndDate
					+ " - "
					+ utcStartDate
					+ ") / (24 * 60 * 60 * 1000)) + 1)) base, "
					+ " (SELECT count(arrive_time) vj_count, to_char(trunc(arrive_time), 'yyyy-mm-dd') as arrive_time "
					+ " FROM t_visit_tj   "
					+ " WHERE device_id = '"
					+ deviceId
					+ "' "
					+ " AND arrive_time >= TO_DATE ('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')   "
					+ " AND arrive_time <= TO_DATE ('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss')   "
					+ " AND (leave_time - arrive_time) > ("
					+ duration
					+ " / 60 / 24)   "
					+ " GROUP BY trunc(arrive_time) ) target_base "
					+ " where base.arrive_time = target_base.arrive_time(+) "
					+ " order by base.arrive_time ";
			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql);
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			list = query.list();
			page.setTotalCount(list.size());
			page.setResult(list);
			return page;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}

	// 业务员拜访客户数所有人员统计图表(sql)改
	@SuppressWarnings("unchecked")
	public Page<Object[]> visitCusCountChartAll2(int pageNo, int pageSize,
			String startDate, String endDate, String searchValue,
			String deviceIds, int duration) {
		Session session = null;
		List<Object[]> list = null;
		try {
			String sql = "SELECT t.device_id, t.term_name, nvl(tab1.c_dev, 0) as vc_device "
					+ " from (select tab.device_id, "
					+ " count(tab.device_id) as c_dev "
					+ " from (SELECT v_tj.poi_id, v_tj.device_id "
					+ " FROM t_visit_tj v_tj "
					+ " WHERE v_tj.arrive_time >= TO_DATE('"
					+ startDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND v_tj.arrive_time <= TO_DATE('"
					+ endDate
					+ "', 'yyyy-mm-dd hh24:mi:ss') "
					+ " AND (v_tj.leave_time - "
					+ " v_tj.arrive_time) >  ("
					+ duration
					+ " / 60 / 24) "
					+ " and v_tj.device_id in ("
					+ deviceIds
					+ ") "
					+ " group by v_tj.poi_id, v_tj.device_id) tab "
					+ " group by tab.device_id) tab1, "
					+ " t_terminal t "
					+ " where tab1.device_id(+) = t.device_id "
					+ " and t.device_id in "
					+ " ("
					+ deviceIds
					+ ") order by  vc_device desc ";

			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql);
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			list = query.list();
			page.setTotalCount(list.size());
			page.setResult(list);
			return page;
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (session != null) {
				session.clear();
				session.close();
				getHibernateTemplate().getSessionFactory().close();
			}
		}
	}
}
