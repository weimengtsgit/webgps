package com.sosgps.wzt.util;

public class PageBean implements java.io.Serializable {

/**
  * ��ҳ��
  */
  private int pageCount;
/**
 * �ܼ�¼��
 */
  private int recordCount;
/**
 * ҳ���
 */
  private int pageId;
/**
 * ÿҳ��ʾ�ļ�¼��
 */
  private int pageSize;
/**
 * ��־��һҳ����һҳ
 */
/**
 * ��ǰҳ�ĵ�һ����¼��
 */
  private int startId;
/**
 * ��ǰҳ�����һ����¼��
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
 * ���캯��
 */
  public PageBean() {

  //��ʼ������ֵ
  //��¼����0
  recordCount=0;
  //��ҳ����0
  pageCount=0;
  //ÿҳ��ʾ��¼��10��
  pageSize=10;
  //ҳ�룺1
  pageId=1;

  }
  public void reset(int flag)     //��Ե�ǰҳ��ĸı����һ�б���
  {
    if(flag==-1)
    {//��־��һҳ����¼��+1
      pageId++;
    }
    else if(flag==-2)
    {//��־��һҳ����¼��-1
      pageId--;
    }
    else
    {
        pageId = flag;
    }
    if(recordCount==0)
    {//����޼�¼
      pageId=0;
      startId=0;
      endId=0;
      pageCount=0;
    }
    else
    {
      //���õ�ǰҳ��һ����¼��
      startId=(pageId-1)*pageSize;

      if (startId < 0 || startId > recordCount - 1) {
          startId = 0;
      }

      //���õ�ǰҳ���һ����¼��
      endId = pageId * pageSize;
      if(endId > recordCount)
      {
        endId = recordCount;
      }

      //������ҳ��
      pageCount=recordCount/pageSize;
      if(recordCount%pageSize!=0)//��ҳ����ʱ
      pageCount=recordCount/pageSize+1;
    }
  }

  public void alterRecordCount()
  { if(recordCount==0)
    {//����޼�¼
      pageId=0;
      startId=0;
      endId=0;
      pageCount=0;
    }
    else
    {
      //���õ�ǰҳ��һ����¼��
      startId=(pageId-1)*pageSize;

      //���õ�ǰҳ���һ����¼��
      endId=pageId*pageSize;
      if(pageId*pageSize>recordCount)
      {
        endId=startId+recordCount-(pageId-1)*pageSize;
      }

      //������ҳ��
      pageCount=recordCount/pageSize;
      if(recordCount%pageSize!=0)//��ҳ����ʱ
      pageCount=recordCount/pageSize+1;
    }
  }

}