Ext.QuickTips.init();   
Ext.form.Field.prototype.msgTarget = 'under';

Ext.onReady(function() {   
    var form = new Ext.form.FormPanel({   
        baseCls: 'x-plain',   
        labelWidth: 80,   
        fileUpload:true,   
        items: [{
            xtype: 'textfield',
            fieldLabel: '文件名',
            name: 'userfile',
            inputType: 'file',
            allowBlank: false,
            blankText: '文件名不能为空',
            anchor: '90%'  
        },
        {
			xtype: 'button',
			text : '下载poi导入模板',
			handler: function(){
				var tmp = Ext.get('vehicletemplet_a');
				window.open(path+'/system/vehicleBasicInformation/vehicletemplet.xls','_self','width=1,height=1,toolbar=no,menubar=no,location=no');      
			}
		}]   
    });   
  	
    var win = new Ext.Panel({   
        title: '车辆基本信息导入',   
        renderTo: Ext.getBody(),
        width: 600,   
        layout: 'fit',   
        plain:true,   
        bodyStyle:'padding:5px;',   
        buttonAlign:'center',   
        items: form,   
        buttons: [{   
            text: '导入',   
            handler: function() {
                if(form.form.isValid()){
                    parent.Ext.MessageBox.show({
                           title: '请稍等',   
                           msg: '导入中...',   
                           progressText: '',   
                           width:300,   
                           progress:true,   
                           closable:false,   
                           animEl: 'loding'  
                       });   
                    form.getForm().submit({
                    	url: path+'/system/vehicleBasicInformation/vehicle_import_result.jsp',
                    	method:'post',
                        success: function(form, action){
                           parent.Ext.Msg.alert('提示',action.result.msg);
                        },
                       failure: function(){
                          parent.Ext.Msg.alert('提示', '导入文件失败.');
                       }
                    })
                    
                }
           }
        }]
    });
});
