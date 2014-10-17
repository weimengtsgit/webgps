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
	// sos��ҵ�б�
	public Page<TEnt> listEnt(int pageNo, int pageSize, String searchValue);
	public Page<TEnt> listEnt2(int pageNo, int pageSize, String searchValue);
	public Page<TEnt> listEnt3(int pageNo, int pageSize, String searchValue);
	//��ҵ�б�+��ҵĿ��ģ������(��/Ѯ/��)
	//public Page<Object[]> listEntAndTemplateType(int pageNo, int pageSize, String searchValue);
	// sosɾ����ҵ
	public void delete(TEnt ent);
}
