package com.sosgps.wzt.diarymarktj.dao.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.sosgps.wzt.diarymarktj.dao.DiaryMarkTjDao;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiaryMark;
import com.sosgps.wzt.orm.TDiaryMarkTjDAO;
import com.sosgps.wzt.util.PageQuery;

public class DiaryMarkTjHibernateDao extends TDiaryMarkTjDAO implements DiaryMarkTjDao {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Object[]> listDiaryMarkTj(String deviceIds,
			String entCode, Long userId, int startint, int limitint,
			String startDate, String endDate, String searchValue) {
		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
        try {
        	String sql = " select count(tt.device_id) as num  from t_terminal tt "
        		+ " left join (select * from t_diary_mark_tj tdmt "
        		+ " where tdmt.device_id in ("+deviceIds+") "
        		+ " and tdmt.ent_code = '"+entCode+"' "
        		+ " and tdmt.tj_date >= to_date('"+startDate+"','yyyy-MM-dd hh24:mi:ss') "
        		+ " and tdmt.tj_date <=to_date('"+endDate+"','yyyy-MM-dd hh24:mi:ss')) tab on tab.device_id = tt.device_id "
        		+ " left join ref_term_group rtg on rtg.device_id = tt.device_id "
        		+ " left join t_term_group ttg on ttg.id = rtg.group_id "
        		+ " where tt.device_id in ("+deviceIds+") ";
        	session = getHibernateTemplate().getSessionFactory().openSession();
            Page<Object[]> page = new Page<Object[]>();
            page.setPageNo(pageNo);
    		page.setPageSize(limitint);
            Query query = session.createSQLQuery(sql);
            list = query.list();
            Iterator iterator = list.iterator();
            if(iterator.hasNext()){
            	BigDecimal object = (BigDecimal)iterator.next();
                int count = object.intValue();
                page.setTotalCount(count);
            }else{
            	return null;
            }
        	sql = "select * from (select a.*, rownum row_num from ( "
        		+ " select tab.id,  to_char(tab.tj_date,'yyyy-MM-dd hh24:mi:ss'), "
        		+ " to_char(tab.diary_date,'yyyy-MM-dd') as diary_date, tab.arrival_rate, "
        		+ "  tt.device_id, tt.term_name, tt.ent_code, ttg.group_name,tt.simcard from t_terminal tt "
        		+ " left join (select * from t_diary_mark_tj tdmt "
        		+ " where tdmt.device_id in ("+deviceIds+") "
        		+ " and tdmt.ent_code = '"+entCode+"' "
        		+ " and tdmt.tj_date >= to_date('"+startDate+"','yyyy-MM-dd hh24:mi:ss') "
        		+ " and tdmt.tj_date <=to_date('"+endDate+"','yyyy-MM-dd hh24:mi:ss')) tab on tab.device_id = tt.device_id "
        		+ " left join ref_term_group rtg on rtg.device_id = tt.device_id "
        		+ " left join t_term_group ttg on ttg.id = rtg.group_id "
        		+ " where tt.device_id in ("+deviceIds+")) a) b  "
            	+ " where b.row_num between "+(startint+1)+" and "+(startint+limitint)+""
            	+ " order by b.device_id, b.diary_date ";
            query = session.createSQLQuery(sql);
            list = query.list();
            page.setResult(list);
            return page;
        } catch (RuntimeException e) {
            throw e;
        } finally {
            if(session != null) {
                session.clear();
                session.close();
                getHibernateTemplate().getSessionFactory().close();
            }
        }
	}
	
	public Page<Object[]> listDiaryTjDetail(String deviceId, String entCode, String diaryDate,
			int pageNo, int pageSize) {
		String hql = " select t,tt.termName from TDiaryMark t,TTerminal tt "
			+" where t.deviceId = ? "
			+" and t.entCode = ? "
			+" and t.diaryDate = to_date(?,'yyyy-MM-dd') "
			+" and t.deviceId = tt.deviceId "
			+" order by t.isArrive";
			
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		//return pageQuery.findByPage(page, hql, startDate, endDate,deviceId);
		return pageQuery.findByPage(page, hql, deviceId, entCode, diaryDate);
	}
}
