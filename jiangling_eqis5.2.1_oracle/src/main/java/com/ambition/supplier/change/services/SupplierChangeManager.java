package com.ambition.supplier.change.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.epm.entity.EpmOrtItem;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.change.dao.SupplierChangeDao;
import com.ambition.supplier.entity.Evaluate;
import com.ambition.supplier.entity.SupplierChange;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月12日 发布
 */
@Service
@Transactional
public class SupplierChangeManager extends AmbWorkflowManagerBase<SupplierChange>{
	@Autowired
	private SupplierChangeDao supplierChangeDao;
	@Override
	public HibernateDao<SupplierChange, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return supplierChangeDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return "SUPPLIER_CHANGE";
	}

	@Override
	public Class<SupplierChange> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return SupplierChange.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "supplier-change";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "PCN流程";
	}
	 /**
     * 方法名: 导出报告
     * <p>功能说明：</p>
     * @return
    */
   public void exportReport(Long entityId) throws IOException {
       exportReport(entityId,"supplier-change.xlsx","PCN申请单");
   }
   public Page<SupplierChange> search(Page<SupplierChange>page){
		return supplierChangeDao.search(page);		
	}
	public Page<SupplierChange> listPageByParams(Page<SupplierChange> page,
			String name) {
		// TODO Auto-generated method stub
		String hql = " from SupplierChange s where s.creator = ?";
		return supplierChangeDao.searchPageByHql(page, hql ,name);
	}
   /**
    * 删除实体，流程相关文件都删除
    * @param entity 删除的对象
    */
   @Override
   public void deleteEntity(SupplierChange entity){
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
	public CompleteTaskTipType submitProcessSelf(SupplierChange supplierChange) {
		// TODO Auto-generated method stub
	   saveEntity(supplierChange);
       Long processId = ApiFactory.getDefinitionService()
               .getWorkflowDefinitionsByCode(getWorkflowDefinitionCode()).get(0).getId();
       return ApiFactory.getInstanceService().submitInstance(processId, supplierChange);
	}

}
