package com.sosgps.wzt.manage.util;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @Title:DAO��������
 * @Description:��DAO��ʵ����ͨ�õ����ݲ���
 * @param <T>
 *            POJOʵ�����
 * @param <ID>
 *            ID
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-4 ����03:53:02
 */
public class BaseHibernateDAO<T, ID extends Serializable> extends
		HibernateDaoSupport {
	private static final Logger logger = Logger
			.getLogger(BaseHibernateDAO.class);

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
	public Page<T> findByPage(final Page<T> page, final String hql,
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
					Query query = createQuery(s, hql, values);
					if (page.isAutoCount()) {
						List li = query.list();
						int totalCount = 0;
						if (li != null) {
							totalCount = ((Long)li.get(0)).intValue();
						}
						page.setTotalCount(totalCount);// �����ܼ�¼��
						page.getNextPage();// �Ƿ�����ҳ
						page.getPrePage();// �Ƿ�����ҳ
					}
					return page;
				}
			});
			return (Page<T>) getHibernateTemplate().execute(
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
