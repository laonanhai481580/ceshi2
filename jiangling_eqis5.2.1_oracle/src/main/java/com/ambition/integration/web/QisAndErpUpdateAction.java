/**   
 * @Title: QisAndErpUpdateAction.java 
 * @Package com.ambition.util.erp.web 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 刘承斌   
 * @date 2015-3-2 上午10:46:59 
 * @version V1.0   
 */ 
package com.ambition.integration.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.UpdateTimestamp;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.common.DateUtil;
import com.ambition.util.erp.dao.UpdateTimestampDao;
import com.ambition.util.erp.service.DcrnReportIntegerationServices;
import com.ambition.util.erp.service.EcnReportIntegerationServices;
import com.ambition.util.erp.service.GpAverageMaterialService;
import com.ambition.util.erp.service.GsmWarmingService;
import com.ambition.util.erp.service.IqcIntegrationService;
import com.ambition.util.erp.service.IqcNoticeIntegrationService;
import com.ambition.util.erp.service.MaterialIntegrationService;
import com.ambition.util.erp.service.SentOutRecordIntegrationService;
import com.ambition.util.erp.service.SpcSynchroDatasService;
import com.ambition.util.erp.service.SupplierEmailIntegrationService;
import com.ambition.util.erp.service.SupplierIntegrationService;
import com.ambition.util.erp.service.SupplierMaterialEvaluateIntegerServices;
import com.norteksoft.product.util.ContextUtils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名: QisAndErpUpdateAction 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  刘承斌
 * @version 1.00  2015-3-2 上午10:46:59  发布
 */
@Namespace("/integration")
@ParentPackage("default")
public class QisAndErpUpdateAction extends CrudActionSupport<UpdateTimestamp>{
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private UpdateTimestampDao updateTimestampDao;//更新时间
	@Autowired
	private IqcIntegrationService iqcIntegrationService;//物料Bom信息(物料)
	@Autowired
	private MaterialIntegrationService materialIntegrationService;//物料Bom信息(物料)
	@Autowired
	private SupplierIntegrationService supplierIntegrationService;//供应商信息
	@Autowired
	private SupplierEmailIntegrationService supplierEmailIntegrationService;//供应商信息
	@Autowired
	private SentOutRecordIntegrationService sentOutRecordIntegrationService;//发料记录
	@Autowired
	private SupplierMaterialEvaluateIntegerServices supplierMaterialEvaluateIntegerServices;
	@Autowired
	private DcrnReportIntegerationServices dcrnReportIntegerationServices;
	@Autowired
	private IqcNoticeIntegrationService iqcNoticeIntegrationService;
	@Autowired
	private EcnReportIntegerationServices ecnReportIntegerationServices;
	@Autowired
	private GsmWarmingService gsmWarmingService;
	@Autowired
	private GpAverageMaterialService gpAverageMaterialService;
	@Autowired
	private SpcSynchroDatasService spcSynchroDatasService;
//	@Autowired
//	private MailSendService mailSendService;//发送邮件
	@Action("input")
	@Override
	public String input() throws Exception {
		@SuppressWarnings("unchecked")
		List<UpdateTimestamp> updateTimestamps = updateTimestampDao.getSession().createQuery("from UpdateTimestamp").list();
		for(UpdateTimestamp updateTimestamp : updateTimestamps){
			ActionContext.getContext().put(updateTimestamp.getTableName(),DateUtil.formateTimeStr(updateTimestamp.getLastModifiedTime()));
		}
		return SUCCESS;
	}
	
	/**
	  * 方法名: bom
	  * <p>功能说明：手动物料Bom信息</p>
	  * @return
	  * @throws Exception
	 */
	@Action("import-material-from-erp")
	public String bom() throws Exception {
		try {
//			materialIntegrationService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
			materialIntegrationService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
			createMessage("物料Bom信息更新成功!");
		} catch (Exception e) {
			log.error("物料Bom信息更新失败!",e);
			createErrorMessage("物料Bom信息更新失败:" + e.getMessage());
		}
		return null;
	}
	
	/**
	  * 方法名: 发料记录
	  * <p>功能说明：手动物料Bom信息</p>
	  * @return
	  * @throws Exception
	 */
	@Action("import-sent-out-record-from-erp")
	public String sentOutRecord() throws Exception {
		try {
//			materialIntegrationService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
			sentOutRecordIntegrationService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
			createMessage("发料记录信息更新成功!");
		} catch (Exception e) {
			log.error("发料记录信息更新失败!",e);
			createErrorMessage("发料记录信息更新失败:" + e.getMessage());
		}
		return null;
	}
	/**
     * 方法名:来料检验
     * <p>功能说明：手动物料Bom信息</p>
     * @return
     * @throws Exception
    */
   @Action("import-iqc-from-erp")
   public String inspectionReport() throws Exception {
       try {
           iqcIntegrationService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
           createMessage("检验信息信息更新成功!");
       } catch (Exception e) {
           log.error("检验信息更新失败!",e);
           createErrorMessage("检验信息更新失败:" + e.getMessage());
       }
       return null;
   }
	/**
	  * 方法名: supplier
	  * <p>功能说明：手动供应商信息</p>
	  * @return
	  * @throws Exception
	 */
	@Action("import-supplier-from-erp")
	public String supplier() throws Exception  {
		try {
			supplierIntegrationService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
			createMessage("供应商信息更新成功!");
		} catch (Exception e) {
			log.error("供应商信息更新失败!",e);
			createErrorMessage("供应商信息更新失败:" + e.getMessage());
		}
		return null;
	}
	/**
	  * 方法名: Dcrn工程变更到期提醒
	  * <p>功能说明：手动供应商信息</p>
	  * @return
	  * @throws Exception
	 */
	@Action("import-dcrn-from-qis")
	public String dcrnEmail() throws Exception  {
		try {
			dcrnReportIntegerationServices.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
			createMessage("dcrn到期提醒成功!");
		} catch (Exception e) {
			log.error("dcrn到期提醒失败!",e);
			createErrorMessage("dcrn到期提醒失败:" + e.getMessage());
		}
		return null;
	}
	/**
	  * 方法名: ecn工程变更到期提醒
	  * <p>功能说明：手动供应商信息</p>
	  * @return
	  * @throws Exception
	 */
	@Action("import-ecn-from-qis")
	public String ecnEmail() throws Exception  {
		try {
			ecnReportIntegerationServices.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
			createMessage("ecn到期提醒成功!");
		} catch (Exception e) {
			log.error("ecn到期提醒失败!",e);
			createErrorMessage("ecn到期提醒失败:" + e.getMessage());
		}
		return null;
	}
	/**
	    * 方法名:计量器具校验提前预警
	    * <p>功能说明：计量器具校验提前预警</p>
	    * @return
	    * @throws Exception
	   */
	  @Action("import-gsm-warming")
	  public String gsmWarming() throws Exception {
	      try {
	    	  gsmWarmingService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
	          createMessage("计量器具校验提前预警信息更新成功!");
	      } catch (Exception e) {
	          log.error("计量器具校验提前预警信息更新失败!",e);
	          createErrorMessage("计量器具校验提前预警信息更新失败:" + e.getMessage());
	      }
	      return null;
	  }		
	  /**
	    * 方法名:均值材料过期提醒
	    * <p>功能说明：供应商稽核提醒</p>
	    * @return
	    * @throws Exception
	   */
	  @Action("import-gp-average-material")
	  public String gpAverageMaterial() throws Exception {
	      try {
	    	  gpAverageMaterialService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
	          createMessage("均值材料过期提醒更新成功!");
	      } catch (Exception e) {
	          log.error("均值材料过期提醒醒更新失败!",e);
	          createErrorMessage("均值材料过期提醒更新失败:" + e.getMessage());
	      }
	      return null;
	  }
	/**
	  * 方法名: 检验任务超期通知
	  * <p>功能说明：手动供应商信息</p>
	  * @return
	  * @throws Exception
	 */
	@Action("import-iqc-notice")
	public String iqcNoticeEmail() throws Exception  {
		try {
			iqcNoticeIntegrationService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
			createMessage("检验任务超期通知成功!");
		} catch (Exception e) {
			log.error("检验任务超期通知失败!",e);
			createErrorMessage("检验任务超期通知失败:" + e.getMessage());
		}
		return null;
	}	
	
	/**
	  * 方法名: supplier
	  * <p>功能说明：手动供应商邮箱信息</p>
	  * @return
	  * @throws Exception
	 */
	@Action("import-supplier-email-erp")
	public String supplierEmail() throws Exception  {
		try {
			supplierEmailIntegrationService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
			createMessage("供应商邮箱信息更新成功!");
		} catch (Exception e) {
			log.error("供应商邮箱信息更新失败!",e);
			createErrorMessage("供应商邮箱信息更新失败:" + e.getMessage());
		}
		return null;
	}
	/**
	    * 方法名:赠品仓数据
	    * <p>功能说明：手动赠品仓数据信息</p>
	    * @return
	    * @throws Exception
	   */
	  @Action("integration-supplier-evaluate-from-erp")
	  public String supplierMaterialEvaluateReport() throws Exception {
	      try {
	    	  supplierMaterialEvaluateIntegerServices.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
	          createMessage("赠品仓数据更新成功!");
	      } catch (Exception e) {
	          log.error("赠品仓数据更新失败!",e);
	          createErrorMessage("赠品仓数据更新失败:" + e.getMessage());
	      }
	      return null;
	  }	
	  /**
		  * 方法名: 检验数据同步到spc
		  * 
		  * <p>功能说明：</p>
		  * @return
		  * @throws Exception
		 */
		@Action("synchro-spc")
		public String sychorDatasSpc() throws Exception {
			try {
				spcSynchroDatasService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
				createMessage("检验数据同步成功!");
			} catch (Exception e) {
				e.printStackTrace();
				log.error("检验数据同步失败!",e);
				createErrorMessage("检验数据同步成功:" + e.getMessage());
			}
			return null;
		}
	/**
	    * 方法名: 主页消息插入OA
	    * <p>功能说明：Message</p>
	    * @return
	    * @throws Exception
	   */
//	  @Action("integration-message-for-oa")
//	  public String messageForOa() throws Exception {
//	      try {	
//	          portalMessageForOaServier.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
//	          createMessage("生产订单信息更新成功!");
//	      } catch (Exception e) {
//	          log.error("生产订单信息更新失败!",e);
//	          createErrorMessage("生产订单信息更新失败:" + e.getMessage());
//	      }
//	      return null;
//	  }
}