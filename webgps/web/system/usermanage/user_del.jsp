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
		        text: 'ɾ��',
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
							parent.Ext.MessageBox.alert('��ʾ', '����Ա�ʺŲ���ɾ��!');
			        		return ;
						}
					}
					if(ids.length>0){
						ids = ids.substring(0,ids.length-1);
					}else{
						parent.Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫɾ�����ʺ�!');
		        		return ;
					}
					delids = ids;
		        	parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ����Щ�ʺ���?', delConfirm);
		        	
		        },
		        iconCls: 'icon-del'
		    });

		    function delConfirm(btn){
		    	if(btn=='yes'){
		    		parent.Ext.Msg.show({
			           msg: '����ɾ�� ���Ե�...',
			           progressText: 'ɾ��...',
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
		   		parent.Ext.Msg.alert('��ʾ', 'ɾ���ɹ�');
		   		return;
		   }else{
		   		//store.reload();
		   		parent.Ext.Msg.alert('��ʾ', "ɾ��ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('��ʾ', "ɾ��ʧ��!");
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
	// sm.on('rowdeselect',function(sm,rowIndex,record){//��δѡ�е�ʱ��    
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
	new Ext.grid.RowNumberer({header:'���',width:40}),
    {id:'id',header: "id", width: 40, dataIndex: 'id',hidden:true,
		renderer : function(value) {
			adminId +=value+",";
			return value;
		}
    },
    {header: "�û�����", width: 100,dataIndex: 'userName'},
    {header: "��¼�ʺ�", width: 100, dataIndex: 'userAccount'},
    {header: "ʡ", width: 100, dataIndex: 'province'},
    {header: "��", width: 100,  dataIndex: 'city'},
    {header: "����ʱ��", width: 150, sortable: true,dataIndex: 'createDate'},
    {header: "password", width: 40, dataIndex: 'password',hidden:true}
];
var userGrid = new Ext.grid.GridPanel({
        region: 'center',
        loadMask: {msg:'��ѯ��...'},
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
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
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
			        text: '��ѯ',
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