
//Ա��������
var reportwin;
//Ա������
function report_btn_click(needDeviceIds){
	//���Ա�������ر�ʵʱ׷��
	StopRealtimeTrack();
	//�ر�ʵʱ���ٳ������з���
	StopCarCurrentDirection();
	if(sellreportwin){
		sellreportwin.hide();//�������۱���
	}
	var treeArr = new Array();
    getTreeId(root,treeArr);
	if(treeArr.length<=0 && needDeviceIds){
		Ext.Msg.alert(main.tips, main.select_terminal);
		return;
	}
	if(!reportwin){
    	reportwin = new Ext.Window({
	    	layout:'fit',
	        width:800,
	        height:500,
	        closeAction:'hide',
			maximizable: true,
	        plain: true,
	        items: [{
	        	xtype: 'tabpanel',
	            autoTabs:true,
	            activeTab:0,
	            enableTabScroll:true,
	            deferredRender:false,
	            border:false,
	            id: 'ReportTabPanel',
	            items:[{
	            	xtype: 'panel',
	            	id: 'VisitStatPanel',
	                title: report.salesman_visit_statistics,
	                layout: 'card',
	                //width: 1500,
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(VisitStatGrid); p.add(VisitStatDetailGrid); p.add(VisitStatDetailGrid2); p.add(VisitStatCountChart); p.add(VisitStatPlaceMap); p.getLayout().setActiveItem(0); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'CusVisitStatPanel',
	                title: report.client_visited_statistics,
	                layout: 'card',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CusVisitStatGrid); p.add(CusVisitStatDetailGrid); p.getLayout().setActiveItem(0); p.doLayout(); } } }
	            },
	            {
	            	xtype: 'panel',
	            	id: 'mobileMonitoringRepoPanel',
	                title: '�ֻ�ģ���ر���',
	                layout: 'card',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(mobileMonitoringRepoGrid); p.getLayout().setActiveItem(0); p.doLayout(); } } }
	            },
	            {
	            	xtype: 'panel',
	            	id: 'travelCostTotalReportPanel',
	                title: '������Ϣ���ܱ�',
	                layout: 'card',
	                //width: 1500,
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(travelCostTotalReportGrid); p.getLayout().setActiveItem(0); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'employeeCheckReportPanel',
	                title: '��Ա���ڱ���',
	                layout: 'card',
	                //width: 1500,
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(employeeCheckReportGrid);  p.getLayout().setActiveItem(0); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'companyCheckTotalReportPanel',
	                title: '��˾�����ڻ��ܱ���',
	                layout: 'card',
	                //width: 1500,
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(companyCheckTotalReportGrid);  p.getLayout().setActiveItem(0); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'VisitStatReportPanel',
	                title: 'ҵ��Ա���û��ܱ�',
	                layout: 'card',
	                //width: 1500,
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(VisitStatReportGrid); p.add(VisitStatReportDetailGrid); p.getLayout().setActiveItem(0); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'CusVisitReportPanel',
	                title: '�ͻ����ݷû��ܱ�',
	                layout: 'card',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CusVisitReportGrid); p.add(CusVisitReportDetailGrid); p.getLayout().setActiveItem(0); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'VisitReportPanel',
	                title: 'ҵ��Ա������ϸ��¼',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(VisitReportGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'VisitRankPanel',
	                title: 'ҵ��Ա����ͳ�ƴ�����',
	                layout: 'card',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(VisitRankGrid); p.add(VisitRankChartPanel); p.getLayout().setActiveItem(0); p.doLayout(); }else{p.getLayout().setActiveItem(0);} } }
	            },{
	            	xtype: 'panel',
	            	id: 'AttendanceReportPanel',
	                title: report.detailed_log_report,
	                layout: 'card',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(AttendanceGrid); p.add(AttendanceDetailGrid); p.getLayout().setActiveItem(0); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'AreaAlarmPanel',
	                title: report.regional_alarming_statistics,
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(AreaAlarmGrid); p.doLayout();} } }
	             },{
	            	xtype: 'panel',
	            	id: 'SpeedAlarmPanel',
	                title: report.speeding_alarming_statistics,
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(SpeedAlarmGrid); p.doLayout(); }} }
	            },{
	            	xtype: 'panel',
	            	id: 'HoldAlarmPanel',
	                title: report.active_alarm_statistics,
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(HoldAlarmGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'DistanceStatPanel',
	                title: report.mileage_statistics,
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(DistanceStatGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'TotalDistanceStatPanel',
	                title: '�����ͳ��',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(TotalDistanceStatGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'VehicleGpsInfoPanel',
	                title: report.vehicle_location_information,
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(VehicleGPSInfoGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'lastGpsInfoPanel',
	                title: '�����λ��Ϣ����',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(lastGPSInfoGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'VehicleMsgStatPanel',
	                title: report.vehicle_msg_stat,
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(VehicleMsgStatGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'DiaryReportPanel',
	                title: '��������ͳ�Ʊ���',
	                layout: 'card',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(DiaryGrid); p.add(DiaryDetailGrid); p.getLayout().setActiveItem(0); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'DoorReportPanel',
	                title: '�Ŵ���Ϣ',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(doorReportGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'TimeDistanceStatPanel',
	                title: '��ʱ���ͳ��',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(TimeDistanceStatGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'AttendanceRecordReportPanel',
	                title: '����ͳ��',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(AttendanceRecordGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'StructionsPanel',
	                title: 'ָ����Ϣ',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(StructionsRecordGrid); p.doLayout(); } } }
	            }/*,{
	            	xtype: 'panel',
	            	id: 'VisitReportPanel',
	                title: '��Чƽ���ݷ���',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(VisitReportPanelRecordGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'SalesDataReportPanel',
	                title: '�������ݱ���',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(SalesDataReportGrid); p.doLayout(); } } }
	            }*/]
	        }]
    	});
	}
	var flag = false;
	if(visit_stat_report != '' && visit_stat_report.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('VisitStatPanel');
        	flag = true;
    }else {
        	Ext.getCmp('ReportTabPanel').remove('VisitStatPanel');
    }
    if(cusvisit_stat_report != '' && cusvisit_stat_report.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('CusVisitStatPanel');
        	flag = true;
    }else {
        	Ext.getCmp('ReportTabPanel').remove('CusVisitStatPanel');
    }
        if(attendance_report != '' && attendance_report.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('AttendanceReportPanel');
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('AttendanceReportPanel');
        }
        if(area_alarm_report != '' && area_alarm_report.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('AreaAlarmPanel');
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('AreaAlarmPanel');
        }
        if(speed_alarm_report != '' && speed_alarm_report.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('SpeedAlarmPanel');
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('SpeedAlarmPanel');
        }
        if(active_alarm_report != '' && active_alarm_report.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('HoldAlarmPanel');
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('HoldAlarmPanel');
        }
        if(licheng_report != '' && licheng_report.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('DistanceStatPanel');
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('DistanceStatPanel');
        }
        if(vehicle_loc_report != '' && vehicle_loc_report.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('VehicleGpsInfoPanel');
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('VehicleGpsInfoPanel');
        }
        if(vehicle_msg_stat_report != '' && vehicle_msg_stat_report.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('VehicleGpsInfoPanel');
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('VehicleMsgStatPanel');
        }
        if(diary_report != '' && diary_report.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('VehicleGpsInfoPanel');
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('DiaryReportPanel');
        }
        if(totalDistanceStat_report != '' && totalDistanceStat_report.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('VehicleGpsInfoPanel');
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('TotalDistanceStatPanel');
        }
        if(door_report != '' && door_report.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('VehicleGpsInfoPanel');
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('DoorReportPanel');
        }
        if(timeDistance_report != '' && timeDistance_report.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('VehicleGpsInfoPanel');
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('TimeDistanceStatPanel');
        }
        if(kqtj != '' && kqtj.length > 0){
        	//Ext.getCmp('ReportTabPanel').activate('VehicleGpsInfoPanel');
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('AttendanceRecordReportPanel');
        }
        if(structions_report != '' && structions_report.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('StructionsPanel');
        }
        //�ͻ��ݷ���ϸ��¼
        if(visit_report != '' && visit_report.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('VisitReportPanel');
        }
        //�ͻ��ݷô�����
        if(visit_rank_report != '' && visit_rank_report.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('VisitRankPanel');
        }
        //ҵ��Ա����ͳ��v2.1
        if(visit_stat_report21 != '' && visit_stat_report21.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('VisitStatReportPanel');
        }
        //�ͻ����ݷ�ͳ��2.1
        if(cusvisit_stat_report21 != '' && cusvisit_stat_report21.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('CusVisitReportPanel');
        }
        if(companyAttend_stat_report21 != '' && companyAttend_stat_report21.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('companyCheckTotalReportPanel');
        }
        //�ֻ�ģ���ر��� add by wangzhen 2012-11-26
        if(mobileMonitoringRepo != '' && mobileMonitoringRepo.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('mobileMonitoringRepoPanel');
        }
        if(travelcost_stat_report21 != '' && travelcost_stat_report21.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('travelCostTotalReportPanel');
        }
        if(employeeAttend_stat_report21 != '' && employeeAttend_stat_report21.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('employeeCheckReportPanel');
        }
        if(last_gps_info != '' && last_gps_info.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('lastGpsInfoPanel');
        }
        
        
        /*//�������ݱ���
        if(sales_data != '' && sales_data.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('ReportTabPanel').remove('SalesDataReportPanel');
        }*/
        reportwin.show();
        var scrollheight=document.documentElement.scrollHeight;
        var scrollwidth=document.documentElement.scrollWidth; 
        reportwin.setSize(scrollwidth - 200, scrollheight - 85);
        reportwin.setPosition(200, 85);
        reportwin.show();
}