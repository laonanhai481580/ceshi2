package com.ambition.supplier.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtil {
	private static SimpleDateFormat getSDF(){
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	private static SimpleDateFormat getTimeSDF(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}
	/**
	 * 格式化日期字符串
	 * @param date
	 * @return
	 */
	public static String formateDateStr(Date date){
		if(date != null){
			return getSDF().format(date);
		}else{
			return "";
		}
	}
	
	/**
	 * 格式化日期字符串
	 * @param date
	 * @return
	 */
	public static String formateDateStr(Date date,String formateStr){
		if(date != null){
			return new SimpleDateFormat(formateStr).format(date);
		}else{
			return "";
		}
	}
	
	/**
	 * 格式化日期字符串
	 * @param date
	 * @return
	 */
	public static String formateDateStr(java.sql.Date date){
		if(date != null){
			return getSDF().format(date);
		}else{
			return "";
		}
	}
	
	/**
	 * 格式化日期字符串
	 * @param date
	 * @return
	 */
	public static String formateDateStr(Calendar calendar){
		if(calendar != null){
			return getSDF().format(calendar.getTime());
		}else{
			return "";
		}
	}
	
	/**
	 * 格式化日期字符串
	 * @param date
	 * @return
	 */
	public static String formateTimeStr(Date date){
		if(date != null){
			return getTimeSDF().format(date);
		}else{
			return "";
		}
	}
	
	/**
	 * 格式化日期字符串
	 * @param date
	 * @return
	 */
	public static String formateTimeStr(java.sql.Date date){
		if(date != null){
			return getTimeSDF().format(date);
		}else{
			return "";
		}
	}
	
	/**
	 * 格式化日期字符串
	 * @param date
	 * @return
	 */
	public static String formateTimeStr(Calendar calendar){
		if(calendar != null){
			return getTimeSDF().format(calendar.getTime());
		}else{
			return "";
		}
	}
	
	/**
	 * 格式化日期时间:2012-04-01 12:30
	 * @param dateStr
	 * @return
	 */
	public static Date parseDateTime(String dateStr){
		if(StringUtils.isNotEmpty(dateStr)){
			try {
				return getTimeSDF().parse(dateStr);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else{
			return null;
		}
	}
	
	/**
	 * 格式化日期
	 * @param dateStr
	 * @return
	 */
	public static Date parseDate(String dateStr){
		if(StringUtils.isNotEmpty(dateStr)){
			try {
				return getSDF().parse(dateStr);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else{
			return null;
		}
	}
	/**
	 * 格式化日期
	 * @param dateStr
	 * @return
	 */
	public static Date parseDate(String dateStr,String formatStr){
		if(StringUtils.isNotEmpty(dateStr)){
			try {
				if(dateStr.endsWith("+08:00")){
					dateStr = dateStr.substring(0,dateStr.indexOf("+"));
				}
				if(dateStr.indexOf("T")>-1){
					dateStr = dateStr.replaceAll("T"," ");
				}
				return new SimpleDateFormat(formatStr).parse(dateStr);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else{
			return null;
		}
	}
	/**
	 * 格式化日期
	 * @param dateStr
	 * @return
	 */
	public static java.sql.Date parseSqlDate(String dateStr){
		if(StringUtils.isNotEmpty(dateStr)){
			try {
				return new java.sql.Date(getSDF().parse(dateStr).getTime());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else{
			return null;
		}
	}
}
