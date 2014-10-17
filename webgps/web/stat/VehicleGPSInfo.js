var gpsDeviceid = '';
var deviceid = '';
var searchValue = '';
var tmpdate = (new Date()).pattern("yyyy-M-d");
var startTime = tmpdate+' '+'08:00:00';
var endTime = tmpdate+' '+'18:00:00';
var cardpanel;
var storedetail;
var openTime;
var closeTime;

function getDeviceId(){
	var treeArr = new Array();
	parent.getTreeId(parent.root, treeArr);
	var tmpgpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2) {
			tmpgpsDeviceid += idArr[0] + ',';
		}
	}
	if (tmpgpsDeviceid.length > 0) {
		tmpgpsDeviceid = tmpgpsDeviceid.substring(0, tmpgpsDeviceid.length - 1);
	}
	return tmpgpsDeviceid;
}

Ext.onReady(function(){
gpsDeviceid = getDeviceId();
Ext.QuickTips.init();

//������λ��Ϣ
var proxy = new Ext.data.HttpProxy({
	 url: path+'/stat/visitStat.do?method=listAttendanceReportDetail'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'deviceId'},
    {name: 'vehicleNumber'},
    {name: 'simcard'},
    {name: 'gpsTime'},
    {name: 'pd'},
    {name: 'visitId'},
    {name: 'visitName'},
    {name: 'longitude'},
    {name: 'latitude'},
    {name: 'speed'},
    {name: 'direction'},
    {name: 'distance'},
    {name: 'accStatus'},
    {name: 'temperature'}
]);

var store = new Ext.data.Store({
    restful: true,
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime,deviceId: gpsDeviceid
    			}
    		}
    	}
    }
});

var grid = new Ext.grid.GridPanel({
    store: store,
    loadMask: {msg:'��ѯ��...'},
    columns: [
            {header: '���ƺ�', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
			{header: '�ֻ�����', width: 130, sortable: true,  dataIndex: 'simcard'},
			{header: 'γ��', width: 130, sortable: true,  dataIndex: 'latitude'},
			{header: '����', width: 130, sortable: true,  dataIndex: 'longitude'},
			{header: '�ٶ�', width: 130, sortable: true,  dataIndex: 'speed'},
			{header: '����', width: 130, sortable: true,  dataIndex: 'direction'},
			{header: '���', width: 130, sortable: true,  dataIndex: 'distance'},
			{header: '��ʻ״̬', width: 130, sortable: true,  dataIndex: 'accStatus'},
			{header: 'λ������', width: 130, sortable: true,  dataIndex: 'pd'},
			{header: '���ÿͻ�', width: 130, sortable: true,  dataIndex: 'visitName'},
			{header: 'ʱ��', width: 130, sortable: true,  dataIndex: 'gpsTime'},
			{header: '�¶�', width: 130, sortable: true,  dataIndex: 'temperature',renderer:function(r,d,v){return r=="null"?"":r;}}
        ],
        stripeRows: true,
        stateful: true,
        stateId: 'grid',
        tbar : new Ext.Toolbar({
        	items:[
        new Ext.form.Label({
        		text:'����'
        }),new Ext.form.DateField({
				id: 'startDateField',
				fieldLabel: 'Date',
				format:'Y-m-d',
				width: 100,
				editable: false,
				maxValue:new Date(),
				value: new Date()
	    }),'-' ,new Ext.form.Label({
        		text:'��ʼʱ��'
        }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
                id: 'startTimeField',
                value: '08:00',
				editable: false,
                invalidText : '��Ч��ʱ���ʽ - �������:12:00',
                increment : 5
        }),'-',new Ext.form.Label({
        		text:'����ʱ��'
        })
//        ,new Ext.form.DateField({
//				width: 100,
//				fieldLabel: 'Date',
//				format:'Y-m-d',
//				editable: false,
//				value: new Date(),
//	            id: 'endDateField'
//	    })
        ,new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
				editable: false,
                id: 'endTimeField',
                value: '18:00',
                increment : 5
        }),'-',new Ext.form.TextField({
	            id: 'DeviceIdField',
	            width: 80
	    }),'-',new Ext.Action({
	        text: '��ѯ',
	        handler: function(){
	    		var expExcel="false";
	        	var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('startDateField').getValue().format('Y-m-d');//
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	searchValue = Ext.getCmp('DeviceIdField').getValue();
	        	startTime= tmpstartdate+' '+tmpstarttime+':00';
	        	endTime= tmpenddate+' '+tmpendtime+':00'
	        	
    			gpsDeviceid = getDeviceId();
	        	var ids=gpsDeviceid.split(",");
	        	if(ids.length>22){
	        		Ext.Msg.alert('��ʾ','���ѡ��22���ն�!');
	        	}else{
	        		if(tmpstarttime>=tmpendtime){
	        			Ext.Msg.alert('��ʾ','��ʼʱ�����С���ڽ���ʱ��!');
	   				}else{
	   					store.load({params:{start:0, limit:100, deviceId: gpsDeviceid ,searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime,expExcel:expExcel}});
	   				}
//	        		if(tmpstartdate>tmpenddate){
//	        			Ext.Msg.alert('��ʾ','��ʼ���ڲ��ܴ��ڽ�������!');
//	        		}else if(tmpstartdate==tmpenddate){
//	        			if(tmpstarttime>=tmpendtime){
//    	        			Ext.Msg.alert('��ʾ','��ʼʱ�����С���ڽ���ʱ��!');
//        				}else{
//        					store.load({params:{start:0, limit:100, deviceId: gpsDeviceid ,searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime,expExcel:expExcel}});
//        				}
//	        		}else{
//	        			store.load({params:{start:0, limit:100, deviceId: gpsDeviceid ,searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime,expExcel:expExcel}});
//	        		}	
	        	}	
	        },
	        iconCls: 'icon-search'
	    }),'-',new Ext.Action({
	        text: '����Excel',
	        handler: function(){
	    		var expExcel='true';
	        	var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('startDateField').getValue().format('Y-m-d');//
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	var tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
	        	var searchValue = encodeURI(tmpDeviceIdField);
	        	var startTime = tmpstartdate+' '+tmpstarttime+':00';
	        	var endTime = tmpenddate+' '+tmpendtime+':00';
	        	
	        	var ids=gpsDeviceid.split(",");
	        	if(ids.length>22){
	        		Ext.Msg.alert('��ʾ','���ѡ��22���ն�!');
	        	}else{
	        		
	        		if(tmpstarttime>=tmpendtime){
	        			Ext.Msg.alert('��ʾ','��ʼʱ�����С���ڽ���ʱ��!');
    				}else{
    					var param="&expExcel="+expExcel+"&deviceId="+gpsDeviceid+"&searchValue="+searchValue+"&startTime="+startTime+"&endTime="+endTime;
    	    			document.excelform.action = path+'/stat/visitStat.do?method=listAttendanceReportDetail'+param;
    	    			document.excelform.submit();
    	    			Ext.Msg.show({
    				    	msg: '���ڵ��� ���Ե�...',
    				        progressText: '������...',
    				        width:300,
    				        wait:true,
    				        icon:'ext-mb-download'
    				    });
    	    			setTimeout(function(){Ext.MessageBox.hide()},3000);
    				}
//	        		if(tmpstartdate>tmpenddate){
//	        			Ext.Msg.alert('��ʾ','��ʼ���ڲ��ܴ��ڽ�������!');
//	        		}else if(tmpstartdate==tmpenddate){
//	        			if(tmpstarttime>=tmpendtime){
//    	        			Ext.Msg.alert('��ʾ','��ʼʱ�����С���ڽ���ʱ��!');
//        				}else{
//        					var param="&expExcel="+expExcel+"&deviceId="+gpsDeviceid+"&searchValue="+searchValue+"&startTime="+startTime+"&endTime="+endTime;
//        	    			document.excelform.action = path+'/stat/visitStat.do?method=listAttendanceReportDetail'+param;
//        	    			document.excelform.submit();
//        	    			Ext.Msg.show({
//        				    	msg: '���ڵ��� ���Ե�...',
//        				        progressText: '������...',
//        				        width:300,
//        				        wait:true,
//        				        icon:'ext-mb-download'
//        				    });
//        	    			setTimeout(function(){Ext.MessageBox.hide()},3000);
//        				}
//	        		}else{
//	        			var param="&expExcel="+expExcel+"&deviceId="+gpsDeviceid+"&searchValue="+searchValue+"&startTime="+startTime+"&endTime="+endTime;
//		    			document.excelform.action = path+'/stat/visitStat.do?method=listAttendanceReportDetail'+param;
//		    			document.excelform.submit();
//		    			Ext.Msg.show({
//					    	msg: '���ڵ��� ���Ե�...',
//					        progressText: '������...',
//					        width:300,
//					        wait:true,
//					        icon:'ext-mb-download'
//					    });
//		    			setTimeout(function(){Ext.MessageBox.hide()},3000);
//	        		}
	        	}
	        	
	        },
	        iconCls: 'icon-excel'
	    })
        ]
        }),
        bbar: new Ext.PagingToolbar({
            pageSize: 100,
            store: store,
            displayInfo: true,
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
        })
    });

    cardpanel = new Ext.Panel({
		layout: 'card',
		activeItem : 0,
		region: 'center',
		items: [grid]
	});

	var view = new Ext.Viewport({
		layout: 'border',
		items: [ cardpanel ]
	});
	
});
