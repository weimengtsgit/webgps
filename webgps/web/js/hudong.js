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
//紧急报警，只实现了山东cellid定位
function hd_alarm(){
	re = getDevids();
	if(re == null || re == ""){
		//alert("请选择终端");
		return;
	}
	if(re.length!=1){
		alert("只能选择一个终端");
		return;
	}  
	called = re;    
	node = getNodeByRealId(called);
	typeCode=node.value;
	var xmlHttp = XmlHttp.create();
	//alert(xmlHttp);
	xmlHttp.open("GET", getAppPath()+"/lbs/lbs.do?method=getPosition&called="+called+"&typeCode="+typeCode, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			//alert(xmlHttp.responseText);
			hd_parseRespXy(xmlHttp.responseXML);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		xmlHttp.send(null);
	}, 10);
}
//解析紧急报警返回x、y、错误码、错误描述
function hd_parseRespXy(oXmlDoc){
	// there is one extra level of tree elements
	if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	if(oXmlDoc.getElementsByTagName("s")!=null && oXmlDoc.getElementsByTagName("s").length==1){
		var c = oXmlDoc.getElementsByTagName("c")[0].firstChild.nodeValue;
		var d = oXmlDoc.getElementsByTagName("d")[0].firstChild.nodeValue;
		var s = oXmlDoc.getElementsByTagName("s")[0].firstChild.nodeValue;
		var x = oXmlDoc.getElementsByTagName("x")[0].firstChild.nodeValue;
		var y = oXmlDoc.getElementsByTagName("y")[0].firstChild.nodeValue;
		var t = oXmlDoc.getElementsByTagName("t")[0].firstChild.nodeValue;
		//查找终端
		hd_node = getNodeByRealId(s);
		var termName = "";
		var imageUrl = null;
		if(hd_node){
			termName = hd_node.text;
			imageUrl = WebRoot+ hd_node.icon.replace("../","/");
			
		}
		addMarker(s,x,y,termName,"LBS定位",imageUrl,true,null);
		
		var hd_content = termName + t;
		if(c!=null && c =="0"){
			hd_content += "成功";
		}else{
			//alert("返回码："+c+"\r\n返回描述："+d);
			hd_content += "失败";
		}
		//在消息框加提示信息
		document.getElementById("tip").style.display="block";
		tip_con_show();
		var old_content = document.getElementById("tip_con").innerHTML;
		if(old_content!=null && old_content!=""){
			var hd_split = old_content.split("<BR>");
			if(hd_split.length==4){
				old_content = hd_split[0]+"<BR>"+hd_split[1]+"<BR>"+hd_split[2];
			}
		}
		document.getElementById("tip_con").innerHTML=hd_content+"<BR>"+old_content;
	}else{
		alert("返回异常");
	}
}
//展现发送短信请求div
function hd_message(devicesId){
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

	//alert(re);
	var divTitle = "短信调度";
  	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\">接收终端：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\"><textarea name=\"termlist\" cols=\"40\" rows=\"10\" class=\"tip_area\" readonly>";
	var tmpTerm = re.split("|");
	var simcards = "";
	for(var i=0;i<tmpTerm.length;i++){
		hd_node = getNodeByRealId(tmpTerm[i]);
		if(hd_node){
			termName = hd_node.text;
			simcard = hd_node.expend5;
		}
		if(i==(tmpTerm.length-1)){
  	        inner += termName;
  	        simcards += simcard;
		}else{
		    inner += termName+"&#13;&#10;";
		    simcards += simcard+"|";
		}
	}
	inner += "</textarea></td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\">短信内容：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\"><textarea name=\"textarea\" class=\"tip_area\"></textarea></td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"发 送\" class=\"btn\" onclick=\"hd_sendMessage('"+simcards+"');\"/>&nbsp;&nbsp;";
  	inner += "<input name=\"\" type=\"button\" value=\"取 消\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	inner += "</div>";
  	hudong_div_show(divTitle, inner);
}



//发送手机短信请求
function hd_m_attemper(devicesId){
	
	var re ="";
	if(typeof devicesId != "undefined" && devicesId!=null && devicesId!=""){
		re = devicesId;
	}
	else {
		var temp = getLbsDevids();
		if(temp)
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

	//alert(re);
	var divTitle = "手机短信";
  	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\">接收终端：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\"><textarea name=\"termlist\" cols=\"40\" rows=\"10\" class=\"tip_area\" readonly>";
	var tmpTerm = re.split("|");
	for(var i=0;i<tmpTerm.length;i++){
		hd_node = getNodeByRealId(tmpTerm[i]);
		if(hd_node){
			termName = hd_node.text;
		}
		if(i==(tmpTerm.length-1)){
  	        inner += termName;
		}else{
		    inner += termName+"&#13;&#10;";
		}
	}
	
	
	
	
	inner += "</textarea></td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\">短信内容：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\"><textarea name=\"textarea\" class=\"tip_area\"></textarea></td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"发 送\" class=\"btn\" onclick=\"hd_lbsrk('"+re+"');\"/>&nbsp;&nbsp;";
  	inner += "<input name=\"\" type=\"button\" value=\"取 消\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	inner += "</div>";
  	hudong_div_show(divTitle, inner);
}

//展现发送语音请求div
function hd_voice(userContact,deviceId,isMenu){
	//alert(userContact+";"+deviceId+";"+isMenu);
	var re ="";
	if(typeof deviceId != "undefined" && deviceId!=null && deviceId!=""){
		re = deviceId; 
	}
	else{
		//re = getDevids();
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
		return;
	}
	//alert(re);
	var divTitle ="语音调度";
	if(isMenu){
		divTitle ="紧急呼叫";
	}else{
		divTitle ="语音调度";
	}
  	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\">接收终端：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td align=\"left\"><textarea name=\"termlist\" cols=\"40\" rows=\"10\" class=\"tip_area\">";
	var tmpTerm = re.split("|");
	var simcards = "";
	for(var i=0;i<tmpTerm.length;i++){
		hd_node = getNodeByRealId(tmpTerm[i]);
		if(hd_node){
			termName = hd_node.text;
			simcard = hd_node.expend5;
		}
		if(i==(tmpTerm.length-1)){
  	        inner += termName;
  	        simcards += simcard;
		}else{
		    inner += termName+"&#13;&#10;";
		    simcards += simcard+"|";
		}
	}
	inner += "</textarea></td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\">主叫号码：<span style=\" color:#ff0000;\">＊</span></td>";
  	inner += "<td colspan=\"2\" align=\"left\"><input name=\"caller\" type=\"text\" class=\"input1\" value="+userContact+" /></td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"发 送\" class=\"btn\" onclick=\"hd_sendVoice('"+simcards+"');\"/>&nbsp;&nbsp;";
  	inner += "<input name=\"\" type=\"button\" value=\"取 消\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}

function multipleSendMessage(deviceIds){
    if(deviceIds == "" || deviceIds == null){
		if(deviceIds.split("|").length == 0){ 
	        alert("没有选择接收终端！");
		    return;
		}
	}
    var callIdArraya = deviceIds.split("|");
	var content = document.getElementById("textarea").value;
	if(content==null||content == ""){
		alert("短信内容不能为空");
		return;
	}
	if(content.length>200){
		alert("短信内容长度不能超过200");
		return;
	}
	for(var i=0;i<callIdArraya.length;i++){
	    hd_sendMessage(callIdArraya[i],content);
	}
}

//发送短信请求
function hd_sendMessage(re){
	if(re == null || re == ""){
		//alert("未选择终端");
		return;
	}
	var called = re;
	var content = document.getElementById("textarea").value;
	if(content==null||content == ""){
		alert("短信内容不能为空");
		return;
	}
	if(content.length>200){
		alert("短信内容长度不能超过200");
		return;
	}
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", getAppPath()+"/ivr/ivr.do?method=playConverseVoice&caller=13800000000&called="+called+"&content="+content, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {			
			hd_parseResp(xmlHttp.responseXML);
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


//手机短信数据插入数据库请求
function hd_lbsrk(re){

	if(re == null || re == ""){
		//alert("未选择终端");
		return;
	}
	var called = re;
	var content = document.getElementById("textarea").value;
	if(content==null||content == ""){
		alert("短信内容不能为空");
		return;
	}
	if(content.length>200){
		alert("短信内容长度不能超过200");
		return;
	}
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", getAppPath()+"/lbsatt/lbsatt.do?method=insertDB&caller=13800000000&called="+called+"&content="+content, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			hd_parse_att(xmlHttp.responseXML);
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
function hd_parseResp(oXmlDoc){
	// there is one extra level of tree elements
	if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	var t = oXmlDoc.getElementsByTagName("t")[0].firstChild.nodeValue;
	var cr = oXmlDoc.getElementsByTagName("cr")[0].firstChild.nodeValue;
	var cd = oXmlDoc.getElementsByTagName("cd")[0].firstChild.nodeValue;
	var c = oXmlDoc.getElementsByTagName("c")[0].firstChild.nodeValue;
	var d = oXmlDoc.getElementsByTagName("d")[0].firstChild.nodeValue;
	////redo
	//查找终端
	var termName = "";
	if(cd!=null){
		var simcards = cd.split("|");
		for(var i=0;i<simcards.length;i++){
			var simcard = simcards[i];
			hd_node = getNodeByRealId(simcard);
			if(hd_node){
				termName += hd_node.text+",";
			}
		}
	}
	
	if(termName.length>0){
		termName = termName.substring(0,termName.length-1);
	}
	var hd_content = termName + t;
	if(c!=null && c =="0"){
		hd_content += "成功";
	}else{
		//alert("返回码："+c+"\r\n返回描述："+d);
		hd_content += "失败";
	}
	//在消息框加提示信息
	document.getElementById("tip").style.display="block";
	addMapOptLog(hd_content);
	/**
	tip_con_show();
	var old_content = document.getElementById("tip_con").innerHTML;
	if(old_content!=null && old_content!=""){
		var hd_split = old_content.split("<BR>");
		if(hd_split.length==4){
			old_content = hd_split[0]+"<BR>"+hd_split[1]+"<BR>"+hd_split[2];
		}
	}
	document.getElementById("tip_con").innerHTML=hd_content+"<BR>"+old_content;
	*/
}


function hd_parse_att(oXmlDoc){
	// there is one extra level of tree elements
	if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	
	var c = oXmlDoc.getElementsByTagName("c")[0].firstChild.nodeValue;
	
	if(c==1)
	{
	
	
	
	//	var t = oXmlDoc.getElementsByTagName("t")[0].firstChild.nodeValue;
		var cr = oXmlDoc.getElementsByTagName("cr")[0].firstChild.nodeValue;
		var cd = oXmlDoc.getElementsByTagName("cd")[0].firstChild.nodeValue;
		
	//	var d = oXmlDoc.getElementsByTagName("d")[0].firstChild.nodeValue;
		////redo
		//查找终端
		var termName = "";
		if(cd!=null){
			var simcards = cd.split("|");
			for(var i=0;i<simcards.length;i++){
				var simcard = simcards[i];
				hd_node = getNodeByRealId(simcard);
				if(hd_node){
					termName += hd_node.text+",";
				}
			}
		}
	
		
		if(termName.length>0){
			termName = termName.substring(0,termName.length-1);
		}
		var hd_content = termName 
		
			hd_content += "短信发送成功";
		
		//在消息框加提示信息
		document.getElementById("tip").style.display="block";
		addMapOptLog(hd_content);
	}
	else{
	
		document.getElementById("tip").style.display="block";
		addMapOptLog("短信发送失败！");
	
	}

}





function multipleSendVoic(deviceIds){
    var callIdArraya = deviceIds.split("|");
	var caller = document.getElementById("caller").value;
	if(caller==null||caller == ""){
		alert("主叫号码不能为空");
		return;
	}
	if (caller.length !=11&&caller.length !=12){
        alert("请输入11位或12位");
        return;
    }else if(!caller.match(/^\d*$/)){
        alert("只能输入数字");
		return;
	}
	for(var i=0;i<callIdArraya.length;i++){
	    hd_sendVoice(callIdArraya[i],caller);
	}
}

//发送语音调度请求
function hd_sendVoice(re){
	if(re == null || re == ""){
		//alert("未选择终端");
		return;
	}
	//size = re.length;
	var called = re;
	var caller = document.getElementById("caller").value;
	if(caller==null||caller == ""){
		alert("主叫号码不能为空");
		return;
	}
	if (caller.length !=11&&caller.length !=12){
        alert("请输入11位或12位");
        return;
    }else if(!caller.match(/^\d*$/)){
        alert("只能输入数字");
		return;
	}
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", getAppPath()+"/ivr/ivr.do?method=playVoice&recordflag=0&caller="+caller+"&called="+called, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			hd_parseResp(xmlHttp.responseXML);
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

//发送语音调度请求
function hd_JJCallVoiceBack(caller,re){
	if(re == null || re == ""){
		//alert("未选择终端");
		return;
	}
	//size = re.length;
	var called = re;
	if(caller==null||caller == ""){
		alert("主叫号码不能为空");
		return;
	}
	if (caller.length !=11){
        alert("请输入11位");
        return;
    }else if(!caller.match(/^\d*$/)){
        alert("只能输入数字");
		return;
	}
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", getAppPath()+"/ivr/ivr.do?method=playVoice&recordflag=0&caller="+caller+"&called="+called, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			hd_parseResp(xmlHttp.responseXML);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		xmlHttp.send(null);
	}, 10);
	alert("已经对'"+re+"'发送紧急呼叫");
}

//车辆参数信息
function find_paramter_info(deviceId){
	if(deviceId==null||deviceId == ""){
		alert("查询号码不能为空");
		return;
	}
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", getAppPath()+"/paramsetting/paramterQuery.do?method=queryParamter&deviceId="+deviceId, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			paramter_info_parseResp(xmlHttp.responseXML,deviceId);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		xmlHttp.send(null);
	}, 10);
	//在消息框加提示信息
	//document.getElementById("tip").style.display="block";
	//tip_con_show();
	//var old_content = document.getElementById("tip_con").innerHTML;
	//if(old_content!=null && old_content!=""){
	//	var hd_split = old_content.split("<BR>");
	//	if(hd_split.length==4){
	//		old_content = hd_split[0]+"<BR>"+hd_split[1]+"<BR>"+hd_split[2];
	//	}
	//}
	//document.getElementById("tip_con").innerHTML="查询参数信息"+"<BR>"+old_content;
}
function paramter_info_parseResp(oXmlDoc,deviceId){
	if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	var area = oXmlDoc.getElementsByTagName("area").length>0?oXmlDoc.getElementsByTagName("area")[0]:null;
	var frequency = oXmlDoc.getElementsByTagName("frequency").length>0?oXmlDoc.getElementsByTagName("frequency")[0]:null;
	var speed = oXmlDoc.getElementsByTagName("speed").length>0?oXmlDoc.getElementsByTagName("speed")[0]:null;
	var areaId=null;
	var areaType=null;
	var areaXY=null;
	var frenqucyId=null;
	var frenqucyValue=null;
	var speedId=null;
	var speedValue=null;
	var tname = getTermNameById(deviceId);
	if(area!=null){
		areaId=area.getElementsByTagName("id")[0].firstChild.nodeValue;
		areaType=area.getElementsByTagName("type")[0].firstChild.nodeValue;
		areaXY=area.getElementsByTagName("xy")[0].firstChild.nodeValue;
	}
	if(frequency!=null){
		frenqucyId=frequency.getElementsByTagName("id")[0].firstChild.nodeValue;
		frenqucyValue=frequency.getElementsByTagName("value")[0].firstChild.nodeValue;
	}
	if(speed!=null){
		speedId=speed.getElementsByTagName("id")[0].firstChild.nodeValue;
		speedValue=speed.getElementsByTagName("value")[0].firstChild.nodeValue;
	}
	//显示
	var divTitle =tname +" 车辆参数信息";
  	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
  	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\" width=\"40%\">区域报警：</td>";
  	inner += "<td  colspan=\"2\" align=\"center\">"+(areaId==null?"未设置":(areaType=='0'?"出区域":"进区域"))+"</td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\">上报频率：</td>";
  	inner += "<td  colspan=\"2\" align=\"center\">"+(frenqucyId==null?"未设置":frenqucyValue+" 秒")+"</td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td align=\"right\" valign=\"top\">速度阀值：</td>";
  	inner += "<td  colspan=\"2\" align=\"center\">"+(speedId==null?"未设置":speedValue+" km/h")+"</td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}

//手机参数信息
function find_phone_info(deviceId){
	if(deviceId==null||deviceId == ""){
		alert("查询号码不能为空");
		return;
	}
	var xmlHttp = XmlHttp.create();
	xmlHttp.open("GET", getAppPath()+"/simple/simpleRule.do?actionMethod=findTermSimpleRule&deviceId="+deviceId, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			phone_info_parseResp(xmlHttp.responseXML,deviceId);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		xmlHttp.send(null);
	}, 10);
}
function phone_info_parseResp(oXmlDoc,deviceId){
	if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	var simple = oXmlDoc.getElementsByTagName("simple").length>0?oXmlDoc.getElementsByTagName("simple")[0]:null;
	var attendance = oXmlDoc.getElementsByTagName("attendance").length>0?oXmlDoc.getElementsByTagName("attendance"):null;
	//显示
	var tname = getTermNameById(deviceId);
	var divTitle =tname +" 手机参数信息";
  	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"0\" bgcolor=\"#000000\">";
  	inner += "<tr bgcolor=\"#e6ecf4\">";
  	inner += "<td align=\"center\" valign=\"top\">参数类型</td>";
  	inner += "<td align=\"center\" valign=\"top\">规则名称</td>";
  	inner += "<td align=\"center\" valign=\"top\">生效期</td>";
  	inner += "<td align=\"center\" valign=\"top\">规则类型</td>";
  	inner += "<td align=\"center\" valign=\"top\">频率</td>";
  	//inner += "<td align=\"center\" valign=\"top\">规则内容</td>";
  	inner += "</tr>";
  	inner += "<tr bgcolor=\"#e6ecf4\">";
  	inner += "<td align=\"center\">采样规则</td>";
  	if(simple==null){
  		inner += "<td colspan=5>未设置</td>";
  	}else{
  		var id=simple.getElementsByTagName("id")[0].firstChild.nodeValue;
  		var name=simple.getElementsByTagName("name")[0].firstChild.nodeValue;
  		var effectPeriod=simple.getElementsByTagName("effectPeriod")[0].firstChild.nodeValue;
		effectPeriod = effectPeriod.replace("1","一");
		effectPeriod = effectPeriod.replace("2","二");
		effectPeriod = effectPeriod.replace("3","三");
		effectPeriod = effectPeriod.replace("4","四");
		effectPeriod = effectPeriod.replace("5","五");
		effectPeriod = effectPeriod.replace("6","六");
		effectPeriod = effectPeriod.replace("7","日");
		raRegExp = new RegExp(",","g");
		effectPeriod = effectPeriod.replace(raRegExp,"、");
		effectPeriod = "周"+effectPeriod;
  		var type=simple.getElementsByTagName("type")[0].firstChild.nodeValue;
  		var content=simple.getElementsByTagName("content")[0].firstChild.nodeValue;
  		var interval=simple.getElementsByTagName("interval")[0].firstChild.nodeValue;
  		inner += "<td align=\"center\">"+name+"</td>";
  		inner += "<td align=\"center\">"+effectPeriod+"</td>";
  		if(type=="0"){
  			inner += "<td align=\"center\">固定频率采样</td>";
  			inner += "<td align=\"center\">"+interval+"分钟</td>";
  		}else if(type=="1"){
  			inner += "<td align=\"center\">时间段固定频率</td>";
  			inner += "<td align=\"center\">"+interval+"分钟</td>";
  		}else if(type=="2"){
  			inner += "<td align=\"center\">固定时间点采样</td>";
  			inner += "<td align=\"center\">"+content+"</td>";
  		}
  		//inner += "<td align=\"center\">"+content+"</td>";
  	}
  	inner += "</tr>";

  	if(attendance==null){
  		inner += "<tr bgcolor=\"#e6ecf4\">";
  		inner += "<td align=\"center\">考勤规则</td>";
  		inner += "<td colspan=5>未设置</td>";
  		inner += "</tr>";
  	}else{
  		for(var i=0;i<attendance.length;i++){
	  		var id=attendance[i].getElementsByTagName("id")[0].firstChild.nodeValue;
	  		var name=attendance[i].getElementsByTagName("name")[0].firstChild.nodeValue;
	  		var effectPeriod=attendance[i].getElementsByTagName("effectPeriod")[0].firstChild.nodeValue;
	  		effectPeriod = effectPeriod.replace("1","一");
			effectPeriod = effectPeriod.replace("2","二");
			effectPeriod = effectPeriod.replace("3","三");
			effectPeriod = effectPeriod.replace("4","四");
			effectPeriod = effectPeriod.replace("5","五");
			effectPeriod = effectPeriod.replace("6","六");
			effectPeriod = effectPeriod.replace("7","日");
			raRegExp = new RegExp(",","g");
			effectPeriod = effectPeriod.replace(raRegExp,"、");
			effectPeriod = "周"+effectPeriod;
	  		var type=attendance[i].getElementsByTagName("type")[0].firstChild.nodeValue;
	  		var interval=attendance[i].getElementsByTagName("interval")[0].firstChild.nodeValue;
	  		var content=attendance[i].getElementsByTagName("content")[0].firstChild.nodeValue;
	  		inner += "<tr bgcolor=\"#e6ecf4\">";
	  		if(i==0)
	  			inner += "<td align=\"center\" rowspan=\""+attendance.length+"\">考勤规则</td>";
	  		inner += "<td align=\"center\">"+name+"</td>";
	  		inner += "<td align=\"center\">"+effectPeriod+"</td>";
	  		inner += "<td align=\"center\">-</td>";
	  		//inner += "<td align=\"center\">-</td>";
	  		inner += "<td align=\"center\">"+content+"</td>";
	  		inner += "</tr>";
  		}
  	}
  	inner += "</table>";
  	phone_hudong_div_show(divTitle, inner);
}


//------------------------------当日里程------------------------------//

//当日里程信息
function find_curDay_lc(deviceId){
	if(deviceId==null||deviceId == ""){
		alert("查询号码不能为空");
		return;
	}
	var xmlHttp = XmlHttp.create();
	 
	xmlHttp.open("GET", getAppPath()+"tongji/tongji.do?method=tongjiCurDayDistance&deviceId="+deviceId, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			curDayLC_parseResp(xmlHttp.responseText,deviceId);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		xmlHttp.send(null);
	}, 10);
	 
}
function curDayLC_parseResp(oXmlDoc,deviceId){
 
	if(oXmlDoc==null){
		alert("未返回");
		return;
	}
	 var tmpObj = eval('(' + oXmlDoc + ')');
	 var disObj = tmpObj.curDayDistanceList;
     var lclistLeng = disObj.length;
     
	//显示
	var divTitle ="当日里程信息";
	var tname = getTermNameById(deviceId);
  	inner = "<table width=\"80%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
  	
  	inner += "<tr>";
  	inner += "<td align=\"center\" valign=\"top\" width=\"10%\">车牌号</td>";
  	inner += "<td align=\"center\" valign=\"top\" width=\"10%\">当日里程数</td>";
  	inner += "</tr>";
  	//for (var i=0; i<lclistLeng; i++){
  	inner += "<tr>";
  	inner += "<td align=\"center\" valign=\"top\" width=\"10%\">"+tname+"</td>";   //disObj[i].deviceId
  	inner += "<td align=\"center\" valign=\"top\" width=\"10%\">"+(lclistLeng==0?0:disObj[0].distance)+"&nbsp;公里</td>";//
  	inner += "</tr>";
 	//}
  	inner += "</table>";
  	inner += "</div>";
  	hudong_div_show(divTitle, inner);
}

//提示不在实时定位，让用户选择是否使用最近位置
function showNotTrack(termName, deviceId, locateType){
	var divTitle ="提示信息";
	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
  	inner += "<tr>";
  	inner += "<td colspan=\"3\" align=\"center\">终端 "+termName+" 未进行实时定位，是否执行最近位置操作？</td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td colspan=\"2\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"执 行\" class=\"btn\" onclick=\"recentPosition('"+deviceId+"','"+locateType+"');\"/>&nbsp;&nbsp;";
  	inner += "<input name=\"\" type=\"button\" value=\"取 消\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}
//最近位置
function recentPosition(){
	//alert(deviceId + " " + locateType);
	hudong_div_close();
	var lbsDeviceIds = new Array();
	var gpsDeviceIds = new Array();
	if(locateType == "0"){
	    lbsDeviceIds[lbsDeviceIds.length] = deviceId;
	}else if(locateType == "1"){
	    gpsDeviceIds[gpsDeviceIds.length] = deviceId;
	}
	var selectObj = getNodeByRealId(deviceId);
	selectObj.setDesc("<img src='maps/images/locating.gif' alt='跟踪中'>","inline");
    closestLocMonitor(gpsDeviceIds,lbsDeviceIds);
}

//显示更多信息内容：
function showMoreInfo(){
	var divTitle ="操作信息";
	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
	if(mapOptLogArray.length == mapOptLogTime.length){
	    for(var i=(mapOptLogArray.length-1);i>=0;i--){
		    tmpContent = mapOptLogArray[i];
			tmpTime = mapOptLogTime[i];
            inner += "<tr>";
  	        inner += "<td align=\"left\">&nbsp;&nbsp;&nbsp;"+tmpContent+"</td>";
  	        inner += "<td colspan=\"2\" align=\"right\">"+tmpTime+"&nbsp;&nbsp;&nbsp;</td>";
  	        inner += "</tr>";
		}
	}
  	inner += "<tr>";
  	inner += "<td colspan=\"3\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"关 闭\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}

//显示终端类型详细信息：
function showTermTypeDetail(name,desc,iconpath){
	var divTitle = name;
  	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
    inner += "<tr>";
  	inner += "<td colspan=\"3\" align=\"left\">"+desc+"</td>";
  	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td colspan=\"3\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"关 闭\" class=\"btn\" onclick='hudong_div_close();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(name, inner);
}
function deleteSelectedLayer(){
    var layers=document.getElementsByName("layer");
    if (layers != "null" && layers != "" && 'undefined' != typeof layers){
    if(layers.length){
     for(var i = 0; i < layers.length; i++){
       layers[i].checked = false;
     }
    }
    else{
     layers.checked = false;
    }
    }
    selectLayerStatus="";
}

//更改图层显示状态和图层在启动时是否可加载属性
function ajaxLayer(urlpar,layer_load){
	
	var xmlHttp;
	var browser = navigator.appName;
    if(browser == "Microsoft Internet Explorer"){
		try{
			xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		}catch (e){
			try{
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			}catch (e2){
				xmlHttp = false;
			}
		}
    }else{
       	xmlHttp = new XMLHttpRequest();
    }
	var locs;
	xmlHttp.onreadystatechange=function(){
        if (xmlHttp.readyState == 4) {
            if(xmlHttp.status==200){ 
           	   processLayer(xmlHttp.responseXML);
				
		    }
	    }
	}
	var sendurl="./poi/poimanage.do?method=updateLayers&ids="+urlpar+"&load="+layer_load
	xmlHttp.open("GET",sendurl,true);
	xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xmlHttp.send(null);	
}

function processLayer(xml){
 var flag=xml.getElementsByTagName("c")[0].firstChild.nodeValue;
	if(flag==1){
		var layers=document.getElementsByName("layer");
    	for(var i=0;i<layers.length;i++){
  			 SetLayerVisible(layers[i].value,layers[i].checked);
 		 }		
 		 
 		 hudong_div_close();	
	}
	else{
		alert("后台数据错误")
	}
}
//初始化图层对象，根据是否启动时加载属性判断
function initLayer_hudong(){
	
	var xmlHttp;
	var browser = navigator.appName;
    if(browser == "Microsoft Internet Explorer"){
		try{
			xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		}catch (e){
			try{
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			}catch (e2){
				xmlHttp = false;
			}
		}
    }else{
       	xmlHttp = new XMLHttpRequest();
    }
	var locs;
	xmlHttp.onreadystatechange=function(){
        if (xmlHttp.readyState == 4) {
            if(xmlHttp.status==200){ 
           	   processInitLayer(xmlHttp.responseXML);
				
		    }
	    }
	}
	var sendurl="./poi/poimanage.do?method=getLayerLoad"
	xmlHttp.open("GET",sendurl,true);
	xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xmlHttp.send(null);	
}

function processInitLayer(xml){
	var load=xml.getElementsByTagName("c")[0].firstChild.nodeValue;
	if(load==1){
		loadAllPoiDatas();
	}
	else{
		loadFlagInit_hudong=1;
	
	}

}



function refreshLayer(){
	var layerids="-1";
    var layers=document.getElementsByName("layer");
    for(var i=0;i<layers.length;i++){
	    if(layers[i].checked){
	    	layerids+=","+layers[i].value
	   	 }  
    }
    var layerLoad=document.getElementsByName("layerLoad")[0].checked;
    if(layerLoad){
   	 layerLoad=1
    }
    else{
    	layerLoad=0
    }
    	ajaxLayer(layerids,layerLoad);
    
    
   // hudong_div_close();
}


function allSelected(status){
    var layers=document.getElementsByName("layer");
    if(layers.length) {
      for(var i = 0; i < layers.length; i++) {
        layers[i].checked = status;
      }
    }
    else {
      layers.checked = status;
    }
}
//SetAllLayerVisible(this.checked)
//显示图层控制
function showLayerControlList(){

    var divTitle = "图层控制";
	inner = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
		inner += "<tr>";
  	    inner += "<td align=\"left\"></td>";
  	    if(loadlayerUser==0){
  	    		inner += "<td colspan=\"2\" align=\"center\"><input type='checkbox'  name=\"layerLoad\" style='cursor:hand' >&nbsp;&nbsp;&nbsp;&nbsp;"+"是否启动时加载图层"+"</td>";
  	    	}
  	    else{
  	    	inner += "<td colspan=\"2\" align=\"center\"><input type='checkbox' checked  name=\"layerLoad\" style='cursor:hand' >&nbsp;&nbsp;&nbsp;&nbsp;"+"是否启动时加载图层"+"</td>";
  	    }
  	    inner += "</tr>";
    for(var i=0;i<layerlist.LayerList.length;i++){
	    var layerId = layerlist.LayerList[i].LayerID;
		var layerName = layerlist.LayerList[i].LayerName;		
		var visflag="";		
		var visible=layerlist.LayerList[i].IsEdit;
		if(visible==1){
			visflag="checked=true";
		}

		inner += "<tr>";
  	    inner += "<td align=\"left\"></td>";
  	    inner += "<td colspan=\"2\" align=\"left\"><input type='checkbox' "+visflag+" name=\"layer\" style='cursor:hand' value=\""+layerId+ "\">&nbsp;&nbsp;&nbsp;&nbsp;"+layerName+"</td>";
  	    inner += "</tr>";
	}
	inner += "<tr>";
	inner += "<td align=\"left\"></td>";
	inner += "<td colspan='2'><input type='checkbox' style='cursor:hand' onclick='allSelected(this.checked)' >&nbsp;&nbsp;&nbsp;&nbsp;全选  共<font color='red'>"+layerlist.LayerList.length+"</font>个图层</td>";
	inner += "</tr>";
  	inner += "<tr>";
  	inner += "<td colspan=\"3\" align=\"center\">";
  	inner += "<input name=\"\" type=\"button\" value=\"确 定\" class=\"btn\" onclick='refreshLayer();'/></td>";
  	inner += "</tr>";
  	inner += "</table>";
  	hudong_div_show(divTitle, inner);
}
//是否启动时加载全局变量
var loadlayerUser=0;
var loadFlagInit_hudong=0;

//*******************图层poi加载处理*******************//
//ajax加载图层、poi数据
function loadAllPoiDatas(){

    var xmlHttp;
	var browser = navigator.appName;
    if(browser == "Microsoft Internet Explorer"){
		try{
			xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		}catch (e){
			try{
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			}catch (e2){
				xmlHttp = false;
			}
		}
    }else{
       	xmlHttp = new XMLHttpRequest();
    }
	var locs;
	xmlHttp.onreadystatechange=function(){
        if (xmlHttp.readyState == 4) {  
            if(xmlHttp.status==200){
				parseLayerPoiDatas(xmlHttp.responseText);
		    }
	    }
	}
	xmlHttp.open("GET","../poi/poimanage.do?method=getAllPois",true);
	xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded")
	xmlHttp.send(null);
}

function parseLayerPoiDatas(jsonxml){

    if(jsonxml==null||jsonxml==""){
    alert("没有图层");
    return;
    }
    layerlist=new Layerlist();
    layerControl.setLayerList(layerlist.LayerList);
    var layerPoiObjs = eval('(' + jsonxml + ')');
    if(layerPoiObjs != null){
	    for(var i=0;i<layerPoiObjs.length-1;i++){
             layerID = layerPoiObjs[i].layerID;
             visible=layerPoiObjs[i].visible;
			 layerName = layerPoiObjs[i].layerName;
			 useStatus = layerPoiObjs[i].useStatus;
			 poiID = layerPoiObjs[i].poiID;
			 XYStr = layerPoiObjs[i].XYStr;
			 LngLatStr = layerPoiObjs[i].LngLatStr;
			 PoiName = layerPoiObjs[i].PoiName;
			 PoiDesc = layerPoiObjs[i].PoiDesc;
			 PoiType = layerPoiObjs[i].PoiType;
			 Tele = layerPoiObjs[i].Tele;
			 Address = layerPoiObjs[i].Address;
			 Keyword = layerPoiObjs[i].Keyword;
			 iconPath = layerPoiObjs[i].iconPath;
			 borderLineWidth = layerPoiObjs[i].borderLineWidth;
			 borderLineColor = layerPoiObjs[i].borderLineColor;
			 borderLineAlpha = layerPoiObjs[i].borderLineAlpha;
			 fillColor = layerPoiObjs[i].fillColor;
			 fillAlpha = layerPoiObjs[i].fillAlpha;
			 AddPoiToLayerList(layerID,visible,layerName,useStatus,poiID,XYStr,LngLatStr,PoiName,PoiDesc,PoiType,Tele,Address,Keyword,iconPath,borderLineWidth,borderLineColor,borderLineAlpha,fillColor,fillAlpha);
		}
		
		loadlayerUser=layerPoiObjs[layerPoiObjs.length-1].load;

		

	}

	if(loadFlagInit_hudong==1){

		showLayerControlList();
		
	}
	else{
		addLayers();
	}

	loadFlagInit_hudong=1;
	

}


