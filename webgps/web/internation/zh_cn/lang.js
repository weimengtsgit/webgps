document.write('<script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-lang-zh_CN.js"></script>');
(function() {
	function Main() {
		this.user = "�û�";
		this.welcome = ",��ӭ��!";
		this.location_platform = "��λƽ̨";
		this.management_center = "��������";
		this.info_acquisition = "��Ϣ�ɼ�";
		this.view_distribution_poi = "�鿴�ѷ����ע��";
		//this.view_distribution_poi = "View the distribution of marked points";
		this.view_distribution_area = "�鿴�ѷ������Χ��";
		this.location_services = "��λ����";
		this.map_search = "��ͼ����";
		this.change_password = "�޸�����";
		this.exit_the_system = "�˳�ϵͳ";
		this.tips = "��ʾ";
		this.are_you_sure_you_want_to_exit_the_system = "��ȷ��Ҫ�˳�ϵͳ��?";
		this.management_system = "��̨����ϵͳ";
		this.ok = "ȷ��";
		this.set_layer_visible = "����ͼ��ɼ�";
		this.close = "�ر�";
		this.layer_control = "ͼ�����";
		this.set_map_bounds = "��ͼ��Ұ�趨";
		this.save_map_bounds = "���浱ǰ��ͼ��Ұ";
		this.are_you_sure_you_want_to_set_the_current_bounds_to_view_the_map_after_login = "��ȷ��Ҫ���õ�ǰ��ҰΪ��¼ϵͳ��ĵ�ͼ��Ұ?";
		this.data_not_found = "û����Ч�Ķ�λ����!";
		this.position = "λ&nbsp;&nbsp;�ã�";
		this.add = "����";
		this.modify = "�޸�";
		this.del = "ɾ��";
		this.bind = "��";
		this.back = "����";
		this.start_time = "��ʼʱ��";
		this.end_time = "����ʱ��";
		this.select_terminal = "��ѡ���ն�!";
		this.loading = "��ѯ��...";
		this.name = "����";
		this.department = "����";
		this.cell_phone_number = "�ֻ�����";
		this.boot_time = "����ʱ��";
		this.off_time = "�ػ�ʱ��";
		this.operating = "����";
		this.detail_information = "��ϸ��Ϣ";
		this.start_time = "��ʼʱ��";
		this.end_time = "����ʱ��";
		this.key_word = "�ؼ���";
		this.search = "��ѯ";
		this.oupput_from_excel = "����Excel";
		this.time = "ʱ��";
		this.position_description = "λ������";
		this.visiting_customers = "���ÿͻ�";
		this.please_select_start_time = "��ѡ��ʼʱ��";
		this.please_select_end_time = "��ѡ�����ʱ��";
		this.export_report = "������";
		this.export_ = "����";
		this.operation_failure = "����ʧ��";
		this.operation_is_successful = "�����ɹ�";
		this.cancel = "ȡ��";
		this.retention_period = "ͣ��ʱ��";
		this.minutes = "����";
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
		this.salesman_visit_statistics = "ҵ��Ա����ͳ��";
		this.client_visited_statistics = "�ͻ����ݷ�ͳ��";
		this.detailed_log_report = "��ϸ��־����";
		this.regional_alarming_statistics = "���򱨾�ͳ��";
		this.speeding_alarming_statistics = "���ٱ���ͳ��";
		this.active_alarm_statistics = "��������ͳ��";
		this.mileage_statistics = "���ͳ��";
		this.vehicle_location_information = "��λ��Ϣ";
		this.vehicle_msg_stat = "������Ϣͳ��";
	}
	window.report = new Report();
	
	function CusVisitStat(){
		this.visit_name = "���ÿͻ�";
		this.vehicle_number = "�ݷ�ҵ��Ա";
		this.arrive_time = "����ʱ��";
		this.leave_time = "�뿪ʱ��";
		this.visitName_ = "��ע����";
		this.visitCount = "�ݷô���";
		
	}
	window.cusVisitStat = new CusVisitStat();
	
	function AttendanceStat(){
		this.salesmanName = "����";
		this.department = "����";
		this.mobile = "�ֻ�����";
		this.noValidData = "û����Ч�Ķ�λ����";
	}
	window.attendanceStat = new AttendanceStat();
	
	function DistanceStat(){
		this.vehicleNumber = "����";
		this.distance = "������";
		this.tjdate = "ͳ��ʱ��";
		
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
		this.vehicleNumber = "ҵ��Ա����";
		this.visitCount = "���ô���";
		this.visitCusCount = "���ÿͻ���";
		this.visitPlaceCount = "�ͻ��ֲ�";
		this.map = "��ͼ";
		this.detail_information = "��ϸ��Ϣ";
		this.visitName = "���ÿͻ�";
		this.arriveTime = "����ʱ��";
		this.leaveTime = "�뿪ʱ��";
		this.stayTime = "ͣ��ʱ��(��)";
		this.pd = "λ������";
		
	}
	window.visitStat = new VisitStat();
	
	function AlarmCenter(){
		this.speedAlarm = "���ٱ���";
		this.areaAlarm = "���򱨾�";
		this.holdAlarm = "��������";
		this.emergencyAlarm = "��������";
		this.deviationAlarm = "ƫ������";
		this.in_area = "������";
		this.out_area = "������";
		this.alarmMessage = "������Ϣ";
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
		this.time = "����ʱ��";
		this.process = "����";

	}
	window.alarmCenter = new AlarmCenter();
	
	function ShowBtnChlick(){
		this.currentTime = "��ǰʱ��";
		this.optionalTime = "��ѡʱ��";
	}
	window.showBtnChlick = new ShowBtnChlick();
	
	function ControlBtnClick(){
		this.outageOfOilAndPower = "���Ͷϵ�";
		this.recoveryOfOilAndPower = "�ָ��͵�";
		this.alarmDisable = "����پ�";
		this.liftingSpeed = "�������";
		this.dischargeArea = "�������";
		
	}
	window.controlBtnClick = new ControlBtnClick();
	
})();

