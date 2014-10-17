<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo"%>
<%
	String path = request.getContextPath();
	UserInfo user = (UserInfo) request.getSession().getAttribute(
			"userInfo");
	if (user == null) {
		response.sendRedirect(path + "/error.jsp");
		return;
	}
%>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/css/buttons.css" />
<script type="text/javascript"
	src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
<script type="text/javascript" src="<%=path%>/jstool/provincescities.js"></script>
<script type="text/javascript">
	Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
	var path = '<%=path%>';


var delids='';
var adminId='';
var delbut = new Ext.Action({
		        text: '删除',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					var tmpRecArr = sm.getSelections();
					var ids = '';
					var delId=adminId.split(',', 1);
					for(var i=0;i<tmpRecArr.length;i++){
						ids+=tmpRecArr[i].get('id')+',';
					}
					var noId=ids.split(',');
					for(var i=0;i<noId.length;i++){
						if(delId==noId[i]){
							parent.Ext.MessageBox.alert('提示', '管理员帐号不能删除!');
			        		return ;
						}
					}
					if(ids.length>0){
						ids = ids.substring(0,ids.length-1);
					}else{
						parent.Ext.MessageBox.alert('提示', '请选择要删除的帐号!');
		        		return ;
					}
					delids = ids;
		        	parent.Ext.MessageBox.confirm('提示', '您确定要删除这些帐号吗?', delConfirm);
		        	
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
		 url:path+'/system/user.do?method=deleteUsers&userIds='+delids,
		 method :'POST', 
		 //params: { ctl:'add', parentid : tmpfrmParendId ,starttime:tmpfrmstarttime,endtime:tmpfrmendtime},
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
    url: path+'/system/user.do?method=listUser'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'userName'},
    {name: 'userAccount'},
    {name: 'province'},
    {name: 'city'},
    {name: 'createDate'},
    {name: 'password'}
]);

var sm = new Ext.grid.CheckboxSelectionModel({});
	// sm.on('rowdeselect',function(sm,rowIndex,record){//行未选中的时候    
	//	 adminId=record.get("id");
    // }, this);   

		
var store = new Ext.data.Store({
    id: 'userstore',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader
});
var userColumns =  [
	sm,
	new Ext.grid.RowNumberer({header:'序号',width:40}),
    {id:'id',header: "id", width: 40, dataIndex: 'id',hidden:true,
		renderer : function(value) {
			adminId +=value+",";
			return value;
		}
    },
    {header: "用户名称", width: 100,dataIndex: 'userName'},
    {header: "登录帐号", width: 100, dataIndex: 'userAccount'},
    {header: "省", width: 100, dataIndex: 'province'},
    {header: "市", width: 100,  dataIndex: 'city'},
    {header: "创建时间", width: 150, sortable: true,dataIndex: 'createDate'},
    {header: "password", width: 40, dataIndex: 'password',hidden:true}
];
var userGrid = new Ext.grid.GridPanel({
        region: 'center',
        loadMask: {msg:'查询中...'},
        //width: 450,
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

        viewConfig: {
            forceFit: true
        },bbar: new Ext.PagingToolbar({
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
			        			var tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
					        	tmpDeviceIdField = encodeURI(tmpDeviceIdField);
				    			store.load({params:{start:0, limit:20, searchValue: tmpDeviceIdField }});
				    			
			                }
	        			}
	        		}
			    }),'-',new Ext.Action({
			        text: '查询',
			        handler: function(){
			        	var tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
			        	tmpDeviceIdField = encodeURI(tmpDeviceIdField);
		    			store.load({params:{start:0, limit:20, searchValue: tmpDeviceIdField }});
		    			
			        },
			        iconCls: 'icon-search'
			    }),'-',delbut
	        ]
    });
    
Ext.onReady(function(){
	Ext.QuickTips.init();
	store.load({params:{start:0, limit:20}});
    var viewport = new Ext.Viewport({layout: 'border',items: [userGrid]});
});
	</script>
</head>
<body>
</body>
</html>