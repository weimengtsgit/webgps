
	var searchValue = '';
	
var delids='';
var delbut = new Ext.Action({
		        text: '删除',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					var tmpRecArr = sm.getSelections();
					var ids = '';
					for(var i=0;i<tmpRecArr.length;i++){
						ids+=tmpRecArr[i].get('entCode')+',';
					}
					if(ids.length>0){
						ids = ids.substring(0,ids.length-1);
					}else{
						parent.Ext.MessageBox.alert('提示', '请选择要删除的企业!');
		        		return ;
					}
					delids = ids;
		        	parent.Ext.MessageBox.confirm('提示', '您确定要删除这些企业吗?', delConfirm);
		        	
		        },
		        iconCls: 'icon-del'
		    });

		    function delConfirm(btn){
		    	if(btn=='yes'){
		    		parent.Ext.Msg.show({
			           msg: '正在删除 请稍等...',
			           progressText: '删除...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			       del();
		        }
		    }


function del(){

	Ext.Ajax.request({
		 //url :url,
		 url:path+'/system/ent.do?method=deleteEnts',
		 method :'POST', 
		 params: { entCodes: encodeURI(delids)},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		store.reload();
		   		parent.Ext.Msg.alert('提示', '删除成功');
		   		return;
		   }else{
		   		//store.reload();
		   		parent.Ext.Msg.alert('提示', "删除失败!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('提示', "删除失败!");
		 }
		});
}

var proxy = new Ext.data.HttpProxy({
    url: path+'/system/ent.do?method=listEnt'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'entCode'},
    {name: 'entName'},
    {name: 'maxUserNum'},
    {name: 'smsAccount'},
    {name: 'smsPwd'},
    {name: 'endTime'},
    {name: 'entStatus'},
    {name: 'smsType'},
    {name: 'visitTjStatus'},
    {name: 'reportStatus'}, {
		name : 'persionGreyInterval'
	}, {
		name : 'carGreyInterval'
	}
]);

var store = new Ext.data.Store({
	autoLoad: {params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) }},
    //id: 'userstore',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				searchValue: encodeURI(searchValue)
    			};
    		}
    	}
    }
});

var sm = new Ext.grid.CheckboxSelectionModel({});

var userColumns =  [
	sm,
	new Ext.grid.RowNumberer({header:'序号',width:40}),
    {header: "企业代码", width: 100, sortable: true, dataIndex: 'entCode'},
    {header: "企业名称", width: 100, sortable: true, dataIndex: 'entName'},
    {header: "最大用户数", width: 100, sortable: true, dataIndex: 'maxUserNum',hidden:true},
    {header: "短信帐号", width: 100, sortable: true, dataIndex: 'smsAccount'},
    {header: "短信密码", width: 100, sortable: true, dataIndex: 'smsPwd',hidden:true},
    {header: "服务到期时间", width: 100, sortable: true, dataIndex: 'endTime', renderer:function (val){
		if(val == 'null')return '';
		return val;
	}},
    {header: "状态", width: 100, sortable: true, dataIndex: 'entStatus', renderer:function (val){
		if(val == '0'){return '停用';}else if(val == '1'){return '正常';}else if(val == '2'){return '服务过期';}
		return val;
	}},
	{header: "短信通道", width: 100, sortable: true, dataIndex: 'smsType', renderer:function (val){
		if(val == 'null'){return '';}else if(val == '1'){return '大汉三通';}else if(val == '3'){return '国都';}
		return val;
	}},
	{header: "拜访统计", width: 100, sortable: true, dataIndex: 'visitTjStatus', renderer:function (val){
		if(val == 'null'){return '否';}else if(val == '0'){return '否';}else if(val == '1'){return '是';}
		return val;
	}},
	{header: "报表日志", width: 100, sortable: true, dataIndex: 'reportStatus', renderer:function (val){
		if(val == 'null'){return '否';}else if(val == '0'){return '否';}else if(val == '1'){return '是';}
		return val;
	}}, {
		header : "人员蓝点时间(分钟)",
		width : 100,
		sortable : true,
		dataIndex : 'persionGreyInterval'
	}, {
		header : "车载灰点时间(分钟)",
		width : 100,
		sortable : true,
		dataIndex : 'carGreyInterval'
	}
];

var userGrid = new Ext.grid.GridPanel({
        region: 'center',
        width: 450,
        loadMask: {msg:'查询中...'},
        //iconCls: 'icon-grid',
        //frame: true,
        //title: 'Users',
        //autoExpandColumn: 'name',
        enableColumnHide : false,
        store: store,
        //plugins: [editor],
        columns : userColumns,
        sm : sm,
        //sm : smcheckbox,
        margins: '0 0 0 0',
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: store,
            displayInfo: true,
            displayMsg: '第{0}到第{1}条数据 共{2}条',
            emptyMsg: "没有数据"
        }),
	    tbar: [
	        new Ext.form.TextField({
	            id: 'DeviceIdField',
	            width: 80,
	            enableKeyEvents: true,
	            listeners: {
        			keypress : function( textField, e ) {
		        		if (e.getKey() == e.ENTER) {
		        			searchValue = Ext.getCmp('DeviceIdField').getValue();
			    			store.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue) }});
			    			
		                }
        			}
        		}
		    }),'-',new Ext.Action({
		        text: '查询',
		        handler: function(){
		        	searchValue = Ext.getCmp('DeviceIdField').getValue();
	    			store.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue) }});
	    			
		        },
		        iconCls: 'icon-search'
		    }),'-',delbut
	    ]
    });

Ext.onReady(function(){
    var viewport = new Ext.Viewport({layout: 'border',items: [userGrid]});
});
