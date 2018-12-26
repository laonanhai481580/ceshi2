package com.ambition.spc.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**    
 * StringUtil.java
 * @authorBy wanglf
 *
 */
public class StringUtil
{
  public static String formatdouble(double num, int precision)
  {
    return formatdouble(Double.toString(num), precision);
  }

  public static String formatdouble(float num, int precision)
  {
    return formatdouble(Float.toString(num), precision);
  }

  private static String formatdouble(String str, int precision)
  {
    int i = Math.max(str.indexOf("e"), str.indexOf("E"));
    String charx;
    if (i > 0) {
       charx= str.substring(0, i);
      str = str.substring(i);
    }
    else {
       charx = str;
      str = "";
    }
    String charxt = String.valueOf(Double.valueOf(charx).doubleValue() + 
      1.E-015D);
    precision = precision < 0 ? 0 : precision;
    NumberFormat formatter = NumberFormat.getNumberInstance();
    formatter.setGroupingUsed(false);
    formatter.setMaximumFractionDigits(precision);
    return formatter.format(Double.valueOf(charxt)) + str;
  }

  public static String formatdoubleAddZero(double num, int precision)
  {
    return formatdoubleAddZero(Double.toString(num), precision);
  }

  public static String formatdoubleAddZero(float num, int precision)
  {
    return formatdoubleAddZero(Float.toString(num), precision);
  }

  private static String formatdoubleAddZero(String str, int precision)
  {
    int i = Math.max(str.indexOf("e"), str.indexOf("E"));
    String charx;
    if (i > 0) {
       charx = str.substring(0, i);
      str = str.substring(i);
    }
    else {
      charx = str;
      str = "";
    }
    String charxt = String.valueOf(Double.valueOf(charx).doubleValue() + 
      1.E-015D);
    precision = precision < 0 ? 0 : precision;
    NumberFormat formatter = NumberFormat.getNumberInstance();
    formatter.setGroupingUsed(false);
    formatter.setMaximumFractionDigits(precision);
    charx = formatter.format(Double.valueOf(charxt));
    int zeroNum;
    if ((charx.indexOf(".") == -1) && (precision > 0)) {
       zeroNum = precision;
      charx = charx + ".";
    }
    else {
      zeroNum = precision - 
        charx.substring(charx.indexOf(".") + 1).length();
    }
    for (i = 0; i < zeroNum; i++) {
      charx = charx + "0";
    }

    return charx + str;
  }

  public static int DataDecimalDigits(double num)
  {
    return DataDecimalDigits(Double.toString(num));
  }

  public static int DataDecimalDigits(float num)
  {
    return DataDecimalDigits(Float.toString(num));
  }

  private static int DataDecimalDigits(String str)
  {
    int k = str.indexOf(".");
    int m;
    if (k >= 0) {
      int i = Math.max(str.indexOf("e"), str.indexOf("E"));
      if (i >= 0) {
        str = str.substring(0, i);
      }
       m = str.length() - k - 1;
      if (str.substring(str.length() - 1).equals("0"))
        m--;
    }
    else
    {
      m = 0;
    }
    return m;
  }

  public static double DataPrec(double num)
  {
    return DataPrec(Double.toString(num));
  }

  public static double DataPrec(float num)
  {
    return DataPrec(Float.toString(num));
  }

  private static double DataPrec(String str)
  {
    int m = 0; int j = 0;

    int i = Math.max(str.indexOf("e"), str.indexOf("E"));
    if (i >= 0) {
      j = Integer.valueOf(str.substring(i + 1)).intValue();
      str = str.substring(0, i);
    }

    int k = str.indexOf(".");
    if (k >= 0) {
      m = -str.length() + k + 1;

      if (str.substring(str.length() - 1).equals("0")) {
        m++;
      }
    }
    return Double.parseDouble("1.0E" + (m + j));
  }

  public static int radomColorNum()
  {
    float color = (float)Math.random();
    color *= 1000.0F;
    color %= 255.0F;
    return (int)color;
  }

  public static boolean isDouble(String str)
  {
    try
    {
      Double.parseDouble(str);
    }
    catch (NumberFormatException ex) {
      return false;
    }
    return true;
  }

  public static double dealData(double upLine, double lowLine)
  {
    double dataDouble = Math.abs(upLine - lowLine);

    DecimalFormat df = new DecimalFormat("0.00E00");
    String retStr = df.format(dataDouble);

    int n = Integer.parseInt(retStr.substring(5));
    retStr = retStr.substring(0, 4);
    dataDouble = Double.parseDouble(retStr);

    if (dataDouble / 0.1D <= 10.0D) {
      return Double.parseDouble("1.0E" + (n - 1));
    }
    if (dataDouble / 0.2D <= 10.0D) {
      return Double.parseDouble("2.0E" + (n - 1));
    }
    if (dataDouble / 0.5D <= 10.0D) {
      return Double.parseDouble("5.0E" + (n - 1));
    }

    return Double.parseDouble("1.0E" + n);
  }

  public static boolean equals(String str1, String str2)
  {
    if (str1 == str2) return true;
    return (str1 != null) && (str2 != null) && (str1.equals(str2));
  }

  public static String floatToTime(float time)
  {
    String str = "";

    float times = Math.round(time % 24.0F * 3600.0F);
    int t = (int)(times / 3600.0F);
    if (t < 10)
      str = str + "0" + t;
    else {
      str = str + t;
    }

    times -= 3600 * t;
    t = (int)(times / 60.0F);
    if (t < 10)
      str = str + ":0" + t;
    else {
      str = str + ":" + t;
    }

    times -= 60 * t;
    t = (int)times;
    if (t < 10)
      str = str + ":0" + t;
    else {
      str = str + ":" + t;
    }
    return str;
  }
}