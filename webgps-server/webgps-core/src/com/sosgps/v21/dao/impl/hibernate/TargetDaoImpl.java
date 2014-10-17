package com.sosgps.v21.dao.impl.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sosgps.v21.dao.TargetDao;
import com.sosgps.v21.model.Kpi;
import com.sosgps.v21.model.TargetTemplate;
import com.sosgps.wzt.orm.TTerminal;

public class TargetDaoImpl extends HibernateDaoSupport implements TargetDao {

	private static final Logger logger = LoggerFactory
			.getLogger(TargetDaoImpl.class);
	/**
	 * 取目标维护值.type=0-目标维护模板类型,1-签单额达成率,2-回款额达成率,3-费用额使用率,4-员工出访达成率,5-客户拜访覆盖率
	 */
	public List<Kpi> getKpi(String entCode, int type) {
		try {
			String hql = "select t from Kpi t where t.states=0 and t.entCode=? and t.type=?";
			return super.getHibernateTemplate().find(hql,
					new Object[] { entCode, type });
		} catch (Exception e) {
			logger.error("[TargetDaoImpl-getKpi] error:", e);
			return null;
		}
	}
	
	/**
	 * 取目标维护值.type=0-目标维护模板类型,1-签单额达成率,2-回款额达成率,3-费用额使用率,4-员工出访达成率,5-客户拜访覆盖率
	 */
	public List<Kpi> getKpi(String entCode, String type) {
		try {
			String hql = "select t from Kpi t where t.states=0 and t.entCode=? and t.type in ("
					+ type + ")";
			return super.getHibernateTemplate().find(hql,
					new Object[] { entCode });
		} catch (Exception e) {
			logger.error("failed", e);
			return null;
		}
	}

	public void addKpi(Kpi kpi) {
		try {
			super.getHibernateTemplate().save(kpi);
		} catch (Exception e) {
			logger.error("failed", e);
		}
	}

	public void updateKpi(Kpi kpi) {
		try {
			String hql = "update Kpi t set t.key=?,t.value=?,t.lastUpdateOn=?,t.lastUpdateBy=? where t.id=?";
			super.getHibernateTemplate().bulkUpdate(
					hql,
					new Object[] { kpi.getKey(), kpi.getValue(),
							kpi.getLastUpdateOn(), kpi.getLastUpdateBy(),
							kpi.getId() });
		} catch (Exception e) {
			logger.error("failed", e);
		}
	}
    
	//modify by wangzhen 员工排序
	public List getTerminalAndGroup(String entCode) {
		try {
			String hql = "select t from TTerminal t "
					+ "left join fetch t.refTermGroups ref "
					//modify by wangzhen start 2012-09-12
//					+ "where t.entCode = ? and t.expirationFlag=0 order by t.vehicleNumber,ref.TTermGroup.id,t.deviceId";
			        + "where t.entCode = ? and t.expirationFlag=0 order by t.termName,ref.TTermGroup.id,t.deviceId";
			        //modify by wangzhen end 2012-09-12
			return super.getHibernateTemplate().find(hql,
					new Object[] { entCode });
		} catch (Exception e) {
			logger.error("failed", e);
			return null;
		}
	}

	public void addTarget(final List<TargetTemplate> targetList) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				int count = 1;
				Transaction tx = session.beginTransaction();
				StringBuffer sb = new StringBuffer();
				//modify by wangzhen 将vehicleNumber_terminal_name
				sb
						.append("insert into t_target_template (id, type, device_id, visit_amount, cash_amount, bill_amount, cost_amount, year, target_on, group_id, group_name, terminal_name, ent_code, states, createon, createby,cus_visit_amount) values (seq_t_target_template.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				try {
					Query query = session.createSQLQuery(sb.toString());
					for (TargetTemplate target : targetList) {
						query.setParameter(0, target.getType());
						query.setParameter(1, target.getDeviceId());
						query.setParameter(2, target.getVisitAmount());
						query.setParameter(3, target.getCashAmount());
						query.setParameter(4, target.getBillAmount());
						query.setParameter(5, target.getCostAmount());
						query.setParameter(6, target.getYear());
						query.setParameter(7, target.getTargetOn());
						query.setParameter(8, target.getGroupId());
						query.setParameter(9, target.getGroupName());
						//modify by wangzhen 2012-09-12 start
//						query.setParameter(10, target.getVehicleNumber());
						query.setParameter(10, target.getTerminalName());
						//modify by wangzhen 2012-09-12 end
						query.setParameter(11, target.getEntCode());
						query.setParameter(12, target.getStates());
						query.setParameter(13, target.getCreateOn());
						query.setParameter(14, target.getCreateBy());
						query.setParameter(15, target.getCusVisitAmount());
						query.executeUpdate();
						if (count++ % 50 == 0) {
							session.flush();
						}
					}
				} catch (Exception e) {
					logger.error("failed", e);
					tx.rollback();
				}
				tx.commit();
				return null;
			}
		});
	}

	//add by wangzhen
	public void addAntTarget(final List<TargetTemplate> targetList) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				int count = 1;
				Transaction tx = session.beginTransaction();
				StringBuffer sb = new StringBuffer();
				sb
						.append("insert into t_target_template (id, type, device_id, visit_amount, cash_amount, bill_amount, cost_amount, year, target_on, group_id, group_name, vehicle_number, ent_code, states, createon, createby) values (seq_t_target_template.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				try {
					Query query = session.createSQLQuery(sb.toString());
					for (TargetTemplate target : targetList) {
						query.setParameter(0, target.getType());
						query.setParameter(1, target.getDeviceId());
						query.setParameter(2, target.getVisitAmount());
						query.setParameter(3, target.getCashAmount());
						query.setParameter(4, target.getBillAmount());
						query.setParameter(5, target.getCostAmount());
						query.setParameter(6, target.getYear());
						query.setParameter(7, target.getTargetOn());
						query.setParameter(8, target.getGroupId());
						query.setParameter(9, target.getGroupName());
						//modify by wangzhen 2012-09-12 start
//						query.setParameter(10, target.getVehicleNumber());
						query.setParameter(10, target.getTerminalName());
						//modify by wangzhen 2012-09-12 end
						query.setParameter(11, target.getEntCode());
						query.setParameter(12, target.getStates());
						query.setParameter(13, target.getCreateOn());
						query.setParameter(14, target.getCreateBy());
						query.executeUpdate();
						if (count++ % 50 == 0) {
							session.flush();
						}
					}
				} catch (Exception e) {
					logger.error("failed", e);
					tx.rollback();
				}
				tx.commit();
				return null;
			}
		});
	}
	
	//modify by wangzhen 
	public void updateTarget(TargetTemplate target) {
		try {
			String hql = "update TargetTemplate t set t.visitAmount=?,t.cashAmount=?,t.billAmount=?,t.costAmount=?,t.cusVisitAmount=?,t.lastUpdateOn=?,t.lastUpdateBy=? where t.id=?";
			super.getHibernateTemplate().bulkUpdate(
					hql,
					new Object[] { target.getVisitAmount(),
							target.getCashAmount(), target.getBillAmount(),
							target.getCostAmount(), target.getCusVisitAmount(),
							target.getLastUpdateOn(),
							target.getLastUpdateBy(), target.getId() });
		} catch (Exception e) {
			logger.error("failed", e);
		}
	}

	public void deleteTarget(String entCode) {
		try {
			String hql = "delete from TargetTemplate t where t.entCode=?";
			super.getHibernateTemplate().bulkUpdate(hql,
					new Object[] { entCode });
		} catch (Exception e) {
			logger.error("failed", e);
		}
	}

	public void deleteTarget(String entCode, String deviceId, int type,
			int year, int targetOn) {
		try {
			String hql = "delete from TargetTemplate t where t.entCode=? and t.type=? and t.year=? and t.targetOn=?";
			super.getHibernateTemplate().bulkUpdate(hql,
					new Object[] { entCode, type, year, targetOn });
		} catch (Exception e) {
			logger.error("failed", e);
		}
	}

	public int getTargetCount(String entCode) {
		try {
			String hql = "select count(*) from TargetTemplate t where t.entCode=?";
			List list = super.getHibernateTemplate().find(hql,
					new Object[] { entCode });
			if (list == null || list.size() < 1)
				return 0;
			else
				return ((Number) list.get(0)).intValue();
		} catch (Exception e) {
			logger.error("failed", e);
			return -1;
		}
	}

	public int getTargetCount(String entCode, int type) {
		try {
			String hql = "select count(*) from TargetTemplate t where t.entCode=? and t.type=?";
			List list = super.getHibernateTemplate().find(hql,
					new Object[] { entCode, type });
			if (list == null || list.size() < 1)
				return 0;
			else
				return ((Number) list.get(0)).intValue();
		} catch (Exception e) {
			logger.error("failed", e);
			return -1;
		}
	}

	public int getTargetCount(String entCode, int type, int year) {
		try {
			String hql = "select count(*) from TargetTemplate t where t.entCode=? and t.type=? and t.year=?";
			List list = super.getHibernateTemplate().find(hql,
					new Object[] { entCode, type, year });
			if (list == null || list.size() < 1)
				return 0;
			else
				return ((Number) list.get(0)).intValue();
		} catch (Exception e) {
			logger.error("failed", e);
			return -1;
		}
	}

	public List<TargetTemplate> getTarget(String entCode, int type, int year) {
		try {
			String hql = "select t from TargetTemplate t where t.entCode=? and t.type=? and t.year=?";
			return super.getHibernateTemplate().find(hql,
					new Object[] { entCode, type, year });
		} catch (Exception e) {
			logger.error("failed", e);
			return null;
		}
	}

	public int getTargetCount(String entCode, String vehicleNumber, int type,
			int year, int targetOn) {
		try {
			//modify by wangzhen start 2012-09-12
			String hql = "select count(*) from TargetTemplate t where t.entCode=? and t.terminalName like ? and t.type=? and t.year=? and t.targetOn>=?";
			//modify by wangzhen start 2012-09-12
			List list = super.getHibernateTemplate().find(
					hql,
					new Object[] { entCode, "%" + vehicleNumber + "%", type,
							year, targetOn });
			if (list == null || list.size() < 1)
				return 0;
			else
				return ((Number) list.get(0)).intValue();
		} catch (Exception e) {
			logger.error("failed", e);
			return -1;
		}
	}

	public List<TargetTemplate> getTarget(final String entCode,
			final String vehicleNumber, final int type, final int year,
			final int targetOn, final int start, final int limit) {
		try {
			//modify by wangzhen
			final String hql = "select t from TargetTemplate t where t.entCode=? and t.terminalName like ? and t.type=? and t.year=? and t.targetOn>=? order by t.groupId";
			return getHibernateTemplate().executeFind(new HibernateCallback() {

				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query = session.createQuery(hql);
					query.setString(0, entCode);
					query.setString(1, "%" + vehicleNumber + "%");
					query.setInteger(2, type);
					query.setInteger(3, year);
					query.setInteger(4, targetOn);
					query.setFirstResult(start);
					query.setMaxResults(limit);
					return query.list();
				}
			});
		} catch (Exception e) {
			logger.error("failed", e);
			return null;
		}
	}

	public int getTargetCount(String entCode, String vehicleNumber, int type,
			int year) {
		try {
			//modify by wangzhen
			String hql = "select count(*) from TargetTemplate t where t.entCode=? and t.terminalName like ? and t.type=? and t.year=?";
			List list = super.getHibernateTemplate().find(
					hql,
					new Object[] { entCode, "%" + vehicleNumber + "%", type,
							year });
			if (list == null || list.size() < 1)
				return 0;
			else
				return ((Number) list.get(0)).intValue();
		} catch (Exception e) {
			logger.error("failed", e);
			return -1;
		}
	}

	@SuppressWarnings("unchecked")
	public List<TargetTemplate> getTarget(final String entCode,
			final String vehicleNumber, final int type, final int year,
			final int start, final int limit) {
		try {
			//modify by wangzhen
			final String hql = "select t from TargetTemplate t where t.entCode=? and t.terminalName like ? and t.type=? and t.year=? order by t.groupId";
			return getHibernateTemplate().executeFind(new HibernateCallback() {

				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query = session.createQuery(hql);
					query.setString(0, entCode);
					query.setString(1, "%" + vehicleNumber + "%");
					query.setInteger(2, type);
					query.setInteger(3, year);
					query.setFirstResult(start);
					query.setMaxResults(limit);
					return query.list();
				}
			});
		} catch (Exception e) {
			logger.error("failed", e);
			return null;
		}
	}
	
	/**
	 * 获取企业级指标(分页）
	 */
	public List getEntTarget(final String entCode,
			final int type, final int year,
			final int start, final int limit) {
		
		try {
			final String hql = "select t.targetOn,sum(t.billAmount) as billsum," +
					"sum(t.cashAmount) as cashsum,sum(t.costAmount) as costsum,sum(t.visitAmount) as visitsum," +
					"sum(t.cusVisitAmount) as cusVisitsum" +
			        " from TargetTemplate t where t.entCode=? and t.type=? and t.year=?  group by t.targetOn";
			//查询结果的集合
			List h = new ArrayList();
			
			h= getHibernateTemplate().executeFind(new HibernateCallback() {
				
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query = session.createQuery(hql);
					query.setString(0, entCode);
					query.setInteger(1, type);
					query.setInteger(2, year);
					query.setFirstResult(start);
					query.setMaxResults(limit);
					return  query.list();
				}
			});
			return h;
		} catch (Exception e) {
			logger.error("failed", e);
			return null;
		}
	}
	
	/**
	 * 获取企业级指标
	 */
	public List getEntTarget(final String entCode,
			final int type, final int year) {
			
		try {
			final String hql = "select t.targetOn,sum(t.billAmount) as billsum," +
					"sum(t.cashAmount) as cashsum,sum(t.costAmount) as costsum,sum(t.visitAmount) as visitsum," +
					"sum(t.cusVisitAmount) as cusVisitsum" +
			        " from TargetTemplate t where t.entCode=? and t.type=? and t.year=?  group by t.targetOn";
			//查询结果的集合
			List h = new ArrayList();
			h= getHibernateTemplate().executeFind(new HibernateCallback() {
				
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query = session.createQuery(hql);
					query.setString(0, entCode);
					query.setInteger(1, type);
					query.setInteger(2, year);
					return  query.list();
				}
			});
			return h;
		} catch (Exception e) {
			logger.error("failed", e);
			return null;
		}
	}

	public void deleteKpi(String entCode) {
		try {
			String hql = "update Kpi t set t.states = 1 where t.entCode = ?";
			super.getHibernateTemplate().bulkUpdate(hql,
					new Object[] { entCode });
		} catch (Exception e) {
			logger.error("[deleteKpi]", e);
		}
	}

	public void executeSql(final String sql) {
		try {
			getHibernateTemplate().execute(new HibernateCallback() {

				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query = session.createSQLQuery(sql);
					query.executeUpdate();
					return null;
				}
			});
		} catch (Exception e) {
			logger.error("failed", e);
		}
	}

	public int getStaffCount(String entCode) {
		try {
			String hql = "select count(*)  from Terminal t where t.entCode=? and t.expirationFlag=0";
			List list = super.getHibernateTemplate().find(hql,
					new Object[] { entCode });
			if (list == null || list.size() < 1)
				return 0;
			else
				return ((Number) list.get(0)).intValue();
		} catch (Exception e) {
			logger.error("failed", e);
			return -1;
		}
	}
    
	/**
	 * @author wangzhen
	 */
	@SuppressWarnings("unchecked")
	public List<TargetTemplate> getTarget(String entCode, Integer targetOn,
			Integer type, Integer year) {
		try {
			String hql = "select t from TargetTemplate t where t.entCode=? and t.type=? and t.year=? and t.targetOn=?";
			return super.getHibernateTemplate().find(hql,
					new Object[] { entCode, type, year,targetOn });
		} catch (Exception e) {
			logger.error("failed", e);
			return null;
		}
	}

	public int getEntTargetCount(String entCode, int type, int year) {
		Session session = null;
		try {
		    String sql = " select count(*) from (select t.target_on from t_target_template t where t.ent_code = '"+entCode+ 
		    		      "' and t.type =" +type+ " and t.year=" + year + " group by t.target_on)";
		    session = getHibernateTemplate().getSessionFactory().openSession();
		    Query query = session.createSQLQuery(sql);
		    return query.list().size();
		} catch (Exception e) {
			logger.error("failed", e);
			return -1;
		} finally {
		      if (session != null) {
		          session.clear();
		          session.close();
		          getHibernateTemplate().getSessionFactory().close();
		        }
	    }
	}
    
	/**
	 * @author wangzhen
	 */
	public List<TTerminal> getTerminal(String entCode) {
		try {
			String hql = "select t from TTerminal t where t.entCode=? and t.expirationFlag=0";
			return super.getHibernateTemplate().find(hql,
					new Object[] { entCode});
		} catch (Exception e) {
			logger.error("failed", e);
			return null;
		}
	}

	public void deleteTarget(String entCode, Integer type, Integer year,
			Integer targetOn) {
		try {
			String hql = "delete from TargetTemplate t where t.entCode=? and t.type=? and t.year=? and t.targetOn=?";
			super.getHibernateTemplate().bulkUpdate(hql,
					new Object[] { entCode, type, year, targetOn });
		} catch (Exception e) {
			logger.error("failed", e);
		}
	}

}
	
