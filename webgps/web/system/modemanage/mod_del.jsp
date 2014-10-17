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
		        text: 'ɾ��',
		        id : 'addbut',
		        handler: function(){

		        	var tmpfrmModuleId = Ext.getCmp('frmModuleId').getValue();
		        	if(tmpfrmModuleId==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '��ѡ��Ȩ��!');
		        		return ;
		        	}
		        	if(tmpfrmModuleId=='-101'){
		        		parent.Ext.MessageBox.alert('��ʾ', '��Ȩ����Ϣ����ɾ��!');
		        		return ;
		        	}
		            parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ��Ȩ����?', addConfirm);
		        },
		        iconCls: 'icon-del'
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
			           msg: '����ɾ�� ���Ե�...',
			           progressText: 'ɾ��...',
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
	
var tmpfrmParentModuleId = Ext.getCmp('frmModuleId').getValue();
	
	if(tmpfrmParentModuleId == '-101'){
		parent.Ext.Msg.alert('��ʾ', '��Ȩ����Ϣ����ɾ��!');
		return;
	}
	
	Ext.Ajax.request({
		 //url :url,
		 url:path+'/system/module.do?method=deleteModules',
		 method :'POST', 
		 params: { moduleIds: tmpfrmParentModuleId},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		 	if(res.result==2){
		   		parent.Ext.Msg.alert('��ʾ', 'ɾ��ʧ��');
		   		return;
		   }else if(res.result==1){
		   		//store.reload();
		   		
		   		parent.Ext.Msg.alert('��ʾ', "ɾ���ɹ�!");
		   		treeload.load(root);
		   		return;
		   }else if(res.result==3){
		   		//store.reload();
		   		parent.Ext.Msg.alert('��ʾ', "�Ѵ��ڴ�Ȩ�ޱ���,����������Ȩ�ޱ���!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('��ʾ', "ɾ��ʧ��!");
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
	                fieldLabel: 'Ȩ������',
	                id: 'frmModuleName',
	                allowBlank:false,
	                readOnly:true
	                //msgTarget : 'under',
	                //enableKeyEvents : true
	                //listeners:{
	                //	keypress : function(t, e) {
	                //	}
	                //}
	            },{
	                fieldLabel: 'Ȩ�ޱ���',
	                readOnly:true,
	                id: 'frmModuleCode'
	                //msgTarget : 'under',
	                //allowBlank:false
	            },new Ext.form.TextArea({
	                fieldLabel: 'Ȩ��·��',
	                readOnly:true,
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
                	readOnly:true,
                	//msgTarget : 'under',
                	id:'frmModuleDesc'
                	//allowBlank:false
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
        		Ext.getCmp('frmModuleId').setValue('-101');
        		Ext.getCmp('frmModuleName').setValue(node.text);
        	}else{
        		var idArr = node.id.split('@#');
        		var tmpid = idArr[0];
        		var tmpModuleName = idArr[1];
        		var tmpModuleCode = idArr[2];
        		var tmpModulePath = idArr[3];
        		var tmpMoudleDesc = idArr[4];
        		var tmpModuleGrade = idArr[5];
        		
        		Ext.getCmp('frmModuleName').setValue(tmpModuleName);
        		Ext.getCmp('frmModuleCode').setValue(tmpModuleCode);
        		Ext.getCmp('frmModuleUrl').setValue(tmpModulePath);
        		Ext.getCmp('frmModuleDesc').setValue(tmpMoudleDesc);
        		Ext.getCmp('frmModuleGrade').setValue(tmpModuleGrade);
        		Ext.getCmp('frmModuleId').setValue(tmpid);
        		
        	}
        
    });
});

function resetForm(){
	//Ext.getCmp('frmParentModuleName').reset();
    Ext.getCmp('frmModuleName').reset();
    Ext.getCmp('frmModuleCode').reset();
    Ext.getCmp('frmModuleUrl').reset();
    Ext.getCmp('frmModuleDesc').reset();
    Ext.getCmp('frmModuleId').reset();
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