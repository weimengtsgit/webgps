
/**
 * 公司级考勤汇总报表 companyCheckTotalReport 2012-11-07
 */
var companyCheckTotalReportSearchValue = '';
var companyCheckTotalReportGrid = '';

//获取当前日期
var myDate = new Date();
var currY = myDate.getFullYear();
var currM = parseInt(myDate.getMonth())+1;
var maxDays = getDaysInMonth(currY,currM);
var week = ['一','二','三','四','五','六','日'];

//是否显示日
function showDayNum(dnum){
	var day = dnum;
	if(parseInt(dnum) > parseInt(maxDays))
		day = '';
	return day;
}

//是否显示周几
function showWeekNum(dnum){

	var currW = '';
	
	if(showDayNum(dnum) != ''){
		var weekIndex = dateToWeekCal(currY+''+currM+''+dnum);
		currW = week[weekIndex];
	}
	
	//六，日红色展示
	if(currW == '六' || currW =='日')
		currW = '<font color="red">'+currW+'</font>';
		
	return currW;
}

var yc = new Ext.form.ComboBox({
	style : 'top:0px;',
	displayField : 'name',
	id : 'y',
	valueField : 'value',
	typeAhead : true,
	mode : 'local',
	triggerAction : 'all',
	store : new Ext.data.SimpleStore({
				fields : ['name', 'value'],
				data :  yearComboxData
			}),
	selectOnFocus : true,
	editable : false,
	width : 80
});

yc.setValue(currY);//选中 

var mc = new Ext.form.ComboBox({
	style : 'top:0px;',
	displayField : 'name',
	id : 'm',
	valueField : 'value',
	typeAhead : true,
	mode : 'local',
	triggerAction : 'all',
	store : new Ext.data.SimpleStore({
				fields : ['name', 'value'],
				data : monthComboxData
			}),
	selectOnFocus : true,
	editable : false,
	width : 80
});

mc.setValue(currM);//选中 


var proxy = new  Ext.data.HttpProxy({url: path+'/employeeAttend/employeeAttend.do?method=listCompanyEmpAttend'});
var reader = new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'id', root: 'data' }, 
		[
		 {name:'attendMans0'},
		 {name:'attendMans1'},
		 {name:'attendMans2'},
		 {name:'attendMans3'},
		 {name:'attendMans4'},
		 {name:'attendMans5'},
		 {name:'attendMans6'},
		 {name:'attendMans7'},
		 {name:'attendMans8'},
		 {name:'attendMans9'},
		 {name:'attendMans10'},
		 {name:'attendMans11'},
		 {name:'attendMans12'},
		 {name:'attendMans13'},
		 {name:'attendMans14'},
		 {name:'attendMans15'},
		 {name:'attendMans16'},
		 {name:'attendMans17'},
		 {name:'attendMans18'},
		 {name:'attendMans19'},
		 {name:'attendMans20'},
		 {name:'attendMans21'},
		 {name:'attendMans22'},
		 {name:'attendMans23'},
		 {name:'attendMans24'},
		 {name:'attendMans25'},
		 {name:'attendMans26'},
		 {name:'attendMans27'},
		 {name:'attendMans28'},
		 {name:'attendMans29'},
		 {name:'attendMans30'},
		 {name:'attendMans31'}
		 ]);

var companyCheckTotalReportStore = new Ext.data.Store({
	restful: true,
	proxy:proxy,
	reader:reader,
	 listeners:{
			beforeload:{
				fn: function(thiz,options){
					var visitStatReportGpsDeviceids = getDeviceId();
					this.baseParams ={
							year : currY,
							month: currM,
							deviceIds:visitStatReportGpsDeviceids
			    	};
				}
			},load:{
				fn: function(thiz,options){
					var year = Ext.getCmp('y').getValue();
	        		var month = Ext.getCmp('m').getValue();
					//构建表头
					buildGirdHeaderAndGridCellValue(year, month);
				}
			}
		}
   });


var result_type_arr = ['出勤人数','缺勤人数','脱岗人数','迟到早退人数','请假人数'];
Ext.grid.RowNumberer = Ext.extend(Ext.grid.RowNumberer, {
    width : 200, 
    align: 'center',  
    renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
        return result_type_arr[rowIndex];   
    }   
}); 

//公司级考勤汇总报表
 companyCheckTotalReportGrid = {
	xtype: 'grid',
	id: 'companyCheckTotalReportGrid',
	enableHdMenu: false,
	enableColumnMove: false,
	store: companyCheckTotalReportStore,
	loadMask: {msg: main.loading},
	   colModel: new Ext.grid.ColumnModel({
		    columns: [
		      //new Ext.grid.RowNumberer(),
		        {header: '', width: 150, dataIndex: 'attendMans0', align: 'center'},
	            {header: '<div id="w1">'+showWeekNum('01')+'</div>', width: 50, dataIndex: 'attendMans1', align: 'center'},
	            {header: '<div id="w2">'+showWeekNum('02')+'</div>', width: 50, dataIndex: 'attendMans2', align: 'center'},
	            {header: '<div id="w3">'+showWeekNum('03')+'</div>', width: 50, dataIndex: 'attendMans3', align: 'center'},
	            {header: '<div id="w4">'+showWeekNum('04')+'</div>', width: 50, dataIndex: 'attendMans4', align: 'center'},
	            {header: '<div id="w5">'+showWeekNum('05')+'</div>', width: 50, dataIndex: 'attendMans5', align: 'center'},
	            {header: '<div id="w6">'+showWeekNum('06')+'</div>', width: 50, dataIndex: 'attendMans6', align: 'center'},
	            {header: '<div id="w7">'+showWeekNum('07')+'</div>', width: 50, dataIndex: 'attendMans7', align: 'center'},
	            {header: '<div id="w8">'+showWeekNum('08')+'</div>', width: 50, dataIndex: 'attendMans8', align: 'center'},
	            {header: '<div id="w9">'+showWeekNum('09')+'</div>', width: 50, dataIndex: 'attendMans9', align: 'center'},
	            {header: '<div id="w10">'+showWeekNum(10)+'</div>', width: 50, dataIndex: 'attendMans10', align: 'center'},
	            {header: '<div id="w11">'+showWeekNum(11)+'</div>', width: 50, dataIndex: 'attendMans11', align: 'center'},
	            {header: '<div id="w12">'+showWeekNum(12)+'</div>', width: 50, dataIndex: 'attendMans12', align: 'center'},
	            {header: '<div id="w13">'+showWeekNum(13)+'</div>', width: 50, dataIndex: 'attendMans13', align: 'center'},
	            {header: '<div id="w14">'+showWeekNum(14)+'</div>', width: 50, dataIndex: 'attendMans14', align: 'center'},
	            {header: '<div id="w15">'+showWeekNum(15)+'</div>', width: 50, dataIndex: 'attendMans15', align: 'center'},
	            {header: '<div id="w16">'+showWeekNum(16)+'</div>', width: 50, dataIndex: 'attendMans16', align: 'center'},
	            {header: '<div id="w17">'+showWeekNum(17)+'</div>', width: 50, dataIndex: 'attendMans17', align: 'center'},
	            {header: '<div id="w18">'+showWeekNum(18)+'</div>', width: 50, dataIndex: 'attendMans18', align: 'center'},
	            {header: '<div id="w19">'+showWeekNum(19)+'</div>', width: 50, dataIndex: 'attendMans19', align: 'center'},
	            {header: '<div id="w20">'+showWeekNum(20)+'</div>', width: 50, dataIndex: 'attendMans20', align: 'center'},
	            {header: '<div id="w21">'+showWeekNum(21)+'</div>', width: 50, dataIndex: 'attendMans21', align: 'center'},
	            {header: '<div id="w22">'+showWeekNum(22)+'</div>', width: 50, dataIndex: 'attendMans22', align: 'center'},
	            {header: '<div id="w23">'+showWeekNum(23)+'</div>', width: 50, dataIndex: 'attendMans23', align: 'center'},
	            {header: '<div id="w24">'+showWeekNum(24)+'</div>', width: 50, dataIndex: 'attendMans24', align: 'center'},
	            {header: '<div id="w25">'+showWeekNum(25)+'</div>', width: 50, dataIndex: 'attendMans25', align: 'center'},
	            {header: '<div id="w26">'+showWeekNum(26)+'</div>', width: 50, dataIndex: 'attendMans26', align: 'center'},
	            {header: '<div id="w27">'+showWeekNum(27)+'</div>', width: 50, dataIndex: 'attendMans27', align: 'center'},
	            {header: '<div id="w28">'+showWeekNum(28)+'</div>', width: 50, dataIndex: 'attendMans28', align: 'center'},
	            {header: '<div id="w29">'+showWeekNum(29)+'</div>', width: 50, dataIndex: 'attendMans29', align: 'center'},
	            {header: '<div id="w30">'+showWeekNum(30)+'</div>', width: 50, dataIndex: 'attendMans30', align: 'center'},
	            {header: '<div id="w31">'+showWeekNum(31)+'</div>', width: 50, dataIndex: 'attendMans31', align: 'center'}
		    ],
		    rows: [
		     [
		      {rowspan: 2}
		     ], [
				{header: '<div id="d1">'+showDayNum('01')+'</div>', colspan: 1, align: 'center'},
				{header: '<div id="d2">'+showDayNum('02')+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d3">'+showDayNum('03')+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d4">'+showDayNum('04')+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d5">'+showDayNum('05')+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d6">'+showDayNum('06')+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d7">'+showDayNum('07')+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d8">'+showDayNum('08')+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d9">'+showDayNum('09')+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d10">'+showDayNum(10)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d11">'+showDayNum(11)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d12">'+showDayNum(12)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d13">'+showDayNum(13)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d14">'+showDayNum(14)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d15">'+showDayNum(15)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d16">'+showDayNum(16)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d17">'+showDayNum(17)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d18">'+showDayNum(18)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d19">'+showDayNum(19)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d20">'+showDayNum(20)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d21">'+showDayNum(21)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d22">'+showDayNum(22)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d23">'+showDayNum(23)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d24">'+showDayNum(24)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d25">'+showDayNum(25)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d26">'+showDayNum(26)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d27">'+showDayNum(27)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d28">'+showDayNum(28)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d29">'+showDayNum(29)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d30">'+showDayNum(30)+'</div>', colspan: 1, align: 'center'},
		        {header: '<div id="d31">'+showDayNum(31)+'</div>', colspan: 1, align: 'center'}
		     ]
		    ]
		   }),
		   enableColumnMove: false,
		   enableHdMenu: false,
		   enableColumnHide:false,
		   plugins: [new Ext.ux.plugins.GroupHeaderGrid()],
		   stripeRows: true,
		   stateful: true,
		   stateId: 'companyCheckTotalReportGridId',
		   tbar : {
			   xtype: 'toolbar',
			   items:[ {xtype: 'label',text: ' 年   '}, yc,' ',{xtype: 'label',text: ' 月  '} ,mc, '-',
			           {xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
			        	   
			        	    var year = Ext.getCmp('y').getValue();
			        		var month = Ext.getCmp('m').getValue();
			        		
			        		if(year == '' ||  month == ''){
			        			alert('请选择年月!');
			        			return ;
			        		}
			        	   
							var travelcostDeviceids = getDeviceId();
							companyCheckTotalReportStore.baseParams = {
								start : 0,
								limit : 20,
								year : year,
								month:month,
								deviceIds:travelcostDeviceids
								
							};
							companyCheckTotalReportStore.load({
								params : {
									start : 0,
									limit : 20,
									year : year,
									month:month,
									deviceIds:travelcostDeviceids
								}
							});
							
			}},{xtype: 'button',text: '导出Excel',iconCls: 'icon-excel',handler: function(){
	        	   
	        	    var year = Ext.getCmp('y').getValue();
	        		var month = Ext.getCmp('m').getValue();
	        		
	        		if(year == '' ||  month == ''){
	        			Ext.Msg.alert('请选择年月!');
	        			return ;
	        		}
	        	   
					var travelcostDeviceids = getDeviceId();
					if(travelcostDeviceids == ''){
						Ext.Msg.alert('提示', '请选择终端!');
						return;
					}else{
						document.excelform.action = encodeURI(encodeURI(path + '/employeeAttend/employeeAttend.do?method=exportCompanyEmpAttend&year='+year+'&month='+month+'&deviceIds='+travelcostDeviceids));
						document.excelform.submit();
					}
					
			}}
		]
	},
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: companyCheckTotalReportStore,
		displayInfo: true,
		displayMsg: '第{0}到第{1}条数据 共{2}条',
		emptyMsg: "没有数据"
	})
};

/**
 * 动态构建Gird
 */
function buildGirdHeaderAndGridCellValue(year, month){

	//清空
	for(var i=1; i<=31; i++){
		document.getElementById('d'+i).innerHTML = '';
		document.getElementById('w'+i).innerHTML = '';
	}
	
	var maxDays = getDaysInMonth(year,month);
	
	//重新赋值
	for(var dayNum=1; dayNum <= maxDays; dayNum++){
		var dayNum_ = dayNum;
		if(dayNum<10){
			dayNum_ = "0"+dayNum;
		}
		var w = week[dateToWeekCal(year+''+month+''+dayNum_)];
		document.getElementById('d'+dayNum).innerHTML = dayNum;
		
		//六，日红色展示
		if(w == '六' || w =='日')
			document.getElementById('w'+dayNum).innerHTML = '<font color="red">'+w+'</font>';
		else
			document.getElementById('w'+dayNum).innerHTML = w;
	}
	
}
