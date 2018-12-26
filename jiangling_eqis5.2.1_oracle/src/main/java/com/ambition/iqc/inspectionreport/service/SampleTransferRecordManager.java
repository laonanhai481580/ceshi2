package com.ambition.iqc.inspectionreport.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.SampleScheme;
import com.ambition.iqc.entity.SampleTransferRecord;
import com.ambition.iqc.inspectionreport.dao.SampleTransferRecordDao;
import com.ambition.supplier.utils.DateUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 规则转换记录
 * @author 赵骏
 *
 */
@Service
@Transactional
public class SampleTransferRecordManager {
	@Autowired
	private SampleTransferRecordDao sampleTransferRecordDao;

	@Autowired
	private LogUtilDao logUtilDao;
	
	public SampleTransferRecord getSampleTransferRecord(Long id){
		return sampleTransferRecordDao.get(id);
	}
	/**
	 * 查询规则转换记录
	 * @param page
	 * @return
	 */
	public Page<SampleTransferRecord> search(Page<SampleTransferRecord> page){
		return sampleTransferRecordDao.search(page);
	}
	
	public void saveSampleTransferRecord(SampleTransferRecord sampleTransferRecord){
		sampleTransferRecordDao.save(sampleTransferRecord);
		logUtilDao.debugLog("保存", sampleTransferRecord.toString());
	}
	
	/**
	 * 添加转移规则
	 * @param sampleTransferRecord
	 */
	public void addSampleTransferRecord(SampleTransferRecord sampleTransferRecord){
		sampleTransferRecord.setAuditTime(DateUtil.parseDateTime(Struts2Utils.getParameter("auditTime")));
		SampleTransferRecord historyRecord = getSampleTransferRecord(sampleTransferRecord.getBusinessUnitCode(),sampleTransferRecord.getSupplierCode(),sampleTransferRecord.getCheckBomCode(),sampleTransferRecord.getAuditTime(),ContextUtils.getCompanyId());
		if(historyRecord == null){
			sampleTransferRecord.setSourceRule(SampleScheme.ORDINARY_TYPE);
		}else{
			sampleTransferRecord.setSourceRule(historyRecord.getTargetRule());
			sampleTransferRecord.setInspectionDate(sampleTransferRecord.getAuditTime());
		}
		sampleTransferRecordDao.save(sampleTransferRecord);
		logUtilDao.debugLog("添加转移规则","添加转移方案规则 " + sampleTransferRecord.getTargetRule());
	}
	/**
	 * 根据参数获取当前最新的转换规则
	 * @param supplierId
	 * @param checkBomCode
	 * @param checkItemNo
	 * @param inspectionDate
	 * @return
	 */
	public SampleTransferRecord getSampleTransferRecord(String businessUnitCode,String businessUnitName,String checkBomCode,String checkItemName,Date inspectionDate){
		String hql = "from SampleTransferRecord s where s.companyId = ? and s.businessUnitName = ? and s.supplierCode = ? and s.checkBomCode = ? and s.checkItemName = ? and s.auditState = ? and s.auditTime < ? order by s.auditTime desc";
		Query query = sampleTransferRecordDao.createQuery(hql, ContextUtils.getCompanyId(),businessUnitCode,businessUnitName,checkBomCode,checkItemName,SampleTransferRecord.AUDITSTATE_PASS,inspectionDate);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<SampleTransferRecord> list = query.list();
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}
	/**
	 * 根据参数获取当前最新的转换规则
	 * @param supplierId
	 * @param checkBomCode
	 * @param inspectionDate
	 * @return
	 */
	public SampleTransferRecord getSampleTransferRecord(String businessUnitCode,String supplierCode,String checkBomCode,Date inspectionDate){
		return getSampleTransferRecord(businessUnitCode,supplierCode, checkBomCode, inspectionDate,ContextUtils.getCompanyId());
	}
	/**
	 * 根据参数获取当前最新的转换规则
	 * @param supplierCode
	 * @param checkBomCode
	 * @param inspectionDate
	 * @return
	 */
	public SampleTransferRecord getSampleTransferRecord(String businessUnitName,String supplierCode,String checkBomCode,Date inspectionDate,Long companyId){
		String checkBomMaterialType = Struts2Utils.getParameter("checkBomMaterialType");
		String hql = "";
		String companyName = ContextUtils.getCompanyName();
		if("欧菲科技-CCM".equals(companyName)){
			hql = "from SampleTransferRecord s where s.companyId = ? and s.businessUnitName = ? and s.supplierCode = ? and s.checkBomCode = ? and s.auditState = ? and s.auditTime < ? order by s.auditTime desc";
			Query query = sampleTransferRecordDao.createQuery(hql, companyId,businessUnitName,supplierCode,checkBomCode,SampleTransferRecord.AUDITSTATE_PASS,inspectionDate);
			query.setMaxResults(1);
			@SuppressWarnings("unchecked")
			List<SampleTransferRecord> list = query.list();
			if(list.isEmpty()){
				return null;
			}else{
				return list.get(0);
			}
		}else{
			hql = "from SampleTransferRecord s where s.companyId = ? and s.businessUnitName = ? and s.supplierCode = ? and s.checkBomCode = ? and s.auditState = ? and s.auditTime < ? order by s.auditTime desc";
			Query query = sampleTransferRecordDao.createQuery(hql, companyId,businessUnitName,supplierCode,checkBomCode,SampleTransferRecord.AUDITSTATE_PASS,inspectionDate);
			query.setMaxResults(1);
			@SuppressWarnings("unchecked")
			List<SampleTransferRecord> list = query.list();
			if(list.isEmpty()){
				return null;
			}else{
				return list.get(0);
			}
		}
		
		
	}
	/**
	 * 审核抽样方案
	 * @param ids
	 * @param state
	 */
	public void auditSampleTransfer(String ids,String state,String auditText){
		for(String id : ids.split(",")){
			if(StringUtils.isNotEmpty(id)){
				SampleTransferRecord sampleTransferRecord = sampleTransferRecordDao.get(Long.valueOf(id));
				if(!sampleTransferRecord.getAuditState().equals(state)){
					sampleTransferRecord.setAuditState(state);
					sampleTransferRecord.setAuditMan(ContextUtils.getUserName());
					sampleTransferRecord.setAuditTime(new Date());
					sampleTransferRecord.setAuditText(auditText);
					sampleTransferRecordDao.save(sampleTransferRecord);
					//发送邮件
					/*String emailContent = "";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
					if(state.equals(SampleTransferRecord.AUDITSTATE_PASS)){
						emailContent = "*"+sdf.format(sampleTransferRecord.getCreatedTime())+"转移规则变更："
								+"供应商为"+sampleTransferRecord.getSupplierName()+"，物料为"+sampleTransferRecord.getCheckBomName()
								+"在检验过程中，其检验规则发生从"
								+sampleTransferRecord.getSourceRule()+"到"+sampleTransferRecord.getTargetRule()+"的转移！";
					}
					if(state.equals(SampleTransferRecord.AUDITSTATE_FAIL)){
						emailContent = "*"+sdf.format(sampleTransferRecord.getCreatedTime())+"转移规则变更："
								+"供应商为"+sampleTransferRecord.getSupplierName()+"，物料为"+sampleTransferRecord.getCheckBomName()
								+"在检验过程中，其检验规则从"
								+sampleTransferRecord.getTargetRule()+"还原成了"+sampleTransferRecord.getSourceRule()+"！";
					}
					String toUsers[] = new String[]{};
					try {
						toUsers  = ResourceBundle.getBundle("mail",Locale.getDefault()).getString("mail.host.user.to").split(";");
						for(String toUser : toUsers){
							if(StringUtils.isNotEmpty(toUser)){
								//AsyncMailUtils.sendMail(toUser,"进货检验的转移规则变更",emailContent.toString());
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}*/
					logUtilDao.debugLog("保存", sampleTransferRecord.toString());
				}
			}
		}
	}
	
	/**
	 * 删除抽样方案变更
	 * @param ids
	 */
	public void delete(String ids){
		for(String id : ids.split(",")){
			if(StringUtils.isNotEmpty(id)){
				logUtilDao.debugLog("删除", sampleTransferRecordDao.get(Long.valueOf(id)).toString());
				sampleTransferRecordDao.delete(Long.valueOf(id));
			}
		}
	}
}
