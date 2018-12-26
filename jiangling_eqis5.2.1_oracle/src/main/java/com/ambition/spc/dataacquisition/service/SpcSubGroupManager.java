package com.ambition.spc.dataacquisition.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.abnormal.service.AbnormalInfoManager;
import com.ambition.spc.dataacquisition.dao.SpcSubGroupDao;
import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.FeatureRules;
import com.ambition.spc.entity.LayerDetail;
import com.ambition.spc.entity.LayerType;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSgSample;
import com.ambition.spc.entity.SpcSgTag;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.importutil.service.SpcImportManager;
import com.ambition.spc.importutil.service.SpcMonitorManager;
import com.ambition.spc.layertype.service.LayerTypeManager;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.DateUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**    
 * SpcSubGroupManager.java
 * @authorBy YUKE
 *
 */
@Service
public class SpcSubGroupManager {
	@Autowired
	private SpcSubGroupDao spcSubGroupDao;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private LayerTypeManager layerTypeManager;
	@Autowired
	private AbnormalInfoManager abnormalInfoManager;
	@Autowired
	private SpcImportManager spcImportManager;
	@Autowired
	private SpcMonitorManager spcMonitorManager;
	public SpcSubGroup getSpcSubGroup(Long id){
		return spcSubGroupDao.get(id);
	}
	
	public SpcSubGroup getSpcSubGroupByFeature(QualityFeature qualityFeature){
		return spcSubGroupDao.getSpcSubGroupByFeature(qualityFeature);
	}
	@Transactional
	public void deleteSpcSubGroup(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id:ids){
			if(StringUtils.isNotEmpty(id)){
				SpcSubGroup spcSubGroup = spcSubGroupDao.get(Long.valueOf(id));
				spcSubGroupDao.delete(spcSubGroup);
			}
		}
	}
	
	@Transactional
	public void deleteSpcSubGroup(String groupIds,String dataIds,Long featureId){
		//deleteSpcSubGroup(groupIds);
		//独立表的数据
		if(StringUtils.isNotEmpty(dataIds)){
			QualityFeature qualityFeature = qualityFeatureManager.getQualityFeature(featureId);
			if(StringUtils.isNotEmpty(qualityFeature.getTargetTableName())){
				String delSql = "delete from " + qualityFeature.getTargetTableName() + " where id = ?";
				for(String dataId : dataIds.split(",")){
					if(StringUtils.isEmpty(dataId)){
						continue;
					}
					spcSubGroupDao.getSession()
						.createSQLQuery(delSql)
						.setParameter(0,dataId)
						.executeUpdate();
				}
			}
		}
	}
	
	public Integer getNumByFeature(QualityFeature qualityFeature){
		String hql="select max(s.subGroupOrderNum) as maxNum from SpcSubGroup s where s.companyId = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(qualityFeature != null){
			searchParams.add(qualityFeature);
			hql = hql + " and s.qualityFeature = ?";
		}
		Query query = spcSubGroupDao.getSession().createQuery(hql.toString());
		for(int i = 0;i < searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if(list.get(0) != null){
			return Integer.valueOf(list.get(0).toString());
		}else{
			return 0;
		}
	}
	
	/**
	 * 计算子组数
	 * qualityFeature
	 */
	public Integer getGroupNumByQualityFeature(QualityFeature qualityFeature){
		return spcSubGroupDao.getGroupNumByQualityFeature(qualityFeature);
	}
	
	/**
	 * 取得子组
	 * qualityFeature
	 * num
	 */
	public SpcSubGroup queryGroupByNumAndFeature(QualityFeature qualityFeature,int num,Session session){
		if(session==null){
			session = spcSubGroupDao.getSession();
		}
		String hql="from SpcSubGroup s where s.companyId = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(qualityFeature.getCompanyId());
		if(qualityFeature != null){
			searchParams.add(qualityFeature);
			hql = hql + " and s.qualityFeature = ?";
		}
		if(num != 0){
			searchParams.add(num);
			hql = hql + " and s.subGroupOrderNum = ?";
		}
		Query query = session.createQuery(hql.toString());
		for(int i = 0;i < searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		List<?> list = query.list();
		if(!list.isEmpty()&&list.get(0) != null){
			return (SpcSubGroup) list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 保存 子组 样本容量值
	 */
	public void saveForInput(){
		String featureId = Struts2Utils.getParameter("qualityFeatureId");
		JSONObject valueMap = new JSONObject();
		valueMap.put("id", UUID.randomUUID().toString());
		valueMap.put("inspectionDate",DateUtil.formateDateStr(new Date(),"yyyyMMddHHmmssSSS"));
		valueMap.put("creatorName",ContextUtils.getUserName());
		valueMap.put("value", Double.valueOf(Struts2Utils.getParameter("samValue")));
		valueMap.put("featureId", featureId);
		String layerItemStrs = Struts2Utils.getParameter("layerItemStrs");
		JSONArray layerItemArray = null;
		if(StringUtils.isNotEmpty(layerItemStrs)){
			layerItemArray = JSONArray.fromObject(layerItemStrs);
		}
		if(layerItemArray != null){
			for(int i=0;i<layerItemArray.size();i++){
				JSONObject json = layerItemArray.getJSONObject(i);
				String tagCode = json.getString("tagCode");
				String tagValue = json.getString("tagValue");
				if(StringUtils.isEmpty(tagValue)){
					continue;
				}
				valueMap.put(tagCode,tagValue);
			}
		}
		List<JSONObject> valueMaps = new ArrayList<JSONObject>();
		valueMaps.add(valueMap);
		spcImportManager.executeImport(spcSubGroupDao.getSession(),Long.valueOf(featureId), valueMaps);
	}
	
	public SpcSubGroup findLastSubGroupNew(Long featureId,Integer capcity,Long companyId,String userName,Session session){
		if(null == session){
			session = spcSubGroupDao.getSession();
		}
		String hql = "from SpcSubGroup s where s.companyId = ? and s.qualityFeature.id = ? and s.subGroupSize = ? and s.actualSmapleNum < s.subGroupSize order by s.modifiedTime desc ";
		List<?> subGroups = session.createQuery(hql)
			//.setParameter(0,userName)
			.setParameter(0,companyId)
			.setParameter(1,featureId)
			.setParameter(2,capcity)
			.list();
		//List<SpcSubGroup> subGroups = spcSubGroupDao.find(hql,userName,companyId,featureId,capcity);
		if(null == subGroups || subGroups.isEmpty()){
			return null;
		}else{
			return (SpcSubGroup)subGroups.get(0);
		}
	}
	
	public JSONObject findLastSubGroup(Long featureId){
		QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(featureId,spcSubGroupDao.getSession());
		if(StringUtils.isEmpty(qualityFeature.getTargetTableName())){
			throw new AmbFrameException("数据表未创建!");
		}
		JSONObject result = new JSONObject();
		result.put("id",-1);
		
		String sql = "select count(*) from " + qualityFeature.getTargetTableName();
		List<?> list = spcSubGroupDao.getSession().createSQLQuery(sql).list();
		Integer total = Integer.valueOf(list.get(0).toString());
		Integer sampless = qualityFeature.getSampleCapacity();
		if(sampless == null){
			sampless = 5;
		}
		Integer groupCount = total/sampless;
		if(total%sampless>0){
			List<String> layerFields = new ArrayList<String>();
			String selSql = "id,data_value";
			for(FeatureLayer layer : qualityFeature.getFeatureLayers()){
				layerFields.add(layer.getDetailCode());
				selSql += "," + layer.getDetailCode();
			}
			sql = "select "+selSql+" from " + qualityFeature.getTargetTableName() + " order by inspection_date asc";
			Query query = spcSubGroupDao.getSession().createSQLQuery(sql);
			int first = groupCount*sampless;
			query.setFirstResult(first);
			query.setMaxResults(sampless);
			list = query.list();
			List<Double> values = new ArrayList<Double>();
			JSONArray samples = new JSONArray();
			int no = 1;
			for(Object obj : list){
				Object[] objs = (Object[])obj;
				Double value = Double.valueOf(objs[1].toString());
				values.add(value);
				
				JSONObject s = new JSONObject();
				s.put("id",objs[0]);
				s.put("sampleNo","X" + no++);
				s.put("samValue",value);
				samples.add(s);
			}
			
//			Map<String,Double> valueMap = caculateSpcGroupValues(values);

			result.put("id",1);
			result.put("samples",samples);
//			result.put("maxValue",valueMap.get("maxValue"));
//			result.put("minValue",valueMap.get("minValue"));
//			result.put("rangeDiff",valueMap.get("rangeDiff"));
//			result.put("sigma",valueMap.get("sigma"));
			
			JSONObject levelMap = new JSONObject();
			
			Object[] lastObjs = (Object[])list.get(list.size()-1);
			for(int i=0;i<layerFields.size();i++){
				Object value = lastObjs[2+i];
				if(value==null){
					continue;
				}
				levelMap.put(layerFields.get(i),value.toString());
			}
			result.put("levelMap",levelMap);
		}
		return result;
	}
	
	public Map<String,Double> caculateSpcGroupValues(List<Double> values){
		Map<String,Double> map = new HashMap<String, Double>();
		Double min=null,max=null,sumValue=0.0;
		Integer num = 0;
		for(int j=0;j<values.size();j++){
			Double value = values.get(j);;
			if(value != null){
				if(min==null){
					min = value;
				}else if(value < min){
					min = value;
				}
				if(max==null){
					max = value;
				}else if(value > max){
					max = value;
				}
				
				sumValue += value;
			}
			num++;
		}
		map.put("maxValue",max);
		map.put("minValue",min);
		map.put("sigma",num>0?sumValue*1.0/num:sumValue);
		map.put("rangeDiff",max-min);
		map.put("sumValue",sumValue);
		return map;
	}
	
	public SpcSubGroup findLastSubGroup(Long featureId,Integer capcity,Long companyId,String userName){
		return findLastSubGroup(featureId,capcity,companyId,userName,null);
	}
	public SpcSubGroup findLastSubGroup(Long featureId,Integer capcity,Long companyId,String userName,Session session){
		if(null == session){
			session = spcSubGroupDao.getSession();
		}
		String hql = "from SpcSubGroup s where s.companyId = ? and s.qualityFeature.id = ? and s.subGroupSize = ? and s.actualSmapleNum < s.subGroupSize order by s.modifiedTime desc ";
		List<?> subGroups = session.createQuery(hql)
			//.setParameter(0,userName)
			.setParameter(0,companyId)
			.setParameter(1,featureId)
			.setParameter(2,capcity)
			.list();
		//List<SpcSubGroup> subGroups = spcSubGroupDao.find(hql,userName,companyId,featureId,capcity);
		if(null == subGroups || subGroups.isEmpty()){
			return null;
		}else{
			return (SpcSubGroup)subGroups.get(0);
		}
	}
	@Transactional
	public void saveLayers(JSONArray layerItemArray,SpcSubGroup spcSubGroup){
		for(int i=0;i<layerItemArray.size();i++){
			JSONObject json = layerItemArray.getJSONObject(i);
			SpcSgTag spcSgTag = new SpcSgTag();
			spcSgTag.setCreatedTime(spcSubGroup.getCreatedTime());
			spcSgTag.setCompanyId(ContextUtils.getCompanyId());
			spcSgTag.setCreator(ContextUtils.getUserName());
			spcSgTag.setModifiedTime(new Date());
			spcSgTag.setModifier(ContextUtils.getUserName());
			try {
				for(Object key : json.keySet()){
					String value = json.getString(key.toString());
					setProperty(spcSgTag, key.toString(),value);
				}
				setProperty(spcSubGroup, json.getString("tagCode"),json.getString("tagValue"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			spcSgTag.setSpcSubGroup(spcSubGroup);
			spcSubGroupDao.getSession().save(spcSgTag);
		}
	}
	
	private void setProperty(Object obj, String property, Object value) throws Exception {
		String fieldName = property,customType = null;
		if(property.indexOf("_")>0){
			String[] strs = property.split("_");
			fieldName = strs[0];
			customType = strs[1];
		}
		Class<?> type = PropertyUtils.getPropertyType(obj, fieldName);
		if (type != null) {
			if (value == null || StringUtils.isEmpty(value.toString())) {
				PropertyUtils.setProperty(obj, fieldName, null);
			} else {
				if("timestamp".equals(customType)){
					PropertyUtils.setProperty(obj, fieldName,DateUtil.parseDateTime(value.toString()));
				}else if (String.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName, value.toString());
				} else if (Integer.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Integer.valueOf(value.toString()));
				} else if (Double.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Double.valueOf(value.toString()));
				} else if (Float.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Float.valueOf(value.toString()));
				} else if (Boolean.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Boolean.valueOf(value.toString()));
				} else {
					PropertyUtils.setProperty(obj, fieldName, value);
				}
			}
		}
	}
	@Transactional
	public void saveSpcSubGroup(SpcSubGroup spcSubGroup){
		int num = 0;
		QualityFeature qualityFeature = null;
		if(Struts2Utils.getParameter("qualityFeatureId")!=null){
			String qualityFeatureId = Struts2Utils.getParameter("qualityFeatureId");
			if(!qualityFeatureId.isEmpty()){
				qualityFeature  = qualityFeatureManager.getQualityFeature(Long.valueOf(qualityFeatureId));
				spcSubGroup.setQualityFeature(qualityFeature);
				num = getNumByFeature(qualityFeatureManager.getQualityFeature(Long.valueOf(qualityFeatureId))) + 1;
				if(Struts2Utils.getParameter("index")!=null){
					if(Integer.valueOf(Struts2Utils.getParameter("index")) == qualityFeature.getSampleCapacity()){
						spcSubGroup.setSubGroupOrderNum(num);
					}
				}
			}
		}
		spcSubGroupDao.save(spcSubGroup);
	}
	@Transactional
	public void save(SpcSubGroup spcSubGroup){
		spcSubGroupDao.save(spcSubGroup);
	}
	
	@Transactional
	public void saveForDataItems(final List<JSONObject> dataItems,Long featureId){
		if(dataItems==null||dataItems.size()==0){
			return;
		}
		QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(featureId,spcSubGroupDao.getSession());
		if(StringUtils.isEmpty(qualityFeature.getTargetTableName())){
			throw new AmbFrameException("迁移数据表未指定!");
		}
		final StringBuffer updateSql = new StringBuffer("update " + qualityFeature.getTargetTableName() + " set DATA_VALUE = ?");
		final List<String> layerFields = new ArrayList<String>();
		for(FeatureLayer layer : qualityFeature.getFeatureLayers()){
			updateSql.append("," + layer.getDetailCode() + "=?");
			layerFields.add(layer.getDetailCode().toUpperCase());
		}
		updateSql.append(" where ID = ?");
		spcSubGroupDao.getSession().doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				PreparedStatement ps = null;
				try {
					ps = conn.prepareStatement(updateSql.toString());
					for(JSONObject json : dataItems){
						String id = json.getString("ID");
						String dataValue = json.getString("DATA_VALUE");
						if(!CommonUtil1.isDouble(dataValue)){
							throw new AmbFrameException("采集["+dataValue+"]值不是有效的数字!");
						}
						int index = 1;
						ps.setObject(index++,Double.valueOf(dataValue));
						for(String layerField : layerFields){
							Object layerValue = null;
							if(json.containsKey(layerField)){
								layerValue = json.get(layerField);
							}
							ps.setObject(index++,layerValue);
						}
						ps.setObject(index,id);
						ps.addBatch();
					}
					ps.executeBatch();
				} finally{
					if(ps != null){
						ps.close();
					}
				}
			}
		});
		//清除预警缓存数据
		spcMonitorManager.clearMonitorCaches(featureId.toString());
	}
	
	/**
	  * 方法名: 保存导入数据
	  * <p>功能说明：</p>
	  * @return
	 */
	public void saveImportItems(QualityFeature qualityFeature,final List<JSONObject> valueMaps){
		spcImportManager.executeImport(spcSubGroupDao.getSession(),qualityFeature.getId(),valueMaps);
	}
	
	public void setLayerInfo(SpcSubGroup spcSubGroup,String info,String value){
		if(info.equals("info1")){
			spcSubGroup.setInfo1(value);
		}
		if(info.equals("info2")){
			spcSubGroup.setInfo2(value);		
		}
		if(info.equals("info3")){
			spcSubGroup.setInfo3(value);
		}
		if(info.equals("info4")){
			spcSubGroup.setInfo4(value);
		}
		if(info.equals("info5")){
			spcSubGroup.setInfo5(value);
		}
		if(info.equals("info6")){
			spcSubGroup.setInfo6(value);
		}
		if(info.equals("info7")){
			spcSubGroup.setInfo7(value);
		}
		if(info.equals("info8")){
			spcSubGroup.setInfo8(value);
		}
		if(info.equals("info9")){
			spcSubGroup.setInfo9(value);
		}
		if(info.equals("info10")){
			spcSubGroup.setInfo10(value);
		}
		if(info.equals("info11")){
			spcSubGroup.setInfo11(value);
		}
		if(info.equals("info12")){
			spcSubGroup.setInfo1(value);
		}
		if(info.equals("info13")){
			spcSubGroup.setInfo13(value);
		}
		if(info.equals("info14")){
			spcSubGroup.setInfo14(value);
		}
		if(info.equals("info15")){
			spcSubGroup.setInfo15(value);
		}
		if(info.equals("info16")){
			spcSubGroup.setInfo16(value);
		}
		if(info.equals("info17")){
			spcSubGroup.setInfo17(value);
		}
		if(info.equals("info18")){
			spcSubGroup.setInfo18(value);
		}
		if(info.equals("info19")){
			spcSubGroup.setInfo19(value);
		}
		if(info.equals("info20")){
			spcSubGroup.setInfo20(value);
		}
	}
	
	/**
	 * 保存 子组  层别信息
	 */
	@Transactional
	public SpcSgTag saveTag(){
		String mainId = Struts2Utils.getParameter("mainId");
		String featureId = Struts2Utils.getParameter("qualityFeatureId");
		String info = Struts2Utils.getParameter("tagCode");
		String value = Struts2Utils.getParameter("tagValue");
		SpcSubGroup spcSubGroup = null;
		if(mainId == null){
			spcSubGroup = new SpcSubGroup();
			spcSubGroup.setCompanyId(ContextUtils.getCompanyId());
			spcSubGroup.setCreatedTime(new Date());
			spcSubGroup.setCreator(ContextUtils.getUserName());
			spcSubGroup.setModifiedTime(new Date());
			spcSubGroup.setModifier(ContextUtils.getUserName());
			if(info != null && !info.equals("")){
				//设置层别的值
				setLayerInfo(spcSubGroup,info,value);
			}
			if(featureId != null && !featureId.isEmpty()){
				QualityFeature qualityFeature  = qualityFeatureManager.getQualityFeature(Long.valueOf(featureId));
				spcSubGroup.setQualityFeature(qualityFeature);
				spcSubGroup.setSubGroupSize(qualityFeature.getSampleCapacity());
				int num = getNumByFeature(qualityFeature) + 1;
				spcSubGroup.setSubGroupOrderNum(num);
			}
		}else{
			spcSubGroup = spcSubGroupDao.get(Long.valueOf(mainId));
		}
		//层别信息（0814之前）
		String id = Struts2Utils.getParameter("id");
		SpcSgTag spcSgTag = null;
		if(StringUtils.isEmpty(id)){
			spcSgTag = new SpcSgTag();
			spcSgTag.setCreatedTime(spcSubGroup.getCreatedTime());
			spcSgTag.setCompanyId(ContextUtils.getCompanyId());
			spcSgTag.setCreator(ContextUtils.getUserName());
		}else{
			spcSgTag = (SpcSgTag) spcSubGroupDao.getSession().get(SpcSgTag.class,Long.valueOf(id));
		}
		spcSgTag.setModifiedTime(new Date());
		spcSgTag.setModifier(ContextUtils.getUserName());
		spcSgTag.setTagName(Struts2Utils.getParameter("tagName"));
		spcSgTag.setTagCode(Struts2Utils.getParameter("tagCode"));
		spcSgTag.setTagValue(Struts2Utils.getParameter("tagValue"));
		spcSgTag.setMethod(Struts2Utils.getParameter("sampleMethod"));
		spcSgTag.setSpcSubGroup(spcSubGroup);
		
		//取值方式 ：0、自由录入；1、自由录入并加入列表；2、从列表中选择
		if(Struts2Utils.getParameter("sampleMethod").equals("1")){
			if(Struts2Utils.getParameter("tagName")!=null && !Struts2Utils.getParameter("tagName").isEmpty()){
				LayerType layerType = layerTypeManager.getLayerTypeByName(Struts2Utils.getParameter("tagName"));
				if(layerType != null){
					Boolean isExist = true;
					if(!Struts2Utils.getParameter("tagValue").equals("") && Struts2Utils.getParameter("tagValue")!=null){
						isExist = layerTypeManager.isExistLayerDetailName(layerType,Struts2Utils.getParameter("tagValue"));
					}
					if(!isExist){
						LayerDetail layerInfo = new LayerDetail();
						layerInfo.setCompanyId(ContextUtils.getCompanyId());
						layerInfo.setCreatedTime(new Date());
						layerInfo.setCreator(ContextUtils.getUserName());
						layerInfo.setModifiedTime(new Date());
						layerInfo.setModifier(ContextUtils.getUserName());
						layerInfo.setDetailName(Struts2Utils.getParameter("tagValue"));
						layerInfo.setDetailCode(Struts2Utils.getParameter("tagValue"));
						layerInfo.setLayerType(layerType);
						layerType.getLayerDetails().add(layerInfo);
					}
				}
			}
		}
		
		if(mainId==null){
			spcSubGroupDao.save(spcSubGroup);
			spcSubGroupDao.getSession().save(spcSgTag);
		}else{
			spcSubGroupDao.getSession().save(spcSgTag);
			QualityFeature qualityFeature = null;
			if(featureId != null && !featureId.isEmpty()){
				qualityFeature  = qualityFeatureManager.getQualityFeature(Long.valueOf(featureId));
				spcSubGroup.setQualityFeature(qualityFeature);
				spcSubGroup.setSubGroupSize(qualityFeature.getSampleCapacity());
			}
			if(info != null && !info.equals("")){
				//设置层别的值
				setLayerInfo(spcSubGroup,info,value);
			}
			spcSubGroupDao.save(spcSubGroup);
		}
		return spcSgTag;
	}
	
	public String getResultJson(Page<FeatureRules> page){
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(FeatureRules rule : page.getResult()){
			JSONObject json = JSONObject.fromObject(JsonParser.object2Json(rule));
			list.add(json);
		}
		//添加jqGrid所需的页信息
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
	
	public List<SpcSubGroup> getSpcSubGroupList(Long featureId,Date startDate,Date endDate){
		return spcSubGroupDao.spcSubGroupList(featureId,startDate,endDate);
	}
	
	public List<SpcSubGroup> getSpcSubGroupListByNo(Long featureId,int  beginNo,int  endNo){
		return spcSubGroupDao.spcSubGroupListByNo(featureId,beginNo,endNo);
	}
	
	public List<SpcSubGroup> getSpcSubGroupListByCodeAndValue(Long featureId,Date startDate,Date endDate,String tag_code,String tag_value,int effectiveCapacity){
		return spcSubGroupDao.getSpcSubGroupListByCodeAndValue(featureId,startDate,endDate,tag_code,tag_value,effectiveCapacity);
	}
	
	@Transactional
	public void saveGroupFromMfg(String featureId,Double[] inspectDatas){
		if(inspectDatas != null){
			QualityFeature qualityFeature = qualityFeatureManager.getQualityFeature(Long.valueOf(featureId));
			if(qualityFeature != null){
				SpcSubGroup spcSubGroup = new SpcSubGroup();
				spcSubGroup.setCompanyId(ContextUtils.getCompanyId());
				spcSubGroup.setCreator(ContextUtils.getUserName());
				spcSubGroup.setCreatedTime(new Date());
				spcSubGroup.setModifier(ContextUtils.getUserName());
				spcSubGroup.setModifiedTime(new Date());
				spcSubGroup.setQualityFeature(qualityFeature);
				int num = getNumByFeature(qualityFeature) + 1;
				spcSubGroup.setSubGroupOrderNum(num);
				spcSubGroup.setSpcSgSamples(new ArrayList<SpcSgSample>());
				if(qualityFeature.getSampleCapacity() != 0){
					spcSubGroup.setSubGroupSize(qualityFeature.getSampleCapacity());
				}
				
				Double sum = 0.0,sigma = 0.0,randDiff = 0.0;
				Double max = 0.0;
				if(inspectDatas[0] != null){
					max = inspectDatas[0];
				}	
				Double min = max;
				if(qualityFeature.getEffectiveCapacity() != 0){
					for(int i=0;i<inspectDatas.length;i++){
						if(i==qualityFeature.getEffectiveCapacity()){
							break;
						}
						SpcSgSample sample = new SpcSgSample();
						sample.setCompanyId(ContextUtils.getCompanyId());
						sample.setCreator(ContextUtils.getUserName());
						sample.setCreatedTime(new Date());
						sample.setModifier(ContextUtils.getUserName());
						sample.setModifiedTime(new Date());
						int j = i + 1;
						sample.setSampleNo("X"+j);
						if(inspectDatas[i] != null){
							sample.setSamValue(inspectDatas[i]);
							sum += inspectDatas[i];
							if(max < inspectDatas[i]){
								max = inspectDatas[i];
							}
							if(min > inspectDatas[i]){
								min = inspectDatas[i];
							}
						}
						sample.setSpcSubGroup(spcSubGroup);
						spcSubGroup.getSpcSgSamples().add(sample);
					}
					sigma = sum/qualityFeature.getEffectiveCapacity();
					randDiff = max - min;
					spcSubGroup.setMaxValue(new Double(new DecimalFormat("0.000").format(max)));
					spcSubGroup.setMinValue(new Double(new DecimalFormat("0.000").format(min)));
					spcSubGroup.setSigma(new Double(new DecimalFormat("0.000").format(sigma)));
					spcSubGroup.setRangeDiff(new Double(new DecimalFormat("0.000").format(randDiff)));
					spcSubGroup.setActualSmapleNum(qualityFeature.getEffectiveCapacity());
				}
				this.save(spcSubGroup);
			}
		}
	}
	
	/**
	 * 数据维护 列表数据
	 */
	public Page<SpcSubGroup> searchByPage(Page<SpcSubGroup> page,JSONObject params){
		StringBuffer sb = new StringBuffer("from SpcSubGroup s where s.companyId = ?");
		params = convertJsonObject(params);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		//查询条件
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			if(params.containsKey("qualityFeatures") && params.getString("qualityFeatures") != null){
				Long featureId = Long.valueOf(params.getString("qualityFeatures"));
				QualityFeature qualityFeature = qualityFeatureManager.getQualityFeature(featureId);
				searchParams.add(qualityFeature);
				sb.append(" and s.qualityFeature = ?");
			}
			if(params.containsKey("startDate_ge_date") && params.containsKey("endDate_le_date")){
				if(params.getString("startDate_ge_date") != null && params.getString("endDate_le_date") != null){
					try {
						startDate = sdf.parse(params.getString("startDate_ge_date"));
						endDate = sdf.parse(params.getString("endDate_le_date"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				searchParams.add(startDate);
				searchParams.add(endDate);
				sb.append(" and s.createdTime between ? and ?");
			}
		}
		page = spcSubGroupDao.findPage(page, sb.toString(), searchParams.toArray());
		return page;
	}
	
	/**
	 * 数据维护 列表数据
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
	public String exportDatas(JSONObject params,Long featureId) throws IOException, InvalidFormatException{
		//查询出最大数65535
		JSONObject pageJson = searchSpcGroups(new Page<SpcSubGroup>(65535), params, featureId);
		
		String savePath = PropUtils.getProp("excel.export.file.path");
		File pathFile = new File(savePath);
		if(!pathFile.exists()){
			pathFile.mkdir();
		}
		String saveFileName = UUID.randomUUID().toString();
		File exportFile = new File(savePath + saveFileName);
		InputStream inputStream = null;
		OutputStream excelOutputStream = null;
		try {
			String hql = "select l from ListColumn l where l.listView.deleted = ? and l.listView.code = ? order by l.displayOrder";
			List<?> listColumns = spcSubGroupDao.getSession().createQuery(hql)
										.setParameter(0,false)
										.setParameter(1,Struts2Utils.getParameter("_list_code"))
										.list();
			if(listColumns.isEmpty()){
				throw new AmbFrameException("找不到编码为" + Struts2Utils.getParameter("_list_code") + "的列表！");
			}
			//找到模板
			inputStream = ExcelUtil.class.getClassLoader().getResourceAsStream("template/report/list-template.xls");
			Workbook book = WorkbookFactory.create(inputStream);
			Sheet sheet = book.getSheetAt(0);
			Row headerRow = sheet.getRow(0);
			if(headerRow==null){
				headerRow = sheet.createRow(0);
			}
			Map<String,Integer> fieldIndexMap = new HashMap<String, Integer>();
			Map<String,ListColumn> columnMap = new HashMap<String, ListColumn>();
			int colIndex = 0;
			for(Object objColumn : listColumns){
				ListColumn listColumn = (ListColumn)objColumn;
				if(listColumn.getExportable() == null 
						|| !listColumn.getExportable()){
					continue;
				}
				if(listColumn.getTableColumn()==null){
					continue;
				}
				columnMap.put(listColumn.getTableColumn().getName(), listColumn);
				Cell cell = headerRow.getCell(colIndex);
				if(cell==null){
					cell = headerRow.createCell(colIndex);
				}
				cell.setCellValue(listColumn.getHeaderName());
				fieldIndexMap.put(listColumn.getTableColumn().getName(),colIndex);
				colIndex++;
			}
			int rowIndex = 0;
			JSONArray rows = pageJson.getJSONArray("rows");
			for(Object obj : rows){
				rowIndex++;
				JSONObject jsonData = (JSONObject)obj;
				Row row = sheet.createRow(rowIndex);
				for(String fieldName : fieldIndexMap.keySet()){
					Object val = null;
					if(jsonData.containsKey(fieldName)){
						val = jsonData.get(fieldName);
					}
					if(val==null){
						continue;
					}
					Cell cell = row.createCell(fieldIndexMap.get(fieldName));
					if(val instanceof Double){
						cell.setCellValue((Double)val);
					}else if(val instanceof Float){
						cell.setCellValue((Float)val);
					}else{
						cell.setCellValue(val.toString());
					}
				}
			}
			excelOutputStream = new FileOutputStream(exportFile);
			book.write(excelOutputStream);
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
			if(excelOutputStream != null){
				excelOutputStream.close();
			}
		}
		return "SPC数据.xls_" + saveFileName;
	}
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
	
	public void setLayers(SpcSubGroup subGroup,QualityFeature param){
		for(FeatureLayer layer : param.getFeatureLayers()){
			SpcSgTag tag = new SpcSgTag();
			tag.setCreatedTime(subGroup.getCreatedTime());//层级时间与spc组时间保持一致,提高查询效率
			tag.setCompanyId(ContextUtils.getCompanyId());
			tag.setCreator(ContextUtils.getUserName());
			tag.setModifiedTime(new Date());
			tag.setModifier(ContextUtils.getUserName());
			tag.setTagCode(layer.getDetailCode());
			tag.setTagName(layer.getDetailName());
			if(subGroup.getInfo1() != null && layer.getDetailCode().equals("info1")){
				tag.setTagValue(subGroup.getInfo1());
			}
			if(subGroup.getInfo2() != null && layer.getDetailCode().equals("info2")){
				tag.setTagValue(subGroup.getInfo2());
			}
			if(subGroup.getInfo3() != null && layer.getDetailCode().equals("info3")){
				tag.setTagValue(subGroup.getInfo3());
			}
			if(subGroup.getInfo4() != null && layer.getDetailCode().equals("info4")){
				tag.setTagValue(subGroup.getInfo4());
			}
			if(subGroup.getInfo5() != null && layer.getDetailCode().equals("info5")){
				tag.setTagValue(subGroup.getInfo5());
			}
			if(subGroup.getInfo6() != null && layer.getDetailCode().equals("info6")){
				tag.setTagValue(subGroup.getInfo6());
			}
			if(subGroup.getInfo7() != null && layer.getDetailCode().equals("info7")){
				tag.setTagValue(subGroup.getInfo7());
			}
			if(subGroup.getInfo8() != null && layer.getDetailCode().equals("info8")){
				tag.setTagValue(subGroup.getInfo8());
			}
			if(subGroup.getInfo9() != null && layer.getDetailCode().equals("info9")){
				tag.setTagValue(subGroup.getInfo9());
			}
			if(subGroup.getInfo10() != null && layer.getDetailCode().equals("info10")){
				tag.setTagValue(subGroup.getInfo10());
			}
			if(subGroup.getInfo11() != null && layer.getDetailCode().equals("info11")){
				tag.setTagValue(subGroup.getInfo11());
			}
			if(subGroup.getInfo12() != null && layer.getDetailCode().equals("info12")){
				tag.setTagValue(subGroup.getInfo12());
			}
			if(subGroup.getInfo13() != null && layer.getDetailCode().equals("info13")){
				tag.setTagValue(subGroup.getInfo13());
			}
			if(subGroup.getInfo14() != null && layer.getDetailCode().equals("info14")){
				tag.setTagValue(subGroup.getInfo14());
			}
			if(subGroup.getInfo15() != null && layer.getDetailCode().equals("info15")){
				tag.setTagValue(subGroup.getInfo15());
			}
			if(subGroup.getInfo16() != null && layer.getDetailCode().equals("info16")){
				tag.setTagValue(subGroup.getInfo16());
			}
			if(subGroup.getInfo17() != null && layer.getDetailCode().equals("info17")){
				tag.setTagValue(subGroup.getInfo17());
			}
			if(subGroup.getInfo18() != null && layer.getDetailCode().equals("info18")){
				tag.setTagValue(subGroup.getInfo18());
			}
			if(subGroup.getInfo19() != null && layer.getDetailCode().equals("info19")){
				tag.setTagValue(subGroup.getInfo19());
			}
			if(subGroup.getInfo20() != null && layer.getDetailCode().equals("info20")){
				tag.setTagValue(subGroup.getInfo20());
			}
			tag.setSpcSubGroup(subGroup);
			subGroup.getSpcSgTags().add(tag);
		}
	}

	/**
	 * 转换map对象到options
	 * @param map
	 * @return
	 */
	public List<Option> convertListToOptions(List<QualityFeature> qualityFeatures){
		List<Option> options = new ArrayList<Option>();
		Option option = new Option();
		option.setName("请选择");
		option.setValue("");
		options.add(0,option);
		for(int i=0;i<qualityFeatures.size();i++){
			option = new Option();
			QualityFeature q=qualityFeatures.get(i);
			option.setName(q.getName());
			option.setValue(Long.toString(q.getId()));
			options.add(option);
		}
		return options;
	}
	
	public String getSpcSubGroupDatas(Page<SpcSubGroup> page,JSONObject params){
		params = convertJsonObject(params);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = null;
		Date endDate = null;
		StringBuffer sql = new StringBuffer("");
		StringBuffer whereSql = new StringBuffer("");

		sql.append("select s.id from SpcSubGroup s ");
		whereSql.append(" where s.company_id = ?");
		
		//查询条件
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());

		if(params.containsKey("qualityFeatures") && params.getString("qualityFeatures") != null){
			Long fid = Long.valueOf(params.getString("qualityFeatures"));
			whereSql.append(" and s.FK_QUALITY_FEATURE_ID = ?");
			searchParams.add(fid);
		}
		if(params.containsKey("startDate_ge_date") && params.containsKey("endDate_le_date")){
			if(params.getString("startDate_ge_date") != null && params.getString("endDate_le_date") != null){
				try {
					startDate = sdf.parse(params.getString("startDate_ge_date")+" 00:00:00");
					endDate = sdf.parse(params.getString("endDate_le_date")+" 23:59:59");
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			searchParams.add(startDate);
			searchParams.add(endDate);
			whereSql.append(" and s.created_time between ? and ?");
		}
		sql.append(whereSql);
//		System.out.println("sql:" + sql.append(whereSql));
		
		Query query = spcSubGroupDao.getSession().createSQLQuery(sql.toString());
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i,searchParams.get(i));
		}
		JSONObject jsonPage = new JSONObject();
		jsonPage.put("page",page.getPageNo());
		
		int totalCount = query.list().size();
		jsonPage.put("records", totalCount);
		int total = totalCount / page.getPageSize();
		if(totalCount % page.getPageSize() > 0){
			total ++;
		}
		jsonPage.put("total", total);
		query.setFirstResult((page.getPageNo() - 1)*page.getPageSize());
		query.setMaxResults(page.getPageSize());
		@SuppressWarnings("rawtypes")
		List results = query.list();
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		DecimalFormat df = new DecimalFormat("#.00");
		for(Object obj : results){
			Long id = Long.valueOf(obj.toString());
			SpcSubGroup spcSubGroup = (SpcSubGroup) spcSubGroupDao.getSession().get(SpcSubGroup.class,id);
			if(spcSubGroup != null){
				JSONObject jsonObject = JSONObject.fromObject(JsonParser.object2Json(spcSubGroup));
				jsonObject.put("name",spcSubGroup.getQualityFeature().getName());
				jsonObject.put("createdTime", sdf.format(spcSubGroup.getCreatedTime()));
				Double sum = 0.0,avg = 0.0;
				if(spcSubGroup.getSpcSgSamples()!=null && spcSubGroup.getSpcSgSamples().size()!=0){
					for(SpcSgSample sample:spcSubGroup.getSpcSgSamples()){
						sum += sample.getSamValue()==null?0.0:sample.getSamValue();
					}
					avg = sum/spcSubGroup.getSpcSgSamples().size();
				}
				jsonObject.put("sigma", df.format(avg));
				list.add(jsonObject);
			}
		}
		jsonPage.put("rows",list);
		return jsonPage.toString();
	}
	/**
	 * 根据过程节点查询质量特性
	 * @param processDefineId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<QualityFeature> queryFeaturesByDefineId(Long processDefineId){
		String hql = "from QualityFeature q where q.processPoint.id = ?";
		return spcSubGroupDao.createQuery(hql,processDefineId).list();
	}
	/**
	 * 导入质量特性
	 * @param file
	 * @param excelType
	 * @param processDefineId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public List<Option> importSpcDatas(File file,String excelType,Long processDefineId) throws Exception{
		List<QualityFeature> features = queryFeaturesByDefineId(processDefineId);
		if(features.isEmpty()){
			throw new AmbFrameException("该产品还未维护质量特性!");
		}
		Map<String,QualityFeature> featureMap = new HashMap<String, QualityFeature>();
		List<Option> options = new ArrayList<Option>();
		
		for(QualityFeature feature : features){
			featureMap.put(feature.getName(),feature);
			Option option = new Option();
        	option.setName(feature.getName());
        	option.setValue(feature.getId().toString());
        	options.add(option);
		}
		InputStream fis = null;
		try {
			fis = new FileInputStream(file);
			Workbook wb = WorkbookFactory.create(fis); // 从文件流中获取Excel工作区对象（WorkBook）
			Sheet sheet = wb.getSheetAt(0);
			if(sheet == null){
				throw new AmbFrameException("第一个Sheet为空!");
			}
			//缓存每个单元格值的
			Map<String,Cell> cellMap = new HashMap<String, Cell>();
			Map<String,CellRangeAddress> cellRangeAddressMap = new HashMap<String, CellRangeAddress>();
			Iterator<Row> rowIterator = sheet.rowIterator();
			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()){
					Cell cell = cellIterator.next();
					Object value = ExcelUtil.getCellValue(cell);
					if(value != null){
						String str = value.toString().replaceAll("\\n","").replaceAll(" ","").replaceAll("　","");
						cellMap.put(str, cell);
					}
				}
			}
			//缓存所有的合并单元格
			cellRangeAddressMap.clear();
			int sheetMergeCount = sheet.getNumMergedRegions();  
		    for(int i = 0 ; i < sheetMergeCount ; i++ ){  
		        CellRangeAddress ca = sheet.getMergedRegion(i);
		        int firstRow = ca.getFirstRow(),lastRow = ca.getLastRow();
		        int firstColumn = ca.getFirstColumn(),lastColumn = ca.getLastColumn();
		        for(int rowIndex = firstRow;rowIndex<=lastRow;rowIndex++){
		        	for(int columnIndex = firstColumn;columnIndex<=lastColumn;columnIndex++){
		        		String key = rowIndex + "_" + columnIndex;
		        		cellRangeAddressMap.put(key,ca);
		        	}
		        }
		    }
		    if("冲压SPC".equals(excelType)){
		    	importExcelSpcDatasForChongYa(sheet,cellMap,cellRangeAddressMap,featureMap);
		    }else if("焊装SPC".equals(excelType)){
		    	importExcelSpcDatasForHangZhuang(sheet,cellMap,cellRangeAddressMap,featureMap);
		    }else if("焊装三坐标".equals(excelType)){
		    	importExcelSpcDatasForHangZhuangShangZuoBiao(sheet,cellMap,cellRangeAddressMap,featureMap);
		    }else if("涂装SPC".equals(excelType)){
		    	importExcelSpcDatasForTuZhuang(sheet,cellMap,cellRangeAddressMap,featureMap);
		    }else if("总装SPC".equals(excelType)){
		    	importExcelSpcDatasForZhongZhuang(sheet,cellMap,cellRangeAddressMap,featureMap);
		    }else{
		    	throw new AmbFrameException("无效的模板类型["+excelType+"]");
		    }
			return options;
		} finally{
			if(fis != null){
				fis.close();
			}
		}
	}
	private Map<String,Object> getLayerImportValue(Row row,Cell currentCell,Map<String,CellRangeAddress> cellRangeAddressMap,Map<String,FeatureLayer> layerMap,String layerName){
		if(layerMap.containsKey(layerName)){
			FeatureLayer layer = layerMap.get(layerName);
			String productLineKey = row.getRowNum() + "_" + currentCell.getColumnIndex();
			Object productLineObj = null;
			CellRangeAddress productLineRange = cellRangeAddressMap.get(productLineKey);
			if(productLineRange != null){
				productLineObj = ExcelUtil.getCellValue(row.getCell(productLineRange.getFirstColumn()));
			}else{
				productLineObj = ExcelUtil.getCellValue(row.getCell(currentCell.getColumnIndex()));
			}
			if(productLineObj != null && StringUtils.isNotEmpty(productLineObj.toString())){
				Map<String,Object> layerObj = new HashMap<String, Object>();
				layerObj.put("featureLayer",layer);
				layerObj.put("value",productLineObj);
				return layerObj;
			}
		}
		return null;
	}
	/**
	 * 冲压SPC模板导入
	 * @param file
	 * @param featureId
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	private void importExcelSpcDatasForChongYa(Sheet sheet,Map<String,Cell> cellMap,Map<String,CellRangeAddress> cellRangeAddressMap,Map<String,QualityFeature> featureMap) throws Exception{
		//生产日期行
		Row dateRow  = sheet.getRow(0);
		//生产线行
		if(!cellMap.containsKey("生产线")){
			throw new AmbFrameException("值为[生产线]的单元格不存在,模板不正确!");
		}
		Row productLineRow = cellMap.get("生产线").getRow();
		//检查者
		if(!cellMap.containsKey("检查者")){
			throw new AmbFrameException("值为[检查者]的单元格不存在,模板不正确!");
		}
		Row checkerRow = cellMap.get("检查者").getRow();
		int lastRowNum = sheet.getLastRowNum();
		for(int i=checkerRow.getRowNum()+1;i<=lastRowNum;i++){
			Row row = sheet.getRow(i);
			if(row==null){
				continue;
			}
			String featureName = (String)ExcelUtil.getCellValue(row.getCell(0));
			if(StringUtils.isEmpty(featureName)){
				continue;
			}
			QualityFeature feature = featureMap.get(featureName);
			if(feature==null){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性还没有在基础维护中定义!");
			}
			Map<String,FeatureLayer> layerMap = new HashMap<String, FeatureLayer>();
			for(FeatureLayer layer : feature.getFeatureLayers()){
				layerMap.put(layer.getDetailName(),layer);
			}
			//检查样本数量是否与模板一致
			if(feature.getSampleCapacity()==null){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性还未设置样本容量,请到基础维护中维护!");
			}
			Cell firstDateCell = dateRow.getCell(2);
			CellRangeAddress cellRangeAddress = cellRangeAddressMap.get(dateRow.getRowNum() + "_" + firstDateCell.getColumnIndex());
			int capacity = 1;
			if(cellRangeAddress != null){
				capacity = cellRangeAddress.getLastColumn() - cellRangeAddress.getFirstColumn() + 1;
			}
			if(capacity != feature.getSampleCapacity()){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性样本容量为"+capacity+",与基础维护中定义的样本容量"+feature.getSampleCapacity()+"不一致,请先修改为一致!");
			}
			List<Double> values = new ArrayList<Double>();
			Iterator<Cell> cellIterator =  row.cellIterator();
			Calendar calendar = Calendar.getInstance();
			Integer currentYear = calendar.get(Calendar.YEAR);
			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				if(cell.getColumnIndex()<2){
					continue;
				}
				Object val = ExcelUtil.getCellValue(cell);
				if(val == null){
					break;
				}
				if(StringUtils.isEmpty(val.toString())){
					break;
				}
				if(!CommonUtil1.isDouble(val.toString())){
					throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列值为"+val+",不是有效的数字!");
				}
				values.add(Double.valueOf(val.toString()));
				if(values.size()==capacity){
					//创建日期
					String dateKey = "0_" + cell.getColumnIndex();
					CellRangeAddress dateRange = cellRangeAddressMap.get(dateKey);
					if(dateRange==null){
						throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列没有对应的日期!");
					}
					Object dateVal = ExcelUtil.getCellValue(dateRow.getCell(dateRange.getFirstColumn()));
					if(dateVal==null){
						throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列日期不能为空!");
					}
					Date date = null;
					if(dateVal instanceof Date){
						date = (Date)dateVal;
					}else{
						date = DateUtil.parseDate(dateVal.toString().replace(".","-").replace("/","-").replace("\\","-"),"MM-dd");
					}
					if(date==null){
						throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列日期"+dateVal+"不是有效有日期格式!");
					}
					String monthDateStr = DateUtil.formateDateStr(date,"MM-dd");
					Date createTime = DateUtil.parseDate(currentYear + "-" + monthDateStr,"yyyy-MM-dd");
					//层级信息
					List<Map<String,Object>> spcTags = new ArrayList<Map<String,Object>>();
//					//生产线
					Map<String,Object> layerObj = getLayerImportValue(productLineRow,cell,cellRangeAddressMap,layerMap,"生产线");
					if(layerObj != null){
						spcTags.add(layerObj);
					}
					//检查者
					layerObj = getLayerImportValue(checkerRow,cell,cellRangeAddressMap,layerMap,"检查者");
					if(layerObj != null){
						spcTags.add(layerObj);
					}
					saveSpcSubGroup(values, spcTags,feature, createTime);
					values.clear();
				}
			}
		}
	}
	/**
	 * 焊装SPC模板导入
	 * @param file
	 * @param featureId
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	private void importExcelSpcDatasForHangZhuang(Sheet sheet,Map<String,Cell> cellMap,Map<String,CellRangeAddress> cellRangeAddressMap,Map<String,QualityFeature> featureMap) throws Exception{
		//生产日期行
		Row dateRow  = sheet.getRow(0);
		int lastRowNum = sheet.getLastRowNum();
		for(int i=2;i<=lastRowNum;i++){
			Row row = sheet.getRow(i);
			if(row==null){
				continue;
			}
			String featureName = (String)ExcelUtil.getCellValue(row.getCell(2));
			if(StringUtils.isEmpty(featureName)){
				continue;
			}
			QualityFeature feature = featureMap.get(featureName);
			if(feature==null){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性还没有在基础维护中定义!");
			}
			Map<String,FeatureLayer> layerMap = new HashMap<String, FeatureLayer>();
			for(FeatureLayer layer : feature.getFeatureLayers()){
				layerMap.put(layer.getDetailName(),layer);
			}
			//检查样本数量是否与模板一致
			if(feature.getSampleCapacity()==null){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性还未设置样本容量,请到基础维护中维护!");
			}
			Cell firstDateCell = dateRow.getCell(4);
			CellRangeAddress cellRangeAddress = cellRangeAddressMap.get(dateRow.getRowNum() + "_" + firstDateCell.getColumnIndex());
			int capacity = 1;
			if(cellRangeAddress != null){
				capacity = cellRangeAddress.getLastColumn() - cellRangeAddress.getFirstColumn() + 1;
			}
			if(capacity != feature.getSampleCapacity()){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性样本容量为"+capacity+",与基础维护中定义的样本容量"+feature.getSampleCapacity()+"不一致,请先修改为一致!");
			}
			List<Double> values = new ArrayList<Double>();
			Iterator<Cell> cellIterator =  row.cellIterator();
			Calendar calendar = Calendar.getInstance();
			Integer currentYear = calendar.get(Calendar.YEAR);
			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				//从第四列开始
				if(cell.getColumnIndex()<4){
					continue;
				}
				Object val = ExcelUtil.getCellValue(cell);
				if(val == null){
					break;
				}
				if(StringUtils.isEmpty(val.toString())){
					break;
				}
				if(!CommonUtil1.isDouble(val.toString())){
					throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列值为"+val+",不是有效的数字!");
				}
				values.add(Double.valueOf(val.toString()));
				if(values.size()==capacity){
					//创建日期
					String dateKey = "0_" + cell.getColumnIndex();
					CellRangeAddress dateRange = cellRangeAddressMap.get(dateKey);
					if(dateRange==null){
						throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列没有对应的日期!");
					}
					Object dateVal = ExcelUtil.getCellValue(dateRow.getCell(dateRange.getFirstColumn()));
					if(dateVal==null){
						throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列日期不能为空!");
					}
					Date date = null;
					if(dateVal instanceof Date){
						date = (Date)dateVal;
					}else{
						date = DateUtil.parseDate(dateVal.toString().replace(".","-").replace("/","-").replace("\\","-"),"MM-dd");
					}
					if(date==null){
						throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列日期"+dateVal+"不是有效有日期格式!");
					}
					String monthDateStr = DateUtil.formateDateStr(date,"MM-dd");
					Date createTime = DateUtil.parseDate(currentYear + "-" + monthDateStr,"yyyy-MM-dd");
					//层级信息
					List<Map<String,Object>> spcTags = new ArrayList<Map<String,Object>>();
					saveSpcSubGroup(values, spcTags,feature, createTime);
					values.clear();
				}
			}
		}
	}
	
	/**
	 * 焊装三坐标模板导入
	 * @param file
	 * @param featureId
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	private void importExcelSpcDatasForHangZhuangShangZuoBiao(Sheet sheet,Map<String,Cell> cellMap,Map<String,CellRangeAddress> cellRangeAddressMap,Map<String,QualityFeature> featureMap) throws Exception{
		//生产日期行
		Row dateRow  = sheet.getRow(2);
		//车架号行
		Row cheJiaRow = sheet.getRow(1);
		int lastRowNum = sheet.getLastRowNum();
		for(int i=3;i<=lastRowNum;i++){
			Row row = sheet.getRow(i);
			if(row==null){
				continue;
			}
			//第一列为特性名称
			String featureName = (String)ExcelUtil.getCellValue(row.getCell(0));
			if(StringUtils.isEmpty(featureName)){
				continue;
			}
			QualityFeature feature = featureMap.get(featureName);
			if(feature==null){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性还没有在基础维护中定义!");
			}
			Map<String,FeatureLayer> layerMap = new HashMap<String, FeatureLayer>();
			for(FeatureLayer layer : feature.getFeatureLayers()){
				layerMap.put(layer.getDetailName(),layer);
			}
			//检查样本数量是否与模板一致
			if(feature.getSampleCapacity()==null){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性还未设置样本容量,请到基础维护中维护!");
			}
			Cell firstDateCell = dateRow.getCell(4);
			CellRangeAddress cellRangeAddress = cellRangeAddressMap.get(dateRow.getRowNum() + "_" + firstDateCell.getColumnIndex());
			int capacity = 1;
			if(cellRangeAddress != null){
				capacity = cellRangeAddress.getLastColumn() - cellRangeAddress.getFirstColumn() + 1;
			}
			if(capacity != feature.getSampleCapacity()){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性样本容量为"+capacity+",与基础维护中定义的样本容量"+feature.getSampleCapacity()+"不一致,请先修改为一致!");
			}
			List<Double> values = new ArrayList<Double>();
			Iterator<Cell> cellIterator =  row.cellIterator();
			Calendar calendar = Calendar.getInstance();
			Integer currentYear = calendar.get(Calendar.YEAR);
			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				//从第四列开始
				if(cell.getColumnIndex()<4){
					continue;
				}
				Object val = ExcelUtil.getCellValue(cell);
				if(val == null){
					break;
				}
				if(StringUtils.isEmpty(val.toString())){
					break;
				}
				if(!CommonUtil1.isDouble(val.toString())){
					throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列值为"+val+",不是有效的数字!");
				}
				values.add(Double.valueOf(val.toString()));
				if(values.size()==capacity){
					//创建日期
					int dateColumnIndex = cell.getColumnIndex();
					//检查是否有合并
					String dateKey = "2_" + cell.getColumnIndex();
					CellRangeAddress dateRange = cellRangeAddressMap.get(dateKey);
					if(dateRange!=null){
						dateColumnIndex = dateRange.getFirstColumn();
					}
					Object dateVal = ExcelUtil.getCellValue(dateRow.getCell(dateColumnIndex));
					if(dateVal==null){
						throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列日期不能为空!");
					}
					Date date = null;
					if(dateVal instanceof Date){
						date = (Date)dateVal;
					}else{
						date = DateUtil.parseDate(dateVal.toString().replace(".","-").replace("/","-").replace("\\","-"),"MM-dd");
					}
					if(date==null){
						throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列日期"+dateVal+"不是有效有日期格式!");
					}
					String monthDateStr = DateUtil.formateDateStr(date,"MM-dd");
					Date createTime = DateUtil.parseDate(currentYear + "-" + monthDateStr,"yyyy-MM-dd");
					//层级信息
					List<Map<String,Object>> spcTags = new ArrayList<Map<String,Object>>();
					//车架号
					Map<String,Object> layerObj = getLayerImportValue(cheJiaRow, cell, cellRangeAddressMap,layerMap,"车架号");
					if(layerObj != null){
						spcTags.add(layerObj);
					}
					saveSpcSubGroup(values, spcTags,feature, createTime);
					values.clear();
				}
			}
		}
	}
	
	/**
	 * 涂装模板导入
	 * @param file
	 * @param featureId
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	private void importExcelSpcDatasForTuZhuang(Sheet sheet,Map<String,Cell> cellMap,Map<String,CellRangeAddress> cellRangeAddressMap,Map<String,QualityFeature> featureMap) throws Exception{
		//生产日期行
		Row dateRow  = sheet.getRow(0);
		int lastRowNum = sheet.getLastRowNum();
		for(int i=2;i<=lastRowNum;i++){
			Row row = sheet.getRow(i);
			if(row==null){
				continue;
			}
			//第二列为特性名称
			String featureName = (String)ExcelUtil.getCellValue(row.getCell(1));
			if(StringUtils.isEmpty(featureName)){
				continue;
			}
			QualityFeature feature = featureMap.get(featureName);
			if(feature==null){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性还没有在基础维护中定义!");
			}
			Map<String,FeatureLayer> layerMap = new HashMap<String, FeatureLayer>();
			for(FeatureLayer layer : feature.getFeatureLayers()){
				layerMap.put(layer.getDetailName(),layer);
			}
			//检查样本数量是否与模板一致
			if(feature.getSampleCapacity()==null){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性还未设置样本容量,请到基础维护中维护!");
			}
			Cell firstDateCell = dateRow.getCell(4);
			CellRangeAddress cellRangeAddress = cellRangeAddressMap.get(dateRow.getRowNum() + "_" + firstDateCell.getColumnIndex());
			int capacity = 1;
			if(cellRangeAddress != null){
				capacity = cellRangeAddress.getLastColumn() - cellRangeAddress.getFirstColumn() + 1;
			}
			if(capacity != feature.getSampleCapacity()){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性样本容量为"+capacity+",与基础维护中定义的样本容量"+feature.getSampleCapacity()+"不一致,请先修改为一致!");
			}
			List<Double> values = new ArrayList<Double>();
			Iterator<Cell> cellIterator =  row.cellIterator();
			Calendar calendar = Calendar.getInstance();
			Integer currentYear = calendar.get(Calendar.YEAR);
			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				//从第四列开始
				if(cell.getColumnIndex()<4){
					continue;
				}
				Object val = ExcelUtil.getCellValue(cell);
				if(val == null){
					break;
				}
				if(StringUtils.isEmpty(val.toString())){
					break;
				}
				if(!CommonUtil1.isDouble(val.toString())){
					throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列值为"+val+",不是有效的数字!");
				}
				values.add(Double.valueOf(val.toString()));
				if(values.size()==capacity){
					//创建日期
					int dateColumnIndex = cell.getColumnIndex();
					//检查是否有合并
					String dateKey = "0_" + cell.getColumnIndex();
					CellRangeAddress dateRange = cellRangeAddressMap.get(dateKey);
					if(dateRange!=null){
						dateColumnIndex = dateRange.getFirstColumn();
					}
					Object dateVal = ExcelUtil.getCellValue(dateRow.getCell(dateColumnIndex));
					if(dateVal==null){
						throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列日期不能为空!");
					}
					Date date = null;
					if(dateVal instanceof Date){
						date = (Date)dateVal;
					}else{
						date = DateUtil.parseDate(dateVal.toString().replace(".","-").replace("/","-").replace("\\","-"),"MM-dd");
					}
					if(date==null){
						throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列日期"+dateVal+"不是有效有日期格式!");
					}
					String monthDateStr = DateUtil.formateDateStr(date,"MM-dd");
					Date createTime = DateUtil.parseDate(currentYear + "-" + monthDateStr,"yyyy-MM-dd");
					//层级信息
					List<Map<String,Object>> spcTags = new ArrayList<Map<String,Object>>();
//					//车架号
//					Map<String,Object> layerObj = getLayerImportValue(cheJiaRow, cell, cellRangeAddressMap,layerMap,"车架号");
//					if(layerObj != null){
//						spcTags.add(layerObj);
//					}
					saveSpcSubGroup(values, spcTags,feature, createTime);
					values.clear();
				}
			}
		}
	}
	
	/**
	 * 总装模板导入
	 * @param file
	 * @param featureId
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	private void importExcelSpcDatasForZhongZhuang(Sheet sheet,Map<String,Cell> cellMap,Map<String,CellRangeAddress> cellRangeAddressMap,Map<String,QualityFeature> featureMap) throws Exception{
		//生产日期行
		Row dateRow  = sheet.getRow(0);
		//车身号
//		Row cheShengRow = sheet.getRow(1);
		int lastRowNum = sheet.getLastRowNum();
		//避免重复
		Map<String,Boolean> existFeatureMap = new HashMap<String, Boolean>();
		for(int i=2;i<=lastRowNum;i++){
			Row row = sheet.getRow(i);
			if(row==null){
				continue;
			}
			//第二列为特性名称
			String featureName = (String)ExcelUtil.getCellValue(row.getCell(1));
			if(StringUtils.isEmpty(featureName)){
				continue;
			}
			QualityFeature feature = featureMap.get(featureName);
			if(feature==null){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性还没有在基础维护中定义!");
			}
			if(existFeatureMap.containsKey(featureName)){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性重复,同一个Sheet中测量点序号不能重复!");
			}
			existFeatureMap.put(featureName, true);
			
			Map<String,FeatureLayer> layerMap = new HashMap<String, FeatureLayer>();
			for(FeatureLayer layer : feature.getFeatureLayers()){
				layerMap.put(layer.getDetailName(),layer);
			}
			//检查样本数量是否与模板一致
			if(feature.getSampleCapacity()==null){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性还未设置样本容量,请到基础维护中维护!");
			}
			Cell firstDateCell = dateRow.getCell(4);
			CellRangeAddress cellRangeAddress = cellRangeAddressMap.get(dateRow.getRowNum() + "_" + firstDateCell.getColumnIndex());
			int capacity = 1;
			if(cellRangeAddress != null){
				capacity = cellRangeAddress.getLastColumn() - cellRangeAddress.getFirstColumn() + 1;
			}
			if(capacity != feature.getSampleCapacity()){
				throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性样本容量为"+capacity+",与基础维护中定义的样本容量"+feature.getSampleCapacity()+"不一致,请先修改为一致!");
			}
			List<Double> values = new ArrayList<Double>();
			Iterator<Cell> cellIterator =  row.cellIterator();
			Calendar calendar = Calendar.getInstance();
			Integer currentYear = calendar.get(Calendar.YEAR);
			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				//从第四列开始
				if(cell.getColumnIndex()<4){
					continue;
				}
				Object val = ExcelUtil.getCellValue(cell);
				if(val == null){
					break;
				}
				if(StringUtils.isEmpty(val.toString())){
					break;
				}
				if(!CommonUtil1.isDouble(val.toString())){
					throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列值为"+val+",不是有效的数字!");
				}
				values.add(Double.valueOf(val.toString()));
				if(values.size()==capacity){
					//创建日期
					int dateColumnIndex = cell.getColumnIndex();
					//检查是否有合并
					String dateKey = "0_" + cell.getColumnIndex();
					CellRangeAddress dateRange = cellRangeAddressMap.get(dateKey);
					if(dateRange!=null){
						dateColumnIndex = dateRange.getFirstColumn();
					}
					Object dateVal = ExcelUtil.getCellValue(dateRow.getCell(dateColumnIndex));
					if(dateVal==null){
						throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列日期不能为空!");
					}
					Date date = null;
					if(dateVal instanceof Date){
						date = (Date)dateVal;
					}else{
						date = DateUtil.parseDate(dateVal.toString().replace(".","-").replace("/","-").replace("\\","-"),"MM-dd");
					}
					if(date==null){
						throw new AmbFrameException("第"+(i+1)+"行名称为["+featureName+"]的质量特性在第"+(cell.getColumnIndex()+1)+"列日期"+dateVal+"不是有效有日期格式!");
					}
					String monthDateStr = DateUtil.formateDateStr(date,"MM-dd");
					Date createTime = DateUtil.parseDate(currentYear + "-" + monthDateStr,"yyyy-MM-dd");
					//层级信息
					List<Map<String,Object>> spcTags = new ArrayList<Map<String,Object>>();
//					//车架号
//					Map<String,Object> layerObj = getLayerImportValue(cheJiaRow, cell, cellRangeAddressMap,layerMap,"车架号");
//					if(layerObj != null){
//						spcTags.add(layerObj);
//					}
					saveSpcSubGroup(values, spcTags,feature, createTime);
					values.clear();
				}
			}
		}
	}
	
	/**
	 * 冲压SPC模板导入
	 * @param file
	 * @param featureId
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	public List<Option> importExcelSpcDatas_1(File file,Long processDefineId) throws Exception{
		InputStream fis = new FileInputStream(file);
		Workbook wb = WorkbookFactory.create(fis); // 从文件流中获取Excel工作区对象（WorkBook）
		Sheet sheet = wb.getSheetAt(0);
		if(sheet == null){
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		List<String> modelSet = new ArrayList<String>();
		modelSet.add("0:0:0:1");
		modelSet.add("1:1:0:1");
		modelSet.add("2:2:0:1");
		modelSet.add("3:3:0:1");
		modelSet.add("4:4:0:1");
		List<String> modelSetName = new ArrayList<String>();
		modelSetName.add("生产日期(月/日)");
		modelSetName.add("零件下线时间(时/分)");
		modelSetName.add("生产线");
		modelSetName.add("样件名称");
		modelSetName.add("检查者");
		int sheetMergeCount = sheet.getNumMergedRegions(); 
		if(sheetMergeCount<5){
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		Set<String> excelSet = new HashSet<String>();
		List<String> qlist = new ArrayList<String>();
		Integer mainSampleCapacity = null;
		for(int j = 0 ; j < sheetMergeCount ; j++ ){  
	        CellRangeAddress ca = sheet.getMergedRegion(j);
	        excelSet.add(ca.getFirstRow()+":"+ca.getLastRow()+":"+ca.getFirstColumn()+":"+ca.getLastColumn());
	        if(ca.getFirstRow() == 0 && ca.getFirstColumn()>= 2){//生产日期
	        	if(mainSampleCapacity == null){
	        		mainSampleCapacity = ca.getLastColumn() - ca.getFirstColumn();
	        	}
	        	if(ca.getFirstRow() != ca.getLastRow() || (mainSampleCapacity !=null && (ca.getLastColumn() - ca.getFirstColumn())!= mainSampleCapacity)){
	        		throw new AmbFrameException("导入的excel模板格式不正确！");
	        	}
	        }
	        if(ca.getFirstRow() == 2 && ca.getFirstColumn()>= 2){//生产线
	        	if(mainSampleCapacity == null){
	        		mainSampleCapacity = ca.getLastColumn() - ca.getFirstColumn();
	        	}
	        	if(ca.getFirstRow() != ca.getLastRow() || (mainSampleCapacity !=null && (ca.getLastColumn() - ca.getFirstColumn())!= mainSampleCapacity)){
	        		throw new AmbFrameException("导入的excel模板格式不正确！");
	        	}
	        }
	        if(ca.getFirstRow() >=5 && ca.getFirstColumn() == 0){//质量特性
	        	if(ca.getLastRow() != ca.getFirstRow() || ca.getLastColumn() != 1){
	        		throw new AmbFrameException("导入的excel模板格式不正确！");
	        	}
	        	Row row = sheet.getRow(ca.getFirstRow());
	        	String code = ExcelUtil.getCellValue(row.getCell(0))==null?null:ExcelUtil.getCellValue(row.getCell(0)).toString();
	        	if(StringUtils.isNotEmpty(code)){
	        		qlist.add(code+"_"+ca.getFirstRow());
	        	}
	        }
	    }
		for (int i = 0; i < modelSet.size(); i++) {
			String key= modelSet.get(i);
			if(!excelSet.contains(key)){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
			String str [] = key.split(":");
			Row row = sheet.getRow(Integer.parseInt(str[0]));
			if(row==null||!modelSetName.get(i).equals(ExcelUtil.getCellValue(row.getCell(0)))){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
		}
		Row row_0 = sheet.getRow(0);
		int lastColumn = row_0.getLastCellNum();
		if(mainSampleCapacity == null){
			mainSampleCapacity = 1;
		}else{
			for (int i = 2; i < lastColumn; i++) {
				String key = "0:0:"+i+":"+(i+mainSampleCapacity);
				if(!excelSet.contains(key)){
					throw new AmbFrameException("导入的excel模板格式不正确！");
				}
				i+=mainSampleCapacity;
			}
			mainSampleCapacity++;
		}
		List<Option> options = new ArrayList<Option>();
		List<Map<String ,Object>> qMaps = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < qlist.size(); i++) {
			int index = qlist.get(i).lastIndexOf("_");
			String code = qlist.get(i).substring(0, index);
			String rowIndex = qlist.get(i).substring(index+1, qlist.get(i).length());
        	QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureByCode(code);
        	if(qualityFeature == null){
        		throw new AmbFrameException("导入的excel模板格式中，找不到简码【"+code+"】的质量特性");
        	}
        	if(!mainSampleCapacity.equals(qualityFeature.getSampleCapacity())){
        		throw new AmbFrameException("导入的excel模板格式中，简码【"+code+"】的质量特性中的样本容量不匹配！");
        	}
        	List<FeatureLayer> flist = qualityFeature.getFeatureLayers();
        	FeatureLayer f1 = null;
        	FeatureLayer f2 = null;
        	for (int j = 0; j < flist.size(); j++) {
				if("生产线".equals(flist.get(j).getDetailName())){
					f1 = flist.get(j);
				}
				if("检查者".equals(flist.get(j).getDetailName())){
					f2 = flist.get(j);
				}
			}
        	if(flist.isEmpty() || f1 == null || f2 == null){
        		throw new AmbFrameException("导入的excel模板格式中，简码【"+code+"】的质量特性中的层别信息不匹配！");
        	}
        	Map<String,Object> map = new HashMap<String, Object>();
        	map.put("row", rowIndex);
        	map.put("q", qualityFeature);
        	map.put("f1", f1);
        	map.put("f2", f2);
        	qMaps.add(map);
        	Option option = new Option();
        	option.setName(qualityFeature.getName());
        	option.setValue(qualityFeature.getId().toString());
        	options.add(option);
		}
		
		for (int i = 0; i < qMaps.size(); i++) {
			Map<String,Object> map = qMaps.get(i);
			Integer rowIndex = Integer.parseInt(map.get("row").toString());
			QualityFeature qualityFeature = (QualityFeature)map.get("q");
			FeatureLayer f1 = (FeatureLayer)map.get("f1");//生产线
        	FeatureLayer f2 = (FeatureLayer)map.get("f2");//检查者
			Row row = sheet.getRow(rowIndex);
			for (int j = 2; j < row.getLastCellNum(); j += mainSampleCapacity) {
				List<Double> values = new ArrayList<Double>();
				for (int k = j; k < (j + mainSampleCapacity); k++) {
					String samValue = ExcelUtil.getCellValue(row.getCell(k))==null?null:ExcelUtil.getCellValue(row.getCell(k)).toString();
					if(StringUtils.isNotEmpty(samValue)){
						if(!CommonUtil1.isDouble(samValue.toString())){
							throw new AmbFrameException("导入的excel模板中有无法获取行【"+(rowIndex+1)+"】列【"+(k+1)+"】的值，请填写正确的格式或填写完整！");
						}else{
							values.add(Double.parseDouble(samValue.toString()));
						}
					}
				}
				if(values.size() != mainSampleCapacity){
					continue;
				}
				Row created_date_row = sheet.getRow(0);
				String created_date = ExcelUtil.getCellValue(created_date_row.getCell(j))==null?null:ExcelUtil.getCellValue(created_date_row.getCell(j)).toString();
				if(created_date == null){
					throw new AmbFrameException("导入的excel模板中有存在【"+(rowIndex+1)+"】列【"+(j+1)+"】【生产日期(月/日)】无法正确读取！");
				}
				String date [] = created_date.split("\\.");
				if(date.length != 2 || !CommonUtil1.isInteger(date[0]) || !CommonUtil1.isInteger(date[1])){
					throw new AmbFrameException("导入的excel模板中有存在【"+(rowIndex+1)+"】列【"+(j+1)+"】【生产日期(月/日)】无法正确读取！");
				}
				Row created_time_row = sheet.getRow(1);
				Calendar createdTimeTemp = Calendar.getInstance();
				try {
					Date created_time_temp = null;
					for (int k = j; k < (j+mainSampleCapacity); k++) {
						created_time_temp = ExcelUtil.getCellValue(created_time_row.getCell(k))==null&&"".equals(ExcelUtil.getCellValue(created_time_row.getCell(k)))?null:created_time_row.getCell(k).getDateCellValue();
						if(created_time_temp != null){
							break;
						}
					}
					if(created_time_temp != null){
						createdTimeTemp.setTime(created_time_temp);
					}else{
						throw new AmbFrameException("导入的excel模板中有存在【"+(rowIndex+1)+"】列【"+(j+1)+"】【零件下线时间(时/分)】无法正确读取！");
					}
				} catch (Exception e) {
					throw new AmbFrameException("导入的excel模板中有存在【"+(rowIndex+1)+"】列【"+(j+1)+"】【零件下线时间(时/分)】无法正确读取！");
				}
				Calendar calendar = Calendar.getInstance();
				Date createdTime = null;
				try {
					String dateStr = calendar.get(Calendar.YEAR)+"-"+Integer.parseInt(date[0])+"-"+Integer.parseInt(date[1])+" "+createdTimeTemp.get(Calendar.HOUR_OF_DAY)+ ":" +createdTimeTemp.get(Calendar.MINUTE) + ":0";
					createdTime = DateUtil.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
				} catch (Exception e) {
					throw new AmbFrameException("导入的excel模板中有存在这样的日期："+calendar.get(Calendar.YEAR)+"-"+Integer.parseInt(date[0])+"-"+Integer.parseInt(date[1])+" "+createdTimeTemp.get(Calendar.HOUR_OF_DAY)+ ":" +createdTimeTemp.get(Calendar.MINUTE)+ ":0");
				}
				List<Map<String,Object>> spcSgTags = new ArrayList<Map<String,Object>>();
				
				Row cbxx_1_row = sheet.getRow(2);
				String cbxx_1 = ExcelUtil.getCellValue(cbxx_1_row.getCell(j))==null?null:ExcelUtil.getCellValue(cbxx_1_row.getCell(j)).toString();
				if(cbxx_1 != null){
					Map<String,Object> spcSgTagMap = new HashMap<String, Object>(); 
					spcSgTagMap.put("value", cbxx_1);
					spcSgTagMap.put("featureLayer", f1);
					spcSgTags.add(spcSgTagMap);
				}
				Row cbxx_2_row = sheet.getRow(4);
				String cbxx_2 = null;
				for (int k = j; k < (j+mainSampleCapacity); k++) {
					cbxx_2 = ExcelUtil.getCellValue(cbxx_2_row.getCell(k))==null?null:ExcelUtil.getCellValue(cbxx_2_row.getCell(k)).toString();
					if(cbxx_2 != null){
						break;
					}
				}
				if(cbxx_2 != null){
					Map<String,Object> spcSgTagMap = new HashMap<String, Object>(); 
					spcSgTagMap.put("value", cbxx_2);
					spcSgTagMap.put("featureLayer", f2);
					spcSgTags.add(spcSgTagMap);
				}
				saveSpcSubGroup(values, spcSgTags, qualityFeature, createdTime);
			}
		}
		return options;
	}
	@Transactional
	public void saveSpcSubGroup(List<Double> values, List<Map<String,Object>> spcSgTags, QualityFeature qualityFeature, Date createdTime){
		SpcSubGroup sub = spcSubGroupDao.getSpcSubGroupByQualityFeatureAndEntryDate(qualityFeature, createdTime);;
		if(sub == null){
			sub = new SpcSubGroup();
			sub.setCompanyId(ContextUtils.getCompanyId());
			sub.setCreatedTime(createdTime);
			sub.setCreator(ContextUtils.getLoginName());
			sub.setCreatorName(ContextUtils.getUserName());
			sub.setModifiedTime(new Date());
			sub.setModifier(ContextUtils.getLoginName());
			sub.setModifierName(ContextUtils.getUserName());
			sub.setActualSmapleNum(1);
			sub.setEntryDate(createdTime);
			sub.setQualityFeature(qualityFeature);
			sub.setSubGroupSize(qualityFeature.getSampleCapacity());
			int num = getNumByFeature(qualityFeature) + 1;
			sub.setSubGroupOrderNum(num);
			sub.setEntryUser(ContextUtils.getUserName());
			spcSubGroupDao.save(sub);
		}else{
			sub.setModifiedTime(new Date());
			sub.setModifier(ContextUtils.getLoginName());
			sub.setModifierName(ContextUtils.getUserName());
		}
		//详情数据
		if(values != null && values.size()>0){
			if(sub.getSpcSgSamples()==null){
				sub.setSpcSgSamples(new ArrayList<SpcSgSample>());
			}else{
				sub.getSpcSgSamples().clear();
			}
			for (int i = 0; i < values.size(); i++) {
				SpcSgSample spcSgSample = new SpcSgSample();
				spcSgSample.setCreatedTime(new Date());
				spcSgSample.setCompanyId(ContextUtils.getCompanyId());
				spcSgSample.setCreator(ContextUtils.getLoginName());
				spcSgSample.setCreatorName(ContextUtils.getUserName());
				spcSgSample.setModifiedTime(new Date());
				spcSgSample.setModifier(ContextUtils.getLoginName());
				spcSgSample.setModifierName(ContextUtils.getUserName());
				spcSgSample.setSpcSubGroup(sub);
				spcSgSample.setSamValue(values.get(i));
				spcSgSample.setSampleOrderNum((i +1 ) + "");;
				spcSgSample.setSampleNo("X" + (i + 1));
				sub.getSpcSgSamples().add(spcSgSample);
			}
		}
		//层别信息
		if(spcSgTags != null && spcSgTags.size() > 0){
			if(sub.getSpcSgTags() == null){
				sub.setSpcSgTags(new ArrayList<SpcSgTag>());
			}else{
				sub.getSpcSgTags().clear();
			}
			for (int i = 0; i < spcSgTags.size(); i++) {
				Map<String,Object> map = spcSgTags.get(i);
				FeatureLayer featureLayer = (FeatureLayer)map.get("featureLayer");
				SpcSgTag spcSgTag = new SpcSgTag();
				spcSgTag.setCreatedTime(sub.getCreatedTime());//创建时间与SPC组一致,提高查询效率
				spcSgTag.setCompanyId(ContextUtils.getCompanyId());
				spcSgTag.setCreator(ContextUtils.getUserName());
				spcSgTag.setModifiedTime(new Date());
				spcSgTag.setModifier(ContextUtils.getUserName());
				spcSgTag.setTagName(featureLayer.getDetailName());
				spcSgTag.setTagCode(featureLayer.getDetailCode());
				spcSgTag.setMethod(featureLayer.getSampleMethod());
				spcSgTag.setSpcSubGroup(sub);
				spcSgTag.setTagValue(map.get("value").toString());
				sub.getSpcSgTags().add(spcSgTag);
			}
		}
		spcSubGroupDao.save(sub);
		updateSpcSubGroup(sub,qualityFeature);
	}
	
	/**
	 * 计划/更改spcSubGroup的一些值  并且判断异警
	 * @param spcSubGroup
	 * @param qualityFeature
	 */
	@Transactional
	public void updateSpcSubGroup(SpcSubGroup spcSubGroup, QualityFeature qualityFeature){
		Double sum = 0.0,sigma = 0.0,randDiff = 0.0;
		for(SpcSgSample sample : spcSubGroup.getSpcSgSamples()){
			sum = sum + sample.getSamValue();
		}
		sigma = sum/spcSubGroup.getSpcSgSamples().size();
		Double max = 0.0,min = 0.0;
		Double tempMax = spcSubGroup.getSpcSgSamples().get(0).getSamValue();
		Double tempMin = tempMax;
		for(int o=0;o<spcSubGroup.getSpcSgSamples().size();o++){ 
			max = spcSubGroup.getSpcSgSamples().get(o).getSamValue() >= tempMax?spcSubGroup.getSpcSgSamples().get(o).getSamValue():tempMax;
			tempMax = max;
			min = spcSubGroup.getSpcSgSamples().get(o).getSamValue() <= tempMin?spcSubGroup.getSpcSgSamples().get(o).getSamValue():tempMin;
			tempMin = min;
		}
		randDiff = max - min;
		spcSubGroup.setMaxValue(new Double(new DecimalFormat( "0.000" ).format(max)));
		spcSubGroup.setMinValue(new Double(new DecimalFormat( "0.000" ).format(min)));
		spcSubGroup.setSigma(new Double(new DecimalFormat( "0.000" ).format(sigma)));
		spcSubGroup.setRangeDiff(new Double(new DecimalFormat( "0.000" ).format(randDiff)));
		spcSubGroup.setActualSmapleNum(spcSubGroup.getSpcSgSamples().size()+1);
		spcSubGroup.setSubGroupSize(qualityFeature.getSampleCapacity());
		spcSubGroupDao.save(spcSubGroup);
		if(spcSubGroup.getSubGroupSize() != null && spcSubGroup.getSubGroupSize().equals(spcSubGroup.getSpcSgSamples().size())){
			//根据条件查询采集的数据
			String hql = "from SpcSubGroup s where s.companyId = ?  and s.qualityFeature = ? and s.createdTime between ? and ?";
			List<Object> searchParams = new ArrayList<Object>();
			searchParams.add(ContextUtils.getCompanyId());
			searchParams.add(qualityFeature);
			//10天以前的数据
			Calendar calendar = Calendar.getInstance();
			searchParams.add(calendar.getTime());
			calendar.add(Calendar.DATE, -10);
			searchParams.add(calendar.getTime());
			List<SpcSubGroup> list = spcSubGroupDao.find(hql,searchParams.toArray());
//			for (int i = 0; i < spcSubGroup.getSubGroupOrderNum(); i++) {
//				//根据规则检测所查询的数据  异常报警
//				abnormalInfoManager.lanchAbnormal(spcSubGroup.getSubGroupOrderNum()+"", qualityFeature, list, spcSubGroupDao.getSession());
//			}
//			//根据规则检测所查询的数据  异常报警
			abnormalInfoManager.lanchAbnormal(spcSubGroup.getSubGroupOrderNum()+"", qualityFeature, list, spcSubGroupDao.getSession());
		}
	}
	
	/**
	 * 焊装三坐标模板导入
	 * @param file
	 * @param featureId
	 * @return
	 */
	@Transactional
	public List<Option> importExcelSpcDatas_2(File file) throws Exception{
		InputStream fis = new FileInputStream(file);
		Workbook wb = WorkbookFactory.create(fis); // 从文件流中获取Excel工作区对象（WorkBook）
		if(wb instanceof HSSFWorkbook){
			wb= (HSSFWorkbook)wb;
		}else if(wb instanceof XSSFWorkbook){
			wb= (XSSFWorkbook)wb;
		}
		Sheet sheet = wb.getSheetAt(0);
		if(sheet == null){
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		Map<String,String> modelMap = new HashMap<String, String>();
		modelMap.put("0:1:0:3","控股焊装厂");
		//测量点,SPC点,理论值,公差,车架号
		int sheetMergeCount = sheet.getNumMergedRegions(); 
		if(sheetMergeCount<1){
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		Set<String> excelSet = new HashSet<String>();
		Integer mainSampleCapacity = null;
		for(int j = 0 ; j < sheetMergeCount ; j++ ){  
	        CellRangeAddress ca = sheet.getMergedRegion(j);
	        excelSet.add(ca.getFirstRow()+":"+ca.getLastRow()+":"+ca.getFirstColumn()+":"+ca.getLastColumn());
	        if(ca.getFirstRow() == 1 && ca.getFirstColumn()>= 4){//车架号
	        	if(mainSampleCapacity == null){
	        		mainSampleCapacity = ca.getLastColumn() - ca.getFirstColumn();
	        	}
	        	if(ca.getFirstRow() != ca.getLastRow() || (mainSampleCapacity !=null && (ca.getLastColumn() - ca.getFirstColumn())!= mainSampleCapacity)){
	        		throw new AmbFrameException("导入的excel模板格式不正确！");
	        	}
	        }
	        if(ca.getFirstRow() == 2 && ca.getFirstColumn()>= 4){//生产日期
	        	if(mainSampleCapacity == null){
	        		mainSampleCapacity = ca.getLastColumn() - ca.getFirstColumn();
	        	}
	        	if(ca.getFirstRow() != ca.getLastRow() || (mainSampleCapacity !=null && (ca.getLastColumn() - ca.getFirstColumn())!= mainSampleCapacity)){
	        		throw new AmbFrameException("导入的excel模板格式不正确！");
	        	}
	        }
	    }
		Row row_0 = sheet.getRow(0);
		int lastColumn = row_0.getLastCellNum();
		if(mainSampleCapacity == null){
			mainSampleCapacity = 1;
		}else{
			for (int i = 4; i < lastColumn; i++) {
				String key = "2:2:"+i+":"+(i+mainSampleCapacity);
				if(!excelSet.contains(key)){
					throw new AmbFrameException("导入的excel模板格式不正确！");
				}
				i+=mainSampleCapacity;
			}
			mainSampleCapacity++;
		}
		modelMap.put("0:0:4:"+(lastColumn-1),"车架号");
		for(String key : modelMap.keySet()){
			if(!excelSet.contains(key)){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
			String str[] = key.split(":");
			Row row = sheet.getRow(Integer.parseInt(str[0]));
			if(!modelMap.get(key).equals(ExcelUtil.getCellValue(row.getCell(Integer.parseInt(str[2]))))){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
		}
		try {
			if(!"测量点".equals(sheet.getRow(2).getCell(0).getStringCellValue())){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
			if(!"SPC点".equals(sheet.getRow(2).getCell(1).getStringCellValue())){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
			if(!"理论值".equals(sheet.getRow(2).getCell(2).getStringCellValue())){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
			if(!"公差".equals(sheet.getRow(2).getCell(3).getStringCellValue())){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
		} catch (Exception e) {
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		List<Option> options = new ArrayList<Option>();
		List<Map<String,Object>> qlist = new ArrayList<Map<String,Object>>();
		for (int i = 3; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			String code = ExcelUtil.getCellValue(row.getCell(0))==null?null:ExcelUtil.getCellValue(row.getCell(0)).toString();
			if(StringUtils.isNotEmpty(code)){
				QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureByCode(code);
	        	if(qualityFeature == null){
	        		throw new AmbFrameException("导入的excel模板格式中，找不到简码【"+code+"】的质量特性");
	        	}
	        	if(!mainSampleCapacity.equals(qualityFeature.getSampleCapacity())){
	        		throw new AmbFrameException("导入的excel模板格式中，简码【"+code+"】的质量特性中的样本容量不匹配！");
	        	}
	        	List<FeatureLayer> flist = qualityFeature.getFeatureLayers();
	        	FeatureLayer f1 = null;
	        	for (int j = 0; j < flist.size(); j++) {
					if("车架号".equals(flist.get(j).getDetailName())){
						f1 = flist.get(j);
					}
				}
	        	if(flist.isEmpty() || f1 == null){
	        		throw new AmbFrameException("导入的excel模板格式中，简码【"+code+"】的质量特性中的层别信息不匹配！");
	        	}
	        	Map<String,Object> map = new HashMap<String, Object>();
	        	map.put("row", i);
	        	map.put("q", qualityFeature);
	        	map.put("f1", f1);
	        	qlist.add(map);
	        	Option option = new Option();
	        	option.setName(qualityFeature.getName());
	        	option.setValue(qualityFeature.getId().toString());
	        	options.add(option);
			}
		}
		for (int i = 0; i < qlist.size(); i++) {
			Map<String,Object> map = qlist.get(i);
			Integer rowIndex = Integer.parseInt(map.get("row").toString());
			QualityFeature qualityFeature = (QualityFeature)map.get("q");
			FeatureLayer f1 = (FeatureLayer)map.get("f1");//车架号
			Row row = sheet.getRow(rowIndex);
			for (int j = 4; j < lastColumn; j += mainSampleCapacity) {
				List<Double> values = new ArrayList<Double>();
				for (int k = j; k < (j + mainSampleCapacity); k++) {
					String samValue = ExcelUtil.getCellValue(row.getCell(k))==null?null:ExcelUtil.getCellValue(row.getCell(k)).toString();
					if(StringUtils.isNotEmpty(samValue)){
						if(!CommonUtil1.isDouble(samValue.toString())){
							throw new AmbFrameException("导入的excel模板中有无法获取行【"+(rowIndex+1)+"】列【"+(k+1)+"】的值，请填写正确的格式或填写完整！");
						}else{
							values.add(Double.parseDouble(samValue.toString()));
						}
					}
				}
				if(values.size() != mainSampleCapacity){
					continue;
				}
				Date createdTime = null;
				try {
					Row created_time_row = sheet.getRow(2);
					createdTime = ExcelUtil.getCellValue(created_time_row.getCell(j))==null?null:created_time_row.getCell(j).getDateCellValue();
				} catch (Exception e) {
					throw new AmbFrameException("导入的excel模板中有存行【3】列【"+(j+1)+"】无法正确读取日期！");
				}
				List<Map<String,Object>> spcSgTags = new ArrayList<Map<String,Object>>();
				Row cbxx_1_row = sheet.getRow(1);
				String cbxx_1 = ExcelUtil.getCellValue(cbxx_1_row.getCell(j))==null?null:ExcelUtil.getCellValue(cbxx_1_row.getCell(j)).toString();
				if(cbxx_1 != null){
					Map<String,Object> spcSgTagMap = new HashMap<String, Object>(); 
					spcSgTagMap.put("value", cbxx_1);
					spcSgTagMap.put("featureLayer", f1);
					spcSgTags.add(spcSgTagMap);
				}
				saveSpcSubGroup(values, spcSgTags, qualityFeature, createdTime);
			}
		}
		return options;
	}
	
	/**
	 * 焊装SPC模板导入
	 * @param file
	 * @param featureId
	 * @return
	 */
	@Transactional
	public List<Option> importExcelSpcDatas_3(File file) throws Exception{
		InputStream fis = new FileInputStream(file);
		Workbook wb = WorkbookFactory.create(fis); // 从文件流中获取Excel工作区对象（WorkBook）
		if(wb instanceof HSSFWorkbook){
			wb= (HSSFWorkbook)wb;
		}else if(wb instanceof XSSFWorkbook){
			wb= (XSSFWorkbook)wb;
		}
		Sheet sheet = wb.getSheet("SPC导入表");
		if(sheet == null){
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		Map<String,String> modelMap = new HashMap<String, String>();
		modelMap.put("0:1:0:0","项目名称");
		modelMap.put("0:1:1:1","测量点序号");
		modelMap.put("0:1:2:2","测量位置");
		//测量点,SPC点,理论值,公差,车架号
		int sheetMergeCount = sheet.getNumMergedRegions(); 
		if(sheetMergeCount<3){
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		Set<String> excelSet = new HashSet<String>();
		Set<String> qSet = new HashSet<String>();
		Integer mainSampleCapacity = null;
		for(int j = 0 ; j < sheetMergeCount ; j++ ){  
	        CellRangeAddress ca = sheet.getMergedRegion(j);
	        excelSet.add(ca.getFirstRow()+":"+ca.getLastRow()+":"+ca.getFirstColumn()+":"+ca.getLastColumn());
	        if(ca.getFirstRow() == 0 && ca.getFirstColumn()>= 4){//生产日期
	        	if(mainSampleCapacity == null){
	        		mainSampleCapacity = ca.getLastColumn() - ca.getFirstColumn();
	        	}
	        	if(ca.getFirstRow() != ca.getLastRow() || (mainSampleCapacity !=null && (ca.getLastColumn() - ca.getFirstColumn())!= mainSampleCapacity)){
	        		throw new AmbFrameException("导入的excel模板格式不正确！");
	        	}
	        }
	        if(ca.getFirstRow() >=2 && ca.getFirstColumn() == 0 && ca.getLastColumn() == 0){
	        	qSet.add(ca.getFirstRow()+":"+ca.getLastRow()+":"+ca.getFirstColumn()+":"+ca.getLastColumn());
	        }
	        
	    }
		Row row_0 = sheet.getRow(0);
		int lastColumn = row_0.getLastCellNum();
		if(mainSampleCapacity == null){
			mainSampleCapacity = 1;
		}else{
			for (int i = 4; i < lastColumn; i++) {
				String key = "0:0:"+i+":"+(i+mainSampleCapacity);
				if(!excelSet.contains(key)){
					throw new AmbFrameException("导入的excel模板格式不正确！");
				}
				i+=mainSampleCapacity;
			}
			mainSampleCapacity++;
		}
		for(String key : modelMap.keySet()){
			if(!excelSet.contains(key)){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
			String str[] = key.split(":");
			Row row = sheet.getRow(Integer.parseInt(str[0]));
			if(!modelMap.get(key).equals(ExcelUtil.getCellValue(row.getCell(Integer.parseInt(str[2]))))){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
		}
		try {
			if(!"日期".equals(row_0.getCell(3).getStringCellValue())){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
			if(!"型面间隙".equals(sheet.getRow(1).getCell(3).getStringCellValue())){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
		} catch (Exception e) {
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		List<Option> options = new ArrayList<Option>();
		List<Map<String,Object>> qlist = new ArrayList<Map<String,Object>>();
		for (int i = 2; i <= sheet.getLastRowNum(); i++) {
			int rowspan = i;
			for (String key : qSet) {//判断合并列
				String str[] = key.split(":");
				if((i+"").equals(str[0])){
					rowspan = Integer.parseInt(str[1]);
				}
			}
			String code = ExcelUtil.getCellValue(sheet.getRow(i).getCell(0))==null?null:ExcelUtil.getCellValue(sheet.getRow(i).getCell(0)).toString();
			if(StringUtils.isNotEmpty(code)){
				for (int j = i; j <= rowspan; j++) {
					Row row = sheet.getRow(j);
					String index = ExcelUtil.getCellValue(row.getCell(1))==null?null:ExcelUtil.getCellValue(row.getCell(1)).toString();
					if(StringUtils.isNotEmpty(index)){
						QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureByCode((code+index));
			        	if(qualityFeature == null){
			        		throw new AmbFrameException("导入的excel模板格式中，找不到简码【"+(code+index)+"】的质量特性");
			        	}
			        	if(!mainSampleCapacity.equals(qualityFeature.getSampleCapacity())){
			        		throw new AmbFrameException("导入的excel模板格式中，简码【"+(code+index)+"】的质量特性中的样本容量不匹配！");
			        	}
			        	Map<String,Object> map = new HashMap<String, Object>();
			        	map.put("row", j);
			        	map.put("q", qualityFeature);
			        	qlist.add(map);
			        	Option option = new Option();
			        	option.setName(qualityFeature.getName());
			        	option.setValue(qualityFeature.getId().toString());
			        	options.add(option);
					}
				}
			}
			i = rowspan;
		}
		for (int i = 0; i < qlist.size(); i++) {
			Map<String,Object> map = qlist.get(i);
			Integer rowIndex = Integer.parseInt(map.get("row").toString());
			QualityFeature qualityFeature = (QualityFeature)map.get("q");
			Row row = sheet.getRow(rowIndex);
			for (int j = 4; j < lastColumn; j += mainSampleCapacity) {
				List<Double> values = new ArrayList<Double>();
				for (int k = j; k < (j + mainSampleCapacity); k++) {
					String samValue = ExcelUtil.getCellValue(row.getCell(k))==null?null:ExcelUtil.getCellValue(row.getCell(k)).toString();
					if(StringUtils.isNotEmpty(samValue)){
						if(!CommonUtil1.isDouble(samValue.toString())){
							throw new AmbFrameException("导入的excel模板中有无法获取行【"+(rowIndex+1)+"】列【"+(k+1)+"】的值，请填写正确的格式或填写完整！");
						}else{
							values.add(Double.parseDouble(samValue.toString()));
						}
					}
				}
				if(values.size() != mainSampleCapacity){
					continue;
				}
				Date createdTime = null;
				try {
					Row created_time_row = sheet.getRow(0);
					String created_time = ExcelUtil.getCellValue(created_time_row.getCell(j))==null?null:ExcelUtil.getCellValue(created_time_row.getCell(j)).toString();
					if(created_time == null){
						throw new AmbFrameException("导入的excel模板中有存在【"+(rowIndex+1)+"】列【"+(j+1)+"】【日期】无法正确读取！");
					}
					String date [] = created_time.split("\\.");
					if(date.length != 2 || !CommonUtil1.isInteger(date[0]) || !CommonUtil1.isInteger(date[1])){
						throw new AmbFrameException("导入的excel模板中有存在【"+(rowIndex+1)+"】列【"+(j+1)+"】【日期】无法正确读取！");
					}
					Calendar calendar = Calendar.getInstance();
					String dateStr = calendar.get(Calendar.YEAR)+"-"+Integer.parseInt(date[0])+"-"+Integer.parseInt(date[1])+" 0:0:0";
					createdTime = DateUtil.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
				} catch (Exception e) {
					throw new AmbFrameException("导入的excel模板中有存在【"+(rowIndex+1)+"】列【"+(j+1)+"】【日期】无法正确读取！");
				}
				saveSpcSubGroup(values, null, qualityFeature, createdTime);
			}
		}
		return options;
	}
	
	/**
	 * 涂装SPC模板导入
	 * @param file
	 * @param featureId
	 * @return
	 */
	@Transactional
	public List<Option> importExcelSpcDatas_4(File file) throws Exception{
		InputStream fis = new FileInputStream(file);
		Workbook wb = WorkbookFactory.create(fis); // 从文件流中获取Excel工作区对象（WorkBook）
		if(wb instanceof HSSFWorkbook){
			wb= (HSSFWorkbook)wb;
		}else if(wb instanceof XSSFWorkbook){
			wb= (XSSFWorkbook)wb;
		}
		Sheet sheet = wb.getSheet("SPC导入表");
		if(sheet == null){
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		Map<String,String> modelMap = new HashMap<String, String>();
		modelMap.put("0:1:0:0","项目名称");
		modelMap.put("0:1:1:1","测量点");
		modelMap.put("0:1:2:2","控制点序号");
		//测量点,SPC点,理论值,公差,车架号
		int sheetMergeCount = sheet.getNumMergedRegions(); 
		if(sheetMergeCount<1){
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		Set<String> excelSet = new HashSet<String>();
		Integer mainSampleCapacity = null;
		Set<String> qSet = new HashSet<String>();
		for(int j = 0 ; j < sheetMergeCount ; j++ ){  
	        CellRangeAddress ca = sheet.getMergedRegion(j);
	        excelSet.add(ca.getFirstRow()+":"+ca.getLastRow()+":"+ca.getFirstColumn()+":"+ca.getLastColumn());
	        if(ca.getFirstRow() == 0 && ca.getFirstColumn()>= 4){//车架号
	        	if(mainSampleCapacity == null){
	        		mainSampleCapacity = ca.getLastColumn() - ca.getFirstColumn();
	        	}
	        	if(ca.getFirstRow() != ca.getLastRow() || (mainSampleCapacity !=null && (ca.getLastColumn() - ca.getFirstColumn())!= mainSampleCapacity)){
	        		throw new AmbFrameException("导入的excel模板格式不正确！");
	        	}
	        }
	        if(ca.getFirstRow() >=2 && ca.getFirstColumn() == 0 && ca.getLastColumn() == 0){
	        	qSet.add(ca.getFirstRow()+":"+ca.getLastRow()+":"+ca.getFirstColumn()+":"+ca.getLastColumn());
	        }
	    }
		Row row_0 = sheet.getRow(0);
		int lastColumn = row_0.getLastCellNum();
		if(mainSampleCapacity == null){
			mainSampleCapacity = 1;
		}else{
			for (int i = 4; i < lastColumn; i++) {
				String key = "0:0:"+i+":"+(i+mainSampleCapacity);
				if(!excelSet.contains(key)){
					throw new AmbFrameException("导入的excel模板格式不正确！");
				}
				i+=mainSampleCapacity;
			}
			mainSampleCapacity++;
		}
		for(String key : modelMap.keySet()){
			if(!excelSet.contains(key)){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
			String str[] = key.split(":");
			Row row = sheet.getRow(Integer.parseInt(str[0]));
			if(!modelMap.get(key).equals(ExcelUtil.getCellValue(row.getCell(Integer.parseInt(str[2]))))){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
		}
		try {
			if(!"序号".equals(row_0.getCell(3).getStringCellValue())){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
			if(!"车身号".equals(sheet.getRow(1).getCell(3).getStringCellValue())){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
		} catch (Exception e) {
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		for (int i = 2; i <= sheet.getLastRowNum(); i++) {
			String key = i+":"+i+":2:3";
			if(!excelSet.contains(key)){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
		}
		List<Option> options = new ArrayList<Option>();
		List<Map<String,Object>> qlist = new ArrayList<Map<String,Object>>();
		for (int i = 2; i <= sheet.getLastRowNum(); i++) {
			int rowspan = i;
			for (String key : qSet) {//判断合并列
				String str[] = key.split(":");
				if((i+"").equals(str[0])){
					rowspan = Integer.parseInt(str[1]);
				}
			}
			String code = ExcelUtil.getCellValue(sheet.getRow(i).getCell(0))==null?null:ExcelUtil.getCellValue(sheet.getRow(i).getCell(0)).toString();
			if(StringUtils.isNotEmpty(code)){
				for (int j = i; j <= rowspan; j++) {
					Row row = sheet.getRow(j);
					String str_1 = ExcelUtil.getCellValue(row.getCell(1))==null?null:ExcelUtil.getCellValue(row.getCell(1)).toString();
					String str_2 = ExcelUtil.getCellValue(row.getCell(2))==null?null:ExcelUtil.getCellValue(row.getCell(2)).toString();
					if(StringUtils.isNotEmpty(str_1)&&StringUtils.isNotEmpty(str_2)){
						QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureByCode((code+str_1+str_2));
			        	if(qualityFeature == null){
			        		throw new AmbFrameException("导入的excel模板格式中，找不到简码【"+(code+str_1+str_2)+"】的质量特性");
			        	}
			        	if(!mainSampleCapacity.equals(qualityFeature.getSampleCapacity())){
			        		throw new AmbFrameException("导入的excel模板格式中，简码【"+(code+str_1+str_2)+"】的质量特性中的样本容量不匹配！");
			        	}
			        	Map<String,Object> map = new HashMap<String, Object>();
			        	map.put("row", j);
			        	map.put("q", qualityFeature);
			        	qlist.add(map);
			        	Option option = new Option();
			        	option.setName(qualityFeature.getName());
			        	option.setValue(qualityFeature.getId().toString());
			        	options.add(option);
					}
				}
			}
			i = rowspan;
		}
		for (int i = 0; i < qlist.size(); i++) {
			Map<String,Object> map = qlist.get(i);
			Integer rowIndex = Integer.parseInt(map.get("row").toString());
			QualityFeature qualityFeature = (QualityFeature)map.get("q");
			Row row = sheet.getRow(rowIndex);
			for (int j = 4; j < lastColumn; j += mainSampleCapacity) {
				List<Double> values = new ArrayList<Double>();
				for (int k = j; k < (j + mainSampleCapacity); k++) {
					String samValue = ExcelUtil.getCellValue(row.getCell(k))==null?null:ExcelUtil.getCellValue(row.getCell(k)).toString();
					if(StringUtils.isNotEmpty(samValue)){
						if(!CommonUtil1.isDouble(samValue.toString())){
							throw new AmbFrameException("导入的excel模板中有无法获取行【"+(rowIndex+1)+"】列【"+(k+1)+"】的值，请填写正确的格式或填写完整！");
						}else{
							values.add(Double.parseDouble(samValue.toString()));
						}
					}
				}
				if(values.size() != mainSampleCapacity){
					continue;
				}
				Date createdTime = null;
				try {
					Row created_time_row = sheet.getRow(0);
					createdTime = ExcelUtil.getCellValue(created_time_row.getCell(j))==null?null:created_time_row.getCell(j).getDateCellValue();
					if(createdTime == null){
						throw new AmbFrameException("导入的excel模板中有存行【3】列【"+(j+1)+"】无法正确读取日期！");
					}
				} catch (Exception e) {
					throw new AmbFrameException("导入的excel模板中有存行【3】列【"+(j+1)+"】无法正确读取日期！");
				}
				saveSpcSubGroup(values, null, qualityFeature, createdTime);
			}
		}
		return options;
	}
	
	/**
	 * 总装SPC模板导入
	 * @param file
	 * @param featureId
	 * @return
	 */
	@Transactional
	public List<Option> importExcelSpcDatas_5(File file) throws Exception{
		InputStream fis = new FileInputStream(file);
		Workbook wb = WorkbookFactory.create(fis); // 从文件流中获取Excel工作区对象（WorkBook）
		if(wb instanceof HSSFWorkbook){
			wb= (HSSFWorkbook)wb;
		}else if(wb instanceof XSSFWorkbook){
			wb= (XSSFWorkbook)wb;
		}
		Sheet sheet = wb.getSheet("SPC导入表");
		if(sheet == null){
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		Map<String,String> modelMap = new HashMap<String, String>();
		modelMap.put("0:1:0:0","项目名称");
		modelMap.put("0:1:1:1","测量点序号");
		modelMap.put("0:1:2:2","班组");
		//测量点,SPC点,理论值,公差,车架号
		int sheetMergeCount = sheet.getNumMergedRegions(); 
		if(sheetMergeCount<1){
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		Set<String> excelSet = new HashSet<String>();
		Integer mainSampleCapacity = null;
		Set<String> qSet = new HashSet<String>();//项目名称，合并列
		Set<String> qSet_1 = new HashSet<String>();//班组合并
		for(int j = 0 ; j < sheetMergeCount ; j++ ){  
	        CellRangeAddress ca = sheet.getMergedRegion(j);
	        excelSet.add(ca.getFirstRow()+":"+ca.getLastRow()+":"+ca.getFirstColumn()+":"+ca.getLastColumn());
	        if(ca.getFirstRow() == 0 && ca.getFirstColumn()>= 4){//车架号
	        	if(mainSampleCapacity == null){
	        		mainSampleCapacity = ca.getLastColumn() - ca.getFirstColumn();
	        	}
	        	if(ca.getFirstRow() != ca.getLastRow() || (mainSampleCapacity !=null && (ca.getLastColumn() - ca.getFirstColumn())!= mainSampleCapacity)){
	        		throw new AmbFrameException("导入的excel模板格式不正确！");
	        	}
	        }
	        if(ca.getFirstRow() >=2 && ca.getFirstColumn() == 0 && ca.getLastColumn() == 0){
	        	qSet.add(ca.getFirstRow()+":"+ca.getLastRow()+":"+ca.getFirstColumn()+":"+ca.getLastColumn());
	        }
	        if(ca.getFirstRow() >=2 && ca.getFirstColumn() == 2 && ca.getLastColumn() == 2){
	        	qSet_1.add(ca.getFirstRow()+":"+ca.getLastRow()+":"+ca.getFirstColumn()+":"+ca.getLastColumn());
	        }
	    }
		Row row_0 = sheet.getRow(0);
		int lastColumn = row_0.getLastCellNum();
		if(mainSampleCapacity == null){
			mainSampleCapacity = 1;
		}else{
			for (int i = 4; i < lastColumn; i++) {
				String key = "0:0:"+i+":"+(i+mainSampleCapacity);
				if(!excelSet.contains(key)){
					throw new AmbFrameException("导入的excel模板格式不正确！");
				}
				i+=mainSampleCapacity;
			}
			mainSampleCapacity++;
		}
		for(String key : modelMap.keySet()){
			if(!excelSet.contains(key)){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
			String str[] = key.split(":");
			Row row = sheet.getRow(Integer.parseInt(str[0]));
			if(!modelMap.get(key).equals(ExcelUtil.getCellValue(row.getCell(Integer.parseInt(str[2]))))){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
		}
		try {
			if(!"日期".equals(row_0.getCell(3).getStringCellValue())){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
			if(!"力矩要求".equals(sheet.getRow(1).getCell(3).getStringCellValue())){
				throw new AmbFrameException("导入的excel模板格式不正确！");
			}
		} catch (Exception e) {
			throw new AmbFrameException("导入的excel模板格式不正确！");
		}
		List<Option> options = new ArrayList<Option>();
		List<Map<String,Object>> qlist = new ArrayList<Map<String,Object>>();
		for (int i = 2; i <= sheet.getLastRowNum(); i++) {
			int rowspan = i;
			for (String key : qSet) {//判断合并列
				String str[] = key.split(":");
				if((i+"").equals(str[0])){
					rowspan = Integer.parseInt(str[1]);
				}
			}
			String code = ExcelUtil.getCellValue(sheet.getRow(i).getCell(0))==null?null:ExcelUtil.getCellValue(sheet.getRow(i).getCell(0)).toString();
			if(StringUtils.isNotEmpty(code)){
				for (int j = i; j <= rowspan; j++) {
					int rowspan_1 = j;
					for (String key : qSet_1) {//判断合并列
						String str[] = key.split(":");
						if((j+"").equals(str[0])){
							rowspan_1 = Integer.parseInt(str[1]);
						}
					}
					for (int k = j; k <= rowspan_1; k++) {
						Row row = sheet.getRow(k);
						String str_1 = ExcelUtil.getCellValue(row.getCell(1))==null?null:ExcelUtil.getCellValue(row.getCell(1)).toString();
						String str_2 = ExcelUtil.getCellValue(sheet.getRow(j).getCell(2))==null?null:ExcelUtil.getCellValue(sheet.getRow(j).getCell(2)).toString();
						if(StringUtils.isNotEmpty(str_1)&&StringUtils.isNotEmpty(str_2)){
							QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureByCode((code+str_1+str_2));
				        	if(qualityFeature == null){
				        		throw new AmbFrameException("导入的excel模板格式中，找不到简码【"+(code+str_1+str_2)+"】的质量特性");
				        	}
				        	if(!mainSampleCapacity.equals(qualityFeature.getSampleCapacity())){
				        		throw new AmbFrameException("导入的excel模板格式中，简码【"+(code+str_1+str_2)+"】的质量特性中的样本容量不匹配！");
				        	}
				        	Map<String,Object> map = new HashMap<String, Object>();
				        	map.put("row", k);
				        	map.put("q", qualityFeature);
				        	qlist.add(map);
				        	Option option = new Option();
				        	option.setName(qualityFeature.getName());
				        	option.setValue(qualityFeature.getId().toString());
				        	options.add(option);
						}
					}
					j = rowspan_1;
				}
			}
			i = rowspan;
		}
		for (int i = 0; i < qlist.size(); i++) {
			Map<String,Object> map = qlist.get(i);
			Integer rowIndex = Integer.parseInt(map.get("row").toString());
			QualityFeature qualityFeature = (QualityFeature)map.get("q");
			Row row = sheet.getRow(rowIndex);
			for (int j = 4; j < lastColumn; j += mainSampleCapacity) {
				List<Double> values = new ArrayList<Double>();
				for (int k = j; k < (j + mainSampleCapacity); k++) {
					String samValue = ExcelUtil.getCellValue(row.getCell(k))==null?null:ExcelUtil.getCellValue(row.getCell(k)).toString();
					if(StringUtils.isNotEmpty(samValue)){
						if(!CommonUtil1.isDouble(samValue.toString())){
							throw new AmbFrameException("导入的excel模板中有无法获取行【"+(rowIndex+1)+"】列【"+(k+1)+"】的值，请填写正确的格式或填写完整！");
						}else{
							values.add(Double.parseDouble(samValue.toString()));
						}
					}
				}
				if(values.size() != mainSampleCapacity){
					continue;
				}
				Date createdTime = null;
				try {
					Row created_time_row = sheet.getRow(0);
					String created_time = ExcelUtil.getCellValue(created_time_row.getCell(j))==null?null:ExcelUtil.getCellValue(created_time_row.getCell(j)).toString();
					if(created_time == null){
						throw new AmbFrameException("导入的excel模板中有存在【"+(rowIndex+1)+"】列【"+(j+1)+"】【日期】无法正确读取！");
					}
					String date [] = created_time.split("\\.");
					if(date.length != 2 || !CommonUtil1.isInteger(date[0]) || !CommonUtil1.isInteger(date[1])){
						throw new AmbFrameException("导入的excel模板中有存在【"+(rowIndex+1)+"】列【"+(j+1)+"】【日期】无法正确读取！");
					}
					Calendar calendar = Calendar.getInstance();
					String dateStr = calendar.get(Calendar.YEAR)+"-"+Integer.parseInt(date[0])+"-"+Integer.parseInt(date[1])+" 0:0:0";
					createdTime = DateUtil.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
				} catch (Exception e) {
					throw new AmbFrameException("导入的excel模板中有存在【"+(rowIndex+1)+"】列【"+(j+1)+"】【日期】无法正确读取！");
				}
				saveSpcSubGroup(values, null, qualityFeature, createdTime);
			}
		}
		return options;
	}
	/**
	  * 方法名:查询列表数据
	  * <p>功能说明：</p>
	  * @return
	 */
	public JSONObject searchSpcGroups(Page<SpcSubGroup> page,JSONObject params,Long featureId){
		QualityFeature qualityFeature = qualityFeatureManager.getQualityFeature(featureId);
		params = convertJsonObject(params);
		Date startDate = null;
		Date endDate = null;
		if(params.containsKey("startDate_ge_date") && params.containsKey("endDate_le_date")){
			startDate = DateUtil.parseDate(params.getString("startDate_ge_date"));
			endDate = DateUtil.parseDate(params.getString("endDate_le_date") + " 23:59:59","yyyy-MM-dd HH:mm:ss");
		}
		Integer precs = null;
		if(CommonUtil1.isInteger(qualityFeature.getPrecs())){
			precs = Integer.valueOf(qualityFeature.getPrecs());
		}
		JSONObject pageJson = new JSONObject();
		if(StringUtils.isEmpty(qualityFeature.getTargetTableName())){//兼容历史数据查询,统计报表不需要兼容
			StringBuffer sb = new StringBuffer("from SpcSubGroup s where s.companyId = ? and s.qualityFeature = ?");
			//查询条件
			List<Object> searchParams = new ArrayList<Object>();
			searchParams.add(ContextUtils.getCompanyId());
			searchParams.add(qualityFeature);
			if(startDate!=null){
				searchParams.add(startDate);
				searchParams.add(endDate);
				sb.append(" and s.createdTime between ? and ?");
			}
			page = spcSubGroupDao.findPage(page, sb.toString(), searchParams.toArray());
			
			pageJson.put("page",page.getPageNo());
			pageJson.put("total", page.getTotalPages());
			pageJson.put("records", page.getTotalCount());
			JSONArray rows = new JSONArray();
			for(SpcSubGroup subGroup : page.getResult()){
				JSONObject row = new JSONObject();
				row.put("id",subGroup.getId());
				row.put("qualityFeature.name",qualityFeature.getName());
				row.put("createdTime", DateUtil.formateDateStr(subGroup.getCreatedTime()));
				row.put("subGroupSize", qualityFeature.getSampleCapacity());
				row.put("sigma",SpcDataManager.formatValue(subGroup.getSigma(), precs));
				row.put("maxValue", subGroup.getMaxValue());
				row.put("minValue",subGroup.getMinValue());
				row.put("rangeDiff",SpcDataManager.formatValue(subGroup.getRangeDiff(), precs));
				rows.add(row);
			}
			pageJson.put("rows",rows);
		}else{
			StringBuffer sb = new StringBuffer("from " + qualityFeature.getTargetTableName() + " s where 1=1");
			//查询条件
			List<Object> searchParams = new ArrayList<Object>();
			if(startDate!=null){
				searchParams.add(startDate);
				searchParams.add(endDate);
				sb.append(" and s.INSPECTION_DATE between ? and ?");
			}
			Query query = spcSubGroupDao.getSession().createSQLQuery("select count(*) " + sb.toString());
			for(int i=0;i<searchParams.size();i++){
				query.setParameter(i,searchParams.get(i));
			}
			Long total = Long.valueOf(query.list().get(0).toString());
			Integer sampleCapacity = qualityFeature.getSampleCapacity();
			Long totalGroups = total/sampleCapacity;
			if(total%sampleCapacity>0){
				totalGroups++;
			}
			page.setTotalCount(totalGroups);
			
			if(StringUtils.isNotEmpty(page.getOrderBy())){
				sb.append(" order by INSPECTION_DATE " + page.getOrder());
			}else{
				sb.append(" order by INSPECTION_DATE ");
			}
			query = spcSubGroupDao.getSession().createSQLQuery("select id,DATA_VALUE,INSPECTION_DATE " + sb.toString());
			for(int i=0;i<searchParams.size();i++){
				query.setParameter(i,searchParams.get(i));
			}
			query.setFirstResult(page.getFirst()*sampleCapacity-1);//加上子组数
			query.setMaxResults(page.getPageSize()*sampleCapacity);
			List<?> list = query.list();
			JSONArray rows = new JSONArray();
			for(int i=0;i<list.size();i+=sampleCapacity){
				Double min=null,max=null,allValue=0.0;
				Date minDate = null;
				String ids= "";
				Integer num = 0;
				for(int j=0;j<sampleCapacity&&(j+i)<list.size();j++){
					Object[] objs = (Object[])list.get(j+i);
					String id = objs[0].toString();
					if(ids.length()>0){
						ids += ",";
					}
					ids += id;
					
					Double value = objs[1]==null?null:Double.valueOf(objs[1].toString());
					if(value != null){
						if(min==null){
							min = value;
						}else if(value < min){
							min = value;
						}
						if(max==null){
							max = value;
						}else if(value > max){
							max = value;
						}
						
						allValue += value;
					}
					
					Date inspectionDate = (Date)objs[2];
					if(inspectionDate != null){
						if(minDate==null||inspectionDate.getTime() < minDate.getTime()){
							minDate = inspectionDate;
						}
					}
					num++;
				}
				JSONObject row = new JSONObject();
				row.put("id",i);
				row.put("dataIds",ids);
				row.put("qualityFeature.name",qualityFeature.getName());
				row.put("createdTime",DateUtil.formateDateStr(minDate));
				row.put("subGroupSize", qualityFeature.getSampleCapacity());
				row.put("sigma",SpcDataManager.formatValue(allValue/num,precs));
				row.put("maxValue",max);
				row.put("minValue",min);
				row.put("rangeDiff",SpcDataManager.formatValue(max-min,precs));
				rows.add(row);
			}
			pageJson.put("rows",rows);
			pageJson.put("page",page.getPageNo());
			pageJson.put("total", page.getTotalPages());
			pageJson.put("records", page.getTotalCount());
		}
		return pageJson;
	}
	
	/**
	  * 方法名:根据数据ID查询数据
	  * <p>功能说明：</p>
	  * @return
	 */
	public List<Map<String,Object>> queryDataValues(QualityFeature feature,String dataIds){
		List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
		if(StringUtils.isNotEmpty(feature.getTargetTableName())){
			List<String> fieldNames = new ArrayList<String>();
			fieldNames.add("ID");
			fieldNames.add("DATA_VALUE");
			fieldNames.add("INSPECTION_DATE");
			String sql = "select ID,DATA_VALUE,INSPECTION_DATE";
			for(FeatureLayer layer : feature.getFeatureLayers()){
				sql += "," + layer.getDetailCode();
				fieldNames.add(layer.getDetailCode().toUpperCase());
			}
			sql += " from " + feature.getTargetTableName() + " where id in ('" + dataIds.replace(",","','") + "') order by inspection_date";
			Query query = spcSubGroupDao.getSession().createSQLQuery(sql);
			List<?> list = query.list();
			for(Object obj : list){
				Object[] objs = (Object[])obj;
				Map<String,Object> objMap = new HashMap<String, Object>();
				for(int i=0;i<fieldNames.size();i++){
					String fieldName = fieldNames.get(i);
					if(fieldName==null){
						fieldName = "";
					}
					fieldName = fieldName.toUpperCase();
					Object value = objs[i];
					if(value != null){
						if(value instanceof Date){
							objMap.put(fieldName,DateUtil.formateDateStr((Date)value,"yyyy-MM-dd HH:mm"));
						}else{
							objMap.put(fieldName,value);
						}
					}
				}
				lists.add(objMap);
			}
		}
		return lists;
	}
}
