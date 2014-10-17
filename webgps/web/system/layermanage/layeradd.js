
var delbut = new Ext.Action({
		        text: '保存',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					var layerNamefrm = Ext.getCmp('layerNamefrm').getValue();
		        	if(layerNamefrm==''){
		        		parent.Ext.MessageBox.alert('提示', '请输入图层名称!');
		        		return ;
		        	}
		        	
		        	/*var tmpselArr = sm.getSelections();
		        	if(tmpselArr.length<=0){
		        		parent.Ext.MessageBox.alert('提示', '请选择可见用户!');
		        		return ;
		        	}*/

		        	parent.Ext.MessageBox.confirm('提示', '您确定要添加新图层吗?', addConfirm);
		        
		        },
		        iconCls: 'icon-save'
		    });
		    
        var toolbar = new Ext.Toolbar({
        	id :'frmtbar',
        	items : [
        		delbut
        	]
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
		    


//添加图层
function add(){
	var layerNamefrm = Ext.getCmp('layerNamefrm').getValue();
	var tmpselArr = sm.getSelections();
	var ids = '';
	for(var i=0;i<tmpselArr.length;i++){
		ids +=tmpselArr[i].get('id')+',';
	}
	
	if(ids.length > 0){
		ids = ids.substring(0, ids.length - 1);
	}
	
	var layerDescfrm = Ext.getCmp('layerDescfrm').getValue();
	var visiblefrm = Ext.getCmp('visiblefrm').getValue().getGroupValue();
	var mapLevelfrm = Ext.getCmp('mapLevelfrm').getValue();
	layerNamefrm = encodeURI(layerNamefrm);
	layerDescfrm = encodeURI(layerDescfrm);

	Ext.Ajax.request({
		 //url :url,
		 url:path+'/poi/poi.do?method=addLayer',
		 method :'POST', 
		 params: { layerName: layerNamefrm, layerDesc : layerDescfrm, visible: visiblefrm, mapLevel: mapLevelfrm, userIdss: ids},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		//store.reload();
		   		parent.Ext.Msg.alert('提示', '保存成功');
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


   var simple = new Ext.FormPanel({
            region: 'west',
	        labelWidth: 100, // label settings here cascade unless overridden
	        //url:'save-form.php',
	        frame:true,
	        //title: '基本信息',
	        bodyStyle:'padding:5px 5px 0',
	        width: 300,
	        defaults: {width: 180},
	        defaultType: 'textfield',
	        items: [{
	        		//xtype : 'textfield',
	                fieldLabel: '图层名称',
	                id: 'layerNamefrm',
	                width: 150,
	                allowBlank:false
	            },{
	        		//xtype : 'textfield',
	                fieldLabel: '图层描述',
	                id: 'layerDescfrm',
	                width: 150
	            },{
	            	xtype: 'radiogroup', 
	            	fieldLabel: '是否可见',
	            	width: 80, 
	            	id: 'visiblefrm',
					items: [{
						boxLabel: '是', 
						name: 'isShow', 
						inputValue: 1, 
						checked: true
						},{
						boxLabel: '否', 
						name: 'isShow', 
						inputValue: 0
					}]
				},{
					id:'mapLevelfrm',
					xtype: 'combo',
					fieldLabel:'图层地图级别',
					editable:false, 
					width:80,
					displayField:'name',
					store: new Ext.data.ArrayStore({
						fields: ['id', 'name'],
						data : [[11,'市'],[13,'县'],[14,'镇'],[16,'街道']]
							//[[3,'3'],[4,'4'],[5,'5'],[6,'6'],[7,'7'],[8,'8'],[9,'9'],[10,'10'],
							//[11,'11'],[12,'12'],[13,'13'],[14,'14'],[15,'15'],[16,'16'],[17,'17']]
					}),
					displayField:'name',
					valueField: 'id',
					typeAhead: true, 
					mode: 'local', 
					forceSelection: true, 
					triggerAction: 'all',
					value: 16, 
					selectOnFocus:true
				}
	        ],
	        //buttons: toolbar
	        tbar:toolbar
	    });

    
var proxy = new Ext.data.HttpProxy({
    url: path+'/system/user.do?method=listUserWithOutLoginUser'
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

var store = new Ext.data.Store({
    id: 'userstore',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader
});

var sm = new Ext.grid.CheckboxSelectionModel({});

var userColumns =  [
	sm,
	new Ext.grid.RowNumberer({header:'序号',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "用户名称", width: 100, sortable: true, dataIndex: 'userName'},
    {header: "登录帐号", width: 100, sortable: true, dataIndex: 'userAccount'},
    {header: "省", width: 100, sortable: true, dataIndex: 'province'},
    {header: "市", width: 100, sortable: true, dataIndex: 'city'},
    {header: "创建时间", width: 100, sortable: true, dataIndex: 'createDate'},
    {header: "password", width: 40, sortable: true, dataIndex: 'password',hidden:true}
];

var userGrid = new Ext.grid.GridPanel({
        
        region: 'center',
        width: 450,
        //iconCls: 'icon-grid',
        //frame: true,
        title: '帐号列表',
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
        })
	    /*tbar: [
	        delbut
	    ]*/
    });
    
Ext.onReady(function(){

	store.load({params:{start:0, limit:20}});
	
    var viewport = new Ext.Viewport({layout: 'border',items: [simple,userGrid]});
    
    //角色下拉列表展开时去查询
    //comboxroleid.on('expand',function(){   
    //    rolecomboexpand();
    //});  
});
