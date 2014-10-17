/**
 * 
 */
var CarCurrentDirection = (function(){
	//map.jsp页面
	var _mapPage = null;
	//地图
	var _mapObj  = null;
	//方向图标的数目
	var _numOfDirectionPic = 36;
	//添加的覆盖物
	var _locationPointArr = [];
	//定位是否成功（目前是当session失效时，没5秒还是刷了一次历史数据,每分钟提示一次“定位失败”）
	var _isQueryCarCurrentDirectionSuccess = true;
	
	/**
	 * 初始化
	 */
	var _initCarCurrentDirection = function(){
		_mapPage = _mapPage || document.getElementById('mapifr').contentWindow;
		_mapObj  = _mapObj || _mapPage.mapObj;
	};
	
	/**
	 * 删除添加的覆盖物
	 */
	var _removeAddedOberlay = function(){
		_mapObj.removeOverlays(_locationPointArr);
		_locationPointArr = [];
	};
	
	//查询车辆终端当前运行方向信息
	var _queryCarCurrentDirection = function(gpsDeviceIds,lbsDeviceIds){
		Ext.Ajax.request({
			url : path + '/locate/locate.do?method=queryLocByTime',
			method : 'POST',
			params : {
				gpsDeviceIds : gpsDeviceIds,
				lbsDeviceIds : lbsDeviceIds,
				now : "true",
				time : ""
			},
			//timeout : 10000,
			success : function(request) {
				var res = request.responseText;
				var locs = Ext.util.JSON.decode(res);
				if (locs == null || locs.length == 0) {
					Ext.Msg.alert('提示', "没有有效的定位数据!");
					return;
				}
				var ps = new Array();
				for ( var i = 0; i < locs.length; i++) {
					var loc_desc = "....";
					var loc = locs[i];
					var imageUrl= _getDirectionPic(loc.direction,loc.speed,loc.status,loc.type);
					var label = '';
					var content = '';
					
					// 判断为GPS还是手机
					if (loc.type == '0') {// 手机
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
					}else{
						// 获得手机号码、车牌号
						var treeArr = new Array();
						getTreeIdByDeviceId(root, treeArr, loc.deviceId);
						var idArr = treeArr[0].id.split('@#');
						var simcard = idArr[2];
						var vehicleNumber = idArr[3];// 车牌号码
						label  = '车牌号码：' + vehicleNumber + '\n';
						label += '速  度：' + loc.speed + ' km/h';
						
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
					var p = new _mapPage.PointMarker(loc.deviceId + 'direc' + i, loc.jmx,
							loc.jmy, label, content, imageUrl, true);
					ps.push(p);
				}
				_locationPointArr = _mapPage.addPointMarkers(ps, false);
			},
			failure : function(request) {
				//定位失败
				_isQueryCarCurrentDirectionSuccess = false;
				_closeWin();
				Ext.Msg.alert('提示', "定位失败!");
			}
		});
	};
	
	//定时装置
	var _Runner = function() {
		var RunnerInterval = null;
		var ReloadInterval = null;
		
		//指定时间请求数据
		var createIntervalForRunnerInterval = function(intervalTime, callback){
			RunnerInterval = setInterval(callback,intervalTime*1000);
		};
		
		//5秒刷一次地图
		var createIntervalForReloadInterval = function(){
			ReloadInterval  = setInterval(function(){
				//5秒刷一下
				_mapObj.removeOverlays(_locationPointArr);
				setTimeout(function(){
					_mapObj.addOverlays(_locationPointArr);
				}, 100 );
			},5*1000);
		};
		
		return {
			run : function(intervalTime, callback) {
				if(RunnerInterval || ReloadInterval){
					stop();
				}
				//立马执行一次
				if(callback){callback();}
				createIntervalForRunnerInterval(intervalTime, callback);
				createIntervalForReloadInterval();
			},
			stop : function(){
				if(ReloadInterval){
					clearInterval(ReloadInterval);
					ReloadInterval = null;
				}
				if(RunnerInterval){
					clearInterval(RunnerInterval);
					RunnerInterval = null;
				}
			}
		};
	}();
	
	/**
	 * 定位
	 */
	var _carCurrentDirectionImgClick = function(){
		//每60秒搜一次
		var intervalTime = 60;
		var treeArr = new Array();
		getTreeId(root, treeArr);
		if (treeArr.length <= 0) {
			Ext.Msg.alert('提示', '请选择终端!');
			_mapPage.document.getElementById("openCarCurrentDirection").style.display="";
    		_mapPage.document.getElementById("closeCarCurrentDirection").style.display="none";
			return;
		}
//		if (treeArr.length > 50) {
//			Ext.Msg.alert('提示', '实时定位操作的终端数请小于50个!');
//			return;
//		}
		
		//gps终端（车辆终端）
		var gpsDeviceIds= '';
		//lbs终端（手机终端）
		var lbsDeviceids = '';
		for ( var i = 0; i < treeArr.length; i++) {
			var idArr = treeArr[i].id.split('@#');
			//车辆终端
			if (idArr.length > 2 && idArr[1] == '0') {
				lbsDeviceids += idArr[0] + ',';
			} else if (idArr.length > 2 && idArr[1] == '1') {
				gpsDeviceIds += idArr[0] + ',';
			}
		}
		if (gpsDeviceIds.length > 0) {
			gpsDeviceIds = gpsDeviceIds.substring(0,gpsDeviceIds.length - 1);
		}
		if (lbsDeviceids.length > 0) {
			lbsDeviceids = lbsDeviceids.substring(0,lbsDeviceids.length - 1);
		}
		_Runner.run(intervalTime, function() {
			_queryCarCurrentDirection(gpsDeviceIds,lbsDeviceids);
		});
	};
	
	//关闭窗口
	function _closeWin() {
		//关闭定时器
		_Runner.stop();
		_removeAddedOberlay();
		_mapPage.document.getElementById("openCarCurrentDirection").style.display="";
		_mapPage.document.getElementById("closeCarCurrentDirection").style.display="none";
	}
	
	/**
	 * 根据direction计算方向图片的下标
	 * @param direction   方向（0-360）
	 * @param n           总的方向数目
	 * @return 方向图标下标
	 */
	var _getIndexOfDirection = function(direction,n){
		direction = isNaN(direction)?0:Number(direction);
		return Math.floor(((direction+360/(2*n))%360)/(360/n)+1);
	};
	
	//根据速度、状态与direction获取显示图标 
	//status:1、2、3分别对应24小时内acc状态正常、24小时内acc状态无效、24小时内无数据(暂时不用这个)
	var _getDirectionPic = function(direction,speed,status,type){
		var imageUrl = '';
		if(type == '0'){//手机终端
			if (status == 1) {// 有效数据
				imageUrl = path + '/images/persion_1.gif';
			} else if (status == 2) {// 无效数据
				imageUrl = path + '/images/persion_2.gif';
			} else {// 无数据
				imageUrl = path + '/images/persion_3.gif';
			}
		}else{
			if(isNaN(direction) || speed==0){
				imageUrl = path + '/images/car_direction/arrow_blue.png';
			}else{
				imageUrl = path + '/images/car_direction/arrow'+_getIndexOfDirection(direction,_numOfDirectionPic)+'.png';
			}
		}
		return imageUrl;
	};
	
	return {
		carCurrentDirectionImgClick:function(){
			//关闭实时定位
    		StopRealtimeTrack();
    		_initCarCurrentDirection();
    		_mapPage.document.getElementById("openCarCurrentDirection").style.display="none";
    		_mapPage.document.getElementById("closeCarCurrentDirection").style.display="";
    		_carCurrentDirectionImgClick();
		},
		closeWin : function(){
			if(_mapPage && _mapPage.document.getElementById("openCarCurrentDirection")){
				_closeWin();
			}
		}
	};
})();

//为了更其它调用方法调用保持一致，将carCurrentDirectionImgClick直接设置为全局变量
window.carCurrentDirectionImgClick = CarCurrentDirection.carCurrentDirectionImgClick;
window.StopCarCurrentDirection = CarCurrentDirection.closeWin;