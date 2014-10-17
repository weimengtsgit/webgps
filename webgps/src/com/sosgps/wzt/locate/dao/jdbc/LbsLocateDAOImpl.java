package com.sosgps.wzt.locate.dao.jdbc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import com.sosgps.wzt.locate.dao.LbsLocateDAO;

/**
 * @Title: LbsLocateDAOImpl.java
 * @Description: LBS定位
 * @Copyright:
 * @Date: 2009-3-25 下午05:57:02
 * @Copyright (c) 2008 Company: www.mapabc.com
 * @author yang.lei
 * @version 1.0
 */
public class LbsLocateDAOImpl extends JdbcDaoSupport implements LbsLocateDAO {
	private static final Logger logger = Logger.getLogger(LbsLocateDAOImpl.class);

	/**
	 * LBS定位数据入库T_LBS_LOCRECORD表
	 * 
	 * @param simcard
	 * @param x
	 * @param y
	 * @param entCode
	 * @param lbsTiem
	 * @param errorCode
	 * @param errorDesc
	 * @param locDesc
	 */
	public void insertLbsLocrecord(String simcard, String x, String y, String entCode, String lbsTime,
			String errorCode, String errorDesc, String locDesc, String lngX, String latY) {
		String sql = "insert into T_LBS_LOCRECORD a (ID,SIMCARD,X,Y,ENT_CODE,LBSTIME,ERROR_CODE,ERROR_DESC,LOC_DESC,LNGX,LATY)"
				+ " values (SEQ_LBS_LOCRECORD.NEXTVAL,'"
				+ simcard
				+ "','"
				+ x
				+ "','"
				+ y
				+ "','"
				+ entCode
				+ "',Timestamp'"
				+ lbsTime
				+ "','"
				+ errorCode
				+ "','"
				+ errorDesc
				+ "','"
				+ locDesc
				+ "','"
				+ lngX + "','" + latY + "')";
		this.getJdbcTemplate().update(sql);
	}

	/**
	 * @param simcard
	 * @param x
	 * @param y
	 * @param lbsTime
	 */
	public void insertLastLocrecordByLbsData(String simcard, String x, String y, String lbsTime) {
		String sql = "insert into T_LAST_LOCRECORD a (ID,LONGITUDE,LATITUDE,SPEED,DIRECTION,HEIGHT,DEVICE_ID,GPSTIME,LOCATE_TYPE)"
				+ " values (SEQ_LAST_LOC.NEXTVAL,'" + x + "','" + y + "',0,0,0,'" + simcard + "',Timestamp'"
				+ lbsTime + "','0')";
		this.getJdbcTemplate().update(sql);
	}

	/**
	 * 根据simcard查询T_LAST_LOCRECORD中是否存在该终端记录
	 * 
	 * @param simcard
	 * @return
	 */
	public boolean isExistRecordLastLoc(String simcard) {
		String sql = "select * from T_LAST_LOCRECORD a where a.DEVICE_ID = '" + simcard + "'";
		List list = this.getJdbcTemplate().queryForList(sql);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据simcard删除T_LAST_LOCRECORD中存在的记录
	 * 
	 * @param simcard
	 */
	public void deleteLastLocByDeviceId(String simcard) {
		String sql = "delete from T_LAST_LOCRECORD a where a.DEVICE_ID = '" + simcard + "'";
		this.getJdbcTemplate().update(sql);
	}

}
