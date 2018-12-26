package com.ambition.iqc.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * 启用的抽样方案标准
 * @authorBy YUKE
 *
 */
@Entity
@Table(name="IQC_USE_BASE_TYPE")
public class UseBaseType extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String baseType = SampleCodeLetter.GB_TYPE;//抽样标准类型
	public String getBaseType() {
		return baseType;
	}
	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}
}
