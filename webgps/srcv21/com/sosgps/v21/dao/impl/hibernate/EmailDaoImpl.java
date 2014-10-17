package com.sosgps.v21.dao.impl.hibernate;

import java.util.List;

import com.sosgps.v21.dao.EmailDao;
import com.sosgps.v21.model.Email;

public class EmailDaoImpl extends BaseHibernateDao implements EmailDao {
	public void saveEmails(Email email) {
		super.save(email);
	}

	public List<Email> getEmails(String entCode) {
		String hql = "select t from Email t where t.entCode = ?";
		return super.getHibernateTemplate().find(hql, new Object[] { entCode });
	}

	public void removeEmails(String entCode) {
		String hql = "delete from Email t where t.entCode = ?";
		super.getHibernateTemplate().bulkUpdate(hql, new Object[] { entCode });
	}
}
