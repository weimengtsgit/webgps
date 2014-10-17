<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	response.sendRedirect(path+"/error.jsp");
	return;
}
String roleCode = user.getRoleCode();
String listModule = "/system/module.do?method=listUserModule&checked=false";
if(roleCode.equalsIgnoreCase("super_administrator")||roleCode.equalsIgnoreCase("defaultEntAdminRole")){
	listModule = "/system/module.do?method=listAllModule&checked=false";
}
%>
<html>
<head>
  <title></title>
	<link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/buttons.css"/>
	
 	<script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
	
	<script type="text/javascript">
	Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
	var path = '<%=path%>';
	
/**
*��ɾ���İ�ť
**/
        var addbut = new Ext.Action({
		        text: '����',
		        id : 'addbut',
		        handler: function(){
		        	var tmpfrmModuleName = Ext.getCmp('frmModuleName').getValue();
		        	if(tmpfrmModuleName==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '������Ȩ������!');
		        		return ;
		        	}
		        	var tmpfrmModuleCode = Ext.getCmp('frmModuleCode').getValue();
		        	if(tmpfrmModuleCode==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '������Ȩ�ޱ���!');
		        		return ;
		        	}
		        	var tmpfrmModuleUrl = Ext.getCmp('frmModuleUrl').getValue();
		        	if(tmpfrmModuleUrl==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '������Ȩ��·��!');
		        		return ;
		        	}
		        	/*var tmpfrmModuleDesc = Ext.getCmp('frmModuleDesc').getValue();
		        	if(tmpfrmModuleDesc==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '������Ȩ�ޱ�ע!');
		        		return ;
		        	}*/
		        	var tmpfrmParentModuleId = Ext.getCmp('frmParentModuleId').getValue();
		        	if(tmpfrmParentModuleId==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '��ѡ������Ȩ��!');
		        		return ;
		        	}
		        	
		            parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�����Ȩ����?', addConfirm);
		        },
		        iconCls: 'icon-add'
		    });
		
        /*var toolbar = new Ext.Toolbar({
        	id :'frmtbar',
        	items : [
        		addbut
        	]
        });*/


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
	
	var tmpfrmModuleName = Ext.getCmp('frmModuleName').getValue();
	var tmpfrmModuleCode = Ext.getCmp('frmModuleCode').getValue();
	var tmpfrmModuleUrl = Ext.getCmp('frmModuleUrl').getValue();
	var tmpfrmModuleDesc = Ext.getCmp('frmModuleDesc').getValue();
	var tmpfrmParentModuleId = Ext.getCmp('frmParentModuleId').getValue();
	var tmpfrmModuleGrade = Ext.getCmp('frmModuleGrade').getValue();
	
	Ext.Ajax.request({
		 //url :url,
		 url:path+'/system/module.do?method=addModule' ,
		 method :'POST', 
		 params: { moduleDesc: encodeURI(tmpfrmModuleDesc) ,moduleName: encodeURI(tmpfrmModuleName) ,parentModuleId: encodeURI(tmpfrmParentModuleId) , moduleCode : encodeURI(tmpfrmModuleCode) ,modulePath : encodeURI(tmpfrmModuleUrl) ,gradeId : tmpfrmModuleGrade },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		 	if(res.result=='2'){
		   		parent.Ext.Msg.alert('��ʾ', '����ʧ��');
		   		return;
		   }else if(res.result=='1'){
		   		//store.reload();
		   		
		   		parent.Ext.Msg.alert('��ʾ', "����ɹ�!");
		   		treeload.load(root);
		   		return;
		   }else if(res.result=='3'){
		   		//store.reload();
		   		parent.Ext.Msg.alert('��ʾ', "�Ѵ��ڴ�ģ�����,����������ģ�����!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('��ʾ', "����ʧ��!");
		 }
		});
}

 Ext.onReady(function(){
  	Ext.QuickTips.init();
  	Ext.form.Field.prototype.msgTarget = 'under';
  	
		var simple = new Ext.FormPanel({
            region: 'center',
	        labelWidth: 75, // label settings here cascade unless overridden
	        //url:'save-form.php',
	        frame:true,
	        //title: 'Ȩ������',
	        bodyStyle:'padding:5px 5px 0',
	        width: 350,
	        defaults: {width: 230},
	        defaultType: 'textfield',
	        items: [{
	                fieldLabel: '����Ȩ��',
	                id: 'frmParentModuleName',
	                //value:'Ȩ����',
	                //disabled : true,
	                //hidden : true,
	                //hideLabel : true,
	                readOnly : true
	            },{
	                fieldLabel: 'Ȩ������',
	                id: 'frmModuleName',
	                allowBlank:false,
	                //msgTarget : 'under',
	                enableKeyEvents : true
	                //listeners:{
	                //	keypress : function(t, e) {
	                //	}
	                //}
	            },{
	                fieldLabel: 'Ȩ�ޱ���',
	                id: 'frmModuleCode'
	                //msgTarget : 'under',
	                //allowBlank:false
	            },new Ext.form.TextArea({
	                fieldLabel: 'Ȩ��·��',
	                id: 'frmModuleUrl'
	                //msgTarget : 'under',
	                //allowBlank:false
	            }),{
					id:'frmModuleGrade',
					xtype: 'combo',
					fieldLabel:'Ȩ�޼���',
					editable:false, 
					width:80,
					displayField:'name',
					store: new Ext.data.ArrayStore({
						fields: ['id', 'name'],
						data : [[0,'ϵͳ����'],[2,'��̨����'],[3,'��ͼ����'],[4,'��ز���'],[5,'��������'],[6,'��Ϣ�ɼ�'],[7,'ƽ̨����']]
					}),
					displayField:'name',
					valueField: 'id',
					typeAhead: true, 
					mode: 'local', 
					forceSelection: true, 
					triggerAction: 'all',
					value: 2, 
					selectOnFocus:true
				}, new Ext.form.TextArea({
                	fieldLabel: '��ע',
                	//msgTarget : 'under',
                	id:'frmModuleDesc'
                	//allowBlank:false
                }), new Ext.form.Hidden({
                	id:'frmParentModuleId'
                }), new Ext.form.Hidden({
                	id:'frmModuleId'
                })
	        ]
        //tbar : toolbar
	    });
	
	root = new Ext.tree.AsyncTreeNode({
		text : 'Ȩ����',
		id : '-100',
		draggable : false // ���ڵ㲻�����϶�
	});
	
    treeload = new Ext.tree.TreeLoader({
    		dataUrl: path+'<%=listModule%>'
	});
	
	//treeload.load();
    tree = new Ext.tree.TreePanel({
                region: 'west',
                id: 'west-panel', // see Ext.getCmp() below
                //title: 'Ȩ����',
                //height: 300,
		        width: 300,
		        //useArrows:true,
		        autoScroll:true,
		        animate:true,
		        //enableDD:true,
		        containerScroll: true,
		        rootVisible: false,
		        //frame: true,
		        root: root,
		        // auto create TreeLoader
		        loader: treeload,
		        tbar: [
		        	addbut
		        ]
            });
            
            root.expand();
            
        var viewport = new Ext.Viewport({
            layout: 'border',
            items: [tree, simple]
        });
        
        tree.on("click",function(node,event){
        	resetForm();
        	//������ڵ�
        	if(node.id==-101){
        		Ext.getCmp('frmParentModuleId').setValue('-1');
        	}else{
        		var tmpidArr = node.id.split('@#');
        		Ext.getCmp('frmParentModuleId').setValue(tmpidArr[0]);
        	}
        	Ext.getCmp('frmParentModuleName').setValue(node.text);
        
    });
    

});

function resetForm(){
	//Ext.getCmp('frmParentModuleName').reset();
    Ext.getCmp('frmModuleName').reset();
    Ext.getCmp('frmModuleCode').reset();
    Ext.getCmp('frmModuleUrl').reset();
    Ext.getCmp('frmModuleDesc').reset();
    //Ext.getCmp('frmParentModuleId').reset();
    //Ext.getCmp('frmModuleId').reset();
    
}


	</script>
</head>
<body>
    <!-- use class="x-hide-display" to prevent a brief flicker of the content -->
    <div id="west" class="x-hide-display">
    </div>
    <div id="center1" class="x-hide-display">
    </div>

</body>
</html>