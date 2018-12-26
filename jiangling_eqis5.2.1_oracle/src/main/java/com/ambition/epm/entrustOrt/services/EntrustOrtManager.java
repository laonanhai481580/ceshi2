package com.ambition.epm.entrustOrt.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.epm.entity.EntrustOrt;
import com.ambition.epm.entity.EntrustOrtSublist;
import com.ambition.epm.entrustOrt.dao.EntrustOrtDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
@Service
@Transactional
public class EntrustOrtManager extends AmbWorkflowManagerBase<EntrustOrt>{
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private EntrustOrtDao entrustOrtDao;
	@Override
	public HibernateDao<EntrustOrt, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return entrustOrtDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return EntrustOrt.ENTITY_LIST_CODE;
	}

	@Override
	public Class<EntrustOrt> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return EntrustOrt.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "epm_entrustOrt";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "ORT试验委托";
	}
	 /**
     * 方法名: 导出报告
     * <p>功能说明：</p>
     * @return
    */
   public void exportReport(Long entityId) throws IOException {
       exportReport(entityId,"epm_entrust-ort.xls","ort试验委托单");
   }
	/**
	  * 方法名: 保存对象
	  * <p>功能说明：</p>
	  * @param report
	  * @param childMaps 子表对象
	  * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 */
	public void saveEntity(EntrustOrt report,Map<String,List<JSONObject>> childMaps){
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		//数据处理
		if(report.getPurpose()!=null){
			report.setPurpose(report.getPurpose().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getSampleDiscription()!=null){
			report.setSampleDiscription(report.getSampleDiscription().replaceAll("", "").replaceAll("", ""));
		}
		getHibernateDao().save(report);
		//设置子表的值
		setChildItems(report,childMaps);
		List<EntrustOrtSublist> entrustOrtSublists=report.getEntrustOrtSublists();
		for (EntrustOrtSublist entrustOrtSublist : entrustOrtSublists) {
			entrustOrtSublist.getId();
			if(entrustOrtSublist.getTestCondition()!=null){
				entrustOrtSublist.setTestCondition(entrustOrtSublist.getTestCondition().replaceAll("", "").replaceAll("", ""));
			}
			if(entrustOrtSublist.getCriterionG()!=null){
				entrustOrtSublist.setCriterionG(entrustOrtSublist.getCriterionG().replaceAll("", "").replaceAll("", ""));
			}
			String ortId=entrustOrtSublist.getOrtId();
			if(ortId==null||"".equals(ortId)){
				entrustOrtSublist.setOrtId(entrustOrtSublist.getId().toString());
			}
		}
	}
	/**
	 * 删除实体，流程相关文件都删除
	 * @param ids
	 */
	public String deleteEntity(String ids) {
		String[] deleteIds = ids.split(",");
		StringBuilder sb = new StringBuilder("");
	     String message = "";
		for (String id : deleteIds) {
			EntrustOrt report=getEntity(Long.valueOf(id));
			deleteEntity(report);
			sb.append(report.getFormNo() + ",");
		}
		message =sb.toString();
		return   message;
	}
	public void deleteEntity(EntrustOrt entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from EPM_ENTRUST_ORT_SUBLIST where EPM_SUBLIST_ID = ?";	
			getHibernateDao().getSession().createSQLQuery(sql31).setParameter(0,reportId).executeUpdate();
			String sql = "delete from product_task_all_his where execution_id = ?";
			getHibernateDao().getSession().createSQLQuery(sql).setParameter(0,workflowId).executeUpdate();
			getHibernateDao().delete(entity);
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			getHibernateDao().delete(entity);
		}
	}
	public void isfromNo(EntrustOrt entity){
		if(entity.getFormNo()!=null){
			String sql = "select form_No from EPM_ENTRUST_ORT where form_No = ?";
			List<Object> a = entrustOrtDao.findBySql(sql,entity.getFormNo());
			if(a.size()>2){
				entity.setFormNo(formCodeGenerated.generateEntrustOrtNo());
			}
		}
	}
	public Page<EntrustOrt> listState(Page<EntrustOrt> page,String state,String str){
		String hql = " from EntrustOrt e where e.hiddenState=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(state);
		if(str!=null ){
			hql=hql+" and e.factoryClassify=?";
			searchParams.add(str);
		}
		return entrustOrtDao.searchPageByHql(page, hql, searchParams.toArray());
	}
	public void epmHide(String hideId,String type){
		String[] ids = hideId.split(",");
		for(String id : ids){
			EntrustOrt entrustOrt = entrustOrtDao.get(Long.valueOf(id));
			if("Y".equals(type)){
				List<EntrustOrtSublist> list=entrustOrt.getEntrustOrtSublists();
				for(int i=0;list.size()>i;i++){
					EntrustOrtSublist entrustOrtSublist=list.get(i);
					entrustOrtSublist.setHiddenState("N");
					entrustOrtDao.getSession().save(entrustOrtSublist);
				}
				entrustOrt.setHiddenState("N");
			}else{
				List<EntrustOrtSublist> list=entrustOrt.getEntrustOrtSublists();
				for(int i=0;list.size()>i;i++){
					EntrustOrtSublist entrustOrtSublist=list.get(i);
					entrustOrtSublist.setHiddenState("Y");
					entrustOrtDao.getSession().save(entrustOrtSublist);
				}
				entrustOrt.setHiddenState("Y");
			}
			entrustOrtDao.save(entrustOrt);
		}
	}
}
