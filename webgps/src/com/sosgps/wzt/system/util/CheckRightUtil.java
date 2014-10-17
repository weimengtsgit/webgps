/**
 * <p>Title:Toten</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: WinChannel </p>
 * <p>Date:Jul 7, 2006</p>
 * @author bxz
 * @version 1.0
 */
package com.sosgps.wzt.system.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.sos.helper.SpringHelper;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TModuleGroup;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.ConnectService;


public class CheckRightUtil {
//private static	Log log = LogUtil.getLoger(CheckRightUtil.class);

    private static final Logger logger = Logger
            .getLogger(CheckRightUtil.class);
	private  ConnectService  connectService = (ConnectService)SpringHelper.getBean("connectService");;
	/**
	 * 判断用户是否具有roleCode指定的角色。
	 * @param user
	 * @param roleCode
	 * @return
	 */
	public static boolean hasRight(UserInfo user,String roleCode){
		return user.getRoleCode().equalsIgnoreCase(roleCode);
		
    }

    public List queryTopModuleGroup(Long userId) {
        List list = null;
        Session session = null;
        List resultList = new ArrayList();
        try {
            SessionFactory sessionFactory = connectService.getConnectDao().getHibernateTemplate()
                    .getSessionFactory();
            session = sessionFactory.openSession();
            StringBuffer sql = new StringBuffer();
            sql.append("select  distinct g2.id as id,g2.groupName as groupName from TModuleGroup mg");
            sql.append(" ,TModule module,RefModuleRole mr,TRole r,RefUserRole rf,TUser u,TModuleGroup g2 where  module.TModuleGroup.id=mg.id");
            sql.append("  and module.id=mr.TModule.id");
            sql.append("  and r.id=mr.TRole.id ");
            sql.append("  and r.id=rf.TRole.id");
            sql.append("  and u.id=rf.TUser.id");
            sql.append("  and mg.parentId=g2.id");
            sql.append(" and u.id='").append(userId).append("'")
                    .append(" and module.usageFlag='1'");
            logger.info(sql.toString());
            Query query = session.createQuery(sql.toString());
            list = query.list();
            for (Iterator it = list.iterator(); it.hasNext();) {
                Object[] o = (Object[]) it.next();
                Long groupId = (Long) o[0];
                String groupName = (String) o[1];
                TModuleGroup group = new TModuleGroup(groupId, groupName);
                resultList.add(group);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
                connectService.getConnectDao().getHibernateTemplate().getSessionFactory().close();
            }
        }
        return resultList;
    }
	    
	    public  List queryChildModuleGroup(Long topGroupId,Long userId){
	        List resultList = null;
	        Connection conn = null;
	        Statement st = null;
	        ResultSet rs = null;
			try {
				conn = ( (DataSource)SpringHelper.getBean("dataSource")).getConnection();
				StringBuffer sql = new StringBuffer();
				sql.append( "select distinct id,groupName");
				sql.append(" from (");
				sql.append("  select  mg.id as id,mg.group_Name as groupName");
				sql.append("  from T_Module_Group mg,T_Module module,Ref_Module_Role mr,T_Role r,Ref_User_Role rf,T_User u ");
				sql.append("  where  module.module_id=mg.id");
				sql.append("  and module.id=mr.module_id");
				sql.append(" and r.id=mr.role_id ");
				sql.append(" and r.id=rf.role_id");
				sql.append(" and u.id=rf.user_id");
				sql.append("  and mg.parent_Id='").append(topGroupId).append("'");
				sql.append(" and u.id='").append(userId).append("'").append(" and module.usage_Flag='1'");
				sql.append(" order by mr.module_order )");
				resultList = new ArrayList();
				st = conn.createStatement();
				rs = st.executeQuery(sql.toString());
				while(rs.next()){
					 String groupId = rs.getString(1);
					    String groupName=rs.getString(2);
					    TModuleGroup group = new TModuleGroup(Long.valueOf(groupId),groupName);  
					    resultList.add(group);
				}
			} catch (HibernateException e) {
				SysLogger.sysLogger.error("E067: CheckRightUtil-queryChildModuleGroup："+e.getMessage());
			} catch (SQLException e) {
				SysLogger.sysLogger.error("E067: CheckRightUtil-queryChildModuleGroup："+e.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (st != null) {
						st.close();
					}
					conn.close();
				} catch (HibernateException e) {
					SysLogger.sysLogger.error(e.getMessage());
				} catch (SQLException e) {
					SysLogger.sysLogger.error(e.getMessage());
				}
			}
	        return resultList;
	        
	    }
	    
	    public  List queryModuleList(Long groupId,Long userId){
	    	List resultList = null;
	        Connection conn = null;
	        Statement st = null;
	        ResultSet rs = null;
			try {
				conn = ( (DataSource)SpringHelper.getBean("dataSource")).getConnection();StringBuffer sql = new StringBuffer();
				sql.append("  select  distinct module.id as id,module.module_Name as moduleName,module.module_Code as moduleCode,");
				sql.append("  module.module_Path as modulePath,module.module_Type as moduleType,mr.module_order ");
				sql.append(" from  T_Module module,Ref_Module_Role mr,T_Role r,Ref_User_Role rf,T_User u");
				sql.append("  where  module.module_id='").append(groupId).append("'");
				sql.append("  and module.id=mr.module_id");
				sql.append(" and r.id=mr.role_id");
				sql.append(" and r.id=rf.role_id");
				sql.append(" and u.id=rf.user_id");
				sql.append(" and u.id='").append(userId).append("'").append(" and module.usage_Flag='1'");
				sql.append(" order by mr.module_order");
				resultList = new ArrayList();
				st = conn.createStatement();
				rs = st.executeQuery(sql.toString());
				while(rs.next()){
					Long moduleId = rs.getLong(1);
		            String moduleName=rs.getString(2);
		            String moduleCode = rs.getString(3);
		            String modulePath = rs.getString(4);
		            String moduleType = rs.getString(5);
		            TModule module = new TModule();
		            module.setId(moduleId);
		            module.setModuleName(moduleName);
		            module.setModuleCode(moduleCode);
		            module.setModulePath(modulePath);
		            module.setModuleType(moduleType);
		            resultList.add(module);
				}
			} catch (HibernateException e) {
				SysLogger.sysLogger.error("E067: CheckRightUtil-queryModuleList: "+e.getMessage());
			} catch (SQLException e) {
				SysLogger.sysLogger.error("E067: CheckRightUtil-queryModuleList: "+e.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (st != null) {
						st.close();
					}
					conn.close();
				} catch (HibernateException e) {
					SysLogger.sysLogger.error("E067: CheckRightUtil-queryModuleList: "+e.getMessage());
				} catch (SQLException e) {
					SysLogger.sysLogger.error("E067: CheckRightUtil-queryModuleList: "+e.getMessage());
				}
			}
	       
	        return resultList;
	    }
	    
	    public static void main(String[] agrs){
	       // CheckRightUtil.queryTopModuleGroup();
	    }	    
	}


