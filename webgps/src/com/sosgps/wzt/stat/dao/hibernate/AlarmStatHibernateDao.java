package com.sosgps.wzt.stat.dao.hibernate;

import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TAlarmShow;
import com.sosgps.wzt.orm.TAlarmShowDAO;
import com.sosgps.wzt.stat.dao.AlarmStatDao;
import com.sosgps.wzt.util.PageQuery;

/**
 * @Title:报警统计数据层接口具体实现类
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2010-4-3 上午10:16:30
 */
public class AlarmStatHibernateDao extends TAlarmShowDAO implements
		AlarmStatDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(AlarmStatHibernateDao.class);

	public Page<Object[]> listAlarmsByToday(String entCode, Long userId,
			int pageNo, int pageSize) {
		String hql = "select tAlarmShow.id,tTerminal,tAreaLocrecord ";
		hql += " from TAlarmShow tAlarmShow,TAreaLocrecord tAreaLocrecord,TTerminal tTerminal,RefUserTgroup refUserTgroup,TTermGroup tTermGroup,RefTermGroup refTermGroup";
		hql += " where tAlarmShow.flag='1' and tTerminal.deviceId=tAlarmShow.deviceId ";
		hql += " and tAlarmShow.alarmId=tAreaLocrecord.id and refUserTgroup.id.TTermGroup.id = tTermGroup.id ";
		hql += " and tTermGroup.id = refTermGroup.id.TTermGroup.id";
		hql += " and refTermGroup.id.TTerminal.deviceId = tTerminal.deviceId and refUserTgroup.id.userId = "
				+ userId;
		hql += " and tTerminal.entCode = '" + entCode
				+ "' and tAreaLocrecord.jmx is not null ";
		hql += " and tAlarmShow.alarmDate between trunc(CURRENT_DATE)";
		hql += " and trunc(CURRENT_DATE+1)";
		hql += " order by tAlarmShow.alarmDate desc";
		PageQuery<Object[]> pq = new PageQuery<Object[]>(this);
		Page<Object[]> re = new Page<Object[]>();
		re.setAutoCount(true);
		re.setPageNo(pageNo);
		re.setPageSize(pageSize);
		return pq.findByPage(re, hql);
	}

	public boolean removeAlarm(Long id) {
		TAlarmShow alarmShow = findById(id);
		if (alarmShow != null) {
			alarmShow.setFlag("0");
			attachDirty(alarmShow);
			return true;
		}
		return false;
	}

	public Page<Object[]> listAlarm(String entCode, Long userId, int pageNo,
			int pageSize, int alarmType, Date startTime, Date endTime,
			String searchValue, String deviceIds) {
		// alarmType报警类型:1 超速报警2 区域报警3 主动报警4紧急报警6偏航报警
		String hql = "select a,t " + "from TAreaLocrecord a,TTerminal t "
				+ "left join t.refTermGroups reftg "
				+ "left join reftg.TTermGroup g "
				+ "left join g.refUserTgroups refug "
				+ "where a.deviceId=t.deviceId and a.alarmType=? "
				+ "and refug.id.userId=? "
				+ "and t.entCode=? and a.jmx is not null "
				+ "and a.alarmTime between ? and ? "
				+ "and t.vehicleNumber like ? " 
				+ "and a.deviceId in (" + deviceIds + ") "
				+ "order by t.vehicleNumber, a.alarmTime desc";

		PageQuery<Object[]> pq = new PageQuery<Object[]>(this);
		Page<Object[]> re = new Page<Object[]>();
		re.setAutoCount(true);
		re.setPageNo(pageNo);
		re.setPageSize(pageSize);
		return pq.findByPage(re, hql, String.valueOf(alarmType), userId,
				entCode, startTime, endTime, "%" + searchValue + "%");
	}

	public Page<Object[]> listAllAlarm(String entCode, Long userId, int pageNo,
			int pageSize, Date startTime, Date endTime, String searchValue) {
		String hql = "select a,t " + "from TAreaLocrecord a,TTerminal t "
				+ "left join t.refTermGroups reftg "
				+ "left join reftg.TTermGroup g "
				+ "left join g.refUserTgroups refug "
				+ "where a.deviceId=t.deviceId " + "and refug.id.userId=? "
				+ "and t.entCode=? and a.jmx is not null "
				//+ "and a.alarmTime between ? and ? "
				+ "and inputdate between ? and ? "
				+ "and t.vehicleNumber like ? " + "order by a.alarmTime desc";

		PageQuery<Object[]> pq = new PageQuery<Object[]>(this);
		Page<Object[]> re = new Page<Object[]>();
		re.setAutoCount(true);
		re.setPageNo(pageNo);
		re.setPageSize(pageSize);
		return pq.findByPage(re, hql, userId, entCode, startTime, endTime, "%"
				+ searchValue + "%");
	}
	
	public boolean removeAllAlarm(final String empCode) {
		try {
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session s)
						throws HibernateException, SQLException {
					Query query = s.createQuery("update TAlarmShow tas set tas.flag = 0 where tas.deviceId in "
							+"(select t.deviceId from TTerminal t where t.entCode = '"+empCode+"')");
					//query.setParameter("ids", ids);
					query.executeUpdate();
					return true;
				}
			});
		} catch (RuntimeException re) {
			logger.error(re);
			return false;
		}
		return true;
	}
}
