
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
		        	
		        	var tmpselArr = sm.getSelections();
		        	if(tmpselArr.length<=0){
		        		parent.Ext.MessageBox.alert('提示', '请选择可见用户!');
		        		return ;
		        	}

		        	parent.Ext.MessageBox.confirm('提示', '您确定要添加新图层吗?', addConfirm);
		        
		        },
		        iconCls: 'icon-save'
		    });
		    
        var toolbar = new Ext.Toolbar({
        	id :'frmtbar',
        	items : [{
	            text: '标点',
	            handler: function(){
	            	map.beginDrawMarkerOnMap();
	            }
	        },
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
	ids = ids.substring(0,ids.length-1);
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
    
   var simple = new Ext.FormPanel({
            region: 'west',
	        labelWidth: 70,
	        frame:true,
	        width: 250,
	    	defaults: {width: 150},
	        items: [{
	    		id: 'poiNamefrm',
	    		xtype: 'textfield',
	    		fieldLabel: '名称',
	    		allowBlank:false
	    	},{
	    		id: 'poiDescfrm',
	    		xtype: 'textfield',
	    		fieldLabel: '描述'
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
	    	},layerCombox
	        ],
	        buttons: toolbar
	    });

var poiPanel= new Ext.Panel({
	region: 'center',
    //el:"main",
    html:'<div id="main"><iframe id="mapifr" name="mapifr" src="'+path+'/map/map.jsp" style="width:100%; height: 100%;" SCROLLING="no" frameborder="0"></iframe></div>',
    autoScroll:false
});

Ext.onReady(function(){
    var viewport = new Ext.Viewport({layout: 'border',items: [simple,poiPanel]});
    layerComboexpand();
});
