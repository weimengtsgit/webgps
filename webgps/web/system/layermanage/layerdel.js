
var delbut = new Ext.Action({
		        text: 'ɾ��',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					/*var layerNamefrm = Ext.getCmp('layerNamefrm').getValue();
		        	if(layerNamefrm==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '������ͼ������!');
		        		return ;
		        	}*/
		        	var tmpselArr = layersm.getSelections();
		        	if(tmpselArr.length <= 0){
		        		parent.Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫɾ����ͼ��!');
		        		return ;
		        	}
		        	parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ��ѡ���ͼ����?', addConfirm);
		        	
		        },
		        iconCls: 'icon-del'
		    });

		    function addConfirm(btn){
		    	if(btn=='yes'){
		    		parent.Ext.Msg.show({
			           msg: '����ɾ�� ���Ե�...',
			           progressText: 'ɾ��...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			       add();
		        }
		    }
		    
//ɾ��ͼ��
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
		   		parent.Ext.Msg.alert('��ʾ', 'ɾ���ɹ�');
		   		return;
		   }else if(res.result==2){
		 		//treeload.load(root);
		 		layerstore.load({params:{start:0, limit:20}});
		   		parent.Ext.Msg.alert('��ʾ', '����ɾ����ͼ���µı�ע��');
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


/*ͼ���б�*/
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
		return '���ɼ�';
	}else if(val == '1'){
		return '�ɼ�';
	}
}

function layermaplevel(val){
	if (val == '11') {
		return '��';
	} else if (val == '13') {
		return '��';
	} else if (val == '14') {
		return '��';
	}else if (val == '16') {
		return '�ֵ�';
	}
}

var layerColumns =  [
	layersm,
	new Ext.grid.RowNumberer({header:'���',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "ͼ������", width: 150, sortable: true, dataIndex: 'layerName',
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
    {header: "ͼ������", width: 300, sortable: true, dataIndex: 'layerDesc',   			
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
    {header: "ͼ���ͼ����", width: 100, sortable: true, dataIndex: 'mapLevel',renderer: layermaplevel},
    {header: "�Ƿ�ɼ�", width: 100, sortable: true, dataIndex: 'visible',renderer: visible}
];

var layerGrid = new Ext.grid.GridPanel({
        region: 'center',
        width: 450,
        //iconCls: 'icon-grid',
        //frame: true,
        //title: 'ͼ���б�',
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
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
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
    
    //��ɫ�����б�չ��ʱȥ��ѯ
    //comboxroleid.on('expand',function(){   
    //    rolecomboexpand();
    //});  
});

