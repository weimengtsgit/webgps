Ext.onReady(function(){

	var button = Ext.get('show-btn');

    button.on('click', function(){
		var AreaAlarmGrid = new Ext.Panel({
			title: '���򱨾�ͳ��',
			html: '<iframe src="AreaAlarm.jsp" style="width:100%; height: 100%;" SCROLLING="no" frameborder="0"></iframe>'
		});
		
    	var SpeedAlarmGrid = new Ext.Panel({
    		title: '���ٱ���ͳ��',
			html: '<iframe src="SpeedAlarm.jsp" style="width:100%; height: 100%;" SCROLLING="no" frameborder="0"></iframe>'
		});
		
		var HoldAlarmGrid = new Ext.Panel({
			title: '��������ͳ��',
			html: '<iframe src="HoldAlarm.jsp" style="width:100%; height: 100%;" SCROLLING="no" frameborder="0"></iframe>'
		});
		
		var DistanceStatGrid = new Ext.Panel({
			title: '���ͳ��',
			html: '<iframe src="DistanceStat.jsp" style="width:100%; height: 100%;" SCROLLING="no" frameborder="0"></iframe>'
		});
		
		var VisitStatGrid = new Ext.Panel({
			title: '�ݷ�ͳ��',
			html: '<iframe src="VisitStat.jsp" style="width:100%; height: 100%;" SCROLLING="no" frameborder="0"></iframe>'
		});
		
		var CusVisitStatGrid = new Ext.Panel({
			title: '�ͻ��ݷ�ͳ��',
			html: '<iframe src="CusVisitStat.jsp" style="width:100%; height: 100%;" SCROLLING="no" frameborder="0"></iframe>'
		});
		
        // tabs for the center
        var tabs = new Ext.TabPanel({
            region: 'center',
            margins:'3 3 3 0', 
            activeTab: 0,
            defaults:{autoScroll:true}
            //items:[AreaAlarmGrid , SpeedAlarmGrid , HoldAlarmGrid, DistanceStatGrid, VisitStatGrid, CusVisitStatGrid]
        });
        if(area_alarm_report != '' && area_alarm_report.length > 0){
        	tabs.add(AreaAlarmGrid);
        }
        if(speed_alarm_report != '' && speed_alarm_report.length > 0){
        	tabs.add(SpeedAlarmGrid);
        }
        if(active_alarm_report != '' && active_alarm_report.length > 0){
        	tabs.add(HoldAlarmGrid);
        }
        if(licheng_report != '' && licheng_report.length > 0){
        	tabs.add(DistanceStatGrid);
        }
        tabs.add(VisitStatGrid);
        tabs.add(CusVisitStatGrid);
        
        var win = new Ext.Window({
            title: 'Layout Window',
            closable:true,
            width:600,
            height:300,
            maximizable: true,
            //border:false,
            plain:true,
            layout: 'border',
            items: [tabs]
        });

        win.show(this);
    });

});
