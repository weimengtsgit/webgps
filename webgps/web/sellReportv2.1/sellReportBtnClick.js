
//销售报表窗口
var sellreportwin;
//销售报表
function sell_report_btn_click(needDeviceIds){
	//点击报表销售报表，关闭实时追踪
	StopRealtimeTrack();
	//关闭实时跟踪车辆运行方向
	StopCarCurrentDirection();
	if(reportwin){
		reportwin.hide();//隐藏员工报表
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
	                title: '签单额报表',
	                layout: 'accordion',
	                hideCollapseTool: true,
	                titleCollapse: true,
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(SignBillReportGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'SignBillDetailPanel',
	                title: '签单额明细',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(SignBillDetailGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'CashReportPanel',
	                title: '回款额报表',
	                layout: 'accordion',
	                hideCollapseTool: true,
	                titleCollapse: true,
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CashReportGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'CashDetailPanel',
	                title: '回款额明细',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CashDetailGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'CostReportPanel',
	                title: '费用报表',
	                layout: 'accordion',
	                hideCollapseTool: true,
	                titleCollapse: true,
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CostReportGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'CostDetailPanel',
	                title: '费用明细',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CostDetailGrid); p.doLayout(); } } }
	            },
	            // add by renxianliang 2013-7-5 for xinhuamai
	            {
	            	xtype: 'panel',
	            	id: 'CostDetailGrid_xinhuamaiPanel',
	                title: '费用明细',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CostDetailGrid_xinhuamai); p.doLayout(); } } }
	            }
	            //add by 2012-12-11  上报
	            ,{
	            	xtype: 'panel',
	            	id: 'EnquiriesPanel',
	                title: '销售上报',
	                layout: 'fit',
//	                hideCollapseTool: true,
//	                titleCollapse: true,
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(CashDetailEnquiriesGrid); p.doLayout(); } } }
	            },{
	            	xtype: 'panel',
	            	id: 'PromotionPanel',
	                title: '促销上报',
	                layout: 'fit',
	                listeners: { activate : function( p ) { if(!p.getComponent(0)){ p.add(PromotionId); p.doLayout(); } } }
	            }]
	        }]
	    });
	}
		var flag = false;
        //签单额报表
        if(signBill_report != '' && signBill_report.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('SignBillReportPanel');
        }
        //签单额明细
        if(signBill_detail != '' && signBill_detail.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('SignBillDetailPanel');
        }
        //回款额报表
        if(cash_report != '' && cash_report.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('CashReportPanel');
        }
        //回款额明细
        if(cash_detail != '' && cash_detail.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('CashDetailPanel');
        }
        //费用报表
        if(cost_report != '' && cost_report.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('CostReportPanel');
        }
        //费用明细
        if(cost_detail != '' && cost_detail.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('CostDetailPanel');
        }
        //add by renxianliang 2013-7-5 for xinhuamai
        //新华脉集团定制需求费用明细
        if(cost_detail_xinhuamai != '' && cost_detail_xinhuamai.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('CostDetailGrid_xinhuamaiPanel');
        }
        //add by 2012-12-11  销售上报
        if(sales_detail != '' && sales_detail.length > 0){
        	flag = true;
        }else {
        	Ext.getCmp('SellReportTabPanel').remove('EnquiriesPanel');
        }
        //促销上报
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
