
package com.sosgps.wzt.util;

import java.util.Calendar;


public class WebTool
{


    public static String getHeader(String sTitle, String sOtherHtmlCode) {

        return  "<html>\n<head>\n<title>"
                + sTitle +"</title>\n<meta http-equiv='Content-Type' content='text/html; charset=GB2312'>\n"
                + sOtherHtmlCode + "<script language=javascript src=../common.js></script>\n"
                + "<script language=JavaScript src='../DateControl.js'></script>"
                + "<link rel='stylesheet' href='../css/sap.css' type='text/css'></head>\n";
    }

    public static String getHeader1(String sTitle, String sOtherHtmlCode) {

        return  "<html>\n<base target=\"_self\">\n<head>\n<title>"
                + sTitle +"</title>\n<meta http-equiv='Content-Type' content='text/html; charset=GB2312'>\n"
                + sOtherHtmlCode + "<script language=javascript src=../common.js></script>\n"
                + "<script language=JavaScript src='../DateControl.js'></script>"
                + "<link rel='stylesheet' href='../css/sap.css' type='text/css'></head>\n";
    }    

/****************************************
 private static String getHtmlSelect(String sSelectName, int iStart, int iEnd,
                                        int iDefault, String sOtherStr)
    生成表单中的select项，常用于生成年月日之类的列表，可设置默认选项iDefault，

 Parameters:
   String sSelectName <select name=sSelectName>
   int iStart, the start option value
   int iEnd, the end option value
   int iDefault, the default option value
   String sOtherStr, other html code attached

 Returns:
    String: the html code made

 Throws:

 Changes:

*****************************************/
    private static String getHtmlSelect(String sSelectName, int iStart, int iEnd,
                                        int iDefault, String sOtherStr)
    {
        String sHtml = "<select name='"+ sSelectName +"'>";
        for (int i = iStart; i <= iEnd; i++)
        {
            sHtml = sHtml + "<option value="+ String.valueOf(i) + (iDefault == i ?" selected":"") +">"
                    + String.valueOf(i) + "</option>";
        }
        sHtml = sHtml + "</select>" + sOtherStr;
        return sHtml;
    }


/****************************************
 public static String getHtmlSelectDate(int iStartYear, int iStartMonth, int iStartDay,
                                           int iEndYear, int iEndMonth, int iEndDay)
 public static String getHtmlSelectDate()
    generate html code used to select start date and end date in Form List Page

 Parameters:
    all the parameters given are the default value
    if no parameters provided, system will set default value automatically

 Returns:
    String: the html code

 Throws:

 Changes:

*****************************************/
    public static String getHtmlSelectDate(int iStartYear, int iStartMonth, int iStartDay,
                                           int iEndYear, int iEndMonth, int iEndDay)
    {
        String sHtml = "请选择查询时间：开始日期 ";
        sHtml += "<input type=\"text\" class=textfield name=\"StartDate\" size=\"20\" onFocus=\"setday(this)\" value=\"" + iStartYear + "-" + iStartMonth + "-" + iStartDay + "\" >";
//        sHtml += getHtmlSelect("StartYear", 2001, 2003, iStartYear, "年");
//        sHtml += getHtmlSelect("StartMonth", 1, 12, iStartMonth, "月");
//        sHtml += getHtmlSelect("StartDay", 1, 31, iStartDay, "日");

        sHtml += "&nbsp;结束日期 ";

        sHtml += "<input type=\"text\" class=textfield name=\"EndDate\" size=\"20\" onFocus=\"setday(this)\" value=\"" + iEndYear + "-" + iEndMonth + "-" + iEndDay + "\" >";
//        sHtml += getHtmlSelect("EndYear", 2001, 2003, iEndYear, "年");
//        sHtml += getHtmlSelect("EndMonth", 1, 12, iEndMonth, "月");
//        sHtml += getHtmlSelect("EndDay", 1, 31, iEndDay, "日");
        return sHtml;
    }

    public static String getHtmlSelectDate()
    {
        return getHtmlSelectDate(2002, 1, 1, 2002, 1, 1);
    }

    public static String getHtmlSelOneDate(int iYear, int iMonth, int iDate) {
        String sHtml = "";
        sHtml += "<input type=\"text\" class=textfield name=\"SelectDate\" size=\"20\" onFocus=\"setday(this)\" value=\"" + iYear + "-" + iMonth + "-" + iDate + "\" >";
//        sHtml += getHtmlSelect("Year", getCurYear(), getCurYear() + 1, iYear, "年");
//        sHtml += getHtmlSelect("Month", 1, 12, iMonth, "月");
//        sHtml += getHtmlSelect("Date", 1, 31, iDate, "日");
        return sHtml;
    }
    public static String getHtmlSelOneDate() {
        return getHtmlSelOneDate(getCurYear(), getCurMonth(), getCurDay());
    }

    public static String getHtmlPageListSubmit(String sLink, String sOrderBy, String sStartDate, String sEndDate)
    {
        String sHtml = "<form name=Form_PageList action="+ sLink +" method=post onsubmit='doPageListSubmit();'>";
        // hide fields for this form
        sHtml += "<input type=hidden name=PageID value=''>";
        sHtml = sHtml + "<input type=hidden name=OrderBy value='"+ sOrderBy +"'>";
//        sHtml += "<input type=hidden name=StartDate value=''>";
//        sHtml += "<input type=hidden name=EndDate value=''>";
        sHtml += "<input type=hidden name=CnOrderBy value=''>";

        if (sStartDate == null || sEndDate == null)
        {
            sHtml+= getHtmlSelectDate();
        } else
            sHtml += getHtmlSelectDate(DateUtility.getDate(sStartDate, "y"),
                                   DateUtility.getDate(sStartDate, "m"),
                                   DateUtility.getDate(sStartDate, "d"),
                                   DateUtility.getDate(sEndDate, "y"),
                                   DateUtility.getDate(sEndDate, "m"),
                                   DateUtility.getDate(sEndDate, "d"));
        sHtml += "<input type='submit' class=button name='Submit' value='查询'></form>";

        return sHtml;
    }

/****************************************
 public static String getSelectStartDate(javax.servlet.http.HttpServletRequest request)
 public static String getSelectEndDate(javax.servlet.http.HttpServletRequest request)
    从request中获取并构造类似"2002-1-1"的日期

 Parameters:
   HttpServletRequest request, the request object which include the data included

 Returns:
    String: the data string like "2002-1-1"

 Throws:

 Changes:

*****************************************/
    public static String getSelectStartDate(javax.servlet.http.HttpServletRequest request)
    {
        try
        {
            String sStartDate = (String) request.getAttribute("StartDate");
          return sStartDate;
        }
        catch (Throwable t)
        {
            return null;
        }
    }

    public static String getSelectEndDate(javax.servlet.http.HttpServletRequest request)
    {
        try
        {
            String sEndDate = (String) request.getParameter("EndDate");
          return sEndDate;
        }
        catch (Throwable t)
        {
            return null;
        }
    }

    public static String getSelectOrderByString(javax.servlet.http.HttpServletRequest request)
    {
        try
        {
            String sOrderBy = (String)request.getParameter("OrderBy");
            return sOrderBy;
        }
        catch (Throwable t)
        {
            return null;
        }
    }

    public static int getCurYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.YEAR);
    }
    public static int getCurMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.MONTH) + 1;
    }
    public static int getCurDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.DATE);
    }

    public static String getCurDate6() {
        Calendar cal = Calendar.getInstance();
        int iYear = cal.get(cal.YEAR) % 100;
        int iMonth = cal.get(cal.MONTH) + 1;
        int iDay = cal.get(cal.DATE);
        String sDate = "";
        if (iYear < 10)	{
            sDate = "0";
        }
        sDate += iYear;
        if (iMonth < 10) {
            sDate += "0" + iMonth;
        } else
            sDate += iMonth;
        if (iDay < 10) {
            sDate += "0" + iDay;
        } else
            sDate += iDay;
        return sDate;
    }

    public static String getCurDate()
    {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.YEAR) + "年" + getCurMonth() + "月" + cal.get(cal.DATE) + "日";
    }


    public static String getCurTime()
    {
        Calendar cal = Calendar.getInstance();
        int iHour = cal.get(cal.HOUR_OF_DAY);
        int iMinute = cal.get(cal.MINUTE);
        int iSecond = cal.get(cal.SECOND);
        String sTime = "" + iHour;
        if (iHour < 10)	{
            sTime = "0" + iHour;
        }
        if (iMinute < 10) {
            sTime += ":0" + iMinute;
        } else {
            sTime += ":" + iMinute;
        }
        if (iSecond < 10) {
            sTime += ":0" + iSecond;
        } else {
            sTime += ":" + iSecond;
        }
        return  sTime;
    }

    public static String getHtmlSelectEngMen(String sDefaultManID) {
        String sHtml = "";

        return sHtml;
    }

    public static String getHtmlSelBrand() {
      String sHtml = "";

      return sHtml;
    }

    public static String getHtmlSelWareHouse(String sCompID, String sWareHouseID) {
      String sHtml = "";

      return sHtml;
    }

    public static String getEmpIDFromIDName(String sIDName) {
        return getIDFromIDName(sIDName);
    }
    public static String getEmpNameFromIDName(String sIDName) {
        return getNameFromIDName(sIDName);
    }

    public static String getIDFromIDName(String sIDName) {
        int iColonPos = sIDName.indexOf(":");
        String sID = sIDName.substring(0, iColonPos);
        return sID;
    }
    public static String getNameFromIDName(String sIDName) {
        int iColonPos = sIDName.indexOf(":");
        String sName = sIDName.substring(iColonPos + 1);
        return sName;
    }


    /*
    give a float,we format is as xxx.xx, and return
    */
    public static String getCashFloat(float fCash) {
        String sCash = String.valueOf(Math.round(100 * fCash) * 1.0 / 100);
        if (sCash.indexOf('.') == -1) {
            sCash += ".00";
        } else
        if (sCash.indexOf('.') == sCash.length() - 2) {
           sCash += "0";
        }
        return (sCash);
    }
    public static String getCashFloat(double fCash) {
        String sCash = String.valueOf(Math.round(100 * fCash) * 1.0 / 100);
        if (sCash.indexOf('.') == -1) {
            sCash += ".00";
        } else
        if (sCash.indexOf('.') == sCash.length() - 2) {
           sCash += "0";
        }
        return (sCash);
    }
   public static String getFloatEx(float fValue, int Decimal) {

    float f = Math.round(fValue * 10 * Decimal);
     f = f / 10 / Decimal;
    String fStr = String.valueOf(f);
    if (fStr.indexOf('.') == -1) {
        fStr = fStr + ".";
        for (int i = 0; i < Decimal; i++) {
            fStr += "0";
        }
    } else if (fStr.indexOf('.') > fStr.length() - Decimal - 1) {
        for (int i = fStr.length() - fStr.indexOf('.'); i <= Decimal; i++) {
          fStr += "0";
        }
    }
    return (fStr);
}
}
