/**
 * 轨迹回放Window
 * @class UITrackPanel
 * @extends Ext.Panel
 */
UITrackWindow = Ext.extend(Ext.Window, {
	title: '轨迹回放',
	width:270,
	height:280,
	shim:false,
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
	listeners:{'beforehide':function(){
		msgpanel.setVisible(false);
		if(map.fullmapflag){
			grid.setHeight(scrollheight);
		}else{
		    grid.setHeight(scrollheight-97);
		}
	    alarmbottonflag = false;}},
	initComponent: function(){
		var trackSearchPanel = new UITrackSearchPanel();
		var trackControlPanel = new UITrackControlPanel();
		//trackSearchPanel.anchor = '100% 45%';
		//trackControlPanel.anchor = '100% 55%';
		//trackControlPanel.layout = 'fit';
	    this.items = [{
	    	id : 'trackquerypanel',
			//title: '轨迹查询',
			//layout: 'anchor',
			activeItem: 0,
			layout:'card',
    		items:[trackSearchPanel,trackControlPanel]
	    }];
	    //trackControlPanel.hide();
	    UITrackWindow.superclass.initComponent.call(this);
	}
});

/**
 * 轨迹回放Window
 * @class UITrackPanel
 * @extends Ext.Panel
 */
UITrackWindow1 = Ext.extend(Ext.Window, {
	title: '轨迹回放',
	width:270,
	height:260,
	shim:false,
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
	listeners:{'beforehide':function(){
		msgpanel.setVisible(false);
		if(map.fullmapflag){
			grid.setHeight(scrollheight);
		}else{
		    grid.setHeight(scrollheight-97);
		}
	    alarmbottonflag = false;}},
	initComponent: function(){
		var trackSearchPanel = new UITrackSearchPanel1();
		var trackControlPanel = new UITrackControlPanel();
		//trackSearchPanel.anchor = '100% 45%';
		//trackControlPanel.anchor = '100% 55%';
		//trackControlPanel.layout = 'fit';
	    this.items = [{
	    	id : 'trackquerypanel',
			//title: '轨迹查询',
			//layout: 'anchor',
			activeItem: 0,
			layout:'card',
    		items:[trackSearchPanel,trackControlPanel]
	    }];
	    //trackControlPanel.hide();
	    UITrackWindow1.superclass.initComponent.call(this);
	}
});