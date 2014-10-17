<%@ page contentType="text/html;charset=GBK"%>
<%
String path = request.getContextPath();
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache, must-revalidate">
    <title>�޸��û��Ա����Ϣ</title>
    <link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
    <script language="javascript">
    var path = '<%=path%>';
    Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
    var LocString=String(window.document.location.href);
    function getQueryStr(str){   
        var rs = new RegExp("(^|)"+str+"=([^\&]*)(\&|$)","gi").exec(LocString), tmp;   
        if(tmp=rs){   
            return tmp[2];   
        }   
        // parameter cannot be found   
        return "";   
    }   
    
	var tmpid = getQueryStr("id");
	var tmpname = getQueryStr("name");
	var tmpaddress = getQueryStr("address");
	var tmptelephone = getQueryStr("telephone");
	var tmpdesc = getQueryStr("desc");
	var tmpvisitDistance = getQueryStr("visitDistance");
	var tmpiconpath = getQueryStr("iconpath");
	var tmplayerid = getQueryStr("layerid");
	var tmppoiDatas = getQueryStr("poiDatas");
	var tmppoiEncryptDatas = getQueryStr("poiEncryptDatas");
	var tmpeditable = getQueryStr("editable");
	//alert(tmpeditable);
    /*//ͼ�������б�
    function layerComboexpand(){
    	Ext.Ajax.request({
		 url : path+'/poi/poi.do?method=comboboxListLayer',
		 method :'POST',
		 success : function(request) {
		   var res = request.responseText;
		   //alert(res);
		   layerStore.loadData(eval(res));
		   Ext.getCmp('poiLayerfrm').setValue(tmplayerid);
		 },
		 failure : function(request) {
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
        disabled:true,
        maxHeight: 200
    });*/
    
Ext.onReady(function(){
	var flag = true;
	if(tmpeditable == 'true'){
		flag = false;
	}

//id='+tmppoiid+'&name='+tmppoiName+'&address='+tmpaddress+'&telephone='+tmptelephone+
//'&desc='+tmppoiDesc+'&visitDistance='+tmpvisitDistance+'&iconpath='+tmppois[j].iconpath+'&layerid='+layerid;
	
    var Poiform = new Ext.Panel({
    	labelWidth:95,
	    bodyStyle:'padding:5px 5px 0',
	    autoScroll : true,
	    renderTo: Ext.getBody(),
	    defaults: {width: 300},
    	items: [{
        	xtype: 'compositefield',
        	items:[{
            	xtype: 'displayfield', 
                value: '����:'
            },{
	    		id: 'poiNamefrm',
	    		xtype: 'textfield',
	    		width:100,
	    		//disabled:true,
            	readOnly: true,
	    		value: tmpname
	    		//fieldLabel: '����',
	    	},{
            	xtype: 'displayfield', 
                value: '��ַ:'
            },{
	    		id: 'address',
	    		xtype: 'textfield',
	    		width:100,
	    		//disabled:true,
            	readOnly: true,
	    		value: tmpaddress
	    		//fieldLabel: '��ַ'
	    	}]
        },{
        	xtype: 'compositefield',
        	items:[{
            	xtype: 'displayfield', 
                value: '�绰:'
            },{
	    		id: 'poiPhoumfrm',
	    		xtype: 'textfield',
	    		width:100,
	    		//disabled:true,
            	readOnly: true,
	    		value: tmptelephone
	    	},{
            	xtype: 'displayfield', 
                value: '��Χ:'
            },{
	    		id: 'range',
	        	xtype: 'numberfield',
	        	width:100,
	    		//disabled:true,
            	readOnly: true,
	    		value: tmpvisitDistance
	    	}]
        },{
        	xtype: 'compositefield',
        	items:[{
            	xtype: 'displayfield', 
                value: '����:'
            },{
	    		id: 'poiDescfrm',
	    		xtype: 'textarea',
	    		height:50,
	    		width:200,
	    		//disabled:true,
            	readOnly: true,
	    		value: tmpdesc
	    	}]
        },/*{
        	xtype: 'compositefield',
        	items:[{
            	xtype: 'displayfield', 
                value: 'ͼ��:'
            },layerCombox]
        },*/{
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

					}
				}								            		
			}]
		},{
        	xtype: 'compositefield',
        	items:[{
        		id: 'modify_btn',
	    		xtype: 'button',
	    		text: '�޸�',
	    		hidden: flag,
	    		handler: function(){
	    			if(this.text == '�޸�'){
	    				Ext.getCmp('modify_btn').setText('����');
	    				Ext.getCmp('del_btn').setText('ȡ��');
	    				Ext.getCmp('poiNamefrm').setDisabled(false);
		    			Ext.getCmp('address').setDisabled(false);
		    			Ext.getCmp('poiPhoumfrm').setDisabled(false);
		    			Ext.getCmp('range').setDisabled(false);
		    			Ext.getCmp('poiDescfrm').setDisabled(false);
	    			}else{
	    				modify();
	    				Ext.getCmp('modify_btn').setText('�޸�');
	    				Ext.getCmp('del_btn').setText('ɾ���õ�');
	    				Ext.getCmp('poiNamefrm').setDisabled(true);
		    			Ext.getCmp('address').setDisabled(true);
		    			Ext.getCmp('poiPhoumfrm').setDisabled(true);
		    			Ext.getCmp('range').setDisabled(true);
		    			Ext.getCmp('poiDescfrm').setDisabled(true);
	    			}
	    		}
	    	},{
	    		id: 'del_btn',
	        	xtype: 'button',
	        	text: 'ɾ���õ�',
	        	hidden: flag,
	        	handler: function(){
	        		if(this.text == 'ɾ���õ�'){
	        			del();
	        		}else{
	        			Ext.getCmp('modify_btn').setText('�޸�');
	        			Ext.getCmp('del_btn').setText('ɾ���õ�');
	        			Ext.getCmp('poiNamefrm').setDisabled(true);
		    			Ext.getCmp('address').setDisabled(true);
		    			Ext.getCmp('poiPhoumfrm').setDisabled(true);
		    			Ext.getCmp('range').setDisabled(true);
		    			Ext.getCmp('poiDescfrm').setDisabled(true);
		    			
		    			Ext.getCmp('poiNamefrm').setValue(tmpname);
		    			Ext.getCmp('address').setValue(tmpaddress);
		    			Ext.getCmp('poiPhoumfrm').setValue(tmptelephone);
		    			Ext.getCmp('range').setValue(tmpvisitDistance);
		    			Ext.getCmp('poiDescfrm').setValue(tmpdesc);
		    			Ext.getCmp('poiImgHid').setValue(tmpiconpath);
		    			
	        		}
	        	}
	    	}]
	    },{
			xtype : 'hidden',
			id : 'idfrm',
			value: tmpid
		},{
			xtype: 'hidden',
			id: 'poiTypeHid'
		},{
			xtype: 'hidden',
			id: 'poiImgHid',
			value: tmpiconpath
		},{
			xtype: 'hidden',
			id: 'poiDatas',
			value: tmppoiDatas
		},{
			xtype: 'hidden',
			id: 'poiEncryptDatas',
			value: tmppoiEncryptDatas
		}]
    });
    
    //layerComboexpand();
    
});

function modify(){

	var id = Ext.getCmp('idfrm').getValue();
	var poiName = Ext.getCmp('poiNamefrm').getValue();
	var poiDesc = Ext.getCmp('poiDescfrm').getValue();
	var address = Ext.getCmp('address').getValue();
	var poiPhoum = Ext.getCmp('poiPhoumfrm').getValue();
	//var poiLayer = Ext.getCmp('poiLayerfrm').getValue();
	var poiImgHid = Ext.getCmp('poiImgHid').getValue();
	var poiDatas = Ext.getCmp('poiDatas').getValue();
	var poiEncryptDatas = Ext.getCmp('poiEncryptDatas').getValue();
	var range = Ext.getCmp('range').getValue();
	
	if(poiName == ''){
		parent.parent.Ext.Msg.alert('��ʾ', '����������!');
		return;
	}
	/*if(poiDesc == ''){
		parent.parent.Ext.Msg.alert('��ʾ', '����������!');
		return;
	}
	if(poiPhoum == ''){
		parent.parent.Ext.Msg.alert('��ʾ', '������绰!');
		return;
	}
	if(address == ''){
		parent.parent.Ext.Msg.alert('��ʾ', '�������ַ!');
		return;
	}*/
	if(range == ''){
		parent.parent.Ext.Msg.alert('��ʾ', '�����뷶Χ!');
		return;
	}
	
		parent.parent.Ext.Msg.show({
			msg: '���ڱ��� ���Ե�...',
			progressText: '����...',
			width:300,
			wait:true,
			//waitConfig: {interval:200},
			icon:'ext-mb-download'
		});
		
		Ext.Ajax.request({
		 url:path+'/poi/poi.do?method=updatePoiProperties',
		 method :'POST', 
		 params: { id: id ,
			 poiName: encodeURI(poiName) , poiDesc: encodeURI(poiDesc),
			 address: encodeURI(address) ,
			 telephone: encodeURI(poiPhoum), poiType:'0',
			 iconpath: poiImgHid,visitDistance: range, 
			 poiDatas: poiDatas, poiEncryptDatas: poiEncryptDatas
		 },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		//AreaAlarmstore.load({params:{start:0, limit:10}});
		   		parent.parent.Ext.Msg.alert('��ʾ', '�޸ĳɹ�');
		   		
		   			
				var tmpArr = new Array();
		   		var tmpmapLevel = '';
				for(var i=0;i<parent.visiPoiObjArr.length;i++){
					if(parent.visiPoiObjArr[i].poiid != ('visiPoi@'+id)){
					   	tmpArr.push(parent.visiPoiObjArr[i]);
					}else{
						tmpmapLevel = parent.visiPoiObjArr[i].mapLevel;
					}
				}
				parent.visiPoiObjArr = [];
				parent.visiPoiObjArr = tmpArr;
				//parent.clearMapById('visiPoi@'+id);
				
				
		   		//parent.listVisibleLayerAndPoi();
		   		
		   		var tmppoiid = id
				var tmppoiType = '0';
				var tmplnglat = tmppoiDatas.split(',');
				var tmppoiEncryptDatas = poiEncryptDatas;
				var tmpiconpath = path+'/images/poi/'+poiImgHid;
				var tmppoiName = poiName;
				var tmpaddress = address;
				var tmppoiDesc = poiDesc;
				var tmptelephone = poiPhoum;
				var tmpvisitDistance = range;
				var param = '?id='+tmppoiid+'&name='+tmppoiName+'&address='+tmpaddress+'&telephone='+tmptelephone+
				'&desc='+tmppoiDesc+'&visitDistance='+tmpvisitDistance+'&iconpath='+poiImgHid+'&layerid='+tmplayerid
				+'&poiDatas='+tmppoiDatas+'&poiEncryptDatas='+tmppoiEncryptDatas;
				var sContent='<iframe  src="'+path+'/map/poitip.jsp'+param+'" width="100%" height="190" marginwidth="0" framespacing="0" marginheight="0" scrolling="no" frameborder="0" border="0" ></iframe>';  //tip����
				var tmppoiOverLay = parent.modifyPoiMarker('visiPoi@'+tmppoiid,tmplnglat[0],tmplnglat[1],tmppoiName,sContent,tmpiconpath);
				
				
				var tmpvisiPoiObj = new parent.visiPoiObj('visiPoi@'+tmppoiid, tmpmapLevel, tmppoiType, tmppoiOverLay);
				parent.visiPoiObjArr.push(tmpvisiPoiObj);
				
		   		return;
		   }else{
		   		//store.reload();
		   		parent.parent.Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.parent.Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
			 return;
		 }
		});
		
}

function del(){
var id = Ext.getCmp('idfrm').getValue();

parent.parent.Ext.Msg.show({
		msg: '����ɾ�� ���Ե�...',
		progressText: 'ɾ��...',
		width:300,
		wait:true,
		//waitConfig: {interval:200},
		icon:'ext-mb-download'
	});
	
	
	
	Ext.Ajax.request({
		 //url :url,
		 url:path+'/poi/poi.do?method=deletePois',
		 method :'POST', 
		 params: {ids: id},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		   		parent.parent.Ext.Msg.alert('��ʾ', 'ɾ���ɹ�');
		   		//parent.listVisibleLayerAndPoi();
				var tmpArr = new Array();
				for(var i=0;i<parent.visiPoiObjArr.length;i++){
					if(parent.visiPoiObjArr[i].poiid != ('visiPoi@'+id)){
					   	tmpArr.push(parent.visiPoiObjArr[i]);
					}
				}
				parent.visiPoiObjArr = [];
				parent.visiPoiObjArr = tmpArr;
				parent.clearMapById('visiPoi@'+id);
				
		   		return;
		   }else{
		   		parent.parent.Ext.Msg.alert('��ʾ', "ɾ��ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.parent.Ext.Msg.alert('��ʾ', "ɾ��ʧ��!");
		 }
		});
}
    </script>
  </head>
  <body scroll="no">
  </body>
</html>
