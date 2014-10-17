<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	response.sendRedirect(path+"/error.jsp");
	return;
}
%>
<html>
<head>
  <title></title>
	<link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/buttons.css"/>
 	<script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
	<script type="text/javascript" src="<%=path%>/jstool/provincescities.js"></script>
	
	<script type="text/javascript">
	Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
	var path = '<%=path%>';


var delbut = new Ext.Action({
		        text: '�޸�',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
		        	
		        	var tmpfrmId = Ext.getCmp('frmId').getValue();
		        	if(tmpfrmId==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '��ѡ���ʺ�!');
		        		return ;
		        	}
		        	
					var tmpfrmuserAccount = Ext.getCmp('frmuserAccount').getValue();
		        	if(tmpfrmuserAccount==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '�������¼�ʺ�!');
		        		return ;
		        	}
		        	var tmpfrmuserName = Ext.getCmp('frmuserName').getValue();
		        	if(tmpfrmuserName==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '�������û���!');
		        		return ;
		        	}
		        	
		        	var tmpPassword = Ext.getCmp('frmPassword').getValue();
		        	var tmpConfirmPassword = Ext.getCmp('frmConfirmPassword').getValue();
		        	if(tmpPassword==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '����������!');
		        		return ;
		        	}
		        	if(tmpPassword!=tmpConfirmPassword){
		        		parent.Ext.MessageBox.alert('��ʾ', '�����������벻һ��,����������!');
		        		return ;
		        	}
		        	
		        	var tmpcontrolPassword = Ext.getCmp('frmcontrolPassword').getValue();
		        	var tmpcontrolConfirmPassword = Ext.getCmp('frmcontrolConfirmPassword').getValue();
		        	/*if(tmpcontrolPassword==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '��������Ͷϵ�����!');
		        		return ;
		        	}*/
		        	if(tmpcontrolPassword!=tmpcontrolConfirmPassword){
		        		parent.Ext.MessageBox.alert('��ʾ', '���ζ��Ͷϵ��������벻һ��,����������!');
		        		return ;
		        	}
		        	
		        	var tmproleid = comboxroleid.getValue();
		        	if(tmproleid==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '��ѡ���ɫ!');
		        		return ;
		        	}
		        	//Ext.getCmp('frmroleid').setValue(tmproleid);
		        	var tmpfrmprovince = Ext.getCmp('frmprovince').getValue();
		        	/*if(tmpfrmprovince==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '��ѡ��ʡ!');
		        		return ;
		        	}*/
		        	vprovince = tmpfrmprovince;
		        	var tmpfrmcity = Ext.getCmp('frmcity').getValue();
		        	/*if(tmpfrmcity==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '��ѡ����!');
		        		return ;
		        	}*/
		        	
		        	setTreeGroupIds('');
					getTreeId(root);
					if(treeGroupIds.length>0){
						treeGroupIds = treeGroupIds.substring(0,treeGroupIds.length-1);
					}else{
						//parent.Ext.MessageBox.alert('��ʾ', '��ѡ���û�����Ͻ����!');
		        		//return ;
					}
					
		        	parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�޸��ʺ���?', modifyConfirm);
		        
		        },
		        iconCls: 'icon-modify'
		    });

		    function modifyConfirm(btn){
		    	if(btn=='yes'){
		    		/*parent.Ext.Msg.show({
			           msg: '���ڱ��� ���Ե�...',
			           progressText: '����...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });*/
			       modify();
		        }
		    }
		    

//�޸Ĳ���
function modify(){
var tmpfrmId = Ext.getCmp('frmId').getValue();
var tmpfrmuserAccount = Ext.getCmp('frmuserAccount').getValue();
var tmpfrmuserName = Ext.getCmp('frmuserName').getValue();
var tmpPassword = Ext.getCmp('frmPassword').getValue();
var tmpfrmprovince = Ext.getCmp('frmprovince').getValue();
var tmpfrmcity = Ext.getCmp('frmcity').getValue();
var tmproleid = comboxroleid.getValue();

var tmpcontrolPassword = Ext.getCmp('frmcontrolPassword').getValue();

var urlstr = '&userId='+tmpfrmId+'&userName='+tmpfrmuserName+ '&userAccount='+tmpfrmuserAccount+ 
'&password='+tmpPassword+ 
'&province='+tmpfrmprovince+ '&city='+tmpfrmcity+ 
'&groupIds='+treeGroupIds+'&roleId='+tmproleid+'&controlPassword='+tmpcontrolPassword;
//alert(urlstr);
	Ext.Ajax.request({
		 //url :url,
		 url:path+'/system/user.do?method=updateUser',
		 method :'POST', 
		 params: { userId: encodeURI(tmpfrmId), userName : encodeURI(tmpfrmuserName) ,
		 userAccount : encodeURI(tmpfrmuserAccount),
		 password : encodeURI(tmpPassword), province : encodeURI(tmpfrmprovince), 
		 city : encodeURI(tmpfrmcity),groupIds : encodeURI(treeGroupIds), 
		 roleId : encodeURI(tmproleid), controlPassword : encodeURI(tmpcontrolPassword)
		 },
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		var cursor = userGrid.getBottomToolbar().cursor;
		 		store.load({params:{start:cursor, limit:20, searchValue: encodeURI(searchValue) }});
		   		parent.Ext.Msg.alert('��ʾ', '�޸ĳɹ�');
		   		return;
		   }else if(res.result==3){
		   		//store.reload();
		   		parent.Ext.Msg.alert('��ʾ', "�Ѵ��ڴ˵�¼�ʺ�!");
		   		return;
		   }else if(res.result==4){
		   		//store.reload();
		   		parent.Ext.Msg.alert('��ʾ', "�Ѵ��ڴ��û�����!");
		   		return;
		   }else{
		   		//store.reload();
		   		parent.Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
		 }
		});
}


	var root = new Ext.tree.AsyncTreeNode({
		text : '��λƽ̨',
		id : '-100',
		draggable : false // ���ڵ㲻�����϶�
	});
	
    var treeload = new Ext.tree.TreeLoader({
    		dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForUserchecked'	
	});
	//treeload.load();
    var tree = new Ext.tree.TreePanel({
                //region: 'west',
                id: 'west-panel', // see Ext.getCmp() below
                title: '�û�����Ͻ����',
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
            });
      //��ɫ
	  var rolesStore = new Ext.data.SimpleStore({
	  	fields:['roleid', 'rolename'],
	  	data:[[]]
	  });
	  //��ɫ
      var comboxroleid = new Ext.form.ComboBox({   
        store : rolesStore,   
        editable:false,   
        mode: 'local',   
        triggerAction:'all',   
        fieldLabel: '��ɫ����',
        id : 'selectroleid',
        displayField:'rolename',
        valueField :'roleid',
        emptyText:'ѡ���ɫ',
        maxHeight: 200
    });
    
   var simple = new Ext.FormPanel({
            
	        labelWidth: 120, // label settings here cascade unless overridden
	        //url:'save-form.php',
	        frame:true,
	        title: '������Ϣ',
	        bodyStyle:'padding:5px 5px 0',
	        width: 200,
	        defaults: {width: 150},
	        defaultType: 'textfield',
	        items: [{
	        		//xtype : 'textfield',
	                fieldLabel: '�û�����',
	                id: 'frmuserName',
	                width: 150,
	                allowBlank:false
	            },{
	        		//xtype : 'textfield',
	                fieldLabel: '��¼�ʺ�',
	                id: 'frmuserAccount',
	                width: 150,
	                allowBlank:false
	            },
	            {
	                fieldLabel: '����',
	                id: 'frmPassword',
	                name : 'frmPassword',
	                inputType: 'password',
	                width: 150,
	                allowBlank:false
	            },{
	                fieldLabel: 'ȷ������',
	                id: 'frmConfirmPassword',
	                name: 'frmConfirmPassword',
	                inputType: 'password',
	                width: 150,
	                //vtype: 'password',
	                //initialPassField: 'frmPassword', // id of the initial password field
	                allowBlank:false
	            },{
	                fieldLabel: '���Ͷϵ�����',
	                id: 'frmcontrolPassword',
	                inputType: 'password',
	                width: 150
	                //allowBlank:false
	            },{
	                fieldLabel: '���Ͷϵ�ȷ������',
	                id: 'frmcontrolConfirmPassword',
	                inputType: 'password',
	                width: 150
	                //vtype: 'password',
	                //initialPassField: 'frmPassword', // id of the initial password field
	                //allowBlank:false
	            },
	            //��ɫ
	            comboxroleid,
	            //ʡ
	            comboProvinces,
	            //��
	            comboCities,
	            new Ext.form.Hidden({
                	id:'frmId'
                }),
	            new Ext.form.Hidden({
                	id:'frmroleid'
                })
	        ]
	    });

var proxy = new Ext.data.HttpProxy({
    url: path+'/system/user.do?method=listUser'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'userName'},
    {name: 'userAccount'},
    {name: 'province'},
    {name: 'city'},
    {name: 'createDate'},
    {name: 'password'},
    {name: 'controlPassword'}
    
]);
var searchValue = '';
var store = new Ext.data.Store({
	autoLoad: {params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) }},
    id: 'userstore',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				searchValue: encodeURI(searchValue)
    			}
    		}
    	}
    }
});

var sm = new Ext.grid.RowSelectionModel({
	singleSelect: true,
	listeners: {
		rowselect: function(sm, row, rec) {
			Ext.getCmp('frmuserAccount').setValue(rec.get('userAccount'));
			Ext.getCmp('frmuserName').setValue(rec.get('userName'));
			Ext.getCmp('frmPassword').setValue(rec.get('password'));
		    Ext.getCmp('frmConfirmPassword').setValue(rec.get('password'));
		    
		    Ext.getCmp('frmcontrolPassword').setValue(rec.get('controlPassword'));
		    Ext.getCmp('frmcontrolConfirmPassword').setValue(rec.get('controlPassword'));
		    var province = rec.get('province');
		    if(province.length > 0 && province!=null){
		    	comboProvinces.setValue(rec.get('province'));
		    }
		    var city = rec.get('city');
		    if(city.length > 0 && city!=null){
		    	comboCities.setValue(rec.get('city'));
		    }
		    
		    Ext.getCmp('frmId').setValue(rec.get('id'));
		    queryRoleidGroupid(rec.get('id'));
			
	    }
	}
});

function queryRoleidGroupid(userId){

	 parent.Ext.Msg.show({
		msg: '���Ե�...',
		//progressText: 'ɾ��...',
		width:300,
		wait:true,
		//waitConfig: {interval:200},
		icon:'ext-mb-download'
	});
	
	
	Ext.Ajax.request({
		 url:path+'/system/user.do?method=queryUser&userId='+userId,
		 method :'POST', 
		  success : function(request) {
		   var res = Ext.decode(request.responseText);
		    //alert('1:'+request.responseText);
		    //alert('2:'+res);
		 	var tmproleid = res.roleId.split(',');
		 	Ext.getCmp('frmroleid').setValue(tmproleid[0]);
		 	comboxroleid.setValue(tmproleid[0]);
		 	var tmpgroupids = res.groupIds;
		 	//alert(tmpgroupids);
		 	//if(tmpgroupids.length>0){
		 		checkEachMod(root,tmpgroupids);
		 	//}
		 	parent.Ext.Msg.hide();
		 },
		 failure : function(request) {
			 //parent.Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
			 parent.Ext.Msg.hide();
		 }
		});
}

function checkEachMod(node,groupIds){
	node.expand();
	var treeid = node.id;
	var idArr = treeid.split('@');
	var id = ','+idArr[0]+',';
	if(groupIds.indexOf(id)!=-1){
		node.getUI().toggleCheck(true);
	}else{
		node.getUI().toggleCheck(false);
	}
	node.eachChild(function(child) {
		checkEachMod(child,groupIds);
	});
}

var userColumns =  [
	new Ext.grid.RowNumberer({header:'���',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "�û�����", width: 100, sortable: true, dataIndex: 'userName'},
    {header: "��¼�ʺ�", width: 100, sortable: true, dataIndex: 'userAccount'},
    {header: "ʡ", width: 100, sortable: true, dataIndex: 'province'},
    {header: "��", width: 100, sortable: true, dataIndex: 'city'},
    {header: "����ʱ��", width: 100, sortable: true, dataIndex: 'createDate'},
    {header: "password", width: 40, sortable: true, dataIndex: 'password',hidden:true},
    {header: "controlPassword", width: 40, sortable: true, dataIndex: 'controlPassword',hidden:true}
    
];

var userGrid = new Ext.grid.GridPanel({
        region: 'west',
        width: 450,
        loadMask: {msg:'��ѯ��...'},
        //iconCls: 'icon-grid',
        //frame: true,
        //title: 'Users',
        //autoExpandColumn: 'name',
        enableColumnHide : false,
        store: store,
        //plugins: [editor],
        columns : userColumns,
        sm : sm,
        //sm : smcheckbox,
        margins: '0 0 0 0',
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: store,
            displayInfo: true,
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
        }),
	    tbar: [
	        new Ext.form.TextField({
	            id: 'DeviceIdField',
	            width: 80,
		        enableKeyEvents: true,
		        listeners: {
	        		keypress : function( textField, e ) {
			        	if (e.getKey() == e.ENTER) {
			        		searchValue = Ext.getCmp('DeviceIdField').getValue();
			    			store.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue) }});
			    			
			            }
	        		}
	        	}
		    }),'-',new Ext.Action({
		        text: '��ѯ',
		        handler: function(){
			        searchValue = Ext.getCmp('DeviceIdField').getValue();
			    	store.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue) }});
	    			
		        },
		        iconCls: 'icon-search'
		    }),'-',delbut
	    ]
    });
    
    var tab = new Ext.TabPanel({
    	activeTab: 0,
    	region: 'center',
    	items : [
    		simple,tree
    	]
    });
    
Ext.onReady(function(){


	//store.load({params:{start:0, limit:20}});
	
    var viewport = new Ext.Viewport({layout: 'border',items: [tab,userGrid]});
    
    //��ɫ�����б�չ��ʱȥ��ѯ
    //comboxroleid.on('expand',function(){   
    //    rolecomboexpand();
    //});  
    rolecomboexpand();
    tab.activate(tree);
    tab.activate(simple);
    
        tree.on('checkchange',function(node,checked){
	    /*if(node.isLeaf()&&checked==true){
	    }else if(node.isLeaf()&&checked==false){
	    }*/
	    node.expand();
	    node.attributes.checked = checked;
	    
	    node.on('expand',function(node){
			//alert('1:'+node);
			node.eachChild(function(child){
		    	child.ui.toggleCheck(checked);
		        child.attributes.checked = checked;
		        child.fireEvent('checkchange',child,checked);
		    });
		});
	
	    node.eachChild(function(child){
	    	//alert('2:'+child);
	    	child.ui.toggleCheck(checked);
	        child.attributes.checked = checked;
	        child.fireEvent('checkchange',child,checked);
	    });
	},tree);
});

//��ɫ�����б�չ��ʱȥ��ѯ
function rolecomboexpand(){
	
	Ext.Ajax.request({
		 url : path+'/system/role.do?method=comboboxRole',
		 method :'POST',
		 success : function(request) {
		   var res = request.responseText;
		   //alert(res);
		   rolesStore.loadData(eval(res));
		 },
		 failure : function(request) {
		 }
		});
}

var treeGroupIds = '';
function setTreeGroupIds(ids){
	treeGroupIds = ids;
}
function getTreeId(node){
	//node.expand();
	//416@#��ͼ����1@#MAP_OPR@##@#��ͼ����
	var treeid = node.id;
	var idArr = treeid.split('@');
	//if(node.isLeaf()&&node.getUI().isChecked()){
	if(node.getUI().isChecked()){
		treeGroupIds = treeGroupIds + idArr[0] + ',';
		setTreeGroupIds(treeGroupIds);
	}
	
	node.eachChild(function(child) {
		getTreeId(child);
	});
}		

	</script>
</head>
<body>
</body>
</html>