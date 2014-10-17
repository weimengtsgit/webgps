/**
 * 
 */
package com.sosgps.wzt.system.dao.hibernate;

import com.sosgps.wzt.orm.TBossRole;
import com.sosgps.wzt.orm.TBossRoleDAO;
import com.sosgps.wzt.system.dao.TBossRoleDao;

/**
 * @author xiaojun.luan
 *
 */
public class TBossRoleDaoImpl extends TBossRoleDAO implements TBossRoleDao {

	/**
	 * 
	 */
	public TBossRoleDaoImpl() {
	}

	public void update(TBossRole transientInstance) {
		try {
			getHibernateTemplate().update(transientInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

}
