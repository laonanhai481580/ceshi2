package com.ambition.spc.util;

import com.ambition.spc.entity.ADTestForm;


/**    
 * NormalJudgeUtil.java
 * @authorBy wanglf
 *
 */
public class NormalJudgeUtil
{
  public static double[] calSkewnessAndKurtosis(double[] data, double avg, double s)
  {
    int length = 0;
    if (data != null) length = data.length;
    double[] ret = (double[])null;
    if ((length > 2) && (s > 0.0D)) {
      ret = new double[2];
      for (int i = 0; i < length; i++) {
        ret[0] += Math.pow(data[i] - avg, 3.0D);
        ret[1] += Math.pow(data[i] - avg, 4.0D);
      }
      ret[0] = (ret[0] / Math.pow(s, 3.0D) / (length - 1));
      ret[1] = (ret[1] / Math.pow(s, 4.0D) / (length - 1) - 3.0D);
    }
    return ret;
  }

  public static boolean skewnessAndKurtosisTest(double[] data, double avg, double s)
  {
    boolean ztYN = false;
    int length = 0;
    if (data != null) length = data.length;
    if ((length >= 30) && (s > 0.0D)) {
      double u3 = 0.0D; double u4 = 0.0D;
      for (int i = 0; i < length; i++) {
        u3 += Math.pow(data[i] - avg, 3.0D);
        u4 += Math.pow(data[i] - avg, 4.0D);
      }

      u3 = u3 / length / Math.pow(s, 3.0D);
      u4 = u4 / length / Math.pow(s, 4.0D) - 3.0D;
      if ((u3 > -2.0D * Math.sqrt(6.0D / length)) && 
        (u3 < 2.0D * Math.sqrt(6.0D / length)) && 
        (u4 > -2.0D * Math.sqrt(24.0D / length)) && 
        (u4 < 2.0D * Math.sqrt(24.0D / length))) {
        ztYN = true;
      }
    }
    return ztYN;
  }

  public static ADTestForm AnDartest(double[] data)
  {
    return AnDartest(data, 0.05D);
  }

  public static ADTestForm AnDartest(double[] data, double alpha)
  {
    ADTestForm form = new ADTestForm();
    if ((data == null) || (alpha <= 0.0D) || (alpha >= 1.0D)) return form;
    int n = data.length;
    if (n < 7) return form;

    double std = Calculator.calculateS(data);
    if (std == 0.0D) return form;
    double avg = Calculator.average(data);

    double[] data_ls = new double[n];
    for (int i = 0; i < n; i++) {
      data_ls[i] = data[i];
    }

    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        if (data_ls[i] > data_ls[j]) {
          double dx = data_ls[i];
          data_ls[i] = data_ls[j];
          data_ls[j] = dx;
        }
      }
    }

    double[] fx = new double[n];
    for (int i = 0; i < n; i++) {
      fx[i] = NormalSchool.zt((data_ls[i] - avg) / std);
    }
    double S = 0.0D;
    for (int i = 0; i < n; i++) {
      if ((fx[i] > 0.0D) && (1.0D - fx[(n - 1 - i)] > 0.0D))
        S += (2 * i + 1) * (Math.log(fx[i]) + Math.log(1.0D - fx[(n - 1 - i)]));
    }
    S /= n;
    double AD2 = -n - S;
    double AD2a = AD2 * (1.0D + 0.75D / n + 2.25D / n / n);
    double P = 0.0D;
    if ((AD2a >= 0.0D) && (AD2a < 0.2D)) {
      P = 1.0D - Math.exp(-13.436D + 101.14D * AD2a - 223.72999999999999D * AD2a * AD2a);
    }
    else if ((AD2a >= 0.2D) && (AD2a < 0.34D)) {
      P = 1.0D - Math.exp(-8.318D + 42.795999999999999D * AD2a - 59.938000000000002D * AD2a * AD2a);
    }
    else if ((AD2a >= 0.34D) && (AD2a < 0.6D)) {
      P = Math.exp(0.9177D - 4.279D * AD2a - 1.38D * AD2a * AD2a);
    }
    else if ((AD2a >= 0.6D) && (AD2a <= 13.0D)) {
      P = Math.exp(1.2937D - 5.709D * AD2a + 0.0186D * AD2a * AD2a);
    }

    form.setAD(AD2);
    form.setAlpha(alpha);
    form.setP(P);
    if (P >= alpha) {
      form.setIsNormal(true);
    }
    return form;
  }
}