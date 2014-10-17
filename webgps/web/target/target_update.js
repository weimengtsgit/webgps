var sm = new Ext.grid.CheckboxSelectionModel({});
var proxy = new Ext.data.HttpProxy({
	url : path + '/target/target.do?method=getTarget'
});
var yearStore = new Ext.data.SimpleStore({
	fields:['id', 'name'],
	data:[['2012','2012年'],['2013','2013年'],['2014','2014年']
	,['2015','2015年'],['2016','2016年']
	,['2017','2017年'],['2018','2018年'],['2019','2019年']
	,['2020','2020年'],['2021','2021年'],['2022','2022年']]
});
var yearCombox = new Ext.form.ComboBox({   
     store : yearStore,
     editable:false,
     mode: 'local',
     triggerAction:'all',
     fieldLabel: '选择年份',
     id : 'year',
     displayField:'name',
     valueField :'id',
     value:'2012',
     allowBlank: false,
     blankText: '请选择年份',
     maxHeight: 200
});
var reader = new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success',idProperty: 'id',root: 'data'},[
			{name: 'id'},
			{name: 'deviceId'},
			{name: 'vehicleNumber'},
			{name: 'simcard'},
			{name: 'groupName'},
			{name: 'billAmount'},
			{name: 'cashAmount'},
			{name: 'costAmount'},
			{name: 'visitAmount'},
			{name: 'targetOn'},
			{name: 'year'},
			{name: 'targetDesc'},
			{name: 'allowModify'},
			{name: 'cusVisitAmount'}]);
var targetColumns = [
            sm,
			{id:'id',header: 'id', width: 40, sortable: true, dataIndex: 'id', hidden: true},
			{header: '终端ID', width: 100, sortable: true, dataIndex: 'deviceId'},
			{header: '员工姓名', width: 100, sortable: true, dataIndex: 'vehicleNumber'},
			{header: '手机号码', width: 100, sortable: true, dataIndex: 'simcard'},
			{header: '所属部门', width: 200, sortable: true, dataIndex: 'groupName'},
			{header: '日期', width: 100, sortable: true, dataIndex: 'targetDesc'},
			{header: '签单额(元)', 
				width: 100, 
				sortable: true, 
				dataIndex: 'billAmount',
				editor: new Ext.form.NumberField({
	                allowBlank: false,
	                allowNegative: false
	        	})},
			{header: '回款额(元)', 
				width: 100, 
				sortable: true, 
				dataIndex: 'cashAmount',
				editor: new Ext.form.NumberField({
	                allowBlank: false,
	                allowNegative: false
	        	})},
			{header: '费用额(元)', 
				width: 100, 
				sortable: true, 
				dataIndex: 'costAmount',
				editor: new Ext.form.NumberField({
	                allowBlank: false,
	                allowNegative: false
	        	})},
			{header: '业务员出访次数（次）', 
				width: 130, 
				sortable: true, 
				dataIndex: 'visitAmount',
				editor: new Ext.form.NumberField({
	                allowBlank: false,
	                allowNegative: false,
	                decimalPrecision: 0,
	                maxValue: 1000000000
	        	})},
        	{header: '拜访客户数（个）', 
			width: 100, 
			sortable: true, 
			dataIndex: 'cusVisitAmount',
			editor: new Ext.form.NumberField({
                allowBlank: false,
                allowNegative: false,
                decimalPrecision: 0,
                maxValue: 1000000000
        	})}];
var store = new Ext.data.Store({
	id : 'store',
	restful : true,
	proxy : proxy,
	reader : reader
});
Ext.onReady(function() {
	var targetGrid = new Ext.grid.EditorGridPanel(
	{
		region : 'center',
		loadMask : {
			msg : '查询中...'
		},
		store : store,
		sm : sm,
		columns : targetColumns,
		autoScroll : true,
		enableColumnHide : false,
		tbar : [
				new Ext.form.Label({
					text : '员工姓名'
				}),
				'-',
				new Ext.form.TextField({
					id : 'vehicleNumber',
					width : 80,
					enableKeyEvents : true
				}),
				'-',
				new Ext.form.Label({
					text : '年份'
				}),
				'-',
				yearCombox,
				'-',
				new Ext.Action({
					text : '查询',
					handler : function() {
						var vehicleNumber = Ext.util.Format.trim(Ext.getCmp('vehicleNumber').getValue());
						if(vehicleNumber==''){
					    	Ext.Msg.alert('提示', '请输入员工姓名！');
					        return ;
					    }
						var year = Ext.getCmp('year').getValue();
						store.baseParams = {
							start : 0,
							limit : 20,
							vehicleNumber : encodeURI(vehicleNumber),
							year : year
						};
						store.load({
							params : {
								start : 0,
								limit : 20,
								vehicleNumber : encodeURI(vehicleNumber),
								year : year
							}
						});
					},
					iconCls : 'icon-search'
				}),
				'-',
				new Ext.Action({
		        	text: '保存',
			        handler: function(){
			        	var modified = store.modified;
						var json = '';
						Ext.each(modified, function(item) {
							if (json == '') {
								json = item.data.id + ',' + item.data.billAmount + ',' + item.data.cashAmount + ',' + item.data.costAmount + ',' + item.data.visitAmount + ',' + item.data.cusVisitAmount;
							}else{
								json += ';' + item.data.id + ',' + item.data.billAmount + ',' + item.data.cashAmount + ',' + item.data.costAmount + ',' + item.data.visitAmount + ',' + item.data.cusVisitAmount;
							}
						});
						if (json != '') {
							Ext.Ajax.request({
								url: path + '/target/target.do?method=updateTarget',
								params: { data: json },
								method: 'POST',
								success: function(response) {
									Ext.Msg.alert('提示', '数据更新成功！', function() { store.reload(); });
								},
								failure: function(response) {
									Ext.Msg.alert('提示', '数据更新失败，请稍后再试！');
								}
							});
						}else {
							Ext.Msg.alert('提示', '没有任何需要更新的数据！');
						}
			        },
			        iconCls: 'icon-save'
		    	}) ],
		bbar : new Ext.PagingToolbar({
			pageSize : 20,
			store : store,
			displayInfo : true,
			displayMsg : '第{0}到第{1}条数据 共{2}条',
			emptyMsg : '没有数据'
		})
	});
	
	targetGrid.on('beforeedit',function(obj){
		var r = obj.record;//获取被修改的行
		var allowModify = r.get('allowModify');   
    	if(!allowModify){
			return false; 
		}
    }); 
    
	var viewport = new Ext.Viewport({
		layout : 'border',
		border : false,
		items : [ {
			region : 'center',
			layout : 'fit',
			items : [ targetGrid ]
		} ]
	});
});