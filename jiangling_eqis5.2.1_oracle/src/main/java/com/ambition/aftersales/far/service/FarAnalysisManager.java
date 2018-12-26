package com.ambition.aftersales.far.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.entity.FarAnalysis;
import com.ambition.aftersales.entity.FarAnalysisItem;
import com.ambition.aftersales.far.dao.FarAnalysisDao;
import com.ambition.iqc.entity.OrtExperimentalItem;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 类名:FAR解析Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月24日 发布
 */
@Service
@Transactional
public class FarAnalysisManager extends AmbWorkflowManagerBase<FarAnalysis>{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private FarAnalysisDao farAnalysisDao;
	@Override
	public Class<FarAnalysis> getEntityInstanceClass() {
		return FarAnalysis.class;
	}

	@Override
	public String getEntityListCode() {
		return FarAnalysis.ENTITY_LIST_CODE;
	}

	@Override
	public HibernateDao<FarAnalysis, Long> getHibernateDao() {
		return farAnalysisDao;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "far-analysis";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "FAR解析流程";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "far-analysis.xlsx", FarAnalysis.ENTITY_LIST_NAME);
	}
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	@Override
	public void deleteEntity(FarAnalysis entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from AFS_FAR_ANALYSIS_ITEMS where FK_FAR_ANALYSIS_ID = ?";	
			
			getHibernateDao().getSession().createSQLQuery(sql31)
			.setParameter(0,reportId)
			.executeUpdate();

			String sql = "delete from product_task_all_his where execution_id = ?";
			getHibernateDao().getSession().createSQLQuery(sql)
			.setParameter(0,workflowId)
			.executeUpdate();
			getHibernateDao().delete(entity);
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			getHibernateDao().delete(entity);
		}
	}

	public void saveChild(FarAnalysis report, String childParams) {
		// TODO Auto-generated method stub
		 JSONArray itemStrArray=null;
         if(!childParams.isEmpty()){
             itemStrArray=JSONArray.fromObject(childParams);
			if (!itemStrArray.isEmpty()) {
				if (report.getFarAnalysisItems() == null) {
					report.setFarAnalysisItems(new ArrayList<FarAnalysisItem>());
				} else {
					report.getFarAnalysisItems().clear();
				}
				for (int i = 0; i < itemStrArray.size(); i++) {
					JSONObject jso = itemStrArray.getJSONObject(i);
					FarAnalysisItem item = new FarAnalysisItem();
					item.setCompanyId(ContextUtils.getCompanyId());
					item.setCreatedTime(new Date());
					item.setCreatorName(ContextUtils.getUserName());
					item.setCreator(ContextUtils.getLoginName());
					for (Object key : jso.keySet()) {
						String value = jso.getString(key.toString());
						try {
							setProperty(item, key.toString(), value);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}

					item.setFarAnalysis(report);
					report.getFarAnalysisItems().add(item);
				}
			}
         }
         farAnalysisDao.save(report);
	}
	 /**
     * 方法名: setProperty 
     * <p>功能说明：设置属性</p>
     * @return void
     * @throws   
      */
     private void setProperty(Object obj,String property,Object value) throws Exception{
         Class<?> type = PropertyUtils.getPropertyType(obj,property);
         if(type != null){
             if(value==null||StringUtils.isEmpty(value.toString())){
                 PropertyUtils.setProperty(obj,property,null);
             }else{
                  if(String.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,value.toString());
                 }else if(Integer.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Integer.valueOf(value.toString()));
                 }else if(Double.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Double.valueOf(value.toString()));
                 }else if(Float.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Float.valueOf(value.toString()));
                 }else if(Boolean.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Boolean.valueOf(value.toString()));
                 }else if(Date.class.getName().equals(type.getName())){
                     if(Date.class.getName().equals(value.getClass().getName())){
                         PropertyUtils.setProperty(obj,property,value);
                     }else if(String.class.getName().equals(value.getClass().getName())&&value.toString().length()==10){
                         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                         PropertyUtils.setProperty(obj,property,sdf.parse(value.toString()));
                     }
                 }else{
                     PropertyUtils.setProperty(obj,property,value);
                 }
             }
         }
     }
}
