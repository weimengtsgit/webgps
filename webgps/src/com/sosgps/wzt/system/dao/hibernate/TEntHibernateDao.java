/**
 * 
 */
package com.sosgps.wzt.system.dao.hibernate;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.orm.TEntDAO;
import com.sosgps.wzt.system.dao.TEntDao;
import com.sosgps.wzt.util.PageQuery;

/**
 * @author xiaojun.luan
 * 
 */
public class TEntHibernateDao extends TEntDAO implements TEntDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.wzt.system.dao.TEntDao#findByEntCode(java.lang.String)
	 */
	public TEnt findByEntCode(String entCode) {
		// TODO Auto-generated method stub
		return super.findById(entCode);
	}

	public List findAll() {
		String hql = " from TEnt";
		return super.getHibernateTemplate().find(hql);
	}

	public Page<TEnt> listEnt(int pageNo, int pageSize, String searchValue) {
		String hql = "select e from TEnt e " + "where e.entStatus='1' "
				+ "and (e.entName like ? or e.entCode like ?) "
				+ "order by e.entName";
		PageQuery<TEnt> pageQuery = new PageQuery<TEnt>(this);
		Page<TEnt> page = new Page<TEnt>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, "%" + searchValue + "%", "%"
				+ searchValue + "%");
	}

	public Page<TEnt> listEnt2(int pageNo, int pageSize, String searchValue) {
		String hql = "select e from TEnt e " + "where (e.entStatus='1' or e.entStatus='2')"
				+ "and (e.entName like ? or e.entCode like ?) "
				+ "order by e.entName";
		PageQuery<TEnt> pageQuery = new PageQuery<TEnt>(this);
		Page<TEnt> page = new Page<TEnt>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, "%" + searchValue + "%", "%"
				+ searchValue + "%");
	}
	
	public Page<TEnt> listEnt3(int pageNo, int pageSize, String searchValue) {
		String hql = " select e from TEnt e "
				+ " where (e.entStatus='1' or e.entStatus='2' or e.entStatus='0') "
				+ " and (e.entName like ? or e.entCode like ?) and e.version='2.0' "
				+ " order by e.entName ";
		PageQuery<TEnt> pageQuery = new PageQuery<TEnt>(this);
		Page<TEnt> page = new Page<TEnt>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, "%" + searchValue + "%", "%"
				+ searchValue + "%");
	}
	//企业列表+企业目标模板类型(周/旬/月)
	/*public Page<Object[]> listEntAndTemplateType(int pageNo, int pageSize, String searchValue) {
		String hql = " select e, k.value from TEnt e "
				+ " left join fetch e.Kpis k with k.states = 0 "
				+ " where (e.entStatus='1' or e.entStatus='2' or e.entStatus='0') "
				+ " and (e.entName like ? or e.entCode like ?) "
				+ " and (k.type = 0 or k.type is null) "
				+ " order by e.entName ";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, "%" + searchValue + "%", "%"
				+ searchValue + "%");
	}*/
	
}
