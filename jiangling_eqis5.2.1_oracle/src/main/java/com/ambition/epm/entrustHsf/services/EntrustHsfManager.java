package com.ambition.epm.entrustHsf.services;

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
import com.ambition.epm.entity.EntrustHsf;
import com.ambition.epm.entity.EntrustHsfSublist;
import com.ambition.epm.entity.EntrustOrt;
import com.ambition.epm.entrustHsf.dao.EntrustHsfDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
@Service
@Transactional
public class EntrustHsfManager extends AmbWorkflowManagerBase<EntrustHsf>{
	
	@Autowired
	private EntrustHsfDao entrustHsfDao;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Override
	public HibernateDao<EntrustHsf, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return entrustHsfDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return EntrustHsf.ENTITY_LIST_CODE;
	}

	@Override
	public Class<EntrustHsf> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return EntrustHsf.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "epm_entrustHsf";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "HSF实验委托流程";
	}
	 /**
     * 方法名: 导出报告
     * <p>功能说明：</p>
     * @return
    */
   public void exportReport(Long entityId) throws IOException {
       exportReport(entityId,"epm_entrust-hsf.xls","hsf实验委托单");
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
	public void saveEntity(EntrustHsf report,Map<String,List<JSONObject>> childMaps){
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		getHibernateDao().save(report);
		//设置子表的值
		setChildItems(report,childMaps);
		List<EntrustHsfSublist> entrustHsfSublists=report.getEntrustHsfSublists();
		for (EntrustHsfSublist entrustHsfSublist : entrustHsfSublists) {
			entrustHsfSublist.getId();
			String hsfId=entrustHsfSublist.getHsfId();
			if(hsfId==null||"".equals(hsfId)){
				entrustHsfSublist.setHsfId(entrustHsfSublist.getId().toString());
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
			EntrustHsf report=getEntity(Long.valueOf(id));
			deleteEntity(report);
			sb.append(report.getFormNo() + ",");
		}
		message =sb.toString();
		return   message;
	}
	public void deleteEntity(EntrustHsf entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from EPM_ENTRUST_HSF_SUBLIST where EPM_SUBLIST_ID = ?";	
			getHibernateDao().getSession().createSQLQuery(sql31).setParameter(0,reportId).executeUpdate();
			String sql = "delete from product_task_all_his where execution_id = ?";
			getHibernateDao().getSession().createSQLQuery(sql).setParameter(0,workflowId).executeUpdate();
			getHibernateDao().delete(entity);
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			getHibernateDao().delete(entity);
		}
	}
	public Page<EntrustHsf> listState(Page<EntrustHsf> page,String state,String str){
		String hql = " from EntrustHsf e where e.hiddenState=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(state);
		if(str!=null ){
			hql=hql+" and e.factoryClassify=?";
			searchParams.add(str);
		}
		return entrustHsfDao.searchPageByHql(page, hql, searchParams.toArray());
	}
	public void epmHide(String hideId,String type){
		String[] ids = hideId.split(",");
		for(String id : ids){
			EntrustHsf entrustHsf = entrustHsfDao.get(Long.valueOf(id));
			if("Y".equals(type)){
				List<EntrustHsfSublist> list=entrustHsf.getEntrustHsfSublists();
				for(int i=0;list.size()>i;i++){
					EntrustHsfSublist entrustHsfSublist=list.get(i);
					entrustHsfSublist.setHiddenState("N");
					entrustHsfDao.getSession().save(entrustHsfSublist);
				}
				entrustHsf.setHiddenState("N");
			}else{
				List<EntrustHsfSublist> list=entrustHsf.getEntrustHsfSublists();
				for(int i=0;list.size()>i;i++){
					EntrustHsfSublist entrustHsfSublist=list.get(i);
					entrustHsfSublist.setHiddenState("N");
					entrustHsfDao.getSession().save(entrustHsfSublist);
				}
				entrustHsf.setHiddenState("Y");
			}
			entrustHsfDao.save(entrustHsf);
		}
	}
	public void isfromNo(EntrustHsf entity){
		if(entity.getFormNo()!=null){
			String sql = "select form_No from EPM_ENTRUST_HSF";
			List<Object> a = entrustHsfDao.findBySql(sql);
			int y=0;
			for(int i=0;i<a.size();i++){
				if(y!=i){
					if(a.get(i).equals(entity.getFormNo())&&a.get(y).equals(a.get(i))){
						entity.setFormNo(formCodeGenerated.generateEntrustHsfNo());
						return;
					}
				}
				if(a.get(i).equals(entity.getFormNo())){
					y=i;
				}
			}
		}
	}
}
