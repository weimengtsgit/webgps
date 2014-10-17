function validateSerialNo(formObj) {
	if (fv['bok'] && checkBlank(formObj))
		{ fv['bok']=false; return true; }
        if(validateBlank(formObj))
        {
	var regex = new RegExp(/^[AB]{1}(?!000000)\d{6}$/);
	if (!regex.test(formObj.value))
	   //return errorProcess3(formObj,formObj.displayName + " is invalid.");	
	   //return errorProcess3(formObj,formObj.displayName + " should be A999999 or B999999.");	
	   return errorProcess3(formObj,formObj.displayName + " is invalid serial number format.");	
	return true;
	}
} 
/***************** Validate the relation of Serial No *********************/
function validateSerialNoR(serialNoFromObj,serialNoToObj) {

	var serialNoFrom = serialNoFromObj.value; 
	var serialNoTo = serialNoToObj.value;
	if(commonValidateSerialNo(serialNoFromObj)&& commonValidateSerialNo(serialNoToObj)) {
            var fromSubStr = serialNoFrom.substr(1,6);
            var toSubStr = serialNoTo.substr(1,6); 
            if (serialNoFrom.charAt(0) != serialNoTo.charAt(0))
                 {
                   errorProcess3(serialNoFromObj,"The first character of  'From Serial No. ' and 'To Serial No.' must be the identical");
                   errorProcess2(serialNoToObj);
                }
            else
              {
                if (toSubStr<fromSubStr)
                    {
                    	//errorProcess3(serialNoFromObj,"The number of 'To Serial No.' must be greater than or equal to 'From Serial No.'!");
						errorProcess3(serialNoFromObj,"The Serial Number From should be smaller than The Serial No To.");
                        errorProcess2(serialNoToObj);
                    }
              }
       }
} 

function commonValidateSerialNo(formObj) {
   var regex = new RegExp(/^[AB]{1}(?!000000)\d{6}$/);
	if (!regex.test(formObj.value))
	   return false;	
	else
	   return true;
}

/*****validate input data length not longer than the desired length either inputing english or inputing chinese******/
function checkDataLength(textValue,count){
	var actualValue = count;
	for(var i=0;i<textValue.length;i++)
	{
		if(textValue.charCodeAt(i) > 255)
		{
			actualValue = count / 2;		
		}
	}
  	
  	if(textValue.length >= actualValue && window.event.keyCode != 8)
  	{
  		window.event.returnValue=false;
  	}
}