package com.ambition.si.entity;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.norteksoft.wf.engine.client.ExtendField;
/**
 * 
 * 类名:不良类别
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2017年2月14日 发布
 */
@Entity
@Table(name = "SI_DEFECTION_TYPE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class SiDefectionType extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String processSection; // 制程区段
	private String defectionTypeNo;//不良类型编码
	private String defectionTypeName;//不良类型名称
	private Boolean isFlag;//标志位
	@OneToMany(mappedBy="defectionType")
	@OrderBy("defectionCodeNo")
	List<SiDefectionCode> defectionCodes;//不良代码
	@Embedded
	private ExtendField extendField;
	//Get、Set方法
	public String getDefectionTypeNo() {
		return defectionTypeNo;
	}
	public void setDefectionTypeNo(String defectionTypeNo) {
		this.defectionTypeNo = defectionTypeNo;
	}
	public String getDefectionTypeName() {
		return defectionTypeName;
	}
	public void setDefectionTypeName(String defectionTypeName) {
		this.defectionTypeName = defectionTypeName;
	}
	public Boolean getIsFlag() {
		return isFlag;
	}
	public void setIsFlag(Boolean isFlag) {
		this.isFlag = isFlag;
	}

	public List<SiDefectionCode> getDefectionCodes() {
		return defectionCodes;
	}
	public void setDefectionCodes(List<SiDefectionCode> defectionCodes) {
		this.defectionCodes = defectionCodes;
	}
	public ExtendField getExtendField() {
		return extendField;
	}
	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}
	public String toString() {
		return "制造质量管理：不良明细维护  ID"+this.getId()+",不良类别名称"+this.defectionTypeName;
	}
	public String getProcessSection() {
		return processSection;
	}
	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}	
	
}
