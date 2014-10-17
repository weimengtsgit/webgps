var xmlHttp;
//����ն����к��Ƿ����ظ�
function checkTerminalId(){
	var url;
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
	var id = document.getElementById("deviceId").value;
	if (id==null || strlen(id)==0)
		return false;
	url = "/terminal/terminal.do?method=checkTerminalId&deviceId="+id;
	xmlHttp.open("post", url, false);
	xmlHttp.onreadystatechange = function(){
		if (xmlHttp.readyState == 4) { 
			if (xmlHttp.status == 200) { 
				var xmlReturn = xmlHttp.responseText;
				xmlHttp = null;
				
				if(xmlReturn == "GPSSN_EXIST"){
				    //alert("���к��Ѿ����ڣ������ظ���");
                    document.getElementById("gpssnMsg").style.display = "block";
				    document.getElementById("gpssnMsg").innerHTML="<FONT COLOR=RED>���к��Ѿ����ڣ������ظ���</FONT>";
				    document.getElementById("deviceId").value="";
				    document.getElementById("deviceId").focus();
			    }else{
                    document.getElementById("gpssnMsg").style.display = "none";
				    document.getElementById("gpssnMsg").innerHTML="";
			    }
		    }
		}
    }
    xmlHttp.send(null);
}

//����ն��ֻ����Ƿ����ظ�
function checkTerminalSim(){
	var url;
	var browser = navigator.appName;
    if(browser == "Microsoft Internet Explorer"){
		try{
			xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		}catch(e){
			try{
			    xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			}catch(e2){
				xmlHttp = false;
			}
		}
    }else{
       	xmlHttp = new XMLHttpRequest();
    }
	var id = document.getElementById("simcard").value;
	
	if (id==null || strlen(id)==0)
		return false;
		 
	url = "/terminal/terminal.do?method=checkTerminalSim&simcard="+id;
	xmlHttp.open("post", url, false);
	xmlHttp.onreadystatechange = function(){
		if (xmlHttp.readyState == 4) { 
			if (xmlHttp.status == 200) { 
				var xmlReturn = xmlHttp.responseText;
				xmlHttp = null;
				
				if(xmlReturn == "SIM_EXIST"){
				    //alert("���к��Ѿ����ڣ������ظ���");
                    document.getElementById("simMsg").style.display = "block";
				    document.getElementById("simMsg").innerHTML="<FONT COLOR=RED>�����Ѿ����ڣ������ظ���</FONT>";
				    document.getElementById("simcard").value="";
				    document.getElementById("simcard").focus();
			    }else {
                    document.getElementById("simMsg").style.display = "none";
				    document.getElementById("simMsg").innerHTML="";
			    }
			}
		}	
	}
    xmlHttp.send(null);
} 

//��ȡ�ն�ͼƬ·��
function getTerminalIcon(deviceid){
	var url;
	var icon ="/images/icon/4.gif";
	var browser = navigator.appName;
  
    if(browser == "Microsoft Internet Explorer"){
    	try{
	        xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		}catch(e){
			try{
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			}catch(e2){
				xmlHttp = false;
			}
		}
    }else{
       	xmlHttp = new XMLHttpRequest();
    }
	 
	if (deviceid==null || strlen(deviceid)==0)
		return false;
		 
	url = "/terminal/terminal.do?method=getTerminalById&devicId="+deviceid;
	xmlHttp.open("post", url, false);
	xmlHttp.onreadystatechange = function(){
		if (xmlHttp.readyState == 4) { 
			if (xmlHttp.status == 200) { 
				var xmlReturn = xmlHttp.responseText;
				if (xmlReturn != null){
					icon = xmlReturn;
				}
				 
			}
		}	
	}
    xmlHttp.send(null);
    return icon;
}
 

//�õ��ַ����ַ�����
function strlen(str) { 
    var len; 
    var i; 
     len = 0; 
    for (i=0;i<str.length;i++) { 
        if (str.charCodeAt(i)>255) len+=2; 
        else len++; 
     } 
    return len; 
}
