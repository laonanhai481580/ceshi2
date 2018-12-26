package com.ambition.epm.sample.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.epm.entity.Sample;
import com.ambition.epm.sample.dao.SampleDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.utils.DateUtil;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Service
@Transactional
public class SampleManager extends AmbWorkflowManagerBase<Sample>{
	
	@Autowired
	private  SampleDao sampleDao;
	@Override
	public HibernateDao<Sample, Long> getHibernateDao() {
		return sampleDao;
	}

	@Override
	public String getEntityListCode() {
		return Sample.ENTITY_LIST_CODE;
	}

	@Override
	public Class<Sample> getEntityInstanceClass() {
		return Sample.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "epm_sample";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "样品管理流程表";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "epm-sample.xlsx", Sample.ENTITY_LIST_NAME);
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
	public void saveEntity(Sample report,Map<String,List<JSONObject>> childMaps){
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		Date sendDate = DateUtil.parseDateTime(Struts2Utils.getParameter("sendDate"));
		if(sendDate!=null){
			report.setSendDate(sendDate);
		}		
		Date receivedDate = DateUtil.parseDateTime(Struts2Utils.getParameter("receivedDate"));
		if(receivedDate!=null){
			report.setReceivedDate(receivedDate);
		}
		getHibernateDao().save(report);
		//设置子表的值
		setChildItems(report,childMaps);
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
			Sample report=getEntity(Long.valueOf(id));
			deleteEntity(report);
			sb.append(report.getFormNo() + ",");
		}
		message =sb.toString();
		return   message;
	}
	public void deleteEntity(Sample entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from EPM_SAMPLE_SUBLIST where EPM_SUBLIST_ID = ?";	
			getHibernateDao().getSession().createSQLQuery(sql31).setParameter(0,reportId).executeUpdate();
			String sql = "delete from product_task_all_his where execution_id = ?";
			getHibernateDao().getSession().createSQLQuery(sql).setParameter(0,workflowId).executeUpdate();
			getHibernateDao().delete(entity);
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			getHibernateDao().delete(entity);
		}
	}
	public Page<Sample> listState(Page<Sample> page,String type){
		String hql = " from Sample e where e.reportNo like ? ";
		return sampleDao.searchPageByHql(page, hql, '%'+type+'%');
	}
}
