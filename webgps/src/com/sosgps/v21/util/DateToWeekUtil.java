package com.sosgps.v21.util;
import java.util.Calendar;

/**
 * buildDateToWeekData
 * @author bin
 * gps前台报表工具类
 */
public class DateToWeekUtil {

    //构建日期与周期几映射
    public static String buildDateToWeekData(){
        
        StringBuffer str = new StringBuffer();
        
        for (int y = 2010; y <= 2025; y++) {
            for (int m = 1 ; m <= 12; m++) {
                int maxDays = getLastDay(y,m) ;
                for (int d = 1; d <= maxDays ; d++) {
                    str.append(String.valueOf(y)+String.valueOf(m)+formatDate(d)+"_"+String.valueOf(dateToWeekCal(y, m, d)));
                    str.append(",");
                }
            }
        }
        
        return str.toString();
    }
    
    /**
        * 取得指定年月的当月总天数
        * @param year 年
         * @param month 月
         * @return 当月总天数
          */
         public static int getLastDay(int year, int month) {
            Calendar cal = Calendar.getInstance();
            cal.set(year,month - 1,1);
            int last = cal.getActualMaximum(Calendar.DATE);
            return last;
        }
    
    /**
     * 计算某天星期几
     * 在計算此問題上比較常用的還是基姆拉尔森计算公式（具體介紹可在網上搜索）
         W= (d+2*m+3*(m+1)/5+y+y/4-y/100+y/400) mod 7 
         d 天
         m 月
         y 年
        1月2月换算为去年的13、14月计算，也即2007年的1月和2月在公式中體現為2006年的13和14月，
                         雖然與實際不符但這是邏輯的需要。
     * @param y 年
     * @param m 月
     * @param d 日
     * @return 
     */
    private static int dateToWeekCal(int y, int m, int d){
        
        if(m==1) {m=13;y--;}
        if(m==2) {m=14;y--;}
        
        //此处week的的值为 '0,1,2,3,4,5,6' 应该转换为周一，二，三，四，五，六，日
        int week=(d+2*m+3*(m+1)/5+y+y/4-y/100+y/400)%7; 
        
        return week;  
         
    }
    
    private static String formatDate(int d){
        String d_ = String.valueOf(d);
        if(d_.length()==1){
            return "0"+d_;
        }
        return d_;
    }
    
    public static void main(String[] args) {
        buildDateToWeekData();
    }
            

}
