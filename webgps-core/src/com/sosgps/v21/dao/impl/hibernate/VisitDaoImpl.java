package com.sosgps.v21.dao.impl.hibernate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import com.sosgps.v21.dao.VisitDao;
import com.sosgps.v21.model.Visit;
import com.sosgps.v21.util.CharTools;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.util.PageQuery;
 
public class VisitDaoImpl extends BaseHibernateDao implements VisitDao {

	private static final Logger logger = LoggerFactory
			.getLogger(VisitDaoImpl.class);

	@SuppressWarnings("unchecked")
	public List<Visit> queryVisitsByCondition(
			final Map<String, Object> paramMap, final String entCode) {
		try {
			configInterceptor(entCode);
			return getHibernateTemplate().executeFind(new HibernateCallback() {

				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					StringBuilder sb = new StringBuilder();
					sb.append(" from Visit where states = 0");
					if (paramMap.get("startTime") != null) {
						sb.append(" and createOn > ?1");
					}
					if (paramMap.get("endTime") != null) {
						sb.append(" and createOn < ?2");
					}
					if (paramMap.get("poiName") != null) {
						sb.append(" and poiName like ?3");
					}
					if (paramMap.get("deviceIds") != null) {
						sb.append(" and deviceId in ("
								+ paramMap.get("deviceIds") + ")");
					}
					sb.append(" and entCode = ?4");
					if (paramMap.get("orderby") != null) {
						sb.append(" order by "+paramMap.get("orderby"));
					}
					
					Query query = session.createQuery(sb.toString());
					if (paramMap.get("startTime") != null) {
						query.setLong("1", (Long) paramMap.get("startTime"));
					}
					if (paramMap.get("endTime") != null) {
						query.setLong("2", (Long) paramMap.get("endTime"));
					}
					if (paramMap.get("poiName") != null) {
						query.setString("3", "%" + paramMap.get("poiName") + "%");
					}
					query.setString("4", entCode);
					if (paramMap.get("start") != null) {
						query.setFirstResult((Integer) paramMap.get("start"));
					}
					if (paramMap.get("limit") != null) {
						query.setMaxResults((Integer) paramMap.get("limit"));
					}
					return query.list();
				}
			});
		} catch (Exception e) {
			logger.error("failed", e);
			return null;
		}
	}

	/**
	 * 客户拜访大排名
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryVisitRank(final Map<String, Object> paramMap,
			final String entCode) {
		try {
			configInterceptor(entCode);
			return getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					StringBuilder sb = new StringBuilder();
					Long startDateL = (Long)paramMap.get("startTime");
					Long endDateL = (Long)paramMap.get("endTime");
					sb.append(" select max(groupId),max(termName),count(deviceId) as counts, deviceId from Visit where states = 0 ");
					if (startDateL != null) {
						sb.append(" and createOn >= :startDate ");
					}
					if (endDateL != null) {
						sb.append(" and createOn <= :endDate ");
					}
					if (paramMap.get("deviceIds") != null) {
						sb.append(" and deviceId in ( :deviceIds ) ");
					}
					sb.append(" and entCode = :entCode ");
					sb.append(" group by deviceId ");
					sb.append(" order by count(deviceId) desc ");
					Query query = session.createQuery(sb.toString());
					if (startDateL != null) {
						query.setParameter("startDate", startDateL);
					}
					if (endDateL != null) {
						query.setParameter("endDate", endDateL);
					}
					if (paramMap.get("deviceIds") != null) {
						query.setParameterList("deviceIds", (String[])paramMap.get("deviceIds"));
					}
					query.setParameter("entCode", entCode);
					/*if (paramMap.get("start") != null) {
						query.setFirstResult((Integer) paramMap.get("start"));
					}
					if (paramMap.get("limit") != null) {
						query.setMaxResults((Integer) paramMap.get("limit"));
					}*/
					return query.list();
				}
			});

		} catch (Exception e) {
			logger.error("[queryVisitRank] failed", e);
		}
		return null;
	}

	/**
	 * 首页仪表盘-员工出访达成率
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getVisitCount(Long startTime, Long endTime, String entCode) {
		try {
			configInterceptor(entCode);
			String hql = "select count(*) " +
					"from Visit t " +
					"where t.createOn > ? and t.createOn < ? " +
					"and t.entCode = ? " +
					"and t.states = 0 ";
			
			return super.getHibernateTemplate().find(hql,
					new Object[] { startTime, endTime, entCode });
		} catch (Exception e) {
			logger.error("[getVisitCount]", e);
			return null;
		}
	}

	/**
	 * 根据时间段查询值，每天的累计员工出访数
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getVisitsByTime(Long startTime, Long endTime,
			String entCode) {
		Session session = null;
		try {
			int index = CharTools.getStrHashCode(entCode);
			String sql = "select visit.visits, to_char(base.createon, 'yyyy-mm-dd') from "
					+ "(select trunc(mapsearch_utc_to_date(t.createon)) as createon,count(t.id) as visits "
					+ "from t_new_visit_"
					+ index
					+ " t "
					+ "where t.createon >= "
					+ startTime
					+ " "
					+ "and t.createon <= "
					+ endTime
					+ "  "
					+ "and t.ent_code = '"
					+ entCode
					+ "' "
					+ "and t.states = 0 "
					+ "group by trunc(mapsearch_utc_to_date(t.createon)) "
					+ ") visit, "
					+ "(select trunc(mapsearch_utc_to_date((trunc("
					+ startTime
					+ " / (24 * 60 * 60 * 1000)) + "
					+ "rownum) * (24 * 60 * 60 * 1000))) as createon "
					+ "from dual connect by rownum <= (trunc(("
					+ endTime
					+ " - "
					+ startTime
					+ ") / (24 * 60 * 60 * 1000)) + 1) "
					+ ") base "
					+ "where base.createon = visit.createon(+) "
					+ "order by base.createon ";

			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql);
			return query.list();
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

	
	@SuppressWarnings("unchecked")
	public List<Long> getCusVisitCount(Long startTime, Long endTime,
			String entCode) {
		try {
			configInterceptor(entCode);
			String hql = 
					" select count(poiId) as poiId from Visit "
					+ "where createOn >= ? " + "and createOn <= ? "
					+ "and entCode = ? " + "and states = 0 "
					+ "group by poiId ";
			return super.getHibernateTemplate().find(hql,
					new Object[] { startTime, endTime, entCode });
		} catch (Exception e) {
			logger.error("[getVisitCount]", e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getCusVisitsByTime(Long startTime, Long endTime,
			String entCode) {
		Session session = null;
		try {
			int index = CharTools.getStrHashCode(entCode);
			String sql = " select count(new_visit.createon) as visit, to_char(base.createon, 'yyyy-mm-dd') from "
					+ "(select trunc(mapsearch_utc_to_date(createon)) as createon, poi_id from t_new_visit_"
					+ index
					+ " "
					+ "where createon >= "
					+ startTime
					+ " "
					+ "and createon <= "
					+ endTime
					+ " "
					+ "and ent_code = '"
					+ entCode
					+ "' "
					+ "and states = 0 "
					+ "group by trunc(mapsearch_utc_to_date(createon)), poi_id) new_visit, "
					+ "(select trunc(mapsearch_utc_to_date((trunc("
					+ startTime
					+ " / (24 * 60 * 60 * 1000)) + "
					+ "rownum) * (24 * 60 * 60 * 1000))) as createon "
					+ "from dual connect by rownum <= (trunc(("
					+ endTime
					+ " - "
					+ startTime
					+ ") / (24 * 60 * 60 * 1000)) + 1) "
					+ ") base "
					+ "where new_visit.createon(+) = base.createon "
					+ "group by base.createon " + "order by base.createon ";

			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql);
			return query.list();
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


	/**
	 * 客户拜访详细记录
	 * @param entCode
	 * @param pageNo
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @param approved
	 * @param poiName
	 * @param deviceIds
	 * @return
	 */
	public Page<Visit> listVisitDetails(String entCode,
			int pageNo, int pageSize, Long startDate,
			Long endDate, String poiName, String deviceIds) {
		configInterceptor(entCode);
		String hql = "select t from Visit t "
				+ " where t.createOn > ? and t.createOn < ? "
				+ " and t.entCode = ? "
				+ " and (t.poiName like ? or t.poiName is null) "
				+ " and t.states = 0 ";
				if(!deviceIds.equals("'-1'")){
					hql += " and t.deviceId in ("+deviceIds+")";
				}
				hql += " order by t.createOn desc, t.groupName, t.deviceId, t.poiName";
		PageQuery<Visit> pageQuery = new PageQuery<Visit>(this);
		Page<Visit> page = new Page<Visit>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPageHash(entCode, page, hql, 
			startDate, endDate, entCode, "%"+poiName+"%");
	}
	
/*	*//**
	 * 客户被拜访统计同2.0
	 *//*
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Object[]> listCustomVisitCountTj(String entCode,
			int startint, int limitint, Long startDate, Long endDate,
			String searchValue, String deviceIds, Long duration) {
		int index = CharTools.getStrHashCode(entCode);
		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
		try {
			String sql = "select count(tj.poi_id) as num from t_new_visit_"+index+" tj "
				+ "where tj.createon >= "+startDate+" and tj.createon <= "+endDate+" "
				+ "and (tj.sign_out_time - tj.sign_in_time)/1000/60 > "+duration+" "
				+ " and tj.ent_code = '"+entCode+"' "
				+ "and tj.device_id in ("+deviceIds+") "
				+ "and tj.poi_name like '%"+searchValue+"%' "
				+ "group by tj.poi_id,tj.poi_name "
				+ "order by count(tj.id) desc,tj.poi_name";
			session = getHibernateTemplate().getSessionFactory().openSession();
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(limitint);
			Query query = session.createSQLQuery(sql);
			list = query.list();
			if(list.size()>0){
			    int count=list.size();
			    page.setTotalCount(count);
			}else{
			    return null;
			}
//			Iterator iterator = list.iterator();
//			if (iterator.hasNext()) {
//				BigDecimal object = (BigDecimal) iterator.next();
//				int count = object.intValue();
//				page.setTotalCount(count);
//			} else {
//				return null;
//			}
			sql = " select * from (select a.*, rownum row_num from ( "
					+ "select tj.poi_id,tj.poi_name,count(tj.id) from t_new_visit_"+index+" tj "
					+ "where tj.createon >= "+startDate+" and tj.createon <= "+endDate+" "
					+ "and (tj.sign_out_time - tj.sign_in_time)/1000/60 > "+duration+" "
					+ " and tj.ent_code = '"+entCode+"' "
					+ "and tj.device_id in ("+deviceIds+") "
					+ "and tj.poi_name like '%"+searchValue+"%' "
					+ "group by tj.poi_id,tj.poi_name "
					+ "order by count(tj.id) desc,tj.poi_name ) a) b "
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
		
		String hql = "select tj.poiId,tj.poiName,count(tj.id) from Visit tj "
				+ "where tj.createOn >= ? and tj.createOn <= ? "
				+ "and tj.signOutTime - tj.signInTime > ? "
				+ " and tj.entCode = ? "
				+ "and tj.deviceId in (?) "
				+ "and tj.poiName like ? "
				+ "group by tj.poiId,tj.poiName "
				+ "order by count(tj.id) desc,tj.poiName";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, startDate, endDate,  entCode,
				deviceIds, "%" + searchValue + "%");
	}*/
	   /**
     * 客户被拜访统计同2.0
     */
    public Page<Object[]> listCustomVisitCountTj(String entCode,
            int startint, int limitint, Long startDate, Long endDate,
            String searchValue, String deviceIds, Long duration) {
        int PageNo= startint / limitint + 1;
        int pageSize=limitint;
        int index = CharTools.getStrHashCode(entCode);
        Session session = null;
        try {
           String sql = " select * from (select a.*, rownum row_num from ( "
                    + "select tj.poi_id,tj.poi_name,count(tj.id) from t_new_visit_"+index+" tj "
                    + "where tj.createon >= "+startDate+" and tj.createon <= "+endDate+" "
                    + "and (tj.sign_out_time - tj.sign_in_time)/1000/60 > "+duration+" "
                    + " and tj.ent_code = '"+entCode+"' "
                    + "and tj.device_id in ("+deviceIds+") "
                    + "and tj.poi_name like '%"+searchValue+"%' "
                    + "group by tj.poi_id,tj.poi_name "
                    + "order by count(tj.id) desc,tj.poi_name ) a) b ";
           Page<Object[]> page = new Page<Object[]>(pageSize, true);
           page.setPageNo(PageNo);
           return new PageQuery<Object[]>(this).findByPageWithSql(page,sql);
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

	/**
	 * 客户被拜访统计,点击操作后跳转页面,查询详细的被拜访客户信息同2.0
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Object[]> listCustomVisitCountTjByCustom(String entCode,
			int startint, int limitint, Long startDate, Long endDate,
			Long poiId, String deviceIds, Long duration) {
		int index = CharTools.getStrHashCode(entCode);
		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
		try {
			String sql = "select count(tj.poi_id) as num from t_new_visit_"+index+" tj "
				+ "where tj.createon >= "+startDate+" and tj.createon <= "+endDate+" "
				+ "and (tj.sign_out_time - tj.sign_in_time)/1000/60 > "+duration+" "
				+ "and tj.ent_code = '"+entCode+"' "
				+ "and tj.device_id in ("+deviceIds+") "
				+ "and tj.poi_id = "+poiId+" "
				+ "order by tj.sign_in_time, tj.device_id";
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
					+ "select tj.poi_name, tj.terminal_name, tj.sign_in_time, tj.sign_out_time, " 
					+ "(tj.sign_out_time - tj.sign_in_time)/1000/60 as stayTime, " +
					" tj.sign_in_desc, tj.sign_out_desc, tj.id, " +
					" tj.SIGN_IN_LNG, tj.SIGN_IN_LAT, tj.SIGN_OUT_LNG, tj.SIGN_OUT_LAT, tj.createon "+
					" from t_new_visit_"+index+" tj "
					+ "where tj.createon >= "+startDate+" and tj.createon <= "+endDate+" "
					+ "and (tj.sign_out_time - tj.sign_in_time)/1000/60 > "+duration+" "
					+ "and tj.ent_code = '"+entCode+"' "
					+ "and tj.device_id in ("+deviceIds+") "
					+ "and tj.poi_id = "+poiId+" "
					+ "order by tj.sign_in_time, tj.device_id ) a) b "
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
	
	// 业务员出访统计表 v2.1
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Object[]> listVisitCountTjSql(String entCode,
			int startint, int limitint, Long startDate, Long endDate,
			String searchValue, String deviceIds, Long duration) {
		int index = CharTools.getStrHashCode(entCode);
		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
		try {
			String sql = "select count(visitCount.device_id) from " +
			"(select count(t.device_id) as c, t.device_id from t_new_visit_" + index + " t " +
			"where t.createon >= " + startDate + " and t.createon <=" + endDate + " " +
			"and (t.sign_out_time - t.sign_in_time)/1000/60 > " + duration + " " +
			"and t.ent_code = '" + entCode + "' " +
			"and t.device_id in ("+deviceIds+") " +
			"and t.poi_name like '%"+searchValue+"%'  " +
			"group by t.device_id " +
			")visitCount, " +
			"(select count(tab.device_id) as c, tab.device_id from  " +
			"(select t.device_id, t.poi_id from t_new_visit_" + index + " t " +
			"where t.createon >= " + startDate + " and t.createon <=" + endDate + " " +
			"and (t.sign_out_time - t.sign_in_time)/1000/60 > " + duration + " " +
			"and t.ent_code = '" + entCode + "' " +
			"and t.device_id in ("+deviceIds+") " +
			"and t.poi_name like '%" + searchValue + "%'  " +
			"group by t.device_id, t.poi_id " +
			") tab " +
			"group by tab.device_id " +
			")cusVisitCount " +
			"where visitCount.device_id = cusVisitCount.device_id";
			
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
			sql = " select * from (select a.*, rownum row_num from ( "+
				"select visitCount.device_id, visitCount.terminal_name, visitCount.c as visitCount, cusVisitCount.c as cusVisitCount from " +
				"(select count(t.device_id) as c, t.device_id, t.terminal_name from t_new_visit_" + index + " t " +
				"where t.createon >= " + startDate + " and t.createon <=" + endDate + " " +
				"and (t.sign_out_time - t.sign_in_time)/1000/60 > " + duration + " " +
				"and t.ent_code = '" + entCode + "' " +
				"and t.device_id in ("+deviceIds+") " +
				"and t.poi_name like '%"+searchValue+"%'  " +
				"group by t.device_id, t.terminal_name " +
				")visitCount, " +
				"(select count(tab.device_id) as c, tab.device_id from  " +
				"(select t.device_id, t.poi_id, t.terminal_name from t_new_visit_" + index + " t " +
				"where t.createon >= " + startDate + " and t.createon <=" + endDate + " " +
				"and (t.sign_out_time - t.sign_in_time)/1000/60 > " + duration + " " +
				"and t.ent_code = '" + entCode + "' " +
				"and t.device_id in ("+deviceIds+") " +
				"and t.poi_name like '%" + searchValue + "%'  " +
				"group by t.device_id, t.poi_id, t.terminal_name " +
				") tab " +
				"group by tab.device_id " +
				")cusVisitCount " +
				"where visitCount.device_id = cusVisitCount.device_id " +
				"order by visitCount desc, cusVisitCount desc, visitCount.terminal_name) a) b "
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
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Object[]> listVisitCountTjByCustom(String entCode,
			int startint, int limitint, Long startDate, Long endDate,
			String deviceId, Long duration) {
		int index = CharTools.getStrHashCode(entCode);
		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
		try {
			String sql = "select count(tj.poi_id) as num from t_new_visit_"+index+" tj "
				+ "where tj.createon >= "+startDate+" and tj.createon <= "+endDate+" "
				+ "and (tj.sign_out_time - tj.sign_in_time)/1000/60 > "+duration+" "
				+ "and tj.ent_code = '"+entCode+"' "
				+ "and tj.device_id = '"+deviceId+"' "
				//+ "and tj.poi_id = "+poiId+" "
				+ "order by tj.sign_in_time, tj.device_id";
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
					+ "select tj.poi_name, tj.terminal_name, tj.sign_in_time, tj.sign_out_time, " 
					+ "(tj.sign_out_time - tj.sign_in_time)/1000/60 as stayTime, " +
					" tj.sign_in_desc, tj.sign_out_desc, tj.id, " +
					" tj.SIGN_IN_LNG, tj.SIGN_IN_LAT, tj.SIGN_OUT_LNG, tj.SIGN_OUT_LAT, tj.createon "+
					" from t_new_visit_"+index+" tj "
					+ "where tj.createon >= "+startDate+" and tj.createon <= "+endDate+" "
					+ "and (tj.sign_out_time - tj.sign_in_time)/1000/60 > "+duration+" "
					+ "and tj.ent_code = '"+entCode+"' "
					+ "and tj.device_id = '"+deviceId+"' "
					//+ "and tj.poi_id = "+poiId+" "
					+ "order by tj.sign_in_time, tj.device_id ) a) b "
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
}
