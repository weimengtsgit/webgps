
    var root;
    var tree;
    var treeload;
    
	
       
/**
*��Ӱ�ť
**/
var addbut = new Ext.Action({
	text: '�޸�',
		        id : 'addbut',
		        handler: function(){
		        	var tmpfrmGroupName = Ext.getCmp('frmGroupName').getValue();
		        	
		        	if(tmpfrmGroupName==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '�����벿������!');
		        		return ;
		        	}
		        	var tmpfrmfrmId = Ext.getCmp('frmId').getValue();
		        	
		        	if(tmpfrmfrmId==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '��ѡ����!');
		        		return ;
		        	}
		        	if(tmpfrmfrmId=='-101'){
		        		parent.Ext.MessageBox.alert('��ʾ', '������Ϣ�����޸�!');
		        		return ;
		        	}
		        	var tmpfrmstarttime = Ext.getCmp('frmstarttime').getValue();
		        	
		        	if(tmpfrmstarttime==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '��ѡ���ϰ�ʱ��!');
		        		return ;
		        	}
		        	var tmpfrmendtime = Ext.getCmp('frmendtime').getValue();
		        	
		        	if(tmpfrmendtime==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '��ѡ���°�ʱ��!');
		        		return ;
		        	}
		        	parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�޸Ĳ�����?', addConfirm);
		        },
		        iconCls: 'icon-modify'
		    });


/**
*���ȷ���ύ
**/
		    function addConfirm(btn){
		    	if(btn=='yes'){
		    		parent.Ext.Msg.show({
			           msg: '���ڱ��� ���Ե�...',
			           progressText: '����...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			       add();
		        }
		    }

//��Ӳ���
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
		   		parent.Ext.Msg.alert('��ʾ', "�޸ĳɹ�!");
		   }else{
		   		//store.reload();
		   		parent.Ext.Msg.alert('��ʾ', '�޸�ʧ��');
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
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
	        //title: 'ģ������',
	        //bodyStyle:'padding:5px 5px 0',
	        width: 250,
	        defaults: {width: 230},
	        defaultType: 'textfield',
	        items: [{
	                fieldLabel: '��������',
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
	            	fieldLabel: '�ϰ�ʱ��',
	            	format:'H:i',
	            	value:'08:00',
	            	//width: 120,
				    increment: 1
	            	
	            }),new Ext.form.TimeField({
	            	id:'frmendtime',
	            	fieldLabel: '�°�ʱ��',
	            	format:'H:i',
	            	value:'18:00',
	            	//width: 120,
				    increment: 1
	            	
	            }),{
			        id: 'weekCheckBoxGroup',
				    xtype: 'checkboxgroup',
				    fieldLabel: '��������',
			        width: 350,
				    items: [
						{boxLabel: '��1', name: 'cb-auto-1', value:'1'},
						{boxLabel: '��2', name: 'cb-auto-2', value:'2'},
						{boxLabel: '��3', name: 'cb-auto-3', value:'3'},
						{boxLabel: '��4', name: 'cb-auto-4', value:'4'},
						{boxLabel: '��5', name: 'cb-auto-5', value:'5'},
						{boxLabel: '��6', name: 'cb-auto-6', value:'6'},
						{boxLabel: '����', name: 'cb-auto-7', value:'7'}
				    ]
				},new Ext.form.Hidden({
                	id:'frmId'
                }),{
					xtype: 'radiogroup',
					fieldLabel: '����״̬',
					width: 130,
					id: 'filterdeviate',
					items: [
						{boxLabel: 'ʹ��', id:'useradio', name: 'filterdeviate', inputValue: 1, checked: true},
					    {boxLabel: 'ͣ��', id:'stopradio', name: 'filterdeviate', inputValue: 0}
				 	]
				}
	        ],
        tbar : toolbar
	    });
	    
	root = new Ext.tree.AsyncTreeNode({
		text : '��λƽ̨',
		id : '-100',
		draggable : false // ���ڵ㲻�����϶�
	});
	
    treeload = new Ext.tree.TreeLoader({
    		dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForEnt'	
	});
	//treeload.load();
    tree = new Ext.tree.TreePanel({
                region: 'west',
                id: 'west-panel', // see Ext.getCmp() below
                //title: '������',
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

