package com.voltor.futureleave.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.util.Strings;

public class DateTimeService {


	public static final String ZONED_DATE_TIME_RESPONSE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSxxx";
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

	private static final DateTimeFormatter LOCAL_DATE_API_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private DateTimeService() {
	}

	public static ZonedDateTime now() {
		return now( ZoneOffset.UTC );
	}

	public static ZonedDateTime now( ZoneOffset offset ) {
		return ZonedDateTime.now( offset );
	}

	public static ZonedDateTime toUTC( ZonedDateTime zonedDateTime ) {
		return zonedDateTime.withZoneSameInstant( ZoneOffset.UTC );
	}

	public static String dateToStringWithPattern( ZonedDateTime zonedDateTime, String pattern ) {
		return zonedDateTime.format( DateTimeFormatter.ofPattern( pattern ) );
	}

	public static ZonedDateTime toZonedDateTime( Date localDate ) {
		return localDate.toInstant().atZone( ZoneOffset.UTC );
	}
	
	/**
	 * This method is used to get first moment of day for UTC date (as String)
	 * in needed TimeZone.
	 * 
	 * @param dateStr
	 * @param timeZone
	 * @return
	 */
	public static ZonedDateTime getFirstMomentOfDate( LocalDate date, ZoneId zoneId ) {
		return ZonedDateTime.of( date, LocalTime.MIN, zoneId );
	}
	
	public static ZonedDateTime getFirstMomentOfLocalDate( String date, ZoneId zoneId ) { 
		return getFirstMomentOfDate( parseLocalDate( date ), zoneId );
	}

	public static LocalDate parseLocalDate( String date ) {
		return LocalDate.parse( date, LOCAL_DATE_API_FORMATTER );
	}

	/**
	 * This method is used to get last moment of day for UTC (as String) date in
	 * needed TimeZone.
	 * 
	 * @param dateStr
	 * @param timeZone
	 * @return
	 */
	public static ZonedDateTime getLastMomentOfDate( LocalDate date, ZoneId zoneId ) {
		return ZonedDateTime.of( date, LocalTime.MAX, zoneId );
	}
	
	public static ZonedDateTime getLastMomentOfLocalDate( String date, ZoneId zoneId ) { 
		return getLastMomentOfDate( parseLocalDate( date ), zoneId );
	}
	
	/**
	 * 
	 * @param timeZoneId
	 * @return
	 */
	public static boolean isTimeZoneIdValid( String timeZoneId ) {
		if ( Strings.isBlank( timeZoneId ) ) {	
			throw new IllegalArgumentException( "Can't validate timezone ID, argument is null." );
		}
		
		String[] timeZoneIds = TimeZone.getAvailableIDs();
		return ArrayUtils.contains( timeZoneIds, timeZoneId );
	}
}
