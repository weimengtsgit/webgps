function terminal_btn_click(){
    //window.open ("http://caoc1983.gicp.net:81/Phone/OutLogin.aspx?username=YWRtaW4=&password=YWRtaW4=", "newwindow", "height=600, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
    window.open ("http://caoc1983.gicp.net:81/Phone/OutLogin.aspx?username=YWRtaW4=&password=YWRtaW4=", "newwindow", "height=600, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
}

function statistics_btn_click(){
    window.open ("http://vip.sosgps.cn/zhy/login/");
}

function mms_btn_click(){
	if(mmsAccount == '' || mmsPwd == ''){
		Ext.Msg.alert('提示', "无登录彩信平台账号!");
		return;
	}
	var name = 'mmswindow';
    var tempForm = document.createElement("form");
    tempForm.id="tempForm1";
    tempForm.method="post";
    tempForm.action=mmspath;
    tempForm.target=name;
    var hideInput = document.createElement("input");
    hideInput.type="hidden";
    hideInput.name= "branchId";
    hideInput.value= mmsAccount;
    tempForm.appendChild(hideInput);
    var hideInput1 = document.createElement("input");
    hideInput1.type="hidden";
    hideInput1.name= "password";
    hideInput1.value= mmsPwd;
    tempForm.appendChild(hideInput1);
    tempForm.attachEvent("onsubmit",function(){ openWindow(name); });
    document.body.appendChild(tempForm);
    tempForm.fireEvent("onsubmit");
    tempForm.submit();
    document.body.removeChild(tempForm);
	/*Ext.Ajax.request({
		url : path + '/system/ent.do?method=queryMmsAccount',
		method : 'POST',
		//timeout : 10000,
		success : function(request) {
			var res = request.responseText;
			var mms_res = Ext.util.JSON.decode(res);
			var result_ = mms_res.result;
			if(result_ == '0'){
				var mmsAccount = mms_res.mmsAccount;
				var mmsPwd = mms_res.mmsPwd;
				//parent.mms_btn_click_success(mmsAccount_, mmsPwd_);
				var name = 'mmswindow';
			    var tempForm = document.createElement("form");
			    tempForm.id="tempForm1";
			    tempForm.method="post";
			    tempForm.action=mmspath;
			    tempForm.target=name;
			    var hideInput = document.createElement("input");
			    hideInput.type="hidden";
			    hideInput.name= "branchId";
			    hideInput.value= "admin";
			    tempForm.appendChild(hideInput);
			    var hideInput1 = document.createElement("input");
			    hideInput1.type="hidden";
			    hideInput1.name= mmsAccount;
			    hideInput1.value= mmsPwd;
			    tempForm.appendChild(hideInput1);
			    tempForm.attachEvent("onsubmit",function(){ openWindow(name); });
			    document.body.appendChild(tempForm);
			    tempForm.fireEvent("onsubmit");
			    tempForm.submit();
			    document.body.removeChild(tempForm);
			}else if(result_ == '1'){
				Ext.Msg.alert('提示', "无登录彩信平台账号!");
			}else{
				Ext.Msg.alert('提示', "无登录彩信平台权限!");
			}
		},
		failure : function(request) {
			Ext.Msg.alert('提示', "登录彩信平台超时!");
		}
	});*/
}

function openWindow(name){
	window.open('about:blank', name, "height=600, width=1000, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");     
}
