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
		        text: '修改',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
		        	
		        	var tmpfrmId = Ext.getCmp('frmId').getValue();
		        	if(tmpfrmId==''){
		        		parent.Ext.MessageBox.alert('提示', '请选择帐号!');
		        		return ;
		        	}
		        	
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
		        	//Ext.getCmp('frmroleid').setValue(tmproleid);
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
					
		        	parent.Ext.MessageBox.confirm('提示', '您确定要修改帐号吗?', modifyConfirm);
		        
		        },
		        iconCls: 'icon-modify'
		    });

		    function modifyConfirm(btn){
		    	if(btn=='yes'){
		    		/*parent.Ext.Msg.show({
			           msg: '正在保存 请稍等...',
			           progressText: '保存...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });*/
			       modify();
		        }
		    }
		    

//修改部门
function modify(){
var tmpfrmId = Ext.getCmp('frmId').getValue();
var tmpfrmuserAccount = Ext.getCmp('frmuserAccount').getValue();
var tmpfrmuserName = Ext.getCmp('frmuserName').getValue();
var tmpPassword = Ext.getCmp('frmPassword').getValue();
var tmpfrmprovince = Ext.getCmp('frmprovince').getValue();
var tmpfrmcity = Ext.getCmp('frmcity').getValue();
var tmproleid = comboxroleid.getValue();

var tmpcontrolPassword = Ext.getCmp('frmcontrolPassword').getValue();

var urlstr = '&userId='+tmpfrmId+'&userName='+tmpfrmuserName+ '&userAccount='+tmpfrmuserAccount+ 
'&password='+tmpPassword+ 
'&province='+tmpfrmprovince+ '&city='+tmpfrmcity+ 
'&groupIds='+treeGroupIds+'&roleId='+tmproleid+'&controlPassword='+tmpcontrolPassword;
//alert(urlstr);
	Ext.Ajax.request({
		 //url :url,
		 url:path+'/system/user.do?method=updateUser',
		 method :'POST', 
		 params: { userId: encodeURI(tmpfrmId), userName : encodeURI(tmpfrmuserName) ,
		 userAccount : encodeURI(tmpfrmuserAccount),
		 password : encodeURI(tmpPassword), province : encodeURI(tmpfrmprovince), 
		 city : encodeURI(tmpfrmcity),groupIds : encodeURI(treeGroupIds), 
		 roleId : encodeURI(tmproleid), controlPassword : encodeURI(tmpcontrolPassword)
		 },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		var cursor = userGrid.getBottomToolbar().cursor;
		 		store.load({params:{start:cursor, limit:20, searchValue: encodeURI(searchValue) }});
		   		parent.Ext.Msg.alert('提示', '修改成功');
		   		return;
		   }else if(res.result==3){
		   		//store.reload();
		   		parent.Ext.Msg.alert('提示', "已存在此登录帐号!");
		   		return;
		   }else if(res.result==4){
		   		//store.reload();
		   		parent.Ext.Msg.alert('提示', "已存在此用户名称!");
		   		return;
		   }else{
		   		//store.reload();
		   		parent.Ext.Msg.alert('提示', "修改失败!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('提示', "修改失败!");
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
	            },
	            {
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
	            comboCities,
	            new Ext.form.Hidden({
                	id:'frmId'
                }),
	            new Ext.form.Hidden({
                	id:'frmroleid'
                })
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
var searchValue = '';
var store = new Ext.data.Store({
	autoLoad: {params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) }},
    id: 'userstore',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				searchValue: encodeURI(searchValue)
    			}
    		}
    	}
    }
});

var sm = new Ext.grid.RowSelectionModel({
	singleSelect: true,
	listeners: {
		rowselect: function(sm, row, rec) {
			Ext.getCmp('frmuserAccount').setValue(rec.get('userAccount'));
			Ext.getCmp('frmuserName').setValue(rec.get('userName'));
			Ext.getCmp('frmPassword').setValue(rec.get('password'));
		    Ext.getCmp('frmConfirmPassword').setValue(rec.get('password'));
		    
		    Ext.getCmp('frmcontrolPassword').setValue(rec.get('controlPassword'));
		    Ext.getCmp('frmcontrolConfirmPassword').setValue(rec.get('controlPassword'));
		    var province = rec.get('province');
		    if(province.length > 0 && province!=null){
		    	comboProvinces.setValue(rec.get('province'));
		    }
		    var city = rec.get('city');
		    if(city.length > 0 && city!=null){
		    	comboCities.setValue(rec.get('city'));
		    }
		    
		    Ext.getCmp('frmId').setValue(rec.get('id'));
		    queryRoleidGroupid(rec.get('id'));
			
	    }
	}
});

function queryRoleidGroupid(userId){

	 parent.Ext.Msg.show({
		msg: '请稍等...',
		//progressText: '删除...',
		width:300,
		wait:true,
		//waitConfig: {interval:200},
		icon:'ext-mb-download'
	});
	
	
	Ext.Ajax.request({
		 url:path+'/system/user.do?method=queryUser&userId='+userId,
		 method :'POST', 
		  success : function(request) {
		   var res = Ext.decode(request.responseText);
		    //alert('1:'+request.responseText);
		    //alert('2:'+res);
		 	var tmproleid = res.roleId.split(',');
		 	Ext.getCmp('frmroleid').setValue(tmproleid[0]);
		 	comboxroleid.setValue(tmproleid[0]);
		 	var tmpgroupids = res.groupIds;
		 	//alert(tmpgroupids);
		 	//if(tmpgroupids.length>0){
		 		checkEachMod(root,tmpgroupids);
		 	//}
		 	parent.Ext.Msg.hide();
		 },
		 failure : function(request) {
			 //parent.Ext.Msg.alert('提示', "修改失败!");
			 parent.Ext.Msg.hide();
		 }
		});
}

function checkEachMod(node,groupIds){
	node.expand();
	var treeid = node.id;
	var idArr = treeid.split('@');
	var id = ','+idArr[0]+',';
	if(groupIds.indexOf(id)!=-1){
		node.getUI().toggleCheck(true);
	}else{
		node.getUI().toggleCheck(false);
	}
	node.eachChild(function(child) {
		checkEachMod(child,groupIds);
	});
}

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
    
    var tab = new Ext.TabPanel({
    	activeTab: 0,
    	region: 'center',
    	items : [
    		simple,tree
    	]
    });
    
Ext.onReady(function(){


	//store.load({params:{start:0, limit:20}});
	
    var viewport = new Ext.Viewport({layout: 'border',items: [tab,userGrid]});
    
    //角色下拉列表展开时去查询
    //comboxroleid.on('expand',function(){   
    //    rolecomboexpand();
    //});  
    rolecomboexpand();
    tab.activate(tree);
    tab.activate(simple);
    
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