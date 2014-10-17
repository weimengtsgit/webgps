//document.write('<script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-lang-zh_CN.js"></script>');
(function() {
	function Main() {
		this.user = "User";
		this.welcome = "Welcome";
		this.location_platform = "Location Platform";
		this.management_center = "Management Center";
		this.info_acquisition = "Info Acquisition";
		this.view_distribution_poi = "View the distribution of marked points";
		this.view_distribution_area = "view Distribution Area";
		this.location_services = "Location Services";
		this.map_search = "Map Search";
		this.change_password = "Change Password";
		this.exit_the_system = "Log out";
		this.tips = "Tips";
		this.are_you_sure_you_want_to_exit_the_system = "Are you sure you want to exit the system?";
		this.management_system = "Management System";
		this.ok = "ok";
		this.set_layer_visible = "设置图层可见";
		this.close = "close";
		this.layer_control = "图层控制";
		this.set_map_bounds = "地图视野设定";
		this.save_map_bounds = "保存当前地图视野";
		this.are_you_sure_you_want_to_set_the_current_bounds_to_view_the_map_after_login = "您确定要设置当前视野为登录系统后的地图视野?";
		this.data_not_found = "没有有效的定位数据!";
		this.position = "位&nbsp;&nbsp;置：";
		this.add = "Add";
		this.modify = "Modify";
		this.del = "Del";
		this.bind = "Bind";
		this.back = "Back";
		this.start_time = "Start Time";
		this.end_time = "End Time";
		this.select_terminal = "请选择终端!";
		this.loading = "loading...";
		this.name = "名称";
		this.department = "Department";
		this.cell_phone_number = "Mobile No.";
		this.boot_time = "Boot Time";
		this.off_time = "Off Time";
		this.operating = "Details";
		this.detail_information = "More Information";
		this.start_time = "Start Time";
		this.end_time = "End Time";
		this.key_word = "Key Word";
		this.search = "Search";
		this.oupput_from_excel = "Oupput From Excel";
		this.time = "Time";
		this.position_description = "位置描述";
		this.visiting_customers = "到访客户";
		this.please_select_start_time = "请选择开始时间";
		this.please_select_end_time = "请选择结束时间";
		this.export_report = "Export Report";
		this.export_ = "Export";
		this.operation_failure = "Operation Failure";
		this.operation_is_successful = "Operation Successful";
		this.cancel = "Cancel";
		this.retention_period = "Retention Period";
		this.minutes = "Minutes";
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
		this.salesman_visit_statistics = "Salesman Visit Statistics";
		this.client_visited_statistics = "Client Visited Statistics";
		this.detailed_log_report = "Detailed Log Report";
		this.regional_alarming_statistics = "Regional Alarming Statistics";
		this.speeding_alarming_statistics = "Speeding Alarming Statistics";
		this.active_alarm_statistics = "Active Alarm Statistics";
		this.mileage_statistics = "Kilometre Statistics";
		this.vehicle_location_information = "Vehicle Location Information";
		this.vehicle_msg_stat = "车辆信息统计";

	}
	window.report = new Report();
	
	function CusVisitStat(){
		this.visit_name = "到访客户";
		this.vehicle_number = "拜访业务员";
		this.arrive_time = "到达时间";
		this.leave_time = "离开时间";
		this.visitName_ = "Mark Name";
		this.visitCount = "Times of Visited";
		
	}
	window.cusVisitStat = new CusVisitStat();
	
	function AttendanceStat(){
		this.salesmanName = "Salesman Name";
		this.department = "Department";
		this.mobile = "Mobile No.";
		this.noValidData = "No Valid Data";
	}
	window.attendanceStat = new AttendanceStat();
	
	function DistanceStat(){
		this.vehicleNumber = "Salesman Name";
		this.distance = "Kilometre";
		this.tjdate = "Date";
		
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
		this.vehicleNumber = "Salesman Name";
		this.visitCount = "Times of Visited";
		this.visitCusCount = "Client Visited by Salesman";
		this.visitPlaceCount = "Location Visited by Salesman";
		this.map = "Map";
		this.detail_information = "More Information";
		this.visitName = "到访客户";
		this.arriveTime = "到达时间";
		this.leaveTime = "离开时间";
		this.stayTime = "停留时间(分)";
		this.pd = "位置描述";
		
	}
	window.visitStat = new VisitStat();
	
	function AlarmCenter(){
		this.speedAlarm = "Speed Alarm";
		this.areaAlarm = "Area Alarm";
		this.holdAlarm = "Hold Alarm";
		this.emergencyAlarm = "Emergency Alarm";
		this.deviationAlarm = "Deviation Alarm";
		this.in_area = "In Area";
		this.out_area = "Out Area";
		this.alarmMessage = "Alarm Message";
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
		this.time = "Alarm Time";
		this.process = "Process";

	}
	window.alarmCenter = new AlarmCenter();
	
	function ShowBtnChlick(){
		this.currentTime = "Current Time";
		this.optionalTime = "Optional Time";
	}
	window.showBtnChlick = new ShowBtnChlick();
	
	function ControlBtnClick(){
		this.outageOfOilAndPower = "Outage Of Oil And Power";
		this.recoveryOfOilAndPower = "Recovery Of Oil And Power";
		this.alarmDisable = "Alarm Disable";
		this.liftingSpeed = "Lifting Speed";
		this.dischargeArea = "Discharge Area";
		
	}
	window.controlBtnClick = new ControlBtnClick();
	
})();

