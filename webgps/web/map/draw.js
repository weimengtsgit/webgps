
function removePolygonById(id){
	mapObj.removeOverlayById(id);
}

function addOverlayOver(event) {
	// mapObj.removeEventListener(mapObj, REMOVE_OVERLAY,addOverlayOver);
	var overlay = mapObj.getOverlayById(event.overlayId);
	// alert("id "+event.overlayId);
	if (drawTempObjectID != "")
		mapObj.removeOverlayById(drawTempObjectID);
	drawTempObjectID = event.overlayId;
	var xs = "";
	var ys = "";
	//��ͼ�ϻ���
	if(overlay.TYPE == "Marker"){
		shapeType = "0";
		shapeRadio = 0;
		xy = overlay.lnglat; // ���ĵ����
		var jmx = xy.lngX;// ���ĵ�X����
		var jmy = xy.latY;// ���ĵ�Y����
		shapeMarker = jmx+","+jmy;
		fillIntoMarkerPage();
		
		return;
	}
	if (overlay.TYPE == "Polygon" || overlay.TYPE == "Polyline"
			|| overlay.TYPE == "Rectangle" || overlay.TYPE == "Circle") {//  
		if (overlay.TYPE == "Polygon") {
			shapeType = "1";
			shapeRadio = 0;
		} else if (overlay.TYPE == "Polyline") {
			shapeType = "2";
			shapeRadio = 500;
		} else if (overlay.TYPE == "Rectangle") {
			shapeType = "3";
			shapeRadio = 500;
		} else if (overlay.TYPE == "Circle") {
			shapeType = "4";
			shapeRadio = 500;
			var radius = overlay.radius;// �뾶
			xy = overlay.center; // ���ĵ����
			var jmx = xy.lngX;// ���ĵ�X����
			var jmy = xy.latY;// ���ĵ�Y����
			shapeCoords = jmx + "," + jmy + "," + radius + "*";
			fillIntoAlarmPage();
			return;
		}
		var points = overlay.lnglatArr;

		for (var i = 0; i < points.length; i++) {
			var pt = points[i];
			shapeCoords += pt.lngX + "," + pt.latY + "#";
		}

		if (shapeCoords != "") {
			shapeCoords = shapeCoords.substring(0, shapeCoords.length - 1);
		}
		fillIntoAlarmPage();
	}
}
//poi.js
function fillIntoMarkerPage(){
	//��ҳ�汣�������
	parent.fillMarker(shapeMarker);
}
//areaalarm.js
function fillIntoAlarmPage(){
	//��ҳ�汣����������
	parent.fillMapData(shapeType, shapeRadio, shapeCoords   );
}

// ��ʼ���ƶ��������
function beginDrawPolygonOnMap() {
	mapObj.addEventListener(mapObj, ADD_OVERLAY, addOverlayOver);
	clearMap();
	mapObj.setCurrentMouseTool(DRAW_POLYGON);
}
// ��ʼ���Ƶ�
function beginDrawMarkerOnMap() {
	mapObj.addEventListener(mapObj, ADD_OVERLAY, addOverlayOver);
	clearMap();
	mapObj.setCurrentMouseTool(ADD_MARKER);
}

/**
 * ��ͼ����
 */
var shapeType = "";
var shapeRadio = "";
var shapeCoords = "";
var drawTempObjectID = "";
var shapeMarker = "";
// ���
function clearMap() {
	shapeType = "";
	shapeRadio = "";
	shapeCoords = "";
	mapObj.removeOverlayById(drawTempObjectID);
}
