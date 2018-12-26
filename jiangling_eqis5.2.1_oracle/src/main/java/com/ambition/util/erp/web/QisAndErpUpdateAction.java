/**   
 * @Title: QisAndErpUpdateAction.java 
 * @Package com.ambition.util.erp.web 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 刘承斌   
 * @date 2015-3-2 上午10:46:59 
 * @version V1.0   
 */ 
package com.ambition.util.erp.web;

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
import com.ambition.util.erp.service.IqcIntegrationService;
import com.ambition.util.erp.service.MaterialIntegrationService;
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
@Namespace("/common/integration")
@ParentPackage("default")
public class QisAndErpUpdateAction extends CrudActionSupport<UpdateTimestamp>{
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private UpdateTimestampDao updateTimestampDao;//更新时间
	@Autowired
	private IqcIntegrationService iqcIntegrationService;//来料检验
	@Autowired
	private MaterialIntegrationService materialIntegrationService;//物料Bom信息(物料)
	@Autowired
	private SupplierIntegrationService supplierIntegrationService;//供应商
	@Autowired
	private SupplierMaterialEvaluateIntegerServices supplierMaterialEvaluateIntegerServices;
	@Action("input")
	@Override
	public String input() throws Exception {
		@SuppressWarnings("unchecked")
		List<UpdateTimestamp> updateTimestamps = updateTimestampDao.getSession().createQuery("from UpdateTimestamp").list();
		for(UpdateTimestamp updateTimestamp : updateTimestamps){
			ActionContext.getContext().put(updateTimestamp.getTableName(),DateUtil.formateTimeStr(updateTimestamp.getModifiedTime()));
		}
		return SUCCESS;
	}
	
	/**
	  * 方法名: bom
	  * <p>功能说明：手动物料Bom信息</p>
	  * @return
	  * @throws Exception
	 */
	@Action("integration-bom-from-erp")
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
     * 方法名:来料检验
     * <p>功能说明：手动物料Bom信息</p>
     * @return
     * @throws Exception
    */
   @Action("integration-iqc-from-erp")
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
	  * 方法名: supplier
	  * <p>功能说明：手动供应商信息</p>
	  * @return
	  * @throws Exception
	 */
	@Action("integration-supplier-from-erp")
	public String supplier() throws Exception {
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
	  * 方法名: mail
	  * <p>功能说明：手动发送邮件</p>
	  * @return
	  * @throws Exception
	 */
	@Action("integration-mail-from-erp")
	public String mail() throws Exception {
		try {
//			mailSendService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
			createMessage("发送邮件更新成功!");
		} catch (Exception e) {
			log.error("发送邮件更新失败!",e);
			createErrorMessage("发送邮件更新失败:" + e.getMessage());
		}
		return null;
	}
	
	
	
	
	/**
	  * 方法名: 组织结构同步
	  * <p>功能说明：组织结构同步</p>
	  * @return
	  * @throws Exception
	 */
	@Action("integration-acs-from-erp")
	public String acs() throws Exception {
		try {
//			deptAndUserIntegrationService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName(),false);
			createMessage("组织结构同步成功!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("组织结构同步失败!",e);
			createErrorMessage("组织结构同步成功:" + e.getMessage());
		}
		return null;
	}

	/**
	  * 方法名: 检定计划提醒
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("integration-inspection-plan-erp")
	public String inspectionPlan() throws Exception {
		try {
//			inspectionPlanIntegerServive.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
			createMessage("检定计划提醒同步成功!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("检定计划提醒同步失败!",e);
			createErrorMessage("检定计划提醒同步成功:" + e.getMessage());
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