
var fv = new Array()

/****************************************************
*	Constants. Do not edit
****************************************************/

//	Global used for flagging the validateBlank() function within most other validation functions
fv['bok'] = false;

//	Array for error totalling while in group error mode
var errorData = new Array();
var errorField = new Array();
var oldErrorField = new Array();
var firstTestBlank = true;
var errorColor = "#FF9900";
var originalColor = "#99CCCC";
var recordOdd = "#B9E8E8";
var recordEven = "#6FB7B7";

//  Whether prompt error message step by step. -- Sunyi 2004-12-13
var promptErrMsgStep = true;
var defaultDatePattern = "yyyy-MM-dd";

//	Placeholder for Group Error boolean
fv['groupError'] = 0;

//	Placeholder for number of group error alerts
fv['groupErrors'] = 0;

//	Browser Sniffer
fv['is'] = function () {
	this.ver = navigator.appVersion; //Cheking for browser version
	this.agent = navigator.userAgent; //Checking for browser type
    var minor = parseFloat(this.ver);
    var major = parseInt(minor);
	this.dom = document.getElementById?1:0;
	this.opera = (agent.indexOf("opera") != -1);
	var iePos  = this.ver.indexOf('msie');
	if (iePos !=-1) {
		minor = parseFloat(this.ver.substring(iePos+5,this.ver.indexOf(';',iePos)))
		major = parseInt(minor);
		}
	this.ie = ((iePos!=-1) && (!this.opera));
	this.gecko = ((navigator.product)&&(navigator.product.toLowerCase()=="gecko"))?true:false;
    this.ie4   = (this.ie && major == 4);
    this.ie4up = (this.ie && minor >= 4);
    this.ie5   = (this.ie && major == 5);
    this.ie5up = (this.ie && minor >= 5);
    this.ie5_5  = (this.ie && (agent.indexOf("msie 5.5") !=-1));
    this.ie5_5up = (this.ie && minor >= 5.5);
    this.ie6   = (this.ie && major == 6);
    this.ie6up = (this.ie && minor >= 6);
	this.mac = this.agent.indexOf("Mac")>-1;
	}

/****************************************************
*	Globals.  Modify these to suit your setup
****************************************************/

//	Attribute used for fValidate Validator codes
fv['code'] = 'alt';

//	Attribute used for fValidate field display name
fv['displayname'] = 'displayname';

//	Attribute used for custom error messages (override built-in error messages)
fv['emsg'] = 'emsg';

//	Attribute used for pattern with custom validator type
fv['pattern'] = 'pattern';

//	If the bConfirm flag is set to true, the users will be prompted with CONFIRM box with this message
fv['confirmMsg'] = 'Your Data is about to be sent.\nPlease click \'Ok\' to proceed or \'Cancel\' to abort.';

//	If user cancels CONFIRM, then this message will be alerted.  If you don't want this alert to show, then
//	empty the variable (  fv['confirmAbortMsg'] = '';  )
fv['confirmAbortMsg'] = 'Submission cancelled.  Data has not been sent.';

//	Enter the name/id of your form's submit button here (works with type=image too)
fv['submitButton'] = 'Submit';

//	Enter the name/id of your form's reset button here (works with type=image too)
fv['resetButton'] = 'Reset';

//	Enter the DOM name of the SELECT object here. Make sure you pay attention to the values (CC Types)
//	used in the case statement for the function validateCC()
fv['ccTypeObj'] = 'form1.Credit_Card_Type';

//	Number of group error mode alerts before switching to normal error mode
fv['switchToEbyE'] = 3;


//  Error message
var blankErrorMsg = "请您输入必填项!";
var blankErrorMsgPrefix = "请您输入";
var blankErrorMsgSuffix = "！";
var dateErrorMsg = "日期无效！";
var invalidMsg = "无效！";
var emailErrorMsg = "Email无效！";
var customErrorMsg = "格式无效！";
var numberErrorMsg = "请您输入数字！";
var lengthErrorMsg = "长度无效！";
var numberBoundErrorMsg = "数字范围无效！";
var decimalErrorMsg = "格式无效！";


//	EOF
