var modifyPassWordwin = null;
function modifyPassWord(){
    	
    	var layerform = new Ext.form.FormPanel({
    		labelWidth: '10',
    		width: '150',
    		buttonAlign : 'center',
    		layout: 'fit',
    		bodyStyle: 'padding:0 10px 10px;',
    		items:[{
    			xtype: 'fieldset',
    			title: '修改密码',
    			id: 'layerfieldset',
    			items:[{
    				xtype: 'textfield',
	                fieldLabel: '旧密码',
	                id: 'frmOldPassword',
	                inputType: 'password',
	                allowBlank:false
	            },{
	            	xtype: 'textfield',
	                fieldLabel: '新密码',
	                id: 'frmNewPassword',
	                inputType: 'password',
	                allowBlank:false
	            },{
	            	xtype: 'textfield',
	                fieldLabel: '确认新密码',
	                id: 'frmConfirmNewPassword',
	                inputType: 'password',
	                allowBlank:false
	            }]
    		}],
    		buttons: [{
    			text: '确定',
    			handler: function(){
    				var tmpOldPassword = Ext.getCmp('frmOldPassword').getValue();
    				if(tmpOldPassword==''){
		        		Ext.MessageBox.alert('提示', '请输入旧密码!');
		        		return ;
		        	}
    				
					var tmpPassword = Ext.getCmp('frmNewPassword').getValue();
		        	var tmpConfirmPassword = Ext.getCmp('frmConfirmNewPassword').getValue();
		        	if(tmpPassword==''){
		        		Ext.MessageBox.alert('提示', '请输入新密码!');
		        		return ;
		        	}
		        	if(tmpPassword!=tmpConfirmPassword){
		        		Ext.MessageBox.alert('提示', '两次新密码输入不一致,请重新输入新密码!');
		        		return ;
		        	}
		        	Ext.MessageBox.confirm('提示', '您确定要修改密码吗?', modifyPassWordConfirm);
    			}
    		}]
    	});
    	
    	if(!modifyPassWordwin){
    		modifyPassWordwin = new Ext.Window({
	            title: '修改密码',
	            closable:true,
	            width:350,
	            height:200,
	            maximizable: true,
	            //border:false,
	            //plain:true,
	            layout: 'fit',
	            items:[layerform],
	            listeners:{
	            	'close':function (p){
	            		modifyPassWordwin = null;
	            	}
	            }
	        });
	        modifyPassWordwin.show(this);
    	}
       
}

function modifyPassWordConfirm(btn){
	var tmpOldPassword = Ext.getCmp('frmOldPassword').getValue();
	var tmpPassword = Ext.getCmp('frmNewPassword').getValue();
	var tmpConfirmPassword = Ext.getCmp('frmConfirmNewPassword').getValue();
	
	if(btn=='yes'){
		Ext.Msg.show({
			msg: '正在保存 请稍等...',
			progressText: '保存...',
			width:300,
			wait:true,
			//waitConfig: {interval:200},
			icon:'ext-mb-download'
		});
		Ext.Ajax.request({
			//url :url,
			url:path+'/system/user.do?method=updatePassword',
			method :'POST', 
			params: {password: tmpOldPassword, newPassword: tmpPassword},
			//timeout : 10000,
			success : function(request) {
				var res = Ext.decode(request.responseText);
				if(res.result == '1'){
					Ext.Msg.alert('提示', '修改成功!');
				}else if(res.result == '2'){
					Ext.Msg.alert('提示', '修改失败,帐号不存在!');
				}else if(res.result == '3'){
					Ext.Msg.alert('提示', '修改失败,旧密码不匹配!');
				}
				
			},
			failure : function(request) {
				Ext.Msg.alert('提示', "修改失败!");
			}
		});
	}
}