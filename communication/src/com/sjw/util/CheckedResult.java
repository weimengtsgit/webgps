package com.sjw.util;
/**
 * @Title:
 * @Description:
 * @Company: 
 * @author: qiang
 * @version 1.0
 * @date: 2013-4-15 ÏÂÎç5:15:41
 */
public class CheckedResult<T> {

    private boolean result;
    private T obj;
    
    public CheckedResult(boolean result, T obj) {
        super();
        this.result = result;
        this.obj = obj;
    }
    
    public CheckedResult() {
        super();
    }
    
    public boolean isResult() {
        return result;
    }
    public void setResult(boolean result) {
        this.result = result;
    }
    public T getObj() {
        return obj;
    }
    public void setObj(T obj) {
        this.obj = obj;
    }
    
}
