<%@ page contentType="text/html;charset=GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html >  
	<head>  
	<meta http-equiv="Content-Type" content="text/html; charset=gbk" />  
	<title>位置通</title> 
	<link rel="stylesheet" type="text/css" href="./css/ext_css/css/ext-all.css" />
   
 	<!-- LIBS -->
 	<script type="text/javascript" src="./js/ext_js/adapter/ext/ext-base.js"></script>
 	<!-- ENDLIBS -->

    <script type="text/javascript" src="./js/ext_js/ext-all.js"></script>
<script>          
var myTabPanel = Ext.extend(Ext.TabPanel, {   
 
 setActiveTab:function(item) {
  item = this.getComponent(item);
        if(!item || this.fireEvent('beforetabchange', this, item, this.activeTab) === false){
            return;
        }
        if(!this.rendered){
            this.activeTab = item;
            return;
        }
        if(this.activeTab != item){
            if(this.activeTab){
                var oldEl = this.getTabEl(this.activeTab);
                if(oldEl){
                    Ext.fly(oldEl).removeClass('x-tab-strip-active');
                }
                this.activeTab.fireEvent('deactivate', this.activeTab);
				
            }
            var el = this.getTabEl(item);
            Ext.fly(el).addClass('x-tab-strip-active');
            this.activeTab = item;
            this.stack.add(item);

            this.layout.setActiveItem(item);
            if(this.layoutOnTabChange && item.doLayout){
                item.doLayout();
            }
            if(this.scrolling){
                this.scrollToTab(item, this.animScroll);
            }

            item.fireEvent('activate', item);
            this.fireEvent('tabchange', this, item);
			frames['ifrmap'].location.reload();
        }
 }
}); 



//页面下部
var southPanel = new Ext.TabPanel({
					region:'south',
					height:80,
					split:true,
					deferredRender:false,
					collapsible:true,
					collapsed:true,
					activeTab:0,
						items:[{ 
							contentEl:'south_result1', 
							title: '&nbsp;&nbsp;查询结果&nbsp;&nbsp;', 
							autoScroll:false 
							},
							{ 
								contentEl:'south_result2', 
								title: '其他',
								autoScroll:false
							}
						] 
					});
//页面中心部分
var center = new myTabPanel({
					region:'center',
					deferredRender:false,
					activeTab:0,
						items:[
							{ 
								contentEl:'center1', 
								title: '&nbsp;&nbsp;地图&nbsp;&nbsp;', 
								autoScroll:false 
							},
							{ 
								contentEl:'center2', 
								title: '系统设置',
								autoScroll:true
							}
						] 
					})   ;
//页面右侧地图服务部分
var eastPanel = new Ext.TabPanel({
					title: '地图服务',
					region:'east',
					width:200,
					split:true,
					deferredRender:false,
					collapsible:true,	
					collapsed:true,				
					//tabPosition:'bottom',
					activeTab:0,
						items:[{ 
							contentEl:'east_query', 
							title: '周边查询', 
							autoScroll:false 
							},
							{ 
								contentEl:'east_zj_query', 
								title: '行车路线',
								autoScroll:false
							}
						] 
					});
	Ext.onReady(function(){
		
		new Ext.Viewport({
			enableTabScroll:true,
			layout:"border",
			items:[
				{
					region:"north",
					height:72,
					collapsible:true,
					contentEl:'top'				
					
				},

				{
					title:"导航",
					id:'west-panel',
					region:"west",
					width:200,
					collapsible:true,
					contentEl:'westFrame'                                  
				},
					center,
					southPanel ,
					eastPanel                     
				]                                  
			});
			
	});
 
//center 中 地图/切换 0 地图页面 1 设置页面
function centerChange(str){
	center.setActiveTab(str);
}

//隐藏右侧页面
function hiddenEast(){
	eastPanel.collapse(true);
}
//展开右侧页面
function expendEast(){
	eastPanel.expand(true)
}
//底部当前tab切换 
function southChange(str){
	southPanel.setActiveTab(str);
}
//隐藏下部页面
function hiddenSouth(){
	southPanel.collapse(true);
}
//展开下部页面
function expendSouth(){
	southPanel.expand(true);
}

</script>  
	</head>  
	<body>  
		<div id="top">
			<iframe id="topFrame" name="topFrame" src="main/top.jsp" style="width: 100%; height: 100%" SCROLLING="no" frameborder="0"></iframe>
		</div>
		<div id="westFrame">
			<iframe id="leftFrame" name="leftFrame" src='<%=basePath %>/locate/locate.do?method=init' style="width: 100%; height: 100%" SCROLLING="no" frameborder="0"></iframe>
		</div>
		<div id="center2">
			<iframe id="ifrlocmsg" name="ifrlocmsg" src="info.html" style="width:100%; height: 100%;" SCROLLING="yes" ></iframe></div>
		<div id="center1">
			<iframe id="ifrmap" name="ifrmap" src="map/map.jsp" style="width: 100%; height: 100%" SCROLLING="no" frameborder="0"></iframe>
	  </div>
		<div id="east_query">
			<iframe id="queryFrame" name="queryFrame" src="maputil/nearbysearch.jsp" style="width: 100%; height: 100%" SCROLLING="no" frameborder="0"></iframe>
	  </div>
		<div id="east_zj_query">
			<iframe id="ifrm2" name="ifrm2" src="maputil/nearbysearch.jsp" style="width: 100%; height: 100%" SCROLLING="no" frameborder="0"></iframe>
	  </div>

		<div id="south_result1">
			<iframe id="ifrm3" name="ifrm3" src="showresult/info.html" style="width:100%; height: 100%;" SCROLLING="no" ></iframe></div>
		<div id="south_result2">
			<iframe id="ifrm4" name="ifrm4" src="mytest/map.html" style="width: 100%; height: 100%" SCROLLING="no" frameborder="0"></iframe>
	    </div>
	</body>  
</html> 