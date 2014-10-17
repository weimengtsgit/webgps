/**
 * 
 */
package com.sosgps.wzt.system.dao.hibernate;

import java.util.List;

import com.sosgps.wzt.orm.RefEntCallednumber;
import com.sosgps.wzt.orm.RefEntCallednumberDAO;
import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.system.dao.RefEntCalledNumberDao;

/**
 * @author xiaojun.luan
 *
 */
public class RefEntCalledNumberHibernateDao extends RefEntCallednumberDAO
		implements RefEntCalledNumberDao {

	public RefEntCallednumber findByCallednumber(String callednumber){

		List list= this.findByCalledNumber(callednumber);
		if(list.size()>0){
			return (RefEntCallednumber)list.get(0);
		}
		return null;
	}
	public List findByEntCode(TEnt tEnt){
		RefEntCallednumber refEntCallednumber=new RefEntCallednumber();
		refEntCallednumber.setTEnt(tEnt);
		List list= this.findByExample(refEntCallednumber);	
		return list;
	}

}
