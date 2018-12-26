package com.ambition.util.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import sun.misc.BASE64Decoder;

import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.portal.service.IndexManager;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Department;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ParameterUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.util.ThreadParameters;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 类名:公共方法类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-5-7 发布
 */
public class CommonUtil1 {
	//特殊字符替换表
	private static Map<String,String> symbolMap = new HashMap<String, String>();
	static{
		symbolMap.put("ø","Φ");
		symbolMap.put("φ","Φ");
		symbolMap.put("∮","Φ");
		symbolMap.put("￠","Φ");
		symbolMap.put("®","R");
		symbolMap.put("«","《");
		symbolMap.put("»","》");
		symbolMap.put("\"","");
		symbolMap.put("º","°");
	}
	private static final Map<String,String> compareMap = new HashMap<String, String>();
	private static final Map<String,String> inspectionReportFieldMap = new HashMap<String, String>();
	private static final Map<String,String> defectiveGoodsFieldMap = new HashMap<String, String>();
	/**
	  * 方法名: 检验台帐对应的字段
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Map<String,String> getInspectionReportFieldMap(){
		return inspectionReportFieldMap;
	}
	
	/**
	 * 初始化访问非平台系统(小窗体专用)
	 */
	public static void initializeEnvironment(){
		String loginName = Struts2Utils.getParameter("loginName");
		String companyId = Struts2Utils.getParameter("companyId");
		User user = null;
		if(StringUtils.isNotEmpty(loginName)){
			user = ApiFactory.getAcsService().getUserByLoginNameAndBranchId(loginName, null);
		}
		String theme = null;
		if(user != null && CommonUtil1.isInteger(companyId)){
			IndexManager indexManager = (IndexManager) ContextUtils.getBean("indexManager");
			theme = indexManager.getThemeByUser(user.getId(), Long.valueOf(companyId));
		}
		//模拟用户登入，设置样式表
		ContextUtils.setTheme(StringUtils.isEmpty(theme)?"black":theme);
		//模拟用户登入，设置上下文
		if(null == ContextUtils.getUserId() && null != user){
			ThreadParameters threadParameters = new ThreadParameters();
			threadParameters.setCompanyId(Long.valueOf(companyId));
			threadParameters.setLoginName(loginName);
			threadParameters.setUserId(user.getId());
			threadParameters.setUserName(user.getName());
			ParameterUtils.setParameters(threadParameters);
		}
	}
	public static String getMainDepartMent(){
		User user = ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		Long mainDepartMentId=user.getMainDepartmentId();
		String mainDepartMent=ApiFactory.getAcsService().getDepartmentById(mainDepartMentId).getName();
    	return mainDepartMent;
	}
	public static String getBusinessUnitCode(String companyName){
		String businessUnitCode = "282";
		if("欧菲科技-神奇工场".equals(companyName)){
			businessUnitCode = "282";
		}else if("欧菲科技-CCM".equals(companyName)){
			if("CCM-下罗园区".equals(ContextUtils.getSubCompanyName())){
				businessUnitCode = "216";
			}else if("CCM-培训中心".equals(ContextUtils.getSubCompanyName())){
				businessUnitCode = "228";
			}else if("CCM-高新园区".equals(ContextUtils.getSubCompanyName())){
				businessUnitCode = "297";
			}else{
				businessUnitCode = "228";
			}
			
		}else if("欧菲科技-FPM".equals(companyName)){
			businessUnitCode = "296";
		}else{
			businessUnitCode = "";
		}
		return businessUnitCode;
	}
	/**
	  * 方法名: 不合格品处理对应的字段
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Map<String,String> getDefectiveGoodsFieldMap(){
		return defectiveGoodsFieldMap;
	}
	/**
	  * 方法名: 比较字段
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Map<String,String> getCompareMap(){
		return compareMap;
	}
	static{
		//初始化比较字符串
		compareMap.put("equals","=");
		compareMap.put("ge",">=");
		compareMap.put("gt",">");
		compareMap.put("le","<=");
		compareMap.put("lt","<");
		compareMap.put("like","like");
		compareMap.put("notlike","not like");
		compareMap.put("in","in");
		
		//初始化检查记录表的字符串对应字段
		inspectionReportFieldMap.put("factory", "factory");
		inspectionReportFieldMap.put("workshop", "workshop");
		inspectionReportFieldMap.put("productionLine", "production_line");
		inspectionReportFieldMap.put("workGroup", "work_group");//班组
		inspectionReportFieldMap.put("workGroupType", "work_group_type");//班别
		inspectionReportFieldMap.put("workProcedure", "work_procedure");
		inspectionReportFieldMap.put("planNo", "plan_no");
		inspectionReportFieldMap.put("productModel", "product_model");
		inspectionReportFieldMap.put("modelSpecification", "model_specification");
		inspectionReportFieldMap.put("batchNo", "batch_no");
		inspectionReportFieldMap.put("startDate", "inspection_time");
		inspectionReportFieldMap.put("endDate", "inspection_time");
		inspectionReportFieldMap.put("inspectionPoint", "inspection_point");
		inspectionReportFieldMap.put("inspectionDate", "inspection_date");
		inspectionReportFieldMap.put("inspectionPointType", "inspection_point_type");
		inspectionReportFieldMap.put("materialModel", "material_model");
		inspectionReportFieldMap.put("itemname", "name");
		inspectionReportFieldMap.put("itemdefectionCodeAttributeLevel", "defection_code_attribute_level");//不良级别,严重度
		inspectionReportFieldMap.put("itemdefectionCodeAttributeScore", "defection_code_attribute_score");//;//不良分数
		inspectionReportFieldMap.put("itemliabilityDepartment", "liability_department");//;//责任部门
		inspectionReportFieldMap.put("itempositionCodeName", "position_code_name");//部位代码名称
		inspectionReportFieldMap.put("itemdirectionCodeName", "direction_code_name");//方位代码名称
		inspectionReportFieldMap.put("itemdefectionTypeName", "defection_type_name");//不良分类名称
		inspectionReportFieldMap.put("itemdutyPart", "duty_part");//责任零部件
		inspectionReportFieldMap.put("batchNo", "batch_no");//批次号
		inspectionReportFieldMap.put("isNew", "is_new");//是否新品
		inspectionReportFieldMap.put("isTestEquipment","is_test_equipment");//是否试装
		inspectionReportFieldMap.put("isRework", "is_rework");//是否返工
		inspectionReportFieldMap.put("specialLabel", "special_label");//特殊标注
		inspectionReportFieldMap.put("yearAndMonth", "year_and_month");//年月
		inspectionReportFieldMap.put("year", "year");//年
		inspectionReportFieldMap.put("yearAndWeek", "year_and_week");//年周
		inspectionReportFieldMap.put("yearAndQuarter", "year_and_quarter");//年季度
		inspectionReportFieldMap.put("totalModel", "total_model");//统计型号
		
		inspectionReportFieldMap.put("month", "month_of_year");//月
		inspectionReportFieldMap.put("week", "week_of_year");//周
		
		//不合格品处理表的字符串对应字段
		defectiveGoodsFieldMap.put("productModel", "product_model");//产品类型
		defectiveGoodsFieldMap.put("modelSpecification", "model_specification");//产品型号
		defectiveGoodsFieldMap.put("productPhase", "product_phase");// 产品阶段
		defectiveGoodsFieldMap.put("workProcedure", "work_procedure");//工序
		defectiveGoodsFieldMap.put("partName", "part_name");// 零部件名称
		defectiveGoodsFieldMap.put("partCode", "part_code");// 零部件代号
		defectiveGoodsFieldMap.put("disposeMethod", "dispose_method");// 处理方法
		defectiveGoodsFieldMap.put("unqualifiedType", "unqualified_type");// 不合格类别
		defectiveGoodsFieldMap.put("unqualifiedSource", "unqualified_source");// 不合格来源
//		defectiveGoodsFieldMap.put("startDate", "inspection_date");
//		defectiveGoodsFieldMap.put("endDate", "inspection_date");
		defectiveGoodsFieldMap.put("itemname", "name");
	}
	/**
	  * 方法名: 处理前台选择人员组件之后登录名带等号的问题
	  * <p>功能说明：</p>
	  * @param str
	  * @return
	 */
	public static String getSimpleName(String str){
		if(StringUtils.isEmpty(str)){
			return str;
		}
		String[] names = str.split(";");
		StringBuffer sb = new StringBuffer("");
		for(String name : names){
			if(StringUtils.isNotEmpty(name)){
				if(sb.length()>0){
					sb.append(",");
				}
				if(name.indexOf("=")>0){
					sb.append(name.split("=")[1]);
				}else{
					sb.append(name);
				}
			}
		}
		return sb.toString();
	}
	
	private static Boolean erpConnectState = null;
	/**
	  * 方法名: 获取ERP的连接状态
	  * @return
	 */
	public static boolean getErpConnectState(){
		if(erpConnectState == null){
			String connectStr = PropUtils.getProp("erp.isConnect");
			if(StringUtils.isNotEmpty(connectStr)){
				erpConnectState = Boolean.valueOf(connectStr);
			}else{
				erpConnectState = Boolean.FALSE;
			}
		}
		return erpConnectState;
	}
	/**
	  * 方法名: 根据用户ID获取公司内码
	  * <p>功能说明：</p>
	  * @param userId
	  * @return
	 */
	public static Integer getCompanyISNByUser(Long userId){
		//先检查会话缓存中是否存在
		if(Struts2Utils.getRequest() != null){//表示是前台访问
			Integer companyISN = (Integer) Struts2Utils.getSession().getAttribute("ambition.companyISN");
			if(companyISN != null){
				return companyISN;
			}
		}
		List<Department> departments = ApiFactory.getAcsService().getDepartmentsByUserId(userId);
		if(departments.isEmpty()){
			return 0;
		}else{
			Department top = getTopDepartment(departments.get(0));
			Integer companyISN = Integer.valueOf(top.getCode());
			if(Struts2Utils.getRequest() != null){//表示是前台访问,加入会话缓存
				Struts2Utils.getSession().setAttribute("ambition.companyISN",companyISN);
			}
			return companyISN;
		}
	}
	private static Department getTopDepartment(Department department){
		if(department.getParent() == null){
			return department;
		}else{
			return getTopDepartment(department.getParent());
		}
	}
	/**
	  * 方法名: 获取整型的年和月
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getYearAndMonth(Calendar calendar){
		StringBuilder sb = new StringBuilder(calendar.get(Calendar.YEAR)+"");
		int month = calendar.get(Calendar.MONTH)+1;
		if(month < 10){
			sb.append("0");
		}
		sb.append(month);
		return Integer.valueOf(sb.toString());
	}
	
	/**
	  * 方法名: 获取整型的月和日
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getMonthAndDay(Calendar calendar){
		StringBuilder sb = new StringBuilder(calendar.get(Calendar.YEAR)+"");
		int month = calendar.get(Calendar.MONTH)+1;
		if(month < 10){
			sb.append("0");
		}
		sb.append(month);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if(day < 10){
			sb.append("0");
		}
		sb.append(day);
		return Integer.valueOf(sb.toString());
	}
	/**
	  * 方法名: 获取整型的年和月
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getYearAndMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return getYearAndMonth(calendar);
	}
	/**
	  * 方法名: 获取整型的年月日期
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getYearMonthAndDate(Calendar calendar){
		StringBuilder sb = new StringBuilder(calendar.get(Calendar.YEAR)+"");
		int month = calendar.get(Calendar.MONTH)+1;
		if(month < 10){
			sb.append("0");
		}
		sb.append(month);
		int date = calendar.get(Calendar.DATE);
		if(date < 10){
			sb.append("0");
		}
		sb.append(date);
		return Integer.valueOf(sb.toString());
	}
	/**
	  * 方法名: 获取整型的年月日期
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getYearMonthAndDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return getYearMonthAndDate(calendar);
	}
	/**
	  * 方法名: 获取整型的年和周
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getYearAndWeek(Calendar calendar){
		StringBuilder sb = new StringBuilder(calendar.get(Calendar.YEAR)+"");
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		if(week < 10){
			sb.append("0");
		}
		sb.append(week);
		return Integer.valueOf(sb.toString());
	}
	/**
	  * 方法名: 获取整型的年和周
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getYearAndWeek(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return getYearAndWeek(calendar);
	}
	
	/**
	  * 方法名: 组合查询语句
	  * <p>功能说明：</p>
	  * @param hql
	  * @param searchParams
	  * @param startPrefix
	 */
	public static void buildHql(JSONObject params,StringBuilder hql,List<Object> searchParams,Map<String,String> fieldMap,String startPrefix){
		//绑定查询条件
		for(Object key : params.keySet()){
			String[] properties = key.toString().split("_");
			if(properties.length<2){
				continue;
			}
			if(fieldMap!=null&&!fieldMap.containsKey(properties[0])){
				continue;
			}
			if(compareMap !=null && !compareMap.containsKey(properties[1])){
				throw new RuntimeException("比较字符串不包含" + properties[1]);
			}
			Object value = params.get(key);
			if(value==null){
				continue;
			}
			if(properties.length > 2){
				value = convertJavaObject(properties[2],value);
			}
			String start = startPrefix + ".";
			if(properties[0].startsWith("item")){
				start = "item.";
				properties[0] = properties[0].substring("item".length());
			}
			if("like".equals(properties[1])){
				hql.append(" and " + start + properties[0] + " like ? ");
				searchParams.add("%" + value + "%");
			}else if("notin".equals(properties[1])){
				hql.append(" and " + start + properties[0] + " not in ('" + value.toString().replaceAll(",","','") + "') ");
			}else if("in".equals(properties[1])){
				if(properties.length==2){
					hql.append(" and " + start + properties[0] + " in ('" + value.toString().replaceAll(",","','") + "') ");
				}
			}else{
				hql.append(" and " + start + properties[0] + " " + compareMap.get(properties[1]) + " ? ");
				searchParams.add(value);
			}
		}
	}
	
	/**
	  * 方法名: 组合查询语句
	  * <p>功能说明：</p>
	  * @param sql
	  * @param searchParams
	  * @param startPrefix
	 */
	public static void buildSql(JSONObject params,StringBuilder sql,List<Object> searchParams,Map<String,String> fieldMap,String startPrefix){
		//绑定查询条件
		for(Object key : params.keySet()){
			String[] properties = key.toString().split("_");
			if(properties.length<2){
				continue;
			}
			if(fieldMap!=null&&!fieldMap.containsKey(properties[0])){
				continue;
			}
			if(compareMap !=null && !compareMap.containsKey(properties[1])){
				throw new RuntimeException("比较字符串不包含" + properties[1]);
			}
			Object value = params.get(key);
			if(value==null){
				continue;
			}
			if(properties.length > 2){
				value = convertJavaObject(properties[2],value);
			}
			String start = startPrefix + ".";
			if(properties[0].startsWith("item")){
				start = "item.";
			}
			if("like".equals(properties[1])){
				sql.append(" and " + start + fieldMap.get(properties[0]) + " like ? ");
				searchParams.add("%" + value + "%");
			}else if("notin".equals(properties[1])){
				sql.append(" and " + start + fieldMap.get(properties[0]) + " not in ('" + value.toString().replaceAll(",","','") + "') ");
			}else if("in".equals(properties[1])){
				if(properties.length==2){
					sql.append(" and " + start + fieldMap.get(properties[0]) + " in ('" + value.toString().replaceAll(",","','") + "') ");
				}
			}else{
				sql.append(" and " + start + fieldMap.get(properties[0]) + " " + compareMap.get(properties[1]) + " ? ");
				searchParams.add(value);
			}
		}
	}
	
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	public static JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}
	
	/**
	  * 方法名: 替换工作流自动填充的处理结果
	  * <p>功能说明：</p>
	  * @param str
	  * @return
	 */
	public static String replaceTaskResult(String str){
		if(StringUtils.isEmpty(str)){
			return str;
		}
		str = str.replaceAll("transition.approval.result.agree","同意");
		str = str.replaceAll("transition.approval.result.disagree","不同意");
		return str;
	}
	
	/**
	  * 方法名: 判断字符串是否数字
	  * <p>功能说明：</p>
	  * @param val
	  * @return
	 */
	public static boolean isNumber(String val){
		return StringUtils.isNotEmpty(val) && val.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	
	/**
	  * 方法名: 获取替换后的字符串
	  * <p>功能说明：</p>
	  * @param str
	  * @return
	 */
	public static String replaceSymbols(String str){
		if(StringUtils.isEmpty(str)){
			return str;
		}
		for(String key : symbolMap.keySet()){
			str = str.replaceAll(key,symbolMap.get(key));
		}
		return str;
	}
	
	/**
     * 转换对象
     * 
     * @param type
     * @param value
     * @return
     */
    public static Object convertJavaObject(String type, Object value)
    {
        if(value==null)
        {
            return null;
        }
        else if("date".equals(type))
        {
            return DateUtil.parseDate(value.toString());
        }
        else if("datetime".equals(type))
        {
        	return DateUtil.parseDate(value.toString(),"yyyy-MM-dd HH:mm:ss");
        }
        else if("long".equals(type))
        {
            return Long.valueOf(value.toString());
        }
        else if("double".equals(type))
        {
            return Double.valueOf(value.toString());
        }
        else if("int".equals(type))
        {
            return Integer.valueOf(value.toString());
        }
        else
        {
            return value;
        }
    }
    
	private static BASE64Decoder base64Decoder = new BASE64Decoder();
	public static String BASE64DecodeToGB2312(String encodeStr){
		if(StringUtils.isEmpty(encodeStr)){
			return encodeStr;
		}
		try {
			encodeStr = encodeStr.replaceAll(" ","+");
			return new String(base64Decoder.decodeBuffer(encodeStr),"gb2312");
		} catch (Exception e) {
			throw new AmbFrameException("BASE64解码失败!",e);
		}
	}
	
	/**
	  * 方法名: 判断是否整型
	  * <p>功能说明：</p>
	  * @param str
	  * @return
	 */
	public static boolean isInteger(String str){
		if(StringUtils.isEmpty(str)){
			return false;
		}else{
			return str.matches("^[-]?[0-9]+$");
		}
	}
	
	/**
	  * 方法名: 判断是否数字
	  * <p>功能说明：</p>
	  * @param str
	  * @return
	 */
	public static boolean isDouble(String str){
		if(StringUtils.isEmpty(str)){
			return false;
		}else{
			//如果是整型,也返回true
			if(isInteger(str)){
				return true;
			}else{
				return str.matches("^[-]?[0-9]+[.]{1}[0-9]+$");
			}
		}
	}
	/**
	  * 方法名:获取单元格的值 
	  * @param cell
	  * @return
	 */
	public static Object getCellValue(Cell cell){
		if(cell == null){
			return null;
		}else if(HSSFCell.CELL_TYPE_STRING == cell.getCellType()){
			return cell.getStringCellValue();
		}else if(HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()){
			if(HSSFDateUtil.isCellDateFormatted(cell)){
				return cell.getDateCellValue();
			}
			Number number = cell.getNumericCellValue();
			if (cell.getCellStyle() != null) {
				// 处理自定义日期格式：m月d日
               String formatStr = cell.getCellStyle().getDataFormatString();
               if(cell.getCellStyle().getDataFormat() == 58
               	||(formatStr != null && formatStr.indexOf("m\"月\"d\"日\"")>-1)){
	                return org.apache.poi.ss.usermodel.DateUtil  
	                        .getJavaDate(number.doubleValue());
				}
           }
			if(number == null){
				return number;
			}else{
				return new DecimalFormat("0.####").format(number);
			}
		}else if(HSSFCell.CELL_TYPE_BLANK == cell.getCellType()){
			return "";
		}else if(HSSFCell.CELL_TYPE_BOOLEAN == cell.getCellType()){
			return cell.getBooleanCellValue();
		}else if(HSSFCell.CELL_TYPE_FORMULA == cell.getCellType()){
			Number number = cell.getNumericCellValue();
			if(number == null){
				return number;
			}else{
				return new DecimalFormat("0.####").format(number);
			}
		}else{
			return "";
		}
	}
	
	/**
	  * 方法名: 设置值
	  * <p>功能说明：</p>
	  * @param obj
	  * @param property
	  * @param value
	  * @throws Exception
	 */
	public static void setProperty(Object obj,String property,Object value) throws Exception{
		Class<?> type = PropertyUtils.getPropertyType(obj,property);
		if(type != null){
			if(value==null||StringUtils.isEmpty(value.toString())){
				PropertyUtils.setProperty(obj,property,null);
			}else{
				if(String.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,value.toString());
				}else if(Integer.class.getName().equals(type.getName()) || int.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,Integer.valueOf(value.toString()));
				}else if(Double.class.getName().equals(type.getName()) || double.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,Double.valueOf(value.toString()));
				}else if(Float.class.getName().equals(type.getName()) || float.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,Float.valueOf(value.toString()));
				}else if(Boolean.class.getName().equals(type.getName()) || boolean.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,Boolean.valueOf(value.toString()));
				}else if(Long.class.getName().equals(type.getName()) || long.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,Long.valueOf(value.toString()));
				}else if(Date.class.getName().equals(type.getName())){
					if(Date.class.getName().equals(value.getClass().getName())){
						PropertyUtils.setProperty(obj,property,value);
					}else if(String.class.getName().equals(value.getClass().getName())){
						String dateStr = value.toString().replaceAll("/","-").replaceAll("\\.","-");
						dateStr = dateStr.replaceAll("年","-").replaceAll("月","-").replaceAll("日","");
						//都是数字的情况
						if(CommonUtil1.isInteger(dateStr)&&dateStr.length()==8){
							PropertyUtils.setProperty(obj,property,DateUtil.parseDate(dateStr,"yyyyMMdd"));
						}else{
							if(dateStr.length()>10){
								PropertyUtils.setProperty(obj,property,DateUtil.parseDateTime(dateStr));
							}else{
								PropertyUtils.setProperty(obj,property,DateUtil.parseDate(dateStr));
							}
						}
					}
				}else{
					PropertyUtils.setProperty(obj,property,value);
				}
			}
		}
	}
	
	/**
	 * 判断是否角度数值
	 * @param val
	 * @return
	 */
	public static boolean isDegree(String val){
		if(StringUtils.isEmpty(val)){
			return false;
		}else{
			val = val.replaceAll("′","'");
			return val.matches("^[-+]?[0-9]+°([0-9]+')?$");
		}
	}
	
	/**
	 * 角度值转为数值
	 * @param value
	 * @return
	 */
	public static Double degreeStrToNumber(String value){
		if(!isDegree(value)){
			return null;
		}else{
			value = value.replaceAll("′","'");
			Double number = null;
			if(value.endsWith("'")){
				String ss = value.substring(value.indexOf("°")+1,value.length()-1);
				number = Integer.valueOf(ss)/60.0;
				number += Integer.valueOf(value.substring(0,value.indexOf("°")));
			}else{
				number = Double.valueOf(value.substring(0,value.indexOf("°")));
			}
			return number;
		}
	}
	
	/**
	 * 角度值转为数值
	 * @param value
	 * @return
	 */
	public static String numberToDetreeStr(Double number){
		if(number==null){
			return null;
		}else{
			String str = number.toString();
			String value = "";
			if(str.indexOf(".")>-1){
				String strs[] = str.split("\\.");
				value += strs[0] + "°";
				Double minute = (Double.valueOf("0." + strs[1]) * 60);
				Integer minuteInt = Integer.valueOf(new DecimalFormat("#").format(minute));
				if(minuteInt>0){
					value += new DecimalFormat("#").format(minute) + "'";
				}
			}else{
				value += number.intValue() + "°";
			}
			return value;
		}
	}

	/**
	  * 方法名: 获取对象可编辑的字段
	  * <p>功能说明：</p>
	  * @param cla
	  * @return
	 */
	public static List<String> getEditFieldNames(Class<?> cla){
		List<String> fieldNames = new ArrayList<String>();
		Field[] fields = cla.getDeclaredFields();
		for(Field f : fields){
			if(f.getModifiers() == 2){
				fieldNames.add(f.getName());
			}
		}
		return fieldNames;
	}
	
	/**
	  * 方法名: 对象复制
	  * <p>功能说明：</p>
	  * @param sourceObj
	  * @param targetObj
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static void copyObj(Object sourceObj,Object targetObj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		copyObj(sourceObj, targetObj,getEditFieldNames(targetObj.getClass()));
	}
	
	/**
	  * 方法名: 对象复制
	  * <p>功能说明：</p>
	  * @param sourceObj
	  * @param targetObj
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static void copyObj(Object sourceObj,Object targetObj,List<String> fieldNames) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		for(String fieldName : fieldNames){
			PropertyUtils.setProperty(targetObj,fieldName,PropertyUtils.getProperty(sourceObj, fieldName));
		}
	}
	/**
	  * 方法名:获取请求参数 
	  * @return
	 */
	public static List<JSONObject> getRequestCheckItems(){
		return getRequestCheckItems("flagIds");
	}
	/**
	  * 方法名:获取请求参数 
	  * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<JSONObject> getRequestCheckItems(String flagParamName){
		String flagIds = Struts2Utils.getParameter(flagParamName);
		if(StringUtils.isEmpty(flagIds)){
			return null;
		}
		String[] flags = flagIds.split(",");
		Map<String,JSONObject> flagMaps = new HashMap<String, JSONObject>();
		for(String flag : flags){
			if(StringUtils.isNotEmpty(flag)){
				JSONObject obj = new JSONObject();
				obj.put("flagIndex",flag.substring(1));
				flagMaps.put(flag,obj);	
			}
		}
		Map<String,String> paramMap = Struts2Utils.getRequest().getParameterMap();
		for(String key : paramMap.keySet()){
			if(key.indexOf("_")>0){
				String flag = key.substring(0,key.indexOf("_"));
				String fieldName = key.substring(key.indexOf("_")+1);
				if(flagMaps.containsKey(flag)){
					flagMaps.get(flag).put(fieldName,Struts2Utils.getParameter(key));
				}
			}
		}
		List<JSONObject> arrays = new ArrayList<JSONObject>();
		for(JSONObject json : flagMaps.values()){
			arrays.add(json);
		}
		Collections.sort(arrays,new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject o1, JSONObject o2) {
				if(o1.getInt("flagIndex")<o2.getInt("flagIndex")){
					return 0;
				}else if(o1.getInt("flagIndex")==o2.getInt("flagIndex")){
					return -1;
				}else{
					return 1;
				}
			}
		});
		return arrays;
	}
	/**
	  * 方法名: 获取映射的对象
	  * <p>功能说明：</p>
	  * @param rowNum
	  * @param objMap
	  * @param fieldMap
	  * @param tempMap
	  * @param targetClass
	  * @return
	  * @throws Exception
	 */
	public static Object getObjectByMapping(int rowNum,Map<String,Object> objMap,Map<String,String> fieldMap,Map<String,Boolean> tempMap,Class<?> targetClass) throws Exception{
		tempMap.clear(); 
		Object obj = targetClass.newInstance();
		for(String headerName : objMap.keySet()){
			String fieldName = fieldMap.get(headerName);
			Object value = objMap.get(headerName);
			Class<?> cla = PropertyUtils.getPropertyType(obj,fieldName);
			if(cla != null){
				tempMap.put(fieldName, true);
				if(value != null){
					if(cla.getName().equals(value.getClass().getName())){
						PropertyUtils.setProperty(obj,fieldName,value);
					}else{
						String valueStr = null;
						if(value instanceof Date){
							valueStr = DateUtil.formateDateStr((Date)value,"yyyy-MM-dd HH:mm");
						}else{
							valueStr = value.toString();
						}
						if(StringUtils.isEmpty(valueStr)){
							continue;
						}
						if(cla.getName().equals(Integer.class.getName())){
							if(!CommonUtil1.isInteger(valueStr)){
								throw new AmbFrameException("第"+rowNum+"行" + headerName + "【"+valueStr+"】不是有效的整数类型!");
							}
							PropertyUtils.setProperty(obj, fieldName,Integer.valueOf(valueStr));
						}else if(cla.getName().equals(Double.class.getName())){
							if(!CommonUtil1.isDouble(valueStr)){
								throw new AmbFrameException("第"+rowNum+"行" + headerName + "【"+valueStr+"】不是有效的双精度类型!");
							}
							PropertyUtils.setProperty(obj, fieldName,Double.valueOf(valueStr));
						}else if(cla.getName().equals(Date.class.getName())){
							Date date = null;
							String tempStr = valueStr.replaceAll("/","-");
							if(tempStr.length()>10){
								date = DateUtil.parseDate(tempStr, "yyyy-MM-dd HH:mm");
							}else{
								date = DateUtil.parseDate(tempStr);
							}
							if(date == null){
								throw new AmbFrameException("第"+rowNum+"行" + headerName + "【"+valueStr+"】不是有效的日期类型!");
							}
							PropertyUtils.setProperty(obj, fieldName,date);
						}else if(cla.getName().equals(String.class.getName())){
							PropertyUtils.setProperty(obj, fieldName,valueStr);
						}else{
							PropertyUtils.setProperty(obj, fieldName,value);
						}
					}
				}
			}
		}
		if(tempMap.isEmpty()){
			return null;
		}
		return obj;
	}
//	/**
//	  * 方法名: 获取查询格式的事业部编码
//	  * <p>功能说明：</p>
//	  * @return
//	 */
//	public static String getCurrentBusinessCodesForSearch(){
//		BusinessUnitManager businessUnitManager = (BusinessUnitManager)ContextUtils.getBean("businessUnitManager");
//		List<BusinessUnit> businessUnits = businessUnitManager.getCacheBusinessUnits(ContextUtils.getLoginName());
//		String codeStrs = "";
//		if(businessUnits != null){
//			for(BusinessUnit unit : businessUnits){
//				if("_ALL".equals(unit.getBusinessUnitCode())){
//					codeStrs="";
//					break;
//				}
//				if(codeStrs.length()>0){
//					codeStrs += ",";
//				}
//				codeStrs += "'" + unit.getBusinessUnitCode() + "'";
//			}
//		}else{
//			codeStrs = "'-1'";
//		}
//		return codeStrs;
//	}
//	/**
//	  * 方法名: 根据事业部编码获取事业部名称
//	  * <p>功能说明：</p>
//	  * @param businessUnitCode
//	  * @return
//	 */
//	public static String getBusinessUnitNameByCode(String businessUnitCode){
//		if(StringUtils.isEmpty(businessUnitCode)){
//			return businessUnitCode;
//		}
//		BusinessUnitManager businessUnitManager = (BusinessUnitManager)ContextUtils.getBean("businessUnitManager");
//		List<BusinessUnit> businessUnits = businessUnitManager.getCacheBusinessUnits();
//		for(BusinessUnit unit : businessUnits){
//			if(businessUnitCode.equals(unit.getBusinessUnitCode())){
//				return unit.getBusinessUnitName();
//			}
//		}
//		return "";
//	}
//	/**
//	  * 方法名: 获取当前用户拥有权限的事业部
//	  * @param List<BusinessUnit>
//	  * @return
//	 */
//	public static List<BusinessUnit> getCurrentBusinessUnits(){
//		BusinessUnitManager businessUnitManager = (BusinessUnitManager)ContextUtils.getBean("businessUnitManager");
//		List<BusinessUnit> businessUnits = businessUnitManager.getCacheBusinessUnits(ContextUtils.getLoginName());
//		if(businessUnits != null){
//			//先找出是否包含全部授权
//			boolean hasAll = false;
//			for(BusinessUnit unit : businessUnits){
//				if("_ALL".equals(unit.getBusinessUnitCode())){
//					hasAll = true;
//					break;
//				}
//			}
//			//如果有全部,取出所有的事业部
//			if(hasAll){
//				businessUnits = businessUnitManager.getCacheBusinessUnits();
//			}
//			//把不为ALL的事业返回到前台
//			List<BusinessUnit> results = new ArrayList<BusinessUnit>();
//			for(BusinessUnit unit : businessUnits){
//				if(!"_ALL".equals(unit.getBusinessUnitCode())){
//					results.add(unit);
//				}
//			}
//			return results;
//		}else{
//			return new ArrayList<BusinessUnit>();
//		}
//	}
	/**
	  * 方法名: 根据列表编码生成导出导入模版
	  * <p>功能说明：</p>
	  * @param listCode 列表编码
	  * @param templateName 模板名称
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void exportImportTemplateByExportColumns(String listCode,String templateName) throws UnsupportedEncodingException, IOException{
		try {
			Workbook book = new HSSFWorkbook();
			Sheet sheet = book.createSheet("导入模版");
			Row row = sheet.createRow(0);
			row.setHeightInPoints(14);
			ListView listView = ApiFactory.getMmsService().getListViewByCode(listCode);
			if(listView != null){
				List<ListColumn> columns = listView.getColumns();
				int columnIndex = 0;
				for(ListColumn column : columns){
					if(column.getExportable() != null && column.getTableColumn() != null 
							&& column.getExportable()){
						Cell cell = row.createCell(columnIndex++);
						cell.setCellValue(column.getHeaderName());// 给单元格赋值
					}
				}
				for(int i=0;i<columnIndex;i++){
					sheet.autoSizeColumn(i);
					int width = sheet.getColumnWidth(i);
					sheet.setColumnWidth(i, width+8*256);
				}
			}
			String fileName = templateName + ".xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(fileName).append("\"")
					.toString());
			book.write(response.getOutputStream());
		}catch (Exception e) {
			Logger.getLogger(ExcelUtil.class).error("导出模板失败!",e);
			
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/txt");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append("error.txt").append("\"")
					.toString());
			response.getOutputStream().write(("服务器错误:" + e.getMessage()).getBytes("8859_1"));
		}
	}
	/**
	  * 方法名: 获取设置为导出的列
	  * <p>功能说明：</p>
	  * @param listCode
	  * @return
	 */
	public static Map<String,String> getExportColumns(String listCode){
		Map<String,String> columnMap = new HashMap<String, String>();
		ListView listView = ApiFactory.getMmsService().getListViewByCode(listCode);
		if(listView != null){
			List<ListColumn> columns = listView.getColumns();
			for(ListColumn column : columns){
				if(column.getExportable() != null && column.getTableColumn() != null 
						&& column.getExportable()){
					columnMap.put(column.getHeaderName(),column.getTableColumn().getName());
				}
			}
		}
		return columnMap;
	}
	/**
	 * 方法名：保留n位小数(四舍五入)
	 */
	public static BigDecimal retainNDigits(double value,int n){
		BigDecimal bigDecimal = new BigDecimal(value); 
		return bigDecimal.setScale(n, BigDecimal.ROUND_HALF_UP); 
	}
	/**
	 * 导出模板
	 * @param fileName 模板名
	 * @param exportName 导出名
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
	public static void downloadTemplate(String fileName,String exportName) throws Exception{
		InputStream inputStream = null;
		try {
			//找到模板
			inputStream = ExcelUtil.class.getClassLoader().getResourceAsStream("template/report/"+fileName);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			
			if(StringUtils.isEmpty(exportName)){
				exportName = fileName;
			}
			byte byname[] = exportName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(fileName).append("\"")
					.toString());
			response.getOutputStream().write(bytes);
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
	}
	/**
	  * 方法名: 获取整型的年
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getYear(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return getYear(calendar);
	}
	
	/**
	  * 方法名: 获取整型的年
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getYear(Calendar calendar){
		StringBuilder sb = new StringBuilder(calendar.get(Calendar.YEAR)+"");
		return Integer.valueOf(sb.toString());
	}
	
	/**
	  * 方法名: 获取整型的月
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return getMonth(calendar);
	}
	
	/**
	  * 方法名: 获取整型的月
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getMonth(Calendar calendar){
		StringBuilder sb = new StringBuilder("");
		int month = calendar.get(Calendar.MONTH)+1;
		if(month < 10){
			sb.append("0");
		}
		sb.append(month);
		return Integer.valueOf(sb.toString());
	}
	/**
	  * 方法名: 获取整型的日
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return getDate(calendar);
	}
	
	/**
	  * 方法名: 获取整型的日
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getDate(Calendar calendar){
		StringBuilder sb = new StringBuilder("");
		int date = calendar.get(Calendar.DATE);
		if(date < 10){
			sb.append("0");
		}
		sb.append(date);
		return Integer.valueOf(sb.toString());
	}
	
	/**
	  * 方法名: 获取整型的周
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getWeek(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return getWeek(calendar);
	}
	
	/**
	  * 方法名: 获取整型的周
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getWeek(Calendar calendar){
		StringBuilder sb = new StringBuilder("");
		int date = calendar.get(Calendar.WEEK_OF_YEAR);
		if(date < 10){
			sb.append("0");
		}
		sb.append(date);
		return Integer.valueOf(sb.toString());
	}
	
	/**
	  * 方法名: 获取整型的季
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getQuarter(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return getQuarter(calendar);
	}
	
	/**
	  * 方法名: 获取整型的季
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getQuarter(Calendar calendar){
		StringBuilder sb = new StringBuilder("");
		int month = calendar.get(Calendar.MONTH)+1;
		int quarter = month <= 3 ? 1 : month <= 6 ? 2 : month <= 9 ? 3 : 4;
		sb.append("0"+quarter);
		return  Integer.valueOf(sb.toString());
	}
	/**
	  * 方法名: 获取整型的年和季
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getYearAndQuarter(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return getYearAndQuarter(calendar);
	}
	
	/**
	  * 方法名: 获取整型的年和季
	  * <p>功能说明：</p>
	  * @param calendar
	  * @return
	 */
	public static Integer getYearAndQuarter(Calendar calendar){
		StringBuilder sb = new StringBuilder(calendar.get(Calendar.YEAR)+"");
		int month = calendar.get(Calendar.MONTH)+1;
		int quarter = month <= 3 ? 1 : month <= 6 ? 2 : month <= 9 ? 3 : 4;
		sb.append("0"+quarter);
		return Integer.valueOf(sb.toString());
	}
	/**
	 * 获取事业部部门
	 * @param department
	 * @return
	 */
	public static Department getTrueBusinessUnits(Department department){
		if (StringUtils.isNotEmpty(department.getSummary()) && 
				(department.getSummary().trim().contains("事业部") || 
						department.getSummary().trim().toUpperCase().contains("BUSINESSUNIT"))) {
			return department;
		}
		return department.getParent() == null?null:getTrueBusinessUnits(department.getParent());
	}
	/**
	 * 获取有权限的所有事业部(字符串)
	 * @return
	 */
	public static String getBusinessUnitsString(){
		List<Department> departmentsByUserId = ApiFactory.getAcsService().getDepartmentsByUserId(ContextUtils.getUserId());
		String businessUnitsString = "";
		for (Department department : departmentsByUserId) {
			department = getTrueBusinessUnits(department);
			if(department != null){
				businessUnitsString += "," + department.getName();
			}
		}
		/*if(StringUtils.isEmpty(businessUnitsString)){
			List<Department> departments = ApiFactory.getAcsService().getAllDepartments();
			for (Department department : departments) {
				if(StringUtils.isEmpty(department.getSummary())){
					continue;
				}
				if(department.getSummary().trim().contains("事业部") || department.getSummary().trim().toUpperCase().contains("BUSINESSUNIT")){
					businessUnitsString += "," + department.getName();
				}
			}
		}*/
    	return StringUtils.isNotEmpty(businessUnitsString)?businessUnitsString.replaceFirst(",", ""):null;
	}

	/**
	  * 方法名: 是否可以初始化同步集成
	  * @return
	 */
	private static Boolean canInitializeSynchronization = null;
	public static boolean canInitializeSynchronization(){
		if(canInitializeSynchronization == null){
			String integrationCanInitializeSynchronization = PropUtils.getProp("erp.isConnect");
			if(StringUtils.isNotEmpty(integrationCanInitializeSynchronization)){
				canInitializeSynchronization = Boolean.valueOf(integrationCanInitializeSynchronization);
			}else{
				canInitializeSynchronization = Boolean.FALSE;
			}
		}
		return canInitializeSynchronization;
	}
}	
