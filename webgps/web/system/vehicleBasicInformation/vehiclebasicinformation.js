var root;
var tree;
var treeload;
/**
 * ��Ӱ�ť
 */
var addbut = new Ext.Action(
		{
			text : '����',
			id : 'addbut',
			handler : function() {
				/**
				 * ���������¼��������ж�
				 */
				var tmpdeviceId = Ext.getCmp('frmParendId').getValue();
				if (tmpdeviceId.length <= 0) {
					Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
					return;
				}
				var tmpexaminationDate = Ext.getCmp('frmexaminationDate')
						.getValue();
				var tmpproject = Ext.getCmp('frmproject').getValue();
				var tmpcondition = Ext.getCmp('frmcondition').getValue();
				var tmpexpenses = Ext.getCmp('frmexpenses').getValue();
				var tmphandler = Ext.getCmp('frmhandler').getValue();
				var tmpexpireDate = Ext.getCmp('frmexpireDate').getValue();
				if (((tmpexaminationDate.length != 0 || tmpproject.length != 0
						|| tmpcondition.length != 0 || tmpexpenses.length != 0
						|| tmphandler.length != 0 || tmpexpireDate.length != 0))
						&& (!(tmpexaminationDate.length != 0
								&& tmpproject.length != 0
								&& tmpcondition.length != 0
								&& tmpexpenses.length != 0
								&& tmphandler.length != 0 && tmpexpireDate.length != 0))) {
					Ext.Msg.alert('��ʾ', '�����¼�������д����!');
					return;
				}
				/**
				 * ���ͼ�¼��������ж�
				 */
				var tmpoilLiter = Ext.getCmp('frmoilLiter').getValue();
				var tmpoilCost = Ext.getCmp('frmoilCost').getValue();
				var tmpcreateDate = Ext.getCmp('frmcreateDate').getValue();
				if ((tmpoilLiter.length != 0 || tmpoilCost.length != 0 || tmpcreateDate.length != 0)
						&& (!(tmpoilLiter.length != 0 && tmpoilCost.length != 0 && tmpcreateDate.length != 0))) {
					Ext.Msg.alert('��ʾ', '���ͼ�¼�������д����!');
					return;
				}
				/**
				 * ��·�Ѽ�¼��������ж�
				 */
				var tmppayDate = Ext.getCmp('frmpayDatet').getValue();
				var tmppayPlace = Ext.getCmp('frmpayPlacet').getValue();
				var tmpexpenses = Ext.getCmp('frmexpensest').getValue();
				var tmphandler = Ext.getCmp('frmhandlert').getValue();
				if ((tmppayDate.length != 0 || tmppayPlace.length != 0
						|| tmpexpenses.length != 0 || tmphandler.length != 0)
						&& (!(tmppayDate.length != 0 && tmppayPlace.length != 0
								&& tmpexpenses.length != 0 && tmphandler.length != 0))) {
					Ext.Msg.alert('��ʾ', '��·�Ѽ�¼�������д����!');
					return;
				}
				/**
				 * ���ü�¼֧����������ж�
				 */
				var tmpvehicleDepreciatione = Ext.getCmp(
						'frmvehicleDepreciatione').getValue();
				var tmppersonalExpensese = Ext.getCmp('frmpersonalExpensese')
						.getValue();
				var tmpinsurancee = Ext.getCmp('frminsurancee').getValue();
				var tmpmaintenancee = Ext.getCmp('frmmaintenancee').getValue();
				var tmptolle = Ext.getCmp('frmtolle').getValue();
				var tmpannualChecke = Ext.getCmp('frmannualChecke').getValue();
				var tmpcreateDatee = Ext.getCmp('frmcreateDatee').getValue();
				if (((tmpvehicleDepreciatione.length != 0
						|| tmppersonalExpensese.length != 0
						|| tmpinsurancee.length != 0
						|| tmpmaintenancee.length != 0 || tmptolle.length != 0
						|| tmpannualChecke.length != 0 || tmpcreateDatee.length != 0))
						&& (!(tmpvehicleDepreciatione.length != 0
								&& tmppersonalExpensese.length != 0
								&& tmpinsurancee.length != 0
								&& tmpmaintenancee.length != 0
								&& tmptolle.length != 0
								&& tmpcreateDatee.length != 0 && tmpannualChecke.length != 0))) {
					Ext.Msg.alert('��ʾ', '���ü�¼֧���������д����!');
					return;
				}
				/**
				 * ���շ�����������ж�
				 */
				var tmpinsuranceNoi = Ext.getCmp('frminsuranceNoi').getValue();
				var tmpinsuranceNamei = Ext.getCmp('frminsuranceNamei')
						.getValue();
				var tmpinsuranceCoi = Ext.getCmp('frminsuranceCoi').getValue();
				var tmpinsuranceDatei = Ext.getCmp('frminsuranceDatei')
						.getValue();
				var tmpexpireDatei = Ext.getCmp('frmexpireDatei').getValue();
				var tmpsumInsuredi = Ext.getCmp('frmsumInsuredi').getValue();
				var tmppremiumi = Ext.getCmp('frmpremiumi').getValue();
				var tmphandleri = Ext.getCmp('frmhandleri').getValue();
				if (((tmpinsuranceNoi.length != 0
						|| tmpinsuranceNamei.length != 0
						|| tmpinsuranceCoi.length != 0
						|| tmpinsuranceDatei.length != 0
						|| tmpexpireDatei.length != 0
						|| tmpsumInsuredi.length != 0
						|| tmppremiumi.length != 0 || tmphandleri.length != 0))
						&& (!(tmpinsuranceNoi.length != 0
								&& tmpinsuranceNamei.length != 0
								&& tmpinsuranceCoi.length != 0
								&& tmpinsuranceDatei.length != 0
								&& tmpexpireDatei.length != 0
								&& tmpsumInsuredi.length != 0
								&& tmppremiumi.length != 0 && tmphandleri.length != 0))) {
					Ext.Msg.alert('��ʾ', '���շ��ü�¼�������д����!');
					return;
				}
				/**
				 * ����ά����������ж�
				 */
				var tmpmaintenanceDatev = Ext.getCmp('frmmaintenanceDatev')
						.getValue();
				var tmpexpensesv = Ext.getCmp('frmexpensesv').getValue();
				var tmpconditionv = Ext.getCmp('frmconditionv').getValue();
				var tmphandlerv = Ext.getCmp('frmhandlerv').getValue();
				var tmpexpireDatev = Ext.getCmp('frmexpireDatev').getValue();
				if (((tmpmaintenanceDatev.length != 0
						|| tmpexpensesv.length != 0
						|| tmpconditionv.length != 0 || tmphandlerv.length != 0 || tmpexpireDatev.length != 0))
						&& (!(tmpmaintenanceDatev.length != 0
								&& tmpexpensesv.length != 0
								&& tmpconditionv.length != 0
								&& tmphandlerv.length != 0 && tmpexpireDatev.length != 0))) {
					Ext.Msg.alert('��ʾ', '����ά����¼�������д����!');
					return;
				}
				/**
				 * ���ͽ���ж�����
				 */
				var dispatchDated = Ext.getCmp('dispatchDated').getValue();
				var dispatchAmountd = Ext.getCmp('dispatchAmountd').getValue();
				var frmhandlerd = Ext.getCmp('frmhandlerd').getValue();
				if (((dispatchDated.length != 0 || dispatchAmountd.length != 0 || frmhandlerd.length != 0))
						&& (!(dispatchDated.length != 0
								&& dispatchAmountd.length != 0 && frmhandlerd.length != 0))) {
					Ext.Msg.alert('��ʾ', '���ͽ���¼�������д����!');
					return;
				}

				parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ����¼�¼��?', addConfirm);
			},
			iconCls : 'icon-add'
		});
/**
 * ���ȷ���ύ
 */
function addConfirm(btn) {
	if (btn == 'yes') {
		var tmpexaminationDate = Ext.getCmp('frmexaminationDate').getValue();
		var tmpoilLiter = Ext.getCmp('frmoilLiter').getValue();
		var tmppayDate = Ext.getCmp('frmpayDatet').getValue();
		var tmpvehicleDepreciatione = Ext.getCmp('frmvehicleDepreciatione')
				.getValue();
		var tmpmaintenanceDatev = Ext.getCmp('frmmaintenanceDatev').getValue();
		var tmpinsuranceNoi = Ext.getCmp('frminsuranceNoi').getValue();
		var dispatchDated = Ext.getCmp('dispatchDated').getValue();
		// û�������κ����ݵ����
		if (tmpexaminationDate.length == 0 && tmpoilLiter.length == 0
				&& tmppayDate.length == 0
				&& tmpvehicleDepreciatione.length == 0
				&& tmpinsuranceNoi.length == 0
				&& tmpmaintenanceDatev.length == 0
				&& dispatchDated.length == 0) {
			Ext.Msg.alert('��ʾ', '����Ч�����!');
			return;
		}
		Ext.Msg.show({
			msg : '���ڱ��� ���Ե�...',
			progressText : '����...',
			width : 300,
			wait : true,
			icon : 'ext-mb-download'
		});
		if (tmpexaminationDate.length != 0) {
			addAnnual();// ��ӳ��������¼
		} else if (tmpoilLiter.length != 0) {
			addOiling();// ��Ӽ��ͼ�¼
		} else if (tmppayDate.length != 0) {
			addToll();// ��·�Ѽ�¼���
		} else if (tmpvehicleDepreciatione.length != 0) {
			addExpense();// ���ü�¼֧�����
		} else if (tmpinsuranceNoi.length != 0) {
			addInsurance(); // ���շ������
		} else if (tmpmaintenanceDatev.length != 0) {
			addVehicles();// ����ά����¼���
		} else if (dispatchDated.length != 0) {
			addDispatchMoney();// ���ͽ�����
		}

	}
}

/**
 * ��ӳ��������¼
 */
function addAnnual() {
	var tmpdeviceId = Ext.getCmp('frmdeviceId').getValue();
	var tmpexaminationDate = Ext.getCmp('frmexaminationDate').getValue();
	var tmpproject = Ext.getCmp('frmproject').getValue();
	var tmpcondition = Ext.getCmp('frmcondition').getValue();
	var tmpexpenses = Ext.getCmp('frmexpenses').getValue();
	var tmphandler = Ext.getCmp('frmhandler').getValue();
	var tmpexpireDate = Ext.getCmp('frmexpireDate').getValue();
	var tmpdemo = Ext.getCmp('frmdemo').getValue();
	Ext.Ajax
			.request({
				url : path
						+ '/annualExamination/annualExamination.do?method=addAnnualExamination',
				method : 'POST',
				params : {
					deviceId : tmpdeviceId,
					examinationDate : tmpexaminationDate,
					project : encodeURI(tmpproject),
					condition : encodeURI(tmpcondition),
					expenses : tmpexpenses,
					handler : encodeURI(tmphandler),
					expireDate : tmpexpireDate,
					demo : encodeURI(tmpdemo)
				},
				success : function(request) {
					var res = Ext.decode(request.responseText);
					var tmpoilLiter = Ext.getCmp('frmoilLiter').getValue();
					var tmppayDate = Ext.getCmp('frmpayDatet').getValue();
					var tmpvehicleDepreciatione = Ext.getCmp(
							'frmvehicleDepreciatione').getValue();
					var tmpmaintenanceDatev = Ext.getCmp('frmmaintenanceDatev')
							.getValue();
					var tmpinsuranceNoi = Ext.getCmp('frminsuranceNoi')
							.getValue();
					var dispatchDated = Ext.getCmp('dispatchDated').getValue();
					if (res.result == 1) {
						if (tmpoilLiter.length != 0) {
							addOiling();
						} else if (tmppayDate.length != 0) {
							addToll();
						} else if (tmpvehicleDepreciatione.length != 0) {
							addExpense();
						} else if (tmpinsuranceNoi.length != 0) {
							addInsurance();
						} else if (tmpmaintenanceDatev.length != 0) {
							addVehicles();
						} else if (dispatchDated.length != 0) {
							addDispatchMoney();
						} else {
							Ext.Msg.alert('��ʾ', "�����ɹ�!");
							return;
						}

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
/**
 * ��Ӽ��ͼ�¼
 */
function addOiling() {
	var tmpdeviceId = Ext.getCmp('frmdeviceId1').getValue();
	var tmpoilLiter = Ext.getCmp('frmoilLiter').getValue();
	var tmpoilCost = Ext.getCmp('frmoilCost').getValue();
	var tmpcreateDate = Ext.getCmp('frmcreateDate').getValue();
	Ext.Ajax.request({
		url : path + '/oiling/oiling.do?method=addOiling',
		method : 'POST',
		params : {
			deviceId : encodeURI(tmpdeviceId),
			oilLiter : encodeURI(tmpoilLiter),
			oilCost : encodeURI(tmpoilCost),
			createDate : tmpcreateDate
		},
		success : function(request) {
			var res = Ext.decode(request.responseText);
			var tmppayDate = Ext.getCmp('frmpayDatet').getValue();
			var tmpvehicleDepreciatione = Ext.getCmp('frmvehicleDepreciatione')
					.getValue();
			var tmpmaintenanceDatev = Ext.getCmp('frmmaintenanceDatev')
					.getValue();
			var tmpinsuranceNoi = Ext.getCmp('frminsuranceNoi').getValue();
			var dispatchDated = Ext.getCmp('dispatchDated').getValue();
			if (res.result == 1) {
				if (tmppayDate.length != 0) {
					addToll();
				} else if (tmpvehicleDepreciatione.length != 0) {
					addExpense();
				} else if (tmpinsuranceNoi.length != 0) {
					addInsurance();
				} else if (tmpmaintenanceDatev.length != 0) {
					addVehicles();
				} else if (dispatchDated.length != 0) {
					addDispatchMoney();
				} else {
					Ext.Msg.alert('��ʾ', "�����ɹ�!");
					return;
				}
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
/**
 * ��·�Ѽ�¼���
 */
function addToll() {
	var tmpdeviceId = Ext.getCmp('frmdeviceId2').getValue();
	var tmppayDate = Ext.getCmp('frmpayDatet').getValue();
	var tmppayPlace = Ext.getCmp('frmpayPlacet').getValue();
	var tmpexpenses = Ext.getCmp('frmexpensest').getValue();
	var tmphandler = Ext.getCmp('frmhandlert').getValue();
	var frmdemot = Ext.getCmp('frmdemot').getValue();
	Ext.Ajax.request({
		url : path + '/toll/toll.do?method=addToll',
		method : 'POST',
		params : {
			deviceId : tmpdeviceId,
			payDate : tmppayDate,
			payPlace : encodeURI(tmppayPlace),
			expenses : tmpexpenses,
			demo : encodeURI(frmdemot),
			handler : encodeURI(tmphandler)
		},
		success : function(request) {
			var res = Ext.decode(request.responseText);
			var tmpvehicleDepreciatione = Ext.getCmp('frmvehicleDepreciatione')
					.getValue();
			var tmpmaintenanceDatev = Ext.getCmp('frmmaintenanceDatev')
					.getValue();
			var tmpinsuranceNoi = Ext.getCmp('frminsuranceNoi').getValue();
			var dispatchDated = Ext.getCmp('dispatchDated').getValue();
			if (res.result == 1) {
				if (tmpvehicleDepreciatione.length != 0) {
					addExpense();
				} else if (tmpinsuranceNoi.length != 0) {
					addInsurance();
				} else if (tmpmaintenanceDatev.length != 0) {
					addVehicles();
				} else if (dispatchDated.length != 0) {
					addDispatchMoney();
				} else {
					Ext.Msg.alert('��ʾ', "�����ɹ�!");
					return;
				}
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
/**
 * ���ü�¼֧�����
 */
function addExpense() {
	var tmpdeviceId = Ext.getCmp('frmdeviceId3').getValue();
	var tmpvehicleDepreciatione = Ext.getCmp('frmvehicleDepreciatione')
			.getValue();
	var tmppersonalExpensese = Ext.getCmp('frmpersonalExpensese').getValue();
	var tmpinsurancee = Ext.getCmp('frminsurancee').getValue();
	var tmpmaintenancee = Ext.getCmp('frmmaintenancee').getValue();
	var tmptolle = Ext.getCmp('frmtolle').getValue();
	var tmpannualChecke = Ext.getCmp('frmannualChecke').getValue();
	var tmpcreateDatee = Ext.getCmp('frmcreateDatee').getValue();
	Ext.Ajax.request({
		url : path
				+ '/vehicleExpense/vehicleExpense.do?method=addVehicleExpense',
		method : 'POST',
		params : {
			deviceId : tmpdeviceId,
			vehicleDepreciation : tmpvehicleDepreciatione,
			personalExpenses : tmppersonalExpensese,
			insurance : tmpinsurancee,
			maintenance : tmpmaintenancee,
			toll : tmptolle,
			annualCheck : tmpannualChecke,
			createDate : tmpcreateDatee
		},
		success : function(request) {
			var res = Ext.decode(request.responseText);
			var tmpmaintenanceDatev = Ext.getCmp('frmmaintenanceDatev')
					.getValue();
			var tmpinsuranceNoi = Ext.getCmp('frminsuranceNoi').getValue();
			var dispatchDated = Ext.getCmp('dispatchDated').getValue();
			if (res.result == 1) {
				if (tmpinsuranceNoi.length != 0) {
					addInsurance();
				} else if (tmpmaintenanceDatev.length != 0) {
					addVehicles();
				} else if (dispatchDated.length != 0) {
					addDispatchMoney();
				} else {
					Ext.Msg.alert('��ʾ', "�����ɹ�!");
					return;
				}
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
/**
 * ���շ������
 */
function addInsurance() {
	var tmpdeviceId = Ext.getCmp('frmdeviceId4').getValue();
	var tmpinsuranceNoi = Ext.getCmp('frminsuranceNoi').getValue();
	var tmpinsuranceNamei = Ext.getCmp('frminsuranceNamei').getValue();
	var tmpinsuranceCoi = Ext.getCmp('frminsuranceCoi').getValue();
	var tmpinsuranceDatei = Ext.getCmp('frminsuranceDatei').getValue();
	var tmpexpireDatei = Ext.getCmp('frmexpireDatei').getValue();
	var tmpsumInsuredi = Ext.getCmp('frmsumInsuredi').getValue();
	var tmppremiumi = Ext.getCmp('frmpremiumi').getValue();
	var tmphandleri = Ext.getCmp('frmhandleri').getValue();
	Ext.Ajax.request({
		url : path + '/insurance/insurance.do?method=addInsurance',
		method : 'POST',
		params : {
			deviceId : tmpdeviceId,
			insuranceNo : encodeURI(tmpinsuranceNoi),
			insuranceName : encodeURI(tmpinsuranceNamei),
			insuranceCo : encodeURI(tmpinsuranceCoi),
			insuranceDate : tmpinsuranceDatei,
			expireDate : tmpexpireDatei,
			sumInsured : tmpsumInsuredi,
			premium : tmppremiumi,
			handler : encodeURI(tmphandleri)
		},
		success : function(request) {
			var res = Ext.decode(request.responseText);
			var tmpmaintenanceDatev = Ext.getCmp('frmmaintenanceDatev')
					.getValue();
			var dispatchDated = Ext.getCmp('dispatchDated').getValue();
			if (res.result == 1) {
				if (tmpmaintenanceDatev.length != 0) {
					addVehicles();
				} else if (dispatchDated.length != 0) {
					addDispatchMoney();
				} else {
					Ext.Msg.alert('��ʾ', "�����ɹ�!");
					return;
				}
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
/**
 * ����ά����¼���
 */
function addVehicles() {
	var frmdevicev = Ext.getCmp('frmdeviceId5').getValue();
	var tmpmaintenanceDatev = Ext.getCmp('frmmaintenanceDatev').getValue();
	var tmpexpensesv = Ext.getCmp('frmexpensesv').getValue();
	var tmpconditionv = Ext.getCmp('frmconditionv').getValue();
	var tmphandlerv = Ext.getCmp('frmhandlerv').getValue();
	var tmpexpireDatev = Ext.getCmp('frmexpireDatev').getValue();
	var tmpdemov = Ext.getCmp('tmpdemov').getValue();
	Ext.Ajax
			.request({
				url : path
						+ '/vehiclesMaintenance/vehiclesMaintenance.do?method=addVehiclesMaintenance',
				method : 'POST',
				params : {
					deviceId : encodeURI(frmdevicev),
					maintenanceDate : tmpmaintenanceDatev,
					expenses : encodeURI(tmpexpensesv),
					condition : encodeURI(tmpconditionv),
					handler : encodeURI(tmphandlerv),
					expireDate : tmpexpireDatev,
					demo : encodeURI(tmpdemov)
				},
				success : function(request) {
					var res = Ext.decode(request.responseText);
					var dispatchDated = Ext.getCmp('dispatchDated').getValue();
					if (res.result == 1) {
						if (dispatchDated.length != 0) {
							addDispatchMoney();
						} else {
							Ext.Msg.alert('��ʾ', "�����ɹ�!");
							return;
						}
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
/**
 * ���ͽ���¼���
 */
function addDispatchMoney() {
	var frmdevicev6 = Ext.getCmp('frmdeviceId6').getValue();
	var dispatchDated = Ext.getCmp('dispatchDated').getValue();
	var dispatchAmountd = Ext.getCmp('dispatchAmountd').getValue();
	var frmhandlerd = Ext.getCmp('frmhandlerd').getValue();
	var frmdemod = Ext.getCmp('frmdemod').getValue();
	Ext.Ajax.request({
		url : path + '/dispatchMoney/dispatchMoney.do?method=addDispatchMoney',
		method : 'POST',
		params : {
			tmpdeviceId : encodeURI(frmdevicev6),
			dispatchDate : dispatchDated,
			frmhandler : encodeURI(frmhandlerd),
			dispatchAmount : encodeURI(dispatchAmountd),
			frmdemo : encodeURI(frmdemod)
		},
		// timeout : 10000,
		success : function(request) {
			var res = Ext.decode(request.responseText);
			if (res.result == 1) {
				Ext.Msg.alert('��ʾ', '�����ɹ�');
				return;
			} else {
				// store.reload();
				Ext.Msg.alert('��ʾ', "����ʧ��!");
				return;
			}
		},
		failure : function(request) {
			Ext.Msg.alert('��ʾ', "����ʧ��!");
		}
	});
}

var toolbar = new Ext.Toolbar({
	id : 'frmtbar',
	items : [ addbut ]
});

Ext
		.onReady(function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'under';
			var simple = new Ext.FormPanel({
				region : 'center',
				// labelAlign: 'right',
				title : '����������Ϣ���',
				region : 'center',
				labelWidth : 300,
				frame : true,
				autoScroll : true,
				// margins : '5 5 5 5',
				width : 500,
				items : [ {// ��1
					layout : 'column',
					items : [
					// ��
					{
						columnWidth : .33,
						layout : 'form',
						xtype : 'fieldset',
						border : false,
						title : '���������¼���',
						// autoHeight : true,
						width : 250,
						labelWidth : 100,
						items : [ {
							fieldLabel : '�ն�����',
							id : 'frmParendId',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : '��������',
							xtype : 'datefield',
							id : 'frmexaminationDate',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							fieldLabel : '������Ŀ',
							id : 'frmproject',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '�������',
							id : 'frmcondition',
							width : 150,
							xtype : 'textfield'
						}, {
							fieldLabel : '������',
							xtype : 'numberfield',
							id : 'frmexpenses',
							width : 150
						}, {
							fieldLabel : '������',
							id : 'frmhandler',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '��������',
							xtype : 'datefield',
							id : 'frmexpireDate',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							fieldLabel : '��ע',
							id : 'frmdemo',
							xtype : 'textfield',
							width : 150
						}, {
							xtype : 'hidden',
							id : 'frmdeviceId'
						} ]
					},
					// ��
					{
						columnWidth : .3,
						layout : 'form',
						xtype : 'fieldset',
						border : false,
						title : '���ü�¼֧�����',
						// autoHeight : true,
						width : 250,
						labelWidth : 100,
						items : [ {
							fieldLabel : '�ն�����',
							id : 'frmdevicee',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : '�����۾ɷ���',
							xtype : 'numberfield',
							id : 'frmvehicleDepreciatione',
							width : 150
						}, {
							fieldLabel : '��Ա����',
							xtype : 'numberfield',
							id : 'frmpersonalExpensese',
							width : 150
						}, {
							fieldLabel : '����̯������',
							xtype : 'numberfield',
							id : 'frminsurancee',
							width : 150
						}, {
							fieldLabel : 'ά�ޱ�������',
							xtype : 'numberfield',
							id : 'frmmaintenancee',
							width : 150
						}, {
							fieldLabel : '��·���ŷ�',
							xtype : 'numberfield',
							id : 'frmtolle',
							width : 150
						}, {
							fieldLabel : '��켰��������',
							xtype : 'numberfield',
							id : 'frmannualChecke',
							width : 150
						}, {
							fieldLabel : '����',
							xtype : 'datefield',
							id : 'frmcreateDatee',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							xtype : 'hidden',
							id : 'frmdeviceId3'
						} ]
					} ]
				}, {// ��2
					layout : 'column',
					items : [
					// ��
					{
						columnWidth : .33,
						layout : 'form',
						xtype : 'fieldset',
						border : false,
						title : '��·�Ѽ�¼���',
						// autoHeight : true,
						width : 300,
						labelWidth : 100,
						items : [ {
							fieldLabel : '�ն�����',
							id : 'frmdevicet',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : '��������',
							xtype : 'datefield',
							id : 'frmpayDatet',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							fieldLabel : '���ѵص�',
							id : 'frmpayPlacet',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '���ѽ��',
							id : 'frmexpensest',
							xtype : 'numberfield',
							width : 150
						}, {
							fieldLabel : '������',
							id : 'frmhandlert',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '��ע',
							id : 'frmdemot',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '',
							// id : 'frmdemot',
							// xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '',
							// id : 'frmdemot',
							// xtype : 'textfield',
							width : 150
						}, {
							xtype : 'hidden',
							id : 'frmdeviceId2'
						} ]
					},
					// ��
					{
						columnWidth : .3,
						layout : 'form',
						border : false,
						xtype : 'fieldset',
						title : '����ά����¼���',
						// autoHeight : true,
						width : 300,
						labelWidth : 100,
						items : [ {
							fieldLabel : '�ն�����',
							id : 'frmdevicev',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : 'ά������',
							id : 'frmmaintenanceDatev',
							width : 150,
							xtype : 'datefield',
							editable : false,
							format : 'Y-m-d'
						}, {
							fieldLabel : 'ά������',
							id : 'frmexpensesv',
							xtype : 'numberfield',
							width : 150
						}, {
							fieldLabel : 'ά�����',
							id : 'frmconditionv',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '������',
							id : 'frmhandlerv',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '��������',
							xtype : 'datefield',
							id : 'frmexpireDatev',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							fieldLabel : '��ע',
							xtype : 'textfield',
							id : 'tmpdemov',
							width : 150
						}, {
							xtype : 'hidden',
							id : 'frmdeviceId5'
						} ]
					} ]
				}, {
					// ��3
					layout : 'column',
					items : [
					// ��
					{
						columnWidth : 0.33,
						layout : 'form',
						border : false,
						xtype : 'fieldset',
						title : '���ͽ�����',
						// autoHeight : true,
						width : 300,
						labelWidth : 100,
						items : [ {
							fieldLabel : '�ն�����',
							id : 'frmdeviced',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : '��������',
							id : 'dispatchDated',
							width : 150,
							xtype : 'datefield',
							editable : false,
							format : 'Y-m-d'
						}, {
							fieldLabel : '��������',
							id : 'dispatchAmountd',
							xtype : 'numberfield',
							width : 150
						}, {
							fieldLabel : '������',
							id : 'frmhandlerd',
							xtype : 'textfield',
							width : 150
						}, {
							xtype : 'textarea',
							fieldLabel : '��ע',
							xtype : 'textfield',
							id : 'frmdemod',
							width : 150
						}, {
							xtype : 'hidden',
							id : 'frmdeviceId6'
						} ]
					},
					// ��
					{
						columnWidth : .3,
						layout : 'form',
						xtype : 'fieldset',
						border : false,
						title : '���ͼ�¼���',
						// autoHeight : true,
						width : 300,
						labelWidth : 100,
						items : [ {
							fieldLabel : '�ն�����',
							id : 'frmdeviceo',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : '������(��)',
							xtype : 'numberfield',
							id : 'frmoilLiter',
							width : 150
						}, {
							fieldLabel : '���ͽ��',
							xtype : 'numberfield',
							id : 'frmoilCost',
							width : 150
						}, {
							fieldLabel : '��������',
							xtype : 'datefield',
							id : 'frmcreateDate',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							xtype : 'hidden',
							id : 'frmdeviceId1'
						} ]
					} ]
				}, {

					// ��3
					layout : 'column',
					items : [
					// ��
					{
						columnWidth : 1,
						layout : 'form',
						xtype : 'fieldset',
						border : false,
						title : '���շ��ü�¼���',
						// autoHeight : true,
						width : 300,
						labelWidth : 100,
						items : [ {
							fieldLabel : '�ն�����',
							id : 'frmdevicei',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : '���յ���',
							id : 'frminsuranceNoi',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '��������',
							id : 'frminsuranceNamei',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '���չ�˾',
							id : 'frminsuranceCoi',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : 'Ͷ������',
							xtype : 'datefield',
							id : 'frminsuranceDatei',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							fieldLabel : '��������',
							xtype : 'datefield',
							id : 'frmexpireDatei',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							fieldLabel : '����',
							xtype : 'numberfield',
							id : 'frmsumInsuredi',
							width : 150
						}, {
							fieldLabel : '����',
							xtype : 'numberfield',
							id : 'frmpremiumi',
							width : 150
						}, {
							fieldLabel : '������',
							id : 'frmhandleri',
							xtype : 'textfield',
							width : 150
						}, {
							xtype : 'hidden',
							id : 'frmdeviceId4'
						} ]
					},
					// ��
					{
					// �����
					} ]

				} ],
				tbar : toolbar
			});
			root = new Ext.tree.AsyncTreeNode({
				text : '��λƽ̨',
				id : '-100',
				draggable : false
			});
			treeload = new Ext.tree.TreeLoader(
					{
						dataUrl : path
								+ '/manage/termGroupManage.do?actionMethod=groupListForEntTerminalNoCheck'
					});
			tree = new Ext.tree.TreePanel({
				region : 'west',
				id : 'west-panel',
				width : 250,
				margins : '5 0 5 5',
				autoScroll : true,
				animate : true,
				containerScroll : true,
				rootVisible : false,
				root : root,
				loader : treeload
			});
			var viewport = new Ext.Viewport({
				layout : 'border',
				items : [ tree, simple ]
			});
			tree.on('click', function(node, e) {
				var id_arr = '';
				id_arr = node.id.split('@#');
				if (id_arr.length >= 8) {
					var deviceIdTmp = id_arr[0];
					// ���������¼
					Ext.getCmp('frmParendId').setValue(node.text);
					Ext.getCmp('frmdeviceId').setValue(deviceIdTmp);
					// ���ͼ�¼
					Ext.getCmp('frmdeviceo').setValue(node.text);
					Ext.getCmp('frmdeviceId1').setValue(deviceIdTmp);
					// ��·�Ѽ�¼
					Ext.getCmp('frmdevicet').setValue(node.text);
					Ext.getCmp('frmdeviceId2').setValue(deviceIdTmp);
					// ���ü�¼֧��
					Ext.getCmp('frmdevicee').setValue(node.text);
					Ext.getCmp('frmdeviceId3').setValue(deviceIdTmp);
					// ���շ���
					Ext.getCmp('frmdevicei').setValue(node.text);
					Ext.getCmp('frmdeviceId4').setValue(deviceIdTmp);
					// ����ά����¼
					Ext.getCmp('frmdevicev').setValue(node.text);
					Ext.getCmp('frmdeviceId5').setValue(deviceIdTmp);
					// ���ͽ��
					Ext.getCmp('frmdeviced').setValue(node.text);
					Ext.getCmp('frmdeviceId6').setValue(deviceIdTmp);

				}
			});
		});
