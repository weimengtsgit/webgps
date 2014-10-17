package com.sosgps.wzt.manage.terminalgroup.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sosgps.wzt.manage.terminalgroup.dao.UserDao;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.orm.TUserDAO;

/**
 * @Title:
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-30 ����11:05:32
 */
public class UserHibernateDao extends TUserDAO implements UserDao {

	/**
	 * * ��HQL��ҳ��ѯ.
	 * 
	 * @param page
	 *            ҳ�����
	 * @param hql
	 *            HQL���
	 * @param values
	 *            �ɱ�����б�
	 * @return ��ҳ����
	 */
	public Page<TUser> findByPage(final Page<TUser> page, final String hql,
			final Object... values) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("��ʼ����ָ��HQL��ҳ����," + hql);
			}
			// ������û�ȡ���������Ȼ�ȡ����
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session s)
						throws HibernateException, SQLException {
					String countHql = hql.substring(hql.indexOf("from"));
					countHql = "select count(*) " + countHql;
					Query query = createQuery(s, countHql, values);
					if (page.isAutoCount()) {
						List li = query.list();
						int totalCount = 0;
						if(li!=null){
							totalCount = ((Long)li.get(0)).intValue();
						}
						page.setTotalCount(totalCount);// �����ܼ�¼��
						page.getNextPage();// �Ƿ�����ҳ
						page.getPrePage();// �Ƿ�����ҳ
					}
					return page;
				}
			});
			return (Page<TUser>) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session s)
								throws HibernateException, SQLException {
							Query query = createQuery(s, hql, values);
							if (page.isFirstSetted()) {
								query.setFirstResult(page.getFirst());
							}
							if (page.isPageSizeSetted()) {
								query.setMaxResults(page.getPageSize());
							}
							page.setResult(query.list());
							if (logger.isDebugEnabled()) {
								logger.debug("����ָ��HQL��ҳ���ݳɹ�," + hql);
							}
							return page;
						}
					});
		} catch (RuntimeException e) {
			logger.error("��ҳ��ѯ�쳣��HQL��" + hql, e);
			throw e;
		}
	}

	/**
	 * ���ݲ�ѯ����������б���Query����
	 * 
	 * @param session
	 *            Hibernate�Ự
	 * @param hql
	 *            HQL���
	 * @param objects
	 *            �����б�
	 * @return Query����
	 */
	public Query createQuery(Session session, String hql, Object... objects) {
		Query query = session.createQuery(hql);
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				query.setParameter(i, objects[i]);
			}
		}
		return query;
	}
}
