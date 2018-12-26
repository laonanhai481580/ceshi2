package com.ambition.improve.entity;

//import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:不良现象
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月8日 发布
 */
@Entity
@Table(name = "IMP_DEFECTION_PHENOMENON")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class DefectionPhenomenon extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String defectionPhenomenonName;//不良现象名称
	private String remark;//描述
	@ManyToOne
	@JoinColumn(name = "FK_PROBLEM_DESCRIBLE_NO")
	@JsonIgnore
	private ProblemDescrible problemDescrible;//问题类型
	public String getDefectionPhenomenonName() {
		return defectionPhenomenonName;
	}
	public void setDefectionPhenomenonName(String defectionPhenomenonName) {
		this.defectionPhenomenonName = defectionPhenomenonName;
	}
	public ProblemDescrible getProblemDescrible() {
		return problemDescrible;
	}
	public void setProblemDescrible(ProblemDescrible problemDescrible) {
		this.problemDescrible = problemDescrible;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}
