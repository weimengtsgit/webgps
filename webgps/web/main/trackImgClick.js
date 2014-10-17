//轨迹回放窗口
var trackWindow;
var track_qd_flag = false;
var track_click = false;
function track_img_click(flag) {
	if(flag == 'qd'){
		track_qd_flag = true;
	}else{
		track_qd_flag = false;
	}
	if(track_click){
		return;
	}
	//点击轨迹回放，关闭实时追踪
	StopRealtimeTrack();
	//关闭实时跟踪车辆运行方向
	StopCarCurrentDirection();
	for ( var i = 0; i < ps.length; i++) {
		map.removeOverlayById(ps[i].pointID);
	}
	locationPointArr = [];
	var treeArr = new Array();
	//取得勾选终端
	getTreeId(root, treeArr);
	if (treeArr.length != 1) {
		Ext.Msg.alert('提示', '请选择一台终端!');
		return;
	}
	var idArr = treeArr[0].id.split('@#');
	trackSearchDeviceid = idArr[0];
	trackSearchDevicelocateType = idArr[1];
	trackSearchDevicesimcard = idArr[2];
	trackSearchDevicevehicleNumber = idArr[3];
	trackSearchStartTime = idArr[4];
	trackSearchEndTime = idArr[5];
	trackSearchWeek = idArr[7];
	
	if (trackSearchDevicelocateType == '0') {
		trackWindow = new UITrackWindow();
	}else{
		trackWindow = new UITrackWindow1();
	}
	//关闭轨迹回放窗口,重置控制面版
	trackWindow.on('close', trackWindowHide);

	trackWindow.show();
	trackWindow.render("tracklocate-div");
	trackWindow.setPosition(5);
	track_click = true;
	var tmpCardPanel = Ext.getCmp('trackquerypanel');
	tmpCardPanel.layout.setActiveItem(0);
	//轨迹回放 , 隐藏中心列表
	var scrollheight = document.documentElement.scrollHeight;
	msgpanel.setVisible(false);
	if (map.fullmapflag) {
		grid.setHeight(scrollheight);
	} else {
		grid.setHeight(scrollheight - 97);
	}
	alarmbottonflag = false;
}
function trackWindowHide() {
	//清除地图上所有覆盖物
	if (trackAllPoint.length > 0) {
		map.mapObj.removeAllOverlays();
		locationPointArr = [];
		map.globalMapPoiOverLayArr = [];
		map.drawPoiOverLay(map.getZoomLevel());
	}
	filtertimeparam = 0;
	clearInterval(playinterval);
	sliderposition = 0;
	sliderlen = 0;
	mapViewModal = false;
	currentstate = 0;
	trackAllPoint = new Array();
	arrowAllPoint = new Array();
	trackPointArr = new Array();
	track_click = false;
}
