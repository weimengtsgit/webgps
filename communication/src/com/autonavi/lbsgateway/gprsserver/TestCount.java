package com.autonavi.lbsgateway.gprsserver;

public class TestCount {
  int test=0;
  public TestCount() {
  }

  public static void main(String[] args) {
    TestCount testcount = new TestCount();
  }
  public synchronized void  addIndex(){
    test++;
   // com.mapabc.directl.Log.getInstance().outLog("======================the car count is:"+test);
  }
}
