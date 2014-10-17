package com.sosgps.wzt.util;

public class PageBean implements java.io.Serializable {

/**
  * 总页数
  */
  private int pageCount;
/**
 * 总记录数
 */
  private int recordCount;
/**
 * 页码号
 */
  private int pageId;
/**
 * 每页显示的记录数
 */
  private int pageSize;
/**
 * 标志上一页、下一页
 */
/**
 * 当前页的第一条记录号
 */
  private int startId;
/**
 * 当前页的最后一条记录号
 */
  private int endId;

  public void setPageCount(int newPageCount) {
    pageCount = newPageCount;
  }
  public int getPageCount() {
    return pageCount;
  }
  public void setRecordCount(int newRecordCount) {
    recordCount = newRecordCount;
  }
  public int getRecordCount() {
    return recordCount;
  }
  public void setPageId(int newPageId) {
    pageId = newPageId;
  }
  public int getPageId() {
    return pageId;
  }
  public void setPageSize(int newPageSize) {
    pageSize = newPageSize;
  }
  public int getPageSize() {
    return pageSize;
  }
  public void setStartId(int newStartId) {
    startId = newStartId;
  }
  public int getStartId() {
    return startId;
  }
  public void setEndId(int newEndId) {
    endId = newEndId;
  }
  public int getEndId() {
    return endId;
  }

/**
 * 构造函数
 */
  public PageBean() {

  //初始化属性值
  //记录数：0
  recordCount=0;
  //总页数：0
  pageCount=0;
  //每页显示记录：10条
  pageSize=10;
  //页码：1
  pageId=1;

  }
  public void reset(int flag)     //针对当前页码的改变计算一切变量
  {
    if(flag==-1)
    {//标志下一页，记录号+1
      pageId++;
    }
    else if(flag==-2)
    {//标志上一页，记录号-1
      pageId--;
    }
    else
    {
        pageId = flag;
    }
    if(recordCount==0)
    {//如果无记录
      pageId=0;
      startId=0;
      endId=0;
      pageCount=0;
    }
    else
    {
      //设置当前页第一条记录号
      startId=(pageId-1)*pageSize;

      if (startId < 0 || startId > recordCount - 1) {
          startId = 0;
      }

      //设置当前页最后一条记录号
      endId = pageId * pageSize;
      if(endId > recordCount)
      {
        endId = recordCount;
      }

      //设置总页数
      pageCount=recordCount/pageSize;
      if(recordCount%pageSize!=0)//整页不满时
      pageCount=recordCount/pageSize+1;
    }
  }

  public void alterRecordCount()
  { if(recordCount==0)
    {//如果无记录
      pageId=0;
      startId=0;
      endId=0;
      pageCount=0;
    }
    else
    {
      //设置当前页第一条记录号
      startId=(pageId-1)*pageSize;

      //设置当前页最后一条记录号
      endId=pageId*pageSize;
      if(pageId*pageSize>recordCount)
      {
        endId=startId+recordCount-(pageId-1)*pageSize;
      }

      //设置总页数
      pageCount=recordCount/pageSize;
      if(recordCount%pageSize!=0)//整页不满时
      pageCount=recordCount/pageSize+1;
    }
  }

}