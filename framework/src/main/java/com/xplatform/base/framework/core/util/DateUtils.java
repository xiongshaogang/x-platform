package com.xplatform.base.framework.core.util;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.util.StringUtils;

/**
 * 
 * 类描述：时间操作定义类
 * 
 * @author: 张代浩
 * @date： 日期：2012-12-8 时间：下午12:15:03
 * @version 1.0
 */
public class DateUtils extends PropertyEditorSupport {
	// 各种时间格式
	public static final SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
	// 各种时间格式
	public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	// 各种时间格式
	public static final SimpleDateFormat date_sdf_wz = new SimpleDateFormat("yyyy年MM月dd日");
	public static final SimpleDateFormat datetime_sdf_wz = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	public static final SimpleDateFormat time_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat short_time_sdf = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// 以毫秒表示的时间
	private static final long DAY_IN_MILLIS = 24 * 3600 * 1000;
	private static final long HOUR_IN_MILLIS = 3600 * 1000;
	private static final long MINUTE_IN_MILLIS = 60 * 1000;
	private static final long SECOND_IN_MILLIS = 1000;

	// 指定模式的时间格式
	private static SimpleDateFormat getSDFormat(String pattern) {
		return new SimpleDateFormat(pattern);
	}

	/**
	 * 当前日历，这里用中国时间表示
	 * 
	 * @return 以当地时区表示的系统当前日历
	 */
	public static Calendar getCalendar() {
		return Calendar.getInstance();
	}

	/**
	 * 指定毫秒数表示的日历
	 * 
	 * @param millis
	 *            毫秒数
	 * @return 指定毫秒数表示的日历
	 */
	public static Calendar getCalendar(long millis) {
		Calendar cal = Calendar.getInstance();
		// --------------------cal.setTimeInMillis(millis);
		cal.setTime(new Date(millis));
		return cal;
	}

	// ////////////////////////////////////////////////////////////////////////////
	// getDate
	// 各种方式获取的Date
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * 当前日期
	 * 
	 * @return 系统当前时间
	 */
	public static Date getDate() {
		return new Date();
	}

	/**
	 * 指定毫秒数表示的日期
	 * 
	 * @param millis
	 *            毫秒数
	 * @return 指定毫秒数表示的日期
	 */
	public static Date getDate(long millis) {
		return new Date(millis);
	}

	/**
	 * 时间戳转换为字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String timestamptoStr(Timestamp time) {
		Date date = null;
		if (null != time) {
			date = new Date(time.getTime());
		}
		return date2Str(date_sdf);
	}

	/**
	 * 字符串转换时间戳
	 * 
	 * @param str
	 * @return
	 */
	public static Timestamp str2Timestamp(String str) {
		Date date = str2Date(str, date_sdf);
		return new Timestamp(date.getTime());
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @param sdf
	 * @return
	 */
	public static Date str2Date(String str, SimpleDateFormat sdf) {
		if (null == str || "".equals(str)) {
			return null;
		}
		Date date = null;
		try {
			date = sdf.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            日期格式
	 * @return 字符串
	 */
	public static String date2Str(SimpleDateFormat date_sdf) {
		Date date = getDate();
		if (null == date) {
			return null;
		}
		return date_sdf.format(date);
	}

	/**
	 * 格式化时间
	 * 
	 * @param data
	 * @param format
	 * @return
	 */
	public static String dataformat(String data, String format) {
		SimpleDateFormat sformat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sformat.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sformat.format(date);
	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            日期格式
	 * @return 字符串
	 */
	public static String date2Str(Date date, SimpleDateFormat date_sdf) {
		if (null == date) {
			return null;
		}
		return date_sdf.format(date);
	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            日期格式
	 * @return 字符串
	 */
	public static String getDate(String format) {
		Date date = new Date();
		if (null == date) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 指定毫秒数的时间戳
	 * 
	 * @param millis
	 *            毫秒数
	 * @return 指定毫秒数的时间戳
	 */
	public static Timestamp getTimestamp(long millis) {
		return new Timestamp(millis);
	}

	/**
	 * 以字符形式表示的时间戳
	 * 
	 * @param time
	 *            毫秒数
	 * @return 以字符形式表示的时间戳
	 */
	public static Timestamp getTimestamp(String time) {
		return new Timestamp(Long.parseLong(time));
	}

	/**
	 * 系统当前的时间戳
	 * 
	 * @return 系统当前的时间戳
	 */
	public static Timestamp getTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * 指定日期的时间戳
	 * 
	 * @param date
	 *            指定日期
	 * @return 指定日期的时间戳
	 */
	public static Timestamp getTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}

	/**
	 * 指定日历的时间戳
	 * 
	 * @param cal
	 *            指定日历
	 * @return 指定日历的时间戳
	 */
	public static Timestamp getCalendarTimestamp(Calendar cal) {
		// ---------------------return new Timestamp(cal.getTimeInMillis());
		return new Timestamp(cal.getTime().getTime());
	}

	public static Timestamp gettimestamp() {
		Date dt = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = df.format(dt);
		java.sql.Timestamp buydate = java.sql.Timestamp.valueOf(nowTime);
		return buydate;
	}

	// ////////////////////////////////////////////////////////////////////////////
	// getMillis
	// 各种方式获取的Millis
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * 系统时间的毫秒数
	 * 
	 * @return 系统时间的毫秒数
	 */
	public static long getMillis() {
		return new Date().getTime();
	}

	/**
	 * 指定日历的毫秒数
	 * 
	 * @param cal
	 *            指定日历
	 * @return 指定日历的毫秒数
	 */
	public static long getMillis(Calendar cal) {
		// --------------------return cal.getTimeInMillis();
		return cal.getTime().getTime();
	}

	/**
	 * 指定日期的毫秒数
	 * 
	 * @param date
	 *            指定日期
	 * @return 指定日期的毫秒数
	 */
	public static long getMillis(Date date) {
		if (date == null) {
			return 0;
		} else {
			return date.getTime();
		}
	}

	/**
	 * 指定时间戳的毫秒数
	 * 
	 * @param ts
	 *            指定时间戳
	 * @return 指定时间戳的毫秒数
	 */
	public static long getMillis(Timestamp ts) {
		return ts.getTime();
	}

	// ////////////////////////////////////////////////////////////////////////////
	// formatDate
	// 将日期按照一定的格式转化为字符串
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * 默认方式表示的系统当前日期，具体格式：年-月-日
	 * 
	 * @return 默认日期按“年-月-日“格式显示
	 */
	public static String formatDate() {
		return date_sdf.format(getCalendar().getTime());
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月13日 下午5:44:08
	 * @Decription 获得当前服务端系统时间(精确到秒:yyyy-MM-dd HH:mm:ss)
	 *
	 * @return
	 */

	public static String getDatetime() {
		return datetimeFormat.format(getCalendar().getTime());
	}

	/**
	 * 获取时间字符串
	 */
	public static String getDataString(SimpleDateFormat formatstr) {
		return formatstr.format(getCalendar().getTime());
	}

	/**
	 * 指定日期的默认显示，具体格式：年-月-日
	 * 
	 * @param cal
	 *            指定的日期
	 * @return 指定日期按“年-月-日“格式显示
	 */
	public static String formatDate(Calendar cal) {
		return date_sdf.format(cal.getTime());
	}

	/**
	 * 指定日期的默认显示，具体格式：年-月-日
	 * 
	 * @param date
	 *            指定的日期
	 * @return 指定日期按“年-月-日“格式显示
	 */
	public static String formatDate(Date date) {
		return date_sdf.format(date);
	}

	/**
	 * 指定毫秒数表示日期的默认显示，具体格式：年-月-日
	 * 
	 * @param millis
	 *            指定的毫秒数
	 * @return 指定毫秒数表示日期按“年-月-日“格式显示
	 */
	public static String formatDate(long millis) {
		return date_sdf.format(new Date(millis));
	}

	/**
	 * 默认日期按指定格式显示
	 * 
	 * @param pattern
	 *            指定的格式
	 * @return 默认日期按指定格式显示
	 */
	public static String formatDate(String pattern) {
		return getSDFormat(pattern).format(getCalendar().getTime());
	}

	/**
	 * 指定日期按指定格式显示
	 * 
	 * @param cal
	 *            指定的日期
	 * @param pattern
	 *            指定的格式
	 * @return 指定日期按指定格式显示
	 */
	public static String formatDate(Calendar cal, String pattern) {
		return getSDFormat(pattern).format(cal.getTime());
	}

	/**
	 * 指定日期按指定格式显示
	 * 
	 * @param date
	 *            指定的日期
	 * @param pattern
	 *            指定的格式
	 * @return 指定日期按指定格式显示
	 */
	public static String formatDate(Date date, String pattern) {
		return getSDFormat(pattern).format(date);
	}

	// ////////////////////////////////////////////////////////////////////////////
	// formatTime
	// 将日期按照一定的格式转化为字符串
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * 默认方式表示的系统当前日期，具体格式：年-月-日 时：分
	 * 
	 * @return 默认日期按“年-月-日 时：分“格式显示
	 */
	public static String formatTime() {
		return time_sdf.format(getCalendar().getTime());
	}

	/**
	 * 指定毫秒数表示日期的默认显示，具体格式：年-月-日 时：分
	 * 
	 * @param millis
	 *            指定的毫秒数
	 * @return 指定毫秒数表示日期按“年-月-日 时：分“格式显示
	 */
	public static String formatTime(long millis) {
		return time_sdf.format(new Date(millis));
	}

	/**
	 * 指定日期的默认显示，具体格式：年-月-日 时：分
	 * 
	 * @param cal
	 *            指定的日期
	 * @return 指定日期按“年-月-日 时：分“格式显示
	 */
	public static String formatTime(Calendar cal) {
		return time_sdf.format(cal.getTime());
	}

	/**
	 * 指定日期的默认显示，具体格式：年-月-日 时：分
	 * 
	 * @param date
	 *            指定的日期
	 * @return 指定日期按“年-月-日 时：分“格式显示
	 */
	public static String formatTime(Date date) {
		return time_sdf.format(date);
	}

	// ////////////////////////////////////////////////////////////////////////////
	// formatShortTime
	// 将日期按照一定的格式转化为字符串
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * 默认方式表示的系统当前日期，具体格式：时：分
	 * 
	 * @return 默认日期按“时：分“格式显示
	 */
	public static String formatShortTime() {
		return short_time_sdf.format(getCalendar().getTime());
	}

	/**
	 * 指定毫秒数表示日期的默认显示，具体格式：时：分
	 * 
	 * @param millis
	 *            指定的毫秒数
	 * @return 指定毫秒数表示日期按“时：分“格式显示
	 */
	public static String formatShortTime(long millis) {
		return short_time_sdf.format(new Date(millis));
	}

	/**
	 * 指定日期的默认显示，具体格式：时：分
	 * 
	 * @param cal
	 *            指定的日期
	 * @return 指定日期按“时：分“格式显示
	 */
	public static String formatShortTime(Calendar cal) {
		return short_time_sdf.format(cal.getTime());
	}

	/**
	 * 指定日期的默认显示，具体格式：时：分
	 * 
	 * @param date
	 *            指定的日期
	 * @return 指定日期按“时：分“格式显示
	 */
	public static String formatShortTime(Date date) {
		return short_time_sdf.format(date);
	}

	// ////////////////////////////////////////////////////////////////////////////
	// parseDate
	// parseCalendar
	// parseTimestamp
	// 将字符串按照一定的格式转化为日期或时间
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
	 * 
	 * @param src
	 *            将要转换的原始字符窜
	 * @param pattern
	 *            转换的匹配格式
	 * @return 如果转换成功则返回转换后的日期
	 * @throws ParseException
	 * @throws AIDateFormatException
	 */
	public static Date parseDate(String src, String pattern) throws ParseException {
		return getSDFormat(pattern).parse(src);

	}

	/**
	 * 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
	 * 
	 * @param src
	 *            将要转换的原始字符窜
	 * @param pattern
	 *            转换的匹配格式
	 * @return 如果转换成功则返回转换后的日期
	 * @throws ParseException
	 * @throws AIDateFormatException
	 */
	public static Calendar parseCalendar(String src, String pattern) throws ParseException {

		Date date = parseDate(src, pattern);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static String formatAddDate(String src, String pattern, int amount) throws ParseException {
		Calendar cal;
		cal = parseCalendar(src, pattern);
		cal.add(Calendar.DATE, amount);
		return formatDate(cal);
	}

	/**
	 * 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
	 * 
	 * @param src
	 *            将要转换的原始字符窜
	 * @param pattern
	 *            转换的匹配格式
	 * @return 如果转换成功则返回转换后的时间戳
	 * @throws ParseException
	 * @throws AIDateFormatException
	 */
	public static Timestamp parseTimestamp(String src, String pattern) throws ParseException {
		Date date = parseDate(src, pattern);
		return new Timestamp(date.getTime());
	}

	// ////////////////////////////////////////////////////////////////////////////
	// dateDiff
	// 计算两个日期之间的差值
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * 计算两个时间之间的差值，根据标志的不同而不同
	 * 
	 * @param flag
	 *            计算标志，表示按照年/月/日/时/分/秒等计算
	 * @param calSrc
	 *            减数
	 * @param calDes
	 *            被减数
	 * @return 两个日期之间的差值
	 */
	public static int dateDiff(char flag, Calendar calSrc, Calendar calDes) {

		long millisDiff = getMillis(calSrc) - getMillis(calDes);

		if (flag == 'y') {
			return (calSrc.get(calSrc.YEAR) - calDes.get(calDes.YEAR));
		}

		if (flag == 'd') {
			return (int) (millisDiff / DAY_IN_MILLIS);
		}

		if (flag == 'h') {
			return (int) (millisDiff / HOUR_IN_MILLIS);
		}

		if (flag == 'm') {
			return (int) (millisDiff / MINUTE_IN_MILLIS);
		}

		if (flag == 's') {
			return (int) (millisDiff / SECOND_IN_MILLIS);
		}

		return 0;
	}

	/**
	 * String类型 转换为Date, 如果参数长度为10 转换格式”yyyy-MM-dd“ 如果参数长度为19 转换格式”yyyy-MM-dd
	 * HH:mm:ss“ * @param text String类型的时间值
	 */
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.hasText(text)) {
			try {
				if (text.indexOf(":") == -1 && text.length() == 10) {
					setValue(this.date_sdf.parse(text));
				} else if (text.indexOf(":") > 0 && text.length() == 19) {
					setValue(this.datetimeFormat.parse(text));
				} else {
					throw new IllegalArgumentException("Could not parse date, date format is error ");
				}
			} catch (ParseException ex) {
				IllegalArgumentException iae = new IllegalArgumentException("Could not parse date: " + ex.getMessage());
				iae.initCause(ex);
				throw iae;
			}
		} else {
			setValue(null);
		}
	}

	public static int getYear() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(getDate());
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 获取系统时间yyyyMMddHHmmssms格式的日期时间字符串，如：201205101432133213
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * @return yyyyMMddHHmmssms格式的日期时间字符串
	 */
	public static String getSystemCurrentDateTimeSimpleStr() {
		Date date = new Date();
		return yyyymmddhhmmss.format(date);
	}

	public static String getDateTimeString(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 日期字符串处理，合法的日期字符串将原样返回，不合法的返回空串
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * @param dateTime
	 *            日期字符串
	 * @return 处理后的日期字符串
	 */
	public static String processDateStr(String date) {
		String dateStr = "";
		if (StringUtil.isEmpty(date)) {
			return "";
		} else {
			try {
				getDate(date);
				dateStr = date;
				return dateStr;
			} catch (Exception e) {
				return dateStr;
			}
		}
	}

	public static Calendar setStartDay(Calendar cal) {
		cal.set(11, 0);
		cal.set(12, 0);
		cal.set(13, 0);
		return cal;
	}

	public static Calendar setEndDay(Calendar cal) {
		cal.set(11, 23);
		cal.set(12, 59);
		cal.set(13, 59);
		return cal;
	}

	public static Date convertString(String value, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (value == null)
			return null;
		try {
			return sdf.parse(value);
		} catch (Exception e) {
		}
		return null;
	}

	public static Date convertString(String value) {
		return convertString(value, "yyyy-MM-dd HH:mm:ss");
	}

	public static String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return formatter.format(date);
	}

	public static String getDateTimeString(long millseconds) {
		return getDate(millseconds, "yyyy-MM-dd HH:mm:ss");
	}

	public static String getDayDate(long time) {
		return getDate(time, "yyyyMMdd");
	}

	public static String getDate(long time, String format) {
		SimpleDateFormat formater = new SimpleDateFormat(format);
		return formater.format(new Date(time));
	}

	public static String getDateTimeString(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public static boolean isTimeLarge(String startTime, String endTime) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ParsePosition pos = new ParsePosition(0);
			ParsePosition pos1 = new ParsePosition(0);
			Date dt1 = formatter.parse(startTime, pos);
			Date dt2 = formatter.parse(endTime, pos1);
			long lDiff = dt2.getTime() - dt1.getTime();
			return lDiff > 0L;
		} catch (Exception e) {
		}
		return false;
	}

	public static long getTime(Date startTime, Date endTime) {
		return endTime.getTime() - startTime.getTime();
	}

	public static String getTimeDiff(String startTime, String endTime) {
		try {
			String tmp = "";
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ParsePosition pos = new ParsePosition(0);
			ParsePosition pos1 = new ParsePosition(0);
			Date dt1 = formatter.parse(startTime, pos);
			Date dt2 = formatter.parse(endTime, pos1);
			long lDiff = dt2.getTime() - dt1.getTime();
			int days = (int) (lDiff / 86400000L);
			if (days > 0) {
				lDiff -= days * 1000 * 60 * 60 * 24;
				tmp = tmp + days + "天";
			}
			int hours = (int) (lDiff / 3600000L);
			if (hours > 0)
				lDiff -= hours * 1000 * 60 * 60;
			tmp = tmp + hours + "小时";
			int minute = (int) (lDiff / 60000L);
			tmp = tmp + minute + "分钟";
			return tmp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-1";
	}

	public static String getTime(Long millseconds) {
		String time = "";
		if (millseconds == null) {
			return "";
		}
		int days = (int) millseconds.longValue() / 1000 / 60 / 60 / 24;
		if (days > 0) {
			time = time + days + "天";
		}
		long hourMillseconds = millseconds.longValue() - days * 1000 * 60 * 60 * 24;
		int hours = (int) hourMillseconds / 1000 / 60 / 60;
		if (hours > 0) {
			time = time + hours + "小时";
		}
		long minuteMillseconds = millseconds.longValue() - days * 1000 * 60 * 60 * 24 - hours * 1000 * 60 * 60;
		int minutes = (int) minuteMillseconds / 1000 / 60;
		if (minutes > 0) {
			time = time + minutes + "分钟";
		}
		return time;
	}

	public static String getDateString(Date date) {
		if (date != null) {
			return new SimpleDateFormat("yyyy-MM-dd").format(date);
		}
		return "";
	}

	public static String getCurrentDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = new Date();
		return formatter.format(date);
	}

	public static String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return formatter.format(date);
	}

	public static String getDateString(long millseconds) {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(millseconds));
	}

	public static String getDateString(String formater) {
		return new SimpleDateFormat(formater).format(new Date());
	}

	public static long getMillsByToday() {
		String str = getDateString("yyyy-MM-dd");
		str = String.valueOf(getMillsByDateString(str));
		return Long.parseLong(str.substring(0, str.length() - 3) + "000");
	}

	public static long getNextDays(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(5, days);
		String str = String.valueOf(cal.getTimeInMillis());
		return Long.parseLong(str.substring(0, str.length() - 3) + "000");
	}

	public static Date getNextDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(5, days);
		return cal.getTime();
	}

	public static long getMillsByDateString(String strDate) {
		Calendar cal = Calendar.getInstance();
		if ((strDate != null) && (strDate.trim().length() > 9)) {
			strDate = strDate.trim();
			try {
				int year = Integer.parseInt(strDate.substring(0, 4));
				int month = Integer.parseInt(strDate.substring(5, 7)) - 1;
				int date = Integer.parseInt(strDate.substring(8, 10));
				cal.set(year, month, date, 0, 0, 0);
				String str = String.valueOf(cal.getTimeInMillis());
				return Long.parseLong(str.substring(0, str.length() - 3) + "000");
			} catch (Exception localException) {
			}
		}

		return 0L;
	}

	public static long getMillsByDateTimeString(String strDateTime) {
		Calendar cal = Calendar.getInstance();
		if ((strDateTime != null) && (strDateTime.trim().length() > 18)) {
			strDateTime = strDateTime.trim();
			try {
				int year = Integer.parseInt(strDateTime.substring(0, 4));
				int month = Integer.parseInt(strDateTime.substring(5, 7)) - 1;
				int date = Integer.parseInt(strDateTime.substring(8, 10));
				int hour = Integer.parseInt(strDateTime.substring(11, 13));
				int minute = Integer.parseInt(strDateTime.substring(14, 16));
				int second = Integer.parseInt(strDateTime.substring(17, 19));
				cal.set(year, month, date, hour, minute, second);
				return cal.getTimeInMillis();
			} catch (Exception localException) {
			}
		}
		return 0L;
	}

	public static String getFormatString(long millseconds, String format) {
		if ((format == null) || (format.trim().length() == 0)) {
			format = "yyyy-MM-dd";
		}
		format = format.trim();
		return new SimpleDateFormat(format).format(new Date(millseconds));
	}

	public static long getCurrentTimeMillis() {
		Calendar c = Calendar.getInstance();
		return c.getTimeInMillis();
	}

	public static String getTimeByMills(long mills) throws Exception {
		try {
			if (mills == 0L)
				return "-";
			Date date = null;
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(mills);
			date = ca.getTime();
			SimpleDateFormat myformat;
			if ((ca.get(11) == 0) && (ca.get(12) == 0) && (ca.get(13) == 0))
				myformat = new SimpleDateFormat("yyyy-MM-dd");
			else {
				myformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
			return myformat.format(date);
		} catch (Exception e) {
		}
		return "-";
	}

	public static long getMillsByTime(String strTime) throws Exception {
		try {
			if ((strTime.length() != 19) && (strTime.length() != 10)) {
				throw new Exception("the time string is wrong.");
			}

			int year = Integer.parseInt(strTime.substring(0, 4));
			int month = Integer.parseInt(strTime.substring(5, 7)) - 1;
			int day = Integer.parseInt(strTime.substring(8, 10));

			if ((year < 1000) || (year > 3000)) {
				throw new Exception("the year is wrong.");
			}

			if ((month < 0) || (month > 12)) {
				throw new Exception("the month is wrong.");
			}

			if ((day < 1) || (day > 31)) {
				throw new Exception("the day is wrong");
			}

			Calendar ca = Calendar.getInstance();
			if (strTime.length() == 19) {
				int hour = Integer.parseInt(strTime.substring(11, 13));
				int minute = Integer.parseInt(strTime.substring(14, 16));
				int second = Integer.parseInt(strTime.substring(17, 19));

				if ((hour < 0) || (hour > 24)) {
					throw new Exception("the hour is wrong.");
				}

				if ((minute < 0) || (minute > 60)) {
					throw new Exception("the minute is wrong.");
				}

				if ((second < 0) || (second > 60)) {
					throw new Exception("the second is wrong.");
				}

				ca.set(year, month, day, hour, minute, second);
			} else if (strTime.length() == 10) {
				ca.set(year, month, day, 0, 0, 0);
			}
			return ca.getTimeInMillis();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1L;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月30日 上午11:02:46
	 * @Decription 获得某个开始时间往后 某数量时间计数 的目标时间 (以毫秒计)
	 *
	 * @param timeUnit
	 *            时间单位(年、月等)
	 * @param interval
	 *            增加的单位数量
	 * @param timeMill
	 *            开始的时间
	 * @return
	 */
	public static long getNextTime(int timeUnit, int interval, long timeMill) {
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(timeMill);
		switch (timeUnit) {
		case 0:
			ca.add(13, interval);
			break;
		case 1:
			ca.add(12, interval);
			break;
		case 2:
			ca.add(10, interval);
			break;
		case 3:
			ca.add(5, interval);
			break;
		case 4:
			ca.add(2, interval);
			break;
		default:
			return 0L;
		}
		return ca.getTimeInMillis();
	}

	public static Date getDateTimeByTimeString(String timeString) {
		Date date = new Date();
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			date = dateFormat.parse(timeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getDateByDateString(String timeString) {
		Date date = new Date();
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
			date = dateFormat.parse(timeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getDate(int year, int month, int date) {
		return getDate(year, month, date, 0, 0, 0);
	}

	public static Date getDate(int year, int month, int date, int hourOfDay, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, date, hourOfDay, minute, second);
		return cal.getTime();
	}

	public static int getSecondDiff(Date endTime, Date startTime) {
		long start = startTime.getTime();
		long end = endTime.getTime();
		return (int) ((end - start) / 1000L);
	}

	public static int getDaysOfMonth(int year, int mon) {
		Calendar cal = Calendar.getInstance();
		cal.set(1, year);
		cal.set(2, mon - 1);
		return cal.getActualMaximum(5);
	}

	public static int getWeekDayOfMonth(int year, int mon) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, mon - 1, 1);
		return cal.get(7);
	}

	public static String getDurationTime(Date time) {
		if (time == null) {
			return "";
		}
		Long millseconds = Long.valueOf(getTime(time, new Date()));
		return getTime(millseconds);
	}

	// 获得"年-月-日"日期文字
	public static String formatDateWZTime(Date date) {
		if (date != null) {
			return date_sdf_wz.format(date);
		}
		return "";
	}

	// 将"年-月-日"转为日期
	public static Date parseDateWZTime(String str) throws Exception {
		return date_sdf_wz.parse(str);
	}

	// 获得"年-月-日 小时:分钟"日期文字
	public static String formatDatetimeWZTime(Date date) {
		if (date != null) {
			return datetime_sdf_wz.format(date);
		}
		return "";
	}

	// 将"年-月-日 小时:分钟"转为日期
	public static Date parseDatetimeWZTime(String str) throws Exception {
		return datetime_sdf_wz.parse(str);
	}

	// 求 两个时间相隔的天数
	public static double betweenDay(Date beginDate, Date endDate) {
		long begin = getMillis(beginDate);
		long end = getMillis(endDate);
		return (end - begin) / (24 * 60 * 60 * 1000.0);
	}

	/**
	 * 获得前一天
	 * 
	 * @author xiaqiang
	 * @createtime 2015年6月4日 下午11:46:26
	 *
	 * @param timestamp
	 * @return
	 */
	public static Calendar getBeforeDay(long timestamp) {
		Calendar c = getCalendar(timestamp);
		c.add(Calendar.DATE, -1);
		return c;
	}

	/**
	 * 获得后一天
	 * 
	 * @author xiaqiang
	 * @createtime 2015年6月4日 下午11:46:26
	 *
	 * @param timestamp
	 * @return
	 */
	public static Calendar getAfterDay(long timestamp) {
		Calendar c = getCalendar(timestamp);
		c.add(Calendar.DATE, 1);
		return c;
	}

	/**
	 * 获得当天的23:59:59
	 * 
	 * @author xiaqiang
	 * @createtime 2015年6月4日 下午11:51:18
	 *
	 * @param c
	 * @return
	 */
	public static Calendar getLastTimeForADay(Calendar c) {
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE), 23, 59, 59);
		return c;
	}

	public static void main(String args[]) {
		Calendar c1 = getBeforeDay(System.currentTimeMillis());
		System.out.println(c1.getTime());
		Calendar c2 = getLastTimeForADay(c1);
		System.out.println(c1.getTime());
	}
}