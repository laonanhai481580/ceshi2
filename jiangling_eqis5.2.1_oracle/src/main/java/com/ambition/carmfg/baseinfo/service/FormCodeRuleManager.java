package com.ambition.carmfg.baseinfo.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.entity.FarAnalysis;
import com.ambition.carmfg.baseinfo.dao.FormCodeRuleDao;
import com.ambition.carmfg.entity.FormCodingRule;
import com.ambition.carmfg.entity.IpqcAuditImprove;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.OrtTestEntrust;
import com.ambition.ecm.entity.DcrnReport;
import com.ambition.ecm.entity.EcnReport;
import com.ambition.ecm.entity.EcrReport;
import com.ambition.epm.entity.EntrustHsf;
import com.ambition.epm.entity.EntrustOrt;
import com.ambition.epm.entity.ExceptionSingle;
import com.ambition.epm.entity.Sample;
import com.ambition.gp.entity.GpMaterial;
import com.ambition.gsm.entity.BorrowRecord;
import com.ambition.gsm.entity.Entrust;
import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.GsmInnerCheckReport;
import com.ambition.gsm.entity.NewEquipmentRegister;
import com.ambition.gsm.entity.NonconformityDispose;
import com.ambition.gsm.entity.ScrapList;
import com.ambition.improve.entity.ImproveReport;
import com.ambition.improve.entity.QualityExceptionReport;
import com.ambition.iqc.entity.ExperimentalDelegation;
import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.qsm.entity.AuditPlan;
import com.ambition.qsm.entity.CorrectMeasures;
import com.ambition.qsm.entity.CustomerAuditIssues;
import com.ambition.qsm.entity.ManageReview;
import com.ambition.qsm.entity.Problems;
import com.ambition.qsm.entity.YearAudit;
import com.ambition.si.entity.SiCheckInspectionReport;
import com.ambition.supplier.entity.SupplierAdmit;
import com.ambition.supplier.entity.SupplierApproval;
import com.ambition.supplier.entity.SupplierAuditImprove;
import com.ambition.supplier.entity.SupplierCancle;
import com.ambition.supplier.entity.SupplierChange;
import com.ambition.supplier.entity.SupplierDataSupply;
import com.ambition.supplier.entity.SupplierDevelop;
import com.ambition.supplier.entity.SupplierImprove;
import com.ambition.supplier.entity.SupplierMaterialAdmit;
import com.ambition.supplier.entity.SupplierMaterialEvaluate;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class FormCodeRuleManager {
	@Autowired
	private FormCodeRuleDao formCodingRuleDao;
	@Autowired
	private LogUtilDao logUtilDao;
	private List<FormCodingRule> formCodingRules;
	
	//设置静态的参数
	private static Map<String,String> staticPropertyMap = new HashMap<String, String>();
	/**
	 * 设置静态的参数
	 * @param key
	 * @param value
	 */
	public static void setStaticPropertyMap(String key,String value){
		synchronized (staticPropertyMap) {
			staticPropertyMap.put(key,value);
		}
	}
	
	public FormCodingRule get(Long id){
		return formCodingRuleDao.get(id);
	}
	
	/**
	 * 保存表单编码规则
	 * @param sampleTranstionArray
	 * @throws RuntimeException
	 */
	public void saveFormCodingRules(JSONArray formCodingRuleArray) throws Exception{
		//删除原来的设置
		String hql = "delete from FormCodingRule f where f.companyId = ?";
		formCodingRuleDao.batchExecute(hql,ContextUtils.getCompanyId());
		
		//保存新的编码规则
		List<FormCodingRule> list = new ArrayList<FormCodingRule>();
		for(int i=0;i<formCodingRuleArray.size();i++){
			JSONObject json = formCodingRuleArray.getJSONObject(i);
			FormCodingRule formCodingRule = null;
			formCodingRule = new FormCodingRule();
			formCodingRule.setCompanyId(ContextUtils.getCompanyId());
			formCodingRule.setCreatedTime(new Date());
			formCodingRule.setCreator(ContextUtils.getUserName());
			formCodingRule.setLastModifiedTime(new Date());
			formCodingRule.setLastModifier(ContextUtils.getUserName());
			for(Object key : json.keySet()){
				String value = json.getString(key.toString());
				setProperty(formCodingRule, key.toString(),value);
			}
			formCodingRuleDao.save(formCodingRule);
			logUtilDao.debugLog("保存", formCodingRule.toString());
			list.add(formCodingRule);
		}
		formCodingRules = list;//缓存到内存中
	}
	
	/**
	 * 根据编码从数据库获取编码规则
	 */
	private FormCodingRule getFormCodingRule(String code,Long companyId){
		String hql = "from FormCodingRule c where c.companyId=? and c.code = ?";
		@SuppressWarnings("unchecked")
		List<FormCodingRule> formCodingRules = formCodingRuleDao.createQuery(hql,companyId,code).list();
		if(formCodingRules.isEmpty()){
			return null;
		}else{
			return formCodingRules.get(0);
		}
	}
	
	public List<FormCodingRule> getAllFormCodingRules(){
		return getAllFormCodingRules(ContextUtils.getCompanyId());
	}
	
	public List<FormCodingRule> getAllFormCodingRules(Long companyId){
		if(formCodingRules==null||formCodingRules.isEmpty()){
			/**
			 * 进料检验单编号
			 * 
			 * */
			FormCodingRule formCodingRule = getFormCodingRule(FormCodingRule.CODE_JLJY,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_JLJY);
				formCodingRule.setName("进货检验报告");
				formCodingRule.setRule("JLJY-$year$month$date-XXXX");
				formCodingRule.setEntityName(IncomingInspectionActionsReport.class.getSimpleName());
				formCodingRule.setTargetField("inspectionNo");
				formCodingRuleDao.save(formCodingRule);
			}
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_IQC,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_IQC);
				formCodingRule.setName("实验委托报告");
				formCodingRule.setRule("IQC-$year$month$date-XXXX");
				formCodingRule.setEntityName(ExperimentalDelegation.class.getSimpleName());
				formCodingRule.setTargetField("experimentalNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 产生量检具编号
			 */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_LJJ,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_LJJ);
				formCodingRule.setName("量检具");
				formCodingRule.setRule("LJJ-$year-$month-$date-XXXX");
				formCodingRule.setEntityName(GsmEquipment.class.getSimpleName());
				formCodingRule.setTargetField("measurementNo");
				formCodingRuleDao.save(formCodingRule);
			}
			
			/**
			 * 产生新设备检测登记申请编号
			 */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_GNE,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_GNE);
				formCodingRule.setName("新进检测设备申请登记");
				formCodingRule.setRule("GNE-$year-$month-$date-XXXX");
				formCodingRule.setEntityName(NewEquipmentRegister.class.getSimpleName());
				formCodingRule.setTargetField("code");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 校验不合格单编号
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_REPA,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_REPA);
				formCodingRule.setName("校验不合格单编号");
				formCodingRule.setRule("REPA-$year$month$date-XXXX");
				formCodingRule.setEntityName(NonconformityDispose.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 检测设备报废编号
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_SRC,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_SRC);
				formCodingRule.setName("检测设备报废编号");
				formCodingRule.setRule("SRC-$year$month$date-XXXX");
				formCodingRule.setEntityName(ScrapList.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 外校委托编号
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_FET,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_FET);
				formCodingRule.setName("外校委托编号");
				formCodingRule.setRule("FET-$year$month$date-XXXX");
				formCodingRule.setEntityName(Entrust.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 仪器借调编号
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_COS,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_COS);
				formCodingRule.setName("仪器借调编号");
				formCodingRule.setRule("COS-$year$month$date-XXXX");
				formCodingRule.setEntityName(BorrowRecord.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * ORT实验委托单
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_ORT,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_ORT);
				formCodingRule.setName("ORT实验委托单");
				formCodingRule.setRule("ORT-$year$month$date-XXXX");
				formCodingRule.setEntityName(OrtTestEntrust.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			
			/**
			 * FAR解析单
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_FAR,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_FAR);
				formCodingRule.setName("FAR解析单");
				formCodingRule.setRule("FAR-$year$month$date-XXXX");
				formCodingRule.setEntityName(FarAnalysis.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 供应商进料异常纠正措施
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_SUPPLIER_IMPROVE,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_SUPPLIER_IMPROVE);
				formCodingRule.setName("供应商进料异常纠正措施报告");
				formCodingRule.setRule("$year$month$date-XXXX");
				formCodingRule.setEntityName(SupplierImprove.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 供应商稽核问题改善
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_SUPPLIER_AUDIT_IMPROVE,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_SUPPLIER_AUDIT_IMPROVE);
				formCodingRule.setName("供应商不符合项改善报告");
				formCodingRule.setRule("$year$month$date-XXXX");
				formCodingRule.setEntityName(SupplierAuditImprove.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 供应商评价单
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_EVALUATE,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_EVALUATE);
				formCodingRule.setName("供应商评价单");
				formCodingRule.setRule("GYSPJ-$year$month$date-XXXX");
				formCodingRule.setEntityName(SupplierDevelop.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 供应商变更PCN
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_CHANGE,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_CHANGE);
				formCodingRule.setName("供应商变更");
				formCodingRule.setRule("PCN-$year$month$date-XXX");
				formCodingRule.setEntityName(SupplierChange.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 材料承认2.0编号
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_PA,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_PA);
				formCodingRule.setName("材料承认管理编号");
				formCodingRule.setRule("PA-$year$month$date-XXXX");
				formCodingRule.setEntityName(SupplierApproval.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 材料承认NEW编号
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_PAAP,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_PAAP);
				formCodingRule.setName("材料承认管理编号");
				formCodingRule.setRule("PAAP-$year$month$date-XXXX");
				formCodingRule.setEntityName(SupplierAdmit.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * GP资料2.0编号
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_CPCF,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_CPCF);
				formCodingRule.setName("产品成分宣告编号");
				formCodingRule.setRule("CPCF-$year$month$date-XXXX");
				formCodingRule.setEntityName(GpMaterial.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 供应商样品评估
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_MATERIAL_EVALUATE,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_MATERIAL_EVALUATE);
				formCodingRule.setName("供应商样品评估");
				formCodingRule.setRule("GYSME-$year$month$date-XXX");
				formCodingRule.setEntityName(SupplierMaterialEvaluate.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * GP资料提供
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_DATASUPPLY,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_DATASUPPLY);
				formCodingRule.setName("GP资料提供");
				formCodingRule.setRule("GP-$year$month$date-XXX");
				formCodingRule.setEntityName(SupplierDataSupply.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 供应商材料承认
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_MATERIAL_ADMIT,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_MATERIAL_ADMIT);
				formCodingRule.setName("供应商材料承认");
				formCodingRule.setRule("CLCR-$year$month$date-XXX");
				formCodingRule.setEntityName(SupplierMaterialAdmit.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 合格供应商取消流程
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_SUPPLIER_CANCLE,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_SUPPLIER_CANCLE);
				formCodingRule.setName("合格供应商取消");
				formCodingRule.setRule("$year$month$date-XXX");
				formCodingRule.setEntityName(SupplierCancle.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 管理评审表
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_MR,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_MR);
				formCodingRule.setName("管理评审表");
				formCodingRule.setRule("MR-$year$month$date-XXXX");
				formCodingRule.setEntityName(ManageReview.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 年度审核计划
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_ND,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_ND);
				formCodingRule.setName("年度审核计划");
				formCodingRule.setRule("ND-$year$month$date-XXXX");
				formCodingRule.setEntityName(YearAudit.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 计量-內校报告
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_NJBG,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_NJBG);
				formCodingRule.setName("內校报告");
				formCodingRule.setRule("NJBG-$year$month$date-XXXX");
				formCodingRule.setEntityName(GsmInnerCheckReport.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 不符合与纠正措施报告
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_CM,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_CM);
				formCodingRule.setName("不符合与纠正措施报告");
				formCodingRule.setRule("CM-$year$month$date-XXXX");
				formCodingRule.setEntityName(CorrectMeasures.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 内审计划与实施
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_AP,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_AP);
				formCodingRule.setName("内审计划与实施");
				formCodingRule.setRule("AP-$year$month$date-XXXX");
				formCodingRule.setEntityName(AuditPlan.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 8D改进报告
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_IR,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_IR);
				formCodingRule.setName("8D改进报告");
				formCodingRule.setRule("IR-$year$month$date-XXXX");
				formCodingRule.setEntityName(ImproveReport.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			
			/**
			 * 品质异常联络单
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_ER,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_ER);
				formCodingRule.setName("品质异常联络单");
				formCodingRule.setRule("ER-$year$month$date-XXXX");
				formCodingRule.setEntityName(QualityExceptionReport.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}			
			/**
			 * IPQC稽核问题点改善报告
			 * 
			 * */
			 formCodingRule = getFormCodingRule(FormCodingRule.CODE_IPQC,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_IPQC);
				formCodingRule.setName("IPQC稽核问题点改善报告");
				formCodingRule.setRule("IPQC-$year$month$date-XXXX");
				formCodingRule.setEntityName(IpqcAuditImprove.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}			
			
			
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_QC,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_QC);
				formCodingRule.setName("制程检验报告");
				formCodingRule.setRule("QC-$year$month$date-XXXX");
				formCodingRule.setEntityName(MfgCheckInspectionReport.class.getSimpleName());
				formCodingRule.setTargetField("inspectionNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 工程变更DCR/N报告
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_DCRN,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_DCRN);
				formCodingRule.setName("DCR/N报告");
				formCodingRule.setRule("DC-$year$month$date-XXX");
				formCodingRule.setEntityName(DcrnReport.class.getSimpleName());
				formCodingRule.setTargetField("dcrnNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 工程变更ECN报告
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_ECN,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_ECN);
				formCodingRule.setName("ECN报告");
				formCodingRule.setRule("ECN-$year$month$date-XXX");
				formCodingRule.setEntityName(EcnReport.class.getSimpleName());
				formCodingRule.setTargetField("ecnNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 现场检验报告
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_SI,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_SI);
				formCodingRule.setName("现场检验报告");
				formCodingRule.setRule("SI-$year$month$date-XXX");
				formCodingRule.setEntityName(SiCheckInspectionReport.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 工程变更ECR报告
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_ECR,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_ECR);
				formCodingRule.setName("ECR报告");
				formCodingRule.setRule("ECR-$year$month$date-XXX");
				formCodingRule.setEntityName(EcrReport.class.getSimpleName());
				formCodingRule.setTargetField("ecrReportNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * HSF委托编号
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_HSF,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_HSF);
				formCodingRule.setName("HSF委托编号");
				formCodingRule.setRule("HSF-$year$month$date-XXXX");
				formCodingRule.setEntityName(EntrustHsf.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * REL委托编号
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_REL,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_REL);
				formCodingRule.setName("可靠性委托编号");
				formCodingRule.setRule("REL-$year$month-XXXX");
				formCodingRule.setEntityName(EntrustOrt.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 异常处理编号
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_YCCL,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_YCCL);
				formCodingRule.setName("异常处理编号");
				formCodingRule.setRule("YCCL-$year$month$date-XXXX");
				formCodingRule.setEntityName(ExceptionSingle.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 样品管理编号
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_YPGL,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_YPGL);
				formCodingRule.setName("样品管理编号");
				formCodingRule.setRule("YPGL-$year$month$date-XXXX");
				formCodingRule.setEntityName(Sample.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 客户问题点履历编号
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_KHWT,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_KHWT);
				formCodingRule.setName("客户问题点履历编号");
				formCodingRule.setRule("KHWT-$year$month$date-XXXX");
				formCodingRule.setEntityName(CustomerAuditIssues.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
			/**
			 * 内审问题点报告编号
			 * */
			formCodingRule = getFormCodingRule(FormCodingRule.CODE_NSWT,companyId);
			if(formCodingRule == null){
				formCodingRule = new FormCodingRule();
				formCodingRule.setCompanyId(companyId);
				formCodingRule.setCreatedTime(new Date());
				formCodingRule.setCreator(ContextUtils.getUserName());
				formCodingRule.setLastModifiedTime(formCodingRule.getCreatedTime());
				formCodingRule.setLastModifier(formCodingRule.getCreator());
				formCodingRule.setCode(FormCodingRule.CODE_NSWT);
				formCodingRule.setName("内审问题点报告编号");
				formCodingRule.setRule("NSWT-$year$month$date-XXXX");
				formCodingRule.setEntityName(Problems.class.getSimpleName());
				formCodingRule.setTargetField("formNo");
				formCodingRuleDao.save(formCodingRule);
			}
            formCodingRules = formCodingRuleDao.getAllCodingRules(companyId);
		}
		return formCodingRules;
	}
	
	/**
	 * 根据编码获取编码规则
	 * @param code
	 * @return
	 */
	public FormCodingRule getFormCodingRuleByCode(String code,Long companyId){
		List<FormCodingRule> formCodingRules = getAllFormCodingRules(companyId);
		for(FormCodingRule formCodingRule : formCodingRules){
			if(formCodingRule.getCode().equals(code)){
				return formCodingRule;
			}
		}
		return null;
	}
	
	/**
	 * 根据编码获取编码规则
	 * @param code
	 * @return
	 */
	public FormCodingRule getFormCodingRuleByCode(String code){
		return getFormCodingRuleByCode(code,ContextUtils.getCompanyId());
	}
	
	/**
	 * 获取系统预定义的规则
	 * @return
	 */
	private Map<String,String> getSystemPropertyForFormCodingRule(){
		Map<String,String> systemRule = new HashMap<String, String>();
		synchronized (staticPropertyMap) {
			for(String key : staticPropertyMap.keySet()){
				systemRule.put(key,staticPropertyMap.get(key));
			}
		}
		Calendar calendar = Calendar.getInstance();
		Integer year = calendar.get(Calendar.YEAR),month = calendar.get(Calendar.MONTH)+1,date=calendar.get(Calendar.DATE);
		systemRule.put("\\$year",year.toString());//年
		//月
		if(month<10){
			systemRule.put("\\$month","0" + month);
		}else{
			systemRule.put("\\$month",month.toString());
		}
		//日
		if(date<10){
			systemRule.put("\\$date","0" + date);
		}else{
			systemRule.put("\\$date",date.toString());
		}
		//部门
//		ContextUtils.get
		return systemRule;
	}
	
	/**
	 * 根据编码规则生成最新的编码
	 * @param ruleCode
	 * @return
	 */
	public String generatedCodeByCode(String ruleCode){
		return generatedCodeByCode(ruleCode,ContextUtils.getCompanyId());
	}
	/**
	 * 根据编码规则(从多表中判断)生成最新的编码
	 * @param ruleCode
	 * @return
	 */
	public String generatedCodeByCode2(String ruleCode){
		return generatedCodeByCode2(ruleCode,ContextUtils.getCompanyId());
	}
	/**
	 * 根据编码规则生成最新的编码
	 * @param ruleCode
	 * @return
	 */
	public String generatedCodeByCode(String ruleCode,Long companyId){
		FormCodingRule formCodingRule = getFormCodingRuleByCode(ruleCode,companyId);
		if(formCodingRule==null){
			return null;
		}else{
			Map<String,String> systemRule = getSystemPropertyForFormCodingRule();
			String rule = formCodingRule.getRule();
			for(String key : systemRule.keySet()){
				rule = rule.replaceAll(key, systemRule.get(key));
			}
			Pattern p=Pattern.compile("X{2,}");
			Matcher m=p.matcher(rule);
			if(!m.find()){
				throw new RuntimeException(formCodingRule.getName() + "的编码规则中流水号不能为空!");
			}else{
				String group = m.group();
//				tempRule = tempRule.re
				int start = m.start(),end = m.end();
				String startPrefix="",endPrefix="";
				if(start>0){
					startPrefix = rule.substring(0,start).replace("\\X","X");
				}
				if(end<rule.length()){
					endPrefix = rule.substring(end).replace("\\X","X");
				}
				rule = startPrefix+"%"+endPrefix;
				String hql = "select "+formCodingRule.getTargetField()+" from " + formCodingRule.getEntityName() + " where companyId = ? and " + formCodingRule.getTargetField() + " like '" + rule + "' " + (StringUtils.isEmpty(formCodingRule.getAdditionalCondition())?"":formCodingRule.getAdditionalCondition())
							+ " and length(" + formCodingRule.getTargetField() + ")=" + end + " order by " + formCodingRule.getTargetField() + " desc";
				Query query = formCodingRuleDao.createQuery(hql,companyId);
				query.setMaxResults(1);
				List<?> list = query.list();
				Integer max = 0;
				if(!list.isEmpty()){
					String code = list.get(0).toString();
					String val = code.substring(startPrefix.length(),code.length() - endPrefix.length());
					if(StringUtils.isNumeric(val)){
						max = Integer.valueOf(val);
					}
				}
				max++;
				for(int i=0;i<group.length()-max.toString().length();i++){
					startPrefix += "0";
				}
				return startPrefix + max + endPrefix;
			}
		}
	}
	/**
	 * 根据编码规则（从多表中判断）生成最新的编码
	 * @param ruleCode
	 * @return
	 */
	public String generatedCodeByCode2(String ruleCode,Long companyId){
		FormCodingRule formCodingRule = getFormCodingRuleByCode(ruleCode,companyId);
		if(formCodingRule==null){
			return null;
		}else{
			Map<String,String> systemRule = getSystemPropertyForFormCodingRule();
			String rule = formCodingRule.getRule();
			for(String key : systemRule.keySet()){
				rule = rule.replaceAll(key, systemRule.get(key));
			}
			Pattern p=Pattern.compile("X{2,}");
			Matcher m=p.matcher(rule);
			if(!m.find()){
				throw new RuntimeException(formCodingRule.getName() + "的编码规则中流水号不能为空!");
			}else{
				String group = m.group();
				int start = m.start(),end = m.end();
				String startPrefix="",endPrefix="";
				if(start>0){
					startPrefix = rule.substring(0,start).replace("\\X","X");
				}
				if(end<rule.length()){
					endPrefix = rule.substring(end).replace("\\X","X");
				}
				rule = startPrefix+"%"+endPrefix;
				Integer max = 0;
				if(formCodingRule.getEntityName().indexOf(",")>0){
					String[] s=formCodingRule.getEntityName().split(",");
					for (int i = 0; i < s.length; i++) {
						String hql = "select "+formCodingRule.getTargetField()+" from " + s[i].toString() + " where companyId = ? and " + formCodingRule.getTargetField() + " like '" + rule + "' " + (StringUtils.isEmpty(formCodingRule.getAdditionalCondition())?"":formCodingRule.getAdditionalCondition())
								+ " and length(" + formCodingRule.getTargetField() + ")=" + end + " order by " + formCodingRule.getTargetField() + " desc";
						Query query = formCodingRuleDao.createQuery(hql,companyId);
						query.setMaxResults(1);
						List<?> list = query.list();
						if(!list.isEmpty()){
							String code = list.get(0).toString();
							String val = code.substring(startPrefix.length(),code.length() - endPrefix.length());
							if(StringUtils.isNumeric(val)){
								Integer temp = max;
								max = Integer.valueOf(val);
								if(temp>=max){
									max=temp;
								}
							}
						}
					}
				}
				max++;
				for(int i=0;i<group.length()-max.toString().length();i++){
					startPrefix += "0";
				}
				return startPrefix + max + endPrefix;
			}
		}
	}
	/**
	 * 根据编码规则和临时的规则生成
	 * @param tempRule
	 * @return
	 */
	public String generatedTempCodeByRule(String tempRule){
		Map<String,String> systemRule = getSystemPropertyForFormCodingRule();
		for(String key : systemRule.keySet()){
			tempRule = tempRule.replaceAll(key, systemRule.get(key));
		}
		Pattern p=Pattern.compile("X{2,}");
		Matcher m=p.matcher(tempRule);
		if(!m.find()){
			throw new RuntimeException("编码规则中流水号不能小于二两位!");
		}else{
			String group = m.group();
//			tempRule = tempRule.re
			int start = m.start(),end = m.end();
			String startPrefix = "",endPrefix="";
			if(start>0){
				startPrefix = tempRule.substring(0,start).replace("\\X","X");
			}
			if(end<tempRule.length()){
				endPrefix = tempRule.substring(end).replace("\\X","X");
			}
			Integer max = 1;
			for(int i=0;i<group.length()-max.toString().length();i++){
				startPrefix += "0";
			}
			return startPrefix + max + endPrefix;
		}
	}
	
	private void setProperty(Object obj, String property, Object value) throws Exception {
		Class<?> type = PropertyUtils.getPropertyType(obj, property);
		if (type != null) {
			if (value == null || StringUtils.isEmpty(value.toString())) {
				PropertyUtils.setProperty(obj, property, null);
			} else {
				if (String.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property, value.toString());
				} else if (Integer.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Integer.valueOf(value.toString()));
				} else if (Double.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Double.valueOf(value.toString()));
				} else if (Float.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Float.valueOf(value.toString()));
				} else if (Boolean.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Boolean.valueOf(value.toString()));
				} else {
					PropertyUtils.setProperty(obj, property, value);
				}
			}
		}
	}

}
