var promotionStartTime = tmpdate + ' ' + '00:00:00';
var promotionEndTime = tmpdate + ' ' + '23:59:59';
var promotionGpsDeviceids = '';
var promotionPoiName = '';
var promotionApproved = -1;

function initPromotionId(deviceID_) {
	promotionGpsDeviceids = deviceID_;
	var tmpstartdate = Ext.getCmp('promotionDetailsdf').getValue().format(
			'Y-m-d');
	var tmpenddate = Ext.getCmp('promotionDetailedf').getValue()
			.format('Y-m-d');
	promotionPoiName = Ext.getCmp('promotionDetaildif').getValue();
	promotionApproved = Ext.getCmp('promotionApproved').getValue();
	promotionStartTime = tmpstartdate + ' ' + '00:00:00';
	promotionEndTime = tmpenddate + ' ' + '23:59:59';
}

function searchPromotionId() {
	storeLoad(PromotionStore, 0, 15, promotionGpsDeviceids, promotionPoiName,
			promotionStartTime, promotionEndTime, promotionApproved, false);
}

var PromotionStore = new Ext.data.Store({
	restful : true,
	proxy : new Ext.data.HttpProxy({
		url : path + '/promotion/promotion.do?method=listPromotionDetails'
	}),
	reader : new Ext.data.JsonReader({
		totalProperty : 'totalp',
		successProperty : 'success',
		idProperty : 'a',
		root : 'data'
	}, [ {
		name : 'id'
	}, {
		name : 'createOn'
	}, {
		name : 'groupName'
	}, {
		name : 'vehicleNumber'
	}, {
		name : 'poiName'
	}, {
		name : 'promotionAmount'
	}, {
		name : 'promotionAmount2'
	}, {
		name : 'promotionAmount3'
	}, {
		name : 'promotionAmount4'
	}, {
		name : 'promotionAmount5'
	}, {
		name : 'promotionAmount6'
	}, {
		name : 'promotionAmount7'
	}, {
		name : 'promotionAmount8'
	}, {
		name : 'promotionAmount9'
	}, {
		name : 'promotionAmount10'
	}, {
		name : 'approved'
	} ]),
	listeners : {
		beforeload : {
			fn : function(thiz, options) {
				this.baseParams = {
					searchValue : encodeURI(promotionPoiName),
					startTime : promotionStartTime,
					endTime : promotionEndTime,
					deviceIds : promotionGpsDeviceids
				};
			}
		}
	}
});

var PromotionSm = new Ext.grid.CheckboxSelectionModel({});
var PromotionId = {
	xtype : 'grid',
	id : 'PromotionId',
	store : PromotionStore,
	loadMask : {
		msg : main.loading
	},
	columns : [ PromotionSm, {
		header : '����',
		width : 130,
		sortable : true,
		dataIndex : 'createOn'
	}, {
		header : '����',
		width : 130,
		sortable : true,
		dataIndex : 'groupName'
	}, {
		header : 'Ա������',
		width : 100,
		sortable : true,
		dataIndex : 'vehicleNumber'
	}, {
		header : '�ͻ�����',
		width : 100,
		sortable : true,
		dataIndex : 'poiName'
	}, {
		header : "�־���",
		width : 100,
		sortable : true,
		dataIndex : 'promotionAmount'
	}, {
		header : "Բ���",
		width : 100,
		sortable : true,
		dataIndex : 'promotionAmount2'
	}, {
		header : "����",
		width : 100,
		sortable : true,
		dataIndex : 'promotionAmount3'
	}, {
		header : "�ͻ���ϵ��",
		width : 100,
		sortable : true,
		dataIndex : 'promotionAmount4'
	}, {
		header : "�۸��ǩ",
		width : 100,
		sortable : true,
		dataIndex : 'promotionAmount5'
	}, {
		header : "��ǩͲ",
		width : 100,
		sortable : true,
		dataIndex : 'promotionAmount6'
	}, {
		header : "KT��",
		width : 100,
		sortable : true,
		dataIndex : 'promotionAmount7'
	}, {
		header : "�й���",
		width : 100,
		sortable : true,
		dataIndex : 'promotionAmount8'
	}, {
		header : "POP",
		width : 100,
		sortable : true,
		dataIndex : 'promotionAmount9'
	}, {
		header : '�̸�',
		width : 100,
		sortable : true,
		dataIndex : 'promotionAmount10'
	}, {
		header : '״̬',
		width : 100,
		sortable : true,
		dataIndex : 'approved',
		renderer : function(value, meta, record) {
			if (value == '0') {
				return "δ���";
			} else {
				return "���";
			}
		}
	} ],
	sm : PromotionSm,
	stripeRows : true,
	stateful : true,
	stateId : 'PromotionIdId',

	tbar : {
		xtype : 'toolbar',
		items : [
				{
					xtype : 'label',
					text : main.start_time
				},
				{
					id : 'promotionDetailsdf',
					xtype : 'datefield',
					fieldLabel : 'Date',
					format : 'Y-m-d',
					width : 80,
					editable : false,
					value : new Date()
				},
				'-',
				{
					xtype : 'label',
					text : main.end_time
				},
				{
					id : 'promotionDetailedf',
					xtype : 'datefield',
					fieldLabel : 'Date',
					format : 'Y-m-d',
					width : 80,
					editable : false,
					value : new Date()
				},
				'-',
				{
					xtype : 'label',
					text : '�ͻ�����'
				},
				{
					id : 'promotionDetaildif',
					xtype : 'textfield',
					width : 60,
					enableKeyEvents : true,
					listeners : {
						keypress : function(textField, e) {
							if (e.getKey() == e.ENTER) {
								var deviceID_ = getDeviceId();
								if (deviceID_ == '') {
									Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
									return;
								} else {
									initPromotionId(deviceID_);
									searchPromotionId();
								}
							}
						}
					}
				},
				'-',
				{
					xtype : 'label',
					text : '״̬'
				},
				new Ext.form.ComboBox({
					width : 60,
					id : 'promotionApproved',
					enableKeyEvents : true,
					grow : true,
					valueField : "id",
					displayField : "name",
					mode : 'local',// �������ڱ���
					forceSelection : true,
					editable : false,
					triggerAction : 'all',
					value : -1,
					store : new Ext.data.SimpleStore({
						fields : [ "name", "id" ],
						data : [ [ '--ȫ��--', '-1' ], [ '���', '1' ],
								[ 'δ���', '0' ] ]
					})
				}),
				'-',
				{
					xtype : 'button',
					text : main.search,
					iconCls : 'icon-search',
					handler : function() {
						var deviceID_ = getDeviceId();
						if (deviceID_ == '') {
							Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
							return;
						} else {
							initPromotionId(deviceID_);
							searchPromotionId();
						}
					}
				},
				'-',
				{
					xtype : 'button',
					text : '����Excel',
					iconCls : 'icon-excel',
					handler : function() {
						var deviceID_ = getDeviceId();
						if (deviceID_ == '') {
							Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
							return;
						} else {
							initPromotionId(deviceID_);
							var excelhtml = path
									+ '/promotion/promotion.do?method=listPromotionExpExcel&searchValue='
									+ encodeURI(encodeURI(promotionPoiName))
									+ '&startTime=' + promotionStartTime
									+ '&endTime=' + promotionEndTime
									+ '&deviceIds=' + promotionGpsDeviceids
									+ '&duration=' + promotionApproved;
							var win = new Ext.Window(
									{
										title : '�����ļ�',
										closable : true,
										closeAction : 'close',
										autoWidth : false,
										width : 200,
										heigth : 150,
										items : [ new Ext.Panel(
												{
													html : "<a href='"
															+ excelhtml
															+ "' target='_blank' onclick='Ext.Msg.hide()'>������������ر���</a>",
													frame : true
												}) ]
									});
							win.show();
						}

					}
				},
				'-',
				{
					xtype : 'button',
					text : '���',
					iconCls : 'icon-saved',
					handler : function() {
						var tmpRecArr = PromotionSm.getSelections();
						var ids = '';
						for ( var i = 0; i < tmpRecArr.length; i++) {
							ids += tmpRecArr[i].get('id') + ',';
						}
						if (ids.length > 0) {
							ids = ids.substring(0, ids.length - 1);
						} else {
							Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫ��˵ļ�¼!');
							return;
						}
						approvedPromotionids = ids;
						Ext.MessageBox.confirm('��ʾ', 'ȷ��ͨ�������?',
								approvedPromotionConfirm);

					}
				} ]
	},
	bbar : new Ext.PagingToolbar({
		pageSize : 15,
		store : PromotionStore,
		displayInfo : true,
		displayMsg : '��{0}����{1}������ ��{2}��',
		emptyMsg : "û������"
	})
};

var approvedPromotionids;
function approvedPromotionConfirm(btn) {
	if (btn == 'yes') {
		Ext.Msg.show({
			msg : '�����ύ ���Ե�...',
			progressText : '�ύ...',
			width : 300,
			wait : true,
			icon : 'ext-mb-download'
		});
		Ext.Ajax.request({
			url : path + '/promotion/promotion.do?method=approved',
			method : 'POST',
			params : {
				ids : encodeURI(approvedPromotionids)
			},
			// timeout : 10000,
			success : function(request) {
				var res = Ext.decode(request.responseText);
				if (res.result == 1) {
					PromotionStore.reload();
					Ext.Msg.alert('��ʾ', '�����ɹ�');
					return;
				} else {
					Ext.Msg.alert('��ʾ', "����ʧ��!");
					return;
				}
			},
			failure : function(request) {
				Ext.Msg.alert('��ʾ', "����ʧ��!");
			}
		});
	}
}
