var root;
var tree;
var treeload;
/**
 *添加按钮
 **/
var addbut = new Ext.Action({
	text : '新增',
	id : 'addbut',
	handler : function() {
		var tmpfrmGroupName = Ext.getCmp('frmGroupName').getValue();
		if (tmpfrmGroupName == '') {
			parent.Ext.MessageBox.alert('提示', '请输入部门名称!');
			return;
		}
		var tmpfrmParendId = Ext.getCmp('frmParendId').getValue();
		if (tmpfrmParendId == '') {
			parent.Ext.MessageBox.alert('提示', '请选择所属部门!');
			return;
		}
		var tmpfrmstarttime = Ext.getCmp('frmstarttime').getValue();
		if (tmpfrmstarttime == '') {
			parent.Ext.MessageBox.alert('提示', '请选择上班时间!');
			return;
		}
		var tmpfrmendtime = Ext.getCmp('frmendtime').getValue();
		if (tmpfrmendtime == '') {
			parent.Ext.MessageBox.alert('提示', '请选择下班时间!');
			return;
		}
		parent.Ext.MessageBox.confirm('提示', '您确定要添加新部门吗?', addConfirm);
	},
	iconCls : 'icon-add'
});
/**
 *添加确定提交
 **/
function addConfirm(btn) {
	if (btn == 'yes') {
		parent.Ext.Msg.show({
			msg : '正在保存 请稍等...',
			progressText : '保存...',
			width : 300,
			wait : true,
			icon : 'ext-mb-download'
		});
		add();
	}
}

//添加部门
function add() {
	var tmpfrmGroupName = Ext.getCmp('frmGroupName').getValue();
	var tmpfrmParendId = Ext.getCmp('frmParendId').getValue();
	var tmpfrmstarttime = Ext.getCmp('frmstarttime').getValue();
	var tmpfrmendtime = Ext.getCmp('frmendtime').getValue();
	var tmpfrmendtime = Ext.getCmp('frmendtime').getValue();
	var tmpfrmWeekArr = Ext.getCmp('weekCheckBoxGroup').getValue();
	var weekNum = 0;
	while(tmpfrmWeekArr.length>0){
		var checkBox = tmpfrmWeekArr.pop();
		var checkedValue = checkBox.value;
		weekNum += Math.pow(2,checkedValue-1);
	}
	if (tmpfrmParendId == '-101') {
		tmpfrmParendId = '-1';
	}
	Ext.Ajax.request({
		url : path+ '/group/group.do?method=tTargetGroupCtl',
		method : 'POST',
		params : {ctl: 'add', groupname: encodeURI(tmpfrmGroupName), parentid: tmpfrmParendId, starttime: tmpfrmstarttime, endtime: tmpfrmendtime, week: weekNum},
		//timeout : 10000,
		success : function(request) {
			var res = Ext.decode(request.responseText);
			if (res.result == 1) {
				treeload.load(root);
				parent.Ext.Msg.alert('提示', '保存成功');
				return;
			} else {
				parent.Ext.Msg.alert('提示', "保存失败!");
				return;
			}
		},
		failure : function(request) {
			parent.Ext.Msg.alert('提示', "保存失败!");
		}
	});
}

var toolbar = new Ext.Toolbar({
	id : 'frmtbar',
	items : [addbut]
});

Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'under';
	var simple = new Ext.FormPanel({
		region : 'center',
		labelWidth : 75,
		frame : true,
		margins : '5 5 5 5',
		width : 250,
		defaults : {
			width : 230
		},
		defaultType : 'textfield',
		items : [{
			fieldLabel : '所属部门',
			id : 'frmParentGroupName',
			readOnly : true
		}, {
			fieldLabel : '部门名称',
			id : 'frmGroupName',
			allowBlank : false,
			enableKeyEvents : true
		}, new Ext.form.TimeField({
			id : 'frmstarttime',
			fieldLabel : '上班时间',
			format : 'H:i',
			value : '08:00',
			increment : 1
		}), new Ext.form.TimeField({
			id : 'frmendtime',
			fieldLabel : '下班时间',
			format : 'H:i',
			value : '18:00',
			increment : 1
		}),{
			id: 'weekCheckBoxGroup',
			xtype: 'checkboxgroup',
			fieldLabel: '工作日期',
			width: 350,
			items: [
				{boxLabel: '周1', name: 'cb-auto-1', checked: true, value:'1'},
				{boxLabel: '周2', name: 'cb-auto-2', checked: true, value:'2'},
				{boxLabel: '周3', name: 'cb-auto-3', checked: true, value:'3'},
				{boxLabel: '周4', name: 'cb-auto-4', checked: true, value:'4'},
				{boxLabel: '周5', name: 'cb-auto-5', checked: true, value:'5'},
				{boxLabel: '周6', name: 'cb-auto-6', checked: true, value:'6'},
				{boxLabel: '周日', name: 'cb-auto-7', checked: true, value:'7'}
			]
		}, new Ext.form.Hidden({
			id : 'frmParendId'
	})],
	tbar : toolbar
});
root = new Ext.tree.AsyncTreeNode({
	text : '定位平台',
	id : '-100',
	draggable : false
});
treeload = new Ext.tree.TreeLoader({dataUrl : path+ '/manage/termGroupManage.do?actionMethod=groupListForEnt'});
tree = new Ext.tree.TreePanel({
	region : 'west',
	id : 'west-panel',
	width : 300,
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
	items : [tree, simple]
});
tree.on("click", function(node, event) {
	//i.getId()+"@"+i.getStartTime()+"@" +i.getEndTime()+"@"+i.getGroupStatus()"@"+i.getWeek()
	var idArr = node.id.split('@');
	var tmpid = idArr[0];
	Ext.getCmp('frmParendId').setValue(tmpid);
	Ext.getCmp('frmParentGroupName').setValue(node.text);
	Ext.getCmp('frmGroupName').reset();
	Ext.getCmp('frmstarttime').reset();
	Ext.getCmp('frmendtime').reset();
});
});
