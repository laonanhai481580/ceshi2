package com.ambition.spc.layertype.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.LayerDetail;
import com.ambition.spc.entity.LayerType;
import com.ambition.spc.layertype.dao.LayerDetailDao;
import com.ambition.spc.layertype.dao.LayerTypeDao;
import com.ambition.spc.processdefine.dao.FeatureLayerDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;

/**    
 * LayerTypeManager.java
 * @authorBy wanglf
 *
 */
@Service
public class LayerTypeManager {
	@Autowired
	private LayerTypeDao layerTypeDao;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private LayerDetailDao layerDetailDao;
	@Autowired
	private FeatureLayerDao featureLayerDao;
	
	public LayerType getLayerType(Long id){
		return layerTypeDao.get(id);
	}
	
	public LayerType getLayerTypeByName(String name){
		return layerTypeDao.getLayerType(name);
	}
	@Transactional
	public void saveLayerType(LayerType layerType,Boolean isEdit){
		if(isEdit){
			if(isExistLayerTypeName(layerType.getTypeName())){
				throw new RuntimeException("已经存在相同的名称!");
			}
		}
		if(layerType.getId() != null){
			String hql = "from FeatureLayer f where f.companyId = ? and f.targetId = ?";
			Query query = layerTypeDao.getSession().createQuery(hql);
			query.setLong(0,ContextUtils.getCompanyId());
			query.setLong(1,layerType.getId());
			@SuppressWarnings({ "unchecked" })
			List<FeatureLayer> layerItems = query.list();
			if(layerItems != null && layerItems.size() != 0){
				for(FeatureLayer layer:layerItems){
					layer.setModifiedTime(new Date());
					layer.setModifier(ContextUtils.getUserName());
					layer.setDetailCode(layerType.getTypeCode());
					layer.setDetailName(layerType.getTypeName());
					layer.setSampleMethod(layerType.getSampleMethod());
					featureLayerDao.save(layer);
				}
			}
		}
		layerTypeDao.save(layerType);
	}
	
	private Boolean isExistLayerTypeName(String typeName){
		String hql = "select count(*) from LayerType l where l.companyId = ? and l.typeName = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(typeName);
		Query query = layerTypeDao.getSession().createQuery(hql);
		for(int i=0;i<params.size();i++){
			query.setParameter(i,params.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	
	public Boolean isExistLayerDetailName(LayerType layerType,String detailName){
		String hql = "select count(*) from LayerDetail l where l.companyId = ? and l.layerType = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(layerType);
		if(detailName != null){
			hql += " and l.detailName = ?";
			params.add(detailName);
		}
		Query query = layerTypeDao.getSession().createQuery(hql);
		for(int i=0;i<params.size();i++){
			query.setParameter(i,params.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	@Transactional
	public void saveLayerDetail(LayerDetail layerDetail){
		layerDetailDao.save(layerDetail);
	}
	@Transactional
	public void deleteLayerDetail(String  deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id:ids){
			LayerDetail ld=	layerDetailDao.get(Long.parseLong(id));
			layerDetailDao.delete(ld);
		}
	}
	@Transactional
	public void deleteLayerType(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			LayerType layerType = layerTypeDao.get(Long.valueOf(id));
			if(layerType.getId() != null){
				if(!layerType.getChildren().isEmpty()){
					throw new RuntimeException(layerType.getTypeName() + "还有子节点不能删除，请先删除子节点!");
				}
				
				String hql = "from FeatureLayer f where f.companyId = ? and f.targetId = ?";
				Query query = layerTypeDao.getSession().createQuery(hql);
				query.setLong(0,ContextUtils.getCompanyId());
				query.setLong(1,layerType.getId());
				@SuppressWarnings({ "unchecked" })
				List<FeatureLayer> layerItems = query.list();
				if(layerItems != null && layerItems.size() != 0){
					for(FeatureLayer layer:layerItems){
						layer.setModifiedTime(new Date());
						layer.setModifier(ContextUtils.getUserName());
						layer.setQualityFeature(null);
						featureLayerDao.save(layer);
					}
				}
				
				logUtilDao.debugLog("删除", layerType.toString());
				layerTypeDao.delete(layerType);
			}
		}
	}
	
	public List<LayerType> getLayerTypeCodes(){
		return layerTypeDao.getLayerTypeCode();
	}
	
	
	public List<LayerType> getLayerTypes(){
		return layerTypeDao.getLayerTypes();
	}
	
	/**
	 * 根据父类查询结构
	 * @param parentId
	 * @return
	 */
	public List<LayerType> getLayerTypes(Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("from LayerType l where l.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		if(parentId == null){
			sb.append(" and l.parent is null");
		}else{
			sb.append(" and l.parent.id = ?");
			params.add(parentId);
		}
		sb.append(" order by l.orderNum");
		return layerTypeDao.find(sb.toString(),params.toArray());
	}
	
	/**
	 * 转换过程节点结构至json对象
	 * @param processPoint
	 * @return
	 */
	public Map<String,Object> convertLayerType(LayerType layerType){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data",layerType.getTypeName());
		Map<String,Object> attrMap = new HashMap<String, Object>();
		attrMap.put("id",layerType.getId());
		attrMap.put("level",layerType.getLevel());
		attrMap.put("name",layerType.getTypeName());
		attrMap.put("code",layerType.getTypeCode());
		attrMap.put("sampleMethod",layerType.getSampleMethod());
		attrMap.put("isInputValue", layerType.getIsInputValue());
		map.put("attr", attrMap);
		if(!layerType.getChildren().isEmpty()){
			map.put("state","open");
			List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
			for(LayerType child : layerType.getChildren()){
				children.add(convertLayerType(child));
			}
			map.put("children",children);
		}
		return map;
	}
	
	public String getDetailResultJson(Page<LayerType> page,LayerType layerType){
		List<JSONObject> list = new ArrayList<JSONObject>();
		List<LayerDetail> details = layerType != null?layerType.getLayerDetails():new ArrayList<LayerDetail>();
		for(LayerDetail de : details){
			HashMap<String,Object> hs = new HashMap<String,Object>();
			hs.put("id", de.getId());
			hs.put("detailCode", de.getDetailCode());
			hs.put("detailName", de.getDetailName());
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append(JsonParser.object2Json(hs).substring(1,JsonParser.object2Json(hs).length()));
			JSONObject jObject = JSONObject.fromObject(sb.toString());
			list.add(jObject);
		}
		//添加jqGrid所需的页信息
		StringBuilder json = new StringBuilder();
		json.append("{\"page\":\"");
		json.append(page.getPageNo());
		json.append("\",\"total\":");
		json.append(details.size());
		json.append(",\"records\":\"");
		json.append(details.size());
		json.append("\",\"rows\":");
		json.append(JSONArray.fromObject(list).toString());
		json.append("}");
		return json.toString();
	}
	
	public LayerType getFirstLevelLayerType(){
		return layerTypeDao.getFirstLevelLayerType();
	}
	
	/**
	 * 根据质量特性ID查询层级信息
	 * @param featureId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FeatureLayer> queryFeatureLayersByFeatureId(Long featureId){
		String hql = "from FeatureLayer f where f.qualityFeature.id = ?";
		return layerTypeDao.createQuery(hql,featureId).list();
	}
}
