// JavaScript Document
loginservice="manage/Go2mapGPSUserLogIn.do";


function $(n){
	return document.getElementById(n);	
}
function Login(){
	if(document.getElementById("UserName").value==''||document.getElementById("PWD").value==''){
		alert("用户名和密码均不能为空，请正确填写");
		return;
	}
	var lf=$("loginForm");
	lf.target="_self" //也可以是_self,_top,_parent,默认为_self 
	lf.action=loginservice; 
	$("PWD").value=calcMD5($("PWD").value,32);
	
	lf.submit();  
}