
//��ʱ��λ
function show_btn_click_qd() {
	//�����ʱ��λ���ر�ʵʱ׷��
	StopRealtimeTrack();
	//�ر�ʵʱ���ٳ������з���
	StopCarCurrentDirection();
	if (trackAllPoint.length > 0) {
		map.mapObj.removeAllOverlays();
		locationPointArr = [];
		map.globalMapPoiOverLayArr = [];
		map.drawPoiOverLay(map.getZoomLevel());
	}
	var treeArr = new Array();
	getTreeId(root, treeArr);
	if (treeArr.length <= 0) {
		Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
		return;
	}
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
	var tmpnow = 'true';
	var tmptime = '';
	location_map(gpsDeviceid, lbsDeviceid, tmpnow, tmptime, deviceId_imgUrl);
	
	//��ʱ��λ , ���������б�
	var scrollheight = document.documentElement.scrollHeight;
	msgpanel.setVisible(false);
	if (map.fullmapflag) {
		grid.setHeight(scrollheight);
	} else {
		grid.setHeight(scrollheight - 97);
	}
	alarmbottonflag = false;
}
