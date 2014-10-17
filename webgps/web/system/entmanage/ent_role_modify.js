
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
					var record = userGrid.getSelectionModel().getSelected();
		        	//alert(selrecord==undefined);
		        	if(!record){
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
		    		var record = userGrid.getSelectionModel().getSelected();
		    		var RoleId = record.get('id');
		    		Ext.Msg.show({
			           msg: '���ڱ��� ���Ե�...',
			           progressText: '����...',
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

 Ext.onReady(function(){
	
	Ext.QuickTips.init();

    // use RowEditor for editing
    editor = new Ext.ux.grid.RowEditor({
        saveText: 'ȷ��',
        cancelText:'ȡ��'
        
    });
	
    // Create a typical GridPanel with RowEditor plugin
    //��ɫ
    userGrid = new Ext.grid.GridPanel({
        region: 'center',
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
			dataUrl: path+listModule
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
		        loader: treeload,
		        listeners:{
    				load :function(node){
    					
		        	}
		        }
            });
            

   Ext.form.Field.prototype.msgTarget = 'under';
    var centerTop = new Ext.FormPanel({
    	title: '��ɫ����',
    	frame:true,
    	anchor:'right 35%',
    	defaults: {width: 150},
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
    	new Ext.grid.RowNumberer({header:'���',width:40}),
        {header: "��ҵ����", width: 100, sortable: true, dataIndex: 'entCode'},
        {header: "��ҵ����", width: 100, sortable: true, dataIndex: 'entName'},
        {header: "����û���", width: 100, sortable: true, dataIndex: 'maxUserNum',hidden:true},
        {header: "�����ʺ�", width: 100, sortable: true, dataIndex: 'smsAccount'},
        //{header: "��������", width: 100, sortable: true, dataIndex: 'smsPwd',hidden:true},
        {header: "������ʱ��", width: 100, sortable: true, dataIndex: 'endTime', renderer:function (val){
    		if(val == 'null')return '';
    		return val;
    	}},
        {header: "״̬", width: 100, sortable: true, dataIndex: 'entStatus', renderer:function (val){
    		if(val == '0'){return 'ͣ��';}else if(val == '1'){return '����';}else if(val == '2'){return '�������';}
    		return val;
    	}},
    	{header: "����ͨ��", width: 100, sortable: true, dataIndex: 'smsType', renderer:function (val){
    		if(val == 'null'){return '';}else if(val == '1'){return '����ͨ';}else if(val == '3'){return '����';}
    		return val;
    	}},
		{header: "�ݷ�ͳ��", width: 100, sortable: true, dataIndex: 'visitTjStatus', renderer:function (val){
			if(val == 'null'){return '��';}else if(val == '0'){return '��';}else if(val == '1'){return '��';}
			return val;
		}},
		{header: "������־", width: 100, sortable: true, dataIndex: 'reportStatus', renderer:function (val){
			if(val == 'null'){return '��';}else if(val == '0'){return '��';}else if(val == '1'){return '��';}
			return val;
		}}
    ];

    var userGrid_ = new Ext.grid.GridPanel({
            region: 'west',
            width: 300,
            loadMask: {msg:'��ѯ��...'},
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
    		        			searchValue_ = Ext.getCmp('DeviceIdField').getValue();
    	    	    			store_.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue_) }});
    	    	    			
    		                }
            			}
            		}
    		    }),'-',new Ext.Action({
    		        text: '��ѯ',
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
	           msg: '���ڲ�ѯ ���Ե�...',
	           progressText: '��ѯ��...',
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
			   Ext.Msg.alert('��ʾ', "δ��ѯ������!");
		   }
		   
		 },
		 failure : function(request) {
			 Ext.Msg.alert('��ʾ', "δ��ѯ������!");
		 }
		});
	}
 
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

function clearEachMod(node){
	node.expand();
	var treeid = node.id;
	node.getUI().toggleCheck(false);
	node.eachChild(function(child) {
		clearEachMod(child);
	});
}
