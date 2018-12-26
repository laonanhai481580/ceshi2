package com.ambition.improve.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:问题描述维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年10月11日 发布
 */
@Entity
@Table(name = "IMP_PROBLEM_DESCRIBLE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class ProblemDescrible extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String defectionType;//不良类型
	private String model;//机种
	@OneToMany(mappedBy="problemDescrible")
	@OrderBy("defectionPhenomenonName")
	List<DefectionPhenomenon> defectionPhenomenons;//不良现象
	public String getDefectionType() {
		return defectionType;
	}
	public void setDefectionType(String defectionType) {
		this.defectionType = defectionType;
	}
	public List<DefectionPhenomenon> getDefectionPhenomenons() {
		return defectionPhenomenons;
	}
	public void setDefectionPhenomenons(
			List<DefectionPhenomenon> defectionPhenomenons) {
		this.defectionPhenomenons = defectionPhenomenons;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	
	
}
