
      //图层
	  var layerStore = new Ext.data.SimpleStore({
	  	fields:['id', 'name'],
	  	data:[[]]
	  });
	  //图层
      var layerCombox = new Ext.form.ComboBox({   
        store : layerStore,   
        editable:false,   
        mode: 'local',   
        triggerAction:'all',   
        fieldLabel: '图层名称',
        id : 'poiLayerfrm',
        displayField:'name',
        valueField :'id',
        emptyText:'选择图层',
        maxHeight: 200
    });
    //图层下拉列表
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
    	labelWidth:95,
	    bodyStyle:'padding:5px 5px 0',
	    autoScroll : true,
	    defaults: {width: 150},
    	items: [{
    		id: 'poiNamefrm',
    		xtype: 'textfield',
    		fieldLabel: '名称',
    		allowBlank:false
    	},{
    		id: 'poiAddressfrm',
    		xtype: 'textfield',
    		fieldLabel: '地址'
    	},{
    		id: 'poiPhoumfrm',
    		xtype: 'textfield',
    		fieldLabel: '电话'
    	},{
    		id: 'poiMarkerfrm',
    		xtype: 'hidden'
    	},{
    		id: 'poiDescfrm',
    		xtype: 'textarea',
    		fieldLabel: '描述'
    		
    	},{
	    	id: 'poiRange',
	        xtype: 'numberfield',
	        width:100,
	        fieldLabel: '范围',
	        value: 500
	    },layerCombox,{
			xtype : 'radiogroup',
			fieldLabel : '是否关联终端',
			width : 80,
			id : 'isguanlianfrm',
			items : [{
				boxLabel : '是',
				name : 'isguanlian',
				inputValue : 0,
				checked : true
			}, {
				boxLabel : '否',
				name : 'isguanlian',
				inputValue : 1
			}]
		},{
			xtype:'fieldset',
			width:265,
			title: '标注样式',
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
						var tmpMarker = map.getOverlayById(map.drawTempObjectID);
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
						var tmpMarker = map.getOverlayById(map.drawTempObjectID);
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
						var tmpMarker = map.getOverlayById(map.drawTempObjectID);
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
						var tmpMarker = map.getOverlayById(map.drawTempObjectID);
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
						var tmpMarker = map.getOverlayById(map.drawTempObjectID);
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
						var tmpMarker = map.getOverlayById(map.drawTempObjectID);
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
						var tmpMarker = map.getOverlayById(map.drawTempObjectID);
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
						var tmpMarker = map.getOverlayById(map.drawTempObjectID);
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
		}],
		buttons: [
		/*{
            text: '标点',
            handler: function(){
            	//map.beginDrawMarkerOnMap();
            	map.addEventListener(map.MOUSE_CLICK,MclickMouse);
            }
        },*/
        {
            text: '确定',
            handler: function(){
            	map.mapObj.removeEventListener(map.mapObj,map.MOUSE_CLICK,MclickMouse);
            	var tmpPoiName = Ext.getCmp("poiNamefrm").getValue();
		    	var tmppoiCoordx = Ext.getCmp('poiCoordx').getValue();
		    	var tmpPoiLayer = Ext.getCmp("poiLayerfrm").getValue();
		    	var tmppoiRange = Ext.getCmp("poiRange").getValue();
		    	if(tmpPoiName.length<=0){
		    		Ext.MessageBox.alert('提示', '请输入标点名称!');
		    		return;
		    	}
		    	if(tmpPoiLayer.length<=0){
		    		Ext.MessageBox.alert('提示', '请选择图层!');
		    		return;
		    	}
		    	if(tmpPoiLayer.length<=0){
		    		Ext.MessageBox.alert('提示', '请选择图层!');
		    		return;
		    	}
		    	if(tmppoiRange.length<=0){
		    		Ext.MessageBox.alert('提示', '请输入范围值!');
		    		return;
		    	}
		    	if(tmppoiCoordx.length<=0){
		    		Ext.MessageBox.alert('提示', '请在地图上标注点!');
		    		return;
		    	}
		    	map.mapObj.setCurrentMouseTool(map.PAN_WHEELZOOM);
		    	//是否选择终端
		    	var tmpisguanlian = Ext.getCmp('isguanlianfrm').getValue().getGroupValue();
		    	if(tmpisguanlian == 0){
		    		var treeArr = new Array();
					getTreeId(root,treeArr);
					if(treeArr.length <= 0){
						Ext.MessageBox.alert('提示', '请选择终端!');
			    		return;
					}
		    	}
		    	
				/*var tmppoiTypeHid = Ext.getCmp('poiTypeHid').getValue();
				if(tmppoiTypeHid.length<=0){
		    		Ext.MessageBox.alert('提示', '请选择标注点样式!');
		    		return;
		    	}*/
				Ext.MessageBox.confirm('提示', '您确定要添加新标注吗?', addPOI);
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
        }]
    });
    
    function MclickMouse(param){
    	//alert('MclickMouse');
    	if(map.drawTempObjectID.length>0){
    		map.removeOverlayById('drawTempObjectID');
    	}
    	map.drawTempObjectID = 'drawTempObjectID';
    	var poiCoordx = param.eventX;
		var poiCoordy = param.eventY;
		Ext.getCmp('poiCoordx').setValue(poiCoordx);
		Ext.getCmp('poiCoordy').setValue(poiCoordy);
		var imageUrl = path+'/images/poi/'+Ext.getCmp('poiImgHid').getValue();
		map.addMarker('drawTempObjectID',poiCoordx,poiCoordy,'','',imageUrl,false);
		
    }
    
    function MclickMouse_clickImg(param){
    	//alert('MclickMouse_clickImg');
    	if(map.drawTempObjectID.length>0){
    		map.removeOverlayById('drawTempObjectID');
    	}
    	map.drawTempObjectID = 'drawTempObjectID';
    	var poiCoordx = param.eventX;
		var poiCoordy = param.eventY;
		Ext.getCmp('poiCoordx').setValue(poiCoordx);
		Ext.getCmp('poiCoordy').setValue(poiCoordy);
		var imageUrl = '../images/poi/'+Ext.getCmp('poiImgHid').getValue();
		map.addMarker('drawTempObjectID',poiCoordx,poiCoordy,'','',imageUrl,false);
		PoiWindow.show();
		PoiWindow.setPosition( 50, 50);
    }
    
    var PoiWindow = new Ext.Window({
		title: '标点',
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
		closable:true,
		animCollapse :true,
		layout:'fit',
		border:false,
		items: [Poiform]
	});
//地图标点数据
function fillMarker(shapeMarker){
	Ext.getCmp("poiMarkerfrm").setValue(shapeMarker);
	var tmpMarker = map.getOverlayById(map.drawTempObjectID);
    tmpMarker.option.imageUrl = '../images/poi/poi0.gif';
    map.updateOverlay(tmpMarker);
}
