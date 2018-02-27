package com.example.xy.dentist.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ʱ�乤����
 *
 * @author yang
 */
public class MyTimeUtils {

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "��ǰ";
    private static final String ONE_MINUTE_AGO = "����ǰ";
    private static final String ONE_HOUR_AGO = "Сʱǰ";
    private static final String ONE_DAY_AGO = "��ǰ";
    private static final String ONE_MONTH_AGO = "��ǰ";
    private static final String ONE_YEAR_AGO = "��ǰ";

    public static final String DATA_STYLE = "yyyy-MM-dd HH:mm:ss";

    /**
     * ��ȡ����ʱ��
     *
     * @return ���ض�ʱ���ַ�����ʽyyyy-MM-dd HH:mm:ss
     */

    @SuppressLint("SimpleDateFormat")
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(DATA_STYLE);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取当前的年月日
     * @return
     */


    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    public static int compare_date(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() >= dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }


    public static int compare_date2(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * ����תyyyy-MM-dd HH:mm:ss��ʽ��ʱ��
     *
     * @param l ����ֵ
     * @return ���ض�ʱ���ַ�����ʽyyyy-MM-dd HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String getStringDate(long l) {
        Date currentTime = new Date(l);
        // currentTime.setTime(l);
        SimpleDateFormat formatter = new SimpleDateFormat(DATA_STYLE);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * yyyy-MM-dd HH:mm:ss ת�� ��Ӧʱ�����ֵ
     *
     * @param time yyyy-MM-dd HH:mm:ss��ʽ��ʱ��
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static Long getLongTime(String time) {

        SimpleDateFormat formatter = new SimpleDateFormat(DATA_STYLE);
        try {
            Date date = formatter.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    @SuppressLint("SimpleDateFormat")
    public static String getTime() {
        SimpleDateFormat    formatter    =   new    SimpleDateFormat("HH:mm");
        Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str    =    formatter.format(curDate);
        return  str;

    }

    @SuppressLint("SimpleDateFormat")
    public static String getTime(String time) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(time);
            return formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * ���ؼ���ǰ ����ǰ
     *
     * @param time "yyyy-MM-dd HH:mm:ss"��ʽ��ʱ��
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String format(String time) {
        SimpleDateFormat format = new SimpleDateFormat(DATA_STYLE);
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "����";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }

        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }
    private static SimpleDateFormat sf = null;

    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;


        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));


        return re_StrTime;


    }


    // 将时间戳转为字符串
    public static String getnormalStrTime(String cc_time) {
        String re_StrTime = null;


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));


        return re_StrTime;


    }
    // 将时间戳转为字符串
    public static String getnormalStrTime1(String cc_time) {
        String re_StrTime = null;


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));


        return re_StrTime;


    }
    // 将时间戳转为字符串
    public static String getnormalStrTime2(String cc_time) {
        String re_StrTime = null;


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));


        return re_StrTime;


    }


/*将字符串转为时间戳*/


    public static long getStringToDate(String time) {
        sf = new SimpleDateFormat("MM-dd");
       Date date = new Date();
        try {
         date = sf.parse(time);
        } catch (ParseException e) {
     // TODO Auto-generated catch block
           e.printStackTrace();
           }
       return date.getTime();
      }

      public static String getExamTime(int time){

        return time / 60 + ":" +((time % 60 < 10)?("0"+time % 60):(time % 60)) ;
    }



    /**
     * 根据出生日期获取人的年龄
     *
     * @param strBirthDate(yyyy-mm-dd or yyyy/mm/dd)
     * @return
     */
    public static String getPersonAgeByBirthDate(Date dateBirthDate){
        if (dateBirthDate == null){
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strBirthDate=dateFormat.format(dateBirthDate);

        //读取当前日期
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DATE);
        //计算年龄
        int age = year - Integer.parseInt(strBirthDate.substring(0, 4)) - 1;
        if (Integer.parseInt(strBirthDate.substring(5, 7)) < month) {
            age++;
        } else if (Integer.parseInt(strBirthDate.substring(5, 7))== month && Integer.parseInt(strBirthDate.substring(8, 10)) <= day){
            age++;
        }
        return String.valueOf(age);
    }
    /**
     * 根据出生日期获取人的年龄
     *
     * @param strBirthDate(yyyy-mm-dd or yyyy/mm/dd)
     * @return
     */
    public static String getPersonAgeByBirthDate(String strBirthDate){

        if ("".equals(strBirthDate) || strBirthDate == null){
            return "";
        }
        //读取当前日期
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DATE);
        //计算年龄
        int age = year - Integer.parseInt(strBirthDate.substring(0, 4)) - 1;
        if (Integer.parseInt(strBirthDate.substring(5, 7)) < month) {
            age++;
        } else if (Integer.parseInt(strBirthDate.substring(5, 7))== month && Integer.parseInt(strBirthDate.substring(8, 10)) <= day){
            age++;
        }
        if(age<=0){
            age=0;
        }
        return String.valueOf(age);
    }



    public static  int com(String s1,String s2){

        java.text.DateFormat df=new java.text.SimpleDateFormat("HH:mm");
        java.util.Calendar c1=java.util.Calendar.getInstance();
        java.util.Calendar c2=java.util.Calendar.getInstance();
        try
        {
            c1.setTime(df.parse(s1));
            c2.setTime(df.parse(s2));
        }catch(java.text.ParseException e){
            System.err.println("格式不正确");
        }
        int result=c1.compareTo(c2);

         /*if(result>=0)
            System.out.println("c1小于c2");
        else
            System.out.println("c1大于c2");*/
        LogUtils.print("result",result);
        return  result;
    }



    // 将字符串转为时间戳
    public static String getTimes(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd- HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        }catch (ParseException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        return re_time;
    }



}