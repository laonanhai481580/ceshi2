package com.ambition.supplier.develop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.develop.dao.SupplierSurveyDao;
import com.ambition.supplier.entity.SupplierSurvey;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月10日 发布
 */
@Service
@Transactional
public class SupplierSurveyManager {

	@Autowired
	private SupplierSurveyDao supplierSurveyDao;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private UseFileManager useFileManager;
	public SupplierSurvey getSupplierSurvey(Long id) {
		// TODO Auto-generated method stub
		return supplierSurveyDao.get(id);
	}

	public void saveSupplierSurvey(SupplierSurvey supplierSurvey) {
		// TODO Auto-generated method stub
		supplierSurveyDao.save(supplierSurvey);
		useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisAttachmentFiles"),supplierSurvey.getFileName());
	}

	public Page<SupplierSurvey> listDatas(Page<SupplierSurvey> page) {
		// TODO Auto-generated method stub
		return supplierSurveyDao.searchPageByHql(page, "from SupplierSurvey s where s.companyId=?", ContextUtils.getCompanyId());
	}

	public void delete(String ids) {
		// TODO Auto-generated method stub
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(supplierSurveyDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", supplierSurveyDao.get(Long.valueOf(id)).toString());
			}
			SupplierSurvey s = supplierSurveyDao.get(Long.valueOf(id));
			supplierSurveyDao.delete(s);
		}
	}
}
