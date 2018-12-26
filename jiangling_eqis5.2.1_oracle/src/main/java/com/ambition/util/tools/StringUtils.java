package com.ambition.util.tools;

import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang.StringUtils {

	private static char[] chars = 
	{
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	};

	private static char[] upperChars = 
	{
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	};

	private static int charsLen = chars.length;
	private static int upperCharsLen = upperChars.length;

	public static String longToString(long n) {
		StringBuffer sb = new StringBuffer();
		while (n != 0) {
			sb.insert(0, chars[(int)(n % charsLen)]);
			n /= charsLen;
		}
		return sb.toString();
	}

	public static String longToUpperString(long n) {
		StringBuffer sb = new StringBuffer();
		while (n != 0) {
			sb.insert(0, upperChars[(int)(n % upperCharsLen)]);
			n /= upperCharsLen;
		}
		return sb.toString();
	}

	public static String format(Object value, int length, boolean alignRight, char fill) {
		String v = toString(value);
		StringBuffer result = new StringBuffer(v);
		int len = length - v.getBytes().length;
		for (int i = 0; i < len; i++) {
			if (alignRight) {
				result.insert(0, fill);
			}
			else {
				result.append(fill);
			}
		}
		return result.toString();
	}
	
	public static String format(Object value, int length, boolean alignRight) {
		return format(value, length, alignRight, ' ');
	}

	public static String format(Object value, int length, char fill) {
		return format(value, length, false, fill);
	}

	public static String format(Object value, int length) {
		return format(value, length, false, ' ');
	}

	public static String toString(Object str) {
		return str == null ? "" : str.toString();
	}

	public static String insert(String str, int index, String substring) {
		return str.substring(0, index) + substring + str.substring(index);
	}

	public static boolean isCode(String str) {
		return Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(str).matches();
	}

	public static void main(String[] args) {
//		System.out.println(isCode("_DSFaasdfasdfDFSFZ123asdfasdf"));
//		Long aaa = null;
//		System.out.println(format(12, "###"));
//		System.out.println(format(12.2, "0.00"));
//		System.out.println(Long.MAX_VALUE);
//		System.out.println(Double.MAX_VALUE);
		
//		System.out.println(format(".45", 10, false));
		
	}
	

}
