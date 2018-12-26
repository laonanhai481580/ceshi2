package com.ambition.carmfg.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.norteksoft.wf.engine.client.ExtendField;

@Entity
@Table(name = "MFG_DEFECTIVE")
public class Defective extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;
	@Embedded
	private ExtendField extendField;

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


	public ExtendField getExtendField() {
		return extendField;
	}


	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}

}
