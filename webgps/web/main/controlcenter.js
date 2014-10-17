//���Ͷϵ�
function oilBreakelBreak() {
	var layerwin = null;
    var layerform = new Ext.form.FormPanel({
    	labelWidth: '10',
    	width: '100',
    	buttonAlign : 'center',
    	layout: 'fit',
    	bodyStyle: 'padding:0 10px 10px;',
    	items:[{
    		xtype: 'fieldset',
    		title: '�������û���������',
    		id: 'layerfieldset',
    		items:[{
    			xtype: 'textfield',
    			id : 'turnOffOilPower_userAccount',
    			fieldLabel : '�û���'
    		},{
    			xtype: 'textfield',
    			id : 'turnOffOilPower_password',
    			fieldLabel : '����',
    			inputType: 'password'
    		},{
    			xtype: 'textfield',
    			id : 'turnOffOilPower_contorlpassword',
    			fieldLabel : '���Ͷϵ�����',
    			inputType: 'password'
    		}]
    	}],
    	buttons: [{
    		text: 'ȷ��',
    		handler: function(){
    			var treeArr = new Array();
				getTreeId(root, treeArr);
				/*if (treeArr.length <= 0) {
					Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
					return;
				}*/
				var gpsDeviceid = '';
				var lbsDeviceid = '';
				for (var i = 0; i < treeArr.length; i++) {
					var idArr = treeArr[i].id.split('@#');
					/*if (idArr.length > 2 && idArr[1] == '0') {
						lbsDeviceid += idArr[0] + ',';
					} else */
					if (idArr.length > 2 && idArr[1] == '1') {
						gpsDeviceid += idArr[0] + ',';
					}
				}
				if (gpsDeviceid.length > 0) {
					gpsDeviceid = gpsDeviceid.substring(0, gpsDeviceid.length - 1);
				}
				/*if (lbsDeviceid.length > 0) {
					lbsDeviceid = lbsDeviceid.substring(0, lbsDeviceid.length - 1);
				}*/
				if(gpsDeviceid.length<=0){
					Ext.Msg.alert('��ʾ', '��ѡ��GPS�ն�!');
					return;
				}
				var tmpturnOffOilPower_userAccount = Ext.getCmp('turnOffOilPower_userAccount').getValue();
			    if(tmpturnOffOilPower_userAccount==''){
			    	Ext.MessageBox.alert('��ʾ', '�������û���!');
			        return ;
			    }
				var tmpturnOffOilPower_password = Ext.getCmp('turnOffOilPower_password').getValue();
			    if(tmpturnOffOilPower_password==''){
			    	Ext.MessageBox.alert('��ʾ', '����������!');
			        return ;
			    }
			    var turnOffOilPower_contorlpassword = Ext.getCmp('turnOffOilPower_contorlpassword').getValue();
			    if(turnOffOilPower_contorlpassword==''){
			    	Ext.MessageBox.alert('��ʾ', '��������Ͷϵ�����!');
			        return ;
			    }
			    Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�·����Ͷϵ�ָ����?', oilBreakelBreakBack);
			    
				
    		}
    	}]
   });
   
	if(!layerwin){
   		layerwin = new Ext.Window({
	    	//title: '���μ��鴰��',
	        closable:true,
	        width:300,
	        height:200,
	        maximizable: true,
	        //border:false,
	        //plain:true,
	        layout: 'fit',
	        items:[layerform],
	        listeners:{
	        	'close':function (p){
	            	layerwin = null;
	            }
	        }
	   });
	   layerwin.show(this);
    }

	
}

function oilBreakelBreakBack(btn){
	if(btn=='yes'){
		Ext.Msg.show({
			msg: '�����·�ָ�� ���Ե�...',
			progressText: '�·���...',
			width:300,
			wait:true,
			//waitConfig: {interval:200},
			icon:'ext-mb-download'
			});
		oilBreakelBreakAjax();
	}
}

function oilBreakelBreakAjax(){
	var treeArr = new Array();
	getTreeId(root, treeArr);
	var gpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2 && idArr[1] == '1') {
			gpsDeviceid += idArr[0] + ',';
		}
	}
	if (gpsDeviceid.length > 0) {
		gpsDeviceid = gpsDeviceid.substring(0, gpsDeviceid.length - 1);
	}
	var tmpturnOffOilPower_userAccount = Ext.getCmp('turnOffOilPower_userAccount').getValue();
	var tmpturnOffOilPower_password = Ext.getCmp('turnOffOilPower_password').getValue();
	var turnOffOilPower_contorlpassword = Ext.getCmp('turnOffOilPower_contorlpassword').getValue();
	
	Ext.Ajax.request({
		 url:path+'/struction/struction.do?method=turnOffOilPowerStruction',
		 method :'POST', 
		 params: { deviceId: gpsDeviceid, userAccount : encodeURI(tmpturnOffOilPower_userAccount) ,password: tmpturnOffOilPower_password ,controlPassword : turnOffOilPower_contorlpassword},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		   		Ext.Msg.alert('��ʾ', '�·��ɹ�');
		   		return;
		   }else if(res.result==2){
		   		//store.reload();
		   		Ext.Msg.alert('��ʾ', "�·�ʧ��,�ʺŲ�����!");
		   		return;
		   }else if(res.result==3){
		   		Ext.Msg.alert('��ʾ', "�·�ʧ��,���벻ƥ��!");
		   		return;
		   }else if(res.result==4){
		   		Ext.Msg.alert('��ʾ', "�·�ʧ��,���Ͷϵ����벻ƥ��!");
		   		return;
		   }else{
		   		Ext.Msg.alert('��ʾ', "�·�ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('��ʾ', "�·�ʧ��!");
		 }
	});
}
//�ָ��͵�
function turnOnOilPower() {
	var treeArr = new Array();
	getTreeId(root, treeArr);
	var gpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2 && idArr[1] == '1') {
			gpsDeviceid += idArr[0] + ',';
		}
	}
	if (gpsDeviceid.length > 0) {
		gpsDeviceid = gpsDeviceid.substring(0, gpsDeviceid.length - 1);
	}
	if(gpsDeviceid.length<=0){
		Ext.Msg.alert('��ʾ', '��ѡ��GPS�ն�!');
		return;
	}
	Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�·��ָ��͵�ָ����?', turnOnOilPowerBack);
	
}

function turnOnOilPowerBack(btn){
	if(btn=='yes'){
		Ext.Msg.show({
			msg: '�����·�ָ�� ���Ե�...',
			progressText: '�·���...',
			width:300,
			wait:true,
			//waitConfig: {interval:200},
			icon:'ext-mb-download'
			});
		turnOnOilPowerAjax();
	}
}

function turnOnOilPowerAjax(){
	var treeArr = new Array();
	getTreeId(root, treeArr);
	var gpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2 && idArr[1] == '1') {
			gpsDeviceid += idArr[0] + ',';
		}
	}
	if (gpsDeviceid.length > 0) {
		gpsDeviceid = gpsDeviceid.substring(0, gpsDeviceid.length - 1);
	}
	
	Ext.Ajax.request({
		 url:path+'/struction/struction.do?method=turnOnOilPowerStruction',
		 method :'POST', 
		 params: { deviceId: gpsDeviceid },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		   		Ext.Msg.alert('��ʾ', '�·��ɹ�');
		   		return;
		   }else{
		   		Ext.Msg.alert('��ʾ', "�·�ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('��ʾ', "�·�ʧ��!");
		 }
	});
}

//����پ�
function stopHoldAlarm() {
	var treeArr = new Array();
	getTreeId(root, treeArr);
	var gpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2 && idArr[1] == '1') {
			gpsDeviceid += idArr[0] + ',';
		}
	}
	if (gpsDeviceid.length > 0) {
		gpsDeviceid = gpsDeviceid.substring(0, gpsDeviceid.length - 1);
	}
	if(gpsDeviceid.length<=0){
		Ext.Msg.alert('��ʾ', '��ѡ��GPS�ն�!');
		return;
	}
	Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�·�����پ�ָ����?', stopHoldAlarmBack);
	
}

function stopHoldAlarmBack(btn){
	if(btn=='yes'){
		Ext.Msg.show({
			msg: '�����·�ָ�� ���Ե�...',
			progressText: '�·���...',
			width:300,
			wait:true,
			//waitConfig: {interval:200},
			icon:'ext-mb-download'
			});
		stopHoldAlarmAjax();
	}
}

function stopHoldAlarmAjax(){
	var treeArr = new Array();
	getTreeId(root, treeArr);
	var gpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2 && idArr[1] == '1') {
			gpsDeviceid += idArr[0] + ',';
		}
	}
	if (gpsDeviceid.length > 0) {
		gpsDeviceid = gpsDeviceid.substring(0, gpsDeviceid.length - 1);
	}
	
	Ext.Ajax.request({
		 url:path+'/struction/struction.do?method=stopHoldAlarmStruction',
		 method :'POST', 
		 params: { deviceId: gpsDeviceid },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		   		Ext.Msg.alert('��ʾ', '�·��ɹ�');
		   		return;
		   }else{
		   		Ext.Msg.alert('��ʾ', "�·�ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('��ʾ', "�·�ʧ��!");
		 }
	});
}

//�������
function stopAreaAlarm() {
	var treeArr = new Array();
	getTreeId(root, treeArr);
	if(treeArr.length <= 0){
		Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
		return;
	}else if(treeArr.length > 1){
		Ext.Msg.alert('��ʾ', '��ѡ��һ���ն�!');
		return;
	}
	
	var gpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2 ) {
			gpsDeviceid += idArr[0] + ',';
		}
	}
	
	if (gpsDeviceid.length > 0) {
		gpsDeviceid = gpsDeviceid.substring(0, gpsDeviceid.length - 1);
	}
	//alert(gpsDeviceid);
	//if(gpsDeviceid.length<=0){
	//	Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
	//	return;
	//}
	//controlcenterwin.close();
	queryAreaByDeviceIdFun(gpsDeviceid);
	//Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�·��������ָ����?', stopAreaAlarmBack);
}
var queryAreaByDeviceIdwin;
var delDeviceId;
var delAreaId;
var AreaAlarmstore;

function queryAreaByDeviceIdFun(gpsDeviceid){

var AreaAlarmproxy = new Ext.data.HttpProxy({
    url: path+'/area/area.do?method=queryAreaByDeviceId'
});
var AreaAlarmreader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
	{name: 'id'},
    {name: 'areaName'},
    {name: 'areaType'},
    {name: 'alarmType'},
    {name: 'areaPoints'}
]);
var sm = new Ext.grid.CheckboxSelectionModel({});
AreaAlarmstore = new Ext.data.Store({
	//autoLoad: {params:{start: 0, limit: 65535, deviceId: gpsDeviceid }},
    restful: true,
    proxy: AreaAlarmproxy,
    reader: AreaAlarmreader
});
var AreaAlarmgrid = new Ext.grid.GridPanel({
    	//anchor: '100% 100%',
    	//width:350,
		//height:500,
		sm: sm,
        store: AreaAlarmstore,
        columns: [
        	sm,
        	{id:'id',header: "id", width: 10, sortable: true, dataIndex: 'id',hidden:true},
        	{header: "areaPoints", width: 10, sortable: true, dataIndex: 'areaPoints',hidden:true},
        	new Ext.grid.RowNumberer({header:'���',width:35}),
            {header: '����Χ������', width: 200, sortable: true, dataIndex: 'areaName'},
            {header: '����Χ������', width: 75, sortable: true, dataIndex: 'alarmType',hidden:true},
            {header: 'areaType', width: 75, sortable: true, dataIndex: 'areaType',hidden:true}
        ],
        tbar: [
        	{
            text: 'ɾ��',
            iconCls: 'del_icons',
            handler: function(){
            	var tmpselArr = sm.getSelections();
            	if(tmpselArr.length<=0){
            		Ext.Msg.alert('��ʾ','��ѡ�����Χ��');
            		return;
            	}
            	delAreaId = '';
            	for(var i = 0;i < tmpselArr.length;i++){
            		delAreaId += tmpselArr[i].get('id')+',';
            	}
            	
            	delDeviceId = gpsDeviceid;
            	delAreaId = delAreaId.substring(0, delAreaId.length-1);
            	//alert(delDeviceId+':'+delAreaId);
            	
            	Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ������Χ����?', delAreaAlarmFun);
            }
            }
        ],
        listeners: {
        	rowcontextmenu: function(grid, rowIndex, e){
        		e.preventDefault();
        		var gridMenu = new Ext.menu.Menu
	            ([
	                {text:"�鿴����Χ��",icon:"",handler:function(){
	                	var tmprecord = grid.getStore().getAt(rowIndex);
	                	var tmpareaPoints = tmprecord.get('areaPoints');
	                	var tmpareaName  = tmprecord.get('areaName');
	                	var tmpalarmType  = tmprecord.get('alarmType');
	                	
	                	var tmpxyArr = tmpareaPoints.split('#');
	                	if(tmpxyArr.length<=1){
	                		Ext.Msg.alert('��ʾ','�޵���Χ��');
	                		return;
	                	}
	                	
	                	if(tmpalarmType == '0'){
							tmpalarmType = '������';
						}else if(tmpalarmType == '1'){
							tmpalarmType = '������';
						}
						
	                	map.AreaAlarmaddPolygon(tmpxyArr, tmpareaName, '<br>��������:  '+tmpareaName+'</br>'+'<br>���򱨾�����:  '+tmpalarmType+'</br>', 'areaalarm_polygon', true ,false);
	                	
	                }}
	            ]);
	            gridMenu.showAt(e.getPoint());
        	}
        }
    });
	    	if(!queryAreaByDeviceIdwin){
	            queryAreaByDeviceIdwin = new Ext.Window({
	                applyTo:'queryAreaByDeviceId-win',
	                layout:'fit',
	                width:350,
	                height:250,
	                autoScroll : true,
	                //title:'��������',
	                closeAction:'hide',
	                plain: true
	            });
	        }
	        //AreaAlarmstore.removeAll();
	        queryAreaByDeviceIdwin.removeAll();
	        queryAreaByDeviceIdwin.add(AreaAlarmgrid);
	        queryAreaByDeviceIdwin.doLayout();
	        AreaAlarmstore.load({params:{start:0, limit:65535, deviceId: gpsDeviceid }});
	        queryAreaByDeviceIdwin.show();
}

function delAreaAlarmFun(btn){
	if(btn=='yes'){
		parent.Ext.Msg.show({
			msg: '����ɾ�� ���Ե�...',
			progressText: 'ɾ��...',
			width:300,
			wait:true,
			//waitConfig: {interval:200},
			icon:'ext-mb-download'
		});
		Ext.Ajax.request({
		 url:path+'/area/area.do?method=deleteRefTermAreasByDeviceIdAndAreaIds',
		 method :'POST',
		 params: { deviceId: delDeviceId , areaIds: delAreaId},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		//AreaAlarmstore.load({params:{start:0, limit:10}});
		 		AreaAlarmstore.load({params:{start:0, limit:65535, deviceId: delDeviceId }});
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
}

function stopAreaAlarmBack(btn){
	if(btn=='yes'){
		Ext.Msg.show({
			msg: '�����·�ָ�� ���Ե�...',
			progressText: '�·���...',
			width:300,
			wait:true,
			//waitConfig: {interval:200},
			icon:'ext-mb-download'
			});
		stopAreaAlarmAjax();
	}
}
function stopAreaAlarmAjax(){
	var treeArr = new Array();
	getTreeId(root, treeArr);
	var gpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2 ) {
			gpsDeviceid += idArr[0] + ',';
		}
	}
	if (gpsDeviceid.length > 0) {
		gpsDeviceid = gpsDeviceid.substring(0, gpsDeviceid.length - 1);
	}
	
	Ext.Ajax.request({
		 url:path+'/area/area.do?method=deleteRefTermAreas',
		 method :'POST', 
		 params: { deviceIds: gpsDeviceid },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		   		Ext.Msg.alert('��ʾ', '�·��ɹ�');
		   		return;
		   }else{
		   		Ext.Msg.alert('��ʾ', "�·�ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('��ʾ', "�·�ʧ��!");
		 }
	});
}

//�������
function stopSpeedAlarm() {
	var treeArr = new Array();
	getTreeId(root, treeArr);
	var gpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2 && idArr[1] == '1') {
			gpsDeviceid += idArr[0] + ',';
		}
	}
	if (gpsDeviceid.length > 0) {
		gpsDeviceid = gpsDeviceid.substring(0, gpsDeviceid.length - 1);
	}
	if(gpsDeviceid.length<=0){
		Ext.Msg.alert('��ʾ', '��ѡ��GPS�ն�!');
		return;
	}
	Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�·��������ָ����?', stopSpeedAlarmBack);
	
}

function stopSpeedAlarmBack(btn){
	if(btn=='yes'){
		Ext.Msg.show({
			msg: '�����·�ָ�� ���Ե�...',
			progressText: '�·���...',
			width:300,
			wait:true,
			//waitConfig: {interval:200},
			icon:'ext-mb-download'
			});
		stopSpeedAlarmAjax();
	}
}

function stopSpeedAlarmAjax(){
	var treeArr = new Array();
	getTreeId(root, treeArr);
	var gpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2 && idArr[1] == '1') {
			gpsDeviceid += idArr[0] + ',';
		}
	}
	if (gpsDeviceid.length > 0) {
		gpsDeviceid = gpsDeviceid.substring(0, gpsDeviceid.length - 1);
	}
	
	Ext.Ajax.request({
		 url:path+'/struction/struction.do?method=stopSpeedAlarmStruction',
		 method :'POST', 
		 params: { deviceId: gpsDeviceid },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		   		Ext.Msg.alert('��ʾ', '�·��ɹ�');
		   		return;
		   }else{
		   		Ext.Msg.alert('��ʾ', "�·�ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('��ʾ', "�·�ʧ��!");
		 }
	});
}
