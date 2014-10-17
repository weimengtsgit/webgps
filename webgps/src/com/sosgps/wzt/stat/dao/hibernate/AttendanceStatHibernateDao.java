package com.sosgps.wzt.stat.dao.hibernate;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TAttendanceDAO;
import com.sosgps.wzt.stat.dao.AttendanceStatDao;

/**
 * @Title:签到签出统计dao接口hibernate实现类
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 下午12:59:18
 */
public class AttendanceStatHibernateDao extends TAttendanceDAO implements
	AttendanceStatDao {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Object[]> listAttendanceRecord(int startint, int limitint,
			String startDate, String endDate, String deviceIds) {
		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
        try {
        	String sql = "select count(*) " +
        			"from T_Attendance ta,T_Terminal tt,t_term_group ttg,ref_term_group rtg " +
        			" where tt.device_id = ta.device_id" +
        			" and rtg.device_id = tt.device_id" +
        			" and rtg.group_id = ttg.id" +
        			" and ta.attendance_date >= "+startDate+
        			" and ta.attendance_date <= "+endDate+
        			" and ta.device_id in ("+deviceIds+") and ta.delete_flag =0";
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
            
        	sql = "select * from (select a.*, rownum row_num from ( select tt.term_name, ta.attendance_date," +
        			"to_char(ta.signin_time, 'YYYY-MM-DD hh24:mi:ss') as signin_time," +
        			" ta.signin_desc," +
        			"to_char(ta.signoff_time, 'YYYY-MM-DD hh24:mi:ss') as signoff_time," +
        			" ta.signoff_desc," +
        			" ta.device_id," +
        			" to_char(ta.create_time ,'YYYY-MM-DD hh24:mi:ss') as create_time," +
        			" ttg.group_name, " +
        			" ta.signin_longitude, " +
        			" ta.signin_latitude, " +
        			" ta.signoff_longitude, " +
        			" ta.signoff_latitude " +
        			" from T_Attendance ta,T_Terminal tt,t_term_group ttg,ref_term_group rtg  " +
			" where tt.device_id = ta.device_id" +
			" and rtg.device_id = tt.device_id" +
			" and rtg.group_id = ttg.id" +
			" and ta.attendance_date >= "+startDate+
			" and ta.attendance_date <= "+endDate+
			" and ta.device_id in ("+deviceIds+") and ta.delete_flag =0" +
			" order by ta.id desc ) a) b  " + " where b.row_num between "+(startint+1)+" and "+(startint+limitint);
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

}
