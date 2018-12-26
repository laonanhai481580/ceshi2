package com.ambition.supplier.baseInfo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.baseInfo.dao.SupplierLevelChangeDao;
import com.ambition.supplier.baseInfo.dao.SupplierMaterialTypeGoalDao;
import com.ambition.supplier.entity.SupplierMaterialTypeGoal;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月9日 发布
 */
@Service
@Transactional
public class SupplierMaterialTypeGoalManager {
	@Autowired
	private SupplierMaterialTypeGoalDao supplierMaterialTypeGoalDao;
	@Autowired
	private LogUtilDao logUtilDao;
	public SupplierMaterialTypeGoal getSupplierMaterialTypeGoal(Long id) {
		// TODO Auto-generated method stub
		return supplierMaterialTypeGoalDao.get(id);
	}
	public Page<SupplierMaterialTypeGoal> listDatas(
			Page<SupplierMaterialTypeGoal> page) {
		// TODO Auto-generated method stub
		String hql = " from SupplierMaterialTypeGoal s where s.companyId=?";
		return supplierMaterialTypeGoalDao.searchPageByHql(page, hql, ContextUtils.getCompanyId());
	}
	public void saveSupplierMaterialTypeGoal(
			SupplierMaterialTypeGoal supplierMaterialTypeGoal) {
		// TODO Auto-generated method stub
		supplierMaterialTypeGoalDao.save(supplierMaterialTypeGoal);
	}
	public void delete(String ids) {
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(supplierMaterialTypeGoalDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", supplierMaterialTypeGoalDao.get(Long.valueOf(id)).toString());
			}
			supplierMaterialTypeGoalDao.delete(Long.valueOf(id));
		}
	}
	public List<SupplierMaterialTypeGoal> getAllType() {
		// TODO Auto-generated method stub
		String hql = "select distinct s from SupplierMaterialTypeGoal s";
		return supplierMaterialTypeGoalDao.find(hql);
	}
	public SupplierMaterialTypeGoal findMaterialType(String materialType) {
		// TODO Auto-generated method stub
		String hql = "select distinct s from SupplierMaterialTypeGoal s where s.materialType=?";
		List<SupplierMaterialTypeGoal> list = supplierMaterialTypeGoalDao.find(hql,materialType);
		if(list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}

}
