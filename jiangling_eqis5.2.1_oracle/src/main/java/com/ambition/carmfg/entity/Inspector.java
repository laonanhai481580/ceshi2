package com.ambition.carmfg.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.norteksoft.wf.engine.client.ExtendField;

@Entity
@Table(name = "MFG_INSPECTOR")
public class Inspector extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String name; // 检验员
	private Long userId;
	@ManyToOne
	@JoinColumn(name = "FK_INSPECTION_POINT_ID")
	private InspectionPoint inspectionPoint;
	@Embedded
	private ExtendField extendField;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public InspectionPoint getInspectionPoint() {
		return inspectionPoint;
	}

	public void setInspectionPoint(InspectionPoint inspectionPoint) {
		this.inspectionPoint = inspectionPoint;
	}

	public ExtendField getExtendField() {
		return extendField;
	}

	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}

}
