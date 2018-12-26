package com.ambition.qsm.inneraudit.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.qsm.entity.AuditPlan;
import com.ambition.qsm.entity.YearAudit;
import com.ambition.qsm.inneraudit.dao.AuditPlanDao;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 类名:内审计划与实施Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年10月26日 发布
 */
@Service
@Transactional
public class AuditPlanManager extends AmbWorkflowManagerBase<AuditPlan>{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private AuditPlanDao yearAuditDao;
	@Override
	public Class<AuditPlan> getEntityInstanceClass() {
		return AuditPlan.class;
	}

	@Override
	public String getEntityListCode() {
		return AuditPlan.ENTITY_LIST_CODE;
	}

	@Override
	public HibernateDao<AuditPlan, Long> getHibernateDao() {
		return yearAuditDao;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "audit-plan";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "内审计划与实施流程";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "audit-plan.xlsx", AuditPlan.ENTITY_LIST_NAME);
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
	public void saveEntity(AuditPlan report,Map<String,List<JSONObject>> childMaps){
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		getHibernateDao().save(report);
		//设置子表的值
		setChildItems(report,childMaps);
		
	}	
}
