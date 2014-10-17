package com.sosgps.wzt.system.common;

import java.util.HashSet;
import java.util.Set;

public class Constants {
	public static final String GLOBAL_FORWARD_LOGIN = "login";
	
	public static final String GLOBAL_FORWARD_LOGOUT = "logout";

	public static final String GLOBAL_FORWARD_ERROR = "error";

	public static final String GLOBAL_FORWARD_SUCCESS = "createSuccess";
	
	public static final String EMP_ROOT = "empRoot";
	
	//短信超时时间
	public static final int MESSAGE_OUT_TIME_VALUE=70;
	
	//企业默认角色
	public static final String EMP_DEFAULT_ROLE = "empDefaultRole";
	
	//报表模块生成临时问价的路径
	public static final String TEMP_PATH="/opt/temp/";
	//public static final String TEMP_PATH="D:\\temp\\";
	//图表下载http头
	//public static final String CHART_HTTP="http://localhost:8080";
	public static final String CHART_HTTP="http://localhost";
	//public static final String CHART_PATH="D:\\ww\\apache-tomcat-6.0.29\\temp\\";
	public static final String CHART_PATH="/opt/appservers/apache-tomcat-6.0.26-web/temp/";
	
	public static final Set SESSION_KEYS_SET = new HashSet();
	/**
	 * 记录所有存储到session中的objects对应的key值，
	 * 在退出登录时需要进行相应的remove操作。
	 * @param key
	 */
	public static void registerKeys(String key){
		SESSION_KEYS_SET.add(key);
	}
}
