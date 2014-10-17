/**
 * 
 */
package com.sosgps.wzt.area.dao.hibernate;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sosgps.wzt.area.dao.AreaDAO;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefTermAreaalarm;
import com.sosgps.wzt.orm.TArea;
import com.sosgps.wzt.orm.TAreaDAO;
import com.sosgps.wzt.util.PageQuery;

/**
 * @author shiguang.zhou
 * 
 */
public class AreaDAOImpl extends TAreaDAO implements AreaDAO {

	public Page<TArea> listArea(String entCode, int pageNo, int pageSize,
			String searchValue) {
		String hql = "select a from TArea a where a.epCode=? and a.flag=1 and a.name like ?";
		PageQuery<TArea> pageQuery = new PageQuery<TArea>(this);
		Page<TArea> page = new Page<TArea>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery
				.findByPage(page, hql, entCode, "%" + searchValue + "%");
	}

	public void deleteRefTermAreasByDeviceIds(final String[] deviceIds) {

		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "select ref from RefTermAreaalarm ref where ref.TTerminal.deviceId in(:deviceIds)";
				Query query = session.createQuery(hql);
				query.setParameterList("deviceIds", deviceIds);
				List refs = query.list();
				if (refs != null) {
					for (Iterator iterator = refs.iterator(); iterator
							.hasNext();) {
						RefTermAreaalarm ref = (RefTermAreaalarm) iterator
								.next();
						session.delete(ref);
					}
				}
				return null;
			}
		});
	}
	
	public void deleteRefTermAreasByDeviceIdsAndAreaIds(final String deviceId,final Long[] areaIds) {

		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "select ref from RefTermAreaalarm ref where ref.TTerminal.deviceId = :deviceId and ref.TArea.id in(:areaIds)";
				Query query = session.createQuery(hql);
				query.setParameter("deviceId", deviceId);
				query.setParameterList("areaIds", areaIds);
				List refs = query.list();
				if (refs != null) {
					for (Iterator iterator = refs.iterator(); iterator
							.hasNext();) {
						RefTermAreaalarm ref = (RefTermAreaalarm) iterator
								.next();
						session.delete(ref);
					}
				}
				return null;
			}
		});
	}

	public void saveRefTermArea(RefTermAreaalarm transientInstance) {
		getHibernateTemplate().save(transientInstance);
	}

	public List findRefTermAreasByDeviceId(final String deviceId) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "select ref from RefTermAreaalarm ref where ref.TTerminal.deviceId = :deviceId";
				Query query = session.createQuery(hql);
				query.setParameter("deviceId", deviceId);
				return query.list();
			}
		});
	}

	public Page<Object[]> listRefTermArea(String entCode, String alarmType,
			int pageNo, int pageSize, String searchValue) {
		String hql = "select a,t,ref from RefTermAreaalarm ref "
				+ "left join ref.TArea a " + "left join ref.TTerminal t "
				+ "where a.epCode=? and a.flag=1 "
				+ "and (a.name like ? or t.vehicleNumber like ?) ";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);

		if (alarmType.equals("")) {
		} else {
			hql += "ref.alarmType=" + alarmType + " ";
		}
		hql += "order by t.vehicleNumber";
		return pageQuery.findByPage(page, hql, entCode,
				"%" + searchValue + "%", "%" + searchValue + "%");
	}

	public Page<TArea> queryAreaByDeviceId(String deviceId) {
		String hql = "select a from TArea a "
				+ "left join fetch a.refTermAreaalarms ref "
				+ "left join ref.TTerminal t "
				+ "where t.deviceId=? order by a.createdate";
		PageQuery<TArea> pageQuery = new PageQuery<TArea>(this);
		Page<TArea> page = new Page<TArea>();
		page.setAutoCount(true);
		page.setPageNo(1);
		page.setPageSize(Integer.MAX_VALUE);
		return pageQuery.findByPage(page, hql, deviceId);
	}
}
