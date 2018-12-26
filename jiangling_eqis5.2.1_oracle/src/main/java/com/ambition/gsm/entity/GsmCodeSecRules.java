package com.ambition.gsm.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.norteksoft.wf.engine.client.ExtendField;


/**
 * 计量编号规则二级分类(ENTITY)
 * @author 张顺治
 *
 */
@Entity
@Table(name="GSM_CODE_SECRULES")
public class GsmCodeSecRules extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String secondaryClassification;//类别
	private String secTypeCode;//类别代码   
	private String codeRuler;// 编号规则
	private String connector = "-";//连接符
	
	@ManyToOne
	@JoinColumn(name = "FK_GsmCodeRules_ID")
	@JsonIgnore
	private GsmCodeRules gsmCodeRules;//一级类别
	
	@Embedded
	private ExtendField extendField;

	public String getSecondaryClassification() {
		return secondaryClassification;
	}

	public void setSecondaryClassification(String secondaryClassification) {
		this.secondaryClassification = secondaryClassification;
	}

	public String getSecTypeCode() {
		return secTypeCode;
	}

	public void setSecTypeCode(String secTypeCode) {
		this.secTypeCode = secTypeCode;
	}

	public GsmCodeRules getGsmCodeRules() {
		return gsmCodeRules;
	}

	public void setGsmCodeRules(GsmCodeRules gsmCodeRules) {
		this.gsmCodeRules = gsmCodeRules;
	}

	public ExtendField getExtendField() {
		return extendField;
	}

	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}

	public String getCodeRuler() {
		return codeRuler;
	}

	public void setCodeRuler(String codeRuler) {
		this.codeRuler = codeRuler;
	}

	public String getConnector() {
		return connector;
	}

	public void setConnector(String connector) {
		this.connector = connector;
	} 
	
}
