<%@ page contentType="text/html;charset=GBK"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title></title>
<script type="text/javascript" src="http://app.mapabc.com/apis?&t=flashmap&v=2.3.3&key=40d3fc2416c6fcef4e969e3556657f0e7eae3ddcabc877093b33b6301625d14d43a4346522ffcca5"></script>
<script type="text/javascript">
	var mapObjPlace=null;
	function  mapInit() {
		var mapoption = new MMapOptions();
		mapoption.zoom = 13;//���õ�ͼzoom����
		mapoption.center=new MLngLat(116.397428, 39.90923);
		mapoption.toolbarPos=new MPoint(0,0);
		mapoption.overviewMap = MINIMIZE; //����ӥ��
		mapoption.mapComButton= HIDE;//�����̻�
		mapoption.isCongruence=true;
		mapoption.returnCoordType=COORD_TYPE_OFFSET;
		mapoption.groundLogo = HIDE;//����ˮӡͼƬ
		mapObjPlace = new MMap("mapPlace", mapoption); //��ͼ��ʼ��
	}

	function addMCircles(locs){
		mapObjPlace.removeAllOverlays();//ɾ����ͼ���������и�����
		var arr_circle = [];
		for(var i=0;i<locs.length;i++){
			var loc = locs[i];
			var lng = loc.x;
			var lat = loc.y;
			var radius = loc.radius;
			var arr=new Array();//Բ�����ĵ�
			arr.push(new MLngLat(lng, lat));
			var circle = new MCircle(arr, radius);//����Բ�����ĵ㡢Բ�뾶��optionѡ��������Բ����
			arr_circle.push(circle);
		}
		mapObjPlace.addOverlays(arr_circle,true);//���ͼ��Ӹ�����
	}

</script>
</head>
<body onload="mapInit();" style="margin:0 0 0 0;" scroll="no" >
<div id="mapPlace" style="width: 100%; height: 100%"></div>
</body>
</html>
