/*
 * Ext JS Library 2.2
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.onReady(function(){
    // basic tabs 1, built from existing content
    var tabs = new Ext.TabPanel({
        renderTo: 'tabs1',
        width:200,
        activeTab: 0,
        frame:true,
        defaults:{autoHeight: true},
        items:[
            {contentEl:'script', title: 'ÊµÊ±¸ú×Ù'},
            {contentEl:'markup', title: '¹ì¼£²éÑ¯'}
        ]
    });

    // second tabs built from JS
     

    function handleActivate(tab){
        alert(tab.title + ' was activated.');
    }
});