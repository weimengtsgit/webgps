	var _newDate = new Date();
	var utc = _newDate.getTime()/1000;
	var utc_ = utc - 24*60*60*3;
	var newDate = new Date(utc_*1000);
	
    var _Year = _newDate.getYear();
    var _Month = _newDate.getMonth()+1;
    var _Day = _newDate.getDate();
    
	var _YearEnd = newDate.getYear();
    var _MonthEnd = newDate.getMonth()+1;
    var _DayEnd = newDate.getDate();
    
    if (_Month.toString().length == 1) {
    	_Month = "0" + _Month;
    }
    if (_Day.toString().length == 1) {
    	_Day = "0" + _Day;
    }
    if (_MonthEnd.toString().length == 1) {
    	_MonthEnd = "0" + _MonthEnd;
    }
    if (_DayEnd.toString().length == 1) {
    	_DayEnd = "0" + _DayEnd;
    }
	//�����䶨ʱȡ,ʱ��
	var startTimerec = _YearEnd + "-" + _MonthEnd + "-" + _DayEnd + " 00:00:00";
	var endTimerec = _Year + "-" + _Month + "-" + _Day + ' 23:59:59';

var searchValue = '';
var tmpdate = (new Date()).pattern("yyyy-M-d");
var startTime = startTimerec;
var endTime = endTimerec;
//�ѷ�����ʱ��
var startTime2 = tmpdate+' '+'08:00:00';
var endTime2 = tmpdate+' '+'18:00:00';

var inboxStore;
var root;
var smsinterval;

function getTreeId(node,treeArr){
	//node.expand();
	//15045412114@#1
	var treeid = node.id;
	var idArr = treeid.split('@#');
	if(idArr.length>1){
		if(node.isLeaf()&&node.getUI().isChecked()){
			treeArr[treeArr.length] = node;
		}
	}
	node.eachChild(function(child) {
		getTreeId(child,treeArr);
	});
}
    
    function hasReadfun(val){
    	if(val == 'true'){
    		return '�Ѷ�';
    	}else{
    		return '<span style="font-weight: bold">δ��</span>';
    	}
    }
	function isreadfun(val){
		if(val.indexOf('_recv')!=-1){
			val = val.substring(0,val.indexOf('_recv'));
			return '<span style="font-weight: bold">'+val+'</span>';
		}else{
			return val;
		}
	}
	
Ext.onReady(function(){
	Ext.QuickTips.init();
	
    var feeds = new Ext.Panel({
    	id:'feed-tree',
        region:'west',
        //title:'Feeds',
        split:true,
        width: 175,
        minSize: 175,
        maxSize: 175,
        //collapsible: true,
        margins:'0 0 5 5',
        cmargins:'0 5 5 5',
        rootVisible:false,
        collapseMode: 'mini',
        lines:false,
        autoScroll:true,
        collapseFirst:false,
        items:[{
		    id:"group-manager",
			html:"<br>"+"<p>&nbsp&nbsp&nbsp&nbsp<img src='images/article.gif'>"+
			"<a href='#' id='inbox_img'>&nbsp;"+sms.inbox+"</a></p>"+//"<br>"+
			//"<p>&nbsp&nbsp&nbsp&nbsp<img src='images/go-to-post.gif'>"+
			//"<a href='#' id='readbox_img'>&nbsp;δ������</a></p>"+
			"<p>&nbsp&nbsp&nbsp&nbsp<img src='images/go-to-post.gif'>"+
			"<a href='#' id='sendbox_img'>&nbsp;"+sms.sentMessages+"</a></p>"+
			"<p>&nbsp&nbsp&nbsp&nbsp<img src='images/go-to-post.gif'>"+
			"<a href='#' id='sms_available'></a></p>"
			,
			border:false,
			iconCls:"settings"
		}],
        tbar: [
        	{
            iconCls:'add-feed',
            text: sms.smsReceived,
            handler: function(){
            	Ext.getCmp('main-tabs').setActiveTab(0);
		    	Ext.getCmp('main-view').layout.setActiveItem(0);
		    	Ext.getCmp('main-view').setTitle(sms.inbox);
            	/*var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();*/
	        	//tmpDeviceIdField = encodeURI(tmpDeviceIdField);
		    	startTime = startTimerec;
		    	endTime = endTimerec;
		    	
    			inboxStore.load({params:{start:0, limit:14, searchValue: '' , startTime: startTimerec, endTime: endTimerec}});
    			
            }
        },
        	{
            id:'delete',
            iconCls:'delete-icon',
            text: sms.createSms,
            handler: function(){
               var maintabs = Ext.getCmp('main-tabs');
               	
			   if(maintabs.get(1) == undefined){
			   	   
               	 var loader = new Ext.tree.TreeLoader({
					dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForEntTerminal'	
				});
				
				root = new Ext.tree.AsyncTreeNode({
						text : '��λƽ̨',
						id : '-100',
						draggable : false // ���ڵ㲻�����϶�
				});
				var api = new Ext.tree.TreePanel({
						region: 'west',
				        rootVisible:false,
				        lines:true,
				        autoScroll:true,
				        width:170,
				        loader: loader,
				        root:root
				});
                   var simple = new Ext.FormPanel({
                   	region: 'center',
			        labelWidth: 75, // label settings here cascade unless overridden
			        //url:'save-form.php',
			        frame:true,
			        //title: 'д����',
			        bodyStyle:'padding:5px 5px 0',
			        
			        width: 350,
			        defaults: {width: 230},
			        defaultType: 'textfield',
			
			        items: [{
			        	    id: 'sendsmscontent',
			                fieldLabel: '��������',
			                height: 100,
			                width: 350,
			                xtype: 'textarea',
			                enableKeyEvents: true,
			    	        listeners:{
			    	        	/*change: function( field , newValue, oldValue ) {
			    	        		alert(newValue);
			    		    	},
			    		    	keydown: function(thiz , e) {
			    	        		alert('1'+thiz.getValue());
			    		    	},*/
			    		    	keyup: function(thiz , e) {
			    		    		var tmpSmsCount = thiz.getValue().length;
			    		    		var sendSmsNum = tmpSmsCount%67==0?tmpSmsCount/67:Math.floor(tmpSmsCount/67)+1;
			    		    		
			    		    		var tmpLabel = Ext.getCmp('sendsmscount');
			    		    		tmpLabel.setText('�������������Ϊ'+tmpSmsCount+"��;���ŷ�������Ϊ"+sendSmsNum+"����һ����������Ϊ67���ַ���");
			    		    		
			    		    	}
			    	        }
			            },{
			        	    id: 'sendsmscount',
			        	    html: '�������������Ϊ:',
			                width: 350,
			                xtype: 'label'
			            }
			        ],
			
			        buttons: [{
			            text: '����',
			            handler: function(){
			            	var treeArr = new Array();
	                    	getTreeId(root,treeArr);
							if(treeArr.length<=0){
								Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
								return;
							}
							var tmpcontent = Ext.getCmp('sendsmscontent').getValue();
				        	if(tmpcontent==''){
				        		Ext.MessageBox.alert('��ʾ', '�������������!');
				        		return ;
				        	}
							saveSms();
			            }
			        }/*,{
			            text: '�ر�',
			            handler: function(){
			            	
			            }
			        }*/]
			    });
			      var viewport = new Ext.Panel({
			      		title: 'д����',
			      		closable: true,
			            layout: 'border',
			            items: [api, simple]
			        });
			        api.expandPath('/-100/-101/');
			        
			        api.on('checkchange',function(node,checked){
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
					},api);
			   	maintabs.add(viewport);
                maintabs.activate(viewport);
			   }
               
            },
            scope: this
        }]
    });
//������
        inboxStore = new Ext.data.Store({
        	autoLoad: {params:{start: 0, limit: 14, searchValue: '' , startTime: startTime, endTime: endTime}},
	        proxy: new Ext.data.HttpProxy({
	            url: path+'/sms/sms.do?method=listAllSms'
	        }),
	
	        reader: new Ext.data.JsonReader({
				totalProperty: 'total',
			    successProperty: 'success',
			    idProperty: 'id',
			    root: 'data'
	        },['id','vehicleNumber','simcard', 'content', 'createTime', 'hasRead']
	        ),
	        listeners:{
		    	beforeload:{
		    		fn: function(thiz,options){
		    			/*var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
			        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
			        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
			        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
			        	//var searchValue = encodeURI(tmpDeviceIdField);
			        	var startTime = tmpstartdate+' '+tmpstarttime+':00';
			        	var endTime = tmpenddate+' '+tmpendtime+':00';*/
		    			this.baseParams ={
		    				searchValue: '' , startTime: startTime, endTime: endTime
		    			};
		    		}
		    	}
		    }
	    });
	    

	    //������
    var inboxGrid = new Ext.grid.GridPanel({
    	//title: '������',
    	region: 'center',
        id: 'inbox-grid',
        loadMask: {msg:'��ѯ��...'},
        sm: new Ext.grid.RowSelectionModel({
            singleSelect:true
        }),
        store : inboxStore,
        columns : [{
	        //id: 'id',
	        header: "id",
	        hidden: true,
	        dataIndex: 'id',
	        sortable:true,
	        width: 100
	      },{
	        //id: 'simcard',
	        header: sms.gridName,
	        dataIndex: 'vehicleNumber',
	        sortable:true,
	        width: 100,
	        renderer: isreadfun
	      },{
	        //id: 'simcard',
	        header: sms.gridMobileNumber,
	        dataIndex: 'simcard',
	        sortable:true,
	        width: 100,
	        renderer: isreadfun
	      },{
	      	//id: "content",
	        header: sms.gridMessage,
	        dataIndex: 'content',
	        width: 200,
	        sortable:true,
	        renderer: function tips(val) {
	        	//alert(val);
	        	var tmp = '';
	            if(val.indexOf('_recv')!=-1){
					val = val.substring(0,val.indexOf('_recv'));
					tmp = '<span style="font-weight: bold;display:table;width:100%;" qtip="' + val + '">'+val+'</span>';
				}else{
					tmp = '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
				}
				//alert(tmp);
				return tmp;
				//return '<span style="display:table;width:100%;" qtip=' + val + '>' + val + '</span>';
			}
	      },{
	        //id: 'createTime',
	        header: sms.gridTime,
	        dataIndex: 'createTime',
	        width: 130,
	        sortable:true,
	        renderer: isreadfun
	    },{
	        //id: 'hasRead',
	        header: sms.gridRead,
	        dataIndex: 'hasRead',
	        width: 100,
	        sortable:true,
	        renderer: hasReadfun
	    }],
	    //autoExpandColumn: 'content',
	    //stripeRows: true,
	    enableColumnHide : false,
        tbar : [
                sms.gridFrom
        ,{
        		xtype:'datefield',
        		id: 'startDateField',
				fieldLabel: 'Date',
				format:'Y-m-d',
				width: 100,
				editable: false,
				value: newDate
	    },{
	    		xtype:'timefield',
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
                id: 'startTimeField',
                value: '08:00',
				editable: false,
                invalidText : '��Ч��ʱ���ʽ - �������:12:00',
                increment : 1
        },'-',sms.gridTo
        ,{
        		xtype:'datefield',
        		width: 100,
        		editable : false,
				format:'Y-m-d',
				value: _newDate,
				editable: false,
	            id: 'endDateField'
	    },{
	    		xtype:'timefield',
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
				editable: false,
                id: 'endTimeField',
                value: '18:00',
                increment : 1
        },'-',new Ext.Action({
	        text: sms.gridSearch,
	        handler: function(){
	        	var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	//tmpDeviceIdField = encodeURI(tmpDeviceIdField);
	        	startTime = tmpstartdate+' '+tmpstarttime+':00';
	        	endTime = tmpenddate+' '+tmpendtime+':00';
    			inboxStore.load({params:{start:0, limit:14, searchValue: '' , startTime: startTime, endTime: endTime}});
    			
	        },
	        iconCls: 'icon-search'
	    }),'-',new Ext.Action({
	        text: sms.gridSendToExcel,
	        handler: function(){
	        	var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
	        	//tmpDeviceIdField = encodeURI(tmpDeviceIdField);
	        	startTime = tmpstartdate+' '+tmpstarttime+':00';
	        	endTime = tmpenddate+' '+tmpendtime+':00';
	        	document.excelformRec.action =path+'/sms/sms.do?method=listAllSms'+'&startTime='+startTime+'&endTime='+endTime+'&expExcel=true';
    			document.excelformRec.submit();
    			Ext.Msg.show({
			    	msg: '���ڵ��� ���Ե�...',
			        progressText: '������...',
			        width:300,
			        wait:true,
			        icon:'ext-mb-download'
			    });
    			setTimeout(function(){Ext.MessageBox.hide();},2000);
	        },
	        iconCls: 'icon-excel'
	    })
        ],
        bbar: new Ext.PagingToolbar({
            pageSize: 14,
            store: inboxStore,
            displayInfo: true,
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
        })
    });
    /*//δ������
    var readboxStore = new Ext.data.Store({
        	autoLoad: {params:{start: 0, limit: 20, searchValue: '' , startTime: startTime, endTime: endTime}},
	        proxy: new Ext.data.HttpProxy({
	            url: path+'/sms/sms.do?method=listNotReadSms'
	        }),
	
	        reader: new Ext.data.JsonReader({
				totalProperty: 'total',
			    successProperty: 'success',
			    idProperty: 'id',
			    root: 'data'
	        },['id','vehicleNumber','simcard', 'content','createTime']
	        ),
	        listeners:{
		    	beforeload:{
		    		fn: function(thiz,options){
		    			var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
			        	var tmpstarttime = Ext.getCmp('startTimeField').getValue();
			        	var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
			        	var tmpendtime = Ext.getCmp('endTimeField').getValue();
			        	//var searchValue = encodeURI(tmpDeviceIdField);
			        	var startTime = tmpstartdate+' '+tmpstarttime+':00';
			        	var endTime = tmpenddate+' '+tmpendtime+':00';
		    			this.baseParams ={
		    				searchValue: '' , startTime: startTime, endTime: endTime
		    			}
		    		}
		    	}
		    }
	    });
	    
     var readboxGrid = new Ext.grid.GridPanel({
     	//title: '�ѷ���',
    	region: 'center',
        id: 'readboxG-grid',
        loadMask: {msg:'Loading...'},
        sm: new Ext.grid.RowSelectionModel({
            singleSelect:true
        }),
        store : readboxStore,
        columns : [{
	        //id: 'id',
	        header: "id",
	        hidden: true,
	        dataIndex: 'id',
	        sortable:true,
	        width: 120
	      },{
	        //id: 'simcard',
	        header: "����",
	        dataIndex: 'vehicleNumber',
	        sortable:true,
	        width: 120
	      },{
	        //id: 'simcard',
	        header: "�ֻ�����",
	        dataIndex: 'simcard',
	        sortable:true,
	        width: 120
	      },{
	      	//id: "content",
	        header: "����",
	        dataIndex: 'content',
	        width: 400,
	        sortable:true
	      },{
	        //id: 'createTime',
	        header: "ʱ��",
	        dataIndex: 'createTime',
	        width: 150,
	        sortable:true
	    }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: readboxStore,
            displayInfo: true,
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
        }),
        tbar : new Ext.Toolbar({
        	items:[
        new Ext.form.Label({
        		//width: 100,
        		text:'��ʼʱ��'
        }),new Ext.form.DateField({
        		id: 'startDateField1',
				fieldLabel: 'Date',
				format:'Y-m-d',
				width: 100,
				value: new Date()
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
                id: 'startTimeField1',
                value: '08:00',
                invalidText : '��Ч��ʱ���ʽ - �������:12:00',
                increment : 1
        }),'-',new Ext.form.Label({
        		//width: 100,
        		text:'����ʱ��'
        }),new Ext.form.DateField({
        		width: 100,
				fieldLabel: 'Date',
				format:'Y-m-d',
				value: new Date(),
	            id: 'endDateField1'
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
                id: 'endTimeField1',
                value: '18:00',
                increment : 1
        }),'-',new Ext.Action({
	        text: '��ѯ',
	        handler: function(){
	        	var tmpstartdate = Ext.getCmp('startDateField1').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField1').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField1').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField1').getValue();
	        	//tmpDeviceIdField = encodeURI(tmpDeviceIdField);
    			readboxStore.load({params:{start:0, limit:20, searchValue: '' , startTime: tmpstartdate+' '+tmpstarttime+':00', endTime: tmpenddate+' '+tmpendtime+':00'}});
    			
	        },
	        iconCls: 'icon-search'
	    })
        ]
        })
    });*/
    
    //�ѷ���
    var sendboxStore = new Ext.data.Store({
        	autoLoad: {params:{start: 0, limit: 14, searchValue: '' , startTime: startTime2, endTime: endTime2}},
	        proxy: new Ext.data.HttpProxy({
	            url: path+'/sms/sms.do?method=listSendSmsLog'
	        }),
	
	        reader: new Ext.data.JsonReader({
				totalProperty: 'total',
			    successProperty: 'success',
			    idProperty: 'id',
			    root: 'data'
	        },['id','vehicleNumber','simcard', 'content','createTime']
	        ),
	        listeners:{
		    	beforeload:{
		    		fn: function(thiz,options){
		    			/*var tmpstartdate = Ext.getCmp('startDateField2').getValue().format('Y-m-d');
			        	var tmpstarttime = Ext.getCmp('startTimeField2').getValue();
			        	var tmpenddate = Ext.getCmp('endDateField2').getValue().format('Y-m-d');
			        	var tmpendtime = Ext.getCmp('endTimeField2').getValue()
			        	//var searchValue = encodeURI(tmpDeviceIdField);
			        	startTime = tmpstartdate+' '+tmpstarttime+':00';
			        	endTime = tmpenddate+' '+tmpendtime+':00';;*/
		    			this.baseParams ={
		    				searchValue: '' , startTime: startTime2, endTime: endTime2
		    			};
		    		}
		    	}
		    }
	    });
	    
     var sendboxGrid = new Ext.grid.GridPanel({
     	//title: '�ѷ���',
    	region: 'center',
        id: 'sendboxG-grid',
        loadMask: {msg:'��ѯ��...'},
        sm: new Ext.grid.RowSelectionModel({
            singleSelect:true
        }),
        store : sendboxStore,
        columns : [{
	        //id: 'id',
	        header: "id",
	        hidden: true,
	        dataIndex: 'id',
	        sortable:true,
	        width: 100
	      },{
	        //id: 'simcard',
	        header: "����",
	        dataIndex: 'vehicleNumber',
	        sortable:true,
	        width: 100
	      },{
	        //id: 'simcard',
	        header: "�ֻ�����",
	        dataIndex: 'simcard',
	        sortable:true,
	        width: 100
	      },{
	      	id: "content",
	        header: "����",
	        dataIndex: 'content',
	        width: 200,
	        sortable:true,
	        renderer: function tips(val) {
	        	return '<span style="display:table;width:100%;" qtip="' + val + '">' + val + '</span>';
			}
	      },{
	        //id: 'createTime',
	        header: "ʱ��",
	        dataIndex: 'createTime',
	        width: 130,
	        sortable:true
	    }],
	    autoExpandColumn: 'content',
        enableColumnHide : false,
        bbar: new Ext.PagingToolbar({
            pageSize: 14,
            store: sendboxStore,
            displayInfo: true,
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
        }),
        tbar : ['��ʼʱ��'
        ,new Ext.form.DateField({
        		id: 'startDateField2',
				fieldLabel: 'Date',
				format:'Y-m-d',
				width: 100,
				editable: false,
				value: new Date()
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
                id: 'startTimeField2',
                value: '08:00',
                invalidText : '��Ч��ʱ���ʽ - �������:12:00',
                increment : 1
        }),'-','����ʱ��'
        ,new Ext.form.DateField({
        		width: 100,
				fieldLabel: 'Date',
				format:'Y-m-d',
				value: new Date(),
				editable: false,
	            id: 'endDateField2'
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 60,
                format :'H:i',
                id: 'endTimeField2',
                value: '18:00',
                increment : 1
        }),'-',new Ext.Action({
	        text: '��ѯ',
	        handler: function(){
	        	var tmpstartdate = Ext.getCmp('startDateField2').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField2').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField2').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField2').getValue();
	        	//tmpDeviceIdField = encodeURI(tmpDeviceIdField);
	        	startTime2 = tmpstartdate+' '+tmpstarttime+':00';
	        	endTime2 = tmpenddate+' '+tmpendtime+':00';
    			sendboxStore.load({params:{start:0, limit:14, searchValue: '' , startTime: startTime2 , endTime: endTime2 }});
    			
	        },
	        iconCls: 'icon-search'
	    }),'-',new Ext.Action({
	        text: '����Excel',
	        handler: function(){
	        	var tmpstartdate = Ext.getCmp('startDateField2').getValue().format('Y-m-d');
	        	var tmpstarttime = Ext.getCmp('startTimeField2').getValue();
	        	var tmpenddate = Ext.getCmp('endDateField2').getValue().format('Y-m-d');
	        	var tmpendtime = Ext.getCmp('endTimeField2').getValue();
	        	//tmpDeviceIdField = encodeURI(tmpDeviceIdField);
	        	startTime = tmpstartdate+' '+tmpstarttime+':00';
	        	endTime = tmpenddate+' '+tmpendtime+':00';
	        	document.excelformSen.action =path+'/sms/sms.do?method=listSendSmsLog&startTime='+startTime+'&endTime='+endTime+'&expExcel=true';
    			document.excelformSen.submit();
    			Ext.Msg.show({
			    	msg: '���ڵ��� ���Ե�...',
			        progressText: '������...',
			        width:200,
			        wait:true,
			        icon:'ext-mb-download'
			    });
    			setTimeout(function(){Ext.MessageBox.hide();},2000);
	        },
	        iconCls: 'icon-excel'
	    })
        ]
    });
    
    
    
    var mainPanel = new Ext.TabPanel({
    	id:'main-tabs',
        activeTab:0,
        region:'center',
        margins:'0 5 5 0',
        resizeTabs:true,
        tabWidth:150,
        minTabWidth: 120,
        enableTabScroll: true,
        plugins: new Ext.ux.TabCloseMenu(),
        items: {
            id:'main-view',
            layout:'card',
            activeItem:0,
            title: sms.inbox,
            //items:[ inboxGrid,readboxGrid, sendboxGrid ]
            items:[ inboxGrid, sendboxGrid ]
        }
        
    });
	
    var viewport = new Ext.Viewport({
        layout:'border',
        items:[
            /*new Ext.BoxComponent({ // raw element
                region:'north',
                el: 'header',
                height:32
            }),*/
            feeds,
            mainPanel
         ]
    });
    
    Ext.get('inbox_img').on('click',function(){
    	Ext.getCmp('main-tabs').setActiveTab(0);
    	Ext.getCmp('main-view').layout.setActiveItem(0);
    	Ext.getCmp('main-view').setTitle(sms.inbox);
    	
    });
    
    /*Ext.get('readbox_img').on('click',function(){
    	Ext.getCmp('main-tabs').setActiveTab(0);
    	Ext.getCmp('main-view').layout.setActiveItem(1);
    	Ext.getCmp('main-view').setTitle('δ������');
    });*/
    
    Ext.get('sendbox_img').on('click',function(){
    	Ext.getCmp('main-tabs').setActiveTab(0);
    	Ext.getCmp('main-view').layout.setActiveItem(1);
    	Ext.getCmp('main-view').setTitle(sms.sentMessages);
    });
    
    Ext.get('sms_available').on('click',function(){
    	smsavailablefun();
    });
    
    //inboxStore.load({params:{start:0, limit:20}});
    //sendboxStore.load({params:{start:0, limit:20}});
    
    /*Ext.get('delbox_img').on('click',function(){
    	Ext.getCmp('main-view').layout.setActiveItem(2);
    	Ext.getCmp('main-view').setTitle('��ɾ��');
    });*/
    
    inboxGrid.on('cellclick',function(grid, rowIndex, columnIndex, e){
    	var record = grid.getStore().getAt(rowIndex);
    	var tmpid = record.get('id');
    	var tmphasRead = record.get('hasRead');
    	if(tmphasRead == 'false'){
    		readSms(tmpid);
    	}
    	
    });
    
    /*inboxStore.on('load',function(thisstore , thisrecords, thisoptions ){
    	
    	// var len = inboxStore.getCount();
		// Ext.getDom('inbox_img').innerHTML = '&nbsp;������'+'('+len+')';
		var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
		var tmpstarttime = Ext.getCmp('startTimeField').getValue();
		var tmpenddate = Ext.getCmp('endDateField').getValue().format('Y-m-d');
		var tmpendtime = Ext.getCmp('endTimeField').getValue();
		var startTime = tmpstartdate+' '+tmpstarttime+':00';
		var endTime = tmpenddate+' '+tmpendtime+':00';
		
		Ext.Ajax.request({
		 //url :url,
		 url:path+'/sms/sms.do?method=listNotReadSms',
		 method :'POST', 
		 params: { start: 0 , limit: 65535 , startTime: startTime , endTime: endTime},
		 //timeout : 60000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   var len = res.total;
		   Ext.getDom('inbox_img').innerHTML = '&nbsp;������'+'('+len+')';
		   
		 },
		 failure : function(request) {
		 }
		});
		
	});*/
	
	smsintervalfun();
	//smsinterval=setInterval('smsintervalfun();',10*1000);

	//��ѯ��ҵ��ǰ���ö������� add by liuhongxiao 2011-12-08
	smsavailablefun();
});

function smsintervalfun(){
	/*var _newDate = new Date()
    var _Year = _newDate.getYear()
    var _Month = 1 + _newDate.getMonth()
    var _Day = _newDate.getDate()
    
	var _YearEnd = _Year;
    var _MonthEnd = _Month - 1;
    if(_MonthEnd == 0){
    	_YearEnd = _YearEnd - 1;
    	_MonthEnd = 12;
    }
    if (_MonthEnd.toString().length == 1) {
	    _MonthEnd = "0" + _MonthEnd;
    }
    if (_Month.toString().length == 1) {
	    _Month = "0" + _Month;
    }
    if (_Day.toString().length == 1) {
	    _Day = "0" + _Day;
	}
	//var startTime = _Year + "-" + _Month + "-" + _Day + " 00:00:00";
	var startTime = _YearEnd + "-" + _MonthEnd + "-" + _Day + " 00:00:00";
	
	//var tmpstartdate = Ext.getCmp('startDateField').getValue().format('Y-m-d');
	//var tmpstarttime = Ext.getCmp('startTimeField').getValue();
	var endTime = _Year + "-" + _Month + "-" + _Day + ' 23:59:59';
	*/
	//alert(startTime);
	//alert(endTime);
	
	Ext.Ajax.request({
		 //url :url,
		 url:path+'/sms/sms.do?method=listNotReadSms',
		 method :'POST', 
		 params: { start: 0 , limit: 65535 , startTime: startTimerec , endTime: endTimerec},
		 //timeout : 60000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   var len = res.total;
		   Ext.getDom('inbox_img').innerHTML = '&nbsp;'+sms.inbox+'('+len+')';
		   
		 },
		 failure : function(request) {
		 }
	});
}

//��ѯ��ҵ��ǰ���ö������� add by liuhongxiao 2011-12-08
function smsavailablefun(){
	Ext.Ajax.request({
		 url:path+'/sms/sms.do?method=smsavailable',
		 method :'POST', 
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   var len = res.total;
		   var smsType = res.smsType;
		   if(smsType=='3'){
			   Ext.getDom('sms_available').innerHTML = '&nbspʣ���������('+len+')';
		   }
		 },
		 failure : function(request) {
		 }
	});
}

function readSms(id){
	
		Ext.Ajax.request({
		 //url :url,
		 url:path+'/sms/sms.do?method=readSms',
		 method :'POST', 
		 params: { recvId: id},
		 //timeout : 60000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		inboxStore.reload();
		   		return;
		   }else{
		   		//store.reload();
		   		return;
		   }
		 },
		 failure : function(request) {
		 }
		});
}

function saveSms(){
	Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�·��˶�����?', saveSmsconfirm );
}

function saveSmsconfirm(btn){
	var treeArr = new Array();
	                    	getTreeId(root,treeArr);
							if(treeArr.length<=0){
								Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
								return;
							}
							var simcard = '';
			            	for(var i=0;i<treeArr.length;i++){
								var idArr = treeArr[i].id.split('@#');
									simcard += idArr[2]+',';
							}
							if(simcard.length>0){
								simcard = simcard.substring(0,simcard.length-1);
							}
							var tmpcontent = Ext.getCmp('sendsmscontent').getValue();
	if(btn=='yes'){
		 parent.Ext.Msg.show({
			 msg: '���ڷ��� ���Ե�...',
			 progressText: '������...',
			 width:300,
			 wait:true,
			 //waitConfig: {interval:200},
			 icon:'ext-mb-download'
		});
		Ext.Ajax.request({
		 //url :url,
		 url:path+'/sms/sms.do?method=saveSms',
		 method :'POST', 
		 params: { simcards: encodeURI(simcard) , message: encodeURI(tmpcontent)},
		 //timeout : 60000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		parent.Ext.Msg.alert('��ʾ', '���ͳɹ�!');
		 		//��ѯ��ҵ��ǰ���ö������� add by liuhongxiao 2011-12-08
		 		//smsavailablefun();
		   		return;
		   }else{
		   		//store.reload();
		   	parent.Ext.Msg.alert('��ʾ', '����ʧ��!');
		   		return;
		   }
		 },
		 failure : function(request) {
		 	parent.Ext.Msg.alert('��ʾ', '����ʧ��!');
		 }
		});
	}
}