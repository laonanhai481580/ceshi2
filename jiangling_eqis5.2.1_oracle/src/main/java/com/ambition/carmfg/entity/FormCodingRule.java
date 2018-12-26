package com.ambition.carmfg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 编码规则
 * @author ambition-zhaojun
 *
 */
@Entity
@Table(name = "MFG_CODING_RULE")
public class FormCodingRule  extends IdEntity {
	/**
	 * 量检具编号
	 */
	public static String CODE_LJJ = "ljj";
	/**
	 * 內校报告
	 */
	public static String CODE_NJBG = "NJBG";
	/**
	 * 现场检验报告
	 */
	public static String CODE_SI = "SI";
	/**
	  *进料检验单
	 */
	public static String CODE_JLJY="JLJY";
	public static String CODE_IQC="IQC";
	/**
	 * 制造检验
	 * */
	public static String CODE_QC="QC";
	/**
	 * ORT实验委托单
	 * */
	public static String CODE_ORT="ORT";
	
	/**
	 * FAR解析单
	 * */
	public static String CODE_FAR="FAR";
	
	/**
	 * 8D改进报告
	 * */
	public static String CODE_IR="IR";
	/**
	 * 品质异常联络单
	 * */
	public static String CODE_ER="ER";	
	/**
	 * 新设备检测登记申请表
	 * */
	public static String CODE_GNE="GNE";
	
	/**
	 * 管理评审表
	 * */
	public static String CODE_MR="MR";
	
	/**
	 * 不符合与纠正措施报告
	 * */
	public static String CODE_CM="CM";
	/**
	 * 年度审核计划
	 * */
	public static String CODE_ND="ND";
	/**
	 * 内审计划与实施
	 * */
	public static String CODE_AP="AP";
	/**
	 * IPQC稽核问题点改善报告
	 * */
	public static String CODE_IPQC="IPQC";
	/**
	 * DCR/N报告
	 * */
	public static String CODE_DCRN="DC";
	/**
	 * ECN报告
	 * */
	public static String CODE_ECN="ECN";
	/**
	 * ECR报告
	 * */
	public static String CODE_ECR="ECR";
	
	/**
	 * 供應商稽核问题点改善报告
	 * */
	public static String CODE_SUPPLIER_AUDIT_IMPROVE="AUDITIMPROVE";
	/**
	 * 校验不合格单编号
	 */
	public static String CODE_REPA = "REPA";
	/**
	 * 检测设备报废编号
	 */
	public static String CODE_SRC = "SRC";
	/**
	 * 外校委托编号
	 */
	public static String CODE_FET = "FET";
	/**
	 * 仪器借调编号
	 */
	public static String CODE_COS = "COS";
	/**
	 * HSF委托编号
	 */
	public static String CODE_HSF = "HSF";
	/**
	 * 可靠性委托编号
	 */
	public static String CODE_REL = "REL";
	/**
	 * 异常处理编号
	 */
	public static String CODE_YCCL = "YCCL";
	/**
	 * 样品管理编号
	 */
	public static String CODE_YPGL = "YPGL";
	/**
	 * 客户问题点履历编号
	 */
	public static String CODE_KHWT = "KHWT";
	/**
	 * 内审问题点报告编号
	 */
	public static String CODE_NSWT = "NSWT";
	/**
	 * 材料承认2.0编号
	 */
	public static String CODE_PA = "PA";
	/**
	 * 材料承认NEW编号
	 */
	public static String CODE_PAAP = "PAAP";
	/**
	 * GP资料2.0编号
	 */
	public static String CODE_CPCF = "CPCF";
	private static final long serialVersionUID = 1L;
	/**
	 * 供应商评价
	 */
	public static final String CODE_EVALUATE = "GYSPJ";
	public static final String CODE_CHANGE = "PCN";
	public static final String CODE_MATERIAL_EVALUATE = "GYSME";
	public static final String CODE_DATASUPPLY = "GP";
	public static final String CODE_MATERIAL_ADMIT = "CLCR";
	public static final String CODE_SUPPLIER_CANCLE = "CANCLE";
	public static final String CODE_SUPPLIER_IMPROVE = "IMPROVE";

	@Column(name="form_code")
    private String code; //表单编码
	@Column(name="form_name")
	private String name; //表单名称
	@Column(name="rule_name")
    private String rule;//规则
    private String entityName;//实体名称
    private String targetField;//对应的字段
    private String additionalCondition;//附加条件 
    
	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getAdditionalCondition() {
		return additionalCondition;
	}

	public void setAdditionalCondition(String additionalCondition) {
		this.additionalCondition = additionalCondition;
	}

	public String getTargetField() {
		return targetField;
	}

	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}

	public String toString() {
		return "制造质量管理：编码规则 	ID"+this.getId()+",表单名称"+this.name+",表单编码"+this.code;
	}
}
