package com.ambition.spc.abnormal.util;


import java.util.ArrayList;

import com.ambition.spc.entity.AbnormalForRealTime;
import com.ambition.spc.entity.Abnormity;
import com.ambition.spc.util.JudgeAbnormalUtil;
/**    
 * JLJudgeAbnormity.java
 * @authorBy YUKE
 *
 */
public class JLJudgeAbnormity {
	@SuppressWarnings("rawtypes")
	public static boolean[] JudgeAbnormal(double[] data, Abnormity[] abnormal, double ucl, double lcl, double cl, Double tu, Double tl, int upLow, int[] jump){
	    int dataLength = data.length;
	    boolean[] abnormalPosition = new boolean[dataLength];
	    for (int i = 0; i < dataLength; i++) {
	    	abnormalPosition[i] = false;
	    }
	    ArrayList retList = JudgeAbnormal(data, abnormal, null, null, ucl, lcl, cl, tu, tl, "", upLow, jump);
	    for (int i = 0; i < retList.size(); i++) {
	    	AbnormalForRealTime AbnormalForRealTime = (AbnormalForRealTime)retList.get(i);
		    int j = AbnormalForRealTime.getStartPosition() - 1;
		    for (; j < AbnormalForRealTime.getEndPosition(); j++) {
		    	abnormalPosition[j] = true;
		    }
	    }
	    return abnormalPosition;
	}

	@SuppressWarnings("rawtypes")
	public static Abnormity[] StatisticAbnormal(double[] data, Abnormity[] abnormal, double ucl, double lcl, double cl, Double tu, Double tl, int upLow, int[] jump){
	    int[] total = new int[14];
	    ArrayList retList = JudgeAbnormal(data, abnormal, null, null, ucl, lcl, cl, tu, tl, "", upLow, jump);
	    for (int i = 0; i < retList.size(); i++) {
	    	AbnormalForRealTime AbnormalForRealTime = (AbnormalForRealTime)retList.get(i);
	    	total[(AbnormalForRealTime.getAbnormity() - 1)] += 1;
	    }
	    for (int i = 0; i < abnormal.length; i++) {
	    	abnormal[i].setAbnormityNum(total[(abnormal[i].getAbnormity() - 1)]);
	    }
	    return abnormal;
    }

	@SuppressWarnings("rawtypes")
	public static ArrayList JudgeAbnormalRealtime(double[] data, Abnormity[] abnormal, String[] dateTime, boolean[] dataJudgeState, double ucl, double lcl, double cl, Double tu, Double tl, String title, int upLow, int[] jump){
		return JudgeAbnormal(data, abnormal, dateTime, dataJudgeState, ucl, lcl, cl, tu, tl, title, upLow, jump);
	}

	@SuppressWarnings("rawtypes")
	private static ArrayList JudgeAbnormal(double[] data, Abnormity[] abnormal, String[] dateTime, boolean[] dataJudgeState, double ucl, double lcl, double cl, Double tu, Double tl, String title, int upLow, int[] jump){
	    ArrayList retList = new ArrayList();
	    for (int i = 0; (abnormal != null) && (i < abnormal.length); i++) {
	    	switch (abnormal[i].getAbnormity()) {
	    		case 1:
	    			retList = JudgeAbnormalUtil.BaseAbnormal1(data, abnormal[i], dateTime, dataJudgeState, title, retList, upLow, jump);
	    			break;
	    		case 2:
	    			retList = JudgeAbnormalUtil.BaseAbnormal2(data, abnormal[i], dateTime, dataJudgeState, title, retList, upLow, jump);
	    			break;
    			case 3:
	  				retList = JudgeAbnormalUtil.BaseAbnormal3(data, abnormal[i], dateTime, dataJudgeState, title, retList, upLow, jump);
    				break;
    			case 4:
	    	  		retList = JudgeAbnormalUtil.BaseAbnormal4(data, abnormal[i], dateTime, dataJudgeState, title, retList, upLow, jump);
	    	  		break;
    			case 5:
    				retList = JudgeAbnormalUtil.BaseAbnormal5(data, abnormal[i], dateTime, dataJudgeState, ucl, lcl, cl, title, retList, upLow, jump);
    				break;
    			case 6:
    				retList = JudgeAbnormalUtil.BaseAbnormal6(data, abnormal[i], dateTime, dataJudgeState, ucl, lcl, cl, title, retList, upLow, jump);
    				break;
    			case 7:
    				retList = JudgeAbnormalUtil.BaseAbnormal7(data, abnormal[i], dateTime, dataJudgeState, ucl, lcl, cl, title, retList, upLow, jump);
    				break;
    			case 8:
    				retList = JudgeAbnormalUtil.BaseAbnormal8(data, abnormal[i], dateTime, dataJudgeState, ucl, lcl, title, retList, upLow, jump);
    				break;
				case 9:	
					retList = JudgeAbnormalUtil.BaseAbnormal9(data, abnormal[i], dateTime, dataJudgeState, tu, tl, title, retList, upLow, jump);
					break;
				case 10:
					retList = JudgeAbnormalUtil.BaseAbnormal10(data, abnormal[i], dateTime, dataJudgeState, ucl, lcl, cl, title, retList, upLow, jump);
					break;
				case 11:
					retList = JudgeAbnormalUtil.BaseAbnormal11(data, abnormal[i], dateTime, dataJudgeState, ucl, lcl, cl, title, retList, upLow, jump);
	        		break;
				case 12:
					retList = JudgeAbnormalUtil.BaseAbnormal12(data, abnormal[i], dateTime, dataJudgeState, cl, title, retList, upLow, jump);
					break;
				case 13:
					retList = JudgeAbnormalUtil.BaseAbnormal13(data, abnormal[i], dateTime, dataJudgeState, ucl, lcl, cl, title, retList, upLow, jump);
	        		break;
				case 14:
					retList = JudgeAbnormalUtil.BaseAbnormal14(data, abnormal[i], dateTime, dataJudgeState, ucl, lcl, cl, title, retList, upLow, jump);
    		}
	
	    }
	    return retList;
	}

	public static Abnormity[] getAbnormity(Abnormity[] abnormal, int upLow){
	    if (abnormal == null) return null;
	    int n = 0;
	    for (int i = 0; i < abnormal.length; i++) {
	    	if ((abnormal[i].getUpLow() != upLow) && (abnormal[i].getUpLow() != 3)) continue; n++;
	    }
	    Abnormity[] retAbnor = new Abnormity[n];
	    n = 0;
	    for (int i = 0; i < abnormal.length; i++) {
			if ((abnormal[i].getUpLow() == upLow) || (abnormal[i].getUpLow() == 3)) {
			    retAbnor[n] = abnormal[i];
			    n++;
			}
	    }
	    return retAbnor;
	}
}