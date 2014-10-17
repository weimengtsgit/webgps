package com.sosgps.wzt.struction.dao.hibernate;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.sos.helper.SpringHelper;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TStructions;
import com.sosgps.wzt.orm.TStructionsDAO;
import com.sosgps.wzt.struction.dao.StructionDAO;

/**
 * <p>
 * Title:StructionDAOImpl.java
 * </p>
 */
public class StructionDAOImpl extends TStructionsDAO implements StructionDAO {

	public TStructions findByDeviceId(final String deviceId) {
		return (TStructions) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session s)
							throws HibernateException, SQLException {
						Query query = s
								.createQuery("select model from TStructions model where model.state='2' and model.TTerminal.deviceId=:deviceId order by model.createdate desc");
						query.setParameter("deviceId", deviceId);
						query.setFirstResult(0);
						query.setMaxResults(1);
						return query.uniqueResult();
					}
				});
	}
	public long getCurrentSequence() {
		long seq = 0;

		Connection conn = null;
		String sql = "SELECT SEQ_INSTRUCTION.NEXTVAL as seq FROM DUAL";
		Statement st = null;
		ResultSet rs = null;

		try {
			conn = ( (DataSource)SpringHelper.getBean("dataSource")).getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				seq = rs.getLong("seq");
			}
			conn.commit();
			conn.setAutoCommit(true);
			SysLogger.sysLogger.info("指令-查询指令序列号成功 ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					SysLogger.sysLogger.error("指令---获取当前序列号，关闭ResultSet 失败");
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {

					SysLogger.sysLogger.error("删除指令失败-关闭Statement 失败", e);
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

					SysLogger.sysLogger.error("删除指令失败-关闭Connection 失败", e);
				}
			}
		}

		return seq;
	}
	/**
	 * 指令信息统计
	 */
	public Page<Object[]> listStructionsRecord(int startint, int limitint,
			String startDate, String endDate, String deviceIds, String type) {
		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
        try {
        	String sql = "select count(*) from t_structions ta,t_terminal tt " +
        			" where tt.device_id = ta.device_id and ta.createdate between to_date('"+startDate+"','yyyy-mm-dd') "+
        			" and to_date('"+endDate+"','yyyy-mm-dd') "+
        			" and ta.device_id in ("+deviceIds+") " +
        			" and ta.type like '%"+type+"%' ";
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
        	sql = "select * from (select a.*, rownum row_num from ( select tt.term_name, " +
        			" ta.device_id," +
        			" ta.instruction," +
        			" ta.state," +
        			" ta.type," +
        			" ta.param," +
        			" to_char(ta.createdate ,'YYYY-MM-DD hh24:mi:ss') as create_time" +
        			" from t_structions ta,t_terminal tt " +
        			" where tt.device_id = ta.device_id and ta.createdate between to_date('"+startDate+"','yyyy-mm-dd') "+
        			" and to_date('"+endDate+"','yyyy-mm-dd') "+
        			" and ta.device_id in ("+deviceIds+") " +
        			" and ta.type like '%"+type+"%' "+
        			" order by ta.createdate desc ) a) b  "
        			+ " where b.row_num between "+(startint+1)+" and "+(startint+limitint);
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
