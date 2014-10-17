package com.sosgps.wzt.exception;


/**
 * <p>Title: WZT异常</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: 图盟科技</p>
 * @author 位置通项目组
 * @version 1.0
 */

public class WZTException extends Exception {

  // 错误码
  protected String errCode = null;
  // 错误描述
  protected String errDesc = null;

  protected Exception history = null;
  //附加错误描述
  protected String errDescEx = null;

  /**
   * 构造方法
   * @param _errCode 错误码
   */
  public WZTException(String _errCode) {
    errCode = _errCode;
  }

  /**
   * 构咱方法
   * @param _errCode 错误码
   * @param _history 异常历史
   */
  public WZTException(String _errCode, Exception _history) {
    errCode = _errCode;
    history = _history;
  }

  /**
   * 构造方法
   * @param _errCode 错误码
   * @param _errDesc 错误描述
   */
  public WZTException(String _errCode, String _errDesc) {
    errCode = _errCode;
    errDesc = _errDesc;
  }

  /**
   * 构造方法
   * @param _errCode 错误码
   * @param _errDesc 错误描述
   * @param history  异常历史
   */
  public WZTException(String _errCode, String _errDesc, Exception _history) {
    errCode = _errCode;
    errDesc = _errDesc;
    history = _history;
  }

  /**
   * 获得错误码
   * @return
   */
  public String getErrCode() {
    return this.errCode;
  }

  /**
   * 错误描述
   * @return
   */
  public String getErrDesc() {
    return this.errDesc;
  }

  /**
   * 获得异常历史
   * @return
   */
  public Exception getHistory(){
    return this.history;
  }

  /**
   * 获得异常信息
   * @return
   */
  public String toString() {
    StringBuffer exString = new StringBuffer();
//    exString.append("错误码：" + this.errCode +"\t");
        exString.append(this.errCode +": ");
    if (this.errDesc != null) {
//      exString.append("错误描述：" + this.errDesc + "\n");
//            exString.append(this.errDesc + "\n");
            exString.append(getErrDescEx() + "\n");
    }
    if(this.history != null){
      //exString.append("异常历史:\n");
      exString.append(history.toString());
    }
    return exString.toString();
  }

  /**
   * 添加扩展错误描述信息
   * @param msg 附加的错误描述信息
   */
  public void appendMsg(String msg){
    this.errDescEx = msg;
  }

  /**
   * 获得扩展后的错误描述信息
   * @return 完整的错误描述信息，包括基本的和扩展的
   */
  public String getErrDescEx(){
    if (errDescEx == null)
      return errDesc;
    else
      return errDesc + "  " + errDescEx;
  }

  public String getMessage() {
      StringBuffer Msg = new StringBuffer(4);
      Msg.append("错误码：");
      Msg.append(errCode);
      Msg.append("  错误信息：");
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
