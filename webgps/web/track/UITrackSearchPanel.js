/**
 * 轨迹回放查询面板
 * @class UITrackSearchPanel
 * @extends Ext.form.FormPanel
 */
UITrackSearchPanel = Ext.extend(Ext.form.FormPanel, {
	id:'trackSearchPanel_id',
	width: 300,
	labelWidth: 100,
	bodyStyle:'padding:5px 10px 0',
	buttonAlign:'center',
	initComponent: function(){
	    this.items = [{fieldLabel: '开始时间',allowBlank :false,editable:false,id: 'startdttrack', width: 115, format:'Y-m-d', xtype: 'datefield',value: new Date() 
					    ,listeners: {
							'focus':function ( thisz ) {
								var treeArr = new Array();
								//取得勾选终端
							    getTreeId(root,treeArr);
								if(treeArr.length!=1){
									Ext.Msg.alert('提示', '请选择一台终端!');
									return;
								}
								//id: deviceId @# locateType @# simcard @# vehicleNumber @# starttime @# endtime
								//15810740687@#1@#15810740687@#京K01011
								//alert(treeArr[0].id);
								var idArr = treeArr[0].id.split('@#');
								trackSearchDeviceid = idArr[0];
								trackSearchDevicelocateType = idArr[1];
								trackSearchDevicesimcard = idArr[2];
								trackSearchDevicevehicleNumber = idArr[3];
								trackSearchStartTime = idArr[4];
								trackSearchEndTime = idArr[5];
								trackSearchWeek = idArr[7];
								
								var extDateFieldWeek = [];
								if((Number(trackSearchWeek) & 1)==1){}else{extDateFieldWeek.push(1);}
								if((Number(trackSearchWeek) & 2)==2){}else{extDateFieldWeek.push(2);}
								if((Number(trackSearchWeek) & 4)==4){}else{extDateFieldWeek.push(3);}
								if((Number(trackSearchWeek) & 8)==8){}else{extDateFieldWeek.push(4);}
								if((Number(trackSearchWeek) & 16)==16){}else{extDateFieldWeek.push(5);}
								if((Number(trackSearchWeek) & 32)==32){}else{extDateFieldWeek.push(6);}
								if((Number(trackSearchWeek) & 64)==64){}else{extDateFieldWeek.push(0);}
								thisz.setDisabledDays(extDateFieldWeek);
								
							}
						}
	    	},
	    	{xtype: 'timefield',
    	    allowBlank: false,
    		editable: false,
    		format: 'H:i',
    		id: 'starttftrack',
    		width: 115,
    		increment: 1,
    		minValue: trackSearchStartTime,
    		maxValue: trackSearchEndTime,
    		value: trackSearchStartTime,
    		validate : function(){
    			return true;
    		},
    		listeners: {
				'focus':function ( thisz ) {
					var treeArr = new Array();
					//取得勾选终端
				    getTreeId(root,treeArr);
					if(treeArr.length!=1){
						Ext.Msg.alert('提示', '请选择一台终端!');
						return;
					}
					var idArr = treeArr[0].id.split('@#');
					trackSearchDeviceid = idArr[0];
					trackSearchDevicelocateType = idArr[1];
					trackSearchDevicesimcard = idArr[2];
					trackSearchDevicevehicleNumber = idArr[3];
					trackSearchStartTime = idArr[4];
					trackSearchEndTime = idArr[5];
					trackSearchWeek = idArr[7];
					if(thisz.minValue!=trackSearchStartTime){
						thisz.setMinValue(trackSearchStartTime);
					}
					if(thisz.maxValue!=trackSearchEndTime){
						thisz.setMaxValue(trackSearchEndTime);
					}
					var value = thisz.getValue();
					if(value<trackSearchStartTime || value>trackSearchEndTime){
						thisz.setValue(trackSearchStartTime);
					}
				}}
			},
			{fieldLabel: '结束时间',allowBlank :false,editable:false, id: 'enddttrack',width: 115, format:'Y-m-d',xtype: 'datefield',value: new Date()
	    		,listeners: {
	    			'focus':function ( thisz ) {
    					var treeArr = new Array();
    					//取得勾选终端
    				    getTreeId(root,treeArr);
    					if(treeArr.length!=1){
    						Ext.Msg.alert('提示', '请选择一台终端!');
    						return;
    					}
    					//id: deviceId @# locateType @# simcard @# vehicleNumber @# starttime @# endtime
    					//15810740687@#1@#15810740687@#京K01011
    					//alert(treeArr[0].id);
    					var idArr = treeArr[0].id.split('@#');
    					trackSearchDeviceid = idArr[0];
    					trackSearchDevicelocateType = idArr[1];
    					trackSearchDevicesimcard = idArr[2];
    					trackSearchDevicevehicleNumber = idArr[3];
    					trackSearchStartTime = idArr[4];
    					trackSearchEndTime = idArr[5];
    					trackSearchWeek = idArr[7];
    					
    					var extDateFieldWeek = [];
    					if((Number(trackSearchWeek) & 1)==1){}else{extDateFieldWeek.push(1); }
    					if((Number(trackSearchWeek) & 2)==2){}else{extDateFieldWeek.push(2); }
    					if((Number(trackSearchWeek) & 4)==4){}else{extDateFieldWeek.push(3); }
    					if((Number(trackSearchWeek) & 8)==8){}else{extDateFieldWeek.push(4); }
    					if((Number(trackSearchWeek) & 16)==16){}else{extDateFieldWeek.push(5); }
    					if((Number(trackSearchWeek) & 32)==32){}else{extDateFieldWeek.push(6); }
    					if((Number(trackSearchWeek) & 64)==64){}else{extDateFieldWeek.push(0); }
    					thisz.setDisabledDays(extDateFieldWeek);
    					
	    			}
	    		}
	    	},
	    	{xtype: 'timefield',
    	    allowBlank: false,
    		editable: false,
    		format: 'H:i',
    		id: 'endtftrack',
    		width: 115,
    		increment: 1,
    		minValue: trackSearchStartTime,
    		maxValue: trackSearchEndTime,
    		value: trackSearchEndTime,
    		validate : function(){
    			return true;
    		},
    		listeners: {
				'focus':function ( thisz ) {
					var treeArr = new Array();
					//取得勾选终端
				    getTreeId(root,treeArr);
					if(treeArr.length!=1){
						Ext.Msg.alert('提示', '请选择一台终端!');
						return;
					}
					var idArr = treeArr[0].id.split('@#');
					trackSearchDeviceid = idArr[0];
					trackSearchDevicelocateType = idArr[1];
					trackSearchDevicesimcard = idArr[2];
					trackSearchDevicevehicleNumber = idArr[3];
					trackSearchStartTime = idArr[4];
					trackSearchEndTime = idArr[5];
					trackSearchWeek = idArr[7];
					if(thisz.minValue!=trackSearchStartTime){
						thisz.setMinValue(trackSearchStartTime);
					}
					if(thisz.maxValue!=trackSearchEndTime){
						thisz.setMaxValue(trackSearchEndTime);
					}
					var value = thisz.getValue();
					if(value<trackSearchStartTime || value>trackSearchEndTime){
						thisz.setValue(trackSearchStartTime);
					}
				}}
			},
			{fieldLabel: '过滤间隔(分钟)', id: 'filtertime', width: 100, xtype: 'textfield',value:10},
			{fieldLabel: '过滤星数', id: 'filterstar', width: 100, xtype: 'textfield',value:4}];
	
		this.buttons = [{
			text: '查询',
			handler:function(){
				//根据终端类型判断是否显示按钮add 2010-11-23
				var vehiclebtn = Ext.getCmp('vbtn');
				if(trackSearchDevicelocateType == '1'){
					vehiclebtn.enable(); 
					vehiclebtn.setVisible(true);
				}else{
					vehiclebtn.disable();
					vehiclebtn.setVisible(false);
				}
				
				var treeArr = new Array();
				//取得勾选终端
			    getTreeId(root,treeArr);
				if(treeArr.length!=1){
					Ext.Msg.alert('提示', '请选择一台终端!');
					return;
				}
				//id: deviceId @# locateType @# simcard @# vehicleNumber @# starttime @# endtime
				//15810740687@#1@#15810740687@#京K01011
				//alert(treeArr[0].id);
				var idArr = treeArr[0].id.split('@#');
				trackSearchDeviceid = idArr[0];
				trackSearchDevicelocateType = idArr[1];
				trackSearchDevicesimcard = idArr[2];
				trackSearchDevicevehicleNumber = idArr[3];
				trackSearchStartTime = idArr[4];
				trackSearchEndTime = idArr[5];
				trackSearchWeek = idArr[7];
				
				//开始日期
				var tmpstartdt = Ext.getCmp('startdttrack').getValue();
				//开始时间
				var tmpstarttf = Ext.getCmp('starttftrack').getValue();
				//结束日期
				var tmpenddt = Ext.getCmp('enddttrack').getValue();
				//结束时间
				var tmpendtf = Ext.getCmp('endtftrack').getValue();
				//过滤时间间隔
				var tmpfiltertime = Ext.getCmp('filtertime').getValue();
				//过滤星数
				var tmpfilterstar = Ext.getCmp('filterstar').getValue();
				
				//比较日期
				if(tmpstartdt == ''||tmpstarttf == '' || tmpenddt == ''||tmpendtf == ''){
					Ext.MessageBox.alert('提示', '请选择开始时间和结束时间!');
					return;
				}
				
				var startday_ = tmpstartdt.getDay();
				if((Number(trackSearchWeek) & 1)!=1 && startday_ == 1){
					Ext.Msg.alert('提示', '周一为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 2)!=2 && startday_ == 2){
					Ext.Msg.alert('提示', '周二为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 4)!=4 && startday_ == 3){
					Ext.Msg.alert('提示', '周三为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 8)!=8 && startday_ == 4){
					Ext.Msg.alert('提示', '周四为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 16)!=16 && startday_ == 5){
					Ext.Msg.alert('提示', '周五为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 32)!=32 && startday_ == 6){
					Ext.Msg.alert('提示', '周六为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 64)!=64 && startday_ == 0){
					Ext.Msg.alert('提示', '周日为非工作日,请选择工作日!');
					return;
				}
				
				var endday_ = tmpenddt.getDay();
				if((Number(trackSearchWeek) & 1)!=1 && endday_ == 1){
					Ext.Msg.alert('提示', '周一为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 2)!=2 && endday_ == 2){
					Ext.Msg.alert('提示', '周二为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 4)!=4 && endday_ == 3){
					Ext.Msg.alert('提示', '周三为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 8)!=8 && endday_ == 4){
					Ext.Msg.alert('提示', '周四为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 16)!=16 && endday_ == 5){
					Ext.Msg.alert('提示', '周五为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 32)!=32 && endday_ == 6){
					Ext.Msg.alert('提示', '周六为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 64)!=64 && endday_ == 0){
					Ext.Msg.alert('提示', '周日为非工作日,请选择工作日!');
					return;
				}
				
				if(tmpstarttf<trackSearchStartTime || tmpstarttf>trackSearchEndTime){
					Ext.Msg.alert('提示', '开始时间不在工作时间范围内!');
					return;
				}
				if(tmpendtf<trackSearchStartTime || tmpendtf>trackSearchEndTime){
					Ext.Msg.alert('提示', '结束时间不在工作时间范围内!');
					return;
				}
				
				if(tmpstartdt>tmpenddt){
					Ext.MessageBox.alert('提示', '开始时间要小于结束时间!');
					return;
				}else if(tmpstartdt.format('Y-m-d')==tmpenddt.format('Y-m-d')){
					if(tmpstarttf>=tmpendtf){
						Ext.MessageBox.alert('提示', '开始时间要小于结束时间!');
						return;
					}
				}

				var strDate1 = tmpstartdt.format('Y/m/d')+' '+tmpstarttf+':00';
				var strDate2 = tmpenddt.format('Y/m/d')+' '+tmpendtf+':00';
				//alert(strDate1);
				var   date1   =   Date.parse(strDate1);   
				//alert(date1);
				var   date2   =   Date.parse(strDate2);   
				
				var   newDate   =   DateAdd( "d ", -3, new Date());
				if(track_qd_flag && (newDate > tmpstartdt)){
					Ext.MessageBox.alert('提示', '只能查询最近三天的轨迹!');
					return;
				}
				//alert("strDate2与strDate1相差"+Math.ceil((date2-date1)/(24*60*60*1000))+"天")   
				if(Math.ceil((date2-date1)/(24*60*60*1000)) > 3){
					Ext.MessageBox.alert('提示', '只能查询三天内的轨迹回放!');
						return;
				}
				
				//add by liuhongxiao 2011-12-02
				if(tmpfilterstar == ''){
					Ext.Msg.alert('提示', '过滤星数不能为空!');
					return;
				}else if(!/^[\d]+$/.test(tmpfilterstar) || Number(tmpfilterstar)<0 || Number(tmpfilterstar)>13){
					Ext.Msg.alert('提示', '过滤星数必须为0~13之间的数字!');
					return;
				}
				
				filtertimeparam = tmpfiltertime;
				starttime=tmpstartdt.format('Y-m-d')+' '+tmpstarttf;
				endtime=tmpenddt.format('Y-m-d')+' '+tmpendtf;
				
				//trackSearchDeviceid = '15045412114';
				//Ext.getCmp('trackquerypanel').setTitle('轨迹查询中...');
				//Ext.getCmp('trackquerypanel').setIconClass('icon-trackqueryload');
				
				Ext.Msg.show({
			           msg: '正在查询 请稍等...',
			           progressText: '查询中...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
				trackSearchAjax(tmpstartdt.format('Y-m-d'),tmpstarttf+':00',tmpenddt.format('Y-m-d'),tmpendtf+':00',tmpfilterstar);
				
			}
		}/*,{
			text: '重置',
			handler:function(){
				//开始日期
				var tmpstartdt = Ext.getCmp('startdttrack').setValue('');
				//开始时间
				var tmpstarttf = Ext.getCmp('starttftrack').setValue('');
				//结束日期
				var tmpenddt = Ext.getCmp('enddttrack').setValue('');
				//结束时间
				var tmpendtf = Ext.getCmp('endtftrack').setValue('');
				//过滤时间间隔
				var tmpfiltertime = Ext.getCmp('filtertime').setValue('');
				//过滤偏移大的点
				var tmpfilterdeviate = Ext.getCmp('filterdeviate').getValue().setValue('2');
				//道路纠偏
				var tmproadcorrect = Ext.getCmp('roadcorrect').getValue().setValue('2');           	
			}
		}*/];
		
	    UITrackSearchPanel.superclass.initComponent.call(this);
	    
	}
});

/**
 * 轨迹回放查询面板
 * @class UITrackSearchPanel
 * @extends Ext.form.FormPanel
 */
UITrackSearchPanel1 = Ext.extend(Ext.form.FormPanel, {
	//id:'conditionform',
	//xtype: 'form',
	//anchor: '100% 53%',
	id:'trackSearchPanel_id',
	width: 300,
	labelWidth: 100,
	bodyStyle:'padding:5px 10px 0',
	buttonAlign:'center',
	initComponent: function(){
	    this.items = [{fieldLabel: '开始时间',allowBlank :false,editable:false,id: 'startdttrack', width: 115, format:'Y-m-d', xtype: 'datefield',value: new Date() 
					    ,listeners: {
							'focus':function ( thisz ) {
								var treeArr = new Array();
								//取得勾选终端
							    getTreeId(root,treeArr);
								if(treeArr.length!=1){
									Ext.Msg.alert('提示', '请选择一台终端!');
									return;
								}
								//id: deviceId @# locateType @# simcard @# vehicleNumber @# starttime @# endtime
								//15810740687@#1@#15810740687@#京K01011
								//alert(treeArr[0].id);
								var idArr = treeArr[0].id.split('@#');
								trackSearchDeviceid = idArr[0];
								trackSearchDevicelocateType = idArr[1];
								trackSearchDevicesimcard = idArr[2];
								trackSearchDevicevehicleNumber = idArr[3];
								trackSearchStartTime = idArr[4];
								trackSearchEndTime = idArr[5];
								trackSearchWeek = idArr[7];
								
								var extDateFieldWeek = [];
								if((Number(trackSearchWeek) & 1)==1){}else{extDateFieldWeek.push(1);}
								if((Number(trackSearchWeek) & 2)==2){}else{extDateFieldWeek.push(2); }
								if((Number(trackSearchWeek) & 4)==4){}else{extDateFieldWeek.push(3); }
								if((Number(trackSearchWeek) & 8)==8){}else{extDateFieldWeek.push(4); }
								if((Number(trackSearchWeek) & 16)==16){}else{extDateFieldWeek.push(5); }
								if((Number(trackSearchWeek) & 32)==32){}else{extDateFieldWeek.push(6); }
								if((Number(trackSearchWeek) & 64)==64){}else{extDateFieldWeek.push(0); }
								thisz.setDisabledDays(extDateFieldWeek);
								
							}
						}
	    	},
	    	{xtype: 'timefield',
    	    allowBlank: false,
    		editable: false,
    		format: 'H:i',
    		id: 'starttftrack',
    		width: 115,
    		increment: 1,
    		minValue: trackSearchStartTime,
    		maxValue: trackSearchEndTime,
    		value: trackSearchStartTime,
    		validate : function(){
    			return true;
    		},
    		listeners: {
				'focus':function ( thisz ) {
					var treeArr = new Array();
					//取得勾选终端
				    getTreeId(root,treeArr);
					if(treeArr.length!=1){
						Ext.Msg.alert('提示', '请选择一台终端!');
						return;
					}
					var idArr = treeArr[0].id.split('@#');
					trackSearchDeviceid = idArr[0];
					trackSearchDevicelocateType = idArr[1];
					trackSearchDevicesimcard = idArr[2];
					trackSearchDevicevehicleNumber = idArr[3];
					trackSearchStartTime = idArr[4];
					trackSearchEndTime = idArr[5];
					trackSearchWeek = idArr[7];
					if(thisz.minValue!=trackSearchStartTime){
						thisz.setMinValue(trackSearchStartTime);
					}
					if(thisz.maxValue!=trackSearchEndTime){
						thisz.setMaxValue(trackSearchEndTime);
					}
					var value = thisz.getValue();
					if(value<trackSearchStartTime || value>trackSearchEndTime){
						thisz.setValue(trackSearchStartTime);
					}
				}}
			},
	    	{fieldLabel: '结束时间',allowBlank :false,editable:false, id: 'enddttrack',width: 115, format:'Y-m-d',xtype: 'datefield',value: new Date()
	    		,listeners: {
	    			'focus':function ( thisz ) {
    					var treeArr = new Array();
    					//取得勾选终端
    				    getTreeId(root,treeArr);
    					if(treeArr.length!=1){
    						Ext.Msg.alert('提示', '请选择一台终端!');
    						return;
    					}
    					//id: deviceId @# locateType @# simcard @# vehicleNumber @# starttime @# endtime
    					//15810740687@#1@#15810740687@#京K01011
    					//alert(treeArr[0].id);
    					var idArr = treeArr[0].id.split('@#');
    					trackSearchDeviceid = idArr[0];
    					trackSearchDevicelocateType = idArr[1];
    					trackSearchDevicesimcard = idArr[2];
    					trackSearchDevicevehicleNumber = idArr[3];
    					trackSearchStartTime = idArr[4];
    					trackSearchEndTime = idArr[5];
    					trackSearchWeek = idArr[7];
    					
    					var extDateFieldWeek = [];
    					if((Number(trackSearchWeek) & 1)==1){}else{extDateFieldWeek.push(1);}
    					if((Number(trackSearchWeek) & 2)==2){}else{extDateFieldWeek.push(2); }
    					if((Number(trackSearchWeek) & 4)==4){}else{extDateFieldWeek.push(3); }
    					if((Number(trackSearchWeek) & 8)==8){}else{extDateFieldWeek.push(4); }
    					if((Number(trackSearchWeek) & 16)==16){}else{extDateFieldWeek.push(5); }
    					if((Number(trackSearchWeek) & 32)==32){}else{extDateFieldWeek.push(6); }
    					if((Number(trackSearchWeek) & 64)==64){}else{extDateFieldWeek.push(0); }
    					thisz.setDisabledDays(extDateFieldWeek);
    					
	    			}
	    		}
	    	},
	    	{xtype: 'timefield',
    	    allowBlank: false,
    		editable: false,
    		format: 'H:i',
    		id: 'endtftrack',
    		width: 115,
    		increment: 1,
    		minValue: trackSearchStartTime,
    		maxValue: trackSearchEndTime,
    		value: trackSearchEndTime,
    		validate : function(){
    			return true;
    		},
    		listeners: {
				'focus':function ( thisz ) {
					var treeArr = new Array();
					//取得勾选终端
				    getTreeId(root,treeArr);
					if(treeArr.length!=1){
						Ext.Msg.alert('提示', '请选择一台终端!');
						return;
					}
					var idArr = treeArr[0].id.split('@#');
					trackSearchDeviceid = idArr[0];
					trackSearchDevicelocateType = idArr[1];
					trackSearchDevicesimcard = idArr[2];
					trackSearchDevicevehicleNumber = idArr[3];
					trackSearchStartTime = idArr[4];
					trackSearchEndTime = idArr[5];
					trackSearchWeek = idArr[7];
					if(thisz.minValue!=trackSearchStartTime){
						thisz.setMinValue(trackSearchStartTime);
					}
					if(thisz.maxValue!=trackSearchEndTime){
						thisz.setMaxValue(trackSearchEndTime);
					}
					var value = thisz.getValue();
					if(value<trackSearchStartTime || value>trackSearchEndTime){
						thisz.setValue(trackSearchStartTime);
					}
				}}
			},
	    	{xtype:'fieldset',
            id: 'fieldFL',
            checkboxToggle:true,
            title: '停车过滤',
            autoHeight:true,
            defaults: {width: 100},
            defaultType: 'textfield',
            collapsed: true,
            items :[/**{fieldLabel: '间距(米)',
                    xtype: 'combo',
                    width: 100,
                    name: 'first',
                	id : 'spacing',
     				enableKeyEvents : true,
     				grow : true,
     				valueField : "id",
     				displayField : "name",
     				mode : 'local',// 数据是在本地
     				value:100,
     				forceSelection : true,
     				editable : false,
     				triggerAction : 'all',
     				store : new Ext.data.SimpleStore({
     					fields : [ "name", "id" ],
     					data : [[ '10m', '10' ],  [ '100m', '100' ],
     							[ '500m', '500' ], [ '1km', '1000' ]]
     				})
                },**/{
                    fieldLabel: '间隔(分钟)',
                    id:'interval',
                    width: 100,
                    value:3,
                    name: 'last'
                }
            ]}];
	
		this.buttons = [{
			text: '查询',
			handler:function(){
				//根据终端类型判断是否显示按钮add 2010-11-23
				var vehiclebtn = Ext.getCmp('vbtn');
				if(trackSearchDevicelocateType == '1'){
					vehiclebtn.enable(); 
					vehiclebtn.setVisible(true);
				}else{
					vehiclebtn.disable();
					vehiclebtn.setVisible(false);
				}
				
				var treeArr = new Array();
				//取得勾选终端
			    getTreeId(root,treeArr);
				if(treeArr.length!=1){
					Ext.Msg.alert('提示', '请选择一台终端!');
					return;
				}
				//id: deviceId @# locateType @# simcard @# vehicleNumber @# starttime @# endtime
				//15810740687@#1@#15810740687@#京K01011
				//alert(treeArr[0].id);
				var idArr = treeArr[0].id.split('@#');
				trackSearchDeviceid = idArr[0];
				trackSearchDevicelocateType = idArr[1];
				trackSearchDevicesimcard = idArr[2];
				trackSearchDevicevehicleNumber = idArr[3];
				trackSearchStartTime = idArr[4];
				trackSearchEndTime = idArr[5];
				trackSearchWeek = idArr[7];
				
				//开始日期
				var tmpstartdt = Ext.getCmp('startdttrack').getValue();
				//开始时间
				var tmpstarttf = Ext.getCmp('starttftrack').getValue();
				//结束日期
				var tmpenddt = Ext.getCmp('enddttrack').getValue();
				//结束时间
				var tmpendtf = Ext.getCmp('endtftrack').getValue();
				
				//比较日期
				if(tmpstartdt == ''||tmpstarttf == '' || tmpenddt == ''||tmpendtf == ''){
					Ext.MessageBox.alert('提示', '请选择开始时间和结束时间!');
					return;
				}
				
				var startday_ = tmpstartdt.getDay();
				if((Number(trackSearchWeek) & 1)!=1 && startday_ == 1){
					Ext.Msg.alert('提示', '周一为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 2)!=2 && startday_ == 2){
					Ext.Msg.alert('提示', '周二为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 4)!=4 && startday_ == 3){
					Ext.Msg.alert('提示', '周三为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 8)!=8 && startday_ == 4){
					Ext.Msg.alert('提示', '周四为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 16)!=16 && startday_ == 5){
					Ext.Msg.alert('提示', '周五为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 32)!=32 && startday_ == 6){
					Ext.Msg.alert('提示', '周六为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 64)!=64 && startday_ == 0){
					Ext.Msg.alert('提示', '周日为非工作日,请选择工作日!');
					return;
				}
				
				var endday_ = tmpenddt.getDay();
				if((Number(trackSearchWeek) & 1)!=1 && endday_ == 1){
					Ext.Msg.alert('提示', '周一为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 2)!=2 && endday_ == 2){
					Ext.Msg.alert('提示', '周二为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 4)!=4 && endday_ == 3){
					Ext.Msg.alert('提示', '周三为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 8)!=8 && endday_ == 4){
					Ext.Msg.alert('提示', '周四为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 16)!=16 && endday_ == 5){
					Ext.Msg.alert('提示', '周五为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 32)!=32 && endday_ == 6){
					Ext.Msg.alert('提示', '周六为非工作日,请选择工作日!');
					return;
				}else if((Number(trackSearchWeek) & 64)!=64 && endday_ == 0){
					Ext.Msg.alert('提示', '周日为非工作日,请选择工作日!');
					return;
				}
				
				if(tmpstarttf<trackSearchStartTime || tmpstarttf>trackSearchEndTime){
					Ext.Msg.alert('提示', '开始时间不在工作时间范围内!');
					return;
				}
				if(tmpendtf<trackSearchStartTime || tmpendtf>trackSearchEndTime){
					Ext.Msg.alert('提示', '结束时间不在工作时间范围内!');
					return;
				}
				
				if(tmpstartdt>tmpenddt){
					Ext.MessageBox.alert('提示', '开始时间要小于结束时间!');
					return;
				}else if(tmpstartdt.format('Y-m-d')==tmpenddt.format('Y-m-d')){
					if(tmpstarttf>=tmpendtf){
						Ext.MessageBox.alert('提示', '开始时间要小于结束时间!');
						return;
					}
				}

				var strDate1 = tmpstartdt.format('Y/m/d')+' '+tmpstarttf+':00';
				var strDate2 = tmpenddt.format('Y/m/d')+' '+tmpendtf+':00';
				//alert(strDate1);
				var   date1   =   Date.parse(strDate1);   
				//alert(date1);
				var   date2   =   Date.parse(strDate2);   
				
				var   newDate   =   DateAdd( "d ", -3, new Date());
				if(track_qd_flag && (newDate > tmpstartdt)){
					Ext.MessageBox.alert('提示', '只能查询最近三天的轨迹!');
					return;
				}
				
				//alert("strDate2与strDate1相差"+Math.ceil((date2-date1)/(24*60*60*1000))+"天")   
				if(Math.ceil((date2-date1)/(24*60*60*1000)) > 3){
					Ext.MessageBox.alert('提示', '只能查询三天内的轨迹回放!');
						return;
				}

				//add by liuhongxiao 2012-02-07
				filterCarStop = Ext.getCmp('fieldFL').collapsed;
				//filterCarSpacing = Ext.getCmp('spacing').getValue();
				filterCarInterval = Ext.getCmp('interval').getValue();
			    if(filterCarInterval!='' && filterCarInterval.search("^-?\\d+(\\.\\d+)?$")!=0){
			    	Ext.Msg.alert('提示', '间隔(分钟)请输入数字!');
			        return ;
			    }
				
				starttime=tmpstartdt.format('Y-m-d')+' '+tmpstarttf;
				endtime=tmpenddt.format('Y-m-d')+' '+tmpendtf;
				
				//trackSearchDeviceid = '15045412114';
				//Ext.getCmp('trackquerypanel').setTitle('轨迹查询中...');
				//Ext.getCmp('trackquerypanel').setIconClass('icon-trackqueryload');
				
				Ext.Msg.show({
			           msg: '正在查询 请稍等...',
			           progressText: '查询中...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
				trackSearchAjax(tmpstartdt.format('Y-m-d'),tmpstarttf+':00',tmpenddt.format('Y-m-d'),tmpendtf+':00','4');
				
			}
		}/*,{
			text: '重置',
			handler:function(){
				//开始日期
				var tmpstartdt = Ext.getCmp('startdttrack').setValue('');
				//开始时间
				var tmpstarttf = Ext.getCmp('starttftrack').setValue('');
				//结束日期
				var tmpenddt = Ext.getCmp('enddttrack').setValue('');
				//结束时间
				var tmpendtf = Ext.getCmp('endtftrack').setValue('');
				//过滤时间间隔
				var tmpfiltertime = Ext.getCmp('filtertime').setValue('');
				//过滤偏移大的点
				var tmpfilterdeviate = Ext.getCmp('filterdeviate').getValue().setValue('2');
				//道路纠偏
				var tmproadcorrect = Ext.getCmp('roadcorrect').getValue().setValue('2');           	
			}
		}*/];
		
	    UITrackSearchPanel1.superclass.initComponent.call(this);
	    
	}
});

function   DateAdd(interval,number,date) 
{ 
/* 
  *---------------   DateAdd(interval,number,date)   ----------------- 
  *   DateAdd(interval,number,date)   
  *   功能:实现VBScript的DateAdd功能. 
  *   参数:interval,字符串表达式，表示要添加的时间间隔. 
  *   参数:number,数值表达式，表示要添加的时间间隔的个数. 
  *   参数:date,时间对象. 
  *   返回:新的时间对象. 
  *   var   now   =   new   Date(); 
  *   var   newDate   =   DateAdd( "d ",5,now); 
  *   author:wanghr100(灰豆宝宝.net) 
  *   update:2004-5-28   11:46 
  *---------------   DateAdd(interval,number,date)   ----------------- 
  */ 
        switch(interval) 
        { 
                case   "y "   :   { 
                        date.setFullYear(date.getFullYear()+number); 
                        return   date; 
                        break; 
                } 
                case   "q "   :   { 
                        date.setMonth(date.getMonth()+number*3); 
                        return   date; 
                        break; 
                } 
                case   "m "   :   { 
                        date.setMonth(date.getMonth()+number); 
                        return   date; 
                        break; 
                } 
                case   "w "   :   { 
                        date.setDate(date.getDate()+number*7); 
                        return   date; 
                        break; 
                } 
                case   "d "   :   { 
                        date.setDate(date.getDate()+number); 
                        return   date; 
                        break; 
                } 
                case   "h "   :   { 
                        date.setHours(date.getHours()+number); 
                        return   date; 
                        break; 
                } 
                case   "m "   :   { 
                        date.setMinutes(date.getMinutes()+number); 
                        return   date; 
                        break; 
                } 
                case   "s "   :   { 
                        date.setSeconds(date.getSeconds()+number); 
                        return   date; 
                        break; 
                } 
                default   :   { 
                        date.setDate(d.getDate()+number); 
                        return   date; 
                        break; 
                } 
        } 
} 
