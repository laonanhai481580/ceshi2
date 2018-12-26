package com.ambition.iqc.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * 使用的检验级别
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "IQC_USE_INSPECTION_LEVEL")
public class UseInspectionLevel extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String inspectionLevel;
	public String getInspectionLevel() {
		return inspectionLevel;
	}
	public void setInspectionLevel(String inspectionLevel) {
		this.inspectionLevel = inspectionLevel;
	}
}
