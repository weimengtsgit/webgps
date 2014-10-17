/**
 * �켣�طſ������
 * @class UITrackControlPanel
 * @extends Ext.Panel
 */
UITrackControlPanel = Ext.extend(Ext.Panel, {
	//title: '�طſ���',
	id:'trackControlPanel_id',
	//anchor: '100% 47%',
	//layout: 'fit',
	initComponent: function(){
    this.items = [{xtype: 'form',bodyStyle:'padding:5px 10px 0',width: 300,labelWidth:100,
					    items: [
					    	/*{ layout:'column', border:false,
					        items:[{ columnWidth:.3,boxLabel: '����', width: 80, name: 'roadcorrect',
					            handler :function(){
					            	if(this.checked){
					            		mapViewModal = true;
					            	}
					            },
					            xtype: 'radio'
					        }]
					    },{layout:'column', border:false,
					        items:[{columnWidth:1,boxLabel: '������Ұ',width: 80,name: 'roadcorrect',checked: true,
					            handler :function(){
					            	if(this.checked){
					            		mapViewModal = false;
					            	}
					            },
					            xtype: 'radio'
					        }]
					    },*/
					    {
				            xtype: 'radiogroup',
				            hideLabel: true,
				            //fieldLabel: 'Auto Layout',
				            items: [
				                {boxLabel: '����', name: 'roadcorrect', inputValue: 1,
					                handler :function(){
						            	if(this.checked){
						            		mapViewModal = true;
						            	}
						            }
				                },
				                {boxLabel: '������Ұ', name: 'roadcorrect', inputValue: 2, checked: true,
					                handler :function(){
						            	if(this.checked){
						            		mapViewModal = false;
						            	}
						            }
				                }
				            ]
				        },{id:'intervalpointcombo',xtype: 'combo',fieldLabel:'�طż��(��)',editable:false, width:80,displayField:'name',store: new Ext.data.ArrayStore({fields: ['id', 'name'],data : [[1,'1X'],[5,'5X'],[10,'10X'],[15,'15X'],[20,'20X']]}),displayField:'name',valueField: 'id',typeAhead: true, mode: 'local', forceSelection: true, triggerAction: 'all',value:'1', selectOnFocus:true
						},{xtype: 'label', fieldLabel:'�ط��ٶ�(��)', id:'playspeedlabel',text :'1'
						},new Ext.Slider({ width: 214,increment: 1,minValue: 1,maxValue: 4,
					        listeners:{'changecomplete':function(slider, newValue){
					        	//�ı䲥���ٶ�
					        	var tmpplayspeedlabel = Ext.getCmp('playspeedlabel');
					        	if(newValue != 1){
					        		tmpplayspeedlabel.setText((newValue-1)*5);
					        		//���ò���
					        		refreshTime = (newValue-1)*5;
					        	}else{
					        		tmpplayspeedlabel.setText(1);
					        		//���ò���
					        		refreshTime = 1;
					        	}
					        	if(currentstate == 1){
					        		play();
					        	}
					        }}
					    }),{xtype: 'label', fieldLabel:'�طŽ���(��)',id:'playtempolabel',text :'0'
						},new Ext.Slider({id:'playtemposlider',width: 214,increment: 1,minValue: 1, maxValue: 1,
					        listeners:{'changecomplete':function(slider, newValue){
					        	//var map = document.getElementById('mapifr').contentWindow;
					        	
					        	//�ı䲥�Ž���,����'�طŽ���'label
					        	var tmpplaytempolabel = Ext.getCmp('playtempolabel');
					        	tmpplaytempolabel.setText(newValue);
					        	//ֹͣ����,�ı䰴ť��ʾͼƬ
								resetimgsrc();
								var tmp = Ext.getCmp('media_controls_pause');
								tmp.setIconClass('media_controls_dark_pause');
								pause();
								//����ǰ���ȵ�
								drawtrackpoint(newValue - 1);
								sliderposition = newValue - 1;
								
					        }}
					    }),new Ext.Panel({layout:'table',layoutConfig: { columns: 9},
							items: [new Ext.Button({id:'media_controls_first',iconCls: 'media_controls_light_first',value:'0',
									handler:function(){
										resetimgsrc();
										this.setIconClass('media_controls_dark_first');
										previousTrack();
									}
								}),new Ext.Spacer({width:10
								}),new Ext.Button({id:'media_controls_play',iconCls: 'media_controls_light_play',
									handler:function(){
										resetimgsrc();
										this.setIconClass('media_controls_dark_play');
										play();
									}
								}),new Ext.Spacer({width:10
								}),new Ext.Button({id:'media_controls_pause',iconCls: 'media_controls_light_pause',
									handler:function(){
										resetimgsrc();
										this.setIconClass('media_controls_dark_pause');
										pause();
									}
								}),new Ext.Spacer({width:10
								}),new Ext.Button({id:'media_controls_stop',iconCls: 'media_controls_light_stop',
									handler:function(){
										resetimgsrc();
										this.setIconClass('media_controls_dark_stop');
										stop();
									}
								}),new Ext.Spacer({width:10
								}),new Ext.Button({id:'media_controls_last',iconCls: 'media_controls_light_last',
									handler:function(){
										resetimgsrc();
										this.setIconClass('media_controls_dark_last');
										nextTrack();
									}
								})
							]
						})
						]
					}];
    this.buttons = [{
		text: '������Ϣ',
		id:'vbtn',
		handler:function(){
    	
 		//�ж��ն�����
// 		if(terminalType=="1"){
 			var termId=trackSearchDeviceid;
 			var scrollheight=document.documentElement.scrollHeight;
	 		if(!alarmbottonflag){
	 			if(termId!=temp){
	 				storeVehicle.proxy = new Ext.data.HttpProxy( {
		 				 url:path+'/locate/locate.do?method=listTrack&deviceId='+ trackSearchDeviceid+"&startTime="+starttime+"&endTime="+endtime
		 			});
	 				storeVehicle.load({params:{start:0, limit:20,startTime:starttime,endTime:endtime}});
	 			}
	 			//��ʾ�����б�
	 			msgpanel.setVisible(true);
	 			msgpanel.setActiveTab(gridVehicle);//����gridVehicleΪ��ǰ�ҳ
	 			msgpanel.unhideTabStripItem(gridVehicle);//ȡ������  gridVehicle tabҳ
	 			msgpanel.hideTabStripItem(gridmsg);//����  gridmsg  tabҳ
	 			msgpanel.hideTabStripItem(gridhis);//����  gridhis  tabҳ
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
// 		}
		}
	},{
		text: '����',
		handler:function(){
		//���������б�
			msgpanel.setVisible(false);
		    if(map.fullmapflag){
				grid.setHeight(scrollheight);
			}else{
			    grid.setHeight(scrollheight-97);
			}
		    alarmbottonflag = false;
		    
			var tmpCardPanel = Ext.getCmp('trackquerypanel');
			tmpCardPanel.layout.setActiveItem(0);
			pause();
		}
	}];
    UITrackControlPanel.superclass.initComponent.call(this);
   }

  });

