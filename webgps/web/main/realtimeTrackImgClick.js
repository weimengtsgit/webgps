/**
 * 
 */
//实时追踪窗口
var realtimetrackwin;
var realtimeTrackLineArray = [];
function realtime_track_img() {
	//realtime_track_btn.on('click', function(){
	//点击实时追踪，关闭实时追踪
	StopRealtimeTrack();
	//关闭实时跟踪车辆运行方向
	StopCarCurrentDirection();
	if (trackAllPoint.length > 0) {
		map.mapObj.removeAllOverlays();
		locationPointArr = [];
		map.globalMapPoiOverLayArr = [];
		map.drawPoiOverLay(map.getZoomLevel());
	}
	if (!realtimetrackwin) {
		realtimetrackwin = new Ext.Window({
			applyTo : 'realtime-track-win',
			layout : 'fit',
			width : 300,
			height : 250,
			closeAction : 'hide',
			plain : true,
			title : '实时追踪',
			//modal:true,
			buttons : [ {
				text : '确定',
				id : 'realtimetrack_btn_ok',
				handler : function() {
					realtime_field = Ext.getCmp('realtime_field').getValue();
					if (!realtime_field) {
						Ext.Msg.alert('提示', '请输入追踪时间间隔!');
						return;
					}
					if(Number(realtime_field)<30){
						Ext.Msg.alert('提示', '追踪时间间隔不能小于30秒!');
						return;
					}
					var treeArr = new Array();
					getTreeId(root, treeArr);
					if (treeArr.length <= 0) {
						Ext.Msg.alert('提示', '请选择终端!');
						return;
					}
					if (treeArr.length > 50) {
						Ext.Msg.alert('提示', '实时追踪操作的终端数请小于50个!');
						return;
					}
					RealtimeTrackF();
					Ext.getCmp('realtimetrack_btn_ok').hide();
					Ext.getCmp('realtimetrack_btn_cancel').hide();
					Ext.getCmp('realtimetrack_btn_back').show();
				}
			}, {
				text : '取消',
				id : 'realtimetrack_btn_cancel',
				handler : function() {
					StopRealtimeTrack();
				}
			}, {
				text : '返回',
				id : 'realtimetrack_btn_back',
				hidden : true,
				handler : function() {
					Ext.getCmp('realtimetrack_btn_ok').show();
					Ext.getCmp('realtimetrack_btn_cancel').show();
					Ext.getCmp('realtimetrack_btn_back').hide();
					var pbar4 = Ext.getCmp('pbar4');
					pbar4.reset();
					clearInterval(realtime_timeout_obj);
					clearInterval(RunnerInterval);
					clearTimeout(RunnerInterval);
					map = document.getElementById('mapifr').contentWindow;
					for ( var i = 0; i < locationPointArr.length; i++) {
						map.removeOverlay(locationPointArr[i]);
					}
					locationPointArr = [];
					for ( var i = 0; i < realtimeTrackLineArray.length; i++) {
						map.removeOverlay(realtimeTrackLineArray[i]);
					}
					realtimeTrackLineArray = [];
				}
			} ],
			listeners : {
				'hide' : function(p) {
					StopRealtimeTrack();
				}
			},
			items : new Ext.FormPanel({
				id : 'realtimetrackform',
				width : 300,
				labelWidth : 120,
				bodyStyle : 'padding:5px 10px 0',
				items : [ {
					xtype : 'numberfield',
					id : 'realtime_field',
					fieldLabel : '追踪时间间隔(秒)',
					value : 60,
					anchor : '95%'
				}, {
					xtype : 'radiogroup',
					fieldLabel : '地图显示模式',
					id : 'realtime_map_radiogroup',
					items : [ {
						boxLabel : '自由视野',
						name : 'realtime_map_radio',
						inputValue : 1
					}, {
						boxLabel : '锁定视野',
						name : 'realtime_map_radio',
						inputValue : 2,
						checked : true
					} ]
				}, {
					xtype : 'radiogroup',
					fieldLabel : '追踪轨迹画线选项',
					id : 'trackline_radiogroup',
					items : [ {
						boxLabel : '画线',
						name : 'trackline_radio',
						inputValue : 1
					}, {
						boxLabel : '不画线',
						name : 'trackline_radio',
						inputValue : 2,
						checked : true
					} ]
				}, {
					xtype : 'label',
					fieldLabel : '距下次定位时间间隔'
				}, new Ext.ProgressBar({
					//text:'Waiting on you...',
					id : 'pbar4',
					//textEl:'p4text',
					cls : 'custom'
				//renderTo:'p4'
				}) ]
			})
		});
	}
	realtimetrackwin.show();
	realtimetrackwin.render("realtime-track-win");
	realtimetrackwin.setPosition(5);
	//实时追踪, 隐藏中心列表
	var scrollheight = document.documentElement.scrollHeight;
	msgpanel.setVisible(false);
	if (map.fullmapflag) {
		grid.setHeight(scrollheight);
	} else {
		grid.setHeight(scrollheight - 97);
	}
	alarmbottonflag = false;
}
//实时追踪-定时对象
var realtime_timeout_obj;
var realtime_field;
var RunnerInterval;
var RunnerIntervalCount = 1;
//实时追踪-点击确定按钮，开始执行
function RealtimeTrackF() {
	realtime_timeout_arr = [];
	RealtimeTrackInterval();
	realtime_timeout_obj = setInterval(RealtimeTrackInterval,
			//realtime_field * 1000 * 60);
			realtime_field * 1000);
}
//实时追踪-停止
function StopRealtimeTrack() {
	clearInterval(realtime_timeout_obj);
	clearInterval(RunnerInterval);
	map = document.getElementById('mapifr').contentWindow;
	for ( var i = 0; i < locationPointArr.length; i++) {
		map.removeOverlay(locationPointArr[i]);
	}
	locationPointArr = [];
	for ( var i = 0; i < realtimeTrackLineArray.length; i++) {
		map.removeOverlay(realtimeTrackLineArray[i]);
	}
	realtimeTrackLineArray = [];
	clearTimeout(RunnerInterval);
	var pbar4 = Ext.getCmp('pbar4');
	if (pbar4) {
		pbar4.reset();
		Ext.getCmp('realtimetrack_btn_ok').show();
		Ext.getCmp('realtimetrack_btn_cancel').show();
		Ext.getCmp('realtimetrack_btn_back').hide();
	}
	if (realtimetrackwin) {
		realtimetrackwin.hide();
	}
}
var Runner = function() {
	var f = function(pbar, count, cb) {
		return function() {
			if (RunnerIntervalCount > count) {
				cb();
			} else {
				var i = RunnerIntervalCount / count;
				pbar.updateProgress(i);
			}
			RunnerIntervalCount++;
		};
	};
	return {
		run : function(pbar, count, cb) {
			//for(var i = 1; i < count; i++){
			RunnerIntervalCount = 1;
			RunnerInterval = setInterval(f(pbar, count, cb), 1000);
			//}
		}
	};
}();
//实时追踪-定时刷新位置方法
function RealtimeTrackInterval() {
	var treeArr = new Array();
	getTreeId(root, treeArr);
	if (treeArr.length <= 0) {
		Ext.Msg.alert('提示', '请选择终端!');
		return;
	}
	if (treeArr.length > 50) {
		Ext.Msg.alert('提示', '实时追踪操作的终端数请小于50个!');
		return;
	}
	var pbar4 = Ext.getCmp('pbar4');
	clearInterval(RunnerInterval);
	//Runner.run(pbar4, realtime_field * 60, function() {
	Runner.run(pbar4, realtime_field, function() {
		//pbar4.updateText('All finished!');
	});
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
		gpsDeviceid = gpsDeviceid.substring(0, gpsDeviceid.length - 1);
	}
	if (lbsDeviceid.length > 0) {
		lbsDeviceid = lbsDeviceid.substring(0, lbsDeviceid.length - 1);
	}
	map = document.getElementById('mapifr').contentWindow;
	for ( var i = 0; i < locationPointArr.length; i++) {
		map.removeOverlay(locationPointArr[i]);
	}
	locationPointArr = [];
	for ( var i = 0; i < realtimeTrackLineArray.length; i++) {
		map.removeOverlay(realtimeTrackLineArray[i]);
	}
	realtimeTrackLineArray = [];
	Ext.Ajax.request({
		url : path + '/locate/locate.do?method=queryLocByTime',
		method : 'POST',
		params : {
			gpsDeviceIds : gpsDeviceid,
			lbsDeviceIds : lbsDeviceid,
			now : true,
			time : ''
		},
		//timeout : 10000,
		success : function(request) {
			var res = request.responseText;
			//alert(res);
			var locs = Ext.util.JSON.decode(res);
			if (locs == null || locs.length == 0) {
				Ext.Msg.alert('提示', "没有有效的定位数据!");
				return;
			}
			ps = new Array();
			var loc_desc = "....";
			for ( var i = 0; i < locs.length; i++) {
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
					content += '时&nbsp;&nbsp;间：' + loc.time + '<br>';
					content += '位&nbsp;&nbsp;置：' + loc_desc + '<br>';
					//content += '速&nbsp;&nbsp;度：'+loc.speed+'&nbsp;km/h';
				} else {// GPS
					// 判断速度，大于0运动，否则停止
					//			           if(loc.speed > 0){
					//			               imageUrl = path+'/images/car_move.gif'
					//			           }else{
					//			           	   imageUrl = path+'/images/car_stop.gif'
					//			           }
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
					content += '时&nbsp;&nbsp;间：' + loc.time + '<br>';
					content += '位&nbsp;&nbsp;置：' + loc_desc + '<br>';
					content += '速&nbsp;&nbsp;度：' + loc.speed + '&nbsp;km/h';
					if (loc.temperature != "") {
						content += "<br>";
						content += '温&nbsp;&nbsp;度：' + loc.temperature
								+ '&nbsp;';
					}
				}

				p = new map.PointMarker(loc.deviceId + '@@@###' + i, loc.jmx,
						loc.jmy, label, content, imageUrl, true);
				ps.push(p);
				
				var obj_ = hashMap.Get(loc.deviceId);
				if(obj_ != undefined){
					obj_.push(new map.MLngLat(loc.jmx, loc.jmy));
				}else{
					var objs = [];
					objs.push(new map.MLngLat(loc.jmx, loc.jmy));
					hashMap.Set(loc.deviceId, objs);
				}
				var trackList_ = hashMap.Get(loc.deviceId);
				var trackline_radiogroup_ = Ext.getCmp('trackline_radiogroup')
				.getValue().getGroupValue();
				var drawtracklineflag = false;
				if (trackline_radiogroup_ == 1) {
					drawtracklineflag = true;
				} else if (trackline_radiogroup_ == 2) {
					drawtracklineflag = false;
				}
				realtimeTrackLineArray.push(addTrackPolyline('realtimeTrackline'+loc.deviceId, trackList_,'','', drawtracklineflag));
			}
			var realtime_map_radio = Ext.getCmp('realtime_map_radiogroup')
					.getValue().getGroupValue();
			if (realtime_map_radio == 1) {
				locationPointArr = map.addPointMarkers(ps, false);
			} else if (realtime_map_radio == 2) {
				locationPointArr = map.addPointMarkers(ps, true);
			}
		},
		failure : function(request) {
			//Ext.Msg.alert('提示', "定位失败!");
		}
	});
}

var hashMap = {  
	    Set : function(key,value){this[key] = value;},
	    Get : function(key){return this[key];},
	    Contains : function(key){return this.Get(key) == null?false:true;},
	    Remove : function(key){delete this[key];}
};


function addTrackPolyline(lineID, tracks, label, title, drawtracklineflag){
  	if( tracks==null || tracks.length==0 || tracks == undefined) return;
  	//var colors = [0xff0000,0x00ff00,0x0000ff,0xfff000,0x000fff];
  	var colors = [0xff0000];
  	var lineopt  = new map.MLineOptions();
	var mlineStyle = new map.MLineStyle();
	//轨迹线颜色和宽度
	mlineStyle.color=colors[0];
	mlineStyle.thickness=3;
	lineopt.lineStyle=mlineStyle;  
	lineopt.canShowTip = false;   
	var polylineAPI = new map.MPolyline(tracks,lineopt);     
	polylineAPI.id = lineID;
	if(drawtracklineflag){
		map.mapObj.addOverlay(polylineAPI,false);
	}
	return polylineAPI;
}
