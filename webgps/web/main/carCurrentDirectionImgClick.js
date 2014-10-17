/**
 * 
 */
var CarCurrentDirection = (function(){
	//map.jspҳ��
	var _mapPage = null;
	//��ͼ
	var _mapObj  = null;
	//����ͼ�����Ŀ
	var _numOfDirectionPic = 36;
	//��ӵĸ�����
	var _locationPointArr = [];
	//��λ�Ƿ�ɹ���Ŀǰ�ǵ�sessionʧЧʱ��û5�뻹��ˢ��һ����ʷ����,ÿ������ʾһ�Ρ���λʧ�ܡ���
	var _isQueryCarCurrentDirectionSuccess = true;
	
	/**
	 * ��ʼ��
	 */
	var _initCarCurrentDirection = function(){
		_mapPage = _mapPage || document.getElementById('mapifr').contentWindow;
		_mapObj  = _mapObj || _mapPage.mapObj;
	};
	
	/**
	 * ɾ����ӵĸ�����
	 */
	var _removeAddedOberlay = function(){
		_mapObj.removeOverlays(_locationPointArr);
		_locationPointArr = [];
	};
	
	//��ѯ�����ն˵�ǰ���з�����Ϣ
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
					Ext.Msg.alert('��ʾ', "û����Ч�Ķ�λ����!");
					return;
				}
				var ps = new Array();
				for ( var i = 0; i < locs.length; i++) {
					var loc_desc = "....";
					var loc = locs[i];
					var imageUrl= _getDirectionPic(loc.direction,loc.speed,loc.status,loc.type);
					var label = '';
					var content = '';
					
					// �ж�ΪGPS�����ֻ�
					if (loc.type == '0') {// �ֻ�
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
					}else{
						// ����ֻ����롢���ƺ�
						var treeArr = new Array();
						getTreeIdByDeviceId(root, treeArr, loc.deviceId);
						var idArr = treeArr[0].id.split('@#');
						var simcard = idArr[2];
						var vehicleNumber = idArr[3];// ���ƺ���
						label  = '���ƺ��룺' + vehicleNumber + '\n';
						label += '��  �ȣ�' + loc.speed + ' km/h';
						
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
					var p = new _mapPage.PointMarker(loc.deviceId + 'direc' + i, loc.jmx,
							loc.jmy, label, content, imageUrl, true);
					ps.push(p);
				}
				_locationPointArr = _mapPage.addPointMarkers(ps, false);
			},
			failure : function(request) {
				//��λʧ��
				_isQueryCarCurrentDirectionSuccess = false;
				_closeWin();
				Ext.Msg.alert('��ʾ', "��λʧ��!");
			}
		});
	};
	
	//��ʱװ��
	var _Runner = function() {
		var RunnerInterval = null;
		var ReloadInterval = null;
		
		//ָ��ʱ����������
		var createIntervalForRunnerInterval = function(intervalTime, callback){
			RunnerInterval = setInterval(callback,intervalTime*1000);
		};
		
		//5��ˢһ�ε�ͼ
		var createIntervalForReloadInterval = function(){
			ReloadInterval  = setInterval(function(){
				//5��ˢһ��
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
				//����ִ��һ��
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
	 * ��λ
	 */
	var _carCurrentDirectionImgClick = function(){
		//ÿ60����һ��
		var intervalTime = 60;
		var treeArr = new Array();
		getTreeId(root, treeArr);
		if (treeArr.length <= 0) {
			Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
			_mapPage.document.getElementById("openCarCurrentDirection").style.display="";
    		_mapPage.document.getElementById("closeCarCurrentDirection").style.display="none";
			return;
		}
//		if (treeArr.length > 50) {
//			Ext.Msg.alert('��ʾ', 'ʵʱ��λ�������ն�����С��50��!');
//			return;
//		}
		
		//gps�նˣ������նˣ�
		var gpsDeviceIds= '';
		//lbs�նˣ��ֻ��նˣ�
		var lbsDeviceids = '';
		for ( var i = 0; i < treeArr.length; i++) {
			var idArr = treeArr[i].id.split('@#');
			//�����ն�
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
	
	//�رմ���
	function _closeWin() {
		//�رն�ʱ��
		_Runner.stop();
		_removeAddedOberlay();
		_mapPage.document.getElementById("openCarCurrentDirection").style.display="";
		_mapPage.document.getElementById("closeCarCurrentDirection").style.display="none";
	}
	
	/**
	 * ����direction���㷽��ͼƬ���±�
	 * @param direction   ����0-360��
	 * @param n           �ܵķ�����Ŀ
	 * @return ����ͼ���±�
	 */
	var _getIndexOfDirection = function(direction,n){
		direction = isNaN(direction)?0:Number(direction);
		return Math.floor(((direction+360/(2*n))%360)/(360/n)+1);
	};
	
	//�����ٶȡ�״̬��direction��ȡ��ʾͼ�� 
	//status:1��2��3�ֱ��Ӧ24Сʱ��acc״̬������24Сʱ��acc״̬��Ч��24Сʱ��������(��ʱ�������)
	var _getDirectionPic = function(direction,speed,status,type){
		var imageUrl = '';
		if(type == '0'){//�ֻ��ն�
			if (status == 1) {// ��Ч����
				imageUrl = path + '/images/persion_1.gif';
			} else if (status == 2) {// ��Ч����
				imageUrl = path + '/images/persion_2.gif';
			} else {// ������
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
			//�ر�ʵʱ��λ
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

//Ϊ�˸��������÷������ñ���һ�£���carCurrentDirectionImgClickֱ������Ϊȫ�ֱ���
window.carCurrentDirectionImgClick = CarCurrentDirection.carCurrentDirectionImgClick;
window.StopCarCurrentDirection = CarCurrentDirection.closeWin;