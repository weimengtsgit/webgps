/**
 * �켣�طŲ�ѯ���
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
	    this.items = [{fieldLabel: '��ʼʱ��',allowBlank :false,editable:false,id: 'startdttrack', width: 115, format:'Y-m-d', xtype: 'datefield',value: new Date() 
					    ,listeners: {
							'focus':function ( thisz ) {
								var treeArr = new Array();
								//ȡ�ù�ѡ�ն�
							    getTreeId(root,treeArr);
								if(treeArr.length!=1){
									Ext.Msg.alert('��ʾ', '��ѡ��һ̨�ն�!');
									return;
								}
								//id: deviceId @# locateType @# simcard @# vehicleNumber @# starttime @# endtime
								//15810740687@#1@#15810740687@#��K01011
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
					//ȡ�ù�ѡ�ն�
				    getTreeId(root,treeArr);
					if(treeArr.length!=1){
						Ext.Msg.alert('��ʾ', '��ѡ��һ̨�ն�!');
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
			{fieldLabel: '����ʱ��',allowBlank :false,editable:false, id: 'enddttrack',width: 115, format:'Y-m-d',xtype: 'datefield',value: new Date()
	    		,listeners: {
	    			'focus':function ( thisz ) {
    					var treeArr = new Array();
    					//ȡ�ù�ѡ�ն�
    				    getTreeId(root,treeArr);
    					if(treeArr.length!=1){
    						Ext.Msg.alert('��ʾ', '��ѡ��һ̨�ն�!');
    						return;
    					}
    					//id: deviceId @# locateType @# simcard @# vehicleNumber @# starttime @# endtime
    					//15810740687@#1@#15810740687@#��K01011
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
					//ȡ�ù�ѡ�ն�
				    getTreeId(root,treeArr);
					if(treeArr.length!=1){
						Ext.Msg.alert('��ʾ', '��ѡ��һ̨�ն�!');
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
			{fieldLabel: '���˼��(����)', id: 'filtertime', width: 100, xtype: 'textfield',value:10},
			{fieldLabel: '��������', id: 'filterstar', width: 100, xtype: 'textfield',value:4}];
	
		this.buttons = [{
			text: '��ѯ',
			handler:function(){
				//�����ն������ж��Ƿ���ʾ��ťadd 2010-11-23
				var vehiclebtn = Ext.getCmp('vbtn');
				if(trackSearchDevicelocateType == '1'){
					vehiclebtn.enable(); 
					vehiclebtn.setVisible(true);
				}else{
					vehiclebtn.disable();
					vehiclebtn.setVisible(false);
				}
				
				var treeArr = new Array();
				//ȡ�ù�ѡ�ն�
			    getTreeId(root,treeArr);
				if(treeArr.length!=1){
					Ext.Msg.alert('��ʾ', '��ѡ��һ̨�ն�!');
					return;
				}
				//id: deviceId @# locateType @# simcard @# vehicleNumber @# starttime @# endtime
				//15810740687@#1@#15810740687@#��K01011
				//alert(treeArr[0].id);
				var idArr = treeArr[0].id.split('@#');
				trackSearchDeviceid = idArr[0];
				trackSearchDevicelocateType = idArr[1];
				trackSearchDevicesimcard = idArr[2];
				trackSearchDevicevehicleNumber = idArr[3];
				trackSearchStartTime = idArr[4];
				trackSearchEndTime = idArr[5];
				trackSearchWeek = idArr[7];
				
				//��ʼ����
				var tmpstartdt = Ext.getCmp('startdttrack').getValue();
				//��ʼʱ��
				var tmpstarttf = Ext.getCmp('starttftrack').getValue();
				//��������
				var tmpenddt = Ext.getCmp('enddttrack').getValue();
				//����ʱ��
				var tmpendtf = Ext.getCmp('endtftrack').getValue();
				//����ʱ����
				var tmpfiltertime = Ext.getCmp('filtertime').getValue();
				//��������
				var tmpfilterstar = Ext.getCmp('filterstar').getValue();
				
				//�Ƚ�����
				if(tmpstartdt == ''||tmpstarttf == '' || tmpenddt == ''||tmpendtf == ''){
					Ext.MessageBox.alert('��ʾ', '��ѡ��ʼʱ��ͽ���ʱ��!');
					return;
				}
				
				var startday_ = tmpstartdt.getDay();
				if((Number(trackSearchWeek) & 1)!=1 && startday_ == 1){
					Ext.Msg.alert('��ʾ', '��һΪ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 2)!=2 && startday_ == 2){
					Ext.Msg.alert('��ʾ', '�ܶ�Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 4)!=4 && startday_ == 3){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 8)!=8 && startday_ == 4){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 16)!=16 && startday_ == 5){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 32)!=32 && startday_ == 6){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 64)!=64 && startday_ == 0){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}
				
				var endday_ = tmpenddt.getDay();
				if((Number(trackSearchWeek) & 1)!=1 && endday_ == 1){
					Ext.Msg.alert('��ʾ', '��һΪ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 2)!=2 && endday_ == 2){
					Ext.Msg.alert('��ʾ', '�ܶ�Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 4)!=4 && endday_ == 3){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 8)!=8 && endday_ == 4){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 16)!=16 && endday_ == 5){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 32)!=32 && endday_ == 6){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 64)!=64 && endday_ == 0){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}
				
				if(tmpstarttf<trackSearchStartTime || tmpstarttf>trackSearchEndTime){
					Ext.Msg.alert('��ʾ', '��ʼʱ�䲻�ڹ���ʱ�䷶Χ��!');
					return;
				}
				if(tmpendtf<trackSearchStartTime || tmpendtf>trackSearchEndTime){
					Ext.Msg.alert('��ʾ', '����ʱ�䲻�ڹ���ʱ�䷶Χ��!');
					return;
				}
				
				if(tmpstartdt>tmpenddt){
					Ext.MessageBox.alert('��ʾ', '��ʼʱ��ҪС�ڽ���ʱ��!');
					return;
				}else if(tmpstartdt.format('Y-m-d')==tmpenddt.format('Y-m-d')){
					if(tmpstarttf>=tmpendtf){
						Ext.MessageBox.alert('��ʾ', '��ʼʱ��ҪС�ڽ���ʱ��!');
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
					Ext.MessageBox.alert('��ʾ', 'ֻ�ܲ�ѯ�������Ĺ켣!');
					return;
				}
				//alert("strDate2��strDate1���"+Math.ceil((date2-date1)/(24*60*60*1000))+"��")   
				if(Math.ceil((date2-date1)/(24*60*60*1000)) > 3){
					Ext.MessageBox.alert('��ʾ', 'ֻ�ܲ�ѯ�����ڵĹ켣�ط�!');
						return;
				}
				
				//add by liuhongxiao 2011-12-02
				if(tmpfilterstar == ''){
					Ext.Msg.alert('��ʾ', '������������Ϊ��!');
					return;
				}else if(!/^[\d]+$/.test(tmpfilterstar) || Number(tmpfilterstar)<0 || Number(tmpfilterstar)>13){
					Ext.Msg.alert('��ʾ', '������������Ϊ0~13֮�������!');
					return;
				}
				
				filtertimeparam = tmpfiltertime;
				starttime=tmpstartdt.format('Y-m-d')+' '+tmpstarttf;
				endtime=tmpenddt.format('Y-m-d')+' '+tmpendtf;
				
				//trackSearchDeviceid = '15045412114';
				//Ext.getCmp('trackquerypanel').setTitle('�켣��ѯ��...');
				//Ext.getCmp('trackquerypanel').setIconClass('icon-trackqueryload');
				
				Ext.Msg.show({
			           msg: '���ڲ�ѯ ���Ե�...',
			           progressText: '��ѯ��...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
				trackSearchAjax(tmpstartdt.format('Y-m-d'),tmpstarttf+':00',tmpenddt.format('Y-m-d'),tmpendtf+':00',tmpfilterstar);
				
			}
		}/*,{
			text: '����',
			handler:function(){
				//��ʼ����
				var tmpstartdt = Ext.getCmp('startdttrack').setValue('');
				//��ʼʱ��
				var tmpstarttf = Ext.getCmp('starttftrack').setValue('');
				//��������
				var tmpenddt = Ext.getCmp('enddttrack').setValue('');
				//����ʱ��
				var tmpendtf = Ext.getCmp('endtftrack').setValue('');
				//����ʱ����
				var tmpfiltertime = Ext.getCmp('filtertime').setValue('');
				//����ƫ�ƴ�ĵ�
				var tmpfilterdeviate = Ext.getCmp('filterdeviate').getValue().setValue('2');
				//��·��ƫ
				var tmproadcorrect = Ext.getCmp('roadcorrect').getValue().setValue('2');           	
			}
		}*/];
		
	    UITrackSearchPanel.superclass.initComponent.call(this);
	    
	}
});

/**
 * �켣�طŲ�ѯ���
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
	    this.items = [{fieldLabel: '��ʼʱ��',allowBlank :false,editable:false,id: 'startdttrack', width: 115, format:'Y-m-d', xtype: 'datefield',value: new Date() 
					    ,listeners: {
							'focus':function ( thisz ) {
								var treeArr = new Array();
								//ȡ�ù�ѡ�ն�
							    getTreeId(root,treeArr);
								if(treeArr.length!=1){
									Ext.Msg.alert('��ʾ', '��ѡ��һ̨�ն�!');
									return;
								}
								//id: deviceId @# locateType @# simcard @# vehicleNumber @# starttime @# endtime
								//15810740687@#1@#15810740687@#��K01011
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
					//ȡ�ù�ѡ�ն�
				    getTreeId(root,treeArr);
					if(treeArr.length!=1){
						Ext.Msg.alert('��ʾ', '��ѡ��һ̨�ն�!');
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
	    	{fieldLabel: '����ʱ��',allowBlank :false,editable:false, id: 'enddttrack',width: 115, format:'Y-m-d',xtype: 'datefield',value: new Date()
	    		,listeners: {
	    			'focus':function ( thisz ) {
    					var treeArr = new Array();
    					//ȡ�ù�ѡ�ն�
    				    getTreeId(root,treeArr);
    					if(treeArr.length!=1){
    						Ext.Msg.alert('��ʾ', '��ѡ��һ̨�ն�!');
    						return;
    					}
    					//id: deviceId @# locateType @# simcard @# vehicleNumber @# starttime @# endtime
    					//15810740687@#1@#15810740687@#��K01011
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
					//ȡ�ù�ѡ�ն�
				    getTreeId(root,treeArr);
					if(treeArr.length!=1){
						Ext.Msg.alert('��ʾ', '��ѡ��һ̨�ն�!');
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
            title: 'ͣ������',
            autoHeight:true,
            defaults: {width: 100},
            defaultType: 'textfield',
            collapsed: true,
            items :[/**{fieldLabel: '���(��)',
                    xtype: 'combo',
                    width: 100,
                    name: 'first',
                	id : 'spacing',
     				enableKeyEvents : true,
     				grow : true,
     				valueField : "id",
     				displayField : "name",
     				mode : 'local',// �������ڱ���
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
                    fieldLabel: '���(����)',
                    id:'interval',
                    width: 100,
                    value:3,
                    name: 'last'
                }
            ]}];
	
		this.buttons = [{
			text: '��ѯ',
			handler:function(){
				//�����ն������ж��Ƿ���ʾ��ťadd 2010-11-23
				var vehiclebtn = Ext.getCmp('vbtn');
				if(trackSearchDevicelocateType == '1'){
					vehiclebtn.enable(); 
					vehiclebtn.setVisible(true);
				}else{
					vehiclebtn.disable();
					vehiclebtn.setVisible(false);
				}
				
				var treeArr = new Array();
				//ȡ�ù�ѡ�ն�
			    getTreeId(root,treeArr);
				if(treeArr.length!=1){
					Ext.Msg.alert('��ʾ', '��ѡ��һ̨�ն�!');
					return;
				}
				//id: deviceId @# locateType @# simcard @# vehicleNumber @# starttime @# endtime
				//15810740687@#1@#15810740687@#��K01011
				//alert(treeArr[0].id);
				var idArr = treeArr[0].id.split('@#');
				trackSearchDeviceid = idArr[0];
				trackSearchDevicelocateType = idArr[1];
				trackSearchDevicesimcard = idArr[2];
				trackSearchDevicevehicleNumber = idArr[3];
				trackSearchStartTime = idArr[4];
				trackSearchEndTime = idArr[5];
				trackSearchWeek = idArr[7];
				
				//��ʼ����
				var tmpstartdt = Ext.getCmp('startdttrack').getValue();
				//��ʼʱ��
				var tmpstarttf = Ext.getCmp('starttftrack').getValue();
				//��������
				var tmpenddt = Ext.getCmp('enddttrack').getValue();
				//����ʱ��
				var tmpendtf = Ext.getCmp('endtftrack').getValue();
				
				//�Ƚ�����
				if(tmpstartdt == ''||tmpstarttf == '' || tmpenddt == ''||tmpendtf == ''){
					Ext.MessageBox.alert('��ʾ', '��ѡ��ʼʱ��ͽ���ʱ��!');
					return;
				}
				
				var startday_ = tmpstartdt.getDay();
				if((Number(trackSearchWeek) & 1)!=1 && startday_ == 1){
					Ext.Msg.alert('��ʾ', '��һΪ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 2)!=2 && startday_ == 2){
					Ext.Msg.alert('��ʾ', '�ܶ�Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 4)!=4 && startday_ == 3){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 8)!=8 && startday_ == 4){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 16)!=16 && startday_ == 5){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 32)!=32 && startday_ == 6){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 64)!=64 && startday_ == 0){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}
				
				var endday_ = tmpenddt.getDay();
				if((Number(trackSearchWeek) & 1)!=1 && endday_ == 1){
					Ext.Msg.alert('��ʾ', '��һΪ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 2)!=2 && endday_ == 2){
					Ext.Msg.alert('��ʾ', '�ܶ�Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 4)!=4 && endday_ == 3){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 8)!=8 && endday_ == 4){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 16)!=16 && endday_ == 5){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 32)!=32 && endday_ == 6){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}else if((Number(trackSearchWeek) & 64)!=64 && endday_ == 0){
					Ext.Msg.alert('��ʾ', '����Ϊ�ǹ�����,��ѡ������!');
					return;
				}
				
				if(tmpstarttf<trackSearchStartTime || tmpstarttf>trackSearchEndTime){
					Ext.Msg.alert('��ʾ', '��ʼʱ�䲻�ڹ���ʱ�䷶Χ��!');
					return;
				}
				if(tmpendtf<trackSearchStartTime || tmpendtf>trackSearchEndTime){
					Ext.Msg.alert('��ʾ', '����ʱ�䲻�ڹ���ʱ�䷶Χ��!');
					return;
				}
				
				if(tmpstartdt>tmpenddt){
					Ext.MessageBox.alert('��ʾ', '��ʼʱ��ҪС�ڽ���ʱ��!');
					return;
				}else if(tmpstartdt.format('Y-m-d')==tmpenddt.format('Y-m-d')){
					if(tmpstarttf>=tmpendtf){
						Ext.MessageBox.alert('��ʾ', '��ʼʱ��ҪС�ڽ���ʱ��!');
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
					Ext.MessageBox.alert('��ʾ', 'ֻ�ܲ�ѯ�������Ĺ켣!');
					return;
				}
				
				//alert("strDate2��strDate1���"+Math.ceil((date2-date1)/(24*60*60*1000))+"��")   
				if(Math.ceil((date2-date1)/(24*60*60*1000)) > 3){
					Ext.MessageBox.alert('��ʾ', 'ֻ�ܲ�ѯ�����ڵĹ켣�ط�!');
						return;
				}

				//add by liuhongxiao 2012-02-07
				filterCarStop = Ext.getCmp('fieldFL').collapsed;
				//filterCarSpacing = Ext.getCmp('spacing').getValue();
				filterCarInterval = Ext.getCmp('interval').getValue();
			    if(filterCarInterval!='' && filterCarInterval.search("^-?\\d+(\\.\\d+)?$")!=0){
			    	Ext.Msg.alert('��ʾ', '���(����)����������!');
			        return ;
			    }
				
				starttime=tmpstartdt.format('Y-m-d')+' '+tmpstarttf;
				endtime=tmpenddt.format('Y-m-d')+' '+tmpendtf;
				
				//trackSearchDeviceid = '15045412114';
				//Ext.getCmp('trackquerypanel').setTitle('�켣��ѯ��...');
				//Ext.getCmp('trackquerypanel').setIconClass('icon-trackqueryload');
				
				Ext.Msg.show({
			           msg: '���ڲ�ѯ ���Ե�...',
			           progressText: '��ѯ��...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
				trackSearchAjax(tmpstartdt.format('Y-m-d'),tmpstarttf+':00',tmpenddt.format('Y-m-d'),tmpendtf+':00','4');
				
			}
		}/*,{
			text: '����',
			handler:function(){
				//��ʼ����
				var tmpstartdt = Ext.getCmp('startdttrack').setValue('');
				//��ʼʱ��
				var tmpstarttf = Ext.getCmp('starttftrack').setValue('');
				//��������
				var tmpenddt = Ext.getCmp('enddttrack').setValue('');
				//����ʱ��
				var tmpendtf = Ext.getCmp('endtftrack').setValue('');
				//����ʱ����
				var tmpfiltertime = Ext.getCmp('filtertime').setValue('');
				//����ƫ�ƴ�ĵ�
				var tmpfilterdeviate = Ext.getCmp('filterdeviate').getValue().setValue('2');
				//��·��ƫ
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
  *   ����:ʵ��VBScript��DateAdd����. 
  *   ����:interval,�ַ������ʽ����ʾҪ��ӵ�ʱ����. 
  *   ����:number,��ֵ���ʽ����ʾҪ��ӵ�ʱ�����ĸ���. 
  *   ����:date,ʱ�����. 
  *   ����:�µ�ʱ�����. 
  *   var   now   =   new   Date(); 
  *   var   newDate   =   DateAdd( "d ",5,now); 
  *   author:wanghr100(�Ҷ�����.net) 
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
