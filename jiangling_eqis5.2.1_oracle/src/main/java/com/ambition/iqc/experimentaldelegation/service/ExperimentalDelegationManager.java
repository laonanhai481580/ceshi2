package com.ambition.iqc.experimentaldelegation.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.ExperimentalDelegation;
import com.ambition.iqc.entity.OrtExperimentalItem;
import com.ambition.iqc.experimentaldelegation.dao.ExperimentalDelegationDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
@Service
@Transactional
public class ExperimentalDelegationManager extends AmbWorkflowManagerBase<ExperimentalDelegation>{
	
	@Autowired
	private ExperimentalDelegationDao experimentalDelegationDao;
	private static final String code="iqc-experment-del";
	
	@Override
	public HibernateDao<ExperimentalDelegation, Long> getHibernateDao() {
		return experimentalDelegationDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return "IQC_EXPERIMENTAL_DELEGATION";
	}

	@Override
	public Class<ExperimentalDelegation> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return ExperimentalDelegation.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return code;
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "iqc-experment-del.xls", "实验委托单");
	}
	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "IQC实验委托";
	}
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	@Override
	public void deleteEntity(ExperimentalDelegation entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from IQC_ORT_EXPERIMENTAL_ITEM where FK_EXP_DELEGATION = ?";	
			
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

	public void saveChild(ExperimentalDelegation report,
			String childParams) {
		// TODO Auto-generated method stub
		 JSONArray itemStrArray=null;
         if(!childParams.isEmpty()){
             itemStrArray=JSONArray.fromObject(childParams);
			if (!itemStrArray.isEmpty()) {
				if (report.getOrtItems() == null) {
					report.setOrtItems(new ArrayList<OrtExperimentalItem>());
				} else {
					report.getOrtItems().clear();
				}
				for (int i = 0; i < itemStrArray.size(); i++) {
					JSONObject jso = itemStrArray.getJSONObject(i);
					OrtExperimentalItem ortItem = new OrtExperimentalItem();
					ortItem.setCompanyId(ContextUtils.getCompanyId());
					ortItem.setCreatedTime(new Date());
					ortItem.setCreatorName(ContextUtils.getUserName());
					ortItem.setCreator(ContextUtils.getLoginName());
					for (Object key : jso.keySet()) {
						String value = jso.getString(key.toString());
						try {
							setProperty(ortItem, key.toString(), value);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}

					ortItem.setExperimentalDelegation(report);
					report.getOrtItems().add(ortItem);
				}
			}
         }
         experimentalDelegationDao.save(report);
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
