/**
 * 
 */
var alarmbottonflag = false;
//报警中心 中心列表显示
function alarm(){
	var scrollheight=document.documentElement.scrollHeight;
	if(!alarmbottonflag){
        alarmajax();
		//显示中心列表
		msgpanel.setVisible(true);
		msgpanel.setActiveTab(gridmsg);
		//add magiejue 2010-11-23
		msgpanel.unhideTabStripItem(gridhis);//取消隐藏 gridhis tab页
		msgpanel.unhideTabStripItem(gridmsg);//取消隐藏 gridmsg tab页
		msgpanel.hideTabStripItem(gridVehicle);//隐藏 gridVehicle tab页
		
		if(map.fullmapflag){
			grid.setHeight(scrollheight);
		}else{
		    grid.setHeight(scrollheight-200-100);
		}
		
		alarmbottonflag = true;
	}else{
		//隐藏中心列表
		msgpanel.setVisible(false);
	    if(map.fullmapflag){
			grid.setHeight(scrollheight);
		}else{
		    grid.setHeight(scrollheight-97);
		}
	    alarmbottonflag = false;
	}
}