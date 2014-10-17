package com.sosgps.wzt.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sos.helper.SpringHelper;


public class PagedRSforSelect_notitle extends PagedRS_notitle{
	private static Log log = LogFactory.getLog(PagedRSforSelect_notitle.class);
    public PagedRSforSelect_notitle() //construct function
    {
    }
    


    private String[] asRecordBGColor = null;

    private int[] aiFieldLength = null;

    private boolean bNeedSelectField = true; // 是否需要选择列

    private String[] asBtnImg = null; //button的图片文件

    private String[] asBtnLink = null; //button的链接

    private Vector vtSearchField = new Vector();

    private String sSearchConDesc = ""; // 查询条件, 在getSearchSqlWhere()中构造,

    // 在getHtmlDataHead()中显示

    private String sSqlWhere = ""; // sql 查询语句中的条件部分

    private String sHtmlSearchCon = ""; //查询条件部分的html代码
    
    private String m_sLabel="";
    
    public void setLabel(String label) {
        this.m_sLabel = label;
    }

    /* 设置记录的背景颜色，为长度是2的数组 */
    public void setRecordBGColors(String[] bgColor) {
        if (bgColor.length == 2)
            asRecordBGColor = bgColor;
    }

    /* 设置各个属性字段的表列宽度 */
    public void setFieldLengths(int[] aiLength) {
        if (aiLength.length > 0)
            aiFieldLength = aiLength;
    }

    /* 设置是否需要选择列 */
    public void setNeedSelectField(boolean isNeed) {
        bNeedSelectField = isNeed;
    }

    /* 设置button的链接 */
    public void setButtonLink(String[] imgFile, String[] imgLink) {
        if (imgFile != null && imgLink != null) {
            if (imgFile.length == imgLink.length) {
                asBtnImg = imgFile;
                asBtnLink = imgLink;
            } else {
                //System.err.println("Button file names and its links are NOT
                // consistent, setButtonLink() ignored");
            }
        }
    }

    public String getHtmlRecordList(String[] asCnPropNames,
            boolean[] bIsOrderField) {
    	bNeedSelectField=false;
    	isPrint=false;
        //generate javascript for setOrderBy() function
        String sArrayValues = "asCnOrderBy = new Array('" + asCnPropNames[0]
                + "'";
        String sJSCode = "<script language=javascript>function setOrderBy(iOrderByID, bIsAscend)"
                + "{asOrderBy = new Array('" + asRequiredProperties[0] + "'";

        for (int i = 1; i < asCnPropNames.length; i++) {

            sArrayValues = sArrayValues + ",'" + asCnPropNames[i] + "'";
            sJSCode = sJSCode + ",'" + asRequiredProperties[i] + "'";
        }

        sJSCode = sJSCode
                + ");"
                + sArrayValues
                + ");"
                + " asAsc = new Array(' asc ', ' desc ');"
                + "asCnAsc = new Array('升序', '降序');"
                + "Form_PageList.OrderBy.value = asOrderBy[iOrderByID] + asAsc[bIsAscend];"
                + "doAfterPrint(); "
                + "Form_PageList.CnOrderBy.value = '按' + asCnOrderBy[iOrderByID] + asCnAsc[bIsAscend] + '排列';"
                + "}</script>";

        /*
         * function setOrderBy(iOrderByID, bIsAscend) { asOrderBy = new
         * Array("a", "b"); asCnOrderBy = new Array("c", "d"); asAsc = new
         * Array(' asc ', ' desc '); asCnAsc = new Array('顺序', '逆序');
         * Form_PageList.OrderBy.value = asOrderBy + asAsc[iOrderByID];
         * Form_PageList.CnOrderBy.value = "按" + asCnOrderBy[iOrderByID] +
         * asAscend[iIsAscend] + "排列"; }
         */
        String sHtml;
        if (isPrint) {
            sHtml = "<table width="
                    + sTotalWidth
                    + " border=0 cellspacing=0 cellpadding=1 "
                    + "bordercolorlight=#999999 bordercolordark=#FFFFFF align=center class=\"conlist\">"
                    //                    + "<form name=frmDataList method=post
                    // action=\"../list/List.do?action=deleteAll\">"
                    + sHidenFieldForCancelDel + sJSCode + "<tr class=\"list\">";
        } else {
            sHtml = "<table width="
                    + sTotalWidth
                    + " border=1 cellspacing=0 cellpadding=1 "
                    + "bordercolorlight=#999999 bordercolordark=#FFFFFF align=center class=\"conlist\">"
                    //                    + "<form name=frmDataList method=post
                    // action=\"../list/List.do?action=deleteAll\">"
                    + sHidenFieldForCancelDel + sJSCode + "<tr class=\"list\">";
        }
        if (bNeedSelectField)
            sHtml += "<td width=30 align=center>"
                    + "选择</td>";

        if (isPrint) {
            for (int i = 0; i < asCnPropNames.length; i++) {
                String sWidth = "";
                if (aiFieldLength != null
                        && aiFieldLength.length == asCnPropNames.length)
                    sWidth = " width=" + aiFieldLength[i] + "";
                sHtml = sHtml + "<td bgcolor=#B6B6B6 " + sWidth
                        + " align=left>" + "<font color=#000066>"
                        + asCnPropNames[i] + "</font></td>";
            }
        } else {
            for (int i = 0; i < asCnPropNames.length; i++) {
                String sWidth = "";
                if (aiFieldLength != null
                        && aiFieldLength.length == asCnPropNames.length)
                    sWidth = " width=" + aiFieldLength[i] + "";
                if (bIsOrderField[i]) {
                    sHtml = sHtml
                            + "<td "
                            + sWidth
                            + " >"
                            + "<table width=100% border=0 cellpadding=0 cellspacing=0>"
                            + "<tr><td rowspan=2 class=\"tdlist\">"
                            + asCnPropNames[i]
                            + "</td><td valign=bottom>"
                            + "<a href=# onclick=\"setOrderBy("
                            + String.valueOf(i)
                            + ", 0);doPageListSubmit();\">"
                            /*
                             * windyang replace 06/01/10
                             */
                            //+ "<img src=../images/ArrowUp.gif width=10 height=10 border=0></a>"
                            + "<img src=/images/ArrowUp.gif width=10 height=10 border=0></a>"
                            /*
                             * windyang end
                             */
                            + "</td></tr><tr><td valign=top>"
                            + "<a href=# onclick=\"setOrderBy("
                            + String.valueOf(i)
                            + ", 1);doPageListSubmit();\">"
                            /*
                             * windyang replace 06/01/10
                             */
                            //+ "<img src=../images/ArrowDown.gif width=10 height=10 border=0></a>"
                            + "<img src=/images/ArrowDown.gif width=10 height=10 border=0></a>"
                            /*
                             * windyang end
                             */
                            + "</td></tr></table></td>";

                } else {
                    sHtml = sHtml + "<td " + sWidth
                            + " >" 
                            + asCnPropNames[i] + "</td>";
                }
            }
        }
        sHtml += "</tr><tr align=center> </tr>";

        String[] asColor = { "#F3F3F3", "#E0E0E0" };
        if (asRecordBGColor != null)
            asColor = asRecordBGColor;

        if (vRSData == null) {
            vRSData = new Vector();
        }
        for (int i = 0; i < vRSData.size(); i++) {
            Hashtable htIC = (Hashtable) vRSData.elementAt(i);
            //windyang replace 
            //sHtml = sHtml + "<tr bgcolor=" + asColor[i % 2] + ">";  
            sHtml = sHtml +"<tr onMouseOver=\"javascript:this.bgColor='#ffffff';\" onMouseOut=\"javascript:this.bgColor='#f3f3f3';\">";         
            //windyang replace end
            
            
            
            if (bNeedSelectField)
                sHtml += "<td align=center>"
                        + "<input type='checkbox' name='ids' value='"
                        // + (String) htIC.get(asRequiredProperties[0]) + "'>"
                        + htIC.get("id") + "'>"

                        + "</td>";

            if (isPrint || sLinkViewForm == null || sLinkViewForm.equals("")) { // without
                // view
                // link
                sHtml += "<td>" + (String) htIC.get(asRequiredProperties[0])
                        + "</td>";
            } else
                sHtml = sHtml + "<td><a href=\"javascript:_selected('"

                        + htIC.get("id")+"|"+htIC.get("label_")
                        //Updated by ALex finish
                        + "')\">"
                        + (String) htIC.get(asRequiredProperties[0])
                        + "</a></td>";
            for (int j = 1; j < asRequiredProperties.length; j++) {
                sHtml = sHtml + "<td>&nbsp;"
                        + htIC.get(asRequiredProperties[j]) + "</td>";
            }
            sHtml += "</tr>";
        }

        sHtml += "</form></table>";

        return sHtml;
    }

    /*
     * 得到页面链接部分的html代码，包括刷新，查看，作废，打印
     */
//    public String getHtmlPageLink() {
//        String sHtml = "<table width=100% border=0 cellspacing=0 cellpadding=0"
//                + " align=center height=30><tr><td align=right bgcolor=#BFBFBF>";
//        if (asBtnImg != null && asBtnLink != null) {
//            int i = 0;
//            for (i = 0; i < asBtnImg.length - 1; i++) {
//                sHtml += "<a href=\"" + asBtnLink[i] + "\"><img src=\""
//                        + asBtnImg[i] + "\" border=0></a>&nbsp;";
//            }
//            sHtml += "<a href=\"" + asBtnLink[i] + "\"><img src=\""
//                    + asBtnImg[i] + "\" border=0></a>";
//        } else { // set default link value
//            sHtml += "<a href='javascript:location.reload();'>"
//                    + "<img src=../images/043.gif width=60 height=22 border=0></a>&nbsp;"
//                    + "<a href='"
//                    + sLinkCancelForm
//                    + "'><img src=images/057.gif width=60 height=22 border=0></a>&nbsp;"
//                    + "<a href='javascript:window.print();'><img src=../images/044.gif width=60 height=22 border=0></a>";
//        }
//        sHtml += "</td></tr></table>";
//        return sHtml;
//    }

    public String getHtmlPageListSubmit() {
        String sHtml = "<table width=" + sTotalWidth
                + " border=0 cellpadding=0 cellspacing=0 align=center><tr><td>"
                + "<form name=Form_PageList action=" + sLink + " method=post>";

        // hide fields for this form
        sHtml += "<input type=hidden name=PageID value=''>";
        sHtml += "<input type=hidden name=OrderBy value='"
                + StringUtility.null2Str(sOrderBy) + "'>";
        sHtml += "<input type=hidden name=CnOrderBy value='"
                + StringUtility.null2Str(sCnOrderBy) + "'>";
        //        sHtml += "<input type=hidden name=StartDate value=''>";
        //        sHtml += "<input type=hidden name=EndDate value=''>";
        sHtml += "<input type=hidden name=print value=''>";

        sHtml += sHtmlSearchCon;

        if (vtSearchField.size() > 0) {
            sHtml += "&nbsp;&nbsp;<input type=button name=Submit value='查询' onclick=\"javascript:doPageListSubmit();\">";
        }

        //        sHtml += "</form></td></tr></table>";
        sHtml += "</td></tr></table>";
        return sHtml;
    }

    public String getHtmlDataHead() {
        return "<table width=" + sTotalWidth
                + " border=0 cellspacing=3 cellpadding=0 align=center><tr><td>"
                + "记录条数 " + iRecordCount + ", " + sSearchConDesc //+ ", " +

                + "</td><td align=right>" 
                + "</td></tr></table>";
//                + "</td><td align=right>" + getHtmlPageCode()
//                + getHtmlGo2Page("1") + "</td></tr></table>";
    }

    /*
     * initialize paged recordset
     */
    public void init(javax.servlet.http.HttpServletRequest request,
            String sQuerySql, String sDefaultOrderBy, boolean[] abIsOrderField,
            String[] asPropCnNames, String[] asRequiredProperties,
            String sLink, String sLinkViewForm, String sLinkCancelForm,
            String sCnFormName, String sCnDefaultOrderBy) {

        // check if want to print this page
        isPrint = StringUtility.null2Str(request.getParameter("print"))
                .equals("true");
        setNeedSelectField(!isPrint);

        String sPageID = (String) request.getParameter("PageID");
        sOrderBy = (String) request.getParameter("OrderBy");

        try {
            iPageID = Integer.parseInt(sPageID);
        } catch (Throwable t) {
            iPageID = 1;
        }

        this.sQuerySql = sQuerySql;
        this.sDefaultOrderBy = sDefaultOrderBy;

        this.abIsOrderField = abIsOrderField;

        this.asPropCnNames = asPropCnNames;
        this.asRequiredProperties = asRequiredProperties;

        this.sLink = sLink;
        this.sLinkViewForm = sLinkViewForm;
        this.sLinkCancelForm = sLinkCancelForm;

        this.sCnFormName = sCnFormName;
        this.sCnOrderBy = sCnDefaultOrderBy;
        if (request.getParameter("CnOrderBy") != null)
            sCnOrderBy = (String) request
                    .getParameter("CnOrderBy");
        // process SqlField

        processSqlField(request);

        if (sOrderBy == null) {
            this.sQuerySql = sQuerySql + sSqlWhere + " order by "
                    + sDefaultOrderBy;
            sOrderBy = sDefaultOrderBy;
        } else
            this.sQuerySql = sQuerySql + sSqlWhere + " order by " + sOrderBy;

        //        System.out.println("SQL after PagedRS.init():" + this.sQuerySql);
        getPagedRSBySql();
    }

    /*
     * FieldTag 0 date yyyy-mm-dd FieldTag 1 date yyy-mm FieldTag 2 number
     * 
     * other default
     */
    public void addSearchField(String sItem, String[] enFieldName,
            String[] cnFieldName, String fieldTag, String fieldName) {
        Hashtable htSearchField = new Hashtable();
        htSearchField.put("Item", StringUtility.null2Str(sItem));
        htSearchField.put("EnNames", enFieldName);
        htSearchField.put("CnNames", cnFieldName);
        htSearchField.put("FieldTag", fieldTag);
        if (fieldName != null)
            htSearchField.put("FieldName", fieldName);
        vtSearchField.add(htSearchField);
    }

    public void processSqlField(javax.servlet.http.HttpServletRequest request) {

        String[] sFieldName = request.getParameterValues("FieldName");
        String[] sFieldValue = request.getParameterValues("FieldValue");

        String fieldname = null;
        String fieldvalue = null;
        String[] enNames = null;
        String[] cnNames = null;
        int fieldTag;
        String item = null;
        String FieldName = null;

        sStartDate = (String) request.getParameter("StartDate");
        sEndDate = (String) request.getParameter("EndDate");

        Calendar cal = Calendar.getInstance();
        int iMonth = cal.get(cal.MONTH) + 1;
        String sCurDate = cal.get(cal.YEAR) + "-" + iMonth + "-"
                + cal.get(cal.DATE);

        // after i get money from company, these code should be removed :-)
        //if (cal.get(cal.DATE) > 8) return;
        //

        if (sStartDate == null || sStartDate.length() < 4) {
            sStartDate = sCurDate;
        }
        if (sEndDate == null || sEndDate.length() < 4)
            sEndDate = sCurDate;

        /* check if it is the first time to invoke this page list */

        for (int i = 0; i < vtSearchField.size(); i++) {
            Hashtable htSearchField = (Hashtable) vtSearchField.elementAt(i);
            enNames = (String[]) htSearchField.get("EnNames");
            cnNames = (String[]) htSearchField.get("CnNames");
            FieldName = (String) htSearchField.get("FieldName");

            fieldTag = Integer.parseInt((String) htSearchField.get("FieldTag"));
            item = (String) htSearchField.get("Item");

            if (sFieldName == null || sFieldName.length < 1) {
                if (FieldName == null) {
                    fieldname = enNames[0];
                    fieldvalue = (fieldTag == 0 || fieldTag == 1) ? "" : "%";
                } else {
                    fieldvalue = enNames[0];
                    fieldname = FieldName;
                }
            } else {
                fieldname = sFieldName[i]; // get from request
                fieldvalue = sFieldValue[i]; //get from request
            }

            sHtmlSearchCon += item + "&nbsp;";

            if (fieldTag == 0 || fieldTag == 1) {

                if (fieldTag == 0) { // date, yyyy-mm-dd
                    sSqlWhere += " and " + fieldname + "  >= to_date('"
                            + sStartDate + "', 'yyyy-mm-dd') " + " and "
                            + fieldname + " < to_date('" + sEndDate
                            + "', 'yyyy-mm-dd') + 1 ";
                    sSearchConDesc += "&nbsp;"
                            + getCnNameByEnName(fieldname, enNames, cnNames)
                            + " 从" + sStartDate + "到" + sEndDate + "&nbsp;";
                    sHtmlSearchCon += WebTool.getHtmlSelectDate(DateUtility
                            .getDate(sStartDate, "y"), DateUtility.getDate(
                            sStartDate, "m"), DateUtility.getDate(sStartDate, "d"),
                            DateUtility.getDate(sEndDate, "y"), DateUtility.getDate(
                                    sEndDate, "m"), DateUtility.getDate(sEndDate,
                                    "d"));

                } else { //date yyyy-mm
                    sSqlWhere += " and " + fieldname + "  >= to_date('"
                            + sStartDate + "', 'yyyy-mm-dd') " + " and "
                            + fieldname + " < add_months(to_date('" + sEndDate
                            + "', 'yyyy-mm-dd'), 1) ";
                    sSearchConDesc += "&nbsp;"
                            + getCnNameByEnName(fieldname, enNames, cnNames)
                            + " 从"
                            + sStartDate.substring(sStartDate.lastIndexOf('-'))
                            + "到"
                            + sEndDate.substring(sEndDate.lastIndexOf('-'))
                            + "&nbsp;";
                    // wait for later process on sHtmlSearchCon;
                }
            } /* end date field */
            else if (fieldTag == 2) { // number field
                sSqlWhere += " and to_number(" + fieldname + ") = "
                        + fieldvalue;
                sSearchConDesc += "&nbsp;"
                        + getCnNameByEnName(fieldname, enNames, cnNames) + " "
                        + fieldvalue + "&nbsp;";
            } else if (fieldTag == 9) { // number field
                sSqlWhere += " and trim(" + fieldname + ") = '" + fieldvalue
                        + "'";
                if (FieldName == null) {
                    //sSearchConDesc += "&nbsp;" + getCnNameByEnName(fieldname,
                    // enNames, cnNames) + " " + fieldvalue + "&nbsp;";
                } else {
                    //sSearchConDesc += "&nbsp;" + item + "&nbsp;" +
                    // getCnNameByEnName(fieldvalue, enNames, cnNames) +
                    // "&nbsp;";
                }
            } else { // default string field
                //Updated By Alex.Lv on 2005-01-24
                if (fieldvalue != null && fieldvalue.length() > 0) {
                    if (!fieldvalue.equals("%")) {
                        sSqlWhere += " and trim(" + fieldname + ") like '%"
                                + fieldvalue + "%'";
                    } else {
                        sSqlWhere += " and (trim(" + fieldname + ") like '%"
                                + fieldvalue + "%' or trim(" + fieldname
                                + ") is null)";
                    }
                }
                if (FieldName == null) {
                    //sSearchConDesc += "&nbsp;" + getCnNameByEnName(fieldname,
                    // enNames, cnNames) + " " + fieldvalue + "&nbsp;";
                } else {
                    //sSearchConDesc += "&nbsp;" + item + "&nbsp;" +
                    // getCnNameByEnName(fieldvalue, enNames, cnNames) +
                    // "&nbsp;";
                }
            }

            // process field value
            if (fieldTag == 0 || fieldTag == 1) {
                sHtmlSearchCon += "<input type=hidden name=FieldValue value=>";
            } else if (FieldName == null) {
                sHtmlSearchCon += "<input type=text size=10 name=FieldValue value='"
                        + (fieldvalue.equals("%") ? "" : fieldvalue) + "'>";
            } else { // FieldName != null
                sHtmlSearchCon += "<input type=hidden name=FieldName value='"
                        + FieldName + "'>"; //
            }

            // process field name
            if (enNames.length == 1) {
                if (FieldName == null) {
                    sHtmlSearchCon += "<input type=hidden name=FieldName value="
                            + enNames[0] + ">";
                } else {
                    sHtmlSearchCon += "<input type=hidden name=FieldValue value='"
                            + enNames[0] + "'>";
                }
            } else { // select on search field from multi search field
                if (FieldName == null) {
                    sHtmlSearchCon += "&nbsp;<select name=FieldName>";
                    for (int k = 0; k < enNames.length; k++) {
                        sHtmlSearchCon += "<option value=" + enNames[k];
                        if (enNames[k].equals(fieldname)) {
                            sHtmlSearchCon += " selected ";
                        }
                        sHtmlSearchCon += ">" + cnNames[k].trim() + "</option>";
                    }
                    sHtmlSearchCon += "</select>&nbsp; ";
                } else {
                    sHtmlSearchCon += "&nbsp;<select name=FieldValue>";
                    for (int k = 0; k < enNames.length; k++) {
                        sHtmlSearchCon += "<option value=" + enNames[k];
                        if (enNames[k].equals(fieldvalue)) {
                            sHtmlSearchCon += " selected ";
                        }
                        sHtmlSearchCon += ">" + cnNames[k].trim() + "</option>";
                    }
                    sHtmlSearchCon += "</select>&nbsp; ";
                }
            }
            if ((fieldTag == 0 || fieldTag == 1) && vtSearchField.size() > 1) {
                sHtmlSearchCon += "<br>";
            }

        }
    }

    private String getCnNameByEnName(String enName, String[] enNames,
            String[] cnNames) {
        for (int i = 0; i < enNames.length; i++) {
            if (enNames[i].equals(enName))
                return cnNames[i];
        }
        return "";
    }

    public void setTotalWidth(String sTotalWidth) {
        this.sTotalWidth = sTotalWidth;
    }

    private String covertString(String srcStr) {
        String newStr = srcStr;
        try {
            newStr = new String(srcStr.getBytes("8859_1"), "gb2312");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newStr;
    }

    /*
     * added by webyeah 2002-8-14 20:54, this code is for cancel form or delete
     * form
     */
    public void setCancelFormParam(Hashtable htParam) {
        //        String sKey = null;
        //        for (Enumeration e = htParam.keys() ; e.hasMoreElements() ;) {
        //            sKey = (String) e.nextElement();
        //            sHidenFieldForCancelDel += "<input type=hidden name='" + sKey + "'
        // value='"
        //                + htParam.get(sKey) +"'>";
        //        }

        sHidenFieldForCancelDel += "<input type=hidden name='clazz'>";
        //sHidenFieldForCancelDel += "<input type=hidden name='ids'>";
        sHidenFieldForCancelDel += "<input type=hidden name='forward'>";
    }

    String sHidenFieldForCancelDel = "";
    /* end 2002-8-14 20:55 */
    
	public String getHtmlBody() {
		isPrint=false;
		if (isPrint) {
			String sPrintJS = "<script language=javascript>window.print();</script>";
			return getHtmlViewTitle() + "<br>" + getHtmlDataHead() + "<br>"
					+ getHtmlRecordList(asPropCnNames, abIsOrderField)
					+ getHtmlDataTail() + sPrintJS;
		}
		/*
		return getHtmlPageLink() + getHtmlViewTitle() + "<br>"
				+ getHtmlPageListSubmit() + getHtmlDataHead() + "<br>"
				+ getHtmlRecordList(asPropCnNames, abIsOrderField)
				+ getHtmlDataTail();
	    */
		return  getHtmlViewTitle() + "<br>"
		+ getHtmlPageListSubmit() + getHtmlDataHead() + "<br>"
		+ getHtmlRecordList(asPropCnNames, abIsOrderField)
		+ getHtmlDataTail();
	}
	
	/* give Query Sql String, and set related properties here */
	public void getPagedRSBySql() {
		Connection conn = null;
		try {
			// connect to database
			// get results set
			// conn = getConnection();
			//session = HibernateUtil.currentSession();
			conn = ( (DataSource)SpringHelper.getBean("dataSource")).getConnection();
			Statement stmt = conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = null;

			log.info(sQuerySql);
			System.out.println(sQuerySql);
			rs = stmt.executeQuery(sQuerySql);

			if (rs == null) {
				//System.err.println("Database operation error:" + sQuerySql);
				return;
			}
			if (!rs.next()) {
				vRSData = new Vector();
				return;
			}

			rs.last();
			iRecordCount = rs.getRow();
			rs.first();

			PageBean objPB = new PageBean();
			objPB.setRecordCount(iRecordCount);
			if (iPageSize != 0) {
				objPB.setPageSize(iPageSize);
			}

			objPB.reset(iPageID);

			this.iPageID = iPageID; //set page no.
			this.iPageCount = objPB.getPageCount(); //set page count

			//Updated by Alex.
			ResultSetMetaData meta = rs.getMetaData();
			int colCount = meta.getColumnCount();
			String[] types = new String[asRequiredProperties.length];
			for (int i = 0; i < asRequiredProperties.length; i++) {
				String propertyName = asRequiredProperties[i];
				for (int j = 0; j < colCount; j++) {

					String colName = meta.getColumnName(j + 1);
					if (colName != null
							&& colName.equalsIgnoreCase(propertyName)) {
						types[i] = meta.getColumnTypeName(j + 1);
//						System.out.println("--------types[" + i + "] = "
//								+ types[i] + "\tcolName = " + colName);
						break;
					}
				}
			}
			//Updated by Alex finish.
			vRSData = new Vector();
			for (int i = objPB.getStartId(); i < objPB.getEndId(); i++) {
				rs.absolute(i + 1);
				Hashtable htRS = new Hashtable();
				//updated by Alex.Lv 2005-01-20
				htRS.put("id", new Long(rs.getLong(pkField)));
				htRS.put("label_",StringUtility.null2Str(rs.getString(m_sLabel)));
				//updated by Alex.Lv 2005-01-20 finish
				for (int j = 0; j < asRequiredProperties.length; j++) { //add
					// properties
					// to
					// hashtable

					if (rs.getString(asRequiredProperties[j]) == null) {

						htRS.put(asRequiredProperties[j], StringUtility.null2Str(rs
								.getString(asRequiredProperties[j])));
					} else {
						if ("DATE".equalsIgnoreCase(types[j])) {
							htRS.put(asRequiredProperties[j], DateUtility
									.dateToStrCh(rs
											.getDate(asRequiredProperties[j])));
						} else if ("MONEY"
								.equalsIgnoreCase(asRequiredProperties[j])
								|| asRequiredProperties[j].endsWith("price")) {
							htRS
									.put(
											asRequiredProperties[j],
											NumberUtility
													.formatDouble(rs
															.getDouble(asRequiredProperties[j])));
						} else {
							htRS.put(asRequiredProperties[j], (String) rs
									.getString(asRequiredProperties[j]));
						}
					}
				}
				vRSData.addElement(htRS); //add one record to vector
			}
		} catch (Throwable t) {
			//System.err.println("error:" + sQuerySql);
			t.printStackTrace(System.err);
		} finally {
			try {

//				HibernateUtil.closeSession(session);
			} catch (Exception e) {

			}
		}
	}

	public String getHtmlDataTail() {
		return "<table width=" + sTotalWidth
				+ " border=0 cellspacing=3 cellpadding=0 align=center>"
				+ "<tr><td>&nbsp;</td><td align=right>" 
				+ "</td></tr></table><table width=" + sTotalWidth
//				+ "<tr><td>&nbsp;</td><td align=right>" + getHtmlPageCode()
//				+ getHtmlGo2Page("2") + "</td></tr></table><table width=" + sTotalWidth
				+ " border=0 cellspacing=3 cellpadding=0 align=center>"
				+ "<tr><td align=center><a href=\"javascript:window.close()\">&lt; 关闭 &gt;</a></td></tr></table>";
	}
	
	public String getHtmlViewTitle() {
		String sHtml = "<table width=100% border=0 cellspacing=0 cellpadding=2 align=center>"
				+ "<tr> <td bgcolor=#FFB926 height=25><b><font color=#999999>"
				+ sCnFormName + "列表</font></b></td></tr><tr><td > &nbsp; &nbsp;</td>  </tr></table>";

		return sHtml;
	}	
	
}
