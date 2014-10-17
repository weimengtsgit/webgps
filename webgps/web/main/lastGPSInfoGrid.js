/**
 * �����λ��Ϣ����
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
				        ['��ѡ��',''],
				        ['��Ա','0'],
				        ['����','1']
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
	loadMask: {msg: '��ѯ��...'},
	columns: [
		{header: '����', width: 130, sortable: true,  dataIndex: 'termName'},
		{header: '�ն�����', width: 130, sortable: true,  dataIndex: 'locateType',
	        renderer: function tips(val) {
				if(val == '0'){
					return '��Ա';
				}else{
					return '����';
				}
			}
		},
		{header: '����', width: 130, sortable: true,  dataIndex: 'groupName'},
		{header: '�ֻ�����', width: 130, sortable: true,  dataIndex: 'simcard'},
		{header: '���һ�γɹ���λʱ��', width: 130, sortable: true,  dataIndex: 'gpstime'},
		{header: '�൱ǰʱ��(Сʱ)', width: 130, sortable: true,  dataIndex: 'disGpstime'},
		{header: '���һ�����ݽ���ʱ��', width: 130, sortable: true,  dataIndex: 'lastTime'},
		{header: '�൱ǰʱ��(Сʱ)', width: 130, sortable: true,  dataIndex: 'disLastTime'}
	],
	stripeRows: true,
	stateful: true,
	stateId: 'lastGPSInfoGridId',
	//autoExpandColumn: 'pd',
	tbar : {
		xtype: 'toolbar',
		items:[
			{xtype: 'label', text:'����'},
			{id: 'lastGPSInfodif',xtype: 'textfield',maxLength: 50, width: 60,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
				if (e.getKey() == e.ENTER) {
					initlastGPSInfoGrid();searchlastGPSInfoGrid();
				}
			}}},'-',
			{xtype: 'label',text: '�ն�����'}, locateTypeComBox,'-',
			{xtype: 'label', text: '��λʱ��൱ǰʱ��(Сʱ��>=' },
			{id: 'lastGPSInfoGpstime',xtype: 'numberfield', maxLength: 8, allowDecimals: false, width: 30, enableKeyEvents: true, value: '', listeners: {keypress : function( textField, e ) {if (e.getKey() == e.ENTER) { 
				initVisitStatGrid();
				searchVisitStatGrid();
			}}}},'-',
			{xtype: 'label', text: '����ʱ��൱ǰʱ��(Сʱ��>=' },
			{id: 'lastGPSInfoLastTime',xtype: 'numberfield', maxLength: 8, allowDecimals: false, width: 30, enableKeyEvents: true, value: '', listeners: {keypress : function( textField, e ) {if (e.getKey() == e.ENTER) { 
				initVisitStatGrid();
				searchVisitStatGrid();
			}}}},'-',
			{xtype: 'button',text: '��ѯ',iconCls: 'icon-search',handler: function(){
				initlastGPSInfoGrid();searchlastGPSInfoGrid();
			}},'-',
			{xtype: 'button',text: '����Excel',iconCls: 'icon-excel',handler: function(){
				//initlastGPSInfoGrid();
//			    lastGPSInfoGpsDeviceids = getDeviceId();
				var excelhtml = path+'/locate/locate.do?method=lastLocrecordList&expExcel=true&name='+encodeURI(encodeURI(lastGPSInfoSearchValue))+ '&deviceIds='+lastGPSInfoGpsDeviceids+'&locateType='+lastGPSInfoLocateType+'&gpstime='+lastGPSInfoGpsTime+'&inputtime='+lastGPSInfoLastTime;
				var win = new Ext.Window({
					title : '�����ļ�',
					closable : true,
					closeAction : 'close',
					autoWidth : false,
					width : 200,
					heigth : 150,
					items : [new Ext.Panel({
						html : "<a href='"+excelhtml+"' target='_blank' onclick='Ext.Msg.hide()'>������������ر���</a>",
						frame : true
					})]
				});
				win.show();
				
			}},
			{xtype: 'button',text: 'ˢ��',iconCls: 'icon-refresh',handler: function(){
				
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
		displayMsg: '��{0}����{1}������ ��{2}��',
		emptyMsg: "û������"
	})
};
