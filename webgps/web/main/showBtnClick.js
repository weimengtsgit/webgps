//随时定位窗口
var win;
var show_btn_click_flag = false;
//定位
var form = new Ext.FormPanel({
	id : 'conditionform',
	width : 300,
	labelWidth : 100,
	title : '随时定位',
	bodyStyle : 'padding:5px 10px 0',
	items : [ {
		xtype : 'radio',
		fieldLabel : showBtnChlick.currentTime,
		width : 80,
		checked : true,
		id : 'anytimeloc',
		name : 'locradio',
		listeners: {
			check: {
				fn: function(thisz, checked ){
					if(checked){
						var tmpstartdt = Ext.getCmp('startdt');
						tmpstartdt.setDisabled(true);
						var tmpstarttf = Ext.getCmp('starttf');
						tmpstarttf.setDisabled(true);
						tmpstartdt.setValue('');
						tmpstarttf.setValue('');
					}
				}
			}
		}
	}, {
		xtype : 'radio',
		fieldLabel : showBtnChlick.optionalTime,
		width : 80,
		id : 'seltimeloc',
		name : 'locradio',
		listeners : {
			check: {
				fn: function(thisz, checked ){
					if(checked){
						var tmpstartdt = Ext.getCmp('startdt');
						tmpstartdt.setDisabled(false);
						var tmpstarttf = Ext.getCmp('starttf');
						tmpstarttf.setDisabled(false);
						var myDate = new Date();
						tmpstartdt.setValue(myDate);
						tmpstarttf.setValue(myDate);
					}
				}
			}
		}
	}, {
		fieldLabel : '',
		allowBlank : false,
		editable : false,
		id : 'startdt',
		width : 115,
		format : 'Y-m-d',
		xtype : 'datefield'
	}, new Ext.form.TimeField({
		allowBlank : false,
		editable : false,
		format : 'H:i',
		id : 'starttf',
		width : 115,
		increment : 1
	}) ]
});

//随时定位
function show_btn_click() {
	//点击随时定位，关闭实时追踪
	StopRealtimeTrack();
	//关闭实时跟踪车辆运行方向
	StopCarCurrentDirection();
	if (trackAllPoint.length > 0) {
		map.mapObj.removeAllOverlays();
		locationPointArr = [];
		map.globalMapPoiOverLayArr = [];
		map.drawPoiOverLay(map.getZoomLevel());
	}
	if (!win) {
		win = new Ext.Window({
			applyTo : 'hello-win',
			layout : 'fit',
			width : 300,
			height : 200,
			closeAction : 'hide',
			plain : true,
			buttons : [{
				text : main.ok,
				handler : function() {
					var treeArr = new Array();
					getTreeId(root, treeArr);
					if (treeArr.length <= 0) {
						Ext.Msg.alert('提示', '请选择终端!');
						return;
					}
					/*if(treeArr.length>50){
						Ext.Msg.alert('提示', '定位操作的终端数请小于50个!');
						return;
					}*/
					var gpsDeviceid = '';
					var lbsDeviceid = '';
					var deviceId_imgUrl = [];
					for ( var i = 0; i < treeArr.length; i++) {
						var idArr = treeArr[i].id.split('@#');
						if (idArr.length > 2 && idArr[1] == '0') {
							lbsDeviceid += idArr[0] + ',';
						} else if (idArr.length > 2 && idArr[1] == '1') {
							gpsDeviceid += idArr[0] + ',';
						}
						if (idArr.length >= 7) {
							deviceId_imgUrl.push(new deviceIdImgUrl(idArr[0], idArr[6]));
						} else if (idArr[1] == '0') {
							deviceId_imgUrl.push(new deviceIdImgUrl(idArr[0], 'persion'));
						} else if (idArr[1] == '1') {
							deviceId_imgUrl.push(new deviceIdImgUrl(idArr[0], 'car'));
						}
					}
					if (gpsDeviceid.length > 0) {
						gpsDeviceid = gpsDeviceid.substring(0,gpsDeviceid.length - 1);
					}
					if (lbsDeviceid.length > 0) {
						lbsDeviceid = lbsDeviceid.substring(0,lbsDeviceid.length - 1);
					}
					var tmpanytimeloc = Ext.getCmp('anytimeloc');
					var tmpseltimeloc = Ext.getCmp('seltimeloc');
					var startdt = Ext.getCmp('startdt');
					var starttf = Ext.getCmp('starttf');
					var tmpnow = 'true';
					var tmptime = '';
					if (tmpseltimeloc.checked) {
						tmpnow = 'false';
						tmptime = Ext.util.Format.date(startdt.getValue(), 'Y-m-d') + ' ' + starttf.getValue() + ':00';
					}
					location_map(gpsDeviceid, lbsDeviceid, tmpnow, tmptime, deviceId_imgUrl);
					win.hide();
				}
			}, {
				text : main.cancel,
				handler : function() {
					win.hide();
				}
			} ],
			items : form
		});
	}
	win.show();
	//随时定位 , 隐藏中心列表
	var scrollheight = document.documentElement.scrollHeight;
	msgpanel.setVisible(false);
	if (map.fullmapflag) {
		grid.setHeight(scrollheight);
	} else {
		grid.setHeight(scrollheight - 97);
	}
	alarmbottonflag = false;
}

var locationPointArr = new Array();
var ps = new Array();
//小人和点 开关
var p_d_flag = false;
function location_map(gpsDeviceIds, lbsDeviceIds, now, time, deviceId_imgUrl) {
	map = document.getElementById('mapifr').contentWindow;
	for ( var i = 0; i < locationPointArr.length; i++) {
		map.removeOverlay(locationPointArr[i]);
	}
	locationPointArr = [];
	Ext.Ajax.request({
		url : path + '/locate/locate.do?method=queryLocByTime',
		method : 'POST',
		params : {
			gpsDeviceIds : gpsDeviceIds,
			lbsDeviceIds : lbsDeviceIds,
			now : now,
			time : time
		},
		//timeout : 10000,
		success : function(request) {
			var res = request.responseText;
			var locs = Ext.util.JSON.decode(res);
			if (locs == null || locs.length == 0) {
				Ext.Msg.alert('提示', "没有有效的定位数据!");
				return;
			}
			ps = new Array();
			for ( var i = 0; i < locs.length; i++) {
				var loc_desc = "....";
				var loc = locs[i];
				var imageUrl;
				var label = '';
				var content = '';
				var imgUrl = '';
				for ( var j = 0; j < deviceId_imgUrl.length; j++) {
					if (deviceId_imgUrl[j].deviceId == loc.deviceId) {
						imgUrl = deviceId_imgUrl[j].imgUrl;
						break;
					}
				}
				// 判断为GPS还是手机
				if (loc.type == '0') {// 手机
					// 判断状态
					if (loc.status == 1) {// 有效数据
						imageUrl = path + '/images/' + imgUrl + '_1.gif';
					} else if (loc.status == 2) {// 无效数据
						imageUrl = path + '/images/' + imgUrl + '_2.gif';
					} else {// 无数据
						imageUrl = path + '/images/' + imgUrl + '_3.gif';
					}
					// 获得手机号码
					var treeArr = new Array();
					getTreeIdByDeviceId(root, treeArr, loc.deviceId);
					var idArr = treeArr[0].id.split('@#');
					var simcard = idArr[2];// 手机号码
					//var vehicleNumber = idArr[3];// 车牌号码
					var text = treeArr[0].text;// 姓名
					label = text;
					content += '姓&nbsp;&nbsp;名：' + text + '<br>';
					content += '手机号码：' + simcard + '<br>';
					content += '定位时间：' + loc.time + '<br>';
					content += '位&nbsp;&nbsp;置：' + loc_desc + '<br>';
					if(loc.lastTime !="null"){
						content += '接收时间：'+loc.lastTime+'<br>';
					}
					//content += '速&nbsp;&nbsp;度：'+loc.speed+'&nbsp;km/h';
				} else {// GPS
					// 判断速度，大于0运动，否则停止
					//        			           if(loc.speed > 0){
					//        			               imageUrl = path+'/images/car_move.gif'
					//        			           }else{
					//        			           	   imageUrl = path+'/images/car_stop.gif'
					//        			           }
					// 判断状态
					if (loc.status == 1) {// 24小时内acc状态正常
						imageUrl = path + '/images/' + imgUrl + '_1.gif';
						//imageUrl = path+'/images/reddot.gif'
					} else if (loc.status == 2) {// 24小时内acc状态无效
						imageUrl = path + '/images/' + imgUrl + '_2.gif';
						//imageUrl = path+'/images/bluedot.png'
					} else {// 24小时内无数据
						imageUrl = path + '/images/' + imgUrl + '_3.gif';
						//imageUrl = path+'/images/graydot.jpg'
					}
					// 获得手机号码、车牌号
					var treeArr = new Array();
					getTreeIdByDeviceId(root, treeArr, loc.deviceId);
					var idArr = treeArr[0].id.split('@#');
					var simcard = idArr[2];
					var vehicleNumber = idArr[3];// 车牌号码
					//var text = treeArr[0].text;// 姓名
					label = vehicleNumber;
					content += '车牌号码：' + vehicleNumber + '<br>';
					content += '手机号码：' + simcard + '<br>';
					content += '定位时间：' + loc.time + '<br>';
					content += '位&nbsp;&nbsp;置：' + loc_desc + '<br>';
					content += '速&nbsp;&nbsp;度：' + loc.speed + '&nbsp;km/h';
					if (loc.temperature != "") {
						content += "<br>";
						content += '温&nbsp;&nbsp;度：' + loc.temperature
								+ '&nbsp;';
					}
					if(loc.lastTime !="null"){
						content += "<br>";
						content += '接收时间：'+loc.lastTime+'<br>';
					}
					
				}

				p = new map.PointMarker(loc.deviceId + 'ss' + i, loc.jmx,
						loc.jmy, label, content, imageUrl, true);
				ps.push(p);
			}
			locationPointArr = map.addPointMarkers(ps, true);
		},
		failure : function(request) {
			Ext.Msg.alert('提示', "定位失败!");
		}
	});
}