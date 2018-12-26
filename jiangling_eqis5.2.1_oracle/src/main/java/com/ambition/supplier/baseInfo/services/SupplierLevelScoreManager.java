package com.ambition.supplier.baseInfo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.baseInfo.dao.SupplierLevelScoreDao;
import com.ambition.supplier.entity.SupplierLevelScore;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年9月27日 发布
 */
@Service
@Transactional
public class SupplierLevelScoreManager {

	@Autowired
	private SupplierLevelScoreDao supplierLevelScoreDao;
	@Autowired
	private LogUtilDao logUtilDao;
	public Page<SupplierLevelScore> listDatas(Page<SupplierLevelScore> page) {
		return supplierLevelScoreDao.listDatas(page);
	}

	public SupplierLevelScore getSupplierLevelScore(Long id) {
		// TODO Auto-generated method stub
		return supplierLevelScoreDao.get(id);
	}

	public void delete(String ids) {
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(supplierLevelScoreDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", supplierLevelScoreDao.get(Long.valueOf(id)).toString());
			}
			supplierLevelScoreDao.delete(Long.valueOf(id));
		}
	}

	public void saveSupplierLevelScore(SupplierLevelScore supplierLevelScore) {
		// TODO Auto-generated method stub
		supplierLevelScoreDao.save(supplierLevelScore);
	}
}
