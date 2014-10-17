var proxy = new Ext.data.HttpProxy({
	url : path + '/target/target.do?method=getRate'
});
var reader = new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success',idProperty: 'id',root: 'data'},[
			{name: 'id'},
			{name: 'type'},
			{name: 'name'},
			{name: 'pass'},
			{name: 'good'},
			{name: 'excellent'}]);
var columns = [
			{header: 'ID', width: 40, sortable: true, dataIndex: 'type'},
			{header: 'ָ������', width: 100, sortable: true, dataIndex: 'name'},
			{header: '�ϲ�', 
				width: 100, 
				sortable: true, 
				dataIndex: 'pass',
				editor: new Ext.form.NumberField({
	                allowBlank: false,
	                allowNegative: false,
	                decimalPrecision: 0,
	                maxValue: 100000
	        	}),
	        	renderer: function(value){
					return value+'%';
				}},
			{header: '����', 
				width: 100, 
				sortable: true, 
				dataIndex: 'good',
				editor: new Ext.form.NumberField({
	                allowBlank: false,
	                allowNegative: false,
	                decimalPrecision: 0,
	                maxValue: 100000
	        	}),
	        	renderer: function(value){
					return value+'%';
				}},
			{header: '����', 
				width: 100, 
				sortable: true, 
				dataIndex: 'excellent',
				editor: new Ext.form.NumberField({
	                allowBlank: false,
	                allowNegative: false,
	                decimalPrecision: 0,
	                maxValue: 100000
	        	}),
	        	renderer: function(value){
					return value+'%';
				}}];
var store = new Ext.data.Store({
	id : 'store',
	autoLoad: true,
	restful : true,
	proxy : proxy,
	reader : reader
});
Ext.onReady(function() {
	var grid = new Ext.grid.EditorGridPanel(
	{
		region : 'center',
		loadMask : {
			msg : '��ѯ��...'
		},
		store : store,
		columns : columns,
		autoScroll : true,
		enableColumnHide : false,
		tbar : [
				new Ext.Action({
		        	text: '����',
			        handler: function(){
			        	var modified = store.modified;
			        	if (modified.length > 0) {
							var json = '';
							var all = store.data;
							for(var i=0,len=store.data.length;i<len;i++){
								var item = store.getAt(i);
								if((item.data.name=='���ö�ʹ����') && (item.data.pass <=item.data.good||item.data.good<=item.data.excellent)) {
									Ext.Msg.alert('��ʾ', '����У��ʧ�ܣ���ȷ������<����<�ϲ�(�����෴)');
									return;
								}
								if(!(item.data.name=='���ö�ʹ����')&&( item.data.pass>=item.data.good||item.data.good>=item.data.excellent)) {
									Ext.Msg.alert('��ʾ', '����У��ʧ�ܣ���ȷ������>����>�ϲ�');
									return;
								}
								if (json == '') {
								    if(item.data.type == 3) {
								        json = item.data.id + ',' + item.data.type + ',' + item.data.excellent + ',' + item.data.good + ',' + item.data.pass;
								    } else {
								        json = item.data.id + ',' + item.data.type + ',' + item.data.pass + ',' + item.data.good + ',' + item.data.excellent;
								    }
								}else{
								    if(item.data.type == 3) {
								        json += ';' + item.data.id + ',' + item.data.type + ',' + item.data.excellent + ',' + item.data.good + ',' + item.data.pass;
								    } else {
								        json += ';' + item.data.id + ',' + item.data.type + ',' + item.data.pass + ',' + item.data.good + ',' + item.data.excellent;
								    }
									
								}
							}
						
							Ext.Ajax.request({
								url: path + '/target/target.do?method=updateRate',
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
		    	}) ]
	});
	
	var viewport = new Ext.Viewport({
		layout : 'border',
		border : false,
		items : [ {
			region : 'center',
			layout : 'fit',
			items : [ grid ]
		} ]
	});
});