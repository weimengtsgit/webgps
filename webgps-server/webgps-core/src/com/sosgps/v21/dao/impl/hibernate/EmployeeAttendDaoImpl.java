package com.sosgps.v21.dao.impl.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sosgps.v21.dao.EmployeeAttendDao;
import com.sosgps.v21.model.EmployeeAttend;
import com.sosgps.v21.model.TravelCost;

public class EmployeeAttendDaoImpl extends BaseHibernateDao implements EmployeeAttendDao {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeAttendDaoImpl.class);
	
    @SuppressWarnings("unchecked")
    public List<EmployeeAttend> findEmployeeAttendByCondition(final String entCode,final String deviceIds,
            final String startAttendDate,final String endAttendDate) {
        StringBuilder sb = new StringBuilder();
        sb.append(" from EmployeeAttend where deleteFlag = 0");
        if (deviceIds != null) {
            sb.append(" and deviceId in ("+ deviceIds + ")");
        }
        sb.append(" and entCode = ?");
        sb.append(" and attendanceDate between "+startAttendDate+" and "+endAttendDate);
        sb.append(" order by createOn desc, groupName, deviceId ");
        final String sql = sb.toString();
        try {
            return getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    Query query = session.createQuery(sql);
                    query.setString(0,entCode);
                    return query.list();
                }
            });
        } catch (Exception e) {
            logger.error("failed", e);
            return null;
        }
    }

    /**
     * ͨ�������ղ�ѯ����Ӧ�Ŀ���״̬
     */
    public EmployeeAttend queryEmployeeAttendByCondition(final Integer attendStates, final String deviceId,
            final String entCode) {
        try {
            return (EmployeeAttend) getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    StringBuilder sb = new StringBuilder();
                    sb.append(" from EmployeeAttend empa where deleteFlag = 0");
                    
                    sb.append(" empa.attendanceDate=? ");
                    sb.append(" and empa.deviceId=? ");
                    sb.append(" and empa.entCode=? ");
                    
                    Query query = session.createQuery(sb.toString());
                    query.setInteger(0, attendStates);
                    query.setString(1, deviceId);
                    query.setString(2,entCode);
                    
                    return query.list();
                }
            });
        } catch (Exception e) {
            logger.error("failed", e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<EmployeeAttend> findEmployeeAttendByParam(final String entCode, final String deviceId,
            final Integer attendanceDate, final int attendStates) {
        StringBuilder sb = new StringBuilder();
        sb.append("from EmployeeAttend e where e.id = (select max(empa.id) ");
        sb.append(" from EmployeeAttend empa where deleteFlag = 0 and");
        sb.append(" empa.attendanceDate<=? ");
        sb.append(" and empa.deviceId=? ");
        sb.append(" and empa.entCode=? ");
        sb.append(" and empa.attendStates=4 )");
        final String sql = sb.toString();
        try {
            return  getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    Query query = session.createQuery(sql);
                    query.setInteger(0, attendStates);
                    query.setString(1, deviceId);
                    query.setString(2,entCode);
                    
                    return query.list();
                }
            });
        } catch (Exception e) {
            logger.error("failed", e);
            return null;
        }
    }


    public List<Object[]> findCompanyAttendData(String entCode, String deviceIds,
            Integer attendanceDateI, String utcStartDate, String utcEndDate,Integer atteStartDateI,Integer atteEndDateI) {
        Session session = null;
        List<Object[]> list = null;
        try {
            /*String sql = "select nvl(target_base0.mans,0) as attend0,nvl(target_base1.mans,0) as attend1,"
                    +"nvl(target_base3.mans,0) as attend3,nvl(target_base4.mans,0) as attend4,"
                    +"nvl(target_base5.mans,0) as attend5, nvl(target_baseT.total, 0) as total,"
                    +"base.arrive_time from "
                    +"(select to_char(mapsearch_utc_to_date((trunc("
                    +utcStartDate
                    +"/(24*60*60*1000))+"
                    +"rownum)*(24*60*60*1000)),"
                    +"'yyyymmdd') as arrive_time"
                    +" from dual"
                    +" connect by rownum<=(trunc(("
                    +utcEndDate
                    +"-"
                    +utcStartDate
                    +")/"
                    +"(24*60*60*1000))+1)) base,"
                    +"(select count(*) as mans,"
                    +" emp.attendance_date as attenddate"
                    +" from t_employee_attend emp"
                    +" where emp.attend_states=0 and emp.ent_code ='" +entCode+"'"
                    +" and emp.delete_flag=0"
                    +" and emp.attendance_date between "+atteStartDateI+" and "+atteEndDateI
                    +" and emp.device_id in (" +deviceIds+")"
                    +" GROUP BY emp.attendance_date) target_base0,"
                    +" (select count(*) as mans,"
                    +" emp.attendance_date as attenddate"
                    +" from t_employee_attend emp"
                    +" where emp.attend_states=1 and emp.ent_code ='" +entCode+"'"
                    +" and emp.delete_flag=0"
                    +" and emp.attendance_date between "+atteStartDateI+" and "+atteEndDateI
                    +" and emp.device_id in(" +deviceIds+")"
                    +" GROUP BY emp.attendance_date) target_base1,"
                    +" (select count(*) as mans,"
                    +" emp.attendance_date attenddate"
                    +" from t_employee_attend emp"
                    +" where emp.attend_states=3 and emp.ent_code ='" +entCode+"'"
                    +" and emp.delete_flag=0"
                    +" and emp.attendance_date between "+atteStartDateI+" and "+atteEndDateI
                    +" and emp.device_id in(" +deviceIds+")"
                    +" GROUP BY emp.attendance_date) target_base3,"
                    +" (select count(*) as mans,"
                    +" emp.attendance_date attenddate"
                    +" from t_employee_attend emp"
                    +" where emp.attend_states=4 and emp.ent_code ='" +entCode+"'"
                    +" and emp.delete_flag=0"
                    +" and emp.attendance_date between "+atteStartDateI+" and "+atteEndDateI
                    +" and emp.device_id in(" +deviceIds+")"
                    +" GROUP BY emp.attendance_date) target_base4,"
                    +" (select count(*) as mans,"
                    +" emp.attendance_date attenddate"
                    +" from t_employee_attend emp"
                    +" where emp.attend_states=5 and emp.ent_code ='" +entCode+"'"
                    +" and emp.delete_flag=0"
                    +" and emp.attendance_date between "+atteStartDateI+" and "+atteEndDateI
                    +" and emp.device_id in(" +deviceIds+")"
                    +" GROUP BY emp.attendance_date) target_base5,"
                    +" (select count(*) as total,"
                    +" emp.attendance_date attenddate"
                    +" from t_employee_attend emp"
                    +" where emp.ent_code ='" +entCode+"'"
                    +" and emp.delete_flag=0"
                    +" and emp.attendance_date between "+atteStartDateI+" and "+atteEndDateI
                    +" and emp.device_id in(" +deviceIds+")"
                    +" GROUP BY emp.attendance_date) target_baseT"
                    +" where base.arrive_time=target_base0.attenddate(+)" 
                    +"  and   base.arrive_time=target_base1.attenddate(+)"
                    + " and   base.arrive_time=target_base3.attenddate(+)"
                    +"  and   base.arrive_time=target_base4.attenddate(+)"
                    +"  and   base.arrive_time=target_base5.attenddate(+)"
                    +"  and   base.arrive_time=target_baseT.attenddate(+)"
                    +" order by base.arrive_time";*/
            String sql = "select nvl(target_base0.mans,0) as attend0,nvl(target_base1.mans,0) as attend1,"
                    +"nvl(target_base3.mans,0) as attend3,nvl(target_base4.mans,0) as attend4,"
                    +"nvl(target_base5.mans,0) as attend5,"
                    +"base.arrive_time from "
                    +"(select to_char(mapsearch_utc_to_date((trunc("
                    +utcStartDate
                    +"/(24*60*60*1000))+"
                    +"rownum)*(24*60*60*1000)),"
                    +"'yyyymmdd') as arrive_time"
                    +" from dual"
                    +" connect by rownum<=(trunc(("
                    +utcEndDate
                    +"-"
                    +utcStartDate
                    +")/"
                    +"(24*60*60*1000))+1)) base,"
                    +"(select count(*) as mans,"
                    +" emp.attendance_date as attenddate"
                    +" from t_employee_attend emp"
                    +" where emp.attend_states=0 and emp.ent_code ='" +entCode+"'"
                    +" and emp.delete_flag=0"
                    +" and emp.attendance_date between "+atteStartDateI+" and "+atteEndDateI
                    +" and emp.device_id in (" +deviceIds+")"
                    +" GROUP BY emp.attendance_date) target_base0,"
                    +" (select count(*) as mans,"
                    +" emp.attendance_date as attenddate"
                    +" from t_employee_attend emp"
                    +" where emp.attend_states=1 and emp.ent_code ='" +entCode+"'"
                    +" and emp.delete_flag=0"
                    +" and emp.attendance_date between "+atteStartDateI+" and "+atteEndDateI
                    +" and emp.device_id in(" +deviceIds+")"
                    +" GROUP BY emp.attendance_date) target_base1,"
                    +" (select count(*) as mans,"
                    +" emp.attendance_date attenddate"
                    +" from t_employee_attend emp"
                    +" where emp.attend_states=3 and emp.ent_code ='" +entCode+"'"
                    +" and emp.delete_flag=0"
                    +" and emp.attendance_date between "+atteStartDateI+" and "+atteEndDateI
                    +" and emp.device_id in(" +deviceIds+")"
                    +" GROUP BY emp.attendance_date) target_base3,"
                    +" (select count(*) as mans,"
                    +" emp.attendance_date attenddate"
                    +" from t_employee_attend emp"
                    +" where emp.attend_states=4 and emp.ent_code ='" +entCode+"'"
                    +" and emp.delete_flag=0"
                    +" and emp.attendance_date between "+atteStartDateI+" and "+atteEndDateI
                    +" and emp.device_id in(" +deviceIds+")"
                    +" GROUP BY emp.attendance_date) target_base4,"
                    +" (select count(*) as mans,"
                    +" emp.attendance_date attenddate"
                    +" from t_employee_attend emp"
                    +" where emp.attend_states=5 and emp.ent_code ='" +entCode+"'"
                    +" and emp.delete_flag=0"
                    +" and emp.attendance_date between "+atteStartDateI+" and "+atteEndDateI
                    +" and emp.device_id in(" +deviceIds+")"
                    +" GROUP BY emp.attendance_date) target_base5"
                    /*+" (select count(*) as total,"
                    +" emp.attendance_date attenddate"
                    +" from t_employee_attend emp"
                    +" where emp.ent_code ='" +entCode+"'"
                    +" and emp.delete_flag=0"
                    +" and emp.attendance_date between "+atteStartDateI+" and "+atteEndDateI
                    +" and emp.device_id in(" +deviceIds+")"
                    +" GROUP BY emp.attendance_date) target_baseT"*/
                    +" where base.arrive_time=target_base0.attenddate(+)" 
                    +"  and   base.arrive_time=target_base1.attenddate(+)"
                    + " and   base.arrive_time=target_base3.attenddate(+)"
                    +"  and   base.arrive_time=target_base4.attenddate(+)"
                    +"  and   base.arrive_time=target_base5.attenddate(+)"
                    //+"  and   base.arrive_time=target_baseT.attenddate(+)"
                    +" order by base.arrive_time";
            System.out.println(sql);
            session = getHibernateTemplate().getSessionFactory().openSession();
            Query query = session.createSQLQuery(sql);
            list = query.list();
            return list;
        } catch (RuntimeException e) {
            throw e;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
                getHibernateTemplate().getSessionFactory().close();
            }
        }
    }

    public EmployeeAttend getEmployeeAttendById(Long empAtteId) {
        return (EmployeeAttend) this.findById(empAtteId, EmployeeAttend.class);
    }

    public EmployeeAttend getEmployeeAttendByDeviceIdAndDateAndStates(final String entCode, final String deviceId,
            final Integer attendDate, final Integer states) {
        EmployeeAttend  empAtte = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(" from EmployeeAttend empa where deleteFlag = 0");
            sb.append(" and empa.attendanceDate=? ");
            sb.append(" and empa.deviceId=? ");
            sb.append(" and empa.entCode=? ");
            sb.append(" and empa.attendStates=? ");
            
            Object[] objArr = {attendDate,deviceId,entCode,states};
            List list = getHibernateTemplate().find(sb.toString(), objArr);
            if(list != null && list.size() > 0 ) {
                empAtte = (EmployeeAttend)list.get(0);
                return empAtte;
            } else {
                return empAtte;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return empAtte;
        }
    }
}
