package com.ambition.aftersales.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:不良项目
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月21日 发布
 */
@Entity
@Table(name = "AFS_DEFECTION_ITEM")//AFS_DEFECTION_ITEM
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class DefectionItem extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String defectionItemNo;//不良项目编码
	private String defectionItemName;//不良项目名称	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "FK_DEFECTION_TYPE_NO")
	private DefectionClass defectionClass;//不良类型	

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
	public DefectionClass getDefectionClass() {
		return defectionClass;
	}
	public void setDefectionClass(DefectionClass defectionClass) {
		this.defectionClass = defectionClass;
	}
	
}
