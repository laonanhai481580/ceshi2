package com.ambition.qsm.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "QSM_CORRECT_MEASURES_ITEM")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CorrectMeasuresItem extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String systemName;//体系名称
	private String clauseName;//条款名称
	
	@ManyToOne
	@JoinColumn(name = "QSM_CORRECT_MEASURES_ID")
	@JsonIgnore()
	private CorrectMeasures correctMeasures;
	
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getClauseName() {
		return clauseName;
	}
	public void setClauseName(String clauseName) {
		this.clauseName = clauseName;
	}
	public CorrectMeasures getCorrectMeasures() {
		return correctMeasures;
	}
	public void setCorrectMeasures(CorrectMeasures correctMeasures) {
		this.correctMeasures = correctMeasures;
	}
	
	
	
}
