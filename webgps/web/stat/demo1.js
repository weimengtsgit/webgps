
var controlcenterwin;
var CardPanel;
var CardWin;

    var myData = [
        ['3m Co',71.72,0.02,0.03,'9/1 12:00am'],
        ['Alcoa Inc',29.01,0.42,1.47,'9/1 12:00am'],
        ['Altria Group Inc',83.81,0.28,0.34,'9/1 12:00am']
    ];

    // create the data store
    var store = new Ext.data.ArrayStore({
        fields: [
           {name: 'company'},
           {name: 'price', type: 'float'},
           {name: 'change', type: 'float'},
           {name: 'pctChange', type: 'float'},
           {name: 'lastChange'}
        ]
    });

    // manually load local data
    

    // create the Grid
    var grid = new Ext.grid.GridPanel({
        store: store,
        columns: [
            {id:'company',header: 'Company', width: 160, sortable: true, dataIndex: 'company'},
            {header: 'Price', width: 75, sortable: true,  dataIndex: 'price'},
            {header: 'Change', width: 75, sortable: true,  dataIndex: 'change'},
            {header: '% Change', width: 75, sortable: true, dataIndex: 'pctChange'},
            {header: 'Last Updated', width: 85, sortable: true, dataIndex: 'lastChange'}
        ],
        stripeRows: true,
        autoExpandColumn: 'company',
        height: 350,
        width: 600,
        title: 'Array Grid',
        // config options for stateful behavior
        stateful: true,
        stateId: 'grid',
        tbar : new Ext.Toolbar({
        	items:[
        new Ext.form.Label({
        		text:'开始时间'
        }),new Ext.form.DateField({
				fieldLabel: 'Date',
	            name: 'BeginDateField'
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 50,
                name: 'BeginTimeField',
                id: 'BeginTimeField',
                minValue: '0:00 AM',
                maxValue: '23:59 PM',
                invalidText : '无效的时间格式 - 必须符合:12:00',
                increment : 1
        }),'-',new Ext.form.Label({
        		text:'结束时间'
        }),new Ext.form.DateField({
				fieldLabel: 'Date',
	            name: 'EndDateField'
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 50,
                name: 'EndTimeField',
                minValue: '0:00 AM',
                maxValue: '23:59 PM',
                increment : 1
        }),'-',new Ext.form.Label({
        		text:'申请人手机号'
        }),new Ext.form.TextField({
	            name: 'DeviceIdField',
	            id: 'DeviceIdField',
	            regex : /^\d{11}$/,
	            regexText : '无效的手机号码格式 - 必须符合11位数字',
	            width: 80
	    }),'-',new Ext.form.Label({
        		text:'授权人手机号'
        }),new Ext.form.TextField({
	            name: 'ParentCardField',
	            id: 'ParentCardField',
	            regex : /^\d{11}$/,
	            regexText : '无效的手机号码格式 - 必须符合11位数字',
	            width: 80
	    }),'-',new Ext.Action({
	        text: '查询',
	        handler: function(){
	        },
	        iconCls: 'icon-search'
	    }),'-',new Ext.Action({
	        text: '导出Excel',
	        handler: function(){
	        },
	        iconCls: 'icon-excel'
	    }),'-',new Ext.Action({
	        text: '清除',
	        handler: function(){
	        },
	        iconCls: 'icon-refresh'
	    })
        ]
        })
    });
    
    var myData1 = [
        ['3m Co1',71.72,0.02,0.03,'9/1 12:00am'],
        ['Alcoa Inc1',29.01,0.42,1.47,'9/1 12:00am'],
        ['Altria Group Inc1',83.81,0.28,0.34,'9/1 12:00am']
    ];

    // create the data store
    var store1 = new Ext.data.ArrayStore({
        fields: [
           {name: 'company'},
           {name: 'price', type: 'float'},
           {name: 'change', type: 'float'},
           {name: 'pctChange', type: 'float'},
           {name: 'lastChange'}
        ]
    });

    // manually load local data
    

    // create the Grid
    var grid1 = new Ext.grid.GridPanel({
        store: store1,
        columns: [
            {id:'company',header: 'Company', width: 160, sortable: true, dataIndex: 'company'},
            {header: 'Price', width: 75, sortable: true,  dataIndex: 'price'},
            {header: 'Change', width: 75, sortable: true,  dataIndex: 'change'},
            {header: '% Change', width: 75, sortable: true, dataIndex: 'pctChange'},
            {header: 'Last Updated', width: 85, sortable: true, dataIndex: 'lastChange'}
        ],
        stripeRows: true,
        autoExpandColumn: 'company',
        height: 350,
        width: 600,
        title: 'Array Grid1',
        // config options for stateful behavior
        stateful: true,
        stateId: 'grid1',
        tbar : new Ext.Toolbar({
        	items:[
        new Ext.form.Label({
        		text:'开始时间'
        }),new Ext.form.DateField({
				fieldLabel: 'Date',
	            name: 'BeginDateField'
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 50,
                name: 'BeginTimeField',
                id: 'BeginTimeField',
                minValue: '0:00 AM',
                maxValue: '23:59 PM',
                invalidText : '无效的时间格式 - 必须符合:12:00',
                increment : 1
        }),'-',new Ext.form.Label({
        		text:'结束时间'
        }),new Ext.form.DateField({
				fieldLabel: 'Date',
	            name: 'EndDateField'
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 50,
                name: 'EndTimeField',
                minValue: '0:00 AM',
                maxValue: '23:59 PM',
                increment : 1
        }),'-',new Ext.form.Label({
        		text:'申请人手机号'
        }),new Ext.form.TextField({
	            name: 'DeviceIdField',
	            id: 'DeviceIdField',
	            regex : /^\d{11}$/,
	            regexText : '无效的手机号码格式 - 必须符合11位数字',
	            width: 80
	    }),'-',new Ext.form.Label({
        		text:'授权人手机号'
        }),new Ext.form.TextField({
	            name: 'ParentCardField',
	            id: 'ParentCardField',
	            regex : /^\d{11}$/,
	            regexText : '无效的手机号码格式 - 必须符合11位数字',
	            width: 80
	    }),'-',new Ext.Action({
	        text: '查询',
	        handler: function(){
	        },
	        iconCls: 'icon-search'
	    }),'-',new Ext.Action({
	        text: '导出Excel',
	        handler: function(){
	        },
	        iconCls: 'icon-excel'
	    }),'-',new Ext.Action({
	        text: '清除',
	        handler: function(){
	        },
	        iconCls: 'icon-refresh'
	    })
        ]
        })
    });
    
    
    var myData2 = [
        ['3m Co2',71.72,0.02,0.03,'9/1 12:00am'],
        ['Alcoa Inc2',29.01,0.42,1.47,'9/1 12:00am'],
        ['Altria Group Inc2',83.81,0.28,0.34,'9/1 12:00am']
    ];

    // create the data store
    var store2 = new Ext.data.ArrayStore({
        fields: [
           {name: 'company'},
           {name: 'price', type: 'float'},
           {name: 'change', type: 'float'},
           {name: 'pctChange', type: 'float'},
           {name: 'lastChange'}
        ]
    });

    // manually load local data
    

    // create the Grid
    var grid2 = new Ext.grid.GridPanel({
        store: store2,
        columns: [
            {id:'company',header: 'Company', width: 160, sortable: true, dataIndex: 'company'},
            {header: 'Price', width: 75, sortable: true,  dataIndex: 'price'},
            {header: 'Change', width: 75, sortable: true,  dataIndex: 'change'},
            {header: '% Change', width: 75, sortable: true, dataIndex: 'pctChange'},
            {header: 'Last Updated', width: 85, sortable: true, dataIndex: 'lastChange'}
        ],
        stripeRows: true,
        autoExpandColumn: 'company',
        height: 350,
        width: 600,
        title: 'Array Grid2',
        // config options for stateful behavior
        stateful: true,
        stateId: 'grid2',
        tbar : new Ext.Toolbar({
        	items:[
        new Ext.form.Label({
        		text:'开始时间'
        }),new Ext.form.DateField({
				fieldLabel: 'Date',
	            name: 'BeginDateField'
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 50,
                name: 'BeginTimeField',
                id: 'BeginTimeField',
                minValue: '0:00 AM',
                maxValue: '23:59 PM',
                invalidText : '无效的时间格式 - 必须符合:12:00',
                increment : 1
        }),'-',new Ext.form.Label({
        		text:'结束时间'
        }),new Ext.form.DateField({
				fieldLabel: 'Date',
	            name: 'EndDateField'
	    }),new Ext.form.TimeField({
                fieldLabel: 'Time',
                width: 50,
                name: 'EndTimeField',
                minValue: '0:00 AM',
                maxValue: '23:59 PM',
                increment : 1
        }),'-',new Ext.form.Label({
        		text:'申请人手机号'
        }),new Ext.form.TextField({
	            name: 'DeviceIdField',
	            id: 'DeviceIdField',
	            regex : /^\d{11}$/,
	            regexText : '无效的手机号码格式 - 必须符合11位数字',
	            width: 80
	    }),'-',new Ext.form.Label({
        		text:'授权人手机号'
        }),new Ext.form.TextField({
	            name: 'ParentCardField',
	            id: 'ParentCardField',
	            regex : /^\d{11}$/,
	            regexText : '无效的手机号码格式 - 必须符合11位数字',
	            width: 80
	    }),'-',new Ext.Action({
	        text: '查询',
	        handler: function(){
	        },
	        iconCls: 'icon-search'
	    }),'-',new Ext.Action({
	        text: '导出Excel',
	        handler: function(){
	        },
	        iconCls: 'icon-excel'
	    }),'-',new Ext.Action({
	        text: '清除',
	        handler: function(){
	        },
	        iconCls: 'icon-refresh'
	    })
        ]
        })
    });
        


Ext.onReady(function(){

    CardPanel = new Ext.TabPanel({
    	activeTab: 0,
    	renderTo: 'grid-example',
    	items: [grid , grid1 , grid2 ]
    });
    store.loadData(myData);
    store1.loadData(myData1);
    store2.loadData(myData2);
});
