package com.autonavi.lbsgateway;

import java.sql.*;
import java.util.*;
import java.util.Date;

import com.autonavi.directl.Log;
import com.autonavi.directl.dbutil.DbOperation;

/*
 全局终端Hash表
 结构：
 GBLTerminalList
 |
 -------KEY  = SIM
 -------CONTENT = LocationInfo
 */

public class GBLTerminalList extends Hashtable {
	static GBLTerminalList instance = null;
 	 

	public static GBLTerminalList getInstance() {
		if (instance == null) {
			instance = new GBLTerminalList();
		}
		return instance;
	}

	private GBLTerminalList() {
	}

	/**
	 * 通过终端号获得设备SIM卡卡号
	 * 
	 * @param termNum
	 *            String
	 * @return String
	 */
	public  synchronized  String getSimcardNum(String termNum) {

		String simcard = null;
		simcard = (String) instance.get(termNum);

		if (simcard == null) {
			Connection conn = null;
			Statement stm = null;
			ResultSet rs = null;	
			
			try {
				conn = DbOperation.getConnection();
				String sql = "select simcard sim from t_terminal where device_id='"+termNum+"'";
 				stm = conn.createStatement();
				rs = stm.executeQuery(sql);
				
				if  (rs.next()) {
					if ( rs.getString("sim") != null) {
						instance.put(termNum, rs.getString("sim"));
						simcard = rs.getString("sim");
					}
				}
				Log.getInstance().outLog("新增了终端"+termNum+",共加载了" + instance.size() + "个终端");
			} catch (SQLException ex) {
				Log.getInstance().errorLog("加载新增设备异常：" + ex.getMessage(), ex);
			} finally {
				DbOperation.release(stm, rs, null, conn);
				// com.mapabc.db.DBConnectionManager.close(conn, stm, rs);
			}
		}
		return simcard;
	}

	/**
	 * 加载所有的终端到服务器中
	 */
	public  void loadTerminals() {

		Connection conn = null;
		//只加载GPS类型终端
		String sql = "select device_id gpssn,simcard sim from t_terminal where locate_type='1'";
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DbOperation.getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				if (rs.getString("gpssn") != null
						&& rs.getString("sim") != null) {
					instance.remove(rs.getString("gpssn"));//去除历史数据
					instance.put(rs.getString("gpssn"), rs.getString("sim"));
				}
			}
			Log.getInstance().outLog("共加载了" + instance.size() + "个终端");
		} catch (SQLException ex) {
			Log.getInstance().errorLog("加载设备异常：" + ex.getMessage(), ex);
		} finally {
			DbOperation.release(stm, rs, null, conn);
			// com.mapabc.db.DBConnectionManager.close(conn, stm, rs);
		}

	}

}
