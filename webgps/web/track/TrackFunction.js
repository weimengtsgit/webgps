/******   轨迹回放使用的方法   ******/
/**
 * 控制面板按钮点击调用的方法
 */
/**
* 第一帧
*/
function previousTrack(){
	//设置控制面板控制状态,第一帧状态
	currentstate = 0;
	//清除播放对象
	clearInterval(playinterval);
	//画第一个点
	drawtrackpoint(0);
	sliderposition = 0;
}
/**
 * 播放
 */
function play(){
	sliderpositionflag = false;
	//设置控制面板控制状态,播放状态
	currentstate = 1;
	//清除播放对象
	clearInterval(playinterval);
	//设置播放对象,(refreshTime)根据所选'回放速度'(playspeedlabel)进行播放.
	playinterval=setInterval('draw();',refreshTime*1000);
}
/**
 * 暂停
 */
function pause(){
	//设置控制面板控制状态,暂停状态
	currentstate = 2;
	//清除播放对象
	clearInterval(playinterval);
}
/**
* 停止
*/
function stop(){
	//设置控制面板控制状态,停止状态
	currentstate = 3;
	//功能与previousTrack一样
	previousTrack();
}

/**
* 最后一帧
*/
function nextTrack(){
	//设置控制面板控制状态,最后一帧状态
	currentstate = 4;
	//清除播放对象
	clearInterval(playinterval);
	//画最后一个点
	drawtrackpoint(sliderlen);
	sliderposition = sliderlen;
}
/**
 * 播放画点
 */
function draw(){
	var p = sliderposition;
	//判断播放进度值是否超过轨迹点最大值
	if((sliderposition > sliderlen)&& (sliderlen > 0)){
		//清除播放对象
		clearInterval(playinterval);
		return;
	}
	var tmpip = Ext.getCmp('intervalpointcombo').getValue();
	p = p + Number(tmpip);
	if((p > sliderlen) && (sliderposition<sliderlen)){
		p = sliderlen;
	}
	//画当前点
	drawtrackpoint(p);
	sliderposition = p;
	//sliderposition++;
}
/**
 * 画当前点
 * @param {} position
 */
function drawtrackpoint(position){
	trackCurrent = trackPointArr[position];
	//alert('position:'+position);
	//alert('trackCurrent:'+trackCurrent);
	//alert(trackCurrent == undefined);
	
	if(trackCurrent == undefined){
		return;
	}
	
	map.mapObj.addOverlay(trackCurrent,mapViewModal);
	//设置回放进度条
	var tmpslider=Ext.getCmp('playtemposlider');
	tmpslider.setValue(position+1,true);
	//改变播放进度,更改'回放进度'label
	var tmpplaytempolabel = Ext.getCmp('playtempolabel');
	tmpplaytempolabel.setText((position+1)+'/'+(sliderlen+1));
}

/**
 * 恢复播放按钮状态
 */
function resetimgsrc(){
	var first = Ext.getCmp('media_controls_first');
	var play = Ext.getCmp('media_controls_play');
	var pause = Ext.getCmp('media_controls_pause');
	var stop = Ext.getCmp('media_controls_stop');
	var last = Ext.getCmp('media_controls_last');
	
    first.setIconClass('media_controls_light_first');
    play.setIconClass('media_controls_light_play');
    pause.setIconClass('media_controls_light_pause');
    stop.setIconClass('media_controls_light_stop');
    last.setIconClass('media_controls_light_last');
    
}

/**
 * 查询轨迹点
 * @param {} deviceid
 * @param {} starttime
 * @param {} endtime
 */
function trackSearchAjax(startdate,starttime,enddate,endtime,filterstar){
	Ext.Ajax.request({
		 url:path+'/locate/locate.do?method=track',
		 method :'POST', 
		 params: { deviceId : trackSearchDeviceid , startDate : startdate , startTime : starttime , endDate : enddate , endTime : endtime , filterStar : filterstar},
		 timeout : 180000,
		 success : function(request) {
		 	Ext.Msg.hide();
		 	var res = Ext.decode(request.responseText);
		 	if(res==null || res.length==0){
		 		//Ext.getCmp('trackquerypanel').setTitle('轨迹查询');
			Ext.getCmp('trackquerypanel').setIconClass('icon-trackquery');
		       Ext.Msg.alert('提示', "没有有效的定位数据!");
		       
		       return;
		   }
		 	if(res.length>0){
		 		//add magiejue 2010-11-23
		 		terminalType=res[0].locType;//获得终端类型
		 		var tmpCardPanel = Ext.getCmp('trackquerypanel');
				tmpCardPanel.layout.setActiveItem(1);
		 		//调用地图页的解析轨迹数据方法,并画点画线
		 		map.parseTrackData(res);
		 	}
		 	Ext.getCmp('trackquerypanel').setTitle('轨迹查询');
			Ext.getCmp('trackquerypanel').setIconClass('icon-trackquery');
		 },
		 failure : function(request) {
			 Ext.Msg.alert('提示', "轨迹回放查询失败!");
			 Ext.getCmp('trackquerypanel').setTitle('轨迹查询');
			Ext.getCmp('trackquerypanel').setIconClass('icon-trackquery');
		 }
		});
}

