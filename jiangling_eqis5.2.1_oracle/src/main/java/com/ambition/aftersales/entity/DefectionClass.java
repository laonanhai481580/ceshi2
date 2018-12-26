package com.ambition.aftersales.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:不良分类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月21日 发布
 */
@Entity
@Table(name = "AFS_DEFECTION_CLASS")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class DefectionClass extends IdEntity{
	private static final long serialVersionUID = 1L;
	//private String businessUnit;//事业部
	private String defectionClass;//不良类型
	@OneToMany(mappedBy="defectionClass")
	@OrderBy("defectionItemNo")
	@JsonIgnore
	List<DefectionItem> defectionItems;//不良项目


	public String getDefectionClass() {
		return defectionClass;
	}
	public void setDefectionClass(String defectionClass) {
		this.defectionClass = defectionClass;
	}
	public List<DefectionItem> getDefectionItems() {
		return defectionItems;
	}
	public void setDefectionItems(List<DefectionItem> defectionItems) {
		this.defectionItems = defectionItems;
	}
		
}
