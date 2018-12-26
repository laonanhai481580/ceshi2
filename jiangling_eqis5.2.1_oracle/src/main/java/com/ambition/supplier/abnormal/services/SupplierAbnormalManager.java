package com.ambition.supplier.abnormal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.abnormal.dao.SupplierAbnormalDao;
import com.ambition.supplier.entity.SupplierAbnormal;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年11月5日 发布
 */
@Service
@Transactional
public class SupplierAbnormalManager {
	@Autowired
	private SupplierAbnormalDao supplierAbnormalDao;
	@Autowired
	private LogUtilDao logUtilDao;
	public Page<SupplierAbnormal> listDatas(Page<SupplierAbnormal> page) {
		// TODO Auto-generated method stub
		return supplierAbnormalDao.searchPageByHql(page, "from SupplierAbnormal s where s.companyId=?", ContextUtils.getCompanyId());
	}
	public SupplierAbnormal getSupplierAbnormal(Long id) {
		// TODO Auto-generated method stub
		return supplierAbnormalDao.get(id);
	}
	public void saveSupplierAbnormal(SupplierAbnormal supplierAbnormal) {
		// TODO Auto-generated method stub
		supplierAbnormalDao.save(supplierAbnormal);
	}
	public void delete(String ids) {
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(supplierAbnormalDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", supplierAbnormalDao.get(Long.valueOf(id)).toString());
			}
			supplierAbnormalDao.delete(Long.valueOf(id));
		}
	}
}
