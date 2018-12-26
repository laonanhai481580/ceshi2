package com.ambition.spc.abnormal.util;

import java.util.ArrayList;

import com.ambition.spc.entity.AbnormalForRealTime;
import com.ambition.spc.entity.Abnormity;

/**    
 * JudgeAbnormalUtil.java
 * @authorBy YUKE
 *
 */

public class JudgeAbnormalUtil {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList BaseAbnormal1(double[] dataJu, Abnormity abnormal, String[] dateTime, boolean[] dataJudgeState, String title, ArrayList retList, int upLow, int[] jump) {
	    float[] data = doubleToFloat(dataJu);
	    int num = 1;
	    boolean dataState = true;
	    int dataLength = data.length;
	    int param1 = abnormal.getParam1();
	    for (int j = 0; j < dataLength; j++) {
	    	if ((jump != null) && (j < jump.length) && (jump[j] > 0)) {
	    		if ((num >= param1) && (!dataState)) {
	    			retList.add(newAbnormalForm(j - num + 2, j + 1, title, abnormal,dateTime == null ? null : dateTime[j], upLow));
	    		}
	    		dataState = true;
	    		num = 1;
			    if (jump[j] == 1) {
			    	continue;
			    }
	    	}
	    	if ((j < dataLength - 1) && (data[j] < data[(j + 1)])) {
			    if ((num < param1) && ((dataJudgeState == null) || (!dataJudgeState[j] ) || (!dataJudgeState[(j + 1)]))) dataState = false;
			    num++;
	    	}else {
		        if ((num >= param1) && (!dataState)) {
					retList.add(newAbnormalForm(j - num + 2, j + 1, title,
							abnormal, dateTime == null ? null : dateTime[j],
							upLow));
		        }
		        dataState = true;
		        num = 1;
	    	}
	    }
	    return retList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList BaseAbnormal2(double[] dataJu, Abnormity abnormal, String[] dateTime, boolean[] dataJudgeState, String title, ArrayList retList, int upLow, int[] jump) {
	    float[] data = doubleToFloat(dataJu);
	    int num = 1;
	    boolean dataState = true;
	    int dataLength = data.length;
	    int param1 = abnormal.getParam1();
	    for (int j = 0; j < dataLength; j++) {
	    	if ((jump != null) && (j < jump.length) && (jump[j] > 0)) {
			    if ((num >= param1) && (!dataState)) {
					retList.add(newAbnormalForm(j - num + 2, j + 1, title,
							abnormal, dateTime == null ? null : dateTime[j],
							upLow));
			    }
			    dataState = true;
			    num = 1;
			    if (jump[j] == 1) {
			    	continue;
			    }
	    	}
	        if ((j < dataLength - 1) && (data[j] > data[(j + 1)])) {
	        	if ((num < param1) && ((dataJudgeState == null) || (!dataJudgeState[j] ) || (!dataJudgeState[(j + 1)]))) dataState = false;
	        	num++;
	        }else {
		        if ((num >= param1) && (!dataState)) {
					retList.add(newAbnormalForm(j - num + 2, j + 1, title,
							abnormal, dateTime == null ? null : dateTime[j],
							upLow));
		        }
		        dataState = true;
		        num = 1;
	        }
	    }
	    return retList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList BaseAbnormal3(double[] dataJu, Abnormity abnormal, String[] dateTime, boolean[] dataJudgeState, String title, ArrayList retList, int upLow, int[] jump) {
	    float[] data = doubleToFloat(dataJu);
	    int num = 1;
	    boolean dataState = true;
	    int dataLength = data.length;
	    int param1 = abnormal.getParam1();
	    for (int j = 0; j < dataLength; j++) {
	    	if ((jump != null) && (j < jump.length) && (jump[j] > 0)) {
		        if ((num >= param1) && (!dataState)) {
					retList.add(newAbnormalForm(j - num + 2, j + 1, title,
							abnormal, dateTime == null ? null : dateTime[j],
							upLow));
		        }
		        dataState = true;
		        num = 1;
		        if (jump[j] == 1) {
		          continue;
		        }
	    	}
	    	if ((j < dataLength - 1) && (data[j] == data[(j + 1)])) {
				if ((num < param1) && ((dataJudgeState == null) || (!dataJudgeState[j]) || (!dataJudgeState[(j + 1)])))dataState = false;
				num++;
			} else {
				if ((num >= param1) && (!dataState)) {
					retList.add(newAbnormalForm(j - num + 2, j + 1, title,
							abnormal, dateTime == null ? null : dateTime[j],
							upLow));
				}
				dataState = true;
				num = 1;
			}
		}
		return retList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList BaseAbnormal4(double[] dataJu, Abnormity abnormal,
			String[] dateTime, boolean[] dataJudgeState, String title,
			ArrayList retList, int upLow, int[] jump) {
		float[] data = doubleToFloat(dataJu);
		int num = 2;
		boolean dataState = true;
		int dataLength = data.length;
		int param1 = abnormal.getParam1();

		for (int j = 0; j < dataLength - 1; j++) {
			if ((jump != null) && (j < jump.length - 1)
					&& ((jump[j] > 0) || (jump[(j + 1)] > 0))) {
				if ((num >= param1) && (!dataState)) {
					retList.add(newAbnormalForm(j - num + 3, j + 2, title,
							abnormal, dateTime == null ? null : dateTime[(j + 1)], upLow));
				}
				dataState = true;
				num = 2;
				if ((jump[j] == 1) || (jump[(j + 1)] == 1)) {
					continue;
				}
			}
			if ((j < dataLength - 2) && (((data[j] > data[(j + 1)]) && (data[(j + 1)] < data[(j + 2)])) || ((data[j] < data[(j + 1)]) && (data[(j + 1)] > data[(j + 2)])))) {
				if ((num < param1) && ((dataJudgeState == null) || (!dataJudgeState[j]) || (!dataJudgeState[(j + 1)]) || (!dataJudgeState[(j + 2)]))) dataState = false;
				num++;
			} else {
				if ((num >= param1) && (!dataState)) {
					retList.add(newAbnormalForm(j - num + 3, j + 2, title,
							abnormal, dateTime == null ? null : dateTime[(j + 1)], upLow));
				}
				dataState = true;
				num = 2;
			}
		}
		return retList;
	}

	@SuppressWarnings("rawtypes")
	public static ArrayList BaseAbnormal5(double[] data, Abnormity abnormal,
			String[] dateTime, boolean[] dataJudgeState, double ucl,
			double lcl, double cl, String title, ArrayList retList, int upLow,
			int[] jump) {
		double yu1 = (ucl - cl) * 2.0D / 3.0D + cl;
		double yl2 = cl - (cl - lcl) * 2.0D / 3.0D;
		return BaseAbnormalABC(data, abnormal, dateTime, dataJudgeState, yu1,
				ucl, lcl, yl2, title, retList, upLow, jump);
	}

	@SuppressWarnings("rawtypes")
	public static ArrayList BaseAbnormal6(double[] data, Abnormity abnormal,
			String[] dateTime, boolean[] dataJudgeState, double ucl,
			double lcl, double cl, String title, ArrayList retList, int upLow,
			int[] jump) {
		double yu1 = (ucl - cl) / 3.0D + cl;
		double yu2 = (ucl - cl) * 2.0D / 3.0D + cl;
		double yl1 = cl - (cl - lcl) * 2.0D / 3.0D;
		double yl2 = cl - (cl - lcl) / 3.0D;
		return BaseAbnormalABC(data, abnormal, dateTime, dataJudgeState, yu1,
				yu2, yl1, yl2, title, retList, upLow, jump);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList BaseAbnormal7(double[] dataJu, Abnormity abnormal,
			String[] dateTime, boolean[] dataJudgeState, double ucl,
			double lcl, double cl, String title, ArrayList retList, int upLow,
			int[] jump) {
		float[] data = doubleToFloat(dataJu);
		double yu2 = (ucl - cl) / 3.0D + cl;
		double yl1 = cl - (cl - lcl) / 3.0D;
		int dataLength = data.length;
		int param1 = abnormal.getParam1();
		int param2 = abnormal.getParam2();

		for (int j = 0; j < dataLength - param2 + 1; j++) {
			int num = 0;
			boolean dataState = true;
			if (j > dataLength - param1)
				param1 = param2;
			for (int k = j; k <= j + param1 - 1; k++) {
				if ((jump != null) && (j < jump.length) && (jump[k] > 0)) {
					dataState = true;
					break;
				}
				if ((num < param2)
						&& ((dataJudgeState == null) || (!dataJudgeState[k])))
					dataState = false;
				if ((data[k] <= yl1) || (data[k] >= yu2))
					continue;
				num++;
			}
			if (num >= param2) {
				if (!dataState) {
					retList.add(newAbnormalForm(j + 1, j + param1, title,
							abnormal, dateTime == null ? null : dateTime[(j + param1 - 1)], upLow));
				}
				j = j + param1 - 1;
			}
		}
		return retList;
	}

	@SuppressWarnings("rawtypes")
	public static ArrayList BaseAbnormal8(double[] data, Abnormity abnormal,
			String[] dateTime, boolean[] dataJudgeState, double ucl,
			double lcl, String title, ArrayList retList, int upLow, int[] jump) {
		return BaseAbnormalSC(data, abnormal, dateTime, dataJudgeState, ucl,
				lcl, title, retList, upLow, jump);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList BaseAbnormal8(double[] dataJu, Abnormity abnormal,
			String[] dateTime, boolean[] dataJudgeState, double[] ucl,
			double[] lcl, String title, ArrayList retList, int upLow, int[] jump) {
		float[] data = doubleToFloat(dataJu);
		int param1 = abnormal.getParam1();
		int param2 = abnormal.getParam2();

		for (int j = 0; j < data.length - param2 + 1; j++) {
			int num = 0;
			boolean dataState = true;
			if (j > data.length - param1)
				param1 = param2;
			for (int k = j; k <= j + param1 - 1; k++) {
				if ((jump != null) && (j < jump.length) && (jump[k] > 0)) {
					dataState = true;
					break;
				}
				if ((num < param2)
						&& ((dataJudgeState == null) || (!dataJudgeState[k])))
					dataState = false;
				if ((data[k] <= ucl[k]) && (data[k] >= lcl[k]))
					continue;
				num++;
			}
			if (num >= param2) {
				if (!dataState) {
					retList.add(newAbnormalForm(j + 1, j + param1, title,
							abnormal, dateTime == null ? null : dateTime[(j + param1 - 1)], upLow));
				}
				j = j + param1 - 1;
			}
		}
		return retList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList BaseAbnormal9(double[] dataJu, Abnormity abnormal,
			String[] dateTime, boolean[] dataJudgeState, Double tu, Double tl,
			String title, ArrayList retList, int upLow, int[] jump) {
		float[] data = doubleToFloat(dataJu);
		int param1 = abnormal.getParam1();
		int param2 = abnormal.getParam2();

		for (int j = 0; j < data.length - param2 + 1; j++) {
			int num = 0;
			boolean dataState = true;
			if (j > data.length - param1)param1 = param2;
			for (int k = j; k <= j + param1 - 1; k++) {
				if ((jump != null) && (j < jump.length) && (jump[k] > 0)) {
					dataState = true;
					break;
				}
				if ((num < param2) && ((dataJudgeState == null) || (!dataJudgeState[k])))
					dataState = false;
				if (((tu != null) && (data[k] > tu.doubleValue())) || ((tl != null) && (data[k] < tl.doubleValue())))
					num++;
			}
			if (num >= param2) {
				if (!dataState) {
					retList.add(newAbnormalForm(j + 1, j + param1, title,
							abnormal, dateTime == null ? null : dateTime[(j + param1 - 1)], upLow));
				}
				j = j + param1 - 1;
			}
		}
		return retList;
	}

	@SuppressWarnings("rawtypes")
	public static ArrayList BaseAbnormal10(double[] data, Abnormity abnormal,
			String[] dateTime, boolean[] dataJudgeState, double ucl,
			double lcl, double cl, String title, ArrayList retList, int upLow,
			int[] jump) {
		return BaseAbnormalTC(data, abnormal, dateTime, dataJudgeState,
				(ucl - cl) * 2.0D / 3.0D + cl, cl - (cl - lcl) * 2.0D / 3.0D,
				title, retList, upLow, jump);
	}

	@SuppressWarnings("rawtypes")
	public static ArrayList BaseAbnormal11(double[] data, Abnormity abnormal,
			String[] dateTime, boolean[] dataJudgeState, double ucl,
			double lcl, double cl, String title, ArrayList retList, int upLow,
			int[] jump) {
		return BaseAbnormalTC(data, abnormal, dateTime, dataJudgeState,
				(ucl - cl) / 3.0D + cl, cl - (cl - lcl) / 3.0D, title, retList,
				upLow, jump);
	}

	@SuppressWarnings("rawtypes")
	public static ArrayList BaseAbnormal12(double[] data, Abnormity abnormal,
			String[] dateTime, boolean[] dataJudgeState, double cl,
			String title, ArrayList retList, int upLow, int[] jump) {
		return BaseAbnormalTC(data, abnormal, dateTime, dataJudgeState, cl, cl,
				title, retList, upLow, jump);
	}

	@SuppressWarnings("rawtypes")
	public static ArrayList BaseAbnormal13(double[] data, Abnormity abnormal,
			String[] dateTime, boolean[] dataJudgeState, double ucl,
			double lcl, double cl, String title, ArrayList retList, int upLow,
			int[] jump) {
		return BaseAbnormalSC(data, abnormal, dateTime, dataJudgeState,
				(ucl - cl) * 2.0D / 3.0D + cl, cl - (cl - lcl) * 2.0D / 3.0D,
				title, retList, upLow, jump);
	}

	@SuppressWarnings("rawtypes")
	public static ArrayList BaseAbnormal14(double[] data, Abnormity abnormal,
			String[] dateTime, boolean[] dataJudgeState, double ucl,
			double lcl, double cl, String title, ArrayList retList, int upLow,
			int[] jump) {
		return BaseAbnormalSC(data, abnormal, dateTime, dataJudgeState,
				(ucl - cl) / 3.0D + cl, cl - (cl - lcl) / 3.0D, title, retList,
				upLow, jump);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static ArrayList BaseAbnormalABC(double[] dataJu,
			Abnormity abnormal, String[] dateTime, boolean[] dataJudgeState,
			double yu1, double yu2, double yl1, double yl2, String title,
			ArrayList retList, int upLow, int[] jump) {
		float[] data = doubleToFloat(dataJu);
		int dataLength = data.length;
		int param1 = abnormal.getParam1();
		int param2 = abnormal.getParam2();

		for (int j = 0; j < dataLength - param2 + 1; j++) {
			int num = 0;
			boolean dataState = true;
			if (j > dataLength - param1)
				param1 = param2;
			for (int k = j; k <= j + param1 - 1; k++) {
				if ((jump != null) && (j < jump.length) && (jump[k] > 0)) {
					dataState = true;
					break;
				}
				if ((num < param2)
						&& ((dataJudgeState == null) || (!dataJudgeState[k])))
					dataState = false;
				if (((data[k] > yu1) && (data[k] < yu2))
						|| ((data[k] > yl1) && (data[k] < yl2)))
					num++;
			}
			if (num >= param2) {
				if (!dataState) {
					retList.add(newAbnormalForm(j + 1, j + param1, title,
							abnormal, dateTime == null ? null : dateTime[(j + param1 - 1)], upLow));
				}
				j = j + param1 - 1;
			}
		}
		return retList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static ArrayList BaseAbnormalTC(double[] dataJu,
			Abnormity abnormal, String[] dateTime, boolean[] dataJudgeState,
			double yu, double yl, String title, ArrayList retList, int upLow,
			int[] jump) {
		float[] data = doubleToFloat(dataJu);
		int dataLength = data.length;
		int param1 = abnormal.getParam1();
		int param2 = abnormal.getParam2();

		for (int j = 0; j < dataLength - param2 + 1; j++) {
			int num = 0;
			boolean dataState = true;
			if (j > dataLength - param1)
				param1 = param2;
			for (int k = j; k <= j + param1 - 1; k++) {
				if ((jump != null) && (j < jump.length) && (jump[k] > 0)) {
					dataState = true;
					break;
				}
				if ((num < param2)
						&& ((dataJudgeState == null) || (!dataJudgeState[k])))
					dataState = false;
				if (data[k] <= yu)
					continue;
				num++;
			}
			if (num >= param2) {
				if (!dataState) {
					retList.add(newAbnormalForm(j + 1, j + param1, title,
							abnormal, dateTime == null ? null : dateTime[(j + param1 - 1)], upLow));
				}
				j = j + param1 - 1;
			}
		}

		param1 = abnormal.getParam1();
		for (int j = 0; j < dataLength - param2 + 1; j++) {
			int num = 0;
			boolean dataState = true;
			if (j > dataLength - param1)
				param1 = param2;
			for (int k = j; k <= j + param1 - 1; k++) {
				if ((jump != null) && (j < jump.length) && (jump[k] > 0)) {
					dataState = true;
					break;
				}
				if ((num < param2)
						&& ((dataJudgeState == null) || (!dataJudgeState[k])))
					dataState = false;
				if (data[k] >= yl)
					continue;
				num++;
			}
			if (num >= param2) {
				if (!dataState) {
					retList.add(newAbnormalForm(j + 1, j + param1, title,
							abnormal, dateTime == null ? null : dateTime[(j
									+ param1 - 1)], upLow));
				}
				j = j + param1 - 1;
			}
		}
		return retList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static ArrayList BaseAbnormalSC(double[] dataJu,
			Abnormity abnormal, String[] dateTime, boolean[] dataJudgeState,
			double yu, double yl, String title, ArrayList retList, int upLow,
			int[] jump) {
		float[] data = doubleToFloat(dataJu);
		int param1 = abnormal.getParam1();
		int param2 = abnormal.getParam2();

		for (int j = 0; j < data.length - param2 + 1; j++) {
			int num = 0;
			boolean dataState = true;
			if (j > data.length - param1)
				param1 = param2;
			for (int k = j; k <= j + param1 - 1; k++) {
				if ((jump != null) && (j < jump.length) && (jump[k] > 0)) {
					dataState = true;
					break;
				}
				if ((num < param2)
						&& ((dataJudgeState == null) || (!dataJudgeState[k])))
					dataState = false;
				if ((data[k] <= yu) && (data[k] >= yl))
					continue;
				num++;
			}
			if (num >= param2) {
				if (!dataState) {
					retList.add(newAbnormalForm(j + 1, j + param1, title,
							abnormal, dateTime == null ? null : dateTime[(j + param1 - 1)], upLow));
				}
				j = j + param1 - 1;
			}
		}
		return retList;
	}

	private static AbnormalForRealTime newAbnormalForm(int startPosition,
			int endPosition, String title, Abnormity abnormal,
			String happenTime, int upLow) {
		AbnormalForRealTime abnormalForRealTime = new AbnormalForRealTime();
		abnormalForRealTime.setStartPosition(startPosition);
		abnormalForRealTime.setEndPosition(endPosition);
		abnormalForRealTime.setTitle(title);
		abnormalForRealTime.setAbnormityCN(abnormal.getAbnormityCN());
		abnormalForRealTime.setAbnormity(abnormal.getAbnormity());
		abnormalForRealTime.setHappenTime(happenTime);
		abnormalForRealTime.setUpLow(upLow);
		return abnormalForRealTime;
	}

	private static float[] doubleToFloat(double[] data) {
		if (data == null) {
			return null;
		}
		float[] retData = new float[data.length];
		for (int i = 0; i < data.length; i++)
			try {
				retData[i] = (float) data[i];
			} catch (Exception localException) {
			}
		return retData;
	}
}