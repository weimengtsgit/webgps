package com.sosgps.wzt.tree;

import java.util.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SortedTree extends java.util.TreeMap{

  public SortedTree() {
    super(new MapabcComparator());
  }
  public void addTreeNode(TreeNode node){
    put(new Long(node.getSortIndex()),node);
  }
 /* public void printTreeNode(){
    //取得最上层节点
    Vector firstNode=new Vector();
    java.util.Enumeration emu=this.elements();
    while(emu.hasMoreElements()){
      TreeNode tempNode=(TreeNode)emu.nextElement();
      if(tempNode.getParentItemID()==-1){
        firstNode.add(tempNode);
      }
    }
    for(int i=0;i<firstNode.size();i++){

    }
  }
  public TreeNode getChildTreeNode(TreeNode node){
    return null;
  }*/

}
/*class KekeSortedSet extends java.util.SortedSet{

}*/
