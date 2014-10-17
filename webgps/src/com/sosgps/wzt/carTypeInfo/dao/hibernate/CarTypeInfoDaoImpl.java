package com.sosgps.wzt.carTypeInfo.dao.hibernate;

import java.sql.SQLException;
import java.util.Date;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import com.sosgps.wzt.carTypeInfo.dao.CarTypeInfoDao;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.CarTypeInfoDAO;
import com.sosgps.wzt.util.PageQuery;

public class CarTypeInfoDaoImpl extends CarTypeInfoDAO implements CarTypeInfoDao {

	public Page<Object[]> listCarTypeInfo(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue) {
		String hql = " select t "
		 +" from CarTypeInfo t "
		 +" where t.enCode = ? "
		 +" and t.typeName like ? "
		 +" order by t.crtdate";
		PageQuery<Object[]> pq = new PageQuery<Object[]>(this);
		Page<Object[]> re = new Page<Object[]>();
		re.setAutoCount(true);
		re.setPageNo(pageNo);
		re.setPageSize(pageSize);
		return pq.findByPage( re, hql, entCode, "%" + searchValue + "%" );
	}
	
	public void deleteAll(final String ids) {
		try {
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session s)
						throws HibernateException, SQLException {
					Query query = s.createQuery("delete CarTypeInfo t where t.id in ("+ids+")");
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
