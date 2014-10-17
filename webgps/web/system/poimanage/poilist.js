var searchValue = '';
var last_year = new Date();
last_year.setFullYear(last_year.getFullYear()-1);
var startTime = (last_year).pattern("yyyy-M-d")+" 00:00:00";
var endTime = (new Date()).pattern("yyyy-M-d")+" 23:59:59";

var delbut = new Ext.Action({
		        text: '����',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
		        	Ext.Msg.show({
				    	msg: '���ڵ��� ���Ե�...',
				        progressText: '������...',
				        width:300,
				        wait:true,
				        icon:'ext-mb-download'
				    });
		        	searchValue = Ext.getCmp('DeviceIdField').getValue();
		        	startTime = Ext.getCmp('startDateField').getValue().format('Y-m-d')+" 00:00:00";
		        	endTime = Ext.getCmp('endDateField').getValue().format('Y-m-d')+" 23:59:59";
					/*var tmpparam = 'expExcel=true&searchValue='+encodeURI(searchValue)+'&startTime='+startTime+'&endTime='+endTime;
					document.excelform.action = path+'/poi/poi.do?method=listPoi&'+tmpparam;
    				document.excelform.submit();
    				setTimeout(function(){Ext.MessageBox.hide();},3000);*/
					document.excelform.action = path+'/poi/poi.do?method=listPoi';
					document.getElementById("searchValue").value=encodeURI(searchValue);
					document.getElementById("startTime").value=startTime;
					document.getElementById("endTime").value=endTime;
					document.getElementById("expExcel").value="true";
					document.excelform.submit();
					setTimeout(function(){Ext.MessageBox.hide();},3000);
					//alert(document.getElementById("searchValue").value);
		            /*var loginForm = this.ownerCt.findByType('form')[0].getForm();
		            alert(loginForm.getValues().loginName);
		            loginForm.doAction('submit', {
		                url: path+'/poi/poi.do?method=listPoi',
		                method:'POST',
		                waitMsg:'���ڵ�½...',
		                timeout:10000,//10�볬ʱ,
		                params:{searchValue: searchValue, startTime: startTime, endTime: endTime},
		                success:function(form, action){
		                    var isSuc = action.result.success;
		                    if(isSuc) {
		                        //��ʾ�û���½�ɹ�
		                        Ext.Msg.alert('��Ϣ', '��½�ɹ�..');
		                    }
		                },
		                failure:function(form, action){
		                    alert('��½ʧ��');
		                }
		            });
		            this.ownerCt.close();*/
		            
		        },
		        iconCls: 'icon-excel'
		    });



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
		    		searchValue: encodeURI(searchValue), startTime: startTime, endTime: endTime
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
    //{header: "poiType", width: 100, sortable: true, dataIndex: 'poiType',hidden:true},
    //{header: "poiDatas", width: 100, sortable: true, dataIndex: 'poiDatas',hidden:true},
    //{header: "poiEncryptDatas", width: 100, sortable: true, dataIndex: 'poiEncryptDatas',hidden:true},
    //{header: "iconpath", width: 100, sortable: true, dataIndex: 'iconpath',hidden:true},
    {header: "����", width: 100, sortable: true, dataIndex: 'poiName'},
    {id: 'poiDesc',header: "����", width: 180, sortable: true, dataIndex: 'poiDesc'},
    {header: "��ַ", width: 100, sortable: true, dataIndex: 'address'},
    {header: "�绰", width: 100, sortable: true, dataIndex: 'telephone'},
    //{header: "deviceId", width: 100, sortable: true, dataIndex: 'deviceId',hidden:true},
    //{header: "vehicleNumber", width: 100, sortable: true, dataIndex: 'vehicleNumber',hidden:true},
    //{header: "layerId", width: 100, sortable: true, dataIndex: 'layerId',hidden:true},
    {header: "����ͼ��", width: 100, sortable: true, dataIndex: 'layerName'},
    {header: "��Χ", width: 100, sortable: true, dataIndex: 'visitDistance'},
    {header: "λ������", width: 300, sortable: true, dataIndex: 'locDesc'},
    {header: "����ʱ��", width: 130, sortable: true, dataIndex: 'cDate'},
    {header: "��ע��", width: 130, sortable: true, dataIndex: 'termName'}
];

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
	    tbar: [
	        new Ext.form.Label({
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
	    }),'-',new Ext.form.TextField({
	            id: 'DeviceIdField',
	            width: 80,
	            enableKeyEvents: true,
	            listeners: {
        			keypress : function( textField, e ) {
		        		if (e.getKey() == e.ENTER) {
		        			searchValue = Ext.getCmp('DeviceIdField').getValue();
				        	startTime = Ext.getCmp('startDateField').getValue().format('Y-m-d')+" 00:00:00";
				        	endTime = Ext.getCmp('endDateField').getValue().format('Y-m-d')+" 23:59:59";
			    			layerstore.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue), startTime: startTime, endTime: endTime }});
			    			
		                }
        			}
        		}
		    }),'-',new Ext.Action({
		        text: '��ѯ',
		        handler: function(){
		        	searchValue = Ext.getCmp('DeviceIdField').getValue();
		        	startTime = Ext.getCmp('startDateField').getValue().format('Y-m-d')+" 00:00:00";
		        	endTime = Ext.getCmp('endDateField').getValue().format('Y-m-d')+" 23:59:59";
	    			layerstore.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue), startTime: startTime, endTime: endTime }});
	    			
		        },
		        iconCls: 'icon-search'
		    }),'-',delbut
	    ]
    });
    
var viewport ;

Ext.onReady(function(){

	layerstore.load({params:{start:0, limit:20}});
	
    viewport = new Ext.Viewport({layout: 'border',items: [layerGrid]});
    
    //��ɫ�����б�չ��ʱȥ��ѯ
    //comboxroleid.on('expand',function(){   
    //    rolecomboexpand();
    //});  
});

