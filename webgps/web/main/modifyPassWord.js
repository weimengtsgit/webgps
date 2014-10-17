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
    			title: '�޸�����',
    			id: 'layerfieldset',
    			items:[{
    				xtype: 'textfield',
	                fieldLabel: '������',
	                id: 'frmOldPassword',
	                inputType: 'password',
	                allowBlank:false
	            },{
	            	xtype: 'textfield',
	                fieldLabel: '������',
	                id: 'frmNewPassword',
	                inputType: 'password',
	                allowBlank:false
	            },{
	            	xtype: 'textfield',
	                fieldLabel: 'ȷ��������',
	                id: 'frmConfirmNewPassword',
	                inputType: 'password',
	                allowBlank:false
	            }]
    		}],
    		buttons: [{
    			text: 'ȷ��',
    			handler: function(){
    				var tmpOldPassword = Ext.getCmp('frmOldPassword').getValue();
    				if(tmpOldPassword==''){
		        		Ext.MessageBox.alert('��ʾ', '�����������!');
		        		return ;
		        	}
    				
					var tmpPassword = Ext.getCmp('frmNewPassword').getValue();
		        	var tmpConfirmPassword = Ext.getCmp('frmConfirmNewPassword').getValue();
		        	if(tmpPassword==''){
		        		Ext.MessageBox.alert('��ʾ', '������������!');
		        		return ;
		        	}
		        	if(tmpPassword!=tmpConfirmPassword){
		        		Ext.MessageBox.alert('��ʾ', '�������������벻һ��,����������������!');
		        		return ;
		        	}
		        	Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�޸�������?', modifyPassWordConfirm);
    			}
    		}]
    	});
    	
    	if(!modifyPassWordwin){
    		modifyPassWordwin = new Ext.Window({
	            title: '�޸�����',
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
			msg: '���ڱ��� ���Ե�...',
			progressText: '����...',
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
					Ext.Msg.alert('��ʾ', '�޸ĳɹ�!');
				}else if(res.result == '2'){
					Ext.Msg.alert('��ʾ', '�޸�ʧ��,�ʺŲ�����!');
				}else if(res.result == '3'){
					Ext.Msg.alert('��ʾ', '�޸�ʧ��,�����벻ƥ��!');
				}
				
			},
			failure : function(request) {
				Ext.Msg.alert('��ʾ', "�޸�ʧ��!");
			}
		});
	}
}