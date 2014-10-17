
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
	//地图上画点
	if(overlay.TYPE == "Marker"){
		shapeType = "0";
		shapeRadio = 0;
		xy = overlay.lnglat; // 中心点对象
		var jmx = xy.lngX;// 中心点X坐标
		var jmy = xy.latY;// 中心点Y坐标
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
			var radius = overlay.radius;// 半径
			xy = overlay.center; // 中心点对象
			var jmx = xy.lngX;// 中心点X坐标
			var jmy = xy.latY;// 中心点Y坐标
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
	//向父页面保存点数据
	parent.fillMarker(shapeMarker);
}
//areaalarm.js
function fillIntoAlarmPage(){
	//向父页面保存多边形数据
	parent.fillMapData(shapeType, shapeRadio, shapeCoords   );
}

// 开始绘制多边型区域
function beginDrawPolygonOnMap() {
	mapObj.addEventListener(mapObj, ADD_OVERLAY, addOverlayOver);
	clearMap();
	mapObj.setCurrentMouseTool(DRAW_POLYGON);
}
// 开始绘制点
function beginDrawMarkerOnMap() {
	mapObj.addEventListener(mapObj, ADD_OVERLAY, addOverlayOver);
	clearMap();
	mapObj.setCurrentMouseTool(ADD_MARKER);
}

/**
 * 地图操作
 */
var shapeType = "";
var shapeRadio = "";
var shapeCoords = "";
var drawTempObjectID = "";
var shapeMarker = "";
// 清除
function clearMap() {
	shapeType = "";
	shapeRadio = "";
	shapeCoords = "";
	mapObj.removeOverlayById(drawTempObjectID);
}
