package com.sosgps.wzt.tree;

import java.sql.*;
import java.util.*;

public class TreeRender {
  MapabcTree sosgpsTree;
  String line_plus = "../images/line_pulus.gif";
  String line_plus_last = "../images/line_pulus_last.gif";
  String line_item = "../images/line_va.gif";
  String line_v = "../images/line_v.gif";
  String line_lastItem = "../images/line_vend.gif";
  String cataImageURL = "../images/folderclose.gif";
  String icon_lineh = "../images/icon_line_h.gif";
  String contentImageURL = "../images/bullet_10x10.gif";
  String icon_line_right_cross = "../images/icon_line_right_cross.gif";
  Vector openNodes = new Vector();
  java.util.Vector treeVector = new java.util.Vector();
  private int renderMethod = RENDER_METHOD_ALL;
  public static final int RENDER_METHOD_ALL = 0;
  public static final int RENDER_METHOD_NODEONLY = 1;
  int openNodeID = -1;
  public TreeRender(MapabcTree sosgpsTree) {
    this.sosgpsTree = sosgpsTree;
  }

  public void setOpenNodeID(int id) {
    this.openNodeID = id;

  }

  /**
   *渲染tree
   * @param out
   * @param method
   * RENDER_METHOD_ALL 渲染全部
   * RENDER_METHOD_NODEONLY 简练方法渲染
   * @throws java.lang.Exception
   */
  public void outPutTree(javax.servlet.jsp.JspWriter out, int method) throws
      Exception {
    renderMethod = method;
    if(renderMethod==RENDER_METHOD_ALL){
          outPutTree(out, sosgpsTree);
    }else{
      outPutTreePart(out, sosgpsTree);
    }

  }

  public void outPutTree(javax.servlet.jsp.JspWriter out) throws Exception {

    outPutTree(out, RENDER_METHOD_ALL);
  }

  public TreeNode getTreeNode(long id) {

    for (int i = 0; i < treeVector.size(); i++) {
      TreeNode tn = (TreeNode) treeVector.get(i);
      if (tn.getId() == id) {
        return tn;
      }
    }
    return null;
  }

  public int getTreeNodeIndex(int id) {

    for (int i = 0; i < treeVector.size(); i++) {
      TreeNode tn = (TreeNode) treeVector.get(i);
      if (tn.getId() == id) {
        return i;
      }
    }
    return -1;

  }

  boolean inOpenNode(long nodeid) {
    for (int i = 0; i < this.openNodes.size(); i++) {
      TreeNode tn = (TreeNode) openNodes.get(i);
      if (tn.getId() == nodeid) {
        return true;
      }
    }
    return false;
  }

  /**
   * 只绘制一部分
   * @param out
   * @param kekeTree
   * @throws java.lang.Exception
   */
  public void outPutTreePart(javax.servlet.jsp.JspWriter out,
                             com.sosgps.wzt.tree.MapabcTree sosgpsTree) throws Exception {
    treeVector = sosgpsTree.getSortedNodeList();
    setOpenNode();
    if (treeVector.size() == 0) {
      return;
    }
    out.print(
        "<table width =100% border='0' cellspacing='0' cellpadding='0' >");
    for(int i=0;i<treeVector.size();i++){
          TreeNode treeNode=(TreeNode)treeVector.get(i);
          if(treeNode.getParentItemID()==-1){
            out.print("<tr><td><img src="+line_plus+"></td><td><img src="+cataImageURL+"></td><td>");
            out.print(getIDNameUseStr(treeNode.getId(),treeNode.getModuleName()));
            out.print("</td></tr>");
          }else if( inOpenNode(treeNode.getParentItemID() ) ){
            //需要打开
            out.print("<tr><td><img src="+line_item+"></td><td><img src="+contentImageURL+"></td><td>");
            out.print(getIDNameUseStr(treeNode.getId(),treeNode.getModuleName()));
            out.print("</td></tr>");
          }
    }
    out.print("</table>");


  }

  private void setOpenNode() {
    if (openNodeID != -1) {

//            需要展开某个分支
      long parentid = -100;
      long startNodeID = openNodeID;
      while (parentid != -1) {
        TreeNode tn = getTreeNode(startNodeID);
        if (tn == null) {
          break;
        }
        parentid = tn.getParentItemID();
        startNodeID = parentid;
        openNodes.addElement(tn);
      }
    }

  }

  public void outPutTree(javax.servlet.jsp.JspWriter out,
                         com.sosgps.wzt.tree.MapabcTree sosgpsTree) throws Exception {
    treeVector = sosgpsTree.getSortedNodeList();
    setOpenNode();
    if (treeVector.size() == 0) {
      return;
    }
    out.println(
        "<table width =100% border='0' cellspacing='0' cellpadding='0' ><tr><td>");
    outPutEntry(out, 0, 0);
    out.println("</td></tr></table>");
  }

  private boolean findHasMoreSameLevelItem(int index) {
    TreeNode curTreeNode = (TreeNode) treeVector.get(index);
    for (int i = index + 1; i < treeVector.size(); i++) {
      TreeNode nextTreeNode = (TreeNode) treeVector.get(i);
      if (nextTreeNode == null) {
        continue;
      }
      if (curTreeNode.getParentItemID() == nextTreeNode.getParentItemID()) {
        return true;
      }
    }
    return false;
  }

  private int outPutEntry(javax.servlet.jsp.JspWriter out, int startIndex,
                          int stat) throws Exception {
//          stat=0 子栏目，stat=1同级别栏目
    int nextIndex = startIndex + 1;
    int lastIndex = startIndex - 1;
    TreeNode curTreeNode = (TreeNode) treeVector.get(startIndex);
    String name = curTreeNode.getModuleName();
    boolean hasMoreSLItem = findHasMoreSameLevelItem(startIndex);
    // boolean sameleveltoLast=lastIndex>=0&&  ((TreeNode)v.get(lastIndex)).getParentItemID()==curTreeNode.getParentItemID();
    if (stat == 0) {
      out.println(
          "<table width =100% border='0' cellspacing='0' cellpadding='0' ><tr><td>");
    }
    else {
      out.println("<tr><td>");
    }

//输出内容图片
    out.println(
        "<table width =100% border='0' cellspacing='0' cellpadding='0' >");
    out.println("<tr>");
    if (nextIndex < treeVector.size() &&
        ( (TreeNode) treeVector.get(nextIndex)).getParentItemID() !=
        curTreeNode.getId()) {
      //内容项
      //前缀空白
      //out.println("<td width=16>&nbsp;</td>");
      if ( ( (TreeNode) treeVector.get(nextIndex)).getParentItemID() ==
          curTreeNode.getParentItemID()) {
        //下一个让然是内容项 输出控制线
        out.println("<td width=16><image src=" + line_item + "></td>");
      }
      else {
        //最后一个内容项 输出控制线
        out.println("<td width=16><image src=" + line_lastItem + "></td>");
      }
      out.println("<td width=16><image src=" + contentImageURL + "></td>");
    }
    else if (nextIndex == treeVector.size()) {
      //最后一个item
      //out.println("<td width=16>&nbsp;</td>");
      out.println("<td width=16><image src=" + line_lastItem +
                  "><td width=16> <image src=" + contentImageURL + "></td>");
    }
    else {
      //out.println("<td width=16>&nbsp;</td>");
      String lineStr = line_plus;
      String onClickFuntion = "clickOnfolder1";
      if (!hasMoreSLItem) {
        lineStr = line_plus_last;
        onClickFuntion = "clickOnfolder2";
      }
      out.println("<td width=16><image name=treel" + curTreeNode.getId() +
                  " src=" + lineStr + "  onclick=" + onClickFuntion + "(" +
                  curTreeNode.getId() +
                  ") ></td><td width=16><image name=plusl" + curTreeNode.getId() +
                  " src=" + cataImageURL + " onclick=" + onClickFuntion + "(" +
                  curTreeNode.getId() + ") ></td>");
    }
    out.println("<td>&nbsp;" + getNodeUseStr(curTreeNode) +
                "</td>");
    //连接线控制
    /*if(nextIndex<v.size()&&((TreeNode)v.get(nextIndex)).getParentItemID()==curTreeNode.getId()){
            //内容项
            out.println( "<image src="+icon_lineh+"  height=15>");
               }*/
    out.println("</tr></table>");
//结束内容图片

    out.println("</td></tr>");
    while (nextIndex < treeVector.size() &&
           ( (TreeNode) treeVector.get(nextIndex)).getParentItemID() ==
           curTreeNode.getId()) {
      //有自栏目
      String tableVisable = "none";
      if (this.inOpenNode(curTreeNode.getId())) {
        tableVisable = "";
      }
      out.println("<tr id=treesutr" + curTreeNode.getId() + " name=treesutr" +
                  curTreeNode.getId() + " style=display:" + tableVisable +
                  "><td>");
      out.println(
          "<table width =100% border='0' cellspacing='0' cellpadding='0' ><tr>");
      if (findHasMoreSameLevelItem(startIndex)) {
        out.println("<td width=16 background=" + line_v + ">&nbsp;</td>");
      }
      else {
        out.println("<td width=16 >&nbsp;</td>");
      }
      out.println("<td>");
      nextIndex = outPutEntry(out, nextIndex, 0);
      out.println("</td></tr></table>");
      out.println("</td></tr>");
    }

    while (nextIndex < treeVector.size() &&
           ( (TreeNode) treeVector.get(nextIndex)).getParentItemID() ==
           curTreeNode.getParentItemID()) {
//out.println(" print samlevel "+((TreeNode)v.get(nextIndex)).getId());
      //没有子栏目，且有下一个同级栏目
      //out.println("<table width =100% border='0' cellspacing='0' cellpadding='0' ><tr ><td valign=top width=100% >");
      //out.println("");
      // out.println("<tr><td>");
      nextIndex = outPutEntry(out, nextIndex, 1);
      // out.println("</td></tr>");
      //out.println("</td></tr></table>");
    }

    if (stat == 0) {
      out.println("</td</tr></table>");
    }
    else {
      out.println("</td></tr>");
    }
    return nextIndex;

  }

  public void setContentImageURL(String contentImageURL) {
    this.contentImageURL = contentImageURL;
  }

  //可以覆盖
  protected String getIDNameUseStr(long id, String name) {
    String escapeName=com.sosgps.wzt.util.CharTools.escape(name);
	 // String escapeName=(name);
	  return "<a href=javascript:clickOneCata(" + id + ",'" + escapeName + "')>" + name +
        "</a>";

  }
  protected String getNodeUseStr(TreeNode node) {
	    //String escapeName=com.sosgps.util.CharTools.escape(name);
		  return "<a href='#' onclick=\"clickOneCata(" + node.getId() + ",'" + node.toString() + "')\">" + node.getModuleName() +
	        "</a>";

	  }
  
  public Vector getOpenNodes() {
    return openNodes;
  }

  public java.util.Vector getTreeVector() {
    return treeVector;
  }
  public void setTreeVector(java.util.Vector treeVector) {
    this.treeVector = treeVector;
  }
}