package com.sosgps.wzt.system.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.sos.dao.JdbcSqlExecutor;
import org.sos.taglibs.pagination.PageIterator;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.system.dao.EnterpManageDao;
import com.sosgps.wzt.system.model.TEntValue;

/**
 * @Title: EnterpManageJdbcDaoImpl.java
 * @Description:
 * @Copyright:
 * @Date: 2009-4-1 下午04:51:52
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yang.lei
 * @version 1.0
 */
public class EnterpManageJdbcDaoImpl extends JdbcSqlExecutor implements EnterpManageDao {
	private static final Logger logger = Logger.getLogger(EnterpManageJdbcDaoImpl.class);

	public void deleteEnterprise(String entCode) {
		String sql = "update T_ENT a set a.USAGE_FLAG = 0 where a.ENT_CODE = '"+entCode+"'";
		this.getJdbcTemplate().execute(sql);
	}

	public TEntValue getTEntValue(String entCode) {
		String sql = "select distinct * from T_ENT a where a.ENT_CODE = '"+entCode+"'";
		final TEntValue value = new TEntValue();
		this.getJdbcTemplate().query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				value.setEntCode(rs.getString("ENT_CODE"));
				value.setEntName(rs.getString("ENT_NAME"));
				value.setEntStatus(rs.getString("ENT_STATUS"));
				value.setEntCreateDate(rs.getDate("ENT_CRT_DATE"));
				value.setCenterX(String.valueOf(rs.getDouble("CENTER_X")));
				value.setCenterY(String.valueOf(rs.getDouble("CENTER_Y")));
				value.setMapZoom(rs.getInt("MAP_ZOOM"));
				value.setEntType(String.valueOf(rs.getInt("ENT_TYPE")));
				value.setUsageFlag(String.valueOf(rs.getInt("USAGE_FLAG")));
				value.setMaxUserNum(String.valueOf(rs.getInt("MAX_USER_NUM")));
			}
		});
		return value;
	}

	public void modifyEntInfo(TEntValue tEntValue) {
		String sql = "update T_ENT a set a.ENT_NAME = '" + tEntValue.getEntName() + "',a.CENTER_X = "
				+ tEntValue.getCenterX() + ",a.CENTER_Y = " + tEntValue.getCenterY() + ",a.MAP_ZOOM = "
				+ tEntValue.getMapZoom() + ",a.MAX_USER_NUM = " + tEntValue.getMaxUserNum()
				+ ",a.USAGE_FLAG = " + tEntValue.getUsageFlag() + " where a.ENT_CODE = '"+tEntValue.getEntCode()+"'";
		this.getJdbcTemplate().execute(sql);
	}

	public PageIterator getEntInfoList(int currentPage, int pageSize) {
		String sql = "select distinct t.ENT_CODE, t.ENT_NAME, t.ENT_CRT_DATE," 
			       + "case when t.ENT_TYPE=1 then '企业用户' else '家庭用户' end as ENT_TYPE from T_ENT t";
		this.setSqlString(sql);
		this.setCurrentPage(currentPage);
		this.setPageSize(pageSize);
		PageIterator pageIterator = this.getPaginationList();
		List rows = pageIterator.getCollection();
		Iterator it = rows.iterator();
		ArrayList<TEntValue> entInfoList = new ArrayList();
		while(it.hasNext()) {
		    Map entMap = (Map) it.next();
		    System.out.print(entMap.get("ENT_CODE") + "\t");
		    System.out.print(entMap.get("ENT_NAME") + "\t");
		    System.out.print(entMap.get("ENT_CRT_DATE") + "\t");
		    System.out.println(entMap.get("ENT_TYPE") + "\t");
		    TEntValue value = new TEntValue();
		    value.setEntCode((String)entMap.get("ENT_CODE"));
		    value.setEntName((String)entMap.get("ENT_NAME"));
		    value.setEntCreateDate((java.util.Date)entMap.get("ENT_CRT_DATE"));
		    value.setEntType((String)entMap.get("ENT_TYPE"));
		    entInfoList.add(value);
		}
		pageIterator.setCollection(entInfoList);
		return pageIterator;
	}

}
