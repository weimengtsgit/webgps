//报警中心-报警提醒
var searchValue = '';
var proxymsg = new Ext.data.HttpProxy({
    url: path+'/stat/alarmStat.do?method=listAlarmsByToday'
});
// 报警中心-报警提醒
var readermsg = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},// id
    {name: 'termName'},// 车主名称
    {name: 'simcard'},// 车主电话
    {name: 'vType'},// 车辆型号
    {name: 'vNumber'},// 车牌号
    {name: 'alarmId'},// 报警记录表id
    {name: 'speed'},// 速度
    {name: 'direction'},// 方向
    {name: 'time'},// 时间
    {name: 'type'},// 类型
    
    {name: 'maxSpeed'},// 超速阀值
    {name: 'areaPoints'},// 区域值
    {name: 'startTime'},// 开始时间
    {name: 'endTime'},// 结束时间
    {name: 'areaType'},// 区域类型
    
    {name: 'x'},// x坐标
    {name: 'y'},// y坐标
    {name: 'jmx'},// x坐标
    {name: 'jmy'},// y坐标
    {name: 'pd'},
    {name: 'process'}
]);

// 报警信息
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
		// title=\"CSS样式按钮\">CSS样式按钮</button>';
    // return "<input type='button' class=btn3_mouseup value='解除警报'
	// onclick='removealarm' >&nbsp<input type='button' class=btn3_mouseup
	// value='定位' onclick='location' >" ;
    // }
    // create the Grid
    // 报警类型
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
    		// return '未知报警';
    	}
    }
    // 区域类型
    function areaTypeRende(val){
    	if(val == '1'){
    		return alarmCenter.in_area;
    	}else if(val == '0'){
    		return alarmCenter.out_area;
    	}else{
    		return '';
    		// return '未知';
    	}
    }
    
    // 报警中心-报信信息
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
            // {header: '开始时间', width: 75, sortable: true, dataIndex:
			// 'startTime'},
            // {header: '结束时间', width: 75, sortable: true, dataIndex:
			// 'endTime'},
            {header: alarmCenter.areaType, width: 75, sortable: true,  dataIndex: 'areaType', renderer:areaTypeRende,hidden: true},
    		
            {header: alarmCenter.time, width: 130, sortable: true,  dataIndex: 'time'},
            /*
			 * {header: '位置', width: 350, sortable: true, dataIndex: 'pd',
			 * renderer: function tips(val) { return '<span
			 * style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>'; } },
			 */
            {header: alarmCenter.process, width: 100, sortable: true,  dataIndex: 'process' , renderer: function (value, meta, record) {   
			    // 在这里定义了2个操作,分别赋予不同的css class以便区分
			    var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='removealarm'>解除警报</a> | <a href='javascript:void({1});' onclick='javscript:return false;' class='location'>定位</a>";
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
   		        text: '清除所有报警',
   		        handler: function(){
   		        	Ext.MessageBox.confirm('提示', '您确定要解除警报吗?', function(btn){
   		        		if(btn == 'yes'){
   		        			Ext.Msg.show({
   		        				msg: '正在解除警报 请稍等...',
   		        				progressText: '解除...',
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
   		        				   	   Ext.Msg.alert('提示', '解除警报成功');
   		        				   	   return;
   		        				   }else{
   		        				   	   Ext.Msg.alert('提示', "解除警报失败!");
   		        				   	   return;
   		        				   }
   		        				 },
   		        				 failure : function(request) {
   		        					 Ext.Msg.alert('提示', "解除警报失败!");
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
            displayMsg: '第{0}到第{1}条数据 共{2}条',
            emptyMsg: "没有数据"
        }),
        stripeRows: true
        /*
		 * bbar: new Ext.PagingToolbar({ pageSize: 5, store: storemsg,
		 * displayInfo: true, displayMsg: '第{0}到第{1}条数据 共{2}条', emptyMsg: "没有数据" })
		 */
    });
// 用来保存解除警报点击的行对象id
var removealarmId;
// 报警中心,报警数组
// var alarmArr = new Array();
// 报警中心-解除警报
function removealarm(id){
	// alert(id);
	removealarmId = id;
	Ext.MessageBox.confirm('提示', '您确定要解除警报吗?', removealarmconfirm);
}
// 报警中心-解除警报 回调方法
function removealarmconfirm(btn){
	if(btn == 'yes'){
		var tmpstore = storemsg.getById(removealarmId);
		var tmpid = tmpstore.get('id');
			Ext.Msg.show({
			           msg: '正在解除警报 请稍等...',
			           progressText: '解除...',
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

// 向后台下发解除警报指令
function removealarmajax(tmpid){
	
	Ext.Ajax.request({
		 url : path+'/stat/alarmStat.do?method=removeAlarm&id='+tmpid,
		 method :'POST',
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		 	if(res.result==1){
		 		storemsg.reload();
		   		Ext.Msg.alert('提示', '解除警报成功');
		   		return;
		   }else{
		   		Ext.Msg.alert('提示', "解除警报失败!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('提示', "解除警报失败!");
		 }
		});
}
// 设置报警中心图片
function setalarmimg(img){
	// Ext.get('alarmimg').src = img;
	if(document.getElementById("alarmimg")){
		document.getElementById("alarmimg").src = img + "?timestamp=" + Date(); 
	}
}

// 报警中心-定位操作
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
	
	var tmptermName = tmpstore.get('termName');// 车主名称
	var tmpsimcard = tmpstore.get('simcard');// 车主电话
	var tmpvType = tmpstore.get('vType');// 车辆型号
	var tmpvNumber = tmpstore.get('vNumber');// 车牌号
	var tmpspeed = tmpstore.get('speed');// 速度
	var tmptime = tmpstore.get('time');// 时间
	
	var tmpmaxSpeed = tmpstore.get('maxSpeed');// 超速阀值
	var tmpareaPoints = tmpstore.get('areaPoints');// 区域值
	var tmpstartTime = tmpstore.get('startTime');// 开始时间
	var tmpendTime = tmpstore.get('endTime');// 结束时间
	var tmpareaType = tmpstore.get('areaType');// 区域类型

	map = document.getElementById('mapifr').contentWindow;

		   var sContent = '';
		   sContent += '名称：'+tmptermName+'<br>';
		   // sContent += '电话：'+tmpsimcard+'<br>';
		   // sContent += '车辆型号：'+tmpvType+'<br>';
		   sContent += '车牌号：'+tmpvNumber+'<br>';
		   sContent += '速度：'+tmpspeed+'<br>';
		   sContent += '时间：'+tmptime+'<br>';
		   map.mapObj.removeOverlayById('point_alarm_center');
		   map.mapObj.removeOverlayById('area_alarm_center');
		   
		   // 地图标报警点和区域数组
		   var overlayArr = new Array();
		   if(tmptype == '1'){
		   	 // 超速报警,画点显示速度阀值
		   	 sContent += '报警类型：超速报警<br>';
		   	 sContent += '速度阀值：'+tmpmaxSpeed+'<br>';
		   }else if(tmptype == '2'){
		   	
		   		if(tmpareaType == '0'){
					sContent += '报警类型：出区域报警<br>';
				}else if(tmpareaType == '1'){
					sContent += '报警类型：进区域报警<br>';
				}
				
		   	// 区域报警,画区域
		   	 var tmpareaxy = tmpareaPoints.split('#');
		   	 if(tmpareaxy.length>0){
		   	 	var tmparea = map.getPolygon(tmpareaxy, '', '', 'area_alarm_center', false);
		   	 	map.mapObj.addOverlay(tmparea,false);
		   	 	// overlayArr.push(tmparea);
		   	 }
		   	 
		   }else if(tmptype == '3'){
		   	sContent += '报警类型：主动报警<br>';
		   }else if(tmptype == '6'){
		   	sContent += '报警类型：偏航报警<br>';
		   }
		   /*
			 * else{ sContent += '报警类型：未知报警<br>'; }
			 */
		   
		 	if(tmpx.length>0&&tmpy.length>0){
				var imageUrl = path+'/images/alarm-icon.gif';
				var tmppoint = map.addTrackMarker('point_alarm_center',tmpx,tmpy,'',sContent,imageUrl,true);
				map.mapObj.addOverlay(tmppoint,true);
				overlayArr.push(tmppoint);
				
			}else{
				Ext.Msg.alert('提示', '没有位置信息!');
			}
			// 如果有点或区域,在地图上画点 区域
			// if(overlayArr.length > 0){
				// map.mapObj.addOverlays(overlayArr,true);
			// }

}

// 定时请求报警信息
function alarmajax(){
	// alert('a');
	storemsg.load({params:{start:0, limit:4}});
	
}
// 报警列表点击事件
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
// 历史报警
var readerhis = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},// id
    {name: 'termName'},// 车主名称
    {name: 'simcard'},// 车主电话
    {name: 'vType'},// 车辆型号
    {name: 'vNumber'},// 车牌号
    {name: 'alarmId'},// 报警记录表id
    {name: 'speed'},// 速度
    {name: 'direction'},// 方向
    {name: 'time'},// 时间
    {name: 'type'},// 类型
    {name: 'maxSpeed'},// 超速阀值
    {name: 'areaPoints'},// 区域值
    {name: 'startTime'},// 开始时间
    {name: 'endTime'},// 结束时间
    {name: 'areaType'},// 区域类型
    {name: 'x'},// x坐标
    {name: 'y'},// y坐标
    {name: 'jmx'},// x坐标
    {name: 'jmy'},// y坐标
    {name: 'pd'},
    {name: 'process'}
]);

var storehis = new Ext.data.Store({
	restful: true,     // <-- This Store is RESTful
	proxy: proxyhis,
	reader: readerhis
});

var gridhis = new Ext.grid.GridPanel({
    	title: '历史报警',
        autoScroll: true,
        store: storehis,
        columns: [
            {header: '车主电话', width: 75, sortable: true, dataIndex: 'simcard',hidden: true},
            {header: '名称', width: 75, sortable: true, dataIndex: 'termName'},
            {header: '车辆型号', width: 75, sortable: true,dataIndex : 'vType',hidden: true},
            {header: '车牌号', width: 85, sortable: true, dataIndex: 'vNumber'},
            {header: '速度', width: 75, sortable: true,  dataIndex: 'speed'},
            {header: '报警类型', width: 75, sortable: true,  dataIndex: 'type', renderer:alarmTypeRender},
            {header: '方向', width: 75, sortable: true, dataIndex: 'direction',hidden: true},
            
            //{header: '超速阀值', width: 75, sortable: true,  dataIndex: 'maxSpeed'},
            {header: '速度', width: 75, sortable: true,  dataIndex: 'areaPoints',hidden: true},
            // {header: '开始时间', width: 75, sortable: true, dataIndex:
			// 'startTime'},
            // {header: '结束时间', width: 75, sortable: true, dataIndex:
			// 'endTime'},
            {header: '区域类型', width: 75, sortable: true,  dataIndex: 'areaType', renderer:areaTypeRende,hidden: true},
    		
            {header: '报警时间', width: 130, sortable: true,  dataIndex: 'time'},
            /*
			 * {header: '位置', width: 350, sortable: true, dataIndex: 'pd',
			 * renderer: function tips(val) { return '<span
			 * style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>'; } },
			 */
            {header: '处理', width: 100, sortable: true,  dataIndex: 'process' , renderer: function (value, meta, record) {   
			    // 在这里定义了2个操作,分别赋予不同的css class以便区分
			    var formatStr = "<a href='javascript:void({0});' onclick='javscript:return false;' class='location'>定位</a>";
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
		        text: '刷新',
		        handler: function(){
		        	storehis.load({params:{start:0, limit:4}});
		        }
        	})
        ],
        bbar: new Ext.PagingToolbar({
            pageSize: 4,
            store: storehis,
            displayInfo: true,
            displayMsg: '第{0}到第{1}条数据 共{2}条',
            emptyMsg: "没有数据"
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
    {name: 'x'},// 经度
    {name: 'y'},// 纬度
    {name: 'distance'},// 里程
    {name: 'poiName'},// 标注点
    {name: 'vNumber'},// 车牌号
    {name: 'termName'},// 终端名
    {name: 'simCard'},// sim卡号
    {name: 'locType'},// 定位类型
    {name: 'direction'},// 方向
    {name: 'speed'},// 速度
    {name: 'pd'},// 位置描述
    {name: 'time'},// 时间
    {name: 'accStatus'},// acc状态
    {name: 'temperature'},// 温度
    {name: 'jmx'},// 加密经度
    {name: 'jmy'},// 加密纬度
    {name: 'deviceId'},// 终端号
    {name: 'deflectionx'},// 偏转经度
    {name: 'deflectiony'}// 偏转纬度
]);
var storeVehicle = new Ext.data.Store({
		autoLoad: {params:{start: 0, limit: 20}},
	    restful: true,     // <-- This Store is RESTful
	    proxy: proxyVehicle,
	    reader: readerVehicle
	});
// 车辆定位信息
var gridVehicle = new Ext.grid.GridPanel({
	title: '车辆定位信息',
	autoScroll: true,
  // loadMask: {msg:'加载中...'},
	store: storeVehicle,
  columns: [
      {header: 'id', width: 75, sortable: true, dataIndex: 'id',hidden: true},
      {header: '车牌号', width: 110, sortable: true, dataIndex: 'vNumber'},
      {header: '手机卡号', width: 85, sortable: true, dataIndex: 'simCard'},
      {header: '经度', width: 85, sortable: true,dataIndex : 'x'},
      {header: '纬度', width: 85, sortable: true, dataIndex: 'y'},
      {header: '速度', width: 50, sortable: true,  dataIndex: 'speed'},
      {header: '方向', width: 55, sortable: true, dataIndex: 'direction'},
      {header: '距离', width: 55, sortable: true,  dataIndex: 'distance'},
      {header: '温度', width: 45, sortable: true,  dataIndex: 'temperature'},
      {header: '状态', width: 60, sortable: true,  dataIndex: 'accStatus',
    	  renderer:function(r,d,v){
    	  	return r=="0"?"停止":"行驶中";
      }},
      {header: '拜访点', width: 90, sortable: true, dataIndex: 'poiName',
    	  renderer:function(r,d,v){
    	  	return r=="null"?"":r;
      }},
      {header: '位置描述', width: 280, sortable: true,  dataIndex: 'pd'},
      {header: '时间', width: 120, sortable: true,  dataIndex: 'time',
	        renderer: function tips(val) {
	        	return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
		}
      // {header: '类型', width: 40, sortable: true, dataIndex: 'locType'},
      // {header: '加密经度', width: 75, sortable: true, dataIndex: 'jmx'},
      // {header: '加密纬度', width: 75, sortable: true, dataIndex: 'jmy'},
      // {header: '终端号', width: 75, sortable: true, dataIndex: 'deviceId'},
      // {header: '偏转经度', width: 75, sortable: true, dataIndex:
		// 'deflectionx'},
      // {header: '偏转纬度', width: 75, sortable: true, dataIndex:
		// 'deflectiony'},
  ],
  	
	  bbar: new Ext.PagingToolbar({
	      pageSize: 20,
	      store: storeVehicle,
	      displayInfo: true,
	      displayMsg: '第{0}到第{1}条数据 共{2}条',
	      emptyMsg: "没有数据"
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

	
	var tmpTermName = tmpstore.get('vNumber');// 车牌号码
	var tmpsimcard = tmpstore.get('simCard');// 手机号码
	var tmpspeed = tmpstore.get('speed');// 速度
	var tmptime = tmpstore.get('time');// 时间
	var tmpTem = tmpstore.get('temperature');// 温度
	var tmpLoc = tmpstore.get('pd');// 位置

	map = document.getElementById('mapifr').contentWindow;

		   var sContent = '';
		   sContent += '车牌号码：'+tmpTermName+'<br>';
		   sContent += '手机号码：'+tmpsimcard+'<br>';
		   sContent += '速度：'+tmpspeed+'<br>';
		   sContent += '时间：'+tmptime+'<br>';
		   sContent += '温度：'+tmpTem+'<br>';
		   sContent += '位置：'+tmpLoc+'<br>';
		   
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
				Ext.Msg.alert('提示', '没有位置信息!');
			}
}

var msgpanel = new Ext.TabPanel({
	border: false, // already wrapped so don't add another border
	activeTab: 0, // second tab initially active
	tabPosition: 'bottom',
	height: 200,
	items: [gridmsg,gridhis,gridVehicle],
	// title:'信息列表',
	region:'south'
});


/*
 * var msgpanel = new Ext.Panel({ id:'bottom-preview', layout:'fit',
 * items:this.preview, height: 200, //hidden:true, split: true, border:false,
 * title:'信息列表', region:'south' });
 */
// msgpanel.setVisible(false);
