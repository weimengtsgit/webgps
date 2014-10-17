//定时画点对象
var intervaltpObj;
//定时画点,记录已画点数
var intervaltplen = 0;
//一次画点数
var intervaltpplen = 10;
//判断轨迹点是否画完
var intervaltpflag = false;

function intervaltrackPoint(){
	
	if(intervaltpflag){
		intervaltpflag = false;
		intervaltplen = 0;
		clearInterval(intervaltpObj);
		return;
	}
	var len = intervaltplen + intervaltpplen;
	if(parent.trackAllPoint.length <= len){
		len = parent.trackAllPoint.length;
		intervaltpflag = true;
	}
	var tmpArr = new Array();
	for(var i = intervaltplen;i < len;i++){
		tmpArr.push(parent.trackAllPoint[i]);
		if(parent.arrowAllPoint[i]){
			tmpArr.push(parent.arrowAllPoint[i]);
		}
		intervaltplen++;
	}
	mapObj.addOverlays(tmpArr,false);
	
}

