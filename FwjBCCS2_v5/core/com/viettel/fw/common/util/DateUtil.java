/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.fw.common.util;


import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Admin
 * @version 1.0
 */
public final class DateUtil {

    public static String getPosProcessCodeDate(Date date) {

        Date curr = date;

        int yyyy = curr.getYear() + 1900;

        String temp = new Long(yyyy).toString();

        int month = curr.getMonth() + 1;

        String monthTemp = DataUtil.addZeroToString(new Long(month).toString(), 2);

        temp += monthTemp;

        int dd = curr.getDate();

        String ddTemp = DataUtil.addZeroToString(new Long(dd).toString(), 2);

        temp += ddTemp;

        return temp;

    }

    public static boolean compare(Date date1, Date date2, String format) throws Exception {
        if (date1 == null || date2 == null) {
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        if (dateFormat.format(date1).equals(dateFormat.format(date2))) {
            return true;
        } else {
            return false;
        }
    }

    public static String dateTime2Stringddmmyyyy(Date value) {
        if (value != null) {
            SimpleDateFormat dateTime = new SimpleDateFormat(
                    "dd/MM/yyyy HH:mm:ss");
            return dateTime.format(value);
        }
        return "";
    }

    public static boolean compareDateOnly(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        DateTime first = new DateTime(date1);
        DateTime second = new DateTime(date2);
        return DateTimeComparator.getDateOnlyInstance().compare(first, second) == 0;
    }

    public static boolean equalOnlyField(Date date1, Date date2, int type) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        return c1.get(type) == c2.get(type);

    }

    /**
     * add day
     *
     * @param nowDate Date
     * @param period  integer
     * @return Date
     */
    public static Date addDay(Date nowDate, int period) {
        LocalDateTime localDate = new LocalDateTime(nowDate);
        return localDate.plusDays(period).toDate();
    }

    /**
     * @param date1 Date
     * @param date2 Date
     * @return long
     */
    public static long minusDate(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2MMyyString(Date value) {
        if (value != null) {
            SimpleDateFormat date = new SimpleDateFormat("MM/yyyy");
            return date.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2ddMMyyNoSlashString(Date value) {
        if (value != null) {
            SimpleDateFormat date = new SimpleDateFormat("ddMMyyyy");
            return date.format(value);
        }
        return "";
    }

    public static String date2yyyyMMddStringWithSlash(Date value) {
        if (value != null) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            return date.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2yyyyMMddHHString(Date value) {
        if (value != null) {
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHH");
            return date.format(value);
        }
        return "";
    }

    public static Date string2DateTime(String value) throws ParseException {
        if (!DataUtil.isNullOrEmpty(value)) {
            SimpleDateFormat dateTime = new SimpleDateFormat(
                    "dd/MM/yyyy hh:mm:ss");
            return dateTime.parse(value);
        }
        return null;
    }

    public static Date string2Date(String value) {
        return DateUtil.string2DateByPattern(value, "dd/MM/yyyy");
    }

    public static Date string2DateByPattern(String value, String pattern) {
        if (!DataUtil.isNullOrEmpty(value)) {
            SimpleDateFormat dateTime = new SimpleDateFormat(pattern);
            dateTime.setLenient(false);
            try {
                return dateTime.parse(value);
            } catch (ParseException ex) {
                return null;
            }
        }
        return null;
    }

    private DateUtil() {
    }

    /**
     * @param value String
     * @return Date
     */
    public static Date string2Date(String value, String format) {
        try {
            SimpleDateFormat dbUpdateDateTime = new SimpleDateFormat(format);
            return dbUpdateDateTime.parse(value);
        } catch (ParseException ex) {
        }

        return new Date();
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2String(Date value) {
        if (value != null) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            return date.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2StringNoSlash(Date value) {
        if (value != null) {
            SimpleDateFormat dateNoSlash = new SimpleDateFormat("yyyyMMdd");
            return dateNoSlash.format(value);
        }
        return "";
    }

    public static Date addDate(Date date, int numDate) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, numDate);
            date = cal.getTime();
        }
        return date;
    }

    /**
     * @param value Date
     * @return String
     */
    public static String dateH2StringNoSlash(Date value) {
        if (value != null) {
            SimpleDateFormat dateNoSlash = new SimpleDateFormat("yyyyMMddHH");
            return dateNoSlash.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String dateTime2StringNoSlash(Date value) {
        if (value != null) {
            SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat(
                    "yyyyMMddHHmmss");
            return dateTimeNoSlash.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String dateTime2String(Date value) {
        if (value != null) {
            SimpleDateFormat dateTime = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            return dateTime.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String dbUpdateDateTime2String(Date value) {
        if (value != null) {
            SimpleDateFormat dbUpdateDateTime = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            return dbUpdateDateTime.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return Timestamp
     */
    public static Timestamp date2Timestamp(Date value) {
        if (value != null) {
            return new Timestamp(value.getTime());
        }
        return null;
    }

    /**
     * @return Date
     */
    public static Date sysDate() {
        return new Date();
    }

    /**
     * return now if input date is null, otherwise return date
     *
     * @param date
     * @return
     */
    public static Date dateToNow(Date date) {
        return date == null ? new Date() : date;
    }

    /**
     * @return Date
     */
    public static Date sysdateYmd() {
        return nextdate(0);
    }

    /**
     * @param day integer
     * @return Date
     */
    public static Date nextdate(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE) + day, 0, // hour
                0, // min
                0); // sec
        /**
         * clear millisecond field
         */
        calendar.clear(Calendar.MILLISECOND);
        return calendar.getTime();
    }

    /**
     * @param date Date
     * @param day  integer
     * @return Date
     */
    public static Date nextdate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE) + day, 0, // hour
                0, // min
                0); // sec
        /**
         * clear millisecond field
         */
        calendar.clear(Calendar.MILLISECOND);
        return calendar.getTime();
    }

    /**
     * get the next n month.
     *
     * @param date  Date
     * @param month number of next month
     * @return Date
     */
    public static Date nextMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + month,
                calendar.get(Calendar.DATE),
                0, // hour
                0, // min
                0); // sec
        /**
         * clear millisecond field
         */
        calendar.clear(Calendar.MILLISECOND);
        return calendar.getTime();
    }

    public static Date nextMonthDateTime(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * get the previos n month
     *
     * @param date  Date
     * @param month integer
     * @return Date
     */
    public static Date getPreMonthDate(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) - month,
                calendar.get(Calendar.DATE),
                0, // hour
                0, // min
                0); // sec
        /**
         * clear millisecond field
         */
        calendar.clear(Calendar.MILLISECOND);
        return calendar.getTime();

    }

    /**
     * @return String
     */
    public static String sysdateString() {
        SimpleDateFormat dbUpdateDateTime = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return dbUpdateDateTime.format(new Date());
    }

    /**
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getDate() {
        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
        return date;
    }

    /**
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getDateTime() {
        SimpleDateFormat dateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateTime;
    }

    /**
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getDateTimeMinute() {
        SimpleDateFormat dateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return dateTime;
    }

    /**
     * [timestampToStringFF function.].<br>
     * [Detail description of method.]
     *
     * @param date Timestamp
     * @return String
     */
    public static String timestampToStringFF(Timestamp date) {
        if (date != null) {
            SimpleDateFormat dbDateTimeString = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss.SSS");
            return dbDateTimeString.format(date);
        }
        return "";
    }

    /**
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getDbUpdateDateTime() {
        SimpleDateFormat dbUpdateDateTime = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return dbUpdateDateTime;
    }

    /**
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getYYYYMM() {
        SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMM");
        return yyyymm;
    }

    /**
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getMMdd() {
        SimpleDateFormat mmdd = new SimpleDateFormat("MM/dd");
        return mmdd;
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2ddMMyyyyString(Date value) {
        if (value != null) {
            SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
            return ddMMyyyy.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String getFirstDateOfMonth(Date value) {
        if (value != null) {
            SimpleDateFormat MMyyyy = new SimpleDateFormat("MM/yyyy");
            return "01/" + MMyyyy.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2MMyyyyString(Date value) {
        if (value != null) {
            SimpleDateFormat yyyyMM = new SimpleDateFormat("MM/yyyy");
            return yyyyMM.format(value);
        }
        return "";
    }

    /**
     * date to yyMMddString
     *
     * @param value Date
     * @return String
     */
    public static String date2yyMMddString(Date value) {
        if (value != null) {
            SimpleDateFormat yyMMdd = new SimpleDateFormat("yy/MM/dd");
            return yyMMdd.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2yyMMddStringNoSlash(Date value) {
        if (value != null) {
            SimpleDateFormat yyMMdd = new SimpleDateFormat("yyMMdd");
            return yyMMdd.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2yyyyMMStringNoSlash(Date value) {
        if (value != null) {
            SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMM");
            return yyyymm.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2yyyyMMddStringNoSlash(Date value) {
        if (value != null) {
            SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMMdd");
            return yyyymm.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2yyMMStringNoSlash(Date value) {
        if (value != null) {
            SimpleDateFormat yymm = new SimpleDateFormat("yyMM");
            return yymm.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2MMddString(Date value) {
        if (value != null) {
            SimpleDateFormat mmdd = new SimpleDateFormat("MM/dd");
            return mmdd.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String second2String(Date value) {
        if (value != null) {
            return SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM).format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return []String
     */
    public static String[] getSplitDate(Date value) {
        if (value != null) {
            DecimalFormat df = new DecimalFormat("00");
            String[] dateTime = dateTime2String(value).split(" ");
            String[] date = new String[6];
            String[] tmpDate = dateTime[0].split("/");
            date[0] = df.format(Integer.parseInt(tmpDate[0]));
            date[1] = df.format(Integer.parseInt(tmpDate[1]));
            date[2] = df.format(Integer.parseInt(tmpDate[2]));
            tmpDate = dateTime[1].split(":");
            date[3] = df.format(Integer.parseInt(tmpDate[0]));
            date[4] = df.format(Integer.parseInt(tmpDate[1]));
            date[5] = df.format(Integer.parseInt(tmpDate[2]));
            return date;
        }
        return new String[6];
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2MMddTime(Date value) {
        if (value != null) {
            SimpleDateFormat mmdd = new SimpleDateFormat("MM/dd HH:mm:ss");
            return mmdd.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2YYYYMMddTime(Date value) {
        if (value != null) {
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            return yyyyMMdd.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2HHMMssNoSlash(Date value) {
        if (value != null) {
            SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("HHmmss");
            return dateTimeNoSlash.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2ddMMyyyyHHMMssNoSlash(Date value) {
        if (value != null) {
            SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("ddMMyyyyHHmmss");
            return dateTimeNoSlash.format(value);
        }
        return "";
    }


    /**
     * @param value Date
     * @return String
     */
    public static String dateyyyyMMddHHmmSS(Date value) {
        if (value != null) {
            SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateTimeNoSlash.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String dateyyyyMMdd(Date value) {
        if (value != null) {
            SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("yyyy-MM-dd");
            return dateTimeNoSlash.format(value);
        }
        return "";
    }

    /**
     * @return
     */
    public static Timestamp nowDateMilli() {
        return new Timestamp(sysDate().getTime());
    }

    /**
     * @param date Date
     * @return integer
     */
    public static int getYY(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (calendar.get(Calendar.YEAR)) % 100;
    }

    /**
     * @param nowDate Date
     * @return integer
     */
    public static int getMonth(Date nowDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * @param nowDate Date
     * @return integer
     */
    public static int getDay(Date nowDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //==========================================================================

    /**
     * addMilli.<br>
     *
     * @param nowDate Date
     * @param period  integer
     * @return Timestamp
     */
    //==========================================================================
    public static Timestamp addMilli(Timestamp nowDate, int period) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.MILLISECOND, period);

        Timestamp stopTerm = date2Timestamp(calendar.getTime());

        return stopTerm;
    }

    /**
     * add minute
     *
     * @param nowDate Date
     * @param period  integer
     * @return Date
     */
    public static Date addMinute(Date nowDate, int period) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.MINUTE, period);

        return calendar.getTime();
    }

    public static Date getddMMyyyy(Date date) throws Exception {
        Date result = date;
        if (result != null) {
            String strDate = DateUtil.date2ddMMyyyyString(date);
            result = DateUtil.string2Date(strDate);
        }
        return result;
    }

    public static Date convertSqlDateToUtilDate(java.sql.Date sqlDate) {
        return new java.util.Date(sqlDate.getTime());
    }

    public static Date convertStringToTime(String date, String pattern) throws ParseException {
        if (date == null || "".equals(date.trim())) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.parse(date);

    }

    /**
     * Ham Reset thoi gian ve cung mot thoi diem
     *
     * @param dateTime
     * @return
     */
    public static Date truncTime(Date dateTime) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateTime);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        } catch (Exception ex) {
            return null;
        }
        return cal.getTime();
    }

    public static DateTime convertStringToDateTime(String date, String pattern) throws ParseException {
        if (date == null || "".equals(date.trim())) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.parseDateTime(date);
    }

    public static String date2StringByPattern(Date date, String pattern) throws ParseException {
        if (date == null || DataUtil.isNullOrEmpty(pattern)) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static void main(String[] args) throws Exception {
        Date now = Calendar.getInstance().getTime();
        Date now2 = DateUtil.addMonth(now, 1);

//        System.out.println(daysBetween2Dates(now, now2));
//        System.out.println(daysBetween2Dates(now2, now));
        System.out.println(compareDateToDateTime(now, now2));
    }

    public static String dateToStringWithPattern(Date date, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        } catch (Exception e) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(date);
        }
    }

    public static Long date2LongUpdateDateTime(Date value) {
        if (value != null) {
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
            String updateDateTime = date.format(value);
            return Long.parseLong(updateDateTime);
        }
        return null;
    }

    /**
     * kiem tra tuoi khach hang o nam trong khoang min - max hay ko
     *
     * @param minOld
     * @param maxOld
     * @param customerBirthDay
     * @param sysDate
     * @return
     * @throws Exception
     * @author quangkm
     */
    public static boolean checkCustomerAge(int minOld, int maxOld, Date customerBirthDay, Date sysDate) throws Exception {
        try {
            Calendar birthDateCalendar = Calendar.getInstance();
            birthDateCalendar.setTime(customerBirthDay);
            Calendar currDateCalendar = Calendar.getInstance();
            currDateCalendar.setTime(sysDate);

            int yearOfBirthDate = birthDateCalendar.get(Calendar.YEAR);

            int yearOfCurrDate = currDateCalendar.get(Calendar.YEAR);

            if (minOld <= yearOfCurrDate - yearOfBirthDate && maxOld >= yearOfCurrDate - yearOfBirthDate) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Ham so sanh hai ngay voi nhau
     *
     * @param date1
     * @param date2
     * @return
     * @author anhbq8
     */
    public static int compareDateToDate(Date date1, Date date2) {
        DateTime date1DT = new DateTime(date1);
        DateTime date2DT = new DateTime(date2);
        return DateTimeComparator.getDateOnlyInstance().compare(date1DT, date2DT);
    }

    /**
     * convert date sang string dinh dang nam-thang-ngay-gio-phut-giay
     *
     * @param value
     * @return
     * @author quangkm
     */
    public static String date2yyyyMMddHHmmSsString(Date value) {
        if (value != null) {
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
            return date.format(value);
        }
        return "";
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2ddMMyyyyHHMMss(Date value) {
        if (value != null) {
            SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return dateTimeNoSlash.format(value);
        }
        return "";
    }

    /**
     * Lay nam chenh lech giua 2 ngay
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int yearsBetween(Date date1, Date date2) {
        return Years.yearsBetween(new DateTime(date1), new DateTime(date2)).getYears();
    }

    /**
     * Lay thang chenh lech giua 2 ngay
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int monthsBetween(Date date1, Date date2) {
        return Months.monthsBetween(new DateTime(date1), new DateTime(date2)).getMonths();
    }

    /**
     * Lay phut chenh lech giua 2 phut
     *
     * @param date1
     * @param date2
     * @return
     * @throws Exception
     */
    public static int minutesBetween(Date date1, Date date2) throws Exception {
        return Minutes.minutesBetween(new DateTime(date1), new DateTime(date2)).getMinutes();
    }

    /**
     * Lay giay chenh lech giua 2 ngay
     *
     * @param date1
     * @param date2
     * @return
     * @throws Exception
     */
    public static int secondsBetween(Date date1, Date date2) throws Exception {
        return Seconds.secondsBetween(new DateTime(date1), new DateTime(date2)).getSeconds();
    }

    /**
     * Tim so ngay chenh lech tu date1 den date2, bo qua gio cu the, chi tinh ngay
     * Date1 > date2: tra ve duong
     * Date2 < Date1: tra ve am
     *
     * @param date1
     * @param date2
     * @return
     * @throws Exception
     */
    public static long daysBetween2Dates(Date date1, Date date2) {
        return Days.daysBetween(new DateTime(date2).toLocalDate(), new DateTime(date1).toLocalDate()).getDays();
    }

    /**
     * @param date
     * @param numMonth
     * @return
     * @desc + / - thang
     */
    public static Date addMonth(Date date, int numMonth) {
        LocalDateTime today = new LocalDateTime(date);
        return today.plusMonths(numMonth).toDate();
    }

    public static Date addMonth(Date date, int... numMonth) {
        for (int i : numMonth) {
            date = addMonth(date, i);
        }
        return date;
    }

    public static Date getLastDayOfMonth(Date date) {
        LocalDate today = new LocalDate(date);
        return today.dayOfMonth().withMaximumValue().toDate();
    }

    public static Date getFirstDayOfMonth(Date date) {
        LocalDate today = new LocalDate(date);
        return today.dayOfMonth().withMinimumValue().toDate();
    }

    public static XMLGregorianCalendar convertToXmlDate(Date date) throws DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        return date2;
    }

    //date1 < date2 (return <0)
    public static int compareDateToDateTime(Date date1, Date date2) {
        DateTime date1DT = new DateTime(date1);
        DateTime date2DT = new DateTime(date2);
        return DateTimeComparator.getInstance().compare(date1DT, date2DT);
    }

    public static Date stripTimeFromDate(Date date) {
        return new LocalDateTime(date).toLocalDate().toDate();
    }

    /**
     * @param strDate1
     * @param strDate2
     * @param pattern
     * @return
     * @date 06-04-2016 14:52:51
     * @author TuyenNT17
     * @description
     */
    public static boolean greaterOrEqualsddMMyyyy(String strDate1, String strDate2, int type) {
        String pattern = "dd/MM/yyyy";
        if (strDate1 == null || strDate1 == "" || strDate2 == null | strDate2 == "") {
            return false;
        }
        Date date1 = string2DateByPattern(strDate1, pattern);
        Date date2 = string2DateByPattern(strDate2, pattern);
        return greaterOrEqualsddMMyyyy(date1, date2, type);
    }

    /**
     * @param date1
     * @param date2
     * @return
     * @date 06-04-2016 14:52:59
     * @author TuyenNT17
     * @description So sanh chinh xac 2 ngay
     */
    public static boolean greaterOrEqualsddMMyyyy(Date date1, Date date2, int type) {
        return date1.compareTo(date2) > 0 || equalsByTypeddMMyyyy(date1, date2, type);
    }

    /**
     * @param date1
     * @param date2
     * @return
     * @date 06-04-2016 14:52:59
     * @author TuyenNT17
     * @description So sanh chinh xac
     */
    public static boolean equalsByTypeddMMyyyy(Date date1, Date date2, int type) {
        return equalOnlyField(date1, date2, type) && equalOnlyField(date1, date2, Calendar.YEAR);
    }

    public static boolean isDate(String str, String datePartern) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePartern);
        try {
            sdf.setLenient(false);
            Date date = sdf.parse(str);
            if (date == null) {
                return false;
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            return false;
        }
        return true;
    }
}
