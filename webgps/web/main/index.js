
function deviceIdImgUrl(deviceId, imgUrl){
	this.deviceId = deviceId;
	this.imgUrl = imgUrl;
}

var grid = new Ext.Panel({
	region: 'center',
    //el:"main",
    html:'<div id="main"><iframe id="mapifr" name="mapifr" src="'+path+'/map/map.jsp" style="width:100%; height: 100%;" SCROLLING="no" frameborder="0"></iframe></div>',
    autoScroll:false
});

var map;

var mainPanel = new Ext.Panel({
	id:'doc-body',
        region:'center',
        margins:'5 5 5 0',
        layout:'border',
    	items:[
    		grid, msgpanel
    	]
});

//update by zhaofeng
var toolbar=new Ext.Toolbar({
	id:"toolbar-panel",
	items:topArray
});

var northPanel = new Ext.Panel({
	id:"north-panel",
	border:false,
	layout:"anchor",
	region:"north",
	height:85,
	items:[{
		id:"header",
		xtype:"box",
		el:"header",
		border:false,
		anchor:"none -28"
		},toolbar
	]
});

        var maintab = new Ext.Panel({
        	id: 'maintab',
            title: main.management_system,
            html: '<iframe id="ifrjs" name="ifrjs" style="width: 100%; height: 100%" SCROLLING="auto" frameborder="0" src="'+path+'/main/blank.html"></iframe>',
            autoScroll: true
        });
		
        var center1 = new Ext.TabPanel({
                    border: false, // already wrapped so don't add another border
                    activeTab: 0, // second tab initially active
                    items: [maintab]
                });
        
var eastPanel = new Ext.Panel({
                region: 'east',
                id:'east-panel',
                
        //width: scrollwidth - 215,
        width: scrollwidth - 275,
        //minSize: 175,
        //maxSize: 500,
        cmargins:'0 0 0 0',
        //lines:false,
        autoScroll:true,
        hideCollapseTool:true,
        //collapseMode:'mini',
        //collapseFirst:false,
        collapsed :true,
        animCollapse:false,
                collapsible: true,
                hidden:true,
                closable:true,
                margins: '5 5 5 0',
                layout: 'fit', // specify layout manager for items
                items:            // this TabPanel is wrapped by another Panel so the title will be applied
                center1
            });
        

var alarminterval;
//初始化
Ext.onReady(function(){

    Ext.QuickTips.init();

    var viewport = new Ext.Viewport({
        layout:'border',
        id:'viewport',
        //mainPanel:center,center:west,northPanel:north,eastPanel:east,southPanel:south
        items:[ northPanel, center, mainPanel, eastPanel, southPanel ],
	    listeners:{
	    	'resize':function (thisz, adjWidth, adjHeight, rawWidth, rawHeight){
	    		if(northMainBtnFlag){
			        var scrollheight=document.documentElement.scrollHeight;
			    	southPanel.setHeight(scrollheight - 85);
					var tmpviewport = Ext.getCmp('viewport');
					tmpviewport.doLayout(true, false);
	    		}
	    	}/*,
	    	'afterlayout':function (this_, layout_ ){
	    		northMainBtnFlag = true;
			    var scrollheight=document.documentElement.scrollHeight;
			    southPanel.setHeight(scrollheight - 85);
				var tmpviewport = Ext.getCmp('viewport');
				tmpviewport.doLayout(true, false);
	    	}*/,
	    	'show': function(this_){
	    		alert('show');
	    	},
	    	'enable': function(this_){
	    		alert('enable');
	    	},
	    	'render': function(this_){
	    		//alert('render');
	    		/*northMainBtnFlag = true;
			    var scrollheight=document.documentElement.scrollHeight;
			    southPanel.setHeight(scrollheight - 85);
				var tmpviewport = Ext.getCmp('viewport');
				tmpviewport.doLayout(true, false);*/
	    	}
	    }
    });
    
    

    api.expandPath('/-100/-101/');

    viewport.doLayout();
	msgpanel.setVisible(false);
	grid.setHeight(scrollheight-95);
	/*************/
	setTimeout(function(){
        Ext.get('loading').remove();
        Ext.get('loading-mask').fadeOut({remove:true});
    }, 5);
    
    //轨迹回放,map对象,给轨迹回放js使用
map = document.getElementById('mapifr').contentWindow;
//trackmap = map;


	var tmpsystem = Ext.getCmp('action-panel');
    var ab = tmpsystem.body;
    	function doAction(e, t){
	    	e.stopEvent();
	    	actions[t.id]();
	    }
	    
    if(ab != undefined){
    	ab.on('mousedown', doAction, null, {delegate:'a'});
		ab.on('click', Ext.emptyFn, null, {delegate:'a', preventDefault:true});


    }

        
        //报警中心,接收报警信息后改变报警图片
        storemsg.on('load', function(store1,records,obj){
        	if( store1.getCount()>0 ){
        		setalarmimg(path+'/internation/'+edition+'/images/3.gif');
        	}else{
        		setalarmimg(path+'/internation/'+edition+'/images/3.png');
        	}
        });
        //报警中心,查报警信息
        alarmajax();
        //报警中心,历史报警
        storehis.load({params:{start:0, limit:4}});
        //定时查询报警ajax
        //alarminterval=setInterval('alarmajax();',120*1000);
    AreaAlarmWindow.on('hide', function(){
    	map.mapObj.removeEventListener(map.mapObj,map.ADD_OVERLAY,map.addOverlayOver);
    	map.removePolygonById(map.drawTempObjectID);
        map.removePolygonById('areaalarm_polygon');
        map.resetControls();
    });
     PoiWindow.on('hide', function(){
     	map.mapObj.removeEventListener(map.mapObj,map.MOUSE_CLICK,MclickMouse);
     	map.mapObj.removeEventListener(map.mapObj,map.MOUSE_CLICK,MclickMouse_clickImg);
    	if(map.drawTempObjectID.length>0){
    		map.removeOverlayById('drawTempObjectID');
    	}
    	map.mapObj.setCurrentMouseTool(map.PAN_WHEELZOOM);
    	//重置POI窗口参数
    	Ext.getCmp("poiNamefrm").reset();
    	Ext.getCmp("poiAddressfrm").reset();
    	Ext.getCmp("poiPhoumfrm").reset();
    	Ext.getCmp("poiDescfrm").reset();
    	Ext.getCmp("poiCoordx").reset();
    	Ext.getCmp("poiCoordy").reset();
    	Ext.getCmp('poiTypeHid').setValue('');
    	Ext.getCmp('poiImgHid').setValue('poi0.gif');
    });
    //POI窗口中,选择图层下拉列表
    layerComboexpand();
 //图层控制
 listVisibleLayer();
    //勾选树根,展开所有子节点
/**
* 框架元素方法例,点击树的checkbox
*/
api.on('checkchange',function(node,checked){
    node.expand();
    node.attributes.checked = checked;
    node.on('expand',function(node){
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
//树右键
api.addListener('contextmenu',apiRightClick);
    
    //add by liuhongxiao 2012-01-30 信息采集跳转到管理中心
	if(module_type=='0'){
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
		tmpsystem.hide();
		tmploc.show();
		//var tmplocation_services = Ext.getCmp('location_services');
		//tmplocation_services.show();
		//tmplocation_services.expand(false);
		resize();
	}else if(module_type=='1'){
    	northMainBtnFlag = false;
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
	}else if(module_type=='2'){
		northMainBtnFlag = true;
    	var area_poi_div = document.getElementById('area_poi_div');
    	area_poi_div.style.display = 'none';
    	
	}else if(northMainFlag){
		//登录首页后显示首页报表
    	/*northMainBtnFlag = true;
    	var area_poi_div = document.getElementById('area_poi_div');
    	area_poi_div.style.display = 'none';*/
		
		//登录首页后显示定位平台
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
		tmpsystem.hide();
		tmploc.show();
		resize();
    }
});


//取得勾选终端
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
function getDeviceId(){
	var treeArr = new Array();
	getTreeId(root, treeArr);
	var tmpgpsDeviceid = '';
	for (var i = 0; i < treeArr.length; i++) {
		var idArr = treeArr[i].id.split('@#');
		if (idArr.length > 2) {
			tmpgpsDeviceid += idArr[0] + ',';
		}
	}
	if (tmpgpsDeviceid.length > 0) {
		tmpgpsDeviceid = tmpgpsDeviceid.substring(0, tmpgpsDeviceid.length - 1);
	}
	return tmpgpsDeviceid;
}


function getTreeIdByDeviceId(node,treeArr,deviceId){
	//node.expand();
	//15045412114@#1
	var treeid = node.id;
	var idArr = treeid.split('@#');
	if(idArr.length>1){
		if(idArr[0] == deviceId){
			treeArr[treeArr.length] = node;
			return;
		}
	}
	node.eachChild(function(child) {
		getTreeIdByDeviceId(child,treeArr,deviceId);
	});
}

function createTab(id,title,srchtml,ifrId,centerTabObj){
	var tmpTabObj = Ext.getCmp(id);
	if(tmpTabObj!=undefined){
		centerTabObj.remove(tmpTabObj);
	}
		centerTabObj.add({
		    id: id,
			title: title,
			//iconCls: 'tabs',
			html: '<iframe id="'+ifrId+'" name="'+ifrId+'" style="width: 100%; height: 100%" SCROLLING="auto" frameborder="0" src="'+srchtml+'"></iframe>',
			//html: '<iframe id="'+ifrId+'" name="'+ifrId+'" style="width: 100%; height: 100%" SCROLLING="auto" frameborder="0" src="blank.html"></iframe>',
			closable:true
		}).show();
	
}
function clickFolder(id,name){
  	var tmpObj = Ext.getDom(id);
  	var tmpli = document.getElementsByTagName('li');
  	if(tmpObj.className=='icon-folder-open'){
		tmpObj.className='icon-folder';
		for(var i=0;i<tmpli.length;i++){
			if(tmpli[i].id.indexOf(name)!=-1){
				tmpli[i].style.display='none';
			}
		}
	}else{
		tmpObj.className='icon-folder-open';
		for(var i=0;i<tmpli.length;i++){
			if(tmpli[i].id.indexOf(name)!=-1){
				tmpli[i].style.display='block';
			}
		}
	}
}

function setDisplay(id,style){
  	var tmpli = Ext.getDom(id);
	tmpli.style.display=style;
}
function setNavigation(title){
	Ext.getCmp('east-panel').setTitle(title);
}


	function resize(){
		var scrollwidth=document.documentElement.scrollWidth;
		
		var tmpsystem = Ext.getCmp('east-panel');
		if(tmpsystem.isVisible()){
			//tmpsystem.setWidth(scrollwidth - 215);
			tmpsystem.setWidth(scrollwidth - 275);
		}else{
			//tmpsystem.setWidth(scrollwidth - 215);
			tmpsystem.setWidth(scrollwidth - 275);
		}
	}

//电子围栏弹出窗口
function map_areaalarm_img(){
    	PoiWindow.hide();
    	if(!AreaActiveItem){
    		AreaAlarmPanel.layout.setActiveItem(AreaAlarmgrid);
    	}
    	
    	AreaAlarmWindow.show();
    	AreaAlarmstore.load({params:{start:0, limit:10}});
    	AreaAlarmWindow.setHeight(400);
}

//图层控制弹出窗口
var layerwin = null;
var layerform = null;
function map_layer_img(){
	visiLayerCheckIdArr = [];
	layerform = new Ext.form.FormPanel({
		labelWidth: '10',
		width: 280,
		buttonAlign : 'center',
		autoScroll: true,
		//layout: 'fit',
		bodyStyle: 'padding:0 10px 10px;',
		items:[{
			xtype: 'fieldset',
			title: main.set_layer_visible,
			id: 'layerfieldset',
			items:[]
		}],
		buttons: [{
			text: main.ok,
			handler: function(){
				var ids = '';
				for(var i = 0;i < visiLayerCheckIdArr.length; i++){
					if(Ext.getCmp(visiLayerCheckIdArr[i]+'_visiLayerCheckId').getValue()){
						ids+=visiLayerCheckIdArr[i]+',';
					}
				}
				if(ids.length>0){
					ids = ids.substring(0,ids.length-1);
				}
				updateLayerVisible(ids);
			}
		},
			{
                 text: main.close,
                 handler: function(){
                 	layerwin.close();
                 	layerwin = null;
                 }
             }
		]
	});
	    	if(!layerwin){
	    		layerwin = new Ext.Window({
		            title: main.layer_control,
		            closable:true,
		            width:300,
		            height:400,
		            maximizable: true,
		            autoScroll: true,
		            //border:false,
		            //plain:true,
		            //layout: 'fit',
		            items:[layerform],
		            listeners:{
		            	'close':function (p){
		            		layerwin = null;
		            	}
		            }
		        });
		        layerwin.show(this);
	    	}
	    	
	    	   for(var i = 0; i < visiLayerControlObj.length; i++){
	    	    	var tmp = visiLayerControlObj[i];
	    	    	var tmpid = tmp.id;
	    	    	var tmplayerName = tmp.layerName;
	    	    	var tmpmapLevel = tmp.mapLevel;
	    	    	var tmpvisible = tmp.visible;
	    	    	var tmpchecked = false;
	    	    	
	    	    	if(tmpvisible == '1'){
	    	    		tmpchecked = true;
	    	    	}
	    	    	
	    	    	var tmpform = new Ext.form.Checkbox({
	    	    		id: tmpid+'_visiLayerCheckId',
	    	    		value: tmpid,
	    	    		boxLabel: tmplayerName,
	    	    		hideLabel : true,
	    	    		checked:tmpchecked
	    	    	});
	    	    	
	    	    	visiLayerCheckIdArr.push(tmpid);
	    	    	
	    	    	var tmplayerfieldset = Ext.getCmp('layerfieldset');
	    	    	tmplayerfieldset.add(tmpform);
	    	    }
	    	    
}

//地图视野设定弹出窗口
var mapsetwin = null;
function map_mapset_img(){
	
	    	if(!mapsetwin){
	    		mapsetwin = new Ext.Window({
		            title: main.set_map_bounds,
		            closable:true,
		            width:140,
		            height:70,
		            maximizable: true,
		            border:false,
		            plain:true,
		            layout: 'fit',
		            items:[
		                	new Ext.Panel({
		                		frame:true,
		                		//layout:'column',
		                		items:[
		                		new Ext.Button({
		                			//columnWidth:.25,
							        text: main.save_map_bounds,
							        //renderTo:Ext.getBody(),
							        handler: function(){
							        	Ext.MessageBox.confirm(main.tips, main.are_you_sure_you_want_to_set_the_current_view_to_view_the_map_after_login, setmapcenterconfirm ,this , 500);
							        }
							    })]
		                	})],
		            listeners:{
		            	'close':function (p){
		            		mapsetwin = null;
		            	}
		            }
		        });
		        mapsetwin.show(this);
	    	}

}

	/**
	 * modify by zhaofeng
	 * date 2011-2-21
	 * time 16:00
	 */      

//新增--点击树上终端,已定位终端地图居中显示
api.on('click',function(node, e) {
	var id_arr='';
	id_arr = node.id.split('@#');
	if(locationPointArr.length > 0 && id_arr.length > 1){
		for(var i = 0;i < locationPointArr.length;i += 1){
			var target_id = '';
			target_id=id_arr[0];
			var point_id = '';
			var marktmp = locationPointArr[i];
			point_id=locationPointArr[i].id;
			if(point_id.indexOf(target_id+'ss') != -1){
				var lng_ = locationPointArr[i].lnglat.lngX;
				var lat_ = locationPointArr[i].lnglat.latY;
				//map.mapObj.setCenter(new map.MLngLat(lng_, lat_, map.COORD_TYPE_ENCODE));
				//取得点对象
				var poi_id=locationPointArr[i].id;
				var obj = map.mapObj.getOverlayById(locationPointArr[i].id);
				//取得点的位置描述
				var loc_desc = obj.option.tipOption.content.split('<br>')[3].split(',');
				if(loc_desc.toString().length==19){
					//通过经纬度提交后台调用相应locDesc方法解析得到位置信息
					Ext.Ajax.request({
						 url:path+'/locate/locate.do?method=locDesc',
						 method :'POST', 
						 params: {x:lng_,y:lat_},
						 success : function(request){
						   	var res= request.responseText;
						   	if(res==null || res.length==0){
						       	//Ext.Msg.alert(main.tips, main.data_not_found);
						       	//return;
						       	res = main.data_not_found;
						   	}
				          	//得到信息框中的内容
						   	var scontent = obj.option.tipOption.content;
				          	//替换信息框中的位置信息
						   	var content=scontent.replace(loc_desc,main.position+res);
						   	//设置消息框能打开
							obj.option.canShowTip=true;
							//根据经纬度居中
							map.mapObj.setZoomAndCenter( 15, new map.MLngLat(lng_, lat_, map.COORD_TYPE_ENCODE));
						   	if(obj.option.tipOption.content!=content){
						   	obj.option.tipOption.content=content;
						   	map.mapObj.updateOverlay(obj);
						   	map.mapObj.openOverlayTip(poi_id);
						   	}
						 },
						 failure : function(request) {
							//alert("查询失败");
						 }
					});
				} else{
					 map.mapObj.openOverlayTip(poi_id);
					 
				}
			}
		}
	}
});

function clickLocButton(){
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

//销售报表链接跳转
var signBillStartTimeReportChart;
var signBillEndTimeReportChart;
var signBillGpsDeviceidsReportChart;
var signBillPoiNameReportChart;
var cashStartTimeReportChart;
var cashEndTimeReportChart;
var cashGpsDeviceidsReportChart;
var cashPoiNameReportChart;
var costStartTimeReportChart;
var costEndTimeReportChart;
var costGpsDeviceidsReportChart;

var signBillGpsDeviceidsReportChart;
var signBillPoiNameReportChart;
var cashGpsDeviceidsReportChart;
var cashPoiNameReportChart;
var costGpsDeviceidsReportChart;