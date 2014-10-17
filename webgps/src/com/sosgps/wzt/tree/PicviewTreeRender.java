package com.sosgps.wzt.tree;

public class PicviewTreeRender extends TreeRender {
  public PicviewTreeRender(MapabcTree aTree) {
    super(aTree);
  }

  //¸²¸Ç
  protected  String getIDNameUseStr(int id,String name){
    return "<a href=index.jsp?cata_id="+id+">"+name+"</a>";
  }

}