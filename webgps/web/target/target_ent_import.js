Ext.QuickTips.init();   
Ext.form.Field.prototype.msgTarget = 'under';
var yearStore = new Ext.data.SimpleStore({
	fields:['id', 'name'],
	data:[['2012','2012��'],['2013','2013��'],['2014','2014��']
	,['2015','2015��'],['2016','2016��']
	,['2017','2017��'],['2018','2018��'],['2019','2019��']
	,['2020','2020��'],['2021','2021��'],['2022','2022��']]
});
var year=new Date();
var yearCombox = new Ext.form.ComboBox({   
     store : yearStore,
     editable:false,
     mode: 'local',
     triggerAction:'all',
     fieldLabel: 'ѡ�����',
     id : 'year',
     displayField:'name',
     valueField :'id',
     value:year.getFullYear(),
     allowBlank: false,
     blankText: '��ѡ�����',
     maxHeight: 200
});

Ext.onReady(function() {   
    var form = new Ext.form.FormPanel({   
        baseCls: 'x-plain',
        labelWidth: 80,
        fileUpload:true,
        items: [{
            xtype: 'button',
            fieldLabel: 'ģ������',
            text: typeDesc+'ģ������',
            handler: function(){
				var year = Ext.getCmp('year').getValue();
				if(year == ''){
					Ext.MessageBox.alert('��ʾ', '��ѡ�����!');
					return;
				}
				document.excelform.action = encodeURI(encodeURI(path + '/target/target.do?method=downloadEntTemplate&type='+typeValue+'&year='+year));
				document.excelform.submit();
				setTimeout(function() {
					Ext.MessageBox.hide()
				}, 3000);
			}
        },
        yearCombox,
        {
            xtype: 'textfield',
            fieldLabel: '�ļ���',
            name: 'targetFile',
            inputType: 'file',
            allowBlank: false,
            blankText: '�ļ�������Ϊ��',
            anchor: '90%'  // anchor width by percentage   
        },
        {
			xtype:'fieldset',
			width:400,
			height:150,
			title: '��ע��',
			//autoheight:true,
			layout:'border',
			//defaultType: 'textfield',
			items:[new Ext.Panel({
                html: 'a)���ݱ����ڵ����Excel�ĵ�һ��sheet��<br>'+
				'b)ģ���е�Ŀ��ֵ����Ϊ��ֵ����ȷ����λС��<br>'+
				'c)������(2003~2010)��Excel�ļ�<br>'+
				'd)���������Excel��ʽ���ļ�<br>'+
				'e)���������ģ������ٻ����ļ�<br>',
				region: 'center'
			})]
		}]   
    });   

    var win = new Ext.Panel({   
        title: 'Ŀ��ά��ģ�嵼��',
        renderTo: Ext.getBody(),
        width: 600,
        layout: 'fit',   
        plain:true,   
        bodyStyle:'padding:5px;',   
        buttonAlign:'center',   
        items: form,
        buttons: [{   
            text: '����',   
            handler: function() {
                if(form.getForm().isValid()){
                	var year = Ext.getCmp('year').getValue();
                    Ext.MessageBox.show({
                           title: '���Ե�',   
                           msg: '������...',   
                           progressText: '',   
                           width:300,   
                           progress:true,   
                           closable:false,   
                           animEl: 'loding'  
                    });   
                    form.getForm().submit({
                           url: path + '/target/target.do?method=uploadEntTarget&type='+typeValue+'&year='+year,
                           method:'post',
                           success: function(form, action){
                           		Ext.MessageBox.hide();
								Ext.Msg.alert('��ʾ', action.result.info);
                           },
                           failure: function(form, action){
                           		Ext.MessageBox.hide();
								Ext.Msg.alert('��ʾ', action.result.info);
                           }
                    });
                }
           }
        }]
    });
});
