
var delbut = new Ext.Action({
		        text: '����',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					var layerNamefrm = Ext.getCmp('layerNamefrm').getValue();
		        	if(layerNamefrm==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '������ͼ������!');
		        		return ;
		        	}
		        	
		        	var tmpselArr = sm.getSelections();
		        	if(tmpselArr.length<=0){
		        		parent.Ext.MessageBox.alert('��ʾ', '��ѡ��ɼ��û�!');
		        		return ;
		        	}

		        	parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�����ͼ����?', addConfirm);
		        
		        },
		        iconCls: 'icon-save'
		    });
		    
        var toolbar = new Ext.Toolbar({
        	id :'frmtbar',
        	items : [{
	            text: '���',
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
		    


//���ͼ��
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
		   		parent.Ext.Msg.alert('��ʾ', '����ɹ�');
		   		return;
		   }else{
		   		//store.reload();
		   		parent.Ext.Msg.alert('��ʾ', "����ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('��ʾ', "����ʧ��!");
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
    
   var simple = new Ext.FormPanel({
            region: 'west',
	        labelWidth: 70,
	        frame:true,
	        width: 250,
	    	defaults: {width: 150},
	        items: [{
	    		id: 'poiNamefrm',
	    		xtype: 'textfield',
	    		fieldLabel: '����',
	    		allowBlank:false
	    	},{
	    		id: 'poiDescfrm',
	    		xtype: 'textfield',
	    		fieldLabel: '����'
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
