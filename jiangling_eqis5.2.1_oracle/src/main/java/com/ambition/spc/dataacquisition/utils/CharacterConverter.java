package com.ambition.spc.dataacquisition.utils;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import sun.io.ByteToCharConverter;
import sun.io.CharToByteConverter;

/**
 * CharacterConverter.java
 * 
 * @authorBy YUKE
 * 
 */
@SuppressWarnings({ "deprecation" })
public class CharacterConverter {
	public static String ChineseStringToAscii(String s) {
		String result = "";
		try {
			if (s != null && s.trim().length() > 0) {
				CharToByteConverter toByte = CharToByteConverter
						.getConverter("gb2312");

				byte[] orig = toByte.convertAll(s.toCharArray());

				char[] dest = new char[orig.length];

				for (int i = 0; i < orig.length; i++)

					dest[i] = (char) (orig[i] & 0xFF);

				result = new String(dest);

			}
			return result;
		}

		catch (Exception e) {

			// System.out.println(e);

			return result;

		}

	}

	@SuppressWarnings("null")
	public static String ISOToGBK(String isostr) {
		try {
			String gbkStr = "";
			if (isostr != null || !isostr.equals("")) {
				byte[] byteStr = isostr.getBytes("ISO-8859-1");
				gbkStr = new String(byteStr, "GBK");
			}
			return gbkStr;
		} catch (Exception e) {
			return null;
		}
	}

	public static String ChineseStringToISO(String s) {
		String result = "";
		try {
			if (s != null && s.trim().length() > 0) {
				byte[] byteStr = s.getBytes("GB2312");
				result = new String(byteStr, "ISO-8859-1");
			}
			return result;
		} catch (Exception e) {
			return result;
		}

	}

	public static String AsciiToChineseString(String s) {
		String result = "";
		if (s != null && s.trim().length() > 0) {
			char[] orig = s.toCharArray();
			byte[] dest = new byte[orig.length];
			for (int i = 0; i < orig.length; i++)
				dest[i] = (byte) (orig[i] & 0xFF);
			try {

				ByteToCharConverter toChar = ByteToCharConverter
						.getConverter("GBK");
				result = new String(toChar.convertAll(dest));
				return result;

			} catch (Exception e) {
				// System.out.println(e);
				return result;
			}

		} else {
			return result;
		}
	}

	public static String encode(HttpServletRequest request, String fileName) {
		String ua = request.getHeader("User-Agent");
		try {
			if (ua.indexOf("MSIE") != -1) {// IE
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {// Firefox
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}
}
