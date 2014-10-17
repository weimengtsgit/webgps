<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%@ page import="java.util.*" %>
<%@ page import="com.sosgps.wzt.util.*" %>
<%@ page import="com.sosgps.v21.util.DateUtils" %>
<%@ page import="com.sosgps.v21.util.DateToWeekUtil" %>

<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");

if(user==null){
	response.sendRedirect(path+"/error.jsp");
	return;
}
//add "entTime" by zhaofeng  time:2011-6-7  
SimpleDateFormat df= new SimpleDateFormat("yyyy年MM月dd日");
String endTime = "";
if(user.getEnt().getEndTime()!=null){
	endTime="服务到期时间："+CharTools.javaScriptEscape(df.format(user.getEnt().getEndTime()).toString());
}

String entCode = user.getEmpCode();
Long userId = user.getUserId();
Long maxUserNum=user.getEnt().getMaxUserNum();
String empCode64 = user.getEmpCodeBase64();
String password64 = user.getPasswordBase64();
String account64 = user.getUserAccountBase64();
String mmsAccount = user.getEnt().getMmsAccount() == null ? "" : user.getEnt().getMmsAccount();
String mmsPwd = user.getEnt().getMmsPwd() == null ? "" : user.getEnt().getMmsPwd();

String edition = (String)request.getSession().getAttribute("edition");
String cooperation = (String)request.getSession().getAttribute("cooperation");
cooperation = CharTools.killNullString(cooperation, "");
String entname = user.getUserAccount();
String mainpage = (String)request.getSession().getAttribute("module_htmlSb");
Hashtable<String,String> mode3_map = (Hashtable)request.getSession().getAttribute("mode3_map");
Hashtable<String,String> mode5_report = (Hashtable)request.getSession().getAttribute("mode5_report");
String area_alarm_report = CharTools.javaScriptEscape(mode5_report.get("area_alarm_report"));
String licheng_report = CharTools.javaScriptEscape(mode5_report.get("licheng_report"));
String speed_alarm_report = CharTools.javaScriptEscape(mode5_report.get("speed_alarm_report"));
String active_alarm_report = CharTools.javaScriptEscape(mode5_report.get("active_alarm_report"));
String visit_stat_report = CharTools.javaScriptEscape(mode5_report.get("visit_stat_report"));
String cusvisit_stat_report = CharTools.javaScriptEscape(mode5_report.get("cusvisit_stat_report"));
String attendance_report = CharTools.javaScriptEscape(mode5_report.get("attendance_report"));
String vehicle_loc_report =CharTools.javaScriptEscape(mode5_report.get("vehicle_loc_report"));
String vehicle_msg_stat_report =CharTools.javaScriptEscape(mode5_report.get("vehicle_msg_stat_report"));
String diary_report =CharTools.javaScriptEscape(mode5_report.get("diary_report"));
String totalDistanceStat_report =CharTools.javaScriptEscape(mode5_report.get("totalDistanceStat_report"));
String door_report =CharTools.javaScriptEscape(mode5_report.get("door_report"));
String timeDistance_report =CharTools.javaScriptEscape(mode5_report.get("timeDistance_report"));
String kqtj = CharTools.javaScriptEscape(mode5_report.get("kqtj"));
String structions_report = CharTools.javaScriptEscape(mode5_report.get("structions_report"));

Hashtable<String,String> mode4_loc = (Hashtable)request.getSession().getAttribute("mode4_loc");
int mode4_loc_len = mode4_loc.size();
String sms_center =CharTools.javaScriptEscape(mode4_loc.get("sms_center"));
String diary_center =CharTools.javaScriptEscape(mode4_loc.get("diary_center"));
String diary_add =CharTools.javaScriptEscape(mode4_loc.get("diary_add"));
String diary_remark =CharTools.javaScriptEscape(mode4_loc.get("diary_remark"));

//add by liuhongxiao 2012-01-30
String infoAcquisitionUrl = user.getInfoAcquisitionUrl();
String moduleType = request.getParameter("moduleType");//信息采集跳转0-定位平台,1-管理中心
if(moduleType==null || moduleType.length()<1){
	moduleType = "-1";//不是跳转
}

//2012-8-21 weimeng v2.1
String visit_report = CharTools.javaScriptEscape(mode5_report.get("visit_report"));
String visit_rank_report = CharTools.javaScriptEscape(mode5_report.get("visit_rank_report"));
String signBill_report = CharTools.javaScriptEscape(mode5_report.get("signBill_report"));
String signBill_detail = CharTools.javaScriptEscape(mode5_report.get("signBill_detail"));
String cash_report = CharTools.javaScriptEscape(mode5_report.get("cash_report"));
String cash_detail = CharTools.javaScriptEscape(mode5_report.get("cash_detail"));
String cost_report = CharTools.javaScriptEscape(mode5_report.get("cost_report"));
String cost_detail = CharTools.javaScriptEscape(mode5_report.get("cost_detail"));
//add by renxianliang 2013-7-5 for xinhuamai
String cost_detail_xinhuamai = CharTools.javaScriptEscape(mode5_report.get("cost_detail_xinhuamai"));
String visit_stat_report21 = CharTools.javaScriptEscape(mode5_report.get("visit_stat_report21"));
String cusvisit_stat_report21 = CharTools.javaScriptEscape(mode5_report.get("cusvisit_stat_report21"));
String companyAttend_stat_report21 = CharTools.javaScriptEscape(mode5_report.get("companyAttend_stat_report21"));
String travelcost_stat_report21 = CharTools.javaScriptEscape(mode5_report.get("travelcost_stat_report21"));
String employeeAttend_stat_report21 = CharTools.javaScriptEscape(mode5_report.get("employeeAttend_stat_report21"));
String last_gps_info = CharTools.javaScriptEscape(mode5_report.get("last_gps_info"));

//add by 2012-12-11 zss 销量上报  促销上报
String sales_detail = CharTools.javaScriptEscape(mode5_report.get("sales_detail"));
String promotion_detail = CharTools.javaScriptEscape(mode5_report.get("promotion_detail"));
//手机模块监控报表
String mobileMonitoringRepo = CharTools.javaScriptEscape(mode5_report.get("mobileMonitoringRepo"));

String targetTemplateType = user.getTargetTemplateType() == null ? "2" : user.getTargetTemplateType();
int targetTemplateType_ = targetTemplateType.equals("") ? 2
		: Integer.valueOf(targetTemplateType);
Date targetTemplateType_D = new Date();
int targetTemplateType_Num = 0;
if (targetTemplateType_ == 0) {
	targetTemplateType = "周";
	targetTemplateType_Num = DateUtils.getWeekNumInThisYear(targetTemplateType_D);
} else if (targetTemplateType_ == 1) {
	targetTemplateType = "旬";
	targetTemplateType_Num = DateUtils.getXunNumInThisYear(targetTemplateType_D);
} else {
	targetTemplateType = "月";
	targetTemplateType_Num = DateUtils.getMonthNumInThisYear(targetTemplateType_D);
}
int targetTemplateType_Year = DateUtils.getCurrentYear(targetTemplateType_D);

Properties properties = (Properties) session.getAttribute("message");
String header = properties.getProperty("cn.net.sosgps.info.cash.header");
List<String> headerList = Arrays.asList(header.split(","));
String cashAmount0c = headerList.contains("0") == true ? "false" : "true";
String cashAmountc = headerList.contains("1") == true ? "false" : "true";
String cashAmount2c = headerList.contains("2") == true ? "false" : "true";
String cashAmount3c = headerList.contains("3") == true ? "false" : "true";
String cashAmount4c = headerList.contains("4") == true ? "false" : "true";
String cashAmount5c = headerList.contains("5") == true ? "false" : "true";
String cashAmount6c = headerList.contains("6") == true ? "false" : "true";
String cashAmount7c = headerList.contains("7") == true ? "false" : "true";
String cashAmount8c = headerList.contains("8") == true ? "false" : "true";
String cashAmount9c = headerList.contains("9") == true ? "false" : "true";
String cashAmount10c = headerList.contains("10") == true ? "false" : "true";
String cashAmount11c = headerList.contains("11") == true ? "false" : "true";
String cashAmount0Header = properties.getProperty("cn.net.sosgps.info.cash.cashAmount0");
String cashAmountHeader = properties.getProperty("cn.net.sosgps.info.cash.cashAmount");
String cashAmount2Header = properties.getProperty("cn.net.sosgps.info.cash.cashAmount2");
String cashAmount3Header = properties.getProperty("cn.net.sosgps.info.cash.cashAmount3");
String cashAmount4Header = properties.getProperty("cn.net.sosgps.info.cash.cashAmount4");
String cashAmount5Header = properties.getProperty("cn.net.sosgps.info.cash.cashAmount5");
String cashAmount6Header = properties.getProperty("cn.net.sosgps.info.cash.cashAmount6");
String cashAmount7Header = properties.getProperty("cn.net.sosgps.info.cash.cashAmount7");
String cashAmount8Header = properties.getProperty("cn.net.sosgps.info.cash.cashAmount8");
String cashAmount9Header = properties.getProperty("cn.net.sosgps.info.cash.cashAmount9");
String cashAmount10Header = properties.getProperty("cn.net.sosgps.info.cash.cashAmount10");
String cashAmount11Header = properties.getProperty("cn.net.sosgps.info.cash.cashAmount11");

// add by renxianliang for wenan 2013-1-21
String costHeader = properties.getProperty("cn.net.sosgps.info.cost.header");
List<String> costHeaderList = Arrays.asList(costHeader.split(","));
String costRemarks = costHeaderList.contains("0") == true ? "false" : "true";
String costDateTime = costHeaderList.contains("1") == true ? "false" : "true";
String coshRemarksHeader = properties.getProperty("cn.net.sosgps.info.cost.costAmount0");
String costDateTiemHeader = properties.getProperty("cn.net.sosgps.info.cost.costAmount1");
String salesmanVisitStatistics = properties.getProperty("cn.net.sosgps.report.salesman_visit_statistics");
// add by renxianliang for shinakang 2013-7-25
String addendaceReportGridHeader = properties.getProperty("com.sosgps.wzt.stat.visitStatInputTime.header");
List<String> addendaceReportGridHeaderList = Arrays.asList(addendaceReportGridHeader.split(","));
String addendaceReportInputTime = addendaceReportGridHeaderList.contains("0") == true ? "false" : "true";
//构建日期与周期几映射
String dateToWeekData = DateToWeekUtil.buildDateToWeekData();

%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK" />
    <title></title> 
	<!-- 设置区域 -->
	<link rel="stylesheet" type="text/css" href="<%=path%>/areaalarm/areaalarm.css" />
    <link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path%>/track/track.css" />
	<link rel="stylesheet" type="text/css" href="<%=path%>/main/index.css" />
	<link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/examples/simple-widgets/progress-bar.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/buttons.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path %>/ext-3.1.1/examples/ux/css/LockingGridView.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path %>/js/ui/GroupHeaderPlugin.css"/>
	<style type="text/css">
　　< !--
　　　.tnt {Writing-mode:tb-rl;Text-align:left;font-size:9pt}
　　-- >
	</style>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
    <script type="text/javascript" src="<%=path %>/ext-3.1.1/examples/ux/LockingGridView.js"></script>
    <!--Grid多列头组件 -->
    <script type="text/javascript" src="<%=path %>/js/ui/GroupHeaderPlugin.js"></script>
    <script type="text/javascript" src="<%=path%>/jstool/provincescities.js"></script>
    <script type="text/javascript" src="<%=path%>/internation/<%=edition%>/lang.js"></script>
    <script type="text/javascript" src="<%=path%>/internation/<%=edition%>/ext-lang-zh_CN.js"></script>
    <!-- 根据日期获取星期几 -->
    <script type="text/javascript" src="<%=path %>/js/dateToWeekCal.js"></script>
    <script type="text/javascript" src="<%=path %>/js/comboxInitData.js"></script>
    
    <script type="text/javascript">
   
  
   
    var scrollheight=document.documentElement.scrollHeight;
    var scrollwidth=document.documentElement.scrollWidth; 
    var entCode = '<%=entCode%>';
    var userId = '<%=userId%>';
    
    var maxUserNum='<%=maxUserNum%>';
    var empCode64 = encodeURI('<%=empCode64%>');
    var password64 = encodeURI('<%=password64%>');
    var account64 = encodeURI('<%=account64%>');
    
    var mmsAccount = '<%=mmsAccount%>';
    var mmsPwd = '<%=mmsPwd%>';
    //报表url
    var excelpath = 'http://220.181.105.84';
    //var excelpath = 'http://localhost:9080/mapsearch';
    //彩信平台url
    var mmspath = 'http://220.181.105.88/ema/login.do';
    
	var endTime='<%=endTime%>';
    var path = '<%=path%>';
    Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
	var entname = '<%=entname%>';
	var edition = '<%=edition%>';
    var area_alarm_report = '<%=area_alarm_report%>';
	var licheng_report = '<%=licheng_report%>';
	var speed_alarm_report = '<%=speed_alarm_report%>';
	var active_alarm_report = '<%=active_alarm_report%>';
	var visit_stat_report = '<%=visit_stat_report%>';
	var cusvisit_stat_report = '<%=cusvisit_stat_report%>';
	var attendance_report = '<%=attendance_report%>';
	var vehicle_loc_report = '<%=vehicle_loc_report%>';
	var vehicle_msg_stat_report = '<%=vehicle_msg_stat_report%>';
	var diary_report = '<%=diary_report%>';
	var totalDistanceStat_report = '<%=totalDistanceStat_report%>';
	var door_report = '<%=door_report%>';
	var timeDistance_report = '<%=timeDistance_report%>';
	var kqtj = '<%=kqtj%>';
	var structions_report = '<%=structions_report%>';
	//2012-8-21 weimeng v2.1
	var visit_report = '<%=visit_report%>';
	var visit_rank_report = '<%=visit_rank_report%>';
	var signBill_report = '<%=signBill_report%>';
	var signBill_detail = '<%=signBill_detail%>';
	var cash_report = '<%=cash_report%>';
	var cash_detail = '<%=cash_detail%>';
	var cost_report = '<%=cost_report%>';
	var cost_detail = '<%=cost_detail%>';
	//add by renxianliang  shinakang 2013-7-5 for xinhuamai
	var cost_detail_xinhuamai = '<%=cost_detail_xinhuamai%>';
	var targetTemplateType = '<%=targetTemplateType%>';
	var targetTemplateType_Num = Number('<%=targetTemplateType_Num%>');
	var targetTemplateType_Year = Number('<%=targetTemplateType_Year%>');
	var visit_stat_report21 = '<%=visit_stat_report21%>';
	var cusvisit_stat_report21 = '<%=cusvisit_stat_report21%>';
	var companyAttend_stat_report21 = '<%=companyAttend_stat_report21%>';
	var travelcost_stat_report21 = '<%=travelcost_stat_report21%>';
	var employeeAttend_stat_report21 = '<%=employeeAttend_stat_report21%>';
	var last_gps_info = '<%=last_gps_info%>';
	var mobileMonitoringRepo = '<%=mobileMonitoringRepo%>';
	  //add by 2012-12-11 zss   销量上报  促销上报
    var sales_detail='<%=sales_detail%>';
    var promotion_detail='<%=promotion_detail%>';
	var labelWidth = 450 / 1366 * screen.width;
	//label
	var label = new Ext.form.Label({text: '', width: labelWidth, style: 'text-align: right; display: inline-block;'});
	
	//获取结果
	var dateToWeekData = '<%=dateToWeekData%>';

	var cashAmount0c = <%=cashAmount0c%>;
	var cashAmountc = <%=cashAmountc%>;
	var cashAmount2c = <%=cashAmount2c%>;
	var cashAmount3c = <%=cashAmount3c%>;
	var cashAmount4c = <%=cashAmount4c%>;
	var cashAmount5c = <%=cashAmount5c%>;
	var cashAmount6c = <%=cashAmount6c%>;
	var cashAmount7c = <%=cashAmount7c%>;
	var cashAmount8c = <%=cashAmount8c%>;
	var cashAmount9c = <%=cashAmount9c%>;
	var cashAmount10c = <%=cashAmount10c%>;
	var cashAmount10c = <%=cashAmount10c%>;
	var cashAmount11c = <%=cashAmount11c%>;
	var cashAmount0Header = '<%=cashAmount0Header%>';
	var cashAmountHeader = '<%=cashAmountHeader%>';
	var cashAmount2Header = '<%=cashAmount2Header%>';
	var cashAmount3Header = '<%=cashAmount3Header%>';
	var cashAmount4Header = '<%=cashAmount4Header%>';
	var cashAmount5Header = '<%=cashAmount5Header%>';
	var cashAmount6Header = '<%=cashAmount6Header%>';
	var cashAmount7Header = '<%=cashAmount7Header%>';
	var cashAmount8Header = '<%=cashAmount8Header%>';
	var cashAmount9Header = '<%=cashAmount9Header%>';
	var cashAmount10Header = '<%=cashAmount10Header%>';
	var cashAmount11Header = '<%=cashAmount11Header%>';
	 //add by renxianliang for wenan
	var costRemarks = <%=costRemarks%>;
	var costDateTime = <%=costDateTime%>;
	var coshRemarksHeader = '<%=coshRemarksHeader%>';
	var costDateTiemHeader = '<%=costDateTiemHeader%>';
	if('<%=salesmanVisitStatistics%>' != 'null'){
		report.salesman_visit_statistics = '<%=salesmanVisitStatistics%>';
	}
	//add by renxianliang for 
	var addendaceReportInputTime = <%=addendaceReportInputTime%>;
	var sms_center = '<%=sms_center%>';
	var diary_center = '<%=diary_center%>';
	var diary_add = '<%=diary_add%>';
	var diary_remark = '<%=diary_remark%>';
	var info_acquisition_url = '<%=infoAcquisitionUrl%>';
	var module_type = '<%=moduleType%>';
    var loader = new Ext.tree.TreeLoader({
		dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForEntTerminal'	
	});
	var root = new Ext.tree.AsyncTreeNode({
			text : main.location_platform,
			id : '-100',
			draggable : false // 根节点不容许拖动
	});
    var api = new Ext.tree.TreePanel({
        rootVisible:false,
        lines:true,
        autoScroll:true,
        width:230,
        loader: loader,
        root:root,
        tbar:[ ' ',
   			new Ext.form.TextField({
   				id: 'queryTreeTextId',
   				width: 200,
   				emptyText:'请输入查询条件',
                enableKeyEvents: true,
   				listeners:{
   					keypress: {
                    	fn: function( textField, e ){
    		        		if (e.getKey() == e.ENTER) {
	                        	var searchValue = Ext.getCmp('queryTreeTextId').getValue();
	                        	if(searchValue == '' && searchValue.length <=0 ){
	                        		Ext.Msg.alert('提示','请输入查询条件');
	                        		return;
	                        	}
	                        	loader.dataUrl = path+'/terminal/terminal.do?method=queryTerminalTree&searchValue='+encodeURI(encodeURI(searchValue));
	                        	root.reload(function(){
		                        	api.expandPath('/-100/-101/');
	                        		Ext.Msg.hide();
	                        	});
	                        	loader.dataUrl = path+'/manage/termGroupManage.do?actionMethod=groupListForEntTerminal';
	                        	Ext.Msg.show({
	         			           msg: '正在查询 请稍等...',
	         			           progressText: '查询中...',
	         			           width:300,
	         			           wait:true,
	         			           //waitConfig: {interval:200},
	         			           icon:'ext-mb-download'
	         			       });
	    		        	}
                    	}
                    }
   				}
   			}),{
                iconCls: 'icon-search',
                handler: function(){
                	var searchValue = Ext.getCmp('queryTreeTextId').getValue();
                	if(searchValue == '' && searchValue.length <=0 ){
                		Ext.Msg.alert('提示','请输入查询条件');
                		return;
                	}
                	loader.dataUrl = path+'/terminal/terminal.do?method=queryTerminalTree&searchValue='+encodeURI(encodeURI(searchValue));
                	root.reload(function(){
                    	api.expandPath('/-100/-101/');
                		Ext.Msg.hide();
                	});
                	loader.dataUrl = path+'/manage/termGroupManage.do?actionMethod=groupListForEntTerminal';
                	Ext.Msg.show({
 			           msg: '正在查询 请稍等...',
 			           progressText: '查询中...',
 			           width:300,
 			           wait:true,
 			           //waitConfig: {interval:200},
 			           icon:'ext-mb-download'
 			       });
                }
            }]
	});
    function apiRightClick (node , e){
	    var tmpidArr = node.id.split('@#');
		if(tmpidArr.length<=1){
			return;
		}
    	var treeMenu = new Ext.menu.Menu({
	        items : [
	        	{text: main.view_distribution_poi, icon: "", handler: function(){
	        		map.queryPoiByDeviceId(node);
	        	}},
	            {text: main.view_distribution_area, icon: "", handler: function(){
	            	map.queryAreaByDeviceId(node);
	            }}
	        ]
	     });
		treeMenu.showAt(e.getPoint());
	}
    var center = new Ext.Panel({
		id:'api-tree',
        region:'west',
        //split:true,
        //width: 200,
        //minSize: 200,
        //maxSize: 200,
        width: 260,
        minSize: 260,
        maxSize: 260,
        collapsible: true,
        margins:'5 5 5 5',
        cmargins:'0 0 0 0',
        //lines:false,
        autoScroll:true,
        hideCollapseTool:true,
        //collapseMode:'mini',
        //collapseFirst:false,
        layout: 'anchor',
        baseCls:'x-plain',
        items:[
        	new Ext.Panel({
        		layout:'accordion',
        		anchor: '100% 100%',
        		id:'loc',
        		items:[{
        			id: 'location_services',
		        	layout:'hbox',
		        	title : main.location_services,
					layoutConfig: {
					    align : 'stretch',
					    pack  : 'start'
					},
					items: [api,{el:'function'}],
					tools:[
							{id:"refresh",
							 qtip: '刷新终端信息',   
							 handler: function (event, toolEl, panel) {
								 loader.dataUrl = path+'/manage/termGroupManage.do?actionMethod=groupListForEntTerminal';
								 root.reload(function(){
				                    	api.expandPath('/-100/-101/');
				                 });
							 }
							}
					       ]
		        },new Ext.Panel({
					title: main.map_search,
					html:'<iframe id="ifrcarsearch" name="ifrcarsearch" style="width: 100%; height: 100%" SCROLLING="auto" frameborder="0" src="'+path+'/carsearch/carsearch.jsp"></iframe>'
				})]
        	}),{
    		anchor: '100%, 100%',
    		id:'action-panel',
    		baseCls:'x-plain',
    		hidden:true
        }]
});
function poi_img_click(){
    AreaAlarmWindow.hide();
    map.addEventListener(map.MOUSE_CLICK,MclickMouse_clickImg);
}
function MM_swapImgRestore(obj,img){
    obj.src = path+"/internation/"+edition+"/map/"+img;
}
var actions = new Object();
var terminalType='0';//终端类型
var stattime='';//开始时间
var endtime='';//结束时间
var temp='';//判断是否重新加载的临时变量
<%
ArrayList rootJsList = (ArrayList)request.getSession().getAttribute("module_panelal");
Iterator i = rootJsList.iterator();
while(i.hasNext()){
	String rootPanel = (String)i.next();
%>
var tmpactionpanel = Ext.getCmp('action-panel');
var tmpPanel = eval("<%=rootPanel%>");
tmpactionpanel.add(tmpPanel);
<%
}
ArrayList nodeJsList = (ArrayList)request.getSession().getAttribute("module_actionsal");
Iterator inode = nodeJsList.iterator();
while(inode.hasNext()){
	String nodeActions = (String)inode.next();
%>
	eval("<%=nodeActions%>");
<%
}
%>

	
	var button1=new Ext.Button({
		id: 'button1',
	    tooltip: main.location_platform,
	    text: main.location_platform,
	    handler: function(){
	    	northMainBtnFlag = false;
	    	//设置north panel的高
	    	southPanel.setHeight(1);
			var tmpviewport = Ext.getCmp('viewport');
			tmpviewport.doLayout(true, false);
			
	    	//显示 区域报警 POI
	    	var area_poi_div = document.getElementById('area_poi_div');
	    	area_poi_div.style.display = '';
	    	
			var tmpeast = Ext.getCmp('east-panel');
			tmpeast.hide();
			tmpeast.collapse(true);
			
			var tmploc = Ext.getCmp('loc');
	    	var tmpsystem = Ext.getCmp('action-panel');
			tmploc.show();
			tmpsystem.hide();
			resize();
	    }
	});
	var button2=new Ext.Button({
		id: 'button2',
	    tooltip: main.management_center,
	    text: main.management_center,
	    handler:function(){
	    	northMainBtnFlag = false;
	    	if(sellreportwin){
				sellreportwin.hide();
	    	}
	    	if(reportwin){
				reportwin.hide();
	    	}
	    	//设置north panel的高
	    	southPanel.setHeight(1);
			var tmpviewport = Ext.getCmp('viewport');
			tmpviewport.doLayout(true, false);
			
	    	//隐藏 区域报警 POI
	    	var area_poi_div = document.getElementById('area_poi_div');
	    	area_poi_div.style.display = 'none';
	    	
			var tmpeast = Ext.getCmp('east-panel');
			tmpeast.expand(true);
	    	tmpeast.show();
	    	
	    	var tmploc = Ext.getCmp('loc');
	    	var tmpsystem = Ext.getCmp('action-panel');
			tmploc.hide();
			tmpsystem.show();
			resize();
	    }
	});
	var button3=new Ext.Button({
		id: 'button3',
	    tooltip: main.change_password,
	    text: main.change_password,
	    handler: function(){
	    	modifyPassWord();
	    }
	});
	//退出系统
	function quitSystem(btn){
		if(btn=='yes'){
			document.quitSystemform.action = 'logout.do';
			document.quitSystemform.submit();
		}
	}
	
	
	var button4=new Ext.Button({
		id: 'button4',
	    tooltip: main.exit_the_system,
	    text: main.exit_the_system,
	    handler:function(){
			Ext.MessageBox.confirm(main.tips, main.are_you_sure_you_want_to_exit_the_system, quitSystem);
	    }
	});
	var topArray = ["&nbsp",main.user,"&nbsp",entname,"&nbsp",main.welcome,"&nbsp",endTime,"&nbsp","&nbsp",
	                "&nbsp","&nbsp","&nbsp","&nbsp","&nbsp","&nbsp","&nbsp","&nbsp","&nbsp","&nbsp","&nbsp",
	                "&nbsp","&nbsp","&nbsp","&nbsp","&nbsp","&nbsp", "-"];

	//定位平台
	topArray.push(button1);
	topArray.push("-");
	
//首页报表按钮标示
var northMainBtnFlag = false;
var northMainFlag = false;
<%
Map<String, String> flatMap = (Map)request.getSession().getAttribute("flat_map");
//首页报表按钮
if(flatMap.containsKey("reportv2.1")){
%>
northMainFlag = true;
//首页按钮
var northMainBtn = new Ext.Button({
	id: 'northMainBtn',
    tooltip: '销售报表',
    text: '销售报表',
    handler: function(){
    	if(sellreportwin){
			sellreportwin.hide();
    	}
    	if(reportwin){
			reportwin.hide();
    	}
    	northMainBtnFlag = true;
    	//隐藏 区域报警 POI
    	var area_poi_div = document.getElementById('area_poi_div');
    	area_poi_div.style.display = 'none';
    	//设置north panel的高
        var scrollheight=document.documentElement.scrollHeight;
    	southPanel.setHeight(scrollheight - 85);
		var tmpviewport = Ext.getCmp('viewport');
		tmpviewport.doLayout(true, true);
		
    }
});
topArray.push(northMainBtn);
topArray.push("-");
//2.1首页报表页面
var southPanel = new Ext.Panel({
	id:'south-panel',
    region:'south',
    //margins:'5 5 5 5',
    html:'<div id="southPanelDiv"><iframe id="southPanelIframe" name="southPanelIframe" src="'+path+'/mainv2.1/index.jsp" style="width:100%; height: 100%;" SCROLLING="yes" frameborder="0"></iframe></div>',
    height: scrollheight - 85
});
<%
}else{
%>
//2.1首页报表页面
var southPanel = new Ext.Panel({
	id:'south-panel',
    region:'south',
    //margins:'5 5 5 5',
    //html:'<div id="southPanelDiv"><iframe id="southPanelIframe" name="southPanelIframe" src="'+path+'/mainv2.1/index.jsp" style="width:100%; height: 100%;" SCROLLING="yes" frameborder="0"></iframe></div>',
    height: 1
});
<%
}
%>
	<%
	//信息采集按钮
	if(flatMap.containsKey("xxcj")){
	%>
		var infoAcquisition = new Ext.Button({
			id: 'infoAcquisition',
		    tooltip: main.info_acquisition,
		    text: main.info_acquisition,
		    handler: function(){
		    	document.location.href = info_acquisition_url;
		    }
		});
		topArray.push(infoAcquisition);
		topArray.push("-");
	<%
	}
	%>
	topArray.push(button2);
	topArray.push("-");
	topArray.push(button3);
	topArray.push("-");
	topArray.push(button4);
	topArray.push("-");
	/*topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");*/
	/*if(navigator.userAgent.indexOf("MSIE") > 0)
	{
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	}
	else if(navigator.userAgent.indexOf("Firefox") > 0)
	{
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
		topArray.push("&nbsp;&nbsp;&nbsp;&nbsp;");
	}
	else
	{
		//其它浏览器
	}*/
	topArray.push(label);
	//topArray.push("服务热线电话：4006007987 | 010-58103698");
	</script>
	<!-- 设置区域 -->
	<script src="<%=path%>/areaalarm/areaalarm.js" type="text/javascript"></script>
	<script src="<%=path%>/areaalarm/areaalarmfun.js" type="text/javascript"></script>
	<!-- 设置标注点 -->
	<script src="<%=path%>/poi/poi.js" type="text/javascript"></script>
	<script src="<%=path%>/poi/poifun.js" type="text/javascript"></script>
	<!-- 轨迹回放 -->
	<script type="text/javascript" src="<%=path%>/track/TrackParam.js"></script>
	<script type="text/javascript" src="<%=path%>/track/TrackFunction.js"></script>
	<script type="text/javascript" src="<%=path%>/track/UITrackSearchPanel.js"></script>
	<script type="text/javascript" src="<%=path%>/track/UITrackControlPanel.js"></script>
	<script type="text/javascript" src="<%=path%>/track/UITrackWindow.js"></script>
	<script type="text/javascript" src="<%=path%>/main/alarmcenter.js"></script>
	<script type="text/javascript" src="<%=path%>/main/controlcenter.js"></script>
	<script type="text/javascript" src="<%=path%>/main/setMapCenterWin.js"></script>
	<script type="text/javascript" src="<%=path%>/js/DateTool.js"></script>
    <script type="text/javascript">
    	var tmpdate = (new Date()).pattern("yyyy-M-d");
    	var yesterday = new Date();
    	yesterday.setDate(yesterday.getDate()-1);
		var tmpdateyesterday = (yesterday).pattern("yyyy-M-d");
		Ext.chart.Chart.CHART_URL = '<%=path%>/ext-3.1.1/resources/charts.swf';
		Ext.form.Field.prototype.msgTarget = 'under';
		function storeLoad(store, start, limit, deviceIds, searchValue, startTime, endTime, duration, expExcel, deviceId, poiId, week, workStartTime, workEndTime){
			store.load({params:{start: start, limit: limit, deviceIds: deviceIds ,searchValue: encodeURI(searchValue) ,startTime: startTime, endTime: endTime, duration: duration, expExcel: expExcel, deviceId: deviceId, poiId: poiId, week: week, workStartTime: workStartTime, workEndTime: workEndTime}});
		}
    </script>
    <script type="text/javascript" src="<%=path%>/main/visitStatGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/cusVisitStatGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/attendanceReportGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/areaAlarmGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/speedAlarmGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/holdAlarmGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/distanceStatGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/totalDistanceStatGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/vehicleGPSInfoGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/doorReportGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/vehicleMsgStatGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/diaryReportGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/diaryEditGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/diaryRemarkGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/main/showBtnClick.js"></script>
	<script type="text/javascript" src="<%=path%>/main/trackImgClick.js"></script>
	<script type="text/javascript" src="<%=path%>/main/showBtnClick_qd.js"></script>
	<script type="text/javascript" src="<%=path%>/main/alarmImgClick.js"></script>
	<script type="text/javascript" src="<%=path%>/main/controlBtnClick.js"></script>
	<script type="text/javascript" src="<%=path%>/main/smsBtnClick.js"></script>
	<script type="text/javascript" src="<%=path%>/main/reportBtnClick.js"></script>
	<script type="text/javascript" src="<%=path%>/main/terminalBtnClick.js"></script>
	<script type="text/javascript" src="<%=path%>/main/realtimeTrackImgClick.js"></script>
	<script type="text/javascript" src="<%=path%>/main/carCurrentDirectionImgClick.js"></script>
	<script type="text/javascript" src="<%=path%>/main/index.js"></script>
	<script type="text/javascript" src="<%=path%>/main/layercontrol.js"></script>
	<script type="text/javascript" src="<%=path%>/main/modifyPassWord.js"></script>
    <script type="text/javascript" src="<%=path%>/main/timeDistanceStatGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/attendanceRecordReportGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/structionsGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/main/lastGPSInfoGrid.js"></script>
    <!-- v2.1 -->
    <script type="text/javascript" src="<%=path%>/sellReportv2.1/visitRankGrid.js"></script>
    <script type="text/javascript" src="<%=path%>/sellReportv2.1/visitReportGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/SignBillDetail.js"></script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/CashDetail.js"></script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/CostDetail.js"></script>
		<script type="text/javascript" src="<%=path%>/sellReportv2.1/CostDetail_xinhuamai.js"></script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/SignBillReport.js"></script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/CashReport.js"></script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/CostReport.js"></script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/sellReportBtnClick.js"></script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/cusVisitReportGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/visitStatReportGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/travelCostTotalReportGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/employeeCheckReportGrid.js"></script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/companyCheckTotalReportGrid.js"></script>
	<!-- add by wangzhen -->
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/mobileMonitoringRepoGrid.js"></script>
	<!-- add 2012-12-11 销量上报、促销上报 -->
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/CashDetailEnquiries.js"></script>
	<script type="text/javascript" src="<%=path%>/sellReportv2.1/PromotionDetail.js"></script>
</head>
<body scroll="no" onResize="resize()">
	<div nowrap class="area_poi_icons" id="area_poi_div" style="display:">
	 <% 
		String poi = (String)mode3_map.get("poi");
		if(poi != null && poi.length() > 0){
			out.println("<img id=\"poi_img\" onclick=\"poi_img_click()\" onMouseOut=\"MM_swapImgRestore(this,'u11.gif')\" onMouseOver=\"MM_swapImgRestore(this,'b11.gif')\" title=\"新增标注\" src=\""+path+"/internation/"+edition+"/map/u11.gif\"  style=\"cursor:hand\" />");
		}
	%>
	</div>
  <div id="loading-mask" style=""></div>
  <div id="loading">
     <div class="loading-indicator"><img src="<%=path%>/images/blue-loading.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>Loading...</div>
  </div>
  <div id="header">
  <% if(cooperation!=null&&cooperation.length()>0){}else{%>
  <img src="" style="position:absolute;top:10px;right:100px;"/>
  <img src="" style="position:absolute;top:0px;right:10px;"/>
  <%} %>
  </div>
  <div id="hello-win_qd"></div>
  <div id="hello-win"></div>
  <div id="tracklocate-div"></div>
  <div id="control-win"><div id="control-win1"></div></div>
  <div id="queryAreaByDeviceId-win"></div>
  <div id="realtime-track-win"></div>
  <div id="dr"></div>
<!-- 六大功能 -->
<div id="function" >
<%
if(mode4_loc_len > 0){
%>
  <table width="2%" border="0">
  	<% 
		String loc = (String)mode4_loc.get("loc");
		if(loc != null && loc.length() > 0){
			out.println("<tr>");
    		out.println("<td align=\"center\" bgcolor=\"#F67609\"><div class=\"tnt\"><font color=\"#FFFFFF\"></font></div> <img src=\""+path+"/internation/"+edition+"/images/1.png\" style=\"cursor:pointer\" id=\"show-btn\" onclick=\"show_btn_click()\"/></td>");
  			out.println("</tr>");
		}
	%>
	<% 
		String track = (String)mode4_loc.get("track");
		if(track != null && track.length() > 0){
			out.println("<tr>");
    		out.println("<td align=\"center\"><img src=\""+path+"/internation/"+edition+"/images/2.png\" style=\"cursor:pointer\" id=\"track-img\" onclick=\"track_img_click()\"/></td>");
  			out.println("</tr>");
		}
	%>
  	<% 
		String loc_qd = (String)mode4_loc.get("loc_qd");
		if(loc_qd != null && loc_qd.length() > 0){
			out.println("<tr>");
    		out.println("<td align=\"center\" bgcolor=\"#F67609\"><div class=\"tnt\"><font color=\"#FFFFFF\"></font></div> <img src=\""+path+"/internation/"+edition+"/images/9.png\" style=\"cursor:pointer\" id=\"show-btn_qd\" onclick=\"show_btn_click_qd()\"/></td>");
  			out.println("</tr>");
		}
	%>
	<% 
		String track_qd = (String)mode4_loc.get("track_qd");
		if(track_qd != null && track_qd.length() > 0){
			out.println("<tr>");
    		out.println("<td align=\"center\"><img src=\""+path+"/internation/"+edition+"/images/10.png\" style=\"cursor:pointer\" id=\"track-img_qd\" onclick=\"track_img_click('qd')\"/></td>");
  			out.println("</tr>");
		}
	%>
	<% 
		String alarm_center = (String)mode4_loc.get("alarm_center");
		if(alarm_center != null && alarm_center.length() > 0){
			out.println("<tr>");
    		out.println("<td align=\"center\"> <img id=\"alarmimg\" src=\""+path+"/internation/"+edition+"/images/3.png\" style=\"cursor:pointer\" onclick=\"alarm()\" /></td>");
  			out.println("</tr>");
		}
	%>
	<% 
		String control_center = (String)mode4_loc.get("control_center");
		if(control_center != null && control_center.length() > 0){
			out.println("<tr>");
    		out.println("<td align=\"center\"> <img src=\""+path+"/internation/"+edition+"/images/4.png\" style=\"cursor:pointer\"  id=\"control-btn\" onclick=\"control_btn_click()\" /></td>");
  			out.println("</tr>");
		}
	%>
	<% 
		String msg_center = (String)mode4_loc.get("msg_center");
		if(msg_center != null && msg_center.length() > 0){
			out.println("<tr>");
    		out.println("<td align=\"center\"> <img src=\""+path+"/internation/"+edition+"/images/5.png\" style=\"cursor:pointer\" id=\"sms-btn\" onclick=\"sms_btn_click()\" /></td>");
  			out.println("</tr>");
		}
	%>
	<% 
		String report_center = (String)mode4_loc.get("report_center");
		if(report_center != null && report_center.length() > 0){
			out.println("<tr>");
    		out.println("<td align=\"center\"> <img src=\""+path+"/internation/"+edition+"/images/6.png\" style=\"cursor:pointer\"  id=\"report-btn\" onclick=\"report_btn_click(true)\"/></td>");
  			out.println("</tr>");
		}
	%>
		<% 
			String sell_report = (String)mode4_loc.get("sell_report");
			if(sell_report != null && sell_report.length() > 0){
				out.println("<tr>");
	    		out.println("<td align=\"center\"> <img src=\""+path+"/internation/"+edition+"/images/11.png\" style=\"cursor:pointer\"  id=\"sell_report-btn\" onclick=\"sell_report_btn_click(true)\"/></td>");
	  			out.println("</tr>");
			}
		%>
		<% 
		String terminal_collect = (String)mode4_loc.get("terminal_collect");
		if(terminal_collect != null && terminal_collect.length() > 0){
			out.println("<tr>");
    		out.println("<td align=\"center\"> <img src=\""+path+"/internation/"+edition+"/images/7.png\" style=\"cursor:pointer\"  id=\"terminal-btn\" onclick=\"terminal_btn_click()\" /></td>");
  			out.println("</tr>");
		}
	%>
	<% 
		String statistics = (String)mode4_loc.get("statistics");
		if(statistics != null && statistics.length() > 0){
			out.println("<tr>");
    		out.println("<td align=\"center\"> <img src=\""+path+"/internation/"+edition+"/images/8.png\" style=\"cursor:pointer\"  id=\"statistics_btn\" onclick=\"statistics_btn_click()\" /></td>");
  			out.println("</tr>");
		}
	%>
	<% 
		String mms_center = (String)mode4_loc.get("mms_center");
		if(mms_center != null && mms_center.length() > 0){
			out.println("<tr>");
    		out.println("<td align=\"center\"> <img src=\""+path+"/internation/"+edition+"/images/12.png\" style=\"cursor:pointer\"  id=\"mms_center_btn\" onclick=\"mms_btn_click()\" /></td>");
  			out.println("</tr>");
		}
	%>
</table>
<%}%>
</div>
<%=mainpage%>
<form name="quitSystemform"  method="post" action="" target="_self"></form>
<form name="excelform" id="excelform" method="post" action="" target="_self"></form>

</body>
</html>
