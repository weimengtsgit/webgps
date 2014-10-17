var root;
var tree;
var treeload;
/**
 * 添加按钮
 */
var addbut = new Ext.Action(
		{
			text : '新增',
			id : 'addbut',
			handler : function() {
				/**
				 * 车辆年审记录添加条件判断
				 */
				var tmpdeviceId = Ext.getCmp('frmParendId').getValue();
				if (tmpdeviceId.length <= 0) {
					Ext.Msg.alert('提示', '请选择终端!');
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
					Ext.Msg.alert('提示', '年审记录添加请填写完整!');
					return;
				}
				/**
				 * 加油记录添加条件判断
				 */
				var tmpoilLiter = Ext.getCmp('frmoilLiter').getValue();
				var tmpoilCost = Ext.getCmp('frmoilCost').getValue();
				var tmpcreateDate = Ext.getCmp('frmcreateDate').getValue();
				if ((tmpoilLiter.length != 0 || tmpoilCost.length != 0 || tmpcreateDate.length != 0)
						&& (!(tmpoilLiter.length != 0 && tmpoilCost.length != 0 && tmpcreateDate.length != 0))) {
					Ext.Msg.alert('提示', '加油记录添加请填写完整!');
					return;
				}
				/**
				 * 过路费记录添加条件判断
				 */
				var tmppayDate = Ext.getCmp('frmpayDatet').getValue();
				var tmppayPlace = Ext.getCmp('frmpayPlacet').getValue();
				var tmpexpenses = Ext.getCmp('frmexpensest').getValue();
				var tmphandler = Ext.getCmp('frmhandlert').getValue();
				if ((tmppayDate.length != 0 || tmppayPlace.length != 0
						|| tmpexpenses.length != 0 || tmphandler.length != 0)
						&& (!(tmppayDate.length != 0 && tmppayPlace.length != 0
								&& tmpexpenses.length != 0 && tmphandler.length != 0))) {
					Ext.Msg.alert('提示', '过路费记录添加请填写完整!');
					return;
				}
				/**
				 * 费用记录支出添加条件判断
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
					Ext.Msg.alert('提示', '费用记录支出添加请填写完整!');
					return;
				}
				/**
				 * 保险费用添加条件判断
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
					Ext.Msg.alert('提示', '保险费用记录添加请填写完整!');
					return;
				}
				/**
				 * 车辆维护添加条件判断
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
					Ext.Msg.alert('提示', '车辆维护记录添加请填写完整!');
					return;
				}
				/**
				 * 配送金额判断条件
				 */
				var dispatchDated = Ext.getCmp('dispatchDated').getValue();
				var dispatchAmountd = Ext.getCmp('dispatchAmountd').getValue();
				var frmhandlerd = Ext.getCmp('frmhandlerd').getValue();
				if (((dispatchDated.length != 0 || dispatchAmountd.length != 0 || frmhandlerd.length != 0))
						&& (!(dispatchDated.length != 0
								&& dispatchAmountd.length != 0 && frmhandlerd.length != 0))) {
					Ext.Msg.alert('提示', '配送金额记录添加请填写完整!');
					return;
				}

				parent.Ext.MessageBox.confirm('提示', '您确定要添加新记录吗?', addConfirm);
			},
			iconCls : 'icon-add'
		});
/**
 * 添加确定提交
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
		// 没有输入任何数据的添加
		if (tmpexaminationDate.length == 0 && tmpoilLiter.length == 0
				&& tmppayDate.length == 0
				&& tmpvehicleDepreciatione.length == 0
				&& tmpinsuranceNoi.length == 0
				&& tmpmaintenanceDatev.length == 0
				&& dispatchDated.length == 0) {
			Ext.Msg.alert('提示', '无有效的添加!');
			return;
		}
		Ext.Msg.show({
			msg : '正在保存 请稍等...',
			progressText : '保存...',
			width : 300,
			wait : true,
			icon : 'ext-mb-download'
		});
		if (tmpexaminationDate.length != 0) {
			addAnnual();// 添加车辆年审记录
		} else if (tmpoilLiter.length != 0) {
			addOiling();// 添加加油记录
		} else if (tmppayDate.length != 0) {
			addToll();// 过路费记录添加
		} else if (tmpvehicleDepreciatione.length != 0) {
			addExpense();// 费用记录支出添加
		} else if (tmpinsuranceNoi.length != 0) {
			addInsurance(); // 保险费用添加
		} else if (tmpmaintenanceDatev.length != 0) {
			addVehicles();// 车辆维护记录添加
		} else if (dispatchDated.length != 0) {
			addDispatchMoney();// 配送金额添加
		}

	}
}

/**
 * 添加车辆年审记录
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
							Ext.Msg.alert('提示', "操作成功!");
							return;
						}

					} else {
						Ext.Msg.alert('提示', "操作失败!");
						return;
					}
				},
				failure : function(request) {
					Ext.Msg.alert('提示', "操作失败!");
				}
			});
}
/**
 * 添加加油记录
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
					Ext.Msg.alert('提示', "操作成功!");
					return;
				}
			} else {
				Ext.Msg.alert('提示', "操作失败!");
				return;
			}
		},
		failure : function(request) {
			Ext.Msg.alert('提示', "操作失败!");
		}
	});
}
/**
 * 过路费记录添加
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
					Ext.Msg.alert('提示', "操作成功!");
					return;
				}
			} else {
				Ext.Msg.alert('提示', "操作失败!");
				return;
			}
		},
		failure : function(request) {
			Ext.Msg.alert('提示', "操作失败!");
		}
	});
}
/**
 * 费用记录支出添加
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
					Ext.Msg.alert('提示', "操作成功!");
					return;
				}
			} else {
				Ext.Msg.alert('提示', "操作失败!");
				return;
			}
		},
		failure : function(request) {
			Ext.Msg.alert('提示', "操作失败!");
		}
	});
}
/**
 * 保险费用添加
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
					Ext.Msg.alert('提示', "操作成功!");
					return;
				}
			} else {
				Ext.Msg.alert('提示', "操作失败!");
				return;
			}
		},
		failure : function(request) {
			Ext.Msg.alert('提示', "操作失败!");
		}
	});
}
/**
 * 车辆维护记录添加
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
							Ext.Msg.alert('提示', "操作成功!");
							return;
						}
					} else {
						Ext.Msg.alert('提示', "操作失败!");
						return;
					}
				},
				failure : function(request) {
					Ext.Msg.alert('提示', "操作失败!");
				}
			});
}
/**
 * 配送金额记录添加
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
				Ext.Msg.alert('提示', '操作成功');
				return;
			} else {
				// store.reload();
				Ext.Msg.alert('提示', "操作失败!");
				return;
			}
		},
		failure : function(request) {
			Ext.Msg.alert('提示', "操作失败!");
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
				title : '车辆基本信息添加',
				region : 'center',
				labelWidth : 300,
				frame : true,
				autoScroll : true,
				// margins : '5 5 5 5',
				width : 500,
				items : [ {// 行1
					layout : 'column',
					items : [
					// 列
					{
						columnWidth : .33,
						layout : 'form',
						xtype : 'fieldset',
						border : false,
						title : '车辆年审记录添加',
						// autoHeight : true,
						width : 250,
						labelWidth : 100,
						items : [ {
							fieldLabel : '终端名称',
							id : 'frmParendId',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : '年审日期',
							xtype : 'datefield',
							id : 'frmexaminationDate',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							fieldLabel : '年审项目',
							id : 'frmproject',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '年审情况',
							id : 'frmcondition',
							width : 150,
							xtype : 'textfield'
						}, {
							fieldLabel : '年审金额',
							xtype : 'numberfield',
							id : 'frmexpenses',
							width : 150
						}, {
							fieldLabel : '经手人',
							id : 'frmhandler',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '到期日期',
							xtype : 'datefield',
							id : 'frmexpireDate',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							fieldLabel : '备注',
							id : 'frmdemo',
							xtype : 'textfield',
							width : 150
						}, {
							xtype : 'hidden',
							id : 'frmdeviceId'
						} ]
					},
					// 列
					{
						columnWidth : .3,
						layout : 'form',
						xtype : 'fieldset',
						border : false,
						title : '费用记录支出添加',
						// autoHeight : true,
						width : 250,
						labelWidth : 100,
						items : [ {
							fieldLabel : '终端名称',
							id : 'frmdevicee',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : '车辆折旧费用',
							xtype : 'numberfield',
							id : 'frmvehicleDepreciatione',
							width : 150
						}, {
							fieldLabel : '人员费用',
							xtype : 'numberfield',
							id : 'frmpersonalExpensese',
							width : 150
						}, {
							fieldLabel : '保险摊销费用',
							xtype : 'numberfield',
							id : 'frminsurancee',
							width : 150
						}, {
							fieldLabel : '维修保养费用',
							xtype : 'numberfield',
							id : 'frmmaintenancee',
							width : 150
						}, {
							fieldLabel : '过路过桥费',
							xtype : 'numberfield',
							id : 'frmtolle',
							width : 150
						}, {
							fieldLabel : '年检及其他费用',
							xtype : 'numberfield',
							id : 'frmannualChecke',
							width : 150
						}, {
							fieldLabel : '日期',
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
				}, {// 行2
					layout : 'column',
					items : [
					// 列
					{
						columnWidth : .33,
						layout : 'form',
						xtype : 'fieldset',
						border : false,
						title : '过路费记录添加',
						// autoHeight : true,
						width : 300,
						labelWidth : 100,
						items : [ {
							fieldLabel : '终端名称',
							id : 'frmdevicet',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : '交费日期',
							xtype : 'datefield',
							id : 'frmpayDatet',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							fieldLabel : '交费地点',
							id : 'frmpayPlacet',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '交费金额',
							id : 'frmexpensest',
							xtype : 'numberfield',
							width : 150
						}, {
							fieldLabel : '经手人',
							id : 'frmhandlert',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '备注',
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
					// 列
					{
						columnWidth : .3,
						layout : 'form',
						border : false,
						xtype : 'fieldset',
						title : '车辆维护记录添加',
						// autoHeight : true,
						width : 300,
						labelWidth : 100,
						items : [ {
							fieldLabel : '终端名称',
							id : 'frmdevicev',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : '维护日期',
							id : 'frmmaintenanceDatev',
							width : 150,
							xtype : 'datefield',
							editable : false,
							format : 'Y-m-d'
						}, {
							fieldLabel : '维护费用',
							id : 'frmexpensesv',
							xtype : 'numberfield',
							width : 150
						}, {
							fieldLabel : '维护情况',
							id : 'frmconditionv',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '经手人',
							id : 'frmhandlerv',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '到期日期',
							xtype : 'datefield',
							id : 'frmexpireDatev',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							fieldLabel : '备注',
							xtype : 'textfield',
							id : 'tmpdemov',
							width : 150
						}, {
							xtype : 'hidden',
							id : 'frmdeviceId5'
						} ]
					} ]
				}, {
					// 行3
					layout : 'column',
					items : [
					// 列
					{
						columnWidth : 0.33,
						layout : 'form',
						border : false,
						xtype : 'fieldset',
						title : '配送金额添加',
						// autoHeight : true,
						width : 300,
						labelWidth : 100,
						items : [ {
							fieldLabel : '终端名称',
							id : 'frmdeviced',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : '配送日期',
							id : 'dispatchDated',
							width : 150,
							xtype : 'datefield',
							editable : false,
							format : 'Y-m-d'
						}, {
							fieldLabel : '配送数量',
							id : 'dispatchAmountd',
							xtype : 'numberfield',
							width : 150
						}, {
							fieldLabel : '经手人',
							id : 'frmhandlerd',
							xtype : 'textfield',
							width : 150
						}, {
							xtype : 'textarea',
							fieldLabel : '备注',
							xtype : 'textfield',
							id : 'frmdemod',
							width : 150
						}, {
							xtype : 'hidden',
							id : 'frmdeviceId6'
						} ]
					},
					// 列
					{
						columnWidth : .3,
						layout : 'form',
						xtype : 'fieldset',
						border : false,
						title : '加油记录添加',
						// autoHeight : true,
						width : 300,
						labelWidth : 100,
						items : [ {
							fieldLabel : '终端名称',
							id : 'frmdeviceo',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : '加油量(升)',
							xtype : 'numberfield',
							id : 'frmoilLiter',
							width : 150
						}, {
							fieldLabel : '加油金额',
							xtype : 'numberfield',
							id : 'frmoilCost',
							width : 150
						}, {
							fieldLabel : '加油日期',
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

					// 行3
					layout : 'column',
					items : [
					// 列
					{
						columnWidth : 1,
						layout : 'form',
						xtype : 'fieldset',
						border : false,
						title : '保险费用记录添加',
						// autoHeight : true,
						width : 300,
						labelWidth : 100,
						items : [ {
							fieldLabel : '终端名称',
							id : 'frmdevicei',
							xtype : 'textfield',
							width : 150,
							readOnly : true
						}, {
							fieldLabel : '保险单号',
							id : 'frminsuranceNoi',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '保险名称',
							id : 'frminsuranceNamei',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '保险公司',
							id : 'frminsuranceCoi',
							xtype : 'textfield',
							width : 150
						}, {
							fieldLabel : '投保日期',
							xtype : 'datefield',
							id : 'frminsuranceDatei',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							fieldLabel : '到期日期',
							xtype : 'datefield',
							id : 'frmexpireDatei',
							editable : false,
							format : 'Y-m-d',
							width : 150
						}, {
							fieldLabel : '保额',
							xtype : 'numberfield',
							id : 'frmsumInsuredi',
							width : 150
						}, {
							fieldLabel : '保费',
							xtype : 'numberfield',
							id : 'frmpremiumi',
							width : 150
						}, {
							fieldLabel : '经手人',
							id : 'frmhandleri',
							xtype : 'textfield',
							width : 150
						}, {
							xtype : 'hidden',
							id : 'frmdeviceId4'
						} ]
					},
					// 列
					{
					// 待添加
					} ]

				} ],
				tbar : toolbar
			});
			root = new Ext.tree.AsyncTreeNode({
				text : '定位平台',
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
					// 车辆年审记录
					Ext.getCmp('frmParendId').setValue(node.text);
					Ext.getCmp('frmdeviceId').setValue(deviceIdTmp);
					// 加油记录
					Ext.getCmp('frmdeviceo').setValue(node.text);
					Ext.getCmp('frmdeviceId1').setValue(deviceIdTmp);
					// 过路费记录
					Ext.getCmp('frmdevicet').setValue(node.text);
					Ext.getCmp('frmdeviceId2').setValue(deviceIdTmp);
					// 费用记录支出
					Ext.getCmp('frmdevicee').setValue(node.text);
					Ext.getCmp('frmdeviceId3').setValue(deviceIdTmp);
					// 保险费用
					Ext.getCmp('frmdevicei').setValue(node.text);
					Ext.getCmp('frmdeviceId4').setValue(deviceIdTmp);
					// 车辆维护记录
					Ext.getCmp('frmdevicev').setValue(node.text);
					Ext.getCmp('frmdeviceId5').setValue(deviceIdTmp);
					// 配送金额
					Ext.getCmp('frmdeviced').setValue(node.text);
					Ext.getCmp('frmdeviceId6').setValue(deviceIdTmp);

				}
			});
		});
