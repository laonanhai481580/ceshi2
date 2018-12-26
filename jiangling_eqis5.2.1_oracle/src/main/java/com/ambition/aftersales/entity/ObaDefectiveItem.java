package com.ambition.aftersales.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 类名:OBA不良明细
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Entity
@Table(name = "AFS_OBA_DEFECTIVE_ITEMS")
public class ObaDefectiveItem extends IdEntity {

	private static final long serialVersionUID = 1L;
	private String defectionClass;//不良类型
	private String defectionItemNo;//不良项目编码
	private String defectionItemName;//不良项目名称
	private Integer defectionItemValue;//不良项目值
	@ManyToOne
	@JoinColumn(name = "FK_OBA_DATA_ID")
	@JsonIgnore()
	private ObaData obaData;
	public String getDefectionClass() {
		return defectionClass;
	}
	public void setDefectionClass(String defectionClass) {
		this.defectionClass = defectionClass;
	}
	public String getDefectionItemNo() {
		return defectionItemNo;
	}
	public void setDefectionItemNo(String defectionItemNo) {
		this.defectionItemNo = defectionItemNo;
	}
	public String getDefectionItemName() {
		return defectionItemName;
	}
	public void setDefectionItemName(String defectionItemName) {
		this.defectionItemName = defectionItemName;
	}
	public ObaData getObaData() {
		return obaData;
	}
	public void setObaData(ObaData obaData) {
		this.obaData = obaData;
	}
	public Integer getDefectionItemValue() {
		return defectionItemValue;
	}
	public void setDefectionItemValue(Integer defectionItemValue) {
		this.defectionItemValue = defectionItemValue;
	}
	
	

	
	
	
	
}
