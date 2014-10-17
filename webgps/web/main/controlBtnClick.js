//控制中心
var controlcenterwin;
//控制中心
        function control_btn_click(){
        		//点击控制中心，关闭实时追踪
        		StopRealtimeTrack();
        		//关闭实时跟踪车辆运行方向
        		StopCarCurrentDirection();
        		
	        	var treeArr = new Array();
				getTreeId(root, treeArr);
				var gpsDeviceid = '';
				var lbsDeviceid = '';
				for (var i = 0; i < treeArr.length; i++) {
					var idArr = treeArr[i].id.split('@#');
					if (idArr.length > 2 && idArr[1] == '1') {
						gpsDeviceid += idArr[0] + ',';
					}
				}
				if (gpsDeviceid.length > 0) {
					gpsDeviceid = gpsDeviceid.substring(0, gpsDeviceid.length - 1);
				}
	    	if(!controlcenterwin){
	            controlcenterwin = new Ext.Window({
	                applyTo:'control-win',
	                layout:'fit',
	                width:400,
	                height:80,
	                title:'控制中心',
	                closeAction:'hide',
	                plain: true,
	                items : [
	                	new Ext.Panel({
	                		frame:true,
	                		layout:'column',
	                		items:[
	                		new Ext.Button({
	                			columnWidth:.20,
						        text: controlBtnClick.outageOfOilAndPower,
						        renderTo:Ext.getBody(),
						        handler: function(){
						        	oilBreakelBreak();
						        }
						    }),new Ext.Button({
						    	columnWidth:.20,
						        text: controlBtnClick.recoveryOfOilAndPower,
						        renderTo:Ext.getBody(),
						        handler: function(){
						        	turnOnOilPower();
						        }
						    }),new Ext.Button({
						    	columnWidth:.20,
						        text: controlBtnClick.alarmDisable,
						        renderTo:Ext.getBody(),
						        handler: function(){
						        	stopHoldAlarm();
						        }
						    }),new Ext.Button({
						    	columnWidth:.20,
						        text: controlBtnClick.liftingSpeed,
						        renderTo:Ext.getBody(),
						        handler: function(){
						        	stopSpeedAlarm();
						        }
						    }),new Ext.Button({
						    	columnWidth:.20,
						        text: controlBtnClick.dischargeArea,
						        renderTo:Ext.getBody(),
						        handler: function(){
						        	stopAreaAlarm();
						        }
						    })]
	                	})
	                	
	                ]
	            });
	        }
	        controlcenterwin.show();
	        //控制中心 , 隐藏中心列表
	        var scrollheight=document.documentElement.scrollHeight;
			msgpanel.setVisible(false);
		    if(map.fullmapflag){
				grid.setHeight(scrollheight);
			}else{
		    	grid.setHeight(scrollheight-97);
			}
		    alarmbottonflag = false;
        }
        