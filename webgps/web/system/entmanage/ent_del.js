
	var searchValue = '';
	
var delids='';
var delbut = new Ext.Action({
		        text: 'ɾ��',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					var tmpRecArr = sm.getSelections();
					var ids = '';
					for(var i=0;i<tmpRecArr.length;i++){
						ids+=tmpRecArr[i].get('entCode')+',';
					}
					if(ids.length>0){
						ids = ids.substring(0,ids.length-1);
					}else{
						parent.Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫɾ������ҵ!');
		        		return ;
					}
					delids = ids;
		        	parent.Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ����Щ��ҵ��?', delConfirm);
		        	
		        },
		        iconCls: 'icon-del'
		    });

		    function delConfirm(btn){
		    	if(btn=='yes'){
		    		parent.Ext.Msg.show({
			           msg: '����ɾ�� ���Ե�...',
			           progressText: 'ɾ��...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			       del();
		        }
		    }


function del(){

	Ext.Ajax.request({
		 //url :url,
		 url:path+'/system/ent.do?method=deleteEnts',
		 method :'POST', 
		 params: { entCodes: encodeURI(delids)},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		store.reload();
		   		parent.Ext.Msg.alert('��ʾ', 'ɾ���ɹ�');
		   		return;
		   }else{
		   		//store.reload();
		   		parent.Ext.Msg.alert('��ʾ', "ɾ��ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('��ʾ', "ɾ��ʧ��!");
		 }
		});
}

var proxy = new Ext.data.HttpProxy({
    url: path+'/system/ent.do?method=listEnt'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'entCode'},
    {name: 'entName'},
    {name: 'maxUserNum'},
    {name: 'smsAccount'},
    {name: 'smsPwd'},
    {name: 'endTime'},
    {name: 'entStatus'},
    {name: 'smsType'},
    {name: 'visitTjStatus'},
    {name: 'reportStatus'}, {
		name : 'persionGreyInterval'
	}, {
		name : 'carGreyInterval'
	}
]);

var store = new Ext.data.Store({
	autoLoad: {params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) }},
    //id: 'userstore',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				searchValue: encodeURI(searchValue)
    			};
    		}
    	}
    }
});

var sm = new Ext.grid.CheckboxSelectionModel({});

var userColumns =  [
	sm,
	new Ext.grid.RowNumberer({header:'���',width:40}),
    {header: "��ҵ����", width: 100, sortable: true, dataIndex: 'entCode'},
    {header: "��ҵ����", width: 100, sortable: true, dataIndex: 'entName'},
    {header: "����û���", width: 100, sortable: true, dataIndex: 'maxUserNum',hidden:true},
    {header: "�����ʺ�", width: 100, sortable: true, dataIndex: 'smsAccount'},
    {header: "��������", width: 100, sortable: true, dataIndex: 'smsPwd',hidden:true},
    {header: "������ʱ��", width: 100, sortable: true, dataIndex: 'endTime', renderer:function (val){
		if(val == 'null')return '';
		return val;
	}},
    {header: "״̬", width: 100, sortable: true, dataIndex: 'entStatus', renderer:function (val){
		if(val == '0'){return 'ͣ��';}else if(val == '1'){return '����';}else if(val == '2'){return '�������';}
		return val;
	}},
	{header: "����ͨ��", width: 100, sortable: true, dataIndex: 'smsType', renderer:function (val){
		if(val == 'null'){return '';}else if(val == '1'){return '����ͨ';}else if(val == '3'){return '����';}
		return val;
	}},
	{header: "�ݷ�ͳ��", width: 100, sortable: true, dataIndex: 'visitTjStatus', renderer:function (val){
		if(val == 'null'){return '��';}else if(val == '0'){return '��';}else if(val == '1'){return '��';}
		return val;
	}},
	{header: "������־", width: 100, sortable: true, dataIndex: 'reportStatus', renderer:function (val){
		if(val == 'null'){return '��';}else if(val == '0'){return '��';}else if(val == '1'){return '��';}
		return val;
	}}, {
		header : "��Ա����ʱ��(����)",
		width : 100,
		sortable : true,
		dataIndex : 'persionGreyInterval'
	}, {
		header : "���ػҵ�ʱ��(����)",
		width : 100,
		sortable : true,
		dataIndex : 'carGreyInterval'
	}
];

var userGrid = new Ext.grid.GridPanel({
        region: 'center',
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

Ext.onReady(function(){
    var viewport = new Ext.Viewport({layout: 'border',items: [userGrid]});
});
