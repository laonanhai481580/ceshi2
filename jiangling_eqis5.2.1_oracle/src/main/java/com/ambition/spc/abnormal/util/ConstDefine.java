package com.ambition.spc.abnormal.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import com.ambition.spc.entity.Abnormity;
import com.ambition.spc.abnormal.util.ConstantsOfSPC;

/**
 * ConstDefine.java
 * 
 * @authorBy YUKE
 * 
 */
public class ConstDefine {

	public static String WORKINF = "(生产中)"; // 如果该工序有数据标记为生产中

	public static String LINEB = "B线"; // 生产线B线

	public static int ADOWIDTH = 10; // 状态(甲班未处理)

	public static String SILK_UNION_B = "制丝联合工房乙"; // 制丝联合工房乙

	public static String DPK_ALARM = "DPK报警"; // DPK报警

	public static String DAY = "白班"; // 白班

	public static String NIGHT = "晚班"; // 晚班

	public static String A_LINE_SPICES_FLOW = "22";// A线香料流量

	public static String B_LINE_SPICES_FLOW = "45";// B线香料流量

	public static String A_LINE_PROP_MIX = "19"; // A线比例搀兑 - 梗丝流量

	public static String B_LINE_PROP_MIX = "42"; // B线比例搀兑 - 梗丝流量

	public static String DPK_MEET = "DPK未达标：批次DPK值<"; // DPK未达标：批次DPK值<

	public static int DEFAULT_NUMBER_CONTROL = 500;// 标记数据监控的默认组数

	public static int ERROE_NUMBER_CONTROL = 300;// 超过这个异常数没有处理就不再向系统中记录异常信息

	public static String TREND = "0";// 趋势图

	public static String RUN = "1";// 运行图

	public static String RAIN_BOW_RULE = "C0";// 彩虹图规则

	public static int NUMBER_PER_PAGE = 10;// 每页记录条数

	public static boolean jdbcFlag = true;// 用来标记是使用 jdbc 还是hibernate 来获取数据

	// 不允许修改

	public static String forbidEdit = "no";

	// true表示是jdbc获取数据，false表示是hibernate获取数据

	public static int IMG_DEFAULT_WIDTH = 666;// 监控图默认的宽度

	public static int IMG_DEFAULT_NUM = 33;// 监控图中默认的点数

	public static int IMG_DEFAULT_WIDTH_ANALYSIS = 600;// 监控图默认的宽度

	public static int NUM_PER_PAGE = 10;// 每页记录条数

	public static int NUMBER_CUR_PAGE = 1;// 当前显示的页

	public static int PROCESS_REP_PER_PAGE = 20;// 统计分析过程分析报告列表每页记录数

	// 数据类型， 计量型 1 和计数型 2
	public static String DATA_TYPE_ONE = "1";//

	public static String DATA_YPPE_TWO = "2";

	public static String Parameter = "quaParameter";// 参数

	public static String Product = "product";// 产品

	public static String ExceptionSampleDo = "sampleDo";// 简单处理

	public static String ExceptionAnalysis = "analysis";// 纠正

	public static String ExceptionCheck = "check";// 纠正

	public static String ExceptionValidate = "validate";// 验证

	public static String mprFactory = "mprFactory";// 工厂监控

	public static String OriginalParameter = "original";// 原始参数

	public static String CiteParameter = "cite";// 引用后的参数

	public static String xbar_R = "xbar_R";// 均值-极差控制图

	public static String xbar_S = "xbar_S";// 均值-标准偏差控制图

	public static String x_RS = "x_RS";// 单值-移动极差控制图

	public static String M_R = "M_R";// 中位数-极差控制图

	public static String nesting = "nesting";// 嵌套控制图

	public static String regress = "regress";// 回归控制图

	public static String nest_reg = "nest_reg";// 嵌套回归控制图

	public static String Z_MR = "Z_MR";// 通用单值-移动极差控制图

	public static String P = "P";// 不合格品率控制图

	public static String PN = "PN";// 不合格品数控制图

	public static String C = "C";// 不合格数控制图

	public static String U = "U";// 单位不合格数控制图

	public static String RAIN_BOW = "rainbow";// 彩虹图

	public static String online = "online";// 在线人工

	public static String offline = "offline";// 离线采集

	public static String auto = "auto";// 自动采集

	public static String JILIANG = "1";// 计量型数据

	public static String JISHU = "2";// 计数型数据

	public static String OTHER = "3";// 其他

	public static String DOUBLE = "double";// 双侧

	public static String SINGLEUP = "singleUp";// 单侧上限

	public static String SINGLELOW = "singleLow";// 单侧下限

	public static String MAN = "man";// 人员

	public static String MACHINE = "machine";// 机器

	public static String MATERIAL = "material";// 材料

	public static String METHOD = "method";// 方法

	public static String ENTIRONMENT = "entironment";// 环境

	public static String MEASURE = "measure";// 测量

	public static String PROPERTY_OTHER = "other";// 其他；

	public static String BRAND_CLASS = "brandClass";// 牌号类别

	public static String POST = "post";// 岗位

	public static String note = "note";// 公告

	public static String light = "light";// 声光报警

	public static String electronDisplay = "electronDisplay";// 电子显示屏

	public static String deskTopNews = "deskTopNews";// 桌面消息

	public static String letter = "letter";// 短信息

	public static String email = "email";// 邮件

	public static String reportMan = "系统自动";

	public static String autoReport = "自动";

	public static String processReport = "过程分析";

	public static String PROPERTY_STATE_YES = "yes";// 附属信息是固定值

	public static String PROPERTY_STATE_NO = "no";// 附属信息不是固定值

	public static String EXCEL_TEMPLATE_URL = "excel/template/";// 数据采集模板存放位置
	public static String EXCEL_DATA_URL = "excel/data/";// 数据采集模板数据的位置

	public static String MONITOR_IMAGE = "monitorImg/";

	public static final String NOT_GETPARAM = "没有获取到这个参数";
	public static final String NOT_RECORD = "数据库中没有这条记录了";

	// 采样方案的状态

	public static String BEUSING = "启用";
	public static String WILL_USING = "未启用";
	public static String FORBID_USE = "禁用";

	// 标记是产品中的质量参数还是采样方案中的质量参数

	public static String PRODUCTQUA = "product";
	public static String PLANQUA = "plan";

	// 监控中数据采集的类型
	public static String SEARCH_DATA_TYPE_TIMEING = "timeing";
	public static String SEARCH_DATA_TYPE_GROUPING = "grouping";
	public static String SEARCH_DATA_TYPE_EVALUATE = "evaluate";
	public static String SEARCH_DATA_TYPE_BATCH = "batching";
	public static String SEARCH_DATA_TYPE_OLDBATCH = "oldBatch";
	// 数据的默认显示形式

	public static String SHOW_DATA_DEFAULTCHOISE_TIMEING = "timeing";
	public static String SHOW_DATA_DEFAULTCHOISE_GROUPING = "grouping";

	// 评价设定中的评价类型

	public static int EVALUATE_TYPE_BATCH = 0;
	public static int EVALUATE_TYPE_DAY = 1;
	public static int EVALUATE_TYPE_WEEK = 2;
	public static int EVALUATE_TYPE_MONTH = 3;
	public static int EVALUATE_TYPE_QUARTER = 4;

	// 报表模板条件中的三种不同的标记

	public static String TEMPLATE_CONDITION_TYPE_TOOL = "statTool";
	public static String TEMPLATE_CONDITION_TYPE_ITEM = "statItem";
	public static String TEMPLATE_CONDITION_TYPE_INFO = "paramInfo";
	public static String TEMPLATE_CONDITION_TYPE_INFO_R = "paramInfoR";// 记录属性

	public static String TEMPLATE_CONDITION_TYPE_PARAMETER = "parameter";
	public static int TEMPLATE_CONDITION_TYPE_INFO_R_INT = 100;// 记录属性

	// 数据采集中的附属信息和数据的前缀字段
	public static String COLLECTION_DATA_PROPERTY = "infoPorp";
	public static String COLLECTION_DATA_ITEM = "dataItem";

	// 统计报表中报告的项目表中有个默认行数设定为7 有最大最小，均值极差 等7项信息，这里默认7,最后一列是记录原始数据的ID值

	public static int DEFAULT_COLUMN = 8;

	// public static String CATCHSOMEDATA="1";//标记取数据是取部分数据
	//
	//
	// public static String CATCHALLDATA="0";//标记取数据是取全部数据

	public static String dirverType = "dirverType";
	public static String _driver_class = "_driver_class";
	public static String _url = "_url";

	public static String THREAD_PARAM_CONFIG = "/config/threadParam.properties";
	public static String PROP_CONFIG = "config/prop.properties";

	private static int COLLECT_FREQUENCY = 10;// 默认值

	public static int silkDataInfoPorpNum = 15;// 数据中的附属信息列的个数

	public static String ALARM_TYPE_RECORD = "0"; // 报警
	public static String ALARM_TYPE_PRE = "1"; // 预警

	/**
	 * 设置采样间隔
	 */
	public static void setCollect_Frequency(int va) {
		if (va > 0)
			COLLECT_FREQUENCY = va;
	}

	/**
	 * 
	 * @return
	 */
	public static int getCollect_Frequency() {
		return COLLECT_FREQUENCY;
	}

	/**
	 * 
	 * @param img_url
	 * @param request
	 * @param image
	 */
	public static void picImageToDir(String img_url,
			HttpServletRequest request, BufferedImage image) {
		if (img_url != null) {
			String projectUrl = request.getSession().getServletContext()
					.getRealPath("/");
			try {
				ImageIO.write(image, "JPEG", new File(projectUrl + img_url));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 控制图类型
	 * 
	 * 
	 * 
	 * @param controlPicType
	 *            String
	 * @return int
	 */

	public static int getChartType(String controlPicType) {
		int chartType = 0;
		if (ConstDefine.xbar_R.equals(controlPicType)) {// 均值级差控制图
			chartType = ConstantsOfSPC.SPC_CONTROLCHARTSTYPE_XAR;
		} else if (ConstDefine.xbar_S.equals(controlPicType)) {// 均值标准差
			chartType = ConstantsOfSPC.SPC_CONTROLCHARTSTYPE_XAS;
		} else if (ConstDefine.M_R.equals(controlPicType)) {// zhongwei jicha
			chartType = ConstantsOfSPC.SPC_CONTROLCHARTSTYPE_XMR;
		} else if (ConstDefine.x_RS.equals(controlPicType)
				|| ConstDefine.Z_MR.equals(controlPicType)) {// danzhi yidong
			// jicha
			chartType = ConstantsOfSPC.SPC_CONTROLCHARTSTYPE_SXRS;
		} else if (ConstDefine.nesting.equals(controlPicType)) {// 嵌套控制图

			chartType = ConstantsOfSPC.SPC_CONTROLCHARTSTYPE_XAS;
		} else if (ConstDefine.regress.equals(controlPicType)) {// 回归控制图

			chartType = ConstantsOfSPC.SPC_CONTROLCHARTSTYPE_XAS;
		} else if (ConstDefine.nest_reg.equals(controlPicType)) {// 嵌套回归控制图

			chartType = ConstantsOfSPC.SPC_CONTROLCHARTSTYPE_XAS;
		} else if (ConstDefine.PN.equals(controlPicType)) {// pn
			chartType = ConstantsOfSPC.SPC_CONTROLCHARTSTYPE_PN;
		} else if (ConstDefine.P.equals(controlPicType)) {// p
			chartType = ConstantsOfSPC.SPC_CONTROLCHARTSTYPE_P;
		} else if (ConstDefine.C.equals(controlPicType)) {// C
			chartType = ConstantsOfSPC.SPC_CONTROLCHARTSTYPE_C;
		} else if (ConstDefine.U.equals(controlPicType)) {// U
			chartType = ConstantsOfSPC.SPC_CONTROLCHARTSTYPE_U;
		} else if (ConstDefine.RAIN_BOW.equals(controlPicType)) {
			chartType = ConstantsOfSPC.SPC_CONTROLCHARTSTYPE_PRE;// 彩虹图

		}
		return chartType;
	}

	/**
	 * 解析判异规则的规则表达式
	 * 
	 * @param exp
	 * @param l
	 * @param dataType
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void parseRule(String exp, List l, String dataType, String controlPicType) {
		if (exp != null) {
			String[] exps = exp.split("[.]");
			if (exps != null) {
				int dataT = Integer.valueOf(dataType).intValue();
				if (OTHER.equals(dataType)) {
					if (!RAIN_BOW.equals(controlPicType)) {
						dataT = 1;
					}
				}
				int length = exps.length;
				Abnormity abnormity = new Abnormity(dataT);
				if (TREND.equals(exps[1])) {// 表示是趋势图
					int index = Integer.parseInt(exps[length - 1]);// 规则号
					int param = Integer.parseInt(exps[length - 2]);// 点数
					abnormity.setAbnormity(index);
					abnormity.setParam1(param);
				} else if (RUN.equals(exps[1])) {// 表示是运行图
					int index = Integer.parseInt(exps[length - 1]);// 规则号
					int param = Integer.parseInt(exps[length - 3]);// 总点数
					int param1 = Integer.parseInt(exps[length - 2]);// 点数
					abnormity.setAbnormity(index);
					abnormity.setParam1(param);
					abnormity.setParam2(param1);
				}
				int uplow = 0;
				if (JILIANG.equals(dataType)) {
					uplow = Integer.parseInt(exps[0]);
					abnormity.setUpLow((uplow + 1));
				} else if (JISHU.equals(dataType)) {
					abnormity.setUpLow(3);
				} else {
					if (RAIN_BOW.equals(controlPicType)) {
						abnormity.setUpLow(3);
					} else {
						uplow = Integer.parseInt(exps[0]);
						abnormity.setUpLow((uplow + 1));
					}
				}
				l.add(abnormity);
			}
		}
	}

	/**
	 * 
	 * @param url
	 * @param time
	 * @return
	 */
	public static String scanFileDir(String url, String fileN, String extendType) {
		int serialNumber = -1;
		File fileDir = new File(url);
		if (!fileDir.isDirectory()) {
			fileDir.mkdirs();
		} else {
			// 删除两个小时之前的文件
			File[] childFile = fileDir.listFiles();
			if (childFile.length > 0) {
				for (int i = 0; i < childFile.length; i++) {
					String sonFileN = childFile[i].getName();
					if (sonFileN.startsWith(fileN)) {
						if (fileN.length() == sonFileN.indexOf(extendType)) {// 说明以前没有出现相同的名字

							serialNumber = 1;
						} else {
							int num = Integer.parseInt(sonFileN.toLowerCase()
									.substring(fileN.length(),
											sonFileN.indexOf(extendType))) + 1;
							serialNumber = serialNumber < num ? num
									: serialNumber;
						}
					}
				}
			} else {
				return fileN;
			}
		}
		return serialNumber < 0 ? fileN : fileN + serialNumber;
	}

	/**
	 * 
	 * @param url
	 * @param time
	 * @return
	 */
	public static boolean delFile(String url) {
		File file = new File(url);
		if (file.exists()) {
			return file.delete();
		}
		return true;
	}

	public static String setControlPicTypeName(String controlPicTypeName) {
		if (xbar_R.equals(controlPicTypeName)) {
			return "均值-极差控制图";
		} else if (xbar_S.equals(controlPicTypeName)) {
			return "均值-标准偏差控制图";
		} else if (x_RS.equals(controlPicTypeName)) {
			return "单值-移动极差控制图";
		} else if (M_R.equals(controlPicTypeName)) {
			return "中位数-极差控制图";
		} else if (nesting.equals(controlPicTypeName)) {
			return "嵌套控制图";
		} else if (RAIN_BOW.equals(controlPicTypeName)) {
			return "彩虹图";
		} else if (Z_MR.equals(controlPicTypeName)) {
			return "通用单值-移动极差控制图";
		} else if (P.equals(controlPicTypeName)) {
			return "不合格品率控制图";
		} else if (PN.equals(controlPicTypeName)) {
			return "不合格品数控制图";
		} else if (C.equals(controlPicTypeName)) {
			return "不合格数控制图";
		} else if (U.equals(controlPicTypeName)) {
			return "单位不合格数控制图";
		}
		return controlPicTypeName;
	}

	public static String setDataTypeName(String dataTypeName) {
		if (JILIANG.equals(dataTypeName)) {
			return "计量型";
		} else if (JISHU.equals(dataTypeName)) {
			return "计数型";
		} else if (OTHER.equals(dataTypeName)) {
			return "其他";
		} else {
			return dataTypeName;
		}
	}

	public static String setReportTypeName(String reportTypeName) {
		if ("0".equals(reportTypeName)) {
			return "个人报表";
		} else if ("1".equals(reportTypeName)) {
			return "公共报表";
		} else {
			return reportTypeName;
		}
	}

	public static String setTypeName(String typeName) {
		if ("0".equals(typeName)) {
			return "日报";
		} else if ("1".equals(typeName)) {
			return "周报";
		} else if ("2".equals(typeName)) {
			return "月报";
		} else {
			return typeName;
		}

	}

	/**
	 * 
	 * @param cls
	 * @param name
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getObject(Object obj, String name, String inputValue) {

		try {
			Class cls = obj.getClass();

			Field fields[] = cls.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (field.getType() == String.class
						|| field.getType() == java.util.Date.class
						|| field.getType() == Long.class
						|| field.getType() == Integer.class
						|| field.getType() == Float.class
						|| field.getType() == Double.class
						|| field.getType() == Boolean.class) {
					String fieldName = field.getName();

					String firstLetter = fieldName.substring(0, 1)
							.toUpperCase();
					// Get the name of a set method which is corresponding to
					// the
					// property
					String setMethodName = "set" + firstLetter
							+ fieldName.substring(1);
					// Get the method which is corresponding to the property
					if (fieldName.equalsIgnoreCase(name)) {
						Method setMethod = cls.getMethod(setMethodName,
								new Class[] { field.getType() });
						// copy the property whose value is not null and not a
						// empty
						// string
						Object value = null;
						try {
							value = inputValue;
							setMethod.invoke(obj, new Object[] { value });

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param cls
	 * @param name
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getObjectValue(Object obj, String name) {

		try {
			Class cls = obj.getClass();

			Field fields[] = cls.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (field.getType() == String.class
						|| field.getType() == java.util.Date.class
						|| field.getType() == Long.class
						|| field.getType() == Integer.class
						|| field.getType() == Float.class
						|| field.getType() == Double.class
						|| field.getType() == Boolean.class) {
					String fieldName = field.getName();

					String firstLetter = fieldName.substring(0, 1)
							.toUpperCase();
					// Get the name of a set method which is corresponding to
					// the
					// property
					String getMethodName = "get" + firstLetter
							+ fieldName.substring(1);
					// Get the method which is corresponding to the property
					if (fieldName.equalsIgnoreCase(name)) {
						Method getMethod = cls.getMethod(getMethodName, null);
						return getMethod.invoke(obj, null);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 过滤附属信息是5M1E的sql条件语句 alias 表（对象）别名
	 * 
	 * 
	 * type 类别 5m1e或者 牌号类别，或者班组
	 * 
	 * 
	 * @return
	 */
	public static String get5M1E_PropertySql(String alias, String type) {

		if (ConstDefine.BRAND_CLASS.equals(type)) {
			return " and " + alias + ".kind='" + ConstDefine.BRAND_CLASS + "'";
		} else if (ConstDefine.POST.equals(type)) {
			return " and " + alias + ".kind='" + ConstDefine.POST + "'";
		} else {
			String[] ty = { ConstDefine.MAN, ConstDefine.MACHINE,
					ConstDefine.MATERIAL, ConstDefine.METHOD,
					ConstDefine.ENTIRONMENT, ConstDefine.MEASURE,
					ConstDefine.PROPERTY_OTHER };
			String sqlw = "";
			for (int i = 0; i < ty.length; i++) {
				if (i == ty.length - 1) {
					sqlw += alias + ".kind='" + ty[i] + "' ";
				} else {
					sqlw += alias + ".kind='" + ty[i] + "' or ";
				}
			}
			return " and (" + sqlw + ")";
		}
	}

}
