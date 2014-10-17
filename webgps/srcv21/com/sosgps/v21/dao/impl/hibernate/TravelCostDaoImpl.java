package com.sosgps.v21.dao.impl.hibernate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sosgps.v21.dao.TravelCostDao;
import com.sosgps.v21.model.TravelCost;
import com.sosgps.wzt.orm.TTerminal;

public class TravelCostDaoImpl extends BaseHibernateDao implements TravelCostDao {

	private static final Logger logger = LoggerFactory.getLogger(TravelCostDaoImpl.class);
	@SuppressWarnings("unchecked")
    public List<TravelCost> queryTravelCostInfoByCondition(final Map<String, Object> paramMap, final String entCode){
	    try {
            configInterceptor(entCode);
            return getHibernateTemplate().executeFind(new HibernateCallback() {

                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    StringBuilder sb = new StringBuilder();
                    sb.append(" from TravelCost where deleteFlag = 0");
                    if (paramMap.get("startTime") != null) {
                        sb.append(" and createOn > ?1");
                    }
                    if (paramMap.get("endTime") != null) {
                        sb.append(" and createOn < ?2");
                    }
                    if (paramMap.get("reviewStatesStr") != null) {
                        sb.append(" and reviewStates = ?3");
                    }
                    if (paramMap.get("deviceIds") != null) {
                        sb.append(" and deviceId in ("
                                + paramMap.get("deviceIds") + ")");
                    }
                    sb.append(" and entCode = ?4");
                    if (paramMap.get("orderby") != null) {
                        sb.append(" order by "+paramMap.get("orderby"));
                    }
                    
                    Query query = session.createQuery(sb.toString());
                    if (paramMap.get("startTime") != null) {
                        query.setLong("1", (Long) paramMap.get("startTime"));
                    }
                    if (paramMap.get("endTime") != null) {
                        query.setLong("2", (Long) paramMap.get("endTime"));
                    }
                    if (paramMap.get("reviewStatesStr") != null) {
                        query.setInteger("3", (Integer)paramMap.get("reviewStatesStr"));
                    }
                    query.setString("4", entCode);
                    if (paramMap.get("start") != null) {
                        query.setFirstResult((Integer) paramMap.get("start"));
                    }
                    if (paramMap.get("limit") != null) {
                        query.setMaxResults((Integer) paramMap.get("limit"));
                    }
                    return query.list();
                }
            });
        } catch (Exception e) {
            logger.error("failed", e);
            return null;
        }
	}

    @SuppressWarnings("unchecked")
    public List<TravelCost> listTravelDetails(final String entCode, final int start, final int limit,
            String startDate, String endDate, final String reviwStates, String deviceIds) {
        String st1 = startDate + " 00:00:00";
        String st2 = startDate + " 23:59:59";
        String et1 = endDate + " 00:00:00";
        String et2 = endDate + " 23:59:59";
        try {
//             //将st2改成et2，作为开始时间和结束时间的范围
//             String hql = "select t from TravelCost t "
//                    + " where t.startTravelTime between to_date('"+st1+"', 'yyyy-mm-dd hh24:mi:ss') and "
//                    + " to_date('"+et2+"', 'yyyy-mm-dd hh24:mi:ss')" 
//                    + " and t.endTravelTime between to_date('"+st1+"', 'yyyy-mm-dd hh24:mi:ss') and"
//                    + " to_date('"+et2+"', 'yyyy-mm-dd hh24:mi:ss')" 
//                    + " and t.entCode =?"
//                    + " and t.reviewStates in(" +reviwStates+")"
//                    + " and t.deleteFlag = 0 ";
             String hql = "select t from TravelCost t "
                     + " where t.createOn between to_date('"+st1+"', 'yyyy-mm-dd hh24:mi:ss') and "
                     + " to_date('"+et2+"', 'yyyy-mm-dd hh24:mi:ss')" 
                     + " and t.entCode =?"
                     + " and t.reviewStates in(" +reviwStates+")"
                     //+ " and t.deleteFlag = 0 and t.flag=1";
                     + " and t.deleteFlag = 0 ";
                    if(!deviceIds.equals("'-1'")){
                        hql += " and t.deviceId in ("+deviceIds+")";
                    }
                    hql += " order by t.createOn desc, t.groupName, t.deviceId";
           final String  sql = hql;
           return getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    Query query = session.createQuery(sql);
                    query.setString(0, entCode);
                    query.setFirstResult(start);
                    query.setMaxResults(limit);
                    return  query.list();
                }
            });
        } catch (Exception e) {
            logger.error("failed", e);
            return null;
        }
    }
    /**
     * 审核差旅信息
     */
    public boolean verifyTravelCost(String ids,String entCode,String flag) {
        try {
            String sql = "update TravelCost t set t.reviewStates="+flag+" where t.id in("+ids+")"+
                          " and t.entCode=? and t.deleteFlag = 0";
            Object[] objArr = {entCode};
            Integer i =  getHibernateTemplate().bulkUpdate(sql, objArr);
            if(i != 0) {
                return true;
            }else {
                return false;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @SuppressWarnings("rawtypes")
    public TravelCost getTravelCost(String deviceId, String entCode, int flag) {
        TravelCost  travelCost = null;
        try {
            StringBuffer hql = new StringBuffer();
            hql.append("from TravelCost t where t.deleteFlag=0 and t.entCode=? and t.flag = 0 and t.deviceId =?");
            Object[] objArr = {entCode,deviceId};
            List list = getHibernateTemplate().find(hql.toString(), objArr);
            if(list != null && list.size() > 0 ) {
                travelCost = (TravelCost)list.get(0);
                return travelCost;
            } else {
                return travelCost;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return travelCost;
        }
    }

    @SuppressWarnings("unchecked")
    public List<TravelCost> listTravelCost(final String entCode, String startDate, final String endDate, final String revStates,
            String deviceIds) {
        String st1 = startDate + " 00:00:00";
        String st2 = startDate + " 23:59:59";
        String et1 = endDate + " 00:00:00";
        String et2 = endDate + " 23:59:59";
        try {
//             String hql = "select t from TravelCost t "
//                    + " where t.startTravelTime between to_date('"+st1+"', 'yyyy-mm-dd hh24:mi:ss') and "
//                    + " to_date('"+st2+"', 'yyyy-mm-dd hh24:mi:ss')" 
//                    + " and t.endTravelTime between to_date('"+et1+"', 'yyyy-mm-dd hh24:mi:ss') and"
//                    + " to_date('"+et2+"', 'yyyy-mm-dd hh24:mi:ss')" 
//                    + " and t.entCode =?"
//                    + " and t.reviewStates in ("+revStates+")"
//                    + " and t.deleteFlag = 0 ";
             String hql = "select t from TravelCost t "
                     + " where t.createOn between to_date('"+st1+"', 'yyyy-mm-dd hh24:mi:ss') and "
                     + " to_date('"+et2+"', 'yyyy-mm-dd hh24:mi:ss')" 
                     + " and t.entCode =?"
                     + " and t.reviewStates in ("+revStates+")"
                    // + " and t.deleteFlag = 0  and t.flag=1";
                     + " and t.deleteFlag = 0 ";
                    if(!deviceIds.equals("'-1'")){
                        hql += " and t.deviceId in ("+deviceIds+")";
                    }
                    hql += " order by t.createOn desc, t.groupName, t.deviceId";
           final String  sql = hql;
           return getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    Query query = session.createQuery(sql);
                    query.setString(0, entCode);
                    query.setFirstResult(0);
                    query.setMaxResults(65535);
                    return  query.list();
                }
            });
        } catch (Exception e) {
            logger.error("failed", e);
            return null;
        }
        
    }

    @SuppressWarnings("rawtypes")
    public int getTravelCostCount(String entCode, String st, String et, String revStates,
            String deviceIds) {
        String st1 = st + " 00:00:00";
        String st2 = st + " 23:59:59";
        String et1 = et + " 00:00:00";
        String et2 = et + " 23:59:59";
//        String hql = "select t from TravelCost t "
//                + " where t.startTravelTime between to_date('"+st1+"', 'yyyy-mm-dd hh24:mi:ss') and "
//                + " to_date('"+st2+"', 'yyyy-mm-dd hh24:mi:ss')" 
//                + " and t.endTravelTime between to_date('"+et1+"', 'yyyy-mm-dd hh24:mi:ss') and"
//                + " to_date('"+et2+"', 'yyyy-mm-dd hh24:mi:ss')" 
//                + " and t.entCode = ? "
//                + " and t.reviewStates in ("+revStates+")"
//                + " and t.deleteFlag = 0 ";
        String hql = "select t from TravelCost t "
                + " where t.createOn between to_date('"+st1+"', 'yyyy-mm-dd hh24:mi:ss') and "
                + " to_date('"+et2+"', 'yyyy-mm-dd hh24:mi:ss')" 
                + " and t.entCode = ? "
                + " and t.reviewStates in ("+revStates+")"
               // + " and t.deleteFlag = 0 and t.flag =1";
                + " and t.deleteFlag = 0 ";
                if(!deviceIds.equals("'-1'")){
                    hql += " and t.deviceId in ("+deviceIds+")";
                }
                hql += " order by t.createOn desc, t.groupName, t.deviceId";
        try {
            List list = super.getHibernateTemplate().find(
                    hql,
                    new Object[] {entCode});
            if (list == null || list.size() < 1)
                return 0;
            else
                return list.size();
        } catch (Exception e) {
            logger.error("failed", e);
            return -1;
        }
    }

    public TravelCost getTravelCostById(Long id) {
        try {
            TravelCost instance = (TravelCost) getHibernateTemplate().get(
                    "com.sosgps.v21.model.TravelCost", id);
            return instance;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    
    public TravelCost getTravelCost(String deviceId, String entCode) {
        TravelCost  travelCost = null;
        try {
            StringBuffer hql = new StringBuffer();
            hql.append("from TravelCost t where t.deleteFlag=0 and t.entCode=? and t.deviceId =?");
            Object[] objArr = {entCode,deviceId};
            List list = getHibernateTemplate().find(hql.toString(), objArr);
            if(list != null && list.size() > 0 ) {
                travelCost = (TravelCost)list.get(0);
                return travelCost;
            } else {
                return travelCost;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return travelCost;
        }
    }
}
