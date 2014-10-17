package org.sos.taglibs.pagination;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.sos.constant.GlobalConstant;


/**
 * @Title: SearchTag.java
 * @Description:
 * @Copyright:
 * @Date: 2008-8-14 ÏÂÎç02:39:08
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yanglei
 * @version 1.0
 */
public class SearchTag extends TagSupport {

	private String pageSize;
	private static final String C_TYPE = "<input type=\"hidden\" name=\"";
	private static final String C_VALUE = "\" value=\"";
	private static final String C_END = "\">";

	public int doStartTag() throws JspException {
		String hiddenStr = C_TYPE + GlobalConstant.CURRENT_PAGE + C_VALUE + "1" + C_END;
		hiddenStr += C_TYPE + GlobalConstant.PAGE_SIZE + C_VALUE + this.pageSize + C_END;
		try {
			pageContext.getOut().write(hiddenStr);
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}
		return SKIP_BODY;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageSize() {
		return this.pageSize;
	}

}
