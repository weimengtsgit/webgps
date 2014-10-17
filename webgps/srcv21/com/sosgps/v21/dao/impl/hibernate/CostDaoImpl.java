package com.sosgps.v21.dao.impl.hibernate;

import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sosgps.v21.dao.CostDao;
import com.sosgps.v21.model.Cost;
import com.sosgps.v21.util.CharTools;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.util.PageQuery;
 
public class CostDaoImpl extends BaseHibernateDao implements CostDao {

	private static final Logger logger = LoggerFactory
			.getLogger(CostDaoImpl.class);

	@SuppressWarnings("unchecked")
	public List<BigDecimal> getCostCount(Long startTime, Long endTime,
			String entCode, int isApproved, int isNotApproved, String deviceIds) {
		try {
			configInterceptor(entCode);
			String deviceIdsCon = "";
			if(deviceIds != null && deviceIds.length() > 0){
				deviceIdsCon = " and t.deviceId in ("+deviceIds+") ";
			}
			String hql = "select nvl(sum(t.meal),0) "
					+ " + nvl(sum(t.transportation),0) "
					+ " + nvl(sum(t.accommodation),0) "
					+ " + nvl(sum(t.communication),0) "
					+ " + nvl(sum(t.gift),0) "
					+ " + nvl(sum(t.other),0) as costCounts "
					+ " from Cost t " +
					" where t.createOn > ? and t.createOn < ? " +
					" and t.entCode = ? " +
					" and t.states = 0 " +
					 deviceIdsCon +
					" and (t.approved = ? or t.approved = ?) ";
			return super.getHibernateTemplate().find(hql,
					new Object[] { startTime, endTime, entCode, isApproved, isNotApproved });
		} catch (Exception e) {
			logger.error("[getCostCount]", e);
			return null;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<Object[]> getCostsByTime(Long startTime, Long endTime,
			String entCode) {
		Session session = null;
		try {
			int index = CharTools.getStrHashCode(entCode);
			String sql = "select cost.costs, to_char(base.createon, 'yyyy-mm-dd') from "
					+ "(select trunc(mapsearch_utc_to_date(t.createon)) as createon,"
					+ " nvl(sum(t.meal),0) "
					+ " + nvl(sum(t.transportation),0) "
					+ " + nvl(sum(t.accommodation),0) "
					+ " + nvl(sum(t.communication),0) "
					+ " + nvl(sum(t.gift),0) "
					+ " + nvl(sum(t.other),0) as costs "
					+ "from t_cost_"
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
					+ ") cost, "
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
					+ "where base.createon = cost.createon(+) "
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
	public List<Object[]> getCostsByTime(Long startTime, Long endTime,
			String entCode, String deviceIds) {
		Session session = null;
		try {
			int index = CharTools.getStrHashCode(entCode);
			String deviceIdsCon = "";
			if(deviceIds != null && deviceIds.length() > 0){
				deviceIdsCon = " and t.device_id in ("+deviceIds+") ";
			}
			String sql = "select cost.value, to_char(base.createon, 'yyyy-mm-dd') from "
					+ "(select trunc(mapsearch_utc_to_date(t.createon)) as createon,"
					+ " nvl(sum(t.meal),0) "
					+ " ||','|| nvl(sum(t.transportation),0) "
					+ " ||','|| nvl(sum(t.accommodation),0) "
					+ " ||','|| nvl(sum(t.communication),0) "
					+ " ||','|| nvl(sum(t.gift),0) "
					+ " ||','|| nvl(sum(t.other),0) as value  "
					+ "from t_cost_"
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
					+ deviceIdsCon
					+ "group by trunc(mapsearch_utc_to_date(t.createon)) "
					+ ") cost, "
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
					+ "where base.createon = cost.createon(+) "
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
	
	/**
	 * 费用明细表
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
	public Page<Cost> listCostDetails(String entCode,
			int pageNo, int pageSize, Long startDate,
			Long endDate, int approved, String deviceIds) {
		int approved_ = 0;
		if(approved == -1){
			approved = 1;
		}else if(approved == 1){
			approved_ = 1;
		}
		configInterceptor(entCode);
		String hql = "select t from Cost t "
				+ " where t.createOn > ? and t.createOn < ? "
				+ " and t.entCode = ? "
				+ " and (t.approved =? or t.approved =?) "
				+ " and t.states = 0 ";
				if(!deviceIds.equals("'-1'")){
					hql += " and t.deviceId in ("+deviceIds+")";
				}
				hql += " order by t.createOn desc, t.groupName, t.deviceId, t.approved";
		PageQuery<Cost> pageQuery = new PageQuery<Cost>(this);
		Page<Cost> page = new Page<Cost>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPageHash(entCode, page, hql, startDate, endDate, entCode,
				approved, approved_);
	}
	
	public boolean approved(Long[] ids, String entCode) {
		try{
			configInterceptor(entCode);
			StringBuffer hql = new StringBuffer(
					"update Cost t set t.approved = 1 where t.id in (?1)");
			Query query = super.getSession().createQuery(hql.toString());
			query.setParameterList("1", ids);
			query.executeUpdate();
			return true;
		}catch(Exception e){
			logger.error("[approved] error", e);
			return false;
		}
	}
}
