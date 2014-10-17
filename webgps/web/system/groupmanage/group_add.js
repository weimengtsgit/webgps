var root;
var tree;
var treeload;
/**
 *��Ӱ�ť
 **/
var addbut = new Ext.Action({
	text : '����',
	id : 'addbut',
	handler : function() {
		var tmpfrmGroupName = Ext.getCmp('frmGroupName').getValue();
		if (tmpfrmGroupName == '') {
			parent.Ext.MessageBox.alert('��ʾ', '�����벿������!');
			return;
		}
		var tmpfrmParendId = Ext.getCmp('frmParendId').getValue();
		if (tmpfrmParendId == '') {
			parent.Ext.MessageBox.alert('��ʾ', '��ѡ����������!');
			return;
		}
		var tmpfrmstarttime = Ext.getCmp('frmstarttime').getValue();
		if (tmpfrmstarttime == '') {
			parent.Ext.MessageBox.alert('��ʾ', '��ѡ���ϰ�ʱ��!');
			return;
		}
		var tmpfrmendtime = Ext.getCmp('frmendtime').getValue();
		if (tmpfrmendtime == '') {
			parent.Ext.MessageBox.alert('��ʾ', '��ѡ���°�ʱ��!');
			return;
		}
		parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ����²�����?', addConfirm);
	},
	iconCls : 'icon-add'
});
/**
 *���ȷ���ύ
 **/
function addConfirm(btn) {
	if (btn == 'yes') {
		parent.Ext.Msg.show({
			msg : '���ڱ��� ���Ե�...',
			progressText : '����...',
			width : 300,
			wait : true,
			icon : 'ext-mb-download'
		});
		add();
	}
}

//��Ӳ���
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
				parent.Ext.Msg.alert('��ʾ', '����ɹ�');
				return;
			} else {
				parent.Ext.Msg.alert('��ʾ', "����ʧ��!");
				return;
			}
		},
		failure : function(request) {
			parent.Ext.Msg.alert('��ʾ', "����ʧ��!");
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
			fieldLabel : '��������',
			id : 'frmParentGroupName',
			readOnly : true
		}, {
			fieldLabel : '��������',
			id : 'frmGroupName',
			allowBlank : false,
			enableKeyEvents : true
		}, new Ext.form.TimeField({
			id : 'frmstarttime',
			fieldLabel : '�ϰ�ʱ��',
			format : 'H:i',
			value : '08:00',
			increment : 1
		}), new Ext.form.TimeField({
			id : 'frmendtime',
			fieldLabel : '�°�ʱ��',
			format : 'H:i',
			value : '18:00',
			increment : 1
		}),{
			id: 'weekCheckBoxGroup',
			xtype: 'checkboxgroup',
			fieldLabel: '��������',
			width: 350,
			items: [
				{boxLabel: '��1', name: 'cb-auto-1', checked: true, value:'1'},
				{boxLabel: '��2', name: 'cb-auto-2', checked: true, value:'2'},
				{boxLabel: '��3', name: 'cb-auto-3', checked: true, value:'3'},
				{boxLabel: '��4', name: 'cb-auto-4', checked: true, value:'4'},
				{boxLabel: '��5', name: 'cb-auto-5', checked: true, value:'5'},
				{boxLabel: '��6', name: 'cb-auto-6', checked: true, value:'6'},
				{boxLabel: '����', name: 'cb-auto-7', checked: true, value:'7'}
			]
		}, new Ext.form.Hidden({
			id : 'frmParendId'
	})],
	tbar : toolbar
});
root = new Ext.tree.AsyncTreeNode({
	text : '��λƽ̨',
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
