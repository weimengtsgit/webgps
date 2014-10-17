
// *******************    The main methods starts here      ******************************//  

/*

ignoreObjects is a array ,there are some form fields object inside, which should ignore validation.
if you do not need this parameter you could still call this function like this "
commonValidateForm(form)

"
*/
function commonValidateForm(Frm,ignoreObjects) {//this function can be called as this "commonValidateForm(form)" if need validate all fields.
	fv['bok'] = false;
	firstTestBlank = true;
	var idForDate;
	var idForTime;
	var dateTime = new Array();
	var time = new Array();
	var sequence = -1;
	var timeSequence = -1;
  var sequenceFlag = false;

	for (var i=0; i<Frm.elements.length; i++) {
      // Add for prompt error message step by step. -- Sunyi begin 2004-12-8
      if(promptErrMsgStep) {
        if(errorField.length > 0 && !sequenceFlag) break;
      }
      // Add for prompt error message step by step. -- Sunyi end 2004-12-8

	   fv['bok'] = false;
	   //add by chenzj for ignore some fields ,
	   var findFlagforIgnore = false;
	   if(ignoreObjects){
		   for(var n=0 ; n < ignoreObjects.length ; n++){
			   if(Frm.elements[i].name == ignoreObjects[n].name) {
				   findFlagforIgnore = true;
				   break;
			   }
		   }
		}
		if(findFlagforIgnore) continue;	//end by chenzj
		if (Frm.elements[i].getAttribute(fv['code'])) {				// Gets the validator attribute, if exists thus starting the validation
			var validateType = Frm.elements[i].getAttribute(fv['code']);
			var validateObj = Frm.elements[i];
			var params = validateType.split("|");
			if (params[params.length-1] == 'bok') fv['bok'] = true;// Sets flag if field is allowed to be blank
			switch (params[0]) {									// Calls appropriate validation function based on type
				case 'blank'	: validateBlank(validateObj); break;
				case 'length'	: validateLength(validateObj, params[1], params[2]); break;
				case 'lengthl'	: validateLengthL(validateObj, params[1]); break;
				case 'lengthr'	: validateLengthR(validateObj, params[1]); break;
				case 'number'	: validateNumber(validateObj) ; break;
				case 'numberl'	: validateNumberL(validateObj, params[1]); break;
				case 'numberr'	: validateNumberR(validateObj, params[1], params[2] ); break;
				case 'decimal'	: validateDecimal(validateObj, params[1], params[2] ); break;
				case 'decimalr'	: validateDecimalR(validateObj, params[1], params[2], params[3], params[4] ); break;
				case 'select'	: validateSelect(validateObj);  break;
				case 'selectm'	: validateSelectM(validateObj,params[1],params[2]);break;
				case 'checkbox'	: validateCheckbox(validateObj, params[1], params[2]); break;
				case 'radio'	: validateRadio(validateObj);   break;
				case 'custom'	: validateCustom(validateObj);  break;
				case 'serialNo'   : validateSerialNo(validateObj); break;
				case 'alnumr'	: validateAlnumR(validateObj); break;
				case 'alnum'	: validateAlnum(validateObj); break;
				case 'email'	: validateEmail(validateObj); break;
			}
		}
		idForDate = Frm.elements[i].dateFormat; 
		idForTime = Frm.elements[i].timeFormat;

		switch(idForDate) {
        // Add for verify the integrated date, such as 2004-12-13. -- Sunyi 2004-12-13
        case 'date'	:
          var datePattern = defaultDatePattern;
          if(Frm.elements[i].pattern)
            datePattern = Frm.elements[i].pattern;
          var dateArr = parseDate(Frm.elements[i].value, datePattern);
          if(dateArr) {
            sequence++;
            dateTime[sequence] = new Object();
            dateTime[sequence].displayName = Frm.elements[i].displayName;
            dateTime[sequence].year = dateArr['year']; 
            dateTime[sequence].month = dateArr['month']; 
            dateTime[sequence].day = dateArr['day']; 
            dateTime[sequence].dayName = Frm.elements[i].name;
            dateTime[sequence].monthName = Frm.elements[i].name;
            dateTime[sequence].yearName = Frm.elements[i].name;
          }
          break;
			case 'day':
				if(!sequenceFlag) {
          sequence++;
				  sequenceFlag = true;
				  dateTime[sequence] = new Object();
				  dateTime[sequence].displayName = Frm.elements[i].displayName;
        }
				dateTime[sequence].day = Frm.elements[i].value; 
				dateTime[sequence].dayName = Frm.elements[i].name;
				break;
			case 'month':
				if(!sequenceFlag) {
          sequence++;
				  sequenceFlag = true;
				  dateTime[sequence] = new Object();
				  dateTime[sequence].displayName = Frm.elements[i].displayName;
        }
				dateTime[sequence].month = Frm.elements[i].value;
				dateTime[sequence].monthName = Frm.elements[i].name;
				break;
			case 'year': 
				if(!sequenceFlag) {
          sequence++;
				  sequenceFlag = true;
				  dateTime[sequence] = new Object();
				  dateTime[sequence].displayName = Frm.elements[i].displayName;
        }
				dateTime[sequence].year = Frm.elements[i].value;
				dateTime[sequence].yearName = Frm.elements[i].name;
				break;
			case 'hour' :
				dateTime[sequence].hour = Frm.elements[i].value;
				dateTime[sequence].hourName = Frm.elements[i].name;
				break;
			case 'minute' :
				dateTime[sequence].minute = Frm.elements[i].value; 
				dateTime[sequence].minuteName = Frm.elements[i].name;
				break;
		}

    if(dateTime[sequence] 
        && dateTime[sequence].day != undefined
        && dateTime[sequence].month != undefined
        && dateTime[sequence].year != undefined) {
      sequenceFlag = false;
      validateDate(dateTime[sequence]);
    }

		switch(idForTime) {
			case 'hour': 
				timeSequence ++; 
				time[timeSequence] = new Object();
				time[timeSequence].hour = Frm.elements[i].value;
				time[timeSequence].hourName = Frm.elements[i].name;
				time[timeSequence].displayName = Frm.elements[i].displayName;
				break;
			case 'minute': 
				time[timeSequence].minute = Frm.elements[i].value;
				time[timeSequence].minuteName = Frm.elements[i].name;
				break;
			case 'second': 
				time[timeSequence].second = Frm.elements[i].value;
				time[timeSequence].secondName = Frm.elements[i].name;
				break;
		}
    if(time[sequence] && time[sequence].hour && time[sequence].minute && time[sequence].second) {
      sequenceFlag = false;
      validateTime(time[timeSequence]);
    }
	}

//	for(var i = 0; i< dateTime.length ; i++) validateDate(dateTime[i]); 
//	for(var i = 0; i< time.length ; i++) validateTime(time[i]);
}	 

function validateForm(Frm)
{
  erase(Frm);
  commonValidateForm(Frm);
  return checkError(Frm);  
} 
   
// *******************  The method is used for the "bok". *****************************//
function checkBlank(formObj) {
	if (formObj.value == "")
		return true;
	var regex = new RegExp(/^\s+$/);
	if (regex.test(formObj.value))
		return true;
	return false;
	}

// ****************** The method is used for checking the blank. **************************//   
function validateBlank(formObj) {
	if (fv['is'].ie5 || fv['is'].mac) {
		if (formObj.value == "") {
			 return errorProcess2(formObj);
	          }
		}
	else {
		var regex = new RegExp(/\S/);
		if (!regex.test(formObj.value)) {
			if(firstTestBlank) { 
        firstTestBlank = false;
        var errorMsg = blankErrorMsg;
//			return errorProcess3(formObj, errorMsg);

        if(formObj.displayName)
          errorMsg = blankErrorMsgPrefix + formObj.displayName + blankErrorMsgSuffix;
        return errorProcess3(formObj, errorMsg);
      }
		  else
		    return errorProcess2(formObj);	  
		}
	}
	return true;
}
   
// ****************** The method is used for checking the specific length. ********************//
function validateLength(formObj,lb, ub) {
	if (fv['bok'] && checkBlank(formObj))
		{ fv['bok']=false; return true; }
         
	if(validateBlank(formObj))
	  {
	     if ((getInputTextLength(formObj.value) < parseInt(lb)) || (getInputTextLength(formObj.value) > parseInt(ub))) {
    var errorMsg = lengthErrorMsg;
    if(formObj.msg)
      errorMsg = formObj.msg;
		return errorProcess3(formObj, errorMsg);
	     }
             return true;
          }
}

// ********************* The method is  used for checking the specific length. ******************//
 function validateLengthL(formObj,len) {
	if (fv['bok'] && checkBlank(formObj))
		{ fv['bok']=false; return true; }
        if(validateBlank(formObj))
	  {
	   if ((getInputTextLength(formObj.value) < parseInt(len))) {
    var errorMsg = lengthErrorMsg;
    if(formObj.msg)
      errorMsg = formObj.msg;
		return errorProcess3(formObj, errorMsg);
           }
	   return true;
	}
}
// ************************ The method is  used for checking the specific length.******************//

function validateLengthR(formObj,len) {
	if (fv['bok'] && checkBlank(formObj))
		{ fv['bok']=false; return true; }
        if(validateBlank(formObj))
	  {
	   if ((getInputTextLength(formObj.value) > parseInt(len))) {
    var errorMsg = lengthErrorMsg;
    if(formObj.msg)
      errorMsg = formObj.msg;
		return errorProcess3(formObj, errorMsg);
           }
	   return true;
	}
}
// ********************* The method is used for checking the number. *************************** //
function validateNumber(formObj) {
	if (fv['bok'] && checkBlank(formObj))
		{ fv['bok']=false; return true; }
        if(validateBlank(formObj))
          {
	    var numReg = "^[0-9]+$";
	    var regex = new RegExp(numReg);
	    if (!regex.test(formObj.value)) {
    var errorMsg = numberErrorMsg;
    if(formObj.msg)
      errorMsg = formObj.msg;
		return errorProcess3(formObj, errorMsg);
	   }	   
	   return true;
	}
}
// ******************** The methos is used for checking the specific length number. ************************** //
function validateNumberL(formObj, len) {
	if (fv['bok'] && checkBlank(formObj))
		{ fv['bok']=false; return true; }
        if(validateBlank(formObj) && validateNumber(formObj))
          {
	     numReg = "^[0-9]{"+parseInt(len)+",}$"
	     var regex = new RegExp(numReg);
	     if (!regex.test(formObj.value)) {
    var errorMsg = numberBoundErrorMsg;
    if(formObj.msg)
      errorMsg = formObj.msg;
		 return errorProcess3(formObj, errorMsg);
	     }
	     return true;
          }
}
// *********************** The method is used for checking the specific scope number. *************************************************** //
function validateNumberR(formObj, lb, ub) {
	if (fv['bok'] && checkBlank(formObj))
		{ fv['bok']=false; return true; }
	if(validateBlank(formObj) && validateNumber(formObj))
          {
	     var num = parseInt(formObj.value,10);
	     numReg = "^\-?[0-9]+$"
	     var regex = new RegExp(numReg);
	     if (!regex.test(formObj.value) || num < lb || num > ub ) {
    var errorMsg = numberBoundErrorMsg;
    if(formObj.msg)
      errorMsg = formObj.msg;
		    return errorProcess3(formObj, errorMsg);
	     }
	     return true;
	  }
}

//******************** The method is used to check the decimal. ************************************************//
function validateDecimal(formObj, lval, rval) {
	if (fv['bok'] && checkBlank(formObj))
		{ fv['bok']=false; return true; }
        if(validateBlank(formObj))
          {
	       (lval == '*')? lval = '*': lval = parseInt(lval);
	       (rval == '*')? rval = '*': rval = parseInt(rval);
	       var decReg = "";
	       if (lval == 0)
		   decReg = "^\\.[0-9]{"+rval+"}$";
	       else if (lval == '*')
		   decReg = "^[0-9]"+lval+"\\.[0-9]{"+rval+"}$";
	       else if (rval == '*')
		   decReg = "^[0-9]{"+lval+"}\\.[0-9]"+rval+"$";
	       else
		   decReg = "^[0-9]{"+lval+"}\\.[0-9]{"+rval+"}$";
	       var regex = new RegExp(decReg);
	       if (!regex.test(formObj.value)) {
            var errorMsg = decimalErrorMsg;
            if(formObj.msg)
              errorMsg = formObj.msg;
		      return errorProcess3(formObj, errorMsg);
		}
	       return true;
	}
}

//************************* The method is used to check the decimal. ************************************************ //
function validateDecimalR(formObj, lmin, lmax, rmin, rmax) {
	if (fv['bok'] && checkBlank(formObj))
		{ fv['bok']=false; return true; }
        if(validateBlank(formObj))
        {
	   (lmin == '*')? lmin = 0: lmin = parseInt(lmin);
	   (lmax == '*')? lmax = '': lmax = parseInt(lmax);
	   (rmin == '*')? rmin = 0: rmin = parseInt(rmin);
	   (rmax == '*')? rmax = '': rmax = parseInt(rmax);
	   var	decReg = "^[0-9]{"+lmin+","+lmax+"}\\.[0-9]{"+rmin+","+rmax+"}$"
	   var regex = new RegExp(decReg);
	   var formValue = formObj.value;
	   if (formValue.indexOf(".") == -1)
		{formValue = formValue + ".";}
	   if (!regex.test(formValue)) {
        var errorMsg = decimalErrorMsg;
        if(formObj.msg)
          errorMsg = formObj.msg;
		   return errorProcess3(formObj, errorMsg);
		}
	   return true;
	}
}
// ********************* The method is used for checking the select. *************************** //
function validateSelect(formObj) {
	if (formObj.selectedIndex == 0) {
		if(firstTestBlank)
	          { 
	            firstTestBlank = false;
		    return errorProcess3(formObj, formObj.msg);
	          }
	      else
		  return errorProcess2(formObj);
	  }
	 return true;
}

// ********************* The method is used for checking the radio. ********************************** //
function validateRadio(formObj) {
	var formObj = formObj.form.elements[formObj.name];
	var selectTotal = 0;

	if(formObj[0])
	{
	   for (i=0; i<formObj.length; i++)
		   if (formObj[i].checked)
	 		   selectTotal++;
    }
    else
    {
        if(formObj.checked)
            selectTotal++;
    }
	if (selectTotal != 1) {
		if(firstTestBlank)
	          { 
	            firstTestBlank = false;
	            if(formObj[0])
	               return errorProcess3(formObj[0], formObj[0].msg);
	            else
	               return errorProcess3(formObj, formObj.msg);
	          }
	      else
	      {
		    if(formObj[0])
		       return errorProcess2(formObj[0]);
		    else
		       return errorProcess2(formObj);
		  }
		}
	return true;
	}

// ********************* The method is used for multiple select. ********************************** //
function validateSelectM(formObj, minS, maxS) {
	var selectCount = 0;
	for (var i=0; i<formObj.length; i++)
		{
		if (formObj.options[i].selected)
			selectCount++;
		}
	if (selectCount < minS || selectCount > maxS) {
		return errorProcess3(formObj, formObj.msg);
		}
	return true;
	}
// ********************* The method is used for checkbox. *************************************** //
function validateCheckbox(formObj, minC, maxC) {
	var formObj = formObj.form.elements[formObj.name];
	var checkTotal = formObj.length;
	var checkCount = 0;

	for (var i=0; i<checkTotal; i++) {
		if (formObj[i].checked) checkCount++;
		}
	if (checkCount < minC || checkCount > maxC) {
                return errorProcess3(formObj[0],formObj[0].msg);
	}
	return true;
	}	
// ********************* The method is used for checking self-defined pattern. *************************** //	
function validateCustom(formObj) {
	if (fv['bok'] && checkBlank(formObj))
		{ fv['bok']=false; return true; }

	if(validateBlank(formObj))
        {
	   var regex = new RegExp(formObj.getAttribute(fv['pattern']));
           if (!regex.test(formObj.value)) {
    var errorMsg = customErrorMsg;
    if(formObj.msg)
      errorMsg = formObj.msg;

			return errorProcess3(formObj,errorMsg);
	   }
	   return true;
	}
}

function validateAlnumR(formObj) {
	if (fv['bok'] && checkBlank(formObj))
		{ fv['bok']=false; return true; }

	numReg = "^[0-9\-]+$";
	if(validateBlank(formObj))
    {
	   var regex = new RegExp(numReg);
	   if (!regex.test(formObj.value)) 
        {
		    return errorProcess3(formObj,formObj.msg);
        }
        return true;
    }
}

function validateAlnum(formObj) {
	if (fv['bok'] && checkBlank(formObj))
		{ fv['bok']=false; return true; }

	numReg = "^[a-zA-Z0-9\-]+$";
	if(validateBlank(formObj))
    {
	   var regex = new RegExp(numReg);
	   if (!regex.test(formObj.value)) 
        {
		    return errorProcess3(formObj,formObj.msg);
        }
        return true;
    }
}
// ************************* The method is used for checking Email pattern. ******************************* //
function validateEmail(formObj)	{
	if (fv['bok'] && checkBlank(formObj))
		{ fv['bok']=false; return true; }

	var emailStr = formObj.value;
	var emailReg1 = /(@.*@)|(\.\.)|(@\.)|(\.@)|(^\.)/; // not valid
	var emailReg2 = /^.+\@(\[?)[a-zA-Z0-9\-\.]+\.([a-zA-Z]{2,6}|[0-9]{1,3})(\]?)$/; // valid
	if(validateBlank(formObj))
        {
          if (!(!emailReg1.test(emailStr) && emailReg2.test(emailStr))) {// if syntax is valid
    var errorMsg = emailErrorMsg;
    if(formObj.msg)
      errorMsg = formObj.msg;
		return errorProcess3(formObj, errorMsg);
		}
	  return true;
	}
}
// ****************** The method is used for checking the Date format. such as 2004-12-13 *******************//
// Add for parse the date according to pattern. -- Sunyi begin 2004-12-13
function parseDate(dateStr, pattern) {
  //date string is empty
  var emptyRegExp = new RegExp("^\\s*$");
  if(dateStr == null || emptyRegExp.test(dateStr)) {
    return false;
  }

  pattern = pattern.toLowerCase();

  //date pattern is not correct format
  if(pattern.indexOf("yyyy") == -1 || pattern.indexOf("mm") == -1 || pattern.indexOf("dd") == -1) {
    return virtualErrorDate();
  }
  
  var dateArr = new Array();

  var regPattern = pattern.replace("yyyy", "(\\d{4})");
  regPattern = regPattern.replace("mm", "\\d{1,2}");
  regPattern = regPattern.replace("dd", "\\d{1,2}");
  regPattern = "^" + regPattern + "$";
  var regExp = new RegExp(regPattern);
  if(!regExp.test(dateStr)) {
    return virtualErrorDate();
  }
  regExp.exec(dateStr);
  dateArr['year'] = RegExp.$1;

  regPattern = pattern.replace("yyyy", "\\d{4}");
  regPattern = regPattern.replace("mm", "(\\d{1,2})");
  regPattern = regPattern.replace("dd", "\\d{1,2}");
  regPattern = "^" + regPattern + "$";
  regExp = new RegExp(regPattern);
  regExp.exec(dateStr);
  dateArr['month'] = RegExp.$1;

  regPattern = pattern.replace("yyyy", "\\d{4}");
  regPattern = regPattern.replace("mm", "\\d{1,2}");
  regPattern = regPattern.replace("dd", "(\\d{1,2})");
  regPattern = "^" + regPattern + "$";
  regExp = new RegExp(regPattern);
  regExp.exec(dateStr);
  dateArr['day'] = RegExp.$1;

  return dateArr;
}

function virtualErrorDate() {
  var dateArr = new Array();
  dateArr['year'] = "0000";
  dateArr['month'] = "00";
  dateArr['day'] = "00";
  return dateArr;
}

function virtualEmptyDate() {
  var dateArr = new Array();
  dateArr['year'] = "";
  dateArr['month'] = "";
  dateArr['day'] = "";
  return dateArr;
}
// Add for parse the date according to pattern. -- Sunyi end 2004-12-13

// ****************** The method is used for checking the Date format. **************************//
function validateDate(DateTime) {
      var day = LTrim(DateTime.day);
      day = RTrim(day);
      var month = LTrim(DateTime.month);
      month = RTrim(month);
      var year = DateTime.year;
      var hour = LTrim(DateTime.hour);
      hour = RTrim(hour);
      var minute = LTrim(DateTime.minute);
      minute = RTrim(minute);
      //var message = DateTime.message;
      //var message = "The format of " + DateTime.displayName + " is invalid!";
      var message = dateErrorMsg;
      if(DateTime.displayName) {
        message = DateTime.displayName + invalidMsg;
      }
      var dateField = new Array();
      dateField.push(DateTime.dayName);
      dateField.push(DateTime.monthName);
      dateField.push(DateTime.yearName);
      if(hour!=null && minute !=null)
        {
          dateField.push(DateTime.hourName);	
          dateField.push(DateTime.minuteName);
          
        }
      var regex = new RegExp(/\S/);		
      if(regex.test(day)|| regex.test(month) || regex.test(year)|| ((hour!=null && minute !=null) && (regex.test(hour)||regex.test(minute))) ) {
         if(hour!=null && minute!=null) 
            if(!commonValidateNumberR(hour,0,23) || ! commonValidateNumberR(minute,0,59))
               return errorProcess1(dateField,message);
         var dayField;
         if(day.charAt(0)=='0')
            dayField = day.charAt(1);
         else
            dayField = day;
         var monthField;
         if(month.charAt(0)=='0')
            monthField = month.charAt(1);
         else
            monthField = month;
         var yearField = year;
 
         var regExp = new RegExp(/^\-?[0-9]+$/);
         if(!regExp.test(dayField)|| !regExp.test(monthField) || !regExp.test(yearField))
            return errorProcess1(dateField,message);

         var tempDateValue = yearField + "/" + monthField + "/" + dayField;
         if(tempDateValue.length<6||tempDateValue.length>10)
           return errorProcess1(dateField,message);     //validate the length. The method is rarely used.

        var tempDate = new Date(tempDateValue);
        if (isNaN(tempDate))
            return errorProcess1(dateField,message);    //validate the scope. When user input over scope data such as 99/99/2000,this error message will be promoted.
         
         if (parseInt(month) <10 && month.charAt(0)!='0') 
         {
             month = "0"+month;
         }
          if (parseInt(day) <10 && day.charAt(0)!='0') 
         {
             day = "0"+day;
         }
        if (parseInt(year+month+day) >= 19000101 && parseInt(year+month+day) <= 99991231 &&
            ((tempDate.getFullYear()).toString()==yearField) && 
            (tempDate.getMonth()==parseInt(monthField)-1) && 
            (tempDate.getDate()==parseInt(dayField)))
            return true;
        else
           return errorProcess1(dateField, message);
  }

}


function validateTime(Time) {
      var hour = Time.hour;
      var minute = Time.minute;
      var second = Time.second;
      //var message = Time.message;
      var message = "The format of " + Time.displayName + " is invalid!";
      var timeField = new Array();         
      timeField.push(Time.hourName);	
      timeField.push(Time.minuteName);
      timeField.push(Time.secondName);
      
      var regex = new RegExp(/\S/);		
      if(regex.test(hour)|| regex.test(minute) || regex.test(second)) {
            if(!commonValidateNumberR(hour,0,23) || !commonValidateNumberR(minute,0,59)|| !commonValidateNumberR(second,0,59) )
               return errorProcess1(timeField,message);
         
  }

}

// ********** The method is used for processing the error, just being used by the checking Date. ************* //
// ***************      The methods also is provide for the developer.       ********************************** //
// ********** The parameter errorObj is a Array contains the name of the error elements.*********************** //
function errorProcess1(errorObj,errorMessage)   
 {
    
    for(var i = 0; i < errorObj.length; i++)
       errorField.push(errorObj[i]);
    errorData[errorData.length] = errorMessage;   //Collect the errorMessage and error field.
    
    //alert(errorField);
    //alert(errorMessage);    
    return false;
 }

// ******************* These method is used for processing the error,just being used by the checking blank. ************ //
function errorProcess2(errorElement)
{
     errorField.push(errorElement.name);
     return false;
}

// **********************           The method is used for processing the error.         ******************************* //
// ******************* The parameter errorObj is a Array contains the name of the error elements.*********************** // 
function errorProcess3(errorElement,errorMessage)
{
    errorField.push(errorElement.name);
    errorData[errorData.length] = errorMessage;
    return false;    
}

// ********************* The method is provided for the developer. ************************************************** //
function errorProcess(errorElementName,errorMessage)
{
    if(errorElementName != null)
    {
     	if(errorElementName[0])
        {
    	for(var i = 0; i < errorElementName.length; i++)
            errorField.push(errorElementName[i]);
        }
        else
          errorField.push(errorElementName);
    }
    errorData[errorData.length] = errorMessage;
    return false; 
}

// ********************** The method is used for checking the error data and invoking the highLightField to high light the fields. *****//
function checkError(Frm){
	if (errorData.length!=0) 
	{
		//var errorString = errorData.join("\n");
		var errorHtmlString = "<table>";
		var errorString = "";
		for(var i = 0;i < errorData.length; i++)
		{
      // Add for prompt error message step by step. -- Sunyi begin 2004-12-8
      if(promptErrMsgStep) {
        if(i > 0) break;
      }
      // Add for prompt error message step by step. -- Sunyi end 2004-12-8

      errorHtmlString += "<tr><td><img src=\"images\\icon_error.gif\" height=\"20\" width=\"20\" /></td><td><font color=\"#FF0000\">" + errorData[i] + "</font></td></tr>"; 
			errorString += errorData[i];
			errorString += "\n";
		}
		errorHtmlString = errorHtmlString + "</table>";
		highLightField(Frm);
		alert(errorString);
//		var errorNode = document.getElementById("errorReport");
//		errorNode.style.display='';
//		errorNode.innerHTML=errorHtmlString;
		
		oldErrorField = errorField;
		errorData = [];
		errorField = [];
		return false;
	} 
    else
	{	
		if (typeof(document.all.submitFlag)!="undefined")
		{
			document.all.submitFlag.value = "1";
		}
		return true;
	}
}      
// ********************** The method is used for displaying the error fields.   ****************************** //
function highLightField(Frm)
{
	
   var redFieldNode;
   for(var i = 0; i < errorField.length ; i++)
   {
      redFieldNode = Frm[errorField[i]];
      if (redFieldNode[0] && (redFieldNode[0].type == "radio"||redFieldNode[0].type=="checkbox"||redFieldNode[0].type=="text"))
      {
         for(var j = 0; j<redFieldNode.length; j++)
            redFieldNode[j].style.backgroundColor = errorColor;
	    }
	    else
      {
        redFieldNode.style.backgroundColor = errorColor;
      }
   }
//   var currentNode = document.getElementById("topNod");
//   currentNode.scrollIntoView();
//    //show the address table when showing ErrorMessage
//    var addressNode = document.getElementById("address");
//    if (addressNode != null)
//    {
//        show("address");
//    }
}

// ********************** The method is used for erasing the backgroup color of error fields. ******************* //
function erase(Frm)
{
	var redFieldNode;
	for(var i = 0; i < oldErrorField.length; i++ )
	{
		if(Frm[oldErrorField[i]])
		{
			redFieldNode = Frm[oldErrorField[i]];
			 if (redFieldNode[0] && (redFieldNode[0].type == "radio"||redFieldNode[0].type=="checkbox"||redFieldNode[0].type=="text"))
			{
				for(var j = 0; j<redFieldNode.length; j++)
					redFieldNode[j].style.backgroundColor = originalColor;
			}
			else
			{
				redFieldNode.style.backgroundColor = "white";
			}
		}
    }
//    if (errorData.length==0)
//    {
//        var errorNode = document.getElementById("errorReport");
//        errorNode.style.display='none';
//    }
}
// ********************** These methods are provided for the developer. ********************************** //

// ********************** Construct the date object **************************************** //
function constructDate(dayObj,monthObj,yearObj,minuteObj,secondObj)
{

  var dayField = dayObj.value;
  var monthField = monthObj.value;
  var yearField = yearObj.value;
  var tempDateValue = yearField + "/" + monthField + "/" + dayField;
  
  return new Date(tempDateValue);
}

// ********************** Validate integer with specified bound ******************************** //
function commonValidateNumberR(field,lb,ub)
{
	var num = parseInt(field);
	var numReg = "^\-?[0-9]+$"
	var regex = new RegExp(numReg);
	if (!regex.test(field) || num < lb || num > ub ) 
		return false;
	else      	
		return true;
}

function commonValidateBlank(field) {
	if (fv['is'].ie5 || fv['is'].mac) 
	{
	  if (field == "") 
	      return false;
	          
	}
	else 
	{
	   var regex = new RegExp(/\S/);
	       if (!regex.test(field)) 
	           return false;			  
		
	}
	return true;
}	

function undisplayErrorMessage(Frm) 
{
	var errorNode = document.getElementById("errorReport");
	errorNode.style.display='';
	errorNode.innerText="";
}

function getInputTextLength(str)
{
	var str1 = escape(str);	
	while(true) {
		var tempStr = str1.replace(/%u./,'');
		if(str1 == tempStr) break;
		else str1 = tempStr;
	}
	
	while(true) {
		var tempStr = str1.replace(/%./,'')
		if(str1 == tempStr) break;
		else str1 = tempStr;
	}
	
	//alert(str1.length)
	return str1.length;
}

function LTrim(str){
    if (str==null){return null;}
    for(var i=0;str.charAt(i)==" ";i++);
    return str.substring(i,str.length);
}

function RTrim(str){
    if (str==null){return null;}
    for(var i=str.length-1;str.charAt(i)==" ";i--);
    return str.substring(0,i+1);
}