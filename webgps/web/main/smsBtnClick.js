//��������
var smswin;
//��������
function sms_btn_click(){
        	//����������ģ��ر�ʵʱ׷��
        	StopRealtimeTrack();
        	//�ر�ʵʱ���ٳ������з���
    		StopCarCurrentDirection();
    		var smsPanel = new Ext.Panel({
    			id: 'smsCenterPanel',
    			title: '��������',
    			layout: 'fit',
    			//region: 'center',
                //margins:'3 3 3 0',
                defaults:{autoScroll:true},
    			html: '<iframe src="sms/sms.jsp" style="width:100%; height: 100%;" SCROLLING="no" frameborder="0"></iframe>'
    		});

            if(!smswin){
            	smswin = new Ext.Window({
    	            title: '��Ϣ����',
    	        	layout:'fit',
    	            closable:true,
    	            width:900,
    	            height:500,
    	            closeAction:'hide',
    	            maximizable: true,
					minimizable: true,
    	            plain:true,
    	            items: [{
                    	xtype: 'tabpanel',
                        autoTabs:true,
                        activeTab:0,
                        deferredRender:false,
                        border:false,
                        id: 'MsgTabPanel',
                        items:[smsPanel,{
                        	xtype: 'tabpanel',
                        	title: '��־����',
                            autoTabs:true,
                            activeTab:0,
                            deferredRender:false,
                            border:false,
                            id: 'DiaryTabPanel',
                            items:[{
                            	xtype: 'panel',
                            	id: 'DiaryEditPanel',
                                title: '��־¼��',
                                layout: 'card',
                                //width: 1500,
                                listeners: {
                                	activate : function( p ) {
                                		if(!p.getComponent(0)){
                                			p.add(DEpanel);
                                			p.add(DiaryMarkPanel);
        									storeLoad(DiaryEditStore, 0, 15, '', diaryEditSearchValue, diaryEditStartTime, diaryEditEndTime, '', false);
        				                    p.getLayout().setActiveItem(0);
        				                    p.doLayout();
        								}
                                	}
                                }
                            },{
                            	xtype: 'panel',
                            	id: 'DiaryRemarkPanel',
                                title: '��־��ע',
                                layout: 'card',
                                //width: 1500,
                                listeners: {
                                	activate : function( p ) {
                                		if(!p.getComponent(0)){
                                			p.add(DRpanel);
                                			p.add(DiaryRemarkForm);
        				                    p.getLayout().setActiveItem(0);
        				                    p.doLayout();
        				                    DRTreePanel.expandPath('/-100/-101/');
                                		}
                                	}
                                }
                            }]
                        }]
                    }],
    	            listeners:{
    	            	'close':function (p){
    	            		//smswin = null;
    	            	}
    	            }
    	        });
            }

			if(sms_center != '' && sms_center.length > 0){
				//Ext.getCmp('ReportTabPanel').activate('VisitStatPanel');
			}else {
				Ext.getCmp('MsgTabPanel').remove('smsCenterPanel');
			}
			if(diary_center != '' && diary_center.length > 0){
				//Ext.getCmp('ReportTabPanel').activate('VisitStatPanel');
				if(diary_add != '' && diary_add.length > 0){
					//Ext.getCmp('ReportTabPanel').activate('VisitStatPanel');
				}else {
					Ext.getCmp('DiaryTabPanel').remove('DiaryEditPanel');
				}
				if(diary_remark != '' && diary_remark.length > 0){
					//Ext.getCmp('ReportTabPanel').activate('VisitStatPanel');
				}else {
					Ext.getCmp('DiaryTabPanel').remove('DiaryRemarkPanel');
				}
			}else {
				Ext.getCmp('MsgTabPanel').remove('DiaryTabPanel');
			}
			
	        smswin.show();
        }
    