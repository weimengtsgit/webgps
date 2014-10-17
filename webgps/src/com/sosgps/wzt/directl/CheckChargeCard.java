package com.sosgps.wzt.directl;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description: 检测充值卡工具类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: sjw
 * </p>
 * 
 * @author sosgps
 * @version 1.0
 */
public class CheckChargeCard {
	public CheckChargeCard() {
	}

	/**
	 * 由输入的手机号和epid判断此终端是否充值有效
	 * 
	 * @param phonenum
	 *            String
	 * @param epid
	 *            String
	 * @return boolean
	 */
	public static boolean isValid(String phonenum) {
		// if(phonenum==null||phonenum.length()!=11)return false;
		// boolean flag = false;
		// java.sql.Statement stmt = null;
		// java.sql.Connection conn = null;
		// java.sql.ResultSet rs = null;
		// String sql = null;
		// try {
		// conn =
		// com.sosgps.db.DBConnectionManager.getInstance().getConnection();
		// if (conn == null) {
		// throw new Exception("");
		// }
		// stmt = conn.createStatement();
		// sql="select cc.* from ep_chargecard cc,ep_users u,ep_utmapscmitems
		// ut,ep_terminals t where "
		// +"u.urid=ut.urid and ut.loctmid=t.tmid and cc.urid=u.urid and "
		// +"cc.isvalid=1 and cc.isbind=1 and cc.ktrq<sysdate and
		// cc.jzrq>sysdate and cc.issend=1 and t.simcard='"+phonenum+"'";
		// rs=stmt.executeQuery(sql);
		// if(rs.next()&&rs.getString("IDCODE")!=null){
		// flag=true;
		// }
		// }
		// catch (Exception ex) {
		// Log.getInstance().outLog("验证卡操作失败："+ex.getMessage());
		// }finally{
		// com.sosgps.db.DBConnectionManager.close(conn,stmt,rs);
		// }
		// return flag;
		return true;
	}

}
