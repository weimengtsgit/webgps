package com.autonavi.directl;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.autonavi.directl.dbutil.DbOperation;
import com.autonavi.directl.idirectl.Terminal;
import com.autonavi.directl.idirectl.TerminalParam;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.JDOMException;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: 终端工厂，用来创建不同类型的终端设备
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: www.mapabc.com
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class TerminalFactory {
	public TerminalFactory() {
	}

	/**
	 * 得到该类型的终端实现类名称
	 * 
	 * @param pTerminalType：终端类型
	 * @return
	 */
	private String getTerminalClassName(String pTerminalType) {
		String terminalClassName = null;
		SAXBuilder sb = new SAXBuilder();
		InputStream is = getClass().getResourceAsStream(
				"/terminallist.properties");
		Document doc = null;
		 
		try {
			doc = sb.build(is);
		} catch (JDOMException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		Element root = doc.getRootElement();
		java.util.List children = root.getChildren("Terminal");
		for (int i = 0; i < children.size(); i++) {
			Element e = (Element) children.get(i);
			String terminalType = e.getChild("Terminal-name").getText();
			if (pTerminalType.equalsIgnoreCase(terminalType)) {
				Element eTclass = e.getChild("Terminal-class");
				if (eTclass == null) {
					 com.autonavi.directl.Log.getInstance().outLog(terminalType+
					 " 没有下发指令解析类");
					 com.autonavi.directl.Log.getInstance().errorLog(terminalType+
					 " 没有下发指令解析类",null);
					terminalClassName = null;
				} else {
					terminalClassName = e.getChild("Terminal-class").getText();
				}

				return terminalClassName;
			}
		}
		return terminalClassName;
	}

	/**
	 * 得到终端设备实例
	 * 
	 * @param deviceType:设备类型
	 * @return 返回设备类接口
	 */

	private Terminal getTerminalInstance(String deviceType)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		String className = getTerminalClassName(deviceType);
		if (className == null) {
			return null;
		}
		Class c = Class.forName(className);
		Terminal tl = (Terminal) c.newInstance();
		return tl;
	}

 
//	 根据序列号创建终端
	public Terminal createTerminalBySn(String gsn) {

		java.sql.Connection conn = null;
		java.sql.ResultSet rs = null;
		String sql = null;
		if (gsn == null || gsn.trim().length() == 0)
			return null;

		conn = DbOperation.getConnection();//com.mapabc.db.DBConnectionManager.getInstance().getConnection();

		sql = "select * from t_terminal tt where  tt.device_Id =?";

		PreparedStatement pst = null;
		String sn = "";
		String type = "";
		String code = "";
		String oemCode = "";
		String protocol_pwd = "";
		
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, gsn);
			rs = pst.executeQuery();
			if (rs.next()) {
 				 
				code = rs.getString("TYPE_CODE");
				oemCode = rs.getString("OEM_CODE");
				protocol_pwd = rs.getString("PROTOCOL_PWD");
			}
		} catch (SQLException ex1) {
			Log.getInstance().getInstance().outLog(
					"查询指定的异常：" + ex1.getMessage());
		} finally {
 
			DbOperation.release(null, rs, pst, conn);
		}
		 
		TerminalParam tparam = new TerminalParam();
		tparam.setGPRSModal(true);
		tparam.setSimCard(gsn);
		tparam.setSeriesNo(gsn);
		tparam.setOemCode(oemCode);
		tparam.setProtocolPwd(protocol_pwd);
		String gpscode = code;

		Terminal t1 = null;

		try {
			t1 = this.getTerminalInstance(gpscode);
		} catch (ClassNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		if (t1 != null) {
			t1.setTerminalParam(tparam);
		}

		return t1;

	}
	
//	 根据终端类型创建终端
	public Terminal createTerminalByType(String phnum, String code) {
		 
		if (phnum == null || phnum.trim().length() == 0)
			return null;

		 
		TerminalParam tparam = new TerminalParam();
		 
		tparam.setSimCard(phnum);	
		
		String gpscode = code;

		Terminal t1 = null;

		try {
			t1 = this.getTerminalInstance(gpscode);
		} catch (ClassNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		if (t1 != null) {
			t1.setTerminalParam(tparam);
		}

		return t1;

	}

	public static void main(String[] args) {
		TerminalFactory tf = new TerminalFactory();
		 
	}
}
