package com.ambition.spc.abnormal.util;

import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.entity.JLSampleData;
import com.ambition.spc.abnormal.util.JLJudgeAbnormity;
import com.ambition.spc.entity.Abnormity;
import java.util.ArrayList;

/**
 * JLJudgeAbnormalAPI.java
 * 
 * @authorBy YUKE
 * 
 */
public class JLJudgeAbnormalAPI {
	@SuppressWarnings("rawtypes")
	public static Abnormity[][] JudgeAbnormal(JLOriginalData originalData, Abnormity[] abnormity) {
		Abnormity[][] retAbnormity = new Abnormity[2][];
		ArrayList dataList = originalData.getDataList();
		int groupTotal = dataList.size();

		double[] arrayx = new double[groupTotal];
		double[] arrayy;
		if (originalData.getChartType() == 4) {
			arrayy = new double[groupTotal - 1];
		} else {
			arrayy = new double[groupTotal];
		}

		switch (originalData.getChartType()) {
		case 1:
			for (int i = 0; i < groupTotal; i++) {
				JLSampleData sampleData = (JLSampleData) dataList.get(i);
				arrayx[i] = sampleData.getAverage();
				arrayy[i] = sampleData.getR();
			}

			Abnormity[] abLS = JLJudgeAbnormity.getAbnormity(abnormity, 1);
			if ((abLS != null) && (abLS.length > 0)) {
				retAbnormity[0] = JLJudgeAbnormity.StatisticAbnormal(arrayx,
						abLS, originalData.getXUCL(), originalData.getXLCL(),
						originalData.getXCL(), originalData.getTu(),
						originalData.getTl(), 1, null);
			}
			abLS = JLJudgeAbnormity.getAbnormity(abnormity, 2);
			if ((abLS == null) || (abLS.length <= 0))
				break;
			retAbnormity[1] = JLJudgeAbnormity.StatisticAbnormal(arrayy, abLS,
					originalData.getRUCL(), originalData.getRLCL(),
					originalData.getRCL(), originalData.getTu(),
					originalData.getTl(), 2, null);

			break;
		case 2:
			for (int i = 0; i < groupTotal; i++) {
				JLSampleData sampleData = (JLSampleData) dataList.get(i);
				arrayx[i] = sampleData.getAverage();
				arrayy[i] = sampleData.getS();
			}

			Abnormity[] abLS2 = JLJudgeAbnormity.getAbnormity(abnormity, 1);
			if ((abLS2 != null) && (abLS2.length > 0)) {
				retAbnormity[0] = JLJudgeAbnormity.StatisticAbnormal(arrayx,
						abLS2, originalData.getXUCL(), originalData.getXLCL(),
						originalData.getXCL(), originalData.getTu(),
						originalData.getTl(), 1, null);
			}
			abLS2 = JLJudgeAbnormity.getAbnormity(abnormity, 2);
			if ((abLS2 == null) || (abLS2.length <= 0))
				break;
			retAbnormity[1] = JLJudgeAbnormity.StatisticAbnormal(arrayy, abLS2,
					originalData.getSUCL(), originalData.getSLCL(),
					originalData.getSCL(), originalData.getTu(),
					originalData.getTl(), 2, null);

			break;
		case 3:
			for (int i = 0; i < groupTotal; i++) {
				JLSampleData sampleData = (JLSampleData) dataList.get(i);
				arrayx[i] = sampleData.getMedian();
				arrayy[i] = sampleData.getR();
			}

			Abnormity[] abLS3 = JLJudgeAbnormity.getAbnormity(abnormity, 1);
			if ((abLS3 != null) && (abLS3.length > 0)) {
				retAbnormity[0] = JLJudgeAbnormity.StatisticAbnormal(arrayx,
						abLS3, originalData.getXUCL(), originalData.getXLCL(),
						originalData.getXCL(), originalData.getTu(),
						originalData.getTl(), 1, null);
			}
			abLS3 = JLJudgeAbnormity.getAbnormity(abnormity, 2);
			if ((abLS3 == null) || (abLS3.length <= 0))
				break;
			retAbnormity[1] = JLJudgeAbnormity.StatisticAbnormal(arrayy, abLS3,
					originalData.getRUCL(), originalData.getRLCL(),
					originalData.getRCL(), originalData.getTu(),
					originalData.getTl(), 2, null);

			break;
		case 4:
			for (int i = 0; i < groupTotal; i++) {
				JLSampleData sampleData = (JLSampleData) dataList.get(i);
				arrayx[i] = sampleData.getAverage();

				if (i > 0) {
					arrayy[(i - 1)] = sampleData.getR();
				}
			}

			Abnormity[] abLS4 = JLJudgeAbnormity.getAbnormity(abnormity, 1);
			if ((abLS4 != null) && (abLS4.length > 0)) {
				retAbnormity[0] = JLJudgeAbnormity.StatisticAbnormal(arrayx,
						abLS4, originalData.getXUCL(), originalData.getXLCL(),
						originalData.getXCL(), originalData.getTu(),
						originalData.getTl(), 1, null);
			}
			abLS4 = JLJudgeAbnormity.getAbnormity(abnormity, 2);
			if ((abLS4 == null) || (abLS4.length <= 0))
				break;
			retAbnormity[1] = JLJudgeAbnormity.StatisticAbnormal(arrayy, abLS4,
					originalData.getRUCL(), originalData.getRLCL(),
					originalData.getRCL(), originalData.getTu(),
					originalData.getTl(), 2, null);
		}

		return retAbnormity;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList JudgeAbnormalRealTime(JLOriginalData originalData,
			Abnormity[] abnormity) {
		ArrayList retAbnormalList = new ArrayList();

		ArrayList dataList = originalData.getDataList();
		int groupTotal = dataList.size();

		double[] arrayx = new double[groupTotal];
		boolean[] judgeState = new boolean[groupTotal];
		String[] dateTime = new String[groupTotal];

		switch (originalData.getChartType()) {
		case 1:
			double[] arrayy = new double[groupTotal];
			for (int i = 0; i < groupTotal; i++) {
				JLSampleData sampleData = (JLSampleData) dataList.get(i);
				arrayx[i] = sampleData.getAverage();
				arrayy[i] = sampleData.getR();
				judgeState[i] = sampleData.getJudgeState();
				dateTime[i] = sampleData.getSamplingTime();
			}

			Abnormity[] abLS = JLJudgeAbnormity.getAbnormity(abnormity, 1);
			if ((abLS != null) && (abLS.length > 0)) {
				retAbnormalList = JLJudgeAbnormity.JudgeAbnormalRealtime(
						arrayx, abLS, dateTime, judgeState,
						originalData.getXUCL(), originalData.getXLCL(),
						originalData.getXCL(), originalData.getTu(),
						originalData.getTl(), originalData.getUpTitle(), 1,
						null);
			}
			abLS = JLJudgeAbnormity.getAbnormity(abnormity, 2);
			if ((abLS == null) || (abLS.length <= 0))
				break;
			ArrayList retAbnormalList1 = JLJudgeAbnormity
					.JudgeAbnormalRealtime(arrayy, abLS, dateTime, judgeState,
							originalData.getRUCL(), originalData.getRLCL(),
							originalData.getRCL(), originalData.getTu(),
							originalData.getTl(), originalData.getLowTitle(),
							2, null);

			retAbnormalList.addAll(retAbnormalList1);

			break;
		case 2:
			double[] arrayy2 = new double[groupTotal];
			for (int i = 0; i < groupTotal; i++) {
				JLSampleData sampleData = (JLSampleData) dataList.get(i);
				arrayx[i] = sampleData.getAverage();
				arrayy2[i] = sampleData.getS();
				judgeState[i] = sampleData.getJudgeState();
				dateTime[i] = sampleData.getSamplingTime();
			}

			Abnormity[] abLS2 = JLJudgeAbnormity.getAbnormity(abnormity, 1);
			if ((abLS2 != null) && (abLS2.length > 0)) {
				retAbnormalList = JLJudgeAbnormity.JudgeAbnormalRealtime(
						arrayx, abLS2, dateTime, judgeState,
						originalData.getXUCL(), originalData.getXLCL(),
						originalData.getXCL(), originalData.getTu(),
						originalData.getTl(), originalData.getUpTitle(), 1,
						null);
			}
			abLS2 = JLJudgeAbnormity.getAbnormity(abnormity, 2);
			if ((abLS2 == null) || (abLS2.length <= 0))
				break;
			ArrayList retAbnormalList2 = JLJudgeAbnormity
					.JudgeAbnormalRealtime(arrayy2, abLS2, dateTime, judgeState,
							originalData.getSUCL(), originalData.getSLCL(),
							originalData.getSCL(), originalData.getTu(),
							originalData.getTl(), originalData.getLowTitle(),
							2, null);
			retAbnormalList.addAll(retAbnormalList2);

			break;
		case 3:
			double[] arrayy3 = new double[groupTotal];
			for (int i = 0; i < groupTotal; i++) {
				JLSampleData sampleData = (JLSampleData) dataList.get(i);
				arrayx[i] = sampleData.getMedian();
				arrayy3[i] = sampleData.getR();
				judgeState[i] = sampleData.getJudgeState();
				dateTime[i] = sampleData.getSamplingTime();
			}

			Abnormity[] abLS3 = JLJudgeAbnormity.getAbnormity(abnormity, 1);
			if ((abLS3 != null) && (abLS3.length > 0)) {
				retAbnormalList = JLJudgeAbnormity.JudgeAbnormalRealtime(
						arrayx, abLS3, dateTime, judgeState,
						originalData.getXUCL(), originalData.getXLCL(),
						originalData.getXCL(), originalData.getTu(),
						originalData.getTl(), originalData.getUpTitle(), 1,
						null);
			}
			abLS3 = JLJudgeAbnormity.getAbnormity(abnormity, 2);
			if ((abLS3 == null) || (abLS3.length <= 0))
				break;
			ArrayList retAbnormalList3 = JLJudgeAbnormity
					.JudgeAbnormalRealtime(arrayy3, abLS3, dateTime, judgeState,
							originalData.getRUCL(), originalData.getRLCL(),
							originalData.getRCL(), originalData.getTu(),
							originalData.getTl(), originalData.getLowTitle(),
							2, null);
			retAbnormalList.addAll(retAbnormalList3);

			break;
		case 4:
			double[] arrayy4 = new double[groupTotal - 1];
			for (int i = 0; i < groupTotal; i++) {
				JLSampleData sampleData = (JLSampleData) dataList.get(i);
				arrayx[i] = sampleData.getAverage();

				if (i > 0) {
					arrayy4[(i - 1)] = sampleData.getR();
				}
				judgeState[i] = sampleData.getJudgeState();
				dateTime[i] = sampleData.getSamplingTime();
			}

			Abnormity[] abLS4 = JLJudgeAbnormity.getAbnormity(abnormity, 1);
			if ((abLS4 != null) && (abLS4.length > 0)) {
				retAbnormalList = JLJudgeAbnormity.JudgeAbnormalRealtime(
						arrayx, abLS4, dateTime, judgeState,
						originalData.getXUCL(), originalData.getXLCL(),
						originalData.getXCL(), originalData.getTu(),
						originalData.getTl(), originalData.getUpTitle(), 1,
						null);
			}
			abLS4 = JLJudgeAbnormity.getAbnormity(abnormity, 2);
			if ((abLS4 == null) || (abLS4.length <= 0))
				break;
			for (int i = 0; i < groupTotal - 1; i++) {
				judgeState[i] = judgeState[(i + 1)];
			}
			ArrayList retAbnormalList4 = JLJudgeAbnormity
					.JudgeAbnormalRealtime(arrayy4, abLS4, dateTime, judgeState,
							originalData.getRUCL(), originalData.getRLCL(),
							originalData.getRCL(), originalData.getTu(),
							originalData.getTl(), originalData.getLowTitle(),
							2, null);
			retAbnormalList.addAll(retAbnormalList4);
		}

		return retAbnormalList;
	}
}
