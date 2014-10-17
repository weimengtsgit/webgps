//��ʱ��λ����
var win;
var show_btn_click_flag = false;
//��λ
var form = new Ext.FormPanel({
	id : 'conditionform',
	width : 300,
	labelWidth : 100,
	title : '��ʱ��λ',
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

//��ʱ��λ
function show_btn_click() {
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
						Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
						return;
					}
					/*if(treeArr.length>50){
						Ext.Msg.alert('��ʾ', '��λ�������ն�����С��50��!');
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

var locationPointArr = new Array();
var ps = new Array();
//С�˺͵� ����
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
				Ext.Msg.alert('��ʾ', "û����Ч�Ķ�λ����!");
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
				// �ж�ΪGPS�����ֻ�
				if (loc.type == '0') {// �ֻ�
					// �ж�״̬
					if (loc.status == 1) {// ��Ч����
						imageUrl = path + '/images/' + imgUrl + '_1.gif';
					} else if (loc.status == 2) {// ��Ч����
						imageUrl = path + '/images/' + imgUrl + '_2.gif';
					} else {// ������
						imageUrl = path + '/images/' + imgUrl + '_3.gif';
					}
					// ����ֻ�����
					var treeArr = new Array();
					getTreeIdByDeviceId(root, treeArr, loc.deviceId);
					var idArr = treeArr[0].id.split('@#');
					var simcard = idArr[2];// �ֻ�����
					//var vehicleNumber = idArr[3];// ���ƺ���
					var text = treeArr[0].text;// ����
					label = text;
					content += '��&nbsp;&nbsp;����' + text + '<br>';
					content += '�ֻ����룺' + simcard + '<br>';
					content += '��λʱ�䣺' + loc.time + '<br>';
					content += 'λ&nbsp;&nbsp;�ã�' + loc_desc + '<br>';
					if(loc.lastTime !="null"){
						content += '����ʱ�䣺'+loc.lastTime+'<br>';
					}
					//content += '��&nbsp;&nbsp;�ȣ�'+loc.speed+'&nbsp;km/h';
				} else {// GPS
					// �ж��ٶȣ�����0�˶�������ֹͣ
					//        			           if(loc.speed > 0){
					//        			               imageUrl = path+'/images/car_move.gif'
					//        			           }else{
					//        			           	   imageUrl = path+'/images/car_stop.gif'
					//        			           }
					// �ж�״̬
					if (loc.status == 1) {// 24Сʱ��acc״̬����
						imageUrl = path + '/images/' + imgUrl + '_1.gif';
						//imageUrl = path+'/images/reddot.gif'
					} else if (loc.status == 2) {// 24Сʱ��acc״̬��Ч
						imageUrl = path + '/images/' + imgUrl + '_2.gif';
						//imageUrl = path+'/images/bluedot.png'
					} else {// 24Сʱ��������
						imageUrl = path + '/images/' + imgUrl + '_3.gif';
						//imageUrl = path+'/images/graydot.jpg'
					}
					// ����ֻ����롢���ƺ�
					var treeArr = new Array();
					getTreeIdByDeviceId(root, treeArr, loc.deviceId);
					var idArr = treeArr[0].id.split('@#');
					var simcard = idArr[2];
					var vehicleNumber = idArr[3];// ���ƺ���
					//var text = treeArr[0].text;// ����
					label = vehicleNumber;
					content += '���ƺ��룺' + vehicleNumber + '<br>';
					content += '�ֻ����룺' + simcard + '<br>';
					content += '��λʱ�䣺' + loc.time + '<br>';
					content += 'λ&nbsp;&nbsp;�ã�' + loc_desc + '<br>';
					content += '��&nbsp;&nbsp;�ȣ�' + loc.speed + '&nbsp;km/h';
					if (loc.temperature != "") {
						content += "<br>";
						content += '��&nbsp;&nbsp;�ȣ�' + loc.temperature
								+ '&nbsp;';
					}
					if(loc.lastTime !="null"){
						content += "<br>";
						content += '����ʱ�䣺'+loc.lastTime+'<br>';
					}
					
				}

				p = new map.PointMarker(loc.deviceId + 'ss' + i, loc.jmx,
						loc.jmy, label, content, imageUrl, true);
				ps.push(p);
			}
			locationPointArr = map.addPointMarkers(ps, true);
		},
		failure : function(request) {
			Ext.Msg.alert('��ʾ', "��λʧ��!");
		}
	});
}