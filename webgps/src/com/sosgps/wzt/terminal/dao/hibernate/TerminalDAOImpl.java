package com.sosgps.wzt.terminal.dao.hibernate;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ctc.wstx.util.DataUtil;
import com.sosgps.v21.util.DateUtils;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.ModuleParamConfig;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TTerminalDAO;
import com.sosgps.wzt.orm.TermParamConfig;
import com.sosgps.wzt.terminal.dao.TerminalDAO;
import com.sosgps.wzt.util.PageQuery;

public class TerminalDAOImpl extends TTerminalDAO implements TerminalDAO {
	/**
	 * find terminal by groupid
	 */
	public List findByGroupId(Long groupId) {
		return this.getHibernateTemplate().find(
				"from TTerminal t where t.TTargetGroup.groupId=" + groupId);
	}

	@SuppressWarnings("unchecked")
	public List<TTerminal> findTerminal(String deviceId, String entCode,
			boolean isVague) {
		Object[] value = { entCode, deviceId };
		if (isVague)
			return getHibernateTemplate()
					.find(" from TTerminal t where t.entCode=? and t.deviceId like ?",
							value);
		else {
			return getHibernateTemplate().find(
					" from TTerminal t where t.entCode=? and t.deviceId = ?",
					value);
		}
	}

	public List findTerminalByTermName(final String termName,
			final String entCode, boolean isVague) {
		Object[] value = { entCode, termName };
		if (isVague) {
			return getHibernateTemplate()
					.find(" from TTerminal t where t.entCode=? and t.termName like ?",
							value);
			/*
			 * return getHibernateTemplate().executeFind(new
			 * HibernateCallback(){
			 * 
			 * public Object doInHibernate(Session session) throws
			 * HibernateException, SQLException { List
			 * list=session.createSQLQuery
			 * ("select t.term_Name from t_terminal t where t.ent_code='"
			 * +entCode+"' and t.term_name like '"+termName+"'").list(); List
			 * tList=new ArrayList(); for(int i=0;i<list.size();i++){ String
			 * termName=(String)list.get(i); TTerminal t=new
			 * TTerminal(termName); tList.add(t); } return tList; }
			 * 
			 * });
			 */
		} else {
			return getHibernateTemplate().find(
					" from TTerminal t where t.entCode=? and t.termName = ?",
					value);
		}
	}

	public void delTTargetObjectById(Long id) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.wzt.terminal.dao.TerminalDAO#findAll()
	 */
	public List findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sosgps.wzt.terminal.dao.TerminalDAO#findNoGroupId(java.lang.Long)
	 */
	public List findNoGroupId(Long groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * delete target by id
	 * 
	 * @param id
	 */
	public void delTTargetObjectById(String id) {
		TTerminal tTargetObject = this.findById(id);
		this.delete(tTargetObject);
	}

	/**
	 * 
	 * 
	 * @param transientInstance
	 * @return
	 */
	public List findTermByTerminal(final TTerminal transientInstance) {
		return (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session s)
							throws HibernateException, SQLException {
						Query query = s
								.createQuery("select model from TTerminal model where model.entCode=:entCode and model.deviceId=:deviceId");
						query.setParameter("entCode",
								transientInstance.getEntCode());
						query.setParameter("deviceId",
								transientInstance.getDeviceId());
						return query.list();
					}
				});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sosgps.wzt.terminal.dao.TerminalDAO#update(com.sosgps.wzt.orm.TTerminal
	 * )
	 */
	public boolean update(TTerminal targetObject) {
		try {
			this.getHibernateTemplate().update(targetObject);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public TTerminal findTermById(String id) {
		TTerminal term = null;

		term = this.findById(id);
		return term;
	}

	public TTerminal findTermBySim(String sim) {
		TTerminal term = null;
		if (this.findBySimcard(sim).size() != 0) {
			term = (TTerminal) this.findBySimcard(sim).get(0);
		}

		return term;
	}

	public Page<Object[]> listTerminal(String entCode, Long userId, int pageNo,
			int pageSize, String searchValue) {
		String hql = "select t,g,tp,tv from TTerminal t "
				+ "left join t.refTermGroups ref "
				+ "left join t.vehicleMsg tv "
				+ "left join ref.TTermGroup g "
				+ "left join g.refUserTgroups ref2 "
				+ "left join t.TEntTermtype tp "
				+ "where t.entCode=? and ref2.id.userId=? "
				+ "and (t.termName like ? or t.simcard like ? or t.deviceId like ? or g.groupName like ?) "
				+ "order by t.termName";
		
		PageQuery<Object[]> pq = new PageQuery<Object[]>(this);
		Page<Object[]> re = new Page<Object[]>();
		re.setAutoCount(true);
		re.setPageNo(pageNo);
		re.setPageSize(pageSize);
		return pq.findByPage(re, hql, entCode, userId, "%" + searchValue + "%",
				"%" + searchValue + "%", "%" + searchValue + "%", "%"
						+ searchValue + "%");
	}

	public Long findTermNum(final String entCode) {
		Long l = (Long) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session s)
							throws HibernateException, SQLException {
						Query query = s
								.createQuery("select count(*) from TTerminal model where model.entCode=:entCode");
						query.setParameter("entCode", entCode);
						return (Long) query.uniqueResult();
					}
				});
		return l;
	}

	// add by wangzhen
	@SuppressWarnings("unchecked")
	public List<TTerminal> findAllTerminal(String entCode) {
		Object[] value = { entCode };
		return getHibernateTemplate().find(
				" from TTerminal t where t.entCode=? and t.expirationFlag=0",
				value);
	}

	@SuppressWarnings("unchecked")
	public List<String> getDeviceId(String entCode) {
		Object[] value = { entCode };
		return getHibernateTemplate()
				.find("select t.deviceId from TTerminal t where t.entCode=? and t.expirationFlag=0",
						value);
	}

	public TermParamConfig findTermParamConfigByDeviceIdAndType(
			String deviceId, Integer type) {
		Object[] value = { deviceId, type };
		List tc = getHibernateTemplate()
				.find("select t from TermParamConfig t where t.deviceId=? and t.type=?",
						value);
		if (tc == null || tc.size() == 0)
			return null;
		else
			return (TermParamConfig) tc.get(0);
	}

	public Integer updateTermConfig(TermParamConfig termPc) {
		int result = 1;
		try {
			getHibernateTemplate().update(termPc);
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}

	public Integer deleteTermParamConfig(String deviceIds) {
		int result = 1;
		try {
			List list = getHibernateTemplate().find(
					"from TermParamConfig tc where tc.deviceId in(" + deviceIds
							+ ")");
			getHibernateTemplate().deleteAll(list);
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	public int countTermParamConfig(String deviceIds) {
		try {
			StringBuffer hql = new StringBuffer();
			StringBuffer param = new StringBuffer();
			hql.append("select count(*) from TermParamConfig t where t.deviceId in("
					+ deviceIds + ")");

			List list = null;
			list = super.getHibernateTemplate().find(hql.toString());
			if (list == null || list.size() < 1)
				return 0;
			else
				return ((Number) list.get(0)).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("failed", e);
			return -1;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ModuleParamConfig> getAllModule() {
		try {
			final StringBuffer hql = new StringBuffer();
			hql.append("select t from ModuleParamConfig t where t.states = 0 ");

			return getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query = session.createQuery(hql.toString());
					return query.list();
				}
			});
		} catch (Exception e) {
			logger.error("failed", e);
			return null;
		}
	}

	public Integer saveTermParamConfig(TermParamConfig termPar0) {
		int result = 1;
		try {
			getHibernateTemplate().save(termPar0);
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}

	@Override
	public boolean updateEmployeeAttend(String deviceId, String termName,
			String simcard, String groupName, Long groupId) {
		try {
			Date endTime = new Date();
			Date startTime = DateUtils.add(endTime, -100);
			String hql = " update EmployeeAttend t set t.termName= ? ,t.simcard=?,t.groupName=?,t.groupId=? where t.deviceId = '"
					+ deviceId + "'" + " and t.createOn between ? and ?";
			Object[] values = { termName, simcard, groupName, groupId,
					startTime, endTime };
			getHibernateTemplate().bulkUpdate(hql, values);
		} catch (Exception e) {
			logger.error("updateEmployeeAttend:", e);
			return false;
		}
		return true;
	}

	public TTermGroup findTermGroupById(Long id) {
		try {
			TTermGroup instance = (TTermGroup) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TTermGroup", id);
			return instance;
		} catch (Exception re) {
			logger.error("findTermGroupById", re);
			return null;
		}
	}

	public Page<Object[]> listTerminalByDeviceId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String deviceIds) {
		String hql = "select t,g,tp,tv from TTerminal t left join t.refTermGroups ref left join t.vehicleMsg tv left join ref.TTermGroup g left join g.refUserTgroups ref2 left join t.TEntTermtype tp where t.entCode=? and ref2.id.userId=? and (t.termName like ? or t.simcard like ? or t.deviceId like ? or g.groupName like ?)  and t.deviceId in ("
				+ deviceIds + ") " + "order by t.termName";
		PageQuery pq = new PageQuery(this);
		Page re = new Page();
		re.setAutoCount(true);
		re.setPageNo(pageNo);
		re.setPageSize(pageSize);
		return pq.findByPage(re, hql, new Object[] { entCode, userId,
				"%" + searchValue + "%", "%" + searchValue + "%",
				"%" + searchValue + "%", "%" + searchValue + "%" });
	}
}
