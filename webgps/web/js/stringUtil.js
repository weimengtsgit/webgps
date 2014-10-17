/*
*判断字符串是否为空	
*/
String.prototype.isEmpty = function () {
	return (this == null || this == undefined || this.trim() == "" || this.trim() == "null" );
};
/*
*返回忽略前导空白和尾部空白字符串	
*/
String.prototype.trim = function () {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};
/*
*返回忽略前导空白字符串	
*/
String.prototype.lTrim = function () {
	return this.replace(/(^\s*)/, "");
};
/*
*返回忽略前导0字符串
*/
String.prototype.lTrimZero = function () {
	return this.replace(/(^0+)/, "");
};
/*
*返回忽略尾部空白字符串	
*/
String.prototype.rTrim = function () {
	return this.replace(/(\s*$)/, "");
};
/*
*返回第一个字符的ascii码
*/
String.prototype.ascii = function () {
	return this.charCodeAt(0);
};
/*
*判断是否以指定的单个字符开始
*/
String.prototype.startsWith = function (character) {
	return (character == this.charAt(0));
};
/*
*判断是否以指定的单个字符结尾
*/
String.prototype.endsWith = function (character) {
	return (character == this.charAt(this.length - 1));
};
/*
*截取定长字符串,一个汉字算两个字母宽
*/
String.prototype.cut = function (n) {
	var strTmp = "";
	for (var i = 0, l = this.length, k = 0; (i < n); k++) {
		var ch = this.charAt(k);
		if (ch.match(/[\x00-\x80]/)) {
			i += 1;
		} else {
			i += 2;
		}
		strTmp += ch;
	}
	return strTmp;
};
/*
*汉字、全角字符算两个长度值
*/
String.prototype.realLength = function () {
	return this.replace(/[^\x00-\xff]/g, "**").length;
};
/*
*判断字符串是否全为汉字   
*/
String.prototype.isAllChinese = function () {
	return /^([\u4E00-\u9FA5])+$/g.test(this);
};  
/*
*判断字符串是否包含汉字   
*/
String.prototype.isContainChinese = function () {
	return /[\u4e00-\u9fa5\uf900-\ufa2d]/.test(this);
};
/*
*这行数还有问题
*全角转半角 
*/
String.prototype.dbc2sbc = function () {
	var result = "";
	for (var i = 0; i < str.length; i++) {
		code = str.charCodeAt(i);//获取当前字符的unicode编码   
		if (code >= 65281 && code <= 65373) {//在这个unicode编码范围中的是所有的英文字母已经各种字符
	  	//把全角字符的unicode编码转换为对应半角字符的unicode码
			result += String.fromCharCode(str.charCodeAt(i) - 65248);
		} else {
			if (code == 12288) {//空格
				result += String.fromCharCode(str.charCodeAt(i) - 12288 + 32);
			} else {
				result += str.charAt(i);
			}
		}
	}
	return result;
};
/*
*字符串首字母大写
*/
String.prototype.capitalize = function () {
	return this.charAt(0).toUpperCase() + this.substr(1);
};
String.prototype.isEmail = function () {
	var emailReg = /^\w+([-.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	return emailReg.test(this);
};
/*
*是否是阿拉伯字符和数字
*/
String.prototype.isAlphaNumeric = function () {
	return /^[a-zA-Z0-9]+$/.test(this);
};
/*
*是否是阿拉伯字符
*/
String.prototype.isAlpha = function () {
	return /^[a-zA-Z]+$/.test(this);
};
/*
*是否是数字，包括整数，浮点数等
*/
String.prototype.isNumeric = function () {
	return /^[-|\+]?\d+(\.\d+)?$/.test(this);
};
/*
*判断字符串是否全是阿拉伯数字
*/
String.prototype.isDigits = function () {
	return /^\d+$/.test(this);
};
/*
*判断字符串是否是整数
*/
String.prototype.isInteger = function () {
	return /^[+|-]?\d+$/.test(this);
};
String.prototype.isColor = function () {
	return /^#?([a-f]|[A-F]|[0-9]){3}(([a-f]|[A-F]|[0-9]){3})?$/.test(this);
};
String.prototype.isDate = function () {
	if(  /^\d{4}-\d{1,2}-\d{1,2}$/.test(this) ){
		var arr = this.split("-");
		var date = new Date(arr[0],arr[1]-1,arr[2]);
		if(date.getFullYear() == arr[0] && date.getMonth()+1 == arr[1] 
			&& date.getDate() == arr[2] ){
			return true;
		}
	}
	return false;
};
String.prototype.isTime = function () {
	if(  /^\d{1,2}:\d{1,2}:\d{1,2}$/.test(this) ){
		var arr = this.split(":");
		if(arr[0]<24 && arr[1]<60 &&arr[2]<60 ){
			return true;
		}
	}
	return false;
};
String.prototype.isDateTime = function () {
	var arr = this.split(" ");
	if( arr.length == 2 ){
		return arr[0].isDate() && arr[1].isTime();
	}
	return false;
};
String.prototype.isPhoneNumber= function () {
	return /(^[0-9]{3,4}\-{0,1}[0-9]{7,8}$)|(^[0-9]{7,8}$)/.test(this);
};
String.prototype.isMobilPhone= function () {
	return /^1[3|4|5][\d]{9}$/.test(this);
};
String.prototype.toHtmlEncode = function(){
	var str = this;
	str=str.replace(/&/g,"&amp;");
	str=str.replace(/</g,"&lt;");
	str=str.replace(/>/g,"&gt;");
	str=str.replace(/\'/g,"&apos;");
	str=str.replace(/\"/g,"&quot;");
	str=str.replace(/\n/g,"<br>");
	str=str.replace(/\ /g,"&nbsp;");
	str=str.replace(/\t/g,"&nbsp;&nbsp;&nbsp;&nbsp;");
	return str;
}
/**
*比较yyyy-MM-dd格式的日期 
*/
function compareDate(startDate,endDate){
	var tmpStart = startDate.split("-");
	var tmpEnd = endDate.split("-");
	//var tmpStartDate = parseInt(tmpStart[0])*366+parseInt(tmpStart[1])*31+parseInt(tmpStart[2]);
	//var tmpEndDate = parseInt(tmpEnd[0])*366+parseInt(tmpEnd[1])*31+parseInt(tmpEnd[2]);
	var tmpStartDate = tmpStart[0]*366+tmpStart[1]*31+tmpStart[2];
	var tmpEndDate = tmpEnd[0]*366+tmpEnd[1]*31+tmpEnd[2];	
	if( tmpEndDate > tmpStartDate ){
		return 1;
	}else if( tmpEndDate < tmpStartDate ){
		return -1;
	}
	return 0;
}
/**
*比较yyyy-MM-dd hh:mm:ss的格式
*/
function compareDateTime(startTime,endTime){
	var tmpS = startTime.split(" ");
	var tmpE = endTime.split(" ");
	var tmpSDate = tmpS[0].split("-");
	var tmpSTime = tmpS[1].split(":");
	var tmpEDate = tmpE[0].split("-");
	var tmpETime = tmpE[1].split(":");
	var tmpStartTime = tmpSDate[0]*12*31*24*60*60+tmpSDate[1]*31*24*60*60+tmpSDate[2]*24*60*60+tmpSTime[0]*60*60+tmpSTime[1]*60+tmpSTime[2];
	var tmpEndTime = tmpEDate[0]*12*31*24*60*60+tmpEDate[1]*31*24*60*60+tmpEDate[2]*24*60*60+tmpETime[0]*60*60+tmpETime[1]*60+tmpETime[2];
	if( tmpEndTime > tmpStartTime ){
		return 1;
	}else if( tmpEndTime < tmpStartTime ){
		return -1;
	}
	return 0;	
}
