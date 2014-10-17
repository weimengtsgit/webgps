var SignBillReportGrid = new Ext.Panel({
	//layout: 'fit',
	id: 'SignBillReportGrid',
	autoScroll: true,
	html: '<iframe id="SignBillReportPanelIfr" name="SignBillReportPanelIfr" src="'+path+'/sellReportv2.1/SignBillReportPanel.jsp" style="width:100%; height: 100%;" SCROLLING="yes" frameborder="0"></iframe>',
	tbar : {
		xtype: 'toolbar',
		items:[{xtype: 'label',text: 'ʱ��'},
		       new Ext.form.ComboBox({
					width: 60,
					id : 'SignBillReportYear',
					enableKeyEvents : true,
					grow : true,
					//name : 'bindingStatus',
					valueField : "id",
					displayField : "name",
					mode : 'local',// �������ڱ���
					forceSelection : true,
					editable : false,
					triggerAction : 'all',
					store : new Ext.data.SimpleStore({
						fields : [ "id", "name" ],
						data : []
					})
				}),
				{xtype: 'label',text: '��'},
				new Ext.form.ComboBox({
					width: 60,
					id : 'SignBillReportNum',
					enableKeyEvents : true,
					grow : true,
					//name : 'bindingStatus',
					valueField : "id",
					displayField : "name",
					mode : 'local',// �������ڱ���
					forceSelection : true,
					editable : false,
					triggerAction : 'all',
					store : new Ext.data.SimpleStore({
						fields : [ "name", "id" ],
						data : []
					})
				}),
				{xtype: 'label',text: targetTemplateType},'-',
				{xtype: 'label', text: '�ͻ�'},
				{id: 'signBillReportdif',xtype: 'textfield',width: 100,enableKeyEvents: true,listeners: {keypress : function( textField, e ) {
					if (e.getKey() == e.ENTER) {}
				}}},'-',
				{xtype: 'button',text: '��ѯ',iconCls: 'icon-search',handler: function(){
					signBillGpsDeviceidsReportChart = getDeviceId();
					if(signBillGpsDeviceidsReportChart == ''){
						Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
						return;
					}
					signBillPoiNameReportChart = Ext.getCmp('signBillReportdif').getValue();
					var reportYear = Ext.getCmp('SignBillReportYear').getValue();
					var reportNum = Ext.getCmp('SignBillReportNum').getValue();
					var ifrObject_ = document.getElementById('SignBillReportPanelIfr').contentWindow;
					if(ifrObject_.flag){
						ifrObject_.getLineChart('../signBill/signBill.do?method=getSignBillsByTime&poiName='+signBillPoiNameReportChart+'&reportYear='+reportYear+'&reportNum='+reportNum+'&deviceIds='+signBillGpsDeviceidsReportChart, 'signBillChartDiv', '#signBillUnauditedSpan');
					}else{
						ifrObject_.getLineChart('../signBill/signBill.do?method=getSignBillHisByTime&poiName='+signBillPoiNameReportChart+'&reportYear='+reportYear+'&reportNum='+reportNum+'&deviceIds='+signBillGpsDeviceidsReportChart, 'signBillChartDiv', '#signBillUnauditedSpan');
					}
				}}
	    ]
	},
	listeners:{
		activate : function(p){
			initReportTimeScope(Ext.getCmp('SignBillReportYear'), Ext.getCmp('SignBillReportNum'));
		}
	}
});

