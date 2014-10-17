package com.sosgps.wzt.system.common;

import java.util.HashSet;
import java.util.Set;

public class Constants {
	public static final String GLOBAL_FORWARD_LOGIN = "login";
	
	public static final String GLOBAL_FORWARD_LOGOUT = "logout";

	public static final String GLOBAL_FORWARD_ERROR = "error";

	public static final String GLOBAL_FORWARD_SUCCESS = "createSuccess";
	
	public static final String EMP_ROOT = "empRoot";
	
	//���ų�ʱʱ��
	public static final int MESSAGE_OUT_TIME_VALUE=70;
	
	//��ҵĬ�Ͻ�ɫ
	public static final String EMP_DEFAULT_ROLE = "empDefaultRole";
	
	//����ģ��������ʱ�ʼ۵�·��
	public static final String TEMP_PATH="/opt/temp/";
	//public static final String TEMP_PATH="D:\\temp\\";
	//ͼ������httpͷ
	//public static final String CHART_HTTP="http://localhost:8080";
	public static final String CHART_HTTP="http://localhost";
	//public static final String CHART_PATH="D:\\ww\\apache-tomcat-6.0.29\\temp\\";
	public static final String CHART_PATH="/opt/appservers/apache-tomcat-6.0.26-web/temp/";
	
	public static final Set SESSION_KEYS_SET = new HashSet();
	/**
	 * ��¼���д洢��session�е�objects��Ӧ��keyֵ��
	 * ���˳���¼ʱ��Ҫ������Ӧ��remove������
	 * @param key
	 */
	public static void registerKeys(String key){
		SESSION_KEYS_SET.add(key);
	}
}
