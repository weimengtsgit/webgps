/**
 * ��Ա���ڱ��� employeeCheckReportGrid
 */
var employeeCheckReportSearchValue = '';
 
//��ȡ��ǰ����
var myDate = new Date();
var currY = myDate.getFullYear();
var currM = parseInt(myDate.getMonth())+1;
var maxDays = getDaysInMonth(currY,currM);
var week = ['һ','��','��','��','��','��','��'];

//�Ƿ���ʾ��
function showDayNum2(dnum){
	var day = dnum;
	if(parseInt(dnum) > parseInt(maxDays))
		day = '';
	return day;
}

//�Ƿ���ʾ�ܼ�
function showWeekNum2(dnum){

	var currW = '';
	
	if(showDayNum2(dnum) != ''){
		currW = week[dateToWeekCal(currY+''+currM+''+dnum)];
	}
	
	//�����պ�ɫչʾ
	if(currW == '��' || currW =='��')
		currW = '<font color="red">'+currW+'</font>';
		
	return currW;
}

//����window����
function buildDetailWin(title, html){

	   return new Ext.Window({
			title : title,
			//iconCls : 'icon-attr',
			width : 600,
			resizable : false,
			closeAction : 'hide',
			modal : true,
			border : false,
			height : 200,
			html: html
		});
	}

//stateNum���� �� 0��������  1�Ѹ�  2ȱ��  3����  4��� 5�ٵ�����
function showMsg(ymd,id,stateNum) {
	if(Number(stateNum) == 2){
		//return;
		html = '<html><body><table><div>' 
			  +'<tr><td>�޳���</td></tr>'
			  +'<tr><td>�޳���</td></tr>'
			  +'<tr><td>�����</td></tr>'
	   		  +'</div></table></body></html>';	
		
		buildDetailWin('ȱ����ϸ', html).show();	
	}
	//alert(ymd + '         '+id+'      '+stateNum);
	
    //��ȡ��̨������ϸ
	Ext.Ajax.request({
		url : path+'/employeeAttend/employeeAttend.do?method=attentdDetail',
		method : 'post',
		params : {
			ymd : ymd,
			deviceId : id,
			stateNum : stateNum
		},
		success : function(response, opts) {

			//alert(response.responseText);
			var json = Ext.decode(response.responseText);
			var html = '';
			
			//��������
			if(stateNum == 0){
				html = '<html><body><table><div>' 
					  +'<tr><td>ǩ��ʱ�䣺</td><td>'+json.v1+'</td></tr>'
					  +'<tr><td>ǩ��ʱ�䣺</td><td>'+json.v2+'</td></tr>'
					  +'<tr><td>ǩ��λ�ã�</td><td>'+json.v3+'</td></tr>'
					  +'<tr><td>ǩ��λ�ã�</td><td>'+json.v4+'</td></tr>'  
			   		  +'</div></table></body></html>';	
				
				buildDetailWin('����������ϸ', html).show();
			}
			
			//�Ѹ�
			if(stateNum == 1){
				html = '<html><body><table><div>' 
					  +'<tr><td>ǩ��ʱ�䣺</td><td>'+json.v1+'</td></tr>'
					  +'<tr><td>ǩ��ʱ�䣺</td><td>'+json.v2+'</td></tr>'
					  +'<tr><td>ǩ��λ�ã�</td><td>'+json.v3+'</td></tr>'
					  +'<tr><td>ǩ��λ�ã�</td><td>'+json.v4+'</td></tr>' 
			   		  +'</div></table></body></html>';	
				
				buildDetailWin('�Ѹ���ϸ', html).show();
			}
			
		    //����
			if(stateNum == 3){
				html = '<html><body><table><div>' 
					  +'<tr><td>��ʼ����ʱ�䣺</td><td>'+json.v1+'</td></tr>'
					  +'<tr><td>��ʼ����λ�ã�</td><td>'+json.v2+'</td></tr>'
					  +'<tr><td>��������ʱ�䣺</td><td>'+json.v3+'</td></tr>'
					  +'<tr><td>��������λ�ã�</td><td>'+json.v4+'</td></tr>'
			   		  +'</div></table></body></html>';	
				
				buildDetailWin('������ϸ', html).show();
			}
			
			//���
			if(stateNum == 4){
				html = '<html><body><table><div>' 
					  +'<tr><td>������ɣ�</td><td>'+json.v1+'</td></tr>'
					  +'<tr><td>���������</td><td>'+json.v2+'</td></tr>'
			   		  +'</div></table></body></html>';	
				
				buildDetailWin('�����ϸ', html).show();	
			}
			
			//�ٵ�����
			if(stateNum == 5){
				html = '<html><body><table><div>' 
					  +'<tr><td>ǩ��ʱ�䣺</td><td>'+json.v1+'</td></tr>'
					  +'<tr><td>ǩ��ʱ�䣺</td><td>'+json.v2+'</td></tr>'
					  +'<tr><td>ǩ��λ�ã�</td><td>'+json.v3+'</td></tr>'
					  +'<tr><td>ǩ��λ�ã�</td><td>'+json.v4+'</td></tr>'  
			   		  +'</div></table></body></html>';	
				
				buildDetailWin('�ٵ�������ϸ', html).show();				
			}
			if(stateNum == 2){
				html = '<html><body><table><div>' 
					  +'<tr><td>�޳���</td></tr>'
					  +'<tr><td>�޿���</td></tr>'
					  +'<tr><td>�����</td></tr>'
			   		  +'</div></table></body></html>';	
				
				buildDetailWin('ȱ����ϸ', html).show();				
			}
			
		},
		failure : function(response, opts) {
			Ext.Msg.alert("������Ϣ", '��ȡ��Ϣʧ�ܣ�');
		}
	});
	
}

//��ȡҪչʾͼƬ
function img(i) {
	
	if(i == '0') {
		return "<img src=images/mainV2.1/state1.gif />";
	} else if(i == '1') {
		return "<img src=images/mainV2.1/state2-m.gif />";
	} else if(i == '2') {
		return "<img src=images/mainV2.1/state3.gif />";
	} else if(i == '3') {
		return "<img src=images/mainV2.1/state4.gif />";
	} else if(i == '4') {
		return "<img src=images/mainV2.1/state5.gif />";
	} else if(i == '5') {
		return "<img src=images/mainV2.1/state6.gif />";
	}else if( i == '-1'){
		return  "";
	}else{
		return "";
	}
}

function hrefChange(value, metadata, record, rowIndex, columnIndex, store) {

	var id = record.get('deviceId');
	//��һ��ͼƬ��indexΪ1
	//var day = (parseInt(columnIndex) - 3);
	var day = (parseInt(columnIndex) -4);
	var year = Ext.getCmp('year').getValue();
	var month = Ext.getCmp('month').getValue();
	var stateNum = record.get('day'+day);
	
	return "<a href=javascript:showMsg('" + (year+','+month+','+day)+ "','"+id+"','"+stateNum+"')>"+img(stateNum)+"</a>";
	
}

function simcardRender(value, metadata, record, rowIndex, columnIndex, store) {
	if(value == 'null'){
		return '';
	}else{
		return value;
	}
}

var yearCombo = new Ext.form.ComboBox({
	style : 'top:0px;',
	displayField : 'name',
	id : 'year',
	valueField : 'value',
	typeAhead : true,
	mode : 'local',
	triggerAction : 'all',
	store : new Ext.data.SimpleStore({
				fields : ['name', 'value'],
				data : yearComboxData
			}),
	selectOnFocus : true,
	editable : false,
	width : 80
});

yearCombo.setValue(currY);//ѡ�� 

var monthCombo = new Ext.form.ComboBox({
	style : 'top:0px;',
	displayField : 'name',
	id : 'month',
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

monthCombo.setValue(currM);//ѡ�� 

var proxy = new  Ext.data.HttpProxy({url: path+'/employeeAttend/employeeAttend.do?method=listEmpAttend1'});
var reader = new Ext.data.JsonReader({totalProperty: 'total',successProperty: 'success', idProperty: 'id', root: 'data' }, 
		[{name:'id'},
		 {name:'termName'},
		 {name:'deviceId'},
		 {name:'groupName'},
		 {name:'simcard'},
		 {name:'attendDays'},
		 {name:'noattendDays'},
		 {name:'noWorkDays'},
		 {name:'lateOutDays'},
		 {name:'vacateDays'},
		 {name:'day1'},
		 {name:'day2'},
		 {name:'day3'},
		 {name:'day4'},
		 {name:'day5'},
		 {name:'day6'},
		 {name:'day7'},
		 {name:'day8'},
		 {name:'day9'},
		 {name:'day10'},
		 {name:'day11'},
		 {name:'day12'},
		 {name:'day13'},
		 {name:'day14'},
		 {name:'day15'},
		 {name:'day16'},
		 {name:'day17'},
		 {name:'day18'},
		 {name:'day19'},
		 {name:'day20'},
		 {name:'day21'},
		 {name:'day22'},
		 {name:'day23'},
		 {name:'day24'},
		 {name:'day25'},
		 {name:'day26'},
		 {name:'day27'},
		 {name:'day28'},
		 {name:'day29'},
		 {name:'day30'},
		 {name:'day31'}
		 ]);
var empAtteStore = new Ext.data.Store({
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
					
					var year = Ext.getCmp('year').getValue();
					var month = Ext.getCmp('month').getValue();
					//������ͷ
					buildGirdHeaderAndGridCellValue2(year, month);
					
				}
			}
		}
   });

//��Ա���ڱ���
var employeeCheckReportGrid = {
	xtype: 'grid',
	id: 'employeeCheckReportGrid',
	enableHdMenu: false,
	enableColumnMove: false,
	store: empAtteStore,
	loadMask: {msg: main.loading},
	  colModel: new Ext.grid.ColumnModel({
		    columns: [
		        {header: 'id', width: 160, dataIndex: 'id', hidden:true,align: 'center'},
		        {header: 'deviceId', width: 160, dataIndex: 'deviceId', hidden:true,align: 'center'},
		        {header: 'Ա������', width: 160, dataIndex: 'termName', align: 'center'},
		        {header: '����', width: 160, dataIndex: 'groupName', align: 'center'},
		        {header: '�ֻ�����', width: 160, dataIndex: 'simcard', align: 'center',renderer : simcardRender},
	            {header: '<div id="ww1">'+showWeekNum2('01')+'</div>', width: 50, dataIndex: 'day1', align: 'center' ,renderer : hrefChange},
	            {header: '<div id="ww2">'+showWeekNum2('02')+'</div>', width: 50, dataIndex: 'day2', align: 'center' ,renderer : hrefChange},
	            {header: '<div id="ww3">'+showWeekNum2('03')+'</div>', width: 50, dataIndex: 'day3', align: 'center' ,renderer : hrefChange},
	            {header: '<div id="ww4">'+showWeekNum2('04')+'</div>', width: 50, dataIndex: 'day4', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww5">'+showWeekNum2('05')+'</div>', width: 50, dataIndex: 'day5', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww6">'+showWeekNum2('06')+'</div>', width: 50, dataIndex: 'day6', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww7">'+showWeekNum2('07')+'</div>', width: 50, dataIndex: 'day7', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww8">'+showWeekNum2('08')+'</div>', width: 50, dataIndex: 'day8', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww9">'+showWeekNum2('09')+'</div>', width: 50, dataIndex: 'day9', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww10">'+showWeekNum2('10')+'</div>', width: 50, dataIndex: 'day10', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww11">'+showWeekNum2('11')+'</div>', width: 50, dataIndex: 'day11', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww12">'+showWeekNum2('12')+'</div>', width: 50, dataIndex: 'day12', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww13">'+showWeekNum2('13')+'</div>', width: 50, dataIndex: 'day13', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww14">'+showWeekNum2('14')+'</div>', width: 50, dataIndex: 'day14', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww15">'+showWeekNum2('15')+'</div>', width: 50, dataIndex: 'day15', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww16">'+showWeekNum2('16')+'</div>', width: 50, dataIndex: 'day16', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww17">'+showWeekNum2('17')+'</div>', width: 50, dataIndex: 'day17', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww18">'+showWeekNum2('18')+'</div>', width: 50, dataIndex: 'day18', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww19">'+showWeekNum2('19')+'</div>', width: 50, dataIndex: 'day19', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww20">'+showWeekNum2('20')+'</div>', width: 50, dataIndex: 'day20', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww21">'+showWeekNum2('21')+'</div>', width: 50, dataIndex: 'day21', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww22">'+showWeekNum2('22')+'</div>', width: 50, dataIndex: 'day22', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww23">'+showWeekNum2('23')+'</div>', width: 50, dataIndex: 'day23', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww24">'+showWeekNum2('24')+'</div>', width: 50, dataIndex: 'day24', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww25">'+showWeekNum2('25')+'</div>', width: 50, dataIndex: 'day25', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww26">'+showWeekNum2('26')+'</div>', width: 50, dataIndex: 'day26', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww27">'+showWeekNum2('27')+'</div>', width: 50, dataIndex: 'day27', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww28">'+showWeekNum2('28')+'</div>', width: 50, dataIndex: 'day28', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww29">'+showWeekNum2('29')+'</div>', width: 50, dataIndex: 'day29', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww30">'+showWeekNum2('30')+'</div>', width: 50, dataIndex: 'day30', align: 'center',renderer : hrefChange},
	            {header: '<div id="ww31">'+showWeekNum2('31')+'</div>', width: 50, dataIndex: 'day31', align: 'center',renderer : hrefChange},
		        {header: '��������', width: 80, dataIndex: 'attendDays', align: 'center'},
		        {header: 'ȱ������', width: 80, dataIndex: 'noattendDays', align: 'center'},
		        {header: '�Ѹ�����', width: 80, dataIndex: 'noWorkDays', align: 'center'},
		        {header: '�ٵ���������', width: 120, dataIndex: 'lateOutDays', align: 'center'},
		        {header: '�������', width: 80, dataIndex: 'vacateDays', align: 'center'}
		    ],
		    rows: [
		     [
		      {rowspan: 2}
		     ], [{},{},{},{},
		         {header: '<div id="dd1">'+showDayNum2('01')+'</div>', colspan: 1, align: 'center'},
					{header: '<div id="dd2">'+showDayNum2('02')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd3">'+showDayNum2('03')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd4">'+showDayNum2('04')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd5">'+showDayNum2('05')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd6">'+showDayNum2('06')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd7">'+showDayNum2('07')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd8">'+showDayNum2('08')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd9">'+showDayNum2('09')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd10">'+showDayNum2('10')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd11">'+showDayNum2('11')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd12">'+showDayNum2('12')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd13">'+showDayNum2('13')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd14">'+showDayNum2('14')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd15">'+showDayNum2('15')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd16">'+showDayNum2('16')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd17">'+showDayNum2('17')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd18">'+showDayNum2('18')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd19">'+showDayNum2('19')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd20">'+showDayNum2('20')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd21">'+showDayNum2('21')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd22">'+showDayNum2('22')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd23">'+showDayNum2('23')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd24">'+showDayNum2('24')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd25">'+showDayNum2('25')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd26">'+showDayNum2('26')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd27">'+showDayNum2('27')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd28">'+showDayNum2('28')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd29">'+showDayNum2('29')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd30">'+showDayNum2('30')+'</div>', colspan: 1, align: 'center'},
			        {header: '<div id="dd31">'+showDayNum2('31')+'</div>', colspan: 1, align: 'center'}
		     ]
		    ]
		   }),
		   enableColumnMove: false,
		   enableHdMenu: false,
		   enableColumnHide:false,
		   plugins: [new Ext.ux.plugins.GroupHeaderGrid()],
	stripeRows: true,
	stateful: true,
	stateId: 'employeeCheckReportGridId',
	tbar : {
		xtype: 'toolbar',
		items:[
		    {xtype: 'label',text: '��'}, yearCombo, {xtype: 'label',text: '��'},monthCombo,'-',
			{xtype: 'button',text: main.search,iconCls: 'icon-search',handler: function(){
			 
				var year = Ext.getCmp('year').getValue();
				var month = Ext.getCmp('month').getValue();
				
				if(year == '' ||  month == ''){
					alert('��ѡ������!');
					return ;
				}
				
				var travelcostDeviceids = getDeviceId();
				
				empAtteStore.baseParams = {
					//start : 0,
					//limit : 20,
					year:year,
					month:month,
					deviceIds:travelcostDeviceids
					
				};
				empAtteStore.load({
					params : {
						//start : 0,
						//limit : 20,
						year:year,
						month:month,
						deviceIds:travelcostDeviceids
					}
				});
				
			}},'-',
			{xtype: 'button',text: '����Excel',iconCls: 'icon-excel',handler: function(){
				//var year = Ext.getCmp('year').getValue();
				//alert(year);
				//var month = Ext.getCmp('month').getValue();
				
				var year = Ext.getCmp('year').getValue();
				var month = Ext.getCmp('month').getValue();
				
				if(year == '' ||  month == ''){
					alert('��ѡ������!');
					return ;
				}
				
				var travelcostDeviceids = getDeviceId();
				
				if(travelcostDeviceids == ''){
					Ext.Msg.alert('��ʾ', '��ѡ���ն�!');
					return;
				}else{
					document.excelform.action = encodeURI(encodeURI(path + '/employeeAttend/employeeAttend.do?method=exportEmpAttendInfo1&year='+year+'&month='+month+'&deviceIds='+travelcostDeviceids));
					document.excelform.submit();
				}
				
			}},'-',
			{xtype: 'label',text: '˵���� �հ�  ��Ϣ��'},
			{xtype: 'button'},
			{xtype: 'label',text: '����������'},
			{xtype: 'button'},
			{xtype: 'label',text: '���Ѹ�'},
			{xtype: 'button'},
			{xtype: 'label',text: '��ȱ��'},
			{xtype: 'button'},
			{xtype: 'label',text: '������'},
			{xtype: 'button'},
			{xtype: 'label',text: '�����'},
			{xtype: 'button'},
			{xtype: 'label',text: '���ٵ�����'},
			{xtype: 'button'}
		]
	}/*,
	bbar: new Ext.PagingToolbar({
		pageSize: 15,
		store: empAtteStore,
		displayInfo: true,
		displayMsg: '��{0}����{1}������ ��{2}��',
		emptyMsg: "û������"
	})*/
};


/**
 * ��̬����Gird
 */
function buildGirdHeaderAndGridCellValue2(year, month){

	//���
	for(var i=1; i<=31; i++){
		document.getElementById('dd'+i).innerHTML = '';
		document.getElementById('ww'+i).innerHTML = '';
	}
	
	//���¸�ֵ
	for(var dayNum=1; dayNum <= getDaysInMonth(year,month); dayNum++){
		var dayNum_ = dayNum;
		if(dayNum<10){
			dayNum_ = "0"+dayNum;
		}
		var weekIndex = dateToWeekCal(year+''+month+''+dayNum_);
		
		//alert(year+'        '+month+'        '+dayNum+'    '+weekIndex);
		
		var w = week[weekIndex];
		document.getElementById('dd'+dayNum).innerHTML = dayNum;
		
		//�����պ�ɫչʾ
		if(w == '��' || w =='��')
			document.getElementById('ww'+dayNum).innerHTML = '<font color="red">'+w+'</font>';
		else
			document.getElementById('ww'+dayNum).innerHTML = w;
	}
}
