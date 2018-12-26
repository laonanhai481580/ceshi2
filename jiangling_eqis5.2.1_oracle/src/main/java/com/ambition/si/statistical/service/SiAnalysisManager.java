package com.ambition.si.statistical.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.cost.entity.Composing;
import com.ambition.cost.entity.CostRecord;
import com.ambition.si.checkinspection.dao.SiCheckInspectionReportDao;
import com.ambition.si.entity.SiCheckInspectionReport;
import com.ambition.si.entity.SiCheckItem;
import com.ambition.si.statistical.dao.SiAnalysisDao;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.date.DateUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.opensymphony.xwork2.ActionContext;

/**
 * SI报告统计表MANAGER
 * 
 * @author LPF
 * 
 */
@Service
@Transactional
public class SiAnalysisManager {
	public SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-mm-dd hh:mm:ss");
	@Autowired
	private SiAnalysisDao siAnalysisDao;
	@Autowired
	private SiCheckInspectionReportDao siCheckInspectionReportDao;
	@Autowired
	private LogUtilDao logUtilDao;

	public SiAnalysisDao getSiAnalysisDao() {
		return siAnalysisDao;
	}

	public void setSiAnalysisDao(SiAnalysisDao siAnalysisDao) {
		this.siAnalysisDao = siAnalysisDao;
	}

	public LogUtilDao getLogUtilDao() {
		return logUtilDao;
	}

	public void setLogUtilDao(LogUtilDao logUtilDao) {
		this.logUtilDao = logUtilDao;
	}
	/*public  List<Map<String,Object>> getItemsForAll(JSONObject params) {
		Date startDate = DateUtil.parseDate(params.getString("startDate"));
		Date endDate = DateUtil.parseDate(params.getString("endDate"));
		String groupStr = params.getString("group");
		String dateField = "";
		if("day".equals(groupStr)){
			dateField = "to_char(c.inspection_date,'YYYY-MM-DD')";
		}else{
			dateField = "to_char(c.inspection_date,'yyyy-mm')";
		}
		String sql ="select " + dateField + " from SI_CHECK_INSPECTION_REPORT c  " +
				" where c.inspection_date between ? and ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(startDate);
		searchParams.add(endDate);
		if(params.containsKey("processSection")){
			String processSection = params.getString("processSection");
			if(StringUtils.isNotEmpty(processSection)){
				sql += " and  c.process_section = ?";
				searchParams.add(processSection);
			}
		}
		sql += " group by " + dateField + "  order by " + dateField+" ";
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		Map<String,Map<String,Object>> dateValueMap = new HashMap<String, Map<String,Object>>();
		List<Object> dateList = siCheckInspectionReportDao.findBySql(sql,searchParams.toArray());
		List<String> categories = new ArrayList<String>();
		Map<String,Object> typeMap = new HashMap<String, Object>();
		for(Object obj : dateList){
			if(!categories.contains(obj.toString())){
				categories.add(obj.toString());
			}
		}
		for (int i = 0; i < categories.size(); i++) {
			String hql="";
			List<Object> searchParams1 = new ArrayList<Object>();
			if("day".equals(groupStr)){
				hql="select c from SiCheckInspectionReport c where to_char(c.inspectionDate,'yyyy-MM-dd') =?";
			}else{
				hql="select c from SiCheckInspectionReport c where to_char(c.inspectionDate,'yyyy-MM') =?";
			}
			searchParams1.add(categories.get(i));
			if(params.containsKey("processSection")){
				String processSection = params.getString("processSection");
				if(StringUtils.isNotEmpty(processSection)){
					sql += " and  c.processSection = ?";
					searchParams1.add(processSection);
				}
			}
			Query query=null; 
			try {
				 query = siCheckInspectionReportDao.getSession().createQuery(hql.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			for(int j=0;j<searchParams1.size();j++){
				query.setParameter(j, searchParams1.get(j));
			}
			@SuppressWarnings("unchecked")
			List<SiCheckInspectionReport>	list = query.list();
			Map<String,Object> map = new HashMap<String, Object>();
			for (SiCheckInspectionReport report : list) {				
				List<SiCheckItem> checkItems=report.getCheckItems();
				for (SiCheckItem item : checkItems) {	
					String str=item.getDefectionTypeName()+"_"+item.getDefectionCodeName();
					if(map.containsKey(str)){
						Integer value=Integer.valueOf(map.get(str).toString())+(item.getValue()==null?0:item.getValue());
						map.put(str, value);
					}
					map.put(str, item.getValue()==null?0:item.getValue());
				}				
			}
			dateValueMap.put(categories.get(i),map);
		}
		return returnList;
		
	}*/
	public  Map<String,Object>  getItemsForAll(JSONObject params) {
		Date startDate = DateUtil.parseDate(params.getString("startDate"));
		Date endDate = DateUtil.parseDate(params.getString("endDate"));
		String groupStr = params.getString("group");
		String dateField = "";
		if("day".equals(groupStr)){
			dateField = "to_char(c.inspection_date,'YYYY-MM-DD')";
		}else if("week".equals(groupStr)){
			dateField = "to_char(c.inspection_date,'yyyy_iw')";
		}else if("month".equals(groupStr)){
			dateField = "to_char(c.inspection_date,'yyyy-mm')";
		}else if("quarter".equals(groupStr)){
			dateField = "to_char(c.inspection_date,'yyyy_q')";
		}else if("year".equals(groupStr)){
			dateField = "to_char(c.inspection_date,'yyyy')";
		}
		String sql ="select " + dateField + " from SI_CHECK_INSPECTION_REPORT c  " +
				" where c.inspection_date between ? and ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(startDate);
		searchParams.add(endDate);
		if(params.containsKey("processSection")){
			String processSection = params.getString("processSection");
			if(StringUtils.isNotEmpty(processSection)){
				sql += " and  c.process_section = ?";
				searchParams.add(processSection);
			}
		}
		sql += " group by " + dateField + "  order by " + dateField+" ";
		//List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		List<Object> dateList = siCheckInspectionReportDao.findBySql(sql,searchParams.toArray());
		List<String> categories = new ArrayList<String>();
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Set<String>> itemMap = new HashMap<String, Set<String>>();
		for(Object obj : dateList){
			if(!categories.contains(obj.toString())){
				categories.add(obj.toString());
			}
		}
		for (int i = 0; i < categories.size(); i++) {
			String hql="";
			List<Object> searchParams1 = new ArrayList<Object>();
			if("day".equals(groupStr)){
				hql="select c.value ,c.defectionTypeName,c.defectionCodeName from SiCheckItem c where to_char(c.inspectionDate,'yyyy-MM-dd') =?";
			}else if("week".equals(groupStr)){
				hql="select c.value,c.defectionTypeName,c.defectionCodeName from SiCheckItem c where to_char(c.inspectionDate,'yyyy_iw') =?";
			}else if("month".equals(groupStr)){
				hql="select c.value,c.defectionTypeName,c.defectionCodeName from SiCheckItem c where to_char(c.inspectionDate,'yyyy-MM') =?";
			}else if("quarter".equals(groupStr)){
				hql="select c.value,c.defectionTypeName,c.defectionCodeName from SiCheckItem c where to_char(c.inspectionDate,'yyyy_q') =?";
			}else if("year".equals(groupStr)){
				hql="select c.value,c.defectionTypeName,c.defectionCodeName from SiCheckItem c where to_char(c.inspectionDate,'yyyy') =?";
			}
			searchParams1.add(categories.get(i));
			if(params.containsKey("businessUnitCode")){
				String businessUnitCode = params.getString("businessUnitCode");
				if(StringUtils.isNotEmpty(businessUnitCode)){
					sql += " and  c.businessUnitCode = ?";
					searchParams.add(businessUnitCode);
				}
			}
			if(params.containsKey("customer")){
				String customer = params.getString("customer");
				if(StringUtils.isNotEmpty(customer)){
					sql += " and  c.customer = ?";
					searchParams.add(customer);
				}
			}
			if(params.containsKey("machineType")){
				String machineType = params.getString("machineType");
				if(StringUtils.isNotEmpty(machineType)){
					sql += " and  c.machineType = ?";
					searchParams.add(machineType);
				}
			}
			Query query=null; 
			try {
				 query = siCheckInspectionReportDao.getSession().createQuery(hql.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			for(int j=0;j<searchParams1.size();j++){
				query.setParameter(j, searchParams1.get(j));
			}
			List<?> list = query.list();			
			for (Object obj : list) {
				Object objs[] = (Object[])obj;
				Integer value=0;
				if(objs[1]==null||objs[2]==null){
					continue;
				}
				if(objs[0]!=null){
					value=Integer.valueOf(objs[0].toString());
				}
				String type=objs[1].toString();
				String name=objs[2].toString();
				if(itemMap.containsKey(type)){
					Set<String> set=itemMap.get(type);
					set.add(name);
				}else{
					Set<String> set=new HashSet<String>();
					set.add(name);
					itemMap.put(type, set);
				}
				if(!map.containsKey(type)){
					map.put(type,value);
				}else{
					Integer typeValue=Integer.valueOf(map.get(type).toString())+value;
					map.put(type, typeValue);
				}
				if(!map.containsKey(type+"_"+name)){
					map.put(type+"_"+name, value);
					if(!map.containsKey(type+"_rows")){
						map.put(type+"_rows", 1);
					}else{
						Integer rowValue=Integer.valueOf(map.get(type+"_rows").toString());
						map.put(type+"_rows",rowValue+ 1);
					}
				}else{
					Integer codeValue=Integer.valueOf(map.get(type+"_"+name).toString())+value;
					map.put(type+"_"+name, codeValue);
				}	
				if(!map.containsKey(type+"_"+name+"_"+categories.get(i))){
					map.put(type+"_"+name+"_"+categories.get(i), value);
				}else{
					Integer dateValue=Integer.valueOf(map.get(type+"_"+name+"_"+categories.get(i)).toString())+value;
					map.put(type+"_"+name+"_"+categories.get(i), dateValue);
				}	
			}
		}
		map.put("itemMap", itemMap);
		return map;
		
	}
	public Map<String,Map<String,Double>> getAmountsForAll(JSONObject params) {
		Date startDate = DateUtil.parseDate(params.getString("startDate"));
		Date endDate = DateUtil.parseDate(params.getString("endDate"));
		String groupStr = params.getString("group");
		String dateField = "";
		if("day".equals(groupStr)){
			dateField = "to_char(c.inspection_date,'YYYY-MM-DD')";
		}else if("week".equals(groupStr)){
			dateField = "to_char(c.inspection_date,'yyyy_iw')";
		}else if("month".equals(groupStr)){
			dateField = "to_char(c.inspection_date,'yyyy-mm')";
		}else if("quarter".equals(groupStr)){
			dateField = "to_char(c.inspection_date,'yyyy_q')";
		}else if("year".equals(groupStr)){
			dateField = "to_char(c.inspection_date,'yyyy')";
		}		
		//统计字段
		String totalSql = "sum(c.stock_amount) as stockAmount,sum(c.appearance_inspection_amount) as appearanceAmount,sum(c.function_inspection_amount) as functionAmount,sum(c.size_inspection_amount) as sizeAmount,sum(c.inspection_lot_amount) as inspectionLotAmount,sum(c.pass_lot_amount) as passLotAmount,sum(c.reject_lot_amount) as rejectLotAmount,sum(c.appearance_un_amount) as appearanceUnAmount,sum(c.size_un_amount) as sizeUnAmount,sum(c.function_un_amount) as functionUnAmount";
		String sql = "select " + dateField + ","+totalSql+"  from SI_CHECK_INSPECTION_REPORT c  " +
				" where c.inspection_date between ? and ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(startDate);
		searchParams.add(endDate);
		if(params.containsKey("businessUnitCode")){
			String businessUnitCode = params.getString("businessUnitCode");
			if(StringUtils.isNotEmpty(businessUnitCode)){
				sql += " and  c.business_unit_code = ?";
				searchParams.add(businessUnitCode);
			}
		}
		if(params.containsKey("customer")){
			String customer = params.getString("customer");
			if(StringUtils.isNotEmpty(customer)){
				sql += " and  c.customer = ?";
				searchParams.add(customer);
			}
		}
		if(params.containsKey("machineType")){
			String machineType = params.getString("machineType");
			if(StringUtils.isNotEmpty(machineType)){
				sql += " and  c.machine_type = ?";
				searchParams.add(machineType);
			}
		}
		sql += " group by " + dateField + "  order by " + dateField+" ";
		List<?> list = siCheckInspectionReportDao.findBySql(sql,searchParams.toArray());
		JSONArray categories = new JSONArray();
		Map<String,Map<String,Double>> dateValueMap = new LinkedHashMap<String, Map<String,Double>>();
		for(Object obj : list){
			Object objs[] = (Object[])obj;
			Map<String,Double> map=new HashMap<String,Double>();
			//投入数
			int stockAmount=0;
			if(objs[1]!=null){
				stockAmount=Integer.valueOf(objs[1].toString());
			}
			map.put("stockAmount", (double)stockAmount);
			//外观检验数
			int appearanceAmount=0;
			if(objs[2]!=null){
				appearanceAmount=Integer.valueOf(objs[2].toString());
			}
			map.put("appearanceAmount", (double)appearanceAmount);
			//功能检验数
			int functionAmount=0;
			if(objs[3]!=null){
				functionAmount=Integer.valueOf(objs[3].toString());
			}
			map.put("functionAmount", (double)functionAmount);
			//尺寸检验数
			int sizeAmount=0;
			if(objs[3]!=null){
				sizeAmount=Integer.valueOf(objs[3].toString());
			}
			map.put("sizeAmount", (double)sizeAmount);
			//投入Lot数
			int stockLotAmount=0;
			if(objs[4]!=null){
				stockLotAmount=Integer.valueOf(objs[4].toString());
			}
			map.put("stockLotAmount", (double)stockLotAmount);
			//检验Lot数
			int inspectionLotAmount=0;
			if(objs[5]!=null){
				inspectionLotAmount=Integer.valueOf(objs[5].toString());
			}
			map.put("inspectionLotAmount", (double)inspectionLotAmount);
			//pass Lot数
			int passLotAmount=0;
			if(objs[6]!=null){
				passLotAmount=Integer.valueOf(objs[6].toString());
			}
			map.put("passLotAmount", (double)passLotAmount);
			//reject Lot数	
			Double rejectLotAmount=0.0;
			if(objs[7]!=null){
				rejectLotAmount=Double.valueOf(objs[7].toString());
			}
			map.put("rejectLotAmount",rejectLotAmount);
			//批退率
			Double lrrRate=0.0;
			if(inspectionLotAmount!=0){
				lrrRate=rejectLotAmount*100/inspectionLotAmount;
			}
			if(lrrRate>0){
				BigDecimal  a=new  BigDecimal(lrrRate);  
				lrrRate=a.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();  
			}			
			map.put("lrrRate", lrrRate);
			//外观不良数		
			Double appearanceUnAmount=0.0;
			if(objs[8]!=null){
				appearanceUnAmount=Double.valueOf(objs[8].toString());
			}
			map.put("appearanceUnAmount", appearanceUnAmount);
			//尺寸不良数	
			Double sizeUnAmount=0.0;
			if(objs[9]!=null){
				sizeUnAmount=Double.valueOf(objs[9].toString());
			}
			map.put("sizeUnAmount",sizeUnAmount);
			//功能不良数		
			Double functionUnAmount=0.0;
			if(objs[10]!=null){
				functionUnAmount=Double.valueOf(objs[10].toString());
			}
			map.put("functionUnAmount",functionUnAmount);
			//外观不良率
			Double appearanceUnRate=0.0;
			if(appearanceAmount!=0){
				appearanceUnRate=appearanceUnAmount*100/appearanceAmount;
			}
			if(appearanceUnRate>0){
				BigDecimal  a=new  BigDecimal(appearanceUnRate);  
				appearanceUnRate=a.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();  
			}				
			map.put("appearanceUnRate", appearanceUnRate);
			//尺寸不良率
			Double sizeUnRate=0.0;
			if(sizeAmount!=0){
				sizeUnRate=sizeUnAmount*100/sizeAmount;
			}
			if(sizeUnRate>0){
				BigDecimal  a=new  BigDecimal(sizeUnRate);  
				sizeUnRate=a.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();  
			}				
			map.put("sizeUnRate", sizeUnRate);
			//功能不良率
			Double functionUnRate=0.0;
			if(functionAmount!=0){
				functionUnRate=functionUnAmount*100/functionAmount;
			}
			if(functionUnRate>0){
				BigDecimal  a=new  BigDecimal(functionUnRate);  
				functionUnRate=a.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();  
			}				
			map.put("functionUnRate", functionUnRate);
			//统计周期
			String dateStr = objs[0]+"";
			if(!categories.contains(dateStr)){
				categories.add(dateStr);
			}
			//日期的值
			if(!dateValueMap.containsKey(dateStr)){
				dateValueMap.put(dateStr,map);
			}else{
				Map<String,Double> map1=dateValueMap.get(dateStr);
				map1.put("stockAmount", map1.get("stockAmount")+stockAmount);
				map1.put("appearanceAmount", map1.get("appearanceAmount")+appearanceAmount);
				map1.put("functionAmount", map1.get("functionAmount")+functionAmount);
				map1.put("sizeAmount", map1.get("sizeAmount")+functionAmount);
				dateValueMap.put(dateStr, map1);
			}
		}
		return dateValueMap;
	}
}
