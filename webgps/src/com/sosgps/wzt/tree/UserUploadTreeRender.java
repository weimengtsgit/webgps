package com.sosgps.wzt.tree;

public class UserUploadTreeRender extends TreeRender {
  public UserUploadTreeRender(MapabcTree sosgpsTree) {
    super(sosgpsTree);
  }


  //����
  protected  String getIDNameUseStr(int id,String name){
    return "<a href=javascript:clickOneCata("+id+",'"+name+"')>"+name+"</a>";
  }

}