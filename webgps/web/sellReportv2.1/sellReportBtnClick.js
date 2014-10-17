
//���۱�����
var sellreportwin;
//���۱���
function sell_report_btn_click(needDeviceIds){
	//����������۱����ر�ʵʱ׷��
	StopRealtimeTrack();
	//�ر�ʵʱ���ٳ������з���
	StopCarCurrentDirection();
	if(reportwin){
		reportwin.hide();//����Ա������
	}
	var treeArr = new Array();
    getTreeId(root,treeArr);
	if(treeArr.length<=0 && needDeviceIds){
		Ext.Msg.alert(main.tips, main.select_terminal);
		return;
	}
	if(!sellreportwin){
		sellreportwin = new Ext.Window({
	    	layout:'fit',
	        width:800,
	        height:500,
	        closeAction:'hide',
			maximizable: true,
	        plain: true,
	        items: [{
	        	xtype: 'tabpanel',
	            autoTabs:true,
	            activeTab:0,
	            enableTabScroll:true,
	            deferredRender:false,
	            border:false,
	            id: 'SellReportTabPanel',
	            items:[{
	            	xtype: 'panel',
	            	id: 'SignBillReportPanel',
	                title: 'ǩ�����',
	                layout: 'accordion',
	                hideCollapseTool: true,
	                titleCollapse: true,
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(SignBillReportGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'SignBillDetailPanel',
	                title: 'ǩ������ϸ',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(SignBillDetailGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'CashReportPanel',
	                title: '�ؿ���',
	                layout: 'accordion',
	                hideCollapseTool: true,
	                titleCollapse: true,
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CashReportGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'CashDetailPanel',
	                title: '�ؿ����ϸ',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CashDetailGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'CostReportPanel',
	                title: '���ñ���',
	                layout: 'accordion',
	                hideCollapseTool: true,
	                titleCollapse: true,
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CostReportGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'CostDetailPanel',
	                title: '������ϸ',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CostDetailGrid); p.doLayout(); } } }
	            },
	            // add by renxianliang 2013-7-5 for xinhuamai
	            {
	            	xtype: 'panel',
	            	id: 'CostDetailGrid_xinhuamaiPanel',
	                title: '������ϸ',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CostDetailGrid_xinhuamai); p.doLayout(); } } }
	            }
	            //add by 2012-12-11  �ϱ�
	            ,{
	            	xtype: 'panel',
	            	id: 'EnquiriesPanel',
	                title: '�����ϱ�',
	                layout: 'fit',
//	                hideCollapseTool: true,
//	                titleCollapse: true,
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CashDetailEnquiriesGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'PromotionPanel',
	                title: '�����ϱ�',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(PromotionId); p.doLayout(); } } }
	            }]
	        }]
	    });
	}
		var flag = false;
        //ǩ�����
        if(signBill_report != '' && signBill_report.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('SignBillReportPanel');
        }
        //ǩ������ϸ
        if(signBill_detail != '' && signBill_detail.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('SignBillDetailPanel');
        }
        //�ؿ���
        if(cash_report != '' && cash_report.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('CashReportPanel');
        }
        //�ؿ����ϸ
        if(cash_detail != '' && cash_detail.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('CashDetailPanel');
        }
        //���ñ���
        if(cost_report != '' && cost_report.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('CostReportPanel');
        }
        //������ϸ
        if(cost_detail != '' && cost_detail.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('CostDetailPanel');
        }
        //add by renxianliang 2013-7-5 for xinhuamai
        //�»������Ŷ������������ϸ
        if(cost_detail_xinhuamai != '' && cost_detail_xinhuamai.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('CostDetailGrid_xinhuamaiPanel');
        }
        //add by 2012-12-11  �����ϱ�
        if(sales_detail != '' && sales_detail.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('EnquiriesPanel');
        }
        //�����ϱ�
        if(promotion_detail != '' && promotion_detail.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('PromotionPanel');
        }

        var scrollheight=document.documentElement.scrollHeight;
        var scrollwidth=document.documentElement.scrollWidth; 
        sellreportwin.setSize(scrollwidth - 200, scrollheight - 85);
        sellreportwin.setPosition(200, 85);
        sellreportwin.show();
        
}

function initReportTimeScope(comboBoxReportYear_, comboBoxReportNum_){
	var dataYear_ = [[ targetTemplateType_Year, targetTemplateType_Year+"" ]];
	comboBoxReportYear_.getStore().loadData(dataYear_);
	comboBoxReportYear_.setValue(targetTemplateType_Year+"");
	var dataNum_ = [];
	for(var i = 1;i <= targetTemplateType_Num ;i++){
		dataNum_.push([i, i+""]);
	}
	comboBoxReportNum_.getStore().loadData(dataNum_);
	comboBoxReportNum_.setValue(targetTemplateType_Num+"");
}
