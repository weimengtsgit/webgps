//�켣�طŴ���
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
	//����켣�طţ��ر�ʵʱ׷��
	StopRealtimeTrack();
	//�ر�ʵʱ���ٳ������з���
	StopCarCurrentDirection();
	for ( var i = 0; i < ps.length; i++) {
		map.removeOverlayById(ps[i].pointID);
	}
	locationPointArr = [];
	var treeArr = new Array();
	//ȡ�ù�ѡ�ն�
	getTreeId(root, treeArr);
	if (treeArr.length != 1) {
		Ext.Msg.alert('��ʾ', '��ѡ��һ̨�ն�!');
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
	//�رչ켣�طŴ���,���ÿ������
	trackWindow.on('close', trackWindowHide);

	trackWindow.show();
	trackWindow.render("tracklocate-div");
	trackWindow.setPosition(5);
	track_click = true;
	var tmpCardPanel = Ext.getCmp('trackquerypanel');
	tmpCardPanel.layout.setActiveItem(0);
	//�켣�ط� , ���������б�
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
	//�����ͼ�����и�����
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
