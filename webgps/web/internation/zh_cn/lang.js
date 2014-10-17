document.write('<script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-lang-zh_CN.js"></script>');
(function() {
	function Main() {
		this.user = "用户";
		this.welcome = ",欢迎您!";
		this.location_platform = "定位平台";
		this.management_center = "管理中心";
		this.info_acquisition = "信息采集";
		this.view_distribution_poi = "查看已分配标注点";
		//this.view_distribution_poi = "View the distribution of marked points";
		this.view_distribution_area = "查看已分配电子围栏";
		this.location_services = "定位服务";
		this.map_search = "地图搜索";
		this.change_password = "修改密码";
		this.exit_the_system = "退出系统";
		this.tips = "提示";
		this.are_you_sure_you_want_to_exit_the_system = "您确定要退出系统吗?";
		this.management_system = "后台管理系统";
		this.ok = "确定";
		this.set_layer_visible = "设置图层可见";
		this.close = "关闭";
		this.layer_control = "图层控制";
		this.set_map_bounds = "地图视野设定";
		this.save_map_bounds = "保存当前地图视野";
		this.are_you_sure_you_want_to_set_the_current_bounds_to_view_the_map_after_login = "您确定要设置当前视野为登录系统后的地图视野?";
		this.data_not_found = "没有有效的定位数据!";
		this.position = "位&nbsp;&nbsp;置：";
		this.add = "新增";
		this.modify = "修改";
		this.del = "删除";
		this.bind = "绑定";
		this.back = "返回";
		this.start_time = "开始时间";
		this.end_time = "结束时间";
		this.select_terminal = "请选择终端!";
		this.loading = "查询中...";
		this.name = "名称";
		this.department = "部门";
		this.cell_phone_number = "手机号码";
		this.boot_time = "开机时间";
		this.off_time = "关机时间";
		this.operating = "操作";
		this.detail_information = "详细信息";
		this.start_time = "开始时间";
		this.end_time = "结束时间";
		this.key_word = "关键字";
		this.search = "查询";
		this.oupput_from_excel = "导出Excel";
		this.time = "时间";
		this.position_description = "位置描述";
		this.visiting_customers = "到访客户";
		this.please_select_start_time = "请选择开始时间";
		this.please_select_end_time = "请选择结束时间";
		this.export_report = "报表导出";
		this.export_ = "导出";
		this.operation_failure = "操作失败";
		this.operation_is_successful = "操作成功";
		this.cancel = "取消";
		this.retention_period = "停留时间";
		this.minutes = "分钟";
	}
	window.main = new Main();
	
	function Area(){
		this.id = "序号";
		this.area_name = "电子围栏名称";
		this.area_type = "电子围栏类型";
		this.select_a_area_msg = "请选择一条电子围栏信息";
		this.already_draw_area = "已绘制电子围栏";
		this.select_area = "请选择电子围栏";
		this.are_you_want_to_del_area = "您确定要删除电子围栏吗?";
		this.view_area_msg = "查看电子围栏";
		this.no_area = "无电子围栏";
		this.draw_area_please = "请绘制电子围栏";
		this.draw_area = "请绘制电子围栏";
		this.input_area_name = "请输入电子围栏名称!";
		this.are_you_want_to_add_area = "您确定要添加新电子围栏吗?";
		this.are_you_want_to_modify_area = "您确定要修改电子围栏吗?";
		this.alarm_type = "报警类型";
		this.in_area = "进区域";
		this.out_area = "出区域";
		
	}
	window.area = new Area();
	
	function Report(){
		this.salesman_visit_statistics = "业务员出访统计";
		this.client_visited_statistics = "客户被拜访统计";
		this.detailed_log_report = "详细日志报表";
		this.regional_alarming_statistics = "区域报警统计";
		this.speeding_alarming_statistics = "超速报警统计";
		this.active_alarm_statistics = "主动报警统计";
		this.mileage_statistics = "里程统计";
		this.vehicle_location_information = "定位信息";
		this.vehicle_msg_stat = "车辆信息统计";
	}
	window.report = new Report();
	
	function CusVisitStat(){
		this.visit_name = "到访客户";
		this.vehicle_number = "拜访业务员";
		this.arrive_time = "到达时间";
		this.leave_time = "离开时间";
		this.visitName_ = "标注名称";
		this.visitCount = "拜访次数";
		
	}
	window.cusVisitStat = new CusVisitStat();
	
	function AttendanceStat(){
		this.salesmanName = "名称";
		this.department = "部门";
		this.mobile = "手机号码";
		this.noValidData = "没有有效的定位数据";
	}
	window.attendanceStat = new AttendanceStat();
	
	function DistanceStat(){
		this.vehicleNumber = "名称";
		this.distance = "公里数";
		this.tjdate = "统计时间";
		
	}
	window.distanceStat = new DistanceStat();

	function HoldAlarm(){
		this.vehicleNumber = "名称";
		this.alarmTime = "报警时间";
		this.pd = "位置描述";
		
	}
	window.holdAlarm = new HoldAlarm();
	
	function SpeedAlarm(){
		this.vehicleNumber = "名称";
		this.alarmTime = "报警时间";
		this.speed = "速度";
		this.speedLimit = "速度阀值";
		this.pd = "位置描述";
		
	}
	window.speedAlarm = new SpeedAlarm();
	
	function VehicleGPSInfo(){
		this.vehicleNumber = "车牌号";
		this.simcard = "手机号码";
		this.latitude = "纬度";
		this.longitude = "经度";
		this.speed = "速度";
		this.direction = "方向";
		this.distance = "里程";
		this.accStatus = "行驶状态";
		this.pd = "位置描述";
		this.visitName = "到访客户";
		this.gpsTime = "时间";
		this.temperature = "温度";
		
	}
	window.vehicleGPSInfo = new VehicleGPSInfo();
	
	function VisitStat(){
		this.vehicleNumber = "业务员名称";
		this.visitCount = "出访次数";
		this.visitCusCount = "出访客户数";
		this.visitPlaceCount = "客户分布";
		this.map = "地图";
		this.detail_information = "详细信息";
		this.visitName = "到访客户";
		this.arriveTime = "到达时间";
		this.leaveTime = "离开时间";
		this.stayTime = "停留时间(分)";
		this.pd = "位置描述";
		
	}
	window.visitStat = new VisitStat();
	
	function AlarmCenter(){
		this.speedAlarm = "超速报警";
		this.areaAlarm = "区域报警";
		this.holdAlarm = "主动报警";
		this.emergencyAlarm = "紧急报警";
		this.deviationAlarm = "偏航报警";
		this.in_area = "进区域";
		this.out_area = "出区域";
		this.alarmMessage = "报警信息";
		this.simcard = "车主电话";
		this.termName = "名称";
		this.vType = "车辆型号";
		this.vNumber = "车牌号";
		this.speed = "速度";
		this.type = "报警类型";
		this.direction = "方向";
		this.maxSpeed = "超速阀值";
		this.areaPoints = "速度";
		this.areaType = "区域类型";
		this.time = "报警时间";
		this.process = "处理";

	}
	window.alarmCenter = new AlarmCenter();
	
	function ShowBtnChlick(){
		this.currentTime = "当前时间";
		this.optionalTime = "自选时间";
	}
	window.showBtnChlick = new ShowBtnChlick();
	
	function ControlBtnClick(){
		this.outageOfOilAndPower = "断油断电";
		this.recoveryOfOilAndPower = "恢复油电";
		this.alarmDisable = "解除劫警";
		this.liftingSpeed = "解除超速";
		this.dischargeArea = "解除区域";
		
	}
	window.controlBtnClick = new ControlBtnClick();
	
})();

