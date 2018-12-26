package com.ambition.spc.jsanalyse.util;

import java.util.ArrayList;

import com.ambition.spc.entity.AbnormalForRealTime;
import com.ambition.spc.entity.Abnormity;
import com.ambition.spc.util.JudgeAbnormalUtil;

/**    
 * JSJudgeAbnormity.java
 * @authorBy wanglf
 *
 */
public class JSJudgeAbnormity
{
  @SuppressWarnings("rawtypes")
public static boolean[] JudgeAbnormal(double[] data, Abnormity[] abnormal, double ucl, double lcl, double cl)
  {
    int dataLength = data.length;
    boolean[] abnormalPosition = new boolean[dataLength];
    for (int i = 0; i < dataLength; i++) {
      abnormalPosition[i] = false;
    }

    ArrayList retList = JudgeAbnormal(data, abnormal, null, null, 
      ucl, lcl, cl, "");
    for (int i = 0; i < retList.size(); i++) {
      AbnormalForRealTime AbnormalForRealTime = (AbnormalForRealTime)retList
        .get(i);
      int j = AbnormalForRealTime.getStartPosition() - 1;
      for (; j < AbnormalForRealTime.getEndPosition(); j++) {
        abnormalPosition[j] = true;
      }
    }
    return abnormalPosition;
  }

  @SuppressWarnings("rawtypes")
public static boolean[] JudgeAbnormal(double[] data, Abnormity[] abnormal, double[] ucl, double[] lcl, double cl)
  {
    int dataLength = data.length;
    boolean[] abnormalPosition = new boolean[dataLength];
    for (int i = 0; i < dataLength; i++) {
      abnormalPosition[i] = false;
    }

    ArrayList retList = JudgeAbnormal(data, abnormal, null, null, 
      ucl, lcl, cl, "");
    for (int i = 0; i < retList.size(); i++) {
      AbnormalForRealTime AbnormalForRealTime = (AbnormalForRealTime)retList
        .get(i);
      int j = AbnormalForRealTime.getStartPosition() - 1;
      for (; j < AbnormalForRealTime.getEndPosition(); j++) {
        abnormalPosition[j] = true;
      }
    }
    return abnormalPosition;
  }

  @SuppressWarnings("rawtypes")
public static Abnormity[] StatisticAbnormal(double[] data, Abnormity[] abnormal, double ucl, double lcl, double cl)
  {
    int[] total = new int[14];

    ArrayList retList = JudgeAbnormal(data, abnormal, null, null, 
      ucl, lcl, cl, "");
    for (int i = 0; i < retList.size(); i++) {
      AbnormalForRealTime AbnormalForRealTime = (AbnormalForRealTime)retList
        .get(i);
      total[(AbnormalForRealTime.getAbnormity() - 1)] += 1;
    }

    for (int i = 0; i < abnormal.length; i++) {
      abnormal[i].setAbnormityNum(total[(abnormal[i].getAbnormity() - 1)]);
    }

    return abnormal;
  }

  @SuppressWarnings("rawtypes")
public static Abnormity[] StatisticAbnormal(double[] data, Abnormity[] abnormal, double[] ucl, double[] lcl, double cl)
  {
    int[] total = new int[14];

    ArrayList retList = JudgeAbnormal(data, abnormal, null, null, 
      ucl, lcl, cl, "");
    for (int i = 0; i < retList.size(); i++) {
      AbnormalForRealTime AbnormalForRealTime = (AbnormalForRealTime)retList
        .get(i);
      total[(AbnormalForRealTime.getAbnormity() - 1)] += 1;
    }

    for (int i = 0; i < abnormal.length; i++) {
      abnormal[i].setAbnormityNum(total[(abnormal[i].getAbnormity() - 1)]);
    }

    return abnormal;
  }

  @SuppressWarnings("rawtypes")
public static ArrayList JudgeAbnormalRealtime(double[] data, Abnormity[] abnormal, String[] dateTime, boolean[] dataJudgeState, double ucl, double lcl, double cl, String title)
  {
    return JudgeAbnormal(data, abnormal, dateTime, 
      dataJudgeState, ucl, lcl, cl, title);
  }

  @SuppressWarnings("rawtypes")
public static ArrayList JudgeAbnormalRealtime(double[] data, Abnormity[] abnormal, String[] dateTime, boolean[] dataJudgeState, double[] ucl, double[] lcl, double cl, String title)
  {
    return JudgeAbnormal(data, abnormal, dateTime, dataJudgeState, 
      ucl, lcl, cl, title);
  }

  @SuppressWarnings("rawtypes")
private static ArrayList JudgeAbnormal(double[] data, Abnormity[] abnormal, String[] dateTime, boolean[] dataJudgeState, double ucl, double lcl, double cl, String title)
  {
    ArrayList retList = new ArrayList();
    for (int i = 0; (abnormal != null) && (i < abnormal.length); i++)
      switch (abnormal[i].getAbnormity()) {
      case 1:
        retList = JudgeAbnormalUtil.BaseAbnormal1(data, abnormal[i], 
          dateTime, dataJudgeState, title, retList, 3, null);

        break;
      case 2:
        retList = JudgeAbnormalUtil.BaseAbnormal2(data, abnormal[i], 
          dateTime, dataJudgeState, title, retList, 3, null);

        break;
      case 3:
        retList = JudgeAbnormalUtil.BaseAbnormal3(data, abnormal[i], 
          dateTime, dataJudgeState, title, retList, 3, null);

        break;
      case 4:
        retList = JudgeAbnormalUtil.BaseAbnormal4(data, abnormal[i], 
          dateTime, dataJudgeState, title, retList, 3, null);

        break;
      case 8:
        retList = JudgeAbnormalUtil.BaseAbnormal8(data, abnormal[i], 
          dateTime, dataJudgeState, ucl, lcl, title, retList, 3, null);

        break;
      case 12:
        retList = JudgeAbnormalUtil.BaseAbnormal12(data, abnormal[i], 
          dateTime, dataJudgeState, cl, title, retList, 3, null);
      case 5:
      case 6:
      case 7:
      case 9:
      case 10:
      case 11: }  return retList;
  }

  @SuppressWarnings("rawtypes")
private static ArrayList JudgeAbnormal(double[] data, Abnormity[] abnormal, String[] dateTime, boolean[] dataJudgeState, double[] ucl, double[] lcl, double cl, String title)
  {
    ArrayList retList = new ArrayList();
    for (int i = 0; (abnormal != null) && (i < abnormal.length); i++)
      switch (abnormal[i].getAbnormity()) {
      case 1:
        retList = JudgeAbnormalUtil.BaseAbnormal1(data, abnormal[i], 
          dateTime, dataJudgeState, title, retList, 3, null);

        break;
      case 2:
        retList = JudgeAbnormalUtil.BaseAbnormal2(data, abnormal[i], 
          dateTime, dataJudgeState, title, retList, 3, null);

        break;
      case 3:
        retList = JudgeAbnormalUtil.BaseAbnormal3(data, abnormal[i], 
          dateTime, dataJudgeState, title, retList, 3, null);

        break;
      case 4:
        retList = JudgeAbnormalUtil.BaseAbnormal4(data, abnormal[i], 
          dateTime, dataJudgeState, title, retList, 3, null);

        break;
      case 8:
        retList = JudgeAbnormalUtil.BaseAbnormal8(data, abnormal[i], 
          dateTime, dataJudgeState, ucl, lcl, title, retList, 3, null);

        break;
      case 12:
        retList = JudgeAbnormalUtil.BaseAbnormal12(data, abnormal[i], 
          dateTime, dataJudgeState, cl, title, retList, 3, null);
      case 5:
      case 6:
      case 7:
      case 9:
      case 10:
      case 11: }  return retList;
  }
}