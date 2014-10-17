//��������-��������
var searchValue = '';
var proxymsg = new Ext.data.HttpProxy({
    url: path+'/stat/alarmStat.do?method=listAlarmsByToday'
});
// ��������-��������
var readermsg = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},// id
    {name: 'termName'},// ��������
    {name: 'simcard'},// �����绰
    {name: 'vType'},// �����ͺ�
    {name: 'vNumber'},// ���ƺ�
    {name: 'alarmId'},// ������¼��id
    {name: 'speed'},// �ٶ�
    {name: 'direction'},// ����
    {name: 'time'},// ʱ��
    {name: 'type'},// ����
    
    {name: 'maxSpeed'},// ���ٷ�ֵ
    {name: 'areaPoints'},// ����ֵ
    {name: 'startTime'},// ��ʼʱ��
    {name: 'endTime'},// ����ʱ��
    {name: 'areaType'},// ��������
    
    {name: 'x'},// x����
    {name: 'y'},// y����
    {name: 'jmx'},// x����
    {name: 'jmy'},// y����
    {name: 'pd'},
    {name: 'process'}
]);

// ������Ϣ
var storemsg = new Ext.data.Store({
    id: 'userstore',
    restful: true,     // <-- This Store is RESTful
    proxy: proxymsg,
    reader: readermsg
});

    // manually load local data
    // storemsg.loadData(myData);
    
	// function createButton(){
		// var str = '<button class="btn3_mouseout"
		// onmouseover="this.className=\'btn3_mouseover\'"
		// onmouseout="this.className=\'btn3_mouseout\'"
		// onmousedown="this.className=\'btn3_mousedown\'"
		// onmouseup="this.className=\'btn3_mouseup\'"
		// title=\"CSS��ʽ��ť\">CSS��ʽ��ť</button>';
    // return "<input type='button' class=btn3_mouseup value='�������'
	// onclick='removealarm' >&nbsp<input type='button' class=btn3_mouseup
	// value='��λ' onclick='location' >" ;
    // }
    // create the Grid
    // ��������
    function alarmTypeRender(val){
    	if(val == '1'){
    		return alarmCenter.speedAlarm;
    	}else if(val == '2'){
    		return alarmCenter.areaAlarm;
    	}else if(val == '3'){
    		return alarmCenter.holdAlarm;
    	}else if(val == '4'){
    		return alarmCenter.emergencyAlarm;
    	}else if(val == '6'){
    		return alarmCenter.deviationAlarm;
    	}else{
    		return '';
    		// return 'δ֪����';
    	}
    }
    // ��������
    function areaTypeRende(val){
    	if(val == '1'){
    		return alarmCenter.in_area;
    	}else if(val == '0'){
    		return alarmCenter.out_area;
    	}else{
    		return '';
    		// return 'δ֪';
    	}
    }
    
    // ��������-������Ϣ
    var gridmsg = new Ext.grid.GridPanel({
    	title: alarmCenter.alarmMessage,
        autoScroll: true,
        store: storemsg,
        columns: [
            {header: alarmCenter.speedAlarm, width: 75, sortable: true, dataIndex: 'simcard',hidden: true},
            {header: alarmCenter.termName, width: 75, sortable: true, dataIndex: 'termName'},
            {header: alarmCenter.vType, width: 75, sortable: true,dataIndex : 'vType',hidden: true},
            {header: alarmCenter.vNumber, width: 85, sortable: true, dataIndex: 'vNumber'},
            {header: alarmCenter.speed, width: 75, sortable: true,  dataIndex: 'speed'},
            {header: alarmCenter.type, width: 75, sortable: true,  dataIndex: 'type', renderer:alarmTypeRender},
            {header: alarmCenter.direction, width: 75, sortable: true, dataIndex: 'direction',hidden: true},
            
            //{header: alarmCenter.maxSpeed, width: 75, sortable: true,  dataIndex: 'maxSpeed'},
            {header: alarmCenter.areaPoints, width: 75, sortable: true,  dataIndex: 'areaPoints',hidden: true},
            // {header: '��ʼʱ��', width: 75, sortable: true, dataIndex:
			// 'startTime'},
            // {header: '����ʱ��', width: 75, sortable: true, dataIndex:
			// 'endTime'},
            {header: alarmCenter.areaType, width: 75, sortable: true,  dataIndex: 'areaType', renderer:areaTypeRende,hidden: true},
    		
            {header: alarmCenter.time, width: 130, sortable: true,  dataIndex: 'time'},
            /*
			 * {header: 'λ��', width: 350, sortable: true, dataIndex: 'pd',
			 * renderer: function tips(val) { return '<span
			 * style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>'; } },
			 */
            {header: alarmCenter.process, width: 100, sortable: true,  dataIndex: 'process' , renderer: function (value, meta, record) {   
			    // �����ﶨ����2������,�ֱ��費ͬ��css class�Ա�����
			    var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='removealarm'>�������</a> | <a href='javascript:void({1});' onclick='javscript:return false;' class='location'>��λ</a>";
			    var resultStr = String.format(formatStr, record.get('id'), record.get('id'));
			    return "<div class='controlBtn'>" + resultStr + "</div>";
			  }.createDelegate(this)
			},
			{header: 'alarmId', width: 150, sortable: true,  dataIndex: 'alarmId' , hidden : true},
            {header: 'id', width: 150, sortable: true,  dataIndex: 'id' , hidden : true},
            {header: 'x', width: 150, sortable: true,  dataIndex: 'x' , hidden : true},
            {header: 'y', width: 150, sortable: true,  dataIndex: 'y' , hidden : true},
            {header: 'jmx', width: 150, sortable: true,  dataIndex: 'jmx' , hidden : true},
            {header: 'jmy', width: 150, sortable: true,  dataIndex: 'jmy' , hidden : true}
            
        ],
        tbar: [
           	new Ext.Action({
   		        text: '������б���',
   		        handler: function(){
   		        	Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ���������?', function(btn){
   		        		if(btn == 'yes'){
   		        			Ext.Msg.show({
   		        				msg: '���ڽ������ ���Ե�...',
   		        				progressText: '���...',
   		        			    width:300,
   		        				wait:true,
   		        				icon:'ext-mb-download'
   		        			});
   		        			Ext.Ajax.request({
   		        				url : path+'/stat/alarmStat.do?method=removeAllAlarm',
   		        				method :'POST',
   		        				success : function(request) {
   		        				   var res = Ext.decode(request.responseText);
   		        				   if(res.result==1){
   		        					   storemsg.reload();
   		        				   	   Ext.Msg.alert('��ʾ', '��������ɹ�');
   		        				   	   return;
   		        				   }else{
   		        				   	   Ext.Msg.alert('��ʾ', "�������ʧ��!");
   		        				   	   return;
   		        				   }
   		        				 },
   		        				 failure : function(request) {
   		        					 Ext.Msg.alert('��ʾ', "�������ʧ��!");
   		        				 }
   		        			});
   		        		}
   		        		
   		        	});
   		        	
   		        }
           	})
           ],
        bbar: new Ext.PagingToolbar({
            pageSize: 4,
            store: storemsg,
            displayInfo: true,
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
        }),
        stripeRows: true
        /*
		 * bbar: new Ext.PagingToolbar({ pageSize: 5, store: storemsg,
		 * displayInfo: true, displayMsg: '��{0}����{1}������ ��{2}��', emptyMsg: "û������" })
		 */
    });
// ��������������������ж���id
var removealarmId;
// ��������,��������
// var alarmArr = new Array();
// ��������-�������
function removealarm(id){
	// alert(id);
	removealarmId = id;
	Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ���������?', removealarmconfirm);
}
// ��������-������� �ص�����
function removealarmconfirm(btn){
	if(btn == 'yes'){
		var tmpstore = storemsg.getById(removealarmId);
		var tmpid = tmpstore.get('id');
			Ext.Msg.show({
			           msg: '���ڽ������ ���Ե�...',
			           progressText: '���...',
			           width:300,
			           wait:true,
			           // waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
		removealarmajax(tmpid);
		/*
		 * storemsg.remove(tmpstore); if(storemsg.getCount()>0){
		 * setalarmimg(path+'/images/3.gif'); }else{
		 * setalarmimg(path+'/images/3.jpg'); }
		 */
	}
}

// ���̨�·��������ָ��
function removealarmajax(tmpid){
	
	Ext.Ajax.request({
		 url : path+'/stat/alarmStat.do?method=removeAlarm&id='+tmpid,
		 method :'POST',
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		 	if(res.result==1){
		 		storemsg.reload();
		   		Ext.Msg.alert('��ʾ', '��������ɹ�');
		   		return;
		   }else{
		   		Ext.Msg.alert('��ʾ', "�������ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('��ʾ', "�������ʧ��!");
		 }
		});
}
// ���ñ�������ͼƬ
function setalarmimg(img){
	// Ext.get('alarmimg').src = img;
	if(document.getElementById("alarmimg")){
		document.getElementById("alarmimg").src = img + "?timestamp=" + Date(); 
	}
}

// ��������-��λ����
function locationalarm(id,msgorhis){
	// alert(id);
	
	var tmpstore;
	if(msgorhis == 'msg'){
		tmpstore = storemsg.getById(id);
	}else{
		tmpstore = storehis.getById(id);
	}
	// var tmpx = tmpstore.get('x');
	// var tmpy = tmpstore.get('y');
	
	var tmpx = tmpstore.get('jmx');
	var tmpy = tmpstore.get('jmy');
	
	var tmpalarmId = tmpstore.get('alarmId');
	var tmptype = tmpstore.get('type');
	
	var tmptermName = tmpstore.get('termName');// ��������
	var tmpsimcard = tmpstore.get('simcard');// �����绰
	var tmpvType = tmpstore.get('vType');// �����ͺ�
	var tmpvNumber = tmpstore.get('vNumber');// ���ƺ�
	var tmpspeed = tmpstore.get('speed');// �ٶ�
	var tmptime = tmpstore.get('time');// ʱ��
	
	var tmpmaxSpeed = tmpstore.get('maxSpeed');// ���ٷ�ֵ
	var tmpareaPoints = tmpstore.get('areaPoints');// ����ֵ
	var tmpstartTime = tmpstore.get('startTime');// ��ʼʱ��
	var tmpendTime = tmpstore.get('endTime');// ����ʱ��
	var tmpareaType = tmpstore.get('areaType');// ��������

	map = document.getElementById('mapifr').contentWindow;

		   var sContent = '';
		   sContent += '���ƣ�'+tmptermName+'<br>';
		   // sContent += '�绰��'+tmpsimcard+'<br>';
		   // sContent += '�����ͺţ�'+tmpvType+'<br>';
		   sContent += '���ƺţ�'+tmpvNumber+'<br>';
		   sContent += '�ٶȣ�'+tmpspeed+'<br>';
		   sContent += 'ʱ�䣺'+tmptime+'<br>';
		   map.mapObj.removeOverlayById('point_alarm_center');
		   map.mapObj.removeOverlayById('area_alarm_center');
		   
		   // ��ͼ�걨�������������
		   var overlayArr = new Array();
		   if(tmptype == '1'){
		   	 // ���ٱ���,������ʾ�ٶȷ�ֵ
		   	 sContent += '�������ͣ����ٱ���<br>';
		   	 sContent += '�ٶȷ�ֵ��'+tmpmaxSpeed+'<br>';
		   }else if(tmptype == '2'){
		   	
		   		if(tmpareaType == '0'){
					sContent += '�������ͣ������򱨾�<br>';
				}else if(tmpareaType == '1'){
					sContent += '�������ͣ������򱨾�<br>';
				}
				
		   	// ���򱨾�,������
		   	 var tmpareaxy = tmpareaPoints.split('#');
		   	 if(tmpareaxy.length>0){
		   	 	var tmparea = map.getPolygon(tmpareaxy, '', '', 'area_alarm_center', false);
		   	 	map.mapObj.addOverlay(tmparea,false);
		   	 	// overlayArr.push(tmparea);
		   	 }
		   	 
		   }else if(tmptype == '3'){
		   	sContent += '�������ͣ���������<br>';
		   }else if(tmptype == '6'){
		   	sContent += '�������ͣ�ƫ������<br>';
		   }
		   /*
			 * else{ sContent += '�������ͣ�δ֪����<br>'; }
			 */
		   
		 	if(tmpx.length>0&&tmpy.length>0){
				var imageUrl = path+'/images/alarm-icon.gif';
				var tmppoint = map.addTrackMarker('point_alarm_center',tmpx,tmpy,'',sContent,imageUrl,true);
				map.mapObj.addOverlay(tmppoint,true);
				overlayArr.push(tmppoint);
				
			}else{
				Ext.Msg.alert('��ʾ', 'û��λ����Ϣ!');
			}
			// ����е������,�ڵ�ͼ�ϻ��� ����
			// if(overlayArr.length > 0){
				// map.mapObj.addOverlays(overlayArr,true);
			// }

}

// ��ʱ���󱨾���Ϣ
function alarmajax(){
	// alert('a');
	storemsg.load({params:{start:0, limit:4}});
	
}
// �����б����¼�
gridmsg.on('cellclick', function (grid, rowIndex, columnIndex, e) {   
  var btn = e.getTarget('.controlBtn');   
  if (btn) {   
    var t = e.getTarget();   
    var record = grid.getStore().getAt(rowIndex);   
    var control = t.className;   
    switch (control) {   
    case 'removealarm':
      this.removealarm(record.id);
      break;
    case 'location':   
      this.locationalarm(record.id,'msg');
      break;
    }   
  }   
},   
this);  

//
var proxyhis = new Ext.data.HttpProxy({
    url: path+'/stat/alarmStat.do?method=listAllAlarmByToday'
});
// ��ʷ����
var readerhis = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},// id
    {name: 'termName'},// ��������
    {name: 'simcard'},// �����绰
    {name: 'vType'},// �����ͺ�
    {name: 'vNumber'},// ���ƺ�
    {name: 'alarmId'},// ������¼��id
    {name: 'speed'},// �ٶ�
    {name: 'direction'},// ����
    {name: 'time'},// ʱ��
    {name: 'type'},// ����
    {name: 'maxSpeed'},// ���ٷ�ֵ
    {name: 'areaPoints'},// ����ֵ
    {name: 'startTime'},// ��ʼʱ��
    {name: 'endTime'},// ����ʱ��
    {name: 'areaType'},// ��������
    {name: 'x'},// x����
    {name: 'y'},// y����
    {name: 'jmx'},// x����
    {name: 'jmy'},// y����
    {name: 'pd'},
    {name: 'process'}
]);

var storehis = new Ext.data.Store({
	restful: true,     // <-- This Store is RESTful
	proxy: proxyhis,
	reader: readerhis
});

var gridhis = new Ext.grid.GridPanel({
    	title: '��ʷ����',
        autoScroll: true,
        store: storehis,
        columns: [
            {header: '�����绰', width: 75, sortable: true, dataIndex: 'simcard',hidden: true},
            {header: '����', width: 75, sortable: true, dataIndex: 'termName'},
            {header: '�����ͺ�', width: 75, sortable: true,dataIndex : 'vType',hidden: true},
            {header: '���ƺ�', width: 85, sortable: true, dataIndex: 'vNumber'},
            {header: '�ٶ�', width: 75, sortable: true,  dataIndex: 'speed'},
            {header: '��������', width: 75, sortable: true,  dataIndex: 'type', renderer:alarmTypeRender},
            {header: '����', width: 75, sortable: true, dataIndex: 'direction',hidden: true},
            
            //{header: '���ٷ�ֵ', width: 75, sortable: true,  dataIndex: 'maxSpeed'},
            {header: '�ٶ�', width: 75, sortable: true,  dataIndex: 'areaPoints',hidden: true},
            // {header: '��ʼʱ��', width: 75, sortable: true, dataIndex:
			// 'startTime'},
            // {header: '����ʱ��', width: 75, sortable: true, dataIndex:
			// 'endTime'},
            {header: '��������', width: 75, sortable: true,  dataIndex: 'areaType', renderer:areaTypeRende,hidden: true},
    		
            {header: '����ʱ��', width: 130, sortable: true,  dataIndex: 'time'},
            /*
			 * {header: 'λ��', width: 350, sortable: true, dataIndex: 'pd',
			 * renderer: function tips(val) { return '<span
			 * style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>'; } },
			 */
            {header: '����', width: 100, sortable: true,  dataIndex: 'process' , renderer: function (value, meta, record) {   
			    // �����ﶨ����2������,�ֱ��費ͬ��css class�Ա�����
			    var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='location'>��λ</a>";
			    var resultStr = String.format(formatStr, record.get('id'), record.get('id'));
			    return "<div class='controlBtn'>" + resultStr + "</div>";
			  }.createDelegate(this)
			},
			{header: 'alarmId', width: 150, sortable: true,  dataIndex: 'alarmId' , hidden : true},
            {header: 'id', width: 150, sortable: true,  dataIndex: 'id' , hidden : true},
            {header: 'x', width: 150, sortable: true,  dataIndex: 'x' , hidden : true},
            {header: 'y', width: 150, sortable: true,  dataIndex: 'y' , hidden : true},
            {header: 'jmx', width: 150, sortable: true,  dataIndex: 'jmx' , hidden : true},
            {header: 'jmy', width: 150, sortable: true,  dataIndex: 'jmy' , hidden : true}
            
        ],
        tbar: [
        	new Ext.Action({
		        text: 'ˢ��',
		        handler: function(){
		        	storehis.load({params:{start:0, limit:4}});
		        }
        	})
        ],
        bbar: new Ext.PagingToolbar({
            pageSize: 4,
            store: storehis,
            displayInfo: true,
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
        }),
        stripeRows: true
});

gridhis.on('cellclick', function (grid, rowIndex, columnIndex, e) {   
  var btn = e.getTarget('.controlBtn');   
  if (btn) {   
    var t = e.getTarget();   
    var record = grid.getStore().getAt(rowIndex);   
    var control = t.className;   
    switch (control) {
    case 'location':   
      this.locationalarm(record.id,'his');
      break;
    }   
  }   
},   
this);  

/*
 * this.preview = new Ext.TabPanel({ border: false, // already wrapped so don't
 * add another border activeTab: 0, // second tab initially active tabPosition:
 * 'bottom', items: [grid1,grid2] });
 */

// add magiejue 2010-11-23
var proxyVehicle = new Ext.data.HttpProxy({
	 url: path+'/locate/locate.do?method=listTrack'
});
var readerVehicle = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},// id
    {name: 'x'},// ����
    {name: 'y'},// γ��
    {name: 'distance'},// ���
    {name: 'poiName'},// ��ע��
    {name: 'vNumber'},// ���ƺ�
    {name: 'termName'},// �ն���
    {name: 'simCard'},// sim����
    {name: 'locType'},// ��λ����
    {name: 'direction'},// ����
    {name: 'speed'},// �ٶ�
    {name: 'pd'},// λ������
    {name: 'time'},// ʱ��
    {name: 'accStatus'},// acc״̬
    {name: 'temperature'},// �¶�
    {name: 'jmx'},// ���ܾ���
    {name: 'jmy'},// ����γ��
    {name: 'deviceId'},// �ն˺�
    {name: 'deflectionx'},// ƫת����
    {name: 'deflectiony'}// ƫתγ��
]);
var storeVehicle = new Ext.data.Store({
		autoLoad: {params:{start: 0, limit: 20}},
	    restful: true,     // <-- This Store is RESTful
	    proxy: proxyVehicle,
	    reader: readerVehicle
	});
// ������λ��Ϣ
var gridVehicle = new Ext.grid.GridPanel({
	title: '������λ��Ϣ',
	autoScroll: true,
  // loadMask: {msg:'������...'},
	store: storeVehicle,
  columns: [
      {header: 'id', width: 75, sortable: true, dataIndex: 'id',hidden: true},
      {header: '���ƺ�', width: 110, sortable: true, dataIndex: 'vNumber'},
      {header: '�ֻ�����', width: 85, sortable: true, dataIndex: 'simCard'},
      {header: '����', width: 85, sortable: true,dataIndex : 'x'},
      {header: 'γ��', width: 85, sortable: true, dataIndex: 'y'},
      {header: '�ٶ�', width: 50, sortable: true,  dataIndex: 'speed'},
      {header: '����', width: 55, sortable: true, dataIndex: 'direction'},
      {header: '����', width: 55, sortable: true,  dataIndex: 'distance'},
      {header: '�¶�', width: 45, sortable: true,  dataIndex: 'temperature'},
      {header: '״̬', width: 60, sortable: true,  dataIndex: 'accStatus',
    	  renderer:function(r,d,v){
    	  	return r=="0"?"ֹͣ":"��ʻ��";
      }},
      {header: '�ݷõ�', width: 90, sortable: true, dataIndex: 'poiName',
    	  renderer:function(r,d,v){
    	  	return r=="null"?"":r;
      }},
      {header: 'λ������', width: 280, sortable: true,  dataIndex: 'pd'},
      {header: 'ʱ��', width: 120, sortable: true,  dataIndex: 'time',
	        renderer: function tips(val) {
	        	return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		}
      // {header: '����', width: 40, sortable: true, dataIndex: 'locType'},
      // {header: '���ܾ���', width: 75, sortable: true, dataIndex: 'jmx'},
      // {header: '����γ��', width: 75, sortable: true, dataIndex: 'jmy'},
      // {header: '�ն˺�', width: 75, sortable: true, dataIndex: 'deviceId'},
      // {header: 'ƫת����', width: 75, sortable: true, dataIndex:
		// 'deflectionx'},
      // {header: 'ƫתγ��', width: 75, sortable: true, dataIndex:
		// 'deflectiony'},
  ],
  	
	  bbar: new Ext.PagingToolbar({
	      pageSize: 20,
	      store: storeVehicle,
	      displayInfo: true,
	      displayMsg: '��{0}����{1}������ ��{2}��',
	      emptyMsg: "û������"
	  })
});
// storeVehicle.load({params:{start:0, limit:20}});

gridVehicle.on('cellclick',function(grid, rowIndex, columnIndex, e){
	 var record = grid.getStore().getAt(rowIndex); 
	 locationVenicle(record.id);
});

function locationVenicle(id){	
	var tmpstore=storeVehicle.getById(id);
	
	// var tmpx = tmpstore.get('x');
	// var tmpy = tmpstore.get('y');
	var tmpx = tmpstore.get('deflectionx');
	var tmpy = tmpstore.get('deflectiony');
	
	var tmpStatus = tmpstore.get('accStatus');

	
	var tmpTermName = tmpstore.get('vNumber');// ���ƺ���
	var tmpsimcard = tmpstore.get('simCard');// �ֻ�����
	var tmpspeed = tmpstore.get('speed');// �ٶ�
	var tmptime = tmpstore.get('time');// ʱ��
	var tmpTem = tmpstore.get('temperature');// �¶�
	var tmpLoc = tmpstore.get('pd');// λ��

	map = document.getElementById('mapifr').contentWindow;

		   var sContent = '';
		   sContent += '���ƺ��룺'+tmpTermName+'<br>';
		   sContent += '�ֻ����룺'+tmpsimcard+'<br>';
		   sContent += '�ٶȣ�'+tmpspeed+'<br>';
		   sContent += 'ʱ�䣺'+tmptime+'<br>';
		   sContent += '�¶ȣ�'+tmpTem+'<br>';
		   sContent += 'λ�ã�'+tmpLoc+'<br>';
		   
		   map.mapObj.removeOverlayById('point_alarm_center');
		   map.mapObj.removeOverlayById('area_alarm_center');
		   
		 	if(tmpx.length>0&&tmpy.length>0){
				var imageUrl =path+tmpStatus=='1'?'/images/car_move_.gif':'/images/car_stop.gif';
				// var tmppoint =
				// map.addTrackMarker('point_alarm_center',tmpx,tmpy,'',sContent,imageUrl);
				// var tmppoint =
				// map.addLabelMarker('point_alarm_center',tmpx,tmpy,'',sContent,imageUrl);
				var tmppoint = map.addLabelPoiMarker('point_alarm_center',tmpx,tmpy,'',sContent,imageUrl);
				map.mapObj.addOverlay(tmppoint,true);
			}else{
				Ext.Msg.alert('��ʾ', 'û��λ����Ϣ!');
			}
}

var msgpanel = new Ext.TabPanel({
	border: false, // already wrapped so don't add another border
	activeTab: 0, // second tab initially active
	tabPosition: 'bottom',
	height: 200,
	items: [gridmsg,gridhis,gridVehicle],
	// title:'��Ϣ�б�',
	region:'south'
});


/*
 * var msgpanel = new Ext.Panel({ id:'bottom-preview', layout:'fit',
 * items:this.preview, height: 200, //hidden:true, split: true, border:false,
 * title:'��Ϣ�б�', region:'south' });
 */
// msgpanel.setVisible(false);
