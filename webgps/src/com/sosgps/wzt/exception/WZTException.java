package com.sosgps.wzt.exception;


/**
 * <p>Title: WZT�쳣</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: ͼ�˿Ƽ�</p>
 * @author λ��ͨ��Ŀ��
 * @version 1.0
 */

public class WZTException extends Exception {

  // ������
  protected String errCode = null;
  // ��������
  protected String errDesc = null;

  protected Exception history = null;
  //���Ӵ�������
  protected String errDescEx = null;

  /**
   * ���췽��
   * @param _errCode ������
   */
  public WZTException(String _errCode) {
    errCode = _errCode;
  }

  /**
   * ���۷���
   * @param _errCode ������
   * @param _history �쳣��ʷ
   */
  public WZTException(String _errCode, Exception _history) {
    errCode = _errCode;
    history = _history;
  }

  /**
   * ���췽��
   * @param _errCode ������
   * @param _errDesc ��������
   */
  public WZTException(String _errCode, String _errDesc) {
    errCode = _errCode;
    errDesc = _errDesc;
  }

  /**
   * ���췽��
   * @param _errCode ������
   * @param _errDesc ��������
   * @param history  �쳣��ʷ
   */
  public WZTException(String _errCode, String _errDesc, Exception _history) {
    errCode = _errCode;
    errDesc = _errDesc;
    history = _history;
  }

  /**
   * ��ô�����
   * @return
   */
  public String getErrCode() {
    return this.errCode;
  }

  /**
   * ��������
   * @return
   */
  public String getErrDesc() {
    return this.errDesc;
  }

  /**
   * ����쳣��ʷ
   * @return
   */
  public Exception getHistory(){
    return this.history;
  }

  /**
   * ����쳣��Ϣ
   * @return
   */
  public String toString() {
    StringBuffer exString = new StringBuffer();
//    exString.append("�����룺" + this.errCode +"\t");
        exString.append(this.errCode +": ");
    if (this.errDesc != null) {
//      exString.append("����������" + this.errDesc + "\n");
//            exString.append(this.errDesc + "\n");
            exString.append(getErrDescEx() + "\n");
    }
    if(this.history != null){
      //exString.append("�쳣��ʷ:\n");
      exString.append(history.toString());
    }
    return exString.toString();
  }

  /**
   * �����չ����������Ϣ
   * @param msg ���ӵĴ���������Ϣ
   */
  public void appendMsg(String msg){
    this.errDescEx = msg;
  }

  /**
   * �����չ��Ĵ���������Ϣ
   * @return �����Ĵ���������Ϣ�����������ĺ���չ��
   */
  public String getErrDescEx(){
    if (errDescEx == null)
      return errDesc;
    else
      return errDesc + "  " + errDescEx;
  }

  public String getMessage() {
      StringBuffer Msg = new StringBuffer(4);
      Msg.append("�����룺");
      Msg.append(errCode);
      Msg.append("  ������Ϣ��");
      Msg.append(errDesc);
      if(errDescEx!=null)
      {
          Msg.append("("+errDescEx+")");
      }
      return Msg.toString();
  }

//  public static void main(String[] args) {
//    WZTException idae = new WZTException("1010108", "request error");
//    System.out.print(idae.toString());
//  }
}
