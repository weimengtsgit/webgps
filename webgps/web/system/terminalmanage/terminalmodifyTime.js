var vsimcard  ;                //手机号码   
var vsimcardserial ;           //SIM卡序号  
var imsi;                       //手机号唯一序列号
var vname ;                   // 姓名       
var vselectGid   ;                //组名       
var vprovince ;               // 省         
var vcity  ;                  // 市         
var vstarttime ;              // 上班时间   
var vendtime;                 // 下班时间   
var vcollectinterval ;         //采集间隔   
var vremark;                   //员工备注   
var vterminaltype  ; //终端类型   radiogroup
var vgpsComboBox ='P-LENOVO' ;               // GPS厂商    
var vcarnumber ='';               //车牌号     
var vcarmodel ='';                //车辆型号   
var vdrivernumber =''   ;        // 车主电话   
var voilelespeedlimit = -1 ;       // 速度限值   
var vjj = 1;                       //劫警       
var vspeedalarm;               //超速报警   
var vspeedlimitvalue = -1;           // 速度限值   
var vduration = 5;                  // 持续时间   
var vold_parent_id;       //old组id
var searchValue = '';
var userGrid;
var vterminalEndtime;            //终端到期时间
function carTypeInfotComboexpand(){
	Ext.Ajax.request({
		url : path+'/carTypeInfo/carTypeInfo.do?method=carTypeInfoCombo&type=noallbox',
		method :'POST',
		success : function(request) {
			var res = request.responseText;
			car_type_info_combo_t.getStore().loadData(eval(res));
		},
		failure : function(request) {
		}
	});
}

var car_type_info_combo_t = new Ext.form.ComboBox( {
	hiddenName : 'id',
	valueField : 'id',
    fieldLabel: '车辆类型',
    width: 150,
	store : new Ext.data.SimpleStore({
	  	fields:['id', 'name'],
	  	data:[[]]
	}),
	displayField : 'name',
    mode: 'local',
	listeners: {
  	    expand : function(combo ) {
	  	  	var count = car_type_info_combo_t.getStore().getCount();
	  		if(count <=1){
	  	    	carTypeInfotComboexpand();
	  		}
    	}
    },
    //disabled : true,
    editable: false,
    triggerAction: 'all',
    emptyText:'选择车辆类型'
});

var icnCombo = new Ext.ux.IconCombo({
    store: new Ext.data.SimpleStore({
        fields: ['code', 'name', 'flag'],
        data: [
            ['persion', '手机', 'x-flag-persion']
            //['car', '车辆', 'x-flag-car'],
            //['ship', '船', 'x-flag-ship']
        ]
    }),
    valueField: 'code',
    displayField: 'name',
    iconClsField: 'flag',
    triggerAction: 'all',
    value: 'persion',
    mode: 'local',
    editable :false,
    width: 160
});

/**
*用户所见组树
**/
var root = new Ext.tree.AsyncTreeNode({
	text : '定位平台',
	id : '-100',
	draggable : false // 根节点不容许拖动
});

var treeload = new Ext.tree.TreeLoader({
	dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForUserCombox'	
});

var tree = new Ext.tree.TreePanel({   
	loader: treeload,   
    border:false,   
    rootVisible : false,
    root:root 
});   
/**
*下拉列表树
**/
      var comboxWithTree = new Ext.form.ComboBox({   
        store:new Ext.data.SimpleStore({fields:['abbr', 'state'],data:[[]]}),   
        editable:false,   
        mode: 'local',   
        triggerAction:'all',   
        fieldLabel: '所属组名称',
        id : 'selectGid',
        displayField:'state',
        emptyText:'选择组',
        maxHeight: 200,   
        tpl: "<tpl for='.'><div style='height:200px'><div id='tree'></div></div></tpl>",   
        selectedClass:'',   
        onSelect:Ext.emptyFn   
    });
    
var delbut = new Ext.Action({
		        text: '修改',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					var tmpfrmsimcard = Ext.getCmp('frmsimcard').getValue();
		        	if(tmpfrmsimcard==''){
		        		parent.Ext.MessageBox.alert('提示', '请输入手机号码!');
		        		return ;
		        	}
		        	vsimcard = tmpfrmsimcard;
		        	var tmpfrmsimcardserial = Ext.getCmp('frmsimcardserial').getValue();
		        	if(tmpfrmsimcardserial==''){
		        		parent.Ext.MessageBox.alert('提示', '请输入SIM卡序号!');
		        		return ;
		        	}
		        	vsimcardserial = tmpfrmsimcardserial;
		        	
		        	var imsiserial = Ext.getCmp('frmimsiserial').getValue();
		        	imsi = imsiserial;
		        	
		        	var tmpfrmname = Ext.getCmp('frmname').getValue();
		        	if(tmpfrmname==''){
		        		parent.Ext.MessageBox.alert('提示', '请输入姓名!');
		        		return ;
		        	}
		        	vname = tmpfrmname;
		        	var tmpselectGid= Ext.getCmp('selectGid').getValue();
		        	if(tmpselectGid==''){
		        		parent.Ext.MessageBox.alert('提示', '请选择所属组!');
		        		return ;
		        	}
		        	var tmpfrmprovince = Ext.getCmp('frmprovince').getValue();
		        	if(tmpfrmprovince==''){
		        		parent.Ext.MessageBox.alert('提示', '请选择省!');
		        		return ;
		        	}
		        	vprovince = tmpfrmprovince;
		        	var tmpfrmcity = Ext.getCmp('frmcity').getValue();
		        	if(tmpfrmcity==''){
		        		parent.Ext.MessageBox.alert('提示', '请选择市!');
		        		return ;
		        	}
		        	vcity = tmpfrmcity;
		        	var tmpfrmstarttime = Ext.getCmp('frmstarttime').getValue();
		        	if(tmpfrmstarttime==''){
		        		parent.Ext.MessageBox.alert('提示', '请选择上班时间!');
		        		return ;
		        	}
		        	vstarttime = tmpfrmstarttime;
		        	var tmpfrmendtime = Ext.getCmp('frmendtime').getValue();
		        	if(tmpfrmendtime==''){
		        		parent.Ext.MessageBox.alert('提示', '请选择下班时间!');
		        		return ;
		        	}
		        	vendtime = tmpfrmendtime;
		        	var nowDate=new Date();
		         	var tmpEndtime = Ext.getCmp('terminalEndtime').getValue();
		        	if(tmpEndtime==''){
		        		parent.Ext.MessageBox.alert('提示', '请选择终端到期时间!');
		        		return ;
		        	}else if(tmpEndtime-nowDate<1){
		        		parent.Ext.MessageBox.alert('提示', '终端到期时间必须大于今天!');
		        		return ;
		        	}
		        	vterminalEndtime=tmpEndtime;
		        	var tmpfrmcollectinterval = Ext.getCmp('frmcollectinterval').getValue();
		        	if(tmpfrmcollectinterval==''){
		        		parent.Ext.MessageBox.alert('提示', '请输入采集间隔!');
		        		return ;
		        	}
		        	vcollectinterval = tmpfrmcollectinterval;
		        	
		        	var tmpfrmremark = Ext.getCmp('frmremark').getValue();
		        	vremark = tmpfrmremark;
		        	
		        	//终端类型判断
		        	var tmpfrmterminaltype = Ext.getCmp('frmterminaltype').getValue().getGroupValue();
		        	//alert(tmpfrmterminaltype);
		        	vterminaltype = tmpfrmterminaltype;
		        	//终端类型为lbs,车牌号为人员名
		        	vcarnumber = vname;
		        	if(tmpfrmterminaltype == 1){
		        		var tmpgpsComboBox = Ext.getCmp('gpsComboBox').getValue();
		        		//alert(tmpgpsComboBox);
			        	if(tmpgpsComboBox==''){
			        		parent.Ext.MessageBox.alert('提示', '请选择终端厂商!');
			        		return ;
			        	}
			        	vgpsComboBox = tmpgpsComboBox;
			        	
			        	var tmpfrmcarnumber = Ext.getCmp('frmcarnumber').getValue();
			        	//alert(tmpfrmcarnumber);
			        	if(tmpfrmcarnumber==''){
			        		parent.Ext.MessageBox.alert('提示', '请输入车牌号!');
			        		return ;
			        	}
			        	vcarnumber = tmpfrmcarnumber;
			        	var tmpfrmcarmodel = Ext.getCmp('frmcarmodel').getValue();
			        	//alert(tmpfrmcarmodel);
			        	if(tmpfrmcarmodel==''){
			        		parent.Ext.MessageBox.alert('提示', '请输入车辆型号!');
			        		return ;
			        	}
			        	vcarmodel = tmpfrmcarmodel;
			        	var tmpfrmdrivernumber = Ext.getCmp('frmdrivernumber').getValue();
			        	//alert(tmpfrmdrivernumber);
			        	if(tmpfrmdrivernumber==''){
			        		parent.Ext.MessageBox.alert('提示', '请输入车主电话!');
			        		return ;
			        	}
			        	vdrivernumber = tmpfrmdrivernumber;
			        	var tmpfrmoilelespeedlimit = Ext.getCmp('frmoilelespeedlimit').getValue();
			        	//alert(tmpfrmoilelespeedlimit);
			        	if(tmpfrmoilelespeedlimit==''){
			        		parent.Ext.MessageBox.alert('提示', '请输入断油断电 速度限值!');
			        		return ;
			        	}
			        	
			        	var tmpfrmjj = Ext.getCmp('frmjj');
			        	if(tmpfrmjj.checked){
			        		vjj = 1;
			        	}else{
			        		vjj = 0;
			        	}
			        	voilelespeedlimit = tmpfrmoilelespeedlimit;
			        	var tmpfrmspeedalarm = Ext.getCmp('frmspeedalarm');
			        	if(tmpfrmspeedalarm.checked){
			        		var tmpspeedlimitvalue = Ext.getCmp('speedlimitvalue').getValue();
				        	//alert(tmpspeedlimitvalue);
				        	if(tmpspeedlimitvalue==''){
				        		parent.Ext.MessageBox.alert('提示', '请输入超速报警 速度限值!');
				        		return ;
				        	}
				        	vspeedlimitvalue = tmpspeedlimitvalue;
				        	var tmpduration = Ext.getCmp('duration').getValue();
				        	//alert(tmpduration);
				        	if(tmpduration==''){
				        		parent.Ext.MessageBox.alert('提示', '请输入超速报警 持续时间!');
				        		return ;
				        	}
				        	vduration = tmpduration;
			        	}else{
			        		vspeedlimitvalue = -1;
			        		vduration = 5;
			        	}
			        	
			        	
			        	
		        	}else{
		        		 vgpsComboBox ='P-LENOVO' ;               // GPS厂商    
						 vcarnumber ='';               //车牌号     
						 vcarmodel ='';                //车辆型号   
						 vdrivernumber =''   ;        // 车主电话   
						 voilelespeedlimit = -1 ;       // 速度限值   
						 vjj = 1;                       //劫警       
						 vspeedalarm;               //超速报警   
						 vspeedlimitvalue = -1;           // 速度限值   
						 vduration = 5;                  // 持续时间   
		        	}
		        	parent.Ext.MessageBox.confirm('提示', '您确定要修改此终端吗?', addConfirm);
		        },
		        iconCls: 'icon-modify'
		    });

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
/*var urlstr = '&simcard='+vsimcard+ '&deviceId='+vsimcardserial+ 
'&termName='+vname+ '&groupId='+vselectGid+ '&province='+vprovince+ 
'&city='+vcity+
'&startTime='+vstarttime+ '&endTime='+vendtime+ '&getherInterval='+
vcollectinterval+'&termDesc=' +vremark+'&locateType='+vterminaltype+ 
'&typeCode='+vgpsComboBox+
'&vehicleNumber='+vcarnumber+ '&vehicleType='+vcarmodel+ '&driverNumber='+
vdrivernumber+'&oilSpeedLimit='+voilelespeedlimit+ 
'&holdAlarmFlag='+vjj+
'&speedAlarmLimit='+vspeedlimitvalue+ '&speedAlarmLast='+vduration+
'&old_parent_id='+vold_parent_id;*/
	
	var tmpfrmWeekArr = Ext.getCmp('weekCheckBoxGroup').getValue();
	var weekNum = 0;
	while(tmpfrmWeekArr.length>0){
		var checkBox = tmpfrmWeekArr.pop();
		var checkedValue = checkBox.value;
		weekNum += Math.pow(2,checkedValue-1);
	}
	var carTypeInfo = car_type_info_combo_t.getValue();
	var imgUrl = icnCombo.getValue();
	
	Ext.Ajax.request({
		 //url :url,
		 url:path+'/terminal/terminal.do?method=updateTerminal',
		 method :'POST', 
		 params:{
		simcard: encodeURI(vsimcard),deviceId: encodeURI(vsimcardserial),termName: encodeURI(vname),
		groupId: encodeURI(vselectGid),province: encodeURI(vprovince),city: encodeURI(vcity),
		startTime: encodeURI(vstarttime),endTime: encodeURI(vendtime),getherInterval: encodeURI(vcollectinterval),
		termDesc: encodeURI(vremark),locateType: encodeURI(vterminaltype),typeCode: encodeURI(vgpsComboBox),terminalEndtime:vterminalEndtime,
		vehicleNumber: encodeURI(vcarnumber),vehicleType: encodeURI(vcarmodel),driverNumber: encodeURI(vdrivernumber),
		oilSpeedLimit: encodeURI(voilelespeedlimit),holdAlarmFlag: encodeURI(vjj),speedAlarmLimit: encodeURI(vspeedlimitvalue),
		speedAlarmLast: encodeURI(vduration),old_parent_id: encodeURI(vold_parent_id),imgUrl: encodeURI(imgUrl), week: weekNum, 
		carTypeInfo: carTypeInfo,imsi:encodeURI(imsi)
		},
		 //params: { ctl:'add', parentid : tmpfrmParendId ,starttime:tmpfrmstarttime,endtime:tmpfrmendtime},
		 timeout : 60000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		//store.reload();
		 		var cursor = userGrid.getBottomToolbar().cursor;
		 		store.load({params:{start:cursor, limit:20, searchValue: encodeURI(searchValue) }});
		   		parent.Ext.Msg.alert('提示', '修改成功');
		   		return;
		   }else{
		   		//store.reload();
		   		parent.Ext.Msg.alert('提示', "修改失败!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 parent.Ext.Msg.alert('提示', "修改失败!");
		 }
		});
}
function locateTypeRenderer(val){
	if(val=='0'){
		return '人员';
	}else if(val=='1'){
		return 'GPS';
	}else{
		return 'GPS';
	}
}


var proxy = new Ext.data.HttpProxy({
    url: path+'/terminal/terminal.do?method=listTerminal'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'simcard'},
    {name: 'deviceId'},
    {name: 'termName'},
    {name: 'termEndtime'},
    {name: 'groupName'},
    {name: 'groupId'},
    {name: 'province'},
    {name: 'city'},
    {name: 'startTime'},
    {name: 'endTime'},
    {name: 'getherInterval'},
    {name: 'termDesc'},
    {name: 'locateType'},
    {name: 'typeCode'},
    {name: 'typeName'},
    {name: 'vehicleNumber'},
    {name: 'vehicleType'},
    {name: 'driverNumber'},
    {name: 'oilSpeedLimit'},
    {name: 'holdAlarmFlag'},
    {name: 'speedAlarmLimit'},
    {name: 'speedAlarmLast'},
    {name: 'imgUrl'},
    {name: 'week'},
    {name: 'carTypeInfo'},
    {name: 'imsi'}
]);

var store = new Ext.data.Store({
	autoLoad: {params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) }},
    id: 'terminalstore',
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

var sm = new Ext.grid.RowSelectionModel({
	singleSelect: true,
	listeners: {
		rowselect: function(sm, row, rec) {
			Ext.getCmp('frmsimcard').setValue(rec.get('simcard'));
			Ext.getCmp('frmsimcardserial').setValue(rec.get('deviceId'));
			Ext.getCmp('frmimsiserial').setValue(rec.get('imsi'));
		    Ext.getCmp('frmname').setValue(rec.get('termName'));
		    Ext.getCmp('terminalEndtime').setValue(rec.get('termEndtime').substring(0,10));//修改终端到期时间
		    vcarnumber = rec.get('termName');
		    Ext.getCmp('selectGid').setValue(rec.get('groupName'));
		    vold_parent_id = rec.get('groupId');
		    vselectGid = rec.get('groupId');
		    Ext.getCmp('frmprovince').setValue(rec.get('province'));
		    Ext.getCmp('frmcity').setValue(rec.get('city'));
		    Ext.getCmp('frmstarttime').setValue(rec.get('startTime'));
		    Ext.getCmp('frmendtime').setValue(rec.get('endTime'));
		    Ext.getCmp('frmcollectinterval').setValue(rec.get('getherInterval'));
			Ext.getCmp('frmremark').setValue(rec.get('termDesc'));
			var tmplocateType = rec.get('locateType');
			Ext.getCmp('frmterminaltype').setValue(tmplocateType);
			var tmpweek = rec.get('week');
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
		    if(tmplocateType == 1){
				var tmpgpsComboBox = Ext.getCmp('gpsComboBox');
				var tmpcarFieldset = Ext.getCmp('carfieldset');
				tmpcarFieldset.expand(true);
				tmpgpsComboBox.enable();
				tmpgpsComboBox.setValue(rec.get('typeCode'));
			    Ext.getCmp('frmcarnumber').setValue(rec.get('vehicleNumber'));
			    Ext.getCmp('frmcarmodel').setValue(rec.get('vehicleType'));
			    Ext.getCmp('frmdrivernumber').setValue(rec.get('driverNumber'));
			    Ext.getCmp('frmoilelespeedlimit').setValue(rec.get('oilSpeedLimit'));
			    var tmpholdAlarmFlag = rec.get('holdAlarmFlag');
			    var tmpfrmjj = Ext.getCmp('frmjj');
			    if(tmpholdAlarmFlag == 1){
			    	tmpfrmjj.setValue(true);
			    }else{
			    	tmpfrmjj.setValue(false);
			    }
			    var tmpfrmspeedalarm = Ext.getCmp('frmspeedalarm');
			    var tmpspeedlimitvalue = Ext.getCmp('speedlimitvalue');
			    var tmpduration = Ext.getCmp('duration');
			    tmpfrmspeedalarm.setValue(true);
			    tmpspeedlimitvalue.enable();
				tmpduration.enable();
			    Ext.getCmp('speedlimitvalue').setValue(rec.get('speedAlarmLimit'));
			    Ext.getCmp('duration').setValue(rec.get('speedAlarmLast'));
				var tmpcarTypeInfo = rec.get('carTypeInfo');
				car_type_info_combo_t.setValue(tmpcarTypeInfo);
		    }
			icnCombo.setValue(rec.get('imgUrl'));
	    }
	}
});

function typeCodeRenderer(val){
	if(val == 'P-LENOVO'){
		return '联想';
	}else if(val == 'GP-SZHQ-GPRS'){
		return '华强';
	}
}


var gps_combo = new Ext.form.ComboBox( {
	hiddenName : 'typeCode',
	valueField : 'typeCode',
	id: 'gpsComboBox',
	store : new Ext.data.SimpleStore({
	  	fields:['typeCode', 'typeName'],
	  	data:[[]]
	  }),   
	displayField : 'typeName',
	//valueField :"abbr",   
    //displayField:'state',
    //typeAhead: true,
    mode: 'local',
	listeners: {
  	    expand : function(combo ) {
  	  		typeComboexpand();
    	}
    },
    disabled : true,
    editable: false,   
    //forceSelection: true,
    triggerAction: 'all',
    emptyText:'选择终端厂商'
    //selectOnFocus:true
});

function typeComboexpand(){
	Ext.Ajax.request({
	 url : path+'/terminal/terminal.do?method=getAllTermType',
	 method :'POST',
	 success : function(request) {
	   var res = request.responseText;
	   //layerStore.loadData(eval(res));
	   gps_combo.getStore().loadData(eval(res));
	 },
	 failure : function(request) {
	 }
	});
}

Ext.onReady(function(){
    var simple = new Ext.FormPanel({
            region: 'center',
	        labelWidth: 90, // label settings here cascade unless overridden
	        //url:'save-form.php',
	        frame:true,
	        //title: '用户参数',
	        bodyStyle:'padding:5px 5px 0',
	        autoScroll : true,
	        width: 200,
	        defaults: {width: 150},
	        defaultType: 'textfield',
	        items: [{
	                fieldLabel: '手机号码',
	                id: 'frmsimcard',
	                width: 150,
	                allowBlank:false
	            },{
	                fieldLabel: 'SIM卡序号',
	                id: 'frmsimcardserial',
	                readOnly : true,
	                width: 150
	            },{
	                fieldLabel: '手机卡序列号',
	                id: 'frmimsiserial',
	                readOnly : true,
	                width: 150
	            },{
	                fieldLabel: '姓名',
	                id: 'frmname',
	                width: 150,
	                allowBlank:false
	            },
	            //下拉列表组树
	            comboxWithTree,
	            //省
	            comboProvinces,
	            //市
	            comboCities,
	            new Ext.form.TimeField({
	            	id:'frmstarttime',
	            	fieldLabel: '上班时间',
	            	format:'H:i',
	            	value:'09:00',
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
	            	xtype:'datefield',
	            	fieldLabel: '终端到期时间',
	                id: 'terminalEndtime',	
	                format:'Y-m-d',
	                editable: false,
	                width: 150	        
	            },{
	            	id: 'weekCheckBoxGroup',
		            xtype: 'checkboxgroup',
		            fieldLabel: '工作日期',
	                width: 350,
		            items: [
				    	{boxLabel: '周1', name: 'cb-auto-1', value:'1', checked: true},
				        {boxLabel: '周2', name: 'cb-auto-2', value:'2', checked: true},
				        {boxLabel: '周3', name: 'cb-auto-3', value:'3', checked: true},
				        {boxLabel: '周4', name: 'cb-auto-4', value:'4', checked: true},
				        {boxLabel: '周5', name: 'cb-auto-5', value:'5', checked: true},
				        {boxLabel: '周6', name: 'cb-auto-6', value:'6', checked: true},
				        {boxLabel: '周日', name: 'cb-auto-7', value:'7', checked: true}
				    ]
		        },{
	            	xtype : 'textfield',
	                fieldLabel: '采集间隔(分钟)',
	                width: 150,
	                regex : /^(-|\+)?\d+(\.\d+)?$/,
	                id: 'frmcollectinterval'
	            },{
	            	xtype : 'textarea',
	                fieldLabel: '员工备注',
	                width: 150,
	                id: 'frmremark'
	            },{
				    xtype:'fieldset',
				    title: '终端类型',
				    autoHeight:true,
				    width: 300,
				    labelWidth: 50,
				    items :[{
			            xtype: 'radiogroup',
			            id : 'frmterminaltype',
			            //fieldLabel: 'Auto Layout',
			            items: [{
				                hideLabel : true,
				                boxLabel: '手机定位', 
				                name: 'rb-auto', 
				                inputValue: 0, 
				                checked: true
			                },{
				                hideLabel : true,
				                boxLabel: '车辆定位', 
				                name: 'rb-auto', 
				                inputValue: 1,
				                handler : function(){
				                	var tmpgpsComboBox = Ext.getCmp('gpsComboBox');
				                	var tmpcarFieldset = Ext.getCmp('carfieldset');
				                	
				                	if(this.checked){
				                		tmpcarFieldset.expand(true);
				                		tmpgpsComboBox.enable();
				                		icnCombo.getStore().loadData([['car', '车辆', 'x-flag-car'],['ship', '船', 'x-flag-ship']]);
				                		icnCombo.setValue('car');
				                	}else{
				                		tmpcarFieldset.collapse(true);
				                		tmpgpsComboBox.disable();
				                		icnCombo.getStore().loadData([['persion', '手机', 'x-flag-persion']]);
				                		icnCombo.setValue('persion');
				                	}
				                }
			            }]
			        }, gps_combo 
			        /*new Ext.form.ComboBox({
			        	id : 'gpsComboBox',
				        store: new Ext.data.ArrayStore({
					        fields: ['abbr', 'state'],
					        data : [['GP-SZHQ-GPRS','华强']]
					    }),
					    valueField :"abbr",   
				        displayField:'state',
				        typeAhead: true,
				        mode: 'local',
				        disabled : true,
				        editable: false,   
				        forceSelection: true,
				        triggerAction: 'all',
				        emptyText:'选择终端厂商',
				        selectOnFocus:true
				    })*/
			        ]
				  },{
					    xtype:'fieldset',
					    title: '图标',
					    autoHeight:true,
					    width: 300,
					    labelWidth: 50,
					    items :[icnCombo]
					  },{
		            xtype:'fieldset',
		            id : 'carfieldset',
		            //checkboxToggle:true,
		            title: '车辆信息',
		            autoHeight:true,
		            width: 300,
		            defaults: {width: 150},
		            defaultType: 'textfield',
		            collapsed: true,
		            items :[{
		                    fieldLabel: '车牌号',
		                    id : 'frmcarnumber',
		                    //name: 'first',
		                    width: 150,
		                    allowBlank:false
		                },{
		                    fieldLabel: '车辆型号',
		                    id : 'frmcarmodel',
		                    width: 150
		                    //name: 'last'
		                },{
		                    fieldLabel: '车主电话',
		                    id : 'frmdrivernumber',
		                    width: 150
		                    //name: 'company'
		                },
		                car_type_info_combo_t,
		                {
				            xtype:'fieldset',
				            title: '断油断电',
				            autoHeight:true,
				            width: 250,
				            defaults: {width: 250},
				            defaultType: 'textfield',
				            labelWidth: 100,
				            items :[{
				                    fieldLabel: '速度限值',
				                    id : 'frmoilelespeedlimit',
				                    //name: 'first',
				                    regex : /^(-|\+)?\d+(\.\d+)?$/,
				                    width: 100
				                    //allowBlank:false
				                }
				            ]
				        },{
				            xtype:'fieldset',
				            title: '报警设置',
				            autoHeight:true,
				            width: 250,
				            defaults: {width: 250},
				            labelWidth: 100,
				            items :[{
				                	xtype : 'checkbox',
				                	id : 'frmjj',
					                fieldLabel: ' ',
					                hideLabel : true,
					                width: 100,
					                checked : true,
					                boxLabel: '劫警'
					            },{
				                	xtype : 'checkbox',
					                fieldLabel: ' ',
					                width: 100,
					                id : 'frmspeedalarm',
					                hideLabel : true,
					                boxLabel: '超速报警',
					                handler : function(){
					                	var tmpspeedlimitvalue = Ext.getCmp('speedlimitvalue');
					                	var tmpduration = Ext.getCmp('duration');
					                	if(this.checked){
					                		tmpspeedlimitvalue.enable();
					                		tmpduration.enable();
					                	}else{
					                		tmpspeedlimitvalue.disable();
					                		tmpduration.disable();
					                	}
					                }
					            },{
					            	xtype : 'textfield',
				                    fieldLabel: '速度限值',
				                    id: 'speedlimitvalue',
				                    width: 100,
				                    disabled : true,
				                    value : '-1',
				                    regex : /^(-|\+)?\d+(\.\d+)?$/
				                    //allowBlank:false
				                },{
				                	xtype : 'textfield',
				                    fieldLabel: '持续时间(分钟)',
				                    id: 'duration',
				                    width: 100,
				                    regex : /^(-|\+)?\d+(\.\d+)?$/,
				                    disabled : true,
				                    value : '5'
				                    //allowBlank:false
				                }
				            ]
				        }
		            ]
		        }
	        ]
	    });

var userColumns =  [
	new Ext.grid.RowNumberer({header:'序号',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "手机号码",fixed:true, width: 100, sortable: true, dataIndex: 'simcard'},
    {header: "SIM卡序号",fixed:true, width: 100, sortable: true, dataIndex: 'deviceId'},
    {header: "姓名",fixed:true, width: 100, sortable: true, dataIndex: 'termName'},
    {header: "终端到期时间",fixed:true, width: 100, sortable: true, dataIndex: 'termEndtime', renderer: function(value){
		return value.substring(0, 10);
	}},
    {header: "所属组名称",fixed:true, width: 100, sortable: true, dataIndex: 'groupName'},
    {header: "groupid",fixed:true, width: 100, sortable: true, dataIndex: 'groupId',hidden:true},
    {header: "省",fixed:true, width: 70, sortable: true, dataIndex: 'province'},
    {header: "市",fixed:true, width: 70, sortable: true, dataIndex: 'city'},
    {header: "上班时间",fixed:true, width: 100, sortable: true, dataIndex: 'startTime'},
    {header: "下班时间",fixed:true, width: 100, sortable: true, dataIndex: 'endTime'},
    {header: "周工作时间",fixed:true, width: 160, sortable: true, dataIndex: 'week', renderer:function (val){
		var tmpweek = '';
		if((Number(val) & 1)==1){ tmpweek += '周1,'; }
		if((Number(val) & 2)==2){ tmpweek += '周2,'; }
		if((Number(val) & 4)==4){ tmpweek += '周3,'; }
		if((Number(val) & 8)==8){ tmpweek += '周4,'; }
		if((Number(val) & 16)==16){ tmpweek += '周5,'; }
		if((Number(val) & 32)==32){ tmpweek += '周6,'; }
		if((Number(val) & 64)==64){ tmpweek += '周日,'; }
		if(tmpweek.length>0){ tmpweek = tmpweek.substring(0,tmpweek.length-1); }
		return tmpweek;
	}},
    {header: "采集间隔",fixed:true, width: 100, sortable: true, dataIndex: 'getherInterval'},
    {header: "员工备注",fixed:true, width: 100, sortable: true, dataIndex: 'termDesc'},
    {header: "终端类型",fixed:true, width: 100, sortable: true, dataIndex: 'locateType',renderer:locateTypeRenderer},
    {header: "GPS厂商",fixed:true, width: 100, sortable: true, dataIndex: 'typeName'},
    //{header: "GPS厂商",fixed:true, width: 100, sortable: true, dataIndex: 'typeCode',renderer:typeCodeRenderer},
    {header: "车牌号",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleNumber'},
    {header: "车辆型号",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleType'},
    {header: "车主电话",fixed:true, width: 100, sortable: true, dataIndex: 'driverNumber'},
    {header: "断油断电-速度限值",fixed:true, width: 100, sortable: true, dataIndex: 'oilSpeedLimit'},
    {header: "劫警",fixed:true, width: 100, sortable: true, dataIndex: 'holdAlarmFlag'},
    {header: "超速报警-速度限值",fixed:true, width: 100, sortable: true, dataIndex: 'speedAlarmLimit'},
    {header: "超速报警-持续时间",fixed:true, width: 100, sortable: true, dataIndex: 'speedAlarmLast'},
    {header: "手机卡序列号",fixed:true, width: 100, sortable: true, dataIndex: 'imsi'}
];

userGrid = new Ext.grid.GridPanel({
        region: 'west',
        width: 500,
        loadMask: {msg:'查询中...'},
        //iconCls: 'icon-grid',
        //frame: true,
        //title: 'Users',
        //autoExpandColumn: 'name',
        enableColumnHide : false,
        store: store,
        sm : sm,
        //plugins: [editor],
        columns : userColumns,
        //sm : sm,
        //sm : smcheckbox,
        margins: '0 0 0 0',
		tbar: [
        	new Ext.form.TextField({
	            id: 'DeviceIdField',
	            width: 80,
	            enableKeyEvents: true,
	            listeners: {
        			keypress : function( textField, e ) {
		        		if (e.getKey() == e.ENTER) {
		        			searchValue = Ext.getCmp('DeviceIdField').getValue();
			    			store.load({params:{start:0, limit:20 , searchValue: encodeURI(searchValue) }});
			    			
		                }
        			}
        		}
		    }),'-',new Ext.Action({
		        text: '查询',
		        handler: function(){
			    	searchValue = Ext.getCmp('DeviceIdField').getValue();
	    			store.load({params:{start:0, limit:20, searchValue: encodeURI(searchValue) }});
	    			
		        },
		        iconCls: 'icon-search'
		    }),'-',delbut
        ],
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: store,
            displayInfo: true,
            displayMsg: '第{0}到第{1}条数据 共{2}条',
            emptyMsg: "没有数据"
        })
    });
	store.load({params:{start:0, limit:20}});
	
    var viewport = new Ext.Viewport({layout: 'border',items: [simple,userGrid]});
    
    
    tree.on('click',function(node){   
          comboxWithTree.setValue(node.text);   
          var tmpidArr = node.id.split('@');
          if(tmpidArr[0] == '-101'){
          	vselectGid = '-1';
          }else{
          	vselectGid = tmpidArr[0];
          }
          comboxWithTree.collapse();   
      });   
    comboxWithTree.on('expand',function(){   
        tree.render('tree');   
        this.innerList.dom.style.overflowX="auto"; //改变横向overflow的样式，显示横向的滚动条
      });   

    comboxWithTree.render('comboxWithTree');  
    typeComboexpand();
    carTypeInfotComboexpand();
});