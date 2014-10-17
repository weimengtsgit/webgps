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
			{header: '指标名称', width: 100, sortable: true, dataIndex: 'name'},
			{header: '较差', 
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
			{header: '及格', 
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
			{header: '优秀', 
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
			msg : '查询中...'
		},
		store : store,
		columns : columns,
		autoScroll : true,
		enableColumnHide : false,
		tbar : [
				new Ext.Action({
		        	text: '保存',
			        handler: function(){
			        	var modified = store.modified;
			        	if (modified.length > 0) {
							var json = '';
							var all = store.data;
							for(var i=0,len=store.data.length;i<len;i++){
								var item = store.getAt(i);
								if((item.data.name=='费用额使用率') && (item.data.pass <=item.data.good||item.data.good<=item.data.excellent)) {
									Ext.Msg.alert('提示', '数据校验失败，请确认优秀<及格<较差(费用相反)');
									return;
								}
								if(!(item.data.name=='费用额使用率')&&( item.data.pass>=item.data.good||item.data.good>=item.data.excellent)) {
									Ext.Msg.alert('提示', '数据校验失败，请确认优秀>及格>较差');
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