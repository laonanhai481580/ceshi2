package com.ambition.gsm.entity;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.norteksoft.wf.engine.client.ExtendField;

/**
 * 计量编号规则一级类别(ENTITY)
 * @author 张顺治
 *
 */
@Entity
@Table(name="GSM_CODE_RULES")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})//忽略Json不认识的属性，避免转换Json出错
public class GsmCodeRules extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String measurementType;// 一级类别
	private String typeCode;// 类别代码
	private String codeRuler;// 编号规则
	private String connector = "-";//连接符
	
	@OneToMany(mappedBy = "gsmCodeRules")
	List<GsmCodeSecRules> gsmCodeSecRuless;// ERJI代码
	
	@Embedded
	private ExtendField extendField;

	public String getMeasurementType() {
		return measurementType;
	}

	public void setMeasurementType(String measurementType) {
		this.measurementType = measurementType;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getCodeRuler() {
		return codeRuler;
	}

	public void setCodeRuler(String codeRuler) {
		this.codeRuler = codeRuler;
	}

	public List<GsmCodeSecRules> getGsmCodeSecRuless() {
		return gsmCodeSecRuless;
	}

	public void setGsmCodeSecRuless(List<GsmCodeSecRules> gsmCodeSecRuless) {
		this.gsmCodeSecRuless = gsmCodeSecRuless;
	}

	public ExtendField getExtendField() {
		return extendField;
	}

	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}

	public String getConnector() {
		return connector;
	}

	public void setConnector(String connector) {
		this.connector = connector;
	} 
	
}
