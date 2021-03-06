package com.sosgps.wzt.vehiclenorm.dao.hibernate;

import java.sql.SQLException;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sosgps.wzt.dispatchmoney.dao.DispatchMoneyDao;
import com.sosgps.wzt.insurance.dao.InsuranceDao;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDispatchMoneyDAO;
import com.sosgps.wzt.orm.TInsuranceDao;
import com.sosgps.wzt.orm.TVehicleNormDAO;
import com.sosgps.wzt.orm.TVehiclesMaintenanceDao;
import com.sosgps.wzt.util.PageQuery;
import com.sosgps.wzt.vehiclenorm.dao.VehicleNormDao;
import com.sosgps.wzt.vehiclesMaintenance.dao.VehiclesMaintenanceDao;

public class VehicleNormDaoImpl extends TVehicleNormDAO implements VehicleNormDao {

	public Page<Object[]> listvehicleNormDaoImpl(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue) {
		String hql = "select tn.id, t.termName, t.vehicleNumber, t.simcard, g.groupName, tn " +
				" from TTerminal t, TVehicleNorm tn "
				+ " left join t.refTermGroups ref "
				+ " left join ref.TTermGroup g "
				+ " where t.entCode = ? " 
				//+ "and to.userId = ?  "
				+ " and (t.termName like ? or t.vehicleNumber like ? or g.groupName like ?) "
				+ " and tn.saveDate >= ? and tn.saveDate <= ? "
				+ " and tn.deviceId = t.deviceId "
				+ " order by tn.saveDate desc, t.termName ";
		PageQuery<Object[]> pq = new PageQuery<Object[]>(this);
		Page<Object[]> re = new Page<Object[]>();
		re.setAutoCount(true);
		re.setPageNo(pageNo);
		re.setPageSize(pageSize);
		//return pq.findByPage( re, hql, entCode, userId, "%" + searchValue + "%", "%" + searchValue + "%", "%" + searchValue + "%", startDate, endDate );
		return pq.findByPage( re, hql, entCode, "%" + searchValue + "%", "%" + searchValue + "%", "%" + searchValue + "%", startDate, endDate );
		
	}
	
	public void deleteAll(final String ids) {
		try {
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session s)
						throws HibernateException, SQLException {
					Query query = s.createQuery("delete TVehicleNorm t where t.id in ("+ids+")");
					//query.setParameter("ids", ids);
					query.executeUpdate();
					return null;
				}
			});
		} catch (RuntimeException re) {
			logger.error(re);
			throw re;
		}
	}

}
