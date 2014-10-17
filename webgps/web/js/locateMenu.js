//超速报警
function showSpeedAlarm(){
	parent.left_up.map.popupComShowDatasPage("超速报警","/alarm/alarm.do?method=speedAlarmList");
}
//越界报警
function showAreaAlarm(){
	parent.left_up.map.popupComShowDatasPage("越界报警","/alarm/alarm.do?method=areaAlarmList");
}


function DoMenu(emid){
	//显示left_up，隐藏left_middle
	parent.parent.down.cols="22%,*,0";//显示左边、地图，隐藏右边
	if(emid=="ChildMenu1" ||emid=="ChildMenu2" || emid=="ChildMenu3"){
		parent.left.rows="60%,0,*";//显示left_up，隐藏left_middle，显示鹰眼
	}else{
		parent.left.rows="*,0,0";//显示left_up，隐藏left_middle，不显示鹰眼
	}
	if(emid=="ChildMenu7"){
		parent.frames["left_middle"].location.href = "locate/vediolist.jsp";
		parent.left.rows="0,*,0";//显示left_middle，不显示鹰眼
	}else{
		parent.left_up.DoMenu2(emid);
	}
}