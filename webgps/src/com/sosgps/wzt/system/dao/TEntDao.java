/**
 * 
 */
package com.sosgps.wzt.system.dao;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TEnt;

/**
 * @author xiaojun.luan
 *
 */
public interface TEntDao {
	public TEnt findByEntCode(String entCode);
	public void save(TEnt tEnt);
	public void update(TEnt tEnt);
	public List findAll();
	// sos企业列表
	public Page<TEnt> listEnt(int pageNo, int pageSize, String searchValue);
	public Page<TEnt> listEnt2(int pageNo, int pageSize, String searchValue);
	public Page<TEnt> listEnt3(int pageNo, int pageSize, String searchValue);
	//企业列表+企业目标模板类型(周/旬/月)
	//public Page<Object[]> listEntAndTemplateType(int pageNo, int pageSize, String searchValue);
	// sos删除企业
	public void delete(TEnt ent);
}
