package com.ambition.supplier.archives.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.archives.dao.SupplierMessagesDao;
import com.ambition.supplier.entity.SupplierMessages;
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
 * @version 1.00 2016年10月26日 发布
 */
@Service
@Transactional
public class SupplierMessagesManager {
	@Autowired
	private SupplierMessagesDao supplierMessagesDao;
	@Autowired
	private UseFileManager useFileManager;
	@Autowired
	private LogUtilDao logUtilDao;
	public SupplierMessages getSupplierMessages(Long id) {
		// TODO Auto-generated method stub
		return supplierMessagesDao.get(id);
	}

	public void saveSupplierMessages(SupplierMessages supplierMessages) {
		// TODO Auto-generated method stub
		supplierMessages.setIsNewVersion(true);
		if(supplierMessages.getFileName()!=null){
		   List<SupplierMessages> list = supplierMessagesDao.searchByFileName(supplierMessages.getFileName(),supplierMessages.getId());
		   //所先确认这里面没有当前保存的数据
		   String max = supplierMessages.getFileVersion();//查找有没有比现在高的版本
		   if(max!=null&&!"".equals(max)){
			   long id = (long) -1;
			   for (int i = 0;i<list.size();i++) {
				   if(list.get(i).getFileVersion().compareTo(max)>0){
					   max = list.get(i).getFileVersion();
					   id = list.get(i).getId();
				   }
			   }
			   for (int i=0;i<list.size();i++) {
				   if(id == list.get(i).getId()){
					  list.get(i).setIsNewVersion(true);
					  supplierMessages.setIsNewVersion(false);
				   }else{
					  list.get(i).setIsNewVersion(false);
				   }
				   supplierMessagesDao.save(list.get(i));
			   }
			   if(!max.equals(supplierMessages.getFileVersion())){
				   supplierMessages.setIsNewVersion(false);
			   }
		   }
		}
		supplierMessagesDao.save(supplierMessages);
		useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisAttachmentFiles"),supplierMessages.getFiles());
	}

	public Page<SupplierMessages> searchByPage(Page<SupplierMessages> page) {
		// TODO Auto-generated method stub
		return supplierMessagesDao.searchPageByHql(page, "from SupplierMessages t where t.isNewVersion=? order by t.id desc",true);
	}

	public void deleteSupplierMessages(String deleteIds) {
		// TODO Auto-generated method stub
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			SupplierMessages supplierMessages = supplierMessagesDao.get(Long.valueOf(id));
			if(supplierMessages != null){
				logUtilDao.debugLog("删除", supplierMessages.toString());
				supplierMessagesDao.delete(supplierMessages);
				useFileManager.useAndCancelUseFiles(supplierMessages.getFiles(), null);
			}
		}
	}
}
