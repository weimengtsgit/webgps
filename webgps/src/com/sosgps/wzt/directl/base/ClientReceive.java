package com.sosgps.wzt.directl.base;

import org.apache.log4j.Logger;

import org.jdom.Document;

import java.io.IOException;
import java.io.InputStream;
import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;
import org.jdom.Element;
import java.util.*;

import com.sosgps.wzt.directl.TerminalFactory;
import com.sosgps.wzt.directl.idirectl.BaseDictate;
import com.sosgps.wzt.directl.idirectl.Terminal;

import java.lang.reflect.*;

/**
 * <p>
 * Title: GPS网关
 * </p>
 * 
 * <p>
 * Description:接收网关接收到的client指令
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: www.sosgps.com
 * </p>
 * 
 * @author musicjiang@sohu.com
 * @version 1.0
 */
public class ClientReceive {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ClientReceive.class);

	public ClientReceive() {
	}

	public String parseAndDeal(String requestID, String simcard, String[] params) {
		String resultCmd = null;
		SAXBuilder sb = new SAXBuilder();
		InputStream is = getClass().getResourceAsStream("/gpsfuncmapping.xml");
		Document doc = null;
		try {
			doc = sb.build(is);
		} catch (JDOMException ex) {
			logger.error("读取gpsfuncmapping.xml异常：", ex);
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		Element root = doc.getRootElement();
		java.util.List gpsfuncs = root.getChildren("gpsfunc");
		for (int i = 0; i < gpsfuncs.size(); i++) {
			Element e = (Element) gpsfuncs.get(i);
			String id = e.getChild("id").getText();
			if (!id.equalsIgnoreCase(requestID)) {
				continue;
			} else {
				String className = e.getChild("class").getText();
				String func = e.getChild("func").getText();
				String desc = e.getChild("desc").getText();
				Element paramList = e.getChild("params");
				List paramtypes = paramList.getChildren("type");
				Class[] c = new Class[paramtypes.size()];
				Iterator it = paramtypes.iterator();
				int j = 0;
				while (it.hasNext()) {
					Element ee = (Element) it.next();
					String classname = ee.getText();
					try {
						c[j] = Class.forName(classname);
						j++;
					} catch (ClassNotFoundException ex1) {
						logger.error("ClientReceive反射类型" + classname + "异常：",
								ex1);
					}
				}
				try {
					TerminalFactory tf = new TerminalFactory();
					Terminal t = tf.createTerminalBySn(simcard);
					Class f = Class.forName(t.getClass().getName());
					Method mm = f.getMethod(className, new Class[] {});
					Object oo = mm.invoke(t, new Object[] {});
					Class ff = Class.forName(oo.getClass().getName());
					Method m = ff.getMethod(func, c);
					// Constructor con = f.getConstructor(new
					// Class[]{TerminalParam.class});
					// Object o = con.newInstance(new
					// Object[]{t.getTerminalParam()});
					Object r = m.invoke(oo, params);
					if (r != null)
						resultCmd = r.toString();
				} catch (Exception ex2) {
					logger.error("ClientReceive异常：", ex2);
					return null;
				}
			}
		}
		return resultCmd;
	}

	public BaseDictate parseAndDeal2(String requestID, String simcard,
			String[] params) {
		BaseDictate result = null;
		SAXBuilder sb = new SAXBuilder();
		InputStream is = getClass().getResourceAsStream("/gpsfuncmapping.xml");
		Document doc = null;
		try {
			doc = sb.build(is);
		} catch (JDOMException ex) {
			logger.error("读取gpsfuncmapping.xml异常：", ex);
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		Element root = doc.getRootElement();
		java.util.List gpsfuncs = root.getChildren("gpsfunc");
		for (int i = 0; i < gpsfuncs.size(); i++) {
			Element e = (Element) gpsfuncs.get(i);
			String id = e.getChild("id").getText();
			if (!id.equalsIgnoreCase(requestID)) {
				continue;
			} else {
				String className = e.getChild("class").getText();
				String func = e.getChild("func").getText();
				String desc = e.getChild("desc").getText();
				Element paramList = e.getChild("params");
				List paramtypes = paramList.getChildren("type");
				Class[] c = new Class[paramtypes.size()];
				Iterator it = paramtypes.iterator();
				int j = 0;
				while (it.hasNext()) {
					Element ee = (Element) it.next();
					String classname = ee.getText();
					try {
						c[j] = Class.forName(classname);
						j++;
					} catch (ClassNotFoundException ex1) {
						logger.error("ClientReceive反射类型" + classname + "异常：",
								ex1);
					}
				}
				try {
					TerminalFactory tf = new TerminalFactory();
					Terminal t = tf.createTerminalBySn(simcard);
					Class f = Class.forName(t.getClass().getName());
					Method mm = f.getMethod(className, new Class[] {});
					Object oo = mm.invoke(t, new Object[] {});
					Class ff = Class.forName(oo.getClass().getName());
					Method m = ff.getMethod(func, c);
					// Constructor con = f.getConstructor(new
					// Class[]{TerminalParam.class});
					// Object o = con.newInstance(new
					// Object[]{t.getTerminalParam()});
					Object r = m.invoke(oo, params);
					if (r != null)
						result = (BaseDictate) oo;
				} catch (Exception ex2) {
					logger.error("ClientReceive异常：", ex2);
					return null;
				}
			}
		}
		return result;
	}

	// test
	public static void main(String[] args) {
		ClientReceive cr = new ClientReceive();
		/**
		 * String requestID = "200"; String simcard = "13401035046"; String[]
		 * params = new String[3]; params[0] = "60.28.28.22"; params[1] =
		 * "7001"; params[2] = "1111";//
		 */
		// /**
		String requestID = "302";
		String simcard = "13958005482";
		String[] params = new String[1];
		params[0] = "adfsdf";// */
		// String cmd = cr.parseAndDeal(requestID,simcard,params);
		// System.out.println(cmd);
		BaseDictate bd = cr.parseAndDeal2(requestID, simcard, params);
		System.out.println(bd);
	}
}
