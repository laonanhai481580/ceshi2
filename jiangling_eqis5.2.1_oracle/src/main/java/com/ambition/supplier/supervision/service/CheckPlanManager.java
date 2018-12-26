package com.ambition.supplier.supervision.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.supplier.archives.service.SupplierManager;
import com.ambition.supplier.entity.CheckPlan;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplyProduct;
import com.ambition.supplier.supervision.dao.CheckPlanDao;
import com.ambition.supplier.supervision.dao.CheckReportDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 稽查计划台帐
 * @author 赵骏
 *
 */
@Service
@Transactional
public class CheckPlanManager {
	@Autowired
	private CheckPlanDao checkPlanDao;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SupplierManager supplierManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	/**
	 * 查询稽查计划
	 * @param page
	 * @return
	 */
	public Page<CheckPlan> list(Page<CheckPlan> page){
		return checkPlanDao.list(page);
	}
	/**
	 * 获取稽查计划
	 * @return
	 */
	public CheckPlan getCheckPlan(Long id){
		return checkPlanDao.get(id);
	}

	/**
	 * 保存评价项目
	 * @param checkPlan
	 */
	public void storeCheckPlan(CheckPlan checkPlan){
		if(checkPlan.getId() == null){
			//创建最新的表单号
//			checkPlan.setPlanCode(formCodeGenerated.generateCheckPlanCode());
			logUtilDao.debugLog("保存", checkPlan.toString());
		}else{
			logUtilDao.debugLog("修改", checkPlan.toString());
		}
		checkPlanDao.save(checkPlan);
	}
	
	@Autowired
	private CheckReportDao checkReportDao;
	/**
	 * 删除稽查计划
	 * @param deleteIds
	 */
	public void deleteCheckPlan(String deleteIds) {
		for(String id : deleteIds.split(",")){
			if(StringUtils.isNotEmpty(id)){
				CheckPlan checkPlan=checkPlanDao.get(Long.valueOf(id));
				String hql = "update CheckReport c set c.checkPlan = null where c.checkPlan = ?";
				checkReportDao.createQuery(hql,checkPlan).executeUpdate();
				logUtilDao.debugLog("删除", checkPlan.toString());
				checkPlanDao.delete(checkPlan);
			}
		}
	}
	
	public void importCheckPlan(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id: ids){
			Supplier s=supplierManager.getSupplier(Long.valueOf(id));
			CheckPlan checkPlan=new CheckPlan();
			checkPlan.setCompanyId(ContextUtils.getCompanyId());
			checkPlan.setCreatedTime(new Date());
			checkPlan.setCreator(ContextUtils.getUserName());
			checkPlan.setLastModifiedTime(new Date());
			checkPlan.setLastModifier(ContextUtils.getUserName());
			checkPlan.setSupplierCode(s.getCode());
			checkPlan.setSupplierId(s.getId());
			checkPlan.setSupplierImportance(s.getImportance());
			checkPlan.setSupplierName(s.getName());
//			checkPlan.setPlanCode(formCodeGenerated.generateCheckPlanCode());
			List<SupplyProduct> supplierProduct=s.getSupplyProducts();
			StringBuffer supplyProducts=new StringBuffer();
			if(supplierProduct!=null){
				for(int i=0;i<supplierProduct.size();i++){
					supplyProducts.append(","+supplierProduct.get(i).getName());
				}
				supplyProducts.delete(0,1);
			}
			checkPlan.setSupplyProducts(supplyProducts.toString());
			checkPlanDao.save(checkPlan);
		}
	}	
}
