package com.ambition.iqc.samplestandard.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.SampleTransitionRule;
import com.ambition.iqc.entity.UseBaseType;
import com.ambition.iqc.samplestandard.dao.SampleTransitionRuleDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.util.ContextUtils;

/**
 * 抽样方案转移规则业务类
 * @author 赵骏
 *
 */
@Service
@Transactional
public class SampleTransitionRuleManager {
	@Autowired
	private SampleTransitionRuleDao sampleTransitionRuleDao;
	@Autowired
	private SampleSchemeManager sampleSchemeManager;
	@Autowired
	private LogUtilDao logUtilDao;
	private List<SampleTransitionRule> sampleTransitionRules = null;
	/**
	 * 根据id获取转移规则
	 * @param id
	 * @return
	 */
	public SampleTransitionRule getSampleTransitionRule(Long id){
		return sampleTransitionRuleDao.get(id);
	}
	
	/**
	 * 查询所有的转移规则
	 * @return
	 */
	public List<SampleTransitionRule> list(){
		if(sampleTransitionRules == null){
			sampleTransitionRules = sampleTransitionRuleDao.list();
		}
		return sampleTransitionRules;
	}
	
	/**
	 * 保存转移规则
	 * @param sampleTranstionArray
	 * @throws RuntimeException
	 */
	public void saveSampleTransitionRule(JSONArray sampleTranstionArray,String baseType) throws Exception{
		//删除原来的设置
		String hql = "delete from SampleTransitionRule s where s.companyId = ?";
		sampleTransitionRuleDao.batchExecute(hql,ContextUtils.getCompanyId());
		
		//保存新的转换规则
		List<SampleTransitionRule> list = new ArrayList<SampleTransitionRule>();
		for(int i=0;i<sampleTranstionArray.size();i++){
			JSONObject json = sampleTranstionArray.getJSONObject(i);
			SampleTransitionRule sampleTransitionRule = null;
			sampleTransitionRule = new SampleTransitionRule();
			sampleTransitionRule.setCompanyId(ContextUtils.getCompanyId());
			sampleTransitionRule.setCreatedTime(new Date());
			sampleTransitionRule.setCreator(ContextUtils.getUserName());
			sampleTransitionRule.setLastModifiedTime(new Date());
			sampleTransitionRule.setLastModifier(ContextUtils.getUserName());
			for(Object key : json.keySet()){
				String value = json.getString(key.toString());
				setProperty(sampleTransitionRule, key.toString(),value);
			}
			sampleTransitionRuleDao.save(sampleTransitionRule);
			logUtilDao.debugLog("保存", sampleTransitionRule.toString());
			list.add(sampleTransitionRule);
		}
		sampleTransitionRules = list;//缓存到内存中
		
		if(baseType != null){
			UseBaseType useBaseType = sampleSchemeManager.getUseBaseType();
			useBaseType.setBaseType(baseType);
			sampleSchemeManager.saveUseBaseType(useBaseType);
		}
	}
	
	/**
	 * 获取统计方法的options
	 * @return
	 */
	public List<Option> getStatisticalMthodOptions(){
		List<Option> options = new ArrayList<Option>();
		Option option = new Option();
		option.setName(SampleTransitionRule.METHOD_ACCUMULATIVE);
		option.setValue(SampleTransitionRule.METHOD_ACCUMULATIVE);
		options.add(option);
		
		option = new Option();
		option.setName(SampleTransitionRule.METHOD_SUCCESSION);
		option.setValue(SampleTransitionRule.METHOD_SUCCESSION);
		options.add(option);
		return options;
	}
	/**
	 * 获取比较字符串的options
	 * @return
	 */
	public List<Option> getComparisonOperatorsOptions(){
		List<Option> options = new ArrayList<Option>();
		
		Option option = new Option();
		option.setName(">=");
		option.setValue(">=");
		options.add(option);
		
		option = new Option();
		option.setName("<=");
		option.setValue("<=");
		options.add(option);
		
		return options;
	}
	private void setProperty(Object obj, String property, Object value) throws Exception {
		Class<?> type = PropertyUtils.getPropertyType(obj, property);
		if (type != null) {
			if (value == null || StringUtils.isEmpty(value.toString())) {
				PropertyUtils.setProperty(obj, property, null);
			} else {
				if (String.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property, value.toString());
				} else if (Integer.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Integer.valueOf(value.toString()));
				} else if (Double.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Double.valueOf(value.toString()));
				} else if (Float.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Float.valueOf(value.toString()));
				} else if (Boolean.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Boolean.valueOf(value.toString()));
				} else {
					PropertyUtils.setProperty(obj, property, value);
				}
			}
		}
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
}
