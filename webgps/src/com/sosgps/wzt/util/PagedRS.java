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


public class PagedRS extends Object {
	private static Log log = LogFactory.getLog(PagedRS.class);
	private ConnectService connectService;
	public PagedRS() //construct function
	{
	}

	//查询条件中日期字段名
	public String sDateFieldName = null;

	// 查询条件列表
	public String[] sSearchFieldCnNames = { },
			sSearchFieldEnNames = { };

	public int iRecordCount = 0;

	public int iPageID = 0;

	public int iPageCount = 0;

	public int iPageSize = 0;

	public String sQuerySql = null;

	public String sOrderBy = null;

	public String sCnOrderBy = null;

	public String sDefaultOrderBy = null;
	
	public String sDefaultGroupby = null;

	public Vector vRSData = null;

	public String[] asRequiredProperties = null;

	public String[] asPropCnNames = null;

	public boolean[] abIsOrderField;
    public String pkField;

	public String sStartDate = null, sEndDate = null;

	public String sLink, //当前页面
			sLinkViewForm, //查看表单
			sLinkCancelForm; //作废表单

	public String sWebTitle;

	public String sCnFormName;

	public boolean isPrint = false; // 是否打印本页

	public String sTotalWidth = "750"; // total form width, default is 750

	/* give Query Sql String, and set related properties here */
	public void getPagedRSBySql() {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = ( (DataSource)SpringHelper.getBean("dataSource")).getConnection();
			 stmt = conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			

			log.info(sQuerySql);

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
				htRS.put("id", rs.getString(pkField));
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
				if (rs != null){
					rs.close();
				}
				if (stmt !=null){
					stmt.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (Exception e) {
				log.error("PagedRS查询异常", e);
			}
		}
	}

	public String getHtmlRecordList(String[] asCnPropNames,
			boolean[] bIsOrderField) {
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
		String sHtml = "";
		if (isPrint) {
			sHtml = "<table width="
					+ sTotalWidth
					+ " border=0 cellspacing=0 cellpadding=1 "
					+ "bordercolorlight=#999999 bordercolordark=#FFFFFF align=center>"
					+ "<form name=frmDataList method=post action=>"
					+ sJSCode
					+ "<tr><td rowspan=2 bgcolor=#C4DCFD width=40 align=center>"
					+ "<font color=#000066>选择</font></td>";

			for (int i = 0; i < asCnPropNames.length; i++) {

				sHtml = sHtml
						+ "<td rowspan=2 bgcolor=#C4DCFD width=123 align=center>"
						+ "<font color=#000066>" + asCnPropNames[i]
						+ "</font></td>";

			}

		} else {
			sHtml = "<table width="
					+ sTotalWidth
					+ " border=1 cellspacing=0 cellpadding=1 "
					+ "bordercolorlight=#999999 bordercolordark=#FFFFFF align=center>"
					+ "<form name=frmDataList method=post action=>"
					+ sJSCode
					+ "<tr><td rowspan=2 bgcolor=#C4DCFD width=40 align=center>"
					+ "<font color=#000066>选择11</font></td>";

			for (int i = 0; i < asCnPropNames.length; i++) {
				if (bIsOrderField[i]) {
					sHtml = sHtml
							+ "<td rowspan=2 width=66 align=center bgcolor=#C4DCFD>"
							+ "<table width=80 border=0 cellpadding=0 cellspacing=0>"
							+ "<tr><td rowspan=2 align=center><font color=#000066>"
							+ asCnPropNames[i]
							+ "</font></td><td>"
							+ "<a href=# onclick=\"setOrderBy("
							+ String.valueOf(i)
							+ ", 0);doPageListSubmit();\">"
							/*
							 * hxy replace 
							 */
							+ "<img src=../images/ArrowUp.gif width=10 height=10 border=0></a>"
							//+ "<img src=/"+DerConstant.APP_NAME+"/images/ArrowUp.gif width=10 height=10 border=0></a>"
							/*
							 * hxy end
							 */
							+ "</td></tr><tr><td>"
							+ "<a href=# onclick=\"setOrderBy("
							+ String.valueOf(i)
							+ ", 1);doPageListSubmit();\">"
							/*
							 * hxy replace
							 */
							+ "<img src=../images/ArrowDown.gif width=10 height=10 border=0>"
							//+ "<img src=/"+DerConstant.APP_NAME+"/images/ArrowDown.gif width=10 height=10 border=0>"
							/*
							 * hxy end
							 */
							+ "</td></tr></table></td>";

				} else {
					sHtml = sHtml
							+ "<td rowspan=2 bgcolor=#C4DCFD width=123 align=center>"
							+ "<font color=#000066>" + asCnPropNames[i]
							+ "</font></td>";
				}
			}
		}

		sHtml += "</tr><tr align=center> </tr>";

		String[] asColor = { "#F3F3F3", "#E0E0E0" };
		if (vRSData == null) {
			vRSData = new Vector();
		}
		for (int i = 0; i < vRSData.size(); i++) {
			Hashtable htIC = (Hashtable) vRSData.elementAt(i);
			sHtml = sHtml + "<tr bgcolor=" + asColor[i % 2]
					+ "><td align=center width=40 bgcolor=#F3F3F3>"
					+ "<input type='checkbox' name='ids' value='"
					//+ (String) htIC.get(asRequiredProperties[0]) + "'>"
					+ htIC.get("id") + "'>" + "</td>";

			sHtml = sHtml + "<td><a href=javascript:openwindow('"
					+ sLinkViewForm + "&id="
					//+ (String) htIC.get(asRequiredProperties[0])
					//Updated by ALex
					+ htIC.get("id")
					//Updated by ALex finish
					+ "','_blank',800,500)>"
					+ (String) htIC.get(asRequiredProperties[0]) + "</a>";
			for (int j = 1; j < asRequiredProperties.length; j++) {
				sHtml = sHtml + "<td>&nbsp;"
						+ htIC.get(asRequiredProperties[j]) + "</td>";
			}
			sHtml += "</tr>";
		}

		sHtml += "</form></table>";

		return sHtml;

	}

	public void setPageSize(int iPageSize) {
		this.iPageSize = iPageSize;
	}

	public int getCount() {
		return iRecordCount;
	}

	public Vector getRSData() {
		return vRSData;
	}

	/*
	 * Get Html Code like 第1页 共2页 首页 上页 下页 尾页 查看第 11 页
	 */
	public String getHtmlPageCode() {
		String sHtml = "";

		sHtml = sHtml + "第" + String.valueOf(iPageID) + "页 共"
				+ String.valueOf(iPageCount) + "页 ";
		if (!isPrint) {
			if (iPageID > 1) {
				sHtml = sHtml
						+ "<a href=# onclick='javascript:setPageID(1);doPageListSubmit();'>首页</a> ";
			}
			if (iPageID > 2) {
				sHtml = sHtml + "<a href=# onclick='javascript:setPageID("
						+ String.valueOf(iPageID - 1)
						+ ");doPageListSubmit();'>上页</a> ";
			}
			if (iPageID < iPageCount - 1) {
				sHtml = sHtml + "<a href=# onclick='javascript:setPageID("
						+ String.valueOf(iPageID + 1)
						+ ");doPageListSubmit();'>下页</a> ";
			}
			if (iPageID < iPageCount) {
				sHtml = sHtml + "<a href=# onclick='javascript:setPageID("
						+ String.valueOf(iPageCount)
						+ ");doPageListSubmit();'>尾页</a> ";
			}
		}
		return sHtml;

	}

	/*
	 * Get Html Code like 查看第11 页 <script language=javascript> function
	 * GoToPage() { window.location.href= 'test.htm?PageID=' + select2.value; }
	 * </script> <select name=select2 onchange='GoToPage();'> <option
	 * value=11>11 </option> <option value=12>12 </option> </select>
	 */
	/*
	 * sLink, link for the page, but sLink do not have link info of PageID,
	 * sIden, some html pages use two Go2Page html code in one page, so we need
	 * to use sIden to identifier them
	 */
	public String getHtmlGo2Page(String sIden) {
		String sHtml = "";

		if (isPrint)
			return "";

		if (iPageCount > 1) {
			sHtml = sHtml + " 查看第 <select name=selpage" + sIden
					+ " onchange='setPageID(selpage" + sIden
					+ ".value); doPageListSubmit();'>";
			if (iPageID > iPageCount) {
				iPageID = iPageCount;
			}
			for (int i = 0; i < iPageCount; i++) {
				if (i != iPageID - 1) {
					sHtml = sHtml + "<option value=" + String.valueOf(i + 1)
							+ ">";
					sHtml = sHtml + String.valueOf(i + 1) + "</option>";
				} else {
					sHtml = sHtml + "<option selected value="
							+ String.valueOf(i + 1) + ">";
					sHtml = sHtml + String.valueOf(i + 1) + "</option>";
				}

			}
			sHtml = sHtml + "</select> 页 ";
		}

		return sHtml;
	}

	/*
	 * 得到页面链接部分的html代码，包括刷新，查看，作废，打印
	 */
	public String getHtmlPageLink() {
		String sHtml = "<table width=100% border=0 cellspacing=0 cellpadding=0 align=center height=30>"
				+ "<tr><td align=right bgcolor=#BFBFBF>"
				+ "<a href='javascript:location.reload();'>"
				+ "<img src=../images/043.gif width=60 height=22 border=0></a>  "
				
				+ "<a href='"
				+ sLinkCancelForm
				+ "'><img src=images/057.gif width=60 height=22 border=0></a>  "
				+ "<a href='javascript:window.print();'><img src=../images/044.gif width=60 height=22 border=0></a>"
				+ "	</td></tr></table>";
		return sHtml;
	}

	public String getHtmlViewTitle() {
		String sHtml = "<table width=100% border=0 cellspacing=0 cellpadding=2 align=center>"
				+ "<tr> <td class=baobiao_title>"
				+ sCnFormName + "列表</td></tr></table>";
		if (isPrint)
			sHtml = "<table width="
					+ sTotalWidth
					+ " border=0 cellspacing=0 cellpadding=2 align=center>"
					+ "<tr> <td class=title>"
					+ sCnFormName + "列表</td></tr></table>";
		return sHtml;
	}

	public String getHtmlPageListSubmit() {
		String sHtml = "<table width=" + sTotalWidth
				+ " border=0 cellpadding=0 cellspacing=0 align=center><tr><td>"
				+ "<form name=Form_PageList action=" + sLink
				+ " method=post onsubmit='";
		sHtml += "doPageListSubmit();";
		sHtml += "'>";
		// hide fields for this form
		sHtml += "<input type=hidden name=PageID value=''>";
		sHtml += "<input type=hidden name=OrderBy value='"
				+ StringUtility.null2Str(sOrderBy) + "'>";
		sHtml += "<input type=hidden name=CnOrderBy value='"
				+ StringUtility.null2Str(sCnOrderBy) + "'>";
		//        sHtml += "<input type=hidden name=StartDate value=''>";
		//        sHtml += "<input type=hidden name=EndDate value=''>";

		if (sSearchFieldCnNames != null && sSearchFieldEnNames != null) {
			if (sStartDate == null || sEndDate == null) {
				// if no data given, we set is as current date
				Calendar cal = Calendar.getInstance();
				sHtml += WebTool.getHtmlSelectDate(cal.get(Calendar.YEAR), cal
						.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE), cal
						.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal
						.get(Calendar.DATE));
			} else
				sHtml += WebTool.getHtmlSelectDate(DateUtility.getDate(sStartDate,
						"y"), DateUtility.getDate(sStartDate, "m"), DateUtility
						.getDate(sStartDate, "d"), DateUtility.getDate(sEndDate,
						"y"), DateUtility.getDate(sEndDate, "m"), DateUtility.getDate(
						sEndDate, "d"));
			// select search condition field
			if (sSearchFieldEnNames.length == 1) {
				sHtml += "<input type=hidden name=DateField value="
						+ sSearchFieldEnNames[0] + ">";
			} else { // select on search field from multi search field
				sHtml += "&nbsp;按 ";
				sHtml += "<select name=DateField>";
				for (int k = 0; k < sSearchFieldCnNames.length; k++) {
					sHtml += "<option value=" + sSearchFieldEnNames[k];
					if (sSearchFieldEnNames[k].equals(sDateFieldName)) {
						sHtml += " selected ";
					}
					sHtml += ">" + sSearchFieldCnNames[k].trim() + "</option>";
				}
				sHtml += "</select>&nbsp; ";
			}

			sHtml += "<input type='submit' name='Submit' value='查询'>";

		} else {
			sHtml += "<input type=hidden name=StartYear>"
					+ "<input type=hidden name=StartMonth>"
					+ "<input type=hidden name=StartDay>"
					+ "<input type=hidden name=EndYear>"
					+ "<input type=hidden name=EndMonth>"
					+ "<input type=hidden name=EndDay>";
		}

		sHtml += "</form></td></tr></table>";

		return sHtml;
	}

	public String getHtmlDataHead() {
		String sTimeString = "";
		String sSearchFieldStr = "";
		if (sSearchFieldCnNames != null) {
			sSearchFieldStr = sSearchFieldCnNames[0];
			for (int i = 1; i < sSearchFieldCnNames.length; i++) {
				if (sDateFieldName.equals(sSearchFieldEnNames[i])) {
					sSearchFieldStr = sSearchFieldCnNames[i];
				}
			}
			sTimeString = sSearchFieldStr + "从"
					+ (StringUtility.isNullOrBlank(sStartDate) ? "未设定" : sStartDate)
					+ "至&nbsp;"
					+ (StringUtility.isNullOrBlank(sEndDate) ? "未设定" : sEndDate);

		}

		return "<table width=" + sTotalWidth
				+ " border=0 cellspacing=3 cellpadding=0 align=center><tr><td>"
				+ sTimeString + " 共有" + String.valueOf(iRecordCount) + "张"
				+ sCnFormName
				//+", "+
				// (StringUtility.isNullOrBlank(sCnOrderBy)?"按单据编号升序排列":sCnOrderBy)
				+ "</td><td align=right>" + getHtmlPageCode()
				+ getHtmlGo2Page("1") + "</td></tr></table>";
	}

	public String getHtmlDataTail() {
		return "<table width=" + sTotalWidth
				+ " border=0 cellspacing=3 cellpadding=0 align=center>"
				+ "<tr><td>&nbsp;</td><td align=right>" + getHtmlPageCode()
				+ getHtmlGo2Page("2") + "</td></tr></table>";
	}

	public String getHtmlBody() {
		if (isPrint) {
			String sPrintJS = "<script language=javascript>window.print();</script>";
			return getHtmlViewTitle() + "<br>" + getHtmlDataHead() + "<br>"
					+ getHtmlRecordList(asPropCnNames, abIsOrderField)
					+ getHtmlDataTail() + sPrintJS;
		}
		return getHtmlPageLink() + getHtmlViewTitle() + "<br>"
				+ getHtmlPageListSubmit() + getHtmlDataHead() 
				+ getHtmlRecordList(asPropCnNames, abIsOrderField)
				+ getHtmlDataTail();
	}

	public void initByRequest(javax.servlet.http.HttpServletRequest request) {
		sStartDate = (String) request.getParameter("StartDate");
		sEndDate = (String) request.getParameter("EndDate");

		Calendar cal = Calendar.getInstance();
		int iMonth = cal.get(Calendar.MONTH) + 1;
		String sCurDate = cal.get(Calendar.YEAR) + "-" + String.valueOf(iMonth)
				+ "-" + cal.get(Calendar.DATE);

		if (sStartDate == null)
			sStartDate = sCurDate;
		if (sEndDate == null)
			sEndDate = sCurDate;

		String sPageID = (String) request.getParameter("PageID");
		sOrderBy = (String) request.getParameter("OrderBy");
		/*
		 * sCnOrderBy = StringUtility.null2Str(request.getParameter("CnOrderBy"));
		 * 
		 * System.err.println("sCnOrderBy:" + sCnOrderBy);
		 * System.err.println("sCnOrderBy:toCn" + StringUtility.toCn(sCnOrderBy));
		 * System.err.println("sCnOrderBy:toiso" + StringUtility.toIso(sCnOrderBy));
		 */

		if (request.getParameter("DateField") != null) {
			sDateFieldName = (String) request.getParameter("DateField");
		} else
			sDateFieldName = "form_make_date";
		try {
			iPageID = Integer.parseInt(sPageID);
		} catch (Throwable t) {
			iPageID = 1;
		}

	}

	public void setDateFieldName(String sDateFieldName) {
		this.sDateFieldName = sDateFieldName;
	}

	public void init(String sQuerySql, String sDefaultOrderBy,
			boolean[] abIsOrderField, String[] asPropCnNames,
			String[] asRequiredProperties, String sLink, String sLinkViewForm,
			String sLinkCancelForm, String sCnFormName) {

		this.sQuerySql = sQuerySql;
		this.sDefaultOrderBy = sDefaultOrderBy;

		this.abIsOrderField = abIsOrderField;

		this.asPropCnNames = asPropCnNames;
		this.asRequiredProperties = asRequiredProperties;

		this.sLink = sLink;
		this.sLinkViewForm = sLinkViewForm;
		this.sLinkCancelForm = sLinkCancelForm;

		this.sCnFormName = sCnFormName;

		// if (sDateFieldName == null)
		//  sDateFieldName = "form_make_date";
		/*
		 * if sDateFieldName == null, it means user does not need any search
		 * condition
		 */

		String sWhere = " ";
		if (sSearchFieldCnNames != null && sDateFieldName != null) {
			if (!StringUtility.isNullOrBlank(sStartDate)) {
				sWhere += " and " + sDateFieldName + "  >= to_date('"
						+ sStartDate + "', 'yyyy-mm-dd') ";
			}
			if (!StringUtility.isNullOrBlank(sEndDate)) {
				sWhere += " and " + sDateFieldName + " < to_date('" + sEndDate
						+ "', 'yyyy-mm-dd') + 1 ";
			}
		}

		if (sOrderBy == null) {
			this.sQuerySql = sQuerySql + sWhere + " order by "
					+ sDefaultOrderBy;
			sOrderBy = sDefaultOrderBy;
		} else
			this.sQuerySql = sQuerySql + sWhere + " order by " + sOrderBy;

		//        System.out.println("SQL after PagedRS.init():" + this.sQuerySql);
		getPagedRSBySql();

	}

	public void setMultiSearchField(String[] sFieldCnNames,
			String[] sFieldEnNames) {
		this.sSearchFieldCnNames = sFieldCnNames;
		this.sSearchFieldEnNames = sFieldEnNames;
	}

    /**
     * @return Returns the pkField.
     */
    public String getPkField() {
        return pkField;
    }

    /**
     * @param pkField The pkField to set.
     */
    public void setPkField(String pkField) {
        this.pkField = pkField;
    }


}