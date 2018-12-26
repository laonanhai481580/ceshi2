package com.ambition.carmfg.baseinfo.service;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.entity.FormCodingRule;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Service
public class FormCodeGenerated extends HibernateDao<Object, Long>{
	
	@Autowired
	private FormCodeRuleManager formCodeRuleManager;
	/**
	 * 方法名: generateMeasurementNoCode 
	 * <p>功能说明：量检具编号</p>
	 * @return String
	 * @throws
	 */
	public String generateMeasurementNoCode(){
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_LJJ);
	}
	/**
	 * 设置静态的参数
	 * @param key
	 * @param value
	 */
	public void setstaticPropertyMap(String key,String value){
		FormCodeRuleManager.setStaticPropertyMap(key, value);
	}

    /**
	 * 进货报告编号
	 * @return
	 */
	public String generateIqcCode() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_JLJY);
	}
	
	/**
		  * 方法名: IQC 实验委托报告
		  * <p>功能说明：</p>
		  * @return
		 */
	public String generateExperimentalCode() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_IQC);
	}
	
	/**
		  * 方法名: DCR/N报告
		  * <p>功能说明：</p>
		  * @return
		 */
	public String generateDcrnReportCode(){
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_DCRN);
	}
	/**
	  * 方法名:ECN报告
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateEcnReportCode(){
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_ECN);
	}
	
	/**
	  * 方法名:ECR报告
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateEcrReportCode(){
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_ECR);
	}
	
	/**
		  * 方法名:制造过程检验编号
		  * <p>功能说明：</p>
		  * @return
		 */
	public String generateMFGode() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_QC);
	}
	/**
	  * 方法名:ORT实验委托单编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateTestEntrustNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_ORT);
	}

	/**
	  * 方法名:IPQC稽核问题点改善编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateIpqcAuditImproveNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_IPQC);
	}
	
	
	/**
	  * 方法名:FAR解析单编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateFarAnalysisNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_FAR);
	}
	
	/**
	  * 方法名:8D改进报告
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateImproveReportNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_IR);
	}
	
	/**
	  * 方法名品质异常联络单
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateQualityExceptionReportNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_ER);
	}	
	/**
	  * 方法名:內校报告
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateSiCheckInspectionReportFormNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_SI);
	}
	/**
	  * 方法名:內校报告
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateGsmInnerCheckReportFormNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_NJBG);
	}
	/**
	  * 方法名:新设备检测登记申请表
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateGsmNewEquipmentNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_GNE);
	}
	
	/**
	  * 方法名:管理评审表编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateManageReviewNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_MR);
	}
	
	/**
	  * 方法名:内审计划与实施编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateAuditPlanNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_AP);
	}
	/**
	  * 方法名:不符合与纠正措施报告编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateCorrectMeasuresNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_CM);
	}
	
	/**
	  * 方法名:年度审核计划编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateYearAuditNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_ND);
	}
	/**
	  * 方法名:校验不合格单编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateGsmNonconformityNo() {
   		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_REPA);
	}
	/**
	  * 方法名:检测设备报废编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateGsmScrapNo() {
  		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_SRC);
	}
	/**
	  * 方法名:外校委托编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateGsmEntrustNo() {
 		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_FET);
	}
	/**
	  * 方法名:仪器借调编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateGsmBorrowRecordNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_COS);
	}
	/**
	  * 方法名:HSF委托编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateEntrustHsfNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_HSF);
	}
	/**
	  * 方法名:可靠性委托编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateEntrustOrtNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_REL);
	}
	/**
	  * 方法名:异常处理编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateExceptionSingleNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_YCCL);
	}
	/**
	  * 方法名:样品管理编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateSampleNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_YPGL);
	}
	/**
	  * 方法名:客户问题点履历编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateCustomerAuditNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_KHWT);
	}
	/**
	  * 方法名:内审问题点报告编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateProblemsNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_NSWT);
	}
	/**
	  * 方法名:材料承认2.0编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateApprovalNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_PA);
	}
	/**
	  * 方法名:材料承认NEW编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateAdmitNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_PAAP);
	}
	/**
	  * 方法名:GP资料2.0编号
	  * <p>功能说明：</p>
	  * @return
	 */
	public String generateGpMaterialNo() {
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_CPCF);
	}
	
	public String getSupplierEvaluateCode() {
		// TODO Auto-generated method stub
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_EVALUATE);
	}
	public String getSupplierMaterialEvaluateCode() {
		// TODO Auto-generated method stub
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_MATERIAL_EVALUATE);
	}
	public String getSupplierChangeCode() {
		// TODO Auto-generated method stub
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_CHANGE);
	}
	public String getSupplierDataSupplyCode() {
		// TODO Auto-generated method stub
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_DATASUPPLY);
	}
	public String getSupplierEvaluateAdmitCode() {
		// TODO Auto-generated method stub
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_MATERIAL_ADMIT);
	}
	public String getSupplierCancleCode() {
		// TODO Auto-generated method stub
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_SUPPLIER_CANCLE);
	}
	public String getSupplierAuditImproveCode() {
		// TODO Auto-generated method stub
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_SUPPLIER_AUDIT_IMPROVE);
	}
	public String getSupplierImproveCode() {
		// TODO Auto-generated method stub
		return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_SUPPLIER_IMPROVE);
	}
	public String generateIncomingInspectionRecordCode(Session session,
			Long companyId) {
		  return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_JLJY,companyId);
	}
	public String getSupplierMaterialEvaluateCode(Session session,
			Long companyId) {
		  return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_MATERIAL_EVALUATE,companyId);
	}
	public String generateMFGode(Session session,
			Long companyId) {
		  return formCodeRuleManager.generatedCodeByCode(FormCodingRule.CODE_QC,companyId);
	}
}
