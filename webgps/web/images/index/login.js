// JavaScript Document
loginservice="manage/Go2mapGPSUserLogIn.do";


function $(n){
	return document.getElementById(n);	
}
function Login(){
	if(document.getElementById("UserName").value==''||document.getElementById("PWD").value==''){
		alert("�û��������������Ϊ�գ�����ȷ��д");
		return;
	}
	var lf=$("loginForm");
	lf.target="_self" //Ҳ������_self,_top,_parent,Ĭ��Ϊ_self 
	lf.action=loginservice; 
	$("PWD").value=calcMD5($("PWD").value,32);
	
	lf.submit();  
}