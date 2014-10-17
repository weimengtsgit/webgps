Ext.QuickTips.init();   
Ext.form.Field.prototype.msgTarget = 'under';
 //ͼ��
	  var layerStore = new Ext.data.SimpleStore({
	  	fields:['id', 'name'],
	  	data:[[]]
	  });
	  //ͼ��
      var layerCombox = new Ext.form.ComboBox({   
        store : layerStore,
        editable:false,
        mode: 'local',
        triggerAction:'all',
        fieldLabel: 'ͼ������',
        id : 'layerIDfrm',
        //name : 'layerIDfrm',
        displayField:'name',
        valueField :'id',
        emptyText:'ѡ��ͼ��',
        allowBlank: false,
        blankText: '��ѡ��ͼ��',
        maxHeight: 200
    });
    //ͼ�������б�
    function layerComboexpand(){
    	Ext.Ajax.request({
		 url : path+'/poi/poi.do?method=comboboxListLayer',
		 method :'POST',
		 success : function(request) {
		   var res = request.responseText;
		   //alert(res);
		   layerStore.loadData(eval(res));
		 },
		 failure : function(request) {
		 }
		});
    }
    
Ext.onReady(function() {   
    var form = new Ext.form.FormPanel({   
        baseCls: 'x-plain',   
        labelWidth: 80,   
        //url:'http://extjs.org.cn/extjs/examples/form/upload.php',   
        
        //url:'upfile.jsp',
        fileUpload:true,   
        items: [{
            xtype: 'textfield',
            fieldLabel: '�ļ���',
            name: 'userfile',
            inputType: 'file',
            allowBlank: false,
            blankText: '�ļ�������Ϊ��',
            anchor: '90%'  // anchor width by percentage   
        },
        layerCombox,
        	/*{
        	id: 'range',
        	xtype: 'numberfield',
        	fieldLabel: '��Χ',
        	allowBlank: false,
        	blankText: '��Χ����Ϊ��',
        	value:'500'
        },*/
        	{
			xtype:'fieldset',
			width:265,
			title: 'ѡ���ע��ʽ',
			autoheight:true,
			layout:'anchor',
			defaultType: 'textfield',
			items:[{
				id:'poi0',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi0').dom.src = path+"/images/poi/poi0.gif";
						
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi0.gif');
						Ext.get('selpoi0').dom.src = path+"/images/poi/poi0.gif";

					}
				}
			},{
				id:'poi1',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi1').dom.src = path+"/images/poi/poi1.gif";
						
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi1.gif');
						Ext.get('selpoi0').dom.src = path+"/images/poi/poi1.gif";

					}
				}
			},{
				id:'poi2',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi2').dom.src = path+"/images/poi/poi2.gif";
						
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi2.gif');
						Ext.get('selpoi0').dom.src = path+"/images/poi/poi2.gif";

					}
				}								            		
			},{
				id:'poi8',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi8').dom.src = path+"/images/poi/poi8.gif";
						
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi8.gif');
						Ext.get('selpoi0').dom.src = path+"/images/poi/poi8.gif";

					}
				}								            		
			},{
				id:'poi9',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi9').dom.src = path+"/images/poi/poi9.gif";
						
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi9.gif');
						Ext.get('selpoi0').dom.src = path+"/images/poi/poi9.gif";

					}
				}								            		
			},{
				id:'poi10',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi10').dom.src = path+"/images/poi/poi10.gif";
						
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi10.gif');
						Ext.get('selpoi0').dom.src = path+"/images/poi/poi12.gif";
						Ext.get('selpoi0').dom.src = path+"/images/poi/poi10.gif";
						
					}
				}								            		
			},{
				id:'poi11',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi11').dom.src = path+"/images/poi/poi11.gif";
						
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi11.gif');
						Ext.get('selpoi0').dom.src = path+"/images/poi/poi11.gif";
						
					}
				}								            		
			},{
				id:'poi12',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('poi12').dom.src = path+"/images/poi/poi12.gif";
						
					},
					'focus':function constructImagePath(){
						Ext.getCmp('poiTypeHid').setValue(this.id);
						Ext.getCmp('poiImgHid').setValue('poi12.gif');
						Ext.get('selpoi0').dom.src = path+"/images/poi/poi12.gif";
						
					}
				}								            		
			},{
				xtype: 'hidden',
				id: 'poiTypeHid'
			},{
				xtype: 'hidden',
				id: 'poiImgHid',
				value: 'poi0.gif'
			}]
		},{
			xtype:'fieldset',
			width:265,
			title: '�����ע��ʽ',
			autoheight:true,
			layout:'anchor',
			defaultType: 'textfield',
			items:[{
				id:'selpoi0',
				inputType:'image',
				listeners:{
					'render':function showImage(){
						Ext.get('selpoi0').dom.src = path+"/images/poi/poi0.gif";
					}
				}
			}]
		},{
			xtype: 'button',
			//xtype: 'form',
			text : '����poi����ģ��',
			//html: '<a src="'+path+'/fileupload/poitemplet.xls" ></a>'
			handler: function(){
				var tmp = Ext.get('poitemplet_a');
				//var tmp = document.getElementById("poitemplet_a");
				//alert(document.getElementById("poitemplet_a").click());
				//tmp.click();
				window.open(path+'/fileupload/poitemplet.xls','_self','width=1,height=1,toolbar=no,menubar=no,location=no');      
			}
		}]   
    });   
  	
    layerComboexpand();
    
    var win = new Ext.Panel({   
        title: '��ע����',   
        renderTo: Ext.getBody(),
        width: 600,   
        //height:200,   
        //minWidth: 300,   
        //minHeight: 100,   
        layout: 'fit',   
        plain:true,   
        bodyStyle:'padding:5px;',   
        buttonAlign:'center',   
        items: form,   
  
        buttons: [{   
            text: '����',   
            handler: function() {
                if(form.form.isValid()){
                	var tmplayerID = Ext.getCmp('layerIDfrm').getValue();
                	var tmppoiImg = Ext.getCmp('poiImgHid').getValue();
                	//var tmprange = Ext.getCmp('range').getValue();
                    parent.Ext.MessageBox.show({
                           title: '���Ե�',   
                           msg: '������...',   
                           progressText: '',   
                           width:300,   
                           progress:true,   
                           closable:false,   
                           animEl: 'loding'  
                       });   
                    form.getForm().submit({
                    	//url: path+'/fileupload/fileUpload.do?method=fileUpload',
                    	//url: path+'/fileupload/poi_import_result.jsp?layerID='+tmplayerID+'&poiImg='+tmppoiImg+'&range='+tmprange,
                    	url: path+'/fileupload/poi_import_result.jsp?layerID='+tmplayerID+'&poiImg='+tmppoiImg,
                    	method:'post',
                    	//params: { layerID : tmplayerID },
                        success: function(form, action){
                           parent.Ext.Msg.alert('��ʾ',action.result.msg);
                           //win.hide();
                        },
                       failure: function(){
                          parent.Ext.Msg.alert('��ʾ', '�����ļ�ʧ��.');
                       }
                    })
                    
                }
           }
        }]
    });
});
