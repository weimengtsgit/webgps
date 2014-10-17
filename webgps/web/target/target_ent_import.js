Ext.QuickTips.init();   
Ext.form.Field.prototype.msgTarget = 'under';
var yearStore = new Ext.data.SimpleStore({
	fields:['id', 'name'],
	data:[['2012','2012年'],['2013','2013年'],['2014','2014年']
	,['2015','2015年'],['2016','2016年']
	,['2017','2017年'],['2018','2018年'],['2019','2019年']
	,['2020','2020年'],['2021','2021年'],['2022','2022年']]
});
var year=new Date();
var yearCombox = new Ext.form.ComboBox({   
     store : yearStore,
     editable:false,
     mode: 'local',
     triggerAction:'all',
     fieldLabel: '选择年份',
     id : 'year',
     displayField:'name',
     valueField :'id',
     value:year.getFullYear(),
     allowBlank: false,
     blankText: '请选择年份',
     maxHeight: 200
});

Ext.onReady(function() {   
    var form = new Ext.form.FormPanel({   
        baseCls: 'x-plain',
        labelWidth: 80,
        fileUpload:true,
        items: [{
            xtype: 'button',
            fieldLabel: '模板下载',
            text: typeDesc+'模板下载',
            handler: function(){
				var year = Ext.getCmp('year').getValue();
				if(year == ''){
					Ext.MessageBox.alert('提示', '请选择年份!');
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
            fieldLabel: '文件名',
            name: 'targetFile',
            inputType: 'file',
            allowBlank: false,
            blankText: '文件名不能为空',
            anchor: '90%'  // anchor width by percentage   
        },
        {
			xtype:'fieldset',
			width:400,
			height:150,
			title: '请注意',
			//autoheight:true,
			layout:'border',
			//defaultType: 'textfield',
			items:[new Ext.Panel({
                html: 'a)数据必须在导入的Excel的第一个sheet中<br>'+
				'b)模板中的目标值必须为数值，精确到两位小数<br>'+
				'c)允许导入(2003~2010)的Excel文件<br>'+
				'd)不允许导入非Excel格式的文件<br>'+
				'e)不允许导入比模板的列少或多的文件<br>',
				region: 'center'
			})]
		}]   
    });   

    var win = new Ext.Panel({   
        title: '目标维护模板导入',
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
                if(form.getForm().isValid()){
                	var year = Ext.getCmp('year').getValue();
                    Ext.MessageBox.show({
                           title: '请稍等',   
                           msg: '导入中...',   
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
								Ext.Msg.alert('提示', action.result.info);
                           },
                           failure: function(form, action){
                           		Ext.MessageBox.hide();
								Ext.Msg.alert('提示', action.result.info);
                           }
                    });
                }
           }
        }]
    });
});
