/**
 * 
 */
package com.sosgps.wzt.system.dao.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.sos.helper.SpringHelper;
import org.hibernate.Session;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.orm.TempShortMessage;
import com.sosgps.wzt.orm.TempShortMessageDAO;
import com.sosgps.wzt.system.dao.TempShortMessageDao;

/**
 * @author xiaojun.luan
 *
 */
public class TempShortMessageDaoImpl extends TempShortMessageDAO implements TempShortMessageDao {

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.system.dao.TempShortMessageDao#deleteMessageByPhoneNum(java.lang.String)
	 */
	public void deleteMessageByPhoneNum(String empCode,String phoneNum,int type) {
		// TODO Auto-generated method stub
		List list=this.findByPhoneNumber(empCode,phoneNum,type);
		if(list!=null &&list.size()>0){
			for(int i=0;i<list.size();i++){
				TempShortMessage temp=(TempShortMessage)list.get(i);
				this.delete(temp);
			}
		}
	}

	public boolean deleteAll(){
		boolean ret = true;
		Connection conn = null;
		String sql = null;
		Statement st = null;
		try{
			conn = ( (DataSource)SpringHelper.getBean("dataSource")).getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();			
				
				sql = "delete from Temp_Short_Message t ";
				st.execute(sql);		
			conn.commit();
			conn.setAutoCommit(true);
			SysLogger.sysLogger.info("I063: ¶¯Ì¬¶ÌÐÅ-É¾³ýÁÙÊ±¶¯Ì¬¶ÌÐÅ ");
		}catch(SQLException se){
			try{
				conn.rollback();
			}catch(SQLException e){
				ret = false;
				SysLogger.sysLogger.error("E063: ¶¯Ì¬¶ÌÐÅ-É¾³ýÁÙÊ±¶¯Ì¬¶ÌÐÅÊ§°Ü",e);
				
			}
			SysLogger.sysLogger.error("E063: ¶¯Ì¬¶ÌÐÅ-É¾³ýÁÙÊ±¶¯Ì¬¶ÌÐÅ",se);
			ret = false;
		}catch(Exception ex){
			ret = false;
			SysLogger.sysLogger.error("E063: ¶¯Ì¬¶ÌÐÅ-É¾³ýÁÙÊ±¶¯Ì¬¶ÌÐÅ",ex);
		}finally{
			if(st != null){
				try{
					st.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E063: ¶¯Ì¬¶ÌÐÅ-É¾³ýÁÙÊ±¶¯Ì¬¶ÌÐÅÊ§°Ü-¹Ø±ÕStatement Ê§°Ü",e);
				}		
			}
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E063: ¶¯Ì¬¶ÌÐÅ-É¾³ýÁÙÊ±¶¯Ì¬¶ÌÐÅÊ§°Ü-¹Ø±ÕConnection Ê§°Ü",e);
				}
				
			}
		}
		return ret;
	}
	
	public List findByPhoneNumber(String empCode,String phoneNumber, int type) {
		try {
			String queryString = "from TempShortMessage as model where model.phoneNumber='"
					+ phoneNumber + "' and model.type= "+type+" and model.empCode='"+empCode+"'";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
}
