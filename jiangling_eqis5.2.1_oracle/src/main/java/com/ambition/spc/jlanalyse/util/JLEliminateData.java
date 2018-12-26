package com.ambition.spc.jlanalyse.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.ambition.spc.entity.Abnormity;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.entity.JLSampleData;

/**    
 * JLEliminateData.java
 * @authorBy wanglf
 *
 */
public class JLEliminateData
{
  @SuppressWarnings("rawtypes")
public static boolean[] EliminateData(JLOriginalData originalData, Abnormity[] abnormal)
  {
    double xUCL = originalData.getXUCL();
    double xLCL = originalData.getXLCL();
    double xCL = originalData.getXCL();
    double rUCL = originalData.getRUCL();
    double rLCL = originalData.getRLCL();
    double rCL = originalData.getRCL();
    double sUCL = originalData.getSUCL();
    double sLCL = originalData.getSLCL();
    double sCL = originalData.getSCL();
    Double tu = originalData.getTu();
    Double tl = originalData.getTl();

    int chartType = originalData.getChartType();

    ArrayList dataList = originalData.getDataList();

    int n = dataList.size();

    double[] media = new double[n];

    double[] xData = new double[n];

    double[] sData = new double[n];

    double[] rData = new double[n];

    boolean[] abnormalPosition = (boolean[])null;

    Iterator dataIte = dataList.iterator();
    int i = 0;
    while (dataIte.hasNext()) {
      JLSampleData sampleData = (JLSampleData)dataIte.next();
      media[i] = sampleData.getMedian();
      xData[i] = sampleData.getAverage();
      sData[i] = sampleData.getS();
      rData[i] = sampleData.getR();
      i++;
    }

    switch (chartType)
    {
    case 1:
      Abnormity[] abLS = JLJudgeAbnormity.getAbnormity(abnormal, 1);
      if ((abLS != null) && (abLS.length > 0)) {
        abnormalPosition = JLJudgeAbnormity.JudgeAbnormal(xData, abLS, xUCL, 
          xLCL, xCL, tu, tl, 1, null);
      }
      abLS = JLJudgeAbnormity.getAbnormity(abnormal, 2);
      if ((abLS == null) || (abLS.length <= 0)) break;
      boolean[] abnormalPosition2 = JLJudgeAbnormity.JudgeAbnormal(rData, 
        abLS, rUCL, rLCL, rCL, tu, tl, 2, null);
      for (i = 0; i < n; i++) {
        if (abnormalPosition2[i]) {
          abnormalPosition[i] = true;
        }

      }

      break;
    case 2:
      Abnormity[] abLS2 = JLJudgeAbnormity.getAbnormity(abnormal, 1);
      if ((abLS2 != null) && (abLS2.length > 0)) {
        abnormalPosition = JLJudgeAbnormity.JudgeAbnormal(xData, abLS2, 
          xUCL, xLCL, xCL, tu, tl, 1, null);
      }
      abLS2 = JLJudgeAbnormity.getAbnormity(abnormal, 2);
      if ((abLS2 == null) || (abLS2.length <= 0)) break;
      boolean[] abnormalPosition3 = JLJudgeAbnormity.JudgeAbnormal(sData, 
    		  abLS2, sUCL, sLCL, sCL, tu, tl, 2, null);

      for (i = 0; i < n; i++) {
        if (abnormalPosition3[i]) {
          abnormalPosition[i] = true;
        }

      }

      break;
    case 3:
      Abnormity[] abLS3 = JLJudgeAbnormity.getAbnormity(abnormal, 1);
      if ((abLS3 != null) && (abLS3.length > 0)) {
        abnormalPosition = JLJudgeAbnormity.JudgeAbnormal(media, abLS3, 
          xUCL, xLCL, xCL, tu, tl, 1, null);
      }
      abLS3 = JLJudgeAbnormity.getAbnormity(abnormal, 2);
      if ((abLS3 == null) || (abLS3.length <= 0)) break;
      boolean[] abnormalPosition4 = JLJudgeAbnormity.JudgeAbnormal(rData, 
    		  abLS3, rUCL, rLCL, rCL, tu, tl, 2, null);

      for (i = 0; i < n; i++) {
        if (abnormalPosition4[i]) {
          abnormalPosition[i] = true;
        }

      }

      break;
    case 4:
      Abnormity[] abLS4 = JLJudgeAbnormity.getAbnormity(abnormal, 1);
      if ((abLS4 != null) && (abLS4.length > 0)) {
        abnormalPosition = JLJudgeAbnormity.JudgeAbnormal(xData, abLS4, 
          xUCL, xLCL, xCL, tu, tl, 1, null);
      }
      abLS4 = JLJudgeAbnormity.getAbnormity(abnormal, 2);
      if ((abLS4 == null) || (abLS4.length <= 0)) break;
      double[] rTemp = new double[n - 1];
      for (int j = 0; j < n - 1; j++) {
        rTemp[j] = rData[(j + 1)];
      }
      rData = rTemp;
      boolean[] abnormalPosition6 = JLJudgeAbnormity.JudgeAbnormal(rData, 
    		  abLS4, rUCL, rLCL, rCL, tu, tl, 2, null);

      for (i = 0; i < n - 1; i++) {
        if (abnormalPosition6[i] ) {
          abnormalPosition[(i + 1)] = true;
        }

      }

    }

    return abnormalPosition;
  }
}