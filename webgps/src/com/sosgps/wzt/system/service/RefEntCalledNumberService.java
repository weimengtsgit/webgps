/**
 * 
 */
package com.sosgps.wzt.system.service;

import java.util.List;

import com.sosgps.wzt.orm.RefEntCallednumber;

/**
 * @author xiaojun.luan
 *
 */
public interface RefEntCalledNumberService {
	public void save(RefEntCallednumber refEntCallednumber);
	public void save(String entCode,String callednumber);

	public void update(RefEntCallednumber refEntCallednumber);
	public void delete(RefEntCallednumber refEntCallednumber);
	public void deleteAll(String[] ids);
	public void deleteByCallednumber(String callednumber);
	public RefEntCallednumber findByCallednumber(String callednumber);
	public List findByEntCode(String entCode);
	public RefEntCallednumber findById(Long id);
}
