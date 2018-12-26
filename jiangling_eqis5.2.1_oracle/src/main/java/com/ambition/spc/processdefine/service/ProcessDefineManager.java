package com.ambition.spc.processdefine.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.bsrules.service.BsRulesManager;
import com.ambition.spc.entity.AbnormalInfo;
import com.ambition.spc.entity.BsRules;
import com.ambition.spc.entity.ControlLimit;
import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.FeaturePerson;
import com.ambition.spc.entity.FeatureRules;
import com.ambition.spc.entity.FeatureTableColumn;
import com.ambition.spc.entity.LayerType;
import com.ambition.spc.entity.ProcessPoint;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.ReasonMeasure;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.layertype.service.LayerTypeManager;
import com.ambition.spc.processdefine.dao.ProcessDefineDao;
import com.ambition.spc.processdefine.dao.QualityFeatureDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;

/**    
 * ProcessDefineManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class ProcessDefineManager {
	@Autowired
	private ProcessDefineDao processDefineDao;
	@Autowired
	private FeatureTableColumnManager tableColumnManager;
	@Autowired
	private QualityFeatureDao qualityFeatureDao;
	@Autowired
 	private BsRulesManager bsRulesManager;
	@Autowired
 	private LayerTypeManager layerTypeManager;
	@Autowired
	private LogUtilDao logUtilDao;
	private static Map<String,Integer> pointKeyMap = new HashMap<String,Integer>();
	public static Map<String,Integer> getPointKeyMap(){
		pointKeyMap.clear();
		String[] strs = PropUtils.getProp("product_structure").split(",");
		int i = 0;
		for(String str : strs){
			if(StringUtils.isNotEmpty(str)){
				pointKeyMap.put(str,++i);
			}
		}
		return pointKeyMap;
	}
	/**
	 * 检查是否存在相同名称的过程节点
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Boolean isExistProcessPoint(Long id,String code,String parentIds){
		String hql = "select count(*) from ProcessPoint p where p.companyId = ? and p.code = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(code);
		if(parentIds != null){
			hql += " and p.parentIds like ?";
			params.add(parentIds + "%");
		}
		if(id != null){
			hql += " and p.id <> ?";
			params.add(id);
		}
		Query query = processDefineDao.getSession().createQuery(hql);
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
	public ProcessPoint getProcessPoint(Long id){
		return processDefineDao.get(id);
	}
	public void saveProcessPoint(ProcessPoint processPoint){
		if(StringUtils.isEmpty(processPoint.getCode())){
			throw new RuntimeException("产品编号不能为空!");
		}
		if(StringUtils.isEmpty(processPoint.getName())){
			throw new RuntimeException("产品名称不能为空!");
		}
		if(isExistProcessPoint(processPoint.getId(),processPoint.getCode(),processPoint.getParentIds())){
			throw new RuntimeException("已经相同的编号!");
		}
		processDefineDao.save(processPoint);
		//更新parentIds
		Long id = processPoint.getId();
		if(id == null){
			if(processPoint.getParent()==null){
				processPoint.setParentIds("," + processPoint.getId() + ",");
			}else{
				processPoint.setParentIds(processPoint.getParent().getParentIds() + processPoint.getId() + ",");
			}
			processDefineDao.save(processPoint);
		}
		
		//遍历质量特性，自动建表
		List<QualityFeature> qualityFeatures = processPoint.getQualityFeatures();
		for(QualityFeature qualityFeature : qualityFeatures){
			//自动建表
			qualityFeature.setTargetTableName(null);			
			String tableName;
			try {
				tableName = tableColumnManager.generateTable(qualityFeature,qualityFeatureDao.getSession());
				qualityFeature.setTargetTableName(tableName);
				qualityFeatureDao.save(qualityFeature);
				logUtilDao.debugLog("保存", qualityFeature.toString());
			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}						
		}
	}
	
	public void setPerson(String parentId, String userIds, String userNames) {
		// TODO Auto-generated method stub
		ProcessPoint point = processDefineDao.get(Long.valueOf(parentId));
		System.out.println(point.getName());
		List<QualityFeature> qualityFeatures = point.getQualityFeatures();
		String[] userIdsArr = userIds.split(",");
		String[] userNamesArr = userNames.split(",");
		List<FeaturePerson> featurePersons = new ArrayList<FeaturePerson>();
		for(QualityFeature qu : qualityFeatures){
			qu.getFeaturePersons().clear();
			for(int i=0;i<userIdsArr.length;i++){
				FeaturePerson person = new FeaturePerson();
				person.setName(userNamesArr[i]);
				person.setCode(userIdsArr[i]);
				person.setTargetId(Long.valueOf(userIdsArr[i]));
				person.setModifiedTime(new Date());
				person.setModifier(ContextUtils.getUserName());
				person.setCompanyId(ContextUtils.getCompanyId());
				person.setCreatedTime(new Date());
				person.setCreator(ContextUtils.getUserName());
				featurePersons.add(person);
				person.setQualityFeature(qu);
				qu.getFeaturePersons().add(person);
			}
			qualityFeatureDao.save(qu);
			logUtilDao.debugLog("保存", qu.toString());
		}
	}
	
	public void bathSetRules(String parentId, String ruleIds) {
		// TODO Auto-generated method stub
		ProcessPoint point = processDefineDao.get(Long.valueOf(parentId));
		System.out.println(point.getName());
		List<QualityFeature> qualityFeatures = point.getQualityFeatures();
		String[] ruleIdsArr = ruleIds.split(",");
		for(QualityFeature qu : qualityFeatures){
			qu.getFeatureRules().clear();
			for(int i=0;i<ruleIdsArr.length;i++){
				BsRules rules =  bsRulesManager.getBsRules(Long.valueOf(ruleIdsArr[i]));
				FeatureRules fRules = new FeatureRules();
				fRules.setName(rules.getName());
				fRules.setNo(rules.getNo());
				fRules.setExpression(rules.getExpression());
				fRules.setType(rules.getType());
				fRules.setModel(rules.getModel());
				fRules.setTargetId(Long.valueOf(ruleIdsArr[i]));
				fRules.setModifiedTime(new Date());
				fRules.setModifier(ContextUtils.getUserName());
				fRules.setCompanyId(ContextUtils.getCompanyId());
				fRules.setCreatedTime(new Date());
				fRules.setCreator(ContextUtils.getUserName());
				fRules.setQualityFeature(qu);
				qu.getFeatureRules().add(fRules);
			}
			qualityFeatureDao.save(qu);
			logUtilDao.debugLog("保存", qu.toString());
		}
	}
	
	public void bathSetLayers(String parentId, String layerIds) {
		// TODO Auto-generated method stub
		ProcessPoint point = processDefineDao.get(Long.valueOf(parentId));
		List<QualityFeature> qualityFeatures = point.getQualityFeatures();
		String[] layerIdsArr = layerIds.split(",");
		for(QualityFeature qu : qualityFeatures){
			qu.getFeatureLayers().clear();
			for(String id : layerIdsArr){
				if(!StringUtils.isEmpty(id)){
					LayerType level =  layerTypeManager.getLayerType(Long.valueOf(id));
					FeatureLayer layer = new FeatureLayer();
					layer.setDetailCode(level.getTypeCode());
					layer.setDetailName(level.getTypeName());
					layer.setSampleMethod(level.getSampleMethod());
					layer.setIsInputValue(level.getIsInputValue());
					layer.setTargetId(Long.valueOf(id));
					layer.setModifiedTime(new Date());
					layer.setModifier(ContextUtils.getUserName());
					layer.setCompanyId(ContextUtils.getCompanyId());
					layer.setCreatedTime(new Date());
					layer.setCreator(ContextUtils.getUserName());
					layer.setQualityFeature(qu);
					qu.getFeatureLayers().add(layer);
				}
			}
			qualityFeatureDao.save(qu);
			logUtilDao.debugLog("保存", qu.toString());
		}
	}
	
	public void deleteProcessPoint(String deleteIds){
		String[] ids = deleteIds.split(",");
		Session	session = qualityFeatureDao.getSessionFactory().openSession();
		for(String id : ids){
			ProcessPoint processPoint = processDefineDao.get(Long.valueOf(id));
			if(processPoint.getId() != null){
				if(!processPoint.getChildren().isEmpty()){
					throw new RuntimeException(processPoint.getName() + "还有子节点不能删除，请先删除子节点!");
				}
				logUtilDao.debugLog("删除", processPoint.toString());
				List<String> list=new ArrayList<String>();
				List<QualityFeature> qualityFeatures = processPoint.getQualityFeatures();
				for (QualityFeature qualityFeature : qualityFeatures) {
					String targetName=qualityFeature.getTargetTableName();
					if(targetName!=null){
						list.add(targetName);
					}
				}
				processDefineDao.delete(processPoint);
				//删除质量特性对应的表
				for (String str : list) {
					String sql = "select column_name from user_tab_columns where table_name = ?";
					List<?> list2 = qualityFeatureDao.getSession().createSQLQuery(sql).setParameter(0,str).list();
					if(str!=null&&list2.size()>0){
						sql = "drop table "+str;
						session.createSQLQuery(sql.toString()).executeUpdate();
					}
				}
			}
		}
		session.close();
	}
	public List<ProcessPoint> getAllProcessPoint(){
		return processDefineDao.getAllProcessPoint();
	}
	public List<ProcessPoint> queryProcessPointByDate(Date startDate,Date endDate){
		return processDefineDao.queryProcessPointByDate(startDate, endDate);
	}
	/**
	 * 根据父类查询结构
	 * @param parentId
	 * @return
	 */
	public List<ProcessPoint> getProcessPoints(Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("from ProcessPoint p where p.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		if(parentId == null){
			sb.append(" and p.parent is null");
		}else{
			sb.append(" and p.parent.id = ?");
			params.add(parentId);
		}
		sb.append(" order by p.orderNum");
		return processDefineDao.find(sb.toString(),params.toArray());
	}
	/**
	 * 转换过程节点结构至json对象
	 * @param processPoint
	 * @return
	 */
	public Map<String,Object> convertProcessPoint(ProcessPoint processPoint){
		//焊装SPC,取：焊装
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data",processPoint.getName());
		Map<String,Object> attrMap = new HashMap<String, Object>();
		attrMap.put("id",processPoint.getId());
		attrMap.put("level",processPoint.getLevel());
		attrMap.put("name",processPoint.getName());
		map.put("attr", attrMap);
		if(!processPoint.getChildren().isEmpty()){
			map.put("state","open");
			List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
			for(ProcessPoint child : processPoint.getChildren()){
				children.add(convertProcessPoint(child));
			}
			map.put("children",children);
		}
		return map;
	}
	public ProcessPoint getFirstLevelProcessPoint(){
		return processDefineDao.getFirstLevelProcessPoint();
	}
	public List<ProcessPoint> getList(){
		return processDefineDao.list();
	}
	/**
	 * 转换map对象到options
	 * @param map
	 * @return
	 */
	public List<Option> convertListToOptions(List<ProcessPoint> processPoints){
		List<Option> options = new ArrayList<Option>();
		for(int i=0;i<processPoints.size();i++){
			Option option = new Option();
			ProcessPoint p = processPoints.get(i);
			option.setName(p.getName());
			option.setValue(Long.toString(p.getId()));
			options.add(option);
		}
		return options;
	}
	
	
	public void copyPointInfo(ProcessPoint processPoint, ProcessPoint copyPoint) {
		// TODO Auto-generated method stub
		List<QualityFeature> qualityFeatures = copyPoint.getQualityFeatures();
		List<QualityFeature> newQualityFeatures = new ArrayList<QualityFeature>();
		for(QualityFeature qualityFeature : qualityFeatures){
			QualityFeature q = new QualityFeature();
			q= (QualityFeature) qualityFeature.clone();
			q.setId(null);
			q.setProcessPoint(processPoint);
			List<FeatureRules> newFeatureRules = new ArrayList<FeatureRules>();
			List<FeatureLayer> newFeatureLayers = new ArrayList<FeatureLayer>();
			List<FeaturePerson> newFeaturePersons = new ArrayList<FeaturePerson>();
			q.setFeatureTableColumns(new ArrayList<FeatureTableColumn>());
			List<FeatureRules> copyFeatureRules = qualityFeature.getFeatureRules();
			List<FeatureLayer> copyFeatureLayers = qualityFeature.getFeatureLayers();
			List<FeaturePerson> copyFeaturePersons = qualityFeature.getFeaturePersons();
			for(FeatureRules featureRule : copyFeatureRules){
				FeatureRules newRule = new FeatureRules();
				newRule = (FeatureRules) featureRule.clone();
				newRule.setId(null);
				newRule.setQualityFeature(q);
				newFeatureRules.add(newRule);
			}
			q.setFeatureRules(newFeatureRules);
			
			for(FeatureLayer featureLayer : copyFeatureLayers){
				FeatureLayer newLayer  = new FeatureLayer();
				newLayer = (FeatureLayer) featureLayer.clone();
				newLayer.setId(null);
				newLayer.setQualityFeature(q);
				newFeatureLayers.add(newLayer);
			}
			q.setFeatureLayers(newFeatureLayers);
			
			for(FeaturePerson featurePerson : copyFeaturePersons){
				FeaturePerson newPerson  = new FeaturePerson();
				newPerson = (FeaturePerson) featurePerson.clone();
				newPerson.setId(null);
				newPerson.setQualityFeature(q);
				newFeaturePersons.add(newPerson);
			}
			q.setFeaturePersons(newFeaturePersons);
			List<ControlLimit> newControlLimits = new ArrayList<ControlLimit>();
			List<ControlLimit> copyControlLimits = qualityFeature.getControlLimits();
			for(ControlLimit controlLimit : copyControlLimits){
				ControlLimit newControlLimit = new ControlLimit();
				newControlLimit = (ControlLimit) controlLimit.clone();
				newControlLimit.setId(null);
				newControlLimit.setQualityFeature(q);
				newControlLimits.add(newControlLimit);
			}
			q.setAbnormalInfos(new ArrayList<AbnormalInfo>());
			q.setControlLimits(newControlLimits);
			q.setReasonMeasures(new ArrayList<ReasonMeasure>());
			q.setSpcSubGroups(new ArrayList<SpcSubGroup>());
			newQualityFeatures.add(q);
		}
		processPoint.setQualityFeatures(newQualityFeatures);
	}
	
}
