package com.sosgps.v21.dao.impl.hibernate;

import org.hibernate.Interceptor;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sosgps.v21.dao.PromotionDao;
import com.sosgps.v21.interceptor.CustomizeInterceptor;
import com.sosgps.v21.model.Promotion;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.util.PageQuery;


public class PromotionDaoImpl extends HibernateDaoSupport implements PromotionDao{

	//�����ϱ�  ��ѯ��ϸ
	public Page<Promotion> listPromotionDetails(String entCode, int pageNo,
			int pageSize, Long startDate, Long endDate, int approved,
			String poiName, String deviceIds) {
		// TODO Auto-generated method stub
	
		int approved_ = 0;
		if(approved == -1){
			approved = 1;
		}else if(approved == 1){
			approved_ = 1;
		}
		configInterceptor(entCode);
		String hql = "select t from Promotion t "
				+ " where t.createOn > ? and t.createOn < ? "
				+ " and t.entCode = ? "
				+ " and (t.approved =? or t.approved =?) "
				+ " and (t.poiName like ? or t.poiName is null) "
				+ " and t.states = 0 ";
				if(!deviceIds.equals("'-1'")){
					hql += " and t.deviceId in ("+deviceIds+")";
				}
				hql += " order by t.createOn desc, t.groupName, t.deviceId, t.poiName, t.approved";
		PageQuery<Promotion> pageQuery = new PageQuery<Promotion>(this);
		Page<Promotion> page = new Page<Promotion>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPageHash(entCode, page, hql, startDate, endDate, entCode,
				approved, approved_, "%"+poiName+"%");
	}
	
	
	//���

	public boolean promotionApproved(Long[] ids, String entCode) {
		try{
			configInterceptor(entCode);
			StringBuffer hql = new StringBuffer(
					"update Promotion t set t.approved = 1 where t.id in (?1)");
			Query query = super.getSession().createQuery(hql.toString());
			query.setParameterList("1", ids);
			query.executeUpdate();
			return true;
		}catch(Exception e){
			logger.error("[approved] error", e);
			return false;
		}
	}
	
	
	public void configInterceptor(String entCode) {
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		if (sf instanceof SessionFactoryImplementor) {
			System.out.println("sf implements SessionFactoryImplementor");
			SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor) sf;
			Interceptor interceptor = sessionFactoryImplementor
					.getInterceptor();
			if (interceptor instanceof CustomizeInterceptor) {
				System.out.println("come in CustomizeInterceptor");
				CustomizeInterceptor customizeInterceptor = (CustomizeInterceptor) interceptor;
				customizeInterceptor.setSeed(entCode);
			}
		}
	}
}
