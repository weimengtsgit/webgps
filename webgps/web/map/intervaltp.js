//��ʱ�������
var intervaltpObj;
//��ʱ����,��¼�ѻ�����
var intervaltplen = 0;
//һ�λ�����
var intervaltpplen = 10;
//�жϹ켣���Ƿ���
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

