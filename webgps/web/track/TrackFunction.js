/******   �켣�ط�ʹ�õķ���   ******/
/**
 * ������尴ť������õķ���
 */
/**
* ��һ֡
*/
function previousTrack(){
	//���ÿ���������״̬,��һ֡״̬
	currentstate = 0;
	//������Ŷ���
	clearInterval(playinterval);
	//����һ����
	drawtrackpoint(0);
	sliderposition = 0;
}
/**
 * ����
 */
function play(){
	sliderpositionflag = false;
	//���ÿ���������״̬,����״̬
	currentstate = 1;
	//������Ŷ���
	clearInterval(playinterval);
	//���ò��Ŷ���,(refreshTime)������ѡ'�ط��ٶ�'(playspeedlabel)���в���.
	playinterval=setInterval('draw();',refreshTime*1000);
}
/**
 * ��ͣ
 */
function pause(){
	//���ÿ���������״̬,��ͣ״̬
	currentstate = 2;
	//������Ŷ���
	clearInterval(playinterval);
}
/**
* ֹͣ
*/
function stop(){
	//���ÿ���������״̬,ֹͣ״̬
	currentstate = 3;
	//������previousTrackһ��
	previousTrack();
}

/**
* ���һ֡
*/
function nextTrack(){
	//���ÿ���������״̬,���һ֡״̬
	currentstate = 4;
	//������Ŷ���
	clearInterval(playinterval);
	//�����һ����
	drawtrackpoint(sliderlen);
	sliderposition = sliderlen;
}
/**
 * ���Ż���
 */
function draw(){
	var p = sliderposition;
	//�жϲ��Ž���ֵ�Ƿ񳬹��켣�����ֵ
	if((sliderposition > sliderlen)&& (sliderlen > 0)){
		//������Ŷ���
		clearInterval(playinterval);
		return;
	}
	var tmpip = Ext.getCmp('intervalpointcombo').getValue();
	p = p + Number(tmpip);
	if((p > sliderlen) && (sliderposition<sliderlen)){
		p = sliderlen;
	}
	//����ǰ��
	drawtrackpoint(p);
	sliderposition = p;
	//sliderposition++;
}
/**
 * ����ǰ��
 * @param {} position
 */
function drawtrackpoint(position){
	trackCurrent = trackPointArr[position];
	//alert('position:'+position);
	//alert('trackCurrent:'+trackCurrent);
	//alert(trackCurrent == undefined);
	
	if(trackCurrent == undefined){
		return;
	}
	
	map.mapObj.addOverlay(trackCurrent,mapViewModal);
	//���ûطŽ�����
	var tmpslider=Ext.getCmp('playtemposlider');
	tmpslider.setValue(position+1,true);
	//�ı䲥�Ž���,����'�طŽ���'label
	var tmpplaytempolabel = Ext.getCmp('playtempolabel');
	tmpplaytempolabel.setText((position+1)+'/'+(sliderlen+1));
}

/**
 * �ָ����Ű�ť״̬
 */
function resetimgsrc(){
	var first = Ext.getCmp('media_controls_first');
	var play = Ext.getCmp('media_controls_play');
	var pause = Ext.getCmp('media_controls_pause');
	var stop = Ext.getCmp('media_controls_stop');
	var last = Ext.getCmp('media_controls_last');
	
    first.setIconClass('media_controls_light_first');
    play.setIconClass('media_controls_light_play');
    pause.setIconClass('media_controls_light_pause');
    stop.setIconClass('media_controls_light_stop');
    last.setIconClass('media_controls_light_last');
    
}

/**
 * ��ѯ�켣��
 * @param {} deviceid
 * @param {} starttime
 * @param {} endtime
 */
function trackSearchAjax(startdate,starttime,enddate,endtime,filterstar){
	Ext.Ajax.request({
		 url:path+'/locate/locate.do?method=track',
		 method :'POST', 
		 params: { deviceId : trackSearchDeviceid , startDate : startdate , startTime : starttime , endDate : enddate , endTime : endtime , filterStar : filterstar},
		 timeout : 180000,
		 success : function(request) {
		 	Ext.Msg.hide();
		 	var res = Ext.decode(request.responseText);
		 	if(res==null || res.length==0){
		 		//Ext.getCmp('trackquerypanel').setTitle('�켣��ѯ');
			Ext.getCmp('trackquerypanel').setIconClass('icon-trackquery');
		       Ext.Msg.alert('��ʾ', "û����Ч�Ķ�λ����!");
		       
		       return;
		   }
		 	if(res.length>0){
		 		//add magiejue 2010-11-23
		 		terminalType=res[0].locType;//����ն�����
		 		var tmpCardPanel = Ext.getCmp('trackquerypanel');
				tmpCardPanel.layout.setActiveItem(1);
		 		//���õ�ͼҳ�Ľ����켣���ݷ���,�����㻭��
		 		map.parseTrackData(res);
		 	}
		 	Ext.getCmp('trackquerypanel').setTitle('�켣��ѯ');
			Ext.getCmp('trackquerypanel').setIconClass('icon-trackquery');
		 },
		 failure : function(request) {
			 Ext.Msg.alert('��ʾ', "�켣�طŲ�ѯʧ��!");
			 Ext.getCmp('trackquerypanel').setTitle('�켣��ѯ');
			Ext.getCmp('trackquerypanel').setIconClass('icon-trackquery');
		 }
		});
}

