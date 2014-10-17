<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	response.sendRedirect(path+"/error.jsp");
	return;
}
%>
<html>
<head>
  <title></title>
	<link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/buttons.css"/>
 	<script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
	<script type="text/javascript" src="<%=path%>/jstool/provincescities.js"></script>
	
	<script type="text/javascript">
	Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
	var path = '<%=path%>';


var delbut = new Ext.Action({
		        text: '保存',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					var tmpfrmuserAccount = Ext.getCmp('frmuserAccount').getValue();
		        	if(tmpfrmuserAccount==''){
		        		parent.Ext.MessageBox.alert('提示', '请输入登录帐号!');
		        		return ;
		        	}
		        	var tmpfrmuserName = Ext.getCmp('frmuserName').getValue();
		        	if(tmpfrmuserName==''){
		        		parent.Ext.MessageBox.alert('提示', '请输入用户名!');
		        		return ;
		        	}
		        	
		        	var tmpPassword = Ext.getCmp('frmPassword').getValue();
		        	var tmpConfirmPassword = Ext.getCmp('frmConfirmPassword').getValue();
		        	if(tmpPassword==''){
		        		parent.Ext.MessageBox.alert('提示', '请输入密码!');
		        		return ;
		        	}
		        	if(tmpPassword!=tmpConfirmPassword){
		        		parent.Ext.MessageBox.alert('提示', '两次密码输入不一致,请重新输入!');
		        		return ;
		        	}
		        	
		        	var tmpcontrolPassword = Ext.getCmp('frmcontrolPassword').getValue();
		        	var tmpcontrolConfirmPassword = Ext.getCmp('frmcontrolConfirmPassword').getValue();
		        	/*if(tmpcontrolPassword==''){
		        		parent.Ext.MessageBox.alert('提示', '请输入断油断电密码!');
		        		return ;
		        	}*/
		        	if(tmpcontrolPassword!=tmpcontrolConfirmPassword){
		        		parent.Ext.MessageBox.alert('提示', '两次断油断电密码输入不一致,请重新输入!');
		        		return ;
		        	}
		        	
		        	var tmproleid = comboxroleid.getValue();
		        	if(tmproleid==''){
		        		parent.Ext.MessageBox.alert('提示', '请选择角色!');
		        		return ;
		        	}
		        	var tmpfrmprovince = Ext.getCmp('frmprovince').getValue();
		        	/*if(tmpfrmprovince==''){
		        		parent.Ext.MessageBox.alert('提示', '请选择省!');
		        		return ;
		        	}*/
		        	vprovince = tmpfrmprovince;
		        	var tmpfrmcity = Ext.getCmp('frmcity').getValue();
		        	/*if(tmpfrmcity==''){
		        		parent.Ext.MessageBox.alert('提示', '请选择市!');
		        		return ;
		        	}*/
		        	
		        	setTreeGroupIds('');
					getTreeId(root);
					if(treeGroupIds.length>0){
						treeGroupIds = treeGroupIds.substring(0,treeGroupIds.length-1);
					}else{
						//parent.Ext.MessageBox.alert('提示', '请选择用户所管辖部门!');
		        		//return ;
					}
					
		        	parent.Ext.MessageBox.confirm('提示', '您确定要添加新帐号吗?', addConfirm);
		        
		        },
		        iconCls: 'icon-save'
		    });

		    function addConfirm(btn){
		    	if(btn=='yes'){
		    		parent.Ext.Msg.show({
			           msg: '正在保存 请稍等...',
			           progressText: '保存...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			       add();
		        }
		    }
		    


//添加部门
function add(){
var tmpfrmuserAccount = Ext.getCmp('frmuserAccount').getValue();
var tmpfrmuserName = Ext.getCmp('frmuserName').getValue();
var tmpPassword = Ext.getCmp('frmPassword').getValue();
var tmpfrmprovince = Ext.getCmp('frmprovince').getValue();
var tmpfrmcity = Ext.getCmp('frmcity').getValue();
var tmproleid = comboxroleid.getValue();
var tmpcontrolPassword = Ext.getCmp('frmcontrolPassword').getValue();

var urlstr = '&userName='+tmpfrmuserName+ '&userAccount='+tmpfrmuserAccount+ '&password='+tmpPassword+ 
'&province='+tmpfrmprovince+ '&city='+tmpfrmcity+ 
'&groupIds='+treeGroupIds+'&roleId='+tmproleid+'&controlPassword='+tmpcontrolPassword;

	Ext.Ajax.request({
		 //url:path+'/system/user.do?method=addUser'+urlstr,
		 url:path+'/system/user.do?method=addUser',
		 method :'POST', 
		 params: { userName: encodeURI(tmpfrmuserName), userAccount: encodeURI(tmpfrmuserAccount) ,password: encodeURI(tmpPassword), 
			 province: encodeURI(tmpfrmprovince), city: encodeURI(tmpfrmcity), groupIds: treeGroupIds,
			 roleId: tmproleid, controlPassword: encodeURI(tmpcontrolPassword)},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		store.reload();
		   		parent.Ext.Msg.alert('提示', '保存成功');
		   		return;
		   }else if(res.result==3){
		   		parent.Ext.Msg.alert('提示', "已存在此登录帐号!");
		   		return;
		   }else if(res.result==4){
		   		parent.Ext.Msg.alert('提示', "已存在此用户名称!");
		   		return;
		   }else{
		   		//store.reload();
		   		parent.Ext.Msg.alert('提示', "保存失败!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('提示', "保存失败!");
		 }
		});
}


	var root = new Ext.tree.AsyncTreeNode({
		text : '定位平台',
		id : '-100',
		draggable : false // 根节点不容许拖动
	});
	
    var treeload = new Ext.tree.TreeLoader({
    		dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForUserchecked'	
	});
	//treeload.load();
    var tree = new Ext.tree.TreePanel({
                //region: 'west',
                id: 'west-panel', // see Ext.getCmp() below
                title: '用户所管辖部门',
                //height: 300,
		        width: 300,
		        margins:'5 0 5 5',
		        //useArrows:true,
		        autoScroll:true,
		        animate:true,
		        //enableDD:true,
		        containerScroll: true,
		        rootVisible: false,
		        //frame: true,
		        root: root,
		        // auto create TreeLoader
		        loader: treeload
            });
      //角色
	  var rolesStore = new Ext.data.SimpleStore({
	  	fields:['roleid', 'rolename'],
	  	data:[[]]
	  });
	  //角色
      var comboxroleid = new Ext.form.ComboBox({   
        store : rolesStore,   
        editable:false,   
        mode: 'local',   
        triggerAction:'all',   
        fieldLabel: '角色名称',
        id : 'selectroleid',
        displayField:'rolename',
        valueField :'roleid',
        emptyText:'选择角色',
        maxHeight: 200
    });
    
   var simple = new Ext.FormPanel({
            
	        labelWidth: 120, // label settings here cascade unless overridden
	        //url:'save-form.php',
	        frame:true,
	        title: '基本信息',
	        bodyStyle:'padding:5px 5px 0',
	        width: 200,
	        defaults: {width: 150},
	        defaultType: 'textfield',
	        items: [{
	        		//xtype : 'textfield',
	                fieldLabel: '用户名称',
	                id: 'frmuserName',
	                width: 150,
	                allowBlank:false
	            },{
	        		//xtype : 'textfield',
	                fieldLabel: '登录帐号',
	                id: 'frmuserAccount',
	                width: 150,
	                allowBlank:false
	            },{
	                fieldLabel: '密码',
	                id: 'frmPassword',
	                name : 'frmPassword',
	                inputType: 'password',
	                width: 150,
	                allowBlank:false
	            },{
	                fieldLabel: '确认密码',
	                id: 'frmConfirmPassword',
	                name: 'frmConfirmPassword',
	                inputType: 'password',
	                width: 150,
	                //vtype: 'password',
	                //initialPassField: 'frmPassword', // id of the initial password field
	                allowBlank:false
	            },{
	                fieldLabel: '断油断电密码',
	                id: 'frmcontrolPassword',
	                inputType: 'password',
	                width: 150
	                //allowBlank:false
	            },{
	                fieldLabel: '断油断电确认密码',
	                id: 'frmcontrolConfirmPassword',
	                inputType: 'password',
	                width: 150
	                //vtype: 'password',
	                //initialPassField: 'frmPassword', // id of the initial password field
	                //allowBlank:false
	            },
	            //角色
	            comboxroleid,
	            //省
	            comboProvinces,
	            //市
	            comboCities
	        ]
	    });

    
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
    {name: 'password'},
    {name: 'controlPassword'}
]);

var store = new Ext.data.Store({
    id: 'userstore',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader
});

var userColumns =  [
	new Ext.grid.RowNumberer({header:'序号',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "用户名称", width: 100, sortable: true, dataIndex: 'userName'},
    {header: "登录帐号", width: 100, sortable: true, dataIndex: 'userAccount'},
    {header: "省", width: 100, sortable: true, dataIndex: 'province'},
    {header: "市", width: 100, sortable: true, dataIndex: 'city'},
    {header: "创建时间", width: 100, sortable: true, dataIndex: 'createDate'},
    {header: "password", width: 40, sortable: true, dataIndex: 'password',hidden:true},
    {header: "controlPassword", width: 40, sortable: true, dataIndex: 'controlPassword',hidden:true}
    
];

var userGrid = new Ext.grid.GridPanel({
        region: 'west',
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
        //sm : sm,
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

    var tab = new Ext.TabPanel({
    	activeTab: 0,
    	region: 'center',
    	items : [
    		simple,tree
    	]
    });

	store.load({params:{start:0, limit:20}});
	
    var viewport = new Ext.Viewport({layout: 'border',items: [tab,userGrid]});
    
    //角色下拉列表展开时去查询
    //comboxroleid.on('expand',function(){   
    //    rolecomboexpand();
    //});  
    rolecomboexpand();
    
    tree.on('checkchange',function(node,checked){
	    /*if(node.isLeaf()&&checked==true){
	    }else if(node.isLeaf()&&checked==false){
	    }*/
	    node.expand();
	    node.attributes.checked = checked;
	    
	    node.on('expand',function(node){
			//alert('1:'+node);
			node.eachChild(function(child){
		    	child.ui.toggleCheck(checked);
		        child.attributes.checked = checked;
		        child.fireEvent('checkchange',child,checked);
		    });
		});
	
	    node.eachChild(function(child){
	    	//alert('2:'+child);
	    	child.ui.toggleCheck(checked);
	        child.attributes.checked = checked;
	        child.fireEvent('checkchange',child,checked);
	    });
	},tree);
});

//角色下拉列表展开时去查询
function rolecomboexpand(){
	
	Ext.Ajax.request({
		 url : path+'/system/role.do?method=comboboxRole',
		 method :'POST',
		 success : function(request) {
		   var res = request.responseText;
		   //alert(res);
		   rolesStore.loadData(eval(res));
		 },
		 failure : function(request) {
		 }
		});
}

var treeGroupIds = '';
function setTreeGroupIds(ids){
	treeGroupIds = ids;
}
function getTreeId(node){
	//node.expand();
	//416@#地图操作1@#MAP_OPR@##@#地图操作
	var treeid = node.id;
	var idArr = treeid.split('@');
	//if(node.isLeaf()&&node.getUI().isChecked()){
	if(node.getUI().isChecked()){
		treeGroupIds = treeGroupIds + idArr[0] + ',';
		setTreeGroupIds(treeGroupIds);
	}
	
	node.eachChild(function(child) {
		getTreeId(child);
	});
}		

	</script>
</head>
<body>
</body>
</html>