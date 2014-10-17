package com.sosgps.wzt.manage.common;

/**
 * @Title:资源管理模块常用变量
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 下午12:59:21
 */
public class Constants {
	/**
	 * 企业管理日志
	 */
	public static final String ADD_ENT = "新增企业";
	public static final String EDIT_ENT = "修改企业";
	public static final String DELETE_ENT = "删除企业";
	public static final String FIND_ENT = "查找企业";
	public static final String ENT_LIST = "企业列表";
	public static final String FIND_ENT_BY_ENTCODE = "根据企业代码查找企业";
	public static final String EDIT_ENT_VISION = "修改企业视野";
	public static final String ADD_ENT_TERM_TYPE = "新增企业终端类型";
	public static final String EDIT_ENT_TERM_TYPE = "修改企业终端类型";
	public static final String DELETE_ENT_TERM_TYPE = "删除企业终端类型";
	public static final String ADD_ENT_SUITE = "新增企业计费套餐";
	public static final String EDIT_ENT_SUITE = "修改企业计费套餐";
	public static final String DELETE_ENT_SUITE = "删除企业计费套餐";
	/**
	 * 终端组管理日志
	 */
	public static final String VIEW_USER_TERMINAL_GROUP = "查看用户终端组";
	public static final String VIEW_ENT_TERMINAL_GROUP = "查看企业终端组";
	public static final String ADD_TERMINAL_GROUP = "增加终端组";
	public static final String EDIT_TERMINAL_GROUP = "修改终端组";
	public static final String DELETE_TERMINAL_GROUP = "删除终端组";
	public static final String MOVE_TERMINAL_GROUP = "移动终端组";
	public static final String USER_LIST = "企业用户列表";
	public static final String SET_USER_TERM_GROUP = "设置用户可见终端组";
	
	/**
	 * 终端管理日志
	 */
	public static final String FIND_TERMINAL_FROM_GROUP = "终端组下查找终端";
	public static final String EDIT_TERMINAL_IN_GROUP = "调整终端所属组";
	public static final String EDIT_GROUP_IN_GROUP = "终端组所属关系调整";
	/**
	 * 企业POI管理日志
	 */
	
	/**
	 * 企业采样设置管理日志
	 */
	
	/**
	 * 错误日志
	 */
	// dao实例未注入
	public static final String ERROR_DAO_INSTANCE_NULL = "DAO实例未注入";
	public static final String ERROR_TYPE_TRANSFORM_WRONG = "类型转换错误";
	public static final String ERROR_TRANSIENT_INSTANCE_NOT_EXIST = "瞬态实例不存在";
	public static final String ERROR_RESPONSE_WRITE = "response写入错误";
	public static final String ERROR_DAO = "数据库层失败";
	public static final String ERROR_OPT_LOG_SAVE = "保存操作日志失败";
}
