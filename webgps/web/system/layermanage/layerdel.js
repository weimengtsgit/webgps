
var delbut = new Ext.Action({
		        text: '删除',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					/*var layerNamefrm = Ext.getCmp('layerNamefrm').getValue();
		        	if(layerNamefrm==''){
		        		parent.Ext.MessageBox.alert('提示', '请输入图层名称!');
		        		return ;
		        	}*/
		        	var tmpselArr = layersm.getSelections();
		        	if(tmpselArr.length <= 0){
		        		parent.Ext.MessageBox.alert('提示', '请选择要删除的图层!');
		        		return ;
		        	}
		        	parent.Ext.MessageBox.confirm('提示', '您确定要删除选择的图层吗?', addConfirm);
		        	
		        },
		        iconCls: 'icon-del'
		    });

		    function addConfirm(btn){
		    	if(btn=='yes'){
		    		parent.Ext.Msg.show({
			           msg: '正在删除 请稍等...',
			           progressText: '删除...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			       add();
		        }
		    }
		    
//删除图层
function add(){

	var tmpselArr = layersm.getSelections();
	var ids = '';
	for(var i=0;i<tmpselArr.length;i++){
		ids +=tmpselArr[i].get('id')+',';
	}
	ids = ids.substring(0,ids.length-1);


	Ext.Ajax.request({
		 //url :url,
		 url:path+'/poi/poi.do?method=deleteLayers',
		 method :'POST', 
		 params: {ids: ids},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		layerstore.load({params:{start:0, limit:20}});
		   		parent.Ext.Msg.alert('提示', '删除成功');
		   		return;
		   }else if(res.result==2){
		 		//treeload.load(root);
		 		layerstore.load({params:{start:0, limit:20}});
		   		parent.Ext.Msg.alert('提示', '请先删除该图层下的标注点');
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


/*图层列表*/
var layerproxy = new Ext.data.HttpProxy({
    url: path+'/poi/poi.do?method=listLayer'
});

var layerreader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'layerName'},
    {name: 'layerDesc'},
    {name: 'mapLevel'},
    {name: 'visible'}
]);

var layerstore = new Ext.data.Store({
    id: 'layerstore',
    restful: true,     // <-- This Store is RESTful
    proxy: layerproxy,
    reader: layerreader
});

var layersm = new Ext.grid.CheckboxSelectionModel({});

function visible(val){
	if(val == '0'){
		return '不可见';
	}else if(val == '1'){
		return '可见';
	}
}

function layermaplevel(val){
	if (val == '11') {
		return '市';
	} else if (val == '13') {
		return '县';
	} else if (val == '14') {
		return '镇';
	}else if (val == '16') {
		return '街道';
	}
}

var layerColumns =  [
	layersm,
	new Ext.grid.RowNumberer({header:'序号',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "图层名称", width: 150, sortable: true, dataIndex: 'layerName',
     renderer: function tips(val) {
				  var tmp = '';
				  if(val.indexOf('_recv')!=-1){
					val = val.substring(0,val.indexOf('_recv'));
					tmp = '<span style="font-weight: bold;display:table;width:100%;" qtip="' + val + '">'+val+'</span>';
				  }else{
					tmp = '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
				  }							
					return tmp;
            	}
    },
    {header: "图层描述", width: 300, sortable: true, dataIndex: 'layerDesc',   			
	 renderer: function tips(val) {
				  var tmp = '';
				  if(val.indexOf('_recv')!=-1){
					val = val.substring(0,val.indexOf('_recv'));
					tmp = '<span style="font-weight: bold;display:table;width:100%;" qtip="' + val + '">'+val+'</span>';
				  }else{
					tmp = '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
				  }							
					return tmp;
            	}
    },
    {header: "图层地图级别", width: 100, sortable: true, dataIndex: 'mapLevel',renderer: layermaplevel},
    {header: "是否可见", width: 100, sortable: true, dataIndex: 'visible',renderer: visible}
];

var layerGrid = new Ext.grid.GridPanel({
        region: 'center',
        width: 450,
        //iconCls: 'icon-grid',
        //frame: true,
        //title: '图层列表',
        //autoExpandColumn: 'name',
        enableColumnHide : false,
        store: layerstore,
        //plugins: [editor],
        columns : layerColumns,
        sm : layersm,
        //sm : smcheckbox,
        margins: '0 0 0 0',
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: layerstore,
            displayInfo: true,
            displayMsg: '第{0}到第{1}条数据 共{2}条',
            emptyMsg: "没有数据"
        }),
	    tbar: [
	        delbut
	    ]
    });
    
var viewport ;

Ext.onReady(function(){
	Ext.QuickTips.init();
	layerstore.load({params:{start:0, limit:20}});
	
    viewport = new Ext.Viewport({layout: 'border',items: [layerGrid]});
    
    //角色下拉列表展开时去查询
    //comboxroleid.on('expand',function(){   
    //    rolecomboexpand();
    //});  
});

