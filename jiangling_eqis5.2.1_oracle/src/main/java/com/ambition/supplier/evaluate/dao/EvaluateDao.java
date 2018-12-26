package com.ambition.supplier.evaluate.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.Evaluate;
import com.ambition.supplier.entity.EvaluateDetail;
import com.ambition.supplier.entity.Supplier;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class EvaluateDao extends HibernateDao<Evaluate, Long> {
	
	/**
	 * 查询评价台帐
	 * @param page
	 * @param supplierId
	 * @return
	 */
	public Page<Evaluate> search(Page<Evaluate> page,Long supplierId){
		String hql = "from Evaluate e where e.companyId = ? and e.supplierId = ?";
		return searchPageByHql(page,hql,new Object[]{ContextUtils.getCompanyId(),supplierId});
	}
	
	/**
	 * 根据供应商的年月返回对应的评价
	 * @param supplier
	 * @param year
	 * @param month
	 * @return
	 */
	public Evaluate findEvaluateBySupplerAndYearMonth(Supplier supplier,Integer year,Integer month,Long estimateModelId,String materialType){
		String businessUnitCode  = CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName());
		String hql = "from Evaluate e where e.companyId = ? and e.supplierId = ? and e.evaluateYear = ? and e.evaluateMonth = ? and e.estimateModelId = ? and e.materialType=? ";
		List<Evaluate> evaluates = this.find(hql,new Object[]{ContextUtils.getCompanyId(),supplier.getId(),year,month,estimateModelId,materialType});
		if(evaluates.isEmpty()){
			return null;
		}else{
			return evaluates.get(0);
		}
	}
	/**
	 * 根据供应商的年月及周期返回对应的评价
	 * @param supplier
	 * @param year
	 * @param month
	 * @return
	 *//*
	public Evaluate findEvaluateBySupplerAndYearMonthSQ(Supplier supplier,Integer year,Integer month,Long estimateModelId,String materialType,String cycle){
		String businessUnitCode  = CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName());
		String hql = "from Evaluate e where e.companyId = ? and e.supplierId = ? and e.evaluateYear = ? and e.evaluateMonth = ? and e.estimateModelId = ? and e.materialType=? and e.cycle=?";
		List<Evaluate> evaluates = this.find(hql,new Object[]{ContextUtils.getCompanyId(),supplier.getId(),year,month,estimateModelId,materialType,cycle});
		if(evaluates.isEmpty()){
			return null;
		}else{
			return evaluates.get(0);
		}
	}*/
	/**
	 * 查询年度的所有评价
	 * @param supplier
	 * @param year
	 * @param estimateModelId
	 * @return
	 */
	public List<Evaluate> findEvaluateBySupplierAndYear(Supplier supplier,Integer year,Long parentModelId,String materialType){
		String businessUnitCode  = CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName());
		String hql = "from Evaluate e where e.companyId = ? and e.supplierId = ? and e.evaluateYear = ? and e.parentModelId = ? and e.materialType=? ";
		return this.find(hql,new Object[]{ContextUtils.getCompanyId(),supplier.getId(),year,parentModelId,materialType});
	}
	/**
	 * 查询年度的所有评价
	 * @param supplier
	 * @param year
	 * @param estimateModelId
	 * @return
	 */
	public List<EvaluateDetail> findEvaluateDetailBySupplierAndYear(Supplier supplier,Integer year,String materialType){
		String businessUnitCode  = CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName());
		String hql = "select e from EvaluateDetail e where e.evaluate.supplierId = ? and e.evaluate.evaluateYear = ? and e.evaluate.materialType = ? ";
		return this.find(hql,new Object[]{supplier.getId(),year,materialType});
	}
}
