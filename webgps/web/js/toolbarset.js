/*************** help ***************/
//取得应用路径
function getAppPath(){
	url = window.location.href;
	pathname = window.location.pathname;
	pathname = pathname.substr(1,pathname.length);
	i = pathname.indexOf("/");
	pathname = pathname.substr(0,i);
	re = window.location.protocol + "//" + window.location.host;
	re += "/"+pathname;
	return re;
}

//-------------------------------------------------------------------------------//
//展现终端调度信息请求div
function sendMessage(){
	re = getDevids();
	if(re == null || re == ""){
		//alert("请选择终端");
		return;
	}	

	var divTitle = "信息调度";
	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
  	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\">短信内容：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\"><textarea name=\"textarea\" id=\"msgcont\" class=\"tip_area\"></textarea></td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"发 送\" class=\"btn\" onclick=\"dd_sendMessage('"+re+"');\"/>&nbsp;&nbsp;";
  	inner += "<input name=\"\" type=\"button\" value=\"取 消\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}

//发送短信请求
function dd_sendMessage(re){

	if(re == null || re == ""){
		//alert("未选择终端");
		return;
	}
	 
	var content = document.getElementById("msgcont").value;
	if(content==null||content == ""){
		alert("短信内容不能为空");
		return;
	}
	if(content.length>200){
		alert("短信内容长度不能超过200");
		return;
	}
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", getAppPath()+"/message/message.do?method=create&deviceIds="+re+"&content="+content, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			msg_parseResp(xmlHttp.responseXML);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		xmlHttp.send(null);
	}, 10);
	//隐藏互动div
	hudong_div_close();
}

//解析短信、语音请求返回状态
function msg_parseResp(oXmlDoc){
 if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	var c = oXmlDoc.getElementsByTagName("c")[0].firstChild.nodeValue;
	var d = oXmlDoc.getElementsByTagName("d")[0].firstChild.nodeValue;
	////redo
	var pl_content = "信息调度";
	if(c!=null && c =="0"){
		pl_content += "成功";
	}else{
		//alert("返回码："+c+"\r\n返回描述："+d);
		pl_content += "失败";
	}
		//在消息框加提示信息
	document.getElementById("tip").style.display="block";
	tip_con_show();
	var old_content = document.getElementById("tip_con").innerHTML;
	if(old_content!=null && old_content!=""){
		var pl_split = old_content.split("<BR>");
		if(pl_split.length==4){
			old_content = pl_split[0]+"<BR>"+pl_split[1]+"<BR>"+pl_split[2];
		}
	}
	document.getElementById("tip_con").innerHTML=pl_content+"<BR>"+old_content;
}
//--------------------------------------------------------------------------------//
//拍照设置请求div
function photoParam_set(){
	re = getDevids();
	if(re == null || re == ""){
		//alert("请选择终端");
		return;
	}	
  	var divTitle = "拍照";
	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
  	 
  	inner += "<tr>";
  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"拍 摄\" class=\"btn\" onclick='setPhotoParam();'/>&nbsp;&nbsp;";
  	inner += "<input name=\"\" type=\"button\" value=\"取 消\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}
//保存报警参数指令
function setPhotoParam(){
	var methodPara= "create";
  	 
  	var deviceIs = getDevids();	
  	var path =getAppPath()+"/photo/takePhoto.do?method="+methodPara+"&deviceIds="+deviceIs;
		
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", path, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
		if(xmlHttp.status==200){ 
			setPhoto_parseResp(xmlHttp.responseXML);
			}
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		 xmlHttp.send(null);
	}, 10);
	//隐藏互动div
	hudong_div_close();
}
//报警参数设置返回状态
function setPhoto_parseResp(oXmlDoc){
if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	var c = oXmlDoc.getElementsByTagName("c")[0].firstChild.nodeValue;
	var d = oXmlDoc.getElementsByTagName("d")[0].firstChild.nodeValue;
	////redo
	var pl_content = "拍照设置";
	if(c!=null && c =="0"){
		pl_content += "成功";
	}else{
		//alert("返回码："+c+"\r\n返回描述："+d);
		pl_content += "失败";
	}
		//在消息框加提示信息
	document.getElementById("tip").style.display="block";
	tip_con_show();
	var old_content = document.getElementById("tip_con").innerHTML;
	if(old_content!=null && old_content!=""){
		var pl_split = old_content.split("<BR>");
		if(pl_split.length==4){
			old_content = pl_split[0]+"<BR>"+pl_split[1]+"<BR>"+pl_split[2];
		}
	}
	document.getElementById("tip_con").innerHTML=pl_content+"<BR>"+old_content;

}
//-------------------------------------------------------------------------------//

//报警参数设置请求div
function alarmParam_set(){
	re = getDevids();
	if(re == null || re == ""){
		//alert("请选择终端");
		return;
	}	
  	var divTitle = "报警参数设置";
	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
  	inner += "<tr>";
  	inner += "<td align=\"left\" nowrap>超速报警 <input type=\"radio\"  checked value=\"202\" name=\"alarmType\"/>&nbsp;&nbsp;&nbsp;区域报警<input type=\"radio\"  value =\"200\" name=\"alarmType\">&nbsp;&nbsp;&nbsp;偏航报警<input type=\"radio\"  value =\"206\" name=\"alarmType\"></td>";
   	inner += "</tr>";
   	  	inner += "<tr>";
  	inner += "<td align=\"left\">持续时间：<span style=\" color:#ff0000;\">＊</span><input  type=text id=timelen name=timelen class='input1' onKeyPress='inputNumberOnly()'> 秒</td>";
 
  	inner += "</tr>";
  	  	inner += "<tr>";
  	inner += "<td align=\"left\">报警间隔：<span style=\" color:#ff0000;\">＊</span><input  type=text id=interval name=interval class='input1' onKeyPress='inputNumberOnly()'> 秒</td>";
  
  	inner += "</tr>";
  	  	inner += "<tr>";
  	inner += "<td align=\"left\">报警次数：<span style=\" color:#ff0000;\">＊</span><input  type=text id=times name=times class='input1'  onKeyPress='inputNumberOnly()'> 秒</td>";
  	 
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"设 置\" class=\"btn\" onclick='setAlarmParam();'/>&nbsp;&nbsp;";
  	inner += "<input name=\"\" type=\"button\" value=\"取 消\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}
//保存报警参数指令
function setAlarmParam(){
	var methodPara= "setAlarmParam";
  	var atype = document.getElementsByName("alarmType");
   	 var setType;
  	 for (var i=0; i<atype.length; i++){
  	  if (atype[i].checked){
  	 	setType = atype[i].value;
  	  } 
  	 }
  	 var timelen = document.getElementById("timelen").value;
  	 var interval = document.getElementById("interval").value;
  	 var times = document.getElementById("times").value;
  	 if (timelen == null || timelen==""){
  	 	alert("持续时间不能为空！");
		return ;
  	 }
  	 if (interval==null||interval==""){
  	 	alert("报警间隔不能为空！");
  	 	return;
  	 }
  	 if (times==null || times ==""){
  	 	alert("报警次数不能为空！");
  	 	return;
  	 }
  	  
  	var deviceIs = getDevids();	
  	var path =getAppPath()+"/alarm/speedAlarm.do?method="+methodPara+"&deviceIds="+deviceIs+"&type="+setType+"&timelen="+timelen+"&interval="+interval+"&times="+times;
		
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", path, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
		if(xmlHttp.status==200){ 
			setAlarmParam_parseResp(xmlHttp.responseXML);
			}
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		 xmlHttp.send(null);
	}, 10);
	//隐藏互动div
	hudong_div_close();
}
//报警参数设置返回状态
function setAlarmParam_parseResp(oXmlDoc){
if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	var c = oXmlDoc.getElementsByTagName("c")[0].firstChild.nodeValue;
	var d = oXmlDoc.getElementsByTagName("d")[0].firstChild.nodeValue;
	////redo
	var pl_content = "报警参数设置";
	if(c!=null && c =="0"){
		pl_content += "成功";
	}else{
		//alert("返回码："+c+"\r\n返回描述："+d);
		pl_content += "失败";
	}
		//在消息框加提示信息
	document.getElementById("tip").style.display="block";
	tip_con_show();
	var old_content = document.getElementById("tip_con").innerHTML;
	if(old_content!=null && old_content!=""){
		var pl_split = old_content.split("<BR>");
		if(pl_split.length==4){
			old_content = pl_split[0]+"<BR>"+pl_split[1]+"<BR>"+pl_split[2];
		}
	}
	document.getElementById("tip_con").innerHTML=pl_content+"<BR>"+old_content;

}
//--------------------------------------------------------------------------------//
//停止报警请求div
function stopAlarm_set(){
	re = getDevids();
	if(re == null || re == ""){
		//alert("请选择终端");
		return;
	}	
  	var divTitle = "停止报警";
	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
  	inner += "<tr>";
  	inner += "<td align=\"left\" nowrap> <input type=\"radio\"  checked value=\"1\" name=\"stopType\"/>主动报警&nbsp;&nbsp;&nbsp;<input type=\"radio\"  value =\"3\" name=\"stopType\">区域报警&nbsp;&nbsp;&nbsp;<input type=\"radio\"  value =\"4\" name=\"stopType\">超速报警&nbsp;&nbsp;&nbsp;<input type=\"radio\"  value =\"0\" name=\"stopType\">其他报警</td>";
  	 
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"设 置\" class=\"btn\" onclick='stopAlarm();'/>&nbsp;&nbsp;";
  	inner += "<input name=\"\" type=\"button\" value=\"取 消\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}
//保存停止报警指令
function stopAlarm(){
	var methodPara= "stopAlarm";
  	var atype = document.getElementsByName("stopType");
   	 var setType;
  	 for (var i=0; i<atype.length; i++){
  	  if (atype[i].checked){
  	 	setType = atype[i].value;
  	  } 
  	 }
  	  
  	var deviceIs = getDevids();	
  	var path =getAppPath()+"/alarm/speedAlarm.do?method="+methodPara+"&deviceIds="+deviceIs+"&type="+setType;
		
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", path, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
		if(xmlHttp.status==200){ 
			stop_parseResp(xmlHttp.responseXML);
			}
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		 xmlHttp.send(null);
	}, 10);
	//隐藏互动div
	hudong_div_close();
}
//停止报警返回状态
function stop_parseResp(oXmlDoc){
if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	var c = oXmlDoc.getElementsByTagName("c")[0].firstChild.nodeValue;
	var d = oXmlDoc.getElementsByTagName("d")[0].firstChild.nodeValue;
	////redo
	var pl_content = "停止报警设置";
	if(c!=null && c =="0"){
		pl_content += "成功";
	}else{
		//alert("返回码："+c+"\r\n返回描述："+d);
		pl_content += "失败";
	}
		//在消息框加提示信息
	document.getElementById("tip").style.display="block";
	tip_con_show();
	var old_content = document.getElementById("tip_con").innerHTML;
	if(old_content!=null && old_content!=""){
		var pl_split = old_content.split("<BR>");
		if(pl_split.length==4){
			old_content = pl_split[0]+"<BR>"+pl_split[1]+"<BR>"+pl_split[2];
		}
	}
	document.getElementById("tip_con").innerHTML=pl_content+"<BR>"+old_content;

}


//-------------------------------------------------------------------------------//

//频率设置请求div
function pl_set(devicesId){
	var darray ;
	var re ="";
	if(typeof devicesId != "undefined" && devicesId!=null && devicesId!=""){
		re = devicesId;
	}
	else {
		var temp = getDevids();
		var size = temp.length;
		for(var i=0;i<size;i++){
			re +=temp[i]+"|";
		}
		if(re.length>0){
			re = re.substring(0,re.length-1);
		}
			var objs = getCheckObjects(false);
    if(objs != null)
	    s = objs.length;
    for(var i=0; i < s; i++){
   		var obj = objs[i];
		if(obj.folder){
		    continue;
		}
   		var type = objs[i].expend1;
		if(type == "0"){
           alert("频率设置不能包含LBS，请重新选择终端");
           return;
		}else if(type == "1"){
		}
	}	
	}
	if(re == null || re == ""){
		//alert("请选择终端");
		return;
	}	

	darray = re.split("|");
	 
  	var curFreqValue;
  	  
  	    //获取终端当前设置的频率值
 		if (darray.length == 1){
	 
		  	var xmlHttp = XmlHttp.create();	 				 		 
			var url = "../paramsetting/freqInterval.do?method=getCurFreqValue&deviceId="+re;
		 	xmlHttp.open("get", url, false);
			xmlHttp.onreadystatechange = function () {
				if (xmlHttp.readyState == 4) {
					curFreqValue = xmlHttp.responseText;					 		 
				}
			};
			xmlHttp.send(null);
		}
		 
	 
	
	 
  	var divTitle = "频率设置";
	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
  	if (darray.length == 1){
  	
  	inner += "<tr>";
  	inner += "<td align=\"right\">当前频率值：</td>";
  	inner += "<td align=\"left\" id=\"curFreq\">"+curFreqValue+"</td>";
  	inner += "</tr>";
  	}
  	inner += "<tr>";
  	inner += "<td align=\"right\">上报频率：</td>";
  	inner += "<td align=\"left\"><input  type=text id=content name=content class='input1'  onKeyPress='inputNumberOnly()'> 秒</textarea></td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"设 置\" class=\"btn\" onclick=\"pl_add('"+re+"');\"/>&nbsp;&nbsp;";
  	inner += "<input name=\"\" type=\"button\" value=\"取 消\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}

//保存频率设置
function pl_add(devicesId){

	var re ="";
	if(typeof devicesId != "undefined" && devicesId!=null && devicesId!=""){
		re = devicesId;
	}
	else {
		var temp = getDevids();
		var size = temp.length;
		for(var i=0;i<size;i++){
			re +=temp[i]+"|";
		}
		if(re.length>0){
			re = re.substring(0,re.length-1);
		}
	}
	if(re == null || re == ""){
		//alert("请选择终端");
		return;
	}	
	var content = document.getElementById("content").value;

	if(content==null||content == ""){
		alert("频率值不能为空");
		return;
	}
	if(content<5){
	    alert("上报频率不能小于5");
	    return;
	}
//	if(content<20){
//	    alert("上报频率不能小于20");
//	    return;
//	}
	var path =getAppPath()+"/paramsetting/freqInterval.do?method=create&deviceIds="+re+"&interval="+content;
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", path, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			pl_parseResp(xmlHttp.responseXML);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		xmlHttp.send(null);
	}, 10);
	//隐藏互动div
	hudong_div_close();
}
//频率解析请求返回状态
function pl_parseResp(oXmlDoc){
	if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	var c = oXmlDoc.getElementsByTagName("c")[0].firstChild.nodeValue;
	var d = oXmlDoc.getElementsByTagName("d")[0].firstChild.nodeValue;
	////redo
	var pl_content = "上报频率设置";
	if(c!=null && c =="0"){
		pl_content += "成功";
	}else{
		//alert("返回码："+c+"\r\n返回描述："+d);
		pl_content += "失败";
	}
		//在消息框加提示信息
	document.getElementById("tip").style.display="block";
	tip_con_show();
	var old_content = document.getElementById("tip_con").innerHTML;
	if(old_content!=null && old_content!=""){
		var pl_split = old_content.split("<BR>");
		if(pl_split.length==4){
			old_content = pl_split[0]+"<BR>"+pl_split[1]+"<BR>"+pl_split[2];
		}
	}
	document.getElementById("tip_con").innerHTML=pl_content+"<BR>"+old_content;
	
}

//超速设置请求div
function speed_set(devicesId){
	var re ="";
	if(typeof devicesId != "undefined" && devicesId!=null && devicesId!=""){
		re = devicesId;
	}
	else {
		var temp = getDevids();
		 
		if (temp=="undefined"){		 
		    return;
		}
		var size = temp.length;
		for(var i=0;i<size;i++){
			re +=temp[i]+"|";
		}
		if(re.length>0){
			re = re.substring(0,re.length-1);
		}
		var objs = getCheckObjects(false);
    if(objs != null)
	    s = objs.length;
    for(var i=0; i < s; i++){
   		var obj = objs[i];
		if(obj.folder){
		    continue;
		}
   		var type = objs[i].expend1;
		if(type == "0"){
           alert("超速报警设置不能包含LBS，请重新选择终端");
           return;
		}else if(type == "1"){
		}
	}	
	}
	if(re == null || re == ""){
		//alert("请选择终端");
		return;
	}	
    
	var darray = re.split("|");
	 
  	var curMaxSpeed;
  	  
  	    //获取终端当前设置的频率值
 		if (darray.length == 1){
	 
		  	var xmlHttp = XmlHttp.create();	 				 		 
			var url = "../alarm/speedAlarm.do?method=getCurSpeedSetting&deviceId="+re;
		 	xmlHttp.open("get", url, false);
			xmlHttp.onreadystatechange = function () {
				if (xmlHttp.readyState == 4) {
					curMaxSpeed = xmlHttp.responseText;					 		 
				}
			};
			xmlHttp.send(null);
		}
		
	if(curMaxSpeed==undefined){
		curMaxSpeed = "";
	}
  	var divTitle = "超速报警";
	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
  	if (darray.length == 1){
  	
  	inner += "<tr>";
  	inner += "<td align=\"right\">当前速阀：</td>";
  	inner += "<td align=\"left\" id=\"curFreq\">"+curMaxSpeed+" km/h</td>";
  	inner += "</tr>";
  	}
  	
  	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\">设置/取消：</td>";
  	inner += "<td align=\"left\"><input type=radio name=isSet id=isSet value='1' checked onClick='_speedchange2()'>设置<input type='radio' name='isSet' id='isSet' value='0' onClick='_speedchange1()'>取消</td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td align=\"right\" >超速设置： </td>";
  	inner += "<td align=\"left\"><input name=alarmSpeed id=alarmSpeed type=text class='input1' szie=12  onKeyPress='inputNumberOnly()'/></input> 公里/小时</td>";
  	inner += "</tr>";
  	//inner += "<tr>";
  	//inner += "<td align=\"right\" >持续时间：<span style=\" color:#ff0000;\">＊</span></td>";
  	//inner += "<td align=\"left\"><input name=continuedTime id=continuedTime type=text class='input1' size=12 onKeyPress='inputNumberOnly()' /></input>秒</td>";
  	//inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"设 置\" class=\"btn\" onclick=\"speed_add('"+re+"');\"/>&nbsp;&nbsp;";
  	inner += "<input name=\"\" type=\"button\" value=\"取 消\" class=\"btn\" onclick='hudong_div_close();'/>";
  	inner += "<span style=\"position:absolute;right:15px;color:#15428b;\"><a href=\"javascript:jump('"+getAppPath()+"alarm/Alarm_Set_List.jsp');\"><span style=\"color:#15428b;\">超速设置列表</span></a></span>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}
//保存超速设置
function speed_add(devicesId){

	var re ="";
	if(typeof devicesId != "undefined" && devicesId!=null && devicesId!=""){
		re = devicesId;
		 
	}
	else {
	   
		var temp = getDevids();
		 
		var size = temp.length;
		for(var i=0;i<size;i++){
			re +=temp[i]+"|";
		}
		if(re.length>0){
			re = re.substring(0,re.length-1);
		}
	}
	if(re == null || re == ""){
		//alert("请选择终端");
		return;
	}	
	
	var isSet=GetSpeedRadioValue("isSet");
  	var methodPara= "create";
  	
  	//var speed_deviceIs = getDevids();
	var continuedTime ="";// document.getElementById("continuedTime").value;
    var alarmSpeed;
    var spedd ;
    
  	//alert(isSet);
    //取消报警
  
    if(isSet == 0 )
  	{		 
     	methodPara="delete";
		alarmSpeed = "0";
     }
	 //报警
	 else if(isSet == 1  ){
	 	  alarmSpeed = document.getElementById("alarmSpeed").value;
	  	if(alarmSpeed<30||alarmSpeed>120){
	  	    alert("超速设置范围30-120");
	  	    return;
	  	}
	  	spedd = parseInt(alarmSpeed)
	 	//if(speed_deviceIs == ""){
     	//	alert("请选择终端");
     	//	return ;
     	//}
		if( alarmSpeed == ""){
	 		alert("请输入超速值");
     			return ;
	 	}    	
	 	if (spedd <= 0 ){
    		alert("请输入大于0的速度值！");
    		return;
    	}
    	if (spedd>255){
    		alert("最大速度值不能超过255！");
    		return;
    	}
	 }
	
  	var path =getAppPath()+"/alarm/speedAlarm.do?method="+methodPara+"&deviceIds="+re+"&continuedTime="+continuedTime+"&alarmSpeed="+alarmSpeed;
		
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", path, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			speed_parseResp(xmlHttp.responseXML);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		xmlHttp.send(null);
	}, 10);
	//隐藏互动div
	hudong_div_close();

}

//超速报警请求返回状态
function speed_parseResp(oXmlDoc){
	if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	var c = oXmlDoc.getElementsByTagName("c")[0].firstChild.nodeValue;
	var d = oXmlDoc.getElementsByTagName("d")[0].firstChild.nodeValue;
	var speed_content = "超速报警设置";
	
		////redo
	var pl_content = "上报频率设置";
	if(c!=null && c =="0"){
		speed_content += "成功";
	}else{
		//alert("返回码："+c+"\r\n返回描述："+d);
		speed_content += "失败";
	}
	
	//在消息框加提示信息
	document.getElementById("tip").style.display="block";
	tip_con_show();
	var old_content = document.getElementById("tip_con").innerHTML;
	if(old_content!=null && old_content!=""){
		var pl_split = old_content.split("<BR>");
		if(pl_split.length==4){
			old_content = pl_split[0]+"<BR>"+pl_split[1]+"<BR>"+pl_split[2];
		}
	}
	document.getElementById("tip_con").innerHTML=speed_content+"<BR>"+old_content;
	
}

var termNameStr = "";
//断油断电设置请求div
function oil_set(){
	termNameStr = "";
	var re = "";
	var objs = getCheckObjects(false);
	if(objs != null)
	    s = objs.length;
	if(s == 0){
		alert("请选择终端！");
        return;
	}
	
    for(var i=0; i < s; i++){
   		var obj = objs[i];
		if(!obj.folder){
		    var type = obj.expend1;
			var realid = obj.id.substring(13);
		    if(type == "0"){
				alert("LBS终端无法设置断油断电功能，请重新选择！");
				return;
			}
			re += realid + "|";
		}
	}

    /**
	var temp = getDevids();
	//alert("temp===: " + temp);
	if(temp == null || temp == ""){
		//alert("请选择终端");
		return;
	}
	var size = temp.length;
	for(var i=0;i<size;i++){
		re += temp[i]+"|";
	}
	*/

	if(re.length>0){
		re = re.substring(0,re.length-1);
	}
	
  	var divTitle = "断油断电设置";
	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\">接收终端：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\">";
    
	var tmpTerm = re.split("|");
	for(var i=0;i<tmpTerm.length;i++){	
		hd_node = getNodeByRealId(tmpTerm[i]);
		var termName;
		if(hd_node){
			termName = hd_node.text;
			termNameStr += termName + ",";
		}
		if(i==(tmpTerm.length-1)){
  	        inner += termName;
		}else{
		    inner += termName+"&#13;&#10;";
		}
	}
	termNameStr = termNameStr.substring(0,termNameStr.length-1);
	inner += "</td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\">设置/取消：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\"><input type=\"radio\"  checked value=\"1\" name=\"type1\"/>断开油电路&nbsp;&nbsp;&nbsp;<input type=\"radio\"  value =\"0\" name=\"type1\">恢复油电路</td>";
  	inner += "</tr>";

  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"设 置\" class=\"btn\" onclick='oil_add();'/>&nbsp;&nbsp;";
  	inner += "<input name=\"\" type=\"button\" value=\"取 消\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}
//提交断油断电设置
function oil_add(){
	var commit = window.confirm("再次确认下发断油断电指令");
	if(!commit){
		return;
	}
  	var methodPara= "create";
  	var oilSetType ="";
  	 var otype = document.getElementsByName("type1");
  	 if (otype[1].checked){
  	 	oilSetType = "0";
  	 }else {
  	 	oilSetType = "1";
  	 }
  	 // alert(oilSetType);
  	var speed_deviceIs = getDevids();	
  	var path =getAppPath()+"/oil/oilElecControl.do?method="+methodPara+"&deviceIds="+speed_deviceIs+"&type="+oilSetType;
		
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", path, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			oil_parseResp(xmlHttp.responseXML);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		 xmlHttp.send(null);
	}, 10);
	//隐藏互动div
	hudong_div_close();

}

//断油断电设置请求返回状态
function oil_parseResp(oXmlDoc){
	if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	var c = oXmlDoc.getElementsByTagName("c")[0].firstChild.nodeValue;
	var d = oXmlDoc.getElementsByTagName("d")[0].firstChild.nodeValue;
	var ret_content = termNameStr + "断油断电设置";
	

	if(c!=null && c =="0"){
		ret_content += "成功";
	}else{
		//alert("返回码："+c+"\r\n返回描述："+d);
		ret_content += "失败";
	}
	
	//在消息框加提示信息
	document.getElementById("tip").style.display="block";
	tip_con_show();
	var old_content = document.getElementById("tip_con").innerHTML;
	if(old_content!=null && old_content!=""){
		var pl_split = old_content.split("<BR>");
		if(pl_split.length==4){
			old_content = pl_split[0]+"<BR>"+pl_split[1]+"<BR>"+pl_split[2];
		}
	}
	document.getElementById("tip_con").innerHTML=ret_content+"<BR>"+old_content;
	
}


  //超速报警获得radio值
  function GetSpeedRadioValue(RadioName){
    var obj;    
    obj=document.getElementsByName(RadioName);
    if(obj!=null){
        var i;
        for(i=0;i<obj.length;i++){
            if(obj[i].checked){
                return obj[i].value;            
            }
        }
    }
    return null;
}
//超速报警
function _speedchange1(){
		document.getElementById("alarmSpeed").value="0";
		//document.getElementById("continuedTime").value="";
		//document.getElementById("continuedTime").readOnly = true;
		document.getElementById("alarmSpeed").readOnly = true;	

}
//超速报警   
function _speedchange2(){
   		//document.getElementById("continuedTime").readOnly = false;
		document.getElementById("alarmSpeed").readOnly = false;	
}

//跳转页面
function jump(url){
		var iHeight = 600;
		var iWidth = 800
	  	var iTop = (window.screen.availHeight-30-iHeight)/2;       //获得窗口的垂直位置;
  		var iLeft = (window.screen.availWidth-10-iWidth)/2;           //获得窗口的水平位置;
		var win = window.open(url,'newwindow', 'height='+iHeight+', width='+iWidth+', top='+iTop+', left='+iLeft+', toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no');
		win.focus();
	}
	
	//跳转页面
function jump1(url){
		var iHeight = 600;
		var iWidth = 800
	  	var iTop = (window.screen.availHeight-30-iHeight)/2;       //获得窗口的垂直位置;
  		var iLeft = (window.screen.availWidth-10-iWidth)/2;           //获得窗口的水平位置;
		window.open(url,'newwindow', 'height='+iHeight+', width='+iWidth+', top='+iTop+', left='+iLeft+', toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');
	}
function lbsLocate(){
	pl_deviceIs = getDevids();
	if(pl_deviceIs == null || pl_deviceIs == ""){
		//alert("未选择终端");
		return;
	}
	var methodPara = "getPosition";
	
	var path =getAppPath()+"/lbs/lbs.do?method="+methodPara+"&called="+pl_deviceIs;
	
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", path, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			lbs_parseResp(xmlHttp.responseXML);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		 xmlHttp.send(null);
	}, 10);
}
function lbs_parseResp(oXmlDoc){
	if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	//alert(oXmlDoc);
	var x = oXmlDoc.getElementsByTagName("x")[0].firstChild.nodeValue;
	var y = oXmlDoc.getElementsByTagName("y")[0].firstChild.nodeValue;
	var called = oXmlDoc.getElementsByTagName("s")[0].firstChild.nodeValue;
	var lbs = new lbsAlarmObject();
	var currentTime = oXmlDoc.getElementsByTagName("e")[0].firstChild.nodeValue;
	//alert("x:"+x+"-y:"+y);
	
	var urlAndName = getTerminalIconAndName(called);
	var imgurl = urlAndName.split(",")[0];
	var tmName =  urlAndName.split(",")[1];
	lbs.deviceid=called;
	lbs.currentTime=currentTime;
	lbs.jmx = x;
	lbs.jmy = y;
	lbs.tmName=tmName;
	lbs.imgurl=imgurl;
	addLbsAlarmPOI(lbs);
}

function lbsAlarmObject(){
	this.deviceid="";
	this.currentTime = "";
	this.jmx="";
	this.jmy="";
	this.imgurl="";
	this.tmName="";
}
//标注LBS报警
	function addLbsAlarmPOI(lbs){
	var imgurl = WebRoot+"images/userface/image21.gif"		 
	var sContent = "名称："+lbs.tmName+"<br>";
	sContent += "卡号："+lbs.deviceid+"<br>";
	sContent += "时间："+lbs.currentTime+"<br>";
	addMarkerToMainMap(lbs.deviceid,lbs.deviceid,lbs.jmx,lbs.jmy,lbs.tmName,sContent,imgurl,true);
	
	}
	
	function inputNumberOnly(){
	if ( !(((window.event.keyCode >= 48) && (window.event.keyCode <= 57))
	|| (window.event.keyCode == 13) || (window.event.keyCode == 46)
	|| (window.event.keyCode == 45))){
		window.event.keyCode = 0 ;
}
	
}