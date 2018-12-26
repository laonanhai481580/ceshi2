package com.ambition.spc.monitprogram.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.abnormal.service.AbnormalInfoManager;
import com.ambition.spc.entity.AbnormalInfo;
import com.ambition.spc.entity.MonitPoint;
import com.ambition.spc.entity.MonitProgram;
import com.ambition.spc.entity.MonitQualityFeature;
import com.ambition.spc.entity.ProcessPoint;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.monitprogram.dao.MonitProgramDao;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;

/**    
 * MonitProgramManager.java
 * @authorBy wanglf
 *
 */
@Service
@Transactional
public class MonitProgramManager {
	@Autowired
	private MonitProgramDao monitProgramDao;
	@Autowired
	private AbnormalInfoManager abnormalInfoManager;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
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
	private Boolean isExistMonitProgram(Long id,String code,String parentIds){
		String hql = "select count(*) from MonitProgram m where m.companyId = ? and m.code = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(code);
		if(parentIds != null){
			hql += " and m.parentIds like ?";
			params.add(parentIds + "%");
		}
		if(id != null){
			hql += " and m.id <> ?";
			params.add(id);
		}
		Query query = monitProgramDao.getSession().createQuery(hql);
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
	public MonitProgram getMonitProgram(Long id){
		return monitProgramDao.get(id);
	}
	public void saveMonitProgram(MonitProgram monitProgram){
		if(StringUtils.isEmpty(monitProgram.getCode())){
			throw new RuntimeException("监控方案编号不能为空!");
		}
		if(StringUtils.isEmpty(monitProgram.getName())){
			throw new RuntimeException("监控方案名称不能为空!");
		}
		if(isExistMonitProgram(monitProgram.getId(),monitProgram.getCode(),monitProgram.getParentIds())){
			throw new RuntimeException("已经相同的编号!");
		}
		monitProgramDao.save(monitProgram);
		//更新parentIds
		Long id = monitProgram.getId();
		if(id == null){
			if(monitProgram.getParent()==null){
				monitProgram.setParentIds("," + monitProgram.getId() + ",");
			}else{
				monitProgram.setParentIds(monitProgram.getParent().getParentIds() + monitProgram.getId() + ",");
			}
			monitProgramDao.save(monitProgram);
		}
	}
	
	public void saveOnlyMonitProgram(MonitProgram monitProgram){
		monitProgramDao.save(monitProgram);
	}
	public void deleteMonitProgram(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			MonitProgram monitProgram = monitProgramDao.get(Long.valueOf(id));
			if(monitProgram.getId() != null){
				if(!monitProgram.getChildren().isEmpty()){
					throw new RuntimeException(monitProgram.getName() + "还有子节点不能删除，请先删除子节点!");
				}
				logUtilDao.debugLog("删除", monitProgram.toString());
				monitProgramDao.delete(monitProgram);
			}
		}
	}
	
	
	public List<MonitProgram> getAllMonitProgram(){
		return monitProgramDao.getAllMonitProgram();
	}
	
	/**
	 * 根据父类查询结构
	 * @param parentId
	 * @return
	 */
	public List<MonitProgram> getMonitPrograms(Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("from MonitProgram m where m.companyId = ? ");
		params.add(ContextUtils.getCompanyId());
		if(null == parentId){
			sb.append(" and m.parent is null ");
		}else{
			sb.append(" and m.parent.id = ?");
			params.add(parentId);
		}
		sb.append(" order by m.orderNum ");
		return  monitProgramDao.find(sb.toString(),params.toArray());
	}
	
	
	/**
	 * 转换过程节点结构至json对象
	 * @param processPoint
	 * @return
	 */
	public Map<String,Object> convertMonitProgram(MonitProgram monitProgram){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data",monitProgram.getName());
		Map<String,Object> attrMap = new HashMap<String, Object>();
		attrMap.put("id",monitProgram.getId());
		attrMap.put("level",monitProgram.getLevel());
		attrMap.put("name",monitProgram.getName());
		map.put("attr", attrMap);
		if(!monitProgram.getChildren().isEmpty()){
			map.put("state","open");
			List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
			for(MonitProgram child : monitProgram.getChildren()){
				children.add(convertMonitProgram(child));
			}
			map.put("children",children);
		}
		return map;
	}
	
	public MonitProgram getFirstLevelMonitProgram(){
		return monitProgramDao.getFirstLevelMonitProgram();
	}
	public List<MonitProgram> getList(){
		return monitProgramDao.list();
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
	
	@SuppressWarnings("deprecation")
	public JSONObject convertToJson(MonitPoint monitPoint){
		JSONObject json = new JSONObject();
		json.put("id",monitPoint.getId());
		json.put("name",monitPoint.getPointName());;
		json.put("myLeft",monitPoint.getMyLeft());
		json.put("myTop",monitPoint.getMyTop());
		json.put("imageWidth",monitPoint.getImageWidth());
		json.put("imageHeight",monitPoint.getImageHeight());
		//通过异常信息判断灯的颜色
		List<MonitQualityFeature> monitQualityFeatures=monitPoint.getMonitQualityFeature();
		Calendar startCal = Calendar.getInstance();
		startCal.add(Calendar.HOUR,-24);
		Date startDate=startCal.getTime();
		
		Calendar endCal = Calendar.getInstance();
		Date endDate=endCal.getTime();
		Map<String,Boolean> colorMap=new HashMap<String, Boolean>();
		if(monitQualityFeatures!=null){
		for(int i=0;i<monitQualityFeatures.size();i++){
			MonitQualityFeature m=monitQualityFeatures.get(i);
			QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(m.getQualityFeatureId(),monitProgramDao.getSession());
			List<AbnormalInfo> abnormalInfo=new ArrayList<AbnormalInfo>();
			List<AbnormalInfo> handleAbnormalInfo=new ArrayList<AbnormalInfo>();
//			List<AbnormalInfo> notHandleAbnormalInfo=new ArrayList<AbnormalInfo>();
			if(qualityFeature!=null){
				if(qualityFeature.getId()!=null){
					abnormalInfo=abnormalInfoManager.getAbnormalInfo(qualityFeature.getId(), startDate, endDate);//异常信息
					handleAbnormalInfo=abnormalInfoManager.getHandleAbnormalInfo(qualityFeature.getId(), startDate, endDate, "2");//产生了异常而且还有异常未处理
//					notHandleAbnormalInfo=abnormalInfoManager.getHandleAbnormalInfo(qualityFeature.getId(), startDate, endDate, "1");//产生了异常而且异常已全部处理
				}
				if(abnormalInfo.size()==0){
					if(!colorMap.containsKey("red")&&!colorMap.containsKey("blue")){
						colorMap.put("green",true);
					}
				}
				//未处理时，为红色
				if(handleAbnormalInfo.size()>0){
					colorMap.put("red",true);
				}
				//有故障但是全部已处理时，为蓝色
				if(abnormalInfo.size()>0&&handleAbnormalInfo.size()==0){
					if(!colorMap.containsKey("red")){
						colorMap.put("blue",true);
					}
				}
			}
		}
		if(colorMap.containsKey("red")){
			json.put("color","red");
		}else if(colorMap.containsKey("blue")){
			json.put("color","blue");
		}else if(colorMap.containsKey("green")){
			json.put("color","green");
		}else if(monitQualityFeatures.size()==0){
			json.put("color","green");
		}
		}else{
			json.put("color","green");
		}
		
		return json;
	}
}
