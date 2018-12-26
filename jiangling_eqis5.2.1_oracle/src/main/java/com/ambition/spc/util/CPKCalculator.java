package com.ambition.spc.util;

import com.ambition.spc.entity.CPKMoudle;
import com.ambition.spc.entity.JohnsonTransferForm;

/**    
 * CPKCalculator.java(cpk核心算法)
 * @authorBy wanglf
 *
 */

public class CPKCalculator
{
  public static CPKMoudle calculate(double[] allData, Double tu, Double tl, Double tm)
  {
    double longTerm = Calculator.calculateS(allData);
    double averageX = Calculator.average(allData);

    return calculate(tu, tl, tm, averageX, longTerm, longTerm);
  }

  public static CPKMoudle calculate(Double tu, Double tl, Double tm, double averageX, double shortTerm, double longTerm)
  {
    CPKMoudle CPKMoudle = new CPKMoudle();
    if ((longTerm == 0.0D) || ((tu == null) && (tl == null))) return CPKMoudle;

    if (shortTerm == 0.0D) shortTerm = longTerm;

    if ((tu != null) && (tl != null)) {
      double t = tu.doubleValue() - tl.doubleValue();
      double M = (tu.doubleValue() + tl.doubleValue()) / 2.0D;
      double mTemp = 2.0D * Math.abs(M - averageX);

      CPKMoudle.setCpu((tu.doubleValue() - averageX) / (3.0D * shortTerm));
      CPKMoudle.setPpu((tu.doubleValue() - averageX) / (3.0D * longTerm));
      CPKMoudle.setZu_cap(3.0D * CPKMoudle.getCpu());
      CPKMoudle.setZu_pref(3.0D * CPKMoudle.getPpu());
      CPKMoudle.setFpu_cap(1.0D - NormalSchool.zt(3.0D * CPKMoudle.getCpu()));
      CPKMoudle.setFpu_pref(1.0D - NormalSchool.zt(3.0D * CPKMoudle.getPpu()));

      CPKMoudle.setCpl((averageX - tl.doubleValue()) / (3.0D * shortTerm));
      CPKMoudle.setPpl((averageX - tl.doubleValue()) / (3.0D * longTerm));
      CPKMoudle.setZl_cap(3.0D * CPKMoudle.getCpl());
      CPKMoudle.setZl_pref(3.0D * CPKMoudle.getPpl());
      CPKMoudle.setFpl_cap(1.0D - NormalSchool.zt(3.0D * CPKMoudle.getCpl()));
      CPKMoudle.setFpl_pref(1.0D - NormalSchool.zt(3.0D * CPKMoudle.getPpl()));

      CPKMoudle.setCpk((t - mTemp) / (6.0D * shortTerm));
      CPKMoudle.setPpk((t - mTemp) / (6.0D * longTerm));
      CPKMoudle.setP(2.0D - (NormalSchool.zt(3.0D * CPKMoudle.getCpk()) + 
        NormalSchool.zt(3.0D * CPKMoudle.getCpk() + 
        mTemp / shortTerm)));

      if (t > 0.0D) {
        CPKMoudle.setK(2.0D * Math.abs(M - averageX) / t);
        CPKMoudle.setCp(t / (6.0D * shortTerm));
        CPKMoudle.setCr(6.0D * shortTerm / t);
        CPKMoudle.setPp(t / (6.0D * longTerm));
        CPKMoudle.setPr(6.0D * longTerm / t);
      }

      if ((tm != null) && (t > 0.0D)) {
        CPKMoudle.setCpm(t / 6.0D / Math.sqrt(Math.pow(shortTerm, 2.0D) + Math.pow(
          averageX - tm.doubleValue(), 2.0D)));
        CPKMoudle.setPpm(t / 6.0D / Math.sqrt(Math.pow(longTerm, 2.0D) + Math.pow(
          averageX - tm.doubleValue(), 2.0D)));
      }

      if (CPKMoudle.getFpu_cap() + CPKMoudle.getFpl_cap() < 1.0D)
        CPKMoudle.setFp_cap(CPKMoudle.getFpu_cap() + CPKMoudle.getFpl_cap());
      if (CPKMoudle.getFpu_pref() + CPKMoudle.getFpl_pref() < 1.0D)
        CPKMoudle.setFp_pref(CPKMoudle.getFpu_pref() + CPKMoudle.getFpl_pref());
    }
    else if (tu != null)
    {
      CPKMoudle.setCpu((tu.doubleValue() - averageX) / (3.0D * shortTerm));
      CPKMoudle.setCpk(CPKMoudle.getCpu());
      CPKMoudle.setP(1.0D - NormalSchool.zt(3.0D * CPKMoudle.getCpu()));
      CPKMoudle.setPpu((tu.doubleValue() - averageX) / (3.0D * longTerm));
      CPKMoudle.setPpk(CPKMoudle.getPpu());

      CPKMoudle.setZu_cap(3.0D * CPKMoudle.getCpu());
      CPKMoudle.setZu_pref(3.0D * CPKMoudle.getPpu());

      CPKMoudle.setFpu_cap(1.0D - NormalSchool.zt(3.0D * CPKMoudle.getCpu()));
      CPKMoudle.setFp_cap(CPKMoudle.getFpu_cap());
      CPKMoudle.setFpu_pref(1.0D - NormalSchool.zt(3.0D * CPKMoudle.getPpu()));
      CPKMoudle.setFp_pref(CPKMoudle.getFpu_pref());
    }
    else if (tl != null)
    {
      CPKMoudle.setCpl((averageX - tl.doubleValue()) / (3.0D * shortTerm));
      CPKMoudle.setCpk(CPKMoudle.getCpl());
      CPKMoudle.setP(1.0D - NormalSchool.zt(3.0D * CPKMoudle.getCpl()));
      CPKMoudle.setPpl((averageX - tl.doubleValue()) / (3.0D * longTerm));
      CPKMoudle.setPpk(CPKMoudle.getPpl());

      CPKMoudle.setZl_cap(3.0D * CPKMoudle.getCpl());
      CPKMoudle.setZl_pref(3.0D * CPKMoudle.getPpl());

      CPKMoudle.setFpl_cap(1.0D - NormalSchool.zt(3.0D * CPKMoudle.getCpl()));
      CPKMoudle.setFp_cap(CPKMoudle.getFpl_cap());
      CPKMoudle.setFpl_pref(1.0D - NormalSchool.zt(3.0D * CPKMoudle.getPpl()));
      CPKMoudle.setFp_pref(CPKMoudle.getFpl_pref());
    }

    CPKMoudle.setSigma(shortTerm);

    if (CPKMoudle.getCpk() > 1.67D) {
      CPKMoudle.setCpGrade(0);
      CPKMoudle.setCpGradeCN("过程能力非常充分:为提高产品质量，对关键或主要项目可缩小公差范围；或为提高效率、降低成本而放宽波动幅度，降低设备精度等级；或将精度要求特别高的零件调至该工序进行加工等");
    }
    else if ((CPKMoudle.getCpk() > 1.33D) && (CPKMoudle.getCpk() <= 1.67D)) {
      CPKMoudle.setCpGrade(1);
      CPKMoudle.setCpGradeCN("过程能力充分:当不是关键或主要项目时，放宽波动幅度；降低对原材料的要求；简化质量检验，采用抽样检验或减少检验频次");
    }
    else if ((CPKMoudle.getCpk() > 1.0D) && (CPKMoudle.getCpk() <= 1.33D)) {
      CPKMoudle.setCpGrade(2);
      CPKMoudle.setCpGradeCN("过程能力尚可:必须用控制图或其他方法对过程进行控制或监督，以便及时发现异常波动；对产品按正常规定进行检验");
    }
    else if ((CPKMoudle.getCpk() >= 0.67D) && (CPKMoudle.getCpk() <= 1.0D)) {
      CPKMoudle.setCpGrade(3);
      CPKMoudle.setCpGradeCN("过程能力不充分:分析分散程度大的原因，制订措施加以改进，在不影响产品质量的情况下，放宽公差范围，加强质量检测，进行全数检验或增加检验频次");
    }
    else if (CPKMoudle.getCpk() < 0.67D) {
      CPKMoudle.setCpGrade(4);
      CPKMoudle.setCpGradeCN("过程能力不足:一般应停止继续加工，找出原因，改进工艺，提高Cpk值，否则全数检验，挑出不合格品");
    }

    return CPKMoudle;
  }

  public static CPKMoudle calculateByJohnsonTransfer(double[] allData, Double tu, Double tl, Double tm)
  {
    if ((allData != null) && (allData.length > 1)) {
      JohnsonTransferForm form = JohnsonTransfer.johnsonTransfer(allData);
      if (form != null) {
        double[] data_ls = JohnsonTransfer.transferDataByJohnsonTransfer(allData, form);
        return calculateByJohnsonTransfer(form, data_ls, tu, tl, tm);
      }
      return calculate(allData, tu, tl, tm);
    }

    return null;
  }

  public static CPKMoudle calculateByJohnsonTransfer(JohnsonTransferForm form, double[] dataHasTransfer, Double tu, Double tl, Double tm)
  {
    if ((dataHasTransfer != null) && (dataHasTransfer.length > 1)) {
      if (form != null) {
        Double tu_ls = tu;
        Double tl_ls = tl;
        Double tm_ls = tm;
        switch (form.getType()) {
        case 1:
          if (tu != null) tu_ls = new Double(form.getPar2() + form.getPar1() * MathUtil.arshx((tu.doubleValue() - form.getPar4()) / form.getPar3()));
          if (tl != null) tl_ls = new Double(form.getPar2() + form.getPar1() * MathUtil.arshx((tl.doubleValue() - form.getPar4()) / form.getPar3()));
          if (tm != null) tm_ls = new Double(form.getPar2() + form.getPar1() * MathUtil.arshx((tm.doubleValue() - form.getPar4()) / form.getPar3()));
          if ((tu_ls != null) && (Double.isNaN(tu_ls.doubleValue()))) tu_ls = null;
          if ((tl_ls != null) && (Double.isNaN(tl_ls.doubleValue()))) tl_ls = null;
          if ((tm_ls != null) && (Double.isNaN(tm_ls.doubleValue()))) tm_ls = null;
          return calculate(dataHasTransfer, tu_ls, tl_ls, tm_ls);
        case 2:
          if (tu != null) tu_ls = new Double(form.getPar2() + form.getPar1() * Math.log(tu.doubleValue() - form.getPar4()));
          if (tl != null) tl_ls = new Double(form.getPar2() + form.getPar1() * Math.log(tl.doubleValue() - form.getPar4()));
          if (tm != null) tm_ls = new Double(form.getPar2() + form.getPar1() * Math.log(tm.doubleValue() - form.getPar4()));
          if ((tu_ls != null) && (Double.isNaN(tu_ls.doubleValue()))) tu_ls = null;
          if ((tl_ls != null) && (Double.isNaN(tl_ls.doubleValue()))) tl_ls = null;
          if ((tm_ls != null) && (Double.isNaN(tm_ls.doubleValue()))) tm_ls = null;
          return calculate(dataHasTransfer, tu_ls, tl_ls, tm_ls);
        case 3:
          if (tu != null) tu_ls = new Double(form.getPar2() + form.getPar1() * Math.log((tu.doubleValue() - form.getPar4()) / (form.getPar3() + form.getPar4() - tu.doubleValue())));
          if (tl != null) tl_ls = new Double(form.getPar2() + form.getPar1() * Math.log((tl.doubleValue() - form.getPar4()) / (form.getPar3() + form.getPar4() - tl.doubleValue())));
          if (tm != null) tm_ls = new Double(form.getPar2() + form.getPar1() * Math.log((tm.doubleValue() - form.getPar4()) / (form.getPar3() + form.getPar4() - tm.doubleValue())));
          if ((tu_ls != null) && (Double.isNaN(tu_ls.doubleValue()))) tu_ls = null;
          if ((tl_ls != null) && (Double.isNaN(tl_ls.doubleValue()))) tl_ls = null;
          if ((tm_ls != null) && (Double.isNaN(tm_ls.doubleValue()))) tm_ls = null;
          return calculate(dataHasTransfer, tu_ls, tl_ls, tm_ls);
        }
        return calculate(dataHasTransfer, tu, tl, tm);
      }

      return calculate(dataHasTransfer, tu, tl, tm);
    }
    return null;
  }

  public static double calculatePassRateBySigma(double sigma)
  {
    return NormalSchool.zt(1.5D + sigma) - NormalSchool.zt(1.5D - sigma);
  }

  public static double calculateDPMOBySigma(double sigma)
  {
    return (1.0D - calculatePassRateBySigma(sigma)) * 1000000.0D;
  }
}