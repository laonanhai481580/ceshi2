package com.ambition.spc.util;

/**    
 * ConstantsOfSPC.java(常数)
 * @authorBy wanglf
 *
 */

public abstract interface ConstantsOfSPC
{
  public static final int SPC_PARAMETER_DATATYPE_JL = 1;
  public static final String SPC_PARAMETER_DATATYPE_JL_CN = "计量值";
  public static final Integer SPC_PARAMETER_DATATYPE_JL_I = new Integer(1);
  public static final int SPC_PARAMETER_DATATYPE_JS = 2;
  public static final String SPC_PARAMETER_DATATYPE_JS_CN = "计数值";
  public static final Integer SPC_PARAMETER_DATATYPE_JS_I = new Integer(2);
  public static final int SPC_SAMPLING_STATE_ANALYSIS = 1;
  public static final String SPC_SAMPLING_STATE_ANALYSIS_CN = "分析阶段";
  public static final Integer SPC_SAMPLING_STATE_ANALYSIS_I = new Integer(1);
  public static final int SPC_SAMPLING_STATE_CONTROL = 2;
  public static final String SPC_SAMPLING_STATE_CONTROL_CN = "控制阶段";
  public static final Integer SPC_SAMPLING_STATE_CONTROL_I = new Integer(2);
  public static final int SPC_CONTROLCHARTSTYPE_XAR = 1;
  public static final String SPC_CONTROLCHARTSTYPE_XAR_CN = "均值-极差控制图";
  public static final Integer SPC_CONTROLCHARTSTYPE_XAR_I = new Integer(1);
  public static final int SPC_CONTROLCHARTSTYPE_XAS = 2;
  public static final String SPC_CONTROLCHARTSTYPE_XAS_CN = "均值-标准偏差控制图";
  public static final Integer SPC_CONTROLCHARTSTYPE_XAS_I = new Integer(2);
  public static final int SPC_CONTROLCHARTSTYPE_XMR = 3;
  public static final String SPC_CONTROLCHARTSTYPE_XMR_CN = "中位数-极差控制图";
  public static final Integer SPC_CONTROLCHARTSTYPE_XMR_I = new Integer(3);
  public static final int SPC_CONTROLCHARTSTYPE_SXRS = 4;
  public static final String SPC_CONTROLCHARTSTYPE_SXRS_CN = "单值移动极差控制图";
  public static final Integer SPC_CONTROLCHARTSTYPE_SXRS_I = new Integer(4);
  public static final int SPC_CONTROLCHARTSTYPE_PN = 5;
  public static final String SPC_CONTROLCHARTSTYPE_PN_CN = "不合格品数控制图";
  public static final Integer SPC_CONTROLCHARTSTYPE_PN_I = new Integer(5);
  public static final int SPC_CONTROLCHARTSTYPE_P = 6;
  public static final String SPC_CONTROLCHARTSTYPE_P_CN = "不合格品率控制图";
  public static final Integer SPC_CONTROLCHARTSTYPE_P_I = new Integer(6);
  public static final int SPC_CONTROLCHARTSTYPE_C = 7;
  public static final String SPC_CONTROLCHARTSTYPE_C_CN = "不合格数控制图";
  public static final Integer SPC_CONTROLCHARTSTYPE_C_I = new Integer(7);
  public static final int SPC_CONTROLCHARTSTYPE_U = 8;
  public static final String SPC_CONTROLCHARTSTYPE_U_CN = "单位不合格数控制图";
  public static final Integer SPC_CONTROLCHARTSTYPE_U_I = new Integer(8);
  public static final int SPC_CONTROLCHARTSTYPE_PRE = 9;
  public static final String SPC_CONTROLCHARTSTYPE_PRE_CN = "彩虹图";
  public static final Integer SPC_CONTROLCHARTSTYPE_PRE_I = new Integer(9);
  public static final int SPC_CONTROLCHARTSTYPE_ZMR = 10;
  public static final String SPC_CONTROLCHARTSTYPE_ZMR_CN = "通用单值移动极差控制图";
  public static final Integer SPC_CONTROLCHARTSTYPE_ZMR_I = new Integer(10);
  public static final int SPC_CONTROLCHARTSTYPE_NEST = 20;
  public static final String SPC_CONTROLCHARTSTYPE_NEST_CN = "嵌套控制图";
  public static final Integer SPC_CONTROLCHARTSTYPE_NEST_I = new Integer(20);
  public static final int SPC_ABNORMITY_NUM = 14;
  public static final int SPC_ABNORMITY1 = 1;
  public static final int SPC_ABNORMITY2 = 2;
  public static final int SPC_ABNORMITY3 = 3;
  public static final int SPC_ABNORMITY4 = 4;
  public static final int SPC_ABNORMITY5 = 5;
  public static final int SPC_ABNORMITY6 = 6;
  public static final int SPC_ABNORMITY7 = 7;
  public static final int SPC_ABNORMITY8 = 8;
  public static final int SPC_ABNORMITY9 = 9;
  public static final int SPC_ABNORMITY10 = 10;
  public static final int SPC_ABNORMITY11 = 11;
  public static final int SPC_ABNORMITY12 = 12;
  public static final int SPC_ABNORMITY13 = 13;
  public static final int SPC_ABNORMITY14 = 14;
  public static final int SPC_CPK_GRADE0 = 0;
  public static final String SPC_CPK_GRADE0_CN = "特级";
  public static final String SPC_CPK_GRADE0_JUDGE = "过程能力非常充分";
  public static final String SPC_CPK_GRADE0_WAY = "为提高产品质量，对关键或主要项目可缩小公差范围；或为提高效率、降低成本而放宽波动幅度，降低设备精度等级；或将精度要求特别高的零件调至该工序进行加工等";
  public static final Integer SPC_CPK_GRADE0_I = new Integer(0);
  public static final int SPC_CPK_GRADE1 = 1;
  public static final String SPC_CPK_GRADE1_CN = "1级";
  public static final String SPC_CPK_GRADE1_JUDGE = "过程能力充分";
  public static final String SPC_CPK_GRADE1_WAY = "当不是关键或主要项目时，放宽波动幅度；降低对原材料的要求；简化质量检验，采用抽样检验或减少检验频次";
  public static final Integer SPC_CPK_GRADE1_I = new Integer(1);
  public static final int SPC_CPK_GRADE2 = 2;
  public static final String SPC_CPK_GRADE2_CN = "2级";
  public static final String SPC_CPK_GRADE2_JUDGE = "过程能力尚可";
  public static final String SPC_CPK_GRADE2_WAY = "必须用控制图或其他方法对过程进行控制或监督，以便及时发现异常波动；对产品按正常规定进行检验";
  public static final Integer SPC_CPK_GRADE2_I = new Integer(2);
  public static final int SPC_CPK_GRADE3 = 3;
  public static final String SPC_CPK_GRADE3_CN = "3级";
  public static final String SPC_CPK_GRADE3_JUDGE = "过程能力不充分";
  public static final String SPC_CPK_GRADE3_WAY = "分析分散程度大的原因，制订措施加以改进，在不影响产品质量的情况下，放宽公差范围，加强质量检测，进行全数检验或增加检验频次";
  public static final Integer SPC_CPK_GRADE3_I = new Integer(3);
  public static final int SPC_CPK_GRADE4 = 4;
  public static final String SPC_CPK_GRADE4_CN = "4级";
  public static final String SPC_CPK_GRADE4_JUDGE = "过程能力不足";
  public static final String SPC_CPK_GRADE4_WAY = "一般应停止继续加工，找出原因，改进工艺，提高Cpk值，否则全数检验，挑出不合格品";
  public static final Integer SPC_CPK_GRADE4_I = new Integer(4);
}