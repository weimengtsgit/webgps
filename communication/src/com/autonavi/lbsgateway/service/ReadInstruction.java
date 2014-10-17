package com.autonavi.lbsgateway.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimerTask;

import oracle.jdbc.driver.OracleTypes;

import org.apache.activemq.transaction.Synchronization;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.admin.SnapshotIF;

import com.autonavi.directl.Tools;

import com.autonavi.directl.dbutil.DbOperation;
import com.autonavi.lbsgateway.GPRSThread;
import com.autonavi.lbsgateway.GPRSThreadList;
import com.autonavi.lbsgateway.bean.InstructionBean;

public class ReadInstruction extends TimerTask {
	private static boolean isRunning = false;
	static long num = 0;

	public ReadInstruction() {
		com.autonavi.directl.Log.getInstance().outLog("启动指令下发定时检测！");
	}

	public  void run() {
		if (!isRunning) {//防止与下一次任务重叠
			try {
				isRunning = true;
				long btime = System.currentTimeMillis();
				
				com.autonavi.directl.Log.getInstance().outLog("开始第"+num+"次指令下发任务检测！");
				// 获取所有待发送的指令
				ArrayList<InstructionBean> list = this
						.getInstructionsByState("1");
				CommonGatewayServiceImpl gate = new CommonGatewayServiceImpl();

				if (null != list && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						InstructionBean ib = list.get(i);
						String simcard = list.get(i).getSimcard();
						String cmd = list.get(i).getInstuction();

						if (cmd == null || cmd.trim().equals(""))
							continue;

						byte[] bcmd = null;
						if (!cmd.startsWith("5b4851") && !cmd.startsWith("5346")) {
							bcmd = cmd.getBytes();
						} else {
							bcmd = Tools.fromHexString(cmd);// 华强特殊协议
						}

						if (bcmd == null)
							continue;
						int state = gate.sendDataToTerminal(simcard, bcmd);// 发送到终端

					}
				}
				long etime = System.currentTimeMillis();
				com.autonavi.directl.Log.getInstance().outLog("指令任务执行花费时间："+(etime-btime)/1000+"秒");
				isRunning = false;
				num++;
			} catch (Exception e) {

				com.autonavi.directl.Log.getInstance().errorLog("定时器下发指令错误", e);
				isRunning = false;
			}
		}
	}

	/**
	 * 根据指令状态获取指令状态对象
	 * 
	 * @param flag
	 * @return
	 */
	private synchronized ArrayList<InstructionBean> getInstructionsByState(
			String flag) {
		ArrayList list = new ArrayList();
		CallableStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;

		conn = DbOperation.getConnection();
//		if (conn == null) {
//			try {
//				wait();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			notifyAll();
//		}
		String sql = "{call ?:= PROC_READ_STRUCTION(?)}";

		try {

			pst = conn.prepareCall(sql);
			pst.registerOutParameter(1, OracleTypes.CURSOR);
			pst.setString(2, flag);
			pst.execute();
			rs = (ResultSet) pst.getObject(1);

			while (rs.next()) {
				InstructionBean bean = new InstructionBean();
				bean.setInstuction(rs.getString("instruction"));
				bean.setState(flag);
				bean.setSimcard(rs.getString("device_id"));
				bean.setReply(rs.getString("reply"));
				bean.setType(rs.getString("type"));
				bean.setId(rs.getLong("id"));
				bean.setCreateDate(rs.getTimestamp("createdate"));
				list.add(bean);
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			com.autonavi.directl.Log.getInstance().errorLog("查询待发指令异常", e);// ("#########SQL:"+sql+"\r########caseSQL:"+caseSql);

		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DbOperation.release(null, rs, null, conn);
		}

		return list;
	}

	private boolean updateInstructionsState(ArrayList<InstructionBean> beanList) {
		ArrayList list = new ArrayList();
		Connection conn = DbOperation.getConnection();
		PreparedStatement pst = null;
		Statement stm = null;

		boolean flag = false;

		if (beanList == null || beanList.size() <= 0)
			return false;
		String sql = "update t_structions set state=? where id=?";
		// String caseSql = "update t_speed_case set flag=0 where device_id="+;

		try {

			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);// conn.createStatement();
			stm = conn.createStatement();

			for (int i = 0; i < beanList.size(); i++) {
				// String sql ="update t_structions set
				// state='"+beanList.get(i).getState()+"' where
				// id="+beanList.get(i).getId();
				pst.setString(1, beanList.get(i).getState());
				pst.setLong(2, beanList.get(i).getId());
				pst.addBatch();

				String caseSql = "";
				if (beanList.get(i).getType().equals("3")) {
					caseSql = "update t_speed_case set flag=0 where flag=1 and device_id="
							+ beanList.get(i).getSimcard();
				} else if (beanList.get(i).getType().equals("4")) {
					caseSql = "update t_area set flag=0 where flag=1 and device_id="
							+ beanList.get(i).getSimcard();
				}
				if (caseSql != null && caseSql.trim().length() > 0) {
					stm.addBatch(caseSql);
				}

				com.autonavi.directl.Log.getInstance().outLog(
						"#########SQL:" + sql + "\r########caseSQL:" + caseSql);
			}

			stm.executeBatch();
			pst.executeBatch();

			conn.commit();
			conn.setAutoCommit(true);
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
			flag = false;
			com.autonavi.directl.Log.getInstance().errorLog("更新指令状态异常", e);

		} finally {
			DbOperation.release(stm, null, pst, conn);
		}
		return flag;
	}

}
