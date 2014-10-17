package com.sosgps.v21.dao.impl.hibernate;

import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sosgps.v21.dao.SignBillDao;
import com.sosgps.v21.model.SignBill;
import com.sosgps.v21.util.CharTools;
import com.sosgps.wzt.util.PageQuery;
import com.sosgps.wzt.manage.util.Page;

public class SignBillDaoImpl extends BaseHibernateDao implements SignBillDao {

	private static final Logger logger = LoggerFactory
			.getLogger(SignBillDaoImpl.class);

	/**
	 * 时间范围内实际签单额总值,isApproved=0,isNotApproved=1查全部(审核及未审核);isApproved=1,isNotApproved=1查审核;isApproved=0,isNotApproved=0查未审核;
	 */
	@SuppressWarnings("unchecked")
	public List<BigDecimal> getSignBillCount(Long startTime, Long endTime,
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
			String hql = "select sum(t.signBillAmount) " +
					"from SignBill t " +
					"where t.createOn >= ? and t.createOn <= ? " +
					"and t.entCode = ? " +
					"and t.states = 0 " +
					poiNameCon +
					deviceIdsCon +
					"and (t.approved = ? or t.approved = ?) ";
			return super.getHibernateTemplate().find(hql,
					new Object[] { startTime, endTime, entCode, isApproved, isNotApproved });
		} catch (Exception e) {
			logger.error("[getSignBillCount]", e);
			return null;
		}
	}
	/**
	 * 鏍规嵁鏃堕棿娈垫煡璇㈠�锛屾瘡澶╃殑绱棰�
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getSignBillsByTime(Long startTime, Long endTime,
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
			String sql = "select sign_bill.sign_bills, to_char(base.createon, 'yyyy-mm-dd') from "
					+ "(select trunc(mapsearch_utc_to_date(t.createon)) as createon,sum(t.sign_bill_amount) as sign_bills "
					+ "from t_sign_bill_"
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
					+ ") sign_bill, "
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
					+ "where base.createon = sign_bill.createon(+) "
					+ "order by base.createon ";

			logger.info("[getSignBillsByTime] " + sql);
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
	 * 绛惧崟棰濇槑缁嗚〃
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
	public Page<SignBill> listSignBillDetails(String entCode,
			int pageNo, int pageSize, Long startDate,
			Long endDate, int approved, String poiName, String deviceIds) {
		int approved_ = 0;
		if(approved == -1){
			approved = 1;
		}else if(approved == 1){
			approved_ = 1;
		}
		configInterceptor(entCode);
		String hql = "select t from SignBill t "
				+ " where t.createOn > ? and t.createOn < ? "
				+ " and t.entCode = ? "
				+ " and (t.approved =? or t.approved =?) "
				+ " and (t.poiName like ? or t.poiName is null) "
				+ " and t.states = 0 ";
				if(!deviceIds.equals("'-1'")){
					hql += " and t.deviceId in ("+deviceIds+")";
				}
				hql += " order by t.createOn desc, t.groupName, t.deviceId, t.poiName, t.approved";
		PageQuery<SignBill> pageQuery = new PageQuery<SignBill>(this);
		Page<SignBill> page = new Page<SignBill>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPageHash(entCode, page, hql, startDate, endDate, entCode,
				approved, approved_, "%"+poiName+"%");
	}
	
	public boolean approved(Long[] ids, String entCode) {
		try{
			configInterceptor(entCode);
			StringBuffer hql = new StringBuffer(
					"update SignBill t set t.approved = 1 where t.id in (?1)");
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
