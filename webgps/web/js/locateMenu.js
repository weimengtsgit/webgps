//���ٱ���
function showSpeedAlarm(){
	parent.left_up.map.popupComShowDatasPage("���ٱ���","/alarm/alarm.do?method=speedAlarmList");
}
//Խ�籨��
function showAreaAlarm(){
	parent.left_up.map.popupComShowDatasPage("Խ�籨��","/alarm/alarm.do?method=areaAlarmList");
}


function DoMenu(emid){
	//��ʾleft_up������left_middle
	parent.parent.down.cols="22%,*,0";//��ʾ��ߡ���ͼ�������ұ�
	if(emid=="ChildMenu1" ||emid=="ChildMenu2" || emid=="ChildMenu3"){
		parent.left.rows="60%,0,*";//��ʾleft_up������left_middle����ʾӥ��
	}else{
		parent.left.rows="*,0,0";//��ʾleft_up������left_middle������ʾӥ��
	}
	if(emid=="ChildMenu7"){
		parent.frames["left_middle"].location.href = "locate/vediolist.jsp";
		parent.left.rows="0,*,0";//��ʾleft_middle������ʾӥ��
	}else{
		parent.left_up.DoMenu2(emid);
	}
}