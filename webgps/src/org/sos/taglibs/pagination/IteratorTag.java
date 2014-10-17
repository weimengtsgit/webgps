package org.sos.taglibs.pagination;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.sos.constant.GlobalConstant;

/**
 * @Title: IteratorTag.java
 * @Description:
 * @Copyright:
 * @Date: 2007-8-13 下午07:43:02
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yanglei
 * @version 1.0
 */

public class IteratorTag extends TagSupport {
	private static Logger logger = Logger.getLogger(IteratorTag.class);
	private String attributeName;
	private static final String C_TYPE = "<input type=\"hidden\" name=\"";
	private static final String C_VALUE = "\" value=\"";
	private static final String C_END = "\">";
	private static final String C_TABLE_HEAD = "<table><tr><td>当前:";
	private static final String C_TABLE_MIDDLE = "</td><td>第";
	private static final String C_HREF_HEAD = "<a href=\"javascript:submit(";
	private static final String C_HREF_END = ")\">";
	private static final String C_JAVA_SCRIPT = "<Script Language=\"JavaScript\">"
			+ "function submit(number){"
			// + "alert(number);"
			+ "document.all.currentPage[0].value=number;"
			// + "alert(document.all.currentPage.value);"
			+ "document.forms[0].submit();" + "}" + "</script>";

	public int doStartTag() throws JspException {
		PageIterator pageIterator = (PageIterator) pageContext.getRequest().getAttribute(GlobalConstant.PAGINATION);
		int currentPage, pageSize, totalPage;
		if (pageIterator != null) {
			currentPage = pageIterator.getCurrentPage();
			pageSize = pageIterator.getPageSize();
			totalPage = pageIterator.getTotalRowNum();
		} else {
			currentPage = Integer.parseInt(pageContext.getRequest().getParameter(GlobalConstant.CURRENT_PAGE));
			pageSize = Integer.parseInt(pageContext.getRequest().getParameter(GlobalConstant.PAGE_SIZE));
			totalPage = Integer.parseInt(pageContext.getRequest().getParameter(GlobalConstant.TOTAL_PAGE));
		}

		int baseNumber = (currentPage - 1) / 10;
		int beginPage = baseNumber * 10 + 1;
		int endPage = baseNumber * 10 + 10;

		if (logger.isDebugEnabled()) {
			logger.debug("IteratorTag:" + currentPage + "|" + pageSize + "|" + totalPage + "|"
					+ baseNumber + "|" + beginPage + "|" + endPage);
		}

		String hiddenField = C_TYPE + GlobalConstant.CURRENT_PAGE + C_VALUE + currentPage + C_END;
		hiddenField += C_TYPE + GlobalConstant.PAGE_SIZE + C_VALUE + pageSize + C_END;
		hiddenField += C_TYPE + "totalPage" + C_VALUE + totalPage + C_END;

		String tableStr = C_TABLE_HEAD + ((currentPage - 1) * pageSize + 1) + " - "
				+ (currentPage * pageSize > totalPage ? totalPage : currentPage * pageSize)
				+ "条 共" + totalPage + "条记录  " + C_TABLE_MIDDLE;
		String hrefStr = "";
		int count;
		if (totalPage % pageSize == 0) {
			count = totalPage / pageSize;
		} else {
			count = totalPage / pageSize + 1;
		}
		for (int i = beginPage; i <= endPage && i <= count; i++) {
			if (i == currentPage) {
				hrefStr += i + "|";
			} else {
				hrefStr += C_HREF_HEAD + i + C_HREF_END + i + "</a>|";
			}
		}
		if (beginPage > 1) {
			int pageNumber = beginPage - 10;
			tableStr += C_HREF_HEAD + pageNumber + C_HREF_END + "PREV|</a>";
		}
		tableStr += hrefStr;
		if (totalPage > endPage * pageSize) {
			int pageNumber = endPage + 1;
			tableStr += C_HREF_HEAD + pageNumber + C_HREF_END + "NEXT</a>";
		}
		tableStr += "页";
		tableStr += "</td></tr></table>";
		tableStr += hiddenField;
		tableStr += C_JAVA_SCRIPT;

		try {
			pageContext.getOut().write(tableStr);
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}
		return SKIP_BODY;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

}
