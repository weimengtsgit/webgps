/**
 * 
 */
package com.sosgps.wzt.system.dao;

import java.util.List;

import com.sosgps.wzt.orm.RefEntCallednumber;
import com.sosgps.wzt.orm.TEnt;

/**
 * @author xiaojun.luan
 *
 */
public interface RefEntCalledNumberDao {
	public void save(RefEntCallednumber refEntCallednumber);
	public void update(RefEntCallednumber refEntCallednumber);
	public void delete(RefEntCallednumber refEntCallednumber);
	public RefEntCallednumber findByCallednumber(String callednumber);
	public List findByEntCode(TEnt tEnt);
	public RefEntCallednumber findById(Long id);
}
