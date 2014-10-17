/**
 * 
 */
package com.sosgps.wzt.system.dao;

import com.sosgps.wzt.orm.TBossRole;

import java.util.List;
/**
 * @author xiaojun.luan
 *
 */
public interface TBossRoleDao {
	public TBossRole findById(Long id);
	public void save(TBossRole tBossRole);
	public void delete(TBossRole tBossRole);
	public void update(TBossRole tBossRole);
	public List findByBossUserType(Object type);
}
