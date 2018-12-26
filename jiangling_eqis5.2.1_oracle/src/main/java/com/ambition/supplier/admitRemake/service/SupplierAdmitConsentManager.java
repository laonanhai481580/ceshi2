package com.ambition.supplier.admitRemake.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.gp.entity.GpMaterial;
import com.ambition.gp.entity.GpMaterialSub;
import com.ambition.gp.gpmaterial.services.GpMaterialManager;
import com.ambition.supplier.admitRemake.dao.SupplierAdmitDao;
import com.ambition.supplier.entity.SupplierAdmit;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.AfterTaskCompleted;

@Service
public class SupplierAdmitConsentManager implements AfterTaskCompleted {
	@Autowired
	private SupplierAdmitDao supplierAdmitDao;
	@Autowired
	private GpMaterialManager gpMaterialManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Override
	public void execute(Long arg0, TaskProcessingResult transact) {
		// TODO Auto-generated method stub
		SupplierAdmit supplierAdmit = supplierAdmitDao.get(arg0);
		String gpFormNo=gpMaterialManager.findGpMaterial(supplierAdmit.getId());
		GpMaterial gpMaterial=null;
			if(gpFormNo!=null){
				supplierAdmit.setGpMaterialNo(gpFormNo);
				gpMaterial =gpMaterialManager.findReportByFormNo(gpFormNo);
				gpMaterial.setApprovalId(supplierAdmit.getId().toString());
				gpMaterial.setApprovalNo(supplierAdmit.getFormNo());
				supplierAdmit.setGpMaterialState(gpMaterial.getTaskProgress());
				supplierAdmitDao.getSession().save(gpMaterial);
				supplierAdmitDao.getSession().save(supplierAdmit);
			}else{
				gpMaterial = new GpMaterial();
				gpMaterial.setCompanyId(ContextUtils.getCompanyId());//公司ID
				gpMaterial.setCompleteDate(new Date());
				gpMaterial.setCreator(ContextUtils.getLoginName());
				gpMaterial.setCreatorName(ContextUtils.getUserName());
				gpMaterial.setModifiedTime(new Date());
				gpMaterial.setModifier(ContextUtils.getUserName());
//				gpMaterial.setFormNo(formCodeGenerated.generateGpMaterialNo());//表单单号
				
				gpMaterial.setSupplierName(supplierAdmit.getSupplierName());
				gpMaterial.setSupplierCode(supplierAdmit.getSupplierCode());
				gpMaterial.setSupplierLoginName(supplierAdmit.getSupplierLoginName());
				gpMaterial.setSupplierEmail(supplierAdmit.getSupplierEmail());
				gpMaterial.setMaterialName(supplierAdmit.getMaterialName());
				gpMaterial.setMaterialCode(supplierAdmit.getMaterialCode());
				gpMaterial.setApprovalId(supplierAdmit.getId().toString());
				gpMaterial.setApprovalNo(supplierAdmit.getFormNo());
				gpMaterial.setInitiator(supplierAdmit.getPurchaseProcesser());
				gpMaterial.setInitiatorLogin(supplierAdmit.getPurchaseProcessLog());
				gpMaterial.setConfirmDept(supplierAdmit.getQsChecker());
				gpMaterial.setTaskProgress("待提交");
				gpMaterial.setConfirmDeptLoing(supplierAdmit.getQsCheckerLog());
				List<GpMaterialSub> gpMaterialSubs= gpMaterial.getGpMaterialSubs();
				if(gpMaterialSubs == null){
					gpMaterialSubs = new ArrayList<GpMaterialSub>();
					GpMaterialSub item = new GpMaterialSub();
					gpMaterialSubs.add(item);
				}
//				gpMaterial.setSupplierDate(new Date());
				String params = supplierAdmit.getQsChecker();
				try {
					gpMaterialManager.releaseEmail(params,gpMaterial.getFormNo());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				supplierAdmitDao.getSession().save(gpMaterial);
				supplierAdmit.setGpMaterialNo(gpMaterial.getFormNo());
				supplierAdmit.setGpMaterialId(gpMaterial.getId().toString());
				supplierAdmitDao.getSession().save(supplierAdmit);
			}
			
	}
	

}
