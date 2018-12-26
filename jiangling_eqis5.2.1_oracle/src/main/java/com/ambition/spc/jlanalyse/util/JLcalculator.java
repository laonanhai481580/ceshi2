package com.ambition.spc.jlanalyse.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.ambition.spc.entity.CPKMoudle;
import com.ambition.spc.entity.JohnsonTransferForm;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.entity.JLResult;
import com.ambition.spc.jlanalyse.entity.JLSampleData;
import com.ambition.spc.util.CPKCalculator;
import com.ambition.spc.util.Calculator;
import com.ambition.spc.util.ConstantsOfSPC;
import com.ambition.spc.util.ControlChartCoefficient;
import com.ambition.spc.util.JohnsonTransfer;
import com.ambition.spc.util.NormalJudgeUtil;

/**    
 * JLSampleData.java(计量核心算法)
 * @authorBy wanglf
 *
 */
public class JLcalculator
{
  private JLResult jLResult;

  @SuppressWarnings("rawtypes")
public void calculate(JLOriginalData originalData)
  {
    this.jLResult = new JLResult();

    ArrayList dataList = originalData.getDataList();

    int chartType = originalData.getChartType();

    int sampleQuantity = originalData.getSampleQuantity();

    int n = dataList.size();
    this.jLResult.setN(n);

    double[] maxData = new double[n];
    double[] minData = new double[n];
    double[] averageData = new double[n];
    double[] sData = new double[n];
    double[] rData = new double[n];
    double[] mData = new double[n];
    double[] allData = new double[n * sampleQuantity];

    Iterator dataIte = dataList.iterator();
    int i = 0;
    while (dataIte.hasNext()) {
      JLSampleData sampleData = (JLSampleData)dataIte.next();
      maxData[i] = sampleData.getMax();
      minData[i] = sampleData.getMin();
      averageData[i] = sampleData.getAverage();
      sData[i] = sampleData.getS();
      rData[i] = sampleData.getR();
      mData[i] = sampleData.getMedian();
      double[] data = sampleData.getData();
      for (int j = 0; j < data.length; j++) {
        allData[(i * data.length + j)] = data[j];
      }
      i++;
    }

    double max = Calculator.max(maxData);
    double min = Calculator.min(minData);
    double S=0.0;
    //单点移动极差计算PPK时的标准差,与普通的一致 2017-04-19 赵骏
//    if(chartType==4){
//    	S = Calculator.calculateOneS(allData,originalData.getAlldataaverage());
//    }else{
//    	S = Calculator.calculateS(allData);
//    }
    S = Calculator.calculateS(allData);

    double averageX = Calculator.average(averageData);
    double averageS = Calculator.average(sData);

    if (chartType == 4&& n!=0 ) {
      double[] rTemp = new double[n - 1];
      for (int j = 0; j < n - 1; j++) {
        rTemp[j] = rData[(j + 1)];
      }
      rData = rTemp;
    }
    double averageR = Calculator.average(rData);
    double averageMedian = Calculator.average(mData);

    this.jLResult.setMax(max);
    this.jLResult.setMin(min);
    this.jLResult.setAverage(Calculator.average(allData));
    this.jLResult.setR(max - min);
    this.jLResult.setS(S);

    ControlChartCoefficient coefficient = new ControlChartCoefficient(sampleQuantity);
    double A1 = coefficient.getA1();
    double A2 = coefficient.getA2();
    double B3 = coefficient.getB3();
    double B4 = coefficient.getB4();
    double D2 = coefficient.getD2();
    double D3 = coefficient.getD3();
    double D4 = coefficient.getD4();
    double M3A2 = coefficient.getM3A2();
    double C4 = coefficient.getC4();

    Integer controlState = originalData.getControlState();

    if ((controlState != null) && 
      (controlState.equals(ConstantsOfSPC.SPC_SAMPLING_STATE_CONTROL_I)))
    {
      this.jLResult.setXUCL(originalData.getXUCL());
      this.jLResult.setXLCL(originalData.getXLCL());
      this.jLResult.setXCL(originalData.getXCL());

      this.jLResult.setRUCL(originalData.getRUCL());
      this.jLResult.setRLCL(originalData.getRLCL());
      this.jLResult.setRCL(originalData.getRCL());

      this.jLResult.setSUCL(originalData.getSUCL());
      this.jLResult.setSLCL(originalData.getSLCL());
      this.jLResult.setSCL(originalData.getSCL());
    }
    else
    {
      switch (chartType) {
      case 1:
        this.jLResult.setXCL(averageX);
        this.jLResult.setXUCL(averageX + A2 * averageR);
        this.jLResult.setXLCL(averageX - A2 * averageR);
        this.jLResult.setRCL(averageR);
        this.jLResult.setRUCL(D4 * averageR);
        this.jLResult.setRLCL(D3 * averageR);

        break;
      case 2:
        if (originalData.getExpentType() == 3) {
          double sw = 0.0D; double ssw = 0.0D;
          for (i = 0; i < n; i++) {
            sw = Math.pow(averageData[i] - averageX, 2.0D) + sw;
            ssw = Math.pow(sData[i] - averageS, 2.0D) + ssw;
          }
          sw = Math.sqrt(sw / (n - 1));
          ssw = Math.sqrt(ssw / (n - 1));
          sw = Math.sqrt(sw * sw + averageS * averageS / sampleQuantity);

          this.jLResult.setXCL(averageX);
          this.jLResult.setXUCL(averageX + 3.0D * sw);
          this.jLResult.setXLCL(averageX - 3.0D * sw);
          this.jLResult.setSCL(averageS);
          this.jLResult.setSUCL(averageS + 3.0D * ssw);
          ssw = averageS - 3.0D * ssw;
          this.jLResult.setSLCL(ssw < 0.0D ? 0.0D : ssw);
        }
        else {
          this.jLResult.setXCL(averageX);
          this.jLResult.setXUCL(averageX + A1 * averageS);
          this.jLResult.setXLCL(averageX - A1 * averageS);
          this.jLResult.setSCL(averageS);
          this.jLResult.setSUCL(B4 * averageS);
          this.jLResult.setSLCL(B3 * averageS);
        }

        break;
      case 3:
        this.jLResult.setXCL(averageMedian);
        this.jLResult.setXUCL(averageMedian + M3A2 * averageR);
        this.jLResult.setXLCL(averageMedian - M3A2 * averageR);
        this.jLResult.setRCL(averageR);
        this.jLResult.setRUCL(D4 * averageR);
        this.jLResult.setRLCL(D3 * averageR);

        break;
      case 4:
        this.jLResult.setXCL(averageX);
        this.jLResult.setXUCL(averageX + 2.659D * averageR);
        this.jLResult.setXLCL(averageX - 2.659D * averageR);
        this.jLResult.setRCL(averageR);
        this.jLResult.setRUCL(3.267D * averageR);
      }

    }

    if (originalData.getExpentType() == 3) {
      JohnsonTransferForm form = JohnsonTransfer.johnsonTransfer(allData);
      double[] data_ls = allData;
      if (form != null) {
        data_ls = JohnsonTransfer.transferDataByJohnsonTransfer(allData, form);
      }
      CPKMoudle CPKMoudle = CPKCalculator.calculateByJohnsonTransfer(form, data_ls, 
        originalData.getTu(), originalData.getTl(), originalData.getM());
      this.jLResult.setCpkMoudle(CPKMoudle);

      double[] skAndKu = NormalJudgeUtil.calSkewnessAndKurtosis(allData, 
        Calculator.average(data_ls), Calculator.calculateS(data_ls));
      if (skAndKu != null) {
        this.jLResult.setSkewness(skAndKu[0]);
        this.jLResult.setKurtosis(skAndKu[1]);
      }
    } else {
      double st = S;
      if (chartType == 2) {
        st = averageS / C4;
      }
      else
      {
        st = averageR / D2;
      }

      double[] skAndKu = NormalJudgeUtil.calSkewnessAndKurtosis(allData, 
        this.jLResult.getAverage(), S);
      if (skAndKu != null) {
        this.jLResult.setSkewness(skAndKu[0]);
        this.jLResult.setKurtosis(skAndKu[1]);
      }
      CPKMoudle CPKMoudle = 
        CPKCalculator.calculate(originalData.getTu(), originalData.getTl(), 
        originalData.getM(), averageX, st, S);
      this.jLResult.setCpkMoudle(CPKMoudle);
    }
  }

  public JLResult getjLResult()
  {
    return this.jLResult;
  }
}