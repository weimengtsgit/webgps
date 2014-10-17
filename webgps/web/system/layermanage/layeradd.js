
var delbut = new Ext.Action({
		        text: '����',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					var layerNamefrm = Ext.getCmp('layerNamefrm').getValue();
		        	if(layerNamefrm==''){
		        		parent.Ext.MessageBox.alert('��ʾ', '������ͼ������!');
		        		return ;
		        	}
		        	
		        	/*var tmpselArr = sm.getSelections();
		        	if(tmpselArr.length<=0){
		        		parent.Ext.MessageBox.alert('��ʾ', '��ѡ��ɼ��û�!');
		        		return ;
		        	}*/

		        	parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�����ͼ����?', addConfirm);
		        
		        },
		        iconCls: 'icon-save'
		    });
		    
        var toolbar = new Ext.Toolbar({
        	id :'frmtbar',
        	items : [
        		delbut
        	]
        });
        
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
		    


//���ͼ��
function add(){
	var layerNamefrm = Ext.getCmp('layerNamefrm').getValue();
	var tmpselArr = sm.getSelections();
	var ids = '';
	for(var i=0;i<tmpselArr.length;i++){
		ids +=tmpselArr[i].get('id')+',';
	}
	
	if(ids.length > 0){
		ids = ids.substring(0, ids.length - 1);
	}
	
	var layerDescfrm = Ext.getCmp('layerDescfrm').getValue();
	var visiblefrm = Ext.getCmp('visiblefrm').getValue().getGroupValue();
	var mapLevelfrm = Ext.getCmp('mapLevelfrm').getValue();
	layerNamefrm = encodeURI(layerNamefrm);
	layerDescfrm = encodeURI(layerDescfrm);

	Ext.Ajax.request({
		 //url :url,
		 url:path+'/poi/poi.do?method=addLayer',
		 method :'POST', 
		 params: { layerName: layerNamefrm, layerDesc : layerDescfrm, visible: visiblefrm, mapLevel: mapLevelfrm, userIdss: ids},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		//store.reload();
		   		parent.Ext.Msg.alert('��ʾ', '����ɹ�');
		   		return;
		   }else{
		   		//store.reload();
		   		parent.Ext.Msg.alert('��ʾ', "����ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('��ʾ', "����ʧ��!");
		 }
		});
}


   var simple = new Ext.FormPanel({
            region: 'west',
	        labelWidth: 100, // label settings here cascade unless overridden
	        //url:'save-form.php',
	        frame:true,
	        //title: '������Ϣ',
	        bodyStyle:'padding:5px 5px 0',
	        width: 300,
	        defaults: {width: 180},
	        defaultType: 'textfield',
	        items: [{
	        		//xtype : 'textfield',
	                fieldLabel: 'ͼ������',
	                id: 'layerNamefrm',
	                width: 150,
	                allowBlank:false
	            },{
	        		//xtype : 'textfield',
	                fieldLabel: 'ͼ������',
	                id: 'layerDescfrm',
	                width: 150
	            },{
	            	xtype: 'radiogroup', 
	            	fieldLabel: '�Ƿ�ɼ�',
	            	width: 80, 
	            	id: 'visiblefrm',
					items: [{
						boxLabel: '��', 
						name: 'isShow', 
						inputValue: 1, 
						checked: true
						},{
						boxLabel: '��', 
						name: 'isShow', 
						inputValue: 0
					}]
				},{
					id:'mapLevelfrm',
					xtype: 'combo',
					fieldLabel:'ͼ���ͼ����',
					editable:false, 
					width:80,
					displayField:'name',
					store: new Ext.data.ArrayStore({
						fields: ['id', 'name'],
						data : [[11,'��'],[13,'��'],[14,'��'],[16,'�ֵ�']]
							//[[3,'3'],[4,'4'],[5,'5'],[6,'6'],[7,'7'],[8,'8'],[9,'9'],[10,'10'],
							//[11,'11'],[12,'12'],[13,'13'],[14,'14'],[15,'15'],[16,'16'],[17,'17']]
					}),
					displayField:'name',
					valueField: 'id',
					typeAhead: true, 
					mode: 'local', 
					forceSelection: true, 
					triggerAction: 'all',
					value: 16, 
					selectOnFocus:true
				}
	        ],
	        //buttons: toolbar
	        tbar:toolbar
	    });

    
var proxy = new Ext.data.HttpProxy({
    url: path+'/system/user.do?method=listUserWithOutLoginUser'
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
    {name: 'password'}
]);

var store = new Ext.data.Store({
    id: 'userstore',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader
});

var sm = new Ext.grid.CheckboxSelectionModel({});

var userColumns =  [
	sm,
	new Ext.grid.RowNumberer({header:'���',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "�û�����", width: 100, sortable: true, dataIndex: 'userName'},
    {header: "��¼�ʺ�", width: 100, sortable: true, dataIndex: 'userAccount'},
    {header: "ʡ", width: 100, sortable: true, dataIndex: 'province'},
    {header: "��", width: 100, sortable: true, dataIndex: 'city'},
    {header: "����ʱ��", width: 100, sortable: true, dataIndex: 'createDate'},
    {header: "password", width: 40, sortable: true, dataIndex: 'password',hidden:true}
];

var userGrid = new Ext.grid.GridPanel({
        
        region: 'center',
        width: 450,
        //iconCls: 'icon-grid',
        //frame: true,
        title: '�ʺ��б�',
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
        })
	    /*tbar: [
	        delbut
	    ]*/
    });
    
Ext.onReady(function(){

	store.load({params:{start:0, limit:20}});
	
    var viewport = new Ext.Viewport({layout: 'border',items: [simple,userGrid]});
    
    //��ɫ�����б�չ��ʱȥ��ѯ
    //comboxroleid.on('expand',function(){   
    //    rolecomboexpand();
    //});  
});
