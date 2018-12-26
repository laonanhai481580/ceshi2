package com.ambition.spc.util;


/**    
 * MathUtil.java
 * @authorBy wanglf
 *
 */

public class MathUtil
{
  public static double shx(double x)
  {
    return (Math.exp(x) - Math.exp(-x)) / 2.0D;
  }

  public static double chx(double x)
  {
    return (Math.exp(x) + Math.exp(-x)) / 2.0D;
  }

  public static double thx(double x)
  {
    return (Math.exp(x) - Math.exp(-x)) / (Math.exp(x) + Math.exp(-x));
  }

  public static double arshx(double x)
  {
    return Math.log(x + Math.sqrt(x * x + 1.0D));
  }

  public static double archx(double x)
  {
    if (x < 1.0D) return (0.0D / 0.0D);
    return Math.log(x + Math.sqrt(x * x - 1.0D));
  }

  public static double arthx(double x)
  {
    if (Math.abs(x) >= 1.0D) return (0.0D / 0.0D);
    return Math.log((1.0D + x) / (1.0D - x) / 2.0D);
  }
}