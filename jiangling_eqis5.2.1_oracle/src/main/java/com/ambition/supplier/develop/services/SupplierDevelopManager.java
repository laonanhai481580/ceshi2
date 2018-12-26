package com.ambition.supplier.develop.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.develop.dao.SupplierDevelopDao;
import com.ambition.supplier.entity.Evaluate;
import com.ambition.supplier.entity.SupplierDevelop;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;

@Service
@Transactional
public class SupplierDevelopManager extends AmbWorkflowManagerBase<SupplierDevelop>{

	@Autowired
	private SupplierDevelopDao supplierDevelopDao;
	@Override
	public HibernateDao<SupplierDevelop, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return supplierDevelopDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return "SUPPLIER_DEVELOP";
	}

	@Override
	public Class<SupplierDevelop> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return SupplierDevelop.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "supplier-evaluate";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "供应商评价流程";
	}
	 /**
     * 方法名: 导出报告
     * <p>功能说明：</p>
     * @return
    */
   public void exportReport(Long entityId) throws IOException {
       exportReport(entityId,"supplier-evaluate.xlsx","供应商评价表");
   }
   /**
    * 删除实体，流程相关文件都删除
    * @param entity 删除的对象
    */
   @Override
   public void deleteEntity(SupplierDevelop entity){
       if(entity.getWorkflowInfo()!=null){
           String workflowId =  entity.getWorkflowInfo().getWorkflowId();
           //先删除子表
           
           Long reportId = entity.getId();
           ApiFactory.getInstanceService().deleteInstance(entity);
           //删除关联的办理期限
           String sql = "delete from product_task_all_his where execution_id = ?";
           getHibernateDao().getSession().createSQLQuery(sql)
           .setParameter(0,workflowId)
           .executeUpdate();
           //删除对象
           //getHibernateDao().delete(entity);
       }else{
           getHibernateDao().delete(entity);
       }
   }
	public CompleteTaskTipType submitProcessSelf(SupplierDevelop evaluate) {
		// TODO Auto-generated method stub
		saveEntity(evaluate);
       Long processId = ApiFactory.getDefinitionService()
               .getWorkflowDefinitionsByCode(getWorkflowDefinitionCode()).get(0).getId();
       return ApiFactory.getInstanceService().submitInstance(processId, evaluate);
	}

	public SupplierDevelop getSupplierDevelopByName(String name) {
		// TODO Auto-generated method stub
		String hql = " from SupplierDevelop e where e.companyId=? and e.supplierName=?";
		List<SupplierDevelop> lists = supplierDevelopDao.find(hql,ContextUtils.getCompanyId(),name);
		if(lists.size()==0){
			return null;
		}else{
			return lists.get(0);
		}
	}
	/**
	 * 查询所有部门
	 * @return
	 */
	public List<Department> queryAllDepartments(){
		String hql = "from Department d where d.deleted = 0 order by d.parent desc,weight";
		List<Department> list = supplierDevelopDao.createQuery(hql).list();
		return list;
	}
	
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<User> queryAllUsers(){
		String hql = "from User d where d.deleted = 0 order by d.weight";
		List<User> list = supplierDevelopDao.createQuery(hql).list();
		return list;
	}
}
