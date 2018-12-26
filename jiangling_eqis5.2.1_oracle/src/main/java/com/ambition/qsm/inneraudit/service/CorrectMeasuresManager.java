package com.ambition.qsm.inneraudit.service;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.OrtTestEntrust;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.qsm.entity.CorrectMeasures;
import com.ambition.qsm.inneraudit.dao.CorrectMeasuresDao;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 类名:不符合与纠正措施报告Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年10月27日 发布
 */
@Service
@Transactional
public class CorrectMeasuresManager extends AmbWorkflowManagerBase<CorrectMeasures>{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private CorrectMeasuresDao yearAuditDao;
	@Override
	public Class<CorrectMeasures> getEntityInstanceClass() {
		return CorrectMeasures.class;
	}

	@Override
	public String getEntityListCode() {
		return CorrectMeasures.ENTITY_LIST_CODE;
	}

	@Override
	public HibernateDao<CorrectMeasures, Long> getHibernateDao() {
		return yearAuditDao;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "correct-measures";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "不符合与纠正措施报告";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "correct-measures.xlsx", CorrectMeasures.ENTITY_LIST_NAME);
	}
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	@Override
	public void deleteEntity(CorrectMeasures entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from QSM_CORRECT_MEASURES_ITEM where QSM_CORRECT_MEASURES_ID = ?";	
			String sql21 = "delete from QSM_SIGN_REASON_ITEM where QSM_CORRECT_MEASURES_ID = ?";
			getHibernateDao().getSession().createSQLQuery(sql31)
			.setParameter(0,reportId)
			.executeUpdate();
			getHibernateDao().getSession().createSQLQuery(sql21)
			.setParameter(0,reportId)
			.executeUpdate();
			String sql41 = "delete from QSM_SIGN_MEASURE_ITEM where QSM_CORRECT_MEASURES_ID = ?";
			getHibernateDao().getSession().createSQLQuery(sql41)
			.setParameter(0,reportId)
			.executeUpdate();
			String sql1 = "delete from QSM_SIGN_COMPLETE_ITEM where QSM_CORRECT_MEASURES_ID = ?";
			getHibernateDao().getSession().createSQLQuery(sql1)
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
	/**
	 * 查询所有部门
	 * @return
	 */
	public List<Department> queryAllDepartments(){
		String hql = "from Department d where d.deleted = 0 order by d.parent desc,weight";
		List<Department> list = yearAuditDao.createQuery(hql).list();
		return list;
	}
	
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<User> queryAllUsers(){
		String hql = "from User d where d.deleted = 0 order by d.weight";
		List<User> list = yearAuditDao.createQuery(hql).list();
		return list;
	}
}
