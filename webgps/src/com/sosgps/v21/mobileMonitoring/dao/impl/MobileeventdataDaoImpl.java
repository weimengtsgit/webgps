package com.sosgps.v21.mobileMonitoring.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sosgps.v21.mobileMonitoring.dao.MobileeventdataDao;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.util.PageQuery;

public class MobileeventdataDaoImpl extends  HibernateDaoSupport implements
		MobileeventdataDao {

	public Page<Object[]> queryMobile(String keyWords,Date startTime, Date entTime, String type,
	        String deviceId,int pageNo, int pageSize) {
		StringBuffer buf = new StringBuffer(
				"select t.id,t.gpstime,t.termName,t.groupName,t.simcard,t.type,t.state "
						+ " from TMobileeventdata t"
						+ " where " );
		buf.append(" t.deviceId in(" +deviceId + ")");
		List<Object> params = new ArrayList<Object>();
		String name = keyWords;
		String simcard = keyWords;
		
		if (startTime != null) {
            //buf.append(" t.gpstime >= ? ");
		    buf.append(" and t.gpstime >= ? ");
            params.add(startTime);
        }
        if (entTime != null) {
            buf.append("  and  t.gpstime <= ? ");
            params.add(entTime);
        }
//        if(deviceId != null) {
//            buf.append(" and t.deviceId in (?)");
//            params.add(deviceId);
//        }
		
//		if (keyWords != null) {
//			buf.append(" and (t.termName like ? ");
//			params.add("%" + name + "%");
//			buf.append(" or t.simcard like ? )");
//			params.add("%" + simcard + "%");
//		}
        if(name != null && !"".equals(name)) {
            buf.append(" and (t.termName like ? ");
            params.add("%" + name + "%");
        }
        if(simcard != null && !"".equals(simcard)) {
            buf.append(" or t.simcard like ? )");
            params.add("%" + simcard + "%");
        }
		
		if (type != null) {
			buf.append(" and  t.type in "+type);
		}
		buf.append(" order by t.gpstime desc,t.deviceId desc");
		Page<Object[]> page = new Page<Object[]>(pageSize, true);
		page.setPageNo(pageNo);
		return new PageQuery<Object[]>(this).findByPage(page, buf.toString(),
				params.toArray());
	}
	/**
	 * 查询统计手机开关机操作次数
	 */
	public Page<Object[]> countMobileeventdata(String entName, String entCode,
			String termName, Date startTime, Date entTime, int pageNo,
			int pageSize) {
		StringBuffer buf = new StringBuffer(
				"select tt.entName,tl.entCode,tl.termName, t.deviceId,t.type,count(t.deviceId) "
						+ " from TEnt tt, TTerminal tl, TMobileeventdata t"
						+ "  where tt.entCode = tl.entCode "
						+ "  and tl.deviceId = t.deviceId "
						+ "  and t.type = 'net_open_or_close' ");
		List<Object> params = new ArrayList<Object>();
		if (entName != null) {
			buf.append(" and  tt.entName like ? ");
			params.add("%" + entName + "%");
		}
		if (entCode != null) {
			buf.append(" and tl.entCode = ? ");
			params.add(entCode);
		}
		if (termName != null) {
			buf.append(" and  tl.termName like ? ");
			params.add("%" + termName + "%");
		}
		if (startTime != null) {
			buf.append(" and  t.gpstime >= ? ");
			params.add(startTime);
		}
		if (entTime != null) {
			buf.append("  and  t.gpstime <= ? ");
			params.add(entTime);
		}
		buf.append(" group by t.deviceId, t.type , tt.entName, tl.entCode, tl.termName order by col_5_0_ desc ");
		Page<Object[]> page = new Page<Object[]>(pageSize, true);
		page.setPageNo(pageNo);
		return new PageQuery<Object[]>(this).findByPage(page, buf.toString(),
				params.toArray());
	}

}
