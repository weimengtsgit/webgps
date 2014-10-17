/**
 * 最近定位信息报表
 */
var lastGPSInfoGpsDeviceids = '';
var lastGPSInfoSearchValue = '';
var lastGPSInfoLocateType = '';
var lastGPSInfoGpsTime = '0';
var lastGPSInfoLastTime = '0';

Ext.form.TextField.prototype.size = 20;   
Ext.form.TextField.prototype.initValue = function() {   
    if (this.value !== undefined) {   
        this.setValue(this.value);   
    } else if (this.el.dom.value.length > 0) {   
        this.setValue(this.el.dom.value);   
    }   
    this.el.dom.size = this.size;   
    if (!isNaN(this.maxLength) && (this.maxLength * 1) > 0  
            && (this.maxLength != Number.MAX_VALUE)) {   
        this.el.dom.maxLength = this.maxLength * 1;   
    }   
};  

function initlastGPSInfoGrid(){
//	var tmpstartdate = Ext.getCmp('lastGPSInfosdf').getValue().format('Y-m-d');
//	var tmpstarttime = Ext.getCmp('lastGPSInfostf').getValue();
//	var tmpenddate = Ext.getCmp('lastGPSInfoedf').getValue().format('Y-m-d');
//	var tmpendtime = Ext.getCmp('lastGPSInfoetf').getValue();

	lastGPSInfoLocateType = Ext.getCmp('locateTypeLastGPSInfo').getValue();
	lastGPSInfoGpsTime = Ext.getCmp('lastGPSInfoGpstime').getValue();
	if(lastGPSInfoGpsTime == '' || lastGPSInfoGpsTime.length <= 0 ){
		lastGPSInfoGpsTime = '0';
	}
	lastGPSInfoLastTime = Ext.getCmp('lastGPSInfoLastTime').getValue();
	if(lastGPSInfoLastTime == '' || lastGPSInfoLastTime.length <= 0 ){
		lastGPSInfoLastTime = '0';
	}
	lastGPSInfoSearchValue = Ext.getCmp('lastGPSInfodif').getValue();
    lastGPSInfoGpsDeviceids = getDeviceId();
}

function searchlastGPSInfoGrid(){
	lastGPSInfoStoreLoad(lastGPSInfoStore, 0, 65535, lastGPSInfoGpsDeviceids, lastGPSInfoSearchValue, false, lastGPSInfoLocateType, lastGPSInfoGpsTime, lastGPSInfoLastTime);
	
}

function lastGPSInfoStoreLoad(store, start, limit, deviceIds, searchValue, expExcel, locateType, gpstime, inputtime){
	store.load({params:{start: start, limit: limit, deviceIds: deviceIds , name: encodeURI(searchValue) , expExcel: expExcel, locateType: locateType, gpstime: gpstime, inputtime: inputtime}});
}

var locateTypeComBox = new Ext.form.ComboBox({
	style : 'top:0px;',
	displayField : 'name',
	name : 'locateType',
	id : 'locateTypeLastGPSInfo',
	value:'',
	valueField : 'value',
	typeAhead : true,
	mode : 'local',
	triggerAction : 'all',
	store : new Ext.data.SimpleStore({
				fields : ['name', 'value'],
				data : [
				        ['请选择',''],
				        ['人员','0'],
				        ['车辆','1']
				        ]
			}),
	selectOnFocus : true,
	editable : false,
	width : 80
}); 

var lastGPSInfoStore = new Ext.data.Store({
	restful: true,
	proxy: new Ext.data.HttpProxy({url: path+'/locate/locate.do?method=lastLocrecordList'}),
	reader: new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', root: 'data' }, [{name: 'termName'},{name: 'locateType'},{name: 'groupName'},{name: 'simcard'},{name: 'gpstime'},{name: 'disGpstime'},{name: 'lastTime'},{name: 'disLastTime'}]),
	listeners:{
		beforeload:{
			fn: function(thiz,options){
				this.baseParams ={
					name: encodeURI(lastGPSInfoSearchValue), deviceIds: lastGPSInfoGpsDeviceids, locateType: lastGPSInfoLocateType, gpstime: lastGPSInfoGpsTime, inputtime: lastGPSInfoLastTime
		    	};
			}
		}
	}
});

var lastGPSInfoGrid = {
	xtype: 'grid',
	id: 'lastGPSInfoGrid',
	store: lastGPSInfoStore,
	loadMask: {msg: '查询中...'},
	columns: [
		{header: '名称', width: 130, sortable: true,  dataIndex: 'termName'},
		{header: '终端类型', width: 130, sortable: true,  dataIndex: 'locateType',
	        renderer: function tips(val) {
				if(val == '0'){
					return '人员';
				}else{
					return '车辆';
				}
			}
		},
		{header: '部门', width: 130, sortable: true,  dataIndex: 'groupName'},
		{header: '手机号码', width: 130, sortable: true,  dataIndex: 'simcard'},
		{header: '最后一次成功定位时间', width: 130, sortable: true,  dataIndex: 'gpstime'},
		{header: '距当前时间(小时)', width: 130, sortable: true,  dataIndex: 'disGpstime'},
		{header: '最后一次数据接收时间', width: 130, sortable: true,  dataIndex: 'lastTime'},
		{header: '距当前时间(小时)', width: 130, sortable: true,  dataIndex: 'disLastTime'}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'lastGPSInfoGridId',
	//autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label', text:'名称'},
			{id: 'lastGPSInfodif',xtype: 'textfield',maxLength: 50, width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {
					initlastGPSInfoGrid();searchlastGPSInfoGrid();
				}
			}}},'-',
			{xtype: 'label',text: '终端类型'}, locateTypeComBox,'-',
			{xtype: 'label', text: '定位时间距当前时间(小时）>=' },
			{id: 'lastGPSInfoGpstime',xtype: 'numberfield', maxLength: 8, allowDecimals: false, width: 30, enableKeyEvents: true, value: '', listeners: {keypress : function( textField, e ) {if (e.getKey() == e.ENTER) { 
				initVisitStatGrid();
				searchVisitStatGrid();
			}}}},'-',
			{xtype: 'label', text: '接收时间距当前时间(小时）>=' },
			{id: 'lastGPSInfoLastTime',xtype: 'numberfield', maxLength: 8, allowDecimals: false, width: 30, enableKeyEvents: true, value: '', listeners: {keypress : function( textField, e ) {if (e.getKey() == e.ENTER) { 
				initVisitStatGrid();
				searchVisitStatGrid();
			}}}},'-',
			{xtype: 'button',text: '查询',iconCls: 'icon-search',handler: function(){
				initlastGPSInfoGrid();searchlastGPSInfoGrid();
			}},'-',
			{xtype: 'button',text: '导出Excel',iconCls: 'icon-excel',handler: function(){
				//initlastGPSInfoGrid();
//			    lastGPSInfoGpsDeviceids = getDeviceId();
				var excelhtml = path+'/locate/locate.do?method=lastLocrecordList&expExcel=true&name='+encodeURI(encodeURI(lastGPSInfoSearchValue))+ '&deviceIds='+lastGPSInfoGpsDeviceids+'&locateType='+lastGPSInfoLocateType+'&gpstime='+lastGPSInfoGpsTime+'&inputtime='+lastGPSInfoLastTime;
				var win = new Ext.Window({
					title : '下载文件',
					closable : true,
					closeAction : 'close',
					autoWidth : false,
					width : 200,
					heigth : 150,
					items : [new Ext.Panel({
						html : "<a href='"+excelhtml+"' target='_blank' onclick='Ext.Msg.hide()'>点击此链接下载报表</a>",
						frame : true
					})]
				});
				win.show();
				
			}},
			{xtype: 'button',text: '刷新',iconCls: 'icon-refresh',handler: function(){
				
				Ext.getCmp('locateTypeLastGPSInfo').setValue('');
				Ext.getCmp('lastGPSInfoGpstime').setValue('');
				Ext.getCmp('lastGPSInfoLastTime').setValue('');
				Ext.getCmp('lastGPSInfodif').setValue('');
				
				initlastGPSInfoGrid();searchlastGPSInfoGrid();
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 65535,
		store: lastGPSInfoStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};
