package com.ambition.cost.composingdetail.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.cost.composingdetail.dao.ComposingDao;
import com.ambition.cost.entity.Composing;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;

@Service
@Transactional
public class ComposingManager {
	@Autowired
	private ComposingDao composingDao;
	
//	@Autowired
//	private LogUtilDao logUtilDao;
	
	/**
	 * 检查是否存在相同名称的质量成本
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Boolean isExistComposing(Long id,String code,String name,Composing parent){
		String hql = "select count(*) from Composing c where c.companyId = ? and c.code = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(code);
		if(parent == null){
			hql += " and c.parent is null ";
		}else{
			hql += " and c.parent = ?";
			params.add(parent);
		}
		if(id != null){
			hql += " and c.id <> ?";
			params.add(id);
		}
		Query query = composingDao.getSession().createQuery(hql);
		for(int i=0;i<params.size();i++){
			query.setParameter(i,params.get(i));
		}
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	public Composing getComposing(Long id){
		return composingDao.get(id);
	}
	/**
	 * 保存质量成本
	 * @param composing
	 */
	public void saveComposing(Composing composing){
		if(StringUtils.isEmpty(composing.getCode())){
			throw new RuntimeException("编码不能为空!");
		}
		if(StringUtils.isEmpty(composing.getName())){
			throw new RuntimeException("名称不能为空!");
		}
		if(isExistComposing(composing.getId(),composing.getCode(),composing.getName(),composing.getParent())){
			throw new RuntimeException("已经相同的编码或名称!");
		}
		composingDao.save(composing);
	}
	
	/**
	 * 删除质量成本
	 * @param id
	 */
	public void deleteComposing(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			Composing composing = composingDao.get(Long.valueOf(id));
			if(composing.getId() != null){
				if(!composing.getChildren().isEmpty()){
					throw new RuntimeException("还有子节点不能删除，请先删除子节点!");
				}
				composingDao.delete(composing);
			}
		}
	}
	
	/**
	 * 获取质量成本表的一级科目
	 * @return
	 */
	public List<Composing> getTopComposingList(String flag){
		List<Composing> topComposings = composingDao.getTopComposing();
		Map<String,Boolean> topMap = new HashMap<String, Boolean>();
		//没有全部时,不显示预防成本和鉴定成本
		for(Composing composing : topComposings){
			topMap.put(composing.getCode(),true);
		}
		List<Option> topComposingOptions = ApiFactory.getSettingService().getOptionsByGroupCode("cost_topComposing");
		int firstIndex = topComposingOptions.size();
		for(Option option : topComposingOptions){
			if(!topMap.containsKey(option.getValue())){
				Composing composing = new Composing();
				composing.setCompanyId(ContextUtils.getCompanyId());
				composing.setCreatedTime(new Date(System.currentTimeMillis()));
				composing.setCreator(ContextUtils.getUserName());
				composing.setModifiedTime(new Date(System.currentTimeMillis()));
				composing.setModifier(ContextUtils.getUserName());
				composing.setName(option.getName());
				composing.setCode(option.getValue());
				composing.setDengji(1);
				composing.setOrderNum(firstIndex++);
				composingDao.save(composing);
				topComposings.add(composing);
			}
		}
		List<Composing> newComposings = new ArrayList<Composing>();
		flag = StringUtils.isEmpty(flag)?"LBCB,WBCB":flag;
		for( Composing composing : topComposings){
			if("all".equals(flag)){
				newComposings.add(composing);
			}else{
				if(("," + flag + ",").indexOf("," + composing.getCode() + ",")>-1){
					newComposings.add(composing);
				}
			}
		}
		return newComposings;
	}
	
	/**
	 * 查询一级科目
	 * @return
	 */
	public Map<String,Object> getTopComposings(){
		List<Composing> composings = composingDao.getTopComposing();
		return convertComposings(composings);
	}
	
	/**
	 * 查询二级科目
	 * @return
	 */
	public Map<String,Object> getSecondComposings(){
		List<Composing> composings = composingDao.getComposingByLevel(2);
		return convertComposings(composings);
	}
	
	/**
	 * 查询质量成本科目
	 * @return
	 */
	public Map<String,Object> getThirdComposings(){
		List<Composing> composings = composingDao.getComposingByLevel(3);
		return convertComposings(composings);
	}
	public List<Composing> getThirdLevels(){
		List<Composing> composings = composingDao.getComposingByLevel(2);
		return composings;
	}
    /**
     * 方法名: 
     * <p>功能说明：</p>
     * @param calendar
     * @return
    */
	public List<Option> converThirdLevelToList(List<Composing> composings){
	   List<Option> options = new ArrayList<Option>();
	   for(Composing composing : composings){
	       Option option = new Option();
	       String name = composing.getName().toString();
	       String value = composing.getName().toString();
	       option.setName(name==null?"":name.replaceAll("\n","").replaceAll(",","，"));
	       option.setValue(value==null?"":value.replaceAll("\n","").replaceAll(",","，"));
	       options.add(option);
	   }
	   return options;
	}
	/**
	 * 转换质量成本
	 * @param composings
	 * @return
	 */
	private Map<String,Object> convertComposings(List<Composing> composings){
		Map<String,Object> map = new HashMap<String, Object>();
		for(Composing composing : composings){
			map.put(composing.getCode(),composing.getName());
		}
		return map;
	}
	
	// 封装不良细项结果数据集的JSON格式
	public String getResultJson(Page<Object> page) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (Object o : page.getResult()) {
			HashMap<String, Object> hs = new HashMap<String, Object>();
			StringBuffer sb = new StringBuffer();
			sb.append(JsonParser.object2Json(o));
			sb.delete(sb.length() - 1, sb.length());
			sb.append(",");
			sb.append(JsonParser.object2Json(hs).substring(1,JsonParser.object2Json(hs).length()));
			JSONObject jObject = JSONObject.fromObject(sb.toString());
			list.add(jObject);
		}
		// 添加jqGrid所需的页信息
		StringBuilder json = new StringBuilder();
		json.append("{\"page\":\"");
		json.append(page.getPageNo());
		json.append("\",\"total\":");
		json.append(page.getTotalPages());
		json.append(",\"records\":\"");
		json.append(page.getTotalCount());
		json.append("\",\"rows\":");
		json.append(JSONArray.fromObject(list).toString());
		json.append("}");
		return json.toString();
	}
	
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	public JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}else{
			for(Object key : params.keySet()){
				resultJson.put(key,params.getJSONArray(key.toString()).get(0));
			}
			return resultJson;
		}
	}
	
	public Integer getYearAndMonthNumber(Calendar calendar){
		StringBuffer sb = new StringBuffer("");
		sb.append(calendar.get(Calendar.YEAR));
		if(calendar.get(Calendar.MONTH)+1<10){
			sb.append("0" + (calendar.get(Calendar.MONTH)+1));
		}else{
			sb.append(calendar.get(Calendar.MONTH)+1);
		}
		return Integer.valueOf(sb.toString());
	}
	
	public List<Map<String,Object>> getComposingsForTotal(JSONObject params) {
		Integer startMonth = Integer.valueOf(params.getString("startDate").replaceAll("-",""));
		Integer endMonth = Integer.valueOf(params.getString("endDate").replaceAll("-",""));
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(startMonth);
		searchParams.add(endMonth);
		String sql = " select level_two_name,level_three_name,SUM(value),SUM(value) as value from COST_COST_RECORD where level_two_name is not null and occurring_month between ? and ?";
		String levelTwoName = null,name=null,itemGroup=null,customerName=null,project=null,dutyDepart=null,feeState=null,companyName=null;
		if(params.containsKey("levelTwoName")){
			levelTwoName = params.getString("levelTwoName");
			if(StringUtils.isNotEmpty(levelTwoName)){
				sql += " and level_two_name like ?";
				searchParams.add("%" + levelTwoName + "%");
			}
		}
		if(params.containsKey("businessUnitCode")){
			String businessUnitCode = params.getString("businessUnitCode");
			if(StringUtils.isNotEmpty(businessUnitCode)){
				sql += " and  business_unit_code = ?";
				searchParams.add(businessUnitCode);
			}
		}
		if(params.containsKey("companyName")){
			companyName = params.getString("companyName");
			if(StringUtils.isNotEmpty(companyName)){
				sql += " and company_Name = ?";
				searchParams.add(companyName);
			}
		}
		sql += "  group by level_two_name,level_three_name";
		Query query = composingDao.getSession().createSQLQuery(sql);
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i,searchParams.get(i));
		}
        List<?> results = query.list();
		Map<String,Double> valueMap = new HashMap<String, Double>();
		Map<String,Double> budgetValueMap = new HashMap<String, Double>();
		DecimalFormat df = new DecimalFormat("#.00");
		for(Object obj : results){
			Object[] objs = (Object[])obj;
			if(objs[0]==null||objs[1]==null||objs[2]==null||objs[3]==null){
				continue;
			}
			//成本金额
			Double value = Double.valueOf(df.format(Double.valueOf(objs[2]+"")));
			valueMap.put(objs[0]+"_" + objs[1],value);
			//预算金额
			budgetValueMap.put(objs[0]+"_" + objs[1],Double.valueOf(objs[3]+""));
		}
		
		//
		List<Composing> composings = getTopComposingList("all");
		List<Composing> downItems = new ArrayList<Composing>();
		for(Composing composing : composings){
//			boolean needShow = true;
//			if(StringUtils.isNotEmpty(levelTwoName)){
//				needShow = false;
//				if(composing.getName()!=null&&composing.getName().indexOf(levelTwoName)>-1){
//					needShow = true;
//				}
//			}
//			if(needShow){
				downItems.addAll(getDownItems(composing));
//			}
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Double totalValue = 0.0d,totalBudgetValue=0.0d;
		for(Composing composing : downItems){
//			boolean needShow = true;
//			if(StringUtils.isNotEmpty(name)){
//				needShow = false;
//				if(composing.getName()!=null&&composing.getName().indexOf(name)>-1){
//					needShow = true;
//				}
//			}
//			if(!needShow){
//				continue;
//			}
			Map<String,Object> map = new HashMap<String, Object>();
			String key = composing.getCode();
			if(composing.getParent() != null){
				key = composing.getParent().getCode() + "_" + key;
			}else{
			    key = key + "_" + key;
			}
			map.put("code",key);
			map.put("unit",composing.getUnit()==null?"":composing.getUnit());
			if(valueMap.containsKey(key)){
				totalValue += valueMap.get(key);
			}
			if(budgetValueMap.containsKey(key)){
				totalBudgetValue += budgetValueMap.get(key);
			}
			map.put("level_two_rowspan",1);
			map.put("level_three_rowspan",1);
			map.put("level_two_total",0.0d);
			map.put("level_two_total_budget",0.0d);
			map.put("level_three_total",0.0d);
			map.put("level_three_total_budget",0.0d);
			if(composing.getParent() == null){
				map.put("level_two_name",composing.getName());
				map.put("level_three_name","");
			}else{
				Composing parent = composing.getParent();
				map.put("level_two_name",parent.getName());
				map.put("level_three_name",composing.getName());
			}
			list.add(map);
		}
		Map<String,Object> lastLevelTwoMap = null;
		for(Map<String,Object> map : list){
			String code = map.get("code")+"";
			map.put("total",totalValue);
			map.put("totalBudget",totalBudgetValue);
			if(lastLevelTwoMap == null){
				lastLevelTwoMap = map;
			}else{
				if(map.get("level_two_name").equals(lastLevelTwoMap.get("level_two_name"))){
					lastLevelTwoMap.put("level_two_rowspan",(Integer)lastLevelTwoMap.get("level_two_rowspan")+1);
					map.remove("level_two_name");
				}else{
					lastLevelTwoMap = map;
				}
			}
			
			if(valueMap.containsKey(code)){
				Double value = valueMap.get(code);
				Double budgetValue = budgetValueMap.get(code);
				if(lastLevelTwoMap != null){
					Double val = (Double)lastLevelTwoMap.get("level_two_total") +value;
					lastLevelTwoMap.put("level_two_total",val);
					Double budgetVal = (Double)lastLevelTwoMap.get("level_two_total_budget") +budgetValue;
					lastLevelTwoMap.put("level_two_total_budget",budgetVal);
				}
				if(StringUtils.isNotEmpty((String)map.get("level_three_name"))){
					map.put("level_three_total",value);
					map.put("level_three_total_budget",budgetValue);
				}
			}
		}
		return list;
	}
	public  List<Map<String,Object>> getComposingsForAll(JSONObject params) {
		Integer startMonth = Integer.valueOf(params.getString("startDate").replaceAll("-",""));
		Integer endMonth = Integer.valueOf(params.getString("endDate").replaceAll("-",""));					//
			List<Composing> composings = getTopComposingList("all");
			List<Composing> downItems = new ArrayList<Composing>();
			Map<String,Double> valueMap = new HashMap<String, Double>();
			for(Composing composing : composings){
				downItems.addAll(getDownItems(composing));
			}
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Double allValue=0.0;
			for(Composing composing : downItems){
				Map<String,Object> map = new HashMap<String, Object>();
				String key = composing.getCode();
				if(composing.getParent()!=null){
					key = composing.getParent().getCode() + "_" + key;
				}
					Double totalValue=0.0;
					for (int k = startMonth; k <= endMonth; k++) {
						List<Object> searchParams = new ArrayList<Object>();
						searchParams.add(composing.getCode());
						searchParams.add(k);						
						String sql1 = " select value from COST_COST_RECORD where level_three_name = ? and occurring_month=? ";
						String sql2 = " select value from COST_COST_RECORD where level_two_name = ? and occurring_month=? ";
						String levelTwoName = null;
						if(params.containsKey("levelTwoName")){
							levelTwoName = params.getString("levelTwoName");
							if(StringUtils.isNotEmpty(levelTwoName)){
								sql1 += " and level_two_name like ?";
								sql2 += " and level_two_name like ?";
								searchParams.add("%" + levelTwoName + "%");
							}
						}
						if(params.containsKey("businessUnitCode")){
							String businessUnitCode = params.getString("businessUnitCode");
							if(StringUtils.isNotEmpty(businessUnitCode)){
								sql1 += " and  business_unit_code = ?";
								sql2 += " and  business_unit_code = ?";
								searchParams.add(businessUnitCode);
							}
						}
						Query query =null;
						map.put("code",key);
						map.put("level_two_rowspan",1);
						map.put("level_three_rowspan",1);
						map.put("level_two_total",0.0d);
						map.put("level_two_total_budget",0.0d);
						map.put("level_three_total",0.0d);
						map.put("level_three_total_budget",0.0d);
						if(composing.getParent() != null){
							query = composingDao.getSession().createSQLQuery(sql1);
							Composing parent = composing.getParent();
							map.put("level_two_name",parent.getName());
							map.put("level_three_name",composing.getName());							
						}else{
							query = composingDao.getSession().createSQLQuery(sql2);
							map.put("level_three_name",composing.getName());
							map.put("level_two_name","");
						}						
						for(int i=0;i<searchParams.size();i++){
							query.setParameter(i,searchParams.get(i));
						}
				        List<?> results = query.list();		
						DecimalFormat df = new DecimalFormat("#.00");
						Double value=0.0;
						if(results.size()>0){
							for (int i = 0; i < results.size(); i++) {
								value =value+ Double.valueOf(df.format(Double.valueOf(results.get(i)+"")));								
							}
							totalValue+=value;
						}
						map.put(String.valueOf(k), value);
						Integer year=(k/100);
						String s = String.valueOf(k);	 
						Integer month=Integer.parseInt(s.substring(s.length() - 2, s.length()));
						if(month==12){
							year++;
							month=1;
							k=Integer.valueOf((year.toString()+"00"));
						}
				     }
					valueMap.put(key, totalValue);
					map.put("totalValue", totalValue);	
					allValue+=totalValue;
				list.add(map);
			}
			Map<String,Object> lastLevelTwoMap = null;
			for(Map<String,Object> map : list){
				String code = map.get("code").toString();
				map.put("total",allValue);
				if(lastLevelTwoMap == null){
					lastLevelTwoMap = map;
				}else{
					if(map.get("level_two_name").equals(lastLevelTwoMap.get("level_two_name"))){
						lastLevelTwoMap.put("level_two_rowspan",(Integer)lastLevelTwoMap.get("level_two_rowspan")+1);
						map.remove("level_two_name");
					}else{
						lastLevelTwoMap = map;
					}
				}
				if(valueMap.containsKey(code)){
					Double value = valueMap.get(code);
					Double val = (Double)lastLevelTwoMap.get("level_two_total") +value;
					lastLevelTwoMap.put("level_two_total",val);
					if(StringUtils.isNotEmpty((String)map.get("level_three_name"))){
						map.put("level_three_total",value);
					}
				}
			}
		return list;
	}
	public Map<Integer, List<Map<String,Object>>> getComposingsForAll1(JSONObject params) {
		Integer startMonth = Integer.valueOf(params.getString("startDate").replaceAll("-",""));
		Integer endMonth = Integer.valueOf(params.getString("endDate").replaceAll("-",""));		
		Map<Integer, List<Map<String,Object>>> hashMap=new HashMap<Integer, List<Map<String,Object>>>();
		for (int k = startMonth; k <= endMonth; k++) {
			List<Object> searchParams = new ArrayList<Object>();
			searchParams.add(k);			
			String sql = " select level_two_name,level_three_name,SUM(value),SUM(value) as value from COST_COST_RECORD where level_two_name is not null and occurring_month=? ";
			String levelTwoName = null;
			if(params.containsKey("levelTwoName")){
				levelTwoName = params.getString("levelTwoName");
				if(StringUtils.isNotEmpty(levelTwoName)){
					sql += " and level_two_name like ?";
					searchParams.add("%" + levelTwoName + "%");
				}
			}
			if(params.containsKey("businessUnitCode")){
				String businessUnitCode = params.getString("businessUnitCode");
				if(StringUtils.isNotEmpty(businessUnitCode)){
					sql += " and  business_unit_code = ?";
					searchParams.add(businessUnitCode);
				}
			}
			sql += "  group by level_two_name,level_three_name";
			Query query = composingDao.getSession().createSQLQuery(sql);
			for(int i=0;i<searchParams.size();i++){
				query.setParameter(i,searchParams.get(i));
			}
	        List<?> results = query.list();
			Map<String,Double> valueMap = new HashMap<String, Double>();
			Map<String,Double> budgetValueMap = new HashMap<String, Double>();
			DecimalFormat df = new DecimalFormat("#.00");
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				if(objs[0]==null||objs[1]==null||objs[2]==null||objs[3]==null){
					continue;
				}
				//成本金额
				Double value = Double.valueOf(df.format(Double.valueOf(objs[2]+"")));
				valueMap.put(objs[0]+"_" + objs[1],value);
				//预算金额
				budgetValueMap.put(objs[0]+"_" + objs[1],Double.valueOf(objs[3]+""));
			}		
			//
			List<Composing> composings = getTopComposingList("all");
			List<Composing> downItems = new ArrayList<Composing>();
			for(Composing composing : composings){
				downItems.addAll(getDownItems(composing));
			}
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Double totalValue = 0.0d,totalBudgetValue=0.0d;
			for(Composing composing : downItems){
				Map<String,Object> map = new HashMap<String, Object>();
				String key = composing.getCode();
				if(composing.getParent() != null){
					key = composing.getParent().getCode() + "_" + key;
				}else{
				    key = key + "_" + key;
				}
				map.put("code",key);
				map.put("unit",composing.getUnit()==null?"":composing.getUnit());
				if(valueMap.containsKey(key)){
					totalValue += valueMap.get(key);
				}
				if(budgetValueMap.containsKey(key)){
					totalBudgetValue += budgetValueMap.get(key);
				}
				map.put("level_two_rowspan",1);
				map.put("level_three_rowspan",1);
				map.put("level_two_total",0.0d);
				map.put("level_two_total_budget",0.0d);
				map.put("level_three_total",0.0d);
				map.put("level_three_total_budget",0.0d);
				if(composing.getParent() == null){
					map.put("level_two_name",composing.getName());
					map.put("level_three_name","");
				}else{
					Composing parent = composing.getParent();
					map.put("level_two_name",parent.getName());
					map.put("level_three_name",composing.getName());
				}
				list.add(map);
			}
			Map<String,Object> lastLevelTwoMap = null;
			for(Map<String,Object> map : list){
				String code = map.get("code")+"";
				map.put("total",totalValue);
				map.put("totalBudget",totalBudgetValue);
				if(lastLevelTwoMap == null){
					lastLevelTwoMap = map;
				}else{
					if(map.get("level_two_name").equals(lastLevelTwoMap.get("level_two_name"))){
						lastLevelTwoMap.put("level_two_rowspan",(Integer)lastLevelTwoMap.get("level_two_rowspan")+1);
						map.remove("level_two_name");
					}else{
						lastLevelTwoMap = map;
					}
				}
				
				if(valueMap.containsKey(code)){
					Double value = valueMap.get(code);
					Double budgetValue = budgetValueMap.get(code);
					if(lastLevelTwoMap != null){
						Double val = (Double)lastLevelTwoMap.get("level_two_total") +value;
						lastLevelTwoMap.put("level_two_total",val);
						Double budgetVal = (Double)lastLevelTwoMap.get("level_two_total_budget") +budgetValue;
						lastLevelTwoMap.put("level_two_total_budget",budgetVal);
					}
					if(StringUtils.isNotEmpty((String)map.get("level_three_name"))){
						map.put("level_three_total",value);
						map.put("level_three_total_budget",budgetValue);
					}
				}
			}
			hashMap.put(k, list);
			Integer year=(k/100);
			String s = String.valueOf(k);	 
			Integer month=Integer.parseInt(s.substring(s.length() - 2, s.length()));
			if(month==12){
				year++;
				month=1;
				k=Integer.valueOf((year.toString()+"00"));
			}
		}
		return hashMap;
	}
	private List<Composing> getDownItems(Composing composing){
		List<Composing> items = new ArrayList<Composing>();
		if(composing.getChildren().isEmpty()){
			items.add(composing);
		}else{
			for(Composing item :composing.getChildren()){
				items.addAll(getDownItems(item));
			}
		}
		return items;
	}
	
	/**
	  * 方法名: 根据编码获取质量成本
	  * <p>功能说明：</p>
	  * @param code
	  * @return
	 */
	public Composing getComposingByCode(String code){
		return getComposingByCode(code,ContextUtils.getCompanyId());
	}
	
	/**
	  * 方法名: 根据编码获取质量成本
	  * <p>功能说明：</p>
	  * @param code
	  * @return
	 */
	public Composing getComposingByCode(String code,Long companyId){
		String hql = "from Composing c where c.code = ? and c.companyId = ?";
		List<Composing> composings = composingDao.find(hql,code,companyId);
		if(composings.isEmpty()){
			return null;
		}else{
			return composings.get(0);
		}
	}
	
	/**
	  * 方法名: 根据名称获取质量成本
	  * <p>功能说明：</p>
	  * @param code
	  * @return
	 */
	public Composing getComposingByName(String name){
		return getComposingByName(name,ContextUtils.getCompanyId());
	}
	/**
	  * 方法名: 根据名称获取质量成本
	  * <p>功能说明：</p>
	  * @param code
	  * @return
	 */
	public Composing getComposingByName(String name,Long companyId){
		String hql = "from Composing c where c.name = ? and c.companyId = ?";
		List<Composing> composings = composingDao.find(hql,name,companyId);
		if(composings.isEmpty()){
			return null;
		}else{
			return composings.get(0);
		}
	}
	public List<Composing> listThree() {
		String hql = "from Composing c where  c.companyId = ? and c.parent is not null";
		List<Composing> composings = composingDao.find(hql,ContextUtils.getCompanyId());
		return composings;
	}
}