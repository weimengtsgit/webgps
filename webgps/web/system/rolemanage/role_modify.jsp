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
	//�޸ı��Ϊ�յ���
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
	new Ext.grid.RowNumberer({header:'���',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "��ɫ��", width: 100, sortable: true, dataIndex: 'roleName'},
    {header: "��ɫ����", width: 100, sortable: true, dataIndex: 'roleCode'},
    {header: "����", width: 100, sortable: true, dataIndex: 'roleDesc'}
    //{header: "ModuleIds", width: 100, sortable: true, dataIndex: 'ModuleIds',hidden : true}
];

var sm = new Ext.grid.RowSelectionModel({
	singleSelect: true,
	listeners: {
		rowselect: function(sm, row, rec) {
			Ext.getCmp('frmRoleName').setValue(rec.get('roleName'));
	    	Ext.getCmp('frmRoleCode').setValue(rec.get('roleCode'));
	    	Ext.getCmp('frmRoleDesc').setValue(rec.get('roleDesc'));
	    	getModuleIdsByRoleId(rec);
	    	
	    	
	    }
	}
});

function getModuleIdsByRoleId(rec){
	var roleid = rec.get('id');
	 Ext.Msg.show({
		msg: '���Ե�...',
		//progressText: 'ɾ��...',
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
	    	Ext.Msg.hide();
		 },
		 failure : function(request) {
			 //Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
			 Ext.Msg.hide();
		 }
		});
}
/**
*��ɾ���İ�ť
**/

		var delbut = new Ext.Action({
		        text: '�޸�',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
		        	
		        	var selrecord = sm.getSelected();
		        	//alert(selrecord==undefined);
		        	if(selrecord==undefined){
		        		Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫ�޸ĵĽ�ɫ!');
		        		return ;
		        	}
		        	var tmpRoleName = Ext.getCmp('frmRoleName').getValue();
		        	if(tmpRoleName==''){
		        		Ext.MessageBox.alert('��ʾ', '�������ɫ����!');
		        		return ;
		        	}
		        	var tmpRoleCode = Ext.getCmp('frmRoleCode').getValue();
		        	if(tmpRoleCode==''){
		        		Ext.MessageBox.alert('��ʾ', '�������ɫ����!');
		        		return ;
		        	}
		        	if(tmpRoleCode == 'super_administrator' || tmpRoleCode == 'defaultEntAdminRole'){
		        		Ext.MessageBox.alert('��ʾ', '�˽�ɫ�����ѱ�ռ��,������������ɫ����!');
		        		return ;
		        	}
		        	//alert(selrecord.id);
		        	Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�޸Ľ�ɫ��?', delConfirm);
		        },
		        iconCls: 'icon-modify'
		    });
		    
		    function delConfirm(btn){
		    	
		    	if(btn=='yes'){
		    		Ext.Msg.show({
			           msg: '���ڱ��� ���Ե�...',
			           progressText: '����...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			       deleteModule();
		        }
		    }

function deleteModule(){

	var selrecord = sm.getSelected();
	var RoleId = selrecord.id;
	var tmpRoleName = Ext.getCmp('frmRoleName').getValue();
	var tmpRoleDesc = Ext.getCmp('frmRoleDesc').getValue();
	var tmpRoleCode = Ext.getCmp('frmRoleCode').getValue();
	
	setTreeModuleIds('');
	getTreeId(root);
	if(treeModuleIds.length>0){
		treeModuleIds = treeModuleIds.substring(0,treeModuleIds.length-1);
	}
	
  	Ext.Ajax.request({
		 //url :url,
		 url:path+'/system/role.do?method=updateRole',
		 params: { roleId: RoleId , moduleIds : encodeURI(treeModuleIds) ,roleName : encodeURI(tmpRoleName),
		 	roleCode : encodeURI(tmpRoleCode) ,roleDesc : encodeURI(tmpRoleDesc)
		 },
		 method :'POST', 
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		 	if(res.result==1){
		 		store.reload();
		 		sm.clearSelections(true);
		 		//treeload.load(root);
		        //root.expand();
		   		Ext.Msg.alert('��ʾ', "�޸ĳɹ�!");
		   		
		   		return;
		   }else if(res.result==3){
		   	
		   		Ext.Msg.alert('��ʾ', '�Ѵ��ڴ˽�ɫ����!');
		   		return;
		   }else{
		   	Ext.Msg.alert('��ʾ', '�޸�ʧ��');
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
		 }
		});
}

// load the store immeditately
store.load();

 Ext.onReady(function(){
	
	Ext.QuickTips.init();
	
    // use RowEditor for editing
    editor = new Ext.ux.grid.RowEditor({
        saveText: 'ȷ��',
        cancelText:'ȡ��'
        
    });
	
    // Create a typical GridPanel with RowEditor plugin
    userGrid = new Ext.grid.GridPanel({
        region: 'west',
        width: 450,
        loadMask: {msg:'��ѯ��...'},
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
			    			store.load({params:{start:0, limit:65535, searchValue: tmpDeviceIdField }});
					 		sm.clearSelections(true);
			    			
		                }
        			}
        		}
		    }),'-',new Ext.Action({
		        text: '��ѯ',
		        handler: function(){
		        	var tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
		        	tmpDeviceIdField = encodeURI(tmpDeviceIdField);
	    			store.load({params:{start:0, limit:65535, searchValue: tmpDeviceIdField }});
			 		sm.clearSelections(true);
	    			
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
		text:'Ȩ����',
		draggable : false 
	});
	
    treeload=new Ext.tree.TreeLoader({
			dataUrl: path+'<%=listModule%>'
			//dataUrl:'check-nodes.json'
	});
	
    tree = new Ext.tree.TreePanel({
               // region: 'center',
               anchor:'right 65%',
                id: 'west-panel', // see Ext.getCmp() below
                title: 'Ȩ����',
                //height: 300,
		        //width: 300,
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


   Ext.form.Field.prototype.msgTarget = 'under';
    var centerTop = new Ext.FormPanel({
    	title: '��ɫ����',
    	frame:true,
    	anchor:'right 35%',
    	defaults: {width: 200},
    	labelWidth: 75,
    	defaultType: 'textfield',
    	items: [{
	    	fieldLabel: '��ɫ����',
	        id: 'frmRoleName',
	        allowBlank:false,
	        width:130,
	        enableKeyEvents : true
	    },{
	        fieldLabel: '��ɫ����',
	        id: 'frmRoleCode',
	        width:130,
	        allowBlank:false
	    },{
	        fieldLabel: '��ɫ����',
	        width:130,
	        id: 'frmRoleDesc'
	    }]
    });
    
    
    var center = new Ext.Panel({
    	id : 'center',
    	region : 'center',
    	layout:'anchor',
    	items:[centerTop,tree]
    });
    
	
    var viewport = new Ext.Viewport({layout: 'border',items: [center,userGrid ]});
    
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

//��������check��ɫӵ�е�Ȩ��  moduleIds=,1223,1229,1214,1215,1251,1273,
function checkEachMod(node,moduleIds){
	
	node.expand();
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
    var treeModuleIds = '';
function setTreeModuleIds(ids){
	treeModuleIds = ids;
}
function getTreeId(node){
	//node.expand();
	//416@#��ͼ����1@#MAP_OPR@##@#��ͼ����
	var treeid = node.id;
	var idArr = treeid.split('@#');
	//if(node.isLeaf()&&node.getUI().isChecked()){
	if(node.getUI().isChecked()){
		treeModuleIds = treeModuleIds + idArr[0] + ',';
		setTreeModuleIds(treeModuleIds);
	}
	
	node.eachChild(function(child) {
		getTreeId(child);
	});
}		
	</script>
</head>
<body><br>

</body>
</html>