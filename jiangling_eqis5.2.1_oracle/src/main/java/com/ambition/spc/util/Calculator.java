package com.ambition.spc.util;

/**    
 * Calculator.java(基础算法,如：平均值,最大值等)
 * @authorBy wanglf
 *
 */

public class Calculator
{
	/**
	 * 一维数组计算最大值
	 * @param double[]
	 * @return double max[]
	 */
  public static double max(double[] data)
  {
    if ((data == null) || (data.length < 1)) return 0.0D;
    double max = data[0];
    for (int i = 1; i < data.length; i++) {
      max = Math.max(data[i], max);
    }
    return max;
  }
  
  /**
   * 二维数组计算最大值
   * @param double[][]
   * @return double max[][]
   */
  public static double max(double[][] data)
  {
    if ((data == null) || (data.length < 1) || (data[0].length < 1)) return 0.0D;
    double max = data[0][0];
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        max = Math.max(data[i][j], max);
      }
    }
    return max;
  }

  /**
	 * 一维数组计算最大值
	 * @param Double[]
	 * @return double max[]
	 */
  public static double max(Double[] data)
  {
    if ((data == null) || (data.length < 1)) return 0.0D;
    double max = 0.0D;
    boolean dataCom = false;
    for (int i = 0; i < data.length; i++) {
      if (data[i] != null) {
        if (dataCom) {
          max = Math.max(data[i].doubleValue(), max);
        }
        else {
          max = data[i].doubleValue();
          dataCom = true;
        }
      }
    }
    return max;
  }

  /**
   * 二维数组计算最大值
   * @param Double[][]
   * @return double max[][]
   */
  public static double max(Double[][] data)
  {
    if ((data == null) || (data.length < 1) || (data[0].length < 1)) return 0.0D;
    double max = 0.0D;
    boolean dataCom = false;
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        if (data[i][j] != null) {
          if (dataCom) {
            max = Math.max(data[i][j].doubleValue(), max);
          }
          else {
            max = data[i][j].doubleValue();
            dataCom = true;
          }
        }
      }
    }
    return max;
  }
  

  /**
   * 一维数组计算最小值
   * @param double[]
   * @return double min[]
   */
  public static double min(double[] data)
  {
    if ((data == null) || (data.length < 1)) return 0.0D;
    double min = data[0];
    for (int i = 1; i < data.length; i++) {
      min = Math.min(data[i], min);
    }
    return min;
  }
  
  /**
   * 二维数组计算最小值
   * @param double[][]
   * @return double min[][]
   */
  public static double min(double[][] data)
  {
    if ((data == null) || (data.length < 1) || (data[0].length < 1)) return 0.0D;
    double min = data[0][0];
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        min = Math.min(data[i][j], min);
      }
    }
    return min;
  }

  /**
   * 一维数组计算最小值
   * @param Double[]
   * @return double min[]
   */
  public static double min(Double[] data)
  {
    if ((data == null) || (data.length < 1)) return 0.0D;
    double min = 0.0D;
    boolean dataCom = false;
    for (int i = 0; i < data.length; i++) {
      if (data[i] != null) {
        if (dataCom) {
          min = Math.min(data[i].doubleValue(), min);
        }
        else {
          min = data[i].doubleValue();
          dataCom = true;
        }
      }
    }
    return min;
  }

  /**
   * 二维数组计算最小值
   * @param Double[][]
   * @return double min[][]
   */
  public static double min(Double[][] data)
  {
    if ((data == null) || (data.length < 1) || (data[0].length < 1)) return 0.0D;
    double min = 0.0D;
    boolean dataCom = false;
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        if (data[i][j] != null) {
          if (dataCom) {
            min = Math.min(data[i][j].doubleValue(), min);
          }
          else {
            min = data[i][j].doubleValue();
            dataCom = true;
          }
        }
      }
    }
    return min;
  }

  /**
   * 一维数组计算最大值的绝对值
   * @param double[]
   * @return double maxAbs
   */
  public static double maxAbs(double[] data)
  {
    if ((data == null) || (data.length < 1)) return 0.0D;
    double max = 0.0D;
    for (int i = 0; i < data.length; i++) {
      max = Math.max(Math.abs(data[i]), max);
    }
    return max;
  }

  /**
   * 二维数组计算最大值的绝对值
   * @param double[][]
   * @return double maxAbs
   */
  public static double maxAbs(double[][] data)
  {
    if ((data == null) || (data.length < 1) || (data[0].length < 1)) return 0.0D;
    double max = 0.0D;
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        max = Math.max(Math.abs(data[i][j]), max);
      }
    }
    return max;
  }

  /**
   * 一维数组求和
   * @param double[]
   * @return double 
   */
  public static double sum(double[] data)
  {
    if ((data == null) || (data.length < 1)) return 0.0D;
    double sum = data[0];
    for (int i = 1; i < data.length; i++) {
      sum += data[i];
    }
    return sum;
  }
  
  /**
   * 二维数组求和
   * @param double[][]
   * @return double 
   */
  public static double sum(double[][] data)
  {
    if ((data == null) || (data.length < 1) || (data[0].length < 1)) return 0.0D;
    double sum = 0.0D;
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[0].length; j++) {
        sum += data[i][j];
      }
    }
    return sum;
  }

  /**
   * 一维数组求平均数
   * @param double[]
   * @return double 
   */
  public static double average(double[] data)
  {
    if ((data == null) || (data.length < 1)) return 0.0D;
    return sum(data) / data.length;
  }

  /**
   * 二维数组求平均数
   * @param double[][]
   * @return double 
   */
  public static double average(double[][] data)
  {
    if ((data == null) || (data.length < 1) || (data[0].length < 1)) return 0.0D;
    return sum(data) / data.length / data[0].length;
  }
  
  /**
   * 一维数组求 Range极差
   * @param double[]
   * @return double 
   */
  public static double calculateR(double[] data)
  {
    return max(data) - min(data);
  }

  /**
   * 二维数组求 Range极差
   * @param double[][]
   * @return double 
   */
  public static double calculateR(double[][] data)
  {
    return max(data) - min(data);
  }

  
  public static double calculateRS(double firstDate, double nextDate)
  {
    return Math.abs(firstDate - nextDate);
  }

  
  public static double calculateAvgOfRS(double[] data)
  {
    if ((data == null) || (data.length < 2)) {
      return 0.0D;
    }
    double da = 0.0D;
    for (int i = 1; i < data.length; i++) {
      da += calculateRS(data[i], data[(i - 1)]);
    }
    return da / (data.length - 1);
  }

  public static double calculateAvgOfRS(double[][] data)
  {
    if ((data == null) || (data.length * data[0].length < 2)) {
      return 0.0D;
    }
    double da = 0.0D;
    for (int i = 0; i < data.length; i++) {
      if (i > 0) {
        da += calculateRS(data[i][0], data[(i - 1)][(data[0].length - 1)]);
      }
      for (int j = 1; j < data[0].length; j++) {
        da += calculateRS(data[i][j], data[i][(j - 1)]);
      }
    }
    return da / (data.length * data[0].length - 1);
  }

  public static double sSquare(double[] data)
  {
//    if ((data == null) || (data.length < 2)) return 0.0D;
//    double temp = 0.0D;
//    for (int i = 0; i < data.length; i++) {
//      temp += data[i] * data[i];
//    }
//    double sum = sum(data);
//    temp = (temp - sum * sum / data.length) / (data.length - 1);
//    if (temp < 0.0D) temp = 0.0D;
//    return temp;
	  if ((data == null) || (data.length < 2)) return 0.0D;
	  double allAverage = sum(data)/data.length;
	  double temp = 0.0D;
	  for (int i = 0; i < data.length; i++) {
	    double val = (data[i]-allAverage);
	    temp += val * val;
	  }
	  return temp/(data.length-1);
  }
  public static double sOneSquare(double[] data,double alldataaverage)
  {
	  if(data.length== 0){
	    	return 0.0D;
	    }
	  if ((data.length!= 0) || (data.length < 2)) 
    {
    	return (data[0]-alldataaverage)*(data[0]-alldataaverage);
    }
    
    double temp = 0.0D;
    for (int i = 0; i < data.length; i++) {
      temp += data[i] * data[i];
    }
    double sum = sum(data);
    temp = (temp - sum * sum / data.length) / (data.length - 1);
    if (temp < 0.0D) temp = 0.0D;
    return temp;
  }
  
  
  public static double sSquare(double[][] data)
  {
    if (data == null) return 0.0D;
    int n = data.length;
    int m = data[0].length;
    if (n * m < 2) return 0.0D;
    double temp = 0.0D;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        temp += data[i][j] * data[i][j];
      }
    }
    double sum = sum(data);
    temp = (temp - sum * sum / (n * m)) / (n * m - 1);
    if (temp < 0.0D) temp = 0.0D;
    return temp;
  }

  /**
   * 一维数组求  StdDev标准差
   * @param double[]
   * @return double 
   */
  public static double calculateS(double[] data)
  {
    return Math.sqrt(sSquare(data));
  }
  
  /**
   * 一维数组求  StdDev标准差
   * @param double[]
   * @return double 
   */
  public static double calculateOneS(double[] data,double alldataaverage)
  {
    return Math.sqrt(sOneSquare(data,alldataaverage));
  }

  /**
   * 二维数组求  StdDev标准差
   * @param double[][]
   * @return double 
   */
  public static double calculateS(double[][] data)
  {
    return Math.sqrt(sSquare(data));
  }

  /**
   * 一维数组求  中位数
   * @param double[]
   * @return double 
   */
  public static double calculateMedian(double[] data)
  {
    if ((data == null) || (data.length < 1)) return 0.0D;
    int n = data.length;

    double[] Dou_LS = new double[n];
    for (int i = 0; i < n; i++) {
      Dou_LS[i] = data[i];
    }

    for (int j = 0; j < n - 1; j++) {
      for (int k = 1; k < n - j; k++) {
        if (Dou_LS[(k - 1)] > Dou_LS[k]) {
          double s_ls = Dou_LS[(k - 1)];
          Dou_LS[(k - 1)] = Dou_LS[k];
          Dou_LS[k] = s_ls;
        }
      }
    }

    if (n % 2 == 0) {
      n /= 2;
      return (Dou_LS[(n - 1)] + Dou_LS[n]) / 2.0D;
    }

    return Dou_LS[(n / 2)];
  }
}