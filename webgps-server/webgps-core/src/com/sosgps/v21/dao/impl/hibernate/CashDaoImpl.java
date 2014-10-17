package com.sosgps.v21.dao.impl.hibernate;

import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sosgps.v21.dao.CashDao;
import com.sosgps.v21.model.Cash;
import com.sosgps.v21.util.CharTools;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.util.PageQuery;

public class CashDaoImpl extends BaseHibernateDao implements CashDao {

	private static final Logger logger = LoggerFactory
			.getLogger(CashDaoImpl.class);

	/**
	 * 获取签单额总值
	 */
	@SuppressWarnings("unchecked")
	public List<BigDecimal> getCashCount(Long startTime, Long endTime,
			String entCode, int isApproved, int isNotApproved, String poiName, String deviceIds) {
		try {
			configInterceptor(entCode);
			String poiNameCon = "";
			if(poiName != null && poiName.length() > 0){
				poiNameCon = " and t.poiName like '%"+poiName+"%' ";
			}
			String deviceIdsCon = "";
			if(deviceIds != null && deviceIds.length() > 0){
				deviceIdsCon = " and t.deviceId in ("+deviceIds+") ";
			}
			StringBuffer hql = new StringBuffer();
			if(entCode.equals("tstx") || entCode.equals("cpbcs") || entCode.equals("tspt")){
				hql.append("select sum((nvl((t.cashAmount),0)* " +
						" nvl((t.cashAmount2),0))+ "+
						" (nvl((t.cashAmount4),0)* "+
						" nvl((t.cashAmount5),0))+ "+
						" (nvl((t.cashAmount6),0)* "+
						" nvl((t.cashAmount7),0))+ "+
						" nvl((t.cashAmount8),0)+ "+
						" nvl((t.cashAmount9),0)+ "+
						" nvl((t.cashAmount10),0)+ "+
						" nvl((t.cashAmount11),0)) as cashAmount ");
			}else{
				hql.append("select nvl(sum(t.cashAmount),0)+ " +
						" nvl(sum(t.cashAmount2),0)+ "+
						" nvl(sum(t.cashAmount3),0)+ "+
						" nvl(sum(t.cashAmount4),0)+ "+
						" nvl(sum(t.cashAmount5),0)+ "+
						" nvl(sum(t.cashAmount6),0)+ "+
						" nvl(sum(t.cashAmount7),0)+ "+
						" nvl(sum(t.cashAmount8),0)+ "+
						" nvl(sum(t.cashAmount9),0)+ "+
						" nvl(sum(t.cashAmount10),0)+ "+
						" nvl(sum(t.cashAmount11),0) as cashAmount ");
			}
			hql.append("from Cash t " +
			"where t.createOn > ? and t.createOn < ? " +
			"and t.entCode = ? " +
			"and t.states = 0 " +
			poiNameCon +
			deviceIdsCon +
			"and (t.approved = ? or t.approved = ?) ");
			
			return super.getHibernateTemplate().find(hql.toString(),
					new Object[] { startTime, endTime, entCode, isApproved, isNotApproved });
		} catch (Exception e) {
			logger.error("[getCashCount]", e);
			return null;
		}
	}

	/**
	 * 获取签单额趋势图
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getCashsByTime(Long startTime, Long endTime,
			String entCode, String poiName, String deviceIds) {
		Session session = null;
		try {
			int index = CharTools.getStrHashCode(entCode);
			String poiNameCon = "";
			if(poiName != null && poiName.length() > 0){
				poiNameCon = "and t.POI_NAME like '%"+poiName+"%' ";
			}
			String deviceIdsCon = "";
			if(deviceIds != null && deviceIds.length() > 0){
				deviceIdsCon = " and t.device_id in ("+deviceIds+") ";
			}
			StringBuffer sql = new StringBuffer();
			sql.append("select cash.cashs, to_char(base.createon, 'yyyy-mm-dd') from "
					+ "(select trunc(mapsearch_utc_to_date(t.createon)) as createon, ");
			if(entCode.equals("tstx") || entCode.equals("cpbcs") || entCode.equals("tspt")){
				sql.append(
						" sum((nvl((t.cash_Amount),0)* "+
						" nvl((t.cash_Amount2),0))+ "+
						" (nvl((t.cash_Amount4),0)* "+
						" nvl((t.cash_Amount5),0))+ "+
						" (nvl((t.cash_Amount6),0)* "+
						" nvl((t.cash_Amount7),0))+ "+
						" nvl((t.cash_Amount8),0)+ "+
						" nvl((t.cash_Amount9),0)+ "+
						" nvl((t.cash_Amount10),0)+ "+
						" nvl((t.cash_Amount11),0)) as cashs ");
			}else{
				sql.append(
						" nvl(sum(t.cash_Amount),0)+ "+
						" nvl(sum(t.cash_Amount2),0)+ "+
						" nvl(sum(t.cash_Amount3),0)+ "+
						" nvl(sum(t.cash_Amount4),0)+ "+
						" nvl(sum(t.cash_Amount5),0)+ "+
						" nvl(sum(t.cash_Amount6),0)+ "+
						" nvl(sum(t.cash_Amount7),0)+ "+
						" nvl(sum(t.cash_Amount8),0)+ "+
						" nvl(sum(t.cash_Amount9),0)+ "+
						" nvl(sum(t.cash_Amount10),0)+ "+
						" nvl(sum(t.cash_Amount11),0) as cashs ");
			}
			sql.append( "from t_cash_"
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
					+ poiNameCon
					+ deviceIdsCon
					+ "group by trunc(mapsearch_utc_to_date(t.createon)) "
					+ ") cash, "
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
					+ "where base.createon = cash.createon(+) "
					+ "order by base.createon ");

			logger.info("[getCashsByTime] " + sql);
			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql.toString());
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
	 * 获取签单额列表
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
	public Page<Cash> listCashDetails(String entCode,
			int pageNo, int pageSize, Long startDate,
			Long endDate, int approved, String poiName, String deviceIds) {
		int approved_ = 0;
		if(approved == -1){
			approved = 1;
		}else if(approved == 1){
			approved_ = 1;
		}
		configInterceptor(entCode);
		String hql = "select t from Cash t "
				+ " where t.createOn > ? and t.createOn < ? "
				+ " and t.entCode = ? "
				+ " and (t.approved =? or t.approved =?) "
				+ " and (t.poiName like ? or t.poiName is null) "
				+ " and t.states = 0 ";
				if(!deviceIds.equals("'-1'")){
					hql += " and t.deviceId in ("+deviceIds+")";
				}
				hql += " order by t.createOn desc, t.groupName, t.deviceId, t.poiName, t.approved";
		PageQuery<Cash> pageQuery = new PageQuery<Cash>(this);
		Page<Cash> page = new Page<Cash>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPageHash(entCode, page, hql, startDate, endDate, entCode,
				approved, approved_, "%"+poiName+"%");
	}
	
	/**
	 * 提交审核
	 */
	public boolean approved(Long[] ids, String entCode) {
		try{
			configInterceptor(entCode);
			StringBuffer hql = new StringBuffer(
					"update Cash t set t.approved = 1 where t.id in (?1)");
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
