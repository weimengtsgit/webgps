/**
* ˵���ͼPOI��¼���϶���
* ���ڣ�2008.04.09
* yang.lei@autonavi.com
*/

var layerControl = null;
  
//ͼ对图层操作对象
function LayerControl(){
	this.LayerList =null;
	this.setLayerList = function(layerlist){
		this.LayerList = layerlist;
	}

	this.getLayerList = function(){
		return this.LayerList ;
	}

	this.addLayer = function(layerid,layername){
		var layer = new Layer();
		layer.LayerID=layerid;
		layer.LayerName=layername;
		layer.UseStatus = true;
		layer.IsEdit = false;
		this.LayerList[this.LayerList.length] = layer;
	}
	
	this.removeLayer = function(layerid){
		var index = this.getLayerIndex(layerid);
		if( index >- 1 ) this.LayerList.splice(index,1);
	}

	this.removePoi = function (poiID){
		for(var i=0; i< this.LayerList.length;i++){
		    var tempLayer = this.LayerList[i];
			for(var j=0; j< tempLayer.LayerPoiList.length; j++ ){
			    var tempID = tempLayer.LayerPoiList[j].ID;
				if(tempID == poiID){
				    tempLayer.LayerPoiList.splice(j,1);
					return;
				}
			}
		}		
	}

	this.getLayer = function(index){
		if(index>-1) return this.LayerList[index];
	}

	this.getLayerIndex = function(pLayerID){
		for( var i=0; i< this.LayerList.length; i++){
			var tempLayer = this.LayerList[i];
			if (tempLayer.LayerID == pLayerID) return i;
		}
		return -1;
	}

	this.setLayerVisible = function (layerid,bVisible){
		var layerIndex = this.getLayerIndex(layerid);
		if( layerIndex >-1 ){
			var layer = this.LayerList[layerIndex];
			layer.IsEdit = bVisible;
		}
	}

	this.setLayerEdit = function(layerid,bEdit){
		var layerIndex = this.getLayerIndex(layerid);
		if( layerIndex >-1 ){
			for(var i=0; i<this.LayerList.length ;i++ ){
				this.LayerList[i].IsEdit = false;
			}
			var layer= this.LayerList[layerIndex];
			layer.IsEdit = bEdit;
		}
	}
	
	this.RefreshMap = function(mapObj){
			
			for (var i=0; i<this.LayerList.length; i++){
				var tempLayer = this.LayerList[i];
				//alert(tempLayer.LayerName + ";" + tempLayer.IsEdit);
				if(tempLayer.IsEdit == true){
					for(var j=0; j<tempLayer.LayerPoiList.length; j++){
						var poi = tempLayer.LayerPoiList[j];
//						var poiContent="图层:"+tempLayer.LayerName+"<br>";
							var poiContent="";
						    var tempPoiName=poi.PoiName;
						    var tempTele=poi.Tele;
						    var tempAddress=poi.Address;
						    if(tempPoiName != "null" || tempPoiName != "" || tempPoiName != "undefined"){
						    poiContent += "名称:"+tempPoiName+"<br>";
						    }
						    //poiContent += "内容:"+poi.PoiDesc+"<br>";
//						    if(tempTele != "null" || tempTele != "" || tempTele != "undefined"){
//						    poiContent += "电话:"+tempTele+"<br>";
//						    }
						    if(tempAddress != "null" && tempAddress != "" && tempAddress != "undefined"){
						    poiContent += "地址:"+tempAddress+"<br>";
						    }
						    else{
						    poiContent += "地址:";
						    }
						if(poi.PoiType == "0"){//POI��
						    var tipOption = new MTipOptions();  
                            //tipOption.title = poi.PoiName;  
                            tipOption.content = poiContent;//poi.PoiDesc;
                            tipOption.fillStyle.color=0xFFFFCC;
                            var markerOption = new MMarkerOptions();  
                            
//                            markerOption.labelStyle.color = 0x000000;  //字体颜色，默认黑色 
//  							markerOption.labelStyle.bold = true;//是否粗体，默认否 
//  							markerOption.labelPosition = new  MPoint(0,0); 
	                        markerOption.picAgent = false;
                            markerOption.imageUrl = "../poi/images/" + poi.IconPath;
                            
//                            markerOption.label = tempPoiName;
//                            markerOption.labelStyle.size = 14; 
//                            markerOption.labelStyle.color = 0x222222;  
//                            markerOption.labelStyle.hasBackground =true;
//                            markerOption.labelStyle.backgroundColor=0xFFff00;
//                            markerOption.labelStyle.bold = false;  
//                            markerOption.labelPosition = new MPoint(25,0);   
							var labelOptions=new MLabelOptions();  
							var fontstyle = new MFontStyle();  //定义字体风格对象   
							  fontstyle.name ="Arial";   
							  fontstyle.size = 12;   
							  fontstyle.color = 0xffffff;   
							  fontstyle.bold = false;   
							  labelOptions.fontStyle=fontstyle;   
//							  labelOptions.borderColor =0x00ff00;   
							  labelOptions.content=tempPoiName;  
							  labelOptions.hasBorder =true;   
							  labelOptions.hasBackground =true;   
							  labelOptions.backgroundColor  =0x728BE9;  
							  
							  markerOption.labelOption=labelOptions;  
							  markerOption.labelPosition = new MPoint(25,0);
							  



                            
                            markerOption.isDraggable = false; 
                            markerOption.imageAlign = 8;
							markerOption.tipOption = tipOption;
                            markerOption.canShowTip= true;
	                        var lngX = poi.PointList[0].lng;
	                        var latY = poi.PointList[0].lat;
                            var ll = new MLngLat(lngX,latY);   
                            Mmarker = new MMarker(ll,markerOption);
                            Mmarker.id = "Poi_Point_" + poi.ID;  
                            mapObj.addOverlay(Mmarker,false);
						}else if(poi.PoiType == "1"){
						    var linest = new MLineStyle();   
                            linest.alpha = 0.5; 
                            linest.color = 0x3366FF ;
                            linest.thickness = 3;   
                            var tipOption = new MTipOptions();
	                        //tipOption.title = poi.PoiName; 
                            tipOption.content = poiContent
                            tipOption.fillStyle.color=0xFFFFCC;
                            tipOption.anchor =  new MPoint(0,0);   
                            var lineopt = new MLineOptions();
                            lineopt.lineStyle = linest;   
                            lineopt.tipOption = tipOption;   
                            lineopt.canShowTip = true;
                            //mapObj.setDefaultLineOption(lineopt); 
							var arr = new Array();
							for(var i=0;i<poi.PointList.length;i++){
							    var lngX = poi.PointList[i].lng;
	                            var latY = poi.PointList[i].lat;
								arr.push(new MLngLat(lngX,latY));  
							}
							var polylineAPI = new MPolyline(arr,lineopt);    
                            polylineAPI.id = "Poi_Polyline_" + poi.ID;
                            mapObj.addOverlay(polylineAPI);
						}else if(poi.PoiType == "2"){
							var areastyle = new MAreaStyle();
                            areastyle.borderStyle.alpha = 0.8; 
                            areastyle.borderStyle.color = 0xFFFFFF; 
                            areastyle.borderStyle.thickness = 2;     
                            areastyle.fillStyle.alpha = 0.5;   
                            areastyle.fillStyle.color = 0x3366FF;  
                            var tipOption = new MTipOptions();
	                        //tipOption.title = poi.PoiName; 
                            tipOption.content = poiContent;
                            tipOption.fillStyle.color=0xFFFFCC;
                            tipOption.anchor =  new MPoint(0,0);      
                            tipOption.hasShadow= false;    
                            var areopt = new MAreaOptions();   
                            areopt.areaStyle=areastyle;   
                            areopt.tipOption=tipOption;   
                            areopt.canShowTip = true;
							var arr = new Array();
                            for(var i=0;i<poi.PointList.length;i++){
							    var lngX = poi.PointList[i].lng;
	                            var latY = poi.PointList[i].lat;
								arr.push(new MLngLat(lngX,latY));  
							}
                            //mapObj.setDefaultAreaOption(areopt);
                            var polygonAPI = new MPolygon(arr,areopt);  
                            polygonAPI.id = "Poi_Polygon_" + poi.ID;  
                            mapObj.addOverlay(polygonAPI);
						}
					}
				}else{
					for(var j=0; j<tempLayer.LayerPoiList.length; j++){
						var poi = tempLayer.LayerPoiList[j];
						if(poi.PoiType == "0"){
						    mapObj.removeOverlayById("Poi_Point_"+poi.ID);
						}else if(poi.PoiType == "1"){
						    mapObj.removeOverlayById("Poi_Polyline_"+poi.ID);
						}else if(poi.PoiType == "2"){
						    mapObj.removeOverlayById("Poi_Polygon_"+poi.ID);
						}
					}
				}
			}
		
	}
}

   


layerControl= new LayerControl();

//ͼ图层数组对象
function Layerlist(){
	this.LayerList = new Array();
}

//ͼ图层对象
function Layer(){
	this.LayerID = "";
	this.LayerName = "";
	this.LayerDesc = "";
	this.UseStatus = false;
	this.IsEdit = 0;
	this.LayerPoiList = new Array();
}

// poi对象
function LayerPoi(){
	this.ID = "";
	this.PointList = new Array();
	this.PoiName = "";
	this.PoiDesc = "";
	this.PoiType = "";
	this.Tele = "";
	this.Address = "";
	this.Keyword = "";
	this.EntCode = "";
	this.IconPath = "";
	this.BorderLineWidth = "";
	this.BorderLineColor = "";
	this.BorderLineAlpha = "";
	this.FillColor = "";
	this.FillAlpha = "";
}


function Point(){
    this.x = "";
	this.y = "";
	this.lng = "";
	this.lat = "";
}


var layerlist = new Layerlist();

function AddPoiToLayerList(layerID,visible,layerName,useStatus,poiID,XYStr,LngLatStr,PoiName,PoiDesc,PoiType,Tele,Address,Keyword,iconPath,borderLineWidth,borderLineColor,borderLineAlpha,fillColor,fillAlpha){
	var layerPoi = new LayerPoi();

    var xyArray = XYStr.split("#");
    var lnglatArray = XYStr.split("#");
	if(xyArray.length == lnglatArray.length){
	    for(var i=0; i< xyArray.length; i++){
		    var tmpXY = xyArray[i].split(",");
		    var tmpLngLat = lnglatArray[i].split(",");
            var point = new Point();
            point.x = tmpXY[0];
            point.y = tmpXY[1];
            point.lng = tmpLngLat[0];
            point.lat = tmpLngLat[1];
		    layerPoi.PointList[layerPoi.PointList.length]=point;
		}
	}else{
	    alert("POI添加到图层发生异常");
	}

	layerPoi.ID = poiID;
	layerPoi.PoiName = PoiName;
	layerPoi.PoiDesc = PoiDesc;
	layerPoi.PoiType = PoiType;
	layerPoi.Tele = Tele;
	layerPoi.Address = Address;
	layerPoi.Keyword = Keyword;
	layerPoi.IconPath = iconPath;
	layerPoi.BorderLineWidth = borderLineWidth;
	layerPoi.BorderLineColor = borderLineColor;
	layerPoi.BorderLineAlpha = borderLineAlpha;
	layerPoi.FillColor = fillColor;
	layerPoi.FillAlpha = fillAlpha;

	var tempLayerIndex = IsHasLayer(layerID);
	if (tempLayerIndex == -1){
		var layer = new Layer();
		layer.LayerID = layerID;
		layer.IsEdit=visible;
		layer.LayerName = layerName;
		layer.UseStatus = useStatus;
		//if (layerPoi.X ==null || layerPoi.Y==null ||layerPoi.X =="" || layerPoi.Y==""  ) return;
		layer.LayerPoiList[layer.LayerPoiList.length] = layerPoi;
		layerlist.LayerList[layerlist.LayerList.length] = layer;
	}else{
		var layer = layerlist.LayerList[tempLayerIndex];

		layerlist.LayerList[tempLayerIndex].IsEdit=visible;
		layer.LayerPoiList[layer.LayerPoiList.length] = layerPoi;
		
	}
	//alert(layer.LayerName + ";" + layer.LayerPoiList.length);
}

//判断layerlist对象中数组是否包含参数所指图层，有返回数组索引，无返回-1
function IsHasLayer(pLayerID){
	for( var i=0; i< layerlist.LayerList.length;i++){
        	var tempLayer = layerlist.LayerList[i];
		if (tempLayer.LayerID == pLayerID) return i;
	}
	return -1;		
}

layerControl.setLayerList(layerlist.LayerList);