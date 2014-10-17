var gpsDeviceid = '';
var poiid = '';
var deviceids = '';
var searchValue = '';
var duration = '';
var tmpdate = (new Date()).pattern("yyyy-M-d");
var startTime = tmpdate+' '+'08:00:00';
var endTime = tmpdate+' '+'18:00:00';
var cardpanel;
var storedetail;

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

function showgriddetail(record){
	var tmpv = record.get('visitName');
	poiid = record.get('id');
	//alert(tmpg+' '+tmpv+'('+tmps+')�Ŀ��ڱ���:');
	cardpanel.layout.setActiveItem(1);
	Ext.getCmp('griddetail_title').setText('�ͻ�('+tmpv+')�İݷ�ͳ�Ʊ���:');
	storedetail.load({params:{start:0, limit:14,startTime: startTime, endTime: endTime, poiId: poiid, deviceIds: gpsDeviceid}});
}

Ext.onReady(function(){
gpsDeviceid = getDeviceId();
Ext.QuickTips.init();

var proxydetail = new Ext.data.HttpProxy({
    url: path+'/stat/visitStat.do?method=listCustomVisitCountTjByCustom'
});

var readerdetail = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'a',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'vehicleNumber'},
    {name: 'simcard'},
    
    {name: 'visitId'},
    {name: 'visitName'},
    {name: 'arriveTime'},
    {name: 'leaveTime'},
    {name: 'stayTime'},
    {name: 'pd'},
    {name: 'tjDate'}
]);

storedetail = new Ext.data.Store({
	//autoLoad: {params:{start: 0, limit: 14, startTime: startTime, endTime: endTime}},
    restful: true,
    proxy: proxydetail,
    reader: readerdetail,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				startTime: startTime, endTime: endTime, poiId: poiid ,deviceIds: gpsDeviceid,duration: duration
    			}
    		}
    	}
    }
});


var griddetail = new Ext.grid.GridPanel({
    store: storedetail,
    loadMask: {msg:'��ѯ��...'},
    columns: [
            {id:'id',header: 'id', width: 10, sortable: true, dataIndex: 'id', hidden:true},
            {header: '���ÿͻ�id', width: 130, sortable: true,  dataIndex: 'visitId', hidden:true},
            {header: 'ҵ��Ա�ֻ�����', width: 130, sortable: true,  dataIndex: 'simcard', hidden:true},
			{header: '���ÿͻ�', width: 130, sortable: true,  dataIndex: 'visitName'},
			{header: '�ݷ�ҵ��Ա', width: 130, sortable: true,  dataIndex: 'vehicleNumber'},
            {header: '����ʱ��', width: 130, sortable: true,  dataIndex: 'arriveTime'},
			{header: '�뿪ʱ��', width: 130, sortable: true,  dataIndex: 'leaveTime'},
			{header: 'ͣ��ʱ��', width: 130, sortable: true,  dataIndex: 'stayTime'},
			{header: 'ͳ��ʱ��', width: 130, sortable: true,  dataIndex: 'tjDate', hidden:true},
            {id: 'pd',header: 'λ������', width: 300, sortable: true, dataIndex: 'pd',
	            renderer: function tips(val) {
					return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
				}
			}
        ],
        stripeRows: true,
        //autoExpandColumn: 'company',
        //height: 350,
        //width: 600,
        //title: 'Array Grid',
        tbar : new Ext.Toolbar({
        	items:[{
	        	id: 'griddetail_title',
	        	//enableToggle: true,
	        	//allowDepress: false,
	        	text: ''
	        },'-',new Ext.Action({
		        text: '����',
		        handler: function(){
		        	cardpanel.layout.setActiveItem(0);
		        },
		        iconCls: 'icon-search'
		    })]
        }),
        stateful: true,
        stateId: 'grid',
        bbar: new Ext.PagingToolbar({
            pageSize: 14,
            store: storedetail,
            displayInfo: true,
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
        })
    });


//�ݷ�ͳ��
var proxy = new Ext.data.HttpProxy({
    url: path+'/stat/visitStat.do?method=listCustomVisitCountTj'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'a',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'visitName'},
    {name: 'visitCount'},
    {name: 'process'}
]);

var store = new Ext.data.Store({
	//autoLoad: {params:{start: 0, limit: 14, searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime, deviceIds: gpsDeviceid}},
    restful: true,
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			/*var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	var tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
	        	var searchValue = encodeURI(tmpDeviceIdField);
	        	startTime = tmpstartdate+' '+tmpstarttime+':00';
	        	endTime = tmpenddate+' '+tmpendtime+':00';*/
    			this.baseParams ={
    				searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime, deviceIds: gpsDeviceid,duration: duration
    			}
    		}
    	}
    }
});

var grid = new Ext.grid.GridPanel({
    store: store,
    loadMask: {msg:'��ѯ��...'},
    columns: [
            {id:'id',header: 'id', width: 10, sortable: false, dataIndex: 'id', hidden:true},
            {header: '��ע����', width: 130, sortable: false,  dataIndex: 'visitName'},
			{header: '�ݷô���', width: 130, sortable: false,  dataIndex: 'visitCount'},
			{id:'process',header: '����', width: 100, sortable: false,  dataIndex: 'process' , renderer: function (value, meta, record) {   
			    //�����ﶨ����2������,�ֱ��費ͬ��css class�Ա�����   
			    var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='showgriddetail'>��ϸ��Ϣ</a>";
			    var resultStr = String.format(formatStr, record.get('id'));
			    return "<div class='controlBtn'>" + resultStr + "</div>";
			  }.createDelegate(this)
			}
        ],
        stripeRows: true,
        autoExpandColumn: 'process',
        //height: 350,
        //width: 600,
        //title: 'Array Grid',
        stateful: true,
        stateId: 'grid',
        tbar : new Ext.Toolbar({
        	items:[
        new Ext.form.Label({
        		text:'��ʼʱ��'
        }),new Ext.form.DateField({
				id: 'startDateField',
				fieldLabel: 'Date',
				format:'Y-m-d',
				width: 80,
				editable: false,
				value: new Date()
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
                id: 'startTimeField',
                value: '08:00',
				editable: false,
                invalidText : '��Ч��ʱ���ʽ - �������:12:00',
                increment : 1
        }),'-',new Ext.form.Label({
        		text:'����ʱ��'
        }),new Ext.form.DateField({
				width: 80,
				fieldLabel: 'Date',
				format:'Y-m-d',
				value: new Date(),
				editable: false,
	            id: 'endDateField'
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
                id: 'endTimeField',
				editable: false,
                value: '18:00',
                increment : 1
        }) ,'-',
         /*new Ext.form.Label({
        		text:'����'
        }),*/
         new Ext.form.Label({
        		text:'ͣ��ʱ��>='
        }),
        new Ext.form.TextField({
	            id: 'duration',
	            width: 30,
	            enableKeyEvents: true,
	            value: '15',
	            listeners: {
        			keypress : function( textField, e ) {
		        		if (e.getKey() == e.ENTER) {
		        			var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
		    	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
		    	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
		    	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
		    	        	searchValue = Ext.getCmp('DeviceIdField').getValue();
		    	        	duration =  Ext.getCmp('duration').getValue();
		    	        	startTime= tmpstartdate+' '+tmpstarttime+':00';
		    	        	endTime= tmpenddate+' '+tmpendtime+':00'
		    	        	
		        			gpsDeviceid = getDeviceId();
		        			store.load({params:{start:0, limit:14, deviceIds: gpsDeviceid ,searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime,duration: duration}});
		        			
		                }
        			}
        		}
	    })	,
	    new Ext.form.Label({
        		text:'����'
        })
        ,'-',
        new Ext.form.Label({
        		text:'�ؼ���'
        }),
        /*new Ext.form.Label({
        		text:'����'
        }),*/
        new Ext.form.TextField({
	            id: 'DeviceIdField',
	            width: 60,
	            enableKeyEvents: true,
	            listeners: {
        			keypress : function( textField, e ) {
		        		if (e.getKey() == e.ENTER) {
		        			var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
		    	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
		    	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
		    	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
		    	        	searchValue = Ext.getCmp('DeviceIdField').getValue();
		    	        	duration = Ext.getCmp('duration').getValue();
		    	        	startTime= tmpstartdate+' '+tmpstarttime+':00';
		    	        	endTime= tmpenddate+' '+tmpendtime+':00'
		    	        	
		        			gpsDeviceid = getDeviceId();
		        			store.load({params:{start:0, limit:14, deviceIds: gpsDeviceid ,searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime,duration: duration}});
		        			
		                }
        			}
        		}
	    })
	    
	    ,'-',
       new Ext.Action({
	        text: '��ѯ',
	        handler: function(){
	        	var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	searchValue = Ext.getCmp('DeviceIdField').getValue();
	        	duration =  Ext.getCmp('duration').getValue();
	        	startTime= tmpstartdate+' '+tmpstarttime+':00';
	        	endTime= tmpenddate+' '+tmpendtime+':00'
	        	
    			gpsDeviceid = getDeviceId();
    			store.load({params:{start:0, limit:14, deviceIds: gpsDeviceid ,searchValue: encodeURI(searchValue) , startTime: startTime, endTime: endTime,duration: duration}});
    			
	        },
	        iconCls: 'icon-search'
	    }),'-',new Ext.Action({
	        text: '����',
	        handler: function(){
	        	/*var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	var tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
	        	var searchValue = encodeURI(tmpDeviceIdField);
	        	var startTime = tmpstartdate+' '+tmpstarttime+':00';
	        	var endTime = tmpenddate+' '+tmpendtime+':00';*/
    			//store.load({params:{expExcel: 'true', start:0, limit:14, searchValue: searchValue , startTime: startTime, endTime: endTime}});
    			Ext.Msg.show({
			    	msg: '���ڵ��� ���Ե�...',
			        progressText: '������...',
			        width:300,
			        wait:true,
			        icon:'ext-mb-download'
			    });
    			var tmpparam = 'expExcel=true&searchValue='+encodeURI(searchValue)+'&startTime='+startTime+'&endTime='+endTime+'&deviceIds='+gpsDeviceid+'&duration='+duration;
    			document.excelform.action = path+'/stat/visitStat.do?method=listCustomVisitCountTj&'+tmpparam;
    			document.excelform.submit();
				setTimeout(function(){Ext.MessageBox.hide()},3000);
	        },
	        iconCls: 'icon-excel'
	    })
        ]
        }),
        bbar: new Ext.PagingToolbar({
            pageSize: 14,
            store: store,
            displayInfo: true,
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
        })
    });

grid.on('cellclick', function (grid, rowIndex, columnIndex, e) {   
  var btn = e.getTarget('.controlBtn');
  if (btn) {   
    var t = e.getTarget();   
    var record = grid.getStore().getAt(rowIndex);   
    var control = t.className;   
    switch (control) {   
    case 'showgriddetail':
      this.showgriddetail(record);
      break;
    }
  }   
},   
this);  



    cardpanel = new Ext.Panel({
		layout: 'card',
		activeItem : 0,
		region: 'center',
		items: [grid, griddetail]
	});

	var view = new Ext.Viewport({
		layout: 'border',
		items: [ cardpanel ]
	});
	
    //var tmpdate = (new Date()).pattern("yyyy-M-d");
    //store.load({params:{start:0, limit:14, startTime: tmpdate+' '+'08:00:00', endTime: tmpdate+' '+'18:00:00'}});
    
});
