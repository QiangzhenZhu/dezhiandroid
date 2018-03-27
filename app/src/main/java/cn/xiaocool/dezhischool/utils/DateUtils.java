package cn.xiaocool.dezhischool.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mac on 2017/6/19.
 */

public class DateUtils {
    public static Date nextWeekMonday(Date date){
        Calendar cal =Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //下周星期一
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
    public static Date lastWeekMonday(Date date){
        Calendar cal =Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.WEEK_OF_YEAR,-1);
        return cal.getTime();
    }
    public static Date currentWeekMonday(Date date){
        Calendar cal =Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }
    public static Date currentWeekSunday(Date date){
        Calendar cal =Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        return cal.getTime();
    }
    public static Date lastDayWholePointDate(Date date) {
        Calendar cal =Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }
    public static Date nextDayWholePointDate(Date date) {
        Calendar cal =Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }
    /**
     *
     * 根据年月获取该月第一天的时间戳
     **/
    public static Long getMonthBeginTimestamp(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.YEAR, year); //year 为 int
        time.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
        time.set(Calendar.DAY_OF_MONTH, 1);
        time.set(Calendar.HOUR_OF_DAY,0);
        time.set(Calendar.MINUTE,0);
        time.set(Calendar.SECOND,0);
        return (time.getTimeInMillis())/1000;

    }

    /**
     *
     * 根据年月获取该月最后的时间戳
     **/
    public static Long getMonthEndTimestamp(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.YEAR, year); //year 为 int
        time.set(Calendar.MONTH,month-1);//注意,Calendar对象默认一月为0
        int day=time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        time.set(Calendar.DAY_OF_MONTH,day);
        time.set(Calendar.HOUR_OF_DAY,23);
        time.set(Calendar.MINUTE,59);
        time.set(Calendar.SECOND,59);
        return time.getTimeInMillis();

    }

    public static String getDate(String time){
        Date date = new Date(Long.parseLong(time)*1000);
        return new SimpleDateFormat("HH:mm").format(date);
    }
}