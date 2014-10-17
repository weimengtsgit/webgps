package com.sosgps.wzt.util;


/**
 * Title:
 * Description:常用到的字符软换工具
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author 
 * @version 1.0
 */

public class CharTools {
  private String string;
  public CharTools() {
  }

///////////////用""替换s中的Null//////////////////////////
  public static String killNullString(String s) {
    if (s == null) {
      s = "";
    }
    return s.trim();
  }
	public static String killNullLong2String(Long s, String defaultValue) {
		if (s == null) {
			return defaultValue;
		} else {
			return s + "";
		}
	}

	public static String killNullFloat2String(Float s, String defaultValue) {
		if (s == null) {
			return defaultValue;
		} else {
			return s + "";
		}
	}
	
	public static String killNullDouble2String(Double s, String defaultValue) {
		if (s == null) {
			return defaultValue;
		} else {
			return s + "";
		}
	}
	
  public static String getFormNum(String s) {
    if (s == null) {
      return "";
    }
    String s1 = s.substring(s.length() - 3, s.length());
    String s2 = s.substring(0, s.length() - 3);

    return s2 + "," + s1;
  }

  public static String getType(String s) {
    return s.substring(s.length() - 3, s.length());
  }

///////////////用String insteadString 替换s中的Null///////////////////
  public static String killNullString(String s, String insteadString) {
    if (s == null) {
      s = insteadString;
    }
    return s;
  }

//////////////用URL的图片替换s中的null//////////////////////////////
  public static String killNullSWithImg(String s, String imgURL) {
    if (s == null) {
      s = "<img src=" + imgURL + ">";
    }
    return s;
  }

////////////////码转换///////////////////////////////////
  public static String getStr(String str) {
    try {
      String temp_p = str;
      byte[] temp_t = temp_p.getBytes("ISO8859-1");
      String temp = new String(temp_t, "GBK");
      return temp;
    }
    catch (Exception e) {
    }
    return "null";
  }

////////////////码转换(f/t)///////////////////////////////////
  public static String getStr(String str, boolean t) {
    if (!t) {
      return str;
    }
    try {
      String temp_p = str;
      byte[] temp_t = temp_p.getBytes("ISO8859-1");
      String temp = new String(temp_t, "GBK");
      return temp;
    }
    catch (Exception e) {
    }
    return "null";
  }

  public static String replayStr(String s) {
    s = s.replaceAll("<", "&lt");
    s = s.replaceAll(">", "&gt");
    s = s.replaceAll("'", "’");
    return s;

  }

  public static String formatStr(String s) {
    if (s == null) {
      return "";
    }
    return s.trim();
  }

  /**
   * 如果s==1 return "true"
   * @param s
   * @return
   */
  public static String getCheckBoxFlag(int m, int n) {
    if (n == m) {
      return " checked ";
    }
    return "";

  }

  public static String getSelectFlag(String standStr, String str) {

    if (str == null || standStr == null) {
      return "";
    }
    if (str.equals(standStr)) {
      return "selected";
    }
    return "";
  }

  public static boolean isInteger(String s) {
    if (s == null || s.length() == 0) {
      return false;
    }
    boolean r = true;
    try {
      int i = Integer.parseInt(s);
    }
    catch (Exception ex) {
      r = false;
    }
    return r;

  }

  public static String format2Int(int n) {
    String s = "" + n;
    if (s.length() < 2) {
      s = "0" + s;
    }
    return s;
  }
  public static String[] split(String srcStr,String delim){
    java.util.StringTokenizer stk=new java.util.StringTokenizer(srcStr,delim);
    String rStr[]=new String[stk.countTokens()];
    int index=0;
    while(stk.hasMoreTokens()){
     rStr[index]=stk.nextToken();
     index++;
    }
    return rStr;
  }

  public static String format3Int(int n) {
    String s = "" + n;
    if (s.length() < 2) {
      s = "00" + s;
    }
    else if (s.length() < 3) {
      s = "0" + s;
    }
    return s;
  }
  
  public static String  escape (String src)
  {
	  if(src==null)return src;
   int i;
   char j;
   StringBuffer tmp = new StringBuffer();
   tmp.ensureCapacity(src.length()*6);

   for (i=0;i<src.length() ;i++ )
   {

    j = src.charAt(i);

    if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
     tmp.append(j);
    else
     if (j<256)
     {
     tmp.append( "%" );
     if (j<16)
      tmp.append( "0" );
     tmp.append( Integer.toString(j,16) );
     }
     else
     {
     tmp.append( "%u" );
     tmp.append( Integer.toString(j,16) );
     }
   }
   return tmp.toString();
  }

  public static String  unescape (String src)
  {
   StringBuffer tmp = new StringBuffer();
   tmp.ensureCapacity(src.length());
   int  lastPos=0,pos=0;
   char ch;
   while (lastPos<src.length())
   {
    pos = src.indexOf("%",lastPos);
    if (pos == lastPos)
     {
     if (src.charAt(pos+1)=='u')
      {
      ch = (char)Integer.parseInt(src.substring(pos+2,pos+6),16);
      tmp.append(ch);
      lastPos = pos+6;
      }
     else
      {
      ch = (char)Integer.parseInt(src.substring(pos+1,pos+3),16);
      tmp.append(ch);
      lastPos = pos+3;
      }
     }
    else
     {
     if (pos == -1)
      {
      tmp.append(src.substring(lastPos));
      lastPos=src.length();
      }
     else
      {
      tmp.append(src.substring(lastPos,pos));
      lastPos=pos;
      }
     }
   }
   return tmp.toString();
  }
 
  // string 2 Long
  public static Long str2Long(String str, Long defauleValue){
	  try {
		return Long.parseLong(str);
	} catch (Exception e) {
		return defauleValue;
	}
  }
  
//string 2 Integer
  public static Integer str2Integer(String str, Integer defauleValue){
	  try {
		return Integer.parseInt(str);
	} catch (Exception e) {
		return defauleValue;
	}
  }
  
//string 2 Float
  public static Float str2Float(String str, Float defauleValue){
	  try {
		return Float.parseFloat(str);
	} catch (Exception e) {
		return defauleValue;
	}
  }
  
//string 2 Double
  public static Double str2Double(String str, Double defauleValue){
	  try {
		return Double.parseDouble(str);
	} catch (Exception e) {
		return defauleValue;
	}
  }

	public static String splitAndAdd(String str) {
		String[] devs = str.split(",");
		String temp = "";
		for (int k = 0; k < devs.length; k++) {
			if(devs[k].equals(""))
				continue;
			if (k == devs.length - 1) {
				temp += "'" + devs[k] + "'";
			} else {
				temp += "'" + devs[k] + "'" + ",";
			}
		}
		return temp;
	}
	public static String javaScriptEscape(String inputvalue) {
		 String input=killNullString(inputvalue);
		 StringBuffer filtered = new StringBuffer(input.length());
		 char prevChar = '\u0000';
		 char c;
		 for (int i = 0; i < input.length(); i++) {
		   c = input.charAt(i);
		   if (c == '"') {
		    filtered.append("\\\"");
		   }
		   else if (c == '\'') {
		    filtered.append("\\'");
		   }
		   else if (c == '\\') {
		    filtered.append("\\\\");
		   }
		   else if (c == '\t') {
		    filtered.append("\\t");
		   }
		   else if (c == '\n') {
		    if (prevChar != '\r') {
		     filtered.append("\\n");
		    }
		   }
		   else if (c == '\r') {
		    filtered.append("\\n");
		   } else if (c == '\f') {
		                filtered.append("\\f");
		 } else if (c == '/') {
		                filtered.append("\\/");
		            }
		   else {
		    filtered.append(c);
		   }
		   prevChar = c;
		 }
		 return filtered.toString();
		 }                           
	public static String sqlEscape(String inputvalue) {
		 String inputs=killNullString(inputvalue);
		 StringBuffer filtereds = new StringBuffer(inputs.length());
		 char prevChars = '\u0000';
		 char c;
		 for (int i = 0; i < inputs.length(); i++) {
		   c = inputs.charAt(i);
		   if (c == '\'') {
		    filtereds.append("\'\'");
		   }
		   else {
		    filtereds.append(c);
		   }
		   prevChars = c;
		 }
		 return filtereds.toString();
		 }
	
	public static int getStrHashCode(String s){
		s = "cpbcs";
    	int index = s.hashCode() % 100;
        index = index > 0 ? index : -index;
        return index;
	}
	
	public static Long[] convertionToLong(String[] strs) {// 将String数组转换为Long类型数组
		Long[] longs = new Long[strs.length]; // 声明long类型的数组
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i]; // 将strs字符串数组中的第i个值赋值给str
			longs[i] = Long.valueOf(str);// 将str转换为long类型赋值给 longs数组中对应的地方
		}
		return longs; // 返回long数组
	}
	
  public static void main(String args[]){
    String rest=javaScriptEscape("  /r afds'afdd''sfsaf aafs\r\nadf ... ");
    System.out.println(rest);
  }
}
