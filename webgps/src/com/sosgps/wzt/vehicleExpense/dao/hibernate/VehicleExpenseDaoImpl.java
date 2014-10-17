package com.sosgps.wzt.vehicleExpense.dao.hibernate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TVehicleExpenseDao;
import com.sosgps.wzt.util.PageQuery;
import com.sosgps.wzt.vehicleExpense.dao.VehicleExpenseDao;

public class VehicleExpenseDaoImpl extends TVehicleExpenseDao implements VehicleExpenseDao {

	public Page<Object[]> listVehicleExpense(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue) {
		String hql = "select to.id, t.termName, t.vehicleNumber, t.simcard, g.groupName, to " +
				" from TTerminal t, TVehicleExpense to "
				+ " left join t.refTermGroups ref "
				+ " left join ref.TTermGroup g "
				+ " where to.empCode = ? " 
				//+ " and to.userId = ?  "
				+ " and (t.termName like ? or t.vehicleNumber like ? or g.groupName like ?) "
				+ " and to.createDate >= ? and to.createDate <= ? "
				+ " and to.deviceId = t.deviceId "
				+ " order by to.createDate desc, t.termName ";
		PageQuery<Object[]> pq = new PageQuery<Object[]>(this);
		Page<Object[]> re = new Page<Object[]>();
		re.setAutoCount(true);
		re.setPageNo(pageNo);
		re.setPageSize(pageSize);
		//return pq.findByPage( re, hql, entCode, userId, "%" + searchValue + "%", "%" + searchValue + "%", "%" + searchValue + "%", startDate, endDate );
		return pq.findByPage( re, hql, entCode, "%" + searchValue + "%", "%" + searchValue + "%", "%" + searchValue + "%", startDate, endDate );
		
	}
	
	public void deleteAll(final String ids) {
		try {
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session s)
						throws HibernateException, SQLException {
					Query query = s.createQuery("delete TVehicleExpense t where t.id in ("+ids+")");
					//query.setParameter("ids", ids);
					query.executeUpdate();
					return null;
				}
			});
		} catch (RuntimeException re) {
			logger.error(re);
			throw re;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Object[]> listVehicleMsg(String deviceIds,
			String entCode, Long userId, int startint, int limitint,
			String startDate, String endDate, String searchValue, String carTypeInfoId){
		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
        try {
        	String sql = " select count(tt.device_id) as num  "
			+ " from t_terminal tt "
			+ " left join ref_term_group rtg on rtg.device_id = tt.device_id "
			+ " left join t_term_group ttg on ttg.id = rtg.group_id "
			+ " left join car_type_info cti on cti.id = tt.car_type_info_id "
			+ " left join  "
			+ " (select sum(t.vehicle_depreciation) as vehicle_depreciation,  "
			+ " sum(t.personal_expenses) as personal_expenses, sum(t.insurance) as insurance, "
			+ " sum(t.maintenance) as maintenance, sum(t.toll) as toll,  "
			+ " sum(t.annual_check) as annual_check ,t.device_id "
			+ " from t_vehicle_expense t "
			+ " where t.emp_code = '"+entCode+"' "
			+ " and t.device_id in ("+deviceIds+") "
			+ " and t.create_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
			+ " and t.create_date <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') "
			+ " group by t.device_id "
			+ " )a on a.device_id = tt.device_id "
			+ " left join  "
			+ " (select sum(t.oil_liter) as oil_liter, sum(t.oil_cost) as oil_cost, t.device_id "
			+ " from t_oiling t "
			+ " where t.emp_code = '"+entCode+"' "
			+ " and t.device_id in ("+deviceIds+") "
			+ " and create_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
			+ " and create_date <= to_date('"+endDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
			+ " group by t.device_id "
			+ " )b on b.device_id = tt.device_id "
			+ " left join  "
			+ " (select sum(t.distance) as distance, t.device_id  "
			+ " from t_distance_day t "
			+ " where t.device_id in ("+deviceIds+") "
			+ " and tjdate >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
			+ " and tjdate <= to_date('"+endDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
			+ " group by t.device_id  "
			+ " )c on c.device_id = tt.device_id "
			+ " left join  "
			+ " (select sum(t.dispatch_amount) as dispatchamount,t.device_id   "
			+ " from t_dispatch_money t "
			+ " where t.device_id in ("+deviceIds+") "
			+ " and t.dispatchdate >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
			+ " and t.dispatchdate <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') "
			+ " group by t.device_id "
			+ " )e on e.device_id = tt.device_id "
			+ " left join  "
			+ " (select sum(t.mileagenorm) as mileagenorm,sum(t.expensenorm) as expensenorm,sum(t.returnnorm) as returnnorm,t.device_id   "
			+ " from t_vehicle_norm t "
			+ " where t.device_id in ("+deviceIds+") "
			+ " and t.save_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
			+ " and t.save_date <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') "
			+ " group by t.device_id "
			+ " )f on f.device_id = tt.device_id "
			+ " where tt.device_id in ("+deviceIds+") ";
        	if(carTypeInfoId.length()>0 && !carTypeInfoId.equals("-1")){
        		sql += " and cti.id = "+carTypeInfoId+" ";
        	}
        	
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
            
        	sql = " select * from (select d.*, rownum row_num from ( "
        		+ " select a.vehicle_depreciation, a.personal_expenses, a.insurance, a.maintenance, a.toll, a.annual_check, " 
        		+ " b.oil_liter, b.oil_cost, c.distance, f.mileagenorm,f.expensenorm,f.returnnorm,e.dispatchamount,tt.device_id, cti.type_name, cti.oil_wear, tt.term_name, "
    			+ " tt.vehicle_number, tt.simcard, ttg.group_name, "
    			+ " (b.oil_liter/c.distance) as oilperkm,  "
    			+ " (b.oil_cost/c.distance) as costperkm "
    			+ " from t_terminal tt "
    			+ " left join ref_term_group rtg on rtg.device_id = tt.device_id "
    			+ " left join t_term_group ttg on ttg.id = rtg.group_id "
    			+ " left join car_type_info cti on cti.id = tt.car_type_info_id "
    			+ " left join  "
    			+ " (select sum(t.vehicle_depreciation) as vehicle_depreciation,  "
    			+ " sum(t.personal_expenses) as personal_expenses, sum(t.insurance) as insurance, "
    			+ " sum(t.maintenance) as maintenance, sum(t.toll) as toll,  "
    			+ " sum(t.annual_check) as annual_check ,t.device_id "
    			+ " from t_vehicle_expense t "
    			+ " where t.emp_code = '"+entCode+"' "
    			+ " and t.device_id in ("+deviceIds+") "
    			+ " and t.create_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
    			+ " and t.create_date <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') "
    			+ " group by t.device_id "
    			+ " )a on a.device_id = tt.device_id "
    			+ " left join  "
    			+ " (select sum(t.oil_liter) as oil_liter, sum(t.oil_cost) as oil_cost, t.device_id "
    			+ " from t_oiling t "
    			+ " where t.emp_code = '"+entCode+"' "
    			+ " and t.device_id in ("+deviceIds+") "
    			+ " and create_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
    			+ " and create_date <= to_date('"+endDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
    			+ " group by t.device_id "
    			+ " )b on b.device_id = tt.device_id "
    			+ " left join  "
    			+ " (select sum(t.distance) as distance, t.device_id  "
    			+ " from t_distance_day t "
    			+ " where t.device_id in ("+deviceIds+") "
    			+ " and tjdate >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
    			+ " and tjdate <= to_date('"+endDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
    			+ " group by t.device_id  "
    			+ " )c on c.device_id = tt.device_id "
    			+ " left join  "
    			+ " (select sum(t.dispatch_amount) as dispatchamount,t.device_id   "
    			+ " from t_dispatch_money t "
    			+ " where t.device_id in ("+deviceIds+") "
    			+ " and t.dispatchdate >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
    			+ " and t.dispatchdate <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') "
    			+ " group by t.device_id "
    			+ " )e on e.device_id = tt.device_id "
    			+ " left join  "
    			+ " (select sum(t.mileagenorm) as mileagenorm,sum(t.expensenorm) as expensenorm,sum(t.returnnorm) as returnnorm,t.device_id   "
    			+ " from t_vehicle_norm t "
    			+ " where t.device_id in ("+deviceIds+") "
    			+ " and t.save_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') "
    			+ " and t.save_date <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') "
    			+ " group by t.device_id "
    			+ " )f on f.device_id = tt.device_id "
    			+ " where tt.device_id in ("+deviceIds+") ";
	        	if(carTypeInfoId.length()>0 && !carTypeInfoId.equals("-1")){
	        		sql += " and cti.id = "+carTypeInfoId+" ";
	        	}
	        	sql += " order by cti.id, costperkm desc, oilperkm desc ) d) e ";
	        	sql += " where e.row_num between "+(startint+1)+" and "+(startint+limitint)+"";

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
