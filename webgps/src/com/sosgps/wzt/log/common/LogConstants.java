package com.sosgps.wzt.log.common;

/**
 * <p>
 * Title: LogConstants
 * </p>
 * <p>
 * Description: 日志类型
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: 图盟科技
 * </p>
 * 
 * @author 位置通项目组 张卫
 * @version 1.0
 */
public class LogConstants {
	/* 用户角色权限变量 */
	public static final String MANAGER_F_CODE = "F-005";// 用户角色权限模块
	public static final String MANAGER_C_MODULEGROUP_CODE = "F-005-001";// 权限组模块
	public static final String MANAGER_C_MODULE_CODE = "F-005-002";// 权限模块
	public static final String MANAGER_C_ROLE_CODE = "F-005-003";// 角色模块
	public static final String MANAGER_C_USER_CODE = "F-005-004";// 用户模块
	/* 日志管理变量 */
	public static final String LOG_F_CODE = "F-006";// 日志管理
	public static final String LOG_C_LOGIN_CODE = "F-006-001";// 登录日志
	public static final String LOG_C_OPT_CODE = "F-006-002";// 操作日志

	public static final String LOCATE_SERVICE_F_CODE = "";

	/* 资源管理模块日志 */
	public static final String LOG_SOURCE_MANAGE = "F-002";// 资源管理
	public static final String LOG_SOURCE_MANAGE_ENT = "F-002-001";// 企业管理
	public static final String LOG_SOURCE_MANAGE_TERMINAL = "F-002-002";// 终端管理
	public static final String LOG_SOURCE_MANAGE_TERMINAL_GROUP = "F-002-003";// 终端组管理
	public static final String LOG_SOURCE_MANAGE_POI = "F-002-004";// POI管理
	public static final String LOG_SOURCE_MANAGE_SAMPLE = "F-002-005";// 采样设置管理
	public static final String LOG_SOURCE_MANAGE_ALARM = "F-002-006";// 采样设置管
	public static final String LOG_SOURCE_SPEED_ALARM = "F-002-007";// 超速报警设置
	public static final String LOG_SOURCE_AREA_ALARM = "F-002-008";// 区域报警设置
	public static final String LOG_SOURCE_INTERVAL_FRE = "F-002-009";// GPS上报频率设置
	public static final String LOG_SOURCE_TAKE_PHOTL = "F-002-010";// 拍摄照片指令
	public static final String LOG_SOURCE_OIL_ELEC = "F-002-011";// 油电控制指令
	public static final String LOG_SOURCE_MESSAGE = "F-002-012";// 调度信息指令
	public static final String LOG_SOURCE_STOP_ALARM = "F-002-013";// 停止报警信息指令
	public static final String LOG_SOURCE_ALARM_PARAM = "F-002-014";// 报警参数信息指令
	public static final String LOG_SOURCE_CAR_TYPE = "F-002-015";// 报警参数信息指令
	public static final String LOG_SOURCE_STRUCTIONS = "F-002-016";// 指令操作
	public static final String LOG_SOURCE_LINE_ALARM = "F-002-017";// 路线报警设置
	
	public static final String LOG_SOURCE_AREA_ADD = "F-002-018";// 新增区域
	public static final String LOG_SOURCE_AREA_UPDATE = "F-002-019";// 修改区域
	public static final String LOG_SOURCE_AREA_DELETE = "F-002-020";// 删除区域
	public static final String LOG_SOURCE_AREA_TERM_DELETE = "F-002-021";// 删除终端设置区域
	public static final String LOG_SOURCE_AREA_TERM_UPDATE = "F-002-022";// 终端设置区域
	
	
	public static final String EDIT_TERMINAL_IN_GROUP = "F-002-020";// 终端移至终端组
	public static final String EDIT_GROUP_IN_GROUP = "F-002-021";// 终端组所属关系调整
	/*短信告知*/
	public static final String LOG_SMS_NOTIFI_PL_MODIFY = "F-002-022";// 批量修改短信告知
	public static final String LOG_SMS_NOTIFI_MODIFY = "F-002-023";// 修改短信告知
	public static final String LOG_SMS_NOTIFI_PL_DELETE = "F-002-024";// 批量删除短信告知
	public static final String LOG_SMS_NOTIFI_DELETE = "F-002-025";// 删除短信告知
	
	//add by 2012-12-17 控制中心
	
	public static final String LOG_SOURCE_RestoreOilPower= "F-002-026"; //恢复油电
	public static final String LOG_SOURCE_LiftPanic = "F-002-027"; //解除劫警
	public static final String LOG_SOURCE_LiftSpeeding = "F-002-028"; //解除超速
	public static final String LOG_SOURCE_LiftArea = "F-002-029"; //解除区域
	public static final String LOG_SOURCE_OffOilPower = "F-002-030";

	/* 统计查询模块日志 F-003 */
	public static final String LOG_STAT = "F-003";// 统计查询
	public static final String LOG_STAT_EMP_LOC = "F-003-001";// 企业定位统计
	public static final String LOG_STAT_PEAK_LOC = "F-003-002";// 定位峰值统计
	public static final String LOG_STAT_USER_LOC = "F-003-003";// 人员定位统计
	public static final String LOG_STAT_EMP = "F-003-004"; // 企业统计
	public static final String LOG_STAT_FUNCTION = "F-003-005"; // 功能统计
	public static final String LOG_STAT_ALARM = "F-003-006"; // 报警统计
	public static final String LOG_STAT_REPORTDETAIL ="F-003-007"; //人员位置详细统计
	public static final String LOG_STAT_SALSEMAN = "F-003-008";//业务员考勤报表统计
	public static final String LOG_STAT_VISIT = "F-003-009";//客户被拜访次数统计
	public static final String LOG_STAT_CUSTOM = "F-003-010";//业务员出访统计
	public static final String LOG_STAT_CAR = "F-003-011";//定位信息统计
	public static final String LOG_STAT_CARINFO = "F-003-012";//车辆信息统计
	public static final String LOG_STAT_CARARRIVAL = "F-003-013";//车辆到达信息统计
	public static final String LOG_STAT_AREAALARM = "F-003-014"; // 区域报警统计
	public static final String LOG_STAT_SPEEDALARM = "F-003-015"; // 超速报警统计
	public static final String LOG_STAT_HOLDALARM = "F-003-016"; // 主动报警统计
	public static final String LOG_STAT_DISTANCE = "F-003-017"; // 里程统计
	public static final String LOG_STAT_HOURDISTANCE = "F-003-018"; // 分时里程统计
	public static final String LOG_STAT_TOTALDISTANCE = "F-003-019"; // 总里程统计
	public static final String LOG_STAT_SENSOR = "F-003-020"; // 门磁信息
	public static final String LOG_STAT_ATTENDANCE_RECORD = "F-003-021"; // 考勤统计
	public static final String LOG_STAT_STRUCTIONS_RECORD = "F-003-022"; // 指令信息统计
	public static final String LOG_STAT_SignBillDetail = "F-003-023"; // 签单额明细
	public static final String LOG_STAT_CashDetail = "F-003-024"; // 回款额明细
	public static final String LOG_STAT_CostDetail = "F-003-025"; // 费用额明细
	public static final String LOG_STAT_VisitReport = "F-003-026"; // 查询客户拜访详细记录
	public static final String LOG_STAT_VisitRank = "F-003-027"; // 查询客户拜访大排名
	public static final String LOG_STAT_CusVisit_v2_1 = "F-003-028"; // 客户被拜访次数统计
	public static final String LOG_STAT_CusVisit_Detail_v2_1 = "F-003-029"; // 客户被拜访统计详细信息
	public static final String LOG_STAT_Visit_v2_1 = "F-003-030"; // 业务员出访统计
	public static final String LOG_STAT_Visit_Detail_v2_1 = "F-003-031"; // 业务员出访详细信息
	
	//add by 2012-12-17 zss 
	public static final String LOG_STAT_SpeedExcel = "F-003-036"; // 超速报警统计
	public static final String LOG_STAT_RemoveAllAlarm = "F-003-037"; //清除报警
	/*add by 2012-12-17 zss 销售报表*/
	public static final String LOG_STAT_SalesReported  = "F-003-032";// 查询销售上报 
	public static final String LOG_STAT_PromotionaRreported = "F-003-033"; // 查询促销上报
	public static final String LOG_STAT_SalesExcel  = "F-003-034";//  导出销售上报 
	public static final String LOG_STAT_PromotionaExcel = "F-003-035"; // 导出促销上报
	
	/*add by 2012-12-18 zss 考勤 */
	public static final String LOG_STAT_Attendance = "F-003-038"; // 考勤明细
	public static final String LOG_STAT_AttendanceExcel = "F-003-039"; //导出公司级考勤数据
	public static final String LOG_STAT_AttendanceShowCompany = "F-003-040"; // 展示公司级的考勤数据
	public static final String LOG_STAT_AttendanceShowStaff = "F-003-041"; // 展示人员考勤数据
	public static final String LOG_STAT_AttendanceStaffExcel = "F-003-042"; // 导出人员考勤汇总报表下载
	public static final String LOG_STAT_AttendanceReconstructedExcel = "F-003-043"; // 重构导出人员考勤汇总报表下载
	public static final String LOG_STAT_AttendanceModifyl = "F-003-044"; //人员考勤报表修改 
	
	/*add by 2012-12-18 zss 差旅*/
	public static final String LOG_STAT_TravelListExcel = "F-003-045"; //导出差旅信息汇总 
	public static final String LOG_STAT_TravelList = "F-003-046"; //查询差旅信息明细 
	public static final String LOG_STAT_TravelVerify = "F-003-047"; //差旅审核 
	
	/*add by 2012-12-18 zss 业务员*/
	public static final String LOG_STAT_VisitListExcel = "F-003-048"; //导出拜访考勤表Excel
	public static final String LOG_STAT_VisitExcel = "F-003-049"; //导出签到签出记录
	public static final String LOG_STAT_VisitRanking = "F-003-050"; //查询客户拜访大排名列表
	public static final String LOG_STAT_VisitRankingExcel = "F-003-051"; //导出客户拜访大排名列表Excel
	public static final String LOG_STAT_VisitGraph = "F-003-052"; //取得业务员出访趋势图
	public static final String LOG_STAT_VisitHistorical  = "F-003-053"; //取得业务员出访历史趋势图
	public static final String LOG_STAT_VisitedGraph = "F-003-054"; //取得客户被拜访趋势图
	public static final String LOG_STAT_VisitedHistorical  = "F-003-055"; //取得客户被拜访历史趋势图

	public static final String LOG_STAT_VisitDetail  = "F-003-056"; //查询业务员出访详细记录
	public static final String LOG_STAT_VisitedStatistics  = "F-003-057"; //客户被拜访统计
	public static final String LOG_STAT_VisitStatistics  = "F-003-058"; //查询业务员出访统计表  
	public static final String LOG_STAT_VisitStatisticsDetail  = "F-003-059"; //查询业务员出访详细表
	public static final String LOG_STAT_VisitedDetail  = "F-003-060"; //查询详细的被拜访客户信息LOG_STAT_VisitedDashboard
	public static final String LOG_STAT_VisitedDashboard  = "F-003-061"; //客户被拜访仪表盘
	public static final String LOG_STAT_VisitDashboard  = "F-003-062"; //员工出访达成率仪表盘

	/*add by 2012-12-18 zss 手机监控模块*/
	public static final String LOG_STAT_MobileList  = "F-003-063"; //员工出访达成率仪表盘
	
	/*add by 2012-12-18 zss  签单报表*/
	public static final String LOG_STAT_SigningGraph  = "F-003-064"; //取得签单额趋势图
	public static final String LOG_STAT_SigningHistoryGraph  = "F-003-065"; //取得签单额历史趋势图
	public static final String LOG_STAT_SigningDetail  = "F-003-066"; //签单额明细报表查询
	public static final String LOG_STAT_SigningExcel  = "F-003-067"; //签单额明细报表导出
	
	public static final String LOG_STAT_SigningAudit  = "F-003-068"; //取得签单额历史趋势图
	public static final String LOG_STAT_SigningDashboard  = "F-003-069"; //取得签单额趋势图
	
	/*add by 2012-12-18 zss  客户*/
	public static final String LOG_STAT_VisitedByDetail  = "F-003-070"; //查询某个客户被拜访详细信息
	public static final String LOG_STAT_VisitCustomerDetail  = "F-003-071"; //查看拜访次数详细统计(某个业务员拜访客户详细)
	
	/*add by 2012-12-18 zss  客户*/
	public static final String LOG_STAT_AttendanceDetail  = "F-003-072"; //查看业务员考勤报表明细统计
	public static final String LOG_STAT_VisitNumber  = "F-003-073"; //查询拜访地点数
	
	
	/* 导出明细 */
	public static final String LOG_Exp = "F-004";// 导出操作
	public static final String LOG_Exp_SignBillDetail = "F-004-001"; // 导出签单额明细
	public static final String LOG_Exp_CashDetail = "F-004-002"; // 导出回款额明细
	public static final String LOG_Exp_CostDetail = "F-004-003"; // 导出费用额明细
	public static final String LOG_Exp_VisitReport = "F-004-004"; // 导出客户拜访详细记录
	public static final String LOG_Exp_VisitRank = "F-004-005"; // 导出客户拜访大排名
	
	/* 审核操作 */
	public static final String LOG_Approved = "F-007";// 审核操作
	public static final String LOG_Approved_SignBill = "F-007-001"; // 审核签单
	public static final String LOG_Approved_Cash = "F-007-002"; // 审核回款
	public static final String LOG_Approved_Cost = "F-007-003"; // 审核费用
	public static final String LOG_Approved_Visit = "F-007-004"; // 审核客户拜访

	/* 仪表盘 */
	public static final String LOG_Gauge = "F-011";// 仪表盘
	public static final String LOG_Gauge_SignBill = "F-011-001"; // 签单
	public static final String LOG_Gauge_Cash = "F-011-002"; // 回款
	public static final String LOG_Gauge_Cost = "F-011-003"; // 费用
	public static final String LOG_Gauge_Visit = "F-011-004"; // 员工出访达成率
	public static final String LOG_Gauge_CusVisit = "F-011-005"; // 客户拜访覆盖率
	
	/* 终端参数 */
	public static final String TERMINAL_PARAMTER = "F-008";
	public static final String TERMINAL_PARAMTER_QUERY = "F-008-01";// 参数查询
	/* 采样设置 */
	public static final String SIMPLE_SET = "F-009";// 采样设置
	public static final String SIMPLE_SET_ADD = "F-009-001";// 新增采样规则
	public static final String SIMPLE_SET_MODIFY = "F-009-002";// 修改采样规则
	public static final String SIMPLE_SET_DELETE = "F-009-003";// 删除采样规则
	public static final String SIMPLE_SET_TERMINAL = "F-009-004";// 终端设置采样
	public static final String SIMPLE_SET_CANCEL = "F-009-005";// 终端取消采样
	/* 考勤设置 */
	public static final String ATTENDANCE_SET = "F-010";// 考勤设置
	public static final String ATTENDANCE_SET_ADD = "F-010-001";// 新增考勤规则
	public static final String ATTENDANCE_SET_MODIFY = "F-010-002";// 修改考勤规则
	public static final String ATTENDANCE_SET_DELETE = "F-010-003";// 删除考勤规则
	public static final String ATTENDANCE_SET_TERMINAL = "F-010-004";// 终端新增考勤规则
	public static final String ATTENDANCE_SET_CANCEL = "F-010-005";// 终端取消考勤规则
	public static final String ATTENDANCE_SET_CANCEL_ALL = "F-010-006";// 终端取消所有考勤规则

	
	/* 手机短信 */
	public static final String LBSATT_SET = "F-012";
	public static final String LBSATT_SET_INSERTDB = "F-012-001"; //新增
	
	/*图层控制*/
	public static final String POIMANAGE_SET = "F-013"; 
	public static final String POIMANAGE_SET_UPDATELAYERS = "F-013-001";//更新图层显示属性
	
	/*分速统计*/
	public static final String SPEEDMANAGE_SET="F-014";
	public static final String SPEEDMANAGE_SET_showDayList = "F-014-001";
	public static final String SPEEDMANAGE_SET_showMonthList = "F-014-002";
	public static final String SPEEDMANAGE_SET_showYearList = "F-014-003";
	
	/*定时程序*/
	public static final String TIMER_SET="F-015";
	public static final String TIMER_SET_GETLOCDESC="F-015-001";
	
	/*轨迹相关*/
	public static final String TRACK_REPLAY="F-016";
	public static final String TRACK_REPLAY_QUERY="F-016-001";//轨迹查询
	public static final String TRACK_REPLAY_REALTIEMLOC="F-016-002";//随时定位
	
	/*短信*/
	public static final String SMS_SET="F-017";
	public static final String SMS_SET_SEND="F-017-001";//短信发送
	public static final String SMS_SET_QUERY="F-017-002";//已发送短信查询
	public static final String SMS_SET_RECEIVE="F-017-003";//查询所有接收短信查询
	
	//add by 2012-12-17 zss
	public static final String SMS_SET_UNREAD="F-017-004";//查询未读短信
	public static final String SMS_SET_UNREADEXCEL="F-017-005";//导出未读短信
	public static final String SMS_SET_UNREADUPDATE="F-017-006";//修改未读短信为已读
	public static final String SMS_SET_RECEIVEEXCEL="F-017-007";//导出所有接收短信
	public static final String SMS_SET_QUERYEXCEL="F-017-008";//导出已发送短信
	

	/*业务员日志*/
	public static final String DIARY_SET="F-018";
	public static final String DIARY_SET_QUERY_USER="F-018-001";//查询登录业务员日志
	public static final String DIARY_SET_ADD_USER="F-018-002";//添加业务员日志
	public static final String DIARY_SET_UPDATE_USER="F-018-003";//修改业务员日志
	public static final String DIARY_SET_REMARK_USER="F-018-004";//批注业务员日志
	
	/*加油记录*/
	public static final String OILING_SET = "F-019";
	public static final String OILING_SET_ADD = "F-019-001";    //添加
	public static final String OILING_SET_UPDATE = "F-019-002"; //修改
	public static final String OILING_SET_DELETE = "F-019-003"; //删除
	
	/*车辆费用记录*/
	public static final String VEHICLE_EXPENSE_SET = "F-020";
	public static final String VEHICLE_EXPENSE_SET_ADD = "F-020-001";    //添加
	public static final String VEHICLE_EXPENSE_SET_UPDATE = "F-020-002"; //修改
	public static final String VEHICLE_EXPENSE_SET_DELETE = "F-020-003"; //删除
	
	/*车辆类型*/
	public static final String CAR_TYPE_INFO_SET = "F-021";
	public static final String CAR_TYPE_INFO_SET_ADD = "F-021-001";    //添加
	public static final String CAR_TYPE_INFO_SET_UPDATE = "F-021-002"; //修改
	public static final String CAR_TYPE_INFO_SET_DELETE = "F-021-003"; //删除

	/*业务员日志标注*/
	public static final String DIARY_MARK_SET="F-022";
	public static final String DIARY_MARK_SET_QUERY_USER="F-022-001";//查询登录业务员日志
	public static final String DIARY_MARK_SET_ADD_USER="F-022-002";//添加业务员日志
	public static final String DIARY_MARK_SET_UPDATE_USER="F-022-003";//修改业务员日志
	public static final String DIARY_MARK_SET_REMARK_USER="F-022-004";//批注业务员日志
	
	/*车辆保险记录标注*/
	public static final String INSURANCE_SET="F-023";
	public static final String INSURANCE_SET_QUERY="F-023-001";//查询保险
	public static final String INSURANCE_SET_ADD="F-023-002";//添加保险
	public static final String INSURANCE_SET_UPDATE="F-023-003";//修改保险
	public static final String INSURANCE_SET_DELETE="F-023-004";//删除保险
	
	/*车辆年审记录标注*/
	public static final String ANNUAL_EXAMINATION_SET="F-024";
	public static final String ANNUAL_EXAMINATION_SET_QUERY="F-024-001";//查询年审记录
	public static final String ANNUAL_EXAMINATION_SET_ADD="F-024-002";//添加年审记录
	public static final String ANNUAL_EXAMINATION_SET_UPDATE="F-024-003";//修改年审记录
	public static final String ANNUAL_EXAMINATION_SET_DELETE="F-024-004";//删除年审记录

	/*车辆维护记录标注*/
	public static final String VEHICLES_MAINTENANCE_SET="F-025";
	public static final String VEHICLES_MAINTENANCE_SET_QUERY="F-025-001";//查询车辆维护记录
	public static final String VEHICLES_MAINTENANCE_SET_ADD="F-025-002";//添加车辆维护记录
	public static final String VEHICLES_MAINTENANCE_SET_UPDATE="F-025-003";//修改车辆维护记录
	public static final String VEHICLES_MAINTENANCE_SET_DELETE="F-025-004";//删除车辆维护记录

	/*过路过桥记录标注*/
	public static final String Toll_SET="F-026";
	public static final String Toll_SET_QUERY="F-026-001";//查询过路过桥记录
	public static final String Toll_SET_ADD="F-026-002";//添加过路过桥护记录
	public static final String Toll_SET_UPDATE="F-026-003";//修改过路过桥记录
	public static final String Toll_SET_DELETE="F-026-004";//删除过路过桥记录
	
	/*驾照年审记录标注*/
	public static final String DriverLicense_SET="F-027";
	public static final String DriverLicense_SET_QUERY="F-027-001";//查询驾照年审记录
	public static final String DriverLicense_SET_ADD="F-027-002";//添加驾照年审记录
	public static final String DriverLicense_SET_UPDATE="F-027-003";//修改驾照年审记录
	public static final String DriverLicense_SET_DELETE="F-027-004";//删除驾照年审记录
	
}
