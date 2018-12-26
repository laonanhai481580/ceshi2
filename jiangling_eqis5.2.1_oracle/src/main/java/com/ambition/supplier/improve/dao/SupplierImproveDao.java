package com.ambition.supplier.improve.dao;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.supplier.entity.SupplierImprove;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年11月4日 发布
 */
@Repository
public class SupplierImproveDao extends HibernateDao<SupplierImprove,Long>{
	public Page<SupplierImprove> searchSingle(Page<SupplierImprove> page) {
		  String hql = "from SupplierImprove o where o.creator=?  ";
		  String login=ContextUtils.getLoginName();
		  return searchPageByHql(page, hql,login);
	}
	public Page<SupplierImprove> searchSupplierSingle(Page<SupplierImprove> page) {
		  String hql = "from SupplierImprove o where o.supplierCode=? ";
		  String login=ContextUtils.getLoginName();
		  return searchPageByHql(page, hql,login);
	}
	
	public Page<SupplierImprove> searchOkSingle(Page<SupplierImprove> page) {
		 
		  String hql = "from SupplierImprove o where o.workflowInfo.state='流程已结束' and o.creator=?  ";
		  String login=ContextUtils.getLoginName();
		  return searchPageByHql(page, hql,login);
	}  
	public Page<SupplierImprove> searchOkSupplierSingle(Page<SupplierImprove> page) {
		  String hql = "from SupplierImprove o where o.workflowInfo.state='流程已结束' and o.supplierCode=? ";
		  String login=ContextUtils.getLoginName();
		  return searchPageByHql(page, hql,login);
	}
	//神奇新增台账
	public Page<SupplierImprove> oklist(Page<SupplierImprove> page){
		 String hql = "from SupplierImprove o where o.workflowInfo.state ='流程已结束'";
		 return searchPageByHql(page, hql);
	}
}
