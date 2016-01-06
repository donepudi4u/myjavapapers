package com.myjavapapers.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class DateUtil {

	private static final long ONE_SEC = 1000;	
	private static final long ONE_MIN = 60 * ONE_SEC;
	private static final long ONE_HOUR = 60 * ONE_MIN;
	private static final int HOURS_IN_A_DAY = 24;
	private static final long DAY = 24 * ONE_HOUR;
	

	public static final String CENTRAL_STANDARD_TIME_ZONE_ID = "CST";
	
	 public static final double MILLISECONDS_PER_HOUR = 3600000.0;
	 public static final double MILLISECONDS_PER_HALF_HOUR = 1800000.0;
	 public static final double MILLISECONDS_PER_DAY = 86400000.0;

	private DateUtil() {

	}

	public static Calendar getCurrentDateTime() {
		
		return Calendar.getInstance(); 
	}
	

	public static Calendar getDateWithoutTimezone(final Date date) {
		Calendar calendar = getIntializedCalendar(date);
		calendar.clear(Calendar.ZONE_OFFSET);

		return calendar;
	}

	public static Calendar getDateWithTimeZone(final Date date, final String timeZone) {
		Calendar calendar = getIntializedCalendar(date);
		calendar.setTimeZone(java.util.TimeZone.getTimeZone(timeZone));
		return calendar;
	}

	public static Date getDate(final String dateTimeS, final String format) {
		if (StringUtils.isEmpty(dateTimeS)) {
			return null;
		}
		DateTimeFormatter fmt	= DateTimeFormat.forPattern(format);
		DateTime dateTime		= fmt.parseDateTime(dateTimeS);

		return dateTime.toDate();
	}

	public static Date getDateFromString(String strDateTime, String format){
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = formatter.parse(strDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getDatePlusMinutes(final Date date, final int minutes) {
		DateTime dateTime	= new DateTime(date);
		dateTime			= dateTime.plusMinutes(minutes);

		return dateTime.toDate();
	}

	public static Date getDateMinusDays(final Date date, final int days) {
		DateTime dateTime	= new DateTime(date);
		dateTime			= dateTime.minusDays(days);

		return dateTime.toDate();
	}

	public static Date getDatePlusDays(final Date date, final int days) {
		DateTime dateTime	= new DateTime(date);
		dateTime			= dateTime.plusDays(days);

		return dateTime.toDate();
	}

	public static Date getEndOfDayMinusDays(final Date date, final int days) {
		Calendar cal = (new DateTime(getDateMinusDays(date, days))).toGregorianCalendar();
		setMaximumTime(cal);

		return cal.getTime();
	}

	public static Date getDateOnly(final Date date) {

		return getDateOnly(new DateTime(date));
	}

	public static Date getDateOnly(final DateTime dateTime) {

		Calendar cal = getEmptyCalendar();
		cal.set(dateTime.getYear(),
				dateTime.getMonthOfYear() - 1,
				dateTime.getDayOfMonth());

		return cal.getTime();
	}

	public static int getDayOfMonth(final Date date) {
		return new DateTime(date).getDayOfMonth();
	}

	public static int getTotalNumberOfDays(final Date dateTime1, final Date dateTime2) {
		// Total days should include dateTime2
		return getDaysBetween(dateTime1, dateTime2).getDays() + 1;
	}

	public static int getNumberOfDaysBetween(final Date dateTime1, final Date dateTime2) {
		return getDaysBetween(dateTime1, dateTime2).getDays();
	}

	public static int getHoursBetween(final Date startDate, final Date endDate) {
		DateTime start	= new DateTime(startDate);
		DateTime end	= new DateTime(endDate);
		Hours hours		= Hours.hoursBetween(start, end);

		return hours.getHours();
	}

	public static String getDayDisplayString(final Date date) {
		if (date == null) {
			return StringUtils.EMPTY;
		}

		return DateTimeFormat.forPattern("dd").print(new DateTime(date));
	}

	public static String getDate(final Date date, final String format) {
		if (date == null) {
			return StringUtils.EMPTY;
		}

		DateTime dt				= new DateTime(date);
		DateTimeFormatter fmt	= DateTimeFormat.forPattern(format);

		return dt.toString(fmt);
	}



	public static Date getEndOfDay(final Date date) {
		Calendar cal	= (new DateTime(date)).toGregorianCalendar();
		cal.set(Calendar.DATE, cal.get(Calendar.DATE));
		setMaximumTime(cal);

		return cal.getTime();
	}

	public static Date getEndOfMonth(Date date) {
		Calendar cal = (new DateTime(date)).toGregorianCalendar();
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		setMaximumTime(cal);

		return cal.getTime();
	}

	public static Date getEndOfMonth(final int month, final int year) {
		Calendar cal = getEmptyCalendar();
		cal.set(year, month, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	public static int getMonth(final Date date) {
		Calendar calendar = getIntializedCalendar(date);
		return (calendar.get(Calendar.MONTH) + 1);
	}

	public static String getMonthDisplayString(Date date) {
		return DateTimeFormat.forPattern("MMMM").print(new DateTime(date));
	}

	public static Date getStartOfDay(final Date date) {
		Calendar cal = getIntializedCalendar(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE));
		setMinimumTime(cal);

		return cal.getTime();
	}

	public static Date getStartOfTomorrow(final Date date) {
		return new DateMidnight(date).plusDays(1).toDate();
	}

	public static Date getStartOfMonth(final Date date) {
		Calendar cal = getIntializedCalendar(date);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		setMinimumTime(cal);

		return cal.getTime();
	}

	public static Date getStartOfMonth(final int month, final int year) {
		Calendar cal = getEmptyCalendar();
		cal.set(year, month - 1, 1);
		return cal.getTime();
	}

	public static Date getStartOfMonthPlusMonths(final Date date, final int monthsToAdd) {
		DateTime dateTime = new DateTime(getStartOfMonth(date));
		return getDateOnly(dateTime.plusMonths(monthsToAdd));
	}

	public static Date getStartOfMonthMinusMonths(final Date date, final int monthsToSubtract) {
		DateTime dateTime = new DateTime(getStartOfMonth(date));
		return getDateOnly(dateTime.minusMonths(monthsToSubtract));
	}

	

	public static Calendar getTimeZoneCalendar(final String requestDateTime, final String timeZone, String dateTimeFormat) {
		DateTime convertedDateTime = new DateTime(DateUtil.getDate(requestDateTime, dateTimeFormat));
		convertedDateTime.withZone(DateTimeZone.forID(timeZone));
		return convertedDateTime.toGregorianCalendar();
	}

	

	public static String getYearDisplayString(final Date date) {
		return DateTimeFormat.forPattern("YYYY").print(new DateTime(date));
	}

	public static boolean isDateBefore(final Date date1, final Date date2, final int hours) {
		DateTime dt1 = new DateTime(date1);
		DateTime dt2 = new DateTime(date2);
		if (dt1.plusHours(hours).isBefore(dt2)) {
			return true;
		}
		return false;
	}

	public static boolean isDateBeforeMillisecondOffset(final Date dateToModify, final Date date2, final int millisecondsToAdd) {
		DateTime dt1 = new DateTime(dateToModify);
		DateTime dt2 = new DateTime(date2);
		if (dt1.plusMillis(millisecondsToAdd).isBefore(dt2)) {
			return true;
		}
		return false;
	}

	public static boolean isDateBeforeNow(final Date date) {
		return (new DateTime(date)).isBeforeNow();
	}

	public static boolean isDateBeforeToday(final Date date) {
		return date.before((new DateMidnight()).toDate());
	}

	// only compare the day
	public static boolean isDateInBetween(final Date start, final Date end, final Date date) {
		LocalDate localStartDate	= new LocalDate(start);
		LocalDate localEndDate 		= new LocalDate(end);
		LocalDate localDate 		= new LocalDate(date);

		if ((localStartDate.isBefore(localDate) || localStartDate.isEqual(localDate))
				&& (localDate.isBefore(localEndDate) || localDate.isEqual(localEndDate))) {
			return true;
		}
		return false;
	}

	public static boolean isSameDay(final Date date1, final Date date2) {
		LocalDate dt1 = new LocalDate(date1);
		LocalDate dt2 = new LocalDate(date2);

		return dt1.isEqual(dt2);
	}

	public static Date getFirstMillisecondOfDate(final Date date) {
		if (date == null) {
			return null;
		}
		return new DateMidnight(date).toDate();
	}

	public static Date getLastMillisecondOfDate(final Date date) {
		if (date == null) {
			return null;
		}
		return new DateMidnight(date).toDateTime().plusDays(1).minus(new Duration(1)).toDate();
	}

	public static List<Date> getPastMonths(final Date date, final int noOfMonths) {

		if (date == null) {
			return new ArrayList<Date>(0);
		}

		List<Date> months = new ArrayList<Date>();

		for (int i = 1; i <= noOfMonths; i++) {
			months.add(getStartOfMonthMinusMonths(date, i));
		}
		return months;
	}

	public static String getMonthYearDisplayString(final Date date) {
		return DateTimeFormat.forPattern("MMMM YYYY").print(new DateTime(date));
	}

	public static Date getStartOfPreviousMonth() {
		return getStartOfMonthMinusMonths(new Date(), 1);
	}

	public static Date getStartOfCurrentMonth() {
		return DateUtil.getStartOfMonth(Calendar.getInstance().getTime());
	}
	//TODO: Use PeriodFormatterBuilder
	public static String getDisplayDays(int totalHours) {
		StringBuffer displayDays	= new StringBuffer(100);
		boolean daysAppended  		= appendDays(displayDays, totalHours);
		appendHours(displayDays, totalHours, daysAppended);
		return displayDays.toString();
	}

	private static boolean appendDays(StringBuffer displayDays, int totalHours) {
		boolean daysAppended 	= false;
		int days				= totalHours / 24;
		if (days > 0) {
			displayDays.append(days + " Day");
			pluralize(displayDays, days);
			daysAppended = true;
		}
		return daysAppended;
	}

	private static void appendHours(StringBuffer displayDays, int totalHours, boolean daysAppended) {
		int remainingHours 	= totalHours % 24;
		if (daysAppended && remainingHours > 0) {
			displayDays.append(" ");
		}
		if (remainingHours > 0) {
			displayDays.append(remainingHours + " Hour");
			pluralize(displayDays, remainingHours);
		}
	}

	private static void pluralize(StringBuffer displayDays, int count) {
		if (count > 1) {
			displayDays.append("s");
		}
	}

	public static Days getDaysBetween(final Date dateTime1, final Date dateTime2) {

		DateMidnight dt1	= new DateMidnight(dateTime1);
		DateMidnight dt2	= new DateMidnight(dateTime2);
		Days days			= Days.daysBetween(dt1, dt2);
		return days;
	}

	private static Calendar getIntializedCalendar(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	private static Calendar getEmptyCalendar() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		return cal;
	}

	private static void setMinimumTime(final Calendar cal) {

		cal.set(Calendar.HOUR, cal.getActualMinimum(Calendar.HOUR));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal
				.getActualMinimum(Calendar.MILLISECOND));
		cal.set(Calendar.AM_PM, cal.getActualMinimum(Calendar.AM_PM));
	}

	private static void setMaximumTime(final Calendar cal) {
		cal.set(Calendar.HOUR, cal.getActualMaximum(Calendar.HOUR));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal
				.getActualMaximum(Calendar.MILLISECOND));
		cal.set(Calendar.AM_PM, cal.getActualMaximum(Calendar.AM_PM));
	}
	//TODO: Rename this method
	public static String getRoundedOffDays(final int totalHours) {
		int days			= (int) Math.ceil((double) totalHours / HOURS_IN_A_DAY);
		StringBuffer result	= new StringBuffer(100);
		result.append(days);
		result.append(" Day");
		pluralize(result, days);
		return result.toString();
	}

	//TODO: Rename this method
	public static String getDurationDays(final int totalHours) {
		int days			= (int) Math.ceil((double) totalHours / HOURS_IN_A_DAY);
		StringBuffer result	= new StringBuffer(100);
		result.append(days);
		return result.toString();
	}

	public static Integer getDays(final int totalHours) {
		int days = (int) Math.ceil((double) totalHours / HOURS_IN_A_DAY);
		return days;
	}

	public static long hoursBetween(final Date start, final Date end) {
		long milliSeconds = (end.getTime() - start.getTime());

		long hours = milliSeconds / ONE_HOUR;
		long minsInMilliSecs = milliSeconds % ONE_HOUR;
		long mins = minsInMilliSecs / ONE_MIN;
		if (mins > 0) {
			hours = hours + 1;
		}
		return hours;
	}

	public static int getMinutesBetween(Date startDate, Date endDate) {
		DateTime start	= new DateTime(startDate);
		DateTime end	= new DateTime(endDate);

		return Minutes.minutesBetween(start, end).getMinutes();
	}

	public static Timestamp getTimestamp(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        month--;
        if (month < 0) {
            month = 0;
        } else if (month > 11) {
            month %= 12;
        }
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        if (day < 1) {
            day = 1;
        } else if (day > cal.getActualMaximum(Calendar.DATE)) {
            day = cal.getActualMaximum(Calendar.DATE);
        }

        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return new Timestamp(cal.getTime().getTime());
    }

	public static Long getHours(long milliSeconds) {
		return milliSeconds / ONE_HOUR;
	}

 /**
     * Returns no.of days between specified days. Result is always a positive number.
     */
    public static int getNumOfDaysBetween(final Date d1, final Date d2)
    {
        return getDaysBetween(d1, d2, true);
    }

    /**
     * Returns no.of days between specified days. If absoluteValue is true, result is always a positive number. If
     * absoluteValue is false, and d2 is less than d1 result would be negative.
     */
    public static int getDaysBetween(final Date d1, final Date d2, final boolean absoluteValue)
    {
        if (d1 != null && d2 != null)
        {
            final Calendar c1 = new GregorianCalendar();
            c1.setTime(d1);

            final Calendar c2 = new GregorianCalendar();
            c2.setTime(d2);

            return getDaysBetween(c1, c2, absoluteValue);
        }

        return 0;
    }
    /**
     * Calculates the number of days between two calendar days in a manner which is independent of the Calendar type
     * used.
     * 
     * @param d1
     *            The first date.
     * @param d2
     *            The second date.
     * @return The number of days between the two dates. Zero is returned if the dates are the same, one if the dates are
     *         adjacent, etc. The order of the dates does not matter, the value returned is always >= 0. If Calendar
     *         types of d1 and d2 are different, the result may not be accurate.
     */
    public static int getDaysBetween(Calendar d1, Calendar d2, final boolean absoluteValue)
    {
        boolean datesSwapped = false;

        if (d1 != null && d2 != null)
        {
            if (d1.after(d2))
            { // swap dates so that d1 is start and d2 is end
                final java.util.Calendar temp = d1;
                d1 = d2;
                d2 = temp;
                datesSwapped = true;
            }
            int days = d2.get(java.util.Calendar.DAY_OF_YEAR) - d1.get(java.util.Calendar.DAY_OF_YEAR);
            final int y2 = d2.get(java.util.Calendar.YEAR);
            if (d1.get(java.util.Calendar.YEAR) != y2)
            {
                d1 = (java.util.Calendar) d1.clone();
                do
                {
                    days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
                    d1.add(java.util.Calendar.YEAR, 1);
                }
                while (d1.get(java.util.Calendar.YEAR) != y2);
            }

            if (datesSwapped && !absoluteValue)
                return -(days);
            else
                return days;
        }

        return 0;
    }
    
    public static Date resetDatetoNextDayStartHours(Date date){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.set(Calendar.DATE ,cal.get(Calendar.DATE)+1);
    	cal.set(Calendar.HOUR_OF_DAY ,0);
    	cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
    	
    }
	
	
	public static String convertDateStringToNCDateTimeString(String dateString){
		int endIndex = dateString.indexOf(".");
		String ncDateTimeString = dateString;
		if(endIndex>0){
			ncDateTimeString = ncDateTimeString.substring(0, endIndex);
		}
		return ncDateTimeString + "Z";
	}
	
	public static String getTodayDateAsString(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		try {
			Date today =  dateFormat.parse(dateFormat.format(new Date()));
			return dateFormat.format(today);
		} catch (ParseException e) {
			
		}
		return StringUtils.EMPTY;
	}
	
	
	public static Date getLaterDateOfTwoDates(Date dateOne, Date dateTwo) {
		Date laterDateVal = null;
		if(dateOne != null && dateTwo != null ){
			laterDateVal = dateOne.after(dateTwo) ? dateOne : dateTwo; 
		}else {
			laterDateVal =  dateOne != null ? dateOne : dateTwo;  
		}
		return laterDateVal;
	}
	
	// This method is for TIH Demurrage accessorial not for common use. Please check the functionality before using it.
	
	public static Date getEarlierDateOfTwoDate(Date date1, Date date2) {
		if(null == date1 && null == date2){
			return new Date();
		}
		else if(null != date1 && null != date2){
			return date1.before(date2) ? date1 : date2; 
		}else {
			return null != date1 ? date1 : date2;  
		}
	}
	
	
	public static String getYearString(String yearMonthDayString){
		return yearMonthDayString.substring(0,yearMonthDayString.indexOf("-"));
	}
	
	public static String getMonthString(String yearMonthDayString){
		return yearMonthDayString.substring(yearMonthDayString.indexOf("-")+1,yearMonthDayString.lastIndexOf("-"));
	}
	
	public static String getDayString(String yearMonthDayString){
		return yearMonthDayString.substring(yearMonthDayString.lastIndexOf("-")+1,yearMonthDayString.length());
	}
	
	
	public static String getHourString(String hourMinuteSecondString){
		return hourMinuteSecondString.substring(0,hourMinuteSecondString.indexOf(":"));
	}
	
	public static String getMinuteString(String hourMinuteSecondString){
		return hourMinuteSecondString.substring(hourMinuteSecondString.indexOf(":")+1,hourMinuteSecondString.lastIndexOf(":"));
	}
	
	public static String getSecondString(String hourMinuteSecondString){
		if(hourMinuteSecondString.contains(".")){
			return hourMinuteSecondString.substring(hourMinuteSecondString.lastIndexOf(":")+1,hourMinuteSecondString.lastIndexOf("."));
		}
		else{
			return hourMinuteSecondString.substring(hourMinuteSecondString.lastIndexOf(":")+1,hourMinuteSecondString.length());
		}
	}
	
	public static String getTimeZoneString(String xmlString){
		int dashIndex = xmlString.lastIndexOf("-0");
		int colonIndex = xmlString.lastIndexOf(":");
		if(((dashIndex>=0) && (colonIndex>=0)) && (colonIndex>dashIndex)){
			return xmlString.substring(dashIndex+1, colonIndex);
		}
		else{
			return "";
		}
	}
	
	public static int convertAdjustedTimeDurationToDays(long adjustmentTimeDuration){
		return (int)Math.ceil(adjustmentTimeDuration/MILLISECONDS_PER_DAY);
	}
	
	public static double convertTimeDurationInMillisecondsToHours(long runTimeInMs){
		return runTimeInMs/(MILLISECONDS_PER_HOUR);
	}
	
	public static String convertDurationMilliSecondsToHourMinuteFormat(long milliSeconds) {
		// TODO: this is the value in ms
		//long ms = 8000000;
		StringBuffer text = new StringBuffer("");
		if (milliSeconds > DAY) {
		//	text.append(ms / DAY).append(":");
			milliSeconds %= DAY;
		}
		if (milliSeconds > ONE_HOUR) {
			text.append(milliSeconds / ONE_HOUR).append(":");
			milliSeconds %= ONE_HOUR;
		}else {
			text.append("00:");
		}
		if (milliSeconds > ONE_MIN) {
			text.append(milliSeconds / ONE_MIN).append("");
			milliSeconds %= ONE_MIN;
		}else{
			text.append("00");
		}
		if (milliSeconds > ONE_SEC) {
			//text.append(ms / SECOND).append(":");
			milliSeconds %= ONE_SEC;
		}
		//text.append(ms + " ms");
		//System.out.println(text.toString());
		return text.toString();
	}

	public static String convertTimeToHourMinutePercentageFormat(String timeVal) {
		StringTokenizer tokenizer = new StringTokenizer(timeVal,":.");
		StringBuffer dateTimeVal = new StringBuffer();
		if(tokenizer.hasMoreTokens()){
			String hoursVal = tokenizer.nextToken();
			dateTimeVal.append(hoursVal+".");
			String minutesVal = tokenizer.nextToken();
			if (StringUtils.isNotBlank(minutesVal)){
				Long val =(Long.valueOf(minutesVal)*100)/60;
				dateTimeVal.append(val/10);
			}
		}
		return dateTimeVal.toString();
	}

	public static String convertMinutePercentageToActualMinutsFormat(String timeVal) {
		StringTokenizer tokenizer = new StringTokenizer(timeVal,":.");
		StringBuffer dateTimeVal = new StringBuffer();
		if (tokenizer.hasMoreTokens()){
			String hours = tokenizer.nextToken();
			dateTimeVal.append(hours);
			String minutesVal = tokenizer.nextToken();
			Float float1 = Float.valueOf("."+minutesVal)*60;
			dateTimeVal.append(":"+ float1.intValue());
			//System.out.println(hours+":"+ float1.intValue());
		}
		return dateTimeVal.toString();
	}
	
	public static Long convertToMinutes(String hoursMinutesVal){
		StringTokenizer tokenizer = new StringTokenizer(hoursMinutesVal,":.");
		if(tokenizer.hasMoreTokens()){
			String hoursVal = tokenizer.nextToken();
			String minutesVal = tokenizer.nextToken();
			
			Long hoursToMin = Long.valueOf(hoursVal) * 60;
			Long TotalMinutsVal = hoursToMin + Long.valueOf(minutesVal);
			return TotalMinutsVal;
		}
		return 0L;
	}
	
	
	public static void main(String[] args) {
		String oneVal = convertDurationMilliSecondsToHourMinuteFormat(960000L);
		String SecondVal = convertTimeToHourMinutePercentageFormat("1:30");
		String thirdVal = convertMinutePercentageToActualMinutsFormat("4.75");
		System.out.println(oneVal);
		System.out.println(SecondVal);
		System.out.println(thirdVal);
		
		Long totalMinutes = convertToMinutes(oneVal) + convertToMinutes(SecondVal) + convertToMinutes(thirdVal);
		
		String finalHoursAndMinuts = convertDurationMilliSecondsToHourMinuteFormat(Long.valueOf(totalMinutes*60*1000));
		System.out.println(finalHoursAndMinuts);
		//System.out.println(convertDurationMilliSecondsToHourMinuteFormat(11124000l));
		//System.out.println(convertTimeToHourMinutePercentageFormat("1:25"));
		//System.out.println(convertMinutePercentageToActualMinutsFormat("4.75"));
		
	}
}
