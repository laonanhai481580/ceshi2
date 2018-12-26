package com.ambition.spc.processdefine.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.entity.BsRules;
import com.ambition.spc.entity.ControlLimit;
import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.FeaturePerson;
import com.ambition.spc.entity.FeatureRules;
import com.ambition.spc.entity.LayerType;
import com.ambition.spc.entity.ProcessPoint;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.importutil.service.SpcMonitorManager;
import com.ambition.spc.processdefine.dao.FeaturePersonDao;
import com.ambition.spc.processdefine.dao.QualityFeatureDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**    
 * QualityFeatureManager.java
 * @authorBy YUKE
 *
 */
@Service
public class QualityFeatureManager {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private QualityFeatureDao qualityFeatureDao;
 	@Autowired
 	private FeaturePersonDao featurePersonDao;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private AcsUtils acsUtils;
	@Autowired
	private FeatureTableColumnManager tableColumnManager;
	@Autowired
	private SpcMonitorManager spcMonitorManager;
	//缓存锁
	private Object lockObj = new Object();
	//缓存对象
	private static Map<String,QualityFeature> cacheFeatureMap = new HashMap<String, QualityFeature>();
	public QualityFeature getQualityFeature(Long id){
		return qualityFeatureDao.get(id);
	}
	public QualityFeature getQualityFeatureFromCache(Long id,Session session){
		if(session == null){
			session = qualityFeatureDao.getSession();
		}
		id = id==null?0l:id;
		String idStr = id + "";
		synchronized (lockObj) {
			if(!cacheFeatureMap.containsKey(idStr)){
				String hql = "from QualityFeature f where f.id = ?";
				List<?> list = session.createQuery(hql).setParameter(0,id).list();
				if(list.size()>0){
					QualityFeature qualityFeature = (QualityFeature)list.get(0);
					try {
						QualityFeature cacheFeature = (QualityFeature)BeanUtils.cloneBean(qualityFeature);
						
						//子表
						List<FeatureLayer> featureLayers = new ArrayList<FeatureLayer>();
						for(FeatureLayer featureLayer : cacheFeature.getFeatureLayers()){
							featureLayers.add((FeatureLayer)BeanUtils.cloneBean(featureLayer));
						}
						cacheFeature.setFeatureLayers(featureLayers);
						
						List<FeatureRules> cacheFeatureRules = new ArrayList<FeatureRules>();
						for(FeatureRules featureRules : cacheFeature.getFeatureRules()){
							cacheFeatureRules.add((FeatureRules)BeanUtils.cloneBean(featureRules));
						}
						cacheFeature.setFeatureRules(cacheFeatureRules);
						
						List<FeaturePerson> cacheFeaturePersons = new ArrayList<FeaturePerson>();
						for(FeaturePerson featurePerson : cacheFeature.getFeaturePersons()){
							cacheFeaturePersons.add((FeaturePerson)BeanUtils.cloneBean(featurePerson));
						}
						cacheFeature.setFeaturePersons(cacheFeaturePersons);
						
						List<ControlLimit> cacheQualityContrlimits = new ArrayList<ControlLimit>();
						for(ControlLimit controlLimit : cacheFeature.getControlLimits()){
							cacheQualityContrlimits.add((ControlLimit)BeanUtils.cloneBean(controlLimit));
						}
						cacheFeature.setControlLimits(cacheQualityContrlimits);
						
						cacheFeatureMap.put(idStr,cacheFeature);
					} catch (Exception e) {
						log.error("缓存对象失败!",e);
					}
				}
			}
		}
		return cacheFeatureMap.get(idStr);
	}
	/**
	* 方法名: 清除缓存
	* <p>功能说明：</p>
	* @return void
	* @throws
	 */
	public void clearCache(Long featueId){
		synchronized (lockObj) {
			if(featueId==null){
				cacheFeatureMap.clear();
			}else{
				cacheFeatureMap.remove(featueId.toString());
			}
		}
	}
	public QualityFeature getQualityFeatureById(Long id){
		List<QualityFeature> list = qualityFeatureDao.find("from QualityFeature f where f.companyId = ? and f.id = ?", ContextUtils.getCompanyId(),id);
		if(list.size()>0){
		return list.get(0); 
		}else{
			return null;
		}
	}
	
	public FeatureLayer getFeatureLayer(Long id){
		List<FeatureLayer> list = qualityFeatureDao.find("from FeatureLayer f where f.companyId = ? and f.id = ?", ContextUtils.getCompanyId(),id);
		if(list.isEmpty()){
			return null;
		}
		return list.get(0);
	}
	public FeaturePerson getFeaturePerson(Long id){
		List<FeaturePerson> list = featurePersonDao.find("from FeaturePerson f where f.companyId = ? and f.id = ?", ContextUtils.getCompanyId(),id);
		return list.get(0);
	}
	private Boolean isExistQualityFeature(String name,Long id,ProcessPoint processPoint){
		String hql = "select count(id) from QualityFeature q where q.companyId = ? and q.name = ? and q.processPoint = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(name);
		params.add(processPoint);
		if(id != null){
			hql += " and q.id <> ?";
			params.add(id);
		}
		List<?> list = qualityFeatureDao.find(hql,params.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	
	private Boolean isExistQualityFeatureCode(String code,Long id,ProcessPoint processPoint){
		String hql = "select count(id) from QualityFeature q where q.companyId = ? and q.code = ? and q.processPoint = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(code);
		params.add(processPoint);
		if(id != null){
			hql += " and q.id <> ?";
			params.add(id);
		}
		List<?> list = qualityFeatureDao.find(hql,params.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	@Transactional
	public void saveQualityFeature(QualityFeature qualityFeature) throws HibernateException, SQLException{
		if(StringUtils.isEmpty(qualityFeature.getName())){
			throw new RuntimeException("名称不能为空!");
		}
		if(isExistQualityFeature(qualityFeature.getName(),qualityFeature.getId(),qualityFeature.getProcessPoint())){
			throw new RuntimeException("已经存在了相同的名称!");
		}
		if(StringUtils.isEmpty(qualityFeature.getCode())){
			qualityFeature.setCode(qualityFeature.getName());
		}
		if(isExistQualityFeatureCode(qualityFeature.getCode(),qualityFeature.getId(),qualityFeature.getProcessPoint())){
			throw new RuntimeException("已经存在了相反的简码!");
		}else{
			Double index = 0.000;
			if(Struts2Utils.getParameter("add")!=null){
				index = Double.valueOf(Struts2Utils.getParameter("add")); 
				qualityFeature.setUpperTarge(qualityFeature.getTargeValue()+index);
			}
			if(Struts2Utils.getParameter("del")!=null){
				index = Double.valueOf(Struts2Utils.getParameter("del")); 
				qualityFeature.setLowerTarge(qualityFeature.getTargeValue()-index);
			}
			if("single-u".equals(qualityFeature.getSpecificationType())){//单侧上公差
				qualityFeature.setLowerLimit(null);//合限下限
				qualityFeature.setLowerTarge(null);//规格下限
			}else if("single-l".equals(qualityFeature.getSpecificationType())){//单侧下公差
				qualityFeature.setUpperLimit(null);//合理上限
				qualityFeature.setUpperTarge(null);//规格上限
			}
			//保存UCL、CL、LCL
			String state = Struts2Utils.getParameter("isAutoCl");
			if(state != null && state.equals("N")){
				ControlLimit limit = new ControlLimit();
				limit.setCompanyId(ContextUtils.getCompanyId());
				limit.setCreatedTime(new Date());
				limit.setCreator(ContextUtils.getUserName());
				limit.setModifiedTime(new Date());
				limit.setModifier(ContextUtils.getUserName());
				limit.setXucl(Double.valueOf(Struts2Utils.getParameter("ucl1")));
				limit.setXcl(Double.valueOf(Struts2Utils.getParameter("cl1")));
				limit.setXlcl(Double.valueOf(Struts2Utils.getParameter("lcl1")));
				limit.setSucl(Double.valueOf(Struts2Utils.getParameter("ucl2")));
				limit.setScl(Double.valueOf(Struts2Utils.getParameter("cl2")));
				limit.setSlcl(Double.valueOf(Struts2Utils.getParameter("lcl2")));
				limit.setQualityFeature(qualityFeature);
				qualityFeature.getControlLimits().add(limit);
			}
			
			String ids[] = null;
			String nos[] = null;
			if(Struts2Utils.getParameter("judgeIds")!=null && !Struts2Utils.getParameter("judgeIds").equals("")){
				ids = Struts2Utils.getParameter("judgeIds").split(",");
				nos = Struts2Utils.getParameter("nos").split(",");
				//删除历史的准则数据
//				qualityFeature.getFeatureRules().clear();
				Map<String,Object> frsMap = new HashMap<String, Object>();//旧数据
				Map<String,Object> newFrsMap = new HashMap<String, Object>();//新数据
				Map<String,Object> listFrsMap = new HashMap<String, Object>();//页面数据
				List<FeatureRules> frs=qualityFeature.getFeatureRules();
				for (int i = 0; i < frs.size(); i++) {
					frsMap.put(frs.get(i).getNo(),frs.get(i));
				}
				for (int i = 0; i < nos.length; i++) {
					listFrsMap.put(ids[i],nos[i]);
				}
				for(String id : ids){
					if(!StringUtils.isEmpty(id)){
						if(frsMap.containsKey(listFrsMap.get(id))){
							newFrsMap.put(listFrsMap.get(id).toString(),frsMap.get(listFrsMap.get(id)));
						}
//						BsRules rules =  bsRulesManager.getBsRules(Long.valueOf(id));
						String hql = "from BsRules b where (b.id = ? or b.no = ?)";
						List<?> brs=qualityFeatureDao.find(hql,Long.valueOf(id),listFrsMap.get(id));
						if(!brs.isEmpty()){
							BsRules rules = (BsRules) brs.get(0);
							FeatureRules fRules = new FeatureRules();
							fRules.setName(rules.getName());
							fRules.setNo(rules.getNo());
							fRules.setExpression(rules.getExpression());
							fRules.setType(rules.getType());
							fRules.setModel(rules.getModel());
							fRules.setTargetId(Long.valueOf(id));
							fRules.setModifiedTime(new Date());
							fRules.setModifier(ContextUtils.getUserName());
							fRules.setCompanyId(ContextUtils.getCompanyId());
							fRules.setCreatedTime(new Date());
							fRules.setCreator(ContextUtils.getUserName());
							fRules.setQualityFeature(qualityFeature);
							if(!newFrsMap.containsKey(fRules.getNo())){
								newFrsMap.put(fRules.getNo(), fRules);
							}
						}
					}
				}
				//删除历史的准则数据
				qualityFeature.getFeatureRules().clear();
				for (String in : newFrsMap.keySet()) {
						qualityFeature.getFeatureRules().add((FeatureRules) newFrsMap.get(in));
					}
			}
			
			if(Struts2Utils.getParameter("levelIds")!=null && !Struts2Utils.getParameter("levelIds").equals("")){
				ids = Struts2Utils.getParameter("levelIds").split(",");
				nos = Struts2Utils.getParameter("detailCodes").split(",");
				//删除历史的层别信息数据
//				qualityFeature.getFeatureLayers().clear();
				Map<String,Object> flsMap = new HashMap<String, Object>();//旧数据
				Map<String,Object> newFlsMap = new HashMap<String, Object>();//新数据
				Map<String,Object> listFlsMap = new HashMap<String, Object>();//页面数据
				List<FeatureLayer> fls=qualityFeature.getFeatureLayers();
				for (int i = 0; i < fls.size(); i++) {
					flsMap.put(fls.get(i).getDetailCode(),fls.get(i));
				}
				for (int i = 0; i < nos.length; i++) {
					listFlsMap.put(ids[i],nos[i]);
				}
				for(String id : ids){
					if(!StringUtils.isEmpty(id)){
						if(flsMap.containsKey(listFlsMap.get(id))){
							newFlsMap.put(listFlsMap.get(id).toString(),flsMap.get(listFlsMap.get(id)));
						}
//						LayerType level =  layerTypeManager.getLayerType(Long.valueOf(id));
						String hql = "from LayerType lt where lt.id = ? or lt.typeCode = ?";
						List<?> lts=qualityFeatureDao.find(hql,Long.valueOf(id),listFlsMap.get(id));
						if(!lts.isEmpty()){
							LayerType level = (LayerType) lts.get(0);
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
							layer.setQualityFeature(qualityFeature);
							if(!newFlsMap.containsKey(layer.getDetailCode())){
								newFlsMap.put(layer.getDetailCode(), layer);
							}
						}
					}
				}
				//删除历史的层别信息数据
				qualityFeature.getFeatureLayers().clear();
				for (String in : newFlsMap.keySet()) {
					qualityFeature.getFeatureLayers().add((FeatureLayer) newFlsMap.get(in));
					}
			}
			
			if(Struts2Utils.getParameter("personStrs")!=null && !Struts2Utils.getParameter("personStrs").equals("")){
				String personStrs = Struts2Utils.getParameter("personStrs");
				JSONArray personArray = null;
				if(StringUtils.isNotEmpty(personStrs)){
					personArray = JSONArray.fromObject(personStrs);
				}
				//删除历史的异常通知人员数据
				qualityFeature.getFeaturePersons().clear();
				for(int i = 0;i<personArray.size();i++){
					FeaturePerson person = new FeaturePerson();
					person.setName(personArray.getJSONObject(i).get("name").toString());
					person.setCode(personArray.getJSONObject(i).get("id").toString());
					person.setTargetId(Long.valueOf(personArray.getJSONObject(i).get("id").toString()));
					person.setModifiedTime(new Date());
					person.setModifier(ContextUtils.getUserName());
					person.setCompanyId(ContextUtils.getCompanyId());
					person.setCreatedTime(new Date());
					person.setCreator(ContextUtils.getUserName());
					person.setQualityFeature(qualityFeature);
					qualityFeature.getFeaturePersons().add(person);
				}
			}
			String isNoAccept = Struts2Utils.getParameter("isNoAccept");
			if(isNoAccept != null && !"".equals(isNoAccept)){
				qualityFeature.setIsNoAccept(true);
			}else{
				qualityFeature.setIsNoAccept(false);
			}
			//设置排序号
			if(qualityFeature.getId()==null
					&&qualityFeature.getOrderNum()==0){
				List<?> maxList = qualityFeatureDao.createQuery("select max(q.orderNum) from QualityFeature q where q.processPoint = ?",qualityFeature.getProcessPoint()).list();
				if(maxList.size()>0&&maxList.get(0)!=null){
					qualityFeature.setOrderNum(Integer.valueOf(maxList.get(0).toString())+1);
				}else{
					qualityFeature.setOrderNum(1);
				}
			}
			qualityFeatureDao.save(qualityFeature);
			
			//自动建表
			String tableName = tableColumnManager.generateTable(qualityFeature,qualityFeatureDao.getSession());
			qualityFeature.setTargetTableName(tableName);
			qualityFeatureDao.save(qualityFeature);
			logUtilDao.debugLog("保存", qualityFeature.toString());
		}
		//清除缓存
		clearCache(qualityFeature.getId());
		//清除监控缓存
		spcMonitorManager.clearMonitorCaches(qualityFeature.getId().toString());
	}
	@Transactional
	public void deleteQualityFeature(String deleteIds){
		Session	session = qualityFeatureDao.getSessionFactory().openSession();
		for(String id : deleteIds.split(",")){
			if(StringUtils.isNotEmpty(id)){
				QualityFeature qualityFeature = qualityFeatureDao.get(Long.valueOf(id));
				logUtilDao.debugLog("删除", qualityFeature.toString());
				qualityFeatureDao.delete(qualityFeature);
				//清除缓存
				clearCache(qualityFeature.getId());
				//删除对应的表
				String sql = "select column_name from user_tab_columns where table_name = ?";
				List<?> list = qualityFeatureDao.getSession().createSQLQuery(sql).setParameter(0,qualityFeature.getTargetTableName()).list();
				if(qualityFeature.getTargetTableName()!=null&&list.size()>0){
					sql = "drop table "+qualityFeature.getTargetTableName();
					session.createSQLQuery(sql.toString()).executeUpdate();
				}
			}
		}
		session.close();
	}
	public List<QualityFeature> getQualityFeatures(){
		return qualityFeatureDao.getQualityFeatures();
	}
	public Page<QualityFeature> getPage(Page<QualityFeature> page,ProcessPoint processPoint){
		return qualityFeatureDao.list(page, processPoint);
	}
	public Page<QualityFeature> getPage(Page<QualityFeature> page,ProcessPoint processPoint,JSONObject params){
		return qualityFeatureDao.list(page, processPoint,params);
	}
	public List<QualityFeature> getList(){
		return qualityFeatureDao.list();
	}
	
	/**
	  * 方法名:随机查询第一个质量特性
	  * <p>功能说明：</p>
	  * @return
	 */
	public QualityFeature queryFirst(){
		String hql = "from QualityFeature q";
		Query query = qualityFeatureDao.createQuery(hql);
		query.setMaxResults(1);
		List<?> list = query.list();
		if(list.isEmpty()){
			return null;
		}else{
			return (QualityFeature)list.get(0);
		}
	}

	public QualityFeature getQualityFeatureByIdNew(Long id,Session session){
		Query query = session.createQuery("from QualityFeature f where f.companyId = ? and f.id = ?").setParameter(0, ContextUtils.getCompanyId()).setParameter(1, id);
		@SuppressWarnings("unchecked")
		List<QualityFeature> list = query.list();
		if(list.size()>0){
		return list.get(0); 
		}else{
			return null;
		}
	}
	
	public Map<String,Object> convertQualityFeatureToMap(QualityFeature qualityFeature){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data",qualityFeature.getName());
		Map<String,Object> attrMap = new HashMap<String, Object>();
		attrMap.put("id",qualityFeature.getId());
		attrMap.put("name",qualityFeature.getName());
		attrMap.put("orderNum",qualityFeature.getOrderNum());
		attrMap.put("isLeaf",true);
		map.put("attr",attrMap);
		return map;
	}
	public Page<QualityFeature> getPageByMoint(Page<QualityFeature> page,String monitPointId){
		return qualityFeatureDao.listByMonit(page, monitPointId);
	}
	public ControlLimit getControlLimitByDesc(QualityFeature qualityFeature){
		String hql="from ControlLimit c where c.companyId = ? and c.qualityFeature = ? order by c.id desc";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(qualityFeature);
		Query query = qualityFeatureDao.getSession().createQuery(hql);
		for(int i=0;i<params.size();i++){
			query.setParameter(i, params.get(i));
		}
		@SuppressWarnings("unchecked")
		List<ControlLimit> list = query.list();
		if(list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	/**
	  * 方法名: 复制特性
	  * <p>功能说明：</p>
	  * @param featureId 源特性ID
	  * @param copyItems 复制的特性
	 */
	@Transactional
	public String copyQualityFeature(Long featureId,JSONArray copyItems){
		QualityFeature sourceFeature = qualityFeatureDao.get(featureId);
		int errorCount = 0;
		int orderNum = sourceFeature.getOrderNum()+1;
		for(Object obj : copyItems){
			JSONObject  json = (JSONObject)obj;
			String code = json.getString("code");
			String name = json.getString("name");
			if(isExistQualityFeature(name,null,sourceFeature.getProcessPoint())){
				errorCount++;
				continue;
			}else if(isExistQualityFeatureCode(code,null,sourceFeature.getProcessPoint())){
				errorCount++;
				continue;
			}
			QualityFeature targetFeature = new QualityFeature();
			targetFeature.setCompanyId(ContextUtils.getCompanyId());
			targetFeature.setCreator(ContextUtils.getUserName());
			targetFeature.setCreatedTime(new Date());
			targetFeature.setModifier(ContextUtils.getUserName());
			targetFeature.setModifiedTime(new Date());
			targetFeature.setOrderNum(orderNum++);
			targetFeature.setName(name);
			targetFeature.setCode(code);
			targetFeature.setParamType(sourceFeature.getParamType());
			targetFeature.setSpecificationType(sourceFeature.getSpecificationType());
			targetFeature.setTargeValue(sourceFeature.getTargeValue());
			targetFeature.setUpperTarge(sourceFeature.getUpperTarge());
			targetFeature.setLowerTarge(sourceFeature.getLowerTarge());
			targetFeature.setSampleCapacity(sourceFeature.getSampleCapacity());
			targetFeature.setEffectiveCapacity(sourceFeature.getEffectiveCapacity());
			targetFeature.setControlChart(sourceFeature.getControlChart());
			targetFeature.setRangeInterval(sourceFeature.getRangeInterval());
			targetFeature.setPrecs(sourceFeature.getPrecs());
			targetFeature.setUnit(sourceFeature.getUnit());
			targetFeature.setUpperLimit(sourceFeature.getUpperLimit());
			targetFeature.setLowerLimit(sourceFeature.getLowerLimit());
			targetFeature.setIsNoAccept(sourceFeature.getIsNoAccept());
			targetFeature.setIsAutoCl(sourceFeature.getIsAutoCl());
			
			targetFeature.setMultiple(sourceFeature.getMultiple());
			targetFeature.setState(sourceFeature.getState());
			targetFeature.setMethod(sourceFeature.getMethod());
			targetFeature.setU(sourceFeature.getU());
			targetFeature.setCpk(sourceFeature.getCpk());
			targetFeature.setUcl1(sourceFeature.getUcl1());
			targetFeature.setUcl2(sourceFeature.getUcl2());
			targetFeature.setCl1(sourceFeature.getCl1());
			targetFeature.setCl2(sourceFeature.getCl2());
			targetFeature.setLcl1(sourceFeature.getLcl1());
			targetFeature.setLcl2(sourceFeature.getLcl2());
			targetFeature.setUclMin(sourceFeature.getUclMin());
			targetFeature.setUclMax(sourceFeature.getUclMax());
			targetFeature.setLclMin(sourceFeature.getLclMin());
			targetFeature.setLclMax(sourceFeature.getLclMax());
			targetFeature.setUclCurrent1(sourceFeature.getUclCurrent1());
			targetFeature.setUclCurrent2(sourceFeature.getUclCurrent2());
			targetFeature.setClCurrent1(sourceFeature.getClCurrent1());
			targetFeature.setClCurrent2(sourceFeature.getClCurrent2());
			targetFeature.setLclCurrent1(sourceFeature.getLclCurrent1());
			targetFeature.setLclCurrent2(sourceFeature.getLclCurrent2());
			
			targetFeature.setCriterionName(sourceFeature.getCriterionName());
			targetFeature.setIsAuto(sourceFeature.getIsAuto());
			targetFeature.setCriterionChart(sourceFeature.getCriterionChart());
			targetFeature.setCriterionType(sourceFeature.getCriterionType());
			targetFeature.setTendType(sourceFeature.getTendType());
			targetFeature.setRunType(sourceFeature.getRunType());
			targetFeature.setPoint1(sourceFeature.getPoint1());
			targetFeature.setPoint2(sourceFeature.getPoint2());
			targetFeature.setPoint2_1(sourceFeature.getPoint2_1());
			targetFeature.setPoint3(sourceFeature.getPoint4());
			targetFeature.setPoint4(sourceFeature.getPoint4());
			targetFeature.setPoint4_1(sourceFeature.getPoint4_1());

			targetFeature.setLevelName(sourceFeature.getLevelName());
			targetFeature.setLevelCode(sourceFeature.getLevelCode());
			targetFeature.setPredictDbk(sourceFeature.getPredictDbk());
			targetFeature.setRecordDbkl(sourceFeature.getRecordDbkl());
			
			targetFeature.setProcessPoint(sourceFeature.getProcessPoint());
			//判断准则
			List<FeatureRules> featureRules = new ArrayList<FeatureRules>();
			for(FeatureRules sourceRule : sourceFeature.getFeatureRules()){
				FeatureRules targetRule = new FeatureRules();
				targetRule.setCompanyId(sourceFeature.getCompanyId());
				targetRule.setCompanyId(ContextUtils.getCompanyId());
				targetRule.setCreator(ContextUtils.getUserName());
				targetRule.setCreatedTime(new Date());
				targetRule.setModifier(ContextUtils.getUserName());
				targetRule.setModifiedTime(new Date());
				targetRule.setName(sourceRule.getName());
				targetRule.setNo(sourceRule.getNo());
				targetRule.setExpression(sourceRule.getExpression());
				targetRule.setType(sourceRule.getType());
				targetRule.setModel(sourceRule.getModel());
				targetRule.setTargetId(sourceRule.getTargetId());
				targetRule.setQualityFeature(targetFeature);
				featureRules.add(targetRule);
			}
			targetFeature.setFeatureRules(featureRules);
			
			//层别信息
			List<FeatureLayer> featureLayers = new ArrayList<FeatureLayer>();
			for(FeatureLayer sourceLayer : sourceFeature.getFeatureLayers()){
				FeatureLayer targetLayer = new FeatureLayer();
				targetLayer.setCompanyId(sourceFeature.getCompanyId());
				targetLayer.setCompanyId(ContextUtils.getCompanyId());
				targetLayer.setCreator(ContextUtils.getUserName());
				targetLayer.setCreatedTime(new Date());
				targetLayer.setModifier(ContextUtils.getUserName());
				targetLayer.setModifiedTime(new Date());
				
				targetLayer.setDetailName(sourceLayer.getDetailName());
				targetLayer.setDetailCode(sourceLayer.getDetailCode());
				targetLayer.setSampleMethod(sourceLayer.getSampleMethod());
				targetLayer.setIsInputValue(sourceLayer.getIsInputValue());
				targetLayer.setTargetId(sourceLayer.getTargetId());
				targetLayer.setQualityFeature(targetFeature);
				featureLayers.add(targetLayer);
			}
			targetFeature.setFeatureLayers(featureLayers);
			
			//控制限
			List<ControlLimit> controlLimits = new ArrayList<ControlLimit>();
			for(ControlLimit sourceLimit : sourceFeature.getControlLimits()){
				ControlLimit targetLimit = new ControlLimit();
				targetLimit.setCompanyId(sourceFeature.getCompanyId());
				targetLimit.setCompanyId(ContextUtils.getCompanyId());
				targetLimit.setCreator(ContextUtils.getUserName());
				targetLimit.setCreatedTime(new Date());
				targetLimit.setModifier(ContextUtils.getUserName());
				targetLimit.setModifiedTime(new Date());
				
				targetLimit.setXucl(sourceLimit.getXucl());
				targetLimit.setXcl(sourceLimit.getXcl());
				targetLimit.setXlcl(sourceLimit.getXlcl());
				targetLimit.setSucl(sourceLimit.getSucl());
				targetLimit.setScl(sourceLimit.getScl());
				targetLimit.setSlcl(sourceLimit.getSlcl());
				targetLimit.setQualityFeature(targetFeature);
				controlLimits.add(targetLimit);
			}
			targetFeature.setControlLimits(controlLimits);
			
			//异常通知人员
			List<FeaturePerson> featurePersons = new ArrayList<FeaturePerson>();
			for(FeaturePerson sourcePerson : sourceFeature.getFeaturePersons()){
				FeaturePerson targetPerson = new FeaturePerson();
				targetPerson.setCompanyId(sourceFeature.getCompanyId());
				targetPerson.setCompanyId(ContextUtils.getCompanyId());
				targetPerson.setCreator(ContextUtils.getUserName());
				targetPerson.setCreatedTime(new Date());
				targetPerson.setModifier(ContextUtils.getUserName());
				targetPerson.setModifiedTime(new Date());
				
				targetPerson.setName(sourcePerson.getName());
				targetPerson.setCode(sourcePerson.getCode());
				targetPerson.setEmail(sourcePerson.getEmail());
				targetPerson.setTargetId(sourcePerson.getTargetId());
				targetPerson.setQualityFeature(targetFeature);
				featurePersons.add(targetPerson);
			}
			targetFeature.setFeaturePersons(featurePersons);
			
			qualityFeatureDao.save(targetFeature);
		}
		if(errorCount==0){
			return "复制完成,成功" + copyItems.size() + "条!";
		}else{
			int success = copyItems.size()-errorCount;
			return "复制完成,成功" + success + "条,失败" + errorCount + "条!";
		}
	}
	
	public QualityFeature getQualityFeatureByCode(String code){
		return qualityFeatureDao.getQualityFeatureByCode(code);
	}
	
	/**
	  * 方法名:数据采集时查询质量特性清单
	  * <p>功能说明：</p>
	  * @return
	 */
	public JSONObject queryQualityFeatures(Page<Object> page,String featureName){
		String hql = "from QualityFeature q where q.companyId = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(StringUtils.isNotEmpty(featureName)){
			hql += " and (q.name like ? or q.processPoint.name like ?)";
			searchParams.add("%"+featureName+"%");
			searchParams.add("%"+featureName+"%");
		}
		Query query = qualityFeatureDao.createQuery("select count(*) " + hql,searchParams.toArray());
		List<?> list = query.list();
		Long total = Long.valueOf(list.get(0).toString());
		page.setTotalCount(total);
		
		if(StringUtils.isNotEmpty(page.getOrderBy())){
			hql += " order by " + page.getOrderBy() + " " + page.getOrder();
		}
		
		query = qualityFeatureDao.createQuery(hql,searchParams.toArray());
		query.setFirstResult(page.getFirst()-1);
		query.setMaxResults(page.getPageSize());
		List<QualityFeature> qualityFeatures = new ArrayList<QualityFeature>();
		List<QualityFeature> qualityFeatureList = query.list();
		boolean flag=	acsUtils.isAuthority("spc_all", ContextUtils.getUserId(), ContextUtils.getCompanyId());
		for (QualityFeature qualityFeature : qualityFeatureList) {
			ProcessPoint processPoint=qualityFeature.getProcessPoint();
			String name="";
			while (processPoint.getParent()!=null) {
				processPoint=processPoint.getParent();			
			}
			name=processPoint.getName();
			if(!flag&&ContextUtils.getSubCompanyName()!=null){
				if(ContextUtils.getSubCompanyName().equals(name)){
					qualityFeatures.add(qualityFeature);
				}
			}else{
				qualityFeatures.add(qualityFeature);
			}
		}				
		JSONObject pageJson = new JSONObject();
		pageJson.put("page",page.getPageNo());
		pageJson.put("total", page.getTotalPages());
		pageJson.put("records", total);
		
		JSONArray rows = new JSONArray();
		for(QualityFeature feature : qualityFeatures){
			JSONObject row = new JSONObject();
			row.put("id",feature.getId());
			row.put("code",feature.getCode());
			String name = feature.getProcessPoint()==null?"":feature.getProcessPoint().getName();
			if(name.length()>0){
				name += "\\";
			}
			name += feature.getName();
			row.put("name",name);
			row.put("isMigration", feature.getIsMigration());
			row.put("sampleCapacity", feature.getSampleCapacity());
			row.put("upperLimit", feature.getUpperLimit());
			row.put("lowerLimit", feature.getLowerLimit());
			row.put("dataNum",queryTotalGroupNum(feature));
			rows.add(row);
		}
		pageJson.put("rows",rows);
		return pageJson;
	}
	/**
	  * 方法名:查询子组数量
	  * <p>功能说明：</p>
	  * @return
	 */
	private Long queryTotalGroupNum(QualityFeature feature){
		Long totalGroups = null;
		//有引用的表时
		if(StringUtils.isNotEmpty(feature.getTargetTableName())){
			String sql = "select count(*) from " + feature.getTargetTableName();
			System.out.println(feature.getTargetTableName());
			List<?> list = qualityFeatureDao.getSession().createSQLQuery(sql).list();
			Long totalNum = Long.valueOf(list.get(0).toString());
			Integer sampleCapacity = feature.getSampleCapacity();
			if(sampleCapacity == null){
				sampleCapacity = 1;
			}
			totalGroups = totalNum/sampleCapacity;
			if(totalNum%sampleCapacity>0){
				totalGroups++;
			}
		}else{
			String sql = "select count(*) from SPC_SUB_GROUP where FK_QUALITY_FEATURE_ID = " + feature.getId();
			List<?> list = qualityFeatureDao.getSession().createSQLQuery(sql).list();
			totalGroups = Long.valueOf(list.get(0).toString());
		}
		return totalGroups;
	}
}
