package com.ambition.supplier.estimate.service;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.EstimateModel;
import com.ambition.supplier.entity.EvaluatingIndicator;
import com.ambition.supplier.entity.ModelIndicator;
import com.ambition.supplier.estimate.dao.ModelIndicatorDao;
import com.ambition.util.common.DateUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.util.ContextUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

@Service
@Transactional
public class ModelIndicatorManager {
	
	@Autowired
	private ModelIndicatorDao modelIndicatorDao;
	
	@Autowired 
	private LogUtilDao logUtilDao;
	
	public ModelIndicator getModelIndicator(Long id){
		return modelIndicatorDao.get(id);
	}
	
	/**
	 * 保存模型指标
	 * @throws Exception 
	 * 
	 */
	public void saveModelIndicator(EstimateModel estimateModel,EvaluatingIndicator evaluatingIndicator,JSONObject params) throws Exception{
		if(!estimateModel.getChildren().isEmpty()){
			throw new RuntimeException("评价模型还有子节点，不能设置指标！");
		}
		if(!evaluatingIndicator.getChildren().isEmpty()){
			throw new RuntimeException("指标还有子节点，不能设置");
		}
		params = convertJsonObject(params);
		List<ModelIndicator> modelIndicators = modelIndicatorDao.getModelIndicatorsByEvaluatingIndicator(evaluatingIndicator);
		boolean isSel = false;
		for(ModelIndicator modelIndicator : modelIndicators){
			if(estimateModel.getId().toString().equals(modelIndicator.getEstimateModel().getId().toString())){
				isSel = true;
				for(Object key : params.keySet()){
					if(key.toString().startsWith("level")){
						String val = params.getString(key.toString());
						String min = "",max="";
						if(StringUtils.isNotEmpty(val)){
							if(val.indexOf("~")==-1){
								min = val;
								max = val;
							}else{
								min = val.split("~")[0];
								max = val.split("~")[1];
							}
						}
						setProperty(modelIndicator,key + "Min",min);
						setProperty(modelIndicator,key + "Max",max);
					}
					setProperty(modelIndicator,key,params.getString(key.toString()));
				}
				modelIndicator.setModifiedTime(new Date());
				modelIndicator.setModifier(ContextUtils.getUserName());
				modelIndicatorDao.save(modelIndicator);
				logUtilDao.debugLog("保存", modelIndicator.toString());
				break;
			}
		}
		if(!isSel){
			throw new RuntimeException("请先勾选是否评价");
		}
	}
	
	
	/**
	 * 获取所有已设置的模型的指标
	 * @return
	 */
	public List<ModelIndicator> getAllModelIndicators(Long parentModelIndicatorId){
		return modelIndicatorDao.getAllModelIndicators(parentModelIndicatorId);
	}
	
	/**
	 * 获取所有已设置的模型的指标
	 * @return
	 */
	public List<ModelIndicator> getAllModelIndicators(){
		return modelIndicatorDao.getAllModelIndicators();
	}
	
	/**
	 * 设置评价
	 * @param estimateModelId
	 * @param evalucatingIndicatorId
	 * @param isEstimate
	 * @throws Exception 
	 */
	public void setModelIndicator(EstimateModel estimateModel,EvaluatingIndicator evaluatingIndicator,String isEstimate,JSONObject params) throws Exception{
		if(!estimateModel.getChildren().isEmpty()){
			throw new RuntimeException("评价模型还有子节点，不能设置指标！");
		}
		if(!evaluatingIndicator.getChildren().isEmpty()){
			throw new RuntimeException("指标还有子节点，不能设置");
		}
		Long selfParentModelId = estimateModel.getParentEstimateModelId()==null?estimateModel.getId():estimateModel.getParentEstimateModelId();
		List<ModelIndicator> modelIndicators = modelIndicatorDao.getModelIndicatorsByEvaluatingIndicator(evaluatingIndicator);
		if("yes".equals(isEstimate)){
			boolean isSet = false;
			for(ModelIndicator modelIndicator : modelIndicators){
				Long parentModelId = modelIndicator.getEstimateModel().getParentEstimateModelId()==null?modelIndicator.getEstimateModel().getId():modelIndicator.getEstimateModel().getParentEstimateModelId();
				if(EstimateModel.STATE_ISUSE.equals(modelIndicator.getEstimateModel().getState())
						&&parentModelId.toString().equals(selfParentModelId.toString())
						&&modelIndicator.getEstimateModel().getChildren().isEmpty()){
					if(estimateModel.getId().toString().equals(modelIndicator.getEstimateModel().getId().toString())){
						isSet = true;
						break;
					}else{
						throw new RuntimeException("评价指标已在模型其他位置设置！");
					}
				}
			}
			if(!isSet){
				ModelIndicator modelIndicator = new ModelIndicator();
				modelIndicator.setCompanyId(ContextUtils.getCompanyId());
				modelIndicator.setCreatedTime(new Date());
				modelIndicator.setCompanyId(ContextUtils.getCompanyId());
				modelIndicator.setCreator(ContextUtils.getUserName());
				modelIndicator.setModifiedTime(new Date());
				modelIndicator.setModifier(ContextUtils.getUserName());
				modelIndicator.setEstimateModel(estimateModel);
				modelIndicator.setEvaluatingIndicator(evaluatingIndicator);
				params = convertJsonObject(params);
				for(Object key : params.keySet()){
					if(key.toString().startsWith("level")){
						String val = params.getString(key.toString());
						String min = "",max="";
						if(StringUtils.isNotEmpty(val)){
							if(val.indexOf("~")==-1){
								min = val;
								max = val;
							}else{
								min = val.split("~")[0];
								max = val.split("~")[1];
							}
						}
						setProperty(modelIndicator,key + "Min",min);
						setProperty(modelIndicator,key + "Max",max);
					}
					setProperty(modelIndicator,key.toString(),params.getString(key.toString()));
				}
				modelIndicatorDao.save(modelIndicator);
			}
		}else{
			for(ModelIndicator modelIndicator : modelIndicators){
				if(estimateModel.getId().toString().equals(modelIndicator.getEstimateModel().getId().toString())){
					modelIndicatorDao.delete(modelIndicator);
					break;
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
		}
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}
	
	private void setProperty(Object obj,Object key,String value) throws Exception{
		Class<?> type = PropertyUtils.getPropertyType(obj,key.toString()); 
		if(type != null){
			if(StringUtils.isEmpty(value)){
				PropertyUtils.setProperty(obj,key.toString(),null);
			}else{
				if(Date.class.equals(type)){
					PropertyUtils.setProperty(obj,key.toString(),DateUtil.parseDate(value));
				}else if(Double.class.equals(type)){
					PropertyUtils.setProperty(obj,key.toString(),Double.valueOf(value));
				}else if(Integer.class.equals(type)){
					PropertyUtils.setProperty(obj,key.toString(),Integer.valueOf(value));
				}else{
					PropertyUtils.setProperty(obj,key.toString(),value);
				}
			}
		}
	}
}
