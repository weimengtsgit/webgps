	var store;
	var vsimcard  ;                //SIM����   
var vsimcardserial ;           //�ն����к�  
var vname ;                   // �ն��豸��       
var vselectGid   ;                //����       
var vprovince ;               // ʡ         
var vcity  ;                  // ��         
var vstarttime ;              // �ϰ�ʱ��   
var vendtime;                 // �°�ʱ��   
var vcollectinterval ;         //�ɼ����   
var vremark;                   //Ա����ע   
var vterminaltype  ; //�ն�����   radiogroup
var vgpsComboBox ='P-LENOVO';             // GPS����    
var vcarnumber ='';               //�ͻ�����     
var vcarmodel ='';                //�����ͺ�   
var vdrivernumber =''   ;        // �ͻ��ֻ�   
var voilelespeedlimit = -1 ;       // �ٶ���ֵ   
var vjj = 1;                       //�پ�       
var vspeedalarm;               //���ٱ���   
var vspeedlimitvalue = -1;           // �ٶ���ֵ   
var vduration = 5;                  // ����ʱ��   
var searchValue = '';
var week = 127;
var vmodel = '';
var vvehicleNum = '';
var vsaleDate = '';
var vdealer = '';
var vinstallers = '';
var vrepaymentPeriod = '';

var icnCombo = new Ext.ux.IconCombo({
    store: new Ext.data.SimpleStore({
        fields: ['code', 'name', 'flag'],
        data: [
            ['persion', '�ֻ�', 'x-flag-persion']
        ]
    }),
    valueField: 'code',
    displayField: 'name',
    iconClsField: 'flag',
    triggerAction: 'all',
    value: 'persion',
    mode: 'local',
    editable :false,
    width: 160
});

/**
*�û���������
**/
var root = new Ext.tree.AsyncTreeNode({
	text : '��λƽ̨',
	id : '-100',
	draggable : false // ���ڵ㲻�����϶�
});
	
var treeload = new Ext.tree.TreeLoader({
	dataUrl: path+'/manage/termGroupManage.do?actionMethod=groupListForUserCombox'	
});

var tree = new Ext.tree.TreePanel({   
	loader: treeload,   
    border:false,   
    rootVisible : false,
    root:root 
});   
/**
*�����б���
**/
      var comboxWithTree = new Ext.form.ComboBox({   
        store:new Ext.data.SimpleStore({fields:['abbr', 'state'],data:[[]]}),   
        editable:false,   
        mode: 'local',   
        triggerAction:'all',   
        fieldLabel: '����������',
        id : 'selectGid',
        displayField:'state',
        emptyText:'ѡ����',
        maxHeight: 200,   
        tpl: "<tpl for='.'><div style='height:200px'><div id='tree'></div></div></tpl>",   
        selectedClass:'',   
        onSelect:Ext.emptyFn   
    });
    
var delbut = new Ext.Action({
		        text: '����',
		        id : 'delbut',
		        //disabled : true,
		        handler: function(){
					var tmpfrmsimcard = Ext.getCmp('frmsimcard').getValue();
		        	if(tmpfrmsimcard==''){
		        		Ext.MessageBox.alert('��ʾ', '������SIM����!');
		        		return ;
		        	}
		        	vsimcard = tmpfrmsimcard;
		        	var tmpfrmsimcardserial = Ext.getCmp('frmsimcardserial').getValue();
		        	if(tmpfrmsimcardserial==''){
		        		Ext.MessageBox.alert('��ʾ', '�������ն����к�!');
		        		return ;
		        	}
		        	vsimcardserial = tmpfrmsimcardserial;
		        	var tmpfrmname = Ext.getCmp('frmname').getValue();
		        	if(tmpfrmname==''){
		        		Ext.MessageBox.alert('��ʾ', '�������ն��豸��!');
		        		return ;
		        	}
		        	vname = tmpfrmname;
		        	var tmpselectGid= Ext.getCmp('selectGid').getValue();
		        	if(tmpselectGid==''){
		        		Ext.MessageBox.alert('��ʾ', '��ѡ��������!');
		        		return ;
		        	}
		        	var tmpfrmprovince = Ext.getCmp('frmprovince').getValue();
		        	if(tmpfrmprovince==''){
		        		Ext.MessageBox.alert('��ʾ', '��ѡ��ʡ!');
		        		return ;
		        	}
		        	vprovince = tmpfrmprovince;
		        	var tmpfrmcity = Ext.getCmp('frmcity').getValue();
		        	if(tmpfrmcity==''){
		        		Ext.MessageBox.alert('��ʾ', '��ѡ����!');
		        		return ;
		        	}
		        	vcity = tmpfrmcity;
		        	var tmpfrmstarttime = Ext.getCmp('frmstarttime').getValue();
		        	if(tmpfrmstarttime==''){
		        		Ext.MessageBox.alert('��ʾ', '��ѡ���ϰ�ʱ��!');
		        		return ;
		        	}
		        	vstarttime = tmpfrmstarttime;
		        	var tmpfrmendtime = Ext.getCmp('frmendtime').getValue();
		        	if(tmpfrmendtime==''){
		        		Ext.MessageBox.alert('��ʾ', '��ѡ���°�ʱ��!');
		        		return ;
		        	}
		        	vendtime = tmpfrmendtime;
		        	var tmpfrmcollectinterval = Ext.getCmp('frmcollectinterval').getValue();
		        	if(tmpfrmcollectinterval==''){
		        		Ext.MessageBox.alert('��ʾ', '������ɼ����!');
		        		return ;
		        	}
		        	vcollectinterval = tmpfrmcollectinterval;
		        	
		        	var tmpfrmremark = Ext.getCmp('frmremark').getValue();
		        	vremark = tmpfrmremark;
		        	
		        	//�ն������ж�
		        	var tmpfrmterminaltype = Ext.getCmp('frmterminaltype').getValue().getGroupValue();
		        	//alert(tmpfrmterminaltype);
		        	vterminaltype = tmpfrmterminaltype;
		        	//�ն�����Ϊlbs,�ͻ�����Ϊ��Ա��
		        	vcarnumber = vname;
		        	if(tmpfrmterminaltype == 1){
		        		var tmpgpsComboBox = Ext.getCmp('gpsComboBox').getValue();
		        		//alert(tmpgpsComboBox);
		        		//return;
			        	if(tmpgpsComboBox==''){
			        		Ext.MessageBox.alert('��ʾ', '��ѡ���ն˳���!');
			        		return ;
			        	}
			        	vgpsComboBox = tmpgpsComboBox;
			        	
			        	var tmpfrmcarnumber = Ext.getCmp('frmcarnumber').getValue();
			        	//alert(tmpfrmcarnumber);
			        	if(tmpfrmcarnumber==''){
			        		Ext.MessageBox.alert('��ʾ', '������ͻ�����!');
			        		return ;
			        	}
			        	vcarnumber = tmpfrmcarnumber;
			        	var tmpfrmcarmodel = Ext.getCmp('frmcarmodel').getValue();
			        	//alert(tmpfrmcarmodel);
			        	if(tmpfrmcarmodel==''){
			        		Ext.MessageBox.alert('��ʾ', '�����복���ͺ�!');
			        		return ;
			        	}
			        	vcarmodel = tmpfrmcarmodel;
			        	var tmpfrmdrivernumber = Ext.getCmp('frmdrivernumber').getValue();
			        	//alert(tmpfrmdrivernumber);
			        	if(tmpfrmdrivernumber==''){
			        		Ext.MessageBox.alert('��ʾ', '������ͻ��ֻ�!');
			        		return ;
			        	}
			        	vdrivernumber = tmpfrmdrivernumber;
			        	
			        	var tmpfrmmodel = Ext.getCmp('frmmodel').getValue();
			        	if(tmpfrmmodel==''){
			        		Ext.MessageBox.alert('��ʾ', '�������е�ͺ�!');
			        		return ;
			        	}
			        	vmodel = tmpfrmmodel;
			        	
			        	var tmpfrmvehicleNum = Ext.getCmp('frmvehicleNum').getValue();
			        	if(tmpfrmvehicleNum==''){
			        		Ext.MessageBox.alert('��ʾ', '���������!');
			        		return ;
			        	}
			        	vvehicleNum = tmpfrmvehicleNum;
			        	
			        	var tmpfrmsaleDate = Ext.getCmp('frmsaleDate').getValue();
			        	if(tmpfrmsaleDate==''){
			        		Ext.MessageBox.alert('��ʾ', '��ѡ����������!');
			        		return ;
			        	}
			        	vsaleDate = tmpfrmsaleDate.format('Y-m-d');
			        	
			        	var tmpfrmdealer = Ext.getCmp('frmdealer').getValue();
			        	if(tmpfrmdealer==''){
			        		Ext.MessageBox.alert('��ʾ', '�����뾭����!');
			        		return ;
			        	}
			        	vdealer = tmpfrmdealer;
			        	
			        	var tmpfrminstallers = Ext.getCmp('frminstallers').getValue();
			        	if(tmpfrminstallers==''){
			        		Ext.MessageBox.alert('��ʾ', '�����밲װ��Ա!');
			        		return ;
			        	}
			        	vinstallers = tmpfrminstallers;

			        	var tmpfrmrepaymentPeriod = Ext.getCmp('frmrepaymentPeriod').getValue();
			        	if(tmpfrmrepaymentPeriod==''){
			        		Ext.MessageBox.alert('��ʾ', '��ѡ�񻹿�����!');
			        		return ;
			        	}
			        	vrepaymentPeriod = tmpfrmrepaymentPeriod.format('Y-m-d');
			        	
			        	/*
			        	var tmpfrmoilelespeedlimit = Ext.getCmp('frmoilelespeedlimit').getValue();
			        	if(tmpfrmoilelespeedlimit==''){
			        		Ext.MessageBox.alert('��ʾ', '��������Ͷϵ� �ٶ���ֵ!');
			        		return ;
			        	}
			        	var tmpfrmjj = Ext.getCmp('frmjj');
			        	if(tmpfrmjj.checked){
			        		vjj = 1;
			        	}else{
			        		vjj = 0;
			        	}
			        	voilelespeedlimit = tmpfrmoilelespeedlimit;
			        	var tmpfrmspeedalarm = Ext.getCmp('frmspeedalarm');
			        	if(tmpfrmspeedalarm.checked){
			        		var tmpspeedlimitvalue = Ext.getCmp('speedlimitvalue').getValue();
				        	//alert(tmpspeedlimitvalue);
				        	if(tmpspeedlimitvalue==''){
				        		Ext.MessageBox.alert('��ʾ', '�����볬�ٱ��� �ٶ���ֵ!');
				        		return ;
				        	}
				        	vspeedlimitvalue = tmpspeedlimitvalue;
				        	var tmpduration = Ext.getCmp('duration').getValue();
				        	//alert(tmpduration);
				        	if(tmpduration==''){
				        		Ext.MessageBox.alert('��ʾ', '�����볬�ٱ��� ����ʱ��!');
				        		return ;
				        	}
				        	vduration = tmpduration;
			        	}else{
			        		vspeedlimitvalue = -1;
			        		vduration = 5;
			        	}
			        	*/
			        	voilelespeedlimit = -1 ;       // �ٶ���ֵ   
						 vjj = 1;                       //�پ�       
						 vspeedalarm;               //���ٱ���   
						 vspeedlimitvalue = -1;           // �ٶ���ֵ   
						 vduration = 5;                  // ����ʱ��   
			        	
		        	}else{
		        		//alert('1:'+vgpsComboBox);
		        		 vgpsComboBox ='P-LENOVO';               // GPS����    
						 vcarnumber ='';               //�ͻ�����     
						 vcarmodel ='';                //�����ͺ�   
						 vdrivernumber =''   ;        // �ͻ��ֻ�   
						 voilelespeedlimit = -1 ;       // �ٶ���ֵ   
						 vjj = 1;                       //�پ�       
						 vspeedalarm;               //���ٱ���   
						 vspeedlimitvalue = -1;           // �ٶ���ֵ   
						 vduration = 5;                  // ����ʱ��   
		        	}
		        	Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ������ն���?', addConfirm);
		        },
		        iconCls: 'icon-save'
		    });

		    function addConfirm(btn){
		    	if(btn=='yes'){
		    		Ext.Msg.show({
			           msg: '���ڱ��� ���Ե�...',
			           progressText: '����...',
			           width:300,
			           wait:true,
			           //waitConfig: {interval:200},
			           icon:'ext-mb-download'
			       });
			       add();
		        }
		    }
		    


//��Ӳ���
function add(){
	var tmpfrmWeekArr = Ext.getCmp('weekCheckBoxGroup').getValue();
	var weekNum = 0;
	while(tmpfrmWeekArr.length>0){
		var checkBox = tmpfrmWeekArr.pop();
		var checkedValue = checkBox.value;
		weekNum += Math.pow(2,checkedValue-1);
	}
	
var imgUrl = icnCombo.getValue();

var tmpfrmsaleMethods = Ext.getCmp('frmsaleMethods').getValue();
var tmpfrmcontractValue = Ext.getCmp('frmcontractValue').getValue();
var tmpfrmloanAmount = Ext.getCmp('frmloanAmount').getValue();
var tmpfrmclaimAct = Ext.getCmp('frmclaimAct').getValue();


	Ext.Ajax.request({
		 //url :url,
		 //url:path+'/terminal/terminal.do?method=addTerminal'+urlstr,
		url:path+'/terminal/terminal.do?method=addTerminal',
		 method :'POST', 
		 params:{
		simcard: encodeURI(vsimcard),deviceId: encodeURI(vsimcardserial),termName: encodeURI(vname),
		groupId: encodeURI(vselectGid),province: encodeURI(vprovince),city: encodeURI(vcity),
		startTime: encodeURI(vstarttime),endTime: encodeURI(vendtime),getherInterval: encodeURI(vcollectinterval),
		termDesc: encodeURI(vremark),locateType: encodeURI(vterminaltype),typeCode: encodeURI(vgpsComboBox),
		vehicleNumber: encodeURI(vcarnumber),vehicleType: encodeURI(vcarmodel),driverNumber: encodeURI(vdrivernumber),
		oilSpeedLimit: encodeURI(voilelespeedlimit),holdAlarmFlag: encodeURI(vjj),speedAlarmLimit: encodeURI(vspeedlimitvalue),
		speedAlarmLast: encodeURI(vduration), imgUrl: encodeURI(imgUrl), week: weekNum,
		model: encodeURI(vmodel), vehicleNum: encodeURI(vvehicleNum), saleDate: vsaleDate, 
		dealer: encodeURI(vdealer), installers: encodeURI(vinstallers), saleMethods: encodeURI(tmpfrmsaleMethods), 
		contractValue: encodeURI(tmpfrmcontractValue), loanAmount: encodeURI(tmpfrmloanAmount), 
		repaymentPeriod: vrepaymentPeriod, claimAct: encodeURI(tmpfrmclaimAct)
		 },
		 //params: { ctl:'add', parentid : tmpfrmParendId ,starttime:tmpfrmstarttime,endtime:tmpfrmendtime},
		 //timeout : 10000,
		 success : function(request) {
		   var res = Ext.decode(request.responseText);
		   //alert(request.responseText);
		 	if(res.result==1){
		 		//treeload.load(root);
		 		store.load({params:{start:0, limit:20}});
		   		Ext.Msg.alert('��ʾ', '����ɹ�');
		   		return;
		   }else if(res.result==3){
		   		//store.reload();
		   		Ext.Msg.alert('��ʾ', "�ն����к��Ѵ���!");
		   		return;
		   }else if(res.result==4){
		   		//store.reload();
		   		Ext.Msg.alert('��ʾ', "�ն����ﵽ����!");
		   		return;
		   }else{
		   		//store.reload();
		   		Ext.Msg.alert('��ʾ', "����ʧ��!");
		   		return;
		   }
		 },
		 failure : function(request) {
			 Ext.Msg.alert('��ʾ', "����ʧ��!");
		 }
		});
}
function locateTypeRenderer(val){
	if(val=='0'){
		return '��Ա';
	}else if(val=='1'){
		return 'GPS';
	}else{
		return 'GPS';
	}
}

function typeCodeRenderer(val){
	if(val == 'P-LENOVO'){
		return '����';
	}else if(val == 'GP-SZHQ-GPRS'){
		return '��ǿ';
	}
}

var gps_combo = new Ext.form.ComboBox( {
	hiddenName : 'typeCode',
	valueField : 'typeCode',
	id: 'gpsComboBox',
	store : new Ext.data.SimpleStore({
	  	fields:['typeCode', 'typeName'],
	  	data:[[]]
	  }),   
	displayField : 'typeName',
	//valueField :"abbr",   
    //displayField:'state',
    //typeAhead: true,
    mode: 'local',
	listeners: {
  	    expand : function(combo ) {
  	  		typeComboexpand();
    	}
    },
    disabled : true,
    editable: false,   
    //forceSelection: true,
    triggerAction: 'all',
    emptyText:'ѡ���ն˳���'
    //selectOnFocus:true
});

function typeComboexpand(){
	Ext.Ajax.request({
	 url : path+'/terminal/terminal.do?method=getAllTermType',
	 method :'POST',
	 success : function(request) {
	   var res = request.responseText;
	   //layerStore.loadData(eval(res));
	   gps_combo.getStore().loadData(eval(res));
	 },
	 failure : function(request) {
	 }
	});
}
Ext.onReady(function(){

  	
    var simple = new Ext.FormPanel({
            region: 'center',
	        labelWidth: 75, // label settings here cascade unless overridden
	        //url:'save-form.php',
	        frame:true,
	        //title: '�û�����',
	        bodyStyle:'padding:5px 5px 0',
	        autoScroll : true,
	        width: 200,
	        defaults: {width: 150},
	        defaultType: 'textfield',
	        items: [{
	                fieldLabel: 'SIM����',
	                id: 'frmsimcard',
	                width: 150,
	                allowBlank:false
	            },{
	                fieldLabel: '�ն����к�',
	                id: 'frmsimcardserial',
	                width: 150,
	                allowBlank:false
	            },{
	                fieldLabel: '�ն��豸��',
	                id: 'frmname',
	                width: 150,
	                allowBlank:false
	            },
	            //�����б�����
	            comboxWithTree,
	            //ʡ
	            comboProvinces,
	            //��
	            comboCities,
	            new Ext.form.TimeField({
	            	id:'frmstarttime',
	            	fieldLabel: '�ϰ�ʱ��',
	            	format:'H:i',
	            	value:'09:00',
	            	//width: 120,
				    increment: 1
	            }),new Ext.form.TimeField({
	            	id:'frmendtime',
	            	fieldLabel: '�°�ʱ��',
	            	format:'H:i',
	            	value:'18:00',
	            	//width: 120,
				    increment: 1
	            }),{
	            	id: 'weekCheckBoxGroup',
		            xtype: 'checkboxgroup',
		            fieldLabel: '��������',
	                width: 350,
		            items: [
				    	{boxLabel: '��1', name: 'cb-auto-1', value:'1', checked: true},
				        {boxLabel: '��2', name: 'cb-auto-2', value:'2', checked: true},
				        {boxLabel: '��3', name: 'cb-auto-3', value:'3', checked: true},
				        {boxLabel: '��4', name: 'cb-auto-4', value:'4', checked: true},
				        {boxLabel: '��5', name: 'cb-auto-5', value:'5', checked: true},
				        {boxLabel: '��6', name: 'cb-auto-6', value:'6', checked: true},
				        {boxLabel: '����', name: 'cb-auto-7', value:'7', checked: true}
				    ]
		        },{
	            	xtype : 'textfield',
	                fieldLabel: '�ɼ����(����)',
	                width: 150,
	                value :5,
	                regex : /^(-|\+)?\d+(\.\d+)?$/,
	                id: 'frmcollectinterval'
	            },{
	            	xtype : 'textarea',
	                fieldLabel: 'Ա����ע',
	                width: 150,
	                id: 'frmremark'
	            },{
				    xtype:'fieldset',
				    title: '�ն�����',
				    autoHeight:true,
				    width: 300,
				    labelWidth: 50,
				    items :[{
			            xtype: 'radiogroup',
			            id : 'frmterminaltype',
			            //fieldLabel: 'Auto Layout',
			            items: [{
				                hideLabel : true,
				                boxLabel: '�ֻ���λ', 
				                name: 'rb-auto', 
				                inputValue: 0, 
				                checked: true
			                },{
				                hideLabel : true,
				                boxLabel: '������λ', 
				                name: 'rb-auto', 
				                inputValue: 1,
				                handler : function(){
				                	var tmpgpsComboBox = Ext.getCmp('gpsComboBox');
				                	var tmpcarFieldset = Ext.getCmp('carfieldset');
				                	
				                	if(this.checked){
				                		tmpcarFieldset.expand(true);
				                		tmpgpsComboBox.enable();
				                		icnCombo.getStore().loadData([['car', '����', 'x-flag-car'],
				                		['1060', '�ó�', 'x-flag-1060'],
				                		['1063', '��׮��', 'x-flag-1063'],
				                		['1065', 'ѹ·��', 'x-flag-1065'],
				                		['1080', '����', 'x-flag-1080'],
				                		['dc', '����', 'x-flag-dc'],
				                		['ttj', '������', 'x-flag-ttj'],
				                		['wj', '�ڻ�', 'x-flag-wj'],
				                		['wj', '���', 'x-flag-wj']
				                		]);
				                		icnCombo.setValue('car');
				                	}else{
				                		tmpcarFieldset.collapse(true);
				                		tmpgpsComboBox.disable();
				                		icnCombo.getStore().loadData([['persion', '�ֻ�', 'x-flag-persion']]);
				                		icnCombo.setValue('persion');
				                	}
				                }
			            }]
			        },gps_combo
			        /*new Ext.form.ComboBox({
			        	id : 'gpsComboBox',
				        store: new Ext.data.ArrayStore({
					        fields: ['abbr', 'state'],
					        data : [['GP-SZHQ-GPRS','��ǿ']]
					    }),
					    valueField :"abbr",   
				        displayField:'state',
				        typeAhead: true,
				        mode: 'local',
				        disabled : true,
				        editable: false,   
				        forceSelection: true,
				        triggerAction: 'all',
				        emptyText:'ѡ���ն˳���',
				        selectOnFocus:true
				    })*/ ]
				  },{
					    xtype:'fieldset',
					    title: 'ͼ��',
					    autoHeight:true,
					    width: 300,
					    labelWidth: 50,
					    items :[icnCombo]
					  } ,{
		            xtype:'fieldset',
		            id : 'carfieldset',
		            //checkboxToggle:true,
		            title: '������Ϣ',
		            autoHeight:true,
		            width: 300,
		            defaults: {width: 300},
		            defaultType: 'textfield',
		            collapsed: true,
		            items :[{
		                    fieldLabel: '�ͻ�����',
		                    id : 'frmcarnumber',
		                    width: 150,
		                    allowBlank:false
		                },{
		                    fieldLabel: '�����ͺ�',
		                    id : 'frmcarmodel',
		                    width: 150
		                },{
		                    fieldLabel: '�ͻ��ֻ�',
		                    id : 'frmdrivernumber',
		                    width: 150
		                },{
		                    fieldLabel: '��е�ͺ�',
		                    id : 'frmmodel',
			                allowBlank:false,
		                    width: 150
		                },{
		                    fieldLabel: '����(���ܺ�)',
		                    id : 'frmvehicleNum',
			                allowBlank:false,
		                    width: 150
		                },{
		                    fieldLabel: '��������(�����)',
		                    id : 'frmsaleDate',
		                    xtype: 'datefield',
		                    format: 'Y-m-d',
		                    editable: false,
			                allowBlank:false,
		                    width: 150
		                },{
		                    fieldLabel: '���۷�ʽ',
		                    id : 'frmsaleMethods',
		                    width: 150
		                },{
		                    fieldLabel: '������',
		                    id : 'frmdealer',
			                allowBlank:false,
		                    width: 150
		                },{
		                    fieldLabel: '��װ��Ա',
		                    id : 'frminstallers',
			                allowBlank:false,
		                    width: 150
		                },{
		                    fieldLabel: '��ͬ���(Ԫ)',
		                    id : 'frmcontractValue',
		                    xtype: 'numberfield',
		                    width: 150
		                },{
		                    fieldLabel: '������(Ԫ)',
		                    id : 'frmloanAmount',
		                    xtype: 'numberfield',
		                    width: 150
		                },{
		                    fieldLabel: '��������',
		                    id : 'frmrepaymentPeriod',
		                    xtype: 'datefield',
		                    editable: false,
			                allowBlank:false,
		                    format: 'Y-m-d',
		                    width: 150
		                },{
		                    fieldLabel: 'ծȨ����',
		                    id : 'frmclaimAct',
		                    width: 150
		                }/*,{
				            xtype:'fieldset',
				            title: '���Ͷϵ�',
				            autoHeight:true,
				            width: 250,
				            defaults: {width: 250},
				            defaultType: 'textfield',
				            labelWidth: 100,
				            items :[{
				                    fieldLabel: '�ٶ���ֵ',
				                    id : 'frmoilelespeedlimit',
				                    value: '-1',
				                    //name: 'first',
				                    regex : /^(-|\+)?\d+(\.\d+)?$/,
				                    width: 100
				                    //allowBlank:false
				                }
				            ]
				        },{
				            xtype:'fieldset',
				            title: '��������',
				            autoHeight:true,
				            width: 250,
				            defaults: {width: 250},
				            labelWidth: 100,
				            items :[{
				                	xtype : 'checkbox',
				                	id : 'frmjj',
					                fieldLabel: ' ',
					                hideLabel : true,
					                width: 100,
					                checked : true,
					                boxLabel: '�پ�'
					            },{
				                	xtype : 'checkbox',
					                fieldLabel: ' ',
					                width: 100,
					                id : 'frmspeedalarm',
					                hideLabel : true,
					                boxLabel: '���ٱ���',
					                handler : function(){
					                	var tmpspeedlimitvalue = Ext.getCmp('speedlimitvalue');
					                	var tmpduration = Ext.getCmp('duration');
					                	if(this.checked){
					                		tmpspeedlimitvalue.enable();
					                		tmpduration.enable();
					                	}else{
					                		tmpspeedlimitvalue.disable();
					                		tmpduration.disable();
					                	}
					                }
					            },{
					            	xtype : 'textfield',
				                    fieldLabel: '�ٶ���ֵ',
				                    id: 'speedlimitvalue',
				                    width: 100,
				                    disabled : true,
				                    value : '-1',
				                    regex : /^(-|\+)?\d+(\.\d+)?$/
				                    //allowBlank:false
				                },{
				                	xtype : 'textfield',
				                    fieldLabel: '����ʱ��(����)',
				                    id: 'duration',
				                    width: 100,
				                    regex : /^(-|\+)?\d+(\.\d+)?$/,
				                    disabled : true,
				                    value : '5'
				                    //allowBlank:false
				                }
				            ]
				        }*/
		            ]
		        }
	        ]
	    });

var proxy = new Ext.data.HttpProxy({
    url: path+'/terminal/terminal.do?method=listTerminal'
});

var reader = new Ext.data.JsonReader({
    totalProperty: 'total',
    successProperty: 'success',
    idProperty: 'id',
    root: 'data'
}, [
    {name: 'id'},
    {name: 'simcard'},
    {name: 'deviceId'},
    {name: 'termName'},
    {name: 'groupName'},
    {name: 'groupId'},
    {name: 'province'},
    {name: 'city'},
    {name: 'startTime'},
    {name: 'endTime'},
    {name: 'getherInterval'},
    {name: 'termDesc'},
    {name: 'locateType'},
    {name: 'typeCode'},
    {name: 'typeName'},
    {name: 'vehicleNumber'},
    {name: 'vehicleType'},
    {name: 'driverNumber'},
    {name: 'oilSpeedLimit'},
    {name: 'holdAlarmFlag'},
    {name: 'speedAlarmLimit'},
    {name: 'speedAlarmLast'},
    {name: 'imgUrl'},
    {name: 'week'},
    {name: 'vehicleMsgId'},
    {name: 'model'},
    {name: 'vehicleNum'},
    {name: 'saleDate'},
    {name: 'saleMethods'},
    {name: 'dealer'},
    {name: 'installers'},
    {name: 'contractValue'},
    {name: 'loanAmount'},
    {name: 'repaymentPeriod'},
    {name: 'claimAct'},
    {name: 'crtDate'}
    
]);

store = new Ext.data.Store({
	autoLoad: {params:{start: 0, limit: 20, searchValue: encodeURI(searchValue) }},
    id: 'terminalstore',
    restful: true,     // <-- This Store is RESTful
    proxy: proxy,
    reader: reader,
    listeners:{
    	beforeload:{
    		fn: function(thiz,options){
    			this.baseParams ={
    				searchValue: encodeURI(searchValue)
    			}
    		}
    	}
    }
});

var userColumns =  [
	new Ext.grid.RowNumberer({header:'���',width:40}),
    {id:'id',header: "id", width: 40, sortable: true, dataIndex: 'id',hidden:true},
    {header: "SIM����",fixed:true, width: 100, sortable: true, dataIndex: 'simcard'},
    {header: "�ն����к�",fixed:true, width: 100, sortable: true, dataIndex: 'deviceId'},
    {header: "�ն��豸��",fixed:true, width: 100, sortable: true, dataIndex: 'termName'},
    {header: "����������",fixed:true, width: 100, sortable: true, dataIndex: 'groupName'},
    {header: "groupid",fixed:true, width: 100, sortable: true, dataIndex: 'groupId',hidden:true},
    {header: "ʡ",fixed:true, width: 70, sortable: true, dataIndex: 'province'},
    {header: "��",fixed:true, width: 70, sortable: true, dataIndex: 'city'},
    {header: "�ϰ�ʱ��",fixed:true, width: 100, sortable: true, dataIndex: 'startTime'},
    {header: "�°�ʱ��",fixed:true, width: 100, sortable: true, dataIndex: 'endTime'},
    {header: "�ܹ���ʱ��",fixed:true, width: 160, sortable: true, dataIndex: 'week', renderer:function (val){
		var tmpweek = '';
		if((Number(val) & 1)==1){ tmpweek += '��1,'; }
		if((Number(val) & 2)==2){ tmpweek += '��2,'; }
		if((Number(val) & 4)==4){ tmpweek += '��3,'; }
		if((Number(val) & 8)==8){ tmpweek += '��4,'; }
		if((Number(val) & 16)==16){ tmpweek += '��5,'; }
		if((Number(val) & 32)==32){ tmpweek += '��6,'; }
		if((Number(val) & 64)==64){ tmpweek += '����,'; }
		if(tmpweek.length>0){ tmpweek = tmpweek.substring(0,tmpweek.length-1); }
		return tmpweek;
	}},
    {header: "�ɼ����",fixed:true, width: 100, sortable: true, dataIndex: 'getherInterval'},
    {header: "Ա����ע",fixed:true, width: 100, sortable: true, dataIndex: 'termDesc'},
    {header: "�ն�����",fixed:true, width: 100, sortable: true, dataIndex: 'locateType',renderer:locateTypeRenderer},
    {header: "GPS����",fixed:true, width: 100, sortable: true, dataIndex: 'typeName'},
    //{header: "GPS����",fixed:true, width: 100, sortable: true, dataIndex: 'typeCode'},
    {header: "�ͻ�����",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleNumber'},
    {header: "�����ͺ�",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleType'},
    {header: "�ͻ��ֻ�",fixed:true, width: 100, sortable: true, dataIndex: 'driverNumber'},
    //{header: "���Ͷϵ�-�ٶ���ֵ",fixed:true, width: 100, sortable: true, dataIndex: 'oilSpeedLimit'},
    //{header: "�پ�",fixed:true, width: 100, sortable: true, dataIndex: 'holdAlarmFlag'},
    //{header: "���ٱ���-�ٶ���ֵ",fixed:true, width: 100, sortable: true, dataIndex: 'speedAlarmLimit'},
    //{header: "���ٱ���-����ʱ��",fixed:true, width: 100, sortable: true, dataIndex: 'speedAlarmLast'},
    {header: "��е�ͺ�",fixed:true, width: 100, sortable: true, dataIndex: 'model'},
    {header: "����(���ܺ�)",fixed:true, width: 100, sortable: true, dataIndex: 'vehicleNum'},
    {header: "��������(�����)",fixed:true, width: 100, sortable: true, dataIndex: 'saleDate'},
    {header: "���۷�ʽ",fixed:true, width: 100, sortable: true, dataIndex: 'saleMethods'},
    {header: "������",fixed:true, width: 100, sortable: true, dataIndex: 'dealer'},
    {header: "��װ��Ա",fixed:true, width: 100, sortable: true, dataIndex: 'installers'},
    {header: "��ͬ���(Ԫ)",fixed:true, width: 100, sortable: true, dataIndex: 'contractValue'},
    {header: "������(Ԫ)",fixed:true, width: 100, sortable: true, dataIndex: 'loanAmount'},
    {header: "��������",fixed:true, width: 100, sortable: true, dataIndex: 'repaymentPeriod'},
    {header: "ծȨ����",fixed:true, width: 100, sortable: true, dataIndex: 'claimAct'}
];

var userGrid = new Ext.grid.GridPanel({
        region: 'west',
        width: 500,
        loadMask: {msg:'��ѯ��...'},
        //iconCls: 'icon-grid',
        //frame: true,
        //title: 'Users',
        //autoExpandColumn: 'name',
        enableColumnHide : false,
        store: store,
        
        //plugins: [editor],
        columns : userColumns,
        //sm : sm,
        //sm : smcheckbox,
        margins: '0 0 0 0',
		tbar: [
        	new Ext.form.TextField({
	            id: 'DeviceIdField',
	            width: 80,
	            enableKeyEvents: true,
	            listeners: {
        			keypress : function( textField, e ) {
		        		if (e.getKey() == e.ENTER) {
		        			searchValue = Ext.getCmp('DeviceIdField').getValue();
			    			store.load({params:{start:0, limit:20, searchValue: searchValue }});
			    			
		                }
        			}
        		}
		    }),'-',new Ext.Action({
		        text: '��ѯ',
		        handler: function(){
		        	searchValue = Ext.getCmp('DeviceIdField').getValue();
	    			store.load({params:{start:0, limit:20, searchValue: searchValue }});
	    			
		        },
		        iconCls: 'icon-search'
		    }),'-',delbut
        ],
        bbar: new Ext.PagingToolbar({
            pageSize: 20,
            store: store,
            displayInfo: true,
            displayMsg: '��{0}����{1}������ ��{2}��',
            emptyMsg: "û������"
        })
    });
	store.load({params:{start:0, limit:20}});
	
    var viewport = new Ext.Viewport({layout: 'border',items: [simple,userGrid]});
    
    
    tree.on('click',function(node){
          comboxWithTree.setValue(node.text);   
          var tmpidArr = node.id.split('@');
          if(tmpidArr[0] == '-101'){
          	vselectGid = '-1';
          }else{
          	vselectGid = tmpidArr[0];
          }
          comboxWithTree.collapse();
         var tmpweek = Number(tmpidArr[4]);
         var myCheckboxGroup = Ext.getCmp('weekCheckBoxGroup').items;
         var cbitems = myCheckboxGroup.items;
         for (var i = 0; i < cbitems.length; i++) {
           	var checkedValue = Number(cbitems[i].value);
            checkedValue = Math.pow(2,checkedValue-1);
            if((tmpweek & checkedValue) == checkedValue){
            	cbitems[i].setValue(true);
            }else{
            	cbitems[i].setValue(false);
            }
         }
         
         Ext.getCmp('frmstarttime').setValue(tmpidArr[1]);
         Ext.getCmp('frmendtime').setValue(tmpidArr[2]);
         
    });
    comboxWithTree.on('expand',function(){   
        tree.render('tree');   
        this.innerList.dom.style.overflowX="auto"; //�ı����overflow����ʽ����ʾ����Ĺ�����
      });   

    comboxWithTree.render('comboxWithTree');  
    
});