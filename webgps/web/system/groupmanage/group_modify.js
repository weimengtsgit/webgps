
    var root;
    var tree;
    var treeload;
    
	
       
/**
*添加按钮
**/
var addbut = new Ext.Action({
	text: '修改',
		        id : 'addbut',
		        handler: function(){
		        	var tmpfrmGroupName = Ext.getCmp('frmGroupName').getValue();
		        	
		        	if(tmpfrmGroupName==''){
		        		parent.Ext.MessageBox.alert('提示', '请输入部门名称!');
		        		return ;
		        	}
		        	var tmpfrmfrmId = Ext.getCmp('frmId').getValue();
		        	
		        	if(tmpfrmfrmId==''){
		        		parent.Ext.MessageBox.alert('提示', '请选择部门!');
		        		return ;
		        	}
		        	if(tmpfrmfrmId=='-101'){
		        		parent.Ext.MessageBox.alert('提示', '此组信息不能修改!');
		        		return ;
		        	}
		        	var tmpfrmstarttime = Ext.getCmp('frmstarttime').getValue();
		        	
		        	if(tmpfrmstarttime==''){
		        		parent.Ext.MessageBox.alert('提示', '请选择上班时间!');
		        		return ;
		        	}
		        	var tmpfrmendtime = Ext.getCmp('frmendtime').getValue();
		        	
		        	if(tmpfrmendtime==''){
		        		parent.Ext.MessageBox.alert('提示', '请选择下班时间!');
		        		return ;
		        	}
		        	parent.Ext.MessageBox.confirm('提示', '您确定要修改部门吗?', addConfirm);
		        },
		        iconCls: 'icon-modify'
		    });


/**
*添加确定提交
**/
		    function addConfirm(btn){
		    	if(btn=='yes'){
		    		parent.Ext.Msg.show({
			           msg: '正在保存 请稍等...',
			           progressText: '保存...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			       add();
		        }
		    }

//添加部门
function add(){
	
	var tmpfrmGroupName = Ext.getCmp('frmGroupName').getValue();
	var tmpfrmId = Ext.getCmp('frmId').getValue();
	var tmpfrmstarttime = Ext.getCmp('frmstarttime').getValue();
	var tmpfrmendtime = Ext.getCmp('frmendtime').getValue();
	var tmpfilterdeviate = Ext.getCmp('filterdeviate').getValue().getGroupValue();
	var tmpfrmWeekArr = Ext.getCmp('weekCheckBoxGroup').getValue();
	var weekNum = 0;
	while(tmpfrmWeekArr.length>0){
		var checkBox = tmpfrmWeekArr.pop();
		var checkedValue = checkBox.value;
		weekNum += Math.pow(2,checkedValue-1);
	}
	
	Ext.Ajax.request({
		 //url :url,
		 url:path+'/group/group.do?method=tTargetGroupCtl',
		 method :'POST', 
		 params: { ctl:'modify', groupname: encodeURI(tmpfrmGroupName), starttime : tmpfrmstarttime , endtime : tmpfrmendtime , groupId : tmpfrmId , groupstatus : tmpfilterdeviate , week: weekNum},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(res.result);
		 	if(res.result==1){
		   		treeload.load(root);
		   		parent.Ext.Msg.alert('提示', "修改成功!");
		   }else{
		   		//store.reload();
		   		parent.Ext.Msg.alert('提示', '修改失败');
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('提示', "修改失败!");
		 }
		});
}

        var toolbar = new Ext.Toolbar({
        	id :'frmtbar',
        	items : [
        		addbut
        	]
        });





 Ext.onReady(function(){
  	Ext.QuickTips.init();
  	Ext.form.Field.prototype.msgTarget = 'under';
  	
		var simple = new Ext.FormPanel({
            region: 'center',
	        labelWidth: 75, // label settings here cascade unless overridden
	        //url:'save-form.php',
	        frame:true,
	        margins:'5 5 5 5',
	        //title: '模块设置',
	        //bodyStyle:'padding:5px 5px 0',
	        width: 250,
	        defaults: {width: 230},
	        defaultType: 'textfield',
	        items: [{
	                fieldLabel: '部门名称',
	                //width: 120,
	                id: 'frmGroupName',
	                allowBlank:false,
	                //msgTarget : 'under',
	                enableKeyEvents : true
	                //listeners:{
	                //	keypress : function(t, e) {
	                //	}
	                //}
	            },new Ext.form.TimeField({
	            	id:'frmstarttime',
	            	fieldLabel: '上班时间',
	            	format:'H:i',
	            	value:'08:00',
	            	//width: 120,
				    increment: 1
	            	
	            }),new Ext.form.TimeField({
	            	id:'frmendtime',
	            	fieldLabel: '下班时间',
	            	format:'H:i',
	            	value:'18:00',
	            	//width: 120,
				    increment: 1
	            	
	            }),{
			        id: 'weekCheckBoxGroup',
				    xtype: 'checkboxgroup',
				    fieldLabel: '工作日期',
			        width: 350,
				    items: [
						{boxLabel: '周1', name: 'cb-auto-1', value:'1'},
						{boxLabel: '周2', name: 'cb-auto-2', value:'2'},
						{boxLabel: '周3', name: 'cb-auto-3', value:'3'},
						{boxLabel: '周4', name: 'cb-auto-4', value:'4'},
						{boxLabel: '周5', name: 'cb-auto-5', value:'5'},
						{boxLabel: '周6', name: 'cb-auto-6', value:'6'},
						{boxLabel: '周日', name: 'cb-auto-7', value:'7'}
				    ]
				},new Ext.form.Hidden({
                	id:'frmId'
                }),{
					xtype: 'radiogroup',
					fieldLabel: '部门状态',
					width: 130,
					id: 'filterdeviate',
					items: [
						{boxLabel: '使用', id:'useradio', name: 'filterdeviate', inputValue: 1, checked: true},
					    {boxLabel: '停用', id:'stopradio', name: 'filterdeviate', inputValue: 0}
				 	]
				}
	        ],
        tbar : toolbar
	    });
	    
	root = new Ext.tree.AsyncTreeNode({
		text : '定位平台',
		id : '-100',
		draggable : false // 根节点不容许拖动
	});
	
    treeload = new Ext.tree.TreeLoader({
    		dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForEnt'	
	});
	//treeload.load();
    tree = new Ext.tree.TreePanel({
                region: 'west',
                id: 'west-panel', // see Ext.getCmp() below
                //title: '部门树',
                //height: 300,
		        width: 300,
		        margins:'5 0 5 5',
		        //useArrows:true,
		        autoScroll:true,
		        animate:true,
		        //enableDD:true,
		        containerScroll: true,
		        rootVisible: false,
		        //frame: true,
		        root: root,
		        // auto create TreeLoader
		        loader: treeload
		        /*tbar: [
		        	addbut
		        ]*/
            });
            
            //root.expand();
            
        var viewport = new Ext.Viewport({
            layout: 'border',
            items: [tree, simple ]
        });
        
        
        tree.on("click",function(node,event){
        	//i.getId()+"@"+i.getStartTime()+"@" +i.getEndTime()+"@"+i.getGroupStatus()
        	var idArr = node.id.split('@');
        	var tmpid = idArr[0];
        	var tmpstarttime = idArr[1];
        	var tmpendtime = idArr[2];
        	var tmpgroupstatus = idArr[3];
        	var tmpweek = Number(idArr[4]);
        	
        	Ext.getCmp('frmId').setValue(tmpid);
        	Ext.getCmp('frmGroupName').setValue(node.text);
        	Ext.getCmp('frmstarttime').setValue(tmpstarttime);
        	Ext.getCmp('frmendtime').setValue(tmpendtime);
        	var myCheckboxGroup = Ext.getCmp('weekCheckBoxGroup').items;
        	var cbitems = myCheckboxGroup.items;
            for (var i = 0; i < cbitems.length; i++) {
            	var checkedValue = Number(cbitems[i].value);
            	checkedValue = Math.pow(2,checkedValue-1);
            	if((tmpweek & checkedValue) == checkedValue){
            		cbitems[i].setValue(true);
            	}else{
            		cbitems[i].setValue(false);
            	}
            }
        	if( tmpgroupstatus == '1'|| tmpid == '-101'){
        		Ext.getCmp('filterdeviate').setValue(1);
        	}else{
        		Ext.getCmp('filterdeviate').setValue(0);
        	}
        	
        });
    });

