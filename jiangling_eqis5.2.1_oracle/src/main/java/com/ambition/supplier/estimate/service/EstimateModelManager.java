package com.ambition.supplier.estimate.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.EstimateModel;
import com.ambition.supplier.entity.ModelIndicator;
import com.ambition.supplier.estimate.dao.EstimateModelDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.util.ContextUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

@Service
@Transactional
public class EstimateModelManager {
	@Autowired
	private EstimateModelDao estimateModelDao;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	/**
	 * 检查是否存在相同名称
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Boolean isExistEstimateModel(Long id,String name,EstimateModel parent){
		String hql = "select count(*) from EstimateModel e where e.companyId = ? and e.name = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(name);
		if(id != null){
			hql += " and e.id <> ?";
			params.add(id);
		}
		if(parent == null){
			hql += " and e.parent is null";
		}else{
			hql += " and e.parent = ?";
			params.add(parent);
		}
		Query query = estimateModelDao.getSession().createQuery(hql);
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
	public EstimateModel getEstimateModel(Long id){
		return estimateModelDao.get(id);
	}
	
	/**
	 * 根据名称和上级查询评价模型
	 * @param estimateModelName,parent
	 * @return
	 */
	public EstimateModel getEstimateModelByName(String estimateModelName,EstimateModel parent){
		return estimateModelDao.getEstimateModelByName(estimateModelName, parent);
	}
	
	public List<EstimateModel> getEstimateModelOfChildren(){
		String hql = "from EstimateModel e where e.parent is not null";
		return estimateModelDao.find(hql);
	}
	
	public List<EstimateModel> getParentEstimateModel(){
		String hql = "from EstimateModel e where e.parent is null";
		return estimateModelDao.find(hql);
	}
	
	/**
	 * 根据名称和上级查询评价模型
	 * @param estimateModelName,parent
	 * @return
	 */
	public EstimateModel getEstimateModelById(Long estimateModelId){
		String hql = "from EstimateModel e where e.id = ?";
		if(estimateModelId==null){
			return null;
		}
		List<EstimateModel> list = estimateModelDao.find(hql,estimateModelId);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}
	/**
	 * 保存模型
	 * @param evaluatingIndicator
	 */
	public void saveEstimateModel(EstimateModel estimateModel){
		if(StringUtils.isEmpty(estimateModel.getName())){
			throw new RuntimeException("名称为能为空！");
		}
		if(isExistEstimateModel(estimateModel.getId(),estimateModel.getName(),estimateModel.getParent())){
			throw new RuntimeException("已存在相同的模型名称");
		}
		estimateModelDao.save(estimateModel);
	}
	
	/**
	 * 删除评价模型
	 * @param id
	 */
	public void deleteEstimateModel(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			EstimateModel estimateModel = estimateModelDao.get(Long.valueOf(id));
			if(estimateModel.getId() != null){
				if(!estimateModel.getChildren().isEmpty()){
					throw new RuntimeException("已存在相同的模型名称");
				}
				logUtilDao.debugLog("删除", estimateModel.toString());
				estimateModelDao.delete(estimateModel);
			}
		}
	}
	
	/**
	 * 获取评价模型
	 * @return
	 */
	public List<EstimateModel> getTopEstimateModels(){
		return estimateModelDao.getTopEstimateModels();
	}
	
	/**
	 * 保存复制的评价模型
	 * @param params
	 */
	public void saveCopyModel(JSONObject params){
		params = convertJsonObject(params);
		EstimateModel estimateModel = estimateModelDao.get(params.getLong("sourceId"));
		if(estimateModel == null){
			throw new RuntimeException("复制的模型不存在！");
		}
		
		EstimateModel targetModel = new EstimateModel();
		targetModel.setCreatedTime(new Date());
		targetModel.setCompanyId(ContextUtils.getCompanyId());
		targetModel.setCreator(ContextUtils.getUserName());
		targetModel.setModifiedTime(new Date());
		targetModel.setModifier(ContextUtils.getUserName());
		targetModel.setName(params.getString("name"));
		saveEstimateModel(targetModel);
		copyModelChildren(estimateModel,targetModel);
	}
	/**
	 * 复制模型子节点
	 * @param source
	 * @param target
	 */
	private void copyModelChildren(EstimateModel source,EstimateModel target){
		for(EstimateModel sourceChild : source.getChildren()){
			EstimateModel targetChild = new EstimateModel();
			targetChild.setCreatedTime(new Date());
			targetChild.setCompanyId(ContextUtils.getCompanyId());
			targetChild.setCreator(ContextUtils.getUserName());
			targetChild.setModifiedTime(new Date());
			targetChild.setModifier(ContextUtils.getUserName());
			targetChild.setParent(target);
			targetChild.setName(sourceChild.getName());
			targetChild.setLevel(target.getLevel()+1);
			targetChild.setCycle(sourceChild.getCycle());
			targetChild.setStartMonth(sourceChild.getStartMonth());
			targetChild.setState(sourceChild.getState());
			targetChild.setRemark(sourceChild.getRemark());
			targetChild.setParentEstimateModelId(target.getParentEstimateModelId()==null?target.getId():target.getParentEstimateModelId());
			estimateModelDao.save(targetChild);
			copyModelChildren(sourceChild, targetChild);
		}
		
		for(ModelIndicator sourceModelIndicator:source.getModelIndicators()){
			ModelIndicator targetModelIndicator = new ModelIndicator();
			targetModelIndicator.setCompanyId(ContextUtils.getCompanyId());
			targetModelIndicator.setCreatedTime(new Date());
			targetModelIndicator.setCompanyId(ContextUtils.getCompanyId());
			targetModelIndicator.setCreator(ContextUtils.getUserName());
			targetModelIndicator.setModifiedTime(new Date());
			targetModelIndicator.setModifier(ContextUtils.getUserName());
			targetModelIndicator.setEstimateModel(target);
			targetModelIndicator.setEvaluatingIndicator(sourceModelIndicator.getEvaluatingIndicator());
			targetModelIndicator.setTotalPoints(sourceModelIndicator.getTotalPoints());
			targetModelIndicator.setLevela(sourceModelIndicator.getLevela());
			targetModelIndicator.setLevelb(sourceModelIndicator.getLevelb());
			targetModelIndicator.setLevelc(sourceModelIndicator.getLevelc());
			targetModelIndicator.setLeveld(sourceModelIndicator.getLeveld());
			targetModelIndicator.setLevele(sourceModelIndicator.getLevele());
			targetModelIndicator.setLevelaMin(sourceModelIndicator.getLevelaMin());
			targetModelIndicator.setLevelaMax(sourceModelIndicator.getLevelaMax());
			targetModelIndicator.setLevelbMin(sourceModelIndicator.getLevelbMin());
			targetModelIndicator.setLevelbMax(sourceModelIndicator.getLevelbMax());
			targetModelIndicator.setLevelcMin(sourceModelIndicator.getLevelcMin());
			targetModelIndicator.setLevelcMax(sourceModelIndicator.getLevelcMax());
			targetModelIndicator.setLeveldMin(sourceModelIndicator.getLeveldMin());
			targetModelIndicator.setLeveldMax(sourceModelIndicator.getLeveldMax());
			targetModelIndicator.setLeveleMin(sourceModelIndicator.getLeveleMin());
			targetModelIndicator.setLeveleMax(sourceModelIndicator.getLeveleMax());
			targetModelIndicator.setRemark(sourceModelIndicator.getRemark());
			estimateModelDao.getSession().save(targetModelIndicator);
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
}
