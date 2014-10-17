var searchValue = '';
var lastMonth = new Date();
lastMonth.setMonth(lastMonth.getMonth()-1);
var startTime = (lastMonth).pattern("yyyy-M-d")+" 00:00:00";
var endTime = (new Date()).pattern("yyyy-M-d")+" 23:59:59";

var delbut = new Ext.Action({
	text: '修改',
	id : 'delbut',
	handler: function(){
		var tmpid = Ext.getCmp('frmid').getValue();
		if(tmpid.length<=0){
			Ext.Msg.alert('提示', '请选择记录!');
			return;
		}
		Ext.MessageBox.confirm('提示', '您确定要修改数据吗?', function (btn){
			if(btn=='yes'){
				var tmpid = Ext.getCmp('frmid').getValue();
				var tmpinsuranceNo = Ext.getCmp('frminsuranceNo').getValue();
				if(tmpinsuranceNo.length<=0){
					Ext.Msg.alert('提示', '请输入保险单号!');
					return;
				}
				var tmpinsuranceName = Ext.getCmp('frminsuranceName').getValue();
				if(tmpinsuranceName.length<=0){
					Ext.Msg.alert('提示', '请输入保险名称!');
					return;
				}
				var tmpinsuranceCo = Ext.getCmp('frminsuranceCo').getValue();
				if(tmpinsuranceCo.length<=0){
					Ext.Msg.alert('提示', '请输入保险公司!');
					return;
				}
				var tmpinsuranceDate = Ext.getCmp('frminsuranceDate').getValue();
				if(tmpinsuranceDate.length<=0){
					Ext.Msg.alert('提示', '请选择投保日期!');
					return;
				}
				var tmpexpireDate = Ext.getCmp('frmexpireDate').getValue();
				if(tmpexpireDate.length<=0){
					Ext.Msg.alert('提示', '请选择到期日期!');
					return;
				}
				var tmpsumInsured = Ext.getCmp('frmsumInsured').getValue();
				if(tmpsumInsured.length<=0){
					Ext.Msg.alert('提示', '请输入保额!');
					return;
				}
				var tmppremium = Ext.getCmp('frmpremium').getValue();
				if(tmppremium.length<=0){
					Ext.Msg.alert('提示', '请输入保费!');
					return;
				}
				var tmphandler = Ext.getCmp('frmhandler').getValue();
				if(tmphandler.length<=0){
					Ext.Msg.alert('提示', '请输入经手人!');
					return;
				}
				var tmpdeviceId = Ext.getCmp('frmdeviceId').getValue();
				Ext.Msg.show({
					msg: '正在保存 请稍等...',
					progressText: '保存...',
					width:300,
					wait:true,
					icon:'ext-mb-download'
				});
				Ext.Ajax.request({
					url:path+'/insurance/insurance.do?method=updateInsurance',
					method :'POST', 
					params:{
						id: tmpid, deviceId: tmpdeviceId, insuranceNo: encodeURI(tmpinsuranceNo) , insuranceName: encodeURI(tmpinsuranceName) ,
						insuranceCo: encodeURI(tmpinsuranceCo), insuranceDate: tmpinsuranceDate, expireDate: tmpexpireDate ,
						sumInsured: tmpsumInsured, premium: tmppremium, handler: encodeURI(tmphandler)
					 },
					 //timeout : 10000,
					 success : function(request) {
					   var res = Ext.decode(request.responseText);
					 	if(res.result==1){
					 		store.load({params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime}});
					   		Ext.Msg.alert('提示', '操作成功');
					   		return;
					   }else{
					   		//store.reload();
					   		Ext.Msg.alert('提示', "操作失败!");
					   		return;
					   }
					 },
					 failure : function(request) {
						 Ext.Msg.alert('提示', "操作失败!");
					 }
					});
			}
		});
	},
	iconCls: 'icon-modify'
});

	var simple = new Ext.FormPanel({
		labelWidth: 75,
		frame: true,
        region: 'center',
		bodyStyle: 'padding:5px 5px 0',
		autoScroll : true,
		width: 200,
		defaults: {width: 150},
		defaultType: 'textfield',
		items: [{
			fieldLabel: '终端名称',
		    id: 'frmdevice',
		    width: 150,
		    readOnly: true,
		    allowBlank:false
		},{
			fieldLabel: '保险单号',
		    id: 'frminsuranceNo',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '保险名称',
		    id: 'frminsuranceName',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '保险公司',
		    id: 'frminsuranceCo',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '投保日期',
		    xtype: 'datefield',
		    id: 'frminsuranceDate',
		    editable: false,
		    format: 'Y-m-d',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '到期日期',
		    xtype: 'datefield',
		    id: 'frmexpireDate',
		    editable: false,
		    format: 'Y-m-d',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '保额',
			xtype: 'numberfield',
		    id: 'frmsumInsured',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '保费',
			xtype: 'numberfield',
		    id: 'frmpremium',
		    width: 150,
		    allowBlank:false
		},{
		    fieldLabel: '经手人',
		    id: 'frmhandler',
		    width: 150,
		    allowBlank:false
		},{
		    xtype: 'hidden',
		    id: 'frmdeviceId'
		},{
		    xtype: 'hidden',
		    id: 'frmid'
		}]
	});

var proxy = new Ext.data.HttpProxy({
	url: path+'/insurance/insurance.do?method=listInsurance'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'deviceId'},
    {name: 'termName'},
    {name: 'vehicleNumber'},
    {name: 'simcard'},
    {name: 'groupName'},
    {name: 'insuranceNo'},
    {name: 'insuranceName'},
    {name: 'insuranceCo'},
    {name: 'insuranceDate'},
    {name: 'expireDate'},
    {name: 'sumInsured'},
    {name: 'premium'},
    {name: 'handler'}
]);

var store = new Ext.data.Store({
	autoLoad: {params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime}},
    id: 'store',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime
    			};
    		}
    	}
    }
});

var sm = new Ext.grid.RowSelectionModel({
	singleSelect: true,
	listeners: {
		rowselect: function(sm, row, rec) {
			Ext.getCmp('frmdevice').setValue(rec.get('termName'));
			Ext.getCmp('frminsuranceNo').setValue(rec.get('insuranceNo'));
			Ext.getCmp('frminsuranceName').setValue(rec.get('insuranceName'));
			Ext.getCmp('frminsuranceCo').setValue(rec.get('insuranceCo'));
			Ext.getCmp('frminsuranceDate').setValue(rec.get('insuranceDate'));
			Ext.getCmp('frmexpireDate').setValue(rec.get('expireDate'));
			Ext.getCmp('frmsumInsured').setValue(rec.get('sumInsured'));
			Ext.getCmp('frmpremium').setValue(rec.get('premium'));
			Ext.getCmp('frmhandler').setValue(rec.get('handler'));
			Ext.getCmp('frmdeviceId').setValue(rec.get('deviceId'));
			Ext.getCmp('frmid').setValue(rec.get('id'));
			
	    }
	}
});

var userColumns =  [
	new Ext.grid.RowNumberer({header:'序号',width:40}),
    //{id:'deviceId',header: "deviceId", width: 40, sortable: true, dataIndex: 'deviceId',hidden:true},
    {header: "名称",fixed:true, width: 100, sortable: true, dataIndex: 'termName'},
    {header: "车牌号",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleNumber'},
    {header: "手机号",fixed:true, width: 100, sortable: true, dataIndex: 'simcard'},
    {header: "所属组",fixed:true, width: 100, sortable: true, dataIndex: 'groupName'},
    {header: "保险单号",fixed:true, width: 100, sortable: true, dataIndex: 'insuranceNo'},
    {header: "保险名称",fixed:true, width: 100, sortable: true, dataIndex: 'insuranceName'},
    {header: "保险公司",fixed:true, width: 100, sortable: true, dataIndex: 'insuranceCo'},
    {header: "投保日期",fixed:true, width: 100, sortable: true, dataIndex: 'insuranceDate'},
    {header: "到期日期",fixed:true, width: 100, sortable: true, dataIndex: 'expireDate'},
    {header: "保额",fixed:true, width: 100, sortable: true, dataIndex: 'sumInsured'},
    {header: "保费",fixed:true, width: 100, sortable: true, dataIndex: 'premium'},
    {header: "经手人",fixed:true, width: 100, sortable: true, dataIndex: 'handler'}
];

var userGrid = new Ext.grid.GridPanel({
	region: 'west',
    width: 500,
    loadMask: {msg:'查询中...'},
    enableColumnHide : false,
    store: store,
    sm : sm,
    columns : userColumns,
    margins: '0 0 0 0',
	tbar: [new Ext.form.DateField({
		id: 'startDateField',
		fieldLabel: 'Date',
		format:'Y-m-d',
		width: 100,
		editable: false,
		value: lastMonth
	}),'-',new Ext.form.DateField({
		width: 100,
		fieldLabel: 'Date',
		format:'Y-m-d',
		editable: false,
		value: new Date(),
	    id: 'endDateField'
	}),'-',new Ext.form.TextField({
	    id: 'DeviceIdField',
	    width: 80,
	    enableKeyEvents: true,
	    listeners: {
        	keypress : function( textField, e ) {
		        if (e.getKey() == e.ENTER) {
		        	searchValue = Ext.getCmp('DeviceIdField').getValue();
				    startTime = Ext.getCmp('startDateField').getValue().format('Y-m-d')+" 00:00:00";
				    endTime = Ext.getCmp('endDateField').getValue().format('Y-m-d')+" 23:59:59";
			    	store.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime}});
			    }
        	}
        }
	}),'-',new Ext.Action({
		text: '查询',
		handler: function(){
			searchValue = Ext.getCmp('DeviceIdField').getValue();
			startTime = Ext.getCmp('startDateField').getValue().format('Y-m-d')+" 00:00:00";
			endTime = Ext.getCmp('endDateField').getValue().format('Y-m-d')+" 23:59:59";
	    	store.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime}});
	    },
		iconCls: 'icon-search'
	}),'-',delbut
    ],
    bbar: new Ext.PagingToolbar({
        pageSize: 20,
        store: store,
        displayInfo: true,
        displayMsg: '第{0}到第{1}条数据 共{2}条',
        emptyMsg: "没有数据"
    })
});

Ext.onReady(function(){
    var viewport = new Ext.Viewport({layout: 'border',items: [simple,userGrid]});
    
});