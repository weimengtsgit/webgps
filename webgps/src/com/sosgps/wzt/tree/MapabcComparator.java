package com.sosgps.wzt.tree;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.*;

public class MapabcComparator  implements java.util.Comparator{
 public int compare(Object obj1,Object obj2){
   long i1=((Long)obj1).longValue();
   long i2=((Long)obj2).longValue();
   if(i1>i2){
     return 1;
   }else if(i1==i2){
     return 0;
   }else{
     return -1;
   }
 }
}
