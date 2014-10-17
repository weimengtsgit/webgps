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
		this.set_layer_visible = "����ͼ��ɼ�";
		this.close = "close";
		this.layer_control = "ͼ�����";
		this.set_map_bounds = "��ͼ��Ұ�趨";
		this.save_map_bounds = "���浱ǰ��ͼ��Ұ";
		this.are_you_sure_you_want_to_set_the_current_bounds_to_view_the_map_after_login = "��ȷ��Ҫ���õ�ǰ��ҰΪ��¼ϵͳ��ĵ�ͼ��Ұ?";
		this.data_not_found = "û����Ч�Ķ�λ����!";
		this.position = "λ&nbsp;&nbsp;�ã�";
		this.add = "Add";
		this.modify = "Modify";
		this.del = "Del";
		this.bind = "Bind";
		this.back = "Back";
		this.start_time = "Start Time";
		this.end_time = "End Time";
		this.select_terminal = "��ѡ���ն�!";
		this.loading = "loading...";
		this.name = "����";
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
		this.position_description = "λ������";
		this.visiting_customers = "���ÿͻ�";
		this.please_select_start_time = "��ѡ��ʼʱ��";
		this.please_select_end_time = "��ѡ�����ʱ��";
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
		this.id = "���";
		this.area_name = "����Χ������";
		this.area_type = "����Χ������";
		this.select_a_area_msg = "��ѡ��һ������Χ����Ϣ";
		this.already_draw_area = "�ѻ��Ƶ���Χ��";
		this.select_area = "��ѡ�����Χ��";
		this.are_you_want_to_del_area = "��ȷ��Ҫɾ������Χ����?";
		this.view_area_msg = "�鿴����Χ��";
		this.no_area = "�޵���Χ��";
		this.draw_area_please = "����Ƶ���Χ��";
		this.draw_area = "����Ƶ���Χ��";
		this.input_area_name = "���������Χ������!";
		this.are_you_want_to_add_area = "��ȷ��Ҫ����µ���Χ����?";
		this.are_you_want_to_modify_area = "��ȷ��Ҫ�޸ĵ���Χ����?";
		this.alarm_type = "��������";
		this.in_area = "������";
		this.out_area = "������";
		
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
		this.vehicle_msg_stat = "������Ϣͳ��";

	}
	window.report = new Report();
	
	function CusVisitStat(){
		this.visit_name = "���ÿͻ�";
		this.vehicle_number = "�ݷ�ҵ��Ա";
		this.arrive_time = "����ʱ��";
		this.leave_time = "�뿪ʱ��";
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
		this.vehicleNumber = "����";
		this.alarmTime = "����ʱ��";
		this.pd = "λ������";
		
	}
	window.holdAlarm = new HoldAlarm();
	
	function SpeedAlarm(){
		this.vehicleNumber = "����";
		this.alarmTime = "����ʱ��";
		this.speed = "�ٶ�";
		this.speedLimit = "�ٶȷ�ֵ";
		this.pd = "λ������";
		
	}
	window.speedAlarm = new SpeedAlarm();
	
	function VehicleGPSInfo(){
		this.vehicleNumber = "���ƺ�";
		this.simcard = "�ֻ�����";
		this.latitude = "γ��";
		this.longitude = "����";
		this.speed = "�ٶ�";
		this.direction = "����";
		this.distance = "���";
		this.accStatus = "��ʻ״̬";
		this.pd = "λ������";
		this.visitName = "���ÿͻ�";
		this.gpsTime = "ʱ��";
		this.temperature = "�¶�";
		
	}
	window.vehicleGPSInfo = new VehicleGPSInfo();
	
	function VisitStat(){
		this.vehicleNumber = "Salesman Name";
		this.visitCount = "Times of Visited";
		this.visitCusCount = "Client Visited by Salesman";
		this.visitPlaceCount = "Location Visited by Salesman";
		this.map = "Map";
		this.detail_information = "More Information";
		this.visitName = "���ÿͻ�";
		this.arriveTime = "����ʱ��";
		this.leaveTime = "�뿪ʱ��";
		this.stayTime = "ͣ��ʱ��(��)";
		this.pd = "λ������";
		
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
		this.simcard = "�����绰";
		this.termName = "����";
		this.vType = "�����ͺ�";
		this.vNumber = "���ƺ�";
		this.speed = "�ٶ�";
		this.type = "��������";
		this.direction = "����";
		this.maxSpeed = "���ٷ�ֵ";
		this.areaPoints = "�ٶ�";
		this.areaType = "��������";
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

