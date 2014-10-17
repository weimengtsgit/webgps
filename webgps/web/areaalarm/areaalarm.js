//当前窗口状态 true:grid ;false:form
var AreaActiveItem = true;
var AreaAlarmsm = new Ext.grid.CheckboxSelectionModel({});

var AreaAlarmproxy = new Ext.data.HttpProxy({
    url: path+'/area/area.do?method=listArea'
});
var AreaAlarmreader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
	{name: 'id'},
    {name: 'areaName'},
    {name: 'areaType'},
    {name: 'areaPoints'}
]);
var AreaAlarmstore = new Ext.data.Store({
	autoLoad: {params:{start: 0, limit: 10}},
    restful: true,
    proxy: AreaAlarmproxy,
    reader: AreaAlarmreader
});

    
    Ext.app.SearchField = Ext.extend(Ext.form.TwinTriggerField, {
	    initComponent : function(){
	        //if(!this.store.baseParams){
			//	this.store.baseParams = {};
			//}
			Ext.app.SearchField.superclass.initComponent.call(this);
			this.on('specialkey', function(f, e){
	            if(e.getKey() == e.ENTER){
	                this.onTrigger2Click();
	            }
	        }, this);
	    },
	
	    validationEvent:false,
	    validateOnBlur:false,
	    trigger1Class:'x-form-clear-trigger',
	    trigger2Class:'x-form-search-trigger',
	    hideTrigger1:true,
	    width:130,
	    hasSearch : false,
	    paramName : 'query',
	
	    onTrigger1Click : function(){
	        if(this.hasSearch){
	            //this.store.baseParams[this.paramName] = '';
				//this.store.removeAll();
				this.el.dom.value = '';
	            this.triggers[0].hide();
	            this.hasSearch = false;
				this.focus();
				AreaAlarmstore.load({params:{start: 0, limit: 10}});
	        }
	    },
	
	    onTrigger2Click : function(){
	        var v = this.getRawValue();
	        if(v.length < 1){
	            this.onTrigger1Click();
	            return;
	        }
			
			//this.store.baseParams[this.paramName] = v;
	        //var o = {start: 0};
	        //this.store.reload({params:o});
	        this.hasSearch = true;
	        this.triggers[0].show();
			this.focus();
			var searchValue = encodeURI(v);
			//alert(searchValue);
			AreaAlarmstore.load({params:{start: 0, limit: 10, searchValue: searchValue}});
			
	    }
	});
	
    var AreaAlarmpagingBar = new Ext.PagingToolbar({
        pageSize: 10,
        store: AreaAlarmstore,
        displayInfo: true,
        displayMsg: '第{0}到第{1}条数据 共{2}条',
        emptyMsg: "没有数据"
    });
	
    var AreaAlarmgrid = new Ext.grid.GridPanel({
    	//anchor: '100% 100%',
    	//width:350,
		//height:500,
        store: AreaAlarmstore,
        sm : AreaAlarmsm,
        columns: [
        	AreaAlarmsm,
        	{id:'id',header: "id", width: 10, sortable: true, dataIndex: 'id',hidden:true},
        	{header: "areaPoints", width: 10, sortable: true, dataIndex: 'areaPoints',hidden:true},
        	new Ext.grid.RowNumberer({header: area.id, width: 35}),
            {header: area.area_name, width: 200, sortable: true, dataIndex: 'areaName'},
            {header: area.area_type, width: 75, sortable: true, dataIndex: 'areaTypeName',hidden:true}
        ],
        bbar: AreaAlarmpagingBar,
        tbar: [
            { 
            text: main.add,
            iconCls: 'add_icons',
            handler: function(){
            	resetAreaForm();
            	//清除地图上区域线
            	map.removePolygonById('areaalarm_polygon');
            	//改变布局,显示表单
            	AreaAlarmPanel.layout.setActiveItem(AreaAlarmform);
            	AreaAlarmWindow.setHeight(200);
            	AreaActiveItem = false;
            }
            },'-',{ 
            text: main.modify,
            iconCls: 'modify_icons',
            handler: function(){
            	var tmpselArr = AreaAlarmsm.getSelections();
            	if(tmpselArr.length<=0){
            		Ext.Msg.alert(main.tips, area.select_a_area);
            		return;
            	}else if(tmpselArr.length>1){
            		Ext.Msg.alert(main.tips, area.select_a_area);
            		return;
            	}
            	//清除地图上区域线
            	map.removePolygonById('areaalarm_polygon');
            	//给表单赋值
            	Ext.getCmp('areaNamefrm').setValue(tmpselArr[0].get('areaName'));
            	Ext.getCmp('areaPointsfrm').setValue(tmpselArr[0].get('areaPoints'));
            	Ext.getCmp('areaIdfrm').setValue(tmpselArr[0].get('id'));
            	
            	//画区域
            	var tmpxyArr = tmpselArr[0].get('areaPoints').split('#');
	            if(tmpxyArr.length<=0){
	                return;
	            }
	            map.AreaAlarmaddPolygon(tmpxyArr, tmpselArr[0].get('areaName'), '', 'modify_polygon', true ,false);
	            map.drawTempObjectID = 'modify_polygon';
	            Ext.getCmp('areaPointsStatefrm').setText(area.already_draw_area);
            	//改变布局,显示表单
            	AreaAlarmPanel.layout.setActiveItem(AreaAlarmform);
            	AreaAlarmWindow.setHeight(200);
            	AreaActiveItem = false;
            }
            },'-',{
            text: main.del,
            iconCls: 'del_icons',
            handler: function(){
            	var tmpselArr = AreaAlarmsm.getSelections();
            	if(tmpselArr.length<=0){
            		Ext.Msg.alert(main.tips, area.select_area);
            		return;
            	}
            	Ext.MessageBox.confirm(main.tips, area.are_you_want_to_del_area, delAreaAlarm);
            }
            },'-',{
            text: main.bind,
            iconCls: 'user_add_icons',
            handler: function(){
            	var tmpselArr = AreaAlarmsm.getSelections();
            	if(tmpselArr.length<=0){
            		Ext.Msg.alert(main.tips, area.select_area);
            		return;
            	}
            	//else if(tmpselArr.length>1){
            	//	Ext.Msg.alert('提示','请选择一条电子围栏信息');
            	//	return;
            	//}
            	var ids = '';
            	var areaNames = '';
            	for(var i = 0;i < tmpselArr.length ;i++){
            		ids += tmpselArr[i].get('id') + ',';
            		areaNames += tmpselArr[i].get('areaName') + ',';
            	}
            	ids = ids.substring(0,ids.length-1);
            	areaNames = areaNames.substring(0,areaNames.length-1);
            	
            	//清除地图上区域线
            	map.removePolygonById('areaalarm_polygon');
            	//给表单赋值
            	Ext.getCmp('refTermAreaNamefrm').setText(areaNames);
            	Ext.getCmp('refTermAreaIdfrm').setValue(ids);
            	//改变布局,显示表单
            	AreaAlarmPanel.layout.setActiveItem(refTermAreaform);
            	AreaAlarmWindow.setHeight(200);
            	AreaActiveItem = false;
            	
            }
            },'->',
            new Ext.app.SearchField({
	        	width:100,
				//store: store,
				paramName: 'q'
	        })
        ],
        listeners: {
        	rowcontextmenu: function(grid, rowIndex, e){
        		e.preventDefault();
        		var gridMenu = new Ext.menu.Menu
	            ([
	                {text: area.view_area_msg,icon:"",handler:function(){
	                	var tmprecord = grid.getStore().getAt(rowIndex);
	                	var tmpareaPoints = tmprecord.get('areaPoints');
	                	var tmpareaName  = tmprecord.get('areaName');
	                	var tmpxyArr = tmpareaPoints.split('#');
	                	if(tmpxyArr.length<=1){
	                		Ext.Msg.alert(main.tips, main.no_area);
	                		return;
	                	}
	                	
	                	map.AreaAlarmaddPolygon(tmpxyArr, tmpareaName, '', 'areaalarm_polygon', true ,false);
	                	
	                }}
	            ]);
	            gridMenu.showAt(e.getPoint());
        	}
        }
    });
    
    var AreaAlarmform = new Ext.FormPanel({
    	//width:350,
		//height:300,
    	//anchor: '100% 100%',
    	labelWidth:100,
	    //frame:true,
	    bodyStyle:'padding:5px 5px 0',
	    autoScroll : true,
	    //width: 200,
	    defaults: {width: 150},
    	items: [{
    		id: 'areaNamefrm',
    		xtype: 'textfield',
    		fieldLabel: area.area_name,
    		allowBlank:false
    	},{
    		id: 'areaPointsStatefrm',
    		xtype: 'label',
    		//fieldLabel: '区域名称',
    		test: area.draw_area_please
    	}
		,{
			xtype: 'hidden',
			id: 'areaPointsfrm'
		},{
			xtype: 'hidden',
			id: 'areaIdfrm'
		}],
		buttons: [{
            text: area.draw_area,
            handler: function(){
            	Ext.getCmp('areaPointsfrm').reset();
            	Ext.getCmp('areaPointsStatefrm').setText(area.draw_area_please);
            	map.removePolygonById('areaalarm_polygon');
            	//开始绘制区域
            	map.beginDrawPolygonOnMap();
            }
        },{
            text: main.ok,
            handler: function(){
            	var tmp = Ext.getCmp('areaPointsfrm').getValue();
            	if(tmp.length<=0){
            		Ext.Msg.alert(main.tips, area.draw_area_please);
            		return;
            	}
            	var tmpname = Ext.getCmp('areaNamefrm').getValue();
            	if(tmpname.length<=0){
            		Ext.Msg.alert(main.tips, area.input_area_name);
            		return;
            	}
            	
            	//取得区域id,如果id不为空,则是修改,如果为空,则为新增
            	var tmpid = Ext.getCmp('areaIdfrm').getValue();
            	
            	if(tmpid.length<=0){
            		Ext.MessageBox.confirm(main.tips, area.are_you_want_to_add_area, addAreaAlarm);
            	}else{
            		Ext.MessageBox.confirm(main.tips, area.are_you_want_to_modify_area, modifyAreaAlarm);
            	}
            	//AreaAlarmPanel.layout.setActiveItem(AreaAlarmgrid);
            	//AreaAlarmWindow.setHeight(400);
            }
        },{
            text: main.back,
            handler: function(){
            	map.removePolygonById(map.drawTempObjectID);
            	map.removePolygonById('areaalarm_polygon');
            	map.resetControls();
            	Ext.getCmp('areaPointsfrm').reset();
            	Ext.getCmp('areaPointsStatefrm').setText(area.draw_area_please);
            	AreaAlarmPanel.layout.setActiveItem(AreaAlarmgrid);
            	AreaAlarmWindow.setHeight(400);
            	AreaActiveItem = true;
            }
        }]
    });
    
    var refTermAreaform = new Ext.FormPanel({
    	labelWidth:75,
	    bodyStyle:'padding:5px 5px 0',
	    autoScroll : true,
	    defaults: {width: 150},
    	items: [{
			xtype: 'hidden',
			id: 'refTermAreaIdfrm'
		},{
    		id: 'refTermAreaNamefrm',
    		xtype: 'label',
    		fieldLabel: area.area_name,
    		allowBlank:false
    	},{
    		id: 'areaAlarmTypefrm',
    		xtype: 'combo',
    		fieldLabel: area.alarm_type,
    		editable: false, 
    		//width:80,
    		displayField: 'name',
    		store: new Ext.data.ArrayStore({
    			fields: ['id', 'name'],
    			data : [[1, area.in_area],[0, area.out_area]]
    		}),
    		displayField:'name',
    		valueField: 'id',
    		typeAhead: true, 
    		mode: 'local', 
    		forceSelection: true, 
    		triggerAction: 'all',
    		value:'1', 
    		selectOnFocus:true
		},{
			xtype: 'timefield',
			id: 'areaAlarmStartTimefrm',
	        fieldLabel: main.start_time,
	        format:'H:i',
	        value:'08:00',
			increment: 1
	     },{
			xtype: 'timefield',
			id: 'areaAlarmEndTimefrm',
	        fieldLabel: main.end_time,
	        format:'H:i',
	        value:'18:00',
			increment: 1
	     }],
		buttons: [{
            text: main.ok,
            handler: function(){
            	var treeArr = new Array();
	            getTreeId(root,treeArr);
				if(treeArr.length<=0){
					Ext.Msg.alert(main.tips, '请选择终端!');
					return;
				}
            	Ext.MessageBox.confirm(main.tips, '您确定要终端绑定电子围栏吗?', refTermArea);
            	
            	//AreaAlarmPanel.layout.setActiveItem(AreaAlarmgrid);
            	//AreaAlarmWindow.setHeight(400);
            }
        },{
            text: main.back,
            handler: function(){
            	map.removePolygonById(map.drawTempObjectID);
            	map.removePolygonById('areaalarm_polygon');
            	AreaAlarmPanel.layout.setActiveItem(AreaAlarmgrid);
            	AreaAlarmWindow.setHeight(400);
            	AreaActiveItem = true;
            }
        }]
    });
    
    var AreaAlarmPanel = new Ext.Panel({
		//width:350,
		//height:500,
		margins: '2 5 5 0',
		activeItem: 0,
		layout:'card',
		items: [AreaAlarmgrid,AreaAlarmform,refTermAreaform]
	});

    var AreaAlarmWindow = new Ext.Window({
		title: '电子围栏列表',
		width:350,
		height:400,
		shim:false,
		closeAction: 'hide',
		animCollapse:false,
		constrainHeader:true,
		collapsible:true,
		plain:true,
		resizable:true,
		maximizable:true,
		closable:true,
		animCollapse :true,
		layout:'fit',
		border:false,
		items: [AreaAlarmPanel]
	});
	
//保存绘制的矩形坐标点
function fillMapData(shapeType, shapeRadio, shapeCoords   ){
	//coords = shapeCoords;
	Ext.getCmp('areaPointsfrm').setValue(shapeCoords);
	Ext.getCmp('areaPointsStatefrm').setText('已绘制电子围栏');
}
function resetAreaForm(){
	Ext.getCmp('areaNamefrm').reset();
	Ext.getCmp('areaPointsfrm').reset();
	Ext.getCmp('areaIdfrm').reset();
	Ext.getCmp('areaPointsStatefrm').setText('请绘制电子围栏');
}