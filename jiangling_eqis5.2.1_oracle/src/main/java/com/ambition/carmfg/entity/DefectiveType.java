package com.ambition.carmfg.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.norteksoft.wf.engine.client.ExtendField;

@Entity
@Table(name = "MFG_DEFECTIVE_TYPE")
public class DefectiveType extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String defectiveTypeCode;
	private String defectiveTypename;

	@Embedded
	private ExtendField extendField;

	public String getDefectiveTypeCode() {
		return defectiveTypeCode;
	}


	public void setDefectiveTypeCode(String defectiveTypeCode) {
		this.defectiveTypeCode = defectiveTypeCode;
	}


	public String getDefectiveTypename() {
		return defectiveTypename;
	}


	public void setDefectiveTypename(String defectiveTypename) {
		this.defectiveTypename = defectiveTypename;
	}


	public ExtendField getExtendField() {
		return extendField;
	}


	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}


}
