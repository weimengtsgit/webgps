/**
 * 轨迹回放控制面板
 * @class UITrackControlPanel
 * @extends Ext.Panel
 */
UITrackControlPanel = Ext.extend(Ext.Panel, {
	//title: '回放控制',
	id:'trackControlPanel_id',
	//anchor: '100% 47%',
	//layout: 'fit',
	initComponent: function(){
    this.items = [{xtype: 'form',bodyStyle:'padding:5px 10px 0',width: 300,labelWidth:100,
					    items: [
					    	/*{ layout:'column', border:false,
					        items:[{ columnWidth:.3,boxLabel: '锁定', width: 80, name: 'roadcorrect',
					            handler :function(){
					            	if(this.checked){
					            		mapViewModal = true;
					            	}
					            },
					            xtype: 'radio'
					        }]
					    },{layout:'column', border:false,
					        items:[{columnWidth:1,boxLabel: '自由视野',width: 80,name: 'roadcorrect',checked: true,
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
				                {boxLabel: '锁定', name: 'roadcorrect', inputValue: 1,
					                handler :function(){
						            	if(this.checked){
						            		mapViewModal = true;
						            	}
						            }
				                },
				                {boxLabel: '自由视野', name: 'roadcorrect', inputValue: 2, checked: true,
					                handler :function(){
						            	if(this.checked){
						            		mapViewModal = false;
						            	}
						            }
				                }
				            ]
				        },{id:'intervalpointcombo',xtype: 'combo',fieldLabel:'回放间隔(点)',editable:false, width:80,displayField:'name',store: new Ext.data.ArrayStore({fields: ['id', 'name'],data : [[1,'1X'],[5,'5X'],[10,'10X'],[15,'15X'],[20,'20X']]}),displayField:'name',valueField: 'id',typeAhead: true, mode: 'local', forceSelection: true, triggerAction: 'all',value:'1', selectOnFocus:true
						},{xtype: 'label', fieldLabel:'回放速度(秒)', id:'playspeedlabel',text :'1'
						},new Ext.Slider({ width: 214,increment: 1,minValue: 1,maxValue: 4,
					        listeners:{'changecomplete':function(slider, newValue){
					        	//改变播放速度
					        	var tmpplayspeedlabel = Ext.getCmp('playspeedlabel');
					        	if(newValue != 1){
					        		tmpplayspeedlabel.setText((newValue-1)*5);
					        		//设置播放
					        		refreshTime = (newValue-1)*5;
					        	}else{
					        		tmpplayspeedlabel.setText(1);
					        		//设置播放
					        		refreshTime = 1;
					        	}
					        	if(currentstate == 1){
					        		play();
					        	}
					        }}
					    }),{xtype: 'label', fieldLabel:'回放进度(点)',id:'playtempolabel',text :'0'
						},new Ext.Slider({id:'playtemposlider',width: 214,increment: 1,minValue: 1, maxValue: 1,
					        listeners:{'changecomplete':function(slider, newValue){
					        	//var map = document.getElementById('mapifr').contentWindow;
					        	
					        	//改变播放进度,更改'回放进度'label
					        	var tmpplaytempolabel = Ext.getCmp('playtempolabel');
					        	tmpplaytempolabel.setText(newValue);
					        	//停止播放,改变按钮显示图片
								resetimgsrc();
								var tmp = Ext.getCmp('media_controls_pause');
								tmp.setIconClass('media_controls_dark_pause');
								pause();
								//画当前进度点
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
		text: '车辆信息',
		id:'vbtn',
		handler:function(){
    	
 		//判断终端类型
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
	 			//显示中心列表
	 			msgpanel.setVisible(true);
	 			msgpanel.setActiveTab(gridVehicle);//设置gridVehicle为当前活动页
	 			msgpanel.unhideTabStripItem(gridVehicle);//取消隐藏  gridVehicle tab页
	 			msgpanel.hideTabStripItem(gridmsg);//隐藏  gridmsg  tab页
	 			msgpanel.hideTabStripItem(gridhis);//隐藏  gridhis  tab页
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
// 		}
		}
	},{
		text: '返回',
		handler:function(){
		//隐藏中心列表
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

