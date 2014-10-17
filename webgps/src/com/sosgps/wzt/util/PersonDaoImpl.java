package com.sosgps.wzt.util;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;





public class PersonDaoImpl extends HibernateDaoSupport{

	  public int getTotalRowNum() throws Exception {
	  		this.getHibernateTemplate().execute(new HibernateCallback() {
	  			public Object doInHibernate(Session session) throws HibernateException {
	  				String totalPageSql = "";
	  				Query query = session.createQuery(totalPageSql);
	  				if (!query.list().isEmpty()) {
	  					Integer totalRowNum = (Integer) query.list().get(0);
	  					return totalRowNum.intValue();
	  				} else {
	  					return 0;
	  				}
	  			}
	  		});
	  		return 0;
	  	}
	  public static void main(String[] args) {
		  PersonDaoImpl PersonDaoImpl = new PersonDaoImpl();
		  try{
		  PersonDaoImpl.getTotalRowNum();
		  }catch(Exception x){
			  x.printStackTrace();
		  }
	}
}



