package com.example.demo.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;

import static java.time.ZoneId.systemDefault;

public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
    /**
     * 一天的时间的毫秒数
     */
    public static final long ONE_DAY_TIME = 86400000L;

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DEFAULT_YEAR_MONTH_PATTERN = "yyyy-MM";
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DISCONNECT_PATTERN = "MM-dd HH:mm:ss";
    public static final String DEFAULT_NUMBER_PATTERN = "yyyyMMdd";


    /***
     * >0未来 =0 今天 <0过去
     * @param date
     * @return
     * @throws DateTimeException 时间不合法
     */
    public static int judgeDay(Date date) {
        Preconditions.checkNotNull(date);
        LocalDate localDate = toLocalDate(date);
        LocalDate now = LocalDate.now();
        return localDate.compareTo(now);
    }




    /**
     * 开始时间
     * @param date
     * @return
     */
    public static long startOfDay(Date date) {
        LocalDateTime localDateTime = toLocalDateTime(date)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        return localDateTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * @Author: liaoze
     * @Description: 开始时间
     * @Return: java.lang.String
     **/
    public static String startDay(Date date) {
        LocalDateTime localDateTime = toLocalDateTime(date)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        return DateUtil.getStringTime(localDateTime);
    }

    /**
     * @Description: 开始时间
     * @Return: java.util.Date
     **/
    public static String startDay(Date date ,int day) {
        LocalDateTime localDateTime = toLocalDateTime(date).plusDays(day)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        return DateUtil.getStringTime(localDateTime);
    }


    /**
     * 结束时间
     * @param day
     * @return
     */
    public static long endOfDay(Date day) {
        LocalDateTime localDateTime = toLocalDateTime(day)
                .withHour(0)
                .withMinute(00)
                .withSecond(00)
                .withNano(000);
        return localDateTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }




    /**
     * @Author: liaoze
     * @Description: 获取一天的结束时间
     * @Date: 2020/9/14
     * @param
     * @Return: long
     **/
    public static long endOfToday() {
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(new Date());
        startTime.set(Calendar.HOUR_OF_DAY, 23);
        startTime.set(Calendar.MINUTE, 59);
        startTime.set(Calendar.SECOND, 59);
        startTime.set(Calendar.MILLISECOND, 999);
        return startTime.getTimeInMillis();
    }


    /**
     * 营业时间内离线时间 offline - 上次下线时间,online - 这次登陆时间，openTime - 营业开始时间，closeTime -营业结束时间
     * 这里共有2个时间段（b1-----e1）【b2-----e2】，4个时间点；
     * 相当于两条线段(b代表起点，e代表端点，b<=e)，4个端点
     * 可分3种情况：
     *  1.不相交。
     *      （b1-----e1）【b2-----e2】（b1-----e1）。if(e1<b2||b1>e2)        ---->此时，重合时间为零。
     *  2.相交。
     *      情况一：（b1---【b2---e1）----e2】       if(b1<b2&&e1<e2&&e1>b2)  ---->此时，重合时间为e1-b2。
     *      情况二：【b2---(b1---e2】----e1)	      if(b1>b2&&b1<e2&&e2<e1)  ---->此时，重合时间为e2-b1。
     * 3.包含：计算较短的时间段日期长度。
     *      （b1---【b2-----e2】--e1）			  if(b1<b2&&e1>e2)         ---->此时，重合时间为e2-b2。
     *      【b2---（b1-----e1）--e2】			  if(b1>b2&&e1<e2)         ---->此时，重合时间为e1-b1。
     */
    public static String calculateInWork(long offline, long online, long openTime, long closeTime) {
        logger.info("calculateInWork start ... offline:{},online:{},openTime:{},closeTime:{}", offline, online, openTime, closeTime);

        //if(e1<b2||b1>e2)
        if (online <= openTime || offline >= closeTime) {
            return "0";
        }

        //if(b1<b2&&e1<e2&&e1>b2)
        if (offline < openTime && online < closeTime && online > openTime) {
            return CalculateUtil.division(Integer.parseInt(String.valueOf(online - openTime)), 1000, 0);
        }
        //if(b1>b2&&b1<e2&&e2<e1)
        if (offline > openTime && offline < closeTime && online > closeTime) {
            return CalculateUtil.division(Integer.parseInt(String.valueOf(closeTime - offline)), 1000, 0);
        }

        //if(b1<b2&&e1>e2)
        if (offline < openTime && online > closeTime){
            return CalculateUtil.division(Integer.parseInt(String.valueOf(closeTime - openTime)), 1000, 0);
        }

        //if(b1>b2&&e1<e2)
        if (offline > openTime && online < closeTime){
            return CalculateUtil.division(Integer.parseInt(String.valueOf(online - offline)), 1000, 0);
        }
        return "0";
    }


    /**
     * @Author: liaoze
     * @Description: 判断给定的时间距离今天的时间天数
     * @Return: int
     **/
    public static int distanceToday(long time) {
        if (time > startOfDay(new Date()) && time < endOfToday()) {
            return 0;
        }
        if (time < startOfDay(new Date())) {
            return (int) ((time - startOfDay(new Date())) / ONE_DAY_TIME) - 1;
        }

        return (int) ((time - startOfDay(new Date())) / ONE_DAY_TIME);

    }


    /**
     * @Author: liaoze
     * @Description: 获取两天的日期差
     * @Return: int
     **/
    public static int getDayDateToDate(Long time,Long currentTime) {
        Long startDay = startOfDay(new Date(currentTime));
        Long endDay = endOfDay(new Date(currentTime));
        if (time > startDay && time < endDay) {
            return 0;
        }
        if (time < startDay) {
            return (int) ((time - startDay) / ONE_DAY_TIME) - 1;
        }

        return (int) ((time - startDay) / ONE_DAY_TIME);

    }


    /**
     * @Author: liaoze
     * @Description: 获取当前时间
     * @Date: 2020/4/28
     * @param pattern
     * @Return: java.lang.String
     **/
    public static String getNowTime(String pattern){
        //获得当前时间
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        String format = ldt.format(dtf);
        return format;
    }

    /**
     * @Author: liaoze
     * @Description: String转Date
     * @Date: 2020/4/28
     * @param time
     * @param pattern
     * @Return: java.util.Date
     **/
    public static Date getDateTime(String time,String pattern){
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(time, pos);
    }

    /**
     * @Author: liaoze
     * @Description: String转LocalDateTime
     * @Date: 2020/4/28
     * @param time 必须是时分秒，要不然报错
     * @param pattern
     * @Return: java.util.Date
     **/
    public static LocalDateTime getLocalDateTime(String time,String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(time, formatter);
    }

    /**
     * @Author: liaoze
     * @Description: String转LocalDate
     * @Date: 2020/4/28
     * @Return: java.util.Date
     **/
    public static LocalDate getLocalDate(String time,String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(time, formatter);
    }


    /**
     * @Author: liaoze
     * @Description: Date转String
     * @Date: 2020/4/28
     * @param date
     * @Return: java.lang.String
     **/
    public static String getStringTime(Date date){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.DEFAULT_DATETIME_PATTERN);
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return dateTimeFormatter.format(localDateTime);
    }

    /**
     * @Author: liaoze
     * @Description: Date转String
     * @Date: 2020/4/28
     * @param date
     * @param pattern
     * @Return: java.lang.String
     **/
    public static String getStringTime(Date date,String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return dateTimeFormatter.format(localDateTime);
    }

    /**
     * @Author: liaoze
     * @Description: LocalDateTime转String
     * @Date: 2020/4/28
     * @param date
     * @Return: java.lang.String
     **/
    public static String getStringTime(LocalDateTime date){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.DEFAULT_DATETIME_PATTERN);
        return dateTimeFormatter.format(date);
    }
    /**
     * @Author: liaoze
     * @Description: LocalDateTime转String
     * @Date: 2020/4/28
     * @param date
     * @param pattern
     * @Return: java.lang.String
     **/
    public static String getStringTime(LocalDateTime date,String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return dateTimeFormatter.format(date);
    }

    /**
     * @Author: liaoze
     * @Description: Date转String
     * @Date: 2020/4/28
     * @param date
     * @param pattern
     * @Return: java.lang.String
     **/
    public static String getStringTime(LocalDate date,String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return dateTimeFormatter.format(date);
    }


    /**
     * @Author: liaoze
     * @Description: LocalDateTime转Date
     * @Date: 2020/4/28
     * @param date
     * @Return: java.lang.String
     **/
    public static Date getStringTimeByLocaldate(LocalDateTime date){
        long timer = date.atZone(systemDefault()).toInstant().toEpochMilli();
        return new Date(timer);
    }

    /**
     * Date 转 LocalDate
     * @param date
     * @return
     */
    public static LocalDate toLocalDate(Date date) {
        return toLocalDateTime(date).toLocalDate();
    }


    /**
     * Date 转 LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), systemDefault());
    }

    /**
     * LocalDateTime 转 Date
     * @return
     */
    public static Date toDateTime(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }



    public static String getStartTime(String time,String pattern1,String pattern2){
        SimpleDateFormat formatter = new SimpleDateFormat(pattern1);
        ParsePosition pos = new ParsePosition(0);
        Date date =  formatter.parse(time, pos);
        LocalDateTime localDateTime = toLocalDateTime(date).withHour(0).withMinute(0).withSecond(0).withNano(0);
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern2);
        return df.format(localDateTime);
    }

    public static String getStartTime(Date date,int day,int hour,String pattern){
        LocalDateTime localDateTime = toLocalDateTime(date).plusDays(day).withHour(hour).withMinute(0).withSecond(0).withNano(0);
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return df.format(localDateTime);
    }

    public static String getFiveStartTime(Date date,int day,String pattern){
        LocalDateTime localDateTime = toLocalDateTime(date).plusDays(day).withHour(5).withMinute(0).withSecond(0).withNano(0);
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return df.format(localDateTime);
    }

    public static String getEndTime(String time,String pattern1,String pattern2){
        SimpleDateFormat formatter = new SimpleDateFormat(pattern1);
        ParsePosition pos = new ParsePosition(0);
        Date date =  formatter.parse(time, pos);
        LocalDateTime localDateTime = toLocalDateTime(date).withHour(0).withMinute(00).withSecond(00).withNano(0);
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern2);
        return df.format(localDateTime);
    }

    public static String getEndTime(Date date,int day,int hour,String pattern){
        LocalDateTime localDateTime = toLocalDateTime(date).plusDays(day).withHour(hour).withMinute(59).withSecond(59).withNano(999);
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return df.format(localDateTime);
    }

    public static String getFiveEndTime(Date date,int day,String pattern){
        LocalDateTime localDateTime = toLocalDateTime(date).plusDays(day).withHour(4).withMinute(59).withSecond(59).withNano(999);
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return df.format(localDateTime);
    }

    //计算计费时长
    public static String getBillingTime(Date rentTime,Date returnTime){
        String hourStr = "";
        String minStr = "";
        LocalDateTime rent = toLocalDateTime(rentTime);
        LocalDateTime returnT = toLocalDateTime(returnTime);
        Duration duration = Duration.between(rent,returnT);
        long days = duration.toDays();
        long hour = duration.toHours()%24;
        long minutes = duration.toMinutes()%60;
        if (days*24+hour !=0){
            hourStr = days*24+hour+"小时";
        }
        if (minutes!=0){
            minStr =  minutes+"分钟";
        }
        return hourStr+minStr;

    }

    /**
     * @Author: liaoze
     * @Description: 秒转XX小时XX分钟
     * @Date: 2020/4/28
     * @Return: java.lang.String
     **/
    public static String getDisconnectTime(long time){
        if (Objects.isNull(time)){
            time = 0;
        }
        String hourStr ="";
        String minStr ="";
        long hour = time/3600;
        long minutes = (time/60)%60;
        if (hour !=0){
            hourStr = hour+"小时";
        }
        if (minutes!=0){
            minStr =  minutes+"分钟";
        }
        return hourStr+minStr;

    }

    /**
     * @Author: liaoze
     * @Description: 生成最近30天的日期
     * @Date: 2020/4/28
     * @Return: java.lang.String
     **/
    public static LinkedList<String> getListDate(Date date) {
        LinkedList<String> linkedList = Lists.newLinkedList();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.DEFAULT_DATE_PATTERN);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), systemDefault());
        for (int i= -29;i<=0;i++){
            String dateStr = dateTimeFormatter.format(localDateTime.plusDays(i));
            linkedList.add(dateStr);
        }
        return linkedList;
    }

    /*
     * @Author: liaoze
     * @Description: 生成两给出时间的日期差
     * @Date: 2020/6/28
     * @param startDate
     * @param endDate
     * @Return: java.util.LinkedList<java.lang.String>
     **/
    public  static LinkedList<String> getTimeList(String startDate,String endDate){
        LinkedList<String> linkedList = Lists.newLinkedList();
        LocalDate start = DateUtil.getLocalDate(startDate,DateUtil.DEFAULT_DATE_PATTERN);
        LocalDate end = DateUtil.getLocalDate(endDate,DateUtil.DEFAULT_DATE_PATTERN);
        linkedList.add(startDate);
        //相差的天数
        LocalDate mid = start;
        Long days = end.toEpochDay() - start.toEpochDay();
        for (int i = 0; i < days; i++) {
            mid = mid.plusDays(1);
            String midDate = DateUtil.getStringTime(mid,DateUtil.DEFAULT_DATE_PATTERN);
            linkedList.add(midDate);
        }
        return linkedList;
    }










}
