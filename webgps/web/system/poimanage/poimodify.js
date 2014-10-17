
var tmpDeviceIdField = '';
var last_year = new Date();
last_year.setFullYear(last_year.getFullYear()-1);
var startTime = (last_year).pattern("yyyy-M-d")+" 00:00:00";
var endTime = (new Date()).pattern("yyyy-M-d")+" 23:59:59";
var delbut = new Ext.Action({
		        text: '�޸�',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					/*var layerNamefrm = Ext.getCmp('layerNamefrm').getValue();
		        	if(layerNamefrm==''){
		        		Ext.MessageBox.alert('��ʾ', '������ͼ������!');
		        		return ;
		        	}*/
		        	var tmpselArr = layersm.getSelected();
		        	if(tmpselArr == undefined){
		        		Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫ�޸ĵı�ע!');
		        		return ;
		        	}
		        	//Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ��ѡ���POI��?', addConfirm);
		        	clearDevice(root);
		        	Ext.getCmp('poiNamefrm').setValue(tmpselArr.get('poiName'));
		        	Ext.getCmp('poiAddressfrm').setValue(tmpselArr.get('address'));
		        	Ext.getCmp('poiPhoumfrm').setValue(tmpselArr.get('telephone'));
		        	Ext.getCmp('poiDescfrm').setValue(tmpselArr.get('poiDesc'));
		        	Ext.getCmp('poiMarkerfrm').setValue(tmpselArr.get('poiDatas'));
		        	
		        	Ext.getCmp('idfrm').setValue(tmpselArr.get('id'));
		        	Ext.getCmp('poiLayerfrm').setValue(tmpselArr.get('layerId'));
		        	//Ext.getCmp('deviceIdfrm').setValue(tmpselArr.get('deviceId'));
		        	Ext.getCmp('iconpathfrm').setValue(tmpselArr.get('iconpath'));
		        	Ext.getCmp('poiImgHid').setValue(tmpselArr.get('iconpath'));
		        	Ext.getCmp('range').setValue(tmpselArr.get('visitDistance'));
		        	
		        	queryPoi(tmpselArr.get('id'));
		        	
		        	var tmppoiDatasArr = tmpselArr.get('poiDatas').split(',');
		        	if(tmppoiDatasArr.length == 2){
		        		Ext.getCmp('poiCoordx').setValue(tmppoiDatasArr[0]);
		        		Ext.getCmp('poiCoordy').setValue(tmppoiDatasArr[1]);
		        		//drawTempObjectID = '';
		        		map.addMarker('poimodify',tmppoiDatasArr[0],tmppoiDatasArr[1],tmpselArr.get('poiName'),'',path+'/images/poi/'+tmpselArr.get('iconpath'),true);
		        	}
		        	
		        	viewport.layout.setActiveItem(modifyPanel);
		        	//api.expandPath('/-100/-101/');
		        	PoiWindow.show();
		        	
		        },
		        iconCls: 'icon-modify'
		    });
//����poi id�������Ѱ��ն�
function queryPoi(id){
	
	Ext.Ajax.request({
		 //url :url,
		 url:path+'/poi/poi.do?method=queryPoi',
		 method :'POST', 
		 params: {id: id},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.deviceIds != undefined){
		 		
		 		var deviceIdfrm = res.deviceIds;
		        	if(deviceIdfrm.length>0){
		        		selDevice(root,','+deviceIdfrm+',');
		        	}
		 		
		   		return;
		   }else{
		   		return;
		   }
		 },
		 failure : function(request) {
		 }
		});
}

function clearDevice(node){
	node.eachChild(function(child){
		child.ui.toggleCheck(false);
        child.attributes.checked = false;
    	clearDevice(child);
    });
}

function selDevice(node,deviceId){
	node.eachChild(function(child){
		var tmpidArr = child.id.split('@#');
		if(tmpidArr.length>0){
			var tmp = ','+tmpidArr[0]+',';
			/*alert(tmp);
			alert(deviceId);
			alert(deviceId.indexOf(tmp));*/
			if(deviceId.indexOf(tmp)!=-1){
				child.ui.toggleCheck(true);
        		child.attributes.checked = true;
			}else{
				child.ui.toggleCheck(false);
        		child.attributes.checked = false;
			}
		}
    	selDevice(child,deviceId);
        //child.fireEvent('checkchange',child,checked);
    });
}

		    function addConfirm(btn){
		    	if(btn=='yes'){
		    		Ext.Msg.show({
			           msg: '���ڱ��� ���Ե�...',
			           progressText: '����...',
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
	//�Ƿ�ѡ���ն�
	var deviceid = '';
	var tmpisguanlian = Ext.getCmp('isguanlianfrm').getValue().getGroupValue();
		    	if(tmpisguanlian == 0){
		    		var treeArr = new Array();
					getTreeId(root,treeArr);
					
					
					for(var i=0;i<treeArr.length;i++){
						var idArr = treeArr[i].id.split('@#');
						deviceid += idArr[0]+',';
					}
					
					if(deviceid.length>0){
						deviceid = deviceid.substring(0,deviceid.length-1);
					}
		    	}
		    	
	
	var id = Ext.getCmp('idfrm').getValue();
	var poiName = Ext.getCmp('poiNamefrm').getValue();
	var poiDesc = Ext.getCmp('poiDescfrm').getValue();
	var poiAddress = Ext.getCmp('poiAddressfrm').getValue();
	var poiPhoum = Ext.getCmp('poiPhoumfrm').getValue();
	var poiMarker = Ext.getCmp('poiMarkerfrm').getValue();
	var poiLayer = Ext.getCmp('poiLayerfrm').getValue();
	
	var poiCoordx = Ext.getCmp('poiCoordx').getValue();
	var poiCoordy = Ext.getCmp('poiCoordy').getValue();
	var poiImgHid = Ext.getCmp('poiImgHid').getValue();
	var range = Ext.getCmp('range').getValue();
	
		Ext.Ajax.request({
		 url:path+'/poi/poi.do?method=updatePoi',
		 method :'POST', 
		 params: { id: id ,
			 poiName: encodeURI(poiName) , poiDesc: encodeURI(poiDesc), poiType:'0',
			 poiDatas: poiCoordx+','+poiCoordy, poiEncryptDatas: poiCoordx+','+poiCoordy, 
			 telephone: encodeURI(poiPhoum), address: encodeURI(poiAddress), 
			 iconpath: poiImgHid, layerId: poiLayer, 
			 deviceIds: deviceid, visitDistance: range
		 },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		//AreaAlarmstore.load({params:{start:0, limit:10}});
		 		var cursor = layerGrid.getBottomToolbar().cursor;
		 		layerstore.load({params:{start:cursor, limit:20 , searchValue: tmpDeviceIdField }});
		 		//layerstore.load({params:{start:0, limit:20}});
		   		Ext.Msg.alert('��ʾ', '�޸ĳɹ�');
		   		return;
		   }else{
		   		//store.reload();
		   		Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
			 return;
		 }
		});
}

      //ͼ��
	  var layerStore = new Ext.data.SimpleStore({
	  	fields:['id', 'name'],
	  	data:[[]]
	  });
	  //ͼ��
      var layerCombox = new Ext.form.ComboBox({   
        store : layerStore,   
        editable:false,   
        mode: 'local',   
        triggerAction:'all',   
        fieldLabel: 'ͼ������',
        id : 'poiLayerfrm',
        displayField:'name',
        valueField :'id',
        emptyText:'ѡ��ͼ��',
        maxHeight: 200
    });
    //ͼ�������б�
    function layerComboexpand(){
    	Ext.Ajax.request({
		 url : path+'/poi/poi.do?method=comboboxListLayer',
		 method :'POST',
		 success : function(request) {
		   var res = request.responseText;
		   //alert(res);
		   layerStore.loadData(eval(res));
		 },
		 failure : function(request) {
		 }
		});
    }
    
    var Poiform = new Ext.FormPanel({
    	labelWidth:75,
	    bodyStyle:'padding:5px 5px 0',
	    autoScroll : true,
	    defaults: {width: 150},
    	items: [{
    		id: 'poiNamefrm',
    		xtype: 'textfield',
    		fieldLabel: '����',
    		allowBlank:false
    	},{
    		id: 'poiAddressfrm',
    		xtype: 'textfield',
    		fieldLabel: '��ַ'
    	},{
    		id: 'poiPhoumfrm',
    		xtype: 'textfield',
    		fieldLabel: '�绰'
    	},{
    		id: 'poiMarkerfrm',
    		xtype: 'hidden'
    	},{
    		id: 'poiDescfrm',
    		xtype: 'textarea',
    		fieldLabel: '����'
    	},layerCombox,{
    		id: 'range',
        	xtype: 'numberfield',
        	fieldLabel: '��Χ',
        	allowBlank: false,
        	value:'500'
    	},{
			xtype : 'radiogroup',
			fieldLabel : '�Ƿ�����ն�',
			width : 80,
			id : 'isguanlianfrm',
			items : [{
				boxLabel : '��',
				name : 'isguanlian',
				inputValue : 0,
				checked : true
			}, {
				boxLabel : '��',
				name : 'isguanlian',
				inputValue : 1
			}]
		},{
			xtype:'fieldset',
			width:265,
			title: '��ע��ʽ',
			autoheight:true,
			layout:'anchor',
			defaultType: 'textfield',
			items:[{
				id:'poi0',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi0').dom.src = path+"/images/poi/poi0.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi0.gif');
						var tmpMarker = map.getOverlayById('poimodify');
						if(tmpMarker != undefined){
						    tmpMarker.option.imageUrl = path+'/images/poi/poi0.gif';
						    map.updateOverlay(tmpMarker);
						}
					}
				}
			},{
				id:'poi1',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi1').dom.src = path+"/images/poi/poi1.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi1.gif');
						var tmpMarker = map.getOverlayById('poimodify');
						if(tmpMarker != undefined){
						    tmpMarker.option.imageUrl = path+'/images/poi/poi1.gif';
						    map.updateOverlay(tmpMarker);
						}
					}
				}
			},{
				id:'poi2',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi2').dom.src = path+"/images/poi/poi2.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi2.gif');
						var tmpMarker = map.getOverlayById('poimodify');
						if(tmpMarker != undefined){
						    tmpMarker.option.imageUrl = path+'/images/poi/poi2.gif';
						    map.updateOverlay(tmpMarker);
						}
					}
				}								            		
			},{
				id:'poi8',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi8').dom.src = path+"/images/poi/poi8.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi8.gif');
						var tmpMarker = map.getOverlayById('poimodify');
						if(tmpMarker != undefined){
						    tmpMarker.option.imageUrl = path+'/images/poi/poi8.gif';
						    map.updateOverlay(tmpMarker);
						}
					}
				}								            		
			},{
				id:'poi9',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi9').dom.src = path+"/images/poi/poi9.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi9.gif');
						var tmpMarker = map.getOverlayById('poimodify');
						if(tmpMarker != undefined){
						    tmpMarker.option.imageUrl = path+'/images/poi/poi9.gif';
						    map.updateOverlay(tmpMarker);
						}
					}
				}								            		
			},{
				id:'poi10',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi10').dom.src = path+"/images/poi/poi10.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi10.gif');
						var tmpMarker = map.getOverlayById('poimodify');
						if(tmpMarker != undefined){
						    tmpMarker.option.imageUrl = path+'/images/poi/poi10.gif';
						    map.updateOverlay(tmpMarker);
						}
					}
				}								            		
			},{
				id:'poi11',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi11').dom.src = path+"/images/poi/poi11.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi11.gif');
						var tmpMarker = map.getOverlayById('poimodify');
						if(tmpMarker != undefined){
						    tmpMarker.option.imageUrl = path+'/images/poi/poi11.gif';
						    map.updateOverlay(tmpMarker);
						}
					}
				}								            		
			},{
				id:'poi12',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi12').dom.src = path+"/images/poi/poi12.gif";
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi12.gif');
						var tmpMarker = map.getOverlayById('poimodify');
						if(tmpMarker != undefined){
						    tmpMarker.option.imageUrl = path+'/images/poi/poi12.gif';
						    map.updateOverlay(tmpMarker);
						}
					}
				}								            		
			},{
				xtype: 'hidden',
				id: 'poiTypeHid'
			},{
				xtype: 'hidden',
				id: 'poiImgHid',
				value: 'poi0.gif'
			},{
				xtype: 'hidden',
				id: 'poiCoordx'
			},{
				xtype: 'hidden',
				id: 'poiCoordy'
			}]
		},{
			xtype : 'hidden',
			id : 'idfrm'
		},{
			xtype : 'hidden',
			id : 'layerIdfrm'
		},{
			xtype : 'hidden',
			id : 'deviceIdfrm'
		},{
			xtype : 'hidden',
			id : 'iconpathfrm'
		}
    	],
		buttons: [{
            text: '���',
            handler: function(){
            	//map.beginDrawMarkerOnMap();
            	Ext.getCmp("poiCoordx").setValue('');
            	Ext.getCmp("poiCoordy").setValue('');
            	//Ext.getCmp('poiImgHid').setValue('');
            	map.removePolygonById('poimodify');
            	map.addEventListener(map.MOUSE_CLICK,MclickMouse);
            	
            }
        },{
            text: '�޸�',
            handler: function(){
            	map.mapObj.removeEventListener(map.mapObj,map.MOUSE_CLICK,MclickMouse);
            	var tmpPoiName = Ext.getCmp("poiNamefrm").getValue();
		    	var tmpPoiMarker = Ext.getCmp("poiMarkerfrm").getValue();
		    	var tmpPoiLayer = Ext.getCmp("poiLayerfrm").getValue();
		    	var tmpid = Ext.getCmp("idfrm").getValue();
		    	var tmprange = Ext.getCmp("range").getValue();
		    	
		    	if(tmpPoiName.length<=0){
		    		Ext.MessageBox.alert('��ʾ', '������������!');
		    		return;
		    	}
		    	if(tmpPoiLayer.length<=0){
		    		Ext.MessageBox.alert('��ʾ', '��ѡ��ͼ��!');
		    		return;
		    	}
		    	if(tmpPoiMarker.length<=0){
		    		Ext.MessageBox.alert('��ʾ', '���ڵ�ͼ�ϱ��!');
		    		return;
		    	}
		    	if(tmprange.length<=0){
		    		Ext.MessageBox.alert('��ʾ', '�����뷶Χֵ!');
		    		return;
		    	}
		    	map.mapObj.setCurrentMouseTool(map.PAN_WHEELZOOM);
		    	//�Ƿ�ѡ���ն�
		    	var tmpisguanlian = Ext.getCmp('isguanlianfrm').getValue().getGroupValue();
		    	if(tmpisguanlian == 0){
		    		var treeArr = new Array();
					getTreeId(root,treeArr);
					if(treeArr.length <= 0){
						Ext.MessageBox.alert('��ʾ', '��ѡ���ն�!');
			    		return;
					}
		    	}
		    	
				
				Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�޸ı�ע��?', addConfirm);
				/*
				var deviceid = '';
				for(var i=0;i<treeArr.length;i++){
					var idArr = treeArr[i].id.split('@#');
					deviceid += idArr[0]+',';
				}
				
				if(deviceid.length>0){
					deviceid = deviceid.substring(0,deviceid.length-1);
				}*/
				
            }
        },{
            text: '����',
            handler: function(){
            	/*map.removePolygonById(drawTempObjectID);
		    	map.mapObj.setCurrentMouseTool(map.PAN_WHEELZOOM);
		    	//����POI���ڲ���
		    	Ext.getCmp("poiNamefrm").reset();
		    	Ext.getCmp("poiAddressfrm").reset();
		    	Ext.getCmp("poiPhoumfrm").reset();
		    	Ext.getCmp("poiDescfrm").reset();
		    	Ext.getCmp("poiMarkerfrm").reset();
		    	//Ext.getCmp("poiLayerfrm").reset();
		    	Ext.getCmp('poiNamefrm').reset();
				Ext.getCmp('poiAddressfrm').reset();
				Ext.getCmp('poiPhoumfrm').reset();
				Ext.getCmp('poiDescfrm').reset();
				Ext.getCmp('poiMarkerfrm').reset();
				Ext.getCmp('idfrm').reset();
				Ext.getCmp('poiLayerfrm').reset();
				//Ext.getCmp('deviceIdfrm').setValue(tmpselArr.get('deviceId'));
				Ext.getCmp('iconpathfrm').reset();*/
            	map.mapObj.removeEventListener(map.mapObj,map.MOUSE_CLICK,MclickMouse);
            	map.mapObj.setCurrentMouseTool(map.PAN_WHEELZOOM);
            	PoiWindow.hide();
            	viewport.layout.setActiveItem(layerGrid);
            }
        }]
    });
var drawTempObjectID = '';

function MclickMouse(param){
	if(drawTempObjectID.length>0){
    	map.removeOverlayById('poimodify');
    }
    drawTempObjectID = 'poimodify';
    var poiCoordx = param.eventX;
	var poiCoordy = param.eventY;
	Ext.getCmp('poiCoordx').setValue(poiCoordx);
	Ext.getCmp('poiCoordy').setValue(poiCoordy);
	var imageUrl = path+'/images/poi/'+Ext.getCmp('poiImgHid').getValue();
	map.addMarker('poimodify',poiCoordx,poiCoordy,'','',imageUrl,false);
	
}
    
//ȡ�ù�ѡ�ն�
function getTreeId(node,treeArr){
	//node.expand();
	//15045412114@#1
	var treeid = node.id;
	var idArr = treeid.split('@#');
	if(idArr.length>1){
		if(node.isLeaf()&&node.getUI().isChecked()){
			treeArr[treeArr.length] = node;
		}
	}
	node.eachChild(function(child) {
		getTreeId(child,treeArr);
	});
}
     var PoiWindow = new Ext.Window({
		title: '���',
		width:310,
		height:450,
		shim:false,
		closeAction: 'hide',
		animCollapse:false,
		constrainHeader:true,
		collapsible:true,
		plain:true,
		resizable:true,
		maximizable:true,
		closable:false,
		animCollapse :true,
		layout:'fit',
		border:false,
		items: [Poiform]
	});
	
var loader = new Ext.tree.TreeLoader({
	dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForEntTerminal'	
});

var root = new Ext.tree.AsyncTreeNode({
		text : '��λƽ̨',
		id : '-100',
		draggable : false // ���ڵ㲻�����϶�
});

var api = new Ext.tree.TreePanel({
		region: 'west',
        rootVisible:false,
        lines:true,
        autoScroll:true,
        width:220,
        loader: loader,
        root:root
});


var poiPanel= new Ext.Panel({
	region: 'center',
    //el:"main",
    html:'<iframe id="mapifr" name="mapifr" src="'+path+'/map/map2.jsp" style="width:100%; height: 100%;" SCROLLING="no" frameborder="0"></iframe>',
    autoScroll:false
});


var modifyPanel = new Ext.Panel({
	layout : 'border',
	items : [api, poiPanel]
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
    {name: 'poiType'},// poi����0��1��2��
    {name: 'poiDatas'},// poi����
    {name: 'poiEncryptDatas'},// poi��������
    {name: 'iconpath'},
    {name: 'poiName'},
    {name: 'poiDesc'},
    {name: 'address'},
    {name: 'telephone'},
    //{name: 'deviceId'},
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

var layersm = new Ext.grid.RowSelectionModel({singleSelect : true});

var layerColumns =  [
	//layersm,
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
    //{header: "deviceId", width: 100, sortable: true, dataIndex: 'deviceId',hidden:true},
    {header: "vehicleNumber", width: 100, sortable: true, dataIndex: 'vehicleNumber',hidden:true},
    {header: "layerId", width: 100, sortable: true, dataIndex: 'layerId',hidden:true},
    {header: "����ͼ��", width: 100, sortable: true, dataIndex: 'layerName'},
    {header: "λ������", width: 300, sortable: true, dataIndex: 'locDesc'},
    {header: "��Χ", width: 100, sortable: true, dataIndex: 'visitDistance'},
    {header: "����ʱ��", width: 130, sortable: true, dataIndex: 'cDate'},
    {header: "��ע��", width: 130, sortable: true, dataIndex: 'termName'}
];

var layerGrid = new Ext.grid.GridPanel({
        //region: 'center',
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
	    }),{
    		xtype : 'timefield',
    		fieldLabel : 'Time',
    		width : 60,
    		format : 'H:i',
    		id : 'startTimeField',
    		value : '08:00',
    		editable : false,
    		invalidText : '��Ч��ʱ���ʽ - �������:12:00',
    		increment : 1
	    },new Ext.form.Label({
        		text:'~'
        }),new Ext.form.DateField({
            	id: 'endDateField',
				fieldLabel: 'Date',
				format:'Y-m-d',
				width: 80,
				editable: false,
				value: new Date()
	    }),{
			xtype : 'timefield',
			fieldLabel : 'Time',
			width : 60,
			format : 'H:i',
			editable : false,
			id : 'endTimeField',
			value : '18:00',
			increment : 1
		},'-',new Ext.form.Label({
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
				        	startTime = Ext.getCmp('startDateField').getValue().format('Y-m-d');
							var tmpstarttime = Ext.getCmp('startTimeField').getValue();
							startTime = startTime + ' ' + tmpstarttime + ':00';
							endTime = Ext.getCmp('endDateField').getValue().format('Y-m-d');
							var tmpendtime = Ext.getCmp('endTimeField').getValue();
							endTime = endTime + ' ' + tmpendtime + ':00';
			    			layerstore.load({params:{start:0, limit:20, searchValue: tmpDeviceIdField, startTime: startTime, endTime: endTime }});
			    			
		                }
        			}
        		}
		    }),'-',new Ext.Action({
		        text: '��ѯ',
		        handler: function(){
		        	tmpDeviceIdField = Ext.getCmp('DeviceIdField').getValue();
		        	tmpDeviceIdField = encodeURI(tmpDeviceIdField);
		        	startTime = Ext.getCmp('startDateField').getValue().format('Y-m-d');
					
		        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
					startTime = startTime + ' ' + tmpstarttime + ':00';
					
					endTime = Ext.getCmp('endDateField').getValue().format('Y-m-d');
					
					var tmpendtime = Ext.getCmp('endTimeField').getValue();
					endTime = endTime + ' ' + tmpendtime + ':00';
					
	    			layerstore.load({params:{start:0, limit:20, searchValue: tmpDeviceIdField, startTime: startTime, endTime: endTime }});
	    			
		        },
		        iconCls: 'icon-search'
		    }),'-',delbut
	    ]
    });
    
var viewport ;
var map;
Ext.onReady(function(){
	Ext.QuickTips.init();
	layerstore.load({params:{start:0, limit:20}});
	
    viewport = new Ext.Viewport({layout: 'card',activeItem : 0,items: [layerGrid,modifyPanel]});
    /*Ext.Msg.show({
		msg: '���ڶ�ȡ���� ���Ե�...',
		progressText: '��ȡ...',
		width:300,
		wait:true,
		//waitConfig: {interval:200},
		icon:'ext-mb-download'
	});*/
    layerComboexpand();
    root.expand(true,false,readback);
    map = document.getElementById('mapifr').contentWindow;
    //��ɫ�����б�չ��ʱȥ��ѯ
    //comboxroleid.on('expand',function(){
    //    rolecomboexpand();
    //});
    
    PoiWindow.on('hide', function(){
    	
    	if(drawTempObjectID.length>0){
	    	map.removeOverlayById('poimodify');
	    }
	    map.mapObj.removeEventListener(map.mapObj,map.MOUSE_CLICK,MclickMouse);
    	map.mapObj.setCurrentMouseTool(map.PAN_WHEELZOOM);
    	//����POI���ڲ���
    	Ext.getCmp("poiNamefrm").setValue('');
    	Ext.getCmp("poiAddressfrm").setValue('');
    	Ext.getCmp("poiPhoumfrm").setValue('');
    	Ext.getCmp("poiDescfrm").setValue('');
    	Ext.getCmp("poiMarkerfrm").setValue('');
    	//Ext.getCmp("poiLayerfrm").reset();
    	Ext.getCmp('poiNamefrm').setValue('');
		Ext.getCmp('poiAddressfrm').setValue('');
		Ext.getCmp('poiPhoumfrm').setValue('');
		Ext.getCmp('poiDescfrm').setValue('');
		Ext.getCmp('idfrm').setValue('');
		Ext.getCmp('poiLayerfrm').setValue('');
		//Ext.getCmp('deviceIdfrm').setValue(tmpselArr.get('deviceId'));
		Ext.getCmp('iconpathfrm').setValue('');
    	
    });
    
api.on('checkchange',function(node,checked){
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
},api);

});

function readback(){
	//Ext.Msg.hide();
}
//��ͼ�������
function fillMarker(shapeMarker){
	Ext.getCmp("poiMarkerfrm").setValue(shapeMarker);
}