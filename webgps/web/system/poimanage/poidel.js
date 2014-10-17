var last_year = new Date();
last_year.setFullYear(last_year.getFullYear()-1);
var startTime = (last_year).pattern("yyyy-M-d")+" 00:00:00";
var endTime = (new Date()).pattern("yyyy-M-d")+" 23:59:59";

var delbut = new Ext.Action({
		        text: 'ɾ��',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					/*var layerNamefrm = Ext.getCmp('layerNamefrm').getValue();
		        	if(layerNamefrm==''){
		        		Ext.MessageBox.alert('��ʾ', '������ͼ������!');
		        		return ;
		        	}*/
		        	var tmpselArr = layersm.getSelections();
		        	if(tmpselArr.length <= 0){
		        		Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫɾ����POI!');
		        		return ;
		        	}
		        	Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ��ѡ���POI��?', addConfirm);
		        	
		        },
		        iconCls: 'icon-del'
		    });

		    function addConfirm(btn){
		    	if(btn=='yes'){
		    		Ext.Msg.show({
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
		 url:path+'/poi/poi.do?method=deletePois',
		 method :'POST', 
		 params: {ids: ids},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		layerstore.load({params:{start:0, limit:20}});
		   		Ext.Msg.alert('��ʾ', 'ɾ���ɹ�');
		   		return;
		   }else{
		   		//store.reload();
		   		Ext.Msg.alert('��ʾ', "ɾ��ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('��ʾ', "ɾ��ʧ��!");
		 }
		});
}


/*ͼ���б�*/
var layerproxy = new Ext.data.HttpProxy({
    url: path+'/poi/poi.do?method=listPoi'
});

var layerreader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'poiType'},
    {name: 'poiDatas'},
    {name: 'poiEncryptDatas'},
    {name: 'iconpath'},
    {name: 'poiName'},
    {name: 'poiDesc'},
    {name: 'address'},
    {name: 'telephone'},
    {name: 'deviceId'},
    {name: 'vehicleNumber'},
    {name: 'layerId'},
    {name: 'layerName'},
    {name: 'visitDistance'},
    {name: 'locDesc'},
    {name: 'cDate'},
    {name: 'deviceId'},
    {name: 'termName'}
]);

var layerstore = new Ext.data.Store({
    id: 'layerstore',
    restful: true,     // <-- This Store is RESTful
    proxy: layerproxy,
    reader: layerreader,
    listeners:{
		beforeload:{
		    fn: function(thiz,options){
		    	this.baseParams ={
		    		searchValue: tmpDeviceIdField, startTime: startTime, endTime: endTime
		    	};
		    }
		}
	}
});

var layersm = new Ext.grid.CheckboxSelectionModel({});

var layerColumns =  [
	layersm,
	new Ext.grid.RowNumberer({header:'���',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "poiType", width: 100, sortable: true, dataIndex: 'poiType',hidden:true},
    {header: "poiDatas", width: 100, sortable: true, dataIndex: 'poiDatas',hidden:true},
    {header: "poiEncryptDatas", width: 100, sortable: true, dataIndex: 'poiEncryptDatas',hidden:true},
    {header: "iconpath", width: 100, sortable: true, dataIndex: 'iconpath',hidden:true},
    {header: "����", width: 150, sortable: true, dataIndex: 'poiName',
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
    {id: 'poiDesc',header: "����", width: 180, sortable: true, dataIndex: 'poiDesc',
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
    {header: "��ַ", width: 180, sortable: true, dataIndex: 'address',
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
    {header: "�绰", width: 100, sortable: true, dataIndex: 'telephone'},
    {header: "deviceId", width: 100, sortable: true, dataIndex: 'deviceId',hidden:true},
    {header: "vehicleNumber", width: 100, sortable: true, dataIndex: 'vehicleNumber',hidden:true},
    {header: "layerId", width: 100, sortable: true, dataIndex: 'layerId',hidden:true},
    {header: "����ͼ��", width: 100, sortable: true, dataIndex: 'layerName'},
    {header: "λ������", width: 300, sortable: true, dataIndex: 'locDesc'},
    {header: "��Χ", width: 100, sortable: true, dataIndex: 'visitDistance'},
    {header: "����ʱ��", width: 130, sortable: true, dataIndex: 'cDate'},
    {header: "��ע��", width: 130, sortable: true, dataIndex: 'termName'}
];

var tmpDeviceIdField = '';

var layerGrid = new Ext.grid.GridPanel({
        region: 'center',
        width: 450,
        loadMask: {msg:'��ѯ��...'},
        //iconCls: 'icon-grid',
        //frame: true,
        //title: 'ͼ���б�',
        //autoExpandColumn: 'poiDesc',
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
	    tbar: [new Ext.form.Label({
        		text:'����'
        }),new Ext.form.DateField({
				id: 'startDateField',
				fieldLabel: 'Date',
				format:'Y-m-d',
				width: 80,
				editable: false,
				value: last_year
	    }),new Ext.form.Label({
        		text:'~'
        }),new Ext.form.DateField({
            	id: 'endDateField',
				fieldLabel: 'Date',
				format:'Y-m-d',
				width: 80,
				editable: false,
				value: new Date()
	    }),'-',new Ext.form.Label({
        		text:'�ؼ���'
        }),new Ext.form.TextField({
	            id: 'DeviceIdField',
	            width: 80,
	            enableKeyEvents: true,
	            listeners: {
        			keypress : function( textField, e ) {
		        		if (e.getKey() == e.ENTER) {
		        			tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
				        	tmpDeviceIdField = encodeURI(tmpDeviceIdField);
				        	startTime = Ext.getCmp('startDateField').getValue().format('Y-m-d')+" 00:00:00";
				        	endTime = Ext.getCmp('endDateField').getValue().format('Y-m-d')+" 23:59:59";
			    			layerstore.load({params:{start:0, limit:20, searchValue: tmpDeviceIdField }});
		                }
        			}
        		}
		    }),'-',new Ext.Action({
		        text: '��ѯ',
		        handler: function(){
		        	tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
		        	tmpDeviceIdField = encodeURI(tmpDeviceIdField);
		        	startTime = Ext.getCmp('startDateField').getValue().format('Y-m-d')+" 00:00:00";
		        	endTime = Ext.getCmp('endDateField').getValue().format('Y-m-d')+" 23:59:59";
	    			layerstore.load({params:{start:0, limit:20, searchValue: tmpDeviceIdField }});
	    			
		        },
		        iconCls: 'icon-search'
		    }),'-',delbut
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

