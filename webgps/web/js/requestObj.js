var xmlHttp;
function createRequestObject() 
{
    var browser = navigator.appName;
  
    if(browser == "Microsoft Internet Explorer")
    {
    	 
		try 
		{
			xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		} 
		catch (e) 
		{
			try 
			{
					xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			} 
			catch (e2) 
			{
				xmlHttp = false;
			}
		}
    }
    else
    	{
       		 xmlHttp = new XMLHttpRequest();
    	}
    return xmlHttp;
}