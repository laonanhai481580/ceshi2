package com.ambition.spc.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * SpcSgTag.java
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "SPC_SG_TAG")
public class SpcSgTag extends IdEntity {

	/**
	 * 子组附属信息表
	 */
	private static final long serialVersionUID = 1L;
	private String tagName;//层别名称	
	private String tagCode;//层别代码
	private String tagValue;//取值
	private String method;//取值方式
	@ManyToOne
	@JoinColumn(name="FK_SUB_GROUP_ID")
	private SpcSubGroup spcSubGroup;//子组表
	
	public String getTagName() {
		return tagName;
	}
	
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	public String getTagCode() {
		return tagCode;
	}

	public void setTagCode(String tagCode) {
		this.tagCode = tagCode;
	}

	public String getTagValue() {
		return tagValue;
	}
	
	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}
	
	public SpcSubGroup getSpcSubGroup() {
		return spcSubGroup;
	}

	public void setSpcSubGroup(SpcSubGroup spcSubGroup) {
		this.spcSubGroup = spcSubGroup;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
