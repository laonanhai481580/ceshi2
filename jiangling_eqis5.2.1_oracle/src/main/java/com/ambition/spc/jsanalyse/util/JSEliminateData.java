package com.ambition.spc.jsanalyse.util;

import java.util.ArrayList;

import com.ambition.spc.entity.Abnormity;
import com.ambition.spc.jsanalyse.entity.JSOriginalData;
import com.ambition.spc.jsanalyse.entity.JSSampleData;

/**    
 * JSEliminateData.java
 * @authorBy wanglf
 *
 */
public class JSEliminateData
{
  @SuppressWarnings("rawtypes")
public static boolean[] EliminateData(JSOriginalData originalData, Abnormity[] abnormal)
  {
    int chartType = originalData.getChartType();

    ArrayList dataList = originalData.getDataList();

    int groupTotal = dataList.size();

    double[] dataArray = new double[groupTotal];

    boolean[] abnormalPosition = (boolean[])null;
    if ((originalData.getCaseSelect()) || 
      (chartType == 5) || 
      (chartType == 7))
    {
      for (int i = 0; i < groupTotal; i++) {
        JSSampleData sampleData = (JSSampleData)dataList.get(i);
        dataArray[i] = sampleData.getRejectNumber();
      }
      double ucl = originalData.getUCL();
      double lcl = originalData.getLCL();
      double cl = originalData.getCL();

      abnormalPosition = JSJudgeAbnormity.JudgeAbnormal(dataArray, abnormal, 
        ucl, lcl, cl);
    }
    else
    {
      double[] uclArray = new double[groupTotal];
      double[] lclArray = new double[groupTotal];

      for (int i = 0; i < groupTotal; i++) {
        JSSampleData sampleData = (JSSampleData)dataList.get(i);
        dataArray[i] = sampleData.getUnitRejectNumber();
        uclArray[i] = sampleData.getUpLine();
        lclArray[i] = sampleData.getLowLine();
      }

      double cl = originalData.getCL();

      abnormalPosition = JSJudgeAbnormity.JudgeAbnormal(dataArray, abnormal, 
        uclArray, lclArray, cl);
    }
    return abnormalPosition;
  }
}