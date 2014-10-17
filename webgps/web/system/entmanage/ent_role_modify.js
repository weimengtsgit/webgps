
	var scrollwidth=document.documentElement.scrollWidth;
    var scrollheight=document.documentElement.scrollHeight;
    var root;
	var userGrid;
	var editor;
	var treeload;
	var tree;
	//var addbutton = false;
	//修改表格为空的列
	//var delrowflag = false;
    var checked = 'true';
    var searchValue = '';
    var searchValue_ = '';
    var entCode = '';
// Create a standard HttpProxy instance.
/*var proxy = new Ext.data.HttpProxy({
	timeout:60000,
    url: path+'/system/role.do?method=listRoleByEntCode'
});*/

// Typical JsonReader.  Notice additional meta-data params for defining the core attributes of your json-response
/*var reader = new Ext.data.JsonReader({
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
]);*/

// The new DataWriter component.
//var writer = new Ext.data.JsonWriter();

// Typical Store collecting the Proxy, Reader and Writer together.
var store = new Ext.data.ArrayStore({
    id: 'rolestore',
    root: 'data',
    //restful: true,     // <-- This Store is RESTful
    fields: ['id' , 'roleName' , 'roleCode' , 'roleDesc']
    //proxy: proxy,
    //reader: reader
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
	    	Ext.Msg.hide();
		 },
		 failure : function(request) {
			 //Ext.Msg.alert('提示', "修改失败!");
			 Ext.Msg.hide();
		 }
		});
}
/**
*添，删，改按钮
**/

		var delbut = new Ext.Action({
		        text: '修改',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					var record = userGrid.getSelectionModel().getSelected();
		        	//alert(selrecord==undefined);
		        	if(!record){
		        		Ext.MessageBox.alert('提示', '请选择要修改的角色!');
		        		return ;
		        	}
		        	var tmpRoleName = Ext.getCmp('frmRoleName').getValue();
		        	if(tmpRoleName==''){
		        		Ext.MessageBox.alert('提示', '请输入角色名称!');
		        		return ;
		        	}
		        	var tmpRoleCode = Ext.getCmp('frmRoleCode').getValue();
		        	if(tmpRoleCode==''){
		        		Ext.MessageBox.alert('提示', '请输入角色编码!');
		        		return ;
		        	}
		        	if(tmpRoleCode == 'super_administrator' || tmpRoleCode == 'defaultEntAdminRole'){
		        		Ext.MessageBox.alert('提示', '此角色编码已被占用,请输入其它角色编码!');
		        		return ;
		        	}
		        	//alert(selrecord.id);
		        	Ext.MessageBox.confirm('提示', '您确定要修改角色吗?', delConfirm);
		        },
		        iconCls: 'icon-modify'
		    });
		    
		    function delConfirm(btn){
		    	
		    	if(btn=='yes'){
		    		var record = userGrid.getSelectionModel().getSelected();
		    		var RoleId = record.get('id');
		    		Ext.Msg.show({
			           msg: '正在保存 请稍等...',
			           progressText: '保存...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			       deleteModule(RoleId);
		        }
		    }

function deleteModule(RoleId){
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
		 params: { roleId: RoleId , moduleIds : treeModuleIds ,roleName : encodeURI(tmpRoleName),
		 	roleCode : encodeURI(tmpRoleCode) ,roleDesc : encodeURI(tmpRoleDesc)
		 },
		 method :'POST', 
		 timeout: 30000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		 	if(res.result==1){
		 		//store.reload();
		 		//treeload.load(root);
		        //root.expand();
		 		//loadRole();
		   		Ext.Msg.alert('提示', "修改成功!");
		   		return;
		   }else if(res.result==3){
		   	
		   		Ext.Msg.alert('提示', '已存在此角色编码!');
		   		return;
		   }else{
		   	Ext.Msg.alert('提示', '修改失败');
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('提示', "修改失败!");
		 }
		});
}

// load the store immeditately

 Ext.onReady(function(){
	
	Ext.QuickTips.init();

    // use RowEditor for editing
    editor = new Ext.ux.grid.RowEditor({
        saveText: '确定',
        cancelText:'取消'
        
    });
	
    // Create a typical GridPanel with RowEditor plugin
    //角色
    userGrid = new Ext.grid.GridPanel({
        region: 'center',
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
			dataUrl: path+listModule
			//dataUrl:'check-nodes.json'
	});
	
    tree = new Ext.tree.TreePanel({
               // region: 'center',
               anchor:'right 65%',
                id: 'west-panel', // see Ext.getCmp() below
                title: '权限树',
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
		        loader: treeload,
		        listeners:{
    				load :function(node){
    					
		        	}
		        }
            });
            

   Ext.form.Field.prototype.msgTarget = 'under';
    var centerTop = new Ext.FormPanel({
    	title: '角色设置',
    	frame:true,
    	anchor:'right 35%',
    	defaults: {width: 150},
    	labelWidth: 75,
    	defaultType: 'textfield',
    	items: [{
	    	fieldLabel: '角色名称',
	        id: 'frmRoleName',
	        allowBlank:false,
	        width:130,
	        enableKeyEvents : true
	    },{
	        fieldLabel: '角色编码',
	        id: 'frmRoleCode',
	        width:130,
	        allowBlank:false
	    },{
	        fieldLabel: '角色描述',
	        width:130,
	        id: 'frmRoleDesc'
	    }]
    });
    
    
    var center = new Ext.Panel({
    	id : 'center',
    	region : 'east',
        width: 250,
    	layout:'anchor',
    	items:[centerTop,tree]
    });
    
    
    var proxy_ = new Ext.data.HttpProxy({
        url: path+'/system/ent.do?method=listEnt'
    });

    var reader_ = new Ext.data.JsonReader({
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
	    {name: 'reportStatus'}
    ]);

    var store_ = new Ext.data.Store({
    	autoLoad: {params:{start: 0, limit: 20, searchValue: encodeURI(searchValue_) }},
        //id: 'userstore',
        restful: true,     // <-- This Store is RESTful
        proxy: proxy_,
        reader: reader_,
        listeners:{
        	beforeload:{
        		fn: function(thiz,options){
        			this.baseParams ={
        				searchValue: encodeURI(searchValue_)
        			};
        		}
        	}
        }
    });

    var userColumns_ =  [
    	new Ext.grid.RowNumberer({header:'序号',width:40}),
        {header: "企业代码", width: 100, sortable: true, dataIndex: 'entCode'},
        {header: "企业名称", width: 100, sortable: true, dataIndex: 'entName'},
        {header: "最大用户数", width: 100, sortable: true, dataIndex: 'maxUserNum',hidden:true},
        {header: "短信帐号", width: 100, sortable: true, dataIndex: 'smsAccount'},
        //{header: "短信密码", width: 100, sortable: true, dataIndex: 'smsPwd',hidden:true},
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
		}}
    ];

    var userGrid_ = new Ext.grid.GridPanel({
            region: 'west',
            width: 300,
            loadMask: {msg:'查询中...'},
            //iconCls: 'icon-grid',
            //frame: true,
            //title: 'Users',
            //autoExpandColumn: 'name',
            enableColumnHide : false,
            store: store_,
            //plugins: [editor],
            columns : userColumns_,
            //sm : sm,
            //sm : smcheckbox,
            margins: '0 0 0 0',
            bbar: new Ext.PagingToolbar({
                pageSize: 20,
                store: store_,
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
    		        			searchValue_ = Ext.getCmp('DeviceIdField').getValue();
    	    	    			store_.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue_) }});
    	    	    			
    		                }
            			}
            		}
    		    }),'-',new Ext.Action({
    		        text: '查询',
    		        handler: function(){
    		        	searchValue_ = Ext.getCmp('DeviceIdField').getValue();
    	    			store_.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue_) }});
    	    			
    		        },
    		        iconCls: 'icon-search'
    		    }),'-',delbut
    	    ],
    	    listeners: {
            	rowclick : function( grid, rowIndex, e ){
            		var record = grid.getStore().getAt(rowIndex);
            		entCode = record.get('entCode');
            		loadRole();
    		 		clearEachMod(root);
    		 		Ext.getCmp('frmRoleName').setValue('');
    		 		Ext.getCmp('frmRoleDesc').setValue('');
    		 		Ext.getCmp('frmRoleCode').setValue('');
    		 		
            	}
            }
        });
    
	
    var viewport = new Ext.Viewport({layout: 'border',items: [userGrid_, center, userGrid ]});
    
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
    
    root.expand(true);
});

 function loadRole(){
	 	Ext.Msg.show({
	           msg: '正在查询 请稍等...',
	           progressText: '查询中...',
	           width:300,
	           wait:true,
	           //waitConfig: {interval:200},
	           icon:'ext-mb-download'
	       });
		Ext.Ajax.request({
		 //url :url,
		 url:path+'/system/role.do?method=listRoleByEntCode',
		 params: { entCode : encodeURI(entCode) },
		 method :'POST', 
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   if(res.data.length > 0){
			   store.removeAll();
			   for(var i = 0;i < res.data.length;i += 1){
  			   var r = res.data[i];
  			   var defaultData = {
  					    id: r.id,
  					    roleName: r.roleName,
  					    roleCode: r.roleCode,
  					    roleDesc: r.roleDesc
  					};
  					var tmpRecord = new store.recordType(defaultData);
  					store.insert(i,tmpRecord);
  		   }
			   Ext.Msg.hide();
		   }else{
			   Ext.Msg.alert('提示', "未查询到数据!");
		   }
		   
		 },
		 failure : function(request) {
			 Ext.Msg.alert('提示', "未查询到数据!");
		 }
		});
	}
 
//遍历树，check角色拥有的权限  moduleIds=,1223,1229,1214,1215,1251,1273,
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
	//416@#地图操作1@#MAP_OPR@##@#地图操作
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

function clearEachMod(node){
	node.expand();
	var treeid = node.id;
	node.getUI().toggleCheck(false);
	node.eachChild(function(child) {
		clearEachMod(child);
	});
}
