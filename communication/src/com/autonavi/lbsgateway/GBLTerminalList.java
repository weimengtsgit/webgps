package com.autonavi.lbsgateway;

import java.sql.*;
import java.util.*;
import java.util.Date;

import com.autonavi.directl.Log;
import com.autonavi.directl.dbutil.DbOperation;

/*
 ȫ���ն�Hash��
 �ṹ��
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
	 * ͨ���ն˺Ż���豸SIM������
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
				Log.getInstance().outLog("�������ն�"+termNum+",��������" + instance.size() + "���ն�");
			} catch (SQLException ex) {
				Log.getInstance().errorLog("���������豸�쳣��" + ex.getMessage(), ex);
			} finally {
				DbOperation.release(stm, rs, null, conn);
				// com.mapabc.db.DBConnectionManager.close(conn, stm, rs);
			}
		}
		return simcard;
	}

	/**
	 * �������е��ն˵���������
	 */
	public  void loadTerminals() {

		Connection conn = null;
		//ֻ����GPS�����ն�
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
					instance.remove(rs.getString("gpssn"));//ȥ����ʷ����
					instance.put(rs.getString("gpssn"), rs.getString("sim"));
				}
			}
			Log.getInstance().outLog("��������" + instance.size() + "���ն�");
		} catch (SQLException ex) {
			Log.getInstance().errorLog("�����豸�쳣��" + ex.getMessage(), ex);
		} finally {
			DbOperation.release(stm, rs, null, conn);
			// com.mapabc.db.DBConnectionManager.close(conn, stm, rs);
		}

	}

}
