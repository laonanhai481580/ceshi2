package com.ambition.supplier.manager.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.SupplierGoal;
import com.ambition.supplier.entity.SupplierImprove;
import com.ambition.supplier.entity.SupplyProduct;
import com.ambition.supplier.manager.dao.ManagerDao;
import com.ambition.supplier.manager.dao.RedYellowSignDao;
import com.ambition.supplier.manager.dao.StateQueryDao;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
/**    
 * ManagerManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class ManagerManager {
	@Autowired
	private ManagerDao managerDao;
	@Autowired
	private StateQueryDao stateQueryDao;
	@Autowired
	private RedYellowSignDao redYellowDao;
	
	/**
	 * 检查是否存在相同的供应商改进记录
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Boolean isExistSupplierImprove(Long id,String type,String name){
		String hql = "select count(*) from SupplierImprove s where s.companyId = ? and (s.auditType = ? or s.auditedSupplier=?)";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(type);
		params.add(name);
		if(id != null){
			hql += " and s.id <> ?";
			params.add(id);
		}
		Query query = managerDao.getSession().createQuery(hql);
		for(int i=0;i<params.size();i++){
			query.setParameter(i,params.get(i));
		}
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	
	public SupplierImprove getSupplierImprove(Long id) {
		return managerDao.get(id);
	}
	
	public void deleteSupplierImprove(String deleteIds) {
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			SupplierImprove supplierImprove = managerDao.get(Long.valueOf(id));
			if(supplierImprove != null){
				managerDao.delete(supplierImprove);
			}
		}
	}
	
	public void saveSupplierImprove(SupplierImprove supplierImprove) {
//		if(StringUtils.isEmpty(supplierImprove.getAuditType())){
//			throw new RuntimeException("审核类型不能为空!");
//		}
//		if(StringUtils.isEmpty(supplierImprove.getAuditedSupplier())){
//			throw new RuntimeException("受审核放方不能为空!");
//		}
//		if(StringUtils.isEmpty(supplierImprove.getReportNo())){
//			throw new RuntimeException("报告编号不能为空!");
//		}
//		if(isExistSupplierImprove(supplierImprove.getId(),supplierImprove.getAuditType(),supplierImprove.getAuditedSupplier())){
//			throw new RuntimeException("已存在相同的改进记录!");
//		}
		managerDao.save(supplierImprove);
	}
	
	/**
	 * 查询供应商改进记录
	 * @param page
	 * @return
	 */
	public Page<SupplierImprove> searchByPage(Page<SupplierImprove> page){
		StringBuffer sb = new StringBuffer("from SupplierImprove s");
		return managerDao.searchPageByHql(page,sb.append(" where s.companyId = ?").toString(),ContextUtils.getCompanyId());
	}
	
	
	/**
	 * 查询供应商开发状态
	 * @param page
	 * @return
	 */
	public Page<SupplyProduct> searchBySupplyProductPage(Page<SupplyProduct> page){
		StringBuffer sb = new StringBuffer("from SupplyProduct s");
		return stateQueryDao.searchPageByHql(page,sb.append(" where s.companyId = ?").toString(),ContextUtils.getCompanyId());
	}	
	
	/**
	 * 查询供应商红黄牌
	 * @param page
	 * @return
	 */
	public Page<SupplierGoal> searchBySignPage(Page<SupplierGoal> page){
		StringBuffer sb = new StringBuffer("from SupplierGoal s where s.companyId = ?");
		sb.append(" order by s.totalPoints desc");
		return redYellowDao.searchPageByHql(page,sb.toString(),ContextUtils.getCompanyId());
	}
	
	public  List<SupplierGoal> list(int year,String businessUnitCode,String importance){
		return redYellowDao.getSupplierGoal(year,businessUnitCode,importance);
	}
	
	public  List<SupplierGoal> listDstimateDegree(int year,String businessUnitCode,String evaluateGrade){
		return redYellowDao.getEvaluateGrade(year,businessUnitCode,evaluateGrade);
	}
	
	public  Page<SupplierGoal> listpage(Page<SupplierGoal> page,int year,String importance){
		return redYellowDao.getSupplierGoalPage(page,year,importance);
	}
	

	
	public JSONObject convertJsonObject(JSONObject params) throws Exception {
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}else{
			for(Object key : params.keySet()){
				resultJson.put(key, params.getJSONArray(key.toString()).get(0));
			}
			return resultJson;
		}
	}
	
}
