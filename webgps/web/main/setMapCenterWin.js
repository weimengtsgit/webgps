function setmapcenterconfirm(btn){
	if(btn=='yes'){
	  	  Ext.Msg.show({
			  msg: '���ڱ��� ���Ե�...',
			  progressText: '����...',
			  width:300,
			  wait:true,
			  //waitConfig: {interval:200},
			  icon:'ext-mb-download'
		  });
	  	  setmapcenterajax();
	}
}

function setmapcenterajax(){
	var center = map.mapObj.getCenter();   
	centerX=center.lngX;   
	centerY=center.latY;   
	//alert("���ĵ�ľ���Ϊ��"+centerX+";"+"���ĵ��γ��Ϊ��"+centerY); 
	var zoom = map.mapObj.getZoomLevel();   
	//alert("��ǰ��ͼ�����ż���Ϊ��"+zoom); 
	
	Ext.Ajax.request({
		 url:path+'/system/ent.do?method=setVeiw',
		 method :'POST', 
		 params: { mapZoom:zoom, centerX : centerX ,centerY:centerY },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		 	if(res.result==1){
		   		Ext.Msg.alert('��ʾ', '����ɹ�');
		   		return;
		   }else{
		   		Ext.Msg.alert('��ʾ', "����ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('��ʾ', "����ʧ��!");
		 }
		});
}