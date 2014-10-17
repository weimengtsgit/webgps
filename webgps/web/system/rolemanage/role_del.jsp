<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	response.sendRedirect(path+"/error.jsp");
	return;
}
String roleCode = user.getRoleCode();
String listModule = "/system/module.do?method=listUserModule&checked=true";
if(roleCode.equalsIgnoreCase("super_administrator")||roleCode.equalsIgnoreCase("defaultEntAdminRole")){
	listModule = "/system/module.do?method=listAllModule&checked=true";
}
%>
<html>
<head>
  <title></title>
	<link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/buttons.css"/>
    
 	<script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
	<script type="text/javascript" src="<%=path%>/ext-3.1.1/examples/ux/RowEditor.js"></script>
	
	<script type="text/javascript">
	Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
	var path = '<%=path%>';
	
	var scrollwidth=document.documentElement.scrollWidth;
    var scrollheight=document.documentElement.scrollHeight;
    var root;
	var userGrid;
	var editor;
	var treeload;
	var tree;
	//var addbutton = false;
	//删除表格为空的列
	//var delrowflag = false;
    var checked = 'true';
    
    
// Create a standard HttpProxy instance.
var proxy = new Ext.data.HttpProxy({
	timeout:60000,
    url: path+'/system/role.do?method=listRole'
});

// Typical JsonReader.  Notice additional meta-data params for defining the core attributes of your json-response
var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'roleName'},
    {name: 'roleCode'},
    {name: 'roleDesc'}
    //{name: 'ModuleIds'}
]);

// The new DataWriter component.
//var writer = new Ext.data.JsonWriter();

// Typical Store collecting the Proxy, Reader and Writer together.
var store = new Ext.data.Store({
    id: 'rolestore',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader
    //writer: writer,    // <-- plug a DataWriter into the store just as you would a Reader
    //listeners: {
    //    write : function(store, action, result, response, rs) {
    //        App.setAlert(response.success, response.message);
    //   }
    //}
});

// Let's pretend we rendered our grid-columns with meta-data from our ORM framework.
/*var smcheckbox = new Ext.grid.CheckboxSelectionModel({
	singleSelect: true,
	listeners: {
		rowselect: function(sm, row, rec) {
			
	    	var roleid = rec.get('id');
	    	
	    	checkEachMod(root,moduleIds);
	    }
	}
});*/

var userColumns =  [
	//smcheckbox,
	new Ext.grid.RowNumberer({header:'序号',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "角色名", width: 100, sortable: true, dataIndex: 'roleName'},
    {header: "角色编码", width: 100, sortable: true, dataIndex: 'roleCode'},
    {header: "描述", width: 100, sortable: true, dataIndex: 'roleDesc'}
    //{header: "ModuleIds", width: 100, sortable: true, dataIndex: 'ModuleIds',hidden : true}
];

var sm = new Ext.grid.RowSelectionModel({
	singleSelect: true,
	listeners: {
		rowselect: function(sm, row, rec) {
	    	getModuleIdsByRoleId(rec);
	    	
	    	
	    }
	}
});

function getModuleIdsByRoleId(rec){
	var roleid = rec.get('id');
	 parent.Ext.Msg.show({
		msg: '请稍等...',
		//progressText: '删除...',
		width:300,
		wait:true,
		//waitConfig: {interval:200},
		icon:'ext-mb-download'
	});
	
	Ext.Ajax.request({
		 //url :url,
		 url:path+'/system/role.do?method=getModuleidsByRoleId&roleid='+roleid,
		 method :'POST', 
		 success : function(request) {
		 
		    var res = Ext.decode(request.responseText);
		    checkEachMod(root,res.result);
	    	parent.Ext.Msg.hide();
		 },
		 failure : function(request) {
			 //parent.Ext.Msg.alert('提示', "删除失败!");
			 parent.Ext.Msg.hide();
		 }
		});
}
/**
*添，删，改按钮
**/

		var delbut = new Ext.Action({
		        text: '删除',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
		        	
		        	var selrecord = sm.getSelected();
		        	//alert(selrecord==undefined);
		        	if(selrecord==undefined){
		        		parent.Ext.MessageBox.alert('提示', '请选择要删除的角色!');
		        		return ;
		        	}
		        	//alert(selrecord.id);
		        	parent.Ext.MessageBox.confirm('提示', '您确定要删除吗?', delConfirm);
		        },
		        iconCls: 'icon-del'
		    });
		    
		    function delConfirm(btn){
		    	
		    	if(btn=='yes'){
		    		var selrecord = sm.getSelected();
		    		var RoleId = selrecord.id;
		    		parent.Ext.Msg.show({
			           msg: '正在删除 请稍等...',
			           progressText: '删除...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			       deleteModule(RoleId);
		        }
		    }

function deleteModule(RoleIds){
  	Ext.Ajax.request({
		 //url :url,
		 url:path+'/system/role.do?method=deleteRoles&roleIds='+RoleIds,
		 method :'POST', 
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		 	if(res.result==1){
		 		store.reload();
		 		treeload.load(root);
		        //root.expand();
		   		parent.Ext.Msg.alert('提示', "删除成功!");
		   		
		   		return;
		   }else{
		   		parent.Ext.Msg.alert('提示', '删除失败');
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('提示', "删除失败!");
		 }
		});
}

// load the store immeditately
store.load();

 Ext.onReady(function(){
	
	Ext.QuickTips.init();
	
    // use RowEditor for editing
    editor = new Ext.ux.grid.RowEditor({
        saveText: '确定',
        cancelText:'取消'
        
    });
	
    // Create a typical GridPanel with RowEditor plugin
    userGrid = new Ext.grid.GridPanel({
        region: 'west',
        width: 450,
        loadMask: {msg:'查询中...'},
        //iconCls: 'icon-grid',
        //frame: true,
        //title: 'Users',
        autoExpandColumn: 'name',
        store: store,
        //plugins: [editor],
        columns : userColumns,
        sm : sm,
        //sm : smcheckbox,
        margins: '0 0 0 0',
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
        ],
        viewConfig: {
            forceFit: true
        }
    });

    function onRefresh() {
        store.reload();
    }
    
	root= new Ext.tree.AsyncTreeNode({
		id:'-1',
		text:'权限树',
		draggable : false 
	});
	
    treeload=new Ext.tree.TreeLoader({
			dataUrl: path+'<%=listModule%>'
			//dataUrl:'check-nodes.json'
	});
	
    tree = new Ext.tree.TreePanel({
                region: 'center',
                id: 'west-panel', // see Ext.getCmp() below
                title: '权限树',
                //height: 300,
		        width: 300,
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
            
            root.expand();
            
        var viewport = new Ext.Viewport({layout: 'border',items: [tree,userGrid ]});
        
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

//遍历树，check角色拥有的权限  moduleIds=,1223,1229,1214,1215,1251,1273,
function checkEachMod(node,moduleIds){
	
	node.expand();
	//1173@警力资源管理@JLZY@ 警力资源管理@#@-1
	var treeid = node.id;
	var idArr = treeid.split('@#');
	var id = ','+idArr[0]+',';
	if(moduleIds.indexOf(id)!=-1){
		node.getUI().toggleCheck(true);
	}else{
		node.getUI().toggleCheck(false);
	}
	node.eachChild(function(child) {
		checkEachMod(child,moduleIds);
	});
}
	</script>
</head>
<body>=

</body>
</html>