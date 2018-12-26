package com.ambition.spc.statistics.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.ambition.spc.baseinfo.service.CpkRuleManager;
import com.ambition.spc.dataacquisition.service.SpcDataManager;
import com.ambition.spc.entity.ControlLimit;
import com.ambition.spc.entity.CpkRule;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.processdefine.dao.QualityFeatureDao;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:SPC应用状况报表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2015-7-1 发布
 */
@Service
public class SpcApplicationDatasManager {
	@Autowired
	private QualityFeatureDao qualityFeatureDao;
	@Autowired
	private CpkTrendDatasManager cpkTrendDatasManager;
	@Autowired
	private CpkRuleManager cpkRuleManager;
	/**
	 * SPC查询数据源
	 */
	@Resource(name="spcCopyDataJdbcTemplate")
	private JdbcTemplate spcJdbcTemplate;
	
	/**
	  * 方法名: 查询质量特性
	  * <p>功能说明：</p>
	  * @param featureIds
	  * @param featureMap
	 */
	public void searchQualityFeature(String processIds,Map<String,QualityFeature> featureMap){
		String sql = "select q.* from SPC_QUALITY_FEATURE q";
		if(StringUtils.isNotEmpty(processIds)){
			sql += " where q.FK_PROCESS_POINT_ID in (" + processIds + ")";
		}
		@SuppressWarnings("unchecked")
		List<QualityFeature> features = spcJdbcTemplate.query(sql,new CustomObjectRowMapper(QualityFeature.class));
		for(QualityFeature feature : features){
			featureMap.put(feature.getId().toString(),feature);
		}
	}
	
	/**
	  * 方法名: 查询控制限
	  * <p>功能说明：</p>
	  * @param processIds
	  * @param controlLimitMap
	 */
	public void searchControlLimits(String processIds,String featureIds,Map<String,List<ControlLimit>> controlLimitMap){
		//查询控制限
		String sql="select c.* from SPC_CONTROL_LIMIT  c ";
		if(StringUtils.isNotEmpty(processIds)){
			sql += " inner join SPC_QUALITY_FEATURE f on c.FK_QUALITY_FEATURE_ID = f.id " +
					"and f.FK_PROCESS_POINT_ID in (" + processIds + ") ";
		}
		if(StringUtils.isNotEmpty(featureIds)){
			sql += " where c.FK_QUALITY_FEATURE_ID in (" + featureIds + ") ";
		}
		sql += " order by c.created_time desc";
		@SuppressWarnings("unchecked")
		List<ControlLimit> controlLimits = spcJdbcTemplate.query(sql,new RowMapper(){
			@Override
			public Object mapRow(ResultSet rs, int i)
					throws SQLException {
				ControlLimit limit = new ControlLimit();
				limit.setId(rs.getLong("id"));
				limit.setCompanyId(rs.getLong("company_id"));
				limit.setXucl(rs.getDouble("xucl"));
				limit.setXcl(rs.getDouble("xcl"));
				limit.setXlcl(rs.getDouble("xlcl"));
				limit.setSucl(rs.getDouble("sucl"));
				limit.setScl(rs.getDouble("scl"));
				limit.setSlcl(rs.getDouble("slcl"));
				Long featureId = rs.getLong("FK_QUALITY_FEATURE_ID");
				if(featureId!=null){
					QualityFeature feature = new QualityFeature();
					feature.setId(featureId);
					limit.setQualityFeature(feature);
				}
				return limit;
			}
		});
		for(ControlLimit limit : controlLimits){
			String key = limit.getQualityFeature().getId()+"";
			if(!controlLimitMap.containsKey(key)){
				controlLimitMap.put(key,new ArrayList<ControlLimit>());
			}
			controlLimitMap.get(key).add(limit);
		}
	}
	/**
	 * SPC应用状况报表
	 * @param params
	 * @return
	 * @throws InterruptedException 
	 */
	public Map<String,Object> getSpcApplicationDatas(JSONObject params) throws InterruptedException{
		params = CommonUtil1.convertJsonObject(params);
		String startDateStr = params.getString("startDate_ge_date");
		String endDateStr = params.getString("endDate_le_date");
		String lastAmout = params.containsKey("lastAmout")?params.getString("lastAmout"):null;//最后子组数
		Integer lastAmoutInt = null;
		if(CommonUtil1.isInteger(lastAmout)){
			lastAmoutInt = Integer.valueOf(lastAmout);
		}
		String processIds = params.containsKey("processId")?params.getString("processId"):null;
		//计算质量特性总数
		Map<String,Object> result = new HashMap<String, Object>();
		
		//查询需要计算的质量特性
		Map<String,QualityFeature> featuryMap = new HashMap<String, QualityFeature>();
		searchQualityFeature(processIds,featuryMap);
		//控制限
		Map<String,List<ControlLimit>> controlLimitMap = new HashMap<String, List<ControlLimit>>();
		searchControlLimits(processIds,null,controlLimitMap);
		
		List<QualityFeature> qualityFeatures = new ArrayList<QualityFeature>();
		for(QualityFeature qualityFeature : featuryMap.values()){
			qualityFeatures.add(qualityFeature);
		}
		Integer featureCount = qualityFeatures.size();
		result.put("featureCount", featureCount);
		
		//SPC数据批量查询
		SpcDataManager spcDataManager = new SpcDataManager(qualityFeatureDao.getSession());
		Map<String,Map<String,Object>> featureDataMap = spcDataManager.multiQuerySpcDataValues(qualityFeatures, startDateStr, endDateStr, params,1,null,lastAmoutInt);
		
		List<String> categories = new ArrayList<String>();
		//第一Y轴线的数据
		List<Map<String,Object>> dataY = new ArrayList<Map<String,Object>>();
		List<CpkRule> cpkRules = cpkRuleManager.getAllList();
		
		if(cpkRules.isEmpty()){
			throw new AmbFrameException("CPK分组规则未维护！");
		}
		
		Map<String,Integer> cpkCountMap = new HashMap<String, Integer>();
		Map<String,Integer> ckpIndexMap = new HashMap<String, Integer>();
		//初始化cpk所占个数map
		int k = 1;
		for (CpkRule cpkRule : cpkRules) {
			cpkCountMap.put(cpkRule.getName(), 0);
			ckpIndexMap.put(cpkRule.getName(), k);
			k++;
		}
		Integer groupCount = 0;
		for(String key : featuryMap.keySet()){
			Map<String,Object> resultMap = featureDataMap.get(key);
			if(resultMap==null){
				continue;
			}
			@SuppressWarnings("unchecked")
			List<double[]> values = (List<double[]>)resultMap.get("values");
			groupCount += values.size();
			
			String cpkRuleName = countCpkRange(featuryMap.get(key),values, 
					controlLimitMap.get(key), ActionContext.getContext(),cpkRules);
			cpkCountMap.put(cpkRuleName, (cpkCountMap.get(cpkRuleName)+1));
		}
		//计算总数
		result.put("subGroupCount", groupCount);
		//表格需要的数据
		List<Map<String,Object>> tableData = new ArrayList<Map<String,Object>>();
		int total = 0;
		//排序，让前台美观点
		for (CpkRule cpkRule : cpkRules) {
			for(Entry<String, Integer> mycount:cpkCountMap.entrySet()){
				if(cpkRule.getName().equals(mycount.getKey())){
					addTableDatas(mycount.getKey(),mycount.getValue(),featureCount,tableData,ckpIndexMap);
					total += mycount.getValue();
				}
			}
		}
		result.put("total",total);
		Collections.sort(tableData,new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1,
					Map<String, Object> o2) {
				if(Integer.valueOf(o1.get("id").toString())>Integer.valueOf(o2.get("id").toString())){
					return 1;
				}else{
					return 0;
				}
			}
		});
		
		//报表需要的数据
		for(Map<String,Object> map:tableData){
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("name","column");
			if(map.containsKey("name")&&map.get("name")!=null){
				categories.add(map.get("name").toString());
			}
			if(map.containsKey("bi1")&&map.get("bi1")!=null){
				String bi =	map.get("bi1").toString().replace("%", "").trim();
				data.put("y",Double.valueOf(bi));
			}else{
				data.put("y",0.0);
			}
			if(map.containsKey("total")&&map.get("total")!=null){
				data.put("arg",Double.valueOf(map.get("total").toString()));
			}
			dataY.add(data);
		}
		result.put("categories", categories);
		result.put("tableData",tableData);
		
		List<Map<String,Object>> myTableData = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map:tableData){
			myTableData.add(map);
		}
		//添加合计行
		Map<String,Object> data = new HashMap<String, Object>();
		DecimalFormat df = new DecimalFormat("#.##%");
		data.put("name", "合计");
		data.put("total", total);
		if(total==0||featureCount==0){
			data.put("bi1", 0.00+"%");
		}else{
			data.put("bi1", df.format(total/(featureCount*1.0)));
		}
		myTableData.add(data);
		
		result.put("myTableData",myTableData);
		result.put("title","CPK水平分布");
		Map<String,Object> series = new HashMap<String, Object>();
		series.put("data",dataY);
		result.put("series", series);
		
		return result;
	}
	
	private String countCpkRange(QualityFeature qualityFeature,List<double[]> values,
			List<ControlLimit> controlLimits,ActionContext actionContext,List<CpkRule> cpkRules){
		//封装数据
		JLcalculator jLcalculator = new JLcalculator();
		JLOriginalData originalData = new JLOriginalData();
		//封装originalDate
		cpkTrendDatasManager.calculatJl(jLcalculator,qualityFeature,values,controlLimits,originalData,null);
		Double cpk = jLcalculator.getjLResult().getCpkMoudle().getCpk();
		for (int i = 0; i < cpkRules.size(); i++) {
			int k = 0;
			if(null == cpkRules.get(i).getBelowLimit() || cpk >= cpkRules.get(i).getBelowLimit()){
				k++;
			}
			if(null==cpkRules.get(i).getUpLimit() || cpk < cpkRules.get(i).getUpLimit()){
				k++;
			}
			if(k == 2){
				return cpkRules.get(i).getName();
			}
		}
		throw new AmbFrameException("CPK规则设置不完整，导致cpk:"+cpk+"无法找到分组范围！");
	}
	
	private void addTableDatas(String name,Integer count,Integer featureCount,List<Map<String,Object>> tableData
			, Map<String,Integer> cpkIndexMap){
		Map<String,Object> data = new HashMap<String, Object>();
		DecimalFormat df = new DecimalFormat("#.##%");
		data.put("id", cpkIndexMap.get(name));
		data.put("name", name);
		data.put("total", count);
		if(count==0||featureCount==0){
			data.put("bi1", 0.00+"%");
		}else{
			data.put("bi1", df.format(count/(featureCount*1.0)));
		}
		tableData.add(data);
	}
}
