var sm = new Ext.grid.CheckboxSelectionModel({});
var proxy = new Ext.data.HttpProxy({
	url : path + '/target/target.do?method=getTarget'
});
var yearStore = new Ext.data.SimpleStore({
	fields:['id', 'name'],
	data:[['2012','2012��'],['2013','2013��'],['2014','2014��']
	,['2015','2015��'],['2016','2016��']
	,['2017','2017��'],['2018','2018��'],['2019','2019��']
	,['2020','2020��'],['2021','2021��'],['2022','2022��']]
});
var yearCombox = new Ext.form.ComboBox({   
     store : yearStore,
     editable:false,
     mode: 'local',
     triggerAction:'all',
     fieldLabel: 'ѡ�����',
     id : 'year',
     displayField:'name',
     valueField :'id',
     value:'2012',
     allowBlank: false,
     blankText: '��ѡ�����',
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
			{header: '�ն�ID', width: 100, sortable: true, dataIndex: 'deviceId'},
			{header: 'Ա������', width: 100, sortable: true, dataIndex: 'vehicleNumber'},
			{header: '�ֻ�����', width: 100, sortable: true, dataIndex: 'simcard'},
			{header: '��������', width: 200, sortable: true, dataIndex: 'groupName'},
			{header: '����', width: 100, sortable: true, dataIndex: 'targetDesc'},
			{header: 'ǩ����(Ԫ)', 
				width: 100, 
				sortable: true, 
				dataIndex: 'billAmount',
				editor: new Ext.form.NumberField({
	                allowBlank: false,
	                allowNegative: false
	        	})},
			{header: '�ؿ��(Ԫ)', 
				width: 100, 
				sortable: true, 
				dataIndex: 'cashAmount',
				editor: new Ext.form.NumberField({
	                allowBlank: false,
	                allowNegative: false
	        	})},
			{header: '���ö�(Ԫ)', 
				width: 100, 
				sortable: true, 
				dataIndex: 'costAmount',
				editor: new Ext.form.NumberField({
	                allowBlank: false,
	                allowNegative: false
	        	})},
			{header: 'ҵ��Ա���ô������Σ�', 
				width: 130, 
				sortable: true, 
				dataIndex: 'visitAmount',
				editor: new Ext.form.NumberField({
	                allowBlank: false,
	                allowNegative: false,
	                decimalPrecision: 0,
	                maxValue: 1000000000
	        	})},
        	{header: '�ݷÿͻ���������', 
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
			msg : '��ѯ��...'
		},
		store : store,
		sm : sm,
		columns : targetColumns,
		autoScroll : true,
		enableColumnHide : false,
		tbar : [
				new Ext.form.Label({
					text : 'Ա������'
				}),
				'-',
				new Ext.form.TextField({
					id : 'vehicleNumber',
					width : 80,
					enableKeyEvents : true
				}),
				'-',
				new Ext.form.Label({
					text : '���'
				}),
				'-',
				yearCombox,
				'-',
				new Ext.Action({
					text : '��ѯ',
					handler : function() {
						var vehicleNumber = Ext.util.Format.trim(Ext.getCmp('vehicleNumber').getValue());
						if(vehicleNumber==''){
					    	Ext.Msg.alert('��ʾ', '������Ա��������');
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
		        	text: '����',
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
									Ext.Msg.alert('��ʾ', '���ݸ��³ɹ���', function() { store.reload(); });
								},
								failure: function(response) {
									Ext.Msg.alert('��ʾ', '���ݸ���ʧ�ܣ����Ժ����ԣ�');
								}
							});
						}else {
							Ext.Msg.alert('��ʾ', 'û���κ���Ҫ���µ����ݣ�');
						}
			        },
			        iconCls: 'icon-save'
		    	}) ],
		bbar : new Ext.PagingToolbar({
			pageSize : 20,
			store : store,
			displayInfo : true,
			displayMsg : '��{0}����{1}������ ��{2}��',
			emptyMsg : 'û������'
		})
	});
	
	targetGrid.on('beforeedit',function(obj){
		var r = obj.record;//��ȡ���޸ĵ���
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