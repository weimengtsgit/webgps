/**
 * 
 */
var alarmbottonflag = false;
//�������� �����б���ʾ
function alarm(){
	var scrollheight=document.documentElement.scrollHeight;
	if(!alarmbottonflag){
        alarmajax();
		//��ʾ�����б�
		msgpanel.setVisible(true);
		msgpanel.setActiveTab(gridmsg);
		//add magiejue 2010-11-23
		msgpanel.unhideTabStripItem(gridhis);//ȡ������ gridhis tabҳ
		msgpanel.unhideTabStripItem(gridmsg);//ȡ������ gridmsg tabҳ
		msgpanel.hideTabStripItem(gridVehicle);//���� gridVehicle tabҳ
		
		if(map.fullmapflag){
			grid.setHeight(scrollheight);
		}else{
		    grid.setHeight(scrollheight-200-100);
		}
		
		alarmbottonflag = true;
	}else{
		//���������б�
		msgpanel.setVisible(false);
	    if(map.fullmapflag){
			grid.setHeight(scrollheight);
		}else{
		    grid.setHeight(scrollheight-97);
		}
	    alarmbottonflag = false;
	}
}