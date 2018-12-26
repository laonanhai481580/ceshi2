package com.ambition.spc.util;

import com.ambition.spc.entity.ADTestForm;
import com.ambition.spc.entity.JohnsonTransferForm;

/**    
 * JohnsonTransfer.java
 * @authorBy wanglf
 *
 */
public class JohnsonTransfer
{
  public static JohnsonTransferForm johnsonTransfer(double[] data)
  {
    if ((data == null) || (data.length < 7)) return null;
    JohnsonTransferForm form = new JohnsonTransferForm();
    double z = 0.25D;
    double p = 0.0D;
    int n = data.length;
    double[] data_px = new double[n];
    double[] data_ls = new double[n];
    double[] q = new double[4];
    for (int i = 0; i < n; i++) {
      data_px[i] = data[i];
    }

    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        if (data_px[i] > data_px[j]) {
          double dx = data_px[i];
          data_px[i] = data_px[j];
          data_px[j] = dx;
        }
      }
    }

    for (int i = 0; i < 101; i++)
    {
      q[0] = NormalSchool.zt(-3.0D * z);
      q[1] = NormalSchool.zt(-z);
      q[2] = NormalSchool.zt(z);
      q[3] = NormalSchool.zt(3.0D * z);

      for (int j = 0; j < 4; j++) {
        q[j] = (n * q[j] + 0.5D);
      }

      for (int j = 0; j < 4; j++) {
        int index = (int)q[j];
        if (index < 1)
          q[j] = data_px[0];
        else if (index >= n) {
          q[j] = data_px[(n - 1)];
        }
        else {
          q[j] = 
            (data_px[(index - 1)] * (index + 1 - q[j]) + 
            data_px[index] * (q[j] - index));
        }
      }

      double lv = q[3] - q[2];
      double mv = q[1] - q[0];
      double pv = q[2] - q[1];
      double da;
      if (pv == 0.0D)
        da = 0.0D;
      else {
        da = lv * mv / pv / pv;
      }
      double qr = Double.parseDouble(StringUtil.formatdouble(da, 
        2));
      if (qr > 1.0D) {
        double c1 = 2.0D * z / MathUtil.archx((lv / pv + mv / pv) / 2.0D);
        double c2 = c1 * 
          MathUtil.arshx((mv - lv) / 
          Math.sqrt(lv * mv - pv * pv) / 2.0D);
        double c3 = 2.0D * pv * Math.sqrt(lv * mv / pv / pv - 1.0D) / (
          lv / pv + mv / pv - 2.0D) / Math.sqrt(lv / pv + mv / pv + 2.0D);
        double c4 = (q[1] + q[2]) / 2.0D + 
          pv * (mv - lv) / (lv + mv - 2.0D * pv) / 2.0D;
        if ((!Double.isNaN(c1)) && (!Double.isNaN(c2)) && (!Double.isNaN(c3)) && 
          (!Double.isNaN(c4)))
        {
          for (int j = 0; j < n; j++) {
            data_ls[j] = (c2 + c1 * MathUtil.arshx((data_px[j] - c4) / c3));
          }
          ADTestForm adform = NormalJudgeUtil.AnDartest(data_ls);
          if ((adform != null) && (adform.getIsNormal()) && (p < adform.getP()) && 
            (adform.getP() > 0.1D)) {
            p = adform.getP();
            form.setP(adform.getP());
            form.setZ(z);
            form.setType(1);
            form.setPar1(c1);
            form.setPar2(c2);
            form.setPar3(c3);
            form.setPar4(c4);
          }
        }
      }
      else if (qr < 1.0D) {
        double c1 = z / 
          MathUtil.archx(Math.sqrt((1.0D + pv / lv) * (1.0D + pv / mv)) / 2.0D);
        double c2 = c1 * 
          MathUtil.arshx((pv / mv - pv / lv) * 
          Math.sqrt((1.0D + pv / lv) * (
          1.0D + pv / mv) - 4.0D) / (pv * pv / lv / mv - 1.0D) / 2.0D);
        double c3 = pv * 
          Math.sqrt(Math.pow((1.0D + pv / lv) * (1.0D + pv / mv) - 2.0D, 2.0D) - 4.0D) / (
          pv * pv / lv / mv - 1.0D);
        double c4 = (q[1] + q[2]) / 2.0D - c3 / 2.0D + 
          pv * (pv / mv - pv / lv) / (pv * pv / lv / mv - 1.0D) / 2.0D;
        if ((!Double.isNaN(c1)) && (!Double.isNaN(c2)) && (!Double.isNaN(c3)) && 
          (!Double.isNaN(c4)))
        {
          if ((data_px[0] > c4) && (data_px[(n - 1)] < c4 + c3)) {
            for (int j = 0; j < n; j++) {
              data_ls[j] = 
                (c2 + 
                c1 * Math.log((data_px[j] - c4) / (c3 + c4 - data_px[j])));
            }
            ADTestForm adform = NormalJudgeUtil.AnDartest(data_ls);
            if ((adform != null) && (adform.getIsNormal()) && (p < adform.getP()) && 
              (adform.getP() > 0.1D)) {
              p = adform.getP();
              form.setP(adform.getP());
              form.setZ(z);
              form.setType(3);
              form.setPar1(c1);
              form.setPar2(c2);
              form.setPar3(c3);
              form.setPar4(c4);
            }
          }
        }
      }
      else {
        double c1 = 2.0D * z / Math.log(lv / pv);
        double c2 = c1 * Math.log((lv / pv - 1.0D) / (p * Math.sqrt(lv / pv)));
        double c4 = (q[1] + q[2]) / 2.0D - pv * (lv / pv + 1.0D) / (lv / pv - 1.0D) / 2.0D;
        if ((!Double.isNaN(c1)) && (!Double.isNaN(c2)) && (!Double.isNaN(c4)))
        {
          if (data_px[0] > c4) {
            for (int j = 0; j < n; j++) {
              data_ls[j] = (c2 + c1 * Math.log(data_px[j] - c4));
            }
            ADTestForm adform = NormalJudgeUtil.AnDartest(data_ls);
            if ((adform != null) && (adform.getIsNormal()) && (p < adform.getP()) && 
              (adform.getP() > 0.1D)) {
              p = adform.getP();
              form.setP(adform.getP());
              form.setZ(z);
              form.setType(2);
              form.setPar1(c1);
              form.setPar2(c2);
              form.setPar4(c4);
            }
          }
        }
      }
      z += 0.01D;
    }
    return form;
  }

  public static double[] transferDataByJohnsonTransfer(double[] allData)
  {
    if ((allData != null) && (allData.length > 1)) {
      JohnsonTransferForm form = johnsonTransfer(allData);
      return transferDataByJohnsonTransfer(allData, form);
    }
    return null;
  }

  public static double[] transferDataByJohnsonTransfer(double[] allData, JohnsonTransferForm form)
  {
    if ((allData != null) && (allData.length > 1)) {
      if (form != null) {
        double[] data_ls = new double[allData.length];
        switch (form.getType()) {
        case 1:
          for (int i = 0; i < allData.length; i++) {
            data_ls[i] = (form.getPar2() + form.getPar1() * MathUtil.arshx((allData[i] - form.getPar4()) / form.getPar3()));
          }
          return data_ls;
        case 2:
          for (int i = 0; i < allData.length; i++) {
            data_ls[i] = (form.getPar2() + form.getPar1() * Math.log(allData[i] - form.getPar4()));
          }
          return data_ls;
        case 3:
          for (int i = 0; i < allData.length; i++) {
            data_ls[i] = 
              (form.getPar2() + form.getPar1() * 
              Math.log((allData[i] - form.getPar4()) / (
              form.getPar3() + form.getPar4() - allData[i])));
          }
          return data_ls;
        }
      } else {
        return null;
      }
    }
    return null;
  }
}